/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRPDB_ACTIVITYTAB_H
#define KONGA_DTL_CRPDB_ACTIVITYTAB_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/PDb/ActivityTab.h>

namespace dtl {

template<> class DefaultSelValidate<PDb::ActivityTabRow>
{
public:
	bool operator()(BoundIOs &cols, PDb::ActivityTabRow &r)
	{
		r.IsProcessIdNull = cols["ProcessId"].IsNull();
		r.IsProcessVersionNull = cols["ProcessVersion"].IsNull();
		r.IsActivityIdNull = cols["ActivityId"].IsNull();
		r.IsActivityNameNull = cols["ActivityName"].IsNull();
		r.IsActivityMethodNull = cols["ActivityMethod"].IsNull();
		r.IsActivityLocationNull = cols["ActivityLocation"].IsNull();
		r.IsErrorHandlerIdNull = cols["ErrorHandlerId"].IsNull();
		r.IsHasInstanceNull = cols["HasInstance"].IsNull();
		r.IsInsertTimeNull = cols["InsertTime"].IsNull();
		r.IsModTimeNull = cols["ModTime"].IsNull();
		  
                                        
                       
    
		return true;
	}
};

template<> class DefaultInsValidate<PDb::ActivityTabRow>
{
public:
	bool operator()(BoundIOs &cols, PDb::ActivityTabRow &r)
	{
		if( r.IsProcessIdNull )
			cols["ProcessId"].SetNull();
		else
			cols["ProcessId"].ClearNull();
		if( r.IsProcessVersionNull )
			cols["ProcessVersion"].SetNull();
		else
			cols["ProcessVersion"].ClearNull();
		if( r.IsActivityIdNull )
			cols["ActivityId"].SetNull();
		else
			cols["ActivityId"].ClearNull();
		if( r.IsActivityNameNull )
			cols["ActivityName"].SetNull();
		else
			cols["ActivityName"].ClearNull();
		if( r.IsActivityMethodNull )
			cols["ActivityMethod"].SetNull();
		else
			cols["ActivityMethod"].ClearNull();
		if( r.IsActivityLocationNull )
			cols["ActivityLocation"].SetNull();
		else
			cols["ActivityLocation"].ClearNull();
		if( r.IsErrorHandlerIdNull )
			cols["ErrorHandlerId"].SetNull();
		else
			cols["ErrorHandlerId"].ClearNull();
		if( r.IsHasInstanceNull )
			cols["HasInstance"].SetNull();
		else
			cols["HasInstance"].ClearNull();
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

template<> class DefaultBCA<PDb::ActivityTabRow>
{
public:
	void operator()(BoundIOs &cols, PDb::ActivityTabRow &rowbuf)
	{
		cols["ProcessId"] == rowbuf.field_ProcessId;
		cols["ProcessVersion"] == rowbuf.field_ProcessVersion;
		cols["ActivityId"] == rowbuf.field_ActivityId;
		cols["ActivityName"] == rowbuf.field_ActivityName;
		cols["ActivityMethod"] == rowbuf.field_ActivityMethod;
		cols["ActivityLocation"] == rowbuf.field_ActivityLocation;
		cols["ErrorHandlerId"] == rowbuf.field_ErrorHandlerId;
		cols["HasInstance"] == rowbuf.field_HasInstance;
		cols["InsertTime"] == rowbuf.field_InsertTime;
		cols["ModTime"] == rowbuf.field_ModTime;
		  
                                        
                       
    
	}
};


}                     



namespace dtl_pdb {

class DtlActivityTabDeleteBinder : public CustomBinder<PDb::ActivityTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		PDb::ActivityTabRow& rowbuf = *m_pRow;
		cols["ProcessId"] == rowbuf.field_ProcessId;
		cols["ProcessVersion"] == rowbuf.field_ProcessVersion;
		cols["ActivityId"] == rowbuf.field_ActivityId;
	}
};

class DtlActivityTab : public PDb::ActivityTab {

public:
	DtlActivityTab(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlActivityTab() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		PDb::ActivityTab::Edit();
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
	DtlActivityTabDeleteBinder m_deleteBinder;
	DtlDbRecordSet<PDb::ActivityTab, PDb::ActivityTabRow> m_dtlRecSet;

};


}                         


#endif

