/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <boost/scoped_array.hpp>

#include <konga/Utils/CrException.h>
#include <konga/Utils/CsvUtils.h>
#include <konga/Utils/ErrorResolver.h>
#include <konga/Utils/FileUtils.h>
#include <konga/Utils/Settings.h>		                      
#include <konga/Utils/Timing.h>			                         
#include <konga/Utils/FileContents.h>

#include <konga/CharacterEncodingConversion/Utf8Conv.h>

#include <konga/Db/DbUtils/DbUtils.h>

#include <konga/SystemVariables/SystemVariables.h>

#include <konga/TransformationLogging/ViewLog.h>
#include <konga/TransformationLogging/ErrorLog.h>
#include <konga/TransformationLogging/ErrorConfiguration.h>

#include "ErrorCodes/resource.h"
#include "SystemVariableDefs.h"
#include "TreeMapper.h"
#include "crom.h"
#include "CROMData.h"
#include "DbAccess.h"
#include "DbWriter.h"


DbWriter::DbWriter(TreeMapper*pTreeMapper, const bool bIsTarget) :
	m_iMode(0), m_overrideModeChar(' '), m_sqlBuilder(0), m_bTarget(bIsTarget),
		m_bFlat(true), m_bIsOracle(false),
		m_pTreeMapper(pTreeMapper), m_conv(0), m_debug_log(0), m_Db(pTreeMapper)
{
	m_viewlog=pTreeMapper->getViewLog();
	m_errlog=pTreeMapper->getErrorLog();

	const char* szDriverName=bIsTarget?  m_pTreeMapper->getTargetDbDriver(): m_pTreeMapper->getSourceDbDriver();
	m_bIsOracle=konga::DriverIsOracle(szDriverName);

	m_conv = konga::Utf8Conv::createEncoderFor(konga::GetSystemVariable(ODBC_TARGET_ENC, m_pTreeMapper->getOperationDataElementHolder(), ""));
	m_sqlBuilder=new SqlForWrite_Builder(pTreeMapper, m_conv, m_bIsOracle, bIsTarget);

	konga::TransactionIsolationLevel lTxtIsolationLevel;
	int iTimeout;
	KString DSN, Username, Pwd, Driver, Server, DbName, ConnectString ;
	if(bIsTarget){
		lTxtIsolationLevel=m_pTreeMapper->m_lTargetTxnIsolationLevel;
		iTimeout=m_pTreeMapper->m_dwTgtTimeout;
		ConnectString=m_pTreeMapper->m_sTargetConnectStr;
	}
	else {
		lTxtIsolationLevel=m_pTreeMapper->m_lSourceTxnIsolationLevel;
		iTimeout=m_pTreeMapper->m_dwSrcTimeout;
		ConnectString=m_pTreeMapper->m_sSourceConnectStr;
		DSN=m_pTreeMapper->m_sSourceDsn;
	}

	                                                            
	if (m_Db.Connect(DSN, Username, Pwd, Driver, Server, DbName, ConnectString, iTimeout, lTxtIsolationLevel)==false)
	{
		const std::string stDSNNoPwd( konga::ReplacePassword((const char*)DSN, "<removed>") );
		const std::string stConnStrNoPwd( konga::ReplacePassword((const char*)ConnectString, "<removed>") );
		m_errlog->MakeError(__FILE__, __LINE__, ID_FAILED_DB_CONNECT, (const char*)m_Db.m_LastErr,
			stDSNNoPwd.c_str(), (const char*)Username, (const char*)Driver, (const char*)Server,
			(const char*)DbName, stConnStrNoPwd.c_str());
		throw konga::CrException((const char*)m_errlog->GetLastErr());
	}

	m_viewlog->DebugString("Create DbWriter - Connected to the target database \n" \
		"dsn = '%s' user = '%s', driver = '%s' server = '%s' dbname = '%s' connect string = '%s', timeout = '%d'\n",
		(const char*)DSN, (const char*)Username, (const char*)Driver, (const char*)Server, (const char*)DbName,
		konga::ReplacePassword((const char*)ConnectString,"<removed>").c_str(), iTimeout);
	                                                                                             
	                                

	
	m_bFlat = bIsTarget?pTreeMapper->IsFlatTarget():pTreeMapper->IsFlatSource();
	m_sBeginQuote=m_sqlBuilder->getBeginQuote();
	m_sEndQuote=m_sqlBuilder->getEndQuote();
	                                  
	if(bIsTarget)
		m_iMode=pTreeMapper->getDbInsertUpdateMode();
	else
		m_iMode=MODE_UPDATE;
	m_viewlog->DebugString("Create DbWriter for %s\nDriver = \"%s\", IsOracle=%s\n", 
		(bIsTarget?"Target":"Source"), szDriverName, (m_bIsOracle?"true":"false"));
	m_debug_log=0;

}

