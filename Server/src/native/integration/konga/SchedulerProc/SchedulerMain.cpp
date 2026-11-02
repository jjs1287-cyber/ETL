/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#include <signal.h>

#include <exception>

#include <konga/Utils/AutoFileCleanup.h>	                       
#include <konga/Utils/FileUtils.h>
#include <konga/Utils/Settings.h>
#include <konga/Utils/StringUtils.h>
#include <konga/Utils/StringTokenizer.h>
#include <konga/Utils/DateUtils.h>
#include <konga/Utils/KeyValues.h>

#include <konga/ServerVersion/ServerVersion.h>

#include <konga/Log/logging.h>

#include <konga/Networking/Communicator.h>

#include <konga/WinUtils/WinErrorUtils.h>	                        

#include <konga/Scheduler/ComponentInfo.h>
#include <konga/Scheduler/Scheduler.h>

                                                                  
int	rsSTOPPED	= 1,
	rsRUNNING	= 2,
	rsSTOPPING	= 4,
	rsERROR	= 8;

using namespace konga;
using namespace networking;
using std::string;

                                            

namespace {
	konga::Scheduler* g_pScheduler=0;
	Communicator* g_pCommunicator=0;
}

                                                                                                    
extern string g_stEventSource;

namespace {

	const string SCHEDULER_EVENT_SOURCE("Scheduler");

	konga::Logger g_logger(SCHEDULER_EVENT_SOURCE, default_log_severity, EVENTLOG_INFORMATION_TYPE);

	Result* gStart(const Command& cmd)
	{
		Result* pResult = new Result(cmd.GetCommandName());
		string line;
		if(g_pScheduler->IsRunning()){
			line="Scheduler is already running.";
		} else {
			g_pScheduler->start();
			line="Scheduler is starting.";
		}
		pResult->AddReturnValue(line);
		g_logger.Log("Start: "+line);
		return pResult;
	}

	Result* gStop(const Command& cmd)
	{
		Result* pResult = new Result(cmd.GetCommandName());
		string line;
		if(g_pScheduler->IsStopped()){
			line = "Scheduler is not running.";
		} else {
			g_pScheduler->stopping();
			line = "Scheduler is stopping.";
		}
		pResult->AddReturnValue(line);
		g_logger.Log("Stop: "+line);
		return pResult;
	}
	Result* gShutdown(const Command& cmd) {
		g_pScheduler->stopping();
		g_pCommunicator->ShouldExit();
		Result* result = new Result(cmd.GetCommandName());
		result->AddReturnValue("Scheduler is shutting down.");
		return result;
	}

	Result* gGetStatus(const Command& cmd)
	{
		Result* pResult = new Result(cmd.GetCommandName());

		unsigned long lRet = 0;
		if( g_pScheduler->IsRunning() ) lRet |= rsRUNNING;
		else if(g_pScheduler->IsStopped()) lRet |= rsSTOPPED;
		else lRet |= rsSTOPPING;
		if( !g_pScheduler->GetLastErr().empty()) lRet |= rsERROR;

		string line=toString(lRet);
		pResult->AddReturnValue(line);
		g_logger.Log("Scheduler Status Reported: "+line);

		string sDesc;
		if( lRet & rsERROR ){
			sDesc = g_pScheduler->GetLastErr();
		} else if( lRet & rsRUNNING ) {
			sDesc = g_pScheduler->GetLastMessages()+" Scheduler is running.";
		} else if( lRet & rsSTOPPED ){
			sDesc = g_pScheduler->GetLastMessages()+" Scheduler is stopped.";
		} else if(lRet & rsSTOPPING){
			sDesc = g_pScheduler->GetLastMessages()+" Scheduler is stopping.";
		}

		pResult->AddReturnValue(sDesc);
		g_logger.Log("Scheduler Status Description: "+sDesc);
		return pResult;
	}

	Result* gUpdate(const Command&cmd)
	{
		g_pScheduler->setDirty();

		Result* pResult = new Result(cmd.GetCommandName());
		string line;
		if(g_pScheduler->IsRunning()){
			line="Scheduler will be updated";
		} else {
			line="Scheduler is not running";
		}
		pResult->AddReturnValue(line);
		g_logger.Log("Update: "+line);
		return pResult;
	}
}

