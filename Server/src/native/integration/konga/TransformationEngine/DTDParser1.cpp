/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <cassert>

#include <konga/Utils/ErrorResolver.h>
#include <konga/Utils/CsvUtils.h>
#include <konga/Utils/StringTokenizer.h>
#include <konga/Utils/StringUtils.h>
#include <konga/Utils/AutoFileCleanup.h>
#include <konga/FileTransfer/FileRetriever.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DbUtils/TableColumnsOdbc.h>
#include <konga/Db/TranDb/DBFieldsTab.h>
#include <konga/Db/TranDb/DBTablesTab.h>
#include <konga/Db/TranDb/Trans_FilterTab.h>
#include <konga/Db/TranDb/SqlScriptTab.h>
#include <konga/Db/TranDb/Tran_RelationsTab.h>
#include <konga/Db/TranDb/TRANSFORMATIONSTAB_V.h>
#include <konga/Db/TranDb/DocumentsTab.h>
#include <konga/Db/TranDb/Document_DefinitionsTab.h>

#include <konga/DataElement/DataElement.h>

#include <konga/TransformationLogging/ViewLog.h>
#include <konga/TransformationLogging/ErrorLog.h>
#include <konga/TransformationUtils/TransformationDataHolder.h>
#include <konga/TransformationUtils/TranDBUtils.h>

#include <konga/TransformationUtils/Definitions.h>

#include "TreeMapper.h"
#include "DTDParser.h"
#include "CDTDParser.h"
#include "CromToJtrWriter.h"
#include "TransFilterTab.h"

using namespace std;
using namespace konga;
using namespace TranDb;

struct Record_
{
	int Field_ID;
	int Parent_ID;
	string Field_Name;
	int Sequence;
	int Var_Type_ID;
	int Start_Pos;
	int End_Pos;
	int Required;
	string Default_value;
	string Format;
	Record_(TranDb::Document_DefinitionsTab*dd, int docId){
		string stErr="";
		if(dd->IsField_NameNull()){
			if(!stErr.empty())stErr+=", ";
			stErr+="Field_Name undefined";
		} else {
			Field_Name=dd->GetField_Name();
		}
		if(dd->IsParent_IDNull()){
			if(!stErr.empty())stErr+=", ";
			stErr+="Parent_ID undefined";
		}
		if(dd->IsSequenceNull()){
			if(!stErr.empty())stErr+=", ";
			stErr+="Sequence field undefined";
		}
		if(dd->IsVar_Type_IDNull()){
			if(!stErr.empty())stErr+=", ";
			stErr+="Variable_Type_ID field undefined";
		}
		if(!stErr.empty()){
			throw konga::CrException(string("CreateJtrForTextDocument(")+konga::toString(docId)+") failed for Segment '"+Field_Name+"' due to "+stErr);
		}
		Field_ID=dd->GetField_ID();
		Parent_ID=dd->GetParent_ID();
		Sequence=dd->GetSequence();
		Var_Type_ID=dd->GetVar_Type_ID();
		Start_Pos=dd->IsStart_PosNull()?-1:dd->GetStart_Pos();
		End_Pos=dd->IsEnd_PosNull()?-1:dd->GetEnd_Pos();
		Required=dd->IsRequiredNull()?0:dd->GetRequired();
		Default_value=dd->IsDefault_valueNull()?"":dd->GetDefault_value();
		Format=dd->IsFormatNull()?"":dd->GetFormat();
	}
};

namespace {
	const char* getSqlTableName(TableAliasMap &mapTableAlias,const string &deTableName)
	{
		TableAliasMap::iterator it=mapTableAlias.find(deTableName);
		if(it==mapTableAlias.end()){
			throw konga::CrException("getSqlTableName failed, deTableName="+deTableName);
		}
		return it->second.c_str();
	}
}

CViewLog* DTDParser::getViewLog(){return getTreeMapper()->getViewLog();}

CErrorLog* DTDParser::getErrorLog(){return getTreeMapper()->getErrorLog();}

