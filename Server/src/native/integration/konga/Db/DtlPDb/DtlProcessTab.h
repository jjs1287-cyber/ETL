/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRPDB_PROCESSTAB_H
#define KONGA_DTL_CRPDB_PROCESSTAB_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/PDb/ProcessTab.h>

namespace dtl {

template<> class DefaultSelValidate<PDb::ProcessTabRow>
{
public:
	bool operator()(BoundIOs &cols, PDb::ProcessTabRow &r)
	{
		r.IsProcessIdNull = cols["ProcessId"].IsNull();
		r.IsProcessVersionNull = cols["ProcessVersion"].IsNull();
		r.IsProcessNameNull = cols["ProcessName"].IsNull();
		r.IsDescriptionNull = cols["Description"].IsNull();
		r.IsErrorHandlerIdNull = cols["ErrorHandlerId"].IsNull();
		r.IsOwnerNull = cols["Owner"].IsNull();
		r.IsProcessTypeNull = cols["ProcessType"].IsNull();
		r.IsInsertTimeNull = cols["InsertTime"].IsNull();
		r.IsModTimeNull = cols["ModTime"].IsNull();
		  
                                        
                       
    
		return true;
	}
};

template<> class DefaultInsValidate<PDb::ProcessTabRow>
{
public:
	bool operator()(BoundIOs &cols, PDb::ProcessTabRow &r)
	{
		if( r.IsProcessIdNull )
			cols["ProcessId"].SetNull();
		else
			cols["ProcessId"].ClearNull();
		if( r.IsProcessVersionNull )
			cols["ProcessVersion"].SetNull();
		else
			cols["ProcessVersion"].ClearNull();
		if( r.IsProcessNameNull )
			cols["ProcessName"].SetNull();
		else
			cols["ProcessName"].ClearNull();
		if( r.IsDescriptionNull )
			cols["Description"].SetNull();
		else
			cols["Description"].ClearNull();
		if( r.IsErrorHandlerIdNull )
			cols["ErrorHandlerId"].SetNull();
		else
			cols["ErrorHandlerId"].ClearNull();
		if( r.IsOwnerNull )
			cols["Owner"].SetNull();
		else
			cols["Owner"].ClearNull();
		if( r.IsProcessTypeNull )
			cols["ProcessType"].SetNull();
		else
			cols["ProcessType"].ClearNull();
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

template<> class DefaultBCA<PDb::ProcessTabRow>
{
public:
	void operator()(BoundIOs &cols, PDb::ProcessTabRow &rowbuf)
	{
		cols["ProcessId"] == rowbuf.field_ProcessId;
		cols["ProcessVersion"] == rowbuf.field_ProcessVersion;
		cols["ProcessName"] == rowbuf.field_ProcessName;
		cols["Description"] == rowbuf.field_Description;
		cols["ErrorHandlerId"] == rowbuf.field_ErrorHandlerId;
		cols["Owner"] == rowbuf.field_Owner;
		cols["ProcessType"] == rowbuf.field_ProcessType;
		cols["InsertTime"] == rowbuf.field_InsertTime;
		cols["ModTime"] == rowbuf.field_ModTime;
		  
                                        
                       
    
	}
};


}                     



namespace dtl_pdb {

class DtlProcessTabDeleteBinder : public CustomBinder<PDb::ProcessTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		PDb::ProcessTabRow& rowbuf = *m_pRow;
		cols["ProcessId"] == rowbuf.field_ProcessId;
		cols["ProcessVersion"] == rowbuf.field_ProcessVersion;
	}
};

class DtlProcessTab : public PDb::ProcessTab {

public:
	DtlProcessTab(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlProcessTab() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		PDb::ProcessTab::Edit();
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
	DtlProcessTabDeleteBinder m_deleteBinder;
	DtlDbRecordSet<PDb::ProcessTab, PDb::ProcessTabRow> m_dtlRecSet;

};


}                         


#endif

