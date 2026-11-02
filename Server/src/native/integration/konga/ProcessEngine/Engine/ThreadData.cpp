/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

                                                          
  
                                                                      

#include <boost/thread/mutex.hpp>

#include <konga/Log/logging.h>					         

#include <konga/Utils/DBConnectionInfo.h>	                   

#include "engine_util.h"					                   
#include "ThreadData.h"

extern boost::recursive_mutex g_db_lock;                             
extern DBConnectionInfo g_mdb_connect;
extern DBConnectionInfo g_idb_connect;
extern DBConnectionInfo g_pdb_connect;
extern int g_log_type;	                      

bool ThreadData::Connect(bool bReconnect              )
{
	boost::recursive_mutex::scoped_lock lock( g_db_lock );
	std::string which_db = g_pdb_connect.VerbosePrettyConnectInfoString();
	try
	{
		if( bReconnect || !m_pdb->IsOpen() )
			m_pdb->Open(g_pdb_connect.connectionString());

		which_db = g_mdb_connect.VerbosePrettyConnectInfoString();
		if( bReconnect || !m_mdb->IsOpen() )
			m_mdb->Open(g_mdb_connect.connectionString());

		which_db = g_idb_connect.VerbosePrettyConnectInfoString();
		if( bReconnect || !m_idb->IsOpen() )
			m_idb->Open(g_idb_connect.connectionString());

		int iTimeOut = GetODBCTimeOut();
		m_pdb->SetQueryTimeout(iTimeOut);
		m_idb->SetQueryTimeout(iTimeOut);
		m_mdb->SetQueryTimeout(iTimeOut);
	}
	catch(konga::DbException& e) 
	{
		std::string stErr = "[Msg 184] Database Error: ThreadData, " + which_db + ", Cannot process message -- " +
			e.GetErrorString() + "  [" + e.GetStateNativeOrigin() + "]";
		ReportError( g_log_type, stErr);
		return false;
	}
	return true;
}
