/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#ifdef WIN32
	#include <windows.h>
#endif
#include <sqlext.h>	                                                                     

#ifdef DS_USE_CORBA
	WINBASEAPI BOOL WINAPI TryEnterCriticalSection(LPCRITICAL_SECTION lpCriticalSection);
	#include "corba_util.h"
#endif                

#include <boost/thread/recursive_mutex.hpp>

#include <boost/thread/thread.hpp>
#include <boost/algorithm/string.hpp>	             

#include <boost/scoped_ptr.hpp>

#include <konga/SysInfo/SysInfo.h>		                                     

#include <konga/Utils/AutoPtrCleanup.h>	                                   
#include <konga/Utils/CrException.h>	                         
#include <konga/Utils/CurrentTime.h>	                     
#include <konga/Utils/DBConnectionInfo.h>	                         
#include <konga/Utils/ErrorResolver.h>	                      
#include <konga/Utils/ProcessUtils.h>	               
#include <konga/Utils/Settings.h>		                      
#include <konga/Utils/StringUtils.h>	              
#include <konga/Utils/ThreadUtils.h>	                    
#include <konga/Utils/Timer.h>			              
#include <konga/Utils/util.h>			                     
#include <konga/Utils/FileUtils.h>		              

#include <konga/WinUtils/WinErrorUtils.h>	                        

#include <konga/ServerVersion/ServerVersion.h>

#include <konga/Db/DbUtils/AutoDbCleanup.h>                       

#include <konga/Db/IDb/ActivityTab.h>		                              
#include <konga/Db/IDb/ProcessTab.h>		                             
#include <konga/Db/IDb/StatusTab.h>			                            
#include <konga/Db/IDb/TaskTab.h>			                          

#include <konga/Db/MsgDb/ArchiveMsgTab.h>	                                  
#include <konga/Db/MsgDb/ErrorMsgTab.h>		                                
#include <konga/Db/MsgDb/EventMsgTab.h>		                                
#include <konga/Db/MsgDb/IncomingMsgTab.h>	                                   

#include <konga/Db/PDb/ProcessTab.h>		                             
#include <konga/Db/PDb/TaskTab.h>			                          
#include <konga/Db/PDb/TaskDataElementsTab.h>                                      

#include <konga/Db/DbMaintenance/DbMaintenance.h>
#include <konga/OperationLog/OperationLog.h>

#include "../ComponentInfo.h"

#include "EngineCore.h"
#include "engine_util.h"		             
#include "resource.h"
#include "../mcmsg.h"
#include "ThreadManager.h"		                      
#include "IncomingMsg.h"		                                                
#include "TaskDataElement.h"	                        
#include "FaultTolerantObject.h"                            
#include "EngineDebugUtils.h"	                 

using namespace konga;
using namespace std;

#ifdef DS_CLUSTER
	FaultTolerantObject g_FaultTolerantObj;
	std::set<long> g_slDistributionJobErrors;
#endif

#ifdef DS_USE_OLEDB
	  
	                                                                           
	                                                                                    
	  
	CDataSource g_dbPersistForConnectionPool;
#endif                

extern std::string g_last_error;	                          
extern EngineCore *g_engine;
                           

const int iMACHINE_SPECIFIC_PRIORITY = 2000;
const int iCR_UPDATE_PRIORITY = 2001;

bool g_bDbNotConnected = true;
bool g_multi_thread = false;
int g_max_threads = 10;
int g_time_interval_ms = 2000;
int g_queue_select_limit = 5;

volatile long g_exiting = 0;
std::string g_machine_name;
ThreadManager g_thread_manager;

DbConnection *g_mdb = 0;
DbConnection *g_idb = 0;
DbConnection *g_pdb = 0;
DbConnection *g_trandb = 0;
                           

boost::recursive_mutex g_db_lock;

DBConnectionInfo g_mdb_connect;
DBConnectionInfo g_idb_connect;
DBConnectionInfo g_pdb_connect;
DBConnectionInfo g_trandb_connect;

std::string g_error_connect;

                             

void EngineCore::EngineTimerFunction::operator()() {
	while(pEngine && pEngine->IsRunning() ) {
		const boost::function0<void>& f = pEngine->m_pTimer->WaitForTimerEvent();
		f();
	}
#ifdef _DEBUG
	printf("%s", "Exited EngineCore::EngineTimerFunction::operator()()");
#endif
}

namespace {
	                                                
	const unsigned long DB_RETRIES = 10;
	const unsigned long DB_RETRY_MILLISLEEP = 30000;
	void gCheckIncoming()
	{
		if( g_exiting ) return;
		                                                                                        
		g_engine->CheckIncoming();
	}

	  
                             
  
                         
                                                                                                 
     
   
                                    
   
                        
   
                                                                                                                                   
                                                          
                                   
   
                                                                  
  
   
}

extern "C" void StartWFDebugger(void*);

EngineCore::EngineCore(int log_type) :
	m_is_running(false),
	m_bCrUpdateMode(false),
	m_ErrorStatus(esNOT_READY),
	m_pThread(0),
	m_pTimer(0),
	m_bSingleThreadMode(false),
	m_iSingleThreadMsgId(-1),
	m_iIncomingTimerId(0), m_iScheduledEventTimerId(0)
{
	#ifdef DS_USE_COM
	::CoInitializeEx(0, COINIT_MULTITHREADED);
	#endif              

	g_engine = this;
}

void StopWFDebugger();
EngineCore::~EngineCore()
{
	if( m_is_running ) Stop();

	if( m_pTimer ) {
		m_pTimer->ShouldExit(true);
		delete m_pTimer;
	}

	g_engine = 0;

	#ifdef DS_USE_COM
	CoUninitialize();
	#endif              
}

namespace {
	  
	                                    
	                             
	               
	                      
	  
	void EngineCoreInitGlobals() {
		const string& sMaxThreads = GetOptionAsString(stPROCESSENGINE_COMPONENT_NAME, "MaxNumberOfWorkerThreads", "auto");
		if( konga::EqualsIgnoreCase(sMaxThreads, "auto") ) {
			string sVendor, sArchitecture, sProduct;
			unsigned long nCores;
			GetCpuInfo(sVendor, sArchitecture, sProduct, nCores);
			g_max_threads = 2 * nCores;
		} else {
			g_max_threads = GetOptionAsInt(stPROCESSENGINE_COMPONENT_NAME, "MaxNumberOfWorkerThreads", 10);
		}
		if( g_max_threads < 1  ) g_max_threads = 1;
		else if( g_max_threads > 100 ) g_max_threads = 100;

		g_time_interval_ms = konga::GetOptionAsInt(stPROCESSENGINE_COMPONENT_NAME, "TimeIntervalMs", 2000);
		if ( g_time_interval_ms < 0 ) g_time_interval_ms = 0;

		g_queue_select_limit = GetOptionAsInt(stPROCESSENGINE_COMPONENT_NAME, "QueueSelectLimit", 5);
		if ( g_queue_select_limit < 1) g_queue_select_limit = 1;

		g_machine_name = getMachineName();
		g_temp_dir = getTmpDir();
	}
}

