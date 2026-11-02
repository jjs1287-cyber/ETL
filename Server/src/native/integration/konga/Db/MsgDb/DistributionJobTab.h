/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_CRMSGDB_DISTRIBUTIONJOBTAB_H
#define KONGA_DB_CRMSGDB_DISTRIBUTIONJOBTAB_H

#ifdef WIN32
	#include <windows.h>
#endif
#include <sqltypes.h>

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/DbRecordSet.h>
#include <konga/Db/DbUtils/DbNullException.h>

namespace MsgDb
{

class DistributionJobTabRow
{
public:
	DistributionJobTabRow() : 
			field_JobId(0),
			IsJobIdNull(true),
			IsJobIdPrimaryKey(true),
			IsJobIdDirty(false),
			field_ActivationStatus(0),
			IsActivationStatusNull(true),
			IsActivationStatusPrimaryKey(false),
			IsActivationStatusDirty(false),
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

	void Copy(const DistributionJobTabRow& r)
	{
		field_JobId = r.field_JobId;
		IsJobIdNull = r.IsJobIdNull;
		IsJobIdDirty = r.IsJobIdDirty;

		field_ActivationStatus = r.field_ActivationStatus;
		IsActivationStatusNull = r.IsActivationStatusNull;
		IsActivationStatusDirty = r.IsActivationStatusDirty;

		field_InsertTime = r.field_InsertTime;
		IsInsertTimeNull = r.IsInsertTimeNull;
		IsInsertTimeDirty = r.IsInsertTimeDirty;

		field_ModTime = r.field_ModTime;
		IsModTimeNull = r.IsModTimeNull;
		IsModTimeDirty = r.IsModTimeDirty;

	}

	void CopyPKs(const DistributionJobTabRow& r)
	{
		field_JobId = r.field_JobId;
		IsJobIdNull = r.IsJobIdNull;
		IsJobIdDirty = r.IsJobIdDirty;

	}

	void SetDirty(bool b)
	{
		IsJobIdDirty = b;
		IsActivationStatusDirty = b;
		IsInsertTimeDirty = b;
		IsModTimeDirty = b;
	}

	bool IsDirty() const
	{
		return IsJobIdDirty ||
			IsActivationStatusDirty ||
			IsInsertTimeDirty ||
			IsModTimeDirty;
	}

	int field_JobId;
	bool IsJobIdNull;
	bool IsJobIdDirty;

	int field_ActivationStatus;
	bool IsActivationStatusNull;
	bool IsActivationStatusDirty;

	TIMESTAMP_STRUCT field_InsertTime;
	bool IsInsertTimeNull;
	bool IsInsertTimeDirty;

	TIMESTAMP_STRUCT field_ModTime;
	bool IsModTimeNull;
	bool IsModTimeDirty;


private:
	bool IsJobIdPrimaryKey;
	bool IsActivationStatusPrimaryKey;
	bool IsInsertTimePrimaryKey;
	bool IsModTimePrimaryKey;

};


class DistributionJobTab : public konga::DbRecordSet {
public:
	DistributionJobTab() {}

	static DistributionJobTab* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "DistributionJobTab";
	}

	void DoFieldExchange(const DistributionJobTabRow& row) {
		m_row.Copy(row);
	}

	void Edit();

	std::string GetUpdateFilter() {
		return m_stUpdateFilter;
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

	int GetActivationStatus() {
		if( m_row.IsActivationStatusNull )
			throw konga::DbNullException("ActivationStatus");
		return m_row.field_ActivationStatus;
	}
	void SetActivationStatus(int i) {
		m_row.field_ActivationStatus = i;
		m_row.IsActivationStatusNull = false;
		m_row.IsActivationStatusDirty = true;
	}
	bool IsActivationStatusNull() const {
		return m_row.IsActivationStatusNull;
	}
	void SetActivationStatusToNull() {
		m_row.IsActivationStatusNull = true;
		m_row.IsActivationStatusDirty = true;
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
	DistributionJobTabRow m_row;

private:
	bool IsJobIdDirty() const {
		return m_row.IsJobIdDirty;
	};
	bool IsActivationStatusDirty() const {
		return m_row.IsActivationStatusDirty;
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
