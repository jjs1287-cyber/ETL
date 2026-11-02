/*
 * Copyright (c) 2010-2011 Jitterbit, Inc.
 *
 */
#ifdef WIN32
	#include <windows.h>
#endif
#include <sqltypes.h>
#include <sqlext.h>

#include <konga/Utils/CrException.h>
#include <konga/Utils/StringBuilder.h>
#include <konga/Utils/CsvUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DbUtils/Sql_ODBC_Ext.h>

#include <konga/TransformationLogging/ViewLog.h>
#include <konga/TransformationLogging/ErrorLog.h>

#include "ErrorCodes/resource.h"
#include "crom.h"
#include "OdbcColumn.h"
#include "OdbcSource.h"
#include "OdbcTable.h"

using std::string;
using std::vector;

#define MAXBUFLEN 2048
                    
                          
                           

OdbcTable::OdbcTable(OdbcSource*dbSource, CROM*pTable, SQLHDBC hConn) :
	m_dbSource(dbSource), m_pTable(pTable), m_debug_log(dbSource->m_debug_log)
{
	SQLRETURN rc=SQLAllocHandle(SQL_HANDLE_STMT, hConn, &m_stmtHandle);
	if(rc!=SQL_SUCCESS){
		throw konga::DbException("Create OdbcTable failed, can't allocate statement handle.");
	}

	m_columns=dbSource->getAttributes(pTable);
	CROM* fk_saved=0;
	for(std::vector<CROM*>::const_iterator it=m_columns->begin(); it!=m_columns->end(); ++it){
		CROM*column=*it;
		if(column->IsForeignKey()){
			m_foreignKeys.push_back(column);
			fk_saved=column;
		}
		else {
			int sqlDataType=column->get_SqlDataType();
			bool bindable=column->get_DbColSize()<=MAXBUFLEN && sqlDataType!=SQL_VARBINARY && sqlDataType!=SQL_LONGVARBINARY;
			if(bindable){
				m_bindableColumns.push_back(column);
			}
			else {
				m_notypeColumns.push_back(column);
			}
		}

	}
	                                                 
	if(m_bindableColumns.size()==0 && m_notypeColumns.size()==0){
			bool bindable=fk_saved->get_DbColSize()<=MAXBUFLEN;
			if(bindable){
				m_bindableColumns.push_back(fk_saved);
			}
			else {
				m_notypeColumns.push_back(fk_saved);
			}
	}

	m_sql=dbSource->buildSqlForTable(pTable, m_bindableColumns, m_notypeColumns, m_foreignKeys);

	rc = SQLPrepare(m_stmtHandle, (SQLCHAR*)m_sql.c_str(), SQL_NTS);
	if(rc != SQL_SUCCESS && rc != SQL_SUCCESS_WITH_INFO)
	{
		std::string sErr="SQL:"+m_sql+"\nError:"+konga::GetError(SQL_HANDLE_STMT, m_stmtHandle);
		getErrorLog()->MakeError(__FILE__, __LINE__, ID_SQL_PREPARE_STATEMENT_ERROR, sErr.c_str());
		KString err = getErrorLog()->GetLastErr();
		throw konga::DbException((const char*)err);
	}

	bindColumns();
	bindMarkers();
}

OdbcTable::~OdbcTable(){
	                                               
	konga::SQLFreeHandle_STMT(m_stmtHandle);

	for(std::vector<OdbcColumn*>::iterator it=m_odbcColumns.begin(); it!=m_odbcColumns.end(); ++it){
		delete *it;
	}
	for(std::vector<OdbcColumn*>::iterator it=m_markers.begin(); it!=m_markers.end(); ++it){
		delete *it;
	}
}

void OdbcTable::bindColumns(){
	assert(m_odbcColumns.empty());
	int iCol=1;
	for(std::vector<CROM*>::const_iterator it=m_bindableColumns.begin(); it!=m_bindableColumns.end(); ++it, ++iCol){
		CROM*column=*it;
		OdbcColumn* value=new OdbcColumn(this, column);
		m_odbcColumns.push_back(value);
		value->bindColumn(iCol);
	}
	for(std::vector<CROM*>::const_iterator it=m_notypeColumns.begin(); it!=m_notypeColumns.end(); ++it, ++iCol){
		CROM*column=*it;
		OdbcColumn* value=new OdbcColumn(this, column);
		m_odbcColumns.push_back(value);
		value->bindColumn(iCol);
	}
}

