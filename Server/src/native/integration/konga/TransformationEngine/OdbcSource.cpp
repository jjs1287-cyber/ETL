/*
 * Copyright (c) 2009-2011 Jitterbit, Inc.
 *
 */

#include <cassert>
#include <set>

#include <konga/Utils/AutoFileCleanup.h>
#include <konga/Utils/CrException.h>
#include <konga/Utils/CsvUtils.h>
#include <konga/Utils/ErrorResolver.h>
#include <konga/Utils/Settings.h>		                      
#include <konga/Utils/StringBuilder.h>
#include <konga/Utils/StringUtils.h>

#include <konga/CharacterEncodingConversion/Utf8Conv.h>

#include <konga/SystemVariables/SystemVariables.h>

#include <konga/Db/DbUtils/DbUtils.h>

#include "ErrorCodes/resource.h"
#include "TreeMapper.h"
#include "crom.h"
#include "DbAccess.h"
#include "OdbcTable.h"
#include "SqlStatementParser.h"
#include "SystemVariableDefs.h"
#include "OdbcSource.h"

                
long long OdbcSource::createSourceTextDocumentFromODBC(CROM*root,
												const std::string& dataFileName,
												const std::string& adHocFilter)
{
	OdbcSource odbcSource(root->getTreeMapper());

	                                                                                           
	FILE* out=fopen(dataFileName.c_str(), "w+");
	if(out==0){
		long lErr=::GetLastError();
		throw konga::CrException(std::string("OdbcSource::createSourceTextDocumentFromODBC failed, cannot open the output file. outputFileName=\"")+dataFileName+"\"\nReason: "+ErrorResolver().Resolve(lErr));
	}
	konga::AutoFileHandle adopt_out(out);

	return odbcSource.dumpSourceDataTree(root, adHocFilter, out);
}

OdbcSource::OdbcSource(TreeMapper*pTreeMapper) :
	m_pTreeMapper(pTreeMapper),
		m_DbAccess(pTreeMapper),
		m_pDbAccess2(0),
		m_conv(0),
		m_pRootTable(0),
		m_debug_log(0),
		m_sBeginQuote(pTreeMapper->getBeginSourceName()),
		m_sEndQuote(pTreeMapper->getEndSourceName())
{
	assert(pTreeMapper);
	m_viewlog = pTreeMapper->getViewLog();
	m_errlog = pTreeMapper->getErrorLog();
	m_bFlat = pTreeMapper->IsFlatSource();
	m_sDriverName=m_pTreeMapper->getSourceDbDriver();
	m_bIsOracle=konga::DriverIsOracle((const char*)m_sDriverName);

	if(m_viewlog->IsDebugLogEnabled()){
		m_viewlog->DebugString("Create OdbcSource for %s\nDriver = \"%s\", IsOracle=%s\n", 
			"Source", (const char*)m_sDriverName, (m_bIsOracle?"true":"false"));
		m_debug_log=fopen(pTreeMapper->getOdbcSourceLogFileName().c_str(), "w+");
	}
	m_conv = konga::Utf8Conv::createEncoderFor(konga::GetSystemVariable(ODBC_SOURCE_ENC, m_pTreeMapper->getOperationDataElementHolder(), ""));
}

OdbcSource::~OdbcSource(){
	for(std::map<CROM*, std::vector<CROM*>*>::iterator it=attributes_map.begin(); it!=attributes_map.end(); ++it){
		delete it->second;
	}
	for(std::map<CROM*, std::vector<CROM*>*>::iterator it=childTables_map.begin(); it!=childTables_map.end(); ++it){
		delete it->second;
	}
	for(std::map<CROM*, OdbcTable*>::iterator it=odbcTable_map.begin(); it!=odbcTable_map.end(); ++it){
		delete it->second;
	}
	m_DbAccess.Disconnect();
	if(m_pDbAccess2){
		m_pDbAccess2->Disconnect();
		delete m_pDbAccess2;
	}
	if(m_debug_log!=0)fclose(m_debug_log);
	delete m_conv;
}