void SignalHandler(int iSig) {
	switch(iSig) {
	case SIGINT:
		printf("Caught Ctrl-C. Goodbye... \n");
		g_logger.Log("Terminating Scheduler.");
		g_pScheduler->stopping();
		g_pCommunicator->ShouldExit();
		break;
                 
                                    
                                                        
                             
          
	}
}

struct KTimeZone
{
	string id;
	long offset,dstSavings;
	bool useDaylight;
	int transitions;
	int startYear;
	int startMode,startMonth,startDay,startDayOfWeek;
	long startTime;
	int startTimeMode;
	int endMode,endMonth,endDay,endDayOfWeek;
	long endTime;
	int endTimeMode;
	KTimeZone(const char*line);
                                 
};

struct KTimeZoneMap
{
	std::map<std::string, KTimeZone*> tz_map;
	void add(KTimeZone*tz){
		tz_map[tz->id]=tz;
	}
	~KTimeZoneMap(){
		for(std::map<string, KTimeZone*>::iterator it=tz_map.begin();
			it!=tz_map.end();it++){
				delete it->second;
		}
	}
	KTimeZone* getTimeZoneById(const string&id){
		return tz_map.find(id)->second;
	}
};

KTimeZone::KTimeZone(const char*line){
	StringTokenizer tk(line,"=,[]\"");
	string param,token0;
	while(tk.NextToken()){
		string token(tk.token());
		switch(token.at(0)){
		case '[':
			break;
		case '=':
			param=token0;
			break;
		case ']':
		case ',':
			param="";
			break;
		case '"':
			token="";
			while(tk.NextToken()){
				string tmp=tk.token();
				if(tmp.at(0)=='"')break;
				token+=tmp;
			}
			break;
		default:
			                
			if(strcmp(token.c_str(),"sun.util.calendar.ZoneInfo")==0||
				strcmp(token.c_str(),"java.util.SimpleTimeZone")==0||
				strcmp(token.c_str(),"lastRule")==0){
				token="";
			} else if(!param.empty()){
				if(param=="id")id=token;
				else if(param=="offset")offset=atol(token.c_str());
				else if(param=="dstSavings")dstSavings=atol(token.c_str());
				else if(param=="useDaylight")useDaylight=strcmp(token.c_str(),"true")==0;
				else if(param=="transitions")transitions=atoi(token.c_str());
				else if(param=="startYear")startYear=atoi(token.c_str());
				else if(param=="startMode")startMode=atoi(token.c_str());
				else if(param=="startMonth")startMonth=atoi(token.c_str());
				else if(param=="startDay")startDay=atoi(token.c_str());
				else if(param=="startDayOfWeek")startDayOfWeek=atoi(token.c_str());
				else if(param=="startTime")startTime=atol(token.c_str());
				else if(param=="startTimeMode")startTimeMode=atoi(token.c_str());
				else if(param=="endMode")endMode=atoi(token.c_str());
				else if(param=="endMonth")endMonth=atoi(token.c_str());
				else if(param=="endDay")endDay=atoi(token.c_str());
				else if(param=="endDayOfWeek")endDayOfWeek=atoi(token.c_str());
				else if(param=="endTime")endTime=atol(token.c_str());
				else if(param=="endTimeMode")endTimeMode=atoi(token.c_str());
				else
				{
					                                         
				}
			}
		}
		token0=token;
	}
}


                                                                                           
int readTZIF(const char* tzfile_name)
{
	throw CrException("int readTZIF(const char* tzfile_name) -- not yet implemented");
	FILE*tzfile=fopen(tzfile_name,"r");
	if(tzfile==0)throw CrException(string("can't open tzfile ")+tzfile_name);
	AutoFileHandle adopt_tzfile(tzfile);
	char line[4096];
	KTimeZoneMap tz_map;
	while(fgets(line,4095,tzfile)!=0){
		KTimeZone* tz=new KTimeZone(line);
		tz_map.add(tz);
	}
	return 0;
}

namespace {
	string getCurrentTime() {
		return CrDateTime::GetCurrentTime().toString();
	}
}

