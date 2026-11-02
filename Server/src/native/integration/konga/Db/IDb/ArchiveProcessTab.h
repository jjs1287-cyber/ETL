/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_CRIDB_ARCHIVEPROCESSTAB_H
#define KONGA_DB_CRIDB_ARCHIVEPROCESSTAB_H

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

class ArchiveProcessTabRow
{
public:
	ArchiveProcessTabRow() : 
			field_ArchiveId(0),
			IsArchiveIdNull(true),
			IsArchiveIdPrimaryKey(true),
			IsArchiveIdDirty(false),
			field_ProcessId(0),
			IsProcessIdNull(true),
			IsProcessIdPrimaryKey(false),
			IsProcessIdDirty(false),
			field_ProcessInstanceId(0),
			IsProcessInstanceIdNull(true),
			IsProcessInstanceIdPrimaryKey(false),
			IsProcessInstanceIdDirty(false),
			field_ProcessVersion(0),
			IsProcessVersionNull(true),
			IsProcessVersionPrimaryKey(false),
			IsProcessVersionDirty(false),
			field_InitialTask(0),
			IsInitialTaskNull(true),
			IsInitialTaskPrimaryKey(false),
			IsInitialTaskDirty(false),
			field_StartTime(0),
			IsStartTimeNull(true),
			IsStartTimePrimaryKey(false),
			IsStartTimeDirty(false),
			field_EndTime(0),
			IsEndTimeNull(true),
			IsEndTimePrimaryKey(false),
			IsEndTimeDirty(false),
			field_Status(0),
			IsStatusNull(true),
			IsStatusPrimaryKey(false),
			IsStatusDirty(false),
			field_StatusTime(0),
			IsStatusTimeNull(true),
			IsStatusTimePrimaryKey(false),
			IsStatusTimeDirty(false),
			field_UpdateFlag(0),
			IsUpdateFlagNull(true),
			IsUpdateFlagPrimaryKey(false),
			IsUpdateFlagDirty(false),
			IsProcessDataElementValueListNull(true),
			IsProcessDataElementValueListPrimaryKey(false),
			IsProcessDataElementValueListDirty(false),
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

	void Copy(const ArchiveProcessTabRow& r)
	{
		field_ArchiveId = r.field_ArchiveId;
		IsArchiveIdNull = r.IsArchiveIdNull;
		IsArchiveIdDirty = r.IsArchiveIdDirty;

		field_ProcessId = r.field_ProcessId;
		IsProcessIdNull = r.IsProcessIdNull;
		IsProcessIdDirty = r.IsProcessIdDirty;

		field_ProcessInstanceId = r.field_ProcessInstanceId;
		IsProcessInstanceIdNull = r.IsProcessInstanceIdNull;
		IsProcessInstanceIdDirty = r.IsProcessInstanceIdDirty;

		field_ProcessVersion = r.field_ProcessVersion;
		IsProcessVersionNull = r.IsProcessVersionNull;
		IsProcessVersionDirty = r.IsProcessVersionDirty;

		field_InitialTask = r.field_InitialTask;
		IsInitialTaskNull = r.IsInitialTaskNull;
		IsInitialTaskDirty = r.IsInitialTaskDirty;

		field_StartTime = r.field_StartTime;
		IsStartTimeNull = r.IsStartTimeNull;
		IsStartTimeDirty = r.IsStartTimeDirty;

		field_EndTime = r.field_EndTime;
		IsEndTimeNull = r.IsEndTimeNull;
		IsEndTimeDirty = r.IsEndTimeDirty;

		field_Status = r.field_Status;
		IsStatusNull = r.IsStatusNull;
		IsStatusDirty = r.IsStatusDirty;

		field_StatusTime = r.field_StatusTime;
		IsStatusTimeNull = r.IsStatusTimeNull;
		IsStatusTimeDirty = r.IsStatusTimeDirty;

		field_UpdateFlag = r.field_UpdateFlag;
		IsUpdateFlagNull = r.IsUpdateFlagNull;
		IsUpdateFlagDirty = r.IsUpdateFlagDirty;

		field_ProcessDataElementValueList = r.field_ProcessDataElementValueList;
		IsProcessDataElementValueListNull = r.IsProcessDataElementValueListNull;
		IsProcessDataElementValueListDirty = r.IsProcessDataElementValueListDirty;

		field_InsertTime = r.field_InsertTime;
		IsInsertTimeNull = r.IsInsertTimeNull;
		IsInsertTimeDirty = r.IsInsertTimeDirty;

		field_ModTime = r.field_ModTime;
		IsModTimeNull = r.IsModTimeNull;
		IsModTimeDirty = r.IsModTimeDirty;

	}

