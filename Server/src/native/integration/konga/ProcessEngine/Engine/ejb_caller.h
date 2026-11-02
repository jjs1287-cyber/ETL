/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */

#if defined(DS_USE_CORBA)

#ifndef EJB_CALLER_H
#define EJB_CALLER_H

#include <process.h>                                  
#include "corba_util.h"
#include "util.h"			                        
#include "logging.h"

#include "AutoCleanup.h"	                         
#include "BeanInvoker.h"	                           
using namespace BInvoker;

extern std::string g_machine_name;

struct StdPipes
{ 
	StdPipes(HANDLE o, HANDLE e, HANDLE evt, HANDLE java, bool& bTerm) :
		 StdOut(o), StdErr(e), Event(evt), hJava(java), bJavaTerminated(bTerm) {};
	HANDLE StdOut, StdErr, Event, hJava;
	bool& bJavaTerminated;
};

class AutoEvent
{
public:
	AutoEvent(HANDLE event) : m_event(event) { ::ResetEvent( m_event ); }
	~AutoEvent() { ::SetEvent( m_event ); }
	bool SetEvent() { return ::SetEvent( m_event ) == TRUE; }
	bool ResetEvent() { return ::ResetEvent( m_event ) == TRUE; }
private:
	HANDLE m_event;
};	

void WatcherThread(void*p);

class EJBInfo {
public:
	EJBInfo() : m_JavaProcHandle(0), m_JavaProcId(0), m_StdOut(0), m_StdErr(0), 
		m_ShowOutput(false)
	{
		::InitializeCriticalSection(&m_init_lock);
		::InitializeCriticalSection(&m_call_lock);
		m_CallCompleted = CreateEvent(0, true, true, 0);                                 
	}
	~EJBInfo()
	{
		if( m_StdOut ) CloseHandle(m_StdOut);
		if( m_StdErr ) CloseHandle(m_StdErr);
		::DeleteCriticalSection(&m_init_lock);
		::DeleteCriticalSection(&m_call_lock);
		::CloseHandle( m_CallCompleted );	
		if( !CORBA_is_nil( m_bInv ) )
			CORBA_release( m_bInv );
		CloseJava();
	}

	KString GetLocationString()
	{
		KString res = "corbaloc::";
		res += GetOptionAsString("konga/Settings","EjbInvokerServer",g_machine_name.c_str()).c_str();
		res += ":";
		res += GetOptionAsString("konga/Settings","EjbInvokerPort","2000").c_str();
		res += "/BInv";
		return res;
	}

	bool IsLocal()
	{		
		return stricmp(g_machine_name.c_str(), 
			GetOptionAsString("konga/Settings",
								"EjbInvokerServer",g_machine_name.c_str()).c_str()) == 0;
	}

	bool LoadEjbCaller();
	void UnLoadEjbCaller();

	bool ExecuteServer();
	HRESULT Exec(TCHAR* args);
	HRESULT HandleError(long err, bool append          );

	BeanInvoker_ptr Server() { return m_bInv; }
	KString InvokeEJB(std::string locationStr, KString args);

	void Release()
	{
		try
		{
			CORBA::release( m_bInv );
			m_bInv = BeanInvoker::_nil();
		}
		catch(CORBA_SystemException&)
		{
		}
		catch(...) {}
	}

protected:

	char* DoInvoke(const char*invokeStr);

	void CloseJava()
	{
		__try {                                                                 
			if( m_JavaProcHandle )
			{
				::TerminateProcess(m_JavaProcHandle,0);
				::CloseHandle( m_JavaProcHandle );
			}
		} __except (1) {}
		m_JavaProcHandle = 0;
	}

private:
	boost::mutex m_init_lock;                                    
	boost::mutex m_call_lock;                             
	HANDLE m_StdOut, m_StdErr, m_CallCompleted;
	HANDLE m_JavaProcHandle;
	DWORD m_JavaProcId;

	BeanInvoker_ptr m_bInv;
	std::string m_LastError, m_LastOutput, m_LastErrOutput;
	KString m_csLocationStr;

	bool m_ShowOutput;
};

#endif                

#endif                         