unsigned long DTDParser::CreateJtrForTextDocument(int docId, const string& stJtrFileName)
{
	PROFILING doPrf(getTreeMapper(),"DTDParser_CreateJtrForTextDocument");
	enum Type{flat_fix_doc=1, flat_delimited_doc=2, complex_fix_text=3, complex_del_text=4};

	SmartDbPtr<DbConnection> pTranDbConn( konga::GetBackEndDbConnection("TranDb") );

	vector<string>dataTypes;
	dataTypes.push_back("String");                 
	dataTypes.push_back("Integer");                 
	dataTypes.push_back("Date");                 
	dataTypes.push_back("Long");                 
	dataTypes.push_back("Float");                 
	dataTypes.push_back("Double");                 
	dataTypes.push_back("Boolean");                 
	dataTypes.push_back("Segment");                 
	dataTypes.push_back("Identifier");                 
	int SegmentTypeID=8;
	string stDelimiter,stStrQual,stNumQual;
	Type docType;
	try
	{
		{
			SmartDbPtr<TranDb::DocumentsTab> documentsTab(TranDb::DocumentsTab::MakeInstance(pTranDbConn));
			string stDocumentTabFilter = "WHERE Document_ID = " + konga::toString(docId);
			documentsTab->Select(stDocumentTabFilter);
			if( documentsTab->Next() )
			{
				stDelimiter = documentsTab->IsDelimiterNull()?"":documentsTab->GetDelimiter();
				stStrQual = documentsTab->IsText_QualIFierNull()?"":documentsTab->GetText_QualIFier();
				stNumQual = documentsTab->IsNumeric_QualIFierNull()?"":documentsTab->GetNumeric_QualIFier();
				docType = (Type)documentsTab->GetFormat_Type_ID();
			} else {
				m_lastErr += "Program failed, couldn't find the document definition in the database!";
				return E_FAIL;
			}
		}
	
		if(m_root)m_root->DeleteNode();
		m_root=0;
		{
			switch(docType){
			case flat_fix_doc:
			case flat_delimited_doc:
				{
					m_root=new CROM(getTreeMapper(), 0, 0, 0, 0, CROM_t_Element|CROM_t_Root,CROM::t_CROM_Text);
					CROM* parent=new CROM(getTreeMapper(), m_root, 0, "", 0, CROM_t_Element|CROM_t_Collection, CROM::t_CROM_Text), *obj=0;
					if(!stStrQual.empty())parent->set_strQual(stStrQual.at(0));
					                                                             
					if(!stDelimiter.empty())parent->set_delimiter(stDelimiter.at(0));
					SmartDbPtr<TranDb::Document_DefinitionsTab> field(TranDb::Document_DefinitionsTab::MakeInstance(pTranDbConn));
					const string stFilter = "WHERE Document_ID = " + konga::toString(docId) + " ORDER BY Field_Id";
					field->Select(stFilter);
					while(field->Next()){
						Record_ r(field,docId);
						obj=new CROM(getTreeMapper(), parent, obj, r.Field_Name.c_str(), 0, CROM_t_Attribute, CROM::t_CROM_Text);
						if(docType!=flat_fix_doc||!r.Field_Name.empty()){
							if(docType==flat_fix_doc){
								obj->set_fieldPos(r.Start_Pos);
								obj->set_fieldLength(r.End_Pos-r.Start_Pos+1);
							}
							obj->set_ValueType(dataTypes.at(r.Var_Type_ID-1).c_str());
							if(!r.Format.empty())obj->set_Format(r.Format.c_str());
							if(!r.Default_value.empty())obj->setDefault(r.Default_value.c_str());
						}
					}
				}
				break;
			case complex_fix_text:
			case complex_del_text:
				{
					SmartDbPtr<TranDb::Document_DefinitionsTab> document_definitionsTab(TranDb::Document_DefinitionsTab::MakeInstance(pTranDbConn));
					string stFilter = "WHERE (Document_ID = " + konga::toString(docId) + ") AND (Var_Type_ID=" + konga::toString(SegmentTypeID) + ") ORDER BY Field_ID";
					document_definitionsTab->Select(stFilter);

					map<int, CROM*>map_segment;
					while(document_definitionsTab->Next()){
						Record_ segment(document_definitionsTab, docId);
						assert(map_segment.find(segment.Field_ID)==map_segment.end());
						CROM*folder=new CROM(getTreeMapper(), 0, 0, segment.Field_Name.c_str(), 0, CROM_t_Element, CROM::t_CROM_Text);
						if(segment.Parent_ID==0){                  
							m_root=folder;
							m_root->AddType(CROM_t_Root|CROM_t_OneToOne);
						}
						map_segment[segment.Field_ID]=folder;
						if(docType==complex_fix_text){
							folder->set_fieldPos(segment.Start_Pos);
							folder->set_fieldLength(segment.End_Pos-segment.Start_Pos+1);
						}
						else {
							if(!stStrQual.empty())folder->set_strQual(stStrQual.at(0));
							                                                             
							if(!stDelimiter.empty())folder->set_delimiter(stDelimiter.at(0));
						}

						int parentId=segment.Sequence==0?segment.Field_ID:segment.Sequence;

						SmartDbPtr<TranDb::Document_DefinitionsTab> field(TranDb::Document_DefinitionsTab::MakeInstance(pTranDbConn));
						string stFilter = "WHERE (Document_ID = " + konga::toString(docId) + ") AND (Parent_ID="+konga::toString(parentId)+") AND (Var_Type_ID!=" + konga::toString(SegmentTypeID) + ") ORDER BY Field_ID";
						field->Select(stFilter);
						CROM*child=0;
						while(field->Next()){
							Record_ r(field, docId);
							child=new CROM(getTreeMapper(), folder, child, r.Field_Name.c_str(), 0, CROM_t_Attribute, CROM::t_CROM_Text);
							if(docType==complex_del_text||!r.Field_Name.empty()){
								if(docType==complex_fix_text){
									child->set_fieldPos(r.Start_Pos);
									child->set_fieldLength(r.End_Pos-r.Start_Pos+1);
								}
								child->set_ValueType(dataTypes.at(r.Var_Type_ID-1).c_str());
								if(!r.Format.empty())child->set_Format(r.Format.c_str());
								if(!r.Default_value.empty())child->setDefault(r.Default_value.c_str());
							}
						}
						if(segment.Parent_ID>0){
							CROM*parent=map_segment[segment.Parent_ID];
							assert(parent!=0);
							parent->addChild(folder);
							switch(segment.Required){
								case 1: folder->AddType(CROM_t_OneToOne);break;
								case 2: folder->AddType(CROM_t_AtLeastOne|CROM_t_LoopNode); break;
								case 3: folder->AddType(CROM_t_Optional); break;
								case 4: folder->AddType(CROM_t_OuterJoin|CROM_t_LoopNode); break;
							}
						}
					}
				}
				break;
			}
			CromToJtrWriter writer;
			writer.createJtr(CROMHeader(m_root), stJtrFileName);

		}
	}
	catch( DbException& e )
	{
		throw CrException(string("Create structure file for text document failed, ")+e.GetErrorString());
	}
	return 0;
}

