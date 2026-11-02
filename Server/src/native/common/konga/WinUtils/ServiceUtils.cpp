/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#include <windows.h>
#include <string>

#include <boost/noncopyable.hpp>
#include <boost/scoped_array.hpp>

#include <konga/Utils/CrException.h>
#include <konga/Utils/ErrorResolver.h>

#include "ServiceUtils.h"

using namespace konga;
using namespace std;

namespace {

	class ScHandlePtr : boost::noncopyable
	{
		public:
			ScHandlePtr(SC_HANDLE hMgr) : m_hMgr(hMgr) {}
			~ScHandlePtr() { if( m_hMgr ) ::CloseServiceHandle(m_hMgr); }

			SC_HANDLE Get() { return m_hMgr; }
			operator SC_HANDLE() { return m_hMgr; }
		private:
			SC_HANDLE m_hMgr;
	};

	SC_HANDLE kOpenSCManager(DWORD dwDesiredAccess) {
		SC_HANDLE h = ::OpenSCManager(0, 0, dwDesiredAccess);
		if( !h ) {
			long lErr = ::GetLastError();
			throw CrException("Failed to open the Windows Service Control "
				"Manager on the local machine. Reason: " + ErrorResolver().Resolve(lErr));
		}
		return h;
	}

	SC_HANDLE kOpenService(SC_HANDLE hMgr, const string& sServiceName, DWORD dwDesiredAccess) {
		SC_HANDLE h = ::OpenService(hMgr, sServiceName.c_str(), dwDesiredAccess);
		if( !h ) {
			long lErr = ::GetLastError();
			throw CrException("Failed to open the Windows Service '" + sServiceName +
				"' on the local machine. Reason: " + ErrorResolver().Resolve(lErr));
		}
		return h;
	}

}                       

bool IsServiceInstalled(const string& sServiceName)
{
    ScHandlePtr pMgr( kOpenSCManager(SC_MANAGER_ALL_ACCESS) );
	SC_HANDLE h = ::OpenService(pMgr, sServiceName.c_str(), SERVICE_QUERY_STATUS);
	if( !h ) {
		long lErr = ::GetLastError();
		if( lErr == ERROR_SERVICE_DOES_NOT_EXIST )
			return false;
		throw CrException("Failed to open the Windows Service '" + sServiceName +
			"' on the local machine. Reason: " + ErrorResolver().Resolve(lErr));
	}
	::CloseServiceHandle(h);
	return true;
}

void kCreateService(const string& sSvcName, const string& sSvcDisplayName,
				   const string& sSvcFileName, const string& sSvcDescription, 
				   const string& sDependOnSvc           , DWORD dwStartType                           )
{
    ScHandlePtr pMgr( kOpenSCManager(SC_MANAGER_ALL_ACCESS) );

	unsigned long nDblNullDependArray = sDependOnSvc.length() + 2;
	boost::scoped_array<char> pDblNullDependArray(new char[nDblNullDependArray]);
	memset(pDblNullDependArray.get(), 0, nDblNullDependArray);
	if( !sDependOnSvc.empty() )
		strcpy(pDblNullDependArray.get(), sDependOnSvc.c_str());

	                     
	ScHandlePtr pSvc( ::CreateService(pMgr, sSvcName.c_str(), sSvcDisplayName.c_str(), SERVICE_ALL_ACCESS,
		SERVICE_WIN32_OWN_PROCESS, SERVICE_AUTO_START, SERVICE_ERROR_NORMAL, sSvcFileName.c_str(),
		0, 0, pDblNullDependArray.get(), 0, ""));
	if( pSvc.Get() == 0 ) {
		long lErr = ::GetLastError();
		throw CrException("Failed to create the Windows Service '" + sSvcName +
			"' on the local machine. Reason: " + ErrorResolver().Resolve(lErr));
	}

	                      
	boost::scoped_array<char> pDesc(new char[sSvcDescription.length() + 1]);
	strcpy(pDesc.get(), sSvcDescription.c_str());

	SERVICE_DESCRIPTION desc;
	desc.lpDescription = pDesc.get();

	int res = ::ChangeServiceConfig2(pSvc, SERVICE_CONFIG_DESCRIPTION, &desc);
    if (res == 0) {
		long lErr = ::GetLastError();
		throw CrException("Failed to change the description of the Windows Service '" +
			sSvcName + "' on the local machine. Reason: " + ErrorResolver().Resolve(lErr));
	}
}