DbWriter::~DbWriter(){
	if(m_debug_log)fclose(m_debug_log);
	if(m_sqlBuilder)delete m_sqlBuilder;
	if(m_conv)delete m_conv;
}

void DbWriter::setDebugLog(const std::string& debug_log_file_name){
	m_debug_log=fopen(debug_log_file_name.c_str(),"w+");
}


                                                                                
                                                           
                                                                  
void DbWriter::fillColumnValues(const CROM*pTableNode,
								const std::vector<std::string>& value_list,
								const std::vector<char>& isNull_list)
{
	std::vector<std::string>::const_iterator it_value=value_list.begin();
	std::vector<char>::const_iterator it_isNull=isNull_list.begin();
	if(!m_bFlat){
		if(it_value==value_list.end() || it_isNull==isNull_list.end()){
			throw konga::CrException("Error in fillColumnValues, the size of value_list or null_list is too small");
		}
		it_value++;
		it_isNull++;
	}

	for(CROM*pChild=pTableNode->GetFirstChild(); pChild && !pChild->IsElement(); pChild=pChild->GetNextSibling()){
		if(!pChild->IsAttribute()||pChild->IsPrecondition())continue;
		if(it_value==value_list.end() || it_isNull==isNull_list.end()){
			throw konga::CrException(std::string("Error occured in fillColumnValues, the size of value_list or isNull_list is too small. TableName is ")+(const char*)pTableNode->GetDisplayName()); 
		}
		std::string value = *it_value;
		char isNull = *it_isNull;
		switch(isNull){
			case 't':
				pChild->set_Value("");
				pChild->SetNullFlag(true);
				pChild->setUnmapped(false);
				break;
			case 'u': 
				pChild->set_Value("");
				pChild->SetNullFlag(true);
				pChild->setUnmapped();
				break;
			default:
				pChild->set_Value(value.c_str());
				pChild->SetNullFlag(false);
				pChild->setUnmapped(false);
				break;
		}
		it_value++;
		it_isNull++;
	}
	if( it_isNull!=isNull_list.end() || it_value!=value_list.end()){
		throw konga::CrException(std::string("Error occured in fillColumnValues, the size of value_list or isNull_list is too large. TableName is ")+(const char*)pTableNode->GetDisplayName()); 
	}
}

void DbWriter::defineForeignKeyValues(CROM* pTableNode)
{
	CROM*pParent=pTableNode->GetParent();
	if(!pParent)return;
	for(CROM*pChild=pTableNode->GetFirstChild(); pChild && !pChild->IsElement(); pChild=pChild->GetNextSibling()){
		if(pChild->IsForeignKey()){
			if(pChild->IsNull()){
				const char* value=pChild->GetForeignKeyValue();
				if(value==0){
					KString sErr;
					sErr.Format("Internal error in defineForeignKeyValues(), the value of the linked PK is NULL. The linked primary key is in Table %s and the foreign key is %s.%s%s%s",
						pParent->get_sqlTableName(),
						pTableNode->get_sqlTableName(),
						m_sBeginQuote.c_str(), (const char*)pChild->GetDisplayName(), m_sEndQuote.c_str());
					throw konga::CrException((const char*)sErr);
				}
				pChild->SetValue(value);
				pChild->SetNullFlag(false);
			}
		}
	}
}

