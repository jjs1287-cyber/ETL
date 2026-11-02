/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_CRMSGDB_EVENTMSGTAB_H
#define KONGA_DB_CRMSGDB_EVENTMSGTAB_H

#ifdef WIN32
	#include <windows.h>
#endif
#include <string>
#include <sqltypes.h>

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/DbRecordSet.h>
#include <konga/Db/DbUtils/DbNullException.h>

namespace MsgDb
{

class EventMsgTabRow
{
public:
	EventMsgTabRow() : 
			IsMsgFromNull(true),
			IsMsgFromPrimaryKey(false),
			IsMsgFromDirty(false),
			IsMsgToNull(true),
			IsMsgToPrimaryKey(false),
			IsMsgToDirty(false),
			IsMsgSubjectNull(true),
			IsMsgSubjectPrimaryKey(false),
			IsMsgSubjectDirty(false),
			IsProcessVariablesNull(true),
			IsProcessVariablesPrimaryKey(false),
			IsProcessVariablesDirty(false),
			field_ReceivedAt(0),
			IsReceivedAtNull(true),
			IsReceivedAtPrimaryKey(false),
			IsReceivedAtDirty(false),
			field_ScheduledAt(0),
			IsScheduledAtNull(true),
			IsScheduledAtPrimaryKey(false),
			IsScheduledAtDirty(false),
			field_ProcessId(0),
			IsProcessIdNull(true),
			IsProcessIdPrimaryKey(false),
			IsProcessIdDirty(false),
			field_ProcessInstanceId(0),
			IsProcessInstanceIdNull(true),
			IsProcessInstanceIdPrimaryKey(false),
			IsProcessInstanceIdDirty(false),
			field_TaskId(0),
			IsTaskIdNull(true),
			IsTaskIdPrimaryKey(false),
			IsTaskIdDirty(false),
			field_TaskInstanceId(0),
			IsTaskInstanceIdNull(true),
			IsTaskInstanceIdPrimaryKey(false),
			IsTaskInstanceIdDirty(false),
			field_MsgId(0),
			IsMsgIdNull(true),
			IsMsgIdPrimaryKey(true),
			IsMsgIdDirty(false),
			IsInsertTimeNull(true),
			IsInsertTimePrimaryKey(false),
			IsInsertTimeDirty(false),
			IsModTimeNull(true),
			IsModTimePrimaryKey(false),
			IsModTimeDirty(false)
	{
		field_InsertTime.year = 0;
		field_InsertTime.month = 0;
		field_InsertTime.day = 0;
		field_InsertTime.hour = 0;
		field_InsertTime.minute = 0;
		field_InsertTime.second = 0;
		field_InsertTime.fraction = 0;
		field_ModTime.year = 0;
		field_ModTime.month = 0;
		field_ModTime.day = 0;
		field_ModTime.hour = 0;
		field_ModTime.minute = 0;
		field_ModTime.second = 0;
		field_ModTime.fraction = 0;
	}

	void Copy(const EventMsgTabRow& r)
	{
		field_MsgFrom = r.field_MsgFrom;
		IsMsgFromNull = r.IsMsgFromNull;
		IsMsgFromDirty = r.IsMsgFromDirty;

		field_MsgTo = r.field_MsgTo;
		IsMsgToNull = r.IsMsgToNull;
		IsMsgToDirty = r.IsMsgToDirty;

		field_MsgSubject = r.field_MsgSubject;
		IsMsgSubjectNull = r.IsMsgSubjectNull;
		IsMsgSubjectDirty = r.IsMsgSubjectDirty;

		field_ProcessVariables = r.field_ProcessVariables;
		IsProcessVariablesNull = r.IsProcessVariablesNull;
		IsProcessVariablesDirty = r.IsProcessVariablesDirty;

		field_ReceivedAt = r.field_ReceivedAt;
		IsReceivedAtNull = r.IsReceivedAtNull;
		IsReceivedAtDirty = r.IsReceivedAtDirty;

		field_ScheduledAt = r.field_ScheduledAt;
		IsScheduledAtNull = r.IsScheduledAtNull;
		IsScheduledAtDirty = r.IsScheduledAtDirty;

		field_ProcessId = r.field_ProcessId;
		IsProcessIdNull = r.IsProcessIdNull;
		IsProcessIdDirty = r.IsProcessIdDirty;

		field_ProcessInstanceId = r.field_ProcessInstanceId;
		IsProcessInstanceIdNull = r.IsProcessInstanceIdNull;
		IsProcessInstanceIdDirty = r.IsProcessInstanceIdDirty;

		field_TaskId = r.field_TaskId;
		IsTaskIdNull = r.IsTaskIdNull;
		IsTaskIdDirty = r.IsTaskIdDirty;

		field_TaskInstanceId = r.field_TaskInstanceId;
		IsTaskInstanceIdNull = r.IsTaskInstanceIdNull;
		IsTaskInstanceIdDirty = r.IsTaskInstanceIdDirty;

		field_MsgId = r.field_MsgId;
		IsMsgIdNull = r.IsMsgIdNull;
		IsMsgIdDirty = r.IsMsgIdDirty;

		field_InsertTime = r.field_InsertTime;
		IsInsertTimeNull = r.IsInsertTimeNull;
		IsInsertTimeDirty = r.IsInsertTimeDirty;

		field_ModTime = r.field_ModTime;
		IsModTimeNull = r.IsModTimeNull;
		IsModTimeDirty = r.IsModTimeDirty;

	}

