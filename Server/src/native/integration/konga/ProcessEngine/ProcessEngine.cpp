/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#include <signal.h>

#include <iostream>
#include <set>

#include <boost/filesystem/path.hpp>                       

#include <konga/Utils/Settings.h>	                                                
#include <konga/WinUtils/WinErrorUtils.h>	                        

#include <konga/Log/logging.h>		              

#include <konga/Networking/CommandListenerUsingSockets.h>
#include <konga/Networking/Request.h>
#include <konga/Networking/Command.h>
#include <konga/Networking/Response.h>
#include <konga/Networking/Result.h>

#include <konga/Server/lmcheck.h>

#include <konga/PluginManager/PluginManager.h>
#include <konga/ServerVersion/ServerVersion.h>
#include <konga/TransformationEngine/KongaProtocolRpcInterface.h>                                                                               

#include "Globals.h"
#include "Engine/EngineCore.h"		                       
#include "ProcessEngine.h"
#include "ComponentInfo.h"

using namespace konga;
using namespace konga::networking;
using namespace std;

extern EngineCore *g_engine;
                           

ProcessEngine *g_pProcessEngine=0;

                                                                         
extern DBConnectionInfo g_mdb_connect;
extern DBConnectionInfo g_idb_connect;
extern DBConnectionInfo g_pdb_connect;
extern DBConnectionInfo g_trandb_connect;


void SignalHandler(int iSig) {
	switch(iSig) {
	case SIGINT:
		ReportError( g_log_type, "[Msg 457] Caught Ctrl-C. Initiating server shutdown...", EVENTLOG_INFORMATION_TYPE);
		g_pProcessEngine->Stop();
		g_pProcessEngine->ShouldExit();
		break;
	}

                
}

namespace {
	Result* gStart(const Command& cmd)
	{
		g_pProcessEngine->Start();

		Result* pResult = new Result(cmd.GetCommandName());
		pResult->AddReturnValue("Engine is running.");
		return pResult;
	}

	Result* gStop(const Command& cmd)
	{
		g_pProcessEngine->Stop();

		Result* pResult = new Result(cmd.GetCommandName());
		pResult->AddReturnValue("Engine is stopped.");
		return pResult;
	}
	Result* gShutdown(const Command& cmd) {
		g_pProcessEngine->Stop();
		g_pProcessEngine->ShouldExit();
		Result* result = new Result(cmd.GetCommandName());
		result->AddReturnValue("Process engine is shutting down.");
		return result;
	}

	Result* gGetStatus(const Command& cmd)
	{
		Result* pResult = new Result(cmd.GetCommandName());
		if( cmd.arg_size() != 1 )
			throw CrException("Wrong number of arguments for the Get Status command. Expected 1, got " + 
				toString(cmd.arg_size()) + ".");
		int iDetailLevel = atoi(cmd.arg_begin()->c_str());

		unsigned long lRet = 0;
		if( g_engine )
		{
			if( g_exiting ) lRet |= rsSTOPPING;
			if( g_engine->IsRunning() ) lRet |= rsRUNNING;
			else lRet |= rsSTOPPED;
			if( g_engine->IsSingleThreadMode() ) lRet |= rsSINGLE_THREAD;
			                                                                                  
			                                                                                             
		}
		else
			lRet |= rsSTOPPED;

		pResult->AddReturnValue(toString(lRet));

		string sDesc = "";
		if( lRet & rsRUNNING ) {
			int nTasks = 0;
			if( iDetailLevel == 0 )
				sDesc = "Process Engine is running.";
			else if( g_engine ) {
				nTasks = g_engine->GetNumberOfRunningTasks();
				sDesc = toString(nTasks) + " task(s) running.";
			}
		} else if( lRet & rsSTOPPED )
			sDesc = "Process Engine is stopped.";
		else
			sDesc = "No further information is available.";

		if( iDetailLevel !=0 ) {
			sDesc += "\nMaximum number of concurrent tasks: " + toString(g_max_threads);
			if( !g_status_str.empty() ) sDesc += "\nStatus: " + g_status_str;
		}

		pResult->AddReturnValue(sDesc);

		return pResult;
	}

	Result* gKillTasks(const Command& cmd)
	{
		Result* pResult = new Result(cmd.GetCommandName());

		Command::const_iterator citArg = cmd.arg_begin();
		set<int> msgids;
		for(; citArg != cmd.arg_end(); ++citArg)
			msgids.insert( atoi( citArg->c_str() ) );

		if( g_engine )
			g_engine->KillTasks(msgids);

		return pResult;
	}

	void engine_lic() {
		konga::lm* plm = konga::get_lm(g_trandb);
		if ( plm != 0 ) {
			try {
				plm->installCloudTrial();
			}
			catch( const CrException& e ) {
				ReportError( g_log_type, "[Msg 471] " + e.GetErrorString(), EVENTLOG_ERROR_TYPE);
			}
		}
	}
}

