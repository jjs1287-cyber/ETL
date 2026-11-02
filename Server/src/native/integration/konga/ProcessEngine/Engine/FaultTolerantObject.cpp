/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */

#ifdef DS_CLUSTER

#include "FaultTolerantObject.h"
#include "DBConnectionInfo.h"	                         
#include "EngineCore.h"			                   
#include "Messenger/msgrCore.h"	                  
#include "ErrorResolver.h"		                      
#include "engine_util.h"		                     
#include "../CRServer.h"		                  

extern CCRServer *g_server;				                          
extern DBConnectionInfo g_pdb_connect;	                            
extern EngineCore* g_engine;			                            
extern Messenger *g_msgr;
extern std::string g_machine_name;		                            
extern bool g_bDbNotConnected;			                            

bool IsNetworkError(HRESULT hr)
{
	switch(hr & 0xffff)
	{
	case 4003:                           
	case RPC_S_CALL_FAILED:
	case RPC_S_SERVER_UNAVAILABLE:
	case CS_E_NETWORK_ERROR:
		return true;
	default: return false;
	}
}

  
                                
  
extern "C" void CALLBACK FaultToleranceHeartbeat(HWND hwnd, UINT uMsg, UINT idEvent, DWORD dwTime);

FaultTolerantObject::FaultTolerantObject() :
	m_iHeartbeatTimerId(0), m_iHeartbeatTimerInterval(5000),
	m_ftState(ftNORMAL), m_bSetWaitingIntention(false)
{
}

FaultTolerantObject::~FaultTolerantObject()
{
	Cleanup();
}

void FaultTolerantObject::Init()
{
	if( !g_engine ) return;
	m_stPrimaryMachineName = g_engine->GetPrimaryMachineName();
	m_stSecondaryMachineName = g_engine->GetSecondaryMachineName();

	m_iHeartbeatTimerInterval = GetOptionAsInt( "Settings/FaultTolerance", "HeartbeatIntervalSeconds", 5);
	m_iHeartbeatTimerInterval *= 1000;                            

	m_ftState = stricmp( GetOptionAsString( "Settings/FaultTolerance", "EngineState", "Normal").c_str(), "Normal") == 0 ?
		ftNORMAL : ftWAITING;

	std::string stLastServerForEngine = GetOptionAsString( "Settings/FaultTolerance", "LastServerForEngine", "Primary");

	m_stIntention = (m_ftState == ftNORMAL) ? stLastServerForEngine : "Waiting";

	m_enCurrentServer = (stLastServerForEngine == "Primary") ? csPRIMARY : csSECONDARY;

	if( ::IsSingleServer() )
		return;

	switch( m_enCurrentServer )
	{
	case csPRIMARY:
		LoadPrimaryMonitor(m_stPrimaryMachineName);
		break;
	case csSECONDARY:
		LoadSecondaryMonitor(m_stSecondaryMachineName);
		break;
	}
}

void FaultTolerantObject::DoLoadPrimaryMonitor(const std::string& stPrimaryMachineName)
{
	USES_CONVERSION;
	COSERVERINFO serverInfo;
	serverInfo.dwReserved1 = serverInfo.dwReserved2 = 0;
	serverInfo.pAuthInfo = 0;
	serverInfo.pwszName = A2W(stPrimaryMachineName.c_str());

	const IID IID_ICrMonitorControl = {0xC8BD26EC,0x347F,0x49DE,{0xAF,0x44,0x5A,0x6B,0xBF,0xE1,0x16,0xA0}};
	MULTI_QI multi_qi[] = { &IID_ICrMonitorControl, 0, 0 } ;

	HRESULT hr = ::CoCreateInstanceEx(__uuidof(CrMonitorControl),0,CLSCTX_SERVER,&serverInfo,1,multi_qi);
	if( FAILED(hr) ) throw _com_error(hr);
	if( FAILED(multi_qi[0].hr) ) throw _com_error(multi_qi[0].hr);

	m_PrimaryMonitor = multi_qi[0].pItf;
}

