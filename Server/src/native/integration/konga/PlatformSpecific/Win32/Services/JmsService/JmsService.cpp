/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
                                             

#include "stdafx.h"
#include "resource.h"

#include <konga/Db/TranDbUtils/TranDbUtils.h>	                                  

#include <konga/PlatformSpecific/Win32/Services/ServiceHelper/ServiceHelper.h>

#include <konga/Log/logging.h>			                                
#include <konga/Server/JavaLaunchDetails.h>		                           
#include <konga/Server/Server.h>		                         

#include <konga/Utils/CrException.h>
#include <konga/Utils/FileUtils.h>	                        
#include <konga/Utils/Settings.h>	                   

#include <konga/JmsEngine/ComponentInfo.h>

#include "JmsService.h"

using namespace konga;
using namespace std;

class CJmsServiceModule : public CAtlServiceModuleT< CJmsServiceModule, IDS_SERVICENAME >
{
	typedef CAtlServiceModuleT< CJmsServiceModule, IDS_SERVICENAME > TheBaseClass;
public :
	DECLARE_LIBID(LIBID_JmsServiceLib)
	DECLARE_REGISTRY_APPID_RESOURCEID(IDR_JMSSERVICE, "{4EED6816-D6D2-434B-9B98-93EFA6B38E9E}")
	HRESULT InitializeSecurity() throw()
	{
		                                                                                      
		               
		                                         
		                                                   
		                                                  

		return S_OK;
	}

	HRESULT ReadConfig() {
		try {
			GetJavaLaunchDetails(m_launchDetails, konga::stJMS_SERVICE_COMPONENT_NAME, konga::lJMS_SERVICE_DEFAULT_PORT);
			return S_OK;
		} catch(CrException& e) {
			ReportError("Failed to read configuration. Reason: " + e.GetErrorString(), "ETLForge JMS Service");
			return E_FAIL;
		}
	}

	HRESULT Run(int nShowCmd = SW_HIDE) throw() {
		HRESULT hr = ReadConfig();
		if( FAILED(hr) )
			return hr;

		hr = konga::ServiceHelper::ServiceRun(m_launchDetails.sProcessPath, m_launchDetails.lPort, konga::stJMS_SERVICE_COMPONENT_NAME,
												m_launchDetails.sArgs, m_launchDetails.sWorkingDir, m_launchDetails.sStdOutPath, m_launchDetails.sStdErrPath);
		if( FAILED(hr) )
			return hr;
		SetServiceStatus(SERVICE_RUNNING);
		return TheBaseClass::Run();
	}

	void OnStop() throw() {
		if( FAILED( ReadConfig() ) )
			return;

		konga::ServiceHelper::ServiceStop("etlforge-jms-engine.jar", m_launchDetails.lPort, konga::stJMS_SERVICE_COMPONENT_NAME, ServiceHelper::SVC_SHUTDOWN);
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
	JavaLaunchDetails m_launchDetails;
};

CJmsServiceModule _AtlModule;


  
extern "C" int WINAPI _tWinMain(HINSTANCE              , HINSTANCE                  , 
                                LPTSTR lpCmdLine, int nShowCmd)
{
                
                                                          
                    
	return _AtlModule.WinMain(nShowCmd);
}

