/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_CRIDB_TEMPORARYFILETAB_H
#define KONGA_DB_CRIDB_TEMPORARYFILETAB_H

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

class TemporaryFileTabRow
{
public:
	TemporaryFileTabRow() : 
			field_FileId(0),
			IsFileIdNull(true),
			IsFileIdPrimaryKey(true),
			IsFileIdDirty(false),
			field_ProcessId(0),
			IsProcessIdNull(true),
			IsProcessIdPrimaryKey(false),
			IsProcessIdDirty(false),
			field_ProcessInstanceId(0),
			IsProcessInstanceIdNull(true),
			IsProcessInstanceIdPrimaryKey(false),
			IsProcessInstanceIdDirty(false),
			IsFileNameNull(true),
			IsFileNamePrimaryKey(false),
			IsFileNameDirty(false),
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

	void Copy(const TemporaryFileTabRow& r)
	{
		field_FileId = r.field_FileId;
		IsFileIdNull = r.IsFileIdNull;
		IsFileIdDirty = r.IsFileIdDirty;

		field_ProcessId = r.field_ProcessId;
		IsProcessIdNull = r.IsProcessIdNull;
		IsProcessIdDirty = r.IsProcessIdDirty;

		field_ProcessInstanceId = r.field_ProcessInstanceId;
		IsProcessInstanceIdNull = r.IsProcessInstanceIdNull;
		IsProcessInstanceIdDirty = r.IsProcessInstanceIdDirty;

		field_FileName = r.field_FileName;
		IsFileNameNull = r.IsFileNameNull;
		IsFileNameDirty = r.IsFileNameDirty;

		field_InsertTime = r.field_InsertTime;
		IsInsertTimeNull = r.IsInsertTimeNull;
		IsInsertTimeDirty = r.IsInsertTimeDirty;

		field_ModTime = r.field_ModTime;
		IsModTimeNull = r.IsModTimeNull;
		IsModTimeDirty = r.IsModTimeDirty;

	}

	void CopyPKs(const TemporaryFileTabRow& r)
	{
		field_FileId = r.field_FileId;
		IsFileIdNull = r.IsFileIdNull;
		IsFileIdDirty = r.IsFileIdDirty;

	}

	void SetDirty(bool b)
	{
		IsFileIdDirty = b;
		IsProcessIdDirty = b;
		IsProcessInstanceIdDirty = b;
		IsFileNameDirty = b;
		IsInsertTimeDirty = b;
		IsModTimeDirty = b;
	}

	bool IsDirty() const
	{
		return IsFileIdDirty ||
			IsProcessIdDirty ||
			IsProcessInstanceIdDirty ||
			IsFileNameDirty ||
			IsInsertTimeDirty ||
			IsModTimeDirty;
	}

	int field_FileId;
	bool IsFileIdNull;
	bool IsFileIdDirty;

	int field_ProcessId;
	bool IsProcessIdNull;
	bool IsProcessIdDirty;

	int field_ProcessInstanceId;
	bool IsProcessInstanceIdNull;
	bool IsProcessInstanceIdDirty;

	std::string field_FileName;
	bool IsFileNameNull;
	bool IsFileNameDirty;

	TIMESTAMP_STRUCT field_InsertTime;
	bool IsInsertTimeNull;
	bool IsInsertTimeDirty;

	TIMESTAMP_STRUCT field_ModTime;
	bool IsModTimeNull;
	bool IsModTimeDirty;


private:
	bool IsFileIdPrimaryKey;
	bool IsProcessIdPrimaryKey;
	bool IsProcessInstanceIdPrimaryKey;
	bool IsFileNamePrimaryKey;
	bool IsInsertTimePrimaryKey;
	bool IsModTimePrimaryKey;

};


class TemporaryFileTab : public konga::DbRecordSet {
public:
	TemporaryFileTab() {}

	static TemporaryFileTab* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "TemporaryFileTab";
	}

	void DoFieldExchange(const TemporaryFileTabRow& row) {
		m_row.Copy(row);
	}

	void Edit();

	std::string GetUpdateFilter() {
		return m_stUpdateFilter;
	}

	int GetFileId() {
		if( m_row.IsFileIdNull )
			throw konga::DbNullException("FileId");
		return m_row.field_FileId;
	}
	void SetFileId(int i) {
		m_row.field_FileId = i;
		m_row.IsFileIdNull = false;
		m_row.IsFileIdDirty = true;
	}
	bool IsFileIdNull() const {
		return m_row.IsFileIdNull;
	}
	void SetFileIdToNull() {
		m_row.IsFileIdNull = true;
		m_row.IsFileIdDirty = true;
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

	std::string GetFileName() {
		if( m_row.IsFileNameNull )
			throw konga::DbNullException("FileName");
		return m_row.field_FileName;
	}
	void SetFileName(const std::string& st) {
		m_row.field_FileName = st;
		m_row.IsFileNameNull = false;
		m_row.IsFileNameDirty = true;
	}
	bool IsFileNameNull() const {
		return m_row.IsFileNameNull;
	}
	void SetFileNameToNull() {
		m_row.field_FileName = "";
		m_row.IsFileNameNull = true;
		m_row.IsFileNameDirty = true;
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
	TemporaryFileTabRow m_row;

private:
	bool IsFileIdDirty() const {
		return m_row.IsFileIdDirty;
	};
	bool IsProcessIdDirty() const {
		return m_row.IsProcessIdDirty;
	};
	bool IsProcessInstanceIdDirty() const {
		return m_row.IsProcessInstanceIdDirty;
	};
	bool IsFileNameDirty() const {
		return m_row.IsFileNameDirty;
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
