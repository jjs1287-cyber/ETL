/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#include <cassert>
#include <map>

#include <konga/Utils/FileUtils.h>
#include <konga/Utils/Settings.h>
#include <konga/Utils/PortDefs.h>
#include <konga/Utils/ProcessUtils.h>
#include <konga/Utils/DateUtils.h>

#include <konga/Log/logging.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/DbRelations/EntityIdConstants.h>

#include <konga/ItemProperties/ItemProperties.h>
#include <konga/ItemProperties/Definitions.h>

#include <konga/WinUtils/WinErrorUtils.h>	                        

#include "ComponentInfo.h"
#include "Scheduler.h"
#include "ClusterHeartbeat.h"

using namespace konga;
using namespace TranDb;
using namespace std;
using namespace boost::posix_time;
using namespace boost::gregorian;

Scheduler::Scheduler(konga::Logger* pLogger, FILE* debug_log)
	: m_pLogger(pLogger), m_debug_log(debug_log), m_TranDbinfo("TranDb"), m_bIsRunning(false), m_bIsDirty(true), m_pSchedulerThread(0)
{
	assert(m_pLogger);
	m_spLastMessages=new std::string*[NUM_MESSAGES_STORED];
	for(int i=0;i<NUM_MESSAGES_STORED;i++){
		m_spLastMessages[i]=0;
	}
	const std::string sSchedulerSimulationStartTime(konga::GetOptionAsString("Debug", "SchedulerSimulationStartTime", "" ));

	if(!sSchedulerSimulationStartTime.empty()){
		m_start=time_from_string(sSchedulerSimulationStartTime.c_str());
		printf("---- SchedulerSimulationStartTime=%s ----\n",to_simple_string(m_start).c_str());
	}
}

PTIME Scheduler::getCurrentTime()
{
	static T_DURATION dt(boost::date_time::not_a_date_time);
	PTIME now=boost::posix_time::second_clock::local_time();
	if(!m_start.is_not_a_date_time()){
		if(dt.is_not_a_date_time()){
			dt=m_start-now;
		}
		now+=dt;
	}
	return now;
}


Scheduler::~Scheduler(){
	m_bIsRunning=false;
	while(m_pSchedulerThread!=0) MilliSleep(1000);
	clear();
	delete[] m_spLastMessages;
}

void Scheduler::addLatestMessage(std::string*psLastMessage){
	int i;
	static int n=NUM_MESSAGES_STORED-1;
	if(m_spLastMessages[n])delete m_spLastMessages[n];
	for(i=n;i>0;i--){
		m_spLastMessages[i]=m_spLastMessages[i-1];
	}
	m_spLastMessages[0]=psLastMessage;
}

std::string Scheduler::GetLastMessages()
{
	std::string sMsg;
	for(int i=0;i<NUM_MESSAGES_STORED;i++){
		if(m_spLastMessages[i]==0)break;
		sMsg += *m_spLastMessages[i]+"\n";
	}
	return sMsg;
}

namespace {
class RunScheduler {
public:
	RunScheduler(konga::Scheduler* pScheduler, konga::Logger* pLogger, FILE* debug_log) :
	  m_pScheduler(pScheduler), m_pLogger(pLogger), m_debug_log(debug_log) {}
	~RunScheduler(void) {}

