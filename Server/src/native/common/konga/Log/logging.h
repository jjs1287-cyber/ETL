/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#ifndef KONGA_LOGGING_H
#define KONGA_LOGGING_H

#include <string>

#ifdef WIN32
	#include <windows.h>	                      
#else
	#define EVENTLOG_SUCCESS                0x0000
	#define EVENTLOG_ERROR_TYPE             0x0001
	#define EVENTLOG_WARNING_TYPE           0x0002
	#define EVENTLOG_INFORMATION_TYPE       0x0004
	#define EVENTLOG_AUDIT_SUCCESS          0x0008
	#define EVENTLOG_AUDIT_FAILURE          0x0010
#endif
const int EVENTLOG_DEBUG = 32;                                                    

enum report_error { report_event_log = 1, report_file = 2, report_messagebox = 4,
					report_trace = 8, report_debug = 16, report_database = 32,
					report_messagebox_ui = 4096 };

const int report_no_trace = report_file;

#ifdef _DEBUG
	const int report_default = report_trace | report_file;
#else
	const int report_default = report_no_trace;
#endif

struct LogParams
{
	LogParams(const std::string& stContextId, int iMsgId = 0) : 
		m_iMsgId(iMsgId), m_stContextId(stContextId) {}
	LogParams(int iMsgId = 0, const std::string& stContextId = "") : 
		m_iMsgId(iMsgId), m_stContextId(stContextId) {}

	int m_iMsgId;
	std::string m_stContextId;
};

  
                                                             
  
                                     
void ReportError(const std::string& str, const std::string& stEventSource,
				 int type=EVENTLOG_ERROR_TYPE, int how = report_default, int iLogTypeOverride=-1);

                                                    
void ReportError(int how, const std::string& str, int errorType = EVENTLOG_ERROR_TYPE,
				 int iLogTypeOverride = -1);

                                                             
void ReportError(int how, const std::string& str, const LogParams& logParams, const std::string& stEventSource = "",
				 int type=EVENTLOG_ERROR_TYPE, int iLogTypeOverride=-1);

                                                                                    
                                                                 
                                                       
                                                    
void LogMessageToFile(const std::string& msg, int type=EVENTLOG_INFORMATION_TYPE);

namespace konga {

	                                                                                                     
	enum LogSeverity {
		KSEVERITY_NONE = 0,
		KSEVERITY_ERROR = 1,
		KSEVERITY_WARNING = 2,
		KSEVERITY_INFORMATION = 4,
		KSEVERITY_DEBUG = 8
	};

	const int default_log_severity = KSEVERITY_ERROR | KSEVERITY_WARNING | KSEVERITY_INFORMATION;

	                                                                        
	LogSeverity GetLogSeverity(int type);

	                                                                               
	class Logger {
	public:
		Logger(const std::string& sEventSource,
			int severity_bits = default_log_severity,
			int defaultLogType = EVENTLOG_ERROR_TYPE, int how = report_default)
			: m_severity_bits(severity_bits), m_iDefaultLogType(defaultLogType),
			m_how(how), m_stEventSource(sEventSource) { }
		~Logger(void) { }

		                                                       
		void Log(const std::string& sMessage) const {
			Log(sMessage, m_iDefaultLogType);
		}

		                                                            
		void LogError(const std::string& sMessage) const {
			Log(sMessage, EVENTLOG_ERROR_TYPE);
		}

		void LogWarning(const std::string& sMessage) const {
			Log(sMessage, EVENTLOG_WARNING_TYPE);
		}

		void Debug(const std::string& sDebugMessage) const {
			Log(sDebugMessage, EVENTLOG_DEBUG);
		}

		void AddSeverityBit(LogSeverity severity_bit) {
			m_severity_bits |= severity_bit;
		}
		    

		                                
		void Log(const std::string& sMessage, int type) const {
			if ( m_severity_bits & GetLogSeverity(type) ) {
				ReportError(sMessage, m_stEventSource, type, m_how);
			}
		}

		static std::string GetLogFileName(const std::string& sEventSource);

	private:
		int m_severity_bits;
		int m_iDefaultLogType;
		int m_how;
		std::string m_stEventSource;
	};
}

#endif                   
