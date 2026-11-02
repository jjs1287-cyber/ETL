/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#ifdef notyetWIN32
	#include <comutil.h>
#endif

#include <string>
#include <iostream>
#include <fstream>
#include <exception>

#include <string.h>
#include <time.h>
#include <assert.h>

#include <boost/thread/mutex.hpp>

#include <konga/Utils/AutoCleanup.h>	                            
#include <konga/Utils/FileUtils.h>		                        
#include <konga/Utils/StringUtils.h>	                            
#include <konga/Utils/CurrentTime.h>	                     
#include <konga/Utils/Settings.h>		                           
#include <konga/Utils/PortDefs.h>
#include <konga/Utils/CrException.h>

#ifdef notyetWIN32
#include "TAppLog.h"			                
#endif

#include "logging.h"			            

                                            
                                                               
                              
                                              
                        
        

using namespace std;
using namespace konga;

string g_stEventSource;

namespace {
	boost::mutex g_log_lock;
}

#ifdef WIN32
                    
  
  
                                                                        
  
void AddToMessageLog(LPCTSTR lpszMsg, int type)
{
	TCHAR   szMsg[256];
	HANDLE  hEventSource;
	LPCTSTR  lpszStrings[2];

	int dwErr = GetLastError();

	  
	                                      
	  
	hEventSource = RegisterEventSource(NULL, "ETLForge");

	sprintf(szMsg, TEXT("%s : %d"), "ETLForge", dwErr);
	lpszStrings[0] = szMsg;
	lpszStrings[1] = lpszMsg;

	if (hEventSource != NULL) {
		ReportEvent(hEventSource,	                         
			type,					             
			0,						                 
			0,						           
			NULL,					                     
			2,						                         
			0,						                       
			(LPCTSTR*)lpszStrings,	                         
			NULL);					              

		(VOID) DeregisterEventSource(hEventSource);
	}
}

#endif

void ReportError(const string& str, const string& stEventSource,
				 int type, int how, int iLogTypeOverride) {

	const int iBUFSIZE = 40;
	char buf[iBUFSIZE];
	memset(buf, 0, iBUFSIZE);
	strncpy(buf, str.c_str(), iBUFSIZE-1);
	int iLogMsgId = 0;
	char buf2[iBUFSIZE];
	char c = 0;
	if( sscanf(buf, "%s%d%c", buf2, &iLogMsgId, &c) != 3 || c != ']' )
		iLogMsgId = 0;
	ReportError(how, str, LogParams(iLogMsgId), stEventSource, type, iLogTypeOverride);
}


void ReportError(int how, const string& str, int type                         , int iLogTypeOverride       )
{
	string stEventSource;
	{                                         
	boost::mutex::scoped_lock lock( g_log_lock );
	stEventSource = g_stEventSource;
	}
	ReportError(str, stEventSource, type, how, iLogTypeOverride);
}

                    
namespace {
	string GetTypeString(int type) {
		switch(type) {
			case EVENTLOG_SUCCESS:
				return "Success";
			case EVENTLOG_ERROR_TYPE:
				return "Error";
			case EVENTLOG_WARNING_TYPE:
				return "Warning";
			case EVENTLOG_INFORMATION_TYPE:
				return "Informative";
			case EVENTLOG_AUDIT_SUCCESS:
				return "Audit Success";
			case EVENTLOG_AUDIT_FAILURE:
				return "Audit Failure";
			case EVENTLOG_DEBUG:
				return "Debug";
			default:
				return "Unknown LogType";
		}
	}
}

namespace {
	                                                   
	                                     
	string GenerateLogFileName(string& sLogError, const string& stEventSource) {
		const string sLogDir( GetLogDirectoryWithSlash(sLogError) );

		if ( stEventSource.empty() ) {
			                                                                                           
			return string( sLogDir + "ETLForge.log" );
		}
		else {
			return string( sLogDir + stEventSource + ".log" );
		}
	}
}