bool EngineCore::Start()
{
	if( IsRunning() ) return true;


	#ifdef DS_CLUSTER
	bool bFaulTolerantSystem = false;
	  
	                                           
	  
	if( ::IsSingleServer() )
		RemoveErrorStatus(esNOT_READY);
	else if( GetOptionAsInt( "konga/Settings", "OnlyRunResDistrJobs", 0) )
	{
		RemoveErrorStatus(esNOT_READY);
		AddErrorStatus(esRESOURCE_DISTRIBUTION_JOBS_ONLY);
	}
	else
	{
		                                                                  
		g_FaultTolerantObj.Init();
		bFaulTolerantSystem = true;
	}

	                                                                         
	  
	                                                                               
	                                                                                       
	                                                                                     
	                   
	  
	::GetDistributionJobErrorList();                                   
	#endif              

	EngineCoreInitGlobals();

	std::string stLogString = "[Msg 51] Process Engine starting.\n[Built: ";
	stLogString += __DATE__;
	stLogString += " at ";
	stLogString += __TIME__;
	stLogString += "]";
	stLogString += "\nVersion: " + GetServerVersion().GetLongVersionString();
	stLogString += "\nNumber of worker threads: " + toString(g_max_threads) +
		"\nServer Home Directory: " + GetServerHomeWithSlash();
	#ifdef _DEBUG
	stLogString += "\nProcess engine was built in debug mode";
	#endif

	std::string stStartMsg = stLogString;
	#ifdef DS_CLUSTER
	if( GetErrorStatus() & esRESOURCE_DISTRIBUTION_JOBS_ONLY )
		stStartMsg += " \nProcessing resource distribution jobs only.";

	if( bFaulTolerantSystem )
	{
		stStartMsg += " \nPrimary database server: " + GetPrimaryMachineName();
		std::string stSecondary = GetSecondaryMachineName();
		if( !stSecondary.empty() )
			stStartMsg += " \nSecondary database server: " + stSecondary;
		stStartMsg += " \nCurrent database server: " + g_FaultTolerantObj.GetCurrentServer();
		stStartMsg += " \nIntention: " + g_FaultTolerantObj.Intention();
	}
	#endif              
	
	ReportError( g_log_type, stStartMsg, EVENTLOG_INFORMATION_TYPE);

	SetLogType( g_log_type );

	#ifdef DS_USE_CORBA
	try
	{
		CORBA_Init();
	}
	catch(CORBA::Exception& ex)
	{
		CORBA::String_var strEx = ex._to_string();
		std::string stErr = "[Msg 358] Unexpected CORBA error when starting engine: %s", (const char*)strEx);
		ReportError( g_log_type, log_str );
	}

	if( GetOptionAsInt(stPROCESSENGINE_COMPONENT_NAME, "EnableDebugging", 0) == 1 )
		::_beginthread(StartWFDebugger,0,0);
	#endif                

	InitThingsThatUseDatabase();

	                                                  
	m_is_running = true;

	m_pTimer = new Timer();
	m_pTimer->AddTimerFunction(gCheckIncoming, g_time_interval_ms);

	                                                                                              
                                                                                    
	m_pTimer->Start();
	m_pThread = new boost::thread(EngineTimerFunction(this));

	RemoveErrorStatus(esNOT_READY);
	return true;			 
}

bool EngineCore::Stop()
{
	if( !IsRunning() ) return true;
	m_is_running = false;

	#ifdef DS_CLUSTER
	if( !::IsSingleServer() )
	{
		m_ErrorStatus &= esNOT_READY;
		g_FaultTolerantObj.Cleanup();
	}
	#endif              

	#ifdef WIN32         
	if(m_iIncomingTimerId) ::KillTimer(0,m_iIncomingTimerId);
	if(m_iScheduledEventTimerId) ::KillTimer(0,m_iScheduledEventTimerId);
	#endif

	m_iIncomingTimerId = m_iScheduledEventTimerId = 0;

	CleanupMessages();

	ReportError( g_log_type, "[Msg 53] Process Engine exiting.", EVENTLOG_INFORMATION_TYPE);

	#ifdef DS_USE_CORBA
	StopWFDebugger();
	#endif

	if( m_pTimer ) {
		m_pTimer->ShouldExit(true);
		delete m_pTimer;
		m_pTimer = 0;
	}

	m_pThread->join();
	delete m_pThread;
	m_pThread = 0;
	return true;
}

void EngineCore::InitThingsThatUseDatabase()
{
	CheckForTasksToReRun();

	MoveExistingMessagesToErrorQueue();

	konga::CleanOperationDataElementTab(*g_idb, *g_mdb);

	                                                         
	bool bAllowObjectCallsByDefault = GetOptionAsInt(stPROCESSENGINE_COMPONENT_NAME, "AllowObjectCallsByDefault", 1) == 1;
	m_ObjectAccessControl.SetDefaultAccess(bAllowObjectCallsByDefault); 
	ReadAccessControledObjectList();
}
	
                                                                             
                              

#ifdef fooWIN32         
void EngineCore::OnStartTimers()
{
	#ifdef DS_CLUSTER
	if( !::IsSingleServer() )
		g_FaultTolerantObj.StartHeartbeat();
	#endif

	if( (m_iIncomingTimerId=::SetTimer(0,0,incoming_time_interval,EngineTimerIncoming)) == 0 )
	{
		ReportError(g_log_type, "[Msg 54] Failed to create timer. Will not be able to read messages from database.",
			EVENTLOG_ERROR_TYPE);
	}		

	if( (m_iScheduledEventTimerId=::SetTimer(0,0,SCHEDULED_EVENT_TIME_INTERVAL,EngineTimerScheduledEvent)) == 0 )
	{
		ReportError(g_log_type, "[Msg 56] Failed to create timer. Will not be able to process scheduled events.",
			EVENTLOG_WARNING_TYPE);
	}

	if( !g_multi_thread ) g_msgr->OnStartTimers();
}

void EngineCore::OnTimer(UINT nIDEvent) 
{
                      
  
                                                 
                                                              
  
  	
	                           
}

LRESULT EngineCore::OnQueryEndSession(WPARAM,LPARAM)
{
	Stop();
	return 1;                    
}
#endif

                                                
  
                                                                                              
                                                                                         
  
  
                                       
 
                                                                                                       
                                                                                     
                                              
         
 
   
                                                                     
                      
   
                      
                                                                      
       

                           

                                        
                                                 

                                      
         

                    
                                                                                                      
                      

            
            

                                     
                            
                                              

                                                                                                                   
                                

     
  
                              
   
                                   
    
                          
           
    

                                                     
                                                                                    
                                                       
                 
                        
   
                       
  
                                                                                                                 
                                                                                           
                                  
  
                 
  
 
  
  
                                                         
 
                                     
  
              
         
  

   
                                                                       
                                                
   
    
  
                                
         
               
  
                               
  

    
  
    
                                                                
                                                             
                                                               
    
                                                                                 
                                
    
                                                                                                
   
               
          
   

    
                                                                                                            
    
                     

                                                                                                        
   
                                                                                                             
                                      

                                
                                    
                                                                          

                                                      
                                           
                                                      
               
   
      
   
                                                                                
                
                           
   
  
                        
  
                               
  
 
  