void FaultTolerantObject::LoadPrimaryMonitor(const std::string& stPrimaryMachineName)
{
	try
	{
		DoLoadPrimaryMonitor(stPrimaryMachineName);
	}
	catch(_com_error& e)
	{
		std::string stErr = "[Msg 407] Failed to connect to primary monitor on '" + stPrimaryMachineName + "'. Reason: ";
		if( e.Description().length() )
		{
			stErr += (LPCTSTR)e.Description();
			stErr += " \n";
		}
		if( e.Error() != E_FAIL )
			stErr += ErrorResolver().Resolve(e.Error());
		char buf[64]; sprintf(buf,"\n(0x%x)",e.Error());
		stErr += buf;
		ReportError(g_log_type, stErr);
		return;
	}
}

void FaultTolerantObject::DoLoadSecondaryMonitor(const std::string& stSecondaryMachineName)
{
	USES_CONVERSION;
	COSERVERINFO serverInfo;
	serverInfo.dwReserved1 = serverInfo.dwReserved2 = 0;
	serverInfo.pAuthInfo = 0;
	serverInfo.pwszName = A2W(stSecondaryMachineName.c_str());

	const IID IID_ICrMonitorControl = {0xC8BD26EC,0x347F,0x49DE,{0xAF,0x44,0x5A,0x6B,0xBF,0xE1,0x16,0xA0}};
	MULTI_QI multi_qi[] = { &IID_ICrMonitorControl, 0, 0 } ;

	HRESULT hr = ::CoCreateInstanceEx(__uuidof(CrMonitorControl),0,CLSCTX_SERVER,&serverInfo,1,multi_qi);
	if( FAILED(hr) ) throw _com_error(hr);
	if( FAILED(multi_qi[0].hr) ) throw _com_error(multi_qi[0].hr);

	m_SecondaryMonitor = multi_qi[0].pItf;
}

void FaultTolerantObject::LoadSecondaryMonitor(const std::string& stSecondaryMachineName)
{
	try
	{
		DoLoadSecondaryMonitor(stSecondaryMachineName);
	}
	catch(_com_error& e)
	{
		std::string stErr = "[Msg 443] Failed to connect to secondary monitor on '" + stSecondaryMachineName + "'. Reason: ";
		if( e.Description().length() )
		{
			stErr += (LPCTSTR)e.Description();
			stErr += " \n";
		}
		if( e.Error() != E_FAIL )
			stErr += ErrorResolver().Resolve(e.Error());
		char buf[64]; sprintf(buf,"\n(0x%x)",e.Error());
		stErr += buf;
		ReportError(g_log_type, stErr);
		return;
	}
}

void FaultTolerantObject::StartHeartbeat()
{
	if( (m_iHeartbeatTimerId = ::SetTimer(0,0,m_iHeartbeatTimerInterval,FaultToleranceHeartbeat)) == 0 )
	{
		ReportError(g_log_type, "[Msg 409] Failed to create timer. Will not be able to send heartbeats.",
			EVENTLOG_ERROR_TYPE);
	}		
}

void FaultTolerantObject::StopHeartbeat()
{
	if( m_iHeartbeatTimerId ) ::KillTimer(0, m_iHeartbeatTimerId);
}

void FaultTolerantObject::Cleanup()
{
	StopHeartbeat();
}

