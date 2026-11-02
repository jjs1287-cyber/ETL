/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

                                       

#ifndef INCOMING_MSG_H
#define INCOMING_MSG_H

#include <algorithm>	                                
#include <functional>	                                         
#include <map>			                            
#include <set>			                            
#include <string>		                               

#include <boost/thread/mutex.hpp>
#include <boost/thread/recursive_mutex.hpp>

#include <string.h>		             

#include <konga/Log/logging.h>				                 

#include <konga/Utils/AutoCleanup.h>		                            
#include <konga/Utils/PortDefs.h>			            
#include <konga/Utils/DBConnectionInfo.h>	                         
#include <konga/Utils/ProcessUtils.h>		             

#include <konga/Db/DbUtils/DbUtils.h>		                     

#include <konga/Db/MsgDb/EventMsgTab.h>		                               
#include <konga/Db/MsgDb/IncomingMsgTab.h>	                                  

#include <konga/Workflow/TaskTypeEnum.h>

#include "ThreadData.h"		             
#include "ThreadManager.h"	                      
#include "ProgressStatus.h"	                       
#include "TaskDataElement.h"                        

#ifdef _MSC_VER
	#pragma warning(disable:4503)
	#pragma warning(disable:4786)
#endif

extern bool g_multi_thread;
extern DbConnection *g_mdb;
extern DbConnection *g_idb;
extern DbConnection *g_pdb;

extern DBConnectionInfo g_mdb_connect;
extern DBConnectionInfo g_idb_connect;
extern DBConnectionInfo g_pdb_connect;
extern DBConnectionInfo g_trandb_connect;

extern std::string g_error_connect;
extern int g_log_type;
extern ThreadManager g_thread_manager;

extern boost::recursive_mutex g_db_lock;                             

namespace std {

template<>
struct less< const string& > : binary_function< const string&, const string&, bool >
{
	bool operator () (const string& lhs, const string& rhs) const 
	{ 
		return stricmp(lhs.c_str(),rhs.c_str()) < 0;
	}
};

template<>
struct less< string& > : binary_function< string&, string&, bool >
{
	bool operator () (string& lhs, string& rhs) const 
	{ 
		return stricmp(lhs.c_str(),rhs.c_str()) < 0;
	}
};

}                 

struct ErrInfo
{
	ErrInfo() : m_hr(0) {}
	void SetErr(unsigned long hr, std::string str)
	{
		m_hr=hr;
		std::replace<std::string::iterator,char>(str.begin(), str.end(), ';', '.');
		m_str=str;
	}
	const char* FullError() { return m_str.length()?m_str.c_str():""; }
	unsigned long m_hr;
	std::string m_str;
};

class IncomingMsg  
{
	IncomingMsg(const IncomingMsg&);                         
	void Init()
	{
		g_thread_manager.AddTask(this);

		m_hCancelEvent = new boost::condition();
		m_hCancelCompletedEvent = new boost::condition();

		m_bWaitingForCall = false;
		m_iStatus = STATUS_UNDEFINED;
		m_bSwitchedDatabaseServers = false;
		m_ProcessType = 0;

		m_pThreadObject = 0;
		m_osProcId = 0;
		m_wasKilled = false;
	}
public:
	IncomingMsg();
	IncomingMsg(int pid, int piid, int tid, int tiid, IncomingMsg*pmsg);
	IncomingMsg(const std::string& MsgFrom, const std::string& MsgTo, const std::string& MsgSubject, 
				const std::string& ProcessVariables, long ReceivedAt, long MsgId, 
				long iMsgPriority, bool bSingleThreadMode = false);
	IncomingMsg(konga::SmartDbPtr<MsgDb::IncomingMsgTab>& pTab);
	IncomingMsg(konga::SmartDbPtr<MsgDb::EventMsgTab>& pTab);
	virtual ~IncomingMsg();

	ThreadData m_thread_data;