void EngineCore::CheckIncoming()
{
	#ifdef WIN32         
	  
	                                                                    
	                     
	  
	if( m_iIncomingTimerId ) { ::KillTimer(0,m_iIncomingTimerId); m_iIncomingTimerId=0; }
	#endif

	if( !IsRunning() ) return;

	  
	                                                                         
	                                                                       
	                          
	  
	try
	{
		CheckIncomingDB();
	}
	catch( const CrException& )
	{
		                                                                            
		ReportError( g_log_type, "Will try to reconnect datasources in 10 seconds.");
		MilliSleep(10000);
		ReConnectDataSources();
	}

	if( g_exiting ) return;
}

namespace {
	void CancelInterruptedOperation(IncomingMsg* message) {
		const std::string id=message->GetDataElementValue("__OperationInstanceID");
		if(id.empty())return;
		const std::string reason="Operation cancelled because its Process Engine stopped before the operation completed. ETLForge will not automatically run an interrupted operation again.";
		try {
			OperationLog operationLog(*g_trandb);
			operationLog.LogMessageUpdateStatus(toLongLong(id),OperationLogStatus_Cancelled,"Operation cancelled.",reason);
		} catch(const CrException& error) {
			ReportError(g_log_type,error.GetErrorString()+"\n"+reason,EVENTLOG_ERROR_TYPE);
		}
	}

	string SqlSelectLimitStr(int max_limit, int nRunning, int nMax) {
		const int delta = nMax - nRunning;
		return toString( delta >= max_limit ? max_limit : delta );
	}

	IncomingMsg* createIncomingMsg(MsgDb::IncomingMsgTab& incomingMsgTab, bool isSingleThreadMode) {
		IncomingMsg* pmsg =
		new IncomingMsg(incomingMsgTab.IsMsgFromNull() ? "<null>" : incomingMsgTab.GetMsgFrom(),
			incomingMsgTab.IsMsgToNull() ? "<null>" : incomingMsgTab.GetMsgTo(),
			incomingMsgTab.IsMsgSubjectNull() ? "<null>" : incomingMsgTab.GetMsgSubject(),
			incomingMsgTab.GetProcessVariables(), incomingMsgTab.GetReceivedAt(), incomingMsgTab.GetMsgId(),
			incomingMsgTab.GetMsgPriority(), isSingleThreadMode
			);
		if( incomingMsgTab.GetMsgPriority() == iMACHINE_SPECIFIC_PRIORITY )
			pmsg->SetTaskType(-1*ttMACHINE_SPECIFIC);                                                                                                                 
		else if( incomingMsgTab.GetMsgPriority() == iCR_UPDATE_PRIORITY )
			pmsg->SetTaskType(-1*ttCR_UPDATE|ttMACHINE_SPECIFIC);                                                                                          
		return pmsg;
	}

}

bool EngineCore::CheckIncomingDbTransaction(const string& sqlSelect, vector<IncomingMsg*>& pmsgs) {
	try {
		                           
		g_mdb->SetTransactionIsolationLevel(READ_COMMITTED);
		AutoRollbackTransaction transaction(g_mdb);

		                                                                                                              
		SmartDbPtr<MsgDb::IncomingMsgTab> pIncomingMsgTab( MsgDb::IncomingMsgTab::MakeInstance(g_mdb) );
		try {
			pIncomingMsgTab->Select(sqlSelect);
		} catch( const DbException& e) {
			std::string stErr = "[Msg 69] Database error: CheckIncoming, Select -- (MsgDB IncomingMsgTab) " +
				e.GetErrorString() + " [" + e.GetStateNativeOrigin() + "] " + g_mdb_connect.VerbosePrettyConnectInfoString();
			ReportError( g_log_type, stErr);
			throw CrException(e.GetErrorString());
		}											   

		while( pIncomingMsgTab->Next() ) {
			if( GetNumberOfRunningTasks() >= g_max_threads ||
				(m_ErrorStatus & (esWAITING | esNOT_READY)) || g_exiting || !IsRunning() )
			{
				break;
			}
	
			if( pIncomingMsgTab->GetNumThreads() == 1 && !m_bSingleThreadMode ) {
				pIncomingMsgTab->Edit();
				pIncomingMsgTab->SetOwner(g_machine_name);
				pIncomingMsgTab->SetIsExecuting(2);	
				pIncomingMsgTab->Update();
	
				const int reservedId = pIncomingMsgTab->GetMsgId();
				transaction.Commit();
				m_bSingleThreadMode = true;
				m_iSingleThreadMsgId = reservedId;
				break;
			}
	
			IncomingMsg* pmsg;
			pmsgs.push_back( pmsg = createIncomingMsg(*pIncomingMsgTab, m_bSingleThreadMode) );

			  
			                                                                           
			  
			pIncomingMsgTab->Edit();
			pIncomingMsgTab->SetIsExecuting(1);
			pIncomingMsgTab->SetOwner(g_machine_name);
			pIncomingMsgTab->Update();
	
			if( (m_ErrorStatus & (esWAITING | esNOT_READY)) || g_exiting || !IsRunning() )
				break;
		}         

		transaction.Commit();
	} catch( const DbException& e) {
		if ( konga::IsSerializationFailure(e) ) {
			ReportError( g_log_type, "[Msg 474] CheckIncomingDb. Transaction rolled back due to serialization failure, will try again shortly.", EVENTLOG_WARNING_TYPE);
			return false;
		}

		                        
		std::string stErr = "[Msg 73] Database error: CheckIncoming. " + e.GetErrorString() + ". The Connection information was: " + g_mdb_connect.PrettyConnectInfoString();
		ReportError( g_log_type, stErr);
		throw CrException(e.GetErrorString());
	}
	return true;
}

