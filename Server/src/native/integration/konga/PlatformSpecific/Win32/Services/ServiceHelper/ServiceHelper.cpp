/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <windows.h>
#include <psapi.h>

#include <string>

#include <boost/thread/thread.hpp>

#include <konga/Networking/BasicCommands.h>               

#include <konga/Server/Server.h>		                                

#include <konga/Log/logging.h>

#include <konga/Utils/CrException.h>
#include <konga/Utils/ErrorResolver.h>
#include <konga/Utils/StringUtils.h>	                      
#include <konga/Utils/AutoCleanup.h>	             
#include <konga/Utils/ProcessUtils.h>	          

#include "ServiceHelper.h"

using namespace std;

namespace konga {
namespace ServiceHelper {

struct ServiceRunInfo {
	ServiceRunInfo(const string& sProcessPath_, unsigned long lPort_,
					const string& sModuleName_, const string& sArgs_ = "",
					const string& sWorkingDir_ = "",
					const string& sStdOutPath_ = "",
					const string& sStdErrPath_ = "") :
		sProcessPath(sProcessPath_), lPort(lPort_), sModuleName(sModuleName_), sArgs(sArgs_),
		sWorkingDir(sWorkingDir_), sStdOutPath(sStdOutPath_), sStdErrPath(sStdErrPath_) {}
	string sProcessPath;
	unsigned long lPort;
	string sModuleName;
	string sArgs;
	string sWorkingDir, sStdOutPath, sStdErrPath;
	void operator()() {
		try {
			if( GetProcId(sProcessPath) != 0 ) {
				string sUserName, sPassword;
				GetInternalAdminCredentials(sUserName, sPassword);
				try {
					networking::Start(sUserName, sPassword, lPort, sModuleName);
					return;
				} catch(CrException&) {
					                                                    
					                                                            
					                                                 
				}
			}

			RunProc(sProcessPath, sArgs, sWorkingDir, sStdOutPath, sStdErrPath);
		} catch(CrException& e) {
			string sErr = "Failed to start " + sModuleName + ". Reason: " + e.GetErrorString();
			ReportError(sErr, sModuleName + " Service");
			                                                               
		}
	}
};

HRESULT ServiceRun(const string& sProcessPath, unsigned long lPort, const string& sModuleName,
				   const string& sArgs           , const string& sWorkingDir           ,
				   const string& sStdOutPath           , const string& sStdErrPath           ) {
	static boost::thread t(ServiceRunInfo(sProcessPath, lPort, sModuleName, sArgs, 
											sWorkingDir, sStdOutPath, sStdErrPath));
	return S_OK;
}

HRESULT ServiceStop(const string& sProcessPath, unsigned long lPort, const string& sServerName, ServiceAction action                 ) {
	try {
		ProcessIdT pid = GetProcId(sProcessPath);
		string sUserName, sPassword;
		GetInternalAdminCredentials(sUserName, sPassword);
		if( action == SVC_STOP )
			networking::Stop(sUserName, sPassword, lPort, sServerName);
		else if( action == SVC_SHUTDOWN )
			networking::Shutdown(sUserName, sPassword, lPort, sServerName);

		if( pid != 0 ) {
			                                                                  
			                                                                     
			                                                           
			Sleep(2000);                                
			KillProc(pid, sProcessPath);
			return S_OK;
		}
	} catch(CrException& e) {
		string sErr = "Failed to stop " + sServerName + ". Reason: " + e.GetErrorString();
		ReportError(sErr, sServerName + "Service");
		                                                               
		return E_FAIL;
	}
	return S_OK;
}

}                           
}                   