int OdbcSource::dumpSourceDataTree(CROM*root, const std::string& adHocFilter, FILE*out){

	m_pRootTable=root->GetFirstChild();;
	if(m_pRootTable==0){
		std::string err("No root table in the source.");
		if(m_debug_log!=0)writeLog(err);
		throw konga::CrException(err);
	}
	initCromMaps(root);
	
	if(!adHocFilter.empty()){
		modifyRootTableFilter(m_pRootTable, adHocFilter);
	}
	
	lTxtIsolationLevel=m_pTreeMapper->m_lSourceTxnIsolationLevel;
	iTimeout=m_pTreeMapper->m_dwSrcTimeout;
	ConnectString=m_pTreeMapper->m_sSourceConnectStr;
	DSN=m_pTreeMapper->m_sSourceDsn;

	if (m_DbAccess.Connect(DSN, Username, Pwd, Driver, Server, DbName, ConnectString, iTimeout, lTxtIsolationLevel)==false)
	{
		const std::string stDSNNoPwd( konga::ReplacePassword((const char*)DSN, "<removed>") );
		const std::string stConnStrNoPwd( konga::ReplacePassword((const char*)ConnectString, "<removed>") );
		m_errlog->MakeError(__FILE__, __LINE__, ID_FAILED_DB_CONNECT, (const char*)m_DbAccess.m_LastErr,
			stDSNNoPwd.c_str(), (const char*)Username, (const char*)Driver, (const char*)Server,
			(const char*)DbName, stConnStrNoPwd.c_str());
		throw konga::DbException((const char*)m_errlog->GetLastErr());
	}
	m_viewlog->DebugString("Create OdbcSource - Connected to the source database \n" \
		"dsn = '%s' user = '%s', driver = '%s' server = '%s' dbname = '%s' connect string = '%s', timeout = '%d'\n",
		(const char*)DSN, (const char*)Username, (const char*)Driver, (const char*)Server, (const char*)DbName,
		konga::ReplacePassword((const char*)ConnectString,"<removed>").c_str(), iTimeout);

	long long numberOfRecords=0;
	try {
		std::vector<CROM*>* tables=childTables_map[root]; 
		assert(tables!=0);
		                                                                      
		for(std::vector<CROM*>::const_iterator it=tables->begin(); it!=tables->end(); ++it){ 
			CROM*pTable=*it;
			std::vector<CROM*>*attribues=getAttributes(pTable);
			OdbcTable odbc_table(this, pTable, m_DbAccess.m_hConn);                            
			if(m_debug_log!=0)writeLog(std::string("SQL for table \"") + (const char*)pTable->GetName() + "\": " + odbc_table.m_sql);
                                                                            
			odbc_table.execute();
			std::vector<CROM*>* childTables=childTables_map[pTable];
		    while(odbc_table.fetch()!=SQL_NO_DATA){
		    	DbRecord record(0, pTable, attribues);
				DbRecord*pRecord=&record;
				                                                                     
				std::vector<DbRecord*> records;
				records.push_back(pRecord);
				odbc_table.loadDbRecord(pRecord, numberOfRecords==0, m_conv);
		    	numberOfRecords++;
	        	if(m_debug_log!=0)writeLog("ADD - "+pRecord->toString());

			    if (childTables!=0 && !childTables->empty()) {
			        createSourceTreeForTable(pTable, childTables, records, "  ");
					checkInnerJoin(records);
					if(records.at(0)==0)continue;
			    }
				record.writeToText(out, m_bFlat);
		    }
			odbc_table.CloseCursor();
			if(m_debug_log!=0){
				KString msg;
				msg.Format("%8d rows of table %s added\n", numberOfRecords, (const char*)pTable->GetName());
				writeLog((const char*)msg);
			}
		}
	}
	catch (const konga::DbException& e) {
		std::string err=e.GetErrorString();
		throw konga::CrException(err);
	}
	return numberOfRecords;
}

