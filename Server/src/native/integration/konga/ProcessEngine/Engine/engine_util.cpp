/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */

#ifdef WIN32
	#include <windows.h>
	#ifdef kDeleteFile
	#undef kDeleteFile
	#endif
#endif         

#include <string>			              
#include <string.h>

#include <sqltypes.h>
#include <sqlucode.h>

#include <konga/Utils/AutoCleanup.h>
#include <konga/Utils/CrException.h>
#include <konga/Utils/ErrorResolver.h>	                      
#include <konga/Utils/FileUtils.h>
#include <konga/Utils/Settings.h>
#include <konga/Utils/DBConnectionInfo.h>
#include <konga/Utils/StringUtils.h>

#include <konga/Db/IDb/TemporaryFileTab.h>	                              
#include <konga/Db/IDb/DataElementTab.h>	                            
#include <konga/Db/DbUtils/DynamicDbRecordSet.h>	                                  
#include <konga/Db/DbUtils/Sql_ODBC_Ext.h>

#include "EngineDebugUtils.h"	                 
#include "engine_util.h"

#include "SpCaller.h"		                 
#include "SpException.h"	                    

#include "KeyValues.h"		                                     

extern DbConnection* g_trandb;
extern std::set<long> g_slDistributionJobErrors;	                            

std::string g_temp_dir;

extern DBConnectionInfo g_mdb_connect;
extern DBConnectionInfo g_idb_connect;
extern DBConnectionInfo g_pdb_connect;

#ifdef WIN32
	                                   
#endif         

using namespace std;
using namespace konga;
using namespace engine_util;

bool StringContainsLetters(const char* route)
{
	int i=0;
	for(;route[i] && isdigit(route[i]) && route[i]!=';' && route[i]!='.';++i);
	return route[i] && route[i]!=';' && route[i]!='.' && !isdigit(route[i]);
}

  
                                       
              
     
              
     
                    
     
                    
  
                                         
  
int parse_route_string(char*& route, int& pid, int& piid, int& tid, int& tiid, std::string& proc_name, std::string& task_name)
{
	if( !route || !*route ) return 0;

	char *start = route;

	if( StringContainsLetters(route) )                                                                 
	{
		int k = findk(route,".;");
		route[k] = 0;
		proc_name = route;
		route += k+1;
	}
	else
	{
		pid = atoi( route );		                      
		route = strpbrk(route,".;");	                     
		if( route ) ++route; else return 0;
	}
	
	piid = atoi( route );		                      
	route = strpbrk(route,".;");	                     
	if( route ) ++route; else return 0;
	
	if( StringContainsLetters(route) )                                                                          
	{
		int k = findk(route,".;");
		route[k] = 0;
		task_name = route;
		route += k+1;
	}
	else
	{
		tid = atoi( route );			          
		route = strpbrk(route,".;");	                     
		if( route ) ++route; else return 0;
	}
	
	tiid = atoi( route );		                   
	route = strpbrk(route,".;");	                     
	if( route ) ++route; else return 0;

	return route-start;
}

int GetODBCTimeOut(int iDefault) {
	return konga::GetOptionAsInt("Settings", "ODBCTimeOut", iDefault);
}

  
                                                                           
                                                              
  
void ExecuteSql(const std::string& connection, const std::string& sql)
{
	DbConnection* pdb = konga::GetDbConnection(connection);
	pdb->SetQueryTimeout( GetODBCTimeOut() );
	try
	{
		pdb->ExecuteSql(sql);
	}
	catch(DbException& e )
	{
		std::string s = "Database error: SQL statement = '" + sql + "' -- Error: " + e.GetErrorString() + ", Origin: " + e.GetStateNativeOrigin();
		throw CrException(s);
	}
}

  
                                                                                           
                                                                                          
                                                                                         
                                                                      
  
