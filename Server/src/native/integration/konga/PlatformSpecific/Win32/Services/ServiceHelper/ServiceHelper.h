/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_SERVICE_HELPER_H
#define KONGA_SERVICE_HELPER_H

#include <string>

namespace konga {
namespace ServiceHelper {
	
                                                                                         
	enum ServiceAction { SVC_STOP, SVC_SHUTDOWN };

                                                                                              
                                                                                          

	bool IsProcessRunning(const std::string& sUserName, const std::string& sPassword,
							const std::string& sServerName, long lPort);

	void StartProcess(const std::string& sUserName, const std::string& sPassword, 
		const std::string& sProcessPath, unsigned long lPort, const std::string& sServerName);

	HRESULT ServiceRun(const std::string& sProcessPath, unsigned long lPort, const std::string& sModuleName,
		const std::string& sArgs = "", const std::string& sWorkingDir = "",
		const std::string& sStdOutPath = "" , const std::string& sStdErrPath = "");

	HRESULT ServiceStop(const std::string& sProcessPath, unsigned long lPort,
		const std::string& sModuleName, ServiceAction action = SVC_STOP);

}                           
}                   

#endif                          
