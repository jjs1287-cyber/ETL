/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_CRIDB_ACTIVITYTAB_H
#define KONGA_DB_CRIDB_ACTIVITYTAB_H

#ifdef WIN32
	#include <windows.h>
#endif
#include <sqltypes.h>

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/DbRecordSet.h>
#include <konga/Db/DbUtils/DbNullException.h>

namespace IDb
{

class ActivityTabRow
{
public:
	ActivityTabRow() : 
			field_ProcessId(0),
			IsProcessIdNull(true),
			IsProcessIdPrimaryKey(true),
			IsProcessIdDirty(false),
			field_TaskId(0),
			IsTaskIdNull(true),
			IsTaskIdPrimaryKey(true),
			IsTaskIdDirty(false),
			field_ActivityId(0),
			IsActivityIdNull(true),
			IsActivityIdPrimaryKey(true),
			IsActivityIdDirty(false),
			field_ProcessInstanceId(0),
			IsProcessInstanceIdNull(true),
			IsProcessInstanceIdPrimaryKey(true),
			IsProcessInstanceIdDirty(false),
			field_TaskInstanceId(0),
			IsTaskInstanceIdNull(true),
			IsTaskInstanceIdPrimaryKey(true),
			IsTaskInstanceIdDirty(false),
			field_ActivityInstanceId(0),
			IsActivityInstanceIdNull(true),
			IsActivityInstanceIdPrimaryKey(true),
			IsActivityInstanceIdDirty(false),
			field_StartTime(0),
			IsStartTimeNull(true),
			IsStartTimePrimaryKey(false),
			IsStartTimeDirty(false),
			field_EndTime(0),
			IsEndTimeNull(true),
			IsEndTimePrimaryKey(false),
			IsEndTimeDirty(false),
			field_StatusTime(0),
			IsStatusTimeNull(true),
			IsStatusTimePrimaryKey(false),
			IsStatusTimeDirty(false),
			field_Status(0),
			IsStatusNull(true),
			IsStatusPrimaryKey(false),
			IsStatusDirty(false),
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

	void Copy(const ActivityTabRow& r)
	{
		field_ProcessId = r.field_ProcessId;
		IsProcessIdNull = r.IsProcessIdNull;
		IsProcessIdDirty = r.IsProcessIdDirty;

		field_TaskId = r.field_TaskId;
		IsTaskIdNull = r.IsTaskIdNull;
		IsTaskIdDirty = r.IsTaskIdDirty;

		field_ActivityId = r.field_ActivityId;
		IsActivityIdNull = r.IsActivityIdNull;
		IsActivityIdDirty = r.IsActivityIdDirty;

		field_ProcessInstanceId = r.field_ProcessInstanceId;
		IsProcessInstanceIdNull = r.IsProcessInstanceIdNull;
		IsProcessInstanceIdDirty = r.IsProcessInstanceIdDirty;

		field_TaskInstanceId = r.field_TaskInstanceId;
		IsTaskInstanceIdNull = r.IsTaskInstanceIdNull;
		IsTaskInstanceIdDirty = r.IsTaskInstanceIdDirty;

		field_ActivityInstanceId = r.field_ActivityInstanceId;
		IsActivityInstanceIdNull = r.IsActivityInstanceIdNull;
		IsActivityInstanceIdDirty = r.IsActivityInstanceIdDirty;

		field_StartTime = r.field_StartTime;
		IsStartTimeNull = r.IsStartTimeNull;
		IsStartTimeDirty = r.IsStartTimeDirty;

		field_EndTime = r.field_EndTime;
		IsEndTimeNull = r.IsEndTimeNull;
		IsEndTimeDirty = r.IsEndTimeDirty;

		field_StatusTime = r.field_StatusTime;
		IsStatusTimeNull = r.IsStatusTimeNull;
		IsStatusTimeDirty = r.IsStatusTimeDirty;

		field_Status = r.field_Status;
		IsStatusNull = r.IsStatusNull;
		IsStatusDirty = r.IsStatusDirty;

		field_InsertTime = r.field_InsertTime;
		IsInsertTimeNull = r.IsInsertTimeNull;
		IsInsertTimeDirty = r.IsInsertTimeDirty;

		field_ModTime = r.field_ModTime;
		IsModTimeNull = r.IsModTimeNull;
		IsModTimeDirty = r.IsModTimeDirty;

	}

