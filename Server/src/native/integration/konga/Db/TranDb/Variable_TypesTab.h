/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_CRTRANDB_VARIABLE_TYPESTAB_H
#define KONGA_DB_CRTRANDB_VARIABLE_TYPESTAB_H

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

class Variable_TypesTabRow
{
public:
	Variable_TypesTabRow() : 
			field_Var_Type_ID(0),
			IsVar_Type_IDNull(true),
			IsVar_Type_IDPrimaryKey(true),
			IsVar_Type_IDDirty(false),
			IsVar_TypeNull(true),
			IsVar_TypePrimaryKey(false),
			IsVar_TypeDirty(false),
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

	void Copy(const Variable_TypesTabRow& r)
	{
		field_Var_Type_ID = r.field_Var_Type_ID;
		IsVar_Type_IDNull = r.IsVar_Type_IDNull;
		IsVar_Type_IDDirty = r.IsVar_Type_IDDirty;

		field_Var_Type = r.field_Var_Type;
		IsVar_TypeNull = r.IsVar_TypeNull;
		IsVar_TypeDirty = r.IsVar_TypeDirty;

		field_InsertTime = r.field_InsertTime;
		IsInsertTimeNull = r.IsInsertTimeNull;
		IsInsertTimeDirty = r.IsInsertTimeDirty;

		field_ModTime = r.field_ModTime;
		IsModTimeNull = r.IsModTimeNull;
		IsModTimeDirty = r.IsModTimeDirty;

	}

	void CopyPKs(const Variable_TypesTabRow& r)
	{
		field_Var_Type_ID = r.field_Var_Type_ID;
		IsVar_Type_IDNull = r.IsVar_Type_IDNull;
		IsVar_Type_IDDirty = r.IsVar_Type_IDDirty;

	}

	void SetDirty(bool b)
	{
		IsVar_Type_IDDirty = b;
		IsVar_TypeDirty = b;
		IsInsertTimeDirty = b;
		IsModTimeDirty = b;
	}

	bool IsDirty() const
	{
		return IsVar_Type_IDDirty ||
			IsVar_TypeDirty ||
			IsInsertTimeDirty ||
			IsModTimeDirty;
	}

	int field_Var_Type_ID;
	bool IsVar_Type_IDNull;
	bool IsVar_Type_IDDirty;

	std::string field_Var_Type;
	bool IsVar_TypeNull;
	bool IsVar_TypeDirty;

	TIMESTAMP_STRUCT field_InsertTime;
	bool IsInsertTimeNull;
	bool IsInsertTimeDirty;

	TIMESTAMP_STRUCT field_ModTime;
	bool IsModTimeNull;
	bool IsModTimeDirty;


private:
	bool IsVar_Type_IDPrimaryKey;
	bool IsVar_TypePrimaryKey;
	bool IsInsertTimePrimaryKey;
	bool IsModTimePrimaryKey;

};


class Variable_TypesTab : public konga::DbRecordSet {
public:
	Variable_TypesTab() {}

	static Variable_TypesTab* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "Variable_TypesTab";
	}

	void DoFieldExchange(const Variable_TypesTabRow& row) {
		m_row.Copy(row);
	}

	void Edit();

	std::string GetUpdateFilter() {
		return m_stUpdateFilter;
	}

	int GetVar_Type_ID() {
		if( m_row.IsVar_Type_IDNull )
			throw konga::DbNullException("Var_Type_ID");
		return m_row.field_Var_Type_ID;
	}
	void SetVar_Type_ID(int i) {
		m_row.field_Var_Type_ID = i;
		m_row.IsVar_Type_IDNull = false;
		m_row.IsVar_Type_IDDirty = true;
	}
	bool IsVar_Type_IDNull() const {
		return m_row.IsVar_Type_IDNull;
	}
	void SetVar_Type_IDToNull() {
		m_row.IsVar_Type_IDNull = true;
		m_row.IsVar_Type_IDDirty = true;
	}

	std::string GetVar_Type() {
		if( m_row.IsVar_TypeNull )
			throw konga::DbNullException("Var_Type");
		return m_row.field_Var_Type;
	}
	void SetVar_Type(const std::string& st) {
		m_row.field_Var_Type = st;
		m_row.IsVar_TypeNull = false;
		m_row.IsVar_TypeDirty = true;
	}
	bool IsVar_TypeNull() const {
		return m_row.IsVar_TypeNull;
	}
	void SetVar_TypeToNull() {
		m_row.field_Var_Type = "";
		m_row.IsVar_TypeNull = true;
		m_row.IsVar_TypeDirty = true;
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
	Variable_TypesTabRow m_row;

private:
	bool IsVar_Type_IDDirty() const {
		return m_row.IsVar_Type_IDDirty;
	};
	bool IsVar_TypeDirty() const {
		return m_row.IsVar_TypeDirty;
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
