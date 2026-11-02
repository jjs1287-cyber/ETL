/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */

#include "stdafx.h"
#include "resource.h"

#include <konga/PlatformSpecific/Win32/Services/ServiceHelper/ServiceHelper.h>

#include <konga/Log/logging.h>			                                
#include <konga/Utils/CrException.h>
#include <konga/Utils/FileUtils.h>	                        
#include <konga/Utils/Settings.h>	                   

#include <konga/Scheduler/ComponentInfo.h>

#include "SchedulerService.h"

using konga::CrException;

class CSchedulerServiceModule : public CAtlServiceModuleT< CSchedulerServiceModule, IDS_SERVICENAME >
{
	typedef CAtlServiceModuleT< CSchedulerServiceModule, IDS_SERVICENAME > TheBaseClass;
public :
	DECLARE_LIBID(LIBID_SchedulerServiceLib)
	DECLARE_REGISTRY_APPID_RESOURCEID(IDR_SCHEDULERSERVICE, "{75FF50CA-AFA3-438D-955A-C549558AE3CC}")
	HRESULT InitializeSecurity() throw()
	{
		                                                                                      
		               
		                                         
		                                                   
		                                                  

		return S_OK;
	}

	                                                   
	                                                            
	                          
	HRESULT Start(int nShowCmd) throw()
	{
		                                  
		                    
		m_bService = TRUE;

		if (m_bService)
		{
			SERVICE_TABLE_ENTRY st[] =
			{
				{ m_szServiceName, _ServiceMain },
				{ NULL, NULL }
			};
			if (::StartServiceCtrlDispatcher(st) == 0)
				m_status.dwWin32ExitCode = GetLastError();
			return m_status.dwWin32ExitCode;
		}
		                                                  
		                     
		m_status.dwWin32ExitCode = this->Run(nShowCmd);
		return m_status.dwWin32ExitCode;
	}

	HRESULT ReadConfig() {
		try {
			m_sProcessPath = konga::GetServerHomeWithSlash() + "bin\\ETLForgeScheduler.exe";
			m_lPort = konga::GetOptionAsInt(konga::stSCHEDULER_COMPONENT_NAME, "CommandPort", konga::lSCHEDULER_DEFAULT_PORT);
			return S_OK;
		} catch( const CrException& e ) {
			ReportError("Failed to read configuration. Reason: " + e.GetErrorString(), "Scheduler Service");
			return E_FAIL;
		}
	}

	HRESULT Run(int nShowCmd = SW_HIDE) throw() {
		HRESULT hr = ReadConfig();
		if( FAILED(hr) )
			return hr;

		hr = konga::ServiceHelper::ServiceRun(m_sProcessPath, m_lPort, "Scheduler");
		if( FAILED(hr) )
			return hr;
		return TheBaseClass::Run();
	}

	void OnStop() throw() {
		if( FAILED( ReadConfig() ) )
			return;

		konga::ServiceHelper::ServiceStop(m_sProcessPath, m_lPort, "Scheduler");
		TheBaseClass::OnStop();
	}

	  
	                                                                                       
	                                                                                   
	  
	HRESULT RegisterClassObjects(DWORD dwClsContext, DWORD dwFlags) throw()
	{
		HRESULT hr = AtlComModuleRegisterClassObjects(&_AtlComModule, dwClsContext, dwFlags);		
		return SUCCEEDED(hr) ? S_OK : hr;
	}
	HRESULT RevokeClassObjects() throw()
	{
		HRESULT hr = AtlComModuleRevokeClassObjects(&_AtlComModule);
		return SUCCEEDED(hr) ? S_OK : hr;
	}

private:
	std::string m_sProcessPath;
	unsigned long m_lPort;
};

CSchedulerServiceModule _AtlModule;


  
extern "C" int WINAPI _tWinMain(HINSTANCE              , HINSTANCE                  , 
                                LPTSTR              , int nShowCmd)
{
    return _AtlModule.WinMain(nShowCmd);
}