#ifdef _DEBUG
                                   
#endif

#ifndef debug_ScheduleOpCollector
int main(int argc, char* argv[])
{
	g_stEventSource = SCHEDULER_EVENT_SOURCE;
	KongaExceptionHandler handler( Logger::GetLogFileName(SCHEDULER_EVENT_SOURCE) );
	srand( (unsigned)time(0) );                            

	if ( CommandLineContainsVersion(argc, argv) ) exit(0);

	                                                           
	FILE* debug_log = 0;
	if ( konga::GetOptionAsBoolean("Scheduler", "WriteLog", false) ) {
		                    
		const string sDebugLogFileName( konga::getTmpDir() + "scheduler_debug.log" );
		debug_log=fopen(sDebugLogFileName.c_str(),"w+");
		if ( debug_log ) {
			fprintf(debug_log, "Scheduler starting at %s\n", getCurrentTime().c_str());
			fflush(debug_log);
		}
	}
	AutoFileHandle adopt_debug_log(debug_log);

	try {
		SetCommandLineOptions(argc, argv);

		Scheduler scheduler(&g_logger, debug_log);
		g_pScheduler=&scheduler;

		Communicator communicator("Scheduler", lSCHEDULER_DEFAULT_PORT, report_default);
		g_pCommunicator=&communicator;
		communicator.getCommandListener()->AddCommandFunction("Start", gStart);
		communicator.getCommandListener()->AddCommandFunction("Stop", gStop);
		communicator.getCommandListener()->AddCommandFunction("Shutdown", gShutdown);
		communicator.getCommandListener()->AddCommandFunction("Update", gUpdate);
		communicator.getCommandListener()->AddCommandFunction("GetStatus", gGetStatus);

		signal(SIGINT, SignalHandler);

		if( konga::GetOptionAsBoolean(stSCHEDULER_COMPONENT_NAME, "AutoStart", true) ) {
			g_pScheduler->start();
		}
		communicator.WaitUntilExit();
		if ( debug_log ) {
			fprintf(debug_log, "Scheduler exiting at %s", getCurrentTime().c_str());
			fflush(debug_log);
		}
	} catch(const CrFatalException& e) {
		g_logger.LogError(e.GetErrorString());
		if ( debug_log ) {
			fprintf(debug_log, "%s\nScheduler exiting after CrFatalException:\n%s",
				getCurrentTime().c_str(), e.GetErrorString().c_str());
			fflush(debug_log);
		}
		return e.GetErrorNumber();
	} catch(const CrException& e) {
		g_logger.LogError(e.GetErrorString());
		if ( debug_log ) {
			fprintf(debug_log, "%s\nScheduler exiting after unexpected CrException was caught:\n%s",
				getCurrentTime().c_str(), e.GetErrorString().c_str());
			fflush(debug_log);
		}
		return e.GetErrorNumber();
	}
	catch ( const std::exception& ex ) {
		string err(ex.what());
		g_logger.LogError(err);
		if ( debug_log ) {
			fprintf(debug_log, "%s\nScheduler exiting after unexpected std::exception was caught:\n%s",
				getCurrentTime().c_str(), err.c_str());
			fflush(debug_log);
		}
	}
}
#else
#include <boost/date_time/posix_time/posix_time.hpp>
#include <boost/date_time/gregorian/gregorian.hpp>
#include <boost/date_time/local_time_adjustor.hpp>
#include <boost/date_time/c_local_time_adjustor.hpp>
typedef boost::gregorian::date GREG_DATE;
typedef boost::posix_time::ptime PTIME;
typedef boost::posix_time::time_duration T_DURATION;
#include <konga/Scheduler/ScheduledOpCollector.h>
using namespace boost::posix_time;
using namespace konga;

#define debug_QueueTimeList 1

