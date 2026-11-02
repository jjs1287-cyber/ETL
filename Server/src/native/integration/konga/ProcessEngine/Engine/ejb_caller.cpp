/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */

#if defined(DS_USE_CORBA)

#include <iostream>
#include <fstream>                 
#include <string>
#include <string.h>

#include "IncomingMsg.h"	                    
#include "util.h"			                             
#include "AutoCleanup.h"	                         

#include "engine_util.h"
#include "corba_util.h"
#include "BeanInvoker.h"	                           
#include "ejb_caller.h"		                

EJBInfo g_ejb_info;

  
                                                                                
                                                                               
                                         
                                                                         
                                                                           
                                                                              
                                      
  
void WatcherThread(void*p)
{
	StdPipes *pipes = static_cast<StdPipes *>(p);

	char buf[1024];
	DWORD cb = sizeof(buf);
	std::string s;
	DWORD res;
	while( (res=::WaitForSingleObject(pipes->Event,100)) == WAIT_TIMEOUT )
	{
		if( ::PeekNamedPipe(pipes->StdErr, 0, 0, 0, &cb, 0) && cb )
		{
			if( !ReadFile(pipes->StdErr, buf, sizeof(buf), &cb, NULL) )
			{
				                                                     
				                                      
			}
			else
			{
				s = "[Msg 306] EJB Invoker: Java sub-system reports: \n";
				buf[cb]=0;
				s += buf;
				ReportError(g_log_type,s.c_str());
				::TerminateProcess(pipes->hJava,9898);
				pipes->bJavaTerminated = true;                                                
			}		
		}
	}
                                                                                               
                                                       
                              
                              
                             
                             
  
}

bool EJBInfo::ExecuteServer()
{
	KString cmd = GetOptionAsString("konga/Settings","java.exe","java").c_str();
	KString params = GetOptionAsString("konga/Settings","EjbInvokerPath","BIServer.BeanInvServer").c_str();
	if( !GetOptionAsInt("konga/Settings","EJBVerbose",0) )
		params += " -nooutput";
	params += " -OAport ";
	params += GetOptionAsString("konga/Settings","EjbInvokerPort","2000").c_str();

	cmd += ' ';
	cmd += params;

	  
	                                                                                                    
	  
	if( FAILED(Exec((char*)(LPCSTR)cmd)) || m_LastOutput.length()<5 ||
		m_LastOutput.compare(0,4,"CRBI") )
	{
		if( m_JavaProcHandle ) CloseJava();
		m_JavaProcHandle = 0;
		std::string stErr = "[Msg 280] EJB Invoker: error executing '%s': ", (LPCTSTR)cmd);
		std::string s = log_str;
		s += m_LastError;
		if( m_LastOutput.length() ) { s += ", Output: "; s += m_LastOutput; }
		if( m_LastErrOutput.length() ) { s += ", Error: "; s += m_LastErrOutput; }
		char bs[50001];
		memset(bs,0,50001);
		strncpy(bs,s.c_str(),s.length()>50000?50000:s.length());
		throw InvokeErr(bs);
	}

	char buf[256];
	std::string s = "[Msg 305] Launched '";
	s += (LPCTSTR)cmd; s += "'. Windows assigned ProcessId "; s += itoa(m_JavaProcId,buf,10);
	ReportError(g_log_type,s,EVENTLOG_INFORMATION_TYPE);
	return true;
}

HRESULT EJBInfo::HandleError(long err, bool append          )
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