void SetServiceStartType(const std::string& sServiceName, DWORD dwStartType) {
    ScHandlePtr pMgr( kOpenSCManager(SC_MANAGER_ALL_ACCESS) );
	ScHandlePtr pSvc( ::OpenService(pMgr, sServiceName.c_str(), SERVICE_ALL_ACCESS));
	if( pSvc.Get() == 0 ) {
		long lErr = ::GetLastError();
		throw CrException("Failed to open the Windows Service '" + sServiceName +
			"' on the local machine. Reason: " + ErrorResolver().Resolve(lErr));
	}

	int res = ::ChangeServiceConfig(pSvc, SERVICE_WIN32_OWN_PROCESS, dwStartType,
									SERVICE_NO_CHANGE, 0, 0, 0, 0, 0, 0, 0);
	if (res == 0) {
		long lErr = ::GetLastError();
		throw CrException("Failed to set start type for the Windows Service '" + sServiceName +
			"' on the local machine. Reason: " + ErrorResolver().Resolve(lErr));
	}
}

void DependService(const string& sMotherSvc, const string& sChildSvc)
{
	if ( sMotherSvc.empty() ) return;
    ScHandlePtr pMgr( kOpenSCManager(SC_MANAGER_ALL_ACCESS) );
	ScHandlePtr pSvc( ::OpenService(pMgr, sChildSvc.c_str(), SERVICE_ALL_ACCESS));
	if( pSvc.Get() == 0 ) {
		long lErr = ::GetLastError();
		throw CrException("Failed to open the Windows Service '" + sChildSvc +
			"' on the local machine. Reason: " + ErrorResolver().Resolve(lErr));
	}

	const unsigned long nBufLen = sMotherSvc.length() + 2;                                                       
	auto_ptr<char>pBuf(new char[nBufLen]); 
	memset(pBuf.get(), 0, nBufLen);
	strcpy(pBuf.get(), sMotherSvc.c_str());
	int res = ::ChangeServiceConfig(pSvc, SERVICE_WIN32_OWN_PROCESS, SERVICE_NO_CHANGE,
									SERVICE_NO_CHANGE, 0, 0, 0, pBuf.get(), 0, 0, 0);
	if (res == 0) {
		long lErr = ::GetLastError();
		throw CrException("Failed to make the Windows Service '" + sChildSvc +
			"' depend on '" + sMotherSvc + "' on the local machine. Reason: " +
			ErrorResolver().Resolve(lErr));
	}
}

void SetServiceRecovery(const string& sServiceName, ULONG lDelay, SC_ACTION_TYPE ActionType)
{
    ScHandlePtr pMgr( kOpenSCManager(SC_MANAGER_ALL_ACCESS) );
	ScHandlePtr pSvc( ::OpenService(pMgr, sServiceName.c_str(), SERVICE_ALL_ACCESS));
	if( pSvc.Get() == 0 ) {
		long lErr = ::GetLastError();
		throw CrException("Failed to open the Windows Service '" + sServiceName +
			"' on the local machine. Reason: " + ErrorResolver().Resolve(lErr));
	}

    SC_ACTION Action;
    Action.Delay = lDelay;                   
	Action.Type = ActionType;

	SERVICE_FAILURE_ACTIONS SFA;  
	SFA.dwResetPeriod  = INFINITE;
	SFA.lpRebootMsg = NULL;
    SFA.lpCommand  = NULL;
	SFA.cActions  = 1;    
	SFA.lpsaActions = &Action;

	int res = ::ChangeServiceConfig2(pSvc, SERVICE_CONFIG_FAILURE_ACTIONS, &SFA);
    if (res == 0) {
		long lErr = ::GetLastError();
		throw CrException("Failed to change the configuration of the Windows Service '" +
			sServiceName + "' on the local machine. Reason: " + ErrorResolver().Resolve(lErr));
	}
}

