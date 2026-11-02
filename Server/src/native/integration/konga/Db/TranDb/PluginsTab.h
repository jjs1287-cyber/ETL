/*
 * Copyright (c) 1998-2006 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_TRANDB_PluginsTab_H
#define KONGA_DB_TRANDB_PluginsTab_H

#ifdef WIN32
	#include <windows.h>
#endif
#include <string>

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/DbRecordSet.h>
#include <konga/Db/DbUtils/DbNullException.h>

namespace TranDb
{

class PluginsTabRow
{
public:
	PluginsTabRow() : 
			Field_PluginId(0),
			IsPluginIdNull(true),
			IsPluginIdPrimaryKey(true),
			IsPluginIdDirty(false),
			IsPluginNameNull(true),
			IsPluginNamePrimaryKey(false),
			IsPluginNameDirty(false),
			IsPluginPathNull(true),
			IsPluginPathPrimaryKey(false),
			IsPluginPathDirty(false)
	{
	}

	void Copy(const PluginsTabRow& r)
	{
		Field_PluginId = r.Field_PluginId;
		IsPluginIdNull = r.IsPluginIdNull;
		IsPluginIdDirty = r.IsPluginIdDirty;

		Field_PluginName = r.Field_PluginName;
		IsPluginNameNull = r.IsPluginNameNull;
		IsPluginNameDirty = r.IsPluginNameDirty;

		Field_PluginPath = r.Field_PluginPath;
		IsPluginPathNull = r.IsPluginPathNull;
		IsPluginPathDirty = r.IsPluginPathDirty;

	}

	void CopyPKs(const PluginsTabRow& r)
	{
		Field_PluginId = r.Field_PluginId;
		IsPluginIdNull = r.IsPluginIdNull;
		IsPluginIdDirty = r.IsPluginIdDirty;

	}

	void SetDirty(bool b)
	{
		IsPluginIdDirty = b;
		IsPluginNameDirty = b;
		IsPluginPathDirty = b;
	}

	bool IsDirty() const
	{
		return IsPluginIdDirty ||
			IsPluginNameDirty ||
			IsPluginPathDirty;
	}

	int Field_PluginId;
	bool IsPluginIdNull;
	bool IsPluginIdDirty;

	std::string Field_PluginName;
	bool IsPluginNameNull;
	bool IsPluginNameDirty;

	std::string Field_PluginPath;
	bool IsPluginPathNull;
	bool IsPluginPathDirty;


private:
	bool IsPluginIdPrimaryKey;
	bool IsPluginNamePrimaryKey;
	bool IsPluginPathPrimaryKey;

};


class PluginsTab : public konga::DbRecordSet {
public:
	PluginsTab() {}

	static PluginsTab* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "PluginsTab";
	}

	void DoFieldExchange(const PluginsTabRow& row) {
		m_row.Copy(row);
	}

	void Edit();

	std::string GetUpdateFilter() {
		return m_stUpdateFilter;
	}

	int GetPluginId() {
		if( m_row.IsPluginIdNull )
			throw konga::DbNullException("PluginId");
		return m_row.Field_PluginId;
	}
	void SetPluginId(int i) {
		m_row.Field_PluginId = i;
		m_row.IsPluginIdNull = false;
		m_row.IsPluginIdDirty = true;
	}
	bool IsPluginIdNull() const {
		return m_row.IsPluginIdNull;
	}
	void SetPluginIdToNull() {
		m_row.IsPluginIdNull = true;
		m_row.IsPluginIdDirty = true;
	}

	std::string GetPluginName() {
		if( m_row.IsPluginNameNull )
			throw konga::DbNullException("PluginName");
		return m_row.Field_PluginName;
	}
	void SetPluginName(const std::string& st) {
		m_row.Field_PluginName = st;
		m_row.IsPluginNameNull = false;
		m_row.IsPluginNameDirty = true;
	}
	bool IsPluginNameNull() const {
		return m_row.IsPluginNameNull;
	}
	void SetPluginNameToNull() {
		m_row.Field_PluginName = "";
		m_row.IsPluginNameNull = true;
		m_row.IsPluginNameDirty = true;
	}

	std::string GetPluginPath() {
		if( m_row.IsPluginPathNull )
			throw konga::DbNullException("PluginPath");
		return m_row.Field_PluginPath;
	}
	void SetPluginPath(const std::string& st) {
		m_row.Field_PluginPath = st;
		m_row.IsPluginPathNull = false;
		m_row.IsPluginPathDirty = true;
	}
	bool IsPluginPathNull() const {
		return m_row.IsPluginPathNull;
	}
	void SetPluginPathToNull() {
		m_row.Field_PluginPath = "";
		m_row.IsPluginPathNull = true;
		m_row.IsPluginPathDirty = true;
	}

protected:
	PluginsTabRow m_row;

private:
	bool IsPluginIdDirty() const {
		return m_row.IsPluginIdDirty;
	};
	bool IsPluginNameDirty() const {
		return m_row.IsPluginNameDirty;
	};
	bool IsPluginPathDirty() const {
		return m_row.IsPluginPathDirty;
	};


	std::string m_stUpdateFilter;

};

}                         


#endif