	void operator()() {
		assert(m_pScheduler);
		assert(m_pLogger);
		KongaExceptionHandler handler(Logger::GetLogFileName("Scheduler"));
		try {
			const long SleepInSeconds=konga::GetOptionAsInt(stSCHEDULER_COMPONENT_NAME, "SleepTimeInSeconds", 5);
			if(m_debug_log){
				fprintf(m_debug_log,"SleepInSeconds=%ld\n",SleepInSeconds);
				fflush(m_debug_log);
			}
			while( m_pScheduler->IsRunning() ) {
				if(m_debug_log){
					fprintf(m_debug_log,"%s gCheck()\n", to_simple_string(m_pScheduler->getCurrentTime()).c_str());
					fflush(m_debug_log);
				}
				gcheck();
				MilliSleep(SleepInSeconds*1000);
			}
		} catch(const CrException& e) {
			m_pLogger->LogError(e.GetErrorString());
		}
        try { etlforge::cluster::releaseSchedulerHeartbeat(); }
        catch(const CrException& e) { m_pLogger->LogError("Cannot release Scheduler registration: " + e.GetErrorString()); }
		m_pLogger->Log("Scheduler exiting. No more events will be scheduled.");
		m_pScheduler->setRunning(false);
		m_pScheduler->killThread();
	}

private:
	PTIME m_time_to_wake_up;
	void gcheck()
	{
        try {
            etlforge::cluster::schedulerHeartbeat();
            const std::string revision=etlforge::cluster::scheduleRevision();
            if(revision!=m_scheduleRevision) { m_pScheduler->setDirty();m_scheduleRevision=revision; }
        }
        catch(const CrException& e) { m_pLogger->LogError("Scheduler cluster heartbeat failed; no scheduling this tick: " + e.GetErrorString()); return; }
		if( m_pScheduler->isDirty()){
			m_pScheduler->ResetLastErr();
			try {
                const PTIME current=m_pScheduler->getCurrentTime();
                const PTIME reloadFrom=(!m_time_to_wake_up.is_not_a_date_time() &&
                    m_time_to_wake_up<=current && m_time_to_wake_up>=current-seconds(30)) ? m_time_to_wake_up : current;
				m_pScheduler->init(reloadFrom);
				m_pLogger->Log("Scheduler was initialized");
			}
			catch( const CrException& ex ) {
				m_pLogger->LogError("Failed to initialize Scheduler: " + ex.GetErrorString());
				MilliSleep(10000);
				return;
			}
			m_time_to_wake_up=m_pScheduler->nextScheduledTime();
			if(m_debug_log){
				fprintf(m_debug_log,"Scheduler.init(), m_time_to_wake_up=%s\n",
					boost::posix_time::to_simple_string(m_time_to_wake_up).c_str());
				fflush(m_debug_log);
			}
		}
		ptime now=m_pScheduler->getCurrentTime();

	#ifdef print_debug
		printf("%s\n",boost::posix_time::to_simple_string(now).c_str());
	#endif

		if(!m_time_to_wake_up.is_not_a_date_time() && now >= m_time_to_wake_up){
			if(m_debug_log){
				fprintf(m_debug_log,"Scheduler.check(%s)", boost::posix_time::to_simple_string(now).c_str());
				fflush(m_debug_log);
			}
			try {
				m_time_to_wake_up=m_pScheduler->check(now);
			}
			catch( const CrException& ex ) {
				m_pLogger->LogError("Failed to check next scheduled event: " + ex.GetErrorString());
				MilliSleep(10000);
				return;
			}
			if(m_debug_log){
				fprintf(m_debug_log,", g_time_to_wake_up=%s\n",
					boost::posix_time::to_simple_string(m_time_to_wake_up).c_str());
				fflush(m_debug_log);
				printf("g_time_to_wake_up=%s\n", boost::posix_time::to_simple_string(m_time_to_wake_up).c_str());
			}
		}
	}

private:
	std::string m_scheduleRevision;
	konga::Scheduler* m_pScheduler;
	konga::Logger* m_pLogger;
	FILE* m_debug_log;
};
}

void Scheduler::start() {
	                         
	m_bIsRunning=true;
	if( !m_pSchedulerThread )
		m_pSchedulerThread = new boost::thread(RunScheduler(this, m_pLogger, m_debug_log));
}

void Scheduler::killThread() {
	if( m_pSchedulerThread ) {
		delete m_pSchedulerThread;
		m_pSchedulerThread=0;
	}
}

