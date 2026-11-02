/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_CRIDB_TASKTAB_H
#define KONGA_DB_CRIDB_TASKTAB_H

#ifdef WIN32
	#include <windows.h>
#endif
#include <string>
#include <sqltypes.h>

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/DbRecordSet.h>
#include <konga/Db/DbUtils/DbNullException.h>

namespace IDb
{

class TaskTabRow
{
public:
	TaskTabRow() : 
			field_ProcessId(0),
			IsProcessIdNull(true),
			IsProcessIdPrimaryKey(true),
			IsProcessIdDirty(false),
			field_ProcessInstanceId(0),
			IsProcessInstanceIdNull(true),
			IsProcessInstanceIdPrimaryKey(true),
			IsProcessInstanceIdDirty(false),
			field_TaskId(0),
			IsTaskIdNull(true),
			IsTaskIdPrimaryKey(true),
			IsTaskIdDirty(false),
			field_TaskInstanceId(0),
			IsTaskInstanceIdNull(true),
			IsTaskInstanceIdPrimaryKey(true),
			IsTaskInstanceIdDirty(false),
			IsTaskUserNull(true),
			IsTaskUserPrimaryKey(false),
			IsTaskUserDirty(false),
			field_InitTime(0),
			IsInitTimeNull(true),
			IsInitTimePrimaryKey(false),
			IsInitTimeDirty(false),
			field_StartTime(0),
			IsStartTimeNull(true),
			IsStartTimePrimaryKey(false),
			IsStartTimeDirty(false),
			field_Status(0),
			IsStatusNull(true),
			IsStatusPrimaryKey(false),
			IsStatusDirty(false),
			IsDataElementValueListNull(true),
			IsDataElementValueListPrimaryKey(false),
			IsDataElementValueListDirty(false),
			IsFromDocDataElementValueListNull(true),
			IsFromDocDataElementValueListPrimaryKey(false),
			IsFromDocDataElementValueListDirty(false),
			IsEscalationIdNull(true),
			IsEscalationIdPrimaryKey(false),
			IsEscalationIdDirty(false),
			field_EndTime(0),
			IsEndTimeNull(true),
			IsEndTimePrimaryKey(false),
			IsEndTimeDirty(false),
			field_MsgId(0),
			IsMsgIdNull(true),
			IsMsgIdPrimaryKey(false),
			IsMsgIdDirty(false),
			field_MsgPriority(0),
			IsMsgPriorityNull(true),
			IsMsgPriorityPrimaryKey(false),
			IsMsgPriorityDirty(false),
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

	void Copy(const TaskTabRow& r)
	{
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

		field_TaskUser = r.field_TaskUser;
		IsTaskUserNull = r.IsTaskUserNull;
		IsTaskUserDirty = r.IsTaskUserDirty;

		field_InitTime = r.field_InitTime;
		IsInitTimeNull = r.IsInitTimeNull;
		IsInitTimeDirty = r.IsInitTimeDirty;

		field_StartTime = r.field_StartTime;
		IsStartTimeNull = r.IsStartTimeNull;
		IsStartTimeDirty = r.IsStartTimeDirty;

		field_Status = r.field_Status;
		IsStatusNull = r.IsStatusNull;
		IsStatusDirty = r.IsStatusDirty;

		field_DataElementValueList = r.field_DataElementValueList;
		IsDataElementValueListNull = r.IsDataElementValueListNull;
		IsDataElementValueListDirty = r.IsDataElementValueListDirty;

		field_FromDocDataElementValueList = r.field_FromDocDataElementValueList;
		IsFromDocDataElementValueListNull = r.IsFromDocDataElementValueListNull;
		IsFromDocDataElementValueListDirty = r.IsFromDocDataElementValueListDirty;

		field_EscalationId = r.field_EscalationId;
		IsEscalationIdNull = r.IsEscalationIdNull;
		IsEscalationIdDirty = r.IsEscalationIdDirty;

		field_EndTime = r.field_EndTime;
		IsEndTimeNull = r.IsEndTimeNull;
		IsEndTimeDirty = r.IsEndTimeDirty;

		field_MsgId = r.field_MsgId;
		IsMsgIdNull = r.IsMsgIdNull;
		IsMsgIdDirty = r.IsMsgIdDirty;

		field_MsgPriority = r.field_MsgPriority;
		IsMsgPriorityNull = r.IsMsgPriorityNull;
		IsMsgPriorityDirty = r.IsMsgPriorityDirty;

		field_InsertTime = r.field_InsertTime;
		IsInsertTimeNull = r.IsInsertTimeNull;
		IsInsertTimeDirty = r.IsInsertTimeDirty;

		field_ModTime = r.field_ModTime;
		IsModTimeNull = r.IsModTimeNull;
		IsModTimeDirty = r.IsModTimeDirty;

	}

	void CopyPKs(const TaskTabRow& r)
	{
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

	}

