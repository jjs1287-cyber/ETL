/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_CRPDB_FUNCTIONTAB_H
#define KONGA_DB_CRPDB_FUNCTIONTAB_H

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

class FunctionTabRow
{
public:
	FunctionTabRow() : 
			field_ID(0),
			IsIDNull(true),
			IsIDPrimaryKey(true),
			IsIDDirty(false),
			IsFunctionNull(true),
			IsFunctionPrimaryKey(false),
			IsFunctionDirty(false),
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

	void Copy(const FunctionTabRow& r)
	{
		field_ID = r.field_ID;
		IsIDNull = r.IsIDNull;
		IsIDDirty = r.IsIDDirty;

		field_Function = r.field_Function;
		IsFunctionNull = r.IsFunctionNull;
		IsFunctionDirty = r.IsFunctionDirty;

		field_InsertTime = r.field_InsertTime;
		IsInsertTimeNull = r.IsInsertTimeNull;
		IsInsertTimeDirty = r.IsInsertTimeDirty;

		field_ModTime = r.field_ModTime;
		IsModTimeNull = r.IsModTimeNull;
		IsModTimeDirty = r.IsModTimeDirty;

	}

	void CopyPKs(const FunctionTabRow& r)
	{
		field_ID = r.field_ID;
		IsIDNull = r.IsIDNull;
		IsIDDirty = r.IsIDDirty;

	}

	void SetDirty(bool b)
	{
		IsIDDirty = b;
		IsFunctionDirty = b;
		IsInsertTimeDirty = b;
		IsModTimeDirty = b;
	}

	bool IsDirty() const
	{
		return IsIDDirty ||
			IsFunctionDirty ||
			IsInsertTimeDirty ||
			IsModTimeDirty;
	}

	int field_ID;
	bool IsIDNull;
	bool IsIDDirty;

	std::string field_Function;
	bool IsFunctionNull;
	bool IsFunctionDirty;

	TIMESTAMP_STRUCT field_InsertTime;
	bool IsInsertTimeNull;
	bool IsInsertTimeDirty;

	TIMESTAMP_STRUCT field_ModTime;
	bool IsModTimeNull;
	bool IsModTimeDirty;


private:
	bool IsIDPrimaryKey;
	bool IsFunctionPrimaryKey;
	bool IsInsertTimePrimaryKey;
	bool IsModTimePrimaryKey;

};


class FunctionTab : public konga::DbRecordSet {
public:
	FunctionTab() {}

	static FunctionTab* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "FunctionTab";
	}

	void DoFieldExchange(const FunctionTabRow& row) {
		m_row.Copy(row);
	}

	void Edit();

	std::string GetUpdateFilter() {
		return m_stUpdateFilter;
	}

	int GetID() {
		if( m_row.IsIDNull )
			throw konga::DbNullException("ID");
		return m_row.field_ID;
	}
	void SetID(int i) {
		m_row.field_ID = i;
		m_row.IsIDNull = false;
		m_row.IsIDDirty = true;
	}
	bool IsIDNull() const {
		return m_row.IsIDNull;
	}
	void SetIDToNull() {
		m_row.IsIDNull = true;
		m_row.IsIDDirty = true;
	}

	std::string GetFunction() {
		if( m_row.IsFunctionNull )
			throw konga::DbNullException("Function");
		return m_row.field_Function;
	}
	void SetFunction(const std::string& st) {
		m_row.field_Function = st;
		m_row.IsFunctionNull = false;
		m_row.IsFunctionDirty = true;
	}
	bool IsFunctionNull() const {
		return m_row.IsFunctionNull;
	}
	void SetFunctionToNull() {
		m_row.field_Function = "";
		m_row.IsFunctionNull = true;
		m_row.IsFunctionDirty = true;
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
	FunctionTabRow m_row;

private:
	bool IsIDDirty() const {
		return m_row.IsIDDirty;
	};
	bool IsFunctionDirty() const {
		return m_row.IsFunctionDirty;
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
