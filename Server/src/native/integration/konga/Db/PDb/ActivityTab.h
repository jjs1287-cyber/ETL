/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_CRPDB_ACTIVITYTAB_H
#define KONGA_DB_CRPDB_ACTIVITYTAB_H

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

class ActivityTabRow
{
public:
	ActivityTabRow() : 
			field_ProcessId(0),
			IsProcessIdNull(true),
			IsProcessIdPrimaryKey(true),
			IsProcessIdDirty(false),
			field_ProcessVersion(0),
			IsProcessVersionNull(true),
			IsProcessVersionPrimaryKey(true),
			IsProcessVersionDirty(false),
			field_ActivityId(0),
			IsActivityIdNull(true),
			IsActivityIdPrimaryKey(true),
			IsActivityIdDirty(false),
			IsActivityNameNull(true),
			IsActivityNamePrimaryKey(false),
			IsActivityNameDirty(false),
			IsActivityMethodNull(true),
			IsActivityMethodPrimaryKey(false),
			IsActivityMethodDirty(false),
			IsActivityLocationNull(true),
			IsActivityLocationPrimaryKey(false),
			IsActivityLocationDirty(false),
			field_ErrorHandlerId(0),
			IsErrorHandlerIdNull(true),
			IsErrorHandlerIdPrimaryKey(false),
			IsErrorHandlerIdDirty(false),
			field_HasInstance(0),
			IsHasInstanceNull(true),
			IsHasInstancePrimaryKey(false),
			IsHasInstanceDirty(false),
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

		field_ProcessVersion = r.field_ProcessVersion;
		IsProcessVersionNull = r.IsProcessVersionNull;
		IsProcessVersionDirty = r.IsProcessVersionDirty;

		field_ActivityId = r.field_ActivityId;
		IsActivityIdNull = r.IsActivityIdNull;
		IsActivityIdDirty = r.IsActivityIdDirty;

		field_ActivityName = r.field_ActivityName;
		IsActivityNameNull = r.IsActivityNameNull;
		IsActivityNameDirty = r.IsActivityNameDirty;

		field_ActivityMethod = r.field_ActivityMethod;
		IsActivityMethodNull = r.IsActivityMethodNull;
		IsActivityMethodDirty = r.IsActivityMethodDirty;

		field_ActivityLocation = r.field_ActivityLocation;
		IsActivityLocationNull = r.IsActivityLocationNull;
		IsActivityLocationDirty = r.IsActivityLocationDirty;

		field_ErrorHandlerId = r.field_ErrorHandlerId;
		IsErrorHandlerIdNull = r.IsErrorHandlerIdNull;
		IsErrorHandlerIdDirty = r.IsErrorHandlerIdDirty;

		field_HasInstance = r.field_HasInstance;
		IsHasInstanceNull = r.IsHasInstanceNull;
		IsHasInstanceDirty = r.IsHasInstanceDirty;

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

		field_ProcessVersion = r.field_ProcessVersion;
		IsProcessVersionNull = r.IsProcessVersionNull;
		IsProcessVersionDirty = r.IsProcessVersionDirty;

		field_ActivityId = r.field_ActivityId;
		IsActivityIdNull = r.IsActivityIdNull;
		IsActivityIdDirty = r.IsActivityIdDirty;

	}

	void SetDirty(bool b)
	{
		IsProcessIdDirty = b;
		IsProcessVersionDirty = b;
		IsActivityIdDirty = b;
		IsActivityNameDirty = b;
		IsActivityMethodDirty = b;
		IsActivityLocationDirty = b;
		IsErrorHandlerIdDirty = b;
		IsHasInstanceDirty = b;
		IsInsertTimeDirty = b;
		IsModTimeDirty = b;
	}

	bool IsDirty() const
	{
		return IsProcessIdDirty ||
			IsProcessVersionDirty ||
			IsActivityIdDirty ||
			IsActivityNameDirty ||
			IsActivityMethodDirty ||
			IsActivityLocationDirty ||
			IsErrorHandlerIdDirty ||
			IsHasInstanceDirty ||
			IsInsertTimeDirty ||
			IsModTimeDirty;
	}

	int field_ProcessId;
	bool IsProcessIdNull;
	bool IsProcessIdDirty;

	int field_ProcessVersion;
	bool IsProcessVersionNull;
	bool IsProcessVersionDirty;

	int field_ActivityId;
	bool IsActivityIdNull;
	bool IsActivityIdDirty;

	std::string field_ActivityName;
	bool IsActivityNameNull;
	bool IsActivityNameDirty;

	std::string field_ActivityMethod;
	bool IsActivityMethodNull;
	bool IsActivityMethodDirty;

	std::string field_ActivityLocation;
	bool IsActivityLocationNull;
	bool IsActivityLocationDirty;

	int field_ErrorHandlerId;
	bool IsErrorHandlerIdNull;
	bool IsErrorHandlerIdDirty;

	int field_HasInstance;
	bool IsHasInstanceNull;
	bool IsHasInstanceDirty;

	TIMESTAMP_STRUCT field_InsertTime;
	bool IsInsertTimeNull;
	bool IsInsertTimeDirty;

	TIMESTAMP_STRUCT field_ModTime;
	bool IsModTimeNull;
	bool IsModTimeDirty;


