/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <cassert>
#include <map>
#include <vector>
#include <exception>

#ifdef WIN32
	#include <windows.h>
#endif
#include <sqltypes.h>
#include <sqlext.h>
#include <boost/scoped_array.hpp>

#include <konga/Utils/AutoPtrCleanup.h>		                                   
#include <konga/Utils/StringUtils.h>
#include <konga/Utils/Settings.h>		                      
#include <konga/Utils/Timing.h>			                         

#include <konga/CharacterEncodingConversion/TextUnicodeConversion.h>
#include <konga/CharacterEncodingConversion/Utf8Conv.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DbUtils/Sql_ODBC_Ext.h>

#include <konga/TransformationLogging/ErrorLog.h>

#include "DbWriter.h"
#include "TreeMapper.h"
#include "DbAccess.h"
#include "CromConstants.h"

using namespace konga;
using std::string;
using std::vector;
using std::map;

                         
                       
                 
                                                                                                 
                                      
  
CDbAccess::CDbAccess(TreeMapper*p) :
	m_NumRowsAffected(0), m_bConnectionOpened(false),
	m_hEnv(0), m_hConn(0), m_iTimeout(DEFAULT_ODBCTIMEOUT),
	m_pTreeMapper(p), m_pSQLPutDataBuffer(0)
{
	m_viewlog=m_pTreeMapper->getViewLog();
	m_errlog=m_pTreeMapper->getErrorLog();

	const bool bTransaction = getDbTargetTransactionFlag(*getTreeMapper()->getOperationDataElementHolder());

	m_bAutoCommit = !bTransaction;
	m_bDoCommit = bTransaction;
	m_bTransactionEnded = !bTransaction;

	                                                
	  
	RETCODE retcode = SQLAllocEnv(&m_hEnv);
	if (SQLERR(retcode))
	{
		GetError(SQL_HANDLE_ENV, m_hEnv, "- SQL Allocation environment.");
		return;
	}

	retcode = SQLAllocHandle(SQL_HANDLE_DBC, m_hEnv, &m_hConn);
	if (SQLERR(retcode))
	{
		GetError(SQL_HANDLE_DBC, m_hConn, "- SQL Alloc Handle");
		return;
	}
}

                        
                
                 
                                                                                                  
                       
  
CDbAccess::~CDbAccess()
{
	if( m_bConnectionOpened )
	{
		                                                
		Disconnect();
	}
	SQLFreeHandle(SQL_HANDLE_DBC, m_hConn);
	SQLFreeHandle(SQL_HANDLE_ENV, m_hEnv);

	if ( m_pSQLPutDataBuffer ) delete[] m_pSQLPutDataBuffer;
}

                                               
                                                                                                     
                                                                     
                                                          