HRESULT EJBInfo::Exec(TCHAR* args)
{
	SECURITY_ATTRIBUTES lsa;
	lsa.nLength=sizeof(SECURITY_ATTRIBUTES);
	lsa.lpSecurityDescriptor=NULL;
	lsa.bInheritHandle=TRUE;

	  
	                                         
	  
	HANDLE ChildOut, outH;
	if (!CreatePipe(&outH,&ChildOut,&lsa,0))
	{
		m_LastError = "Could Not Create Child-->Parent Pipe. ";
		return HandleError(::GetLastError(), true);
	}
	if( m_StdOut ) ::CloseHandle(m_StdOut); 
	m_StdOut = outH;
	                                                         

	  
	                                         
	  
	HANDLE ChildErr, errH;
	if (!CreatePipe(&errH,&ChildErr,&lsa,0))
	{
		m_LastError = "Could Not Create Child-->Parent Pipe. ";
		return HandleError(::GetLastError(), true);
	}
	if( m_StdErr ) ::CloseHandle(m_StdErr); 
	m_StdErr = errH;
	                                                         

	m_ShowOutput = GetOptionAsInt("konga/Settings","EJBShowOutput",0) == 1;

	  
	                              
	  
	STARTUPINFO 		si;
	si.cb=sizeof(STARTUPINFO);
	si.lpReserved=NULL;
	si.lpTitle="CommerceRoute Enterprise Java Bean Invoker";
	si.lpDesktop=NULL;
	si.dwX=si.dwY=si.dwYSize=si.dwXSize=0;
	si.dwFlags = m_ShowOutput ? STARTF_USESHOWWINDOW : STARTF_USESTDHANDLES;
	si.hStdInput =0;
	si.hStdOutput=ChildOut;
	si.hStdError =ChildErr;
	si.wShowWindow=m_ShowOutput?SW_NORMAL:SW_HIDE;
	si.lpReserved2=NULL;
	si.cbReserved2=0;

	PROCESS_INFORMATION pi;

	  
	                       
	  
	if (!CreateProcess
		 (
			NULL,
			args,
			NULL,
			NULL,
			TRUE,
			m_ShowOutput ? NORMAL_PRIORITY_CLASS | CREATE_NEW_CONSOLE :
				NORMAL_PRIORITY_CLASS | CREATE_NO_WINDOW,
			NULL,
			NULL,
			&si,
			&pi)
		 )
	{
		long err = ::GetLastError();
		m_LastError = "Could not create child process. ";
		return HandleError(err, true);		
	}

	int nSecondsTimeOut = konga::GetOptionAsInt( "konga/Settings", "EJBInvServerTimeoutSeconds", 60);

	if( m_ShowOutput )
	{
		Sleep(5000); m_LastOutput = "CRBI "; 
	}
	else
	{
		char buf[1024];
		DWORD cb = sizeof(buf);
		for(int i=0; i<nSecondsTimeOut; ++i)
		{
			if( ::PeekNamedPipe(errH, 0, 0, 0, &cb, 0) && cb )
			{
				if( !ReadFile(errH, buf, sizeof(buf), &cb, NULL) )
				{
					m_LastError = "When reading from process stderr: ";
					HandleError(::GetLastError(), true);
				}
				else
				{
					buf[cb]=0;
					m_LastErrOutput = buf;
				}
				break;
			}

			if( ::PeekNamedPipe(outH, 0, 0, 0, &cb, 0) && cb )
			{
				if( !ReadFile(outH, buf, sizeof(buf), &cb, NULL) )
				{
					m_LastError = "When reading from process stdout: ";
					HandleError(::GetLastError(), true);
				}
				else
				{
					buf[cb]=0;
					m_LastOutput = buf;
				}
				break;
			}

			if( ::WaitForSingleObject(pi.hProcess,1000) != WAIT_TIMEOUT )
				break;
		}
	}

	                                                        
	if( m_JavaProcHandle )
		::CloseHandle(m_JavaProcHandle);
	m_JavaProcHandle = pi.hProcess;
	m_JavaProcId = pi.dwProcessId;

	CloseHandle(pi.hThread);                                                           
	CloseHandle(ChildOut);
	CloseHandle(ChildErr);

	return S_OK;
}

void EJBInfo::UnLoadEjbCaller()
{
	if( CORBA::is_nil(m_bInv) )
		return;
	try
	{
		m_bInv->stopServer();
	}
	catch(...)
	{
	                
	}
}