TableField::TableField(const string& stName,
		   const string& stDataType,
		   int sqlDataType,
		   int dbColSize,
		   int dbDecDigits,
		   const string& stDefault,
		   bool bNullable,
		   bool bHasDefault) : name(stName),
		   					   defaultValue(stDefault),
		   					   nullable(bNullable),
		   					   hasDefault(bHasDefault)
{ 
	DataElement::Type type=DataElement::get_type(stDataType.c_str(),sqlDataType);
	const char* str=DataElement::get_type_name(type);
	if(str!=0)dataType=str;
	int k=(int)dataType.find(" identity");
	if(k>-1)dataType=dataType.substr(0,k);
	this->sqlDataType=sqlDataType;
	this->dbColSize=dbColSize;
	this->dbDecDigits=dbDecDigits;
}

void DTDParser::get_connectionStr(DbConnection* pTranDbConn, int transId, char cSorT,
								  string& sConnectionStr, long& lTimeOut, TransactionIsolationLevel& txnIsolationLevel,
								  string& sBeginQuote, string& sEndQuote)
{
	assert(pTranDbConn);
	SmartDbPtr<TranDb::TRANSFORMATIONSTAB_V> tranDb( TranDb::TRANSFORMATIONSTAB_V::MakeInstance(pTranDbConn) );
	string stTranDbFilter = "WHERE Transformation_ID = " + konga::toString(transId);
	tranDb->Select(stTranDbFilter);
	if( tranDb->Next() )
	{
		unsigned long hr = S_OK;
		DbConnectionInfoStruct dbConnInfo;
		string sErr;
		if(cSorT=='T'){
			const int targetId=atoi(tranDb->GetTarget_TargetID().c_str());
			hr=getTargetConnectionInfo(*pTranDbConn, getTreeMapper()->getOperationContext()->getCurrentUser(), targetId,
					getTreeMapper()->getOperationDataElementHolder(), dbConnInfo, sErr);
			if ( FAILED(hr) ) {
				m_lastErr.Format("Failed to get the target connection string. Called from CreateJtrForDB()\n%s", sErr.c_str());
				throw konga::CrException((const char*)m_lastErr);
			}
		} else if(cSorT=='S'){
			const int sourceId=atoi(tranDb->GetSource_SourceID().c_str());
			hr=getSourceConnectionInfo(*pTranDbConn, getTreeMapper()->getOperationContext()->getCurrentUser(), sourceId,
				getTreeMapper()->getOperationDataElementHolder(), dbConnInfo, sErr);
			if ( FAILED(hr) ) {
				m_lastErr.Format("Failed to get the source connection string. Called from CreateJtrForDB()\n%s", sErr.c_str());
				throw konga::CrException((const char*)m_lastErr);
			}
		}
		else {
			throw InternalErrorException("Invalid location type '" + string(1, cSorT) + "'. T and S are the only allowed values.");
		}

		                          
		sConnectionStr = dbConnInfo.connectionString;
		lTimeOut = dbConnInfo.timeOutSeconds;
		txnIsolationLevel = dbConnInfo.txnIsolationLevel;

		                                                          
		const string& sBeginQuote1 = dbConnInfo.beginQuote;
		const string& sEndQuote1 = dbConnInfo.endQuote;
		if(sBeginQuote.empty() && !sBeginQuote1.empty())sBeginQuote=sBeginQuote1;
		if(sEndQuote.empty() && !sEndQuote1.empty())sEndQuote=sEndQuote1;
	}
	else
	{
		m_lastErr += "No transformation defined with ID " + konga::toString(transId);
		throw konga::CrException((const char*)m_lastErr);
	}
}