bool CDbAccess::CommitTransaction()
{
	if( !m_bTransactionEnded )
	{
		RETCODE retcode;
		
		retcode = SQLEndTran(SQL_HANDLE_DBC, m_hConn, SQL_COMMIT);
		m_bTransactionEnded = true;
		                                                             
		if (SQLERR(retcode))
		{
			GetError(SQL_HANDLE_DBC, m_hConn, "- SQL End Transaction");
			return false;
		}
	}
	return true;
}


                                              
                                                                                                       
                                                          
bool CDbAccess::AbortTransaction()
{
	if( !m_bTransactionEnded )
	{
		RETCODE retcode;
		
		retcode = SQLEndTran(SQL_HANDLE_DBC, m_hConn, SQL_ROLLBACK);
		m_bTransactionEnded = true;
		                                                             
		if (SQLERR(retcode))
		{
			GetError(SQL_HANDLE_DBC, m_hConn, "SQL - Abort Transaction");
			return false;
		}
	}
	return true;
}

                     
                                             
                                                                   
                      
                                            
                                                              
  
bool CDbAccess::Connect(const KString& DSN_in, const KString& UserName_in, const KString& Passwd_in, const KString& Driver,
						const KString& Server, const KString& DbName, const KString& ConnectString_in, int iTimeout, konga::TransactionIsolationLevel txnIsolationLevel)
{
	RETCODE retcode = SQL_SUCCESS;

	KString ConnectString(ConnectString_in), DSN(DSN_in), UserName(UserName_in),Passwd(Passwd_in);
	m_iTimeout = iTimeout;

	assert(m_hEnv);
	assert(m_hConn);

	if ( txnIsolationLevel != konga::UNDEFINED ) {
		m_viewlog->DebugString("Setting TXN ISOLATION LEVEL = %d\n", (int)txnIsolationLevel);
		try {
			konga::SetTransactionIsolationLevel(m_hConn, txnIsolationLevel);
		}
		catch( const CrException& ex ) {
			m_LastErr += ex.GetErrorString();
			return false;
		}
	}

	                                                                                                        
	if (DSN_LESS_CONNECTION == DSN || CUSTOM_CONNECTION == DSN || DSN.IsEmpty())
	{
		                                                                                          
		if (ConnectString.IsEmpty())
		{
			m_LastErr += "CDbAccess::Connect - Error : Empty Connection String for a DSN less connection.";
			return false;
		}

		KString OutConnStr;
		SQLSMALLINT	StrLen2;

		retcode = SQLDriverConnect(m_hConn, 0, (UCHAR FAR *) ConnectString.GetBuffer((int)ConnectString.GetLength()), ConnectString.GetLength(),
										(UCHAR FAR *) OutConnStr.GetBuffer(1024), 1024, &StrLen2, SQL_DRIVER_NOPROMPT);
	}
	else
	{
		                                           
		retcode = SQLConnect(m_hConn, (UCHAR FAR *) DSN.GetBuffer(DSN.GetLength()), DSN.GetLength(),                                           
								(UCHAR FAR *) UserName.GetBuffer(UserName.GetLength()), (int)UserName.GetLength(),                
								(UCHAR FAR *) Passwd.GetBuffer(Passwd.GetLength()), (int)Passwd.GetLength());              
	}

	                                                             
	if (SQLERR(retcode))
	{
		GetError(SQL_HANDLE_DBC, m_hConn, "SQL Connect / SQL Driver Connect");
		return false;
	}

	                                                                                             
	retcode = SQLSetConnectAttr(m_hConn, SQL_ATTR_AUTOCOMMIT,
		reinterpret_cast<SQLPOINTER>(static_cast<SQLULEN>(m_bAutoCommit ? SQL_AUTOCOMMIT_ON : SQL_AUTOCOMMIT_OFF)), 0);
	if (SQLERR(retcode)) {
		m_bAutoCommit = true;
		retcode = SQL_SUCCESS;
	}

	                                              
	char szDriverName[350] = {0};
	if(SQLGetInfo(m_hConn, SQL_DRIVER_NAME, szDriverName, 350, 0) == SQL_SUCCESS)
	{
		konga::strupr(szDriverName);
		if(strstr(szDriverName, EXCEL_DRIVER))
			retcode = SQLSetConnectAttr(m_hConn, SQL_ACCESS_MODE, (SQLPOINTER) SQL_MODE_READ_WRITE, 0);
	}

	                                                     
	m_bConnectionOpened = true;

	return true;
}

namespace {
	const int STR_LEN = 256;
}

