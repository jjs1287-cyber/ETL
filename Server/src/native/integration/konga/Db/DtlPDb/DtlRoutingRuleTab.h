/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRPDB_ROUTINGRULETAB_H
#define KONGA_DTL_CRPDB_ROUTINGRULETAB_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/PDb/RoutingRuleTab.h>

namespace dtl {

template<> class DefaultSelValidate<PDb::RoutingRuleTabRow>
{
public:
	bool operator()(BoundIOs &cols, PDb::RoutingRuleTabRow &r)
	{
		r.IsProcessIdNull = cols["ProcessId"].IsNull();
		r.IsProcessVersionNull = cols["ProcessVersion"].IsNull();
		r.IsRoutingRuleIdNull = cols["RoutingRuleId"].IsNull();
		r.IsSequenceStackNull = cols["SequenceStack"].IsNull();
		r.IsDestinationTaskIdNull = cols["DestinationTaskId"].IsNull();
		r.IsTimeToEscalationNull = cols["TimeToEscalation"].IsNull();
		r.IsEscalationDestinationTaskIdNull = cols["EscalationDestinationTaskId"].IsNull();
		r.IsInsertTimeNull = cols["InsertTime"].IsNull();
		r.IsModTimeNull = cols["ModTime"].IsNull();
		  
                                        
                       
    
		return true;
	}
};

template<> class DefaultInsValidate<PDb::RoutingRuleTabRow>
{
public:
	bool operator()(BoundIOs &cols, PDb::RoutingRuleTabRow &r)
	{
		if( r.IsProcessIdNull )
			cols["ProcessId"].SetNull();
		else
			cols["ProcessId"].ClearNull();
		if( r.IsProcessVersionNull )
			cols["ProcessVersion"].SetNull();
		else
			cols["ProcessVersion"].ClearNull();
		if( r.IsRoutingRuleIdNull )
			cols["RoutingRuleId"].SetNull();
		else
			cols["RoutingRuleId"].ClearNull();
		if( r.IsSequenceStackNull )
			cols["SequenceStack"].SetNull();
		else
			cols["SequenceStack"].ClearNull();
		if( r.IsDestinationTaskIdNull )
			cols["DestinationTaskId"].SetNull();
		else
			cols["DestinationTaskId"].ClearNull();
		if( r.IsTimeToEscalationNull )
			cols["TimeToEscalation"].SetNull();
		else
			cols["TimeToEscalation"].ClearNull();
		if( r.IsEscalationDestinationTaskIdNull )
			cols["EscalationDestinationTaskId"].SetNull();
		else
			cols["EscalationDestinationTaskId"].ClearNull();
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

template<> class DefaultBCA<PDb::RoutingRuleTabRow>
{
public:
	void operator()(BoundIOs &cols, PDb::RoutingRuleTabRow &rowbuf)
	{
		cols["ProcessId"] == rowbuf.field_ProcessId;
		cols["ProcessVersion"] == rowbuf.field_ProcessVersion;
		cols["RoutingRuleId"] == rowbuf.field_RoutingRuleId;
		cols["SequenceStack"] == rowbuf.field_SequenceStack;
		cols["DestinationTaskId"] == rowbuf.field_DestinationTaskId;
		cols["TimeToEscalation"] == rowbuf.field_TimeToEscalation;
		cols["EscalationDestinationTaskId"] == rowbuf.field_EscalationDestinationTaskId;
		cols["InsertTime"] == rowbuf.field_InsertTime;
		cols["ModTime"] == rowbuf.field_ModTime;
		  
                                        
                       
    
	}
};


}                     



namespace dtl_pdb {

class DtlRoutingRuleTabDeleteBinder : public CustomBinder<PDb::RoutingRuleTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		PDb::RoutingRuleTabRow& rowbuf = *m_pRow;
		cols["ProcessId"] == rowbuf.field_ProcessId;
		cols["ProcessVersion"] == rowbuf.field_ProcessVersion;
		cols["RoutingRuleId"] == rowbuf.field_RoutingRuleId;
	}
};

class DtlRoutingRuleTab : public PDb::RoutingRuleTab {

public:
	DtlRoutingRuleTab(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlRoutingRuleTab() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		PDb::RoutingRuleTab::Edit();
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
	DtlRoutingRuleTabDeleteBinder m_deleteBinder;
	DtlDbRecordSet<PDb::RoutingRuleTab, PDb::RoutingRuleTabRow> m_dtlRecSet;

};


}                         


#endif