	void CopyPKs(const ArchiveProcessTabRow& r)
	{
		field_ArchiveId = r.field_ArchiveId;
		IsArchiveIdNull = r.IsArchiveIdNull;
		IsArchiveIdDirty = r.IsArchiveIdDirty;

	}

	void SetDirty(bool b)
	{
		IsArchiveIdDirty = b;
		IsProcessIdDirty = b;
		IsProcessInstanceIdDirty = b;
		IsProcessVersionDirty = b;
		IsInitialTaskDirty = b;
		IsStartTimeDirty = b;
		IsEndTimeDirty = b;
		IsStatusDirty = b;
		IsStatusTimeDirty = b;
		IsUpdateFlagDirty = b;
		IsProcessDataElementValueListDirty = b;
		IsInsertTimeDirty = b;
		IsModTimeDirty = b;
	}

	bool IsDirty() const
	{
		return IsArchiveIdDirty ||
			IsProcessIdDirty ||
			IsProcessInstanceIdDirty ||
			IsProcessVersionDirty ||
			IsInitialTaskDirty ||
			IsStartTimeDirty ||
			IsEndTimeDirty ||
			IsStatusDirty ||
			IsStatusTimeDirty ||
			IsUpdateFlagDirty ||
			IsProcessDataElementValueListDirty ||
			IsInsertTimeDirty ||
			IsModTimeDirty;
	}

	int field_ArchiveId;
	bool IsArchiveIdNull;
	bool IsArchiveIdDirty;

	int field_ProcessId;
	bool IsProcessIdNull;
	bool IsProcessIdDirty;

	int field_ProcessInstanceId;
	bool IsProcessInstanceIdNull;
	bool IsProcessInstanceIdDirty;

	int field_ProcessVersion;
	bool IsProcessVersionNull;
	bool IsProcessVersionDirty;

	int field_InitialTask;
	bool IsInitialTaskNull;
	bool IsInitialTaskDirty;

	int field_StartTime;
	bool IsStartTimeNull;
	bool IsStartTimeDirty;

	int field_EndTime;
	bool IsEndTimeNull;
	bool IsEndTimeDirty;

	int field_Status;
	bool IsStatusNull;
	bool IsStatusDirty;

	int field_StatusTime;
	bool IsStatusTimeNull;
	bool IsStatusTimeDirty;

	int field_UpdateFlag;
	bool IsUpdateFlagNull;
	bool IsUpdateFlagDirty;

	std::string field_ProcessDataElementValueList;
	bool IsProcessDataElementValueListNull;
	bool IsProcessDataElementValueListDirty;

	TIMESTAMP_STRUCT field_InsertTime;
	bool IsInsertTimeNull;
	bool IsInsertTimeDirty;

	TIMESTAMP_STRUCT field_ModTime;
	bool IsModTimeNull;
	bool IsModTimeDirty;


private:
	bool IsArchiveIdPrimaryKey;
	bool IsProcessIdPrimaryKey;
	bool IsProcessInstanceIdPrimaryKey;
	bool IsProcessVersionPrimaryKey;
	bool IsInitialTaskPrimaryKey;
	bool IsStartTimePrimaryKey;
	bool IsEndTimePrimaryKey;
	bool IsStatusPrimaryKey;
	bool IsStatusTimePrimaryKey;
	bool IsUpdateFlagPrimaryKey;
	bool IsProcessDataElementValueListPrimaryKey;
	bool IsInsertTimePrimaryKey;
	bool IsModTimePrimaryKey;

};


class ArchiveProcessTab : public konga::DbRecordSet {
public:
	ArchiveProcessTab() {}

