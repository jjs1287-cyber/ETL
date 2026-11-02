/*
 * Copyright (c) 1998-2006 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_TRANDB_PIPELINEPLUGINPOSITIONTab_H
#define KONGA_DB_TRANDB_PIPELINEPLUGINPOSITIONTab_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/DbRecordSet.h>
#include <konga/Db/DbUtils/DbNullException.h>

namespace TranDb
{

class PipelinePluginPositionTabRow
{
public:
	PipelinePluginPositionTabRow() : 
			Field_PipelinePluginPositionId(0),
			IsPipelinePluginPositionIdNull(true),
			IsPipelinePluginPositionIdPrimaryKey(true),
			IsPipelinePluginPositionIdDirty(false),
			Field_PluginId(0),
			IsPluginIdNull(true),
			IsPluginIdPrimaryKey(false),
			IsPluginIdDirty(false),
			Field_PipelinePluginInstanceId(0),
			IsPipelinePluginInstanceIdNull(true),
			IsPipelinePluginInstanceIdPrimaryKey(false),
			IsPipelinePluginInstanceIdDirty(false),
			Field_PipelineEntityTypeId(0),
			IsPipelineEntityTypeIdNull(true),
			IsPipelineEntityTypeIdPrimaryKey(false),
			IsPipelineEntityTypeIdDirty(false),
			Field_RelativePositionId(0),
			IsRelativePositionIdNull(true),
			IsRelativePositionIdPrimaryKey(false),
			IsRelativePositionIdDirty(false)
	{
	}

	void Copy(const PipelinePluginPositionTabRow& r)
	{
		Field_PipelinePluginPositionId = r.Field_PipelinePluginPositionId;
		IsPipelinePluginPositionIdNull = r.IsPipelinePluginPositionIdNull;
		IsPipelinePluginPositionIdDirty = r.IsPipelinePluginPositionIdDirty;

		Field_PluginId = r.Field_PluginId;
		IsPluginIdNull = r.IsPluginIdNull;
		IsPluginIdDirty = r.IsPluginIdDirty;

		Field_PipelinePluginInstanceId = r.Field_PipelinePluginInstanceId;
		IsPipelinePluginInstanceIdNull = r.IsPipelinePluginInstanceIdNull;
		IsPipelinePluginInstanceIdDirty = r.IsPipelinePluginInstanceIdDirty;

		Field_PipelineEntityTypeId = r.Field_PipelineEntityTypeId;
		IsPipelineEntityTypeIdNull = r.IsPipelineEntityTypeIdNull;
		IsPipelineEntityTypeIdDirty = r.IsPipelineEntityTypeIdDirty;

		Field_RelativePositionId = r.Field_RelativePositionId;
		IsRelativePositionIdNull = r.IsRelativePositionIdNull;
		IsRelativePositionIdDirty = r.IsRelativePositionIdDirty;

	}

	void CopyPKs(const PipelinePluginPositionTabRow& r)
	{
		Field_PipelinePluginPositionId = r.Field_PipelinePluginPositionId;
		IsPipelinePluginPositionIdNull = r.IsPipelinePluginPositionIdNull;
		IsPipelinePluginPositionIdDirty = r.IsPipelinePluginPositionIdDirty;

	}

	void SetDirty(bool b)
	{
		IsPipelinePluginPositionIdDirty = b;
		IsPluginIdDirty = b;
		IsPipelinePluginInstanceIdDirty = b;
		IsPipelineEntityTypeIdDirty = b;
		IsRelativePositionIdDirty = b;
	}

	bool IsDirty() const
	{
		return IsPipelinePluginPositionIdDirty ||
			IsPluginIdDirty ||
			IsPipelinePluginInstanceIdDirty ||
			IsPipelineEntityTypeIdDirty ||
			IsRelativePositionIdDirty;
	}

	int Field_PipelinePluginPositionId;
	bool IsPipelinePluginPositionIdNull;
	bool IsPipelinePluginPositionIdDirty;

	int Field_PluginId;
	bool IsPluginIdNull;
	bool IsPluginIdDirty;

	int Field_PipelinePluginInstanceId;
	bool IsPipelinePluginInstanceIdNull;
	bool IsPipelinePluginInstanceIdDirty;

	int Field_PipelineEntityTypeId;
	bool IsPipelineEntityTypeIdNull;
	bool IsPipelineEntityTypeIdDirty;

	int Field_RelativePositionId;
	bool IsRelativePositionIdNull;
	bool IsRelativePositionIdDirty;


private:
	bool IsPipelinePluginPositionIdPrimaryKey;
	bool IsPluginIdPrimaryKey;
	bool IsPipelinePluginInstanceIdPrimaryKey;
	bool IsPipelineEntityTypeIdPrimaryKey;
	bool IsRelativePositionIdPrimaryKey;

};


class PipelinePluginPositionTab : public konga::DbRecordSet {
public:
	PipelinePluginPositionTab() {}

	static PipelinePluginPositionTab* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "PipelinePluginPositionTab";
	}

	void DoFieldExchange(const PipelinePluginPositionTabRow& row) {
		m_row.Copy(row);
	}

	void Edit();

	std::string GetUpdateFilter() {
		return m_stUpdateFilter;
	}

	int GetPipelinePluginPositionId() {
		if( m_row.IsPipelinePluginPositionIdNull )
			throw konga::DbNullException("PipelinePluginPositionId");
		return m_row.Field_PipelinePluginPositionId;
	}
	void SetPipelinePluginPositionId(int i) {
		m_row.Field_PipelinePluginPositionId = i;
		m_row.IsPipelinePluginPositionIdNull = false;
		m_row.IsPipelinePluginPositionIdDirty = true;
	}
	bool IsPipelinePluginPositionIdNull() const {
		return m_row.IsPipelinePluginPositionIdNull;
	}
	void SetPipelinePluginPositionIdToNull() {
		m_row.IsPipelinePluginPositionIdNull = true;
		m_row.IsPipelinePluginPositionIdDirty = true;
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

	int GetPipelineEntityTypeId() {
		if( m_row.IsPipelineEntityTypeIdNull )
			throw konga::DbNullException("PipelineEntityTypeId");
		return m_row.Field_PipelineEntityTypeId;
	}
	void SetPipelineEntityTypeId(int i) {
		m_row.Field_PipelineEntityTypeId = i;
		m_row.IsPipelineEntityTypeIdNull = false;
		m_row.IsPipelineEntityTypeIdDirty = true;
	}
	bool IsPipelineEntityTypeIdNull() const {
		return m_row.IsPipelineEntityTypeIdNull;
	}
	void SetPipelineEntityTypeIdToNull() {
		m_row.IsPipelineEntityTypeIdNull = true;
		m_row.IsPipelineEntityTypeIdDirty = true;
	}

	int GetRelativePositionId() {
		if( m_row.IsRelativePositionIdNull )
			throw konga::DbNullException("RelativePositionId");
		return m_row.Field_RelativePositionId;
	}
	void SetRelativePositionId(int i) {
		m_row.Field_RelativePositionId = i;
		m_row.IsRelativePositionIdNull = false;
		m_row.IsRelativePositionIdDirty = true;
	}
	bool IsRelativePositionIdNull() const {
		return m_row.IsRelativePositionIdNull;
	}
	void SetRelativePositionIdToNull() {
		m_row.IsRelativePositionIdNull = true;
		m_row.IsRelativePositionIdDirty = true;
	}

protected:
	PipelinePluginPositionTabRow m_row;

private:
	bool IsPipelinePluginPositionIdDirty() const {
		return m_row.IsPipelinePluginPositionIdDirty;
	};
	bool IsPluginIdDirty() const {
		return m_row.IsPluginIdDirty;
	};
	bool IsPipelinePluginInstanceIdDirty() const {
		return m_row.IsPipelinePluginInstanceIdDirty;
	};
	bool IsPipelineEntityTypeIdDirty() const {
		return m_row.IsPipelineEntityTypeIdDirty;
	};
	bool IsRelativePositionIdDirty() const {
		return m_row.IsRelativePositionIdDirty;
	};


	std::string m_stUpdateFilter;

};

}                         


#endif
