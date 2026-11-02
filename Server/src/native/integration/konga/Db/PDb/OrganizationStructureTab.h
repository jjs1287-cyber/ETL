/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_CRPDB_ORGANIZATIONSTRUCTURETAB_H
#define KONGA_DB_CRPDB_ORGANIZATIONSTRUCTURETAB_H

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

class OrganizationStructureTabRow
{
public:
	OrganizationStructureTabRow() : 
			IsProcessNameNull(true),
			IsProcessNamePrimaryKey(true),
			IsProcessNameDirty(false),
			IsHierarchyNameNull(true),
			IsHierarchyNamePrimaryKey(true),
			IsHierarchyNameDirty(false),
			field_NodeId(0),
			IsNodeIdNull(true),
			IsNodeIdPrimaryKey(true),
			IsNodeIdDirty(false),
			IsOrganizationalUnitNameNull(true),
			IsOrganizationalUnitNamePrimaryKey(false),
			IsOrganizationalUnitNameDirty(false),
			field_LevelInTree(0),
			IsLevelInTreeNull(true),
			IsLevelInTreePrimaryKey(false),
			IsLevelInTreeDirty(false),
			IsLevelNameNull(true),
			IsLevelNamePrimaryKey(false),
			IsLevelNameDirty(false),
			field_SiblingOrder(0),
			IsSiblingOrderNull(true),
			IsSiblingOrderPrimaryKey(false),
			IsSiblingOrderDirty(false),
			IsFullPathNull(true),
			IsFullPathPrimaryKey(false),
			IsFullPathDirty(false),
			field_NodeImage(0),
			IsNodeImageNull(true),
			IsNodeImagePrimaryKey(false),
			IsNodeImageDirty(false),
			field_VisibleStatus(0),
			IsVisibleStatusNull(true),
			IsVisibleStatusPrimaryKey(false),
			IsVisibleStatusDirty(false),
			IsRoleAssociationTypeNull(true),
			IsRoleAssociationTypePrimaryKey(false),
			IsRoleAssociationTypeDirty(false),
			field_RoleMappingId(0),
			IsRoleMappingIdNull(true),
			IsRoleMappingIdPrimaryKey(false),
			IsRoleMappingIdDirty(false),
			IsFullTextPathNull(true),
			IsFullTextPathPrimaryKey(false),
			IsFullTextPathDirty(false),
			field_IsRoleMultiple(0),
			IsIsRoleMultipleNull(true),
			IsIsRoleMultiplePrimaryKey(false),
			IsIsRoleMultipleDirty(false),
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

	void Copy(const OrganizationStructureTabRow& r)
	{
		field_ProcessName = r.field_ProcessName;
		IsProcessNameNull = r.IsProcessNameNull;
		IsProcessNameDirty = r.IsProcessNameDirty;

		field_HierarchyName = r.field_HierarchyName;
		IsHierarchyNameNull = r.IsHierarchyNameNull;
		IsHierarchyNameDirty = r.IsHierarchyNameDirty;

		field_NodeId = r.field_NodeId;
		IsNodeIdNull = r.IsNodeIdNull;
		IsNodeIdDirty = r.IsNodeIdDirty;

		field_OrganizationalUnitName = r.field_OrganizationalUnitName;
		IsOrganizationalUnitNameNull = r.IsOrganizationalUnitNameNull;
		IsOrganizationalUnitNameDirty = r.IsOrganizationalUnitNameDirty;

		field_LevelInTree = r.field_LevelInTree;
		IsLevelInTreeNull = r.IsLevelInTreeNull;
		IsLevelInTreeDirty = r.IsLevelInTreeDirty;

		field_LevelName = r.field_LevelName;
		IsLevelNameNull = r.IsLevelNameNull;
		IsLevelNameDirty = r.IsLevelNameDirty;

		field_SiblingOrder = r.field_SiblingOrder;
		IsSiblingOrderNull = r.IsSiblingOrderNull;
		IsSiblingOrderDirty = r.IsSiblingOrderDirty;

		field_FullPath = r.field_FullPath;
		IsFullPathNull = r.IsFullPathNull;
		IsFullPathDirty = r.IsFullPathDirty;

		field_NodeImage = r.field_NodeImage;
		IsNodeImageNull = r.IsNodeImageNull;
		IsNodeImageDirty = r.IsNodeImageDirty;

		field_VisibleStatus = r.field_VisibleStatus;
		IsVisibleStatusNull = r.IsVisibleStatusNull;
		IsVisibleStatusDirty = r.IsVisibleStatusDirty;

		field_RoleAssociationType = r.field_RoleAssociationType;
		IsRoleAssociationTypeNull = r.IsRoleAssociationTypeNull;
		IsRoleAssociationTypeDirty = r.IsRoleAssociationTypeDirty;

		field_RoleMappingId = r.field_RoleMappingId;
		IsRoleMappingIdNull = r.IsRoleMappingIdNull;
		IsRoleMappingIdDirty = r.IsRoleMappingIdDirty;

		field_FullTextPath = r.field_FullTextPath;
		IsFullTextPathNull = r.IsFullTextPathNull;
		IsFullTextPathDirty = r.IsFullTextPathDirty;

		field_IsRoleMultiple = r.field_IsRoleMultiple;
		IsIsRoleMultipleNull = r.IsIsRoleMultipleNull;
		IsIsRoleMultipleDirty = r.IsIsRoleMultipleDirty;

		field_InsertTime = r.field_InsertTime;
		IsInsertTimeNull = r.IsInsertTimeNull;
		IsInsertTimeDirty = r.IsInsertTimeDirty;

		field_ModTime = r.field_ModTime;
		IsModTimeNull = r.IsModTimeNull;
		IsModTimeDirty = r.IsModTimeDirty;

	}