int DTDParser::define_mapTableAlias(DbConnection* pTranDbConn, int transId, int type, TableAliasMap& mapTableAlias, const string& sBeginQuote, const string& sEndQuote){
	string stDBTables;
	SmartDbPtr<TranDb::DBTablesTab> dbTablesTab( TranDb::DBTablesTab::MakeInstance(pTranDbConn) );
	string stTablesTabFilter = "WHERE (Transformation_ID = " + konga::toString(transId) + ") AND (TablesType = " + konga::toString(type) + ")";
	dbTablesTab->Select(stTablesTabFilter);
	if( dbTablesTab->Next() && !dbTablesTab->IsTablesNull() ) {
		stDBTables = dbTablesTab->GetTables();
	}
	return updateTableNameAlias(stDBTables.c_str(), mapTableAlias, sBeginQuote, sEndQuote);
}

void DTDParser::define_DbRelation_FieldVectors(DbConnection* pTranDbConn, int transId, char cSorT, TranRelationVector& vRelations, TableFieldVectorMap& mapTableFieldVector, const string& sBeginQuote, const string& sEndQuote)
{
	SmartDbPtr<TranDb::Tran_RelationsTab> relTab( TranDb::Tran_RelationsTab::MakeInstance(pTranDbConn) );
	string stRelFilter = "WHERE (Transformation_ID = " + konga::toString(transId) + ") AND (Doc_Type = '" + string(&cSorT,1) + "')";
	relTab->Select(stRelFilter);
	while( relTab->Next() )
	{
		string Parent_Name=relTab->IsParent_NameNull()?"":getNameFromSqlTableName(relTab->GetParent_Name().c_str(), sBeginQuote, sEndQuote);
		string Parent_Field=relTab->IsParent_FieldNull()?"":(relTab->GetParent_Field());
		string Child_Name=relTab->IsChild_NameNull()?"":getNameFromSqlTableName(relTab->GetChild_Name().c_str(), sBeginQuote, sEndQuote);
		string Child_Field=relTab->IsChild_FieldNull()?"":(relTab->GetChild_Field());
		int parent_field_len=Parent_Field.length();
		if( parent_field_len>2 && !sBeginQuote.empty() && !sEndQuote.empty() &&
			Parent_Field.at(0)==sBeginQuote.at(0) && Parent_Field.at(parent_field_len-1)==sEndQuote.at(0))
		{
				Parent_Field=Parent_Field.substr(1,parent_field_len-2);
		}
		int child_field_len=Child_Field.length();
		if(child_field_len>2 && !sBeginQuote.empty() && !sEndQuote.empty() &&
		   Child_Field.at(0)==sBeginQuote.at(0) && Child_Field.at(child_field_len-1)==sEndQuote.at(0))
		{
				Child_Field=Child_Field.substr(1,child_field_len-2);
		}
		int Outer_Join=relTab->IsOuter_JoinNull()?0:relTab->GetOuter_Join();
		int OneToOne=relTab->IsOneToOneNull()?0:relTab->GetOneToOne();

		vRelations.push_back( TranRelationInfo(Parent_Name, 
											   Parent_Field, 
											   Child_Name,
											   Child_Field,
											   Outer_Join,
											   OneToOne) );
		if( mapTableFieldVector.find(Parent_Name) == mapTableFieldVector.end() ) {
			mapTableFieldVector[Parent_Name] = new FieldVector();
		}
		if( mapTableFieldVector.find(Child_Name)==mapTableFieldVector.end() ) {
			mapTableFieldVector[Child_Name] = new FieldVector();
		}
	}
}