void EngineCore::CheckIncomingDB()
{
	const int nRunning = GetNumberOfRunningTasks();
	if( nRunning >= g_max_threads || ( nRunning > 0 && m_bSingleThreadMode ) ||
		(m_ErrorStatus & (esWAITING | esNOT_READY)) )
		return;

	  
	                                                                                    
	                                                                                       
	                                                                                 
	  
	if( m_viTasksToReRun.size() )
	{
		std::string stUpdateSql = "UPDATE IncomingMsgTab SET IsRunning = 0 WHERE MsgId in (";
		std::vector<int>::const_iterator cItMsgId = m_viTasksToReRun.begin();
		for(;cItMsgId != m_viTasksToReRun.end(); ++cItMsgId)
			stUpdateSql += toString(*cItMsgId) + ',';
		stUpdateSql.erase(stUpdateSql.end()-1);                               
		stUpdateSql += ')';

		try {
			g_mdb->ExecuteSql(stUpdateSql);
		} catch( const DbException& e) {
			std::string stErr = "[Msg 406] Database error: CheckIncoming, Update (" + stUpdateSql + ") -- (MsgDB IncomingMsgTab) " +
				 e.GetErrorString() + " [" + e.GetStateNativeOrigin() + "] " + g_mdb_connect.VerbosePrettyConnectInfoString();
			ReportError( g_log_type, stErr);
			throw;
		}
		m_viTasksToReRun.clear();
	}

	  
	                                                 
	                                                                                                   
	                                                 
	  
	                                                                                                                                                                        
	std::string cmd;
	if( m_bSingleThreadMode ) {
		cmd += "WHERE MsgId = " + toString(m_iSingleThreadMsgId) + " AND Owner = '" + SqlEscape(g_machine_name) + "' FOR UPDATE SKIP LOCKED";
	}
	else {
		cmd += "WHERE (ISEXECUTING = 0) ORDER BY MsgPriority DESC, MsgId ASC LIMIT " + SqlSelectLimitStr(g_queue_select_limit, nRunning, g_max_threads) + " FOR UPDATE SKIP LOCKED";
	}

	                             
	vector<IncomingMsg*> pmsgs;
	AutoPtrDeleteVector<IncomingMsg> adopt_pmsgs(pmsgs);
	while (!CheckIncomingDbTransaction(cmd, pmsgs)) {
        for (vector<IncomingMsg*>::iterator failed=pmsgs.begin(); failed!=pmsgs.end(); ++failed) delete *failed;
        pmsgs.clear();
        MilliSleep(25);
    }

	  
	                                                                     
	                                                           
	  
	for ( vector<IncomingMsg*>::iterator it = pmsgs.begin(); it != pmsgs.end(); ++it ) {
		IncomingMsg* pmsg = *it;
		*it = 0;

		DUMPTIMINGS AutoDebugDumper(pmsg, "Processing message batch");
		ProcessMessage( pmsg );
	}
	                                
    
                          
    
                                           
                                 
    

	CleanupMessages();
}

                                                             
   
                                                                            
         

	                                                        
                                                                                                                                                                       
                                            

                                                                                            
                                                
                 
  
                                                                                                                                                                                                                             
                                                                                                 
                                  
                           
            
                

                                                                                  
                 
  
                                                                                                                                                                                                                            
                                                                                                 
                                  
                           
                              
  
                                                                                
 
                                         
                 
                                    
                             
  
                       
                                        
                           
                
  

                 
  
                                                                                                                                                                                                                
                                                                                                 
                                  
                       
                                        
                           
            
                

                                                                                                                                    
                                                                                                    
                                                                                                               
                                                                                                            
                                                             

                                                                      
                                                                                                                                                            
                                                                    
                                                                                                                                                 

   
                                                                            
   
                                    
                                                                    
                                   

                 
  
                                                                                                                                     
                                                              
                                                                                                                       
                                  
              
                           
                              
  

   
                                                                                    
   
    
  
                          
  
                        
  
                                         
                               
              
                           
                
  

                                             
                      
                 
  
                                                                                                                           
                                                                                                                 
                                  
                           
                              
  

                          
   
	
            
   

void ProcessMessageGlobal(void* pvmsg);

                               
void EngineCore::ProcessMessage(IncomingMsg* pmsg)
{
	if( g_multi_thread )
	{
		pmsg->SetThreadObject( new boost::thread( LegacyThreadFunction(ProcessMessageGlobal, (void*)pmsg) ) );

		                                  
                                                                      
   
                               
                                                                                                                        
                                                                 
                                                            
   
    
	}
	else {
		g_engine->ProcessMessage2(pmsg);
	}
}

void ProcessMessageGlobal(void* pvmsg)
{
	#ifdef DS_USE_COM
	::CoInitializeEx(0,COINIT_MULTITHREADED);
	#endif              
	
	KongaExceptionHandler handler(Logger::GetLogFileName("ProcessEngine"));
	IncomingMsg* pmsg = static_cast<IncomingMsg*>(pvmsg);

	try 
	{
		g_engine->ProcessMessage2(pmsg);
	}
	catch ( const DbException& ) {
		                                                    
		return;
	}
	catch( const CrFatalException& e ) {
		std::string stErr = "[Msg 324] A severe error has occurred and this task cannot continue. "
			"You can re-run it or delete it using the user interface. The error was (" + toString(e.GetErrorNumber()) +
			"): \n\n" + e.GetErrorString();
		ReportError( g_log_type, stErr);
		return;
	}
	catch(const std::string& s) {
		std::string stErr;
		stErr = "[Msg 472] A fatal error occurred. The error was: " + s;
		ReportError( g_log_type, stErr);
		return;
	}
#ifndef _DEBUG
	catch(...) {
		ReportError( g_log_type, "[Msg 473] A fatal, unspecified error occurred.");
		return;
	}
#endif
}																													 

  
                                             
 
                                                                        
                                                      

     
  
                                        
  
                           
  
                                                        
                                                                                   
                                                           
                                  
                                                      
  
           
  
                                                                         
                                        
                                                                                                    
      
        
   
                                                                                                                        
                                   
                                                      
   
         
  
                               
  

  
                                                                    
                               
                
  
void EngineCore::ProcessMessage2(IncomingMsg* pmsg)
{
	boost::scoped_ptr<IncomingMsg> adopt_pmsg(pmsg);

	{
	DUMPTIMINGS AutoDebugDumper(pmsg, "Connecting to system db");

	if( !g_multi_thread ) pmsg->m_thread_data.m_is_thread = false;
	if( !pmsg->m_thread_data.Connect() )
	{
		ArchiveMessage( pmsg );
		return;
	}
	}

	  
	                                                                      
	                                               
	                                                                
	                                                                   
	                                                                     
	                                                    
	  
	try
	{
		pmsg->ParseProcessVariables();
	}
	catch(const std::string& s)
	{
		ReportError( g_log_type, s);
		try {
			MoveMessageToErrorQueue(pmsg, -4, s);
		}
		catch(...) {
			addMsgToDelete(pmsg);
		}
		return;
	}
#ifndef _DEBUG
	catch(...)
	{
		std::string stErr = "[Msg 78] [" + pmsg->GetRoute() +
			"] Unknown error occurred while processing incoming message. It has been copied to msgdb : ErrorMsgTab";
		ReportError( g_log_type, stErr);
		MoveMessageToErrorQueue(pmsg, 78, stErr);
		return;
	}
#endif

	try {
		ArchiveMessage( pmsg );
		ProcessMessage3( pmsg );
	}
	catch( ... ) {
		addMsgToDelete(pmsg);
		throw;
	}
}

void EngineCore::ProcessMessage3(IncomingMsg* pmsg)
{
	DUMPTIMINGS AutoDebugDumper(pmsg, "Validate and run");

	  
	                                                      
	  
	if( !VerifyValidProcess(pmsg) ) {
		return;
	}

	  
	                             
	  
	if( !ContinueProcess(pmsg) ) {
		MoveMessageToErrorQueue(pmsg, -3, "The process is invalid.");
		return;
	}

	  
	                                                                                                
	  
	bool bSuppressError = false;
	if( !VerifyValidTask(pmsg, bSuppressError) ) {
		if( !bSuppressError )
			MoveMessageToErrorQueue(pmsg, -2, "The task is invalid.");
		else
			DoneWithMessage( pmsg );
		return;
	}

	if( pmsg->m_piid <= 0 ) {
		if( !StartProcessInstance(pmsg) ) {	                                       
			MoveMessageToErrorQueue(pmsg, -1, "Could not start process instance.");
			return;
		}
	}
	else if( !VerifyValidProcessInstance(pmsg) ) {	                                                 
		MoveMessageToErrorQueue(pmsg, -5, "The process instance is invalid.");
		return;
	}

	  
	                            
	  
	WriteTaskInstance( pmsg );

	pmsg->ReadDataElementsFromDB();

	DUMPTIMINGS DumpDataElements(pmsg, "Data elements as read from MsgDb");

	  
	                                                                      
	  
	if( !CheckRequiredDataElements( pmsg ) ) {
		return;
	}
	
	  
	                                                                                  
	                                                     
	  
	ExecuteActivities( pmsg );

	  
	                                                                                         
	  
	UpdateTaskStatus( pmsg, STATUS_COMPLETED );

	DoneWithMessage( pmsg );
}

