/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_CRTRANDB_DBTABLESTAB_H
#define KONGA_DB_CRTRANDB_DBTABLESTAB_H

#ifdef WIN32
	#include <windows.h>
#endif
#include <string>
#include <sqltypes.h>

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/DbRecordSet.h>
#include <konga/Db/DbUtils/DbNullException.h>

namespace TranDb
{

class DBTablesTabRow
{
public:
	DBTablesTabRow() : 
			field_DBTableID(0),
			IsDBTableIDNull(true),
			IsDBTableIDPrimaryKey(true),
			IsDBTableIDDirty(false),
			field_Transformation_ID(0),
			IsTransformation_IDNull(true),
			IsTransformation_IDPrimaryKey(false),
			IsTransformation_IDDirty(false),
			IsTablesNull(true),
			IsTablesPrimaryKey(false),
			IsTablesDirty(false),
			field_TablesType(0),
			IsTablesTypeNull(true),
			IsTablesTypePrimaryKey(false),
			IsTablesTypeDirty(false),
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

	void Copy(const DBTablesTabRow& r)
	{
		field_DBTableID = r.field_DBTableID;
		IsDBTableIDNull = r.IsDBTableIDNull;
		IsDBTableIDDirty = r.IsDBTableIDDirty;

		field_Transformation_ID = r.field_Transformation_ID;
		IsTransformation_IDNull = r.IsTransformation_IDNull;
		IsTransformation_IDDirty = r.IsTransformation_IDDirty;

		field_Tables = r.field_Tables;
		IsTablesNull = r.IsTablesNull;
		IsTablesDirty = r.IsTablesDirty;

		field_TablesType = r.field_TablesType;
		IsTablesTypeNull = r.IsTablesTypeNull;
		IsTablesTypeDirty = r.IsTablesTypeDirty;

		field_InsertTime = r.field_InsertTime;
		IsInsertTimeNull = r.IsInsertTimeNull;
		IsInsertTimeDirty = r.IsInsertTimeDirty;

		field_ModTime = r.field_ModTime;
		IsModTimeNull = r.IsModTimeNull;
		IsModTimeDirty = r.IsModTimeDirty;

	}

	void CopyPKs(const DBTablesTabRow& r)
	{
		field_DBTableID = r.field_DBTableID;
		IsDBTableIDNull = r.IsDBTableIDNull;
		IsDBTableIDDirty = r.IsDBTableIDDirty;

	}

	void SetDirty(bool b)
	{
		IsDBTableIDDirty = b;
		IsTransformation_IDDirty = b;
		IsTablesDirty = b;
		IsTablesTypeDirty = b;
		IsInsertTimeDirty = b;
		IsModTimeDirty = b;
	}

	bool IsDirty() const
	{
		return IsDBTableIDDirty ||
			IsTransformation_IDDirty ||
			IsTablesDirty ||
			IsTablesTypeDirty ||
			IsInsertTimeDirty ||
			IsModTimeDirty;
	}

	int field_DBTableID;
	bool IsDBTableIDNull;
	bool IsDBTableIDDirty;

	int field_Transformation_ID;
	bool IsTransformation_IDNull;
	bool IsTransformation_IDDirty;

	std::string field_Tables;
	bool IsTablesNull;
	bool IsTablesDirty;

	int field_TablesType;
	bool IsTablesTypeNull;
	bool IsTablesTypeDirty;

	TIMESTAMP_STRUCT field_InsertTime;
	bool IsInsertTimeNull;
	bool IsInsertTimeDirty;

	TIMESTAMP_STRUCT field_ModTime;
	bool IsModTimeNull;
	bool IsModTimeDirty;


private:
	bool IsDBTableIDPrimaryKey;
	bool IsTransformation_IDPrimaryKey;
	bool IsTablesPrimaryKey;
	bool IsTablesTypePrimaryKey;
	bool IsInsertTimePrimaryKey;
	bool IsModTimePrimaryKey;

};


class DBTablesTab : public konga::DbRecordSet {
public:
	DBTablesTab() {}

	static DBTablesTab* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "DBTablesTab";
	}

	void DoFieldExchange(const DBTablesTabRow& row) {
		m_row.Copy(row);
	}

	void Edit();

	std::string GetUpdateFilter() {
		return m_stUpdateFilter;
	}

	int GetDBTableID() {
		if( m_row.IsDBTableIDNull )
			throw konga::DbNullException("DBTableID");
		return m_row.field_DBTableID;
	}
	void SetDBTableID(int i) {
		m_row.field_DBTableID = i;
		m_row.IsDBTableIDNull = false;
		m_row.IsDBTableIDDirty = true;
	}
	bool IsDBTableIDNull() const {
		return m_row.IsDBTableIDNull;
	}
	void SetDBTableIDToNull() {
		m_row.IsDBTableIDNull = true;
		m_row.IsDBTableIDDirty = true;
	}

	int GetTransformation_ID() {
		if( m_row.IsTransformation_IDNull )
			throw konga::DbNullException("Transformation_ID");
		return m_row.field_Transformation_ID;
	}
	void SetTransformation_ID(int i) {
		m_row.field_Transformation_ID = i;
		m_row.IsTransformation_IDNull = false;
		m_row.IsTransformation_IDDirty = true;
	}
	bool IsTransformation_IDNull() const {
		return m_row.IsTransformation_IDNull;
	}
	void SetTransformation_IDToNull() {
		m_row.IsTransformation_IDNull = true;
		m_row.IsTransformation_IDDirty = true;
	}

	std::string GetTables() {
		if( m_row.IsTablesNull )
			throw konga::DbNullException("Tables");
		return m_row.field_Tables;
	}
	void SetTables(const std::string& st) {
		m_row.field_Tables = st;
		m_row.IsTablesNull = false;
		m_row.IsTablesDirty = true;
	}
	bool IsTablesNull() const {
		return m_row.IsTablesNull;
	}
	void SetTablesToNull() {
		m_row.field_Tables = "";
		m_row.IsTablesNull = true;
		m_row.IsTablesDirty = true;
	}

	int GetTablesType() {
		if( m_row.IsTablesTypeNull )
			throw konga::DbNullException("TablesType");
		return m_row.field_TablesType;
	}
	void SetTablesType(int i) {
		m_row.field_TablesType = i;
		m_row.IsTablesTypeNull = false;
		m_row.IsTablesTypeDirty = true;
	}
	bool IsTablesTypeNull() const {
		return m_row.IsTablesTypeNull;
	}
	void SetTablesTypeToNull() {
		m_row.IsTablesTypeNull = true;
		m_row.IsTablesTypeDirty = true;
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
	DBTablesTabRow m_row;

private:
	bool IsDBTableIDDirty() const {
		return m_row.IsDBTableIDDirty;
	};
	bool IsTransformation_IDDirty() const {
		return m_row.IsTransformation_IDDirty;
	};
	bool IsTablesDirty() const {
		return m_row.IsTablesDirty;
	};
	bool IsTablesTypeDirty() const {
		return m_row.IsTablesTypeDirty;
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
