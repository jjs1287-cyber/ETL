/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */

#ifdef DS_CLUSTER
                      

#include "EngineCore.h"
#include "FaultTolerantObject.h"
#include "Messenger/msgrCore.h"	                  

extern FaultTolerantObject g_FaultTolerantObj;                             
extern Messenger *g_msgr;

extern "C" void CALLBACK FaultToleranceHeartbeat(HWND hwnd, UINT uMsg, UINT idEvent, DWORD dwTime)
{
	if( g_exiting ) return;
	g_FaultTolerantObj.SendHeartbeat();
}

std::string EngineCore::GetPrimaryMachineName() const
{
	return GetOptionAsString( "Settings/FaultTolerance", "PrimaryDbServer", "Unknown");
}

std::string EngineCore::GetSecondaryMachineName() const
{
	return GetOptionAsString( "Settings/FaultTolerance", "SecondaryDbServer", "Unknown");
}

bool EngineCore::SwitchDatabases(const std::string stNewServer       )
{
	bool bRet = LoadDBConnectionInfo(stNewServer);
	if( !bRet ) return false;

	  
	                                         
	  
	DisconnectDataSources();
	ConnectDataSources();

	if( g_msgr )
	{
		g_msgr->DisconnectDataSources();
		g_msgr->ConnectDataSources();
		g_msgr->SetupSMTP();
		g_msgr->SetupPOP();
	}

	g_thread_manager.SwitchDatabaseServers(stNewServer);
	return true;
}

#endif              

