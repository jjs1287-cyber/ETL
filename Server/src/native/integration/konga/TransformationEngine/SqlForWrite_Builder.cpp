/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#ifdef WIN32
	#include <windows.h>
#endif
#include <sqlext.h>

#include <konga/Utils/StringTokenizer.h>
#include <konga/Utils/CrException.h>
#include <konga/Utils/util.h>

#include <konga/CharacterEncodingConversion/Utf8Conv.h>

#include <konga/Db/DbUtils/DbUtils.h>

#include "crom.h"
#include "misc.h"
#include "SqlForWrite_Builder.h"
#include "TreeMapper.h"
#include "DbWriter.h"
#include "DbAccess.h"
#include "TxtParser.h"

using namespace konga;
using std::string;

namespace {
	const char empty[] = "";
}

SqlForWrite_Builder::SqlForWrite_Builder(TreeMapper*p,
										 Utf8Conv* conv,
										 bool isOracle,
										 bool isTarget) :
				pTreeMapper(p),
				m_conv(conv),
				bDriverIsOracle(isOracle),
				bIsTarget(isTarget)
{
	                                                                                  

	if (bIsTarget) {
		sBeginQuote = p->getSqlBeginTargetName();
		sEndQuote = p->getSqlEndTargetName();
		mode = p->getDbInsertUpdateMode();
		update_fields = empty;
	}
	else {
		sBeginQuote=p->getSqlBeginSourceName();
		sEndQuote=p->getSqlEndSourceName();
		mode = MODE_UPDATE;
		update_fields=p->getUpdateFieldString();
	}
}

                
std::string SqlForWrite_Builder::getSqlValue(const char*value, const CROM* const column, konga::Utf8Conv* conv, bool &bParameterized, std::string& errmsg)
{

	int sqlDataType = column->get_SqlDataType();
	int iCharSize = column->get_DbColSize();
	errmsg.clear();
	bParameterized=false;

	if(value==0)return "NULL";

	if (column->IsUDF())
	{
		string udfvalue(value);	
		if (BeginsWith(udfvalue,USER_DEFINED_FUNCTION)) {
			udfvalue=ltrim(udfvalue.substr(strlen(USER_DEFINED_FUNCTION)));
		}
		return udfvalue;	
	}

	if(konga::IsDataTypeNumeric(sqlDataType)){
		if(!konga::isNumeric(value)){
			if(*value!=0) {
				errmsg="Invalid value for numeric field. value:\""+std::string(value)+"\"";
				return "";
			}
			else {
				                               
				return "NULL";
			}
		}
		return string(value);
	}

	else if(konga::IsDataTypeDateTime(sqlDataType)) {
		if(*value==0) {
			                               
			return "NULL";
		}
	}
	else if(konga::IsBinaryData(sqlDataType)){
		                                                   
		bParameterized = true;
		return string(value);
	}

	const bool bIsWideCharData = konga::IsWideCharData(sqlDataType);

	string sqlValue(value);
	if(!bIsWideCharData && conv!=0) {
		sqlValue = conv->to(sqlValue);
	}
	int size = sqlValue.size();
	if(size>iCharSize && iCharSize>0) {
		size = iCharSize;
		sqlValue.erase(size);
	}

	bParameterized = size > 30 || bIsWideCharData;

	if(konga::requireSqlEscape(sqlValue)){
		sqlValue=konga::SqlEscape(sqlValue);
	}
	sqlValue.insert(0, 1, '\'');
	sqlValue.append(1, '\'');
	return sqlValue;
}


