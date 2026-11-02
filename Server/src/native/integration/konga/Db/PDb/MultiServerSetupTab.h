/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_CRPDB_MULTISERVERSETUPTAB_H
#define KONGA_DB_CRPDB_MULTISERVERSETUPTAB_H

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

class MultiServerSetupTabRow
{
public:
	MultiServerSetupTabRow() : 
			IsMachineNameNull(true),
			IsMachineNamePrimaryKey(false),
			IsMachineNameDirty(false),
			field_Enabled(0),
			IsEnabledNull(true),
			IsEnabledPrimaryKey(false),
			IsEnabledDirty(false),
			field_CurrentRole(0),
			IsCurrentRoleNull(true),
			IsCurrentRolePrimaryKey(false),
			IsCurrentRoleDirty(false),
			field_Capability(0),
			IsCapabilityNull(true),
			IsCapabilityPrimaryKey(false),
			IsCapabilityDirty(false),
			IsAssetTagNull(true),
			IsAssetTagPrimaryKey(true),
			IsAssetTagDirty(false),
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

	void Copy(const MultiServerSetupTabRow& r)
	{
		field_MachineName = r.field_MachineName;
		IsMachineNameNull = r.IsMachineNameNull;
		IsMachineNameDirty = r.IsMachineNameDirty;

		field_Enabled = r.field_Enabled;
		IsEnabledNull = r.IsEnabledNull;
		IsEnabledDirty = r.IsEnabledDirty;

		field_CurrentRole = r.field_CurrentRole;
		IsCurrentRoleNull = r.IsCurrentRoleNull;
		IsCurrentRoleDirty = r.IsCurrentRoleDirty;

		field_Capability = r.field_Capability;
		IsCapabilityNull = r.IsCapabilityNull;
		IsCapabilityDirty = r.IsCapabilityDirty;

		field_AssetTag = r.field_AssetTag;
		IsAssetTagNull = r.IsAssetTagNull;
		IsAssetTagDirty = r.IsAssetTagDirty;

		field_InsertTime = r.field_InsertTime;
		IsInsertTimeNull = r.IsInsertTimeNull;
		IsInsertTimeDirty = r.IsInsertTimeDirty;

		field_ModTime = r.field_ModTime;
		IsModTimeNull = r.IsModTimeNull;
		IsModTimeDirty = r.IsModTimeDirty;

	}

	void CopyPKs(const MultiServerSetupTabRow& r)
	{
		field_AssetTag = r.field_AssetTag;
		IsAssetTagNull = r.IsAssetTagNull;
		IsAssetTagDirty = r.IsAssetTagDirty;

	}

	void SetDirty(bool b)
	{
		IsMachineNameDirty = b;
		IsEnabledDirty = b;
		IsCurrentRoleDirty = b;
		IsCapabilityDirty = b;
		IsAssetTagDirty = b;
		IsInsertTimeDirty = b;
		IsModTimeDirty = b;
	}

	bool IsDirty() const
	{
		return IsMachineNameDirty ||
			IsEnabledDirty ||
			IsCurrentRoleDirty ||
			IsCapabilityDirty ||
			IsAssetTagDirty ||
			IsInsertTimeDirty ||
			IsModTimeDirty;
	}

	std::string field_MachineName;
	bool IsMachineNameNull;
	bool IsMachineNameDirty;

	int field_Enabled;
	bool IsEnabledNull;
	bool IsEnabledDirty;

	int field_CurrentRole;
	bool IsCurrentRoleNull;
	bool IsCurrentRoleDirty;

	int field_Capability;
	bool IsCapabilityNull;
	bool IsCapabilityDirty;

	std::string field_AssetTag;
	bool IsAssetTagNull;
	bool IsAssetTagDirty;

	TIMESTAMP_STRUCT field_InsertTime;
	bool IsInsertTimeNull;
	bool IsInsertTimeDirty;

	TIMESTAMP_STRUCT field_ModTime;
	bool IsModTimeNull;
	bool IsModTimeDirty;


private:
	bool IsMachineNamePrimaryKey;
	bool IsEnabledPrimaryKey;
	bool IsCurrentRolePrimaryKey;
	bool IsCapabilityPrimaryKey;
	bool IsAssetTagPrimaryKey;
	bool IsInsertTimePrimaryKey;
	bool IsModTimePrimaryKey;

};