void OdbcSource::createSourceTreeForTable(CROM* parentTable, std::vector<CROM*>*childTables, std::vector<DbRecord*>&parents, const std::string& header)
{
	                                      
	std::vector<CROM*>* parentColumns=getAttributes(parentTable);
	try {
		for(std::vector<CROM*>::const_iterator it=childTables->begin(); it!=childTables->end(); ++it){
			CROM*pTable=*it;
			std::vector<CROM*>*attributes=getAttributes(pTable);
			OdbcTable& odbc_table(*getOdbcTable(pTable));
			std::vector<DbRecord*> records;
			if(m_debug_log!=0)writeLog(std::string("SQL for table \"") + (const char*)pTable->GetName() + "\": " + odbc_table.m_sql);
                                                                            
			int iParent=1;
			for(std::vector<DbRecord*>::iterator it1=parents.begin(); it1!=parents.end(); ++it1, ++iParent){
				DbRecord*parentRecord=*it1;
				if(parentRecord==0)continue;
				parentRecord->loadColumns();
				bool success=odbc_table.execute_with_params();
				if(!success)continue;
                int numberOfRecords=0;
                while(odbc_table.fetch()!=SQL_NO_DATA){
                    DbRecord* pRecord = new DbRecord(parentRecord, pTable, attributes);
                    records.push_back(pRecord);
					odbc_table.loadDbRecord(pRecord, numberOfRecords==0, m_conv);
                    numberOfRecords++;
                    if(m_debug_log!=0)writeLog("ADD - "+pRecord->toString());
                }
				odbc_table.CloseCursor();
				if(m_debug_log!=0){
					KString msg;
					msg.Format("%8d rows of table \"%s\" add to the parent table \"%s\"\n", numberOfRecords, (const char*)pTable->GetName(), (const char*)parentTable->GetName());
					writeLog(msg);
				}
                if(numberOfRecords==0 && pTable->IsInnerJoin()){
					parentRecord->remove();
                   *it1=0;
                }
            }
            createSourceTreeForTable(pTable, getChildTables(pTable), records, header+"  ");
		}
	}
	catch (const konga::DbException& e) {
		std::string err=e.GetErrorString();
        if(m_debug_log!=0)writeLog(err);
		throw konga::DbException(err);
	}
}

void OdbcSource::checkInnerJoin(std::vector<DbRecord*>& records){
	for(std::vector<DbRecord*>::iterator it=records.begin(); it!=records.end(); ++it){
		DbRecord* parentRecord=*it;
		if(parentRecord==0)continue;
		checkInnerJoin(parentRecord->getChildRecords());
		const std::vector<CROM*>* childTables=getChildTables(parentRecord->table);
		for(std::vector<CROM*>::const_iterator it1=childTables->begin(); it1!=childTables->end(); ++it1){
			CROM*childTable=*it1;
			if(childTable->IsInnerJoin()){
				if(!parentRecord->hasChildren(childTable)){
					parentRecord->remove();
					*it=0;
					break;
				}
			}
		}
	}
}

void OdbcSource::writeLog(const std::string& msg){
	if(m_debug_log==0)return;
	fprintf(m_debug_log, "%s\n", msg.c_str());
}

void OdbcSource::writeLog(const char* msg){
	if(m_debug_log==0)return;
	fprintf(m_debug_log, "%s\n", msg);
}

void OdbcSource::initCromMaps(CROM*pTable){
	std::vector<CROM*>* attributes=new std::vector<CROM*>();
	std::vector<CROM*>* childTables=new std::vector<CROM*>();
	for(CROM*child=pTable->GetFirstChild(); child; child=child->GetNextSibling()){
		if(child->IsElement())childTables->push_back(child);
		else attributes->push_back(child);
	}
	attributes_map[pTable]=attributes;
	childTables_map[pTable]=childTables;
	for(std::vector<CROM*>::iterator it=childTables->begin(); it!=childTables->end(); ++it){
		CROM* childTable=*it;
		initCromMaps(childTable);
		                                      
		const int nFk=setPkLinks(childTable);
		                
	}
}


