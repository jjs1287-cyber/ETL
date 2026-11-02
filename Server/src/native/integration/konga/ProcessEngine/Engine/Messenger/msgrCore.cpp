/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

                                         

#include <string>

#include <boost/algorithm/string/trim.hpp>	               

#include <boost/scoped_array.hpp>

#include <konga/Mime/mhtml.h>			                          
#include <konga/Utils/util.h>			                 
#include <konga/Utils/FileUtils.h>		                             
#include <konga/Utils/AutoFileCleanup.h>	                    
#include <konga/Utils/CurrentTime.h>	                     
#include <konga/Utils/PortDefs.h>		                 
#include <konga/Utils/Settings.h>		                                                
#include <konga/Utils/Timer.h>			              

#include <konga/Db/DbUtils/DbUtils.h>			                                 

                                 
#include <konga/Db/MsgDb/POPAccountsTab.h>		                            
#include <konga/Db/MsgDb/IncomingMsgTab.h>		                            
#include <konga/Db/MsgDb/OutgoingMsgTab.h>		                            
#include <konga/Db/MsgDb/AttachmentTab.h>		                           
#include <konga/Db/MsgDb/SettingsTab.h>			                         
#include <konga/Db/MsgDb/TransientErrorMsgTab.h>	                                  
#include <konga/Db/MsgDb/PermanentErrorMsgTab.h>	                                  

#include <konga/Server/Server.h>				              

#include "msgrCore.h"		                  

#include "../EngineCore.h"	                   
#include "../engine_util.h"	                          
#include "../KeyValues.h"	                       

void parse_smtp(const char*filename,char*to,char*from,char*subject,char*process_vars);                 

const unsigned int Messenger::INITIAL_INCOMING_TIME_INTERVAL(10000);              
const unsigned int Messenger::INITIAL_OUTGOING_TIME_INTERVAL(10000);              

extern EngineCore *g_engine;
extern Messenger *g_msgr;

extern int g_log_type;
extern std::string g_machine_name;	                            
extern boost::recursive_mutex g_db_lock;	                            
extern DBConnectionInfo g_mdb_connect;                             
extern DBConnectionInfo g_pdb_connect;                             

namespace {
	                                                                       
	const std::string stMessengerConfigSection("ProcessEngine/Messenger");
}

bool RunTimerJobs()
{
	if( g_exiting || !g_msgr || !g_engine ||
		(g_engine->GetErrorStatus() & esWAITING) ||
		!g_msgr->IsRunning() ) return false;
	return true;
}

void Messenger::MessengerTimerFunction::operator()() {
	while(RunTimerJobs()) {
		const boost::function0<void>& f = pMessenger->m_pTimer->WaitForTimerEvent();
		f();
	}
}

static void gCheckIncoming()
{
	if( !RunTimerJobs() ) return;
	                                                                                        
                           
}
  
                            
 
                              
                                                                                         
                           
 

                                  
 
                              
                                                                                               
                                 
 
  
Messenger::Messenger() : 
	outgoing_time_interval(INITIAL_OUTGOING_TIME_INTERVAL),
	transienterror_time_interval(INITIAL_TRANSIENTERROR_TIME_INTERVAL),
	m_incoming_timer_id(0), m_outgoing_timer_id(0), m_transienterror_timer_id(0),
	m_is_running(false), m_bEmailDeleteIfPermFailure(false),
                       
	m_mdb(0), m_pdb(0),
                      
	m_pThread(0),
	m_pTimer(0)
{
	int iDelete = konga::GetOptionAsInt(stMessengerConfigSection,"EmailDeleteIfPermFailure", 0);
	if (iDelete > 0)
		m_bEmailDeleteIfPermFailure = true;
	else
		m_bEmailDeleteIfPermFailure = false;

	#ifdef DS_USE_COM
	HRESULT hr = CoInitializeEx(0, COINIT_MULTITHREADED);
	if( FAILED(hr) )
	{
	}
	#endif              

	INITIAL_TRANSIENTERROR_TIME_INTERVAL = konga::GetOptionAsInt("Settings/ErrorHandling","EmailResendInterval",600) * 1000;
	if( INITIAL_TRANSIENTERROR_TIME_INTERVAL < 2 ) INITIAL_TRANSIENTERROR_TIME_INTERVAL = 2;
	transienterror_time_interval = INITIAL_TRANSIENTERROR_TIME_INTERVAL;
}