int	CDbAccess::GetColumnsInfo(const KString& sTableName_in, vector<CROM*>& aColNames)
{
	SQLRETURN retcode = SQL_SUCCESS;
	KString sTableName(sTableName_in);

	SQLHSTMT hstmt = 0;

	                                      
	if (::SQLAllocHandle(SQL_HANDLE_STMT, m_hConn, &hstmt)!=SQL_SUCCESS)
	{
		return -1;
	}

	char TabName[STR_LEN];

	                                         
	int iPos = sTableName.Find(":");
	if(iPos != -1)
	{
		char ScmName[STR_LEN];
		const KString sSchemaName(sTableName.Left(iPos));
		sTableName = sTableName.Mid(iPos+1);
		strcpy(TabName, (const char*)sTableName);
		strcpy(ScmName, (const char*)sSchemaName);
		retcode = ::SQLColumns(hstmt, 
							0, 0,                
							(unsigned char *) ScmName, SQL_NTS,
							(unsigned char *) TabName, SQL_NTS,         
							0, 0);                   
	}
	else
	{
		strcpy(TabName, sTableName);

		retcode = ::SQLColumns(hstmt, 
							0, 0,                     
							0, 0,                    
							(unsigned char *) TabName, SQL_NTS,                  
							0, 0);                   
	}

	int iNumCols = -1;
	if (retcode == SQL_SUCCESS || retcode == SQL_SUCCESS_WITH_INFO) 
	{

		                                                   
		SQLCHAR szColumnName[STR_LEN], szIsNullable[STR_LEN];
		SQLLEN cbColumnName = STR_LEN, cbIsNullable = STR_LEN;
		
		SQLSMALLINT DataType = 0;
		SQLLEN cbDataType = sizeof(SQLSMALLINT);

		SQLINTEGER DataSize = 0;
		SQLLEN cbDataSize = sizeof(SQLINTEGER);

		SQLSMALLINT Nullable = 0;
		SQLLEN cbNullable = sizeof(SQLSMALLINT);

	                                              
		retcode = ::SQLBindCol(hstmt, 4, SQL_C_CHAR, szColumnName, STR_LEN, &cbColumnName);	              
		retcode = ::SQLBindCol(hstmt, 5, SQL_C_SSHORT, &DataType, sizeof(DataType), &cbDataType);	                
		retcode = ::SQLBindCol(hstmt, 7, SQL_C_LONG, &DataSize, sizeof(DataSize), &cbDataSize);	              

		retcode = ::SQLBindCol(hstmt, 11, SQL_C_SSHORT, &Nullable, sizeof(Nullable), &cbNullable);	              

		                       
		if( GetOdbcVersion(hstmt) > 2.9 )
			retcode = ::SQLBindCol(hstmt, 18, SQL_C_CHAR, szIsNullable, STR_LEN, &cbIsNullable);
		else
			cbIsNullable = -1;

		iNumCols = 0;
		while(true) {
			retcode = ::SQLFetch(hstmt);
			if (retcode == SQL_ERROR || retcode == SQL_SUCCESS_WITH_INFO) {
				                
			}

			if (retcode == SQL_SUCCESS || retcode == SQL_SUCCESS_WITH_INFO){
				                                          
				if ( cbColumnName < 1 )
				   szColumnName[0] = 0;
				if ( cbIsNullable < 0 )
				   szIsNullable[0] = 0;
				KString tmp=sTableName + "." + (const char*)szColumnName;
				for ( vector<CROM*>::const_iterator it = aColNames.begin(); it != aColNames.end(); ++it )
				{
					CROM* aColName = *it;
					const char* szDisplayName=(const char*)aColName->GetDisplayName();
					if (stricmp(szDisplayName,(const char*)szColumnName)==0 ||
						stricmp((const char*)tmp, szDisplayName)==0)
					{
						                                                   
						aColName->set_SqlDataType(DataType);

						                                                            
						switch(DataType) {
							                   
							case SQL_CHAR:
							case SQL_VARCHAR:
								aColName->set_DbColSize(DataSize);
								break;
							default:
								aColName->set_DbColSize(0);
						};
						                                                                      
						                                            
						                                                                                   
						                                                                                   
						                                                                         
						                                                                                    
						                                                                    
						                                   
						                                               

						if ( Nullable == SQL_NULLABLE ) {
							                                          
							aColName->AddType(CROM_t_Optional);
						}
						else if(strncmp((const char*)szIsNullable, "NO", 2)==0) {
							                                             
							aColName->AddType(CROM_t_AtLeastOne);
						}
						else if ( Nullable == SQL_NO_NULLS ) {
							                                                                                    
							aColName->AddType(CROM_t_AtLeastOne);
						}
						else if(strncmp((const char*)szIsNullable, "YES", 3)==0) {
							                                                                                               
							aColName->AddType(CROM_t_Optional);
						}
						else {
							                                       
							aColName->AddType(CROM_t_Optional);
						}

						break;
					}
				}
				iNumCols++;			 
			} 
			else
			{
				break;
			}
		}
	}

	                                                             
	if (SQLERR(retcode))
	{
		return -1;
	}

	return iNumCols;
}