std::string OdbcSource::buildSqlForTable(CROM* pTable, const std::vector<CROM*>& bindableColumns, const std::vector<CROM*>& notypeColumns, const std::vector<CROM*>& fkColumns)
{
	std::string manualSql = pTable->get_manualSQL();
	if (!manualSql.empty()) {
        return manualSql;
    }
	std::string tableName = pTable->get_sqlTableName();

	konga::StringBuilder selectBuilder, whereBuilder;

	for(std::vector<CROM*>::const_iterator it=bindableColumns.begin(); it!=bindableColumns.end(); ++it){
		CROM*column=*it;
		if(selectBuilder.size()>0)selectBuilder.append(",");
		selectBuilder.append(m_sBeginQuote).append(column->GetDisplayName()).append(m_sEndQuote);
	}

	for(std::vector<CROM*>::const_iterator it=notypeColumns.begin(); it!=notypeColumns.end(); ++it){
		CROM*column=*it;
		if(selectBuilder.size()>0)selectBuilder.append(",");
		selectBuilder.append(m_sBeginQuote).append(column->GetDisplayName()).append(m_sEndQuote);
	}

	for(std::vector<CROM*>::const_iterator it=fkColumns.begin(); it!=fkColumns.end(); ++it){
		CROM*column=*it;
		if(whereBuilder.size()>0)whereBuilder.append(" AND ");
		whereBuilder.append("(").append(m_sBeginQuote).append(column->GetDisplayName()).append(m_sEndQuote).append("=?)");
	}

	bool hasJoins=whereBuilder.size()>0;
	std::string filter = pTable->get_sqlFilter(), order_by = pTable->get_sqlOrderBy();
	bool hasFilter = !filter.empty(), hasOrderBy = !order_by.empty();
	if(hasFilter || hasOrderBy){
		if(hasFilter){
			if(whereBuilder.size()>0)whereBuilder.append(" AND ");
			if(konga::startsWith(filter, "(") || !hasJoins)whereBuilder.append(filter);
			else whereBuilder.append("(").append(filter).append(")");
		}
		if(hasOrderBy){
			whereBuilder.append(" ORDER BY ").append(order_by);
		}
	}

	std::string where_clause=whereBuilder.toString();
	std::string select_clause=selectBuilder.toString();
	if(where_clause.empty()){
		return "SELECT " + select_clause + " FROM " + tableName;
	}
	else if( konga::startsWith(where_clause, " ORDER BY")){
		return "SELECT " + select_clause + " FROM " + tableName + where_clause;
	}
	else {
		return "SELECT " + select_clause + " FROM " + tableName + " WHERE " + where_clause;
	}
}


void OdbcSource::modifyRootTableFilter(CROM* rootTable, const std::string& adHocFilter0)
{
	if(adHocFilter0.empty())return;
	std::string adHocFilter = removeTagFromAdHocFilter(adHocFilter0);
	if(m_debug_log!=0)writeLog("adHocFilter="+adHocFilter);
	std::string manualSql = rootTable->get_manualSQL();
	if (!manualSql.empty()){
        applyAdHocFilterToManualSqlStatement(rootTable, adHocFilter, manualSql);
	} else {
        std::string ORDER_BY="ORDER BY ";
        std::string sqlFilter=rootTable->get_sqlFilter(), sqlOrderBy=rootTable->get_sqlOrderBy();
        int k=adHocFilter.find(ORDER_BY);
        if(k>=0){
            sqlFilter=adHocFilter.substr(0, k);
            sqlOrderBy=adHocFilter.substr(k+ORDER_BY.length());
			rootTable->set_sqlFilter(sqlFilter.c_str());
            rootTable->set_sqlOrderBy(sqlOrderBy.c_str());
            if(m_debug_log!=0){
                writeLog("rootTable->sqlFilter="+sqlFilter);
                writeLog("rootTable->sqlOrderBy="+sqlOrderBy);
            }
        }
        else {
            sqlFilter=adHocFilter;
            rootTable->set_sqlFilter(sqlFilter.c_str());
            if(m_debug_log!=0)writeLog("rootTable.sqlFilter="+sqlFilter);
        }
	}
}

std::string OdbcSource::removeTagFromAdHocFilter(const std::string& adHocFilter) {
	const char* tag="TransformationsTab.Source_SQL=";
	if(konga::startsWith(adHocFilter.c_str(), tag)){
		int k0=adHocFilter.size();
		int k1=strlen(tag);
		if(k0>k1 && adHocFilter.at(k1)=='"'){
			int k2;

			                                        
			for(k2=k1+1; k2<k0; ++k2)if(adHocFilter.at(k2)=='"')break;
			if(k2==k0)k2=-1;

			if(k2>k1)k1=k2+1;
		}

		                                 
		for(; k1<k0; ++k1)if(adHocFilter.at(k1)==':')break;
		if(k1==k0)k1=-1;

		if(k0>k1){
			return adHocFilter.substr(k1+1);
		} 
		else {
		    return "";
		}
	}
    return adHocFilter;
}

void OdbcSource::applyAdHocFilterToManualSqlStatement(CROM*rootTable, const std::string& adHocFilter, const std::string& manualSql){
    try {
        SqlStatementParser parser(manualSql);
		std::string newSql = parser.replaceFilter(adHocFilter);
        rootTable->set_manualSQL(newSql);
	} catch (const konga::CrException& ex) {
		throw konga::InternalErrorException("ETLForge could not modify the manual SQL statement by adding " \
			"the filter \"" + adHocFilter + "\". This is a bug. Please report it to ETLForge Support.\nReported error: " + ex.GetErrorString());
    }
}