	void CopyPKs(const ActivityTabRow& r)
	{
		field_ProcessId = r.field_ProcessId;
		IsProcessIdNull = r.IsProcessIdNull;
		IsProcessIdDirty = r.IsProcessIdDirty;

		field_TaskId = r.field_TaskId;
		IsTaskIdNull = r.IsTaskIdNull;
		IsTaskIdDirty = r.IsTaskIdDirty;

		field_ActivityId = r.field_ActivityId;
		IsActivityIdNull = r.IsActivityIdNull;
		IsActivityIdDirty = r.IsActivityIdDirty;

		field_ProcessInstanceId = r.field_ProcessInstanceId;
		IsProcessInstanceIdNull = r.IsProcessInstanceIdNull;
		IsProcessInstanceIdDirty = r.IsProcessInstanceIdDirty;

		field_TaskInstanceId = r.field_TaskInstanceId;
		IsTaskInstanceIdNull = r.IsTaskInstanceIdNull;
		IsTaskInstanceIdDirty = r.IsTaskInstanceIdDirty;

		field_ActivityInstanceId = r.field_ActivityInstanceId;
		IsActivityInstanceIdNull = r.IsActivityInstanceIdNull;
		IsActivityInstanceIdDirty = r.IsActivityInstanceIdDirty;

	}

	void SetDirty(bool b)
	{
		IsProcessIdDirty = b;
		IsTaskIdDirty = b;
		IsActivityIdDirty = b;
		IsProcessInstanceIdDirty = b;
		IsTaskInstanceIdDirty = b;
		IsActivityInstanceIdDirty = b;
		IsStartTimeDirty = b;
		IsEndTimeDirty = b;
		IsStatusTimeDirty = b;
		IsStatusDirty = b;
		IsInsertTimeDirty = b;
		IsModTimeDirty = b;
	}

	bool IsDirty() const
	{
		return IsProcessIdDirty ||
			IsTaskIdDirty ||
			IsActivityIdDirty ||
			IsProcessInstanceIdDirty ||
			IsTaskInstanceIdDirty ||
			IsActivityInstanceIdDirty ||
			IsStartTimeDirty ||
			IsEndTimeDirty ||
			IsStatusTimeDirty ||
			IsStatusDirty ||
			IsInsertTimeDirty ||
			IsModTimeDirty;
	}

	int field_ProcessId;
	bool IsProcessIdNull;
	bool IsProcessIdDirty;

	int field_TaskId;
	bool IsTaskIdNull;
	bool IsTaskIdDirty;

	int field_ActivityId;
	bool IsActivityIdNull;
	bool IsActivityIdDirty;

	int field_ProcessInstanceId;
	bool IsProcessInstanceIdNull;
	bool IsProcessInstanceIdDirty;

	int field_TaskInstanceId;
	bool IsTaskInstanceIdNull;
	bool IsTaskInstanceIdDirty;

	int field_ActivityInstanceId;
	bool IsActivityInstanceIdNull;
	bool IsActivityInstanceIdDirty;

	int field_StartTime;
	bool IsStartTimeNull;
	bool IsStartTimeDirty;

	int field_EndTime;
	bool IsEndTimeNull;
	bool IsEndTimeDirty;

	int field_StatusTime;
	bool IsStatusTimeNull;
	bool IsStatusTimeDirty;

	int field_Status;
	bool IsStatusNull;
	bool IsStatusDirty;

	TIMESTAMP_STRUCT field_InsertTime;
	bool IsInsertTimeNull;
	bool IsInsertTimeDirty;