bool EJBInfo::LoadEjbCaller()
{
	KString locationStr = GetLocationString();
	  
	                                                                                     
	  
	if( m_csLocationStr == locationStr && !CORBA::is_nil(m_bInv) )
	{
		try
		{
			  
			                                          
			  
			if( !m_bInv->_non_existent() )
				return true;
		}
		catch(CORBA_SystemException&)
		{
			if( IsLocal() && !m_JavaProcHandle && !ExecuteServer() )
				return false;		
		}
	}

	m_csLocationStr = locationStr;
	KString res;
	CORBA::Object_var obj;
	try
	{
		obj = g_orb->string_to_object( locationStr );
	}
	catch(CORBA_SystemException& e)
	{
		CORBA::String_var reason( e._to_string() );                                                           
		std::string stErr = "[Msg 281] CORBA system error when doing string_to_object( '%s' ). Reason: %s",
			locationStr, reason.in()&&*reason.in()?reason.in():"unknown");
		throw InvokeErr(log_str);;
	}

	if( CORBA::is_nil(obj) )
	{
		std::string stErr = "[Msg 282] Failed to find '%s' (string_to_object returned nil object)", locationStr);
		throw InvokeErr(log_str);;
	}

	try
	{
		obj->_non_existent();
	}
	catch(CORBA_SystemException&)
	{
		if( IsLocal() && !ExecuteServer() )
			return false;		
	}

	try
	{
		m_bInv = BeanInvoker::_narrow(obj);
	}
	catch(CORBA_SystemException& e)
	{
		CORBA::String_var reason( e._to_string() );                                                           
		std::string stErr = "[Msg 283] CORBA system error when doing _narrow() for '%s'. Reason: %s",
			locationStr, reason.in()&&*reason.in()?reason.in():"unknown");
		throw InvokeErr(log_str);;
	}

	if( CORBA::is_nil(m_bInv) )
	{
		std::string stErr = "[Msg 284] Failed to bind to '%s' (_narrow() returned nil object)", locationStr);
		throw InvokeErr(log_str);;
	}

	return true;                                                    
}

KString EJBInfo::InvokeEJB(std::string locationStr, KString args)
{
	boost::mutex::scoped_lock lock(m_init_lock);
	if( !LoadEjbCaller() ) return "";

	KString res="";
	try
	{
		res = DoInvoke((locationStr+(LPCTSTR)args).c_str());
	}
	catch(CORBA_SystemException& e)
	{
		CORBA::String_var reason( e._to_string() );                                                           
		std::string stErr = "[Msg 285] CORBA system error when invoking '%s;%s'. Reason: %s",
			args, locationStr.c_str(), reason.in()&&*reason.in()?reason.in():"unknown");
		Release();
		throw InvokeErr(log_str);;
	}
	catch(BeanInvoker::BIException& e)
	{
		std::string stErr = "[Msg 307] Error when invoking '%s;%s'. Reason: %s", 
			locationStr.c_str(), args, e.bInvException?e.bInvException:"unknown");
		throw InvokeErr(log_str);;
	}

	return res;
}

char* EJBInfo::DoInvoke(const char*invokeStr)
{
	boost::mutex::scoped_lock lock(m_init_lock);
	AutoEvent event(m_CallCompleted);

	  
	                                                                                                       
	  
	HANDLE hEvent,hOut,hErr,hJava;
	::DuplicateHandle(::GetCurrentProcess(),m_CallCompleted,::GetCurrentProcess(),&hEvent,0,TRUE,DUPLICATE_SAME_ACCESS);
	::DuplicateHandle(::GetCurrentProcess(),m_StdOut,::GetCurrentProcess(),&hOut,0,TRUE,DUPLICATE_SAME_ACCESS);
	::DuplicateHandle(::GetCurrentProcess(),m_StdErr,::GetCurrentProcess(),&hErr,0,TRUE,DUPLICATE_SAME_ACCESS);
	::DuplicateHandle(::GetCurrentProcess(),m_JavaProcHandle,::GetCurrentProcess(),&hJava,0,TRUE,DUPLICATE_SAME_ACCESS);

	bool bJavaTerminated=false;
	StdPipes pipes(hOut,hErr,hEvent,hJava,bJavaTerminated);
	HANDLE hThread = reinterpret_cast<HANDLE>(::_beginthread(WatcherThread,0,static_cast<void*>(&pipes)));

	char* ret;
	try
	{
		ret = Server()->invokeEJBean( invokeStr );
	}
	catch(...)
	{
		event.SetEvent();
		::WaitForSingleObject(hThread,INFINITE);
		if( bJavaTerminated )
		{
			::CloseHandle(m_JavaProcHandle);
			m_JavaProcHandle=0;
		}
		throw;
	}

	event.SetEvent();
	::WaitForSingleObject(hThread,INFINITE);
	if( bJavaTerminated )
	{
		::CloseHandle(m_JavaProcHandle);
		m_JavaProcHandle=0;
	}
	return ret;
}

  
                                                  
                                                   
                                                                                           
                                                  
  
KString InvokeEJB(IncomingMsg* pmsg, std::string locationStr, KString args)
{
	return g_ejb_info.InvokeEJB(locationStr, args);
}


#endif                         

