/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#ifdef WIN32
	#include <stdlib.h>	            
	#include <windows.h>                        
	#include <psapi.h>	                                         
	#ifndef PATH_MAX
		namespace { const unsigned long PATH_MAX = _MAX_PATH; }
	#endif
	#pragma comment( lib, "psapi.lib" )
#else
	#include <stdio.h>
	#include <limits.h>		           
    #include <sys/types.h>
    #include <sys/wait.h>
    #include <signal.h>
	#include <unistd.h>		          
	#include <wordexp.h>	            
	#include <errno.h>
	#include "PortDefs.h"	                 

	#include <iostream>
#endif

#include <string.h>	         
#include <string>

#include <konga/Utils/AutoPtrCleanup.h>	                    
#include <konga/Utils/AutoCleanup.h>	             
#include <konga/Utils/CrException.h>
#include <konga/Utils/ErrorResolver.h>	                      
#include <konga/Utils/StringUtils.h>	                        
#include <konga/Utils/FileUtils.h>		             

#include "ProcessUtils.h"

using namespace konga;
using namespace std;


RunProcFileHandles::RunProcFileHandles(void) : hOut(0), hErr(0) {
}

RunProcFileHandles::~RunProcFileHandles(void) {
	Close();
}

void RunProcFileHandles::Close() {
#ifdef WIN32
	BOOL retVal;
	if ( hOut ) {
		retVal = ::CloseHandle(hOut);
		hOut = 0;
	}
	if ( hErr ) {
		retVal = ::CloseHandle(hErr);
		hErr = 0;
	}
#endif
}

ProcessIdT konga::RunProc(const std::string& sFileName, const std::string& sArgs           ,
				   const std::string& sWorkingDir           ,
				   const std::string& sStdOutPath           ,
				   const std::string& sStdErrPath           ,
				   RunProcFileHandles* handles          )
{
	if( sFileName.empty() ) {
		throw CrException("Failed to start process. No file name was specified.");
	}

#ifdef WIN32
	STARTUPINFO	si;
	memset(&si, 0, sizeof(si));
	si.cb =	sizeof(si);

	const BOOL bInheritHandles = (sStdOutPath.empty() && sStdErrPath.empty()) ? FALSE : TRUE;

	if( !sStdOutPath.empty() ) {
		SECURITY_ATTRIBUTES sa;
		memset(&sa, 0, sizeof SECURITY_ATTRIBUTES);
		sa.nLength = sizeof SECURITY_ATTRIBUTES;
		sa.bInheritHandle = TRUE;
		HANDLE h = ::CreateFile(sStdOutPath.c_str(),
							GENERIC_WRITE | GENERIC_READ, 
							FILE_SHARE_READ | FILE_SHARE_WRITE | FILE_SHARE_DELETE,
							&sa,                       
							OPEN_ALWAYS,                         
							0,                        
							0);                
		si.hStdOutput = h;
		si.dwFlags |= STARTF_USESTDHANDLES;
		
		if( h ) {
			if ( handles != 0 ) {
				handles->hOut = h;
			}
			::SetFilePointer(h, 0, 0, FILE_END);
		}
		
	}

	if( !sStdErrPath.empty() ) {
		SECURITY_ATTRIBUTES sa;
		memset(&sa, 0, sizeof SECURITY_ATTRIBUTES);
		sa.nLength = sizeof SECURITY_ATTRIBUTES;
		sa.bInheritHandle = TRUE;
		HANDLE h = ::CreateFile(sStdErrPath.c_str(),
							GENERIC_WRITE | GENERIC_READ, 
							FILE_SHARE_READ | FILE_SHARE_WRITE | FILE_SHARE_DELETE,
							&sa,                       
							OPEN_ALWAYS,                         
							0,                        
							0);                
		si.hStdError = h;
		si.dwFlags |= STARTF_USESTDHANDLES;
		
		if( h ) {
			if ( handles != 0 ) {
				handles->hErr = h;
			}
			::SetFilePointer(h, 0, 0, FILE_END);
		}		
	}


	PROCESS_INFORMATION	pi;
	memset(&pi, 0, sizeof(pi));

	const string sArgsWithFileName( "\"" + sFileName + "\" " + sArgs );
	char* pArgs = 0; 
	pArgs = new char[sArgsWithFileName.length() + 1];
	strcpy(pArgs, sArgsWithFileName.c_str());
	AutoArrayDelete<char> adopt_pArgs(pArgs);

	LPCSTR lpCurrentDirectory = NULL;
	if ( !sWorkingDir.empty() )
		lpCurrentDirectory = sWorkingDir.c_str();


	if( !CreateProcess(
		0,					                                                  
		pArgs,				                 
		NULL,				                                   
		NULL,				                                 
		bInheritHandles,	                                   
		0,					                     
		NULL,				                                   
		lpCurrentDirectory,	                       
		&si,				                                    
		&pi	)				                                            
	) 
	{
		long lErr = GetLastError();
		throw CrException("Failed to create process '" + sFileName +
			"'. Reason: " + ErrorResolver().Resolve(lErr));
	}

	                                    
	CloseHandle(pi.hProcess);
	CloseHandle(pi.hThread);
	return pi.dwProcessId;
#else
	ProcessIdT pid = fork();
	if ( pid == -1 ) {
		long lErr = GetLastError();
		throw CrException("Failed to create process '" + sFileName +
			"'. Reason: " + ErrorResolver().Resolve(lErr));
	}

	                                                                   
	                                                              
	if( pid )
		return pid;

	if ( !sWorkingDir.empty() )
		chdir(sWorkingDir.c_str());

	if ( !sStdOutPath.empty() ) {
		FILE* f = freopen(sStdOutPath.c_str(), "w", stdout);
		if ( f == 0 ) {
			long lErr = GetLastError();
			throw CrException("Failed to create process '" + sFileName +
				"'. Failed to redirect stdout to '" + sStdOutPath + "'. Reason: "
				+ ErrorResolver().Resolve(lErr));
		}
	}

	if ( !sStdErrPath.empty() ) {
		FILE* f = freopen(sStdErrPath.c_str(), "w", stderr);
		if ( f == 0 ) {
			long lErr = GetLastError();
			throw CrException("Failed to create process '" + sFileName +
				"'. Failed to redirect stderr to '" + sStdErrPath + "'. Reason: "
				+ ErrorResolver().Resolve(lErr));
		}
	}

	if( sArgs.empty() ) {
		if( sFileName.length() > MAX_PATH )
			throw CrException("File name is too long (max=" + toString(MAX_PATH) + "): " + sFileName);
		char pFileName[MAX_PATH+1];
		strcpy(pFileName, sFileName.c_str());
		char* argv[] = {pFileName, 0};
		execvp(sFileName.c_str(), argv);                         
	} else {
		wordexp_t we;
		string sAllArgs = sFileName + ' ' + sArgs;
		wordexp(sAllArgs.c_str(),  &we, 0);
		execvp(sFileName.c_str(), we.we_wordv);                         
		wordfree(&we);                          
	}

	unsigned long lErr = GetLastError();
	throw CrException("Cannot start process '" + sFileName + "'. Reason: " + ErrorResolver().Resolve(lErr));
#endif
}