std::string DBExecute(IncomingMsg* pmsg, const char*connect_str, const char*sql_str, std::vector<std::string>& de_vec, const std::vector<CColValType>& paramValues, long lTimeOut               )
{
	konga::DbConnection* pDb = pmsg->GetDBConnection(connect_str);
	if( !pDb ) pDb = konga::GetDbConnection(connect_str);
	if( lTimeOut>0 ) pDb->SetQueryTimeout(lTimeOut);

	bool bIsSelectStatement = strnicmp(sql_str,"SELECT",6) == 0;

	if( pmsg->IsDebugEnabled() )
	{
		std::string stActivity = "About to invoke database SQL statement: ";
		stActivity += sql_str;
		DumpActivity(pmsg, stActivity);
	}

	try
	{
		try
		{
			if( !pDb->IsOpen() )
			{
				pDb->SetQueryTimeout( GetODBCTimeOut() );
				                                                                                                            
				        
				pDb->Open(connect_str);
				pmsg->SetDBConnection(connect_str, pDb);
			}

			int iNumRowsAffected=-1;
			std::string stErrorNumRowsAffected;
			if( bIsSelectStatement )
			{
   				SmartDbPtr<DynamicDbRecordSet> ie(CreateDynamicDbRecordSet(pDb, sql_str));
				if(ie->Next())
				{
					for(int i=0, maxCol=ie->GetFieldCount();i<maxCol;++i)
					{
						FieldInfo fi = ie->GetFieldInfo(i);
						std::string stVal;
						switch( fi.SqlType )
						{
						case SQL_WCHAR:
						case SQL_WVARCHAR:
						case SQL_WLONGVARCHAR:
							ie->Close();
							char s[2048];
							sprintf(s,"The field %s is of wide character type.", fi.Name.c_str());
							throw s;
                                                                                          
                                                                             
                           
        
                                        
                                   
                           
                                                                                           
                                                                
         
                       
                                                                               
                                                                  
                                                                              
                 
         
                                           
         
                                                    
                                      
                                                                                                                
                                                                 
          
                        
                                                                      
                                                                   
                                                                               
                  
          
         
        
             
  
						default:
							stVal = ie->GetFieldValue(i);
						}
						de_vec.push_back(stVal);
					}
				}
				ie->Close();
			}
			else if( strnicmp(sql_str,"DELETE",6) == 0 )
			{
				pDb->ExecuteSql(sql_str);
				iNumRowsAffected = pDb->GetAffectedRowCount();
			}
			else
			{
				std::string stErr = ExecSqlWithLongText(sql_str, paramValues, pDb, &iNumRowsAffected, stErrorNumRowsAffected);
				if( !stErr.empty() )
				{
					char s[10000];
					strncpy(s,stErr.c_str(),9999);
					throw s;
				}
			}
			if( pmsg->IsDebugEnabled() )
			{
				std::string stActivity = "Invoked database SQL statement: ";
				stActivity += sql_str;
				if( !bIsSelectStatement )
				{
					stActivity += "\n\t\tNumber of rows affected: " + toString(iNumRowsAffected);
					if( !stErrorNumRowsAffected.empty() )
						stActivity += " (error = " + stErrorNumRowsAffected + ")";
				}
				stActivity += "\n";
				DumpActivity(pmsg, stActivity);
			}
		}
		catch(konga::DbException& e)
		{
			char s[10000];
			sprintf(s,"Database error: SQL statement = '%s' -- Error: %s, Origin: %s",
				sql_str,e.GetErrorString().c_str(),e.GetStateNativeOrigin().c_str());
			
			throw s;
		}
	}
	catch(const char* s)
	{
		pDb->Close();
		pmsg->SetDBConnection(connect_str, 0);
		if( pmsg->IsDebugEnabled() )
		{
			std::string stActivity = "Error invoking database SQL statement: ";
			stActivity += sql_str;
			stActivity += "\n\tError is: ";
			stActivity += s;
			stActivity += "\n";
			DumpActivity(pmsg, stActivity);
		}
		throw;
	}
	return "";                          
}

#ifdef DS_USE_SP
std::string SPExecute(IncomingMsg* pmsg, const char*execStr, long lTimeOut               )
{
	std::string ret;
	CSpCaller caller(execStr);
	try 
	{
		caller.SetTimeout( lTimeOut );
		caller.InvokeStoredProcedure();
	}
	catch(CSpException e)
	{
		throw strnewdup(e.Cause().c_str());
	}

	return caller.GetSPResults(pmsg);
}
#endif             