void FaultTolerantObject::SendHeartbeat()
{
	if(m_ftState == ftSWITCHED_INTENTION )
	{
		try
		{
			if( m_enCurrentServer == csPRIMARY )
			{
				_bstr_t bsSecondaryCurSvr = m_SecondaryMonitor->CurrentServer;
				if( stricmp(bsSecondaryCurSvr, "Secondary") == 0 && m_stIntention == "Secondary" )
					SwitchServers();
			}
			if( m_enCurrentServer == csSECONDARY )
			{
			}
		}
		catch(_com_error&)
		{
		}				
	}
	
	AppHealthStatus eStatus = appUNDEFINED;
	switch( m_enCurrentServer )
	{
	case csPRIMARY:
		switch( eStatus = SendHeartbeatToPrimary() )
		{
		case appHEALTHY:
			switch(m_ftState)
			{
			case ftNORMAL:                                             
			case ftDISCONNECTED:
				if( g_engine )
				{
					int iErrStat = g_engine->GetErrorStatus();
					if(iErrStat & esNOT_READY ) g_engine->RemoveErrorStatus(esNOT_READY);
					if(iErrStat & esWAITING ) g_engine->RemoveErrorStatus(esWAITING);
					if( g_bDbNotConnected )
						g_engine->SwitchDatabases("Primary");
				}
				m_ftState = ftNORMAL;
				break;
			case ftWAITING:
				try
				{
					_bstr_t bsPrimaryCurSvr = m_PrimaryMonitor->CurrentServer;
					if( stricmp(bsPrimaryCurSvr, "Primary") == 0 && m_stIntention == "Primary" )
						SwitchServers();
				}
				catch(_com_error&)
				{
				}
				break;
			case ftSWITCHED_INTENTION:	
				if( m_stIntention == "Secondary" )	                                                  
				{									                                                    
					m_stIntention = "Primary";		                            
					m_ftState = ftNORMAL;
				}
				else
				{
					std::string stErr = "[Msg NNN] Unexpected state. Please report. Heartbeat to Primary ('" +
						m_stPrimaryMachineName + "'). Intention is " + m_stIntention;
					ReportError(g_log_type, stErr, EVENTLOG_AUDIT_FAILURE);
				}
				break;
			default:
				;
			}
			break;
		case appWAITING:	                                                   
							                                                   
							                            
							                                                            
			break;
		case appPRESUMEDEAD:                 
			break;
		case appNOTREADY:	                                          
			if( g_engine ) 
				g_engine->AddErrorStatus(esNOT_READY);                                     
			break;
		case appPERMANENTFAILURE:
			RegisterIntentionToSwitchServers();
			if(m_ftState == ftSWITCHED_INTENTION )
			{
				try
				{
					_bstr_t bsSecondaryCurSvr = m_SecondaryMonitor->CurrentServer;
					if( stricmp(bsSecondaryCurSvr, "Secondary") == 0 && m_stIntention == "Secondary" )
						SwitchServers();
				}
				catch(_com_error&)
				{
				}				
			}
			break;
		case appWRONGSERVER:                                                                                     
			RegisterIntentionToSwitchServers();
			if( m_ftState == ftSWITCHED_INTENTION )	                                         
			{
				try
				{
					_bstr_t bsSecondaryCurSvr = m_SecondaryMonitor->CurrentServer;
					if( stricmp(bsSecondaryCurSvr, "Secondary") == 0 && m_stIntention == "Secondary" )
						SwitchServers();
					else if( stricmp(bsSecondaryCurSvr, "Waiting") == 0 )
					{
						EnterWaitingState();
						m_enCurrentServer = csSECONDARY;
					}
				}
				catch(_com_error&)
				{
				}				
			}
			break;
		case appNOTAMEMBER:
			ReportError(g_log_type, "[Msg 423] The ETLForge Monitor (Primary) on machine '" + m_stPrimaryMachineName + 
				"' reports that this server (" + g_machine_name + ") is not a member of its cluster.");
			if( g_server )
				g_server->Stop();
			break;
		default:

			  
			                                                                                                 
			                                               
			  
			if( m_ftState == ftDISCONNECTED && IsPrimaryTheCurrentServer() )
			{
				RegisterIntentionToSwitchServers();
				if( m_ftState == ftSWITCHED_INTENTION )	                                         
					SwitchServers();
			}
			else if(m_ftState == ftSWITCHED_INTENTION )
			{
				try
				{
					_bstr_t bsSecondaryCurSvr = m_SecondaryMonitor->CurrentServer;
					if( stricmp(bsSecondaryCurSvr, "Secondary") == 0 && m_stIntention == "Secondary" )
						SwitchServers();
				}
				catch(_com_error&)
				{
				}				
			}
		}
		break;
	case csSECONDARY:
		switch( eStatus = SendHeartbeatToSecondary() )
		{
		case appHEALTHY:
			switch(m_ftState)
			{
			case ftNORMAL:
				                                            
				if( g_engine )
				{
					int iErrStat = g_engine->GetErrorStatus();
					if(iErrStat & esNOT_READY ) g_engine->RemoveErrorStatus(esNOT_READY);
					if(iErrStat & esWAITING ) g_engine->RemoveErrorStatus(esWAITING);
					if( g_bDbNotConnected )
						g_engine->SwitchDatabases("Secondary");
				}
				break;
			case ftDISCONNECTED:
				  
				                                                                                       
				                                
				  
				if( m_stIntention == "Secondary" )
					m_ftState = ftNORMAL;
				break;
			case ftWAITING:
			case ftSWITCHED_INTENTION:
				try
				{
					_bstr_t bsSecondaryCurSvr = m_SecondaryMonitor->CurrentServer;
					if( stricmp(bsSecondaryCurSvr, "Secondary") == 0 && m_stIntention == "Secondary" ||
						stricmp(bsSecondaryCurSvr, "Primary") == 0 && m_stIntention == "Waiting" )
						SwitchServers();
					else if( stricmp(bsSecondaryCurSvr, "Secondary") == 0 && m_stIntention == "Primary" )
					{
						m_stIntention = "Secondary";                                                              
						m_ftState = ftNORMAL;
					}
				}
				catch(_com_error&)
				{
				}				
				break;
			default:
				;
			}
			break;
		case appWAITING:	                                                   
							                                                   
							                            
							                                                            
			EnterWaitingState();
			break;
		case appPRESUMEDEAD:                 
			break;
		case appNOTREADY:	                                          
			if( g_engine ) 
				g_engine->AddErrorStatus(esNOT_READY);                                     
			break;
		case appPERMANENTFAILURE:
			RegisterIntentionToSwitchServers();
			if(m_ftState == ftSWITCHED_INTENTION )
			{
				try
				{
					_bstr_t bsPrimaryCurSvr = m_PrimaryMonitor->CurrentServer;
					if( stricmp(bsPrimaryCurSvr, "Primary") == 0 && m_stIntention == "Primary" )
						SwitchServers();
				}
				catch(_com_error&)
				{
				}				
			}
			break;
		case appWRONGSERVER:                                                                                     
			RegisterIntentionToSwitchServers();
			if( m_ftState == ftSWITCHED_INTENTION )	                                         
			{
				try
				{
					_bstr_t bsPrimaryCurSvr = m_PrimaryMonitor->CurrentServer;
					if( stricmp(bsPrimaryCurSvr, "Primary") == 0 )
						SwitchServers();
				}
				catch(_com_error&)
				{
				}				
			}
			break;
		case appNOTAMEMBER:
			ReportError(g_log_type, "[Msg 424] The ETLForge Monitor (Secondary) on machine '" + m_stSecondaryMachineName + 
				"' reports that this servers is no longer a member of its cluster.");
			if( g_server )
				g_server->Stop();
			break;
		default:

			if( g_engine ) 
				g_engine->AddErrorStatus(esNOT_READY);                                     

			  
			                                                                 
			                                                                                      
			  
			if( m_ftState == ftDISCONNECTED && IsSecondaryTheCurrentServer() )
			{
				RegisterIntentionToSwitchServers();
				if( m_ftState == ftNORMAL )	                                         
					SwitchServers();
			}
		}
		break;
		default:
			;
	}

	if( eStatus == appPRESUMEDEAD )
	{
		AbandonAllJobs();
		return;
	}

	if( m_bSetWaitingIntention )
	{
		try
		{
			m_SecondaryMonitor->SetEngineIntention(g_machine_name.c_str(), "Waiting");
			m_stIntention = "Waiting";
			m_ftState = ftWAITING;
			SetOptionAsString( "Settings/FaultTolerance", "EngineState", "Waiting");
			m_bSetWaitingIntention = false;
		}
		catch(_com_error& e)
		{
			std::string stErr = "[Msg 418] Failed to set intention to Waiting for secondary monitor on '" + m_stSecondaryMachineName + "'. Reason: ";
			if( e.Description().length() )
				stErr += (LPCTSTR)e.Description();
			char buf[64]; sprintf(buf,"\n(0x%x)",e.Error());
			stErr += buf;
			ReportError(g_log_type, stErr);
		}
	}
}