private:
	bool IsProcessIdPrimaryKey;
	bool IsProcessVersionPrimaryKey;
	bool IsActivityIdPrimaryKey;
	bool IsActivityNamePrimaryKey;
	bool IsActivityMethodPrimaryKey;
	bool IsActivityLocationPrimaryKey;
	bool IsErrorHandlerIdPrimaryKey;
	bool IsHasInstancePrimaryKey;
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

	std::string GetActivityName() {
		if( m_row.IsActivityNameNull )
			throw konga::DbNullException("ActivityName");
		return m_row.field_ActivityName;
	}
	void SetActivityName(const std::string& st) {
		m_row.field_ActivityName = st;
		m_row.IsActivityNameNull = false;
		m_row.IsActivityNameDirty = true;
	}
	bool IsActivityNameNull() const {
		return m_row.IsActivityNameNull;
	}
	void SetActivityNameToNull() {
		m_row.field_ActivityName = "";
		m_row.IsActivityNameNull = true;
		m_row.IsActivityNameDirty = true;
	}

	std::string GetActivityMethod() {
		if( m_row.IsActivityMethodNull )
			throw konga::DbNullException("ActivityMethod");
		return m_row.field_ActivityMethod;
	}
	void SetActivityMethod(const std::string& st) {
		m_row.field_ActivityMethod = st;
		m_row.IsActivityMethodNull = false;
		m_row.IsActivityMethodDirty = true;
	}
	bool IsActivityMethodNull() const {
		return m_row.IsActivityMethodNull;
	}
	void SetActivityMethodToNull() {
		m_row.field_ActivityMethod = "";
		m_row.IsActivityMethodNull = true;
		m_row.IsActivityMethodDirty = true;
	}

	std::string GetActivityLocation() {
		if( m_row.IsActivityLocationNull )
			throw konga::DbNullException("ActivityLocation");
		return m_row.field_ActivityLocation;
	}
	void SetActivityLocation(const std::string& st) {
		m_row.field_ActivityLocation = st;
		m_row.IsActivityLocationNull = false;
		m_row.IsActivityLocationDirty = true;
	}
	bool IsActivityLocationNull() const {
		return m_row.IsActivityLocationNull;
	}
	void SetActivityLocationToNull() {
		m_row.field_ActivityLocation = "";
		m_row.IsActivityLocationNull = true;
		m_row.IsActivityLocationDirty = true;
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

	int GetHasInstance() {
		if( m_row.IsHasInstanceNull )
			throw konga::DbNullException("HasInstance");
		return m_row.field_HasInstance;
	}
	void SetHasInstance(int i) {
		m_row.field_HasInstance = i;
		m_row.IsHasInstanceNull = false;
		m_row.IsHasInstanceDirty = true;
	}
	bool IsHasInstanceNull() const {
		return m_row.IsHasInstanceNull;
	}
	void SetHasInstanceToNull() {
		m_row.IsHasInstanceNull = true;
		m_row.IsHasInstanceDirty = true;
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
	bool IsProcessVersionDirty() const {
		return m_row.IsProcessVersionDirty;
	};
	bool IsActivityIdDirty() const {
		return m_row.IsActivityIdDirty;
	};
	bool IsActivityNameDirty() const {
		return m_row.IsActivityNameDirty;
	};
	bool IsActivityMethodDirty() const {
		return m_row.IsActivityMethodDirty;
	};
	bool IsActivityLocationDirty() const {
		return m_row.IsActivityLocationDirty;
	};
	bool IsErrorHandlerIdDirty() const {
		return m_row.IsErrorHandlerIdDirty;
	};
	bool IsHasInstanceDirty() const {
		return m_row.IsHasInstanceDirty;
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