int konga::WaitForProc(ProcessIdT pid, unsigned long nTimeoutInMillies           ) {
#ifdef WIN32
	HANDLE hProc = ::OpenProcess(SYNCHRONIZE | PROCESS_QUERY_INFORMATION, FALSE, pid);
	if( !hProc ) {
		long lErr = ::GetLastError();
		throw CrException("Failed to open process handle with id = " + toString(pid) + 
			". Reason: " + ErrorResolver().Resolve(lErr) );
	}
	AutoHandle adopt_hProc(hProc);
	if( ::WaitForSingleObject(hProc, nTimeoutInMillies) == WAIT_FAILED ) {
		long lErr = ::GetLastError();
		throw CrException("Failed to wait for process with id = " + toString(pid) + 
			". Reason: " + ErrorResolver().Resolve(lErr) );
	}
	DWORD nExitCode = -1;
	if( !GetExitCodeProcess(hProc, &nExitCode) ) {
		long lErr = ::GetLastError();
		throw CrException("Failed to get exit code for process with id = " + toString(pid) + 
			". Reason: " + ErrorResolver().Resolve(lErr) );
	}
	return nExitCode;
#else
	int iExitCode = -1;
	if( waitpid(pid, &iExitCode, 0) != pid ) {
		long lErr = ::GetLastError();
		throw CrException("Failed to wait for process with id = " + toString(pid) + 
			". Reason: " + ErrorResolver().Resolve(lErr) );
	}
	return iExitCode;
#endif
}

                                                                                               
ProcessIdT konga::GetProcId(const string& sProcessPathIn) {
	if( sProcessPathIn.empty() )
		return 0;
#ifdef WIN32
	const unsigned int nMAX_PROCESSES = 100000;                                                        
	DWORD arProcIds[nMAX_PROCESSES], cbNeeded;

	                                   
	if( !EnumProcesses(arProcIds, sizeof arProcIds, &cbNeeded) ) {
		long nErr = ::GetLastError();
		throw CrException("Failed to enumerate process. Reason: " + ErrorResolver().Resolve(nErr));
	}

	unsigned long nProcs = cbNeeded / sizeof(DWORD);
	char pcProcImageName[ MAX_PATH ];

	string sProcessPath = sProcessPathIn;
	ReplaceCharWithChar(sProcessPath, '/', '\\');
	ToUpper(sProcessPath);

	                                                              
	                                                              
	for(int i=0; i<nProcs; ++i) {

	    HANDLE hProc = OpenProcess(PROCESS_QUERY_INFORMATION | PROCESS_VM_READ, FALSE, arProcIds[i]);
		if( !hProc )
			continue;                                                   
		AutoHandle adopt_handle(hProc);

                                                                                   
		if( !GetModuleFileNameEx(hProc, 0, pcProcImageName, sizeof(pcProcImageName)) )
			continue;                                                   

		string sProcImageName(pcProcImageName);
		ToUpper(sProcImageName);

                                                               
		if( strstr(sProcImageName.c_str(), sProcessPath.c_str()) != 0 ) {
			return arProcIds[i];
		}
	}
#endif
	return 0;
}