static const int MAXBUFLEN = 4096;
std::string GetDbError(SQLSMALLINT plm_handle_type, SQLHANDLE plm_handle, const std::string& stDesc)
{
	char		plm_szSqlState[MAXBUFLEN] = "", plm_szErrorMsg[MAXBUFLEN] = "";
	SDWORD		plm_pfNativeError = 0L;
	SWORD		plm_pcbErrorMsg = 0;
	SQLSMALLINT	plm_cRecNmbr = 1;

	SQLRETURN plm_retcode = SQLGetDiagRec(plm_handle_type, plm_handle,
		plm_cRecNmbr, reinterpret_cast<unsigned char*>(plm_szSqlState), &plm_pfNativeError,
		reinterpret_cast<unsigned char*>(plm_szErrorMsg), MAXBUFLEN - 1, &plm_pcbErrorMsg);

	switch(plm_retcode)
	{
	case SQL_SUCCESS:
	case SQL_SUCCESS_WITH_INFO:
		{
		std::string stErr = "Database error (" + stDesc + "). SQL State: ";
		stErr += plm_szSqlState;
		stErr += ", Native error: ";
		stErr += plm_szErrorMsg;
		return stErr;
		}
	case SQL_ERROR:
	case SQL_INVALID_HANDLE:
	default:
		{
		std::string stErr = "Database error (" + stDesc + "). No further information was available.";
		return stErr;
		}
	}
}

#ifdef DS_USE_DB_TEXT_ROUTINES

static const int CHUNK_SIZE = 1024;

class AutoCArrayOfCStrings
{
public:
	AutoCArrayOfCStrings(CArray<KString*, KString*>& aDataList) : m_aDataList(aDataList) {}
	~AutoCArrayOfCStrings()
	{
		for (int i = 0; i < m_aDataList.GetSize(); ++i)
			delete m_aDataList.GetAt(i);
	}
private:
	CArray<KString*, KString*>& m_aDataList;
};

class AutoSQLHandle
{
public:
	AutoSQLHandle(SQLHSTMT hstmt, int SqlHandleKind = SQL_HANDLE_STMT) : 
	  m_hstmt(hstmt), m_SqlHandleKind(SqlHandleKind) {}
	~AutoSQLHandle()
	{
		if (SQL_HANDLE_STMT==SqlHandleKind) {
			konga::SQLFreeHandle_STMT(m_hstmt)
		}else {
			SQLFreeHandle(m_SqlHandleKind, m_hstmt);
		}
	}
private:
	SQLHSTMT m_hstmt;
	int m_SqlHandleKind;
};

#define SQLERR(retcode) (retcode == SQL_ERROR)

