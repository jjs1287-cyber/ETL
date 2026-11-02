/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
                                                                  
  
                                                                      

#include "ExecuteCommand.h"

     
                                                                              
                                                                             
                                                                            
                   
  
                                                                                 
                           
  
     

#ifdef WIN32

                    
#include "tlhelp32.h"
#include "AutoCleanup.h"

long ExecuteCommand::Exec(std::string stCmd, long nSecondsTimeOut)
{
	SECURITY_ATTRIBUTES lsa;
	lsa.nLength = sizeof(SECURITY_ATTRIBUTES);
	lsa.lpSecurityDescriptor = NULL;
	lsa.bInheritHandle = TRUE;
	  
	                                        
	  
	HANDLE hOutputWrite, hOutputReadTmp;
	if (!CreatePipe(&hOutputReadTmp,&hOutputWrite,&lsa,0))
	{
		m_LastError = "Could not create child --> parent output pipe. ";
		return HandleError(::GetLastError(), true);
	}

	  
	                                        
	  
	HANDLE hErrorWrite, hErrorReadTmp;
	if (!CreatePipe(&hErrorReadTmp,&hErrorWrite,&lsa,0))
	{
		m_LastError = "Could not create child --> parent error pipe. ";
		return HandleError(::GetLastError(), true);
	}

	  
	                                        
	  
	HANDLE hInputRead, hInputWriteTmp;
	if (!CreatePipe(&hInputRead,&hInputWriteTmp,&lsa,0))
	{
		m_LastError = "Could not create parent --> child input pipe. ";
		return HandleError(::GetLastError(), true);
	}

	                                                                 
	                                                             
	                                                                  
	               
	if (!DuplicateHandle(GetCurrentProcess(), hOutputReadTmp, GetCurrentProcess(),
						&m_hStdOut,                          
						0, FALSE,                          
						DUPLICATE_SAME_ACCESS))
	{
		m_LastError = "Could not duplicate stdout. ";
		return HandleError(::GetLastError(), true);
	}

	if (!DuplicateHandle(GetCurrentProcess(), hErrorReadTmp, GetCurrentProcess(),
						&m_hStdErr,                          
						0, FALSE,                          
						DUPLICATE_SAME_ACCESS))
	{
		m_LastError = "Could not duplicate stderr. ";
		return HandleError(::GetLastError(), true);
	}

	if (!DuplicateHandle(GetCurrentProcess(), hInputWriteTmp, GetCurrentProcess(),
						&m_hStdIn,                          
						0, FALSE,                          
						DUPLICATE_SAME_ACCESS))
	{
		m_LastError = "Could not duplicate stdin. ";
		return HandleError(::GetLastError(), true);
	}

	                                                                           
	if (!CloseHandle(hOutputReadTmp))
	{
		m_LastError = "Could not close temporary stdout. ";
		return HandleError(::GetLastError(), true);
	}
	if (!CloseHandle(hErrorReadTmp))
	{
		m_LastError = "Could not close temporary stdout. ";
		return HandleError(::GetLastError(), true);
	}
	if (!CloseHandle(hInputWriteTmp))
	{
		m_LastError = "Could not close temporary stdin. ";
		return HandleError(::GetLastError(), true);
	}

	  
	                              
	  
	STARTUPINFO 		si;
	si.cb=sizeof(STARTUPINFO);
	si.lpReserved=NULL;
	si.lpTitle=NULL;
	si.lpDesktop=NULL;
	si.dwX=si.dwY=si.dwYSize=si.dwXSize=0;
	si.dwFlags = m_ShowOutput ? STARTF_USESHOWWINDOW : STARTF_USESTDHANDLES;
	si.hStdInput =hInputRead;
	si.hStdOutput=hOutputWrite;
	si.hStdError =hErrorWrite;
	si.wShowWindow=m_ShowOutput?SW_NORMAL:SW_HIDE;
	si.lpReserved2=NULL;
	si.cbReserved2=0;

	PROCESS_INFORMATION pi;

	char* buf = new char[ stCmd.length() + 1 ];
	AutoArrayDelete<char> adopt_buf(buf);
	_tcscpy(buf,stCmd.c_str());
	  
	                      
	  
	if (!CreateProcess
		 (
			NULL,
			buf,
			NULL,
			NULL,
			TRUE,
			NORMAL_PRIORITY_CLASS | (m_ShowOutput?0:CREATE_NO_WINDOW),
			NULL,
			NULL,
			&si,
			&pi)
		 )
	{
		long err = ::GetLastError();
		char* pos = strstr(stCmd.c_str(), "-pw");
		if (pos != NULL)
		{
			pos += 4;
			while (*pos != ' ')
				*pos++  = '*';
		}
		m_LastError = "Could not create child process. CMD line is ";
		m_LastError += stCmd;
		return HandleError(err, true);		
	}

	  
	                                       
	  
	if(hOutputWrite) ::CloseHandle(hOutputWrite);
	if(hErrorWrite) ::CloseHandle(hErrorWrite);
	if(hInputRead) ::CloseHandle(hInputRead);
	if(pi.hThread) ::CloseHandle(pi.hThread);

	m_hProcess = pi.hProcess;
	m_dwProcessId = pi.dwProcessId;

	if( m_ShowOutput )
		Sleep(15000);	                                                                                                      

	  
	                                                                                          
	                                          
	  
	InteractWithProcess();

	long retval = WaitForProcess(nSecondsTimeOut);

	std::string stErr = ReadFromStderr();
	if( stErr.length() )
	{
		char* pos = strstr(stCmd.c_str(), "-pw");
		if (pos != NULL)
		{
			pos += 4;
			while (*pos != ' ')
				*pos++  = '*';
		}
		m_LastError += " For ";
		m_LastError += stCmd;
		m_LastError += "; Error message from command: ";
		m_LastError += stErr;
		m_LastError += ".";
	}

	m_LastOutput += ReadFromStdout();

	DWORD exitCode;
	if( !::GetExitCodeProcess(pi.hProcess, &exitCode))
		retval = HandleError(::GetLastError(), false );
	else if( exitCode )
	{
                                                              
                                  
			m_LastError += ". Exit code: ";
			char abuf[20];
			m_LastError += itoa(exitCode, abuf, 10);
			return E_FAIL;
	}

    if(m_hStdIn) ::CloseHandle(m_hStdIn);
	if(m_hStdOut) ::CloseHandle(m_hStdOut);
	if(m_hStdErr) ::CloseHandle(m_hStdErr);

	if(pi.hProcess) ::CloseHandle(pi.hProcess);
	return retval||m_LastError.length();
}