void OdbcTable::bindMarkers(){
	int iMarker=1;
	for(std::vector<CROM*>::const_iterator it=m_foreignKeys.begin(); it!=m_foreignKeys.end(); ++it, ++iMarker){
		CROM*fk=*it;
		OdbcColumn* value=new OdbcColumn(this, fk);
		m_markers.push_back(value);
		value->bindMarker(iMarker);
	}
}

CViewLog* OdbcTable::getViewLog(){
	return m_dbSource->getViewLog();
}

CErrorLog* OdbcTable::getErrorLog(){
	return m_dbSource->getErrorLog();
}

void OdbcTable::execute()
{
	SQLRETURN rc = SQLExecute(m_stmtHandle);
	if (SQLERR(rc)) 
	{
		                                                        
		konga::StringBuilder sb;
		string::size_type pos0=0, pos;
		for(vector<CROM*>::const_iterator it=m_foreignKeys.begin(); it != m_foreignKeys.end(); ++it)
		{
			pos = m_sql.find('?', pos0);
			if(pos!=string::npos)
			{
				CROM*fk=*it;
				sb.append(m_sql.substr(pos0, pos-pos0));
				sb.append(fk->GetValue());
				pos0=pos+1;
			}
		}
		if(pos0!=std::string::npos)sb.append(m_sql.substr(pos0));
		const string sql="SQL:"+m_sql+"\nError:"+sb.toString();
		const string sErr=sql+"\n"+konga::GetError(SQL_HANDLE_STMT, m_stmtHandle);
		getErrorLog()->MakeError(__FILE__, __LINE__, ID_SQL_EXECUTE_STMT_ERROR, sErr.c_str());
		throw konga::DbException((const char*)getErrorLog()->GetLastErr());
	}
}


SQLRETURN OdbcTable::fetch()
{
	return SQLFetch(m_stmtHandle);
}


bool OdbcTable::execute_with_params(){
	for(std::vector<OdbcColumn*>::const_iterator it=m_markers.begin(); it!=m_markers.end(); ++it){
		OdbcColumn*odbcColumn=*it;
		unsigned long hr=odbcColumn->setMarkerValue();
		if(hr!=0)return false;
	}
	SQLRETURN retcode;
	KString LastOp;

	SQLPOINTER pParamId;
	long lDataSize = 0;
	long lBytes = 0;
	char szBuffer[CHUNK_SIZE+1] = {0};
	std::string params;

	if(m_debug_log!=0){
		konga::StringBuilder sb;
		sb.append("execute_with_params() parameter value(s) = {");
		bool first=true;
		for(std::vector<CROM*>::const_iterator it=m_foreignKeys.begin(); it!=m_foreignKeys.end(); ++it, first=false){
			CROM*fk=*it;
			if(!first)sb.append(", ");
			std::string value((const char*)fk->GetValue());
			if(!fk->IsNull()){
				konga::qualifyCsvString(value, ',', value.size()==0);
				sb.append(value);
			}
		}
		sb.append('}');
		params=sb.toString();
		writeLog(params.c_str());
	}

	                  
	retcode = ::SQLExecute(m_stmtHandle);

	if (SQLERR(retcode)) 
	{
		std::string sErr="SQL:"+m_sql+"\nError:"+konga::GetError(SQL_HANDLE_STMT, m_stmtHandle) + "Params:"+ params;
		getErrorLog()->MakeError(__FILE__, __LINE__, ID_SQL_EXECUTE_STMT_ERROR, sErr.c_str());
		throw konga::DbException((const char*)getErrorLog()->GetLastErr());
	}

	if(retcode == SQL_NEED_DATA)
	{
		int iParamNum = 0;
		KString LastOp = " SQLParamData - First call";
		retcode = ::SQLParamData(m_stmtHandle, &pParamId);
		while(retcode == SQL_NEED_DATA)
		{
			lBytes = 0;
			lDataSize = (int)strlen(reinterpret_cast<const char*>(pParamId));

			while(lBytes + CHUNK_SIZE < lDataSize)
			{
				strncpy(szBuffer, static_cast<const char*>(pParamId) + lBytes, CHUNK_SIZE);
				szBuffer[CHUNK_SIZE] = 0;
				LastOp = " SQLPutData ";
				retcode = ::SQLPutData(m_stmtHandle, szBuffer, CHUNK_SIZE);
				if (SQLERR(retcode)) 
				{
					std::string sErr="SQL:"+m_sql+"\nError:"+konga::GetError(SQL_HANDLE_STMT, m_stmtHandle);
					getErrorLog()->MakeError(__FILE__, __LINE__, ID_SQL_PUTDATA_ERROR, sErr.c_str());
					throw konga::DbException((const char*)getErrorLog()->GetLastErr());
				}

				lBytes += CHUNK_SIZE;
			}

			                      
			strncpy(szBuffer, static_cast<const char*>(pParamId) + lBytes, lDataSize - lBytes);

			LastOp = " SQLPutData ";
			if (lDataSize)
				retcode = ::SQLPutData(m_stmtHandle, szBuffer, lDataSize - lBytes);
			else
				retcode = ::SQLPutData(m_stmtHandle, 0, SQL_NULL_DATA);

			if (SQLERR(retcode)) 
			{
				std::string sErr="SQL - Put Data failed\nSQL:" + m_sql+"\nError:"+konga::GetError(SQL_HANDLE_STMT, m_stmtHandle);
				getErrorLog()->MakeError(__FILE__, __LINE__, ID_SQL_PUTDATA_ERROR, sErr.c_str());
				throw konga::DbException((const char*)getErrorLog()->GetLastErr());
			}

			                                                                  
			                       
			LastOp.Format(" SQLParamData Param# = %d", iParamNum);
			retcode = ::SQLParamData(m_stmtHandle, &pParamId);
			iParamNum++;
		}
	}

	if (SQLERR(retcode)) 
	{
		                                                                           
		konga::StringBuilder sb;
		sb.append('\n');
		int i=0;
		for(std::vector<CROM*>::const_iterator it=m_foreignKeys.begin(); it!=m_foreignKeys.end(); ++it, ++i)
		{
			CROM*column=*it;
			const KString& value=column->GetValue();
			sb.append("Param #").append(i).append(" = '").append(value).append("', Len = ").append(value.GetLength()).append('\n');
		}
		std::string sErr="SQL - Param Data " + m_sql + sb.toString()+konga::GetError(SQL_HANDLE_STMT, m_stmtHandle);
		getErrorLog()->MakeError(__FILE__, __LINE__, ID_SQL_PARAMDATA_ERROR, sErr.c_str());
		throw konga::DbException((const char*)getErrorLog()->GetLastErr());
	}
	return true;
}

