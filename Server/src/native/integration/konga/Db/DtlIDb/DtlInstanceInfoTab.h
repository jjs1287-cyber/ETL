/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRIDB_INSTANCEINFOTAB_H
#define KONGA_DTL_CRIDB_INSTANCEINFOTAB_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/IDb/InstanceInfoTab.h>

namespace dtl {

template<> class DefaultSelValidate<IDb::InstanceInfoTabRow>
{
public:
	bool operator()(BoundIOs &cols, IDb::InstanceInfoTabRow &r)
	{
		r.IsProcessIdNull = cols["ProcessId"].IsNull();
		r.IsProcessVersionNull = cols["ProcessVersion"].IsNull();
		r.IsHighestProcessInstanceIdNull = cols["HighestProcessInstanceId"].IsNull();
		r.IsInsertTimeNull = cols["InsertTime"].IsNull();
		r.IsModTimeNull = cols["ModTime"].IsNull();
		  
                                        
                       
    
		return true;
	}
};

template<> class DefaultInsValidate<IDb::InstanceInfoTabRow>
{
public:
	bool operator()(BoundIOs &cols, IDb::InstanceInfoTabRow &r)
	{
		if( r.IsProcessIdNull )
			cols["ProcessId"].SetNull();
		else
			cols["ProcessId"].ClearNull();
		if( r.IsProcessVersionNull )
			cols["ProcessVersion"].SetNull();
		else
			cols["ProcessVersion"].ClearNull();
		if( r.IsHighestProcessInstanceIdNull )
			cols["HighestProcessInstanceId"].SetNull();
		else
			cols["HighestProcessInstanceId"].ClearNull();
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

template<> class DefaultBCA<IDb::InstanceInfoTabRow>
{
public:
	void operator()(BoundIOs &cols, IDb::InstanceInfoTabRow &rowbuf)
	{
		cols["ProcessId"] == rowbuf.field_ProcessId;
		cols["ProcessVersion"] == rowbuf.field_ProcessVersion;
		cols["HighestProcessInstanceId"] == rowbuf.field_HighestProcessInstanceId;
		cols["InsertTime"] == rowbuf.field_InsertTime;
		cols["ModTime"] == rowbuf.field_ModTime;
		  
                                        
                       
    
	}
};


}                     



namespace dtl_idb {

class DtlInstanceInfoTabDeleteBinder : public CustomBinder<IDb::InstanceInfoTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		IDb::InstanceInfoTabRow& rowbuf = *m_pRow;
		cols["ProcessId"] == rowbuf.field_ProcessId;
		cols["ProcessVersion"] == rowbuf.field_ProcessVersion;
	}
};

class DtlInstanceInfoTab : public IDb::InstanceInfoTab {

public:
	DtlInstanceInfoTab(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlInstanceInfoTab() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		IDb::InstanceInfoTab::Edit();
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
	DtlInstanceInfoTabDeleteBinder m_deleteBinder;
	DtlDbRecordSet<IDb::InstanceInfoTab, IDb::InstanceInfoTabRow> m_dtlRecSet;

};


}                         


#endif

