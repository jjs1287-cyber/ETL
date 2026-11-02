/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_CRPDB_ACTIVITYRULETAB_H
#define KONGA_DB_CRPDB_ACTIVITYRULETAB_H

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

class ActivityRuleTabRow
{
public:
	ActivityRuleTabRow() : 
			field_ProcessId(0),
			IsProcessIdNull(true),
			IsProcessIdPrimaryKey(true),
			IsProcessIdDirty(false),
			field_ProcessVersion(0),
			IsProcessVersionNull(true),
			IsProcessVersionPrimaryKey(true),
			IsProcessVersionDirty(false),
			field_ActivityRuleId(0),
			IsActivityRuleIdNull(true),
			IsActivityRuleIdPrimaryKey(true),
			IsActivityRuleIdDirty(false),
			field_ActivityId(0),
			IsActivityIdNull(true),
			IsActivityIdPrimaryKey(false),
			IsActivityIdDirty(false),
			IsTrueActivityListNull(true),
			IsTrueActivityListPrimaryKey(false),
			IsTrueActivityListDirty(false),
			IsFalseActivityListNull(true),
			IsFalseActivityListPrimaryKey(false),
			IsFalseActivityListDirty(false),
			field_ErrorHandlerId(0),
			IsErrorHandlerIdNull(true),
			IsErrorHandlerIdPrimaryKey(false),
			IsErrorHandlerIdDirty(false),
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

	void Copy(const ActivityRuleTabRow& r)
	{
		field_ProcessId = r.field_ProcessId;
		IsProcessIdNull = r.IsProcessIdNull;
		IsProcessIdDirty = r.IsProcessIdDirty;

		field_ProcessVersion = r.field_ProcessVersion;
		IsProcessVersionNull = r.IsProcessVersionNull;
		IsProcessVersionDirty = r.IsProcessVersionDirty;

		field_ActivityRuleId = r.field_ActivityRuleId;
		IsActivityRuleIdNull = r.IsActivityRuleIdNull;
		IsActivityRuleIdDirty = r.IsActivityRuleIdDirty;

		field_ActivityId = r.field_ActivityId;
		IsActivityIdNull = r.IsActivityIdNull;
		IsActivityIdDirty = r.IsActivityIdDirty;

		field_TrueActivityList = r.field_TrueActivityList;
		IsTrueActivityListNull = r.IsTrueActivityListNull;
		IsTrueActivityListDirty = r.IsTrueActivityListDirty;

		field_FalseActivityList = r.field_FalseActivityList;
		IsFalseActivityListNull = r.IsFalseActivityListNull;
		IsFalseActivityListDirty = r.IsFalseActivityListDirty;

		field_ErrorHandlerId = r.field_ErrorHandlerId;
		IsErrorHandlerIdNull = r.IsErrorHandlerIdNull;
		IsErrorHandlerIdDirty = r.IsErrorHandlerIdDirty;

		field_InsertTime = r.field_InsertTime;
		IsInsertTimeNull = r.IsInsertTimeNull;
		IsInsertTimeDirty = r.IsInsertTimeDirty;

		field_ModTime = r.field_ModTime;
		IsModTimeNull = r.IsModTimeNull;
		IsModTimeDirty = r.IsModTimeDirty;

	}

	void CopyPKs(const ActivityRuleTabRow& r)
	{
		field_ProcessId = r.field_ProcessId;
		IsProcessIdNull = r.IsProcessIdNull;
		IsProcessIdDirty = r.IsProcessIdDirty;

		field_ProcessVersion = r.field_ProcessVersion;
		IsProcessVersionNull = r.IsProcessVersionNull;
		IsProcessVersionDirty = r.IsProcessVersionDirty;

		field_ActivityRuleId = r.field_ActivityRuleId;
		IsActivityRuleIdNull = r.IsActivityRuleIdNull;
		IsActivityRuleIdDirty = r.IsActivityRuleIdDirty;

	}

	void SetDirty(bool b)
	{
		IsProcessIdDirty = b;
		IsProcessVersionDirty = b;
		IsActivityRuleIdDirty = b;
		IsActivityIdDirty = b;
		IsTrueActivityListDirty = b;
		IsFalseActivityListDirty = b;
		IsErrorHandlerIdDirty = b;
		IsInsertTimeDirty = b;
		IsModTimeDirty = b;
	}

	bool IsDirty() const
	{
		return IsProcessIdDirty ||
			IsProcessVersionDirty ||
			IsActivityRuleIdDirty ||
			IsActivityIdDirty ||
			IsTrueActivityListDirty ||
			IsFalseActivityListDirty ||
			IsErrorHandlerIdDirty ||
			IsInsertTimeDirty ||
			IsModTimeDirty;
	}