bool CDbAccess::Disconnect()
{
	if( !m_bAutoCommit && !m_bTransactionEnded )
	{
		                                                                            
		                                                                                        
		if( m_bDoCommit )
			CommitTransaction();
		else
			AbortTransaction();
	}

	RETCODE retcode;

	retcode = SQLDisconnect(m_hConn);

	                                                             
	if (SQLERR(retcode))
	{
		GetError(SQL_HANDLE_DBC, m_hConn, "SQL - Disconnect");
		return false;
	}

	                                                               
	m_bConnectionOpened = false;
	return true;
}

namespace {
	struct ValueInfo {
		ValueInfo(void) : Size(0), IsBinary(false) {}
		ValueInfo(SQLLEN s, bool b) : Size(s), IsBinary(b) {}
		SQLLEN Size;
		bool IsBinary;
	};
}

bool CDbAccess::ExecSqlWithLongText(const KString& sStmt, const vector<CROM*>& paramCroms, bool bIsOracle, konga::Utf8Conv* conv)
{
	std::auto_ptr<konga::EnterAccumulativeScope> scope_profiling(
		m_pTreeMapper->m_pProfilingKit == 0 ? 0 :
		new konga::EnterAccumulativeScope(m_pTreeMapper->m_pProfilingKit, "InternalReentrantScopes", "ExecSqlWithLongText")
		);

	KString LastOp;
	SQLHSTMT hstmt = 0;
	RETCODE retcode = SQL_SUCCESS;

	SQLLEN lDataSize = 0;
	const bool bDebugLogging = m_viewlog->IsDebugLogEnabled();
	try
	{
		if(bDebugLogging)m_viewlog->DebugString("Executing = '%s'\n", (const char*)sStmt);
		                                      
		LastOp = " SQLAllocHandle ";
		if (::SQLAllocHandle(SQL_HANDLE_STMT, m_hConn, &hstmt)!=SQL_SUCCESS)
		{
			GetError(SQL_HANDLE_DBC, m_hConn, "SQL - Allocate Handle");
			return false;
		}
		
		LastOp = " SQLSetStmtAttr ";
		                                     
		::SQLSetStmtAttr(hstmt, SQL_ATTR_QUERY_TIMEOUT, reinterpret_cast<SQLPOINTER>(static_cast<SQLULEN>(m_iTimeout)), SQL_IS_UINTEGER);

		const int iNumToBind=paramCroms.size();
		SQLLEN* pTextSize = new SQLLEN[iNumToBind];
		konga::AutoArrayDelete<SQLLEN> adopt_pTextSize(pTextSize);

		map<SQLPOINTER, ValueInfo> valueSizeMap;

		vector<SQLCHAR*> garbage_collector;
		AutoArrayDeleteVector<SQLCHAR> adopt_garbage_collector(garbage_collector);

		                                
		for(int i = 0; i < iNumToBind; ++i)
		{
			CROM* pColumn=paramCroms.at(i);
			SQLSMALLINT SqlDataType = pColumn->get_SqlDataType();
			
			const KString& value = pColumn->GetValue();
			if(value.GetLength()>300000){
				m_message="The column \""+pColumn->GetDisplayName()+"\" value length "+ konga::toString(value.GetLength()).c_str()+" may be too long for ODBC to handle.";
			}
			if(bDebugLogging)m_viewlog->DebugString("Param %d = '%s', ColType = %d\n", i, (const char*)value, SqlDataType);
			LastOp = " SQLBindParameter ";

			if ( konga::IsBinaryData(SqlDataType) ) {
				size_t len = 0;
				SQLCHAR* pDataPtr = konga::fromHexStringPtr((const char*)value, len);
				garbage_collector.push_back(pDataPtr);
				lDataSize = len;
				pTextSize[i] = lDataSize;

				valueSizeMap[pDataPtr]=ValueInfo(lDataSize, true);
				retcode = ::SQLBindParameter(hstmt,
											i + 1,				                   
											SQL_PARAM_INPUT,	                    
											SQL_C_BINARY,		                               
											SqlDataType,		                                 
											lDataSize,			                   
											0,
											(SQLPOINTER)pDataPtr,		                                 
											lDataSize,
											&pTextSize[i]);
			}
			else if(konga::IsWideCharData(SqlDataType)){
				string utf8((const char*)value);
				konga::TextUnicodeConversion TUC;
				std::wstring utf16 =TUC.ConvertUtf8ToUtf16 (utf8);
				long size=utf16.size()*2;
				SQLCHAR* pDataPtr=(SQLCHAR*)(new char[size+2]);
				::memcpy(pDataPtr, utf16.c_str(),size+2);
				garbage_collector.push_back(pDataPtr);
				lDataSize=utf16.size();
				pTextSize[i] = lDataSize*2;

				valueSizeMap[pDataPtr]=ValueInfo(lDataSize, true);
				retcode = ::SQLBindParameter(hstmt,
											i + 1,				                   
											SQL_PARAM_INPUT,	                    
											SQL_C_WCHAR,			                               
											SqlDataType,		                                 
											lDataSize,			                   
											0,
											pDataPtr,		                                 
											lDataSize*2,
											&pTextSize[i]);
			}
			else {
				                                      
				                                     
				const string output( conv!=0 ? conv->to((const char*)value) : (const char*)value);
				lDataSize=output.size();
				unsigned char* pDataPtr=new unsigned char[lDataSize+2];
				::memcpy(pDataPtr,output.c_str(),lDataSize+1);

				                                                              
				                                            
				                                                                             
				pTextSize[i] = SQL_LEN_DATA_AT_EXEC(lDataSize);

				valueSizeMap[pDataPtr]=ValueInfo(lDataSize, false);
				if(bIsOracle && (SqlDataType == SQL_UNKNOWN_TYPE || SqlDataType == SQL_LONGVARBINARY))	                   
					SqlDataType = SQL_CHAR;
				retcode = ::SQLBindParameter(hstmt,
											i + 1,				                   
											SQL_PARAM_INPUT,	                    
											SQL_C_CHAR,			                               
											SqlDataType,		                                 
											lDataSize,			                   
											0,
											pDataPtr,		                                 
											0,
											&pTextSize[i]);
			}
			if (SQLERR(retcode)) 
			{
				GetError(SQL_HANDLE_STMT, hstmt, " SQL - Bind Parameters " + sStmt);
				LastOp = " SQLFreeHandle ";
				::SQLFreeHandle(SQL_HANDLE_STMT, hstmt);
				return false;
			}
		}

		                   
		LastOp = " SQLExecDirect ";
		KString stmt(sStmt);	                                                                                
		retcode = ::SQLExecDirect(hstmt, (SQLCHAR*)stmt.GetBuffer(stmt.GetLength()), stmt.GetLength());
		if (SQLERR(retcode)) 
		{
			GetError(SQL_HANDLE_STMT, hstmt, " SQL - ExecDirect = " + stmt);
			LastOp = " SQLFreeHandle ";
			::SQLFreeHandle(SQL_HANDLE_STMT, hstmt);
			return false;
		}

		if(retcode == SQL_NEED_DATA ) {
			if ( m_pSQLPutDataBuffer == 0 ) {
				m_pSQLPutDataBuffer = new char[CHUNK_SIZE+1];
			}
			m_pSQLPutDataBuffer[0] = 0;
			int iParamNum = 0;
			SQLPOINTER pParamId = 0;
			LastOp.Format(" SQLParamData Param# = %d ", iParamNum);
			while((retcode=SQLParamData(hstmt, &pParamId)) == SQL_NEED_DATA)
			{
				long lBytes = 0;
				lDataSize = -1;
				bool isBinary;
				if ( pParamId != 0 ) {
					const map<SQLPOINTER, ValueInfo>::const_iterator param = valueSizeMap.find(pParamId);
					if ( param != valueSizeMap.end() ) {
						lDataSize = param->second.Size;
						isBinary = param->second.IsBinary;
					}
				}
				if ( lDataSize < 0 ) {
					                                                           
					lDataSize = pParamId?strlen(reinterpret_cast<const char*>(pParamId)):0;
					isBinary = false;
				}

				while(lBytes + CHUNK_SIZE < lDataSize)
				{
					strncpy(m_pSQLPutDataBuffer, static_cast<const char*>(pParamId) + lBytes, CHUNK_SIZE);
					m_pSQLPutDataBuffer[CHUNK_SIZE] = 0;
					LastOp = " SQLPutData ";
					retcode = ::SQLPutData(hstmt, m_pSQLPutDataBuffer, isBinary ? CHUNK_SIZE : SQL_NTS);
					if (SQLERR(retcode)) 
					{
						GetError(SQL_HANDLE_STMT, hstmt, "SQL - Put Data " + stmt);
						LastOp = " SQLFreeHandle ";
						::SQLFreeHandle(SQL_HANDLE_STMT, hstmt);
						return false;
					}

					lBytes += CHUNK_SIZE;
				}

				                      
				if ( pParamId != 0 && lDataSize > lBytes ) {
					strncpy(m_pSQLPutDataBuffer, static_cast<const char*>(pParamId) + lBytes, lDataSize - lBytes);
					m_pSQLPutDataBuffer[lDataSize - lBytes] = 0;
				}

				LastOp = " SQLPutData ";
				                                                                                                                  
				                                                                                                              
				if (lDataSize) {
					retcode = ::SQLPutData(hstmt, m_pSQLPutDataBuffer, isBinary ? lDataSize - lBytes : SQL_NTS);
				}
				else {
					retcode = ::SQLPutData(hstmt, 0, SQL_NULL_DATA);
				}
				if( retcode == SQL_SUCCESS_WITH_INFO ) {
					if(bDebugLogging) {
						const string& warning = konga::GetError(SQL_HANDLE_STMT, hstmt);
						m_viewlog->DebugString("SQLPutData returned SQL_SUCCESS_WITH_INFO for the last chunk. This error is ignored: %s", warning.c_str());
					}
				}
				else if ( !SQL_SUCCEEDED(retcode) ) {
					GetError(SQL_HANDLE_STMT, hstmt, "SQL - Put Data " + stmt);
					LastOp = " SQLFreeHandle ";
					::SQLFreeHandle(SQL_HANDLE_STMT, hstmt);
					return false;
				}

				LastOp.Format(" SQLParamData Param# = %d ", iParamNum);
				iParamNum++;
			}

			if (SQLERR(retcode)) 
			{
				                                                                           
				KString sParams("\n");
				m_BindingValueList="\n";

				for(int i = 0; i < iNumToBind; i++)
				{
					const CROM* const pColumn=paramCroms.at(i);
					const KString& value = pColumn->GetValue();
					const int lValueSize = strlen(value);

					KString sTemp1, sTemp2;
					if(lValueSize<=200){
						sTemp1.Format("Param #%d = '%s'\n",i, (const char*)value);
						sTemp2.Format("Param #%d = '%s', Len = %d\n", i, (const char*)value, lValueSize);
					}
					else {
						const std::string stValue(value, 197);
						sTemp1.Format("Param #%d = '%s...'\n",i, stValue.c_str());
						sTemp2.Format("Param #%d = '%s', Len = %d\n", i, stValue.c_str(), lValueSize);
					}
					m_BindingValueList += sTemp1;
					sParams += sTemp2;
				}
				GetError(SQL_HANDLE_STMT, hstmt, "SQL - Param Data " + stmt + sParams);
				LastOp = " SQLFreeHandle ";
				::SQLFreeHandle(SQL_HANDLE_STMT, hstmt);
				return false;
			}
		}

		                                                                      
		LastOp = " SQLFreeHandle ";
		if(GetRowsAffected(hstmt)==0){
			m_BindingValueList="\n";
			for(int i = 0; i < iNumToBind; i++)
			{
				const CROM* const pColumn=paramCroms.at(i);
				const KString& value = pColumn->GetValue();
				const int lValueSize = strlen(value);

				KString sTemp;
				if(lValueSize<=200){
					sTemp.Format("Param #%d = '%s'\n",i, (const char*)value);
				}
				else {
					std::string stValue(value, 197);
					sTemp.Format("Param #%d = '%s...'\n",i, stValue.c_str());
				}
				m_BindingValueList+=sTemp;
			}
		}

		konga::SQLFreeHandle_STMT(hstmt);
		return true;
	}
	catch(const konga::CrException& e)
	{
		const KString msg("Unexpected CrException in CDbAccess::ExecSqlWithLongText - Called " + LastOp + "\n%s");
		if(bDebugLogging)m_viewlog->DebugString((const char*)msg, e.GetErrorString().c_str());
		::SQLFreeHandle(SQL_HANDLE_STMT, hstmt);
		throw e;
	}
	catch(const std::exception& e)
	{
		m_LastErr = " Unexpected exception in CDbAccess::ExecSqlWithLongText - Called " + LastOp;
		m_LastErr += e.what();
		if(bDebugLogging)m_viewlog->DebugString("%s\n", (const char*)m_LastErr);
		::SQLFreeHandle(SQL_HANDLE_STMT, hstmt);
		return false;
	}
#ifndef _DEBUG
	catch(...)
	{
		m_LastErr = " Unhandled exception in CDbAccess::ExecSqlWithLongText - Called " + LastOp;
		if(bDebugLogging)m_viewlog->DebugString("%s\n", (const char*)m_LastErr);
		::SQLFreeHandle(SQL_HANDLE_STMT, hstmt);
		return false;
	}
#endif
}

                        
                                                
                                                       
                                           
  
bool CDbAccess::ExecuteSQL(KString Stmt)
{
	std::auto_ptr<konga::EnterAccumulativeScope> scope_profiling(
		m_pTreeMapper->m_pProfilingKit == 0 ? 0 :
		new konga::EnterAccumulativeScope(m_pTreeMapper->m_pProfilingKit, "InternalReentrantScopes", "ExecuteSQL")
		);

	                                      
	SQLHSTMT hstmt;	
	if (SQLAllocHandle(SQL_HANDLE_STMT, m_hConn, &hstmt)!=SQL_SUCCESS)
	{
		GetError(SQL_HANDLE_DBC, m_hConn, "SQL - Allocate Handle");
		return false;
	}
	
	                                     
	RETCODE retcode = SQLSetStmtAttr(hstmt, SQL_ATTR_QUERY_TIMEOUT, reinterpret_cast<SQLPOINTER>(static_cast<SQLULEN>(m_iTimeout)), SQL_IS_UINTEGER);

	                   
	retcode = SQLExecDirect(hstmt, (SQLCHAR*) Stmt.GetBuffer(Stmt.GetLength()), Stmt.GetLength());
	if (SQLERR(retcode)) 
	{
		GetError(SQL_HANDLE_STMT, hstmt, "SQL Exec Direct = " + Stmt);
		SQLFreeHandle(SQL_HANDLE_STMT, hstmt);
		return false;
	}

	                                                                      
	GetRowsAffected(hstmt);
	konga::SQLFreeHandle_STMT ( hstmt);
	return true;
}

