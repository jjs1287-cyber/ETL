/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#ifndef ENGINE_CORE_H
#define ENGINE_CORE_H

#include <string>
#include <vector>
#include <set>

#include <boost/thread/mutex.hpp>

#include <konga/Utils/DBConnectionInfo.h>	                         
#include <konga/Utils/PortDefs.h>			          

#include <konga/Db/IDb/RouteTab.h>		                          
#include <konga/Db/IDb/TaskTab.h>		                         

#include <konga/Db/PDb/RoutingRuleTab.h>                                

#include "IncomingMsg.h"			                                                
#include <konga/Workflow/node.h>	             

#include "ObjectAccessControl.h"                            
#include "FaultTolerantObject.h"                            
#include "ProgressStatus.h"		                       


#define MSG_ENGINE_START_TIMERS (WM_USER+101)

namespace konga {
	class Timer;
}

extern int 				g_log_type;
extern volatile long	g_exiting;
extern bool 			g_multi_thread;                             
extern std::set<long> 	g_slDistributionJobErrors;                             

extern DbConnection *g_mdb;
extern DbConnection *g_idb;
extern DbConnection *g_pdb;
extern DbConnection *g_trandb;

extern DBConnectionInfo g_mdb_connect;

#ifdef DS_CLUSTER
	extern FaultTolerantObject g_FaultTolerantObj;
#endif              

bool ConnectDataSources();
bool ReConnectDataSources();
bool DisconnectDataSources();
void DestroyDataSources();
bool LoadDBConnectionInfo(const std::string stNewServer       );

enum tagWHICH_ACTIVITY { PostRoleActivity, PreRoleActivity };

struct tagRouteStruct
{
	tagRouteStruct() : m_pid(0), m_piid(0), m_tid(0), m_tiid(0) {}
	tagRouteStruct(int pid, int piid, int tid, int tiid) :
		m_pid(pid), m_piid(piid), m_tid(tid), m_tiid(tiid) {}
	int m_pid, m_piid, m_tid, m_tiid;
};
typedef struct tagRouteStruct RouteStruct;

                                                                             

class EngineCore
{
public:
	EngineCore(int log_type=0);

                 
public:
	virtual ~EngineCore();

	void SetLogType(int lt) { g_log_type = lt; }
	int GetLogType() const { return g_log_type; }

	bool Start();
	bool Stop();
	bool IsRunning() { return m_is_running; }
	int GetNumberOfRunningTasks() const { return g_thread_manager.GetNumberOfTasks() + g_thread_manager.GetNumberOfThreads(); }

	ErrorStatusEnum GetErrorStatus() const { return ErrorStatusEnum(m_ErrorStatus); }
	void ResetErrorStatus()
	{ 
		if( (m_ErrorStatus & esMACHINE_SPECIFIC_MSG_ERROR) != esMACHINE_SPECIFIC_MSG_ERROR  )
			return;
		boost::mutex::scoped_lock lock(m_MacSpecificCounter_lock);
		m_ErrorStatus &= ~esMACHINE_SPECIFIC_MSG_ERROR;
		g_slDistributionJobErrors.clear();
	}
	void AddErrorStatus(ErrorStatusEnum es)
	{
		boost::mutex::scoped_lock lock(m_MacSpecificCounter_lock);
		m_ErrorStatus |= es;
	}
	void RemoveErrorStatus(ErrorStatusEnum es)
	{
		boost::mutex::scoped_lock lock(m_MacSpecificCounter_lock);
		m_ErrorStatus &= ~es;
	}

	void CheckIncoming();
                               
                                                   

	void UpdateTaskStatus(IncomingMsg*  pmsg, int status);
	void UpdateProcessStatus(IncomingMsg*  pmsg, int status);
	void ArchiveProcessInstance(IncomingMsg*  pmsg) const;

                                                 
	void ProcessMessage2(IncomingMsg* pmsg);
	void ProcessMessage3(IncomingMsg* pmsg);
	bool VerifyValidProcess(IncomingMsg* pmsg);                                                  
	void MoveMessageToErrorQueue(IncomingMsg* pmsg, int iErr, const std::string& stErr, bool deleteFromIncomingQ=true);

