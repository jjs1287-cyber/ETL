/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_CRPDB_OBJECTPERMISSIONSTAB_H
#define KONGA_DB_CRPDB_OBJECTPERMISSIONSTAB_H

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

class ObjectPermissionsTabRow
{
public:
	ObjectPermissionsTabRow() : 
			field_ObjectId(0),
			IsObjectIdNull(true),
			IsObjectIdPrimaryKey(true),
			IsObjectIdDirty(false),
			IsObjectNameNull(true),
			IsObjectNamePrimaryKey(false),
			IsObjectNameDirty(false),
			field_AllowAccess(0),
			IsAllowAccessNull(true),
			IsAllowAccessPrimaryKey(false),
			IsAllowAccessDirty(false),
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

	void Copy(const ObjectPermissionsTabRow& r)
	{
		field_ObjectId = r.field_ObjectId;
		IsObjectIdNull = r.IsObjectIdNull;
		IsObjectIdDirty = r.IsObjectIdDirty;

		field_ObjectName = r.field_ObjectName;
		IsObjectNameNull = r.IsObjectNameNull;
		IsObjectNameDirty = r.IsObjectNameDirty;

		field_AllowAccess = r.field_AllowAccess;
		IsAllowAccessNull = r.IsAllowAccessNull;
		IsAllowAccessDirty = r.IsAllowAccessDirty;

		field_InsertTime = r.field_InsertTime;
		IsInsertTimeNull = r.IsInsertTimeNull;
		IsInsertTimeDirty = r.IsInsertTimeDirty;

		field_ModTime = r.field_ModTime;
		IsModTimeNull = r.IsModTimeNull;
		IsModTimeDirty = r.IsModTimeDirty;

	}

	void CopyPKs(const ObjectPermissionsTabRow& r)
	{
		field_ObjectId = r.field_ObjectId;
		IsObjectIdNull = r.IsObjectIdNull;
		IsObjectIdDirty = r.IsObjectIdDirty;

	}

	void SetDirty(bool b)
	{
		IsObjectIdDirty = b;
		IsObjectNameDirty = b;
		IsAllowAccessDirty = b;
		IsInsertTimeDirty = b;
		IsModTimeDirty = b;
	}

	bool IsDirty() const
	{
		return IsObjectIdDirty ||
			IsObjectNameDirty ||
			IsAllowAccessDirty ||
			IsInsertTimeDirty ||
			IsModTimeDirty;
	}

	int field_ObjectId;
	bool IsObjectIdNull;
	bool IsObjectIdDirty;

	std::string field_ObjectName;
	bool IsObjectNameNull;
	bool IsObjectNameDirty;

	int field_AllowAccess;
	bool IsAllowAccessNull;
	bool IsAllowAccessDirty;

	TIMESTAMP_STRUCT field_InsertTime;
	bool IsInsertTimeNull;
	bool IsInsertTimeDirty;

	TIMESTAMP_STRUCT field_ModTime;
	bool IsModTimeNull;
	bool IsModTimeDirty;


private:
	bool IsObjectIdPrimaryKey;
	bool IsObjectNamePrimaryKey;
	bool IsAllowAccessPrimaryKey;
	bool IsInsertTimePrimaryKey;
	bool IsModTimePrimaryKey;

};


class ObjectPermissionsTab : public konga::DbRecordSet {
public:
	ObjectPermissionsTab() {}

	static ObjectPermissionsTab* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "ObjectPermissionsTab";
	}

	void DoFieldExchange(const ObjectPermissionsTabRow& row) {
		m_row.Copy(row);
	}

	void Edit();

	std::string GetUpdateFilter() {
		return m_stUpdateFilter;
	}

	int GetObjectId() {
		if( m_row.IsObjectIdNull )
			throw konga::DbNullException("ObjectId");
		return m_row.field_ObjectId;
	}
	void SetObjectId(int i) {
		m_row.field_ObjectId = i;
		m_row.IsObjectIdNull = false;
		m_row.IsObjectIdDirty = true;
	}
	bool IsObjectIdNull() const {
		return m_row.IsObjectIdNull;
	}
	void SetObjectIdToNull() {
		m_row.IsObjectIdNull = true;
		m_row.IsObjectIdDirty = true;
	}

	std::string GetObjectName() {
		if( m_row.IsObjectNameNull )
			throw konga::DbNullException("ObjectName");
		return m_row.field_ObjectName;
	}
	void SetObjectName(const std::string& st) {
		m_row.field_ObjectName = st;
		m_row.IsObjectNameNull = false;
		m_row.IsObjectNameDirty = true;
	}
	bool IsObjectNameNull() const {
		return m_row.IsObjectNameNull;
	}
	void SetObjectNameToNull() {
		m_row.field_ObjectName = "";
		m_row.IsObjectNameNull = true;
		m_row.IsObjectNameDirty = true;
	}

	int GetAllowAccess() {
		if( m_row.IsAllowAccessNull )
			throw konga::DbNullException("AllowAccess");
		return m_row.field_AllowAccess;
	}
	void SetAllowAccess(int i) {
		m_row.field_AllowAccess = i;
		m_row.IsAllowAccessNull = false;
		m_row.IsAllowAccessDirty = true;
	}
	bool IsAllowAccessNull() const {
		return m_row.IsAllowAccessNull;
	}
	void SetAllowAccessToNull() {
		m_row.IsAllowAccessNull = true;
		m_row.IsAllowAccessDirty = true;
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
	ObjectPermissionsTabRow m_row;

private:
	bool IsObjectIdDirty() const {
		return m_row.IsObjectIdDirty;
	};
	bool IsObjectNameDirty() const {
		return m_row.IsObjectNameDirty;
	};
	bool IsAllowAccessDirty() const {
		return m_row.IsAllowAccessDirty;
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
