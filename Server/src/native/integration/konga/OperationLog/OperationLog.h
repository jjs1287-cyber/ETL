/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef KONGA_LOG_OPERATIONS_LOG_H
#define KONGA_LOG_OPERATIONS_LOG_H

#include <string>
#include <vector>

#include <konga/Utils/DateUtils.h>

#include "OperationLogStatus.h"

namespace konga {

	class DbConnection;

	class OperationLogAux_Old {
	public:
		OperationLogAux_Old(const std::string& stFieldName, const std::string& stLongMessage)
			: m_stFieldName(stFieldName), m_stLongMessage(stLongMessage) {}

		~OperationLogAux_Old(void) {
		}

		std::string getFieldName() const {
			return m_stFieldName;
		}

		std::string getLongMessage() const {
			return m_stLongMessage;
		}

	private:
		std::string m_stFieldName, m_stLongMessage;
	};

	class OperationLogTabEntry {

	public:
		enum Severity {
			Severity_OK = 1,
			Severity_Alert = 2,
			Severity_Error = 3
		};

	public:
		OperationLogTabEntry(long lOperationId, const std::string& stLogSource, const std::string& stShortMessage,
			Severity severity = Severity_OK, long lErrorCode = 0, const CrDateTime& timeStamp = CrDateTime::GetCurrentUtcTime())
		: m_lOperationId(lOperationId), m_TimeStamp(timeStamp), m_Severity(severity),
			m_lErrorCode(lErrorCode), m_stLogSource(stLogSource), m_stShortMessage(stShortMessage) {}
		~OperationLogTabEntry(void) { }

		long getOperationId() const {
			return m_lOperationId;
		}

		CrDateTime getTimeStamp() const {
			return m_TimeStamp;
		}

		Severity getSeverity() const {
			return m_Severity;
		}

		void setSeverity(Severity level) {
			m_Severity = level;
		}

		long getErrorCode() const {
			return m_lErrorCode;
		}

		std::string getLogSource() const {
			return m_stLogSource;
		}

		std::string getShortMessage() const {
			return m_stShortMessage;
		}


		std::string toString() const;

	private:
		long m_lOperationId;
		CrDateTime m_TimeStamp;
		Severity m_Severity;
		long m_lErrorCode;
		std::string m_stLogSource, m_stShortMessage;

	};

	                                      
	class OperationLog_Old {
	public:
		explicit OperationLog_Old(DbConnection& TranDbConnection) : m_dbConnection(TranDbConnection) { }
		~OperationLog_Old(void) { }

		                                        
		                               
		void Log(const OperationLogTabEntry& entry) const;

		                                                        
		void DeleteOldLogs(const CrDateTime& deleteBeforeDate);

		                                       
		void ArchiveOldLogs(const CrDateTime& deleteBeforeDate, const std::string& severity);
		
		                                                                                      
		void DeleteLogsForOperation(long lOperationId, const CrDateTime& deleteBeforeDate);

		                                             
		void DeleteLogsForOperation(long lOperationId);

	private:
		DbConnection& m_dbConnection;
	};

	OperationLogStatus most_severe(OperationLogStatus s1, OperationLogStatus s2);

	class OperationLog {
	public:
		explicit OperationLog(DbConnection& TranDbConnection) : m_dbConnection(TranDbConnection) { }
		~OperationLog(void) { }

		void CreatePendingEntry(long lOperationId, long long llOperationInstanceId);

		void CreateRunningEntry(long lOperationId, long long llOperationInstanceId);

		void UpdateLogStatus(long long lOperationInstanceId, OperationLogStatus newStatus);

		void LogMessageUpdateStatus(long long lOperationInstanceId, OperationLogStatus newStatus, const std::string& stFieldName, const std::string& stMessage);

		void LogMessage(long long lOperationInstanceId, const std::string& stFieldName, const std::string& stMessage);

		void OperationMsgCancelled(long long msgId);

		void OperationCancelled(long long operationInstanceId);

		                                             
		void DeleteLogsForOperation(long lOperationId);

		void ArchiveOldLogs(CrDateTime deleteBeforeDate, const std::string) {
			   
		}

	private:
		static void GetISO8601UtcNow(std::string& utcNow, long long& tm);

	private:
		DbConnection& m_dbConnection;
	};

}
#endif                              