	DbConnection* GetPDB() const { return g_multi_thread?const_cast<DbConnection*>(m_thread_data.m_pdb):g_pdb; }
	DbConnection* GetMDB() const { return g_multi_thread?const_cast<DbConnection*>(m_thread_data.m_mdb):g_mdb; }
	DbConnection* GetIDB() const { return g_multi_thread?const_cast<DbConnection*>(m_thread_data.m_idb):g_idb; }

	  
	                                                                  
	  
	std::string	m_From;
	std::string	m_To;
	std::string	m_Subject;
	std::string	m_ProcessVariables;
	long	m_ReceivedAt;
	int		m_MsgId;

	std::string	m_ProcessName;
	std::string	m_TaskName;

	typedef std::map< int, std::map< std::string, TaskDataElement, std::less< const std::string& > > > tid2de_map;
	typedef tid2de_map::iterator tid2de_it;
	typedef tid2de_map::const_iterator tid2de_const_it;
	tid2de_map m_source_elements;

	typedef std::set<int> searched_set;
	typedef searched_set::iterator searched_set_it;
	typedef searched_set::const_iterator searched_set_const_it;
	searched_set m_searched_tids;

public:

	void ParseProcessVariables();

	std::string GetDataElementValueList();
	void UpdateDataElements(const std::string& theList);
	void SetDataElementValue(const char* de_name, const char* de_value);
	bool IsNonProcessDataElementDefined(const char* de_name) const;
	std::string GetDataElementValue(const char* de_name);
	TaskDataElement GetDataElement(const std::string& de_name);
	TaskDataElement GetLocalDataElement(const char* de_str);
	void LoadSourceDataElements(int tid);
	void ReadDataElementsFromDB();
	void WriteDataElementsToDB();
	void ReadMsgDataElements();

	#ifdef DS_USE_COM
	void SetCOMObject(const char* obj_name, IDispatch* pdisp);
	IDispatch* GetCOMObject(const char* obj_name);

	void SetCOMObjectToken(const char* obj_name, long lToken);
	long GetCOMObjectToken(const char* obj_name);
	ICrComCallerPtr GetExternalComCaller() const
	{
		if( !(bool)m_ExternalComCaller )
		{
			ICrComCallerPtr tmp(__uuidof(CrComCaller));
			m_ExternalComCaller.Attach( tmp.Detach() );
		}
		return m_ExternalComCaller;
	}
	#endif              
	
	void SetDBConnection(const char* connect_str, DbConnection* pDb);
	DbConnection* GetDBConnection(const char* connect_str);

	void SwitchDatabaseServers(std::string stDatabaseServerName)
	{
		m_bSwitchedDatabaseServers = true;
		m_thread_data.Connect(true);
	}
	bool DidSwitchDatabaseServers() const { return m_bSwitchedDatabaseServers; }

	const char* GetTaskName();
	const char* GetProcessName();
	std::string GetRouteId();
	std::string GetRoute();

	int GetThreadPriority() const { return m_iPriority; }
	int GetMsgPriority() const { return m_iMsgPriority; }
	int GetNumberOfThreads() const { return m_bSingleThreadMode?1:-1; }

	void SetThreadObject(boost::thread* pt) {
		boost::mutex::scoped_lock lock(m_ThreadObjectLock);
		m_pThreadObject = pt;
		g_thread_manager.AddThread( pt, this );
	}
	boost::thread* GetThreadObject() const {
		boost::mutex::scoped_lock lock(m_ThreadObjectLock);
		return m_pThreadObject;
	}
	
	const char* ErrFull() { return m_err.FullError(); }
	unsigned long ErrHresult() const { return m_err.m_hr; }
	void ErrSet(unsigned long hr, std::string str) { m_err.SetErr(hr,str); }
	void TaskStatus(int iStatus) { m_iStatus |= iStatus; }                                                   
	int TaskStatus() const { return m_iStatus; }

	void SetProcessType(long pt) { m_ProcessType = pt; }
	long GetProcessType() const { return m_ProcessType; }