void EngineCore::CleanupMessages()
{
	string stMsgids;
	{
		boost::mutex::scoped_lock lock(m_MsgsToDelete_lock);
		if ( m_MsgsToDelete.empty() ) {
			return;
		}

		std::vector<int>::iterator it = m_MsgsToDelete.begin();
		for(; it!=m_MsgsToDelete.end(); ++it)
		{
			stMsgids += toString(*it);
			if( it+1 != m_MsgsToDelete.end() )
				stMsgids += " OR MsgId=";
		}
		m_MsgsToDelete.clear();
	}

	if( !stMsgids.empty() ) {
		std::string sql = "DELETE FROM IncomingMsgTab WHERE MsgId=" + stMsgids;
		try {
			g_mdb->ExecuteSql(sql);

			sql = "INSERT INTO ArchiveDataElementTab (MsgId, DataElementName, DataType, IntValue, DoubleValue, StringValue)"
					"SELECT MsgId, DataElementName, DataType, IntValue, DoubleValue, "
					"StringValue FROM DataElementTab WHERE MsgId = " + stMsgids;
			g_mdb->ExecuteSql(sql);

			sql = "DELETE FROM DataElementTab WHERE MsgId = " + stMsgids;
			g_mdb->ExecuteSql(sql);

		} catch(DbException& e) {
			std::string stErr = "[Msg 81] Database error: CleanupMessages Open -- (MsgDB IncomingMsgTab). "
				"SQL=" + sql + ", " + e.GetErrorString() + ". The Connection information was: " + g_mdb_connect.PrettyConnectInfoString();
			ReportError( g_log_type, stErr);
		}
	}
}

void EngineCore::DoneWithMessage(IncomingMsg* pmsg)
{
	if( !pmsg->RemoveFromIncomingQueueWhenDone() )
		return;
	addMsgToDelete(pmsg);
}

void EngineCore::addMsgToDelete(IncomingMsg* pmsg) {
	boost::mutex::scoped_lock lock(m_MsgsToDelete_lock);
	m_MsgsToDelete.push_back( pmsg->m_MsgId );
}

  
                                                                 
                                                                       
  
void EngineCore::ArchiveMessage(IncomingMsg* pmsg) const
{
	DUMPTIMINGS AutoDebugDumper(pmsg, "Archiving");

	time_t t=0;
	string stSql("INSERT INTO ARCHIVEMSGTAB (MSGFROM,MSGTO,MSGSUBJECT,PROCESSVARIABLES,RECEIVEDAT,MSGID,MSGPRIORITY,OWNER) VALUES("
				"'" + SqlEscape(pmsg->m_From) + "','" + SqlEscape(pmsg->m_To) + "','" + SqlEscape(pmsg->m_Subject) + "','" + SqlEscape(pmsg->m_ProcessVariables) +
				"'," + toString(time(&t)) + ", " + toString(pmsg->m_MsgId) + ", " + toString(pmsg->GetMsgPriority()) + ", '" + SqlEscape(g_machine_name) + "')");
	try 
	{
		pmsg->GetMDB()->ExecuteSql(stSql);
	} 
	catch(DbException& e) {
		std::string stErr = "[Msg 86] [" + pmsg->GetRoute() + "] Database error: ArchiveMessage() -- ('" +
			stSql + "') (MsgDB ArchiveMsgTab) " + e.GetErrorString() + " [" + e.GetStateNativeOrigin() + "] " + g_mdb_connect.VerbosePrettyConnectInfoString();
		ReportError( g_log_type, stErr);
		throw;
	}

	stSql = "INSERT INTO ARCHIVEDATAELEMENTTAB (MsgId, DataElementName, DataType, IntValue, DoubleValue, StringValue) SELECT "
			"MsgId, DataElementName, DataType, IntValue, DoubleValue, StringValue FROM DATAELEMENTTAB WHERE MSGID = " + toString(pmsg->m_MsgId);
	try
	{
		pmsg->GetMDB()->ExecuteSql(stSql);
	} 
	catch(DbException& e) 
	{
		std::string stErr = "[Msg 371] [" + pmsg->GetRoute() + "] Database error while moving data elements to archive (" + stSql +
			") (MsgDB ArchiveDataElementTab) " + e.GetErrorString() + " [" + e.GetStateNativeOrigin() + "] " + g_mdb_connect.VerbosePrettyConnectInfoString();
		ReportError( g_log_type, stErr);
		throw;
	}
}