	TIMESTAMP_STRUCT field_ModTime;
	bool IsModTimeNull;
	bool IsModTimeDirty;


private:
	bool IsProcessIdPrimaryKey;
	bool IsTaskIdPrimaryKey;
	bool IsActivityIdPrimaryKey;
	bool IsProcessInstanceIdPrimaryKey;
	bool IsTaskInstanceIdPrimaryKey;
	bool IsActivityInstanceIdPrimaryKey;
	bool IsStartTimePrimaryKey;
	bool IsEndTimePrimaryKey;
	bool IsStatusTimePrimaryKey;
	bool IsStatusPrimaryKey;
	bool IsInsertTimePrimaryKey;
	bool IsModTimePrimaryKey;

};


class ActivityTab : public konga::DbRecordSet {
public:
	ActivityTab() {}

	static ActivityTab* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "ActivityTab";
	}

	void DoFieldExchange(const ActivityTabRow& row) {
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

	int GetActivityId() {
		if( m_row.IsActivityIdNull )
			throw konga::DbNullException("ActivityId");
		return m_row.field_ActivityId;
	}
	void SetActivityId(int i) {
		m_row.field_ActivityId = i;
		m_row.IsActivityIdNull = false;
		m_row.IsActivityIdDirty = true;
	}
	bool IsActivityIdNull() const {
		return m_row.IsActivityIdNull;
	}
	void SetActivityIdToNull() {
		m_row.IsActivityIdNull = true;
		m_row.IsActivityIdDirty = true;
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

	int GetActivityInstanceId() {
		if( m_row.IsActivityInstanceIdNull )
			throw konga::DbNullException("ActivityInstanceId");
		return m_row.field_ActivityInstanceId;
	}
	void SetActivityInstanceId(int i) {
		m_row.field_ActivityInstanceId = i;
		m_row.IsActivityInstanceIdNull = false;
		m_row.IsActivityInstanceIdDirty = true;
	}
	bool IsActivityInstanceIdNull() const {
		return m_row.IsActivityInstanceIdNull;
	}
	void SetActivityInstanceIdToNull() {
		m_row.IsActivityInstanceIdNull = true;
		m_row.IsActivityInstanceIdDirty = true;
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

	int GetStatusTime() {
		if( m_row.IsStatusTimeNull )
			throw konga::DbNullException("StatusTime");
		return m_row.field_StatusTime;
	}
	void SetStatusTime(int i) {
		m_row.field_StatusTime = i;
		m_row.IsStatusTimeNull = false;
		m_row.IsStatusTimeDirty = true;
	}
	bool IsStatusTimeNull() const {
		return m_row.IsStatusTimeNull;
	}
	void SetStatusTimeToNull() {
		m_row.IsStatusTimeNull = true;
		m_row.IsStatusTimeDirty = true;
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
	ActivityTabRow m_row;

private:
	bool IsProcessIdDirty() const {
		return m_row.IsProcessIdDirty;
	};
	bool IsTaskIdDirty() const {
		return m_row.IsTaskIdDirty;
	};
	bool IsActivityIdDirty() const {
		return m_row.IsActivityIdDirty;
	};
	bool IsProcessInstanceIdDirty() const {
		return m_row.IsProcessInstanceIdDirty;
	};
	bool IsTaskInstanceIdDirty() const {
		return m_row.IsTaskInstanceIdDirty;
	};
	bool IsActivityInstanceIdDirty() const {
		return m_row.IsActivityInstanceIdDirty;
	};
	bool IsStartTimeDirty() const {
		return m_row.IsStartTimeDirty;
	};
	bool IsEndTimeDirty() const {
		return m_row.IsEndTimeDirty;
	};
	bool IsStatusTimeDirty() const {
		return m_row.IsStatusTimeDirty;
	};
	bool IsStatusDirty() const {
		return m_row.IsStatusDirty;
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
