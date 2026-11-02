/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_CRPDB_PROCESSTAB_H
#define KONGA_DB_CRPDB_PROCESSTAB_H

#ifdef WIN32
	#include <windows.h>
#endif
#include <string>
#include <sqltypes.h>

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/DbRecordSet.h>
#include <konga/Db/DbUtils/DbNullException.h>

namespace PDb
{

class ProcessTabRow
{
public:
	ProcessTabRow() : 
			field_ProcessId(0),
			IsProcessIdNull(true),
			IsProcessIdPrimaryKey(true),
			IsProcessIdDirty(false),
			field_ProcessVersion(0),
			IsProcessVersionNull(true),
			IsProcessVersionPrimaryKey(true),
			IsProcessVersionDirty(false),
			IsProcessNameNull(true),
			IsProcessNamePrimaryKey(false),
			IsProcessNameDirty(false),
			IsDescriptionNull(true),
			IsDescriptionPrimaryKey(false),
			IsDescriptionDirty(false),
			field_ErrorHandlerId(0),
			IsErrorHandlerIdNull(true),
			IsErrorHandlerIdPrimaryKey(false),
			IsErrorHandlerIdDirty(false),
			IsOwnerNull(true),
			IsOwnerPrimaryKey(false),
			IsOwnerDirty(false),
			field_ProcessType(0),
			IsProcessTypeNull(true),
			IsProcessTypePrimaryKey(false),
			IsProcessTypeDirty(false),
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

	void Copy(const ProcessTabRow& r)
	{
		field_ProcessId = r.field_ProcessId;
		IsProcessIdNull = r.IsProcessIdNull;
		IsProcessIdDirty = r.IsProcessIdDirty;

		field_ProcessVersion = r.field_ProcessVersion;
		IsProcessVersionNull = r.IsProcessVersionNull;
		IsProcessVersionDirty = r.IsProcessVersionDirty;

		field_ProcessName = r.field_ProcessName;
		IsProcessNameNull = r.IsProcessNameNull;
		IsProcessNameDirty = r.IsProcessNameDirty;

		field_Description = r.field_Description;
		IsDescriptionNull = r.IsDescriptionNull;
		IsDescriptionDirty = r.IsDescriptionDirty;

		field_ErrorHandlerId = r.field_ErrorHandlerId;
		IsErrorHandlerIdNull = r.IsErrorHandlerIdNull;
		IsErrorHandlerIdDirty = r.IsErrorHandlerIdDirty;

		field_Owner = r.field_Owner;
		IsOwnerNull = r.IsOwnerNull;
		IsOwnerDirty = r.IsOwnerDirty;

		field_ProcessType = r.field_ProcessType;
		IsProcessTypeNull = r.IsProcessTypeNull;
		IsProcessTypeDirty = r.IsProcessTypeDirty;

		field_InsertTime = r.field_InsertTime;
		IsInsertTimeNull = r.IsInsertTimeNull;
		IsInsertTimeDirty = r.IsInsertTimeDirty;

		field_ModTime = r.field_ModTime;
		IsModTimeNull = r.IsModTimeNull;
		IsModTimeDirty = r.IsModTimeDirty;

	}

	void CopyPKs(const ProcessTabRow& r)
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
		IsProcessNameDirty = b;
		IsDescriptionDirty = b;
		IsErrorHandlerIdDirty = b;
		IsOwnerDirty = b;
		IsProcessTypeDirty = b;
		IsInsertTimeDirty = b;
		IsModTimeDirty = b;
	}

	bool IsDirty() const
	{
		return IsProcessIdDirty ||
			IsProcessVersionDirty ||
			IsProcessNameDirty ||
			IsDescriptionDirty ||
			IsErrorHandlerIdDirty ||
			IsOwnerDirty ||
			IsProcessTypeDirty ||
			IsInsertTimeDirty ||
			IsModTimeDirty;
	}

	int field_ProcessId;
	bool IsProcessIdNull;
	bool IsProcessIdDirty;

	int field_ProcessVersion;
	bool IsProcessVersionNull;
	bool IsProcessVersionDirty;

	std::string field_ProcessName;
	bool IsProcessNameNull;
	bool IsProcessNameDirty;

	std::string field_Description;
	bool IsDescriptionNull;
	bool IsDescriptionDirty;

	int field_ErrorHandlerId;
	bool IsErrorHandlerIdNull;
	bool IsErrorHandlerIdDirty;

	std::string field_Owner;
	bool IsOwnerNull;
	bool IsOwnerDirty;

	int field_ProcessType;
	bool IsProcessTypeNull;
	bool IsProcessTypeDirty;