void FaultTolerantObject::EnterWaitingState()
{
	if( m_ftState == ftWAITING )
	{
		try
		{
			m_SecondaryMonitor->SetEngineIntention(g_machine_name.c_str(), "Waiting");
		}
		catch(_com_error& e) 
		{
			if( !IsNetworkError(e.Error()) )
			{
				std::string stErr = "[Msg 422] Failed to set intention to Waiting for secondary monitor on '" + m_stSecondaryMachineName + "'. Reason: ";
				if( e.Description().length() )
					stErr += (LPCTSTR)e.Description();
				char buf[64]; sprintf(buf,"\n(0x%x)",e.Error());
				stErr += buf;
				ReportError(g_log_type, stErr);
			}
		}
	}

	g_engine->AddErrorStatus(esWAITING);                                                    
	if( g_engine->GetNumberOfRunningTasks() == 0 )
	{
		m_bSetWaitingIntention = true;
		g_engine->CleanupMessages();
	}
}


AppHealthStatus FaultTolerantObject::SendHeartbeatToPrimary()
{
	AppHealthStatus eStatus = appUNDEFINED;
	try
	{
		eStatus = m_PrimaryMonitor->AcceptHBOfEngine(g_machine_name.c_str(), g_server->StatusNumber());
	}
	catch(_com_error& e)
	{
		if( m_ftState == ftNORMAL )
		{
			std::string stErr = "[Msg 410] Failed to send heart beat to primary monitor on '" + m_stPrimaryMachineName + "'. Reason: ";
			if( e.Description().length() )
				stErr += (LPCTSTR)e.Description();
			else stErr += ErrorResolver().Resolve( e.Error() );
			char buf[64]; sprintf(buf,"\n(0x%x)",e.Error());
			stErr += buf;
			ReportError(g_log_type, stErr);
		}

		  
		                                             
		                                                                 
		  
		try
		{
			DoLoadPrimaryMonitor(m_stPrimaryMachineName);
		}
		catch(_com_error&)
		{
			RegisterIntentionToSwitchServers();
		}
	}

	return eStatus;
}