KString DbWriter::getWhereClauseForSequencePK(const CROM* pTable, const CROM* pPK) {
	KString sWhereClause;
	for(CROM* pChild = pTable->GetFirstChild(); pChild && !pChild->IsElement(); pChild=pChild->GetNextSibling())
	{
		if (!pChild->IsPrimaryKey() || pPK==pChild)continue;
		KString tmp;
		const KString& value(pChild->GetValue());
		if(pChild->IsNull()){
			tmp.Format("(%s%s%s IS NULL)",  m_sBeginQuote.c_str(), (const char*)pChild->GetDisplayName(), m_sEndQuote.c_str());
			if (!sWhereClause.IsEmpty())sWhereClause += " AND ";
			sWhereClause += tmp;
		}
		else {
			tmp.Format("(%s%s%s = '%s')",  m_sBeginQuote.c_str(), (const char*)pChild->GetDisplayName(), m_sEndQuote.c_str(),  konga::SqlEscape((const char*)value).c_str());
			if (!sWhereClause.IsEmpty())sWhereClause += " AND ";
			sWhereClause += tmp;
		}
	}
	return sWhereClause;
}

void DbWriter::preSqlExe(const CROM*pTableNode, CROM*& pIdentityField)
{
	pIdentityField=0;
	for(CROM* pChild=pTableNode->GetFirstChild(); pChild && !pChild->IsElement(); pChild=pChild->GetNextSibling())
	{
		if(!pChild->IsAttribute()||pChild->IsPrecondition())continue;
		                                                                                                                      
		if(pChild->isSqlIdentity()){
			pIdentityField = pChild;
			break;
		}
		if(pChild->isSqlNextVal()){
			int err;
			KString sql=pChild->getNextValSql();
			KString csID(m_Db.GetDbValue(sql, err));
			if (err != 0)
			{
				m_errlog->MakeError(__FILE__, __LINE__, ID_ENTERTODB_TARGET_NEXTVAL_ERROR, (const char*)sql, (const char*)m_Db.m_LastErr);
				throw konga::CrException((const char*)m_errlog->GetLastErr());
			}
			pChild->SetValue(csID);
			break;
		}
	}
}

void DbWriter::postSqlExe(const CROM* pTableNode, CROM* pIdentityField)
{
	                                                                                  
	                                                                                             
	                                                          

	if (!pIdentityField)return;

	assert(pTableNode);

	if(pIdentityField->isSqlCurrVal()){
		int err;
		KString sql(pIdentityField->getCurrValSql());
		m_viewlog->DebugString("sIdentSQL=\"%s\"\n", (const char*)sql);
		KString csID(m_Db.GetDbValue(sql, err));
		if (err != 0)
		{
			m_errlog->MakeError(__FILE__, __LINE__, ID_ENTERTODB_TARGET_CURRVAL_ERROR, (const char*)sql, (const char*)m_Db.m_LastErr);
			throw konga::CrException((const char*)m_errlog->GetLastErr());
		}
		pIdentityField->SetValue(csID);
		return;
	}


	const KString sSqlTableName( pTableNode->get_sqlTableName() );

	if(sSqlTableName.IsEmpty())return;
	KString sIdentSQL;

	                                                              
	                                                                   
	                                                                      
	                              
	sIdentSQL=m_pTreeMapper->getAutoSequenceSql((const char*)pTableNode->GetDisplayName(), (const char*)pIdentityField->GetDisplayName());
	if(sIdentSQL.IsEmpty()){
		m_errlog->MakeError(__FILE__, __LINE__, ID_POST_PROCESS_DB_ERR);
		throw konga::CrException((const char*)m_errlog->GetLastErr());
	}

	m_viewlog->DebugString("sIdentSQL=\"%s\"\n", (const char*)sIdentSQL);

	int err = 0;
	KString sIdentValue = m_Db.GetDbValue(sIdentSQL, err);
	if (!err)
	{
		m_viewlog->DebugString("sIdentSQL=\"%s\"\n", (const char*)sIdentSQL);
		pIdentityField->SetValue(sIdentValue);
		pIdentityField->SetNullFlag(false);
		return;
	}
}

void DbWriter::debugLogPrepareStatement(const char*title, const char* sql, const std::vector<CROM*>*croms)
{
	if ( m_debug_log ) {
		fprintf(m_debug_log, "%s SQL = '%s'\n", title, sql);
		if(croms && !croms->empty()){
			fprintf(m_debug_log, "%s Parameters = ", title);
			for(std::vector<CROM*>::const_iterator cit1 = croms->begin();
				cit1 != croms->end(); ++cit1) {
				CROM*pColumn = *cit1;
				fprintf(m_debug_log, "'%s', ", (const char*)pColumn->GetValue());
			}
		}
	}
}

