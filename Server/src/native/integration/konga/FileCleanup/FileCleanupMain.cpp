/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <signal.h>
#include <exception>

#include <konga/Db/DbUtils/DbUtils.h>		                               

#include <konga/Utils/DBConnectionInfo.h>
#include <konga/Utils/FileUtils.h>
#include <konga/Utils/ProcessUtils.h>
#include <konga/Utils/Settings.h>		                      
#include <konga/Utils/StringUtils.h>

#include <konga/Log/logging.h>

#include <konga/ServerVersion/ServerVersion.h>

#include <konga/Networking/Communicator.h>

#include "CleanupRule.h"
#include "../../../ssh/SshKeys.h"
#include "OperationRetentionRule.h"
#include "FileCleanupMain.h"
#include "RuleParser.h"
#include "CleanupUtils.h"
#include "resource.h"
#include "ComponentInfo.h"

                                                                  
int	rsSTOPPED	= 1,
	rsRUNNING	= 2,
	rsSTOPPING	= 4,
	rsERROR	= 8;

using namespace konga;
using namespace konga::networking;
using namespace std;
using namespace CleanupUtils;

                   
std::atomic<bool> g_cancel{false};
int g_log_type;
extern std::string g_stEventSource;

namespace {
	Communicator* g_pCommunicator=0;
	FileCleanupMain* g_pCleanupService;
	std::atomic<bool> g_doNow{true};
}

                    
namespace {
	Result* gStart(const Command& cmd)
	{
		g_pCleanupService->start();

		Result* pResult = new Result(cmd.GetCommandName());
		std::string line="Cleanup Service is starting.";
		pResult->AddReturnValue(line);
		ReportError( g_log_type, line, EVENTLOG_INFORMATION_TYPE);
		return pResult;
	}

	Result* gStop(const Command& cmd)
	{
		g_pCleanupService->stopping();

		Result* pResult = new Result(cmd.GetCommandName());
		std::string line="Cleanup Service is stopping.";
		pResult->AddReturnValue(line);
		ReportError( g_log_type, line, EVENTLOG_INFORMATION_TYPE);
		return pResult;
	}

	Result* gShutdown(const Command& cmd) {
		g_pCleanupService->stopping();
		g_pCleanupService->cancel();
		g_pCommunicator->ShouldExit();
		Result* result = new Result(cmd.GetCommandName());
		result->AddReturnValue("Cleanup is shutting down.");
		return result;
	}
	Result* gCancel(const Command& cmd){
		g_pCleanupService->cancel();

		Result* pResult = new Result(cmd.GetCommandName());
		std::string line="Cleanup Service has been cancelled.";
		pResult->AddReturnValue(line);
		ReportError( g_log_type, line, EVENTLOG_INFORMATION_TYPE);
		return pResult;
	}

	Result* gGetStatus(const Command& cmd)
	{
		Result* pResult = new Result(cmd.GetCommandName());

		unsigned long lRet = 0;
		if( g_pCleanupService->IsRunning() ) lRet |= rsRUNNING;
		else if(g_pCleanupService->IsStopped()) lRet |= rsSTOPPED;
		else lRet |= rsSTOPPING;

		pResult->AddReturnValue(toString(lRet));

		std::string sDesc = "";
		if( lRet & rsRUNNING ) {
			sDesc = "Cleanup Service is running.";
		} else if( lRet & rsSTOPPED ){
			sDesc = "Cleanup Service is stopped.";
		} else if(lRet & rsSTOPPING){
			sDesc = "Cleanup Service is stopping.";
		}

		pResult->AddReturnValue(sDesc);
		ReportError( g_log_type, sDesc, EVENTLOG_INFORMATION_TYPE);
		return pResult;
	}

	Result*gUpdate(const Command&cmd)
	{
		g_pCleanupService->setDirty();

		Result* pResult = new Result(cmd.GetCommandName());
		string line="Update() at "+toLocalTimeString(0);
		pResult->AddReturnValue(line);
		ReportError( g_log_type, line, EVENTLOG_INFORMATION_TYPE);
		return pResult;
	}

	Result*gDoNow(const Command&cmd)
	{
		g_doNow=true;

		Result* pResult = new Result(cmd.GetCommandName());
		string line="DoNow() at "+toLocalTimeString(0);
		pResult->AddReturnValue(line);
		ReportError( g_log_type, line, EVENTLOG_INFORMATION_TYPE);
		return pResult;
	}
}