long ExecuteCommand::WaitForProcessInputIdle(int nSecondsTimeOut, bool bTerminateIfTimedOut         )
{
	return ::WaitForInputIdle(m_hProcess, nSecondsTimeOut);
}

long ExecuteCommand::WaitForProcess(int nSecondsTimeOut, bool bTerminateIfTimedOut         )
{
	long retval = E_FAIL;
	DWORD timeOut = nSecondsTimeOut > 0 ? nSecondsTimeOut * 1000 : INFINITE;
	DWORD res = ::WaitForSingleObject(m_hProcess,timeOut);
	switch( res ) 
	{
	case WAIT_TIMEOUT: 
		{
			m_LastError = _T("ExecuteCommand::WaitForProcess::Operation timed out.");
			retval = E_FAIL;
			if( bTerminateIfTimedOut )
			{
				KillChildProcesses();
				::TerminateProcess( m_hProcess, 0 );                            
			}
			break; 
		}
	case WAIT_OBJECT_0:
		{
			retval = S_OK;
			if( !::GetExitCodeProcess(m_hProcess,&m_lExitCode) )
				retval = HandleError( ::GetLastError(), false );
			else if( m_lExitCode )
			{
				m_LastError = "Operation failed. Exit code: ";
				char buf[20];
				m_LastError += itoa(m_lExitCode,buf,10);
				retval = E_FAIL;
			}
			break;
		}
	case WAIT_FAILED:
		{
			retval = ::GetLastError();
			m_LastError += "Operation failed with wait failed parameter. ";
			break;
		}
	}
	return retval;
}

std::string ExecuteCommand::ReadFromStdout()
{
	char buf[1025];
	DWORD cb = 1024;
	memset(buf,0,cb);
	BOOL bErr;
	std::string stBuf;
    while( (bErr=::PeekNamedPipe(m_hStdOut, 0, 0, 0, &cb, 0)) && cb )
	{
		if( !ReadFile(m_hStdOut, buf, sizeof(buf), &cb, NULL) )
		{
			m_LastError += "When reading from stdout of process: ";
			HandleError(::GetLastError(), true);
		}
		else
		{
			buf[cb]=0;
			stBuf += buf;
		}
	}
	if( !bErr )
	{
		long err = ::GetLastError();
		if( err != ERROR_BROKEN_PIPE )                                                
		{
			m_LastError = "When peeking at stdout of process: ";
			HandleError(err, true);
		}
	}
	return stBuf;
}

std::string ExecuteCommand::ReadFromStderr()
{
	char buf[1024];
	DWORD cb = sizeof(buf);
	memset(buf,0,cb);
	std::string stBuf;
    BOOL bErr;

    if( (bErr=::PeekNamedPipe(m_hStdErr, 0, 0, 0, &cb, 0)) && cb )
	{
		if( !ReadFile(m_hStdErr, buf, sizeof(buf), &cb, NULL) )
		{
			m_LastError = "When reading from stderr of process: ";
			HandleError(::GetLastError(), true);
		}
		else
		{
			buf[cb]=0;
			stBuf += buf;
		}
	}

	if( !bErr )
	{
		long err = ::GetLastError();
		if( err != ERROR_BROKEN_PIPE )                                                
		{
			m_LastError += "When peeking at stderr of process: ";
			HandleError(err, true);
		}
	}
	return stBuf;
}