	TIMESTAMP_STRUCT field_InsertTime;
	bool IsInsertTimeNull;
	bool IsInsertTimeDirty;

	TIMESTAMP_STRUCT field_ModTime;
	bool IsModTimeNull;
	bool IsModTimeDirty;


private:
	bool IsProcessIdPrimaryKey;
	bool IsProcessVersionPrimaryKey;
	bool IsProcessNamePrimaryKey;
	bool IsDescriptionPrimaryKey;
	bool IsErrorHandlerIdPrimaryKey;
	bool IsOwnerPrimaryKey;
	bool IsProcessTypePrimaryKey;
	bool IsInsertTimePrimaryKey;
	bool IsModTimePrimaryKey;

};


class ProcessTab : public konga::DbRecordSet {
public:
	ProcessTab() {}

	static ProcessTab* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "ProcessTab";
	}

	void DoFieldExchange(const ProcessTabRow& row) {
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

	std::string GetProcessName() {
		if( m_row.IsProcessNameNull )
			throw konga::DbNullException("ProcessName");
		return m_row.field_ProcessName;
	}
	void SetProcessName(const std::string& st) {
		m_row.field_ProcessName = st;
		m_row.IsProcessNameNull = false;
		m_row.IsProcessNameDirty = true;
	}
	bool IsProcessNameNull() const {
		return m_row.IsProcessNameNull;
	}
	void SetProcessNameToNull() {
		m_row.field_ProcessName = "";
		m_row.IsProcessNameNull = true;
		m_row.IsProcessNameDirty = true;
	}

	std::string GetDescription() {
		if( m_row.IsDescriptionNull )
			throw konga::DbNullException("Description");
		return m_row.field_Description;
	}
	void SetDescription(const std::string& st) {
		m_row.field_Description = st;
		m_row.IsDescriptionNull = false;
		m_row.IsDescriptionDirty = true;
	}
	bool IsDescriptionNull() const {
		return m_row.IsDescriptionNull;
	}
	void SetDescriptionToNull() {
		m_row.field_Description = "";
		m_row.IsDescriptionNull = true;
		m_row.IsDescriptionDirty = true;
	}

	int GetErrorHandlerId() {
		if( m_row.IsErrorHandlerIdNull )
			throw konga::DbNullException("ErrorHandlerId");
		return m_row.field_ErrorHandlerId;
	}
	void SetErrorHandlerId(int i) {
		m_row.field_ErrorHandlerId = i;
		m_row.IsErrorHandlerIdNull = false;
		m_row.IsErrorHandlerIdDirty = true;
	}
	bool IsErrorHandlerIdNull() const {
		return m_row.IsErrorHandlerIdNull;
	}
	void SetErrorHandlerIdToNull() {
		m_row.IsErrorHandlerIdNull = true;
		m_row.IsErrorHandlerIdDirty = true;
	}

	std::string GetOwner() {
		if( m_row.IsOwnerNull )
			throw konga::DbNullException("Owner");
		return m_row.field_Owner;
	}
	void SetOwner(const std::string& st) {
		m_row.field_Owner = st;
		m_row.IsOwnerNull = false;
		m_row.IsOwnerDirty = true;
	}
	bool IsOwnerNull() const {
		return m_row.IsOwnerNull;
	}
	void SetOwnerToNull() {
		m_row.field_Owner = "";
		m_row.IsOwnerNull = true;
		m_row.IsOwnerDirty = true;
	}

	int GetProcessType() {
		if( m_row.IsProcessTypeNull )
			throw konga::DbNullException("ProcessType");
		return m_row.field_ProcessType;
	}
	void SetProcessType(int i) {
		m_row.field_ProcessType = i;
		m_row.IsProcessTypeNull = false;
		m_row.IsProcessTypeDirty = true;
	}
	bool IsProcessTypeNull() const {
		return m_row.IsProcessTypeNull;
	}
	void SetProcessTypeToNull() {
		m_row.IsProcessTypeNull = true;
		m_row.IsProcessTypeDirty = true;
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
	ProcessTabRow m_row;

private:
	bool IsProcessIdDirty() const {
		return m_row.IsProcessIdDirty;
	};
	bool IsProcessVersionDirty() const {
		return m_row.IsProcessVersionDirty;
	};
	bool IsProcessNameDirty() const {
		return m_row.IsProcessNameDirty;
	};
	bool IsDescriptionDirty() const {
		return m_row.IsDescriptionDirty;
	};
	bool IsErrorHandlerIdDirty() const {
		return m_row.IsErrorHandlerIdDirty;
	};
	bool IsOwnerDirty() const {
		return m_row.IsOwnerDirty;
	};
	bool IsProcessTypeDirty() const {
		return m_row.IsProcessTypeDirty;
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