std::string ExecSqlWithLongText(const std::string& stSql, const std::vector<CColValType>& paramValues, SQLHDBC hConn, int*pNumRowsAffected, std::string& stErrorNumRowsAffected)
{
	RETCODE retcode;
	SQLPOINTER pParamId;
	KString sValue = _T(""), LastOp;
	long * pTextSize = 0;
	long lDataSize = 0;
	long lBytes = 0;
	TCHAR szBuffer[CHUNK_SIZE+1] = {0};
	CArray<KString*, KString*> aDataList;
	AutoCArrayOfCStrings adopt_aDataList(aDataList);

	int iRowsAffected;
	if( !pNumRowsAffected ) pNumRowsAffected = &iRowsAffected;


	try
	{
		                                      
		LastOp = " SQLAllocHandle ";

		SQLHSTMT hstmt;
		if (::SQLAllocHandle(SQL_HANDLE_STMT, hConn, &hstmt)!=SQL_SUCCESS)
			return GetDbError(SQL_HANDLE_DBC, hConn, "SQL - Allocate Handle");
		AutoSQLHandle adopt_hstmt(hstmt);
		
		LastOp = " SQLSetStmtAttr ";

		                                                                                 
		                                      

		int iPos = 0;
		unsigned int iNumToBind = 0;

		int k = 0;
		std::string stNonQuoted;
		std::string::const_iterator cItSt = stSql.begin();
		while( cItSt < stSql.end() )
		{
			if( *cItSt == '\'' )
			{
				int n = cItSt - stSql.begin();
				if( (k = stSql.find('\'',n+1)) != std::string::npos )
					cItSt += (k-n+1);	                                              
				else
					break;                                                                                 
				continue;
			}
			stNonQuoted += *cItSt;
			++cItSt;
		}

		while ((iPos = stNonQuoted.find('?', iPos)) != std::string::npos)
		{
			iNumToBind++;
			iPos++;
		}

		if (iNumToBind > paramValues.size())
			return "Number of params to bind with is not equal to number of params available\n";

		pTextSize = new long[iNumToBind];
		AutoArrayDelete<long> adopt_pTextSize(pTextSize);

		                                
		                                                                                                 
		                                                                  
		for(unsigned int i = 0; i < iNumToBind; ++i)
		{
			lDataSize = paramValues[i].m_sColVal.GetLength();
			pTextSize[i] = SQL_NTS;
			TRACE("Param " + toString(xyx) + " , ColType = " + toString(xyx) + "\n", i,                               paramValues[i].m_iColType);
			KString *pDataStr = (KString*) new KString(paramValues[i].m_sColVal);
			aDataList.Add(pDataStr);                                                             
			LPTSTR pStrData = pDataStr->GetBufferSetLength(lDataSize);
			VOID* pDataPtr = (VOID*)(pStrData);
			LastOp = " SQLBindParameter ";
			retcode = ::SQLBindParameter(hstmt, 
										i + 1,				                    
										SQL_PARAM_INPUT,	                    
										SQL_C_CHAR,			                               
										paramValues[i].m_iColType,                                  
										lDataSize,			                   
										0, 
										pDataPtr,		       
										0, 
										&pTextSize[i]);
			if (SQLERR(retcode)) 
				return GetDbError(SQL_HANDLE_STMT, hstmt, " SQL - Bind Parameters " + stSql);
		}

		                   
		LastOp = " SQLExecDirect ";
		retcode = ::SQLExecDirect(hstmt, (UCHAR FAR *) stSql.c_str(), stSql.length());
		if (SQLERR(retcode)) 
			return GetDbError(SQL_HANDLE_STMT, hstmt, " SQL - ExecDirect = " + stSql);

		SQLRETURN lRet = SQLRowCount(hstmt, (long*)pNumRowsAffected);
		if( lRet < 0 ) *pNumRowsAffected = lRet;

		if( retcode == SQL_NEED_DATA && iNumToBind > 0 )
		{
			int iParamNum = 0;
			LastOp = " SQLParamData - First call";
			retcode = ::SQLParamData(hstmt, &pParamId);
			while(retcode == SQL_NEED_DATA)
			{
				lBytes = 0;
				lDataSize = _tcslen(reinterpret_cast<LPCTSTR>(pParamId));

				while(lBytes + CHUNK_SIZE < lDataSize)
				{
					_tcsncpy(szBuffer, static_cast<LPCTSTR>(pParamId) + lBytes, CHUNK_SIZE);
					szBuffer[CHUNK_SIZE]=0;
					LastOp = " SQLPutData ";
					retcode = ::SQLPutData(hstmt, szBuffer, CHUNK_SIZE);
					if (SQLERR(retcode)) 
						return GetDbError(SQL_HANDLE_STMT, hstmt, "SQL - Put Data " + stSql);

					lBytes += CHUNK_SIZE;
				}

				                      
				_tcsncpy(szBuffer, static_cast<LPCTSTR>(pParamId) + lBytes, lDataSize - lBytes);

				LastOp = " SQLPutData ";
				                 
				retcode = ::SQLPutData(hstmt, szBuffer, lDataSize - lBytes);
				       
				                                                                      

				if (SQLERR(retcode)) 
					return GetDbError(SQL_HANDLE_STMT, hstmt, "SQL - Put Data " + stSql);

				                                                                  
				                                                                               
				                                   
				LastOp.Format(" SQLParamData Param# = " + toString(xyx) + "", iParamNum);
				retcode = ::SQLParamData(hstmt, &pParamId);
				++iParamNum;
			}

			if (SQLERR(retcode)) 
				return GetDbError(SQL_HANDLE_STMT, hstmt, "SQL - Param Data " + stSql);
		}

		return "";
	}
	catch(...)
	{
		return (LPCTSTR)((" Generic exception in CDbAccess::ExecSqlWithLongText - Called ") + LastOp);
	}
}