void EngineCore::MoveMessageToErrorQueue(IncomingMsg* pmsg, int iErr, const std::string& stErr, bool deleteFromIncomingQ         )
{
	#ifndef _DEBUG
	try 
	{
	#endif
		try 
		{
			SmartDbPtr<MsgDb::ErrorMsgTab> pErrorMsgTab( MsgDb::ErrorMsgTab::MakeInstance(pmsg->GetMDB()));
		                                      
			pErrorMsgTab->AddNew();
			pErrorMsgTab->SetMsgFrom(pmsg->m_From);
			pErrorMsgTab->SetMsgTo(pmsg->m_To);
			pErrorMsgTab->SetMsgSubject(pmsg->m_Subject);
			pErrorMsgTab->SetMsgId(pmsg->m_MsgId);
			pErrorMsgTab->SetProcessVariables(pmsg->m_ProcessVariables);
			pErrorMsgTab->SetReceivedAt(pmsg->m_ReceivedAt);
			pErrorMsgTab->SetProcessId(pmsg->m_pid);
			pErrorMsgTab->SetProcessInstanceId(pmsg->m_piid);
			pErrorMsgTab->SetTaskId(pmsg->m_tid);
			pErrorMsgTab->SetTaskInstanceId(pmsg->m_tiid);
			pErrorMsgTab->SetEngineErrorId(iErr);
			std::string stOutErr;
			if( !stErr.empty() ) ReplaceCharWithString(stErr, stOutErr, '\'', "''");
			pErrorMsgTab->SetEngineErrorText(!stOutErr.empty() ? stOutErr : "unknown error");
			pErrorMsgTab->SetOwner(g_machine_name);
			pErrorMsgTab->SetMsgPriority(pmsg->GetMsgPriority());
			pErrorMsgTab->Update();
		} 
		catch(const DbException& e) 
		{
			const std::string stLogErr(
				"[Msg 323] [" + pmsg->GetRoute() + "] Database error while moving message to error queue (MsgDB ErrorMsgTab) " + e.GetErrorString() + " [" + e.GetStateNativeOrigin() + "] " + g_mdb_connect.VerbosePrettyConnectInfoString()
				);
			ReportError( g_log_type, stLogErr);			
		}

		std::string stSql = "INSERT INTO ERRORDATAELEMENTTAB (MsgId, DataElementName, DataType, IntValue, DoubleValue, StringValue) SELECT "
				"MsgId, DataElementName, DataType, IntValue, DoubleValue, StringValue FROM DATAELEMENTTAB WHERE MSGID = " + toString(pmsg->m_MsgId);
		try
		{
			pmsg->GetMDB()->ExecuteSql(stSql);
			stSql = "DELETE FROM DATAELEMENTTAB WHERE MSGID = " + toString(pmsg->m_MsgId);
			pmsg->GetMDB()->ExecuteSql(stSql);
		} 
		catch(const DbException& e) 
		{
			std::string stLogErr = "[Msg 370] [" + pmsg->GetRoute() + "] Database error while moving data elements to error queue (" + stSql +
				") (MsgDB ErrorDataElementTab) " + e.GetErrorString() + " [" + e.GetStateNativeOrigin() + "] " + g_mdb_connect.VerbosePrettyConnectInfoString();
			ReportError( g_log_type, stLogErr);			
		}

		if( deleteFromIncomingQ )
		{
			addMsgToDelete(pmsg);
		}
	#ifndef _DEBUG
	} catch(...) {}
	#endif
}

  
                                                                                                   
                                                                                                         
                                          
  
void EngineCore::CheckForTasksToReRun()
{

	SmartDbPtr<MsgDb::IncomingMsgTab> pIncomingMsgTab( MsgDb::IncomingMsgTab::MakeInstance(g_mdb) );
	try {
		const std::string stWhereClause(
			"WHERE (ISEXECUTING = 1) AND (OWNER IS NULL OR OWNER = '' OR OWNER = '"
			+ SqlEscape(g_machine_name) + "') ORDER BY MsgPriority DESC, MsgId ASC, Owner DESC"
		);
		pIncomingMsgTab->Select(stWhereClause);
	} catch(DbException& e) {
		std::string stErr = "[Msg 69] Database error: CheckForTasksToReRun, Open -- (MsgDB IncomingMsgTab) " + e.GetErrorString() +
			" [" + e.GetStateNativeOrigin() + "] " + g_mdb_connect.VerbosePrettyConnectInfoString();
		ReportError( g_log_type, stErr);
		pIncomingMsgTab->Close();
		return;
	}											   

	try {
		IncomingMsg *pmsg = 0;
		while( pIncomingMsgTab->Next() )
		{
			if( (m_ErrorStatus & (esWAITING | esNOT_READY)) || g_exiting )
				break;
	
			pmsg = new IncomingMsg(pIncomingMsgTab->IsMsgFromNull()?"<null>":pIncomingMsgTab->GetMsgFrom(),
								   pIncomingMsgTab->IsMsgToNull()?"<null>":pIncomingMsgTab->GetMsgTo(),
								   pIncomingMsgTab->IsMsgSubjectNull()?"<null>":pIncomingMsgTab->GetMsgSubject(), 
						 pIncomingMsgTab->GetProcessVariables(), pIncomingMsgTab->GetReceivedAt(), pIncomingMsgTab->GetMsgId(), 
						 pIncomingMsgTab->GetMsgPriority(), m_bSingleThreadMode);
			AutoPtrDelete<IncomingMsg> adopt_pmsg(pmsg);
	
			  
			                                      
			  
			  
                                                          
                                                      
                            
                                     
                                                                                                          
     
	
			if( !pmsg->m_thread_data.Connect() )
			{
				ArchiveMessage( pmsg );
				goto getNextMessage;
			}
	
			try {
				pmsg->ParseProcessVariables();
			} catch(const char*s)
			{
				std::string stErr = "Error occurred while parsing process variables when re-running message: ";
				stErr += s;
				ReportError( g_log_type, stErr);
			} catch(CrFatalException& e)
			{
				std::string stErr = "Error occurred while parsing process variables when re-running message: ";
				stErr += e.GetErrorString();
				ReportError( g_log_type, stErr);
			} catch(...)
			{
				std::string stErr = "Unknown error occurred while parsing process variables when re-running message.";
				ReportError( g_log_type, stErr);
			}
	
			  
			                                                                                                
			  
			VerifyValidProcess(pmsg);
	
			CancelInterruptedOperation(pmsg);
			MoveMessageToErrorQueue(pmsg,CR_E_FAIL,"Interrupted operation was cancelled and will not be automatically run again.",true);
	
			DoneWithMessage(pmsg);
	
			getNextMessage:		
	
			if( (m_ErrorStatus & (esWAITING | esNOT_READY)) || g_exiting )
				break;
		}
	} catch(DbException& e) {
		std::string stErr = "[Msg 73] Database error: CheckForTasksToReRun, MoveNext -- (MsgDB IncomingMsgTab). " + e.GetErrorString() +
			" [" + e.GetStateNativeOrigin() + "] . The Connection information was: " + g_mdb_connect.PrettyConnectInfoString();
		ReportError( g_log_type, stErr);
	}
	pIncomingMsgTab->Close();
}

void dbErrLogger(const string& sMsg) {
	ReportError(g_log_type, "[Msg 462] " + sMsg);
}


  
                                                                       
                                                                           
                                                                             
  
void EngineCore::MoveExistingMessagesToErrorQueue()
{
	SmartDbPtr<DbConnection> db_mdb(0);
	try
	{
		db_mdb.Attach(
			GetDbConnectionWithRetries(g_mdb_connect.connectionString(), dbErrLogger, DB_RETRIES, DB_RETRY_MILLISLEEP)
			);
		db_mdb->SetQueryTimeout( GetODBCTimeOut() );
	}
	catch( const DbException& e )
	{
		std::string stErr = "[Msg 319] Database error: Moving messages to ErrorMsgTab. "
			"-- Error: " + e.GetErrorString() + " [" + e.GetStateNativeOrigin() + "] " + g_mdb_connect.VerbosePrettyConnectInfoString();
		ReportError( g_log_type, stErr);		
		return;
	}

	std::string cmd = "INSERT INTO ErrorMsgTab (MsgFrom, MsgTo, MsgSubject, ProcessVariables, "
		"ReceivedAt, MsgId) SELECT MsgFrom, MsgTo, MsgSubject, "
		"ProcessVariables, ReceivedAt, MsgId FROM IncomingMsgTab WHERE "
		"IsExecuting = 1 AND Owner = '" + SqlEscape(g_machine_name) + "'";
	try
	{
		db_mdb->ExecuteSql(cmd);
	}
	catch(DbException& e )
	{
		std::string stErr = "[Msg 321] Database error: Moving messages to ErrorMsgTab. SQL statement = '" + cmd + "' "
			"-- Error: " + e.GetErrorString() + " [" + e.GetStateNativeOrigin() + "] " + g_mdb_connect.VerbosePrettyConnectInfoString();
		ReportError( g_log_type, stErr);
		
		return;
	}

	  
	                                                                                             
	  
	cmd = "INSERT INTO ERRORDATAELEMENTTAB (MsgId, DataElementName, DataType, IntValue, DoubleValue, StringValue)"
					"SELECT DATAELEMENTTAB.MsgId, DATAELEMENTTAB.DataElementName, DATAELEMENTTAB.DataType, DATAELEMENTTAB.IntValue, "
						"DATAELEMENTTAB.DoubleValue, DATAELEMENTTAB.StringValue "
					"FROM DATAELEMENTTAB LEFT JOIN IncomingMsgTab ON DATAELEMENTTAB.MsgId = IncomingMsgTab.MsgId";
	try
	{
		db_mdb->ExecuteSql(cmd);

		cmd = "DELETE FROM DATAELEMENTTAB WHERE DATAELEMENTTAB.MsgId IN (SELECT IncomingMsgTab.MsgId "
				"FROM IncomingMsgTab WHERE IsExecuting = 1 AND Owner = '" + SqlEscape(g_machine_name) + "')";
		db_mdb->ExecuteSql(cmd);

		cmd = "DELETE FROM IncomingMsgTab WHERE IsExecuting = 1 AND Owner = '" + SqlEscape(g_machine_name) + "'";
		db_mdb->ExecuteSql(cmd);
	} 
	catch(DbException& e) 
	{
		std::string stErr = "[Msg 436] Database error while moving data elements to error queue (" + cmd + ") (MsgDB ErrorDataElementTab) " +
			 e.GetErrorString() + " [" + e.GetStateNativeOrigin() + "] " + g_mdb_connect.VerbosePrettyConnectInfoString();
		ReportError( g_log_type, stErr);		
	}

	db_mdb->Close();


	  
	                                                                                               
	                                                                                            
	                                                                                                 
	                                                                 
	  
	                                                                     
	                                                                                    
	  
	cmd = "UPDATE XferHist SET ProgressStatus = 0 WHERE (ProgressStatus = 1) OR (ProgressStatus = 2)";
	try
	{
		if( g_trandb )
			g_trandb->ExecuteSql(cmd);
	} 
	catch(DbException& e) 
	{
		std::string stErr = "[Msg 444] Database error while updating progress status for transfer history (" + cmd +
			") (TranDB) " + e.GetErrorString() + " [" + e.GetStateNativeOrigin() + "] " + g_trandb_connect.VerbosePrettyConnectInfoString();
		ReportError( g_log_type, stErr);	
	}

}