	void SetDirty(bool b)
	{
		IsProcessIdDirty = b;
		IsProcessInstanceIdDirty = b;
		IsTaskIdDirty = b;
		IsTaskInstanceIdDirty = b;
		IsTaskUserDirty = b;
		IsInitTimeDirty = b;
		IsStartTimeDirty = b;
		IsStatusDirty = b;
		IsDataElementValueListDirty = b;
		IsFromDocDataElementValueListDirty = b;
		IsEscalationIdDirty = b;
		IsEndTimeDirty = b;
		IsMsgIdDirty = b;
		IsMsgPriorityDirty = b;
		IsInsertTimeDirty = b;
		IsModTimeDirty = b;
	}

	bool IsDirty() const
	{
		return IsProcessIdDirty ||
			IsProcessInstanceIdDirty ||
			IsTaskIdDirty ||
			IsTaskInstanceIdDirty ||
			IsTaskUserDirty ||
			IsInitTimeDirty ||
			IsStartTimeDirty ||
			IsStatusDirty ||
			IsDataElementValueListDirty ||
			IsFromDocDataElementValueListDirty ||
			IsEscalationIdDirty ||
			IsEndTimeDirty ||
			IsMsgIdDirty ||
			IsMsgPriorityDirty ||
			IsInsertTimeDirty ||
			IsModTimeDirty;
	}

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

	std::string field_TaskUser;
	bool IsTaskUserNull;
	bool IsTaskUserDirty;

	int field_InitTime;
	bool IsInitTimeNull;
	bool IsInitTimeDirty;

	int field_StartTime;
	bool IsStartTimeNull;
	bool IsStartTimeDirty;

	int field_Status;
	bool IsStatusNull;
	bool IsStatusDirty;

	std::string field_DataElementValueList;
	bool IsDataElementValueListNull;
	bool IsDataElementValueListDirty;

	std::string field_FromDocDataElementValueList;
	bool IsFromDocDataElementValueListNull;
	bool IsFromDocDataElementValueListDirty;

	std::string field_EscalationId;
	bool IsEscalationIdNull;
	bool IsEscalationIdDirty;

	int field_EndTime;
	bool IsEndTimeNull;
	bool IsEndTimeDirty;

	int field_MsgId;
	bool IsMsgIdNull;
	bool IsMsgIdDirty;

	int field_MsgPriority;
	bool IsMsgPriorityNull;
	bool IsMsgPriorityDirty;

	TIMESTAMP_STRUCT field_InsertTime;
	bool IsInsertTimeNull;
	bool IsInsertTimeDirty;

	TIMESTAMP_STRUCT field_ModTime;
	bool IsModTimeNull;
	bool IsModTimeDirty;


private:
	bool IsProcessIdPrimaryKey;
	bool IsProcessInstanceIdPrimaryKey;
	bool IsTaskIdPrimaryKey;
	bool IsTaskInstanceIdPrimaryKey;
	bool IsTaskUserPrimaryKey;
	bool IsInitTimePrimaryKey;
	bool IsStartTimePrimaryKey;
	bool IsStatusPrimaryKey;
	bool IsDataElementValueListPrimaryKey;
	bool IsFromDocDataElementValueListPrimaryKey;
	bool IsEscalationIdPrimaryKey;
	bool IsEndTimePrimaryKey;
	bool IsMsgIdPrimaryKey;
	bool IsMsgPriorityPrimaryKey;
	bool IsInsertTimePrimaryKey;
	bool IsModTimePrimaryKey;

};


class TaskTab : public konga::DbRecordSet {
public:
	TaskTab() {}

	static TaskTab* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "TaskTab";
	}

	void DoFieldExchange(const TaskTabRow& row) {
		m_row.Copy(row);
	}

	void Edit();