KString DbWriter::doDbCountRecords(const KString& sql)
{
	std::auto_ptr<konga::EnterAccumulativeScope> scope_profiling(
		m_pTreeMapper->m_pProfilingKit == 0 ? 0 :
		new konga::EnterAccumulativeScope(m_pTreeMapper->m_pProfilingKit, "InternalReentrantScopes", "doDbCountRecords")
		);

	if ( !sql.IsEmpty() ) {
		int err = 0;
		m_viewlog->DebugString("Checking if record exists: %s", (const char*)sql);
		KString csCount(m_Db.GetDbValue(sql, err));
		if (err != 0)
		{
			m_errlog->MakeError(__FILE__, __LINE__, ID_ENTERTODB_INSERTUPDATEMODE_COUNT_FAILED, (const char*)sql, (const char*)m_Db.m_LastErr);
			throw konga::CrException((const char*)m_errlog->GetLastErr());
		}
		return csCount;
	}
	else {
		return "0";
	}
}


bool DbWriter::doDbInsertRecord(const KString& insertSql, const char* desciption, const std::vector<CROM*>&insertCroms, bool&bErroccurred)
{
	std::auto_ptr<konga::EnterAccumulativeScope> scope_profiling(
		m_pTreeMapper->m_pProfilingKit == 0 ? 0 :
		new konga::EnterAccumulativeScope(m_pTreeMapper->m_pProfilingKit, "InternalReentrantScopes", "doDbInsertRecord")
		);

	bool bRetVal;
	                                    
	if( !insertCroms.empty() ) {
		debugLogPrepareStatement(desciption, insertSql, &insertCroms);
		bRetVal = m_Db.ExecSqlWithLongText(insertSql, insertCroms, m_bIsOracle, m_conv);
	}
	else {
		debugLogPrepareStatement(desciption, insertSql, 0);
		bRetVal = m_Db.ExecuteSQL(insertSql);
	}

	if (!bRetVal) {
		bErroccurred = true;
		KString csErr = " : SQL = " + insertSql + "\n" + m_Db.m_LastErr + "\n" + m_Db.m_message;
		m_errlog->MakeError(__FILE__, __LINE__, IDS_ODBC_ERROR, desciption, (const char*)csErr);
		throw konga::CrException((const char*)m_errlog->GetLastErr());
	}
	return bRetVal;
}

bool DbWriter::doDbUpdateRecord(const KString& updateSql, const char*desciption, const std::vector<CROM*>& updateCroms, bool& bErroccurred)
{
	std::auto_ptr<konga::EnterAccumulativeScope> scope_profiling(
		m_pTreeMapper->m_pProfilingKit == 0 ? 0 :
		new konga::EnterAccumulativeScope(m_pTreeMapper->m_pProfilingKit, "InternalReentrantScopes", "doDbUpdateRecord")
		);

	bool bRetVal;
	if( !updateCroms.empty() ) {
		debugLogPrepareStatement(desciption, updateSql, &updateCroms);
		bRetVal = m_Db.ExecSqlWithLongText(updateSql, updateCroms, m_bIsOracle, m_conv);
	}
	else {
		debugLogPrepareStatement(desciption, updateSql, 0);
		bRetVal = m_Db.ExecuteSQL(updateSql);
	}

	if (!bRetVal) {
		bErroccurred = true;
		KString csErr = " : SQL = " + updateSql + "\n" + m_Db.m_LastErr + "\n";
		m_errlog->MakeError(__FILE__, __LINE__, ID_ENTERTODB_INSERTUPDATEMODE_UPDATESQL_EXEC_FAILED, desciption, (const char*)csErr, (const char*)(updateSql+m_Db.m_BindingValueList), (const char*)m_Db.m_message);
		throw konga::CrException((const char*)m_errlog->GetLastErr());
	} 
	else if (m_Db.GetNumRowsAffected() == 0) {
		const std::string& optionName = konga::ErrorConfiguration::NO_ROWS_AFFECTED_ON_DBUPDATES;
		if (konga::ErrorConfiguration::isWarning(optionName)) {
			m_errlog->MakeError(__FILE__, __LINE__, ID_ENTERTODB_UPDATE_ZERO_ROWS_AFFECTED, desciption, (const char*)(updateSql+m_Db.m_BindingValueList));
		}
		else if (konga::ErrorConfiguration::isError(optionName)) {
			bErroccurred = true;
			m_errlog->MakeError(__FILE__, __LINE__, ID_ENTERTODB_UPDATE_ZERO_ROWS_AFFECTED, desciption, (const char*)(updateSql+m_Db.m_BindingValueList));
			KString err="This update did not change the database: SQL - " + (updateSql+m_Db.m_BindingValueList);
			throw konga::CrException((const char*)err);
		}
	}
	return bRetVal;
}