HRESULT ReadTextValueFromDb(std::string& stValue, DBConnectionInfo& connectInfo, const std::string& stSql)
{
	ColedbGenericTextTab oledbGenericTextTab;
	HRESULT hr = oledbGenericTextTab.Open(connectInfo, stSql);

	if( hr == S_OK )
		hr = oledbGenericTextTab.MoveFirst();

	if( hr != S_OK )
		return hr;

	char sBigBuf[4097];
	DWORD cb;
	if( pGenericTextTab->GetStringValue()_status != DBSTATUS_S_ISNULL )
	{
		do
		{
			pGenericTextTab->GetpStreamStringValue()->Read(sBigBuf, 4096, &cb);
			sBigBuf[cb]=0;
	   		stValue += sBigBuf;
		} while (cb > 0);
	}

	return S_OK;
}

HRESULT WriteTextValueToDb(const std::string& stValue, const std::string& stTableName, const std::string& stSql, const std::string& stWhereClause, DBConnectionInfo& connectInfo, DbConnection* pIdb, bool bInsert)
{
	std::vector<CColValType> paramValues;
	paramValues.push_back(CColValType(stValue.c_str(), SQL_LONGVARCHAR));
	int iNumRowsAffected=0;
	std::string stErrorNumRowsAffected;
	std::string stErr = ExecSqlWithLongText(stSql, paramValues, pIdb->m_hdbc, &iNumRowsAffected, stErrorNumRowsAffected);
	if( stErr.empty() )
		return S_OK;
	else
	{
		std::string stErr = "[Msg 420] Database error when writing data element to DB -- %s - %s", 
			stErr.c_str(), connectInfo.VerbosePrettyConnectInfoString().c_str());
		ReportError( g_log_type, stErr);
		return E_FAIL;
	}
	return S_OK;
}
#else
                                          
std::string ExecSqlWithLongText(const std::string& stSql, const std::vector<CColValType>& paramValues, SQLHDBC hConn, int*pNumRowsAffected, std::string& stErrorNumRowsAffected)
{
	throw new CrException("Not implemented yet. ExecSqlWithLongText() engine_util.cpp");
}

void RegisterFileForProcessInstance(int pid, int piid, std::string stFileName, DbConnection* pIDB)
{
	try
	{
		konga::SmartDbPtr<IDb::TemporaryFileTab> pTemporaryFileTab( IDb::TemporaryFileTab::MakeInstance(pIDB));
		pTemporaryFileTab->AddNew();
		pTemporaryFileTab->SetFileName(stFileName);
		pTemporaryFileTab->SetProcessId(pid);
		pTemporaryFileTab->SetProcessInstanceId(piid);
		pTemporaryFileTab->Update();
	}
	catch(DbException& e) 
	{
		std::string stErr = "[Msg 392] (PID=" + toString(pid) + ",PIID=" + toString(piid) + 
			") Database error while adding temporary file '" + stFileName + "' to database. "
			"(IDB TemporaryFileTab) " + e.GetErrorString() + " [" + e.GetStateNativeOrigin() + 
			"  " + g_idb_connect.VerbosePrettyConnectInfoString();
		ReportError( g_log_type, stErr);
	}
                              
}

void RenameRegisteredFileForProcessInstance(int pid, int piid, DbConnection* pIDB, std::string stOldFileName, std::string stNewFileName)
{
	konga::SmartDbPtr<IDb::TemporaryFileTab> pTemporaryFileTab( IDb::TemporaryFileTab::MakeInstance(pIDB));
	std::string stFilter = "WHERE (ProcessId=" + toString(pid) + ") AND (ProcessInstanceId="
		+ toString(piid) + ") AND (FileName='" + SqlEscape(stOldFileName) + "')";
	try
	{
		pTemporaryFileTab->Select(stFilter);
		if( pTemporaryFileTab->Next() )
		{
			if( stNewFileName.empty() )
				pTemporaryFileTab->Delete();
			else
			{
				pTemporaryFileTab->Edit();
				pTemporaryFileTab->SetFileName(stNewFileName);
				pTemporaryFileTab->Update();
			}
		}
	}
	catch(DbException& e)
	{
		std::string stErr = "[Msg 401] (PID=" + toString(pid) + ",PIID=" + toString(piid) +
			") Database error renaming registered files. (IDB TemporaryFileTab) " +
			e.GetErrorString() + " [" + e.GetStateNativeOrigin() + "  " + g_idb_connect.VerbosePrettyConnectInfoString();
		ReportError( g_log_type, stErr);
	}
	pTemporaryFileTab->Close();
}

