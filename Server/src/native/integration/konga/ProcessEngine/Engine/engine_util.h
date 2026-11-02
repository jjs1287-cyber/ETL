/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */

#ifndef ENGINE_UTIL_H
#define ENGINE_UTIL_H

                      
#include <sql.h>	              

#include <konga/Db/DbUtils/DbUtils.h>	                     
#include <konga/Utils/KString.h>		                     

#include "IncomingMsg.h"				                                                

  
                                                                     
                                                           
                                                                          
  
class InstanceTerminated {};

extern std::string g_temp_dir;

struct InvokeErr { 
	InvokeErr(const char*err, unsigned long hr=0x80004005L) : m_err(err), m_hr(hr) {}
	std::string m_err;
	unsigned long m_hr;
};

KString InvokeCOM(IncomingMsg* pmsg, const KString& progId, KString args);
KString InvokeComRemote(IncomingMsg* pmsg, const KString& progId, KString args);
KString InvokeCORBA(IncomingMsg* pmsg, const char* location, KString args);
KString InvokeEJB(IncomingMsg* pmsg, std::string location, KString args);

void CORBA_Init();

  
                                       
             
     
             
     
                     
     
                     
  
                                         
  
int parse_route_string(char*& route, int& pid, int& piid, int& tid, int& tiid, std::string& proc_name, std::string& task_name);

                                                                                                                  
namespace engine_util {
	struct CColValType
	{ 
		CColValType(KString sColVal, int iColType) : m_sColVal(sColVal), m_iColType(iColType) {}
		KString m_sColVal;
		int m_iColType;
	};
}

                                                                       
int GetODBCTimeOut(int iDefault=5*60);

void ExecuteSql(const std::string& connection, const std::string& sql);

std::string DBExecute(IncomingMsg* pmsg, const char*connect_str, const char*sql_str, std::vector<std::string>& de_vec, const std::vector<engine_util::CColValType>& paramValues, long lTimeOut = -1);
std::string SPExecute(IncomingMsg* pmsg, const char*execStr, long lTimeOut = -1);

unsigned long ReadTextValueFromDb(std::string& stValue, DBConnectionInfo& connectInfo, const std::string& stSql);
unsigned long WriteTextValueToDb(const std::string& stValue, const std::string& stTableName, const std::string& stSql, const std::string& stWhereClause, DBConnectionInfo& connectInfo, DbConnection* pIdb, bool bInsert);

std::string GetDbError(SQLSMALLINT plm_handle_type, SQLHANDLE plm_handle, const std::string& stDesc);
  
                                                                        
                                                          
  
std::string ReplaceDE_ExecScripts(IncomingMsg* pmsg, const std::string& str, const std::string& stDbConnect="");
                                                                           

              
                                                                        
  
                                                                     

              
                                                                                  
                                                
  
                                                                            

  
                                                                          
                                                               
  
std::string ReplaceDataElementWithValue(IncomingMsg* pmsg, const std::string& str, const std::string& stDbConnect="");
std::string ReplaceDataElementWithValueAddEscapes(IncomingMsg* pmsg, const char* str, const std::string& stEscChars);

  
                                            
  
bool ExecuteBooleanScript(IncomingMsg* pmsg, const char* script );

  
                                                                          
                                                                         
                   
  
TaskDataElement ExecuteScript(IncomingMsg* pmsg, const char* script, long lTimeOut=-1);

void ReportDataElements(IncomingMsg* pmsg,const char *de);

void GetDataElementTypeAndId(IncomingMsg* pmsg, int pid, int proc_version, int tid, const char*de, data_type& type, int& id,std::string& initValue);

void GetProcessDataElementList(IncomingMsg* pmsg, int pid, int piid, int proc_version, std::vector<TaskDataElement>& vecTde);
TaskDataElement GetProcessDataElement(IncomingMsg* pmsg, int pid, int piid, int proc_version, const char* str);
void SetProcessDataElement(IncomingMsg* pmsg, int pid, int piid, int proc_version, const char* de, const char* val, TaskDataElement* pTde=0);