void ReportError(int how, const string& msg, const LogParams& logParams,
				 const string& stEventSource, int type                         , int iLogTypeOverride       )
{

	#ifdef notyetWIN32
	if( type == EVENTLOG_ERROR_TYPE )
		CreateErrorInfo(str, __uuidof(IUnknown), konga::getModuleFileName());

	if( how & report_event_log ) 
	{
		if( !g_applog )
		{
			AutoCriticalSection lock( g_log_lock );
			g_applog = new TAppLog(stEventSource.empty()?"ETLForge Generic":stEventSource.c_str(), LOG_INFORM);
		}

		int eventLogType = type;
		if( eventLogType & EVENTLOG_DEBUG ) {
			eventLogType &= ~EVENTLOG_DEBUG;                                               
		}

		LOG_LEVEL logtype;
		switch(eventLogType)
		{
		case EVENTLOG_SUCCESS: logtype = LOG_INFORM; break;
		case EVENTLOG_ERROR_TYPE: logtype = LOG_ERROR; break;
		case EVENTLOG_WARNING_TYPE: logtype = LOG_WARNING; break;
		case EVENTLOG_INFORMATION_TYPE: logtype = LOG_INFORM; break;
		case EVENTLOG_AUDIT_SUCCESS: logtype = LOG_AUDIT_SUCCESS; break;
		default:  logtype = LOG_NONE;
		}

		string strRep;
		::ReplaceCharWithString(str.substr(0,8192), strRep, '%', "%%");
		if( strRep.length() > (1024*32-1) )
			strRep[1024*32-1] = 0;
		if( logtype == LOG_NONE )
			g_applog->LogEventWithId(logtype, strRep.c_str(), eventLogType, iLogTypeOverride);
		else
			g_applog->LogEvent(logtype, strRep.c_str());
	}

	#endif         
	
	if( how & report_trace )
	{
		                                                                         
		              
		boost::mutex::scoped_lock lock( g_log_lock );
		cout << "[" << stEventSource << ", " << GetTypeString(type) << "] " << msg << endl;
	}
	if( how & report_file )
	{
		string sLogError;                                                                        
		const string& sFileName = GenerateLogFileName(sLogError, stEventSource);

		try {
			boost::mutex::scoped_lock lock( g_log_lock );
			ofstream outfile(sFileName.c_str(), ios_base::app | ios_base::out);
			if ( outfile ) {
				outfile << CCurrentTime().Now() << 
					" [" << stEventSource << ", " << GetTypeString(type) << "] " << msg << sLogError << endl;
			}
			outfile.close();
		}
		catch( const exception& ex ) {
			cerr << "Failed to log the following message:" << endl << msg << endl << "Reason: " << ex.what() << endl;
		}
		catch( ... ) {
			cerr << "Failed to log the following message:" << endl << msg << endl << "Uncaught exception" << endl;
		}

		if ( !sLogError.empty() )
			cerr << sLogError << endl;
	}

	#ifdef WIN32
	if( how & report_messagebox )
	{
	
		#if defined(_DEBUG)
		::MessageBox(0, msg.c_str(), stEventSource.c_str(), MB_OK | MB_SETFOREGROUND);
		#endif          
	}

	if( how & report_messagebox_ui )
	{
		::MessageBox(0, msg.c_str(), stEventSource.c_str(), MB_OK | MB_SETFOREGROUND);
	}
	#endif         

                               
                              
    
       
     
                                                                                           
     
              
     
                                                                                                                   
                                                                                                          
                                                                                          
                                                               
     
    
                          
}

#ifdef WIN32
                                                                                    
                                                                 
                                                       
                                                    
void LogMessageToFile(const string& msg, int type                              )
{
	if (msg.empty())
		return;
#ifdef CR_ROSETTANET_MSGHANDLER
	static int iLog = 0, iCount=0;
	if (!iCount) {
		iLog = GetRegistryInt(HKEY_LOCAL_MACHINE, "konga/Settings", "LogRNMessages", 0);
		iCount = 1;
	}
	if (!iLog)                    
		return;

	string stLogFile = GetRegistryString(HKEY_LOCAL_MACHINE, "konga/Settings", "RosettanetLogFile", "");
	if (!stLogFile.empty()) g_log_filename_ = stLogFile.c_str();

	                                                                           
	                                      
	::SetLastError(0);
#endif
	string stEventSource;
	{                                         
	boost::mutex::scoped_lock lock( g_log_lock );
	stEventSource = g_stEventSource;
	}
	ReportError(report_file, msg, LogParams(), stEventSource, type);
}
#endif         

string Logger::GetLogFileName(const string& sEventSource) {
	string sLogError;
	return GenerateLogFileName(sLogError, sEventSource);
}

konga::LogSeverity konga::GetLogSeverity(int type) {
	switch(type) {
		case EVENTLOG_DEBUG:
			return KSEVERITY_DEBUG;
		case EVENTLOG_INFORMATION_TYPE:
			return KSEVERITY_INFORMATION;
		case EVENTLOG_WARNING_TYPE:
			return KSEVERITY_WARNING;
		case EVENTLOG_ERROR_TYPE:
			return KSEVERITY_ERROR;
		default:
			return KSEVERITY_INFORMATION;
	};
}