void EngineCore::OnError( int pid ) const
{

	                                                  
	                                  

	if( pid == -1 )                   
	{

	}

	                                                  
	             

	                                                         
	                                                      
	                                                      
	              

	      

}

  
                                                                   
                                                                
  
bool EngineCore::CheckRequiredDataElements(IncomingMsg* pmsg)
{
	SmartDbPtr<PDb::TaskDataElementsTab> pTaskDataElementsTab( PDb::TaskDataElementsTab::MakeInstance(pmsg->GetPDB()));

	std::string stNow(CCurrentTime().Now());
	std::string stFilter;

	std::string missing_data_elements = "The following data elements are required, but were not provided: ";
	int nmissing_data_elements = 0;

	try
	{
		stFilter = "WHERE (ProcessId = " + toString(pmsg->m_pid) + ") AND (ProcessVersion = " + toString(pmsg->m_process_version) +
			") AND (TaskId = " + toString(pmsg->m_tid) + ")";
		pTaskDataElementsTab->Select(stFilter);

		while( pTaskDataElementsTab->Next() ) 
		{			
			if( pTaskDataElementsTab->GetIsRequired() &&
				pmsg->m_elements.find( pTaskDataElementsTab->GetDataElementName() )
				== pmsg->m_elements.end() )
			{
				missing_data_elements += pTaskDataElementsTab->GetDataElementName() + ' ';
				++nmissing_data_elements;
			}
		}
		pTaskDataElementsTab->Close();
	} 
	catch(DbException& e) 
	{
		std::string stErr = "[Msg 96] [" + pmsg->GetRoute() + "] Database error: CheckRequiredDataElements() -- (PDBTaskDataElementsTab) (" +
			stFilter + ") " + e.GetErrorString() + " [" + e.GetStateNativeOrigin() + "] " + g_pdb_connect.VerbosePrettyConnectInfoString();
		pTaskDataElementsTab->Close();
		throw CrFatalException(CR_E_FAIL, stErr);
	}

	if( nmissing_data_elements == 0 ) return true;

	std::string stErr = "[Msg 97] [" + pmsg->GetRoute() + "] " + missing_data_elements + '\n';
	ReportError( g_log_type, stErr);
	MoveMessageToErrorQueue(pmsg, 97, stErr);

	  
	                                
	  

	      
	return false;
}
  
                                                                          
                              
                                                                        
                                                    
  
void EngineCore::CreateFakeIncomingMessage(IncomingMsg*pmsg, int pid, int piid, int tid, int tiid)
{
	std::string stRouteId = '{' + toString(pid) + ';' + toString(piid) + ';' + toString(tid) + ';' + toString(tiid) + ";}";

                                                                                   
#if defined(USE_OLEDB_FOR_MSGDB)

	time_t t=0;
	SmartDbPtr<MsgDb::IncomingMsgTab> pIncomingMsgTab( MsgDb::IncomingMsgTab::MakeInstance(g_mdb) );
	std::string stCmd = "INSERT INTO INCOMINGMSGTAB (MSGFROM,MSGTO,MSGSUBJECT,PROCESSVARIABLES,RECEIVEDAT,MSGPRIORITY,NUMTHREADS) VALUES("
						"'ETLForge','ETLForge','Incoming message generated by the Process Engine','";
				stCmd += pr;
				stCmd += "','";
				stCmd += itoa(time(&t),buf,10);
				stCmd.erase(stCmd.length()-1);
				stCmd += "',";
				stCmd += itoa(pmsg->GetMsgPriority(),buf,10);
				stCmd += ',';
				stCmd += itoa(pmsg->GetNumberOfThreads(),buf,10);
				stCmd += ')';
	HRESULT hr = pIncomingMsgTab->Open(stCmd,0,DBGUID_DEFAULT,false);
	if( FAILED(hr) )
	{
		std::string stErr = "[Msg 98] [" + pmsg->GetRoute() + "] Database error: CreateFakeIncomingMessage ProcessVariables = '%s' -- (MsgDB IncomingMsgTab) (%x). %s. The Connection "
			"information was: %s", pmsg->GetRoute().c_str(), pr, hr, ErrorResolver().Resolve(hr,true), g_mdb_connect.PrettyConnectInfoString().c_str());
		ReportError( g_log_type, stErr);
	}											   
	pIncomingMsgTab->Close();

#else

	SmartDbPtr<MsgDb::IncomingMsgTab> pIncomingMsgTab( MsgDb::IncomingMsgTab::MakeInstance(pmsg->GetMDB()));
	time_t t=0;
	try 
	{
		pIncomingMsgTab->AddNew();
		pIncomingMsgTab->SetMsgFrom("ETLForge");
		pIncomingMsgTab->SetMsgTo("ETLForge");
		pIncomingMsgTab->SetMsgSubject("incoming message generated by the Process Engine");
		pIncomingMsgTab->SetProcessVariables(stRouteId);
		pIncomingMsgTab->SetReceivedAt(time(&t));
		pIncomingMsgTab->Update();
	}
	catch(DbException& e )
	{
		std::string stErr = "[Msg 98] [" + pmsg->GetRoute() + "] Database error: CreateFakeIncomingMessage ProcessVariables = '" +
			stRouteId + "' -- " + e.GetErrorString() + " [" + e.GetStateNativeOrigin() + "] " + g_mdb_connect.VerbosePrettyConnectInfoString();
		ReportError( g_log_type, stErr);
	}
                            