class MultiServerSetupTab : public konga::DbRecordSet {
public:
	MultiServerSetupTab() {}

	static MultiServerSetupTab* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "MultiServerSetupTab";
	}

	void DoFieldExchange(const MultiServerSetupTabRow& row) {
		m_row.Copy(row);
	}

	void Edit();

	std::string GetUpdateFilter() {
		return m_stUpdateFilter;
	}

	std::string GetMachineName() {
		if( m_row.IsMachineNameNull )
			throw konga::DbNullException("MachineName");
		return m_row.field_MachineName;
	}
	void SetMachineName(const std::string& st) {
		m_row.field_MachineName = st;
		m_row.IsMachineNameNull = false;
		m_row.IsMachineNameDirty = true;
	}
	bool IsMachineNameNull() const {
		return m_row.IsMachineNameNull;
	}
	void SetMachineNameToNull() {
		m_row.field_MachineName = "";
		m_row.IsMachineNameNull = true;
		m_row.IsMachineNameDirty = true;
	}

	int GetEnabled() {
		if( m_row.IsEnabledNull )
			throw konga::DbNullException("Enabled");
		return m_row.field_Enabled;
	}
	void SetEnabled(int i) {
		m_row.field_Enabled = i;
		m_row.IsEnabledNull = false;
		m_row.IsEnabledDirty = true;
	}
	bool IsEnabledNull() const {
		return m_row.IsEnabledNull;
	}
	void SetEnabledToNull() {
		m_row.IsEnabledNull = true;
		m_row.IsEnabledDirty = true;
	}

	int GetCurrentRole() {
		if( m_row.IsCurrentRoleNull )
			throw konga::DbNullException("CurrentRole");
		return m_row.field_CurrentRole;
	}
	void SetCurrentRole(int i) {
		m_row.field_CurrentRole = i;
		m_row.IsCurrentRoleNull = false;
		m_row.IsCurrentRoleDirty = true;
	}
	bool IsCurrentRoleNull() const {
		return m_row.IsCurrentRoleNull;
	}
	void SetCurrentRoleToNull() {
		m_row.IsCurrentRoleNull = true;
		m_row.IsCurrentRoleDirty = true;
	}

	int GetCapability() {
		if( m_row.IsCapabilityNull )
			throw konga::DbNullException("Capability");
		return m_row.field_Capability;
	}
	void SetCapability(int i) {
		m_row.field_Capability = i;
		m_row.IsCapabilityNull = false;
		m_row.IsCapabilityDirty = true;
	}
	bool IsCapabilityNull() const {
		return m_row.IsCapabilityNull;
	}
	void SetCapabilityToNull() {
		m_row.IsCapabilityNull = true;
		m_row.IsCapabilityDirty = true;
	}

	std::string GetAssetTag() {
		if( m_row.IsAssetTagNull )
			throw konga::DbNullException("AssetTag");
		return m_row.field_AssetTag;
	}
	void SetAssetTag(const std::string& st) {
		m_row.field_AssetTag = st;
		m_row.IsAssetTagNull = false;
		m_row.IsAssetTagDirty = true;
	}
	bool IsAssetTagNull() const {
		return m_row.IsAssetTagNull;
	}
	void SetAssetTagToNull() {
		m_row.field_AssetTag = "";
		m_row.IsAssetTagNull = true;
		m_row.IsAssetTagDirty = true;
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
	MultiServerSetupTabRow m_row;

private:
	bool IsMachineNameDirty() const {
		return m_row.IsMachineNameDirty;
	};
	bool IsEnabledDirty() const {
		return m_row.IsEnabledDirty;
	};
	bool IsCurrentRoleDirty() const {
		return m_row.IsCurrentRoleDirty;
	};
	bool IsCapabilityDirty() const {
		return m_row.IsCapabilityDirty;
	};
	bool IsAssetTagDirty() const {
		return m_row.IsAssetTagDirty;
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
