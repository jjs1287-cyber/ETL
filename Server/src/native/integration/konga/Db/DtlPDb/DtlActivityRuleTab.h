/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRPDB_ACTIVITYRULETAB_H
#define KONGA_DTL_CRPDB_ACTIVITYRULETAB_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/PDb/ActivityRuleTab.h>

namespace dtl {

template<> class DefaultSelValidate<PDb::ActivityRuleTabRow>
{
public:
	bool operator()(BoundIOs &cols, PDb::ActivityRuleTabRow &r)
	{
		r.IsProcessIdNull = cols["ProcessId"].IsNull();
		r.IsProcessVersionNull = cols["ProcessVersion"].IsNull();
		r.IsActivityRuleIdNull = cols["ActivityRuleId"].IsNull();
		r.IsActivityIdNull = cols["ActivityId"].IsNull();
		r.IsTrueActivityListNull = cols["TrueActivityList"].IsNull();
		r.IsFalseActivityListNull = cols["FalseActivityList"].IsNull();
		r.IsErrorHandlerIdNull = cols["ErrorHandlerId"].IsNull();
		r.IsInsertTimeNull = cols["InsertTime"].IsNull();
		r.IsModTimeNull = cols["ModTime"].IsNull();
		  
                                        
                       
    
		return true;
	}
};

template<> class DefaultInsValidate<PDb::ActivityRuleTabRow>
{
public:
	bool operator()(BoundIOs &cols, PDb::ActivityRuleTabRow &r)
	{
		if( r.IsProcessIdNull )
			cols["ProcessId"].SetNull();
		else
			cols["ProcessId"].ClearNull();
		if( r.IsProcessVersionNull )
			cols["ProcessVersion"].SetNull();
		else
			cols["ProcessVersion"].ClearNull();
		if( r.IsActivityRuleIdNull )
			cols["ActivityRuleId"].SetNull();
		else
			cols["ActivityRuleId"].ClearNull();
		if( r.IsActivityIdNull )
			cols["ActivityId"].SetNull();
		else
			cols["ActivityId"].ClearNull();
		if( r.IsTrueActivityListNull )
			cols["TrueActivityList"].SetNull();
		else
			cols["TrueActivityList"].ClearNull();
		if( r.IsFalseActivityListNull )
			cols["FalseActivityList"].SetNull();
		else
			cols["FalseActivityList"].ClearNull();
		if( r.IsErrorHandlerIdNull )
			cols["ErrorHandlerId"].SetNull();
		else
			cols["ErrorHandlerId"].ClearNull();
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

template<> class DefaultBCA<PDb::ActivityRuleTabRow>
{
public:
	void operator()(BoundIOs &cols, PDb::ActivityRuleTabRow &rowbuf)
	{
		cols["ProcessId"] == rowbuf.field_ProcessId;
		cols["ProcessVersion"] == rowbuf.field_ProcessVersion;
		cols["ActivityRuleId"] == rowbuf.field_ActivityRuleId;
		cols["ActivityId"] == rowbuf.field_ActivityId;
		cols["TrueActivityList"] == rowbuf.field_TrueActivityList;
		cols["FalseActivityList"] == rowbuf.field_FalseActivityList;
		cols["ErrorHandlerId"] == rowbuf.field_ErrorHandlerId;
		cols["InsertTime"] == rowbuf.field_InsertTime;
		cols["ModTime"] == rowbuf.field_ModTime;
		  
                                        
                       
    
	}
};


}                     



namespace dtl_pdb {

class DtlActivityRuleTabDeleteBinder : public CustomBinder<PDb::ActivityRuleTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		PDb::ActivityRuleTabRow& rowbuf = *m_pRow;
		cols["ProcessId"] == rowbuf.field_ProcessId;
		cols["ProcessVersion"] == rowbuf.field_ProcessVersion;
		cols["ActivityRuleId"] == rowbuf.field_ActivityRuleId;
	}
};

class DtlActivityRuleTab : public PDb::ActivityRuleTab {

public:
	DtlActivityRuleTab(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlActivityRuleTab() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		PDb::ActivityRuleTab::Edit();
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
	DtlActivityRuleTabDeleteBinder m_deleteBinder;
	DtlDbRecordSet<PDb::ActivityRuleTab, PDb::ActivityRuleTabRow> m_dtlRecSet;

};


}                         


#endif

