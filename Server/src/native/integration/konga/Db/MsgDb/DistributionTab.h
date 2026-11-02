/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_CRMSGDB_DISTRIBUTIONTAB_H
#define KONGA_DB_CRMSGDB_DISTRIBUTIONTAB_H

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

class DistributionTabRow
{
public:
	DistributionTabRow() : 
			field_JobDescId(0),
			IsJobDescIdNull(true),
			IsJobDescIdPrimaryKey(true),
			IsJobDescIdDirty(false),
			field_JobId(0),
			IsJobIdNull(true),
			IsJobIdPrimaryKey(false),
			IsJobIdDirty(false),
			IsKeyNameNull(true),
			IsKeyNamePrimaryKey(false),
			IsKeyNameDirty(false),
			IsKeyValueNull(true),
			IsKeyValuePrimaryKey(false),
			IsKeyValueDirty(false),
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

	void Copy(const DistributionTabRow& r)
	{
		field_JobDescId = r.field_JobDescId;
		IsJobDescIdNull = r.IsJobDescIdNull;
		IsJobDescIdDirty = r.IsJobDescIdDirty;

		field_JobId = r.field_JobId;
		IsJobIdNull = r.IsJobIdNull;
		IsJobIdDirty = r.IsJobIdDirty;

		field_KeyName = r.field_KeyName;
		IsKeyNameNull = r.IsKeyNameNull;
		IsKeyNameDirty = r.IsKeyNameDirty;

		field_KeyValue = r.field_KeyValue;
		IsKeyValueNull = r.IsKeyValueNull;
		IsKeyValueDirty = r.IsKeyValueDirty;

		field_InsertTime = r.field_InsertTime;
		IsInsertTimeNull = r.IsInsertTimeNull;
		IsInsertTimeDirty = r.IsInsertTimeDirty;

		field_ModTime = r.field_ModTime;
		IsModTimeNull = r.IsModTimeNull;
		IsModTimeDirty = r.IsModTimeDirty;

	}

	void CopyPKs(const DistributionTabRow& r)
	{
		field_JobDescId = r.field_JobDescId;
		IsJobDescIdNull = r.IsJobDescIdNull;
		IsJobDescIdDirty = r.IsJobDescIdDirty;

	}

	void SetDirty(bool b)
	{
		IsJobDescIdDirty = b;
		IsJobIdDirty = b;
		IsKeyNameDirty = b;
		IsKeyValueDirty = b;
		IsInsertTimeDirty = b;
		IsModTimeDirty = b;
	}

	bool IsDirty() const
	{
		return IsJobDescIdDirty ||
			IsJobIdDirty ||
			IsKeyNameDirty ||
			IsKeyValueDirty ||
			IsInsertTimeDirty ||
			IsModTimeDirty;
	}

	int field_JobDescId;
	bool IsJobDescIdNull;
	bool IsJobDescIdDirty;

	int field_JobId;
	bool IsJobIdNull;
	bool IsJobIdDirty;

	std::string field_KeyName;
	bool IsKeyNameNull;
	bool IsKeyNameDirty;

	std::string field_KeyValue;
	bool IsKeyValueNull;
	bool IsKeyValueDirty;

	TIMESTAMP_STRUCT field_InsertTime;
	bool IsInsertTimeNull;
	bool IsInsertTimeDirty;

	TIMESTAMP_STRUCT field_ModTime;
	bool IsModTimeNull;
	bool IsModTimeDirty;


private:
	bool IsJobDescIdPrimaryKey;
	bool IsJobIdPrimaryKey;
	bool IsKeyNamePrimaryKey;
	bool IsKeyValuePrimaryKey;
	bool IsInsertTimePrimaryKey;
	bool IsModTimePrimaryKey;

};


class DistributionTab : public konga::DbRecordSet {
public:
	DistributionTab() {}

	static DistributionTab* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "DistributionTab";
	}

	void DoFieldExchange(const DistributionTabRow& row) {
		m_row.Copy(row);
	}

	void Edit();

	std::string GetUpdateFilter() {
		return m_stUpdateFilter;
	}

	int GetJobDescId() {
		if( m_row.IsJobDescIdNull )
			throw konga::DbNullException("JobDescId");
		return m_row.field_JobDescId;
	}
	void SetJobDescId(int i) {
		m_row.field_JobDescId = i;
		m_row.IsJobDescIdNull = false;
		m_row.IsJobDescIdDirty = true;
	}
	bool IsJobDescIdNull() const {
		return m_row.IsJobDescIdNull;
	}
	void SetJobDescIdToNull() {
		m_row.IsJobDescIdNull = true;
		m_row.IsJobDescIdDirty = true;
	}

	int GetJobId() {
		if( m_row.IsJobIdNull )
			throw konga::DbNullException("JobId");
		return m_row.field_JobId;
	}
	void SetJobId(int i) {
		m_row.field_JobId = i;
		m_row.IsJobIdNull = false;
		m_row.IsJobIdDirty = true;
	}
	bool IsJobIdNull() const {
		return m_row.IsJobIdNull;
	}
	void SetJobIdToNull() {
		m_row.IsJobIdNull = true;
		m_row.IsJobIdDirty = true;
	}

	std::string GetKeyName() {
		if( m_row.IsKeyNameNull )
			throw konga::DbNullException("KeyName");
		return m_row.field_KeyName;
	}
	void SetKeyName(const std::string& st) {
		m_row.field_KeyName = st;
		m_row.IsKeyNameNull = false;
		m_row.IsKeyNameDirty = true;
	}
	bool IsKeyNameNull() const {
		return m_row.IsKeyNameNull;
	}
	void SetKeyNameToNull() {
		m_row.field_KeyName = "";
		m_row.IsKeyNameNull = true;
		m_row.IsKeyNameDirty = true;
	}

	std::string GetKeyValue() {
		if( m_row.IsKeyValueNull )
			throw konga::DbNullException("KeyValue");
		return m_row.field_KeyValue;
	}
	void SetKeyValue(const std::string& st) {
		m_row.field_KeyValue = st;
		m_row.IsKeyValueNull = false;
		m_row.IsKeyValueDirty = true;
	}
	bool IsKeyValueNull() const {
		return m_row.IsKeyValueNull;
	}
	void SetKeyValueToNull() {
		m_row.field_KeyValue = "";
		m_row.IsKeyValueNull = true;
		m_row.IsKeyValueDirty = true;
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
	DistributionTabRow m_row;

private:
	bool IsJobDescIdDirty() const {
		return m_row.IsJobDescIdDirty;
	};
	bool IsJobIdDirty() const {
		return m_row.IsJobIdDirty;
	};
	bool IsKeyNameDirty() const {
		return m_row.IsKeyNameDirty;
	};
	bool IsKeyValueDirty() const {
		return m_row.IsKeyValueDirty;
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
