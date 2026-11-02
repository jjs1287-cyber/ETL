/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */

#ifndef AUTOCLEANUP_H
#define AUTOCLEANUP_H

  
                                                                     
                                                                     
                                                                     
                          
                                                                        
                                                                      
                                                                         
  


#ifdef WIN32
#include <windows.h>
#endif         

#include <boost/noncopyable.hpp>

#include "AutoDbCleanup.h"

namespace konga {

#ifdef WIN32
class AutoHandle : boost::noncopyable
{
public:
	AutoHandle(HANDLE& h) : m_bClose(true), m_h(h) { }
	~AutoHandle() {
		if ( m_bClose && m_h ) {
			::CloseHandle(m_h);
			m_h = 0;
		}
	}

	                                                       
	void Release() {
		m_bClose = false;
	}
private:
	bool m_bClose;
	HANDLE& m_h;
};
#endif         

#if defined(_WININET_)
#include <winsock2.h>	          

class AutoInternetHandle : boost::noncopyable
{
public:
	AutoInternetHandle(HINTERNET& h) : m_hInetHandle(h) { }
	~AutoInternetHandle() { Close(); }
	void Close()
	{
#ifndef _DEBUG
		try{
#endif		::InternetCloseHandle( m_hInetHandle );
		m_hInetHandle = 0;
#ifndef _DEBUG
		} catch(...) {}
#endif
	}
private:
	HINTERNET& m_hInetHandle;
};

struct AutoWinsockInit : boost::noncopyable
{
	AutoWinsockInit()
	{
		WSADATA wsaData;
		WORD wVersion = MAKEWORD(1,1);
		::WSAStartup(wVersion, &wsaData);
	}
	~AutoWinsockInit() { ::WSACleanup(); }
};

#endif             

                                                                                 
template<typename T>
class AutoAssign : boost::noncopyable
{
public:
	AutoAssign(T& variable, const T& value) : m_var(variable), m_value(value) {}
	~AutoAssign() {
		m_var = m_value;
	}

private:
	T& m_var;
	T m_value;
};

                                                                                                       
                                                                                                                                
template<typename T>
class LinkVariables : boost::noncopyable
{
public:
	LinkVariables(T& target, T& source) : m_TargetRef(target), m_SourceRef(source) {}
	~LinkVariables() { m_TargetRef = m_SourceRef; }
private:
	T& m_TargetRef;
	const T& m_SourceRef;                                                                                                
};

                                                                        
template<typename T>
class AutoCallRelease : boost::noncopyable
{
public:
	AutoCallRelease(T* pObj) : m_pObj(pObj) {}
	~AutoCallRelease() { Release(); }
	void Release()
	{
		if ( m_pObj != 0 )
			m_pObj->Release();
	}
	void Cancel() { m_pObj = 0; }
private:
	T* m_pObj;
};

                                                                          
template <typename T>
class AutoCallTerminate : boost::noncopyable
{
public:
	AutoCallTerminate(T* pObj) : m_pObj(pObj) {}
	~AutoCallTerminate() { Terminate(); }
	void Terminate()
	{
		if( m_pObj != 0 )
			m_pObj->Terminate();
	}
	void Attach(T* pObj)
	{
		Terminate();
		m_pObj = pObj;
	}
	void Cancel() { m_pObj = 0; }
private:
	T* m_pObj;
};

}         

#endif                 