	void CopyPKs(const OrganizationStructureTabRow& r)
	{
		field_ProcessName = r.field_ProcessName;
		IsProcessNameNull = r.IsProcessNameNull;
		IsProcessNameDirty = r.IsProcessNameDirty;

		field_HierarchyName = r.field_HierarchyName;
		IsHierarchyNameNull = r.IsHierarchyNameNull;
		IsHierarchyNameDirty = r.IsHierarchyNameDirty;

		field_NodeId = r.field_NodeId;
		IsNodeIdNull = r.IsNodeIdNull;
		IsNodeIdDirty = r.IsNodeIdDirty;

	}

	void SetDirty(bool b)
	{
		IsProcessNameDirty = b;
		IsHierarchyNameDirty = b;
		IsNodeIdDirty = b;
		IsOrganizationalUnitNameDirty = b;
		IsLevelInTreeDirty = b;
		IsLevelNameDirty = b;
		IsSiblingOrderDirty = b;
		IsFullPathDirty = b;
		IsNodeImageDirty = b;
		IsVisibleStatusDirty = b;
		IsRoleAssociationTypeDirty = b;
		IsRoleMappingIdDirty = b;
		IsFullTextPathDirty = b;
		IsIsRoleMultipleDirty = b;
		IsInsertTimeDirty = b;
		IsModTimeDirty = b;
	}

	bool IsDirty() const
	{
		return IsProcessNameDirty ||
			IsHierarchyNameDirty ||
			IsNodeIdDirty ||
			IsOrganizationalUnitNameDirty ||
			IsLevelInTreeDirty ||
			IsLevelNameDirty ||
			IsSiblingOrderDirty ||
			IsFullPathDirty ||
			IsNodeImageDirty ||
			IsVisibleStatusDirty ||
			IsRoleAssociationTypeDirty ||
			IsRoleMappingIdDirty ||
			IsFullTextPathDirty ||
			IsIsRoleMultipleDirty ||
			IsInsertTimeDirty ||
			IsModTimeDirty;
	}

	std::string field_ProcessName;
	bool IsProcessNameNull;
	bool IsProcessNameDirty;

	std::string field_HierarchyName;
	bool IsHierarchyNameNull;
	bool IsHierarchyNameDirty;

	int field_NodeId;
	bool IsNodeIdNull;
	bool IsNodeIdDirty;

	std::string field_OrganizationalUnitName;
	bool IsOrganizationalUnitNameNull;
	bool IsOrganizationalUnitNameDirty;

	int field_LevelInTree;
	bool IsLevelInTreeNull;
	bool IsLevelInTreeDirty;

	std::string field_LevelName;
	bool IsLevelNameNull;
	bool IsLevelNameDirty;

	int field_SiblingOrder;
	bool IsSiblingOrderNull;
	bool IsSiblingOrderDirty;

	std::string field_FullPath;
	bool IsFullPathNull;
	bool IsFullPathDirty;

