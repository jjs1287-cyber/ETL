/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#ifndef KONGA_PROCESS_UTILS_H
#define KONGA_PROCESS_UTILS_H

#include <string>

#ifdef WIN32
	#include <windows.h>
	#include <process.h>
	typedef DWORD ProcessIdT;
	typedef DWORD ThreadIdT;
#else
	#include <sys/types.h>
	#include <unistd.h>
	typedef pid_t ProcessIdT;
	typedef pid_t ThreadIdT;
#endif

namespace konga {

	class RunProcFileHandles {
	public:
		RunProcFileHandles(void);
		~RunProcFileHandles(void);

		void Close();

#ifdef WIN32
		HANDLE hOut, hErr;
#else
		FILE *hOut, *hErr;
#endif
	};

	ProcessIdT RunProc(const std::string& sFileName, const std::string& sArgs = "",
				   const std::string& sWorkingDir = "",
				   const std::string& sStdOutPath = "",
				   const std::string& sStdErrPath = "",
				   RunProcFileHandles* handles = 0);

	void KillProc(const std::string& sProcessPath);
	void KillProc(ProcessIdT pid, const std::string& sProcessPath = "");

	int WaitForProc(ProcessIdT pid, unsigned long nTimeoutInMillies = static_cast<unsigned long>(-1));

	ProcessIdT GetProcId(const std::string& sProcessPath = "");

	ProcessIdT GetCurrentProcId();
	ThreadIdT GetCurrentThreadId();

	inline void MilliSleep(unsigned int nMilliseconds) {
		#ifdef WIN32
			Sleep(nMilliseconds);
		#else
			usleep(nMilliseconds*1000);
		#endif
	}

}                   

#endif                         