	static ArchiveProcessTab* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "ArchiveProcessTab";
	}

	void DoFieldExchange(const ArchiveProcessTabRow& row) {
		m_row.Copy(row);
	}

	void Edit();

	std::string GetUpdateFilter() {
		return m_stUpdateFilter;
	}

	int GetArchiveId() {
		if( m_row.IsArchiveIdNull )
			throw konga::DbNullException("ArchiveId");
		return m_row.field_ArchiveId;
	}
	void SetArchiveId(int i) {
		m_row.field_ArchiveId = i;
		m_row.IsArchiveIdNull = false;
		m_row.IsArchiveIdDirty = true;
	}
	bool IsArchiveIdNull() const {
		return m_row.IsArchiveIdNull;
	}
	void SetArchiveIdToNull() {
		m_row.IsArchiveIdNull = true;
		m_row.IsArchiveIdDirty = true;
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

	int GetProcessVersion() {
		if( m_row.IsProcessVersionNull )
			throw konga::DbNullException("ProcessVersion");
		return m_row.field_ProcessVersion;
	}
	void SetProcessVersion(int i) {
		m_row.field_ProcessVersion = i;
		m_row.IsProcessVersionNull = false;
		m_row.IsProcessVersionDirty = true;
	}
	bool IsProcessVersionNull() const {
		return m_row.IsProcessVersionNull;
	}
	void SetProcessVersionToNull() {
		m_row.IsProcessVersionNull = true;
		m_row.IsProcessVersionDirty = true;
	}

	int GetInitialTask() {
		if( m_row.IsInitialTaskNull )
			throw konga::DbNullException("InitialTask");
		return m_row.field_InitialTask;
	}
	void SetInitialTask(int i) {
		m_row.field_InitialTask = i;
		m_row.IsInitialTaskNull = false;
		m_row.IsInitialTaskDirty = true;
	}
	bool IsInitialTaskNull() const {
		return m_row.IsInitialTaskNull;
	}
	void SetInitialTaskToNull() {
		m_row.IsInitialTaskNull = true;
		m_row.IsInitialTaskDirty = true;
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

	int GetUpdateFlag() {
		if( m_row.IsUpdateFlagNull )
			throw konga::DbNullException("UpdateFlag");
		return m_row.field_UpdateFlag;
	}
	void SetUpdateFlag(int i) {
		m_row.field_UpdateFlag = i;
		m_row.IsUpdateFlagNull = false;
		m_row.IsUpdateFlagDirty = true;
	}
	bool IsUpdateFlagNull() const {
		return m_row.IsUpdateFlagNull;
	}
	void SetUpdateFlagToNull() {
		m_row.IsUpdateFlagNull = true;
		m_row.IsUpdateFlagDirty = true;
	}

	std::string GetProcessDataElementValueList() {
		if( m_row.IsProcessDataElementValueListNull )
			throw konga::DbNullException("ProcessDataElementValueList");
		return m_row.field_ProcessDataElementValueList;
	}
	void SetProcessDataElementValueList(const std::string& st) {
		m_row.field_ProcessDataElementValueList = st;
		m_row.IsProcessDataElementValueListNull = false;
		m_row.IsProcessDataElementValueListDirty = true;
	}
	bool IsProcessDataElementValueListNull() const {
		return m_row.IsProcessDataElementValueListNull;
	}
	void SetProcessDataElementValueListToNull() {
		m_row.field_ProcessDataElementValueList = "";
		m_row.IsProcessDataElementValueListNull = true;
		m_row.IsProcessDataElementValueListDirty = true;
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
	ArchiveProcessTabRow m_row;

private:
	bool IsArchiveIdDirty() const {
		return m_row.IsArchiveIdDirty;
	};
	bool IsProcessIdDirty() const {
		return m_row.IsProcessIdDirty;
	};
	bool IsProcessInstanceIdDirty() const {
		return m_row.IsProcessInstanceIdDirty;
	};
	bool IsProcessVersionDirty() const {
		return m_row.IsProcessVersionDirty;
	};
	bool IsInitialTaskDirty() const {
		return m_row.IsInitialTaskDirty;
	};
	bool IsStartTimeDirty() const {
		return m_row.IsStartTimeDirty;
	};
	bool IsEndTimeDirty() const {
		return m_row.IsEndTimeDirty;
	};
	bool IsStatusDirty() const {
		return m_row.IsStatusDirty;
	};
	bool IsStatusTimeDirty() const {
		return m_row.IsStatusTimeDirty;
	};
	bool IsUpdateFlagDirty() const {
		return m_row.IsUpdateFlagDirty;
	};
	bool IsProcessDataElementValueListDirty() const {
		return m_row.IsProcessDataElementValueListDirty;
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