void SignalHandler(int iSig) {
	switch(iSig) {
	case SIGINT:
		printf("Caught Ctrl-C. Goodbye... \n");
		ReportError( g_log_type, "Terminating Cleanup Service.", EVENTLOG_INFORMATION_TYPE);
		g_pCleanupService->stopping();
		g_pCommunicator->ShouldExit();
		break;
	}
}

void dbErrLogger(const string& sMsg) {
	ReportError(g_log_type, sMsg);
}

int main(int argc, char* argv[])
{
	if ( CommandLineContainsVersion(argc, argv) ) exit(0);

	try {
		SetCommandLineOptions(argc, argv);

		g_stEventSource = "CleanupService";
		g_log_type = report_file | report_trace;

		                                                    
		delete GetDbConnectionWithRetries(DBConnectionInfo("TranDb").connectionString(), dbErrLogger);

		FileCleanupMain cleanup;
		g_pCleanupService=&cleanup;

		Communicator communicator(stFILE_CLEANUP_COMPONENT_NAME, lFILE_CLEANUP_DEFAULT_PORT, g_log_type);
		g_pCommunicator=&communicator;
		communicator.getCommandListener()->AddCommandFunction("Start", gStart);
		communicator.getCommandListener()->AddCommandFunction("Shutdown", gShutdown);
		communicator.getCommandListener()->AddCommandFunction("Stop", gStop);
		communicator.getCommandListener()->AddCommandFunction("Update", gUpdate);
		communicator.getCommandListener()->AddCommandFunction("DoNow", gDoNow);
		communicator.getCommandListener()->AddCommandFunction("Cancel", gCancel);
		communicator.getCommandListener()->AddCommandFunction("GetStatus", gGetStatus);

		signal(SIGINT, SignalHandler);

		if( konga::GetOptionAsBoolean(stFILE_CLEANUP_COMPONENT_NAME, "AutoStart", true) ) {
			cleanup.start();
			ReportError( g_log_type, "File Cleanup Service started", EVENTLOG_INFORMATION_TYPE);
		}
		communicator.WaitUntilExit();
	} catch(CrFatalException& ex) {
		ReportError( g_log_type, "A fatal error occurred: " + ex.GetErrorString(), EVENTLOG_ERROR_TYPE);
		return ex.GetErrorNumber();
	} catch(CrException& ex) {
		ReportError( g_log_type, "A severe error occurred: " + ex.GetErrorString(), EVENTLOG_ERROR_TYPE);
		return ex.GetErrorNumber();
	}
}

void gcheck()
{
}

namespace konga {
void RunFileCleanupService() {
	g_pCleanupService->setDirty();
	try {
		std::string tmp = konga::GetOptionAsString(stFILE_CLEANUP_COMPONENT_NAME, "SleepTimeInSeconds", "5");
		long SleepInSeconds=atoi(tmp.c_str());
		while( g_pCleanupService->IsRunning() ) {
			if( g_pCleanupService->isDirty()){
				g_pCleanupService->init();
				std::string line("Cleanup Service initialized, next execution time is: "+toLocalTimeString(g_pCleanupService->getWakeupTime()));
				ReportError( g_log_type, line, EVENTLOG_INFORMATION_TYPE);
			}

			time_t now=CleanupUtils::GetCurrentTime();
			if(g_doNow|| now >= g_pCleanupService->getWakeupTime()){
				std::string line("Starting cleanup process");
				ReportError( g_log_type, line, EVENTLOG_INFORMATION_TYPE);
				g_pCleanupService->execute();
				line = "Finished cleanup process, next execution time is: " + toLocalTimeString(g_pCleanupService->getWakeupTime());
				ReportError( g_log_type, line, EVENTLOG_INFORMATION_TYPE);
			}
			g_doNow=false;
			g_cancel=false;
			MilliSleep(SleepInSeconds*1000);
		}
	} catch(CrFatalException& e) {
		ReportError( g_log_type, "A fatal error occurred: "+e.GetErrorString(), EVENTLOG_ERROR_TYPE);
	} catch(CrException& e) {
		ReportError( g_log_type, "A severe error occurred: "+e.GetErrorString(), EVENTLOG_ERROR_TYPE);
	}
	g_pCleanupService->stopping();
	g_pCleanupService->finished();
}
}