void DTDParser::addDbFilterToTableNodes(DbConnection* pTranDbConn, TableAliasMap& mapTableAlias, const std::string& sBeginQuote,  const std::string& sEndQuote){
	PROFILING doPrf(getTreeMapper(),"addDbFilterToTableNodes");
	int transId=getTreeMapper()->getTransformationID();
	try
	{
		assert(m_root!=0);
		m_root->clearFilter_recursive();

		map<string,CTableFilters*> tableFilters;
		CTableFilters* pTableFilter = 0;
		if(mapTableAlias.empty())
			define_mapTableAlias(pTranDbConn, transId, 1, mapTableAlias, sBeginQuote, sEndQuote);

		SmartDbPtr<Trans_FilterTab> tff( Trans_FilterTab::MakeInstance(pTranDbConn) );
		const string stTransFilterTabFilter( "WHERE Transformation_ID = " +
			toString(transId) + " ORDER BY tname" );
		tff->Select(stTransFilterTabFilter);

		while( tff->Next() )
		{
			const string stTableName_Alias = tff->GetTName();
			const string name(getNameFromSqlTableName(stTableName_Alias.c_str(), sBeginQuote, sEndQuote));
			KString sqlTableName=getSqlTableName(mapTableAlias, name);
			KString sqlColName = tff->GetCName();
			int n=sqlColName.GetLength();
			if( n<2 || (!sBeginQuote.empty() && sqlColName.GetAt(0)!=sBeginQuote.at(0)) || (!sEndQuote.empty() && sqlColName.GetAt(n-1)!=sEndQuote.at(0)))
			{
				sqlColName=sBeginQuote.c_str()+sqlColName+sEndQuote.c_str();
			}
			KString sTmp=sqlTableName+"."+sqlColName;

			KString sCondition = tff->GetCondition();
			if(!sCondition.IsEmpty())                                 
				sCondition = sTmp + sCondition;
			KString sSortBy = tff->GetSortBy();
			if(sSortBy=="NONE")sSortBy="";
			if(!sSortBy.IsEmpty())                                 
				sSortBy = sTmp + " " + tff->GetSortBy();

			map<string,CTableFilters*>::const_iterator cit = tableFilters.find(name);
			if ( cit == tableFilters.end() ) {
				pTableFilter = new CTableFilters(name.c_str());
				tableFilters[name]=pTableFilter;
			}
			else {
				pTableFilter = cit->second;
			}
			pTableFilter->m_vColumnNames.push_back(sqlColName);
			pTableFilter->m_vConditions.push_back(sCondition);
			pTableFilter->m_vOrderBy.push_back(sSortBy);

			if ( getViewLog()->IsDebugLogEnabled() ) {
				getViewLog()->DebugString("pTableFilter TableName=\"%s\", Alias=\"%s\", ColName=\"%s\", Condition=\"%s\", SortBy=\"%s\"\n",
					name.c_str(),stTableName_Alias.c_str(),(const char*)sqlColName,(const char*)sCondition,(const char*)sSortBy);
			}
		}
		                                           
		for(map<string,CTableFilters*>::iterator it=tableFilters.begin(); it!=tableFilters.end(); ++it){
			string tableName(it->first);
			CROM*pTableNode=m_root->FindFirstObject(tableName.c_str());
			pTableFilter=it->second;
			KString sCondition0, sSortBy0;
			for(int i=0, n=pTableFilter->m_vColumnNames.size(); i<n; ++i){
				KString sCondition=pTableFilter->m_vConditions[i];
				if(!sCondition.IsEmpty()){
					if(!sCondition0.IsEmpty())sCondition0+=" AND ";
					sCondition0 += "("+sCondition+")";
				}
				KString sSortBy=pTableFilter->m_vOrderBy[i];
				if(!sSortBy.IsEmpty()){
					if(!sSortBy0.IsEmpty())sSortBy0+=",";
					sSortBy0 += sSortBy;
				}
			}
			if(!sCondition0.IsEmpty()){
				KString sqlFilter(pTableNode->get_sqlFilter());
				if(!sCondition0.IsEmpty()){
					if(!sqlFilter.IsEmpty())sqlFilter+=" AND ";
					sqlFilter+=sCondition0;
				}
				pTableNode->set_sqlFilter(sqlFilter);
			}
			if(!sSortBy0.IsEmpty()){
				KString sqlOrderBy(pTableNode->get_sqlOrderBy());
				if(!sSortBy0.IsEmpty()){
					if(!sqlOrderBy.IsEmpty())sqlOrderBy+=",";
					sqlOrderBy+=sSortBy0;
				}
				pTableNode->set_sqlOrderBy(sqlOrderBy);
			}
		}

		                                                               
		if(getTreeMapper()->IsFlatSource()){

			SmartDbPtr<SqlScriptTab> sst( SqlScriptTab::MakeInstance(pTranDbConn) );
			const string stSqlScriptTabFilter( "WHERE (Transformation_ID = " + toString(transId)
				+") AND (SqlScriptType = 1)");
			sst->Select(stSqlScriptTabFilter);
			sst->Next();
			CROM* pTableNode=m_root->GetFirstChild();
			assert(pTableNode!=0 && pTableNode->IsElement());
			KString source_where;
			if(sst->IsSqlWhereNull()==false)source_where=sst->GetSqlWhere();
			if(!source_where.IsEmpty()){
				const char *sOrderBy=" ORDER BY ";
				int k2=source_where.Find(sOrderBy);
				if(k2==0){
					KString sqlOrderBy=source_where.Mid(k2+strlen(sOrderBy));
					pTableNode->set_sqlOrderBy(sqlOrderBy);
				}
				if(k2>0){
					KString sqlFilter=source_where.Mid(0,k2), sqlOrderBy=source_where.Mid(k2+strlen(sOrderBy));
					pTableNode->set_sqlFilter(sqlFilter);
					pTableNode->set_sqlOrderBy(sqlOrderBy);
				}
				else {
					pTableNode->set_sqlFilter(source_where);
				}
			}
		}

		                                                                                                              
		m_root->addUpdateKeyIfNoneDefined();
	}
	catch( const DbException& e )
	{
		throw konga::CrException(e.GetErrorString());
	}
}


