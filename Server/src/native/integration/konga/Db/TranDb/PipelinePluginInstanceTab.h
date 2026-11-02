/*
 * Copyright (c) 1998-2006 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_TRANDB_PipelinePluginInstanceTab_H
#define KONGA_DB_TRANDB_PipelinePluginInstanceTab_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/DbRecordSet.h>
#include <konga/Db/DbUtils/DbNullException.h>

namespace TranDb
{

class PipelinePluginInstanceTabRow
{
public:
	PipelinePluginInstanceTabRow() : 
			Field_PipelinePluginInstanceId(0),
			IsPipelinePluginInstanceIdNull(true),
			IsPipelinePluginInstanceIdPrimaryKey(true),
			IsPipelinePluginInstanceIdDirty(false),
			Field_PluginId(0),
			IsPluginIdNull(true),
			IsPluginIdPrimaryKey(false),
			IsPluginIdDirty(false),
			Field_EntityTypeId(0),
			IsEntityTypeIdNull(true),
			IsEntityTypeIdPrimaryKey(false),
			IsEntityTypeIdDirty(false),
			Field_EntityId(0),
			IsEntityIdNull(true),
			IsEntityIdPrimaryKey(false),
			IsEntityIdDirty(false)
	{
	}

	void Copy(const PipelinePluginInstanceTabRow& r)
	{
		Field_PipelinePluginInstanceId = r.Field_PipelinePluginInstanceId;
		IsPipelinePluginInstanceIdNull = r.IsPipelinePluginInstanceIdNull;
		IsPipelinePluginInstanceIdDirty = r.IsPipelinePluginInstanceIdDirty;

		Field_PluginId = r.Field_PluginId;
		IsPluginIdNull = r.IsPluginIdNull;
		IsPluginIdDirty = r.IsPluginIdDirty;

		Field_EntityTypeId = r.Field_EntityTypeId;
		IsEntityTypeIdNull = r.IsEntityTypeIdNull;
		IsEntityTypeIdDirty = r.IsEntityTypeIdDirty;

		Field_EntityId = r.Field_EntityId;
		IsEntityIdNull = r.IsEntityIdNull;
		IsEntityIdDirty = r.IsEntityIdDirty;

	}

	void CopyPKs(const PipelinePluginInstanceTabRow& r)
	{
		Field_PipelinePluginInstanceId = r.Field_PipelinePluginInstanceId;
		IsPipelinePluginInstanceIdNull = r.IsPipelinePluginInstanceIdNull;
		IsPipelinePluginInstanceIdDirty = r.IsPipelinePluginInstanceIdDirty;

	}

	void SetDirty(bool b)
	{
		IsPipelinePluginInstanceIdDirty = b;
		IsPluginIdDirty = b;
		IsEntityTypeIdDirty = b;
		IsEntityIdDirty = b;
	}

	bool IsDirty() const
	{
		return IsPipelinePluginInstanceIdDirty ||
			IsPluginIdDirty ||
			IsEntityTypeIdDirty ||
			IsEntityIdDirty;
	}

	int Field_PipelinePluginInstanceId;
	bool IsPipelinePluginInstanceIdNull;
	bool IsPipelinePluginInstanceIdDirty;

	int Field_PluginId;
	bool IsPluginIdNull;
	bool IsPluginIdDirty;

	int Field_EntityTypeId;
	bool IsEntityTypeIdNull;
	bool IsEntityTypeIdDirty;

	int Field_EntityId;
	bool IsEntityIdNull;
	bool IsEntityIdDirty;


private:
	bool IsPipelinePluginInstanceIdPrimaryKey;
	bool IsPluginIdPrimaryKey;
	bool IsEntityTypeIdPrimaryKey;
	bool IsEntityIdPrimaryKey;

};


class PipelinePluginInstanceTab : public konga::DbRecordSet {
public:
	PipelinePluginInstanceTab() {}

	static PipelinePluginInstanceTab* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "PipelinePluginInstanceTab";
	}

	void DoFieldExchange(const PipelinePluginInstanceTabRow& row) {
		m_row.Copy(row);
	}

	void Edit();

	std::string GetUpdateFilter() {
		return m_stUpdateFilter;
	}

	int GetPipelinePluginInstanceId() {
		if( m_row.IsPipelinePluginInstanceIdNull )
			throw konga::DbNullException("PipelinePluginInstanceId");
		return m_row.Field_PipelinePluginInstanceId;
	}
	void SetPipelinePluginInstanceId(int i) {
		m_row.Field_PipelinePluginInstanceId = i;
		m_row.IsPipelinePluginInstanceIdNull = false;
		m_row.IsPipelinePluginInstanceIdDirty = true;
	}
	bool IsPipelinePluginInstanceIdNull() const {
		return m_row.IsPipelinePluginInstanceIdNull;
	}
	void SetPipelinePluginInstanceIdToNull() {
		m_row.IsPipelinePluginInstanceIdNull = true;
		m_row.IsPipelinePluginInstanceIdDirty = true;
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

	int GetEntityTypeId() {
		if( m_row.IsEntityTypeIdNull )
			throw konga::DbNullException("EntityTypeId");
		return m_row.Field_EntityTypeId;
	}
	void SetEntityTypeId(int i) {
		m_row.Field_EntityTypeId = i;
		m_row.IsEntityTypeIdNull = false;
		m_row.IsEntityTypeIdDirty = true;
	}
	bool IsEntityTypeIdNull() const {
		return m_row.IsEntityTypeIdNull;
	}
	void SetEntityTypeIdToNull() {
		m_row.IsEntityTypeIdNull = true;
		m_row.IsEntityTypeIdDirty = true;
	}

	int GetEntityId() {
		if( m_row.IsEntityIdNull )
			throw konga::DbNullException("EntityId");
		return m_row.Field_EntityId;
	}
	void SetEntityId(int i) {
		m_row.Field_EntityId = i;
		m_row.IsEntityIdNull = false;
		m_row.IsEntityIdDirty = true;
	}
	bool IsEntityIdNull() const {
		return m_row.IsEntityIdNull;
	}
	void SetEntityIdToNull() {
		m_row.IsEntityIdNull = true;
		m_row.IsEntityIdDirty = true;
	}

protected:
	PipelinePluginInstanceTabRow m_row;

private:
	bool IsPipelinePluginInstanceIdDirty() const {
		return m_row.IsPipelinePluginInstanceIdDirty;
	};
	bool IsPluginIdDirty() const {
		return m_row.IsPluginIdDirty;
	};
	bool IsEntityTypeIdDirty() const {
		return m_row.IsEntityTypeIdDirty;
	};
	bool IsEntityIdDirty() const {
		return m_row.IsEntityIdDirty;
	};


	std::string m_stUpdateFilter;

};

}                         


#endif