FileCleanupMain::FileCleanupMain() 
	: m_bIsRunning(false), m_Dirty(true),
	m_pLoopThread(0), m_lastExeTime(0), m_wakeup(0),
	m_nFreq(CleanupUtils::GetCleanupFrequency())
{
	std::string stPath=GetServerHomeWithSlash();

	m_stXmlCleanupRules=stPath+"CleanupRules.xml";
}

FileCleanupMain::~FileCleanupMain(){
    stopping();
    cancel();
    killThread();
    eraseRules();
}
void FileCleanupMain::start() {
    if (m_bIsRunning || !m_stopped) return;
    killThread();
    m_stopped=false;
    m_bIsRunning=true;
    m_pLoopThread = new boost::thread(RunFileCleanupService);
}
void FileCleanupMain::killThread() {
    if (m_pLoopThread) {
        m_pLoopThread->join();
        delete m_pLoopThread;
        m_pLoopThread=0;
    }
}

void FileCleanupMain::eraseRules(){
	for(std::vector<CleanupRule*>::iterator it=m_rules.begin();
		it!=m_rules.end();it++){
			delete *it;
	}
	m_rules.clear();
}

namespace {
	void LogWarnings(const vector<string>& warnings) {
		for( vector<string>::const_iterator cit = warnings.begin();
			cit != warnings.end(); ++cit )
		{
			const string& warning = *cit;
			ReportError( g_log_type, warning, EVENTLOG_WARNING_TYPE);
		}
	}
}

void FileCleanupMain::init()
{
	eraseRules();
	m_rules.push_back(new OperationRetentionRule());
	try {
		RuleParser parser(m_stXmlCleanupRules);
		parser.InitCleanupRules(m_rules);
		LogWarnings(parser.GetWarnings());
	}
	catch( const CrException& ex ) {
		ReportError( g_log_type, ex.GetErrorString(), EVENTLOG_ERROR_TYPE);
	}

	vector<string> warnings;
	for(std::vector<CleanupRule*>::iterator it=m_rules.begin();
		it!=m_rules.end();it++){
		(*it)->ValidateRule(warnings);
	}
	LogWarnings(warnings);

	m_Dirty=false;
	m_wakeup=CleanupUtils::GetCurrentTime();
}

void FileCleanupMain::cancel()
{
	g_cancel=true;
}

void FileCleanupMain::execute(){
    try{etlforge::ssh::cleanup(konga::GetServerHomeWithSlash()+"private/ssh-keys");}catch(const std::exception& ex){ReportError(g_log_type,std::string("SSH key cleanup: ")+ex.what(),EVENTLOG_ERROR_TYPE);}

	if(g_cancel){
		std::string line = "Cleanup operation cancelled.";
		ReportError( g_log_type, line, EVENTLOG_INFORMATION_TYPE);
		g_cancel=false;
		return;
	}

	for(std::vector<CleanupRule*>::iterator it=m_rules.begin(); it!=m_rules.end();it++) {
		CleanupRule* rule = *it;
		if ( rule == 0 ) continue;

		vector<string> warnings;
		try {
			if (!rule->Execute(warnings)){
				ReportError( g_log_type, "An error occurred while executing the '" + rule->getName() + "' cleanup rule.", EVENTLOG_ERROR_TYPE);
			}
		}
		catch( const CrException& e ) {
			ReportError( g_log_type, "An error occurred while executing the '" + rule->getName() + "' cleanup rule (CrException).", EVENTLOG_ERROR_TYPE);
			ReportError( g_log_type, e.GetErrorString(), EVENTLOG_ERROR_TYPE);
		}
		catch( std::exception& e ) {
			ReportError( g_log_type, "An unexpected error (std::exception) occurred while executing the '" + rule->getName() + "' cleanup rule.", EVENTLOG_ERROR_TYPE);
			ReportError( g_log_type, e.what(), EVENTLOG_ERROR_TYPE);
		}

		for( vector<string>::const_iterator cit = warnings.begin();
			cit != warnings.end(); ++cit)
		{
			ReportError( g_log_type, *cit, EVENTLOG_WARNING_TYPE);
		}
	}

	m_lastExeTime=m_wakeup.load();
	m_wakeup=nextScheduledTime();
}

time_t FileCleanupMain::nextScheduledTime()
{
	time_t currentTime=CleanupUtils::GetCurrentTime();
	time_t nFreqInSeconds = (long)(m_nFreq * 3600);

	return nFreqInSeconds + currentTime;
}

