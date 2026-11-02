/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRPDB_ORGANIZATIONSTRUCTURETAB_H
#define KONGA_DTL_CRPDB_ORGANIZATIONSTRUCTURETAB_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/PDb/OrganizationStructureTab.h>

namespace dtl {

template<> class DefaultSelValidate<PDb::OrganizationStructureTabRow>
{
public:
	bool operator()(BoundIOs &cols, PDb::OrganizationStructureTabRow &r)
	{
		r.IsProcessNameNull = cols["ProcessName"].IsNull();
		r.IsHierarchyNameNull = cols["HierarchyName"].IsNull();
		r.IsNodeIdNull = cols["NodeId"].IsNull();
		r.IsOrganizationalUnitNameNull = cols["OrganizationalUnitName"].IsNull();
		r.IsLevelInTreeNull = cols["LevelInTree"].IsNull();
		r.IsLevelNameNull = cols["LevelName"].IsNull();
		r.IsSiblingOrderNull = cols["SiblingOrder"].IsNull();
		r.IsFullPathNull = cols["FullPath"].IsNull();
		r.IsNodeImageNull = cols["NodeImage"].IsNull();
		r.IsVisibleStatusNull = cols["VisibleStatus"].IsNull();
		r.IsRoleAssociationTypeNull = cols["RoleAssociationType"].IsNull();
		r.IsRoleMappingIdNull = cols["RoleMappingId"].IsNull();
		r.IsFullTextPathNull = cols["FullTextPath"].IsNull();
		r.IsIsRoleMultipleNull = cols["IsRoleMultiple"].IsNull();
		r.IsInsertTimeNull = cols["InsertTime"].IsNull();
		r.IsModTimeNull = cols["ModTime"].IsNull();
		  
                                        
                       
    
		return true;
	}
};

template<> class DefaultInsValidate<PDb::OrganizationStructureTabRow>
{
public:
	bool operator()(BoundIOs &cols, PDb::OrganizationStructureTabRow &r)
	{
		if( r.IsProcessNameNull )
			cols["ProcessName"].SetNull();
		else
			cols["ProcessName"].ClearNull();
		if( r.IsHierarchyNameNull )
			cols["HierarchyName"].SetNull();
		else
			cols["HierarchyName"].ClearNull();
		if( r.IsNodeIdNull )
			cols["NodeId"].SetNull();
		else
			cols["NodeId"].ClearNull();
		if( r.IsOrganizationalUnitNameNull )
			cols["OrganizationalUnitName"].SetNull();
		else
			cols["OrganizationalUnitName"].ClearNull();
		if( r.IsLevelInTreeNull )
			cols["LevelInTree"].SetNull();
		else
			cols["LevelInTree"].ClearNull();
		if( r.IsLevelNameNull )
			cols["LevelName"].SetNull();
		else
			cols["LevelName"].ClearNull();
		if( r.IsSiblingOrderNull )
			cols["SiblingOrder"].SetNull();
		else
			cols["SiblingOrder"].ClearNull();
		if( r.IsFullPathNull )
			cols["FullPath"].SetNull();
		else
			cols["FullPath"].ClearNull();
		if( r.IsNodeImageNull )
			cols["NodeImage"].SetNull();
		else
			cols["NodeImage"].ClearNull();
		if( r.IsVisibleStatusNull )
			cols["VisibleStatus"].SetNull();
		else
			cols["VisibleStatus"].ClearNull();
		if( r.IsRoleAssociationTypeNull )
			cols["RoleAssociationType"].SetNull();
		else
			cols["RoleAssociationType"].ClearNull();
		if( r.IsRoleMappingIdNull )
			cols["RoleMappingId"].SetNull();
		else
			cols["RoleMappingId"].ClearNull();
		if( r.IsFullTextPathNull )
			cols["FullTextPath"].SetNull();
		else
			cols["FullTextPath"].ClearNull();
		if( r.IsIsRoleMultipleNull )
			cols["IsRoleMultiple"].SetNull();
		else
			cols["IsRoleMultiple"].ClearNull();
		if( r.IsInsertTimeNull )
			cols["InsertTime"].SetNull();
		else
			cols["InsertTime"].ClearNull();
		if( r.IsModTimeNull )
			cols["ModTime"].SetNull();
		else
			cols["ModTime"].ClearNull();
		  
                                        
                       
    
		return true;
	}
};

template<> class DefaultBCA<PDb::OrganizationStructureTabRow>
{
public:
	void operator()(BoundIOs &cols, PDb::OrganizationStructureTabRow &rowbuf)
	{
		cols["ProcessName"] == rowbuf.field_ProcessName;
		cols["HierarchyName"] == rowbuf.field_HierarchyName;
		cols["NodeId"] == rowbuf.field_NodeId;
		cols["OrganizationalUnitName"] == rowbuf.field_OrganizationalUnitName;
		cols["LevelInTree"] == rowbuf.field_LevelInTree;
		cols["LevelName"] == rowbuf.field_LevelName;
		cols["SiblingOrder"] == rowbuf.field_SiblingOrder;
		cols["FullPath"] == rowbuf.field_FullPath;
		cols["NodeImage"] == rowbuf.field_NodeImage;
		cols["VisibleStatus"] == rowbuf.field_VisibleStatus;
		cols["RoleAssociationType"] == rowbuf.field_RoleAssociationType;
		cols["RoleMappingId"] == rowbuf.field_RoleMappingId;
		cols["FullTextPath"] == rowbuf.field_FullTextPath;
		cols["IsRoleMultiple"] == rowbuf.field_IsRoleMultiple;
		cols["InsertTime"] == rowbuf.field_InsertTime;
		cols["ModTime"] == rowbuf.field_ModTime;
		  
                                        
                       
    
	}
};


}                     



namespace dtl_pdb {

class DtlOrganizationStructureTabDeleteBinder : public CustomBinder<PDb::OrganizationStructureTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		PDb::OrganizationStructureTabRow& rowbuf = *m_pRow;
		cols["ProcessName"] == rowbuf.field_ProcessName;
		cols["HierarchyName"] == rowbuf.field_HierarchyName;
		cols["NodeId"] == rowbuf.field_NodeId;
	}
};

class DtlOrganizationStructureTab : public PDb::OrganizationStructureTab {

public:
	DtlOrganizationStructureTab(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlOrganizationStructureTab() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		PDb::OrganizationStructureTab::Edit();
		m_dtlRecSet.Edit();
	}
	void AddNew() {
		m_dtlRecSet.AddNew();
	}
	int Delete() {
		return m_dtlRecSet.Delete();
	}
	bool IsEOF() {
		return m_dtlRecSet.IsEOF();
	}
	bool Next() {
		return m_dtlRecSet.Next();
	}
	void Close() {
		m_dtlRecSet.Close();
	}
	void SetWhereClause(const std::string& stWhereClause) {
		m_dtlRecSet.SetWhereClause(stWhereClause);
	}
	std::string GetWhereClause() const {
		return m_dtlRecSet.GetWhereClause();
	}

	std::string GetLastError() const {
		return m_dtlRecSet.GetLastError();
	}
	long GetLastErrorNumber() const {
		return m_dtlRecSet.GetLastErrorNumber();
	}

private:
	DtlOrganizationStructureTabDeleteBinder m_deleteBinder;
	DtlDbRecordSet<PDb::OrganizationStructureTab, PDb::OrganizationStructureTabRow> m_dtlRecSet;

};


}                         


#endif