SQLLEN	 CDbAccess::GetRowsAffected(SQLHSTMT stmt)
{
	RETCODE retVal;
	retVal = SQLRowCount(stmt, &m_NumRowsAffected);
	if (SQLERR(retVal))
		return -1;
	return m_NumRowsAffected;
}

                      
                                               
                               
                 
                                                                                                   
                                                                   
  
#define MAXBUFLEN 256
void CDbAccess::GetError(SQLSMALLINT plm_handle_type, SQLHANDLE plm_handle, const KString& sOperation)
{
	KString Msg;
    RETCODE        plm_retcode = SQL_SUCCESS;
    UCHAR        plm_szSqlState[MAXBUFLEN] = "", plm_szErrorMsg[MAXBUFLEN] = "";
    SDWORD        plm_pfNativeError = 0L;
    SWORD        plm_pcbErrorMsg = 0;
    SQLSMALLINT    plm_cRecNmbr = 1;

    plm_retcode = SQLGetDiagRec(plm_handle_type, plm_handle,
        plm_cRecNmbr, plm_szSqlState, &plm_pfNativeError,
        plm_szErrorMsg, MAXBUFLEN - 1, &plm_pcbErrorMsg);

    KString tmp=(const char*)plm_szErrorMsg + KString("\n") + sOperation;
	if (m_viewlog->IsDebugLogEnabled())
	{
		m_viewlog->DebugString("Database Error : '%s' '%d'\n'%s'\n", (const char*)plm_szSqlState, plm_pfNativeError ,(const char*)tmp);
	}

	m_LastErr.Format("SqlState = %s NativeError = %d\n%s\n", (const char*)plm_szSqlState, plm_pfNativeError ,(const char*)tmp);
	m_SqlState = (const char*)plm_szSqlState;
}