std::vector<CROM*>* OdbcSource::getAttributes(CROM*table){
	std::vector<CROM*>* attributes=attributes_map[table];
	if(attributes!=0)return attributes;
	attributes=new std::vector<CROM*>();
	for(CROM*attribute=table->GetFirstChild(); attribute; attribute=attribute->GetNextSibling()){
		if(attribute->IsLeaf()){
			attributes->push_back(attribute);
		}
	}
	attributes_map[table]=attributes;
	return attributes;
}

std::vector<CROM*>* OdbcSource::getChildTables(CROM*table){
	std::vector<CROM*>* childTables=childTables_map[table];
	if(childTables!=0)return childTables;
	childTables=new std::vector<CROM*>();
	for(CROM*child=table->GetFirstChild(); child; child=child->GetNextSibling()){
		if(child->IsElement()){
			childTables->push_back(child);
		}
	}
	childTables_map[table]=childTables;
	return childTables;
}

int OdbcSource::setPkLinks(CROM* table){
	CROM* parent = table->GetParent();
	if(parent == 0)return 0;

	std::vector<CROM*>* attributes=getAttributes(table);
	std::vector<CROM*>* parent_attributes=getAttributes(parent);
	int i=0, k=0;
	for(std::vector<CROM*>::const_iterator it=attributes->begin(); it!=attributes->end(); ++it){
		CROM*column=*it;
		if(column->IsForeignKey()){
			k++;
			std::string pkName = column->get_LinkToPrimaryKey();
			for(std::vector<CROM*>::const_iterator it1=parent_attributes->begin(); it1!=parent_attributes->end(); ++it1){
				CROM* parentColumn=*it1;
				if(parentColumn->GetName()==pkName){
					column->set_PkLink(parentColumn);
					i++;
					break;
				}
			}
		}
	}
	assert(i==k);
	return k;
}


OdbcTable* OdbcSource::getOdbcTable(CROM*pTable) {
    OdbcTable* odbc_table = odbcTable_map[pTable];
    if (odbc_table != 0)return odbc_table;
	if(pTable==m_pRootTable)
		odbc_table=new OdbcTable(this, pTable, m_DbAccess.m_hConn);
	else {
		if(m_pDbAccess2==0){
			m_pDbAccess2=new CDbAccess(m_pTreeMapper);
			if (m_pDbAccess2->Connect(DSN, Username, Pwd, Driver, Server, DbName, ConnectString, iTimeout, lTxtIsolationLevel)==false)
			{
				const std::string stDSNNoPwd( konga::ReplacePassword((const char*)DSN, "<removed>") );
				const std::string stConnStrNoPwd( konga::ReplacePassword((const char*)ConnectString, "<removed>") );
				m_errlog->MakeError(__FILE__, __LINE__, ID_FAILED_DB_CONNECT, (const char*)m_pDbAccess2->m_LastErr,
					stDSNNoPwd.c_str(), (const char*)Username, (const char*)Driver, (const char*)Server,
					(const char*)DbName, stConnStrNoPwd.c_str());
				throw konga::DbException((const char*)m_errlog->GetLastErr());
			}
			m_viewlog->DebugString("Create OdbcSource - Connected to the source database \n" \
				"dsn = '%s' user = '%s', driver = '%s' server = '%s' dbname = '%s' connect string = '%s', timeout = '%d'\n",
				(const char*)DSN, (const char*)Username, (const char*)Driver, (const char*)Server, (const char*)DbName,
				konga::ReplacePassword((const char*)ConnectString,"<removed>").c_str(), iTimeout);
		}
		odbc_table=new OdbcTable(this, pTable, m_pDbAccess2->m_hConn);
	}
	odbcTable_map[pTable]=odbc_table;
	return odbc_table;
}

DbRecord::DbRecord(DbRecord*parent0, CROM* table, std::vector<CROM*>*attributes0) :
	parent(parent0), attributes(attributes0)
{
	this->table=table;
	if(parent!=0){
		parent->childRecords.push_back(this);
	}
}

DbRecord::~DbRecord(){
	for(std::vector<DbRecord*>::iterator it=childRecords.begin(); it!=childRecords.end(); ++it){
		delete *it;
	}
}

