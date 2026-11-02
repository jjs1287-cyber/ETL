/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_TRANDB_TEMPLATE_SQLSCRIPTTAB_H
#define KONGA_DB_TRANDB_TEMPLATE_SQLSCRIPTTAB_H

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

class SqlScriptTabRow
{
public:
	SqlScriptTabRow() : 
			field_SqlId(0),
			IsSqlIdNull(true),
			IsSqlIdPrimaryKey(true),
			IsSqlIdDirty(false),

			IsTransformation_IdNull(true),
			IsTransformation_IdPrimaryKey(false),
			IsTransformation_IdDirty(false),

			IsSqlScriptNull(true),
			IsSqlScriptPrimaryKey(false),
			IsSqlScriptDirty(false),

			IsSqlScriptTypeNull(true),
			IsSqlScriptTypePrimaryKey(false),
			IsSqlScriptTypeDirty(false),

			IsSqlWhereNull(true),
			IsSqlWherePrimaryKey(false),
			IsSqlWhereDirty(false)

	{
	}

	void Copy(const SqlScriptTabRow& r)
	{
		field_SqlId = r.field_SqlId;
		IsSqlIdNull = r.IsSqlIdNull;
		IsSqlIdDirty = r.IsSqlIdDirty;

		field_Transformation_Id = r.field_Transformation_Id;
		IsTransformation_IdNull = r.IsTransformation_IdNull;
		IsTransformation_IdDirty = r.IsTransformation_IdDirty;

		field_SqlScript = r.field_SqlScript;
		IsSqlScriptNull = r.IsSqlScriptNull;
		IsSqlScriptDirty = r.IsSqlScriptDirty;

		field_SqlScriptType = r.field_SqlScriptType;
		IsSqlScriptTypeNull = r.IsSqlScriptTypeNull;
		IsSqlScriptTypeDirty = r.IsSqlScriptTypeDirty;

		field_SqlWhere = r.field_SqlWhere;
		IsSqlWhereNull = r.IsSqlWhereNull;
		IsSqlWhereDirty = r.IsSqlWhereDirty;
	}

	void CopyPKs(const SqlScriptTabRow& r)
	{
		field_SqlId = r.field_SqlId;
		IsSqlIdNull = r.IsSqlIdNull;
		IsSqlIdDirty = r.IsSqlIdDirty;
	}

	void SetDirty(bool b)
	{
		IsSqlIdDirty = b;
		IsTransformation_IdDirty = b;
		IsSqlScriptDirty = b;
		IsSqlScriptTypeDirty = b;
		IsSqlWhereDirty = b;
	}

	bool IsDirty() const
	{
		return IsSqlIdDirty ||
			IsTransformation_IdDirty ||
			IsSqlScriptDirty ||
			IsSqlScriptTypeDirty ||
			IsSqlWhereDirty;
	}

	int field_SqlId;
	bool IsSqlIdNull;
	bool IsSqlIdDirty;

	int field_Transformation_Id;
	bool IsTransformation_IdNull;
	bool IsTransformation_IdDirty;

	std::string field_SqlScript;
	bool IsSqlScriptNull;
	bool IsSqlScriptDirty;

	int field_SqlScriptType;
	bool IsSqlScriptTypeNull;
	bool IsSqlScriptTypeDirty;

	std::string field_SqlWhere;
	bool IsSqlWhereNull;
	bool IsSqlWhereDirty;

private:
	bool IsSqlIdPrimaryKey;
	bool IsTransformation_IdPrimaryKey;
	bool IsSqlScriptPrimaryKey;
	bool IsSqlScriptTypePrimaryKey;
	bool IsSqlWherePrimaryKey;
};


class SqlScriptTab : public konga::DbRecordSet {
public:
	SqlScriptTab() {}

	static SqlScriptTab* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "SqlScriptTab";
	}

	void DoFieldExchange(const SqlScriptTabRow& row) {
		m_row.Copy(row);
	}

	void Edit();

	std::string GetUpdateFilter() {
		return m_stUpdateFilter;
	}

	int GetSqlId() {
		if( m_row.IsSqlIdNull )
			throw konga::DbNullException("SqlId");
		return m_row.field_SqlId;
	}
	void SetSqlId(int i) {
		m_row.field_SqlId = i;
		m_row.IsSqlIdNull = false;
		m_row.IsSqlIdDirty = true;
	}
	bool IsSqlIdNull() const {
		return m_row.IsSqlIdNull;
	}
	void SetSqlIdToNull() {
		m_row.IsSqlIdNull = true;
		m_row.IsSqlIdDirty = true;
	}

	int GetTransformation_Id() {
		if( m_row.IsTransformation_IdNull )
			throw konga::DbNullException("Transformation_ID");
		return m_row.field_Transformation_Id;
	}
	void SetTransformation_Id(int i) {
		m_row.field_Transformation_Id = i;
		m_row.IsTransformation_IdNull = false;
		m_row.IsTransformation_IdDirty = true;
	}
	bool IsTransformation_IdNull() const {
		return m_row.IsTransformation_IdNull;
	}
	void SetTransformation_IdToNull() {
		m_row.IsTransformation_IdNull = true;
		m_row.IsTransformation_IdDirty = true;
	}

	std::string GetSqlScript() {
		if( m_row.IsSqlScriptNull )
			throw konga::DbNullException("SqlScript");
		return m_row.field_SqlScript;
	}
	void SetSqlScript(const std::string& st) {
		m_row.field_SqlScript = st;
		m_row.IsSqlScriptNull = false;
		m_row.IsSqlScriptDirty = true;
	}
	bool IsSqlScriptNull() const {
		return m_row.IsSqlScriptNull;
	}
	void SetSqlScriptToNull() {
		m_row.field_SqlScript = "";
		m_row.IsSqlScriptNull = true;
		m_row.IsSqlScriptDirty = true;
	}

	int GetSqlScriptType() {
		if( m_row.IsSqlScriptTypeNull )
			throw konga::DbNullException("SqlScriptType");
		return m_row.field_SqlScriptType;
	}
	void SetSqlScript(int i) {
		m_row.field_SqlScriptType = i;
		m_row.IsSqlScriptTypeNull = false;
		m_row.IsSqlScriptTypeDirty = true;
	}
	bool IsSqlScriptTypeNull() const {
		return m_row.IsSqlScriptTypeNull;
	}
	void SetSqlScriptTypeToNull() {
		m_row.field_SqlScriptType = 0;
		m_row.IsSqlScriptTypeNull = true;
		m_row.IsSqlScriptTypeDirty = true;
	}

	std::string GetSqlWhere() {
		if( m_row.IsSqlWhereNull )
			throw konga::DbNullException("SqlWhere");
		return m_row.field_SqlWhere;
	}
	void SetSqlWhere(const std::string& st) {
		m_row.field_SqlWhere = st;
		m_row.IsSqlWhereNull = false;
		m_row.IsSqlWhereDirty = true;
	}
	bool IsSqlWhereNull() const {
		return m_row.IsSqlWhereNull;
	}
	void SetSqlWhereToNull() {
		m_row.field_SqlWhere = "";
		m_row.IsSqlWhereNull = true;
		m_row.IsSqlWhereDirty = true;
	}

protected:
	SqlScriptTabRow m_row;

private:
	bool IsSqlIdDirty() const {
		return m_row.IsSqlIdDirty;
	};
	bool IsSqlScriptDirty() const {
		return m_row.IsSqlScriptDirty;
	};
	bool IsSqlWhereDirty() const {
		return m_row.IsSqlWhereDirty;
	};

	std::string m_stUpdateFilter;

};

}                         


#endif                                           