	void CopyPKs(const EventMsgTabRow& r)
	{
		field_MsgId = r.field_MsgId;
		IsMsgIdNull = r.IsMsgIdNull;
		IsMsgIdDirty = r.IsMsgIdDirty;

	}

	void SetDirty(bool b)
	{
		IsMsgFromDirty = b;
		IsMsgToDirty = b;
		IsMsgSubjectDirty = b;
		IsProcessVariablesDirty = b;
		IsReceivedAtDirty = b;
		IsScheduledAtDirty = b;
		IsProcessIdDirty = b;
		IsProcessInstanceIdDirty = b;
		IsTaskIdDirty = b;
		IsTaskInstanceIdDirty = b;
		IsMsgIdDirty = b;
		IsInsertTimeDirty = b;
		IsModTimeDirty = b;
	}

	bool IsDirty() const
	{
		return IsMsgFromDirty ||
			IsMsgToDirty ||
			IsMsgSubjectDirty ||
			IsProcessVariablesDirty ||
			IsReceivedAtDirty ||
			IsScheduledAtDirty ||
			IsProcessIdDirty ||
			IsProcessInstanceIdDirty ||
			IsTaskIdDirty ||
			IsTaskInstanceIdDirty ||
			IsMsgIdDirty ||
			IsInsertTimeDirty ||
			IsModTimeDirty;
	}

	std::string field_MsgFrom;
	bool IsMsgFromNull;
	bool IsMsgFromDirty;

	std::string field_MsgTo;
	bool IsMsgToNull;
	bool IsMsgToDirty;

	std::string field_MsgSubject;
	bool IsMsgSubjectNull;
	bool IsMsgSubjectDirty;

	std::string field_ProcessVariables;
	bool IsProcessVariablesNull;
	bool IsProcessVariablesDirty;

	int field_ReceivedAt;
	bool IsReceivedAtNull;
	bool IsReceivedAtDirty;

	int field_ScheduledAt;
	bool IsScheduledAtNull;
	bool IsScheduledAtDirty;

	int field_ProcessId;
	bool IsProcessIdNull;
	bool IsProcessIdDirty;

	int field_ProcessInstanceId;
	bool IsProcessInstanceIdNull;
	bool IsProcessInstanceIdDirty;

	int field_TaskId;
	bool IsTaskIdNull;
	bool IsTaskIdDirty;

	int field_TaskInstanceId;
	bool IsTaskInstanceIdNull;
	bool IsTaskInstanceIdDirty;

	int field_MsgId;
	bool IsMsgIdNull;
	bool IsMsgIdDirty;

	TIMESTAMP_STRUCT field_InsertTime;
	bool IsInsertTimeNull;
	bool IsInsertTimeDirty;

	TIMESTAMP_STRUCT field_ModTime;
	bool IsModTimeNull;
	bool IsModTimeDirty;


private:
	bool IsMsgFromPrimaryKey;
	bool IsMsgToPrimaryKey;
	bool IsMsgSubjectPrimaryKey;
	bool IsProcessVariablesPrimaryKey;
	bool IsReceivedAtPrimaryKey;
	bool IsScheduledAtPrimaryKey;
	bool IsProcessIdPrimaryKey;
	bool IsProcessInstanceIdPrimaryKey;
	bool IsTaskIdPrimaryKey;
	bool IsTaskInstanceIdPrimaryKey;
	bool IsMsgIdPrimaryKey;
	bool IsInsertTimePrimaryKey;
	bool IsModTimePrimaryKey;

};


class EventMsgTab : public konga::DbRecordSet {
public:
	EventMsgTab() {}

	static EventMsgTab* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "EventMsgTab";
	}

	void DoFieldExchange(const EventMsgTabRow& row) {
		m_row.Copy(row);
	}

	void Edit();

	std::string GetUpdateFilter() {
		return m_stUpdateFilter;
	}