bool DbRecord::hasChildren(CROM* childTable) const {
	for(std::vector<DbRecord*>::const_iterator it=childRecords.begin(); it!=childRecords.end(); ++it){
		DbRecord* child=*it;
		if(child!=0 && child->table==childTable)return true;
	}
	return false;
}

bool DbRecord::IsInnerJoin() const {
	return table->IsInnerJoin();
}

void DbRecord::loadColumns(){
	std::vector<CROM*>::const_iterator it=attributes->begin();
	std::vector<std::string>::const_iterator it1=columnValues.begin();
	std::vector<bool>::const_iterator it2=columnIsNulls.begin();
	for(; it!=attributes->end(); ++it, ++it1, ++it2){
		assert(it1!=columnValues.end());
		assert(it2!=columnIsNulls.end());
		CROM*column=*it;
		column->SetValue(it1->c_str());
		column->SetNullFlag(*it2);
	}
}


void DbRecord::writeToText(FILE* out, bool isFlat)
{
	const char* nodeName((const char*)table->GetName());
	if(nodeName[0]!=0)
	{
		bool init=false;;
		if(!isFlat){
			fprintf(out, "%s", nodeName);
			init=true;
		}
		std::vector<std::string>::const_iterator it1=columnValues.begin();
		std::vector<bool>::const_iterator it2=columnIsNulls.begin();
		for(; it1!=columnValues.end(); ++it1, ++it2){
			assert(it2!=columnIsNulls.end());
			const std::string& value(*it1);
			bool isNull=*it2;
			if(init){
				fputc(',',out);
			}
			else {
				init=true;
			}
			if(!isNull){
				if(value.empty())fprintf(out,"\"\"");
				else konga::writeCsvData(out, value.c_str(), ',');
			}
		}
		fputc('\n',out);
	}
	std::set<CROM*>processed_table;
	for(std::vector<DbRecord*>::iterator it=childRecords.begin(); it!=childRecords.end(); ++it){
		DbRecord* childRecord=*it;
		if(childRecord==0)continue;
		CROM*child=childRecord->table;
		if(child->isLoopNode()==false){
			if(processed_table.find(child)!=processed_table.end()){
				throw konga::CrException(std::string("multiple instances for a non_loop node: \"")+(const char*)child->GetName()+"\"");
			}
		}
		processed_table.insert(child);
		childRecord->writeToText(out, isFlat);
	}
	for(CROM*child=table->GetFirstChild(); child; child=child->GetNextSibling()){
		if(child->IsLeaf())continue;
		if(child->IsOptionalNode()|| child->IsCollection())continue;
		if(processed_table.find(child)==processed_table.end()){
			throw konga::CrException(std::string("no instance for inner join node: \"")+(const char*)child->GetName()+"\"");
		}
	}
}

std::string DbRecord::toString(){
	std::vector<CROM*>::const_iterator it=attributes->begin();
	std::vector<std::string>::const_iterator it1=columnValues.begin();
	std::vector<bool>::const_iterator it2=columnIsNulls.begin();
	konga::StringBuilder sb;
	sb.append('{');
	for(bool first=true; it!=attributes->end(); ++it, ++it1, ++it2, first=false){
		assert(it1!=columnValues.end());
		assert(it2!=columnIsNulls.end());
		CROM*column=*it;
		if(!first)sb.append(',');
		if(column->IsAttribute())sb.append((const char*)column->GetName()).append("=");
		sb.append((*it2)? "<NULL>": (*it1)); 
	}
	sb.append('}');
	return sb.toString();
}

std::vector<DbRecord*>& DbRecord::getChildRecords(){
	return childRecords;
}

void DbRecord::remove(){
	for(std::vector<DbRecord*>::iterator it=childRecords.begin(); it!=childRecords.end(); ++it){
		DbRecord* child=*it;
		if(child!=0)delete child;
	}
	if(parent){
		std::vector<DbRecord*>& siblings(parent->childRecords);
		for(std::vector<DbRecord*>::iterator it=siblings.begin(); it!=siblings.end(); ++it){
			if(*it==this){
				*it=0;
				delete this;
				break;
			}
		}
	}
}

void DbRecord::addColumn(const std::string& value, bool bIsNull){
	columnValues.push_back(value);
	columnIsNulls.push_back(bIsNull);
}

