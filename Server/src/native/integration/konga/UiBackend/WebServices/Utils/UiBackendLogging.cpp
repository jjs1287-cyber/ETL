/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */

#include <time.h>

#include <fstream>		           
#include <iostream>		       

#include <konga/Utils/Settings.h>
#include <konga/Utils/CrException.h>

#include <konga/Log/logging.h>

#include <boost/thread/mutex.hpp>

#include "UiBackendLogging.h"

using namespace konga;
using namespace std;

                    
namespace {
	boost::mutex g_DebugFileMutex;
	string g_sDebugFile;
	bool g_bDebugDisabled = false;

#ifdef WIN32
	const string DEFAULT_DEBUG_FILE_PATH("/etlforge_ws_log");
#else
	const string DEFAULT_DEBUG_FILE_PATH("/tmp/etlforge_ws_log");
#endif
}

                              
bool konga::isDebugLogEnabled_() {
#ifdef _DEBUG
	return true;
#endif
	return !g_bDebugDisabled;
}

void konga::debugLog_(const string& stMsg) {
#ifdef _DEBUG
	                                     
	std::cout << stMsg << std::endl;
#endif
	try {
		                                        
		boost::mutex::scoped_lock debug_file_lock(g_DebugFileMutex);
		if ( g_bDebugDisabled ) return;

		if ( g_sDebugFile.empty() ) {
			g_sDebugFile = konga::GetOptionAsString("Debug", "WebServiceDebugFile", DEFAULT_DEBUG_FILE_PATH);
			if ( g_sDebugFile.empty() ) {
				                                                                          
				g_bDebugDisabled = true;
				return;
			}
		}

		time_t t;
		time(&t);
		const string sTime( ctime(&t) );
		ofstream file(g_sDebugFile.c_str(), ios_base::app|ios_base::out);
		file << sTime.substr(0,sTime.length()-1) << " -- " << stMsg << endl;
		file.flush();
		file.close();
	}
	catch( const CrException& e ) {
		cerr << e.GetErrorString() << endl;
	}
	catch( const std::exception& e ) {
		cerr << e.what() << endl;
	}
#ifndef _DEBUG
	catch(...) {
		                                                     
		cerr << "unexpected exception thrown in debugLog" << endl;
	}
#endif
}

namespace {
#ifdef _DEBUG
	const int audit_howto_log = report_default;
#else
	const int audit_howto_log = report_file | report_event_log;
#endif

	Logger g_AuditLogger("Audit", default_log_severity, EVENTLOG_INFORMATION_TYPE, audit_howto_log);
}

void konga::auditLog(const string& stMsg) {
	try {
		g_AuditLogger.Log(stMsg);
	}
	catch( const CrException& e ) {
		cerr << e.GetErrorString() << endl;
	}
	catch( const std::exception& e ) {
		cerr << e.what() << endl;
	}
#ifndef _DEBUG
	catch(...) {
		                                                                             
		cerr << "unexpected exception thrown in auditLog" << endl;
	}
#endif
}
