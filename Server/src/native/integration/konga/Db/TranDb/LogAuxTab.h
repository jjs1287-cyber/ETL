/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_CRTRANDB_LOGAUXTAB_H
#define KONGA_DB_CRTRANDB_LOGAUXTAB_H

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

class LogAuxTabRow
{
public:
	LogAuxTabRow() : 
			field_LogAuxID(0),
			IsLogAuxIDNull(true),
			IsLogAuxIDPrimaryKey(true),
			IsLogAuxIDDirty(false),
			field_Log_ID(0),
			IsLog_IDNull(true),
			IsLog_IDPrimaryKey(false),
			IsLog_IDDirty(false),
			IsField_NameNull(true),
			IsField_NamePrimaryKey(false),
			IsField_NameDirty(false),
			IsLong_MessageNull(true),
			IsLong_MessagePrimaryKey(false),
			IsLong_MessageDirty(false),
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

	void Copy(const LogAuxTabRow& r)
	{
		field_LogAuxID = r.field_LogAuxID;
		IsLogAuxIDNull = r.IsLogAuxIDNull;
		IsLogAuxIDDirty = r.IsLogAuxIDDirty;

		field_Log_ID = r.field_Log_ID;
		IsLog_IDNull = r.IsLog_IDNull;
		IsLog_IDDirty = r.IsLog_IDDirty;

		field_Field_Name = r.field_Field_Name;
		IsField_NameNull = r.IsField_NameNull;
		IsField_NameDirty = r.IsField_NameDirty;

		field_Long_Message = r.field_Long_Message;
		IsLong_MessageNull = r.IsLong_MessageNull;
		IsLong_MessageDirty = r.IsLong_MessageDirty;

		field_InsertTime = r.field_InsertTime;
		IsInsertTimeNull = r.IsInsertTimeNull;
		IsInsertTimeDirty = r.IsInsertTimeDirty;

		field_ModTime = r.field_ModTime;
		IsModTimeNull = r.IsModTimeNull;
		IsModTimeDirty = r.IsModTimeDirty;

	}

	void CopyPKs(const LogAuxTabRow& r)
	{
		field_LogAuxID = r.field_LogAuxID;
		IsLogAuxIDNull = r.IsLogAuxIDNull;
		IsLogAuxIDDirty = r.IsLogAuxIDDirty;

	}

	void SetDirty(bool b)
	{
		IsLogAuxIDDirty = b;
		IsLog_IDDirty = b;
		IsField_NameDirty = b;
		IsLong_MessageDirty = b;
		IsInsertTimeDirty = b;
		IsModTimeDirty = b;
	}

	bool IsDirty() const
	{
		return IsLogAuxIDDirty ||
			IsLog_IDDirty ||
			IsField_NameDirty ||
			IsLong_MessageDirty ||
			IsInsertTimeDirty ||
			IsModTimeDirty;
	}

	int field_LogAuxID;
	bool IsLogAuxIDNull;
	bool IsLogAuxIDDirty;

	int field_Log_ID;
	bool IsLog_IDNull;
	bool IsLog_IDDirty;

	std::string field_Field_Name;
	bool IsField_NameNull;
	bool IsField_NameDirty;

	std::string field_Long_Message;
	bool IsLong_MessageNull;
	bool IsLong_MessageDirty;

	TIMESTAMP_STRUCT field_InsertTime;
	bool IsInsertTimeNull;
	bool IsInsertTimeDirty;

	TIMESTAMP_STRUCT field_ModTime;
	bool IsModTimeNull;
	bool IsModTimeDirty;


private:
	bool IsLogAuxIDPrimaryKey;
	bool IsLog_IDPrimaryKey;
	bool IsField_NamePrimaryKey;
	bool IsLong_MessagePrimaryKey;
	bool IsInsertTimePrimaryKey;
	bool IsModTimePrimaryKey;

};


class LogAuxTab : public konga::DbRecordSet {
public:
	LogAuxTab() {}

	static LogAuxTab* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "LogAuxTab";
	}

	void DoFieldExchange(const LogAuxTabRow& row) {
		m_row.Copy(row);
	}

	void Edit();

	std::string GetUpdateFilter() {
		return m_stUpdateFilter;
	}

	int GetLogAuxID() {
		if( m_row.IsLogAuxIDNull )
			throw konga::DbNullException("LogAuxID");
		return m_row.field_LogAuxID;
	}
	void SetLogAuxID(int i) {
		m_row.field_LogAuxID = i;
		m_row.IsLogAuxIDNull = false;
		m_row.IsLogAuxIDDirty = true;
	}
	bool IsLogAuxIDNull() const {
		return m_row.IsLogAuxIDNull;
	}
	void SetLogAuxIDToNull() {
		m_row.IsLogAuxIDNull = true;
		m_row.IsLogAuxIDDirty = true;
	}

	int GetLog_ID() {
		if( m_row.IsLog_IDNull )
			throw konga::DbNullException("Log_ID");
		return m_row.field_Log_ID;
	}
	void SetLog_ID(int i) {
		m_row.field_Log_ID = i;
		m_row.IsLog_IDNull = false;
		m_row.IsLog_IDDirty = true;
	}
	bool IsLog_IDNull() const {
		return m_row.IsLog_IDNull;
	}
	void SetLog_IDToNull() {
		m_row.IsLog_IDNull = true;
		m_row.IsLog_IDDirty = true;
	}

	std::string GetField_Name() {
		if( m_row.IsField_NameNull )
			throw konga::DbNullException("Field_Name");
		return m_row.field_Field_Name;
	}
	void SetField_Name(const std::string& st) {
		m_row.field_Field_Name = st;
		m_row.IsField_NameNull = false;
		m_row.IsField_NameDirty = true;
	}
	bool IsField_NameNull() const {
		return m_row.IsField_NameNull;
	}
	void SetField_NameToNull() {
		m_row.field_Field_Name = "";
		m_row.IsField_NameNull = true;
		m_row.IsField_NameDirty = true;
	}

	std::string GetLong_Message() {
		if( m_row.IsLong_MessageNull )
			throw konga::DbNullException("Long_Message");
		return m_row.field_Long_Message;
	}
	void SetLong_Message(const std::string& st) {
		m_row.field_Long_Message = st;
		m_row.IsLong_MessageNull = false;
		m_row.IsLong_MessageDirty = true;
	}
	bool IsLong_MessageNull() const {
		return m_row.IsLong_MessageNull;
	}
	void SetLong_MessageToNull() {
		m_row.field_Long_Message = "";
		m_row.IsLong_MessageNull = true;
		m_row.IsLong_MessageDirty = true;
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
	LogAuxTabRow m_row;

private:
	bool IsLogAuxIDDirty() const {
		return m_row.IsLogAuxIDDirty;
	};
	bool IsLog_IDDirty() const {
		return m_row.IsLog_IDDirty;
	};
	bool IsField_NameDirty() const {
		return m_row.IsField_NameDirty;
	};
	bool IsLong_MessageDirty() const {
		return m_row.IsLong_MessageDirty;
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
