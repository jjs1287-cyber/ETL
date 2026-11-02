/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_CRIDB_STATUSTAB_H
#define KONGA_DB_CRIDB_STATUSTAB_H

#ifdef WIN32
	#include <windows.h>
#endif
#include <sqltypes.h>

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/DbRecordSet.h>
#include <konga/Db/DbUtils/DbNullException.h>

namespace IDb
{

class StatusTabRow
{
public:
	StatusTabRow() : 
			field_ProcessId(0),
			IsProcessIdNull(true),
			IsProcessIdPrimaryKey(true),
			IsProcessIdDirty(false),
			field_ProcessVersion(0),
			IsProcessVersionNull(true),
			IsProcessVersionPrimaryKey(true),
			IsProcessVersionDirty(false),
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

	void Copy(const StatusTabRow& r)
	{
		field_ProcessId = r.field_ProcessId;
		IsProcessIdNull = r.IsProcessIdNull;
		IsProcessIdDirty = r.IsProcessIdDirty;

		field_ProcessVersion = r.field_ProcessVersion;
		IsProcessVersionNull = r.IsProcessVersionNull;
		IsProcessVersionDirty = r.IsProcessVersionDirty;

		field_Status = r.field_Status;
		IsStatusNull = r.IsStatusNull;
		IsStatusDirty = r.IsStatusDirty;

		field_StatusTime = r.field_StatusTime;
		IsStatusTimeNull = r.IsStatusTimeNull;
		IsStatusTimeDirty = r.IsStatusTimeDirty;

		field_UpdateFlag = r.field_UpdateFlag;
		IsUpdateFlagNull = r.IsUpdateFlagNull;
		IsUpdateFlagDirty = r.IsUpdateFlagDirty;

		field_InsertTime = r.field_InsertTime;
		IsInsertTimeNull = r.IsInsertTimeNull;
		IsInsertTimeDirty = r.IsInsertTimeDirty;

		field_ModTime = r.field_ModTime;
		IsModTimeNull = r.IsModTimeNull;
		IsModTimeDirty = r.IsModTimeDirty;

	}

	void CopyPKs(const StatusTabRow& r)
	{
		field_ProcessId = r.field_ProcessId;
		IsProcessIdNull = r.IsProcessIdNull;
		IsProcessIdDirty = r.IsProcessIdDirty;

		field_ProcessVersion = r.field_ProcessVersion;
		IsProcessVersionNull = r.IsProcessVersionNull;
		IsProcessVersionDirty = r.IsProcessVersionDirty;

	}

	void SetDirty(bool b)
	{
		IsProcessIdDirty = b;
		IsProcessVersionDirty = b;
		IsStatusDirty = b;
		IsStatusTimeDirty = b;
		IsUpdateFlagDirty = b;
		IsInsertTimeDirty = b;
		IsModTimeDirty = b;
	}

	bool IsDirty() const
	{
		return IsProcessIdDirty ||
			IsProcessVersionDirty ||
			IsStatusDirty ||
			IsStatusTimeDirty ||
			IsUpdateFlagDirty ||
			IsInsertTimeDirty ||
			IsModTimeDirty;
	}

	int field_ProcessId;
	bool IsProcessIdNull;
	bool IsProcessIdDirty;

	int field_ProcessVersion;
	bool IsProcessVersionNull;
	bool IsProcessVersionDirty;

	int field_Status;
	bool IsStatusNull;
	bool IsStatusDirty;

	int field_StatusTime;
	bool IsStatusTimeNull;
	bool IsStatusTimeDirty;

	int field_UpdateFlag;
	bool IsUpdateFlagNull;
	bool IsUpdateFlagDirty;

	TIMESTAMP_STRUCT field_InsertTime;
	bool IsInsertTimeNull;
	bool IsInsertTimeDirty;

	TIMESTAMP_STRUCT field_ModTime;
	bool IsModTimeNull;
	bool IsModTimeDirty;


private:
	bool IsProcessIdPrimaryKey;
	bool IsProcessVersionPrimaryKey;
	bool IsStatusPrimaryKey;
	bool IsStatusTimePrimaryKey;
	bool IsUpdateFlagPrimaryKey;
	bool IsInsertTimePrimaryKey;
	bool IsModTimePrimaryKey;

};


class StatusTab : public konga::DbRecordSet {
public:
	StatusTab() {}

	static StatusTab* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "StatusTab";
	}

	void DoFieldExchange(const StatusTabRow& row) {
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
	StatusTabRow m_row;

private:
	bool IsProcessIdDirty() const {
		return m_row.IsProcessIdDirty;
	};
	bool IsProcessVersionDirty() const {
		return m_row.IsProcessVersionDirty;
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