bool DbWriter::doDbDeleteRecord(const KString& deleteSql, bool&bErroccurred)
{
	bool bRetVal = false;
	if (deleteSql.IsEmpty()) {
		m_errlog->MakeError(__FILE__, __LINE__, ID_ENTERTODB_DELETEMODE_DELETESQL_EMPTY);
	}
	else {
		debugLogPrepareStatement("DoDelete", deleteSql, 0);
		bRetVal = m_Db.ExecuteSQL(deleteSql);
	}

	if ( !bRetVal ) {
		bErroccurred = true;
		m_errlog->MakeError(__FILE__, __LINE__, ID_ENTERTODB_DELETEMODE_DELETE_EXEC_FAILED, (const char*)deleteSql, (const char*)m_Db.m_LastErr);
		throw konga::CrException((const char*)m_errlog->GetLastErr());
	}
	return bRetVal;
}

void DbWriter::TextDoc2DB(const std::string& srcDatFile, CROM* pCromRoot, bool &bErroccurred, int& iSuccessOperations, const std::string& pre_sql)
{
	if(pre_sql.empty()==false){
		bool ret=m_Db.ExecuteSQL(pre_sql.c_str());
		if(ret==false){
			throw konga::CrException(std::string("Execute Pre SQL failed, error:")+(const char*)m_Db.m_LastErr);
		}
	}
	std::string backupFileName=srcDatFile+".bak";
	if(konga::FileExists(srcDatFile)==false){
		std::string errMsg=std::string("The source file '")+srcDatFile+"' does not exist.";
		m_viewlog->LogMessage("%s", errMsg.c_str());
		throw konga::CrException(errMsg);
	}
	konga::kMoveFile(srcDatFile, backupFileName, true);
	konga::FileContents fc(backupFileName);
	size_t len=fc.size();

	                               
	if(len==0)
	{
		std::string errMsg=std::string("The source file '")+srcDatFile+"' is empty.";
		m_viewlog->LogMessage("%s", errMsg.c_str());
		throw konga::CrException(errMsg);
	}

	CROM* pTableNode=pCromRoot->GetFirstChild();
	if(!m_bTarget){
		assert(m_bFlat);
		for(CROM*pAttr=pTableNode->GetFirstChild(), *next; pAttr; pAttr=next){
			next=pAttr->GetNextSibling();
			if(!(pAttr->IsPrimaryKey()||pAttr->IsUpdateKey()) && !pAttr->IsSourceUpdateColumn())pAttr->DeleteNode();
		}
	}
	std::vector<std::string> value_list;
	std::vector<char> isNull_list;
	CROM* pIdentityField=0;
	KString updateWhere;

	FILE* out1=fopen(srcDatFile.c_str(),"w");
	if(out1==0){
		long lErr=::GetLastError();
		throw konga::CrException(std::string("Write to target database failed, cannot open the output file. outputFileName=\"")+srcDatFile+"\"\nReason: "+ErrorResolver().Resolve(lErr));
	}
	konga::AutoFileHandle adopt_out1(out1);

	size_t pos2=0;
	for(size_t pos1=0; pos1<len; pos1=pos2){
		int c;
		while((c=fc.at(pos1))=='\r' || c=='\n')pos1++;
		pos2=readDbLine(fc, pos1, value_list, isNull_list)+1;
		if(value_list.size()==0)continue;
		if(!m_bFlat)pTableNode=pTableNode->findSegment(value_list.at(0));
		fillColumnValues(pTableNode, value_list, isNull_list);
		if(m_bTarget){
			defineForeignKeyValues(pTableNode);
			preSqlExe(pTableNode, pIdentityField);
		}
		enterRecord(pTableNode, bErroccurred, pIdentityField);
		++iSuccessOperations;
		if(pIdentityField)postSqlExe(pTableNode, pIdentityField);
		writeCromElement(out1, pTableNode, m_bFlat, true);
	}
}


                                                                               
                                                                 