void ProcessEngine::CommandListenerFunction::operator()() {
	try {
		m_processEngine.m_commandListener.SetListeningPort(m_lListeningPort);
		m_processEngine.m_commandListener.AddCommandFunction("Start", gStart);
		m_processEngine.m_commandListener.AddCommandFunction("Stop", gStop);
		m_processEngine.m_commandListener.AddCommandFunction("Shutdown", gShutdown);
		m_processEngine.m_commandListener.AddCommandFunction("GetStatus", gGetStatus);
		m_processEngine.m_commandListener.AddCommandFunction("KillTasks", gKillTasks);
		ReportError( report_file|report_trace, "About to listen for commands on port "
			+ toString(m_lListeningPort) + ".", EVENTLOG_INFORMATION_TYPE);
		m_processEngine.m_commandListener.StartListening();
	} catch(CrException& e) {
		ReportError( g_log_type, "Failed to listen for commands on port "
			+ toString(m_lListeningPort) + ". Reason: " + e.GetErrorString());
	}
}

ProcessEngine::ProcessEngine() : m_pCommandListenerThread(0), m_commandListener(konga::stPROCESSENGINE_COMPONENT_NAME) {
	if( !ConnectDataSources() )
		throw CrFatalException(0, "Failed to connect to data sources. Must exit now.");

	long lCommandPort = konga::GetOptionAsInt(stPROCESSENGINE_COMPONENT_NAME, "CommandPort", lPROCESSENGINE_DEFAULT_PORT);
	m_pCommandListenerThread = new boost::thread(CommandListenerFunction(*this, lCommandPort));
}

ProcessEngine::~ProcessEngine() {
	m_commandListener.StopListening();
	if(m_pCommandListenerThread && m_pCommandListenerThread->joinable()) m_pCommandListenerThread->join();

	if( g_engine ) delete g_engine; g_engine = 0;
                                          

	delete m_pCommandListenerThread;

	DestroyDataSources();
}

void ProcessEngine::Start() {
	if( !g_engine )
		g_engine = new EngineCore(report_file);
	g_engine->Start();

                
                             
                   
}

void ProcessEngine::Stop() {	
	if( g_engine )
		g_engine->Stop();

               
                   
}

bool ProcessEngine::IsRunning() {
	if( g_engine )
		return g_engine->IsRunning();
	return false;
}

void ProcessEngine::WaitUntilExit() {
	boost::mutex::scoped_lock lock(m_exitMutex);
	while(!m_exitRequested) m_exitCond.wait(lock);
}

void ProcessEngine::ShouldExit() {
	boost::mutex::scoped_lock lock(m_exitMutex);
	m_exitRequested = true;
	m_exitCond.notify_all();
}

int main(int argc, char* argv[]) {
	g_stEventSource = "ProcessEngine";
#ifdef _DEBUG
	g_log_type = report_file | report_trace;
#else
	g_log_type = report_file;
#endif
	KongaExceptionHandler handler(g_stEventSource);

	if ( CommandLineContainsVersion(argc, argv) ) exit(0);

	                                                                                                                    
                                                                                  

	try {
		SetCommandLineOptions(argc, argv);

		                                                                           
		                                           
		                                                                           
		g_mdb_connect = DBConnectionInfo("MsgDb");
		g_idb_connect = DBConnectionInfo("IDb");
		g_pdb_connect = DBConnectionInfo("PDb");
		g_trandb_connect = DBConnectionInfo("TranDb");

		ProcessEngine processEngine;
		g_pProcessEngine = &processEngine;

		                                            

		                                     
		signal(SIGINT, SignalHandler);

		engine_lic();

		                              
		try {
			string sWarnings;
			PluginManager::UpdatePluginsInDatabase(sWarnings);
			if ( !sWarnings.empty() )
				ReportError(g_log_type, "[464] Warning: There might be a problem with one or more plugins.\n" + sWarnings);
		}
		catch( const CrException& ex ) {
			ReportError(g_log_type, "[463] Failed to update plugins in database. Plugins cannot be used until this problem has been corrected.\n"
				+ ex.GetErrorString());
		}

		                                       
		                                                                             
                                                                                                             
                                                   
                                                  
                                                                                                
                                               

		                    
		if(GetOptionAsBoolean(stPROCESSENGINE_COMPONENT_NAME, "AutoStart", true)) processEngine.Start();
		processEngine.WaitUntilExit();

		                                                                               
                                                                                                                 
                                    

	} catch(CrFatalException& e) {
		ReportError( g_log_type, "[Msg 460] A fatal error occurred: " + e.GetErrorString(), EVENTLOG_ERROR_TYPE);
		return e.GetErrorNumber();
	} catch(CrException& e) {
		ReportError( g_log_type, "[Msg 459] A severe error occurred: " + e.GetErrorString(), EVENTLOG_ERROR_TYPE);
		return e.GetErrorNumber();
	}

	ReportError( g_log_type, "[Msg 458] Server shutdown complete.", EVENTLOG_INFORMATION_TYPE);
	return 0;
}