AppHealthStatus FaultTolerantObject::SendHeartbeatToSecondary()
{
	AppHealthStatus eStatus = appUNDEFINED;
	try
	{
		long lStatus = 0;
		g_server->get_StatusNumber(&lStatus);
		eStatus = m_SecondaryMonitor->AcceptHBOfEngine(g_machine_name.c_str(), lStatus);
	}
	catch(_com_error& e)
	{
		try {
			std::string stErr = "[Msg 411] Failed to send heart beat to secondary monitor on '" + m_stSecondaryMachineName + "'. Reason: ";
			if( e.Description().length() )
				stErr += (LPCTSTR)e.Description();
			char buf[64]; sprintf(buf,"\n(0x%x)",e.Error());
			stErr += buf;
			ReportError(g_log_type, stErr);
		} catch(...) {}

		  
		                                               
		                                                               
		  
		try
		{
			DoLoadSecondaryMonitor(m_stSecondaryMachineName);
		}
		catch(_com_error&)
		{
			RegisterIntentionToSwitchServers();
		}
	}

	return eStatus;
}

void FaultTolerantObject::AbandonAllJobs()
{
	HRESULT hr = g_thread_manager.KillAllThreads();
	if( FAILED(hr) )
	{
		std::string stErr = "[Msg 412] An error occurred while abandoning jobs. The error was: " + g_thread_manager.GetLastError();
		ReportError(g_log_type, stErr);
	}
}

  
                                                                                      
                                                                                           
                                                                                        
                                         
  
