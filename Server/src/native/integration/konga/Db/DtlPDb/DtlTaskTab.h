/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRPDB_TASKTAB_H
#define KONGA_DTL_CRPDB_TASKTAB_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/PDb/TaskTab.h>

namespace dtl {

template<> class DefaultSelValidate<PDb::TaskTabRow>
{
public:
	bool operator()(BoundIOs &cols, PDb::TaskTabRow &r)
	{
		r.IsProcessIdNull = cols["ProcessId"].IsNull();
		r.IsProcessVersionNull = cols["ProcessVersion"].IsNull();
		r.IsTaskIdNull = cols["TaskId"].IsNull();
		r.IsTaskNameNull = cols["TaskName"].IsNull();
		r.IsDescriptionNull = cols["Description"].IsNull();
		r.IsErrorHandlerIdNull = cols["ErrorHandlerId"].IsNull();
		r.IsTaskRoleNull = cols["TaskRole"].IsNull();
		r.IsDelegationRoleNull = cols["DelegationRole"].IsNull();
		r.IsRoutingRuleIdNull = cols["RoutingRuleId"].IsNull();
		r.IsDocumentIdListNull = cols["DocumentIdList"].IsNull();
		r.IsActivityRuleIdNull = cols["ActivityRuleId"].IsNull();
		r.IsPreRoleActivityRuleIdNull = cols["PreRoleActivityRuleId"].IsNull();
		r.IsTaskTypeNull = cols["TaskType"].IsNull();
		r.IsTaskPriorityNull = cols["TaskPriority"].IsNull();
		r.IsInsertTimeNull = cols["InsertTime"].IsNull();
		r.IsModTimeNull = cols["ModTime"].IsNull();
		  
                                        
                       
    
		return true;
	}
};

template<> class DefaultInsValidate<PDb::TaskTabRow>
{
public:
	bool operator()(BoundIOs &cols, PDb::TaskTabRow &r)
	{
		if( r.IsProcessIdNull )
			cols["ProcessId"].SetNull();
		else
			cols["ProcessId"].ClearNull();
		if( r.IsProcessVersionNull )
			cols["ProcessVersion"].SetNull();
		else
			cols["ProcessVersion"].ClearNull();
		if( r.IsTaskIdNull )
			cols["TaskId"].SetNull();
		else
			cols["TaskId"].ClearNull();
		if( r.IsTaskNameNull )
			cols["TaskName"].SetNull();
		else
			cols["TaskName"].ClearNull();
		if( r.IsDescriptionNull )
			cols["Description"].SetNull();
		else
			cols["Description"].ClearNull();
		if( r.IsErrorHandlerIdNull )
			cols["ErrorHandlerId"].SetNull();
		else
			cols["ErrorHandlerId"].ClearNull();
		if( r.IsTaskRoleNull )
			cols["TaskRole"].SetNull();
		else
			cols["TaskRole"].ClearNull();
		if( r.IsDelegationRoleNull )
			cols["DelegationRole"].SetNull();
		else
			cols["DelegationRole"].ClearNull();
		if( r.IsRoutingRuleIdNull )
			cols["RoutingRuleId"].SetNull();
		else
			cols["RoutingRuleId"].ClearNull();
		if( r.IsDocumentIdListNull )
			cols["DocumentIdList"].SetNull();
		else
			cols["DocumentIdList"].ClearNull();
		if( r.IsActivityRuleIdNull )
			cols["ActivityRuleId"].SetNull();
		else
			cols["ActivityRuleId"].ClearNull();
		if( r.IsPreRoleActivityRuleIdNull )
			cols["PreRoleActivityRuleId"].SetNull();
		else
			cols["PreRoleActivityRuleId"].ClearNull();
		if( r.IsTaskTypeNull )
			cols["TaskType"].SetNull();
		else
			cols["TaskType"].ClearNull();
		if( r.IsTaskPriorityNull )
			cols["TaskPriority"].SetNull();
		else
			cols["TaskPriority"].ClearNull();
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

template<> class DefaultBCA<PDb::TaskTabRow>
{
public:
	void operator()(BoundIOs &cols, PDb::TaskTabRow &rowbuf)
	{
		cols["ProcessId"] == rowbuf.field_ProcessId;
		cols["ProcessVersion"] == rowbuf.field_ProcessVersion;
		cols["TaskId"] == rowbuf.field_TaskId;
		cols["TaskName"] == rowbuf.field_TaskName;
		cols["Description"] == rowbuf.field_Description;
		cols["ErrorHandlerId"] == rowbuf.field_ErrorHandlerId;
		cols["TaskRole"] == rowbuf.field_TaskRole;
		cols["DelegationRole"] == rowbuf.field_DelegationRole;
		cols["RoutingRuleId"] == rowbuf.field_RoutingRuleId;
		cols["DocumentIdList"] == rowbuf.field_DocumentIdList;
		cols["ActivityRuleId"] == rowbuf.field_ActivityRuleId;
		cols["PreRoleActivityRuleId"] == rowbuf.field_PreRoleActivityRuleId;
		cols["TaskType"] == rowbuf.field_TaskType;
		cols["TaskPriority"] == rowbuf.field_TaskPriority;
		cols["InsertTime"] == rowbuf.field_InsertTime;
		cols["ModTime"] == rowbuf.field_ModTime;
		  
                                        
                       
    
	}
};


}                     



namespace dtl_pdb {

class DtlTaskTabDeleteBinder : public CustomBinder<PDb::TaskTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		PDb::TaskTabRow& rowbuf = *m_pRow;
		cols["ProcessId"] == rowbuf.field_ProcessId;
		cols["ProcessVersion"] == rowbuf.field_ProcessVersion;
		cols["TaskId"] == rowbuf.field_TaskId;
	}
};

class DtlTaskTab : public PDb::TaskTab {

public:
	DtlTaskTab(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlTaskTab() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		PDb::TaskTab::Edit();
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
	DtlTaskTabDeleteBinder m_deleteBinder;
	DtlDbRecordSet<PDb::TaskTab, PDb::TaskTabRow> m_dtlRecSet;

};


}                         


#endif