void konga::KillProc(const std::string& sProcessPath) {
	ProcessIdT pid = GetProcId(sProcessPath);
	if( pid )
		KillProc(pid);
}

#ifndef WIN32
namespace {

	                                                           
	bool isProcessRunning(ProcessIdT pid) {
		const long lErr = kill(pid, 0);
		return lErr == 0;
	}

	                                                       
	                                                
	                                               
	                                         
	bool sendSignal(ProcessIdT pid, int sg) {
		std::cout << "Sending " << sg << " to " << pid << std::endl;
		const int retval = kill(pid, sg);
		std::cout << "retval = " << retval << std::endl;
		if ( retval == 0 ) return true;

		const long lErr = GetLastError();
		if ( lErr == ESRCH ) {
			                         
			return false;
		}
		else {
			throw CrException("Failed to send SIGINT to process. Reason: " + ErrorResolver().Resolve(lErr));
		}
	}

	                               
	                                                                    
	bool waitForProcessToExit(ProcessIdT pid, long mxWaitMs) {
		std::cout << "waitForProcessToExit..." << std::endl;
		long lTime = 0;
		const long ms_tick = 200;	                                           
		do {
			if ( isProcessRunning(pid) ) {
				std::cout << "Waiting for process for " << lTime << " ms..." << std::endl;
				MilliSleep(ms_tick);
				lTime += ms_tick;
			}
			else {
				return true;
			}
		} while ( lTime < mxWaitMs );
		return false;
	}

	const long MAX_PROC_WAIT_MS = 10000;

}
#endif

void konga::KillProc(ProcessIdT pid, const std::string& sProcessPath          ) {

#ifdef WIN32
	HANDLE hProc = ::OpenProcess(PROCESS_TERMINATE, false, pid);
	if ( hProc == 0 ) {
		long lErr = ::GetLastError();
		throw CrException("Failed to open the process with PID=" + toString(pid)
			+ string(sProcessPath.empty() ? "" : (" (" + sProcessPath + ")"))
			+ " for terminating. " + ErrorResolver().Resolve(lErr));
	}

	if ( !::TerminateProcess(hProc, 0) ) {
		long lErr = ::GetLastError();
		throw CrException("Failed to terminate process PID=" + toString(pid)
			+ string(sProcessPath.empty() ? "" : (" (" + sProcessPath + ")"))
			+ ". " + ErrorResolver().Resolve(lErr));
	}
#else
	if ( pid < 2 ) return;	                                                                    

	if ( !sendSignal(pid, SIGINT) ) return;
	MilliSleep(500);

	if ( waitForProcessToExit(pid, MAX_PROC_WAIT_MS) ) return;

	                                      
	if ( !sendSignal(pid, SIGTERM) ) return;
	MilliSleep(500);

	if ( waitForProcessToExit(pid, MAX_PROC_WAIT_MS) ) return;

	                                      
	if ( !sendSignal(pid, SIGKILL) ) return;
	MilliSleep(500);

	if ( waitForProcessToExit(pid, MAX_PROC_WAIT_MS) ) return;

	std::cout << "Failed to kill process " << pid << " Giving up without error..." << std::endl;

#endif
}

ProcessIdT konga::GetCurrentProcId() {
#ifdef WIN32
	return ::GetCurrentProcessId();
#else
	return getpid();
#endif
}

ThreadIdT konga::GetCurrentThreadId() {
#ifdef WIN32
	return ::GetCurrentThreadId();
#else
	                                                                   
	                                                                
	                                 
	return getpid();
#endif
}