	int field_NodeImage;
	bool IsNodeImageNull;
	bool IsNodeImageDirty;

	int field_VisibleStatus;
	bool IsVisibleStatusNull;
	bool IsVisibleStatusDirty;

	std::string field_RoleAssociationType;
	bool IsRoleAssociationTypeNull;
	bool IsRoleAssociationTypeDirty;

	int field_RoleMappingId;
	bool IsRoleMappingIdNull;
	bool IsRoleMappingIdDirty;

	std::string field_FullTextPath;
	bool IsFullTextPathNull;
	bool IsFullTextPathDirty;

	int field_IsRoleMultiple;
	bool IsIsRoleMultipleNull;
	bool IsIsRoleMultipleDirty;

	TIMESTAMP_STRUCT field_InsertTime;
	bool IsInsertTimeNull;
	bool IsInsertTimeDirty;

	TIMESTAMP_STRUCT field_ModTime;
	bool IsModTimeNull;
	bool IsModTimeDirty;


private:
	bool IsProcessNamePrimaryKey;
	bool IsHierarchyNamePrimaryKey;
	bool IsNodeIdPrimaryKey;
	bool IsOrganizationalUnitNamePrimaryKey;
	bool IsLevelInTreePrimaryKey;
	bool IsLevelNamePrimaryKey;
	bool IsSiblingOrderPrimaryKey;
	bool IsFullPathPrimaryKey;
	bool IsNodeImagePrimaryKey;
	bool IsVisibleStatusPrimaryKey;
	bool IsRoleAssociationTypePrimaryKey;
	bool IsRoleMappingIdPrimaryKey;
	bool IsFullTextPathPrimaryKey;
	bool IsIsRoleMultiplePrimaryKey;
	bool IsInsertTimePrimaryKey;
	bool IsModTimePrimaryKey;

};


class OrganizationStructureTab : public konga::DbRecordSet {
public:
	OrganizationStructureTab() {}