void SetServiceRunAs(const string& sServiceName, const string& sUserName, const string& sPassword)
{
    ScHandlePtr pMgr( kOpenSCManager(SC_MANAGER_ALL_ACCESS) );
	ScHandlePtr pSvc( ::OpenService(pMgr, sServiceName.c_str(), SERVICE_ALL_ACCESS));
	if( pSvc.Get() == 0 ) {
		long lErr = ::GetLastError();
		throw CrException("Failed to open the Windows Service '" + sServiceName +
			"' on the local machine. Reason: " + ErrorResolver().Resolve(lErr));
	}

                               

	int res = ::ChangeServiceConfig(pSvc, SERVICE_NO_CHANGE, SERVICE_NO_CHANGE, SERVICE_NO_CHANGE,	
									 NULL, NULL, NULL, NULL, sUserName.c_str(), sPassword.c_str(), NULL);
	if (res == 0) {
		long lErr = ::GetLastError();
		throw CrException("Failed to change the configuration of the Windows Service '" + sServiceName +
			"' on the local machine. Reason: " + ErrorResolver().Resolve(lErr));
	}
}

SERVICE_STATUS GetServiceStatus(const string& sServiceName)
{
    ScHandlePtr pMgr( kOpenSCManager(SC_MANAGER_ALL_ACCESS) );
	ScHandlePtr pSvc( ::OpenService(pMgr, sServiceName.c_str(), SERVICE_ALL_ACCESS));
	if( pSvc.Get() == 0 ) {
		long lErr = ::GetLastError();
		throw CrException("Failed to open the Windows Service '" + sServiceName +
			"' on the local machine. Reason: " + ErrorResolver().Resolve(lErr));
	}

    SERVICE_STATUS ssStatus; 

	int res =::QueryServiceStatus(pSvc, &ssStatus);   
	if( res == 0 ) {
		long lErr = ::GetLastError();
		throw CrException("Failed to query service status of the Windows Service '" + sServiceName +
			"' on the local machine. Reason: " + ErrorResolver().Resolve(lErr));
	}
		
    return ssStatus;
}

bool IsServiceRunning(const string& sServiceName)
{	
    return GetServiceStatus(sServiceName).dwCurrentState == SERVICE_RUNNING;
}


void kStartService(const string& sServiceName)
{
    ScHandlePtr pMgr( kOpenSCManager(SC_MANAGER_ALL_ACCESS) );
	ScHandlePtr pSvc( ::OpenService(pMgr, sServiceName.c_str(), SERVICE_ALL_ACCESS));

    if( GetServiceStatus(sServiceName).dwCurrentState == SERVICE_STOPPED) {    
		if( !::StartService(pSvc, 0, NULL) ) {
			long lErr = ::GetLastError();
			throw CrException("Failed to start the Windows Service '" + sServiceName +
				"' on the local machine. Reason: " + ErrorResolver().Resolve(lErr));
		}
    } 
}

void kStopService(const string& sServiceName)
{
    ScHandlePtr pMgr( kOpenSCManager(SC_MANAGER_ALL_ACCESS) );
	ScHandlePtr pSvc( ::OpenService(pMgr, sServiceName.c_str(), SERVICE_ALL_ACCESS));
  
                                      
                               
                                                                           
                                                                       
  
  
	SERVICE_STATUS ssStatus;
	ssStatus.dwCurrentState = 0;

	int iResult = ::ControlService(pSvc, SERVICE_CONTROL_STOP, &ssStatus);

    while( (ssStatus.dwCurrentState != SERVICE_STOPPED) && iResult) {
		::Sleep(500);
		iResult = ::QueryServiceStatus(pSvc, &ssStatus);
    }

	if (ssStatus.dwCurrentState != SERVICE_STOPPED) {
		long lErr = ::GetLastError();
		throw CrException("Failed to stop the Windows Service '" + sServiceName +
			"' on the local machine. Reason: " + ErrorResolver().Resolve(lErr));
	}		
}

void kDeleteService(const string& sServiceName)
{
    ScHandlePtr pMgr( kOpenSCManager(SC_MANAGER_ALL_ACCESS) );
	ScHandlePtr pSvc( ::OpenService(pMgr, sServiceName.c_str(), SERVICE_ALL_ACCESS));

	if( !::DeleteService(pSvc) ) {
		long lErr = ::GetLastError();
		if( lErr == ERROR_SERVICE_MARKED_FOR_DELETE )                                
			return;
		throw CrException("Failed to delete the Windows Service '" + sServiceName +
			"' on the local machine. Reason: " + ErrorResolver().Resolve(lErr));
    } 
}