unsigned long DTDParser::CreateJtrForDB(long transId, int type, const string& stJtrFileName)
{
	PROFILING doPrf(getTreeMapper(),"DTDParser_CreateJtrForDB");
	const bool bIgnoreDefault=false;
	unsigned long hr=S_OK;
	char cSorT=type==1?'S':'T';
	string sBeginQuote, sEndQuote;
	switch(cSorT){
		case 'S':
			sBeginQuote=getTreeMapper()->getBeginSourceName();
			sEndQuote=getTreeMapper()->getEndSourceName();
			break;
		case 'T':
			sBeginQuote=getTreeMapper()->getBeginTargetName();
			sEndQuote=getTreeMapper()->getEndTargetName();
			break;
	}
	string quote("\"");
	if(m_root){
		m_root->DeleteNode();
		m_root=0;
	}
	
	DbConnection* pTranDbConn = konga::GetBackEndDbConnection("TranDb");
	SmartDbPtr<DbConnection> adopt_dbConn(pTranDbConn);

	                                       
	TranRelationVector vRelations;
	TableFieldVectorMap mapTableFieldVector;
	TableAliasMap mapTableAlias;

	string sConnectionStr, sErr;
	long lTimeOut = 0;
	TransactionIsolationLevel txnIsolationLevel;
	bool bIgnoreDefaultValue = true;
	try
	{
		get_connectionStr(pTranDbConn, transId, cSorT, sConnectionStr, lTimeOut, txnIsolationLevel, sBeginQuote, sEndQuote);
		string sDriver;
		konga::defineDbDriverDependentStr(sConnectionStr, sDriver);
		bIgnoreDefaultValue=IsDbColumnDefaultValueIgnored(sDriver);

		int nTables=define_mapTableAlias(pTranDbConn, transId, type, mapTableAlias, sBeginQuote, sEndQuote);
		if(nTables==1)
		{
			map<string,string>::iterator it=mapTableAlias.begin();
			if(it!=mapTableAlias.end()){
				string tableName=it->first;
				mapTableFieldVector[tableName] = new FieldVector();
			}
		}
		
		define_DbRelation_FieldVectors(pTranDbConn, transId, cSorT, vRelations, mapTableFieldVector, sBeginQuote, sEndQuote);
	
	}
	catch( DbException& e )
	{
		                          
		m_lastErr += string("DTDParser::CreateJtrForDB() failed, ")+e.GetErrorString();
		hr = E_FAIL;
		return hr;
	}
	
	{
		for(TableFieldVectorMap::iterator it=mapTableFieldVector.begin();it!=mapTableFieldVector.end();it++){
			string tableAlias = it->first, owner, tableName, sqlTableName;
			sqlTableName=getSqlTableName(mapTableAlias, tableAlias);
			separateOwnerTableNames(sqlTableName, owner, tableName, sBeginQuote, sEndQuote);

			FieldVector* v = it->second;
            vector<DbColumn> vColumns;
			OdbcColumns tableColumns(sConnectionStr);
			tableColumns.SetTimeOut(lTimeOut);
                                       
            if( tableColumns.GetColumns( vColumns, tableName, owner ))
            {
				if(vColumns.size()==0){
					m_lastErr="Cannot get column information for table \""
						+ tableName + "\" from database. Either the table does not exist or the table has no columns.";
					tableColumns.Close();
					hr=E_FAIL;
					return hr;
				}
                for( int nCol = 0, nCols = (int)vColumns.size(); nCol < nCols; ++nCol )
                {
                    DbColumn col = vColumns[nCol];
                    bool hasDefault = (!bIgnoreDefaultValue)&&col.HasDefaultValue;
					string sDefaultValue;
					if(!bIgnoreDefaultValue)sDefaultValue=col.DefaultValue;
                    bool isNullable = col.Nullable!=0;
					v->push_back( TableField(col.Name,col.DataTypeName,col.SqlDataType,
						col.Size, col.DecimalDigits,
						sDefaultValue,isNullable,hasDefault) );
                }
            }
            else
            {
                tableColumns.Close();
				m_lastErr=string("CreateJtrForDB() failed while retrieving column information from database.\nTableName=\"")+tableName.c_str()+"\"\n"+tableColumns.GetLastError();                           
				throw konga::CrException((const char*)m_lastErr);
			}
			tableColumns.Close();
		}
	}

	TreeMapper*treemapper=0;
	{
		                                                                                    
		                                   
		                                                                                          
		                                                                                                                                                             
		   
		map<string,CROM*>element_map;
		for(TableFieldVectorMap::iterator it=mapTableFieldVector.begin();it!=mapTableFieldVector.end();it++){
			string tableAlias = it->first, owner, tableName, sqlTableName;
			sqlTableName = getSqlTableName(mapTableAlias, tableAlias);
			separateOwnerTableNames(sqlTableName, owner, tableName, sBeginQuote, sEndQuote);
			FieldVector*v=it->second;
			CROM*element=new CROM(treemapper, 0, 0, tableAlias.c_str(),"",CROM_t_Element, CROM::t_CROM_DB), *child=0;
			element->set_dbSchema(owner.c_str());
			element->set_sqlTableName(sqlTableName.c_str());
			element_map[tableAlias]=element;			
			for(FieldVector::iterator it1=v->begin();it1!=v->end();it1++){
				bool required=!it1->nullable;
				if(!bIgnoreDefault)required=required&&!it1->hasDefault;
				if((!bIgnoreDefault)&&it1->defaultValue.length()>0)required=false;
				child=new CROM(treemapper, element, child, it1->name.c_str(), 0, CROM_t_Attribute, CROM::t_CROM_DB);               
				if(!required)child->m_Type|=CROM_t_Optional;
				child->SetValueType(it1->dataType.c_str());
				child->set_SqlDataType(it1->sqlDataType);
				child->set_DbColSize(it1->dbColSize);
				child->set_DbDecimalDigits(it1->dbDecDigits);
				if(it1->hasDefault)child->setDefault(it1->defaultValue.c_str());
			}
		}
		for(TranRelationVector::iterator it=vRelations.begin();it!=vRelations.end();it++){
			TranRelationInfo& relation(*it);
			CROM*parent=element_map[relation.parent],*child=element_map[relation.child];
			if(parent)parent->addChild(child);
			if(relation.one_one){
				if(relation.join){
					child->AddType(CROM_t_Optional);
				}
			}
			else if(relation.join){
				child->AddType(CROM_t_Collection|CROM_t_LoopNode);
			}
			else{
				child->AddType(CROM_t_AtLeastOne|CROM_t_LoopNode);
			}
			if(parent){
				int err=DTDParser::DefinePrimaryKey(parent, relation.PK.c_str());
				if(err!=0)throw konga::CrException("DefinePrimaryKey failed, can't find primary key \""+relation.PK+"\" in table \""+parent->get_sqlTableName()+"\"");
			}
			else {
				throw konga::CrException("DefinePrimaryKey failed, can't find table \""+std::string(relation.parent)+"\"");
			}
			DTDParser::DefineForeignKey(child, relation.PK.c_str(), relation.FK.c_str());
		}

		                                                                          
		SmartDbPtr<TranDb::DBFieldsTab> dbfields( TranDb::DBFieldsTab::MakeInstance(pTranDbConn) );
		string stTranDbFilter = "WHERE Transformation_ID = " + konga::toString(transId);
		dbfields->Select(stTranDbFilter);
		while( dbfields->Next() )
		{
			if(!dbfields->IsKeyFieldsTypeNull()&&dbfields->GetKeyFieldsType()==3&&!dbfields->IsKeyFieldsNull()){
				string stKeyFields=dbfields->GetKeyFields();
				StringTokenizer tk(stKeyFields.c_str(),",");
				while(tk.NextToken()){
					string token=tk.token();
					if(token.at(0)==',')continue;
					string owner,tableName,field;
					parseOwnerTableFieldString(token.c_str(),owner,tableName,field, sBeginQuote, sEndQuote);
					string ownerTableName=owner.empty()?tableName:(owner+":"+tableName);
					TableFieldVectorMap::iterator it=mapTableFieldVector.find(ownerTableName);
					if(it!=mapTableFieldVector.end()){
						CROM*parent=element_map[ownerTableName];
						if(parent){
							int err=DTDParser::DefineUpdateKey(parent,field.c_str());
							if(err!=0)throw konga::CrException("DefineUpdateKey failed, can't find update key \""+field+"\" in table \""+parent->get_sqlTableName()+"\"");
						}
						else {
							throw konga::CrException("DefineUpdateKey failed,  can't find table \""+ownerTableName+"\"");
						}
					}
				}
			}
		}

		for(TranRelationVector::iterator it=vRelations.begin();it!=vRelations.end();it++){
			TranRelationInfo& relation(*it);
			element_map[relation.child]=0;
		}

		m_root=new CROM(treemapper, 0, 0, 0, 0, CROM_t_Element|CROM_t_Root, CROM::t_CROM_DB);
		if(vRelations.size()>0){
			                                                                
			for(TranRelationVector::iterator it=vRelations.begin();it!=vRelations.end();it++){
				TranRelationInfo& relation(*it);
				CROM*parent=element_map[relation.parent];
				CROM*child=element_map[relation.child];
				if(parent && child){
					parent->addChild(child);
					if(!relation.one_one){
						child->AddType(relation.join?CROM_t_AtLeastOne: CROM_t_Collection);
						child->AddType(CROM_t_LoopNode);
					}
					else if(!relation.join){
						child->AddType(CROM_t_Optional);
					}
				}
			}
		}
		for(map<string,CROM*>::iterator it=element_map.begin();it!=element_map.end();++it){
			CROM*node=it->second;
			if(node!=0 && node->GetParent()==0){
				m_root->addChild(node);
				node->AddType(CROM_t_Collection|CROM_t_LoopNode);
			}
		}
	}

	                                               
	if(type==1 ){               
		addDbFilterToTableNodes(pTranDbConn, mapTableAlias, sBeginQuote,  sEndQuote);
	}
	CromToJtrWriter writer;
	writer.createJtr(CROMHeader(m_root, DB_CURRENT_VERSION,sBeginQuote, sEndQuote), stJtrFileName);
	return hr;
}