int main(int argc, char* argv[]){
	try{
#if debug_QueueTimeList == 0
	PTIME now=time_from_string("2008-12-19 09:30"), end=time_from_string("2009-12-31 09:30");
	std::vector<ScheduledItem> v;
	konga::ScheduledOpCollector collector;
	collector.collect(v, now, end);
	printf("now=%s\n", to_simple_string(now).c_str());
	printf("end=%s\n", to_simple_string(end).c_str());
	for(std::vector<ScheduledItem>::iterator it=v.begin(); it!=v.end(); it++){
		ScheduledOp* so=it->m_pScheduledOp;
		PTIME qtime=it->m_qtime;
		printf("%s %s - sch%d %s\top%d\n",
			to_simple_string(qtime).c_str(),
			qtime.date().day_of_week().as_short_string(),
			so->getScheduleId(),
			so->getScheduleName().c_str(),
			so->getOperationId());
	}
#elif debug_QueueTimeList == 1
	PTIME now=time_from_string("2009-01-19 12:01");
	std::vector<PTIME> qlist;
	konga::KeyValues schedule_properties;

           
	                                                     
	                                     
	                           
	                                          
	                                                                               
	schedule_properties.AddString("schedule_name","test1");
	schedule_properties.AddInt("occurs",	3);
           
	schedule_properties.AddInt("daily",		1);
           
	schedule_properties.AddInt("weekly",	1);
	schedule_properties.AddInt("mon",		-1);
	schedule_properties.AddInt("tue",		-1);
	schedule_properties.AddInt("wed",		1);
	schedule_properties.AddInt("thu",		-1);
	schedule_properties.AddInt("fri",		-1);
	schedule_properties.AddInt("sat",		-1);
	schedule_properties.AddInt("sun",		1);
           
	schedule_properties.AddInt("monthly",					0);
	            
	schedule_properties.AddInt("monthly_a_day",				9);
	schedule_properties.AddInt("monthly_a_of_every_x_month",1);
	            
	schedule_properties.AddInt("monthly_b_num",				1);
	schedule_properties.AddInt("monthly_b_day",				-1);
	schedule_properties.AddInt("monthly_b_of_every_x_month",1);

	schedule_properties.AddInt("frequency",					0);
              
	schedule_properties.AddString("frequency_once_time",	"");
              
	schedule_properties.AddInt("frequency_every_num",		1);
	schedule_properties.AddInt("frequency_every_hm",		60);
	schedule_properties.AddString("frequency_start_time",	"12:00:00");
	schedule_properties.AddString("frequency_end_time",		"13:00:00");

	schedule_properties.AddString("duration_start_date",	"2008-12-31");
	schedule_properties.AddString("duration_end_date",		"");
	schedule_properties.AddInt("duration_yesno_end",		0);

	schedule_properties.AddInt("priority",					5);
                                                     
	schedule_properties.AddInt("new_monthly_a_day",			0);
	schedule_properties.AddInt("monthly_a_day_working",		0);
	schedule_properties.AddString("holiday_list","2009-01-01,2009-02-16,2009-05-25,2009-07-03,2009-09-07,2009-11-26,2009-11-27,2009-12-25");

	int i=1, n=20;
	printf("schedule_properties:\n%s\n", schedule_properties.toString().c_str());

	printf("now=%s\n", to_simple_string(now).c_str());
	printf("n=%d\n", n);

	QueueTimeList(qlist, now, schedule_properties,n);
	for(std::vector<PTIME>::iterator it=qlist.begin(); it!=qlist.end(); it++, i++){
		PTIME qtime=*it;
		printf("%d %s %s\n",i,to_simple_string(qtime).c_str(),
			qtime.date().day_of_week().as_short_string());
	}
#elif debug_QueueTimeList == 2
	PTIME now=time_from_string("2009-05-01 09:30");
	std::vector<PTIME> qlist;
	int i=1, n=20;
	int scheduleId=24;
	QueueTimeList(qlist, now, scheduleId, n);
	printf("scheduleId=%d\n", scheduleId);
	printf("now=%s\n", to_simple_string(now).c_str());
	printf("n=%d\n", n);
	for(std::vector<PTIME>::iterator it=qlist.begin(); it!=qlist.end(); it++, i++){
		PTIME qtime=*it;
		printf("%d %s %s\n",i,to_simple_string(qtime).c_str(),
			qtime.date().day_of_week().as_short_string());
	}
#endif
	}
	catch(const CrException& e){
		printf("%s\n",e.GetErrorString().c_str());
	}
	char c;
	scanf("%c",&c);
}
#endif