void FaultTolerantObject::RegisterIntentionToSwitchServers()
{
	switch(m_enCurrentServer)
	{
	case csPRIMARY:
		                                                                                                
		try
		{
			if( !(bool)m_SecondaryMonitor )
				DoLoadSecondaryMonitor(m_stSecondaryMachineName);
			m_SecondaryMonitor->SetEngineIntention(g_machine_name.c_str(), "Secondary");
		}
		catch(_com_error& e)
		{
			if( m_ftState == ftNORMAL )                                                    
			{
				std::string stErr = "[Msg 413] An error occurred while registering intention to switch "
					"to secondary database server. The error was: ";
				if( e.Description().length() )
					stErr += (LPCTSTR)e.Description();
				char buf[64]; sprintf(buf,"\n(0x%x)",e.Error());
				stErr += buf;
				ReportError(g_log_type, stErr);
			}
			m_ftState = ftDISCONNECTED;
			if( IsNetworkError(e.Error()) )
				DoLoadSecondaryMonitor(m_stSecondaryMachineName);
			return;
		}
		m_stIntention = "Secondary";
		break;
	case csSECONDARY:
		                                                                                                
		try
		{
			if( !(bool)m_PrimaryMonitor )
				DoLoadPrimaryMonitor(m_stPrimaryMachineName);
			m_PrimaryMonitor->SetEngineIntention(g_machine_name.c_str(), "Primary");
		}
		catch(_com_error& e)
		{
			if( m_ftState == ftNORMAL )                                                    
			{
				std::string stErr = "[Msg 414] An error occurred while registering intention to switch "
					"to secondary database server. The error was: ";
				if( e.Description().length() )
					stErr += (LPCTSTR)e.Description();
				char buf[64]; sprintf(buf,"\n(0x%x)",e.Error());
				stErr += buf;
				ReportError(g_log_type, stErr);
			}
			m_ftState = ftDISCONNECTED;
			if( IsNetworkError(e.Error()) )
				DoLoadPrimaryMonitor(m_stPrimaryMachineName);
			return;
		}
		m_stIntention = "Primary";
		break;
	}

	m_ftState = ftSWITCHED_INTENTION;
}

void FaultTolerantObject::SwitchServers()
{
	switch(m_enCurrentServer)
	{
	case csPRIMARY:
		if( g_engine->SwitchDatabases("Secondary") )
		{
			m_enCurrentServer = csSECONDARY;
			SetOptionAsString( "FaultTolerance", "LastServerForEngine", "Secondary");
			m_ftState = ftNORMAL;
		}
		else
			m_ftState = ftDISCONNECTED;
		break;
	case csSECONDARY:
		if( g_engine->SwitchDatabases("Primary") )
		{
			m_enCurrentServer = csPRIMARY;
			SetOptionAsString( "Settings/FaultTolerance", "LastServerForEngine", "Primary");
			m_ftState = ftNORMAL;
		}
		else
			m_ftState = ftDISCONNECTED;
		break;
	}
	SetOptionAsString( "Settings/FaultTolerance", "EngineState", "Normal");
}

bool FaultTolerantObject::IsPrimaryTheCurrentServer() const
{
	try
	{
		return m_PrimaryMonitor->CurrentServer == _bstr_t("Primary");
	}
	catch(_com_error&)
	{
		return false;
	}
}

bool FaultTolerantObject::IsSecondaryTheCurrentServer() const
{
	try
	{
		return m_SecondaryMonitor->CurrentServer == _bstr_t("Secondary");
	}
	catch(_com_error&)
	{
		return false;
	}
}

#endif              