	std::string GetMsgFrom() {
		if( m_row.IsMsgFromNull )
			throw konga::DbNullException("MsgFrom");
		return m_row.field_MsgFrom;
	}
	void SetMsgFrom(const std::string& st) {
		m_row.field_MsgFrom = st;
		m_row.IsMsgFromNull = false;
		m_row.IsMsgFromDirty = true;
	}
	bool IsMsgFromNull() const {
		return m_row.IsMsgFromNull;
	}
	void SetMsgFromToNull() {
		m_row.field_MsgFrom = "";
		m_row.IsMsgFromNull = true;
		m_row.IsMsgFromDirty = true;
	}

	std::string GetMsgTo() {
		if( m_row.IsMsgToNull )
			throw konga::DbNullException("MsgTo");
		return m_row.field_MsgTo;
	}
	void SetMsgTo(const std::string& st) {
		m_row.field_MsgTo = st;
		m_row.IsMsgToNull = false;
		m_row.IsMsgToDirty = true;
	}
	bool IsMsgToNull() const {
		return m_row.IsMsgToNull;
	}
	void SetMsgToToNull() {
		m_row.field_MsgTo = "";
		m_row.IsMsgToNull = true;
		m_row.IsMsgToDirty = true;
	}

	std::string GetMsgSubject() {
		if( m_row.IsMsgSubjectNull )
			throw konga::DbNullException("MsgSubject");
		return m_row.field_MsgSubject;
	}
	void SetMsgSubject(const std::string& st) {
		m_row.field_MsgSubject = st;
		m_row.IsMsgSubjectNull = false;
		m_row.IsMsgSubjectDirty = true;
	}
	bool IsMsgSubjectNull() const {
		return m_row.IsMsgSubjectNull;
	}
	void SetMsgSubjectToNull() {
		m_row.field_MsgSubject = "";
		m_row.IsMsgSubjectNull = true;
		m_row.IsMsgSubjectDirty = true;
	}

	std::string GetProcessVariables() {
		if( m_row.IsProcessVariablesNull )
			throw konga::DbNullException("ProcessVariables");
		return m_row.field_ProcessVariables;
	}
	void SetProcessVariables(const std::string& st) {
		m_row.field_ProcessVariables = st;
		m_row.IsProcessVariablesNull = false;
		m_row.IsProcessVariablesDirty = true;
	}
	bool IsProcessVariablesNull() const {
		return m_row.IsProcessVariablesNull;
	}
	void SetProcessVariablesToNull() {
		m_row.field_ProcessVariables = "";
		m_row.IsProcessVariablesNull = true;
		m_row.IsProcessVariablesDirty = true;
	}

	int GetReceivedAt() {
		if( m_row.IsReceivedAtNull )
			throw konga::DbNullException("ReceivedAt");
		return m_row.field_ReceivedAt;
	}
	void SetReceivedAt(int i) {
		m_row.field_ReceivedAt = i;
		m_row.IsReceivedAtNull = false;
		m_row.IsReceivedAtDirty = true;
	}
	bool IsReceivedAtNull() const {
		return m_row.IsReceivedAtNull;
	}
	void SetReceivedAtToNull() {
		m_row.IsReceivedAtNull = true;
		m_row.IsReceivedAtDirty = true;
	}

	int GetScheduledAt() {
		if( m_row.IsScheduledAtNull )
			throw konga::DbNullException("ScheduledAt");
		return m_row.field_ScheduledAt;
	}
	void SetScheduledAt(int i) {
		m_row.field_ScheduledAt = i;
		m_row.IsScheduledAtNull = false;
		m_row.IsScheduledAtDirty = true;
	}
	bool IsScheduledAtNull() const {
		return m_row.IsScheduledAtNull;
	}
	void SetScheduledAtToNull() {
		m_row.IsScheduledAtNull = true;
		m_row.IsScheduledAtDirty = true;
	}

	int GetProcessId() {
		if( m_row.IsProcessIdNull )
			throw konga::DbNullException("ProcessId");
		return m_row.field_ProcessId;
	}
	void SetProcessId(int i) {
		m_row.field_ProcessId = i;
		m_row.IsProcessIdNull = false;
		m_row.IsProcessIdDirty = true;
	}
	bool IsProcessIdNull() const {
		return m_row.IsProcessIdNull;
	}
	void SetProcessIdToNull() {
		m_row.IsProcessIdNull = true;
		m_row.IsProcessIdDirty = true;
	}

	int GetProcessInstanceId() {
		if( m_row.IsProcessInstanceIdNull )
			throw konga::DbNullException("ProcessInstanceId");
		return m_row.field_ProcessInstanceId;
	}
	void SetProcessInstanceId(int i) {
		m_row.field_ProcessInstanceId = i;
		m_row.IsProcessInstanceIdNull = false;
		m_row.IsProcessInstanceIdDirty = true;
	}
	bool IsProcessInstanceIdNull() const {
		return m_row.IsProcessInstanceIdNull;
	}
	void SetProcessInstanceIdToNull() {
		m_row.IsProcessInstanceIdNull = true;
		m_row.IsProcessInstanceIdDirty = true;
	}