void GetDataElementValue(IncomingMsg* pmsg, char*& s, std::string& sVal);
void GetDataElementValue(IncomingMsg* pmsg, std::string& s, std::string& sVal);

bool IsUsingCRSystemDB(const std::string& connectionInfo);

inline int MsgPriorityToThreadPriority(int iMsgPriority)
{
	#ifdef WIN32
	switch(iMsgPriority)
	{
	case 0: return THREAD_PRIORITY_IDLE;
	case 1:
	case 2: return THREAD_PRIORITY_LOWEST;
	case 3:
	case 4: return THREAD_PRIORITY_BELOW_NORMAL;
	case 5:	return THREAD_PRIORITY_NORMAL;
	case 6:	return THREAD_PRIORITY_ABOVE_NORMAL;
	case 7:
	case 8:
	case 9:	return THREAD_PRIORITY_HIGHEST;
	default: return iMsgPriority<0?THREAD_PRIORITY_IDLE:THREAD_PRIORITY_HIGHEST;
	}
	#else
		switch(iMsgPriority)
		{
		case 0: return -15;
		case 1:
		case 2: return -2;
		case 3:
		case 4: return -1;
		case 5:	return 0;
		case 6:	return 1;
		case 7:
		case 8:
		case 9:	return 2;
		default: return iMsgPriority<0 ? -15 : 2;
		}
	#endif
}

#ifndef _BLOB_ENTRY_CODE_EX
                                                                                   
                                                                

#define _BLOB_ENTRY_CODE_EX(nOrdinal, IID, flags, dataOffset, lengthOffset, statusOffset) \
	if (pBuffer != NULL) \
	{ \
		CAccessorBase::FreeType(DBTYPE_IUNKNOWN, pBuffer + dataOffset); \
	} \
	else if (pBinding != NULL) \
	{ \
		DBOBJECT* pObject = NULL; \
		ATLTRY(pObject = new DBOBJECT); \
		if (pObject == NULL) \
			return E_OUTOFMEMORY; \
		pObject->dwFlags = flags; \
		pObject->iid     = IID; \
		CAccessorBase::Bind(pBinding, nOrdinal, DBTYPE_IUNKNOWN, sizeof(IUnknown*), 0, 0, eParamIO, \
			dataOffset, lengthOffset, statusOffset, pObject); \
		pBinding++; \
	} \
	nColumns++;
#endif                       

#ifndef BLOB_ENTRY_LENGTH_STATUS
#define BLOB_ENTRY_LENGTH_STATUS(nOrdinal, IID, flags, data, length, status) \
	_BLOB_ENTRY_CODE_EX(nOrdinal, IID, flags, offsetbuf(data), offsetbuf(length), offsetbuf(status));
#endif                            

                                                              
  
void RenameRegisteredFileForProcessInstance(int pid, int piid, DbConnection* pIDB, std::string stOldFileName, std::string stNewFileName);
void PurgeRegisteredFilesForProcessInstance(int pid, int piid, DbConnection* pIDB);
std::string GetRegisteredTmpFileName(int pid, int piid, DbConnection* pIDB, const std::string& prefix="", const std::string& stTempDir="");
void RegisterFileForProcessInstance(int pid, int piid, std::string stFileName);
bool IsFileRegisteredForProcessInstance(int pid, int piid, std::string stFileName, DbConnection* pIDB);

  
                                                                  
  
bool IsSingleServer();

  
                                                                                         
  
std::set<long> GetDistributionJobErrorList();
long SetDistributionJobErrorList(std::set<long>& vlJobErrList);

std::string ExecSqlWithLongText(const std::string& stSql, const std::vector<engine_util::CColValType>& paramValues, SQLHDBC hConn, int*pNumRowsAffected, std::string& stErrorNumRowsAffected);

std::string BuildRouteWhereClause(int iProcessId, int iProcessInstanceId, int iTaskId, int iTaskInstanceId);

#endif                 