	std::string GetUpdateFilter() {
		return m_stUpdateFilter;
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

	std::string GetTaskUser() {
		if( m_row.IsTaskUserNull )
			throw konga::DbNullException("TaskUser");
		return m_row.field_TaskUser;
	}
	void SetTaskUser(const std::string& st) {
		m_row.field_TaskUser = st;
		m_row.IsTaskUserNull = false;
		m_row.IsTaskUserDirty = true;
	}
	bool IsTaskUserNull() const {
		return m_row.IsTaskUserNull;
	}
	void SetTaskUserToNull() {
		m_row.field_TaskUser = "";
		m_row.IsTaskUserNull = true;
		m_row.IsTaskUserDirty = true;
	}

	int GetInitTime() {
		if( m_row.IsInitTimeNull )
			throw konga::DbNullException("InitTime");
		return m_row.field_InitTime;
	}
	void SetInitTime(int i) {
		m_row.field_InitTime = i;
		m_row.IsInitTimeNull = false;
		m_row.IsInitTimeDirty = true;
	}
	bool IsInitTimeNull() const {
		return m_row.IsInitTimeNull;
	}
	void SetInitTimeToNull() {
		m_row.IsInitTimeNull = true;
		m_row.IsInitTimeDirty = true;
	}

	int GetStartTime() {
		if( m_row.IsStartTimeNull )
			throw konga::DbNullException("StartTime");
		return m_row.field_StartTime;
	}
	void SetStartTime(int i) {
		m_row.field_StartTime = i;
		m_row.IsStartTimeNull = false;
		m_row.IsStartTimeDirty = true;
	}
	bool IsStartTimeNull() const {
		return m_row.IsStartTimeNull;
	}
	void SetStartTimeToNull() {
		m_row.IsStartTimeNull = true;
		m_row.IsStartTimeDirty = true;
	}

	int GetStatus() {
		if( m_row.IsStatusNull )
			throw konga::DbNullException("Status");
		return m_row.field_Status;
	}
	void SetStatus(int i) {
		m_row.field_Status = i;
		m_row.IsStatusNull = false;
		m_row.IsStatusDirty = true;
	}
	bool IsStatusNull() const {
		return m_row.IsStatusNull;
	}
	void SetStatusToNull() {
		m_row.IsStatusNull = true;
		m_row.IsStatusDirty = true;
	}

	std::string GetDataElementValueList() {
		if( m_row.IsDataElementValueListNull )
			throw konga::DbNullException("DataElementValueList");
		return m_row.field_DataElementValueList;
	}
	void SetDataElementValueList(const std::string& st) {
		m_row.field_DataElementValueList = st;
		m_row.IsDataElementValueListNull = false;
		m_row.IsDataElementValueListDirty = true;
	}
	bool IsDataElementValueListNull() const {
		return m_row.IsDataElementValueListNull;
	}
	void SetDataElementValueListToNull() {
		m_row.field_DataElementValueList = "";
		m_row.IsDataElementValueListNull = true;
		m_row.IsDataElementValueListDirty = true;
	}

	std::string GetFromDocDataElementValueList() {
		if( m_row.IsFromDocDataElementValueListNull )
			throw konga::DbNullException("FromDocDataElementValueList");
		return m_row.field_FromDocDataElementValueList;
	}
	void SetFromDocDataElementValueList(const std::string& st) {
		m_row.field_FromDocDataElementValueList = st;
		m_row.IsFromDocDataElementValueListNull = false;
		m_row.IsFromDocDataElementValueListDirty = true;
	}
	bool IsFromDocDataElementValueListNull() const {
		return m_row.IsFromDocDataElementValueListNull;
	}
	void SetFromDocDataElementValueListToNull() {
		m_row.field_FromDocDataElementValueList = "";
		m_row.IsFromDocDataElementValueListNull = true;
		m_row.IsFromDocDataElementValueListDirty = true;
	}

	std::string GetEscalationId() {
		if( m_row.IsEscalationIdNull )
			throw konga::DbNullException("EscalationId");
		return m_row.field_EscalationId;
	}
	void SetEscalationId(const std::string& st) {
		m_row.field_EscalationId = st;
		m_row.IsEscalationIdNull = false;
		m_row.IsEscalationIdDirty = true;
	}
	bool IsEscalationIdNull() const {
		return m_row.IsEscalationIdNull;
	}
	void SetEscalationIdToNull() {
		m_row.field_EscalationId = "";
		m_row.IsEscalationIdNull = true;
		m_row.IsEscalationIdDirty = true;
	}

	int GetEndTime() {
		if( m_row.IsEndTimeNull )
			throw konga::DbNullException("EndTime");
		return m_row.field_EndTime;
	}
	void SetEndTime(int i) {
		m_row.field_EndTime = i;
		m_row.IsEndTimeNull = false;
		m_row.IsEndTimeDirty = true;
	}
	bool IsEndTimeNull() const {
		return m_row.IsEndTimeNull;
	}
	void SetEndTimeToNull() {
		m_row.IsEndTimeNull = true;
		m_row.IsEndTimeDirty = true;
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

	int GetMsgPriority() {
		if( m_row.IsMsgPriorityNull )
			throw konga::DbNullException("MsgPriority");
		return m_row.field_MsgPriority;
	}
	void SetMsgPriority(int i) {
		m_row.field_MsgPriority = i;
		m_row.IsMsgPriorityNull = false;
		m_row.IsMsgPriorityDirty = true;
	}
	bool IsMsgPriorityNull() const {
		return m_row.IsMsgPriorityNull;
	}
	void SetMsgPriorityToNull() {
		m_row.IsMsgPriorityNull = true;
		m_row.IsMsgPriorityDirty = true;
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
	TaskTabRow m_row;

private:
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
	bool IsTaskUserDirty() const {
		return m_row.IsTaskUserDirty;
	};
	bool IsInitTimeDirty() const {
		return m_row.IsInitTimeDirty;
	};
	bool IsStartTimeDirty() const {
		return m_row.IsStartTimeDirty;
	};
	bool IsStatusDirty() const {
		return m_row.IsStatusDirty;
	};
	bool IsDataElementValueListDirty() const {
		return m_row.IsDataElementValueListDirty;
	};
	bool IsFromDocDataElementValueListDirty() const {
		return m_row.IsFromDocDataElementValueListDirty;
	};
	bool IsEscalationIdDirty() const {
		return m_row.IsEscalationIdDirty;
	};
	bool IsEndTimeDirty() const {
		return m_row.IsEndTimeDirty;
	};
	bool IsMsgIdDirty() const {
		return m_row.IsMsgIdDirty;
	};
	bool IsMsgPriorityDirty() const {
		return m_row.IsMsgPriorityDirty;
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