void PurgeRegisteredFilesForProcessInstance(int pid, int piid, DbConnection* pIDB)
{
	konga::SmartDbPtr<IDb::TemporaryFileTab> pTemporaryFileTab( IDb::TemporaryFileTab::MakeInstance(pIDB));
	std::string stFilter = "WHERE (ProcessId=" + toString(pid) + ") AND (ProcessInstanceId=" + toString(piid) + ")";
	try
	{
		pTemporaryFileTab->Select(stFilter);
		while(pTemporaryFileTab->Next())
		{
			::kDeleteFile(pTemporaryFileTab->GetFileName());
			std::string stFile = pTemporaryFileTab->GetFileName();
			const std::string::size_type pos1 = stFile.find_last_of('\\');
			const std::string::size_type pos2 = stFile.find_last_of('/');
			const std::string::size_type pos = pos1 > pos2 ? pos1 : pos2;
			if( pos != std::string::npos &&	                                       
					strnicmp(g_temp_dir.c_str(),stFile.c_str(),g_temp_dir.length()) == 0 &&                             
					strnicmp(g_temp_dir.c_str(),stFile.substr(0,pos).c_str(),stFile.length()) != 0 )                                                
				kRemoveDirectory( stFile.substr(0,pos).c_str() );
		}
	}
	catch(DbException& e) 
	{
		std::string stErr = "[Msg 393] (PID=" + toString(pid) + ",PIID=" + toString(piid) + 
			") Database error purging temporary files. (IDB TemporaryFileTab) " + e.GetErrorString() + 
			" [" + e.GetStateNativeOrigin() + "  " + g_idb_connect.VerbosePrettyConnectInfoString();
		ReportError( g_log_type, stErr);
	}
	pTemporaryFileTab->Close();
}

bool IsFileRegisteredForProcessInstance(int pid, int piid, std::string stFileName, DbConnection* pIDB)
{
	konga::SmartDbPtr<IDb::TemporaryFileTab> pTemporaryFileTab( IDb::TemporaryFileTab::MakeInstance(pIDB));
	std::string stFilter = "(ProcessId=" + toString(pid) + ") AND (ProcessInstanceId="
		+ toString(piid) + ") AND (FileName='" + SqlEscape(stFileName) + "')";
	bool bRet = false;
	try
	{
		pTemporaryFileTab->Select(stFilter);
		pTemporaryFileTab->Next();
		bRet = !pTemporaryFileTab->IsEOF();
	}
	catch(DbException& e) 
	{
		std::string stErr = "[Msg 394] (PID=" + toString(pid) + ",PIID=" + toString(piid) +
			") Database error purging temporary files. (IDB TemporaryFileTab) " + e.GetErrorString() +
			" [" + e.GetStateNativeOrigin() + "  " + g_idb_connect.VerbosePrettyConnectInfoString();
		ReportError( g_log_type, stErr);
	}
	pTemporaryFileTab->Close();
	return bRet;
}

std::string GetRegisteredTmpFileName(int pid, int piid, DbConnection* pIDB, const std::string& prefix       , const std::string& stTempDir       )
{
	std::string stFileName = getTmpFileName(prefix,stTempDir);
	RegisterFileForProcessInstance(pid, piid, stFileName, pIDB);
	return stFileName;
}

bool IsSingleServer()
{
	std::string stCurrentServer = ::GetOptionAsString("Settings/FaultTolerance", "Current Server", "Single");
	if( stCurrentServer.empty() )
		return true;
	return stricmp(stCurrentServer.c_str(),"Single") == 0;
}

bool IsUsingCRSystemDB(const std::string& connectionInfo)
{
	return false;                                                                                
}

std::string BuildRouteWhereClause(int iProcessId, int iProcessInstanceId, int iTaskId, int iTaskInstanceId) { 
	return "(ProcessId = " + konga::toString(iProcessId) + ") AND (ProcessInstanceId = " + konga::toString(iProcessInstanceId) +
		") AND (TaskId = " + konga::toString(iTaskId) + ") AND (TaskInstanceId = " + konga::toString(iTaskInstanceId) + ")";
}


#endif                           