	void SetMultiServerConfig(short serverId, short numServers) {                                                                 }
                                                                                                                                     
                                                                                                                                                              

	bool IsObjectAccessAllowed(std::string stName) const { return m_ObjectAccessControl.IsAllowed(stName); }

	DbConnection* GetPDB() const { return g_pdb; }
	DbConnection* GetMDB() const {	return g_mdb; }
	DbConnection* GetIDB() const { return g_idb; }
	DbConnection* GetTranDB() const { return g_trandb; }

	void ReconnectDB(const std::string& connStr, DbConnection*& pConn);
	void ReconnectPDB();
	void ReconnectMDB();
	void ReconnectIDB();

	void InitThingsThatUseDatabase();

	void OnTimer(UINT nIDEvent);

	#ifdef DS_CLUSTER
	  
	                  
	  
	std::string LastServer() const { return g_FaultTolerantObj.GetCurrentServer(); }
	void LastCompletedTask(const RouteStruct& routeStruct) { m_LastCompletedTask = routeStruct; }
	RouteStruct LastCompletedTask() const { return m_LastCompletedTask; }

	std::string GetPrimaryMachineName() const;
	std::string GetSecondaryMachineName() const;

	bool IsInResourceDistributionMode() { return m_bCrUpdateMode; }
	bool IsInResourceDistributionErrorMode() { return g_slDistributionJobErrors.size() > 0; }
	#endif              
	
	bool SwitchDatabases(const std::string stNewServer="");

	void ExitSingleThreadMode()
	{
		m_bSingleThreadMode = false;
		m_iSingleThreadMsgId = -1;
	}

	bool IsSingleThreadMode() { return m_bSingleThreadMode; }

	void CleanupMessages();                                  
	void KillTasks(const std::set<int>& msgids);

private:
	                               
	                                                                                                     
	                                            
	void CheckIncomingDB();

	                                                                                               
	bool CheckIncomingDbTransaction(const std::string& sqlSelect, std::vector<IncomingMsg*>& pmsgs);

	                    
	void ResolveRole(const std::string& role_str, std::vector< std::string >& email_list);
	void ProcessMessage(IncomingMsg* pmsg);	                         
	void OnError(int pid) const ;

	                                
	bool CheckRequiredDataElements(IncomingMsg* pmsg);
	std::string GetDataElementsForRoute(IncomingMsg* pmsg, int tid, int tiid, int document_id, char separator_char);
	std::string GetDestinationRoleString(IncomingMsg* pmsg, int dtid);
	int GetDestinationDocumentId(IncomingMsg* pmsg, int dtid);
	bool GetDocumentDetails(IncomingMsg* pmsg, std::string& msg_template_name, std::string& msg_template_path,
		std::string& msg_format, std::string& msg_subject, std::string& msg_cc, std::string& msg_bcc, 
		std::string& msg_replyTo, std::string& msg_from, int& msg_priority, std::string& msg_URL,
		char*& s_data_elements, int dtid, int document_id);
	void ReplaceDataElementsForDocument(IncomingMsg* pmsg, const std::string& source, std::string& dest,
										const 	std::map< std::string, std::string >& local_elements, bool& replacedUrl);
	int GetEscalationTaskId(IncomingMsg *nextTask, int& time);
	int GetRoutingRuleId(IncomingMsg* pmsg);
	bool RoutingRuleComplete(IncomingMsg* pmsg, int dtid, int dtiid);
	int GetActivityRuleId( IncomingMsg *pmsg, tagWHICH_ACTIVITY which = PostRoleActivity);
	void ExecuteActivities( IncomingMsg *pmsg );
	void ExecuteActivity(IncomingMsg* pmsg, int activity_id );
	void ExecuteActivityRule(IncomingMsg* pmsg, int rule_id );
	IncomingMsg *ExecutePreRoleActivities(IncomingMsg* pmsg, int dtid, int dtiid);
	bool ExecuteBooleanActivity(IncomingMsg* pmsg, int id );
	void RouteToTask(IncomingMsg* pmsg, int tid, int tiid);
	void ReadAccessControledObjectList();

	                                
	void CreateEscalation(IncomingMsg *pmsg, int dtid, int dtiid, int esctid, int esctiid, int time);
	bool CancelEscalation(IncomingMsg *pmsg, char *escal_id);
	int CreateTaskInstance(IncomingMsg* pmsg, int pid, int piid, int tid, bool overwrite=false) const;
	bool ContinueProcess(IncomingMsg* pmsg);
	void WriteTaskInstance(IncomingMsg* pmsg);
	progress_status GetTaskStatus(IncomingMsg*  pmsg, int pid, int piid, int tid, int tiid);
	void UpdateTaskStatus(IncomingMsg*  pmsg, int pid, int piid, int tid, int tiid, int status);
	void UpdateActivityStatus(IncomingMsg*  pmsg, int activity_id, int& activity_iid, int status);
	                                                                   