bool DbWriter::enterRecord(CROM* pCrom, bool& bErroccurred, CROM*& pIdentityField)
{
	KString sqlTableName(pCrom->get_sqlTableName());

	unsigned long hr = S_OK;
	if (sqlTableName.IsEmpty()) {
		bErroccurred = true;
		hr = m_errlog->MakeError(__FILE__, __LINE__, ID_ENTERTODB_NO_TABLE_NAME_AVAILABLE);
		throw konga::CrException((const char*)m_errlog->GetLastErr());
	}

	m_sqlBuilder->buildSqlForTarget(pCrom, pIdentityField, m_overrideModeChar);

	if(m_iMode == MODE_INSERT || m_iMode == MODE_INSERT_UPDATE) {
		if(m_debug_log)debugLogPrepareStatement("Insert", m_sqlBuilder->getInsertSql(), &m_sqlBuilder->getInsertCroms());
	}
	KString whereClause;
	if(m_iMode == MODE_UPDATE || m_iMode == MODE_INSERT_UPDATE) {
		const KString& updateSql(m_sqlBuilder->getUpdateSql());
		if(m_debug_log)debugLogPrepareStatement("Update", updateSql, &m_sqlBuilder->getUpdateCroms());
		int iWherePos=updateSql.Find(" WHERE ");
		if(iWherePos>0)whereClause=updateSql.Mid(iWherePos+1);
	}
	if(m_iMode == MODE_DELETE) {
		if(m_debug_log)debugLogPrepareStatement("Delete", m_sqlBuilder->getDeleteSql(), 0);
	}

	bool bHasUpdateValues = !m_sqlBuilder->getUpdateSql().IsEmpty();

	if ( !bHasUpdateValues && m_viewlog->IsDebugLogEnabled() ) {
		m_viewlog->DebugString("Target has no values to update.");
	}

	bool bRetVal = false;
	switch(m_iMode) {
		case MODE_INSERT_UPDATE:
			if (doDbCountRecords(m_sqlBuilder->getCountSql()) == "0") {
				m_viewlog->DebugString("The record does not exist --> INSERT");
				const KString& insertSql(m_sqlBuilder->getInsertSql());
				if (insertSql.IsEmpty()) {
					m_errlog->MakeError(__FILE__, __LINE__, ID_ENTERTODB_INSERTMODE_INSERTSQL_EMPTY, "Insert/Update");
					return false;
				}
				bRetVal=doDbInsertRecord(insertSql, "Insert/Update", m_sqlBuilder->getInsertCroms(), bErroccurred);
			}
			else {
				m_viewlog->DebugString("The record exists --> UPDATE");
				const KString& updateSql(m_sqlBuilder->getUpdateSql());
				if ( bHasUpdateValues ) {
					if (updateSql.IsEmpty()) {
						m_errlog->MakeError(__FILE__, __LINE__, ID_ENTERTODB_INSERTUPDATEMODE_UPDATESQL_EMPTY);
						break;
					}
					                                                            
					                                                                         
					bRetVal=doDbUpdateRecord(updateSql, "Insert/Update", m_sqlBuilder->getUpdateCroms(), bErroccurred);
				}
				else {
					                                                                                                
					bRetVal = true;
				}
				pIdentityField=0;
				                                                                        
			}
			break;
		case MODE_INSERT:
			{
				const KString& insertSql(m_sqlBuilder->getInsertSql());
				if (insertSql.IsEmpty()) {
					m_errlog->MakeError(__FILE__, __LINE__, ID_ENTERTODB_INSERTMODE_INSERTSQL_EMPTY, "Insert");
					break;
				}
				bRetVal=doDbInsertRecord(insertSql, "Insert", m_sqlBuilder->getInsertCroms(), bErroccurred);
			}
			break;
		case MODE_UPDATE:
			                                    
			{
				const KString& updateSql(m_sqlBuilder->getUpdateSql());
				if (updateSql.IsEmpty()) {
					m_errlog->MakeError(__FILE__, __LINE__, ID_ENTERTODB_UPDATEMODE_UPDATESQL_EMPTY);
					break;
				}
				if ( bHasUpdateValues ) {
					bRetVal=doDbUpdateRecord(updateSql, "Update", m_sqlBuilder->getUpdateCroms(), bErroccurred);
				}
				else {
					bRetVal = true;
				}
				pIdentityField=0;
			}
			break;
		case MODE_UPDATE_INSERT:
			throw konga::CrException("Please use Insert/Update instead");
		case MODE_DELETE:
			                         
			bRetVal=doDbDeleteRecord(m_sqlBuilder->getDeleteSql(), bErroccurred);
			pIdentityField=0;
			break;
		default:
			bErroccurred = true;
			hr = m_errlog->MakeError(__FILE__, __LINE__, ID_ENTERTODB_INVALID_MODE, m_iMode);
			throw konga::CrException((const char*)m_errlog->GetLastErr());
			break;
	}
	
	return bRetVal;
}