void SqlForWrite_Builder::buildSqlForTarget(CROM* pTableNode, CROM* pIdentityField, char override_mode)
{
	sqlTableName=pTableNode->get_sqlTableName();

	const bool bIncludeNullInSqlStatement = pTreeMapper->IncludeNullInTargetSQLStatement();

	KString colNames, colValues, setClause, whereClause;
	KString csTmp;
	std::string sqlValue, sqlValue_saved;
	bool bInsert_only = false;
	bool doInsert = (mode==MODE_INSERT || (mode==MODE_INSERT_UPDATE && override_mode!='U'));
	bool doUpdate = (mode==(MODE_INSERT_UPDATE && override_mode!='I') || mode==MODE_UPDATE);
	bool doDelete = mode==MODE_DELETE;
	insert_croms.clear();
	update_croms.clear();
	where_croms.clear();
	std::string errmsg;

	for (CROM* pCROM = pTableNode->GetFirstChild(); pCROM && !pCROM->IsElement(); pCROM=pCROM->GetNextSibling()) {
		if(!pCROM->IsAttribute()||pCROM->IsPrecondition())continue;
		KString ColName(pCROM->GetDisplayName());
		const bool bUpdateKey = pCROM->IsUpdateKey();
		const int SqlDataType=pCROM->get_SqlDataType();
		const char* sValueType=pCROM->GetValueType();
		konga::DataElement::Type dataType=konga::DataElement::get_type(sValueType, SqlDataType);
		KString value = pCROM->GetValue();
		bool isNull=pCROM->IsNull();
		const char*sourceExpr=pCROM->getSourceExpr();
		bool isMapped = (sourceExpr && sourceExpr[0] && !pCROM->isUnmapped());
		bool bParameterized=true;
		if(bIsTarget){
			if(pCROM->IsForeignKey()){
				const char* csValue=pCROM->GetForeignKeyValue();
				if(csValue==0){
					KString err, err1;
					err.Format("Can't find foreign key value for FK: %s.%s%s%s",
						pTableNode->get_sqlTableName(), sBeginQuote.c_str(), (const char*)pCROM->GetDisplayName(), sEndQuote.c_str());
					CROM*pParent=pTableNode->GetParent();
					if(pParent==0){
						err1.Format(", and the parent table does not exist.");
					}
					else {
						err1.Format(" link to the parent PK: %s.%s%s%s.",
						pParent->get_sqlTableName(), sBeginQuote.c_str(), pCROM->get_LinkToPrimaryKey(), sEndQuote.c_str());
					}
					throw konga::CrException((const char*)(err+err1));
				}
				value=csValue;
			}
			else if(pCROM==pIdentityField){
				continue;
			}
			else if(isMapped){
				if ( isNull && !bIncludeNullInSqlStatement ) continue;
			}
			else {
				continue;
			}
		}

		if(bDriverIsOracle) {
			if(dataType==konga::DataElement::t_date || dataType==konga::DataElement::t_timespan){
				const bool isDate = value.GetLength()>7 && value.GetAt(4)=='-' && value.GetAt(7)=='-';
				const bool isTime = value.GetLength()>5 && value.GetAt(2)==':' && value.GetAt(5)==':';
				if(isDate || isTime){
					const bool isDateTime=isDate && value.GetLength()>=19 && value.GetAt(13)==':' && value.GetAt(16)==':';
					if((SqlDataType==SQL_TYPE_DATE || stricmp(sValueType,"date")==0) && (isDate || isDateTime)){
						int y,m,d;
						if(sscanf((const char*)value,"%04d-%02d-%02d",&y,&m,&d)==3){
							value.Format("%02d-%s-%02d",d,konga::DataElement::monthsAbr[m-1],y%100);
						}
					}
					else if((SqlDataType==SQL_TYPE_TIMESTAMP || strnicmp("timestamp",sValueType, 9)==0) && isDateTime){
						int y,m,d,h,M,s;
						if(sscanf((const char*)value,"%04d-%02d-%02d %02d:%02d:%02d",&y,&m,&d,&h,&M,&s)==6){
							value.Format("%02d-%s-%02d %02d:%02d:%02d",d,konga::DataElement::monthsAbr[m-1],y%100,h,M,s);
						}
					}
				}
			}
		}

		if(isNull){
			value.Empty();
			sqlValue.assign("NULL");
			bParameterized=false;
		}
		else {
			                                                                                                   
			sqlValue=getSqlValue(value, pCROM, m_conv, bParameterized, errmsg);
			if(!errmsg.empty()){
				throw konga::CrException("Error occurred on writing to target database, "+errmsg+" for column: \""+(const char*)pCROM->get_Path()+"\"\n");
			}
			sqlValue_saved=sqlValue;                                                                                                
			if(bParameterized){
				sqlValue = "?";
			}
		}

		ColName = sBeginQuote + ColName + sEndQuote;
		if(doInsert){
			if (!colNames.IsEmpty()){
				colNames += ",";
				colValues += ",";
			}
			colNames += ColName;

			colValues += sqlValue.c_str();
			if(bParameterized)insert_croms.push_back(pCROM);
		}
		if(doUpdate || doDelete){
			if(bInsert_only)continue;

			if(bUpdateKey){
				if(!whereClause.IsEmpty())whereClause += " AND ";
				if(isNull){
					csTmp.Format("(%s IS NULL)", (const char*)ColName);
				}
				else {
					csTmp.Format("(%s = %s)", (const char*)ColName, sqlValue_saved.c_str());
				}
				whereClause += csTmp;
			}
			else {
				if(!setClause.IsEmpty())setClause += ", ";
				csTmp.Format("%s = %s", (const char*)ColName, sqlValue.c_str());
				setClause += csTmp;
				if(bParameterized)update_croms.push_back(pCROM);
			}
		}
	}


	if(doInsert){
	                             
		if (colNames.IsEmpty() && colValues.IsEmpty())
			sqlInsert.Empty();
		else
			sqlInsert.Format("INSERT INTO %s (%s) VALUES (%s)", (const char*)sqlTableName, (const char*)colNames, (const char*)colValues);
	}

	if(doUpdate){
	                             
		if ( !whereClause.IsEmpty() && !setClause.IsEmpty() ) {
			sqlUpdate.Format("UPDATE %s SET %s Where %s", 
				(const char*)sqlTableName, (const char*)setClause, (const char*)whereClause);
			whereSql.Format("WHERE %s ", (const char*)whereClause);
			for(std::vector<CROM*>::iterator it=where_croms.begin(); it!=where_croms.end(); it++)
				update_croms.push_back(*it);
		}
		else {
			                                                                   
			                                                               
			sqlUpdate.Empty();
			update_croms.clear();
			if ( !whereClause.IsEmpty()) {
				whereSql.Format("WHERE %s ", (const char*)whereClause);
			}
		}
	}

	if(doDelete){
	                             
		if (whereClause.IsEmpty())
			sqlDelete.Empty();
		else 
			sqlDelete.Format("DELETE FROM %s WHERE %s", (const char*)sqlTableName, (const char*)whereClause);
	}

	if(mode==MODE_INSERT_UPDATE && !whereClause.IsEmpty()){
		sqlGetCount.Format("SELECT COUNT(*) FROM %s WHERE %s", (const char*)sqlTableName, (const char*)whereClause);
	}
	else {
		sqlGetCount.Empty();
	}
}