Messenger::~Messenger()
{
	if( m_pTimer ) {
		m_pTimer->ShouldExit(true);
		delete m_pTimer;
		m_pTimer = 0;                                        
	}

	if( IsRunning() ) { DisconnectDataSources(); Stop(); }

	g_msgr = 0;

	  
          
  
                                                     
               
            
  
           
  
                
             
  
  
	DisconnectDataSources();
	if( m_mdb ) { delete m_mdb; m_mdb = 0; }
	if( m_pdb ) { delete m_pdb; m_pdb = 0; }

	#ifdef WIN32
	CoUninitialize();
	#endif
}

bool Messenger::Start()
{
	if( IsRunning() ) return true;

	if( !ConnectDataSources() ) return false;

	ReportError( g_log_type, "[Msg 230] Messenger starting.", EVENTLOG_INFORMATION_TYPE);

	m_pTimer = new konga::Timer();
	m_pTimer->AddTimerFunction(gCheckIncoming, INITIAL_INCOMING_TIME_INTERVAL);
                                                                              
                                                                                          
	m_pTimer->Start();
	m_pThread = new boost::thread(MessengerTimerFunction(this));

	m_is_running = true;
	return true;
}

bool Messenger::Stop()
{
	if( !IsRunning() ) return true;
	m_is_running = false;

	ReportError( g_log_type, "[Msg 231] Messenger exiting.", EVENTLOG_INFORMATION_TYPE);

	if( m_pTimer ) {
		m_pTimer->ShouldExit(true);
		delete m_pTimer;
		m_pTimer = 0;
	}

	delete m_pThread;
	m_pThread = 0;

	return true;
}

void Messenger::Setup()
{
	StartTimers();
}

void Messenger::StartTimers()
{
}


void Messenger::CheckIncoming() 
{
                                     
	if( g_exiting ) return;
	if( !IsRunning() ) return;

	  
	                                                 
	                                               
	  
	                                            
                                            

}

bool Messenger::ConnectDataSources()
{
	boost::recursive_mutex::scoped_lock lock( g_db_lock );
	try
	{
		std::string stMdb_con = g_mdb_connect.connectionString()?g_mdb_connect.connectionString():";DNS=ODBC;DSN=msgdb;";

		if(!m_mdb) m_mdb = konga::GetDbConnection(stMdb_con);
		if(!m_mdb->IsOpen())
			m_mdb->Open(stMdb_con);
	}
	catch(konga::DbException& e )
	{
		std::string stErr = "[Msg 260] Messenger ConnectDataSources. Failed to connect to MsgDB database: " + e.GetErrorString() + 
			" [" + e.GetStateNativeOrigin() + "] " + g_mdb_connect.VerbosePrettyConnectInfoString();
		ReportError( g_log_type, stErr);
	}

	try
	{
		std::string stPdb_con = g_pdb_connect.connectionString()?g_pdb_connect.connectionString():";DNS=ODBC;DSN=pdb;";
		if(!m_pdb) m_pdb = konga::GetDbConnection(stPdb_con);
		if(!m_pdb->IsOpen())
			m_pdb->Open(stPdb_con);
	}
	catch(konga::DbException& e )
	{
		std::string stErr = "[Msg 262] Messenger ConnectDataSources. Failed to connect to Process database: " + e.GetErrorString() + 
			" [" + e.GetStateNativeOrigin() + "] " + g_mdb_connect.VerbosePrettyConnectInfoString();
		ReportError( g_log_type, stErr);
	}
	return true;
}

bool Messenger::DisconnectDataSources()
{
	if( m_mdb && m_mdb->IsOpen() ) m_mdb->Close();
	if( m_pdb && m_pdb->IsOpen() ) m_pdb->Close();
	return true;
}

void Messenger::ReconnectMDB()
{
	try
	{
		if( m_mdb )
		{
			if( m_mdb->IsOpen() ) m_mdb->Close();
			m_mdb->Open( g_mdb_connect.connectionString() );
		}
		else
			m_mdb = konga::GetDbConnection(g_mdb_connect.connectionString());
	}
	catch(konga::DbException& e )
	{
		std::string stErr = "[Msg 263] Failed to reconnect to MDB -- " + e.GetErrorString() + " [" + e.GetStateNativeOrigin()
		+ "] " + g_mdb_connect.VerbosePrettyConnectInfoString();
		ReportError( g_log_type, stErr);
	}
}