	int GetTaskId() {
		if( m_row.IsTaskIdNull )
			throw konga::DbNullException("TaskId");
		return m_row.field_TaskId;
	}
	void SetTaskId(int i) {
		m_row.field_TaskId = i;
		m_row.IsTaskIdNull = false;
		m_row.IsTaskIdDirty = true;
	}
	bool IsTaskIdNull() const {
		return m_row.IsTaskIdNull;
	}
	void SetTaskIdToNull() {
		m_row.IsTaskIdNull = true;
		m_row.IsTaskIdDirty = true;
	}

	int GetTaskInstanceId() {
		if( m_row.IsTaskInstanceIdNull )
			throw konga::DbNullException("TaskInstanceId");
		return m_row.field_TaskInstanceId;
	}
	void SetTaskInstanceId(int i) {
		m_row.field_TaskInstanceId = i;
		m_row.IsTaskInstanceIdNull = false;
		m_row.IsTaskInstanceIdDirty = true;
	}
	bool IsTaskInstanceIdNull() const {
		return m_row.IsTaskInstanceIdNull;
	}
	void SetTaskInstanceIdToNull() {
		m_row.IsTaskInstanceIdNull = true;
		m_row.IsTaskInstanceIdDirty = true;
	}

	int GetMsgId() {
		if( m_row.IsMsgIdNull )
			throw konga::DbNullException("MsgId");
		return m_row.field_MsgId;
	}
	void SetMsgId(int i) {
		m_row.field_MsgId = i;
		m_row.IsMsgIdNull = false;
		m_row.IsMsgIdDirty = true;
	}
	bool IsMsgIdNull() const {
		return m_row.IsMsgIdNull;
	}
	void SetMsgIdToNull() {
		m_row.IsMsgIdNull = true;
		m_row.IsMsgIdDirty = true;
	}

	TIMESTAMP_STRUCT GetInsertTime() {
		if( m_row.IsInsertTimeNull )
			throw konga::DbNullException("InsertTime");
		return m_row.field_InsertTime;
	}
	void SetInsertTime(const TIMESTAMP_STRUCT& ts) {
		m_row.field_InsertTime = ts;
		m_row.IsInsertTimeNull = false;
		m_row.IsInsertTimeDirty = true;
	}
	bool IsInsertTimeNull() const {
		return m_row.IsInsertTimeNull;
	}
	void SetInsertTimeToNull() {
		m_row.IsInsertTimeNull = true;
		m_row.IsInsertTimeDirty = true;
	}

	TIMESTAMP_STRUCT GetModTime() {
		if( m_row.IsModTimeNull )
			throw konga::DbNullException("ModTime");
		return m_row.field_ModTime;
	}
	void SetModTime(const TIMESTAMP_STRUCT& ts) {
		m_row.field_ModTime = ts;
		m_row.IsModTimeNull = false;
		m_row.IsModTimeDirty = true;
	}
	bool IsModTimeNull() const {
		return m_row.IsModTimeNull;
	}
	void SetModTimeToNull() {
		m_row.IsModTimeNull = true;
		m_row.IsModTimeDirty = true;
	}

protected:
	EventMsgTabRow m_row;

private:
	bool IsMsgFromDirty() const {
		return m_row.IsMsgFromDirty;
	};
	bool IsMsgToDirty() const {
		return m_row.IsMsgToDirty;
	};
	bool IsMsgSubjectDirty() const {
		return m_row.IsMsgSubjectDirty;
	};
	bool IsProcessVariablesDirty() const {
		return m_row.IsProcessVariablesDirty;
	};
	bool IsReceivedAtDirty() const {
		return m_row.IsReceivedAtDirty;
	};
	bool IsScheduledAtDirty() const {
		return m_row.IsScheduledAtDirty;
	};
	bool IsProcessIdDirty() const {
		return m_row.IsProcessIdDirty;
	};
	bool IsProcessInstanceIdDirty() const {
		return m_row.IsProcessInstanceIdDirty;
	};
	bool IsTaskIdDirty() const {
		return m_row.IsTaskIdDirty;
	};
	bool IsTaskInstanceIdDirty() const {
		return m_row.IsTaskInstanceIdDirty;
	};
	bool IsMsgIdDirty() const {
		return m_row.IsMsgIdDirty;
	};
	bool IsInsertTimeDirty() const {
		return m_row.IsInsertTimeDirty;
	};
	bool IsModTimeDirty() const {
		return m_row.IsModTimeDirty;
	};


	std::string m_stUpdateFilter;

};

}                        


#endif