	bool EvaluateRoutingRule(IncomingMsg *pmsg, int dtid, int dtiid, const std::string& stack_str) const ;
	bool EvaluateRoutingRule(IncomingMsg *pmsg, int dtid, int dtiid, node* pn, konga::SmartDbPtr<IDb::RouteTab>& pIDBRouteTab) const ;

	bool StartProcessInstance(IncomingMsg* pmsg);                         
	void ExecuteRoutingRule(IncomingMsg* pmsg, int rule_id );
	bool VerifyValidProcessInstance(IncomingMsg* pmsg) const ;
	bool VerifyValidTask(IncomingMsg* pmsg, bool& bSuppressError) const ;
	void PrepareRouteToTask(IncomingMsg* pmsg, int tid, int tiid);
	void ReRunTask(IncomingMsg* pmsg) const;

	                                
	void DoneWithMessage(IncomingMsg* pmsg);
	void CheckForTasksToReRun();
	void MoveExistingMessagesToErrorQueue();
	void ArchiveMessage(IncomingMsg* pmsg) const ;
	void CreateFakeIncomingMessage(IncomingMsg* pmsg, int pid, int piid, int dtid, int dtiid);
	int CreateScheduledEvent(IncomingMsg* pmsg, int tid, int tiid, int when);
	bool CancelScheduledEvent(IncomingMsg* pmsg, int pid, int piid, int tid, int tiid);
	bool EscalateTask(IncomingMsg* pmsg);
	bool ReadEscalationIds(IncomingMsg* pmsg);

private:
	void addMsgToDelete(IncomingMsg* pmsg);

private:

	struct EngineTimerFunction {
		EngineTimerFunction(EngineCore* pEngine0) : pEngine(pEngine0) {}
		void operator()();
		EngineCore* pEngine;
	};

	bool m_is_running, m_bCrUpdateMode;
	long m_ErrorStatus;
	std::vector<int> m_MsgsToDelete;
	std::vector<int> m_viTasksToReRun;

	mutable boost::mutex m_MacSpecificCounter_lock;
	mutable boost::mutex m_MsgsToDelete_lock;
	mutable boost::mutex m_db_lock;
	mutable boost::mutex m_outgoing_msg_lock;                                            

	boost::thread *m_pThread;
	konga::Timer *m_pTimer;

	ObjectAccessControl m_ObjectAccessControl;

	                  
	RouteStruct m_LastCompletedTask;                                                                     
	std::string m_stLastServer;                                                  
	bool m_bSingleThreadMode;	                                                                 
	int m_iSingleThreadMsgId;

public:
	UINT m_iIncomingTimerId, m_iScheduledEventTimerId;
};

                                                                             

#endif                 