void ExecuteCommand::WriteToStdin(std::string stStr)
{
	DWORD cb=0;
	if( !::WriteFile(m_hStdIn, stStr.c_str(), stStr.length()*sizeof(char), &cb, NULL) )
	{
		m_LastError = "When writing to stdin of process: ";
		HandleError(::GetLastError(), true);
	}

                                         
                                  
  
                                                     
                                      
  
  
}


long ExecuteCommand::HandleError(long err, bool append          )
{
	_com_error ce(err);
	if( ce.WCode() )
	{
		if( append ) m_LastError += ce.ErrorMessage();
		else m_LastError = ce.ErrorMessage();
	}
	else
	{
		LPTSTR lpMsgBuf;
		int len = FormatMessage( FORMAT_MESSAGE_ALLOCATE_BUFFER | FORMAT_MESSAGE_FROM_SYSTEM,
			NULL, err, MAKELANGID(LANG_NEUTRAL, SUBLANG_DEFAULT), (LPTSTR)&lpMsgBuf, 1, NULL);
		lpMsgBuf[ len ] = 0;
		if( append ) m_LastError += lpMsgBuf;                                 
		else m_LastError = lpMsgBuf;
		LocalFree( lpMsgBuf );
		SetLastError(0);
		return E_FAIL;
	}
	return err;
}

long ExecuteCommand::KillChildProcesses()
{
	DWORD dwVersion = ::GetVersion();
	if( (DWORD)(LOBYTE(LOWORD(dwVersion))) < 5 )
	{
	}
	else
	{
		HMODULE hKernel32Lib;

		typedef HANDLE (WINAPI *pCreateToolhelp32Snapshot)(DWORD dwFlags, DWORD th32ProcessID);
		typedef BOOL (WINAPI *pProcess32First)(HANDLE hSnapshot, LPPROCESSENTRY32 lppe);
		typedef BOOL (WINAPI *pProcess32Next)(HANDLE hSnapshot, LPPROCESSENTRY32 lppe);

		pCreateToolhelp32Snapshot CreateToolhelp32Snapshot;
		pProcess32First Process32First;
		pProcess32Next Process32Next;

		if( (hKernel32Lib = ::LoadLibrary("Kernel32")) &&
			(CreateToolhelp32Snapshot=(pCreateToolhelp32Snapshot)::GetProcAddress(hKernel32Lib,"CreateToolhelp32Snapshot")) &&
			(Process32First=(pProcess32First)::GetProcAddress(hKernel32Lib,"Process32First")) &&
			(Process32Next=(pProcess32Next)::GetProcAddress(hKernel32Lib,"Process32Next")) )
		{

			HANDLE hSnapshot = CreateToolhelp32Snapshot(TH32CS_SNAPPROCESS,0);
			if( !hSnapshot )
			{
				m_LastError += "When enumerating child processes of process: ";
				return HandleError(::GetLastError(), true);
			}

			m_LastError += "";
			PROCESSENTRY32 procEntry;
			procEntry.dwSize = sizeof(PROCESSENTRY32);
			if( Process32First(hSnapshot,&procEntry) )
			{
				do
				{
					procEntry.dwSize = sizeof(PROCESSENTRY32);
					if( procEntry.th32ParentProcessID == m_dwProcessId )
					{
						HANDLE hProc = ::OpenProcess(PROCESS_TERMINATE, FALSE, procEntry.th32ProcessID);
						if( !hProc )
						{
							m_LastError += "When opening child process for termination: ";
							HandleError(::GetLastError(), true);
						}
						if( !::TerminateProcess(hProc, 5152) )
						{
							m_LastError += "When terminating child process: ";
							HandleError(::GetLastError(), true);
						}
					}			
				} while( Process32Next(hSnapshot,&procEntry) );
			}
			::CloseHandle(hSnapshot);
		}
	}
	return 0;
}

#else

long ExecuteCommand::Exec(std::string stCmd, long nSecondsTimeOut) {
	                                         
	return 0;
}

long ExecuteCommand::WaitForProcessInputIdle(int nSecondsTimeOut, bool bTerminateIfTimedOut         ) {
	                                                            
	return 0;
}

long ExecuteCommand::WaitForProcess(int nSecondsTimeOut, bool bTerminateIfTimedOut         ) {
	                                                   
	return 0;
}

std::string ExecuteCommand::ReadFromStdout() {
	                                                   
	return "";
}

void ExecuteCommand::WriteToStdin(std::string stStr) {
	                                                 
}

long ExecuteCommand::HandleError(long err, bool append          ) {
	                                                
	return 0;
}

long ExecuteCommand::KillChildProcesses() {
	                                                       
	return 0;
}

#endif 