	static OrganizationStructureTab* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "OrganizationStructureTab";
	}

	void DoFieldExchange(const OrganizationStructureTabRow& row) {
		m_row.Copy(row);
	}

	void Edit();

	std::string GetUpdateFilter() {
		return m_stUpdateFilter;
	}

	std::string GetProcessName() {
		if( m_row.IsProcessNameNull )
			throw konga::DbNullException("ProcessName");
		return m_row.field_ProcessName;
	}
	void SetProcessName(const std::string& st) {
		m_row.field_ProcessName = st;
		m_row.IsProcessNameNull = false;
		m_row.IsProcessNameDirty = true;
	}
	bool IsProcessNameNull() const {
		return m_row.IsProcessNameNull;
	}
	void SetProcessNameToNull() {
		m_row.field_ProcessName = "";
		m_row.IsProcessNameNull = true;
		m_row.IsProcessNameDirty = true;
	}

	std::string GetHierarchyName() {
		if( m_row.IsHierarchyNameNull )
			throw konga::DbNullException("HierarchyName");
		return m_row.field_HierarchyName;
	}
	void SetHierarchyName(const std::string& st) {
		m_row.field_HierarchyName = st;
		m_row.IsHierarchyNameNull = false;
		m_row.IsHierarchyNameDirty = true;
	}
	bool IsHierarchyNameNull() const {
		return m_row.IsHierarchyNameNull;
	}
	void SetHierarchyNameToNull() {
		m_row.field_HierarchyName = "";
		m_row.IsHierarchyNameNull = true;
		m_row.IsHierarchyNameDirty = true;
	}

	int GetNodeId() {
		if( m_row.IsNodeIdNull )
			throw konga::DbNullException("NodeId");
		return m_row.field_NodeId;
	}
	void SetNodeId(int i) {
		m_row.field_NodeId = i;
		m_row.IsNodeIdNull = false;
		m_row.IsNodeIdDirty = true;
	}
	bool IsNodeIdNull() const {
		return m_row.IsNodeIdNull;
	}
	void SetNodeIdToNull() {
		m_row.IsNodeIdNull = true;
		m_row.IsNodeIdDirty = true;
	}

	std::string GetOrganizationalUnitName() {
		if( m_row.IsOrganizationalUnitNameNull )
			throw konga::DbNullException("OrganizationalUnitName");
		return m_row.field_OrganizationalUnitName;
	}
	void SetOrganizationalUnitName(const std::string& st) {
		m_row.field_OrganizationalUnitName = st;
		m_row.IsOrganizationalUnitNameNull = false;
		m_row.IsOrganizationalUnitNameDirty = true;
	}
	bool IsOrganizationalUnitNameNull() const {
		return m_row.IsOrganizationalUnitNameNull;
	}
	void SetOrganizationalUnitNameToNull() {
		m_row.field_OrganizationalUnitName = "";
		m_row.IsOrganizationalUnitNameNull = true;
		m_row.IsOrganizationalUnitNameDirty = true;
	}

	int GetLevelInTree() {
		if( m_row.IsLevelInTreeNull )
			throw konga::DbNullException("LevelInTree");
		return m_row.field_LevelInTree;
	}
	void SetLevelInTree(int i) {
		m_row.field_LevelInTree = i;
		m_row.IsLevelInTreeNull = false;
		m_row.IsLevelInTreeDirty = true;
	}
	bool IsLevelInTreeNull() const {
		return m_row.IsLevelInTreeNull;
	}
	void SetLevelInTreeToNull() {
		m_row.IsLevelInTreeNull = true;
		m_row.IsLevelInTreeDirty = true;
	}

	std::string GetLevelName() {
		if( m_row.IsLevelNameNull )
			throw konga::DbNullException("LevelName");
		return m_row.field_LevelName;
	}
	void SetLevelName(const std::string& st) {
		m_row.field_LevelName = st;
		m_row.IsLevelNameNull = false;
		m_row.IsLevelNameDirty = true;
	}
	bool IsLevelNameNull() const {
		return m_row.IsLevelNameNull;
	}
	void SetLevelNameToNull() {
		m_row.field_LevelName = "";
		m_row.IsLevelNameNull = true;
		m_row.IsLevelNameDirty = true;
	}

	int GetSiblingOrder() {
		if( m_row.IsSiblingOrderNull )
			throw konga::DbNullException("SiblingOrder");
		return m_row.field_SiblingOrder;
	}
	void SetSiblingOrder(int i) {
		m_row.field_SiblingOrder = i;
		m_row.IsSiblingOrderNull = false;
		m_row.IsSiblingOrderDirty = true;
	}
	bool IsSiblingOrderNull() const {
		return m_row.IsSiblingOrderNull;
	}
	void SetSiblingOrderToNull() {
		m_row.IsSiblingOrderNull = true;
		m_row.IsSiblingOrderDirty = true;
	}

	std::string GetFullPath() {
		if( m_row.IsFullPathNull )
			throw konga::DbNullException("FullPath");
		return m_row.field_FullPath;
	}
	void SetFullPath(const std::string& st) {
		m_row.field_FullPath = st;
		m_row.IsFullPathNull = false;
		m_row.IsFullPathDirty = true;
	}
	bool IsFullPathNull() const {
		return m_row.IsFullPathNull;
	}
	void SetFullPathToNull() {
		m_row.field_FullPath = "";
		m_row.IsFullPathNull = true;
		m_row.IsFullPathDirty = true;
	}

	int GetNodeImage() {
		if( m_row.IsNodeImageNull )
			throw konga::DbNullException("NodeImage");
		return m_row.field_NodeImage;
	}
	void SetNodeImage(int i) {
		m_row.field_NodeImage = i;
		m_row.IsNodeImageNull = false;
		m_row.IsNodeImageDirty = true;
	}
	bool IsNodeImageNull() const {
		return m_row.IsNodeImageNull;
	}
	void SetNodeImageToNull() {
		m_row.IsNodeImageNull = true;
		m_row.IsNodeImageDirty = true;
	}

	int GetVisibleStatus() {
		if( m_row.IsVisibleStatusNull )
			throw konga::DbNullException("VisibleStatus");
		return m_row.field_VisibleStatus;
	}
	void SetVisibleStatus(int i) {
		m_row.field_VisibleStatus = i;
		m_row.IsVisibleStatusNull = false;
		m_row.IsVisibleStatusDirty = true;
	}
	bool IsVisibleStatusNull() const {
		return m_row.IsVisibleStatusNull;
	}
	void SetVisibleStatusToNull() {
		m_row.IsVisibleStatusNull = true;
		m_row.IsVisibleStatusDirty = true;
	}

	std::string GetRoleAssociationType() {
		if( m_row.IsRoleAssociationTypeNull )
			throw konga::DbNullException("RoleAssociationType");
		return m_row.field_RoleAssociationType;
	}
	void SetRoleAssociationType(const std::string& st) {
		m_row.field_RoleAssociationType = st;
		m_row.IsRoleAssociationTypeNull = false;
		m_row.IsRoleAssociationTypeDirty = true;
	}
	bool IsRoleAssociationTypeNull() const {
		return m_row.IsRoleAssociationTypeNull;
	}
	void SetRoleAssociationTypeToNull() {
		m_row.field_RoleAssociationType = "";
		m_row.IsRoleAssociationTypeNull = true;
		m_row.IsRoleAssociationTypeDirty = true;
	}

	int GetRoleMappingId() {
		if( m_row.IsRoleMappingIdNull )
			throw konga::DbNullException("RoleMappingId");
		return m_row.field_RoleMappingId;
	}
	void SetRoleMappingId(int i) {
		m_row.field_RoleMappingId = i;
		m_row.IsRoleMappingIdNull = false;
		m_row.IsRoleMappingIdDirty = true;
	}
	bool IsRoleMappingIdNull() const {
		return m_row.IsRoleMappingIdNull;
	}
	void SetRoleMappingIdToNull() {
		m_row.IsRoleMappingIdNull = true;
		m_row.IsRoleMappingIdDirty = true;
	}

	std::string GetFullTextPath() {
		if( m_row.IsFullTextPathNull )
			throw konga::DbNullException("FullTextPath");
		return m_row.field_FullTextPath;
	}
	void SetFullTextPath(const std::string& st) {
		m_row.field_FullTextPath = st;
		m_row.IsFullTextPathNull = false;
		m_row.IsFullTextPathDirty = true;
	}
	bool IsFullTextPathNull() const {
		return m_row.IsFullTextPathNull;
	}
	void SetFullTextPathToNull() {
		m_row.field_FullTextPath = "";
		m_row.IsFullTextPathNull = true;
		m_row.IsFullTextPathDirty = true;
	}

	int GetIsRoleMultiple() {
		if( m_row.IsIsRoleMultipleNull )
			throw konga::DbNullException("IsRoleMultiple");
		return m_row.field_IsRoleMultiple;
	}
	void SetIsRoleMultiple(int i) {
		m_row.field_IsRoleMultiple = i;
		m_row.IsIsRoleMultipleNull = false;
		m_row.IsIsRoleMultipleDirty = true;
	}
	bool IsIsRoleMultipleNull() const {
		return m_row.IsIsRoleMultipleNull;
	}
	void SetIsRoleMultipleToNull() {
		m_row.IsIsRoleMultipleNull = true;
		m_row.IsIsRoleMultipleDirty = true;
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
	OrganizationStructureTabRow m_row;

private:
	bool IsProcessNameDirty() const {
		return m_row.IsProcessNameDirty;
	};
	bool IsHierarchyNameDirty() const {
		return m_row.IsHierarchyNameDirty;
	};
	bool IsNodeIdDirty() const {
		return m_row.IsNodeIdDirty;
	};
	bool IsOrganizationalUnitNameDirty() const {
		return m_row.IsOrganizationalUnitNameDirty;
	};
	bool IsLevelInTreeDirty() const {
		return m_row.IsLevelInTreeDirty;
	};
	bool IsLevelNameDirty() const {
		return m_row.IsLevelNameDirty;
	};
	bool IsSiblingOrderDirty() const {
		return m_row.IsSiblingOrderDirty;
	};
	bool IsFullPathDirty() const {
		return m_row.IsFullPathDirty;
	};
	bool IsNodeImageDirty() const {
		return m_row.IsNodeImageDirty;
	};
	bool IsVisibleStatusDirty() const {
		return m_row.IsVisibleStatusDirty;
	};
	bool IsRoleAssociationTypeDirty() const {
		return m_row.IsRoleAssociationTypeDirty;
	};
	bool IsRoleMappingIdDirty() const {
		return m_row.IsRoleMappingIdDirty;
	};
	bool IsFullTextPathDirty() const {
		return m_row.IsFullTextPathDirty;
	};
	bool IsIsRoleMultipleDirty() const {
		return m_row.IsIsRoleMultipleDirty;
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