void Scheduler::clear(){
	for(SOVector::iterator it=m_ScheduledOps.begin();it!=m_ScheduledOps.end();it++){
		delete *it;
	} 
	m_ScheduledOps.clear();

	for(int i=0;i<NUM_MESSAGES_STORED;i++){
		if(m_spLastMessages[i]!=0){
			delete m_spLastMessages[i];
			m_spLastMessages[i]=0;
		}
	}
}

namespace {
	void FillDisabledMap(map<long, bool>& disabled, const map<long, KeyValues>& allProperties, const string& enabledKeyName) {
		for( map<long, KeyValues>::const_iterator cit = allProperties.begin(); cit != allProperties.end(); ++cit ) {
			const long lItemId = cit->first;
			const KeyValues& keyValues = cit->second;

			const bool bEnabled = keyValues.GetBool(enabledKeyName, true);
			if ( !bEnabled ) disabled[lItemId] = true;
		}
	}
}

void Scheduler::init()
{
	return init(this->getCurrentTime());
}

namespace {
	konga::Logger* g_pLogger = 0;
}

void LogErrorString(const string& sMsg) {
	if ( g_pLogger ) g_pLogger->LogError(sMsg);
}

void Scheduler::init(PTIME now)
{
	const string sConnectionString(m_TranDbinfo.connectionString());
	g_pLogger = m_pLogger;
	SmartDbPtr<DbConnection> pTranDbConn(
		konga::GetDbConnectionWithRetries(sConnectionString, LogErrorString)
		);

	clear();

	map<long, bool> disabledSchedules;
	map<long, bool> disabledOperationSchedules;
	try {
		ItemProperties itemProps(pTranDbConn);

		                                       
		map<long, KeyValues> allProperties;
		itemProps.GetItemPropertiesForEntityType(ScheduleEntityId, allProperties);
		FillDisabledMap(disabledSchedules, allProperties, SCHEDULE_ENABLED);

		                                                   
		allProperties.clear();
		itemProps.GetItemPropertiesForEntityType(OperationEntityId, allProperties);
		FillDisabledMap(disabledOperationSchedules, allProperties, OPERATION_SCHEDULE_ENABLED);
	}
	catch( const CrException& ex ) {
		m_lastErr = ex.GetErrorString();
		throw;
	}

    SmartDbPtr<ScheduledOperationView> sov( ScheduledOperationView::MakeInstance(pTranDbConn) );
	try {
		sov->Select();
	}
	catch( const DbException& e)
	{
		m_lastErr = e.GetErrorString();
		throw CrException(m_lastErr);
	}

	GREG_DATE scheduled_date;

	m_lastErr.clear();
	while( sov->Next() ){
		try {
			ScheduledOp* pSO=new ScheduledOp(now,this,*sov,pTranDbConn,m_pLogger, m_debug_log);
			if ( disabledSchedules.find(pSO->getScheduleId()) != disabledSchedules.end() ) {
				delete pSO;
				continue;
			}
			if ( disabledOperationSchedules.find(pSO->getOperationId()) != disabledOperationSchedules.end() ) {
				delete pSO;
				continue;
			}

			m_ScheduledOps.push_back(pSO);
		} catch(CrException&e){
			m_lastErr+=e.GetErrorString();
		}
	}
	if(!m_lastErr.empty()){
		m_pLogger->LogError(m_lastErr);
	}
	m_bIsDirty=false;
}

                                                                           
PTIME Scheduler::check(PTIME now)
{
	                                          
	for(SOVector::iterator it=m_ScheduledOps.begin();it!=m_ScheduledOps.end();it++){
		ScheduledOp*pSO=*it;
		pSO->checkOp(now);
	}
	return nextScheduledTime();
}

                                                
PTIME Scheduler::nextScheduledTime()
{
	PTIME tmin;
	for(SOVector::iterator it=m_ScheduledOps.begin();it!=m_ScheduledOps.end();it++){
		const PTIME& p=(*it)->getScheduledDateTime();
		if(p.is_not_a_date_time())continue;
		if(tmin.is_not_a_date_time())tmin=p;
		else if(tmin>p)tmin=p;
	}
	return tmin;
}