void SqlForWrite_Builder::parseSourceUpdateFields(const CROM* pTableNode, const char*update_fields)
{
	if(update_fields==0 || update_fields[0]==0)
		return;

	konga::StringTokenizer tk(update_fields, "^=");
	std::string name;
	std::string owner, table_name, field_name;
	bool bValue=false;

	while(tk.NextToken())
	{
		const char* token = tk.token();
		switch(token[0])
		{
		case '^':
			bValue=false;
			break;
		case '=':
			bValue=true;
			break;
		default:
			switch(bValue){
				case false: name=token; break;
				case true: 
					{
						parseOwnerTableFieldString(name.c_str(), owner, table_name, field_name, sBeginQuote, sEndQuote);
						CROM* pColumn = pTableNode->FindChildLeaf(field_name.c_str());
						if(pColumn)pColumn->SetValue(token);
					}
					break;
			}
			break;
		}
	}
}

                                                                        
void SqlForWrite_Builder::buildSourceUpdateSql(const CROM* pTableNode)
{
	update_croms.clear();

	const KString sqlTableName(pTableNode->get_sqlTableName());

	std::map<CROM*,std::string> map;
	parseSourceUpdateFields(pTableNode, update_fields);

	std::string errmsg;

	KString set_clause, where_clause, csTmp;
	for(CROM* pCROM = pTableNode->GetFirstChild(); pCROM && !pCROM->IsElement(); pCROM=pCROM->GetNextSibling()) {
		if(!pCROM->IsAttribute())continue;
		bool bPrimaryKey = pCROM->IsPrimaryKey();
		if(!bPrimaryKey && map.find(pCROM)==map.end())continue;

		KString ColName(pCROM->GetDisplayName());
		KString sqlColName = sBeginQuote+ColName+sEndQuote;

		if(bPrimaryKey) {
			if(!where_clause.IsEmpty())where_clause += " AND ";
			std::string sqlValue;
			bool bParameterized;
			sqlValue=getSqlValue(pCROM->GetValue(), pCROM, m_conv, bParameterized, errmsg);
			if(!errmsg.empty()){
				throw konga::CrException("Error occurred on updating source database, "+errmsg+" for column: \""+(const char*)pCROM->get_Path()+"\"\n");
			}
			csTmp.Format("(%s=%s)", (const char*)sqlColName, sqlValue.c_str());
			where_clause += csTmp;			
		}
		else {
			if(!set_clause.IsEmpty())set_clause += ",";
			const KString& value(pCROM->GetValue());
			csTmp.Format("%s=%s", (const char*)sqlColName, (const char*)value);
			set_clause += csTmp;
		}
	}
	                             
	if( !where_clause.IsEmpty() && !set_clause.IsEmpty() ) {
		sqlUpdate.Format("UPDATE %s SET %s WHERE %s",
			(const char*)sqlTableName, (const char*)set_clause, (const char*)where_clause);
		update_croms.clear();
	}
	else {
		sqlUpdate.Empty();
		std::string err("Internal error, failed to generate update sql for update source database.");
		err+=std::string("\nUpdateFields=")+update_fields+"\nTableName="+(const char*)sqlTableName;
		throw konga::CrException(err);
	}
}