void OdbcTable::loadDbRecord(DbRecord*record, bool bFirstRow, konga::Utf8Conv* conv)
{
	int iCol=0;
	for(std::vector<OdbcColumn*>::const_iterator it=m_odbcColumns.begin(); it!=m_odbcColumns.end(); ++it, ++iCol){
		OdbcColumn*colValue=*it;
		KString value;
		bool bIsNull = false;
		                          
		int iErr = colValue->getColumnValue(iCol, value, bFirstRow, bIsNull, conv);
		colValue->saveColumnValue(value, bIsNull);
	}
	assert(record!=0);
	                                                                             
	for(std::vector<CROM*>::const_iterator it=m_columns->begin(); it!=m_columns->end(); ++it){
		CROM* column=*it;
		record->addColumn((const char*)column->GetValue(), column->IsNull());
	}
}

const char*OdbcTable::getDriverName(){
	return m_dbSource->getDriverName();
}

void OdbcTable::writeLog(const char* msg){
	m_dbSource->writeLog(msg);
}

unsigned long OdbcTable::CloseCursor(){
	                                                          
	SQLRETURN rc=SQL_SUCCESS;
	if(m_stmtHandle != 0)rc = SQLCloseCursor(m_stmtHandle);
	if(SQLERR(rc))
	{
		std::string sErr="SQL - SQLCloseCursor\nError:"+konga::GetError(SQL_HANDLE_STMT, m_stmtHandle);
		return getErrorLog()->MakeError(__FILE__, __LINE__, ID_SQL_CLOSECURSOR_ERROR, sErr.c_str());
	}
	return S_OK;
}

                        
                                                               
                                                                          
                                                                              
                                         
SQLRETURN OdbcTable::GetSQLState(char* sState)
{
	SQLSMALLINT MsgLen;
	return SQLGetDiagField(SQL_HANDLE_STMT, m_stmtHandle, 1, SQL_DIAG_SQLSTATE, sState, 6, &MsgLen);
}







