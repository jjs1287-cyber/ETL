/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRPDB_OBJECTPERMISSIONSTAB_H
#define KONGA_DTL_CRPDB_OBJECTPERMISSIONSTAB_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/PDb/ObjectPermissionsTab.h>

namespace dtl {

template<> class DefaultSelValidate<PDb::ObjectPermissionsTabRow>
{
public:
	bool operator()(BoundIOs &cols, PDb::ObjectPermissionsTabRow &r)
	{
		r.IsObjectIdNull = cols["ObjectId"].IsNull();
		r.IsObjectNameNull = cols["ObjectName"].IsNull();
		r.IsAllowAccessNull = cols["AllowAccess"].IsNull();
		r.IsInsertTimeNull = cols["InsertTime"].IsNull();
		r.IsModTimeNull = cols["ModTime"].IsNull();
		  
                                        
                       
    
		return true;
	}
};

template<> class DefaultInsValidate<PDb::ObjectPermissionsTabRow>
{
public:
	bool operator()(BoundIOs &cols, PDb::ObjectPermissionsTabRow &r)
	{
		if( r.IsObjectNameNull )
			cols["ObjectName"].SetNull();
		else
			cols["ObjectName"].ClearNull();
		if( r.IsAllowAccessNull )
			cols["AllowAccess"].SetNull();
		else
			cols["AllowAccess"].ClearNull();
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

template<> class DefaultBCA<PDb::ObjectPermissionsTabRow>
{
public:
	void operator()(BoundIOs &cols, PDb::ObjectPermissionsTabRow &rowbuf)
	{
		cols["ObjectId"] == rowbuf.field_ObjectId;
		cols["ObjectName"] == rowbuf.field_ObjectName;
		cols["AllowAccess"] == rowbuf.field_AllowAccess;
		cols["InsertTime"] == rowbuf.field_InsertTime;
		cols["ModTime"] == rowbuf.field_ModTime;
		  
                                        
                       
    
	}
};


}                     



namespace dtl_pdb {


class DtlObjectPermissionsTabInsertBinder : public CustomBinder<PDb::ObjectPermissionsTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		PDb::ObjectPermissionsTabRow& rowbuf = *m_pRow;
		cols["ObjectName"] << rowbuf.field_ObjectName;
		cols["AllowAccess"] << rowbuf.field_AllowAccess;
		cols["InsertTime"] << rowbuf.field_InsertTime;
		cols["ModTime"] << rowbuf.field_ModTime;
	}
};

class DtlObjectPermissionsTabDeleteBinder : public CustomBinder<PDb::ObjectPermissionsTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		PDb::ObjectPermissionsTabRow& rowbuf = *m_pRow;
		cols["ObjectId"] == rowbuf.field_ObjectId;
	}
};

class DtlObjectPermissionsTab : public PDb::ObjectPermissionsTab {

public:
	DtlObjectPermissionsTab(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlObjectPermissionsTab() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		PDb::ObjectPermissionsTab::Edit();
		m_dtlRecSet.Edit();
	}
	void AddNew() {
		m_dtlRecSet.AddNew(&m_insertBinder);
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
	DtlObjectPermissionsTabInsertBinder m_insertBinder;
	DtlObjectPermissionsTabDeleteBinder m_deleteBinder;
	DtlDbRecordSet<PDb::ObjectPermissionsTab, PDb::ObjectPermissionsTabRow> m_dtlRecSet;

};


}                         


#endif