	void ReadTaskType();
	void SetTaskType(long tt);                                                          
	void AddTaskType(long tt) { m_TaskType |= tt; }
	bool IsInitialTask() { if(m_TaskType<0) ReadTaskType(); return (m_TaskType & konga::ttINITIAL) == konga::ttINITIAL; }
	bool IsTerminatingTask() { if(m_TaskType<0) ReadTaskType(); return (m_TaskType & konga::ttTERMINATING) == konga::ttTERMINATING; }
	bool IsNotificationTask() { if(m_TaskType<0) ReadTaskType(); return (m_TaskType & konga::ttNOTIFICATION) == konga::ttNOTIFICATION; }
	bool IsMachineSpecificTask() { if(m_TaskType<0) ReadTaskType(); return (m_TaskType & konga::ttMACHINE_SPECIFIC) == konga::ttMACHINE_SPECIFIC; }
	bool IsCrUpdateTask() { if(m_TaskType<0) ReadTaskType(); return (m_TaskType & konga::ttCR_UPDATE) == konga::ttCR_UPDATE; }	
	bool IsDebugEnabled();
	bool IsAutoReRun() { return (m_ProcessType & konga::ptAUTO_RERUN) == konga::ptAUTO_RERUN; }

	bool RemoveFromIncomingQueueWhenDone() const { return m_bRemoveFromQueueWhenDone; }
	void StayInQueue(bool bStay) { m_bRemoveFromQueueWhenDone = !bStay; }

	void Kill();
	bool WasKilled() const { return m_wasKilled; }

	void SetOsProcessId(const ProcessIdT& id) { m_osProcId = id; }
	ProcessIdT GetOsProcessId() const { return m_osProcId; }

	int m_pid, m_piid, m_tid, m_tiid;		          
	int m_opid, m_opiid, m_otid, m_otiid;	         
	int m_esc_pid, m_esc_piid, m_esc_tid, m_esc_tiid;	             
	int m_process_version, m_oprocess_version;

	std::string m_from_doc_de_list;

	typedef std::map< std::string, TaskDataElement*, std::less< const std::string& > > name2de_map;
	typedef std::map< std::string, TaskDataElement*, std::less< const std::string& > >::iterator name2de_it;
	typedef std::map< std::string, TaskDataElement*, std::less< const std::string& > >::const_iterator name2de_constit;
	name2de_map m_elements;

	#ifdef DS_USE_COM
	typedef std::map< std::string, IDispatch*, std::less< const std::string& > > name2COM_map;
	typedef name2COM_map::iterator name2COM_it;
	typedef name2COM_map::const_iterator name2COM_constit;
	name2COM_map m_COMObjects;

	typedef std::map< std::string, long, std::less< const std::string& > > name2ComToken_map;
	typedef name2ComToken_map::iterator name2ComToken_it;
	typedef name2ComToken_map::const_iterator name2ComToken_constit;
	name2ComToken_map m_ComTokens;
	#endif

	typedef std::map< std::string, DbConnection*, std::less< const std::string& > > name2db_map;
	typedef name2db_map::iterator name2db_it;
	typedef name2db_map::const_iterator name2db_constit;
	name2db_map m_DBConnections;

	  
	                                               
	                                                   
	                        
	  
	std::map< int, std::vector< std::string > > m_emails;

	const char* FullError();
protected:

private:
	bool	m_bSingleThreadMode;	                                                     
	bool	m_bRemoveFromQueueWhenDone;	                                                     
										                                                 
	bool	m_bSwitchedDatabaseServers;                                                
										                                                     
										                                                      
										                                                       
										                                                          
										                                                              
										                                                             
	int		m_iStatus;	                                         
	long	m_TaskType, m_ProcessType;
	ErrInfo m_err;
	boost::condition*	m_hCancelEvent;                                                                  
	                                                               
	                                                                 
	boost::condition*	m_hCancelCompletedEvent; 
	bool m_bWaitingForCall;                                                                                
	int m_iPriority, m_iMsgPriority;

	ProcessIdT m_osProcId;                                                                 
	bool m_wasKilled;                                                              

	boost::thread* m_pThreadObject;
	mutable boost::mutex m_ThreadObjectLock;

	#ifdef DS_USE_COM
	mutable ICrComCallerPtr m_ExternalComCaller;
	#endif
};

#endif                  