KString CDbAccess::GetDbValue(const KString& sSQL_in, int& err)
{
	if(sSQL_in.IsEmpty()) {
		err=-1;
		return "";
	}
	err = 0;

	                                      
	SQLHSTMT hstmt = 0;
	if (SQLAllocHandle(SQL_HANDLE_STMT, m_hConn, &hstmt)!=SQL_SUCCESS) {
		GetError(SQL_HANDLE_DBC, m_hConn, "SQL - Allocate Handle");
		err = 1;
		return "";
	}
	
	                                     
	SQLSetStmtAttr(hstmt, SQL_ATTR_QUERY_TIMEOUT, reinterpret_cast<SQLPOINTER>(static_cast<SQLULEN>(m_iTimeout)), SQL_IS_UINTEGER);

	                   
	KString sSQL(sSQL_in);
	RETCODE retcode = SQLExecDirect(hstmt, (SQLCHAR*) sSQL.GetBuffer(sSQL.GetLength()), sSQL.GetLength());
	if (SQLERR(retcode)) {
		GetError(SQL_HANDLE_STMT, hstmt, "SQL Exec Direct = " + sSQL);
		SQLFreeHandle(SQL_HANDLE_STMT, hstmt);
		err = 1;
		return "";
	}

	short nFieldIndex = 1;
	KString sStrVal;
	SQLLEN nActualSize = 100;

	retcode = ::SQLFetch(hstmt);
	if (SQLERR(retcode)) 
	{
		GetError(SQL_HANDLE_STMT, hstmt, "SQLGetData" );
		SQLFreeHandle(SQL_HANDLE_STMT, hstmt);
		err = 1;
		return "";
	}
	
	                                                          
	char buff[100];
	retcode = ::SQLGetData(hstmt, nFieldIndex , SQL_CHAR, (SQLPOINTER)buff, 100, &nActualSize);
	if (SQLERR(retcode)) {
		GetError(SQL_HANDLE_STMT, hstmt, "SQLGetData" );
		SQLFreeHandle(SQL_HANDLE_STMT, hstmt);
		err = 1;
		return "";
	}
	sStrVal=buff;
	konga::SQLFreeHandle_STMT(hstmt);
	return sStrVal;
}