const char DbWriter::cDelimiter=',';
const char DbWriter::cStrQual='"';

size_t DbWriter::readDbLine(konga::FileContents&fc, int pos1, std::vector<std::string>& value_list, std::vector<char>& isNull_list)
{
	value_list.clear();
	isNull_list.clear();

	size_t pos=pos1;

	m_overrideModeChar=' ';
	int c=0;
	if(fc.at(pos)=='<' && fc.at(pos+2)=='>' && (c=fc.at(pos+1))=='I'||c=='U'){
		m_overrideModeChar=c;
		pos+=3;
	}
	for(; (c=fc.at(pos))!=0; ++pos){                                                   
		if(fc.at(pos)=='<' && fc.at(pos+1)=='u' && fc.at(pos+2)=='>'){
			value_list.push_back("");
			isNull_list.push_back('u');
			pos+=3;
			c=fc.at(pos);
		}
		else {
			std::string tmp;
			size_t pos1;
			char isNull=' ';
			if(c==cStrQual){
				pos++;
				for(pos1=pos; (c=fc.at(pos1))!=cStrQual || fc.at(pos1+1)==cStrQual; pos1++){ 	                                                       
					                                        
					if(c==cStrQual){                                      
						pos1++;

						tmp+=fc.getStr(pos,pos1-pos);
						pos=pos1+1;
					}
				}
				if(pos1>pos)tmp+=fc.getStr(pos,pos1-pos);
				pos1++;
				c=fc.at(pos1);
			}
			else {
				for(pos1=pos; (c=fc.at(pos1))!=cDelimiter && c!='\n' && c!='\r' && c!=0; ++pos1){;}                                                              
				if(pos1>pos){
					tmp+=fc.getStr(pos,pos1-pos);
				}
				else {
					isNull='t';
				}
			}
			value_list.push_back(tmp);
			isNull_list.push_back(isNull);
			pos=pos1;
		}
		if(c==0 || c=='\n' || c=='\r')break;
	}
	return pos;
}

void DbWriter::writeCromElement(FILE*out, CROM* pTableNode, const bool bIsFlat, bool bPostSqlExe)
{
	bool started=false;
	CROM_DB::MODE mode=pTableNode->getDbMode();
	switch(mode){
		case CROM_DB::set_insert: fprintf(out,"%s", "<I>"); break;
		case CROM_DB::set_update: fprintf(out,"%s", "<U>"); break;
	}
	if(!bIsFlat){
		const KString& nodeName( pTableNode->GetName() );
		konga::writeCsvData(out, nodeName, cDelimiter, nodeName.GetAt(0)=='<');
		started=true;
	}
	for(CROM*pChild=pTableNode->GetFirstChild(); pChild && !pChild->IsElement(); pChild=pChild->GetNextSibling())
	{
		if(!pChild->IsAttribute()||pChild->IsPrecondition())continue;

		if(started)fputc(cDelimiter,out);
		else started=true;

		if(!bPostSqlExe){
			if(pChild->isUnmapped()){
				fprintf(out,"%s", "<u>");
				continue;
			}
		}

		if(!pChild->IsForeignKey() || bPostSqlExe){
			if(!pChild->IsNull()){
				const char*value=pChild->GetValue();
				konga::writeCsvData(out, value, cDelimiter, value[0]==0||value[0]=='<');
			}
		}
	}
	fputc('\n', out);
}
