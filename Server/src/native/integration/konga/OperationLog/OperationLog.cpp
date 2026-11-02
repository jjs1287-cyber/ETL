/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <konga/Utils/CrException.h>
#include <konga/Utils/DateUtils.h>
#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>
#include <konga/Db/DbUtils/DbException.h>
#include <konga/Db/DbUtils/DynamicDbRecordSet.h>

#include <konga/Db/DbUtils/AutoDbCleanup.h>

#include "OperationLogStatus.h"
#include "OperationLog.h"

using namespace konga;
using std::string;
using std::vector;


namespace {
	const string::size_type LOG_SHORT_MESSAGE_MAX_SIZE = 255;

	const string::size_type AUX_FIELD_NAME_MAX_SIZE = 250;
	const string::size_type AUX_LONG_MESSAGE_MAX_SIZE = 7000;
	const string OVERSIZE_MSG("...");

	string truncLargeMsg(const string& msg, const string::size_type max_size) {
		if ( msg.size() > max_size ) {
			return msg.substr(0, max_size - OVERSIZE_MSG.size()) + OVERSIZE_MSG;
		}
		else {
			return msg;
		}
	}
}

OperationLogStatus konga::most_severe(OperationLogStatus s1, OperationLogStatus s2) {
	return ( s1 > s2 ) ? s1 : s2;
}

         
void OperationLog::GetISO8601UtcNow(string& utcNow, long long& tm) {
	const konga::CrDateTime& dt = CrDateTime::GetCurrentUtcTime();
	utcNow = dt.toString("yyyy-mm-dd HH:MM:SS");
	tm = dt.GetTime();
}

void OperationLog::CreatePendingEntry(long lOperationId, long long llOperationInstanceId) {
	string utcNow;
	long long tm;
	GetISO8601UtcNow(utcNow, tm);
	utcNow.insert(0, ",'").append("'," + konga::toString(tm));
	string sql("INSERT INTO OperationLogTab (operation_instance_id, operation_id, status, entered_ts, entered_tm, status_ts, status_tm) VALUES (");
	sql.append(toString(llOperationInstanceId)).append(1,',').append(toString(lOperationId)).append(1,',')
		.append(toString(OperationLogStatus_Pending))
		.append(utcNow).append(utcNow)
		.append(1,')');
	m_dbConnection.ExecuteSql(sql);
}

void OperationLog::CreateRunningEntry(long lOperationId, long long llOperationInstanceId) {
	string utcNow;
	long long tm;
	GetISO8601UtcNow(utcNow, tm);
	utcNow.insert(0, ",'").append("'," + konga::toString(tm));
	string sql("INSERT INTO OperationLogTab (operation_instance_id, operation_id, status, entered_ts, entered_tm, started_ts, started_tm, status_ts, status_tm) VALUES (");
	sql.append(toString(llOperationInstanceId)).append(1,',').append(toString(lOperationId)).append(1, ',')
		.append(toString(OperationLogStatus_Running))
		.append(utcNow).append(utcNow).append(utcNow)
		.append(1, ')');
	m_dbConnection.ExecuteSql(sql);
}

void OperationLog::UpdateLogStatus(long long llOperationInstanceId, OperationLogStatus newStatus) {
	string utcNow;
	long long tm;
	GetISO8601UtcNow(utcNow, tm);
	string sql( "UPDATE OperationLogTab SET status_ts = '" + utcNow + "', status_tm = " + toString(tm) + " ,Status = " + toString(newStatus) );
	if ( newStatus == OperationLogStatus_Running ) {
		sql.append(", started_ts = '" + utcNow + "', started_tm = " + toString(tm));
	}
	sql.append(" WHERE operation_instance_id = " + toString(llOperationInstanceId));
	m_dbConnection.ExecuteSql(sql);
}

void OperationLog::LogMessageUpdateStatus(long long llOperationInstanceId, OperationLogStatus newStatus, const string& stFieldName, const string& stMessage) {
	UpdateLogStatus(llOperationInstanceId, newStatus);
	LogMessage(llOperationInstanceId, stFieldName, stMessage);
}

void OperationLog::LogMessage(long long lOperationInstanceId, const std::string& stFieldName, const std::string& stMessage) {
	const string sql("INSERT INTO OperationMessageTab (operation_instance_id, field_name, message) VALUES ("
		+ toString(lOperationInstanceId) + ",'" + konga::SqlEscape(stFieldName) + "','" + konga::SqlEscape(stMessage) + "')");
	m_dbConnection.ExecuteSql(sql);
}

void OperationLog::OperationMsgCancelled(long long msgId) {
	const string oidSelectSql("SELECT operation_instance_id FROM OperationInstanceTab WHERE msgid = " + toString(msgId));
	SmartDbPtr<DynamicDbRecordSet> rs( DynamicDbRecordSet::MakeInstance(&m_dbConnection, oidSelectSql) );
	rs->Open();
	if ( !rs->Next() ) return;
	const string& operationInstanceId = rs->GetFieldValue(0);
	rs.CloseAndDelete();

	OperationCancelled(toLongLong(operationInstanceId));
}

void OperationLog::OperationCancelled(long long operationInstanceId) {
	string sql("SELECT status from operationlogtab WHERE operation_instance_id = " + toString(operationInstanceId));
	SmartDbPtr<DynamicDbRecordSet> rs( DynamicDbRecordSet::MakeInstance(&m_dbConnection, sql) );
	rs->Open();
	if ( !rs->Next() ) return;

	const OperationLogStatus status = static_cast<OperationLogStatus>(atoi(rs->GetFieldValue(0).c_str()));
	if ( !IsCompletedStatus(status) ) {
		sql = "INSERT INTO OperationMessageTab (operation_instance_id, field_name, message) VALUES (" + 
			toString(operationInstanceId) + ",'', 'Operation Cancelled')";
		m_dbConnection.ExecuteSql(sql);
		UpdateLogStatus(operationInstanceId, OperationLogStatus_Cancelled);
	}
}

void OperationLog::DeleteLogsForOperation(long lOperationId) {
	const string stOperationId(konga::toString(lOperationId));
	string sql("DELETE FROM OperationMessageTab WHERE operation_instance_id IN(SELECT operation_instance_id FROM OperationLogTab WHERE operation_id = " + stOperationId + ")");
	m_dbConnection.ExecuteSql(sql);

	sql = "DELETE FROM OperationLogTab WHERE operation_id = " + stOperationId;
	m_dbConnection.ExecuteSql(sql);
}