#endif
}

void EngineCore::KillTasks(const std::set<int>& msgids) {
	g_thread_manager.KillTasks(msgids);
}

void EngineCore::ReconnectDB(const std::string& connStr, DbConnection*& pConn) {
	boost::mutex::scoped_lock lock(m_db_lock);
	try {
		if( pConn ) {
			try {
				if( pConn->IsOpen() ) {
					pConn->Close();
				}
			}
			catch( const CrException& ex ) {
				ReportError(g_log_type, "Failed to close old connection. Opening new. Error was: " + ex.GetErrorString());
			}
			delete pConn;
		}
		pConn = konga::GetDbConnectionWithRetries(g_pdb_connect.connectionString(), dbErrLogger, DB_RETRIES, DB_RETRY_MILLISLEEP);
	}
	catch(const DbException& e )
	{
		std::string stErr = "[Msg 99] Engine: Failed to reconnect database " + e.GetErrorString() +
			" [" + e.GetStateNativeOrigin() + "] " + g_pdb_connect.VerbosePrettyConnectInfoString();
		ReportError( g_log_type, stErr);
	}
}

void EngineCore::ReconnectPDB() {
	ReconnectDB(g_pdb_connect.connectionString(), g_pdb);
}

void EngineCore::ReconnectMDB() {
	ReconnectDB(g_mdb_connect.connectionString(), g_mdb);
}

void EngineCore::ReconnectIDB() {
	ReconnectDB(g_idb_connect.connectionString(), g_idb);
}

bool ReConnectDataSources()
{
	{
		boost::recursive_mutex::scoped_lock lock(g_db_lock);
		DestroyDataSources();
	}
	if( ConnectDataSources() ) {
		ReportError( g_log_type, "Successfully reconnected to the system databases.", EVENTLOG_INFORMATION_TYPE);
		return true;
	} else
		return false;
}

bool ConnectDataSources()
{
	boost::recursive_mutex::scoped_lock lock(g_db_lock);

	  
	                                  
	  
	::SQLSetEnvAttr(NULL, SQL_ATTR_CONNECTION_POOLING, (SQLPOINTER)SQL_CP_ONE_PER_DRIVER, SQL_IS_INTEGER);

	std::string pdb_con = g_pdb_connect.connectionString()?g_pdb_connect.connectionString():";DSN=ODBC;DSN=pdb;UID=sa;PWD=;";
	std::string idb_con = g_idb_connect.connectionString()?g_idb_connect.connectionString():";DSN=ODBC;DSN=idb;UID=sa;PWD=;";
	std::string mdb_con = g_mdb_connect.connectionString()?g_mdb_connect.connectionString():";DSN=ODBC;DSN=msgdb;UID=sa;PWD=;";
	std::string trandb_con = g_trandb_connect.connectionString()?g_trandb_connect.connectionString():";DSN=ODBC;DSN=trandb;UID=sa;PWD=;";

	bool bConnected = false;
	std::string stCurDb = "Process Definition Database (PDB). " + g_pdb_connect.VerbosePrettyConnectInfoString();
	try {
		if(!g_pdb) g_pdb = GetDbConnectionWithRetries(pdb_con, dbErrLogger, DB_RETRIES, DB_RETRY_MILLISLEEP);

		                                                    

		stCurDb = "Message Database (MsgDB). " + g_mdb_connect.VerbosePrettyConnectInfoString();
		if(!g_mdb) g_mdb = GetDbConnectionWithRetries(mdb_con, dbErrLogger);

		stCurDb = "Instance Database (IDB). " + g_idb_connect.VerbosePrettyConnectInfoString();
		if(!g_idb) g_idb = GetDbConnectionWithRetries(idb_con, dbErrLogger);

		stCurDb = "Transformation Database (TranDB). " + g_trandb_connect.VerbosePrettyConnectInfoString();
		if(!g_trandb) g_trandb = GetDbConnectionWithRetries(trandb_con, dbErrLogger);

		bConnected = true;
	}
	catch( const konga::DbException& e )
	{
		ReportError(g_log_type, "[Msg 105] Failed to connect to system database. Error: " + e.GetPrettyErrorMessage());
	}

	if( bConnected ) {
		int iTimeOut = GetODBCTimeOut();
		g_pdb->SetQueryTimeout(iTimeOut);
		g_idb->SetQueryTimeout(iTimeOut);
		g_mdb->SetQueryTimeout(iTimeOut);
		g_trandb->SetQueryTimeout(iTimeOut);
	} else {
		if( ::IsSingleServer() )
			return false;
		else
			return g_bDbNotConnected = true;
	}
			
	g_multi_thread = true;

	#ifdef DS_USE_OLEDB
	  
	                                    
	  
	CDBPropSet	dbinit(DBPROPSET_DBINIT);
	USES_CONVERSION;

	dbinit.AddProperty(DBPROP_AUTH_USERID, T2OLE(g_pdb_connect.uid()) );
	if( g_pdb_connect.pwd() )
		dbinit.AddProperty(DBPROP_AUTH_PASSWORD, T2OLE(g_pdb_connect.pwd()) );
	dbinit.AddProperty(DBPROP_INIT_CATALOG, T2OLE(g_pdb_connect.catalog()) );
	if( g_pdb_connect.oledbProvider() && strnicmp(g_pdb_connect.oledbProvider(),"MSDASQL",7) == 0 )
		dbinit.AddProperty(DBPROP_INIT_DATASOURCE, T2OLE(g_pdb_connect.dsnName()) );
	else
		dbinit.AddProperty(DBPROP_INIT_DATASOURCE, T2OLE(g_pdb_connect.dbServer()) );
	dbinit.AddProperty(DBPROP_INIT_LCID, (long)1033);
	dbinit.AddProperty(DBPROP_INIT_PROMPT, (short)DBPROMPT_NOPROMPT);
	dbinit.AddProperty(DBPROP_INIT_TIMEOUT, (long)300);

	HRESULT hr = g_dbPersistForConnectionPool.OpenWithServiceComponents(g_pdb_connect.oledbProvider(), &dbinit);
	if (FAILED(hr))
		return false;
	#endif                

	g_bDbNotConnected = false;
	if( g_engine ) 
		g_engine->InitThingsThatUseDatabase();
	return true;
}

bool DisconnectDataSources()
{
	if( g_pdb && g_pdb->IsOpen() ) g_pdb->Close();
	if( g_mdb && g_mdb->IsOpen() ) g_mdb->Close();
	if( g_idb && g_idb->IsOpen() ) g_idb->Close();
	if( g_trandb && g_trandb->IsOpen() ) g_trandb->Close();
	                                                 
	#ifdef DS_USE_OLEDB
	g_dbPersistForConnectionPool.Close();
	#endif
	return true;
}

void DestroyDataSources()
{
	DisconnectDataSources();
	if( g_pdb ) delete g_pdb; g_pdb = 0;
	if( g_mdb ) delete g_mdb; g_mdb = 0;
	if( g_idb ) delete g_idb; g_idb = 0;
	if( g_trandb ) delete g_trandb; g_trandb = 0;
	                                      
}

