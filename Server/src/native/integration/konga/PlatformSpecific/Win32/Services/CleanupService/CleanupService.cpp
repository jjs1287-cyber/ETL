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

#include <konga/FileCleanup/ComponentInfo.h>

#include "CleanupService.h"

using konga::CrException;

class CCleanupServiceModule : public CAtlServiceModuleT< CCleanupServiceModule, IDS_SERVICENAME >
{
	typedef CAtlServiceModuleT< CCleanupServiceModule, IDS_SERVICENAME > TheBaseClass;
public :
	DECLARE_LIBID(LIBID_CleanupServiceLib)
	DECLARE_REGISTRY_APPID_RESOURCEID(IDR_CLEANUPSERVICE, "{4EED6816-D6D2-434B-9B98-93EFA6B38E9F}")
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
			m_sProcessPath = konga::GetServerHomeWithSlash() + "bin\\ETLForgeFileCleanup.exe";
			m_lPort = konga::GetOptionAsInt(konga::stFILE_CLEANUP_COMPONENT_NAME, "CommandPort", konga::lFILE_CLEANUP_DEFAULT_PORT);
			return S_OK;
		} catch(CrException& e) {
			ReportError("Failed to read configuration. Reason: " + e.GetErrorString(), "File Cleanup Service");
			return E_FAIL;
		}
	}

	HRESULT Run(int nShowCmd = SW_HIDE) throw() {
		SetServiceStatus(SERVICE_RUNNING);
		HRESULT hr = ReadConfig();
		if( FAILED(hr) )
			return hr;

		hr = konga::ServiceHelper::ServiceRun(m_sProcessPath, m_lPort, "FileCleanup");
		if( FAILED(hr) )
			return hr;
		return TheBaseClass::Run();
	}

	void OnStop() throw() {
		if( FAILED( ReadConfig() ) )
			return;

		konga::ServiceHelper::ServiceStop(m_sProcessPath, m_lPort, "FileCleanup");
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

CCleanupServiceModule _AtlModule;


  
extern "C" int WINAPI _tWinMain(HINSTANCE              , HINSTANCE                  , 
                                LPTSTR lpCmdLine, int nShowCmd)
{
                
                                                          
	return _AtlModule.WinMain(nShowCmd);
}

