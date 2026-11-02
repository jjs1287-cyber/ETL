/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_CRTRANDB_DBFIELDSTAB_H
#define KONGA_DB_CRTRANDB_DBFIELDSTAB_H

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

class DBFieldsTabRow
{
public:
	DBFieldsTabRow() : 
			field_DBFieldID(0),
			IsDBFieldIDNull(true),
			IsDBFieldIDPrimaryKey(true),
			IsDBFieldIDDirty(false),
			field_Transformation_ID(0),
			IsTransformation_IDNull(true),
			IsTransformation_IDPrimaryKey(false),
			IsTransformation_IDDirty(false),
			IsKeyFieldsNull(true),
			IsKeyFieldsPrimaryKey(false),
			IsKeyFieldsDirty(false),
			field_KeyFieldsType(0),
			IsKeyFieldsTypeNull(true),
			IsKeyFieldsTypePrimaryKey(false),
			IsKeyFieldsTypeDirty(false),
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

	void Copy(const DBFieldsTabRow& r)
	{
		field_DBFieldID = r.field_DBFieldID;
		IsDBFieldIDNull = r.IsDBFieldIDNull;
		IsDBFieldIDDirty = r.IsDBFieldIDDirty;

		field_Transformation_ID = r.field_Transformation_ID;
		IsTransformation_IDNull = r.IsTransformation_IDNull;
		IsTransformation_IDDirty = r.IsTransformation_IDDirty;

		field_KeyFields = r.field_KeyFields;
		IsKeyFieldsNull = r.IsKeyFieldsNull;
		IsKeyFieldsDirty = r.IsKeyFieldsDirty;

		field_KeyFieldsType = r.field_KeyFieldsType;
		IsKeyFieldsTypeNull = r.IsKeyFieldsTypeNull;
		IsKeyFieldsTypeDirty = r.IsKeyFieldsTypeDirty;

		field_InsertTime = r.field_InsertTime;
		IsInsertTimeNull = r.IsInsertTimeNull;
		IsInsertTimeDirty = r.IsInsertTimeDirty;

		field_ModTime = r.field_ModTime;
		IsModTimeNull = r.IsModTimeNull;
		IsModTimeDirty = r.IsModTimeDirty;

	}

	void CopyPKs(const DBFieldsTabRow& r)
	{
		field_DBFieldID = r.field_DBFieldID;
		IsDBFieldIDNull = r.IsDBFieldIDNull;
		IsDBFieldIDDirty = r.IsDBFieldIDDirty;

	}

	void SetDirty(bool b)
	{
		IsDBFieldIDDirty = b;
		IsTransformation_IDDirty = b;
		IsKeyFieldsDirty = b;
		IsKeyFieldsTypeDirty = b;
		IsInsertTimeDirty = b;
		IsModTimeDirty = b;
	}

	bool IsDirty() const
	{
		return IsDBFieldIDDirty ||
			IsTransformation_IDDirty ||
			IsKeyFieldsDirty ||
			IsKeyFieldsTypeDirty ||
			IsInsertTimeDirty ||
			IsModTimeDirty;
	}

	int field_DBFieldID;
	bool IsDBFieldIDNull;
	bool IsDBFieldIDDirty;

	int field_Transformation_ID;
	bool IsTransformation_IDNull;
	bool IsTransformation_IDDirty;

	std::string field_KeyFields;
	bool IsKeyFieldsNull;
	bool IsKeyFieldsDirty;

	int field_KeyFieldsType;
	bool IsKeyFieldsTypeNull;
	bool IsKeyFieldsTypeDirty;

	TIMESTAMP_STRUCT field_InsertTime;
	bool IsInsertTimeNull;
	bool IsInsertTimeDirty;

	TIMESTAMP_STRUCT field_ModTime;
	bool IsModTimeNull;
	bool IsModTimeDirty;


private:
	bool IsDBFieldIDPrimaryKey;
	bool IsTransformation_IDPrimaryKey;
	bool IsKeyFieldsPrimaryKey;
	bool IsKeyFieldsTypePrimaryKey;
	bool IsInsertTimePrimaryKey;
	bool IsModTimePrimaryKey;

};


class DBFieldsTab : public konga::DbRecordSet {
public:
	DBFieldsTab() {}

	static DBFieldsTab* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "DBFieldsTab";
	}

	void DoFieldExchange(const DBFieldsTabRow& row) {
		m_row.Copy(row);
	}

	void Edit();

	std::string GetUpdateFilter() {
		return m_stUpdateFilter;
	}

	int GetDBFieldID() {
		if( m_row.IsDBFieldIDNull )
			throw konga::DbNullException("DBFieldID");
		return m_row.field_DBFieldID;
	}
	void SetDBFieldID(int i) {
		m_row.field_DBFieldID = i;
		m_row.IsDBFieldIDNull = false;
		m_row.IsDBFieldIDDirty = true;
	}
	bool IsDBFieldIDNull() const {
		return m_row.IsDBFieldIDNull;
	}
	void SetDBFieldIDToNull() {
		m_row.IsDBFieldIDNull = true;
		m_row.IsDBFieldIDDirty = true;
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

	std::string GetKeyFields() {
		if( m_row.IsKeyFieldsNull )
			throw konga::DbNullException("KeyFields");
		return m_row.field_KeyFields;
	}
	void SetKeyFields(const std::string& st) {
		m_row.field_KeyFields = st;
		m_row.IsKeyFieldsNull = false;
		m_row.IsKeyFieldsDirty = true;
	}
	bool IsKeyFieldsNull() const {
		return m_row.IsKeyFieldsNull;
	}
	void SetKeyFieldsToNull() {
		m_row.field_KeyFields = "";
		m_row.IsKeyFieldsNull = true;
		m_row.IsKeyFieldsDirty = true;
	}

	int GetKeyFieldsType() {
		if( m_row.IsKeyFieldsTypeNull )
			throw konga::DbNullException("KeyFieldsType");
		return m_row.field_KeyFieldsType;
	}
	void SetKeyFieldsType(int i) {
		m_row.field_KeyFieldsType = i;
		m_row.IsKeyFieldsTypeNull = false;
		m_row.IsKeyFieldsTypeDirty = true;
	}
	bool IsKeyFieldsTypeNull() const {
		return m_row.IsKeyFieldsTypeNull;
	}
	void SetKeyFieldsTypeToNull() {
		m_row.IsKeyFieldsTypeNull = true;
		m_row.IsKeyFieldsTypeDirty = true;
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
	DBFieldsTabRow m_row;

private:
	bool IsDBFieldIDDirty() const {
		return m_row.IsDBFieldIDDirty;
	};
	bool IsTransformation_IDDirty() const {
		return m_row.IsTransformation_IDDirty;
	};
	bool IsKeyFieldsDirty() const {
		return m_row.IsKeyFieldsDirty;
	};
	bool IsKeyFieldsTypeDirty() const {
		return m_row.IsKeyFieldsTypeDirty;
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