	int field_ProcessId;
	bool IsProcessIdNull;
	bool IsProcessIdDirty;

	int field_ProcessVersion;
	bool IsProcessVersionNull;
	bool IsProcessVersionDirty;

	int field_ActivityRuleId;
	bool IsActivityRuleIdNull;
	bool IsActivityRuleIdDirty;

	int field_ActivityId;
	bool IsActivityIdNull;
	bool IsActivityIdDirty;

	std::string field_TrueActivityList;
	bool IsTrueActivityListNull;
	bool IsTrueActivityListDirty;

	std::string field_FalseActivityList;
	bool IsFalseActivityListNull;
	bool IsFalseActivityListDirty;

	int field_ErrorHandlerId;
	bool IsErrorHandlerIdNull;
	bool IsErrorHandlerIdDirty;

	TIMESTAMP_STRUCT field_InsertTime;
	bool IsInsertTimeNull;
	bool IsInsertTimeDirty;

	TIMESTAMP_STRUCT field_ModTime;
	bool IsModTimeNull;
	bool IsModTimeDirty;


private:
	bool IsProcessIdPrimaryKey;
	bool IsProcessVersionPrimaryKey;
	bool IsActivityRuleIdPrimaryKey;
	bool IsActivityIdPrimaryKey;
	bool IsTrueActivityListPrimaryKey;
	bool IsFalseActivityListPrimaryKey;
	bool IsErrorHandlerIdPrimaryKey;
	bool IsInsertTimePrimaryKey;
	bool IsModTimePrimaryKey;

};


class ActivityRuleTab : public konga::DbRecordSet {
public:
	ActivityRuleTab() {}

	static ActivityRuleTab* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "ActivityRuleTab";
	}

	void DoFieldExchange(const ActivityRuleTabRow& row) {
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

	int GetActivityRuleId() {
		if( m_row.IsActivityRuleIdNull )
			throw konga::DbNullException("ActivityRuleId");
		return m_row.field_ActivityRuleId;
	}
	void SetActivityRuleId(int i) {
		m_row.field_ActivityRuleId = i;
		m_row.IsActivityRuleIdNull = false;
		m_row.IsActivityRuleIdDirty = true;
	}
	bool IsActivityRuleIdNull() const {
		return m_row.IsActivityRuleIdNull;
	}
	void SetActivityRuleIdToNull() {
		m_row.IsActivityRuleIdNull = true;
		m_row.IsActivityRuleIdDirty = true;
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

	std::string GetTrueActivityList() {
		if( m_row.IsTrueActivityListNull )
			throw konga::DbNullException("TrueActivityList");
		return m_row.field_TrueActivityList;
	}
	void SetTrueActivityList(const std::string& st) {
		m_row.field_TrueActivityList = st;
		m_row.IsTrueActivityListNull = false;
		m_row.IsTrueActivityListDirty = true;
	}
	bool IsTrueActivityListNull() const {
		return m_row.IsTrueActivityListNull;
	}
	void SetTrueActivityListToNull() {
		m_row.field_TrueActivityList = "";
		m_row.IsTrueActivityListNull = true;
		m_row.IsTrueActivityListDirty = true;
	}

	std::string GetFalseActivityList() {
		if( m_row.IsFalseActivityListNull )
			throw konga::DbNullException("FalseActivityList");
		return m_row.field_FalseActivityList;
	}
	void SetFalseActivityList(const std::string& st) {
		m_row.field_FalseActivityList = st;
		m_row.IsFalseActivityListNull = false;
		m_row.IsFalseActivityListDirty = true;
	}
	bool IsFalseActivityListNull() const {
		return m_row.IsFalseActivityListNull;
	}
	void SetFalseActivityListToNull() {
		m_row.field_FalseActivityList = "";
		m_row.IsFalseActivityListNull = true;
		m_row.IsFalseActivityListDirty = true;
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
	ActivityRuleTabRow m_row;

private:
	bool IsProcessIdDirty() const {
		return m_row.IsProcessIdDirty;
	};
	bool IsProcessVersionDirty() const {
		return m_row.IsProcessVersionDirty;
	};
	bool IsActivityRuleIdDirty() const {
		return m_row.IsActivityRuleIdDirty;
	};
	bool IsActivityIdDirty() const {
		return m_row.IsActivityIdDirty;
	};
	bool IsTrueActivityListDirty() const {
		return m_row.IsTrueActivityListDirty;
	};
	bool IsFalseActivityListDirty() const {
		return m_row.IsFalseActivityListDirty;
	};
	bool IsErrorHandlerIdDirty() const {
		return m_row.IsErrorHandlerIdDirty;
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
