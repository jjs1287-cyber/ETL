/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_TRANDB_TEMPLATE_SCRIPTSTAB_H
#define KONGA_DB_TRANDB_TEMPLATE_SCRIPTSTAB_H

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

class ScriptsTabRow
{
public:
	ScriptsTabRow() : 
			field_Script_ID(0),
			IsScript_IDNull(true),
			IsScript_IDPrimaryKey(true),
			IsScript_IDDirty(false),
			IsScript_NameNull(true),
			IsScript_NamePrimaryKey(false),
			IsScript_NameDirty(false),
			IsScript_TextNull(true),
			IsScript_TextPrimaryKey(false),
			IsScript_TextDirty(false)
	{
	}

	void Copy(const ScriptsTabRow& r)
	{
		field_Script_ID = r.field_Script_ID;
		IsScript_IDNull = r.IsScript_IDNull;
		IsScript_IDDirty = r.IsScript_IDDirty;

		field_Script_Name = r.field_Script_Name;
		IsScript_NameNull = r.IsScript_NameNull;
		IsScript_NameDirty = r.IsScript_NameDirty;

		field_Script_Text = r.field_Script_Text;
		IsScript_TextNull = r.IsScript_TextNull;
		IsScript_TextDirty = r.IsScript_TextDirty;
	}

	void CopyPKs(const ScriptsTabRow& r)
	{
		field_Script_ID = r.field_Script_ID;
		IsScript_IDNull = r.IsScript_IDNull;
		IsScript_IDDirty = r.IsScript_IDDirty;
	}

	void SetDirty(bool b)
	{
		IsScript_IDDirty = b;
		IsScript_NameDirty = b;
		IsScript_TextDirty = b;
	}

	bool IsDirty() const
	{
		return IsScript_IDDirty ||
			IsScript_NameDirty ||
			IsScript_TextDirty;
	}

	int field_Script_ID;
	bool IsScript_IDNull;
	bool IsScript_IDDirty;

	std::string field_Script_Name;
	bool IsScript_NameNull;
	bool IsScript_NameDirty;

	std::string field_Script_Text;
	bool IsScript_TextNull;
	bool IsScript_TextDirty;

private:
	bool IsScript_IDPrimaryKey;
	bool IsScript_NamePrimaryKey;
	bool IsScript_TextPrimaryKey;
};


class ScriptsTab : public konga::DbRecordSet {
public:
	ScriptsTab() {}

	static ScriptsTab* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "ScriptsTab";
	}

	void DoFieldExchange(const ScriptsTabRow& row) {
		m_row.Copy(row);
	}

	void Edit();

	std::string GetUpdateFilter() {
		return m_stUpdateFilter;
	}

	int GetScript_ID() {
		if( m_row.IsScript_IDNull )
			throw konga::DbNullException("Script_ID");
		return m_row.field_Script_ID;
	}
	void SetScript_ID(int i) {
		m_row.field_Script_ID = i;
		m_row.IsScript_IDNull = false;
		m_row.IsScript_IDDirty = true;
	}
	bool IsScript_IDNull() const {
		return m_row.IsScript_IDNull;
	}
	void SetScript_IDToNull() {
		m_row.IsScript_IDNull = true;
		m_row.IsScript_IDDirty = true;
	}

	std::string GetScript_Name() {
		if( m_row.IsScript_NameNull )
			throw konga::DbNullException("Script_Name");
		return m_row.field_Script_Name;
	}
	void SetScript_Name(const std::string& st) {
		m_row.field_Script_Name = st;
		m_row.IsScript_NameNull = false;
		m_row.IsScript_NameDirty = true;
	}
	bool IsScript_NameNull() const {
		return m_row.IsScript_NameNull;
	}
	void SetScript_NameToNull() {
		m_row.field_Script_Name = "";
		m_row.IsScript_NameNull = true;
		m_row.IsScript_NameDirty = true;
	}

	std::string GetScript_Text() {
		if( m_row.IsScript_TextNull )
			throw konga::DbNullException("Script_Text");
		return m_row.field_Script_Text;
	}
	void SetScript_Text(const std::string& st) {
		m_row.field_Script_Text = st;
		m_row.IsScript_TextNull = false;
		m_row.IsScript_TextDirty = true;
	}
	bool IsScript_TextNull() const {
		return m_row.IsScript_TextNull;
	}
	void SetScript_TextToNull() {
		m_row.field_Script_Text = "";
		m_row.IsScript_TextNull = true;
		m_row.IsScript_TextDirty = true;
	}

protected:
	ScriptsTabRow m_row;

private:
	bool IsScript_IDDirty() const {
		return m_row.IsScript_IDDirty;
	};
	bool IsScript_NameDirty() const {
		return m_row.IsScript_NameDirty;
	};
	bool IsScript_TextDirty() const {
		return m_row.IsScript_TextDirty;
	};

	std::string m_stUpdateFilter;

};

}                         


#endif                                         

