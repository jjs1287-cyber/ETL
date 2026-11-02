/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRMSGDB_EVENTMSGTAB_H
#define KONGA_DTL_CRMSGDB_EVENTMSGTAB_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/MsgDb/EventMsgTab.h>

namespace dtl {

template<> class DefaultSelValidate<MsgDb::EventMsgTabRow>
{
public:
	bool operator()(BoundIOs &cols, MsgDb::EventMsgTabRow &r)
	{
		r.IsMsgFromNull = cols["MsgFrom"].IsNull();
		r.IsMsgToNull = cols["MsgTo"].IsNull();
		r.IsMsgSubjectNull = cols["MsgSubject"].IsNull();
		r.IsProcessVariablesNull = cols["ProcessVariables"].IsNull();
		r.IsReceivedAtNull = cols["ReceivedAt"].IsNull();
		r.IsScheduledAtNull = cols["ScheduledAt"].IsNull();
		r.IsProcessIdNull = cols["ProcessId"].IsNull();
		r.IsProcessInstanceIdNull = cols["ProcessInstanceId"].IsNull();
		r.IsTaskIdNull = cols["TaskId"].IsNull();
		r.IsTaskInstanceIdNull = cols["TaskInstanceId"].IsNull();
		r.IsMsgIdNull = cols["MsgId"].IsNull();
		r.IsInsertTimeNull = cols["InsertTime"].IsNull();
		r.IsModTimeNull = cols["ModTime"].IsNull();
		  
                                        
                       
    
		return true;
	}
};

template<> class DefaultInsValidate<MsgDb::EventMsgTabRow>
{
public:
	bool operator()(BoundIOs &cols, MsgDb::EventMsgTabRow &r)
	{
		if( r.IsMsgFromNull )
			cols["MsgFrom"].SetNull();
		else
			cols["MsgFrom"].ClearNull();
		if( r.IsMsgToNull )
			cols["MsgTo"].SetNull();
		else
			cols["MsgTo"].ClearNull();
		if( r.IsMsgSubjectNull )
			cols["MsgSubject"].SetNull();
		else
			cols["MsgSubject"].ClearNull();
		if( r.IsProcessVariablesNull )
			cols["ProcessVariables"].SetNull();
		else
			cols["ProcessVariables"].ClearNull();
		if( r.IsReceivedAtNull )
			cols["ReceivedAt"].SetNull();
		else
			cols["ReceivedAt"].ClearNull();
		if( r.IsScheduledAtNull )
			cols["ScheduledAt"].SetNull();
		else
			cols["ScheduledAt"].ClearNull();
		if( r.IsProcessIdNull )
			cols["ProcessId"].SetNull();
		else
			cols["ProcessId"].ClearNull();
		if( r.IsProcessInstanceIdNull )
			cols["ProcessInstanceId"].SetNull();
		else
			cols["ProcessInstanceId"].ClearNull();
		if( r.IsTaskIdNull )
			cols["TaskId"].SetNull();
		else
			cols["TaskId"].ClearNull();
		if( r.IsTaskInstanceIdNull )
			cols["TaskInstanceId"].SetNull();
		else
			cols["TaskInstanceId"].ClearNull();
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

template<> class DefaultBCA<MsgDb::EventMsgTabRow>
{
public:
	void operator()(BoundIOs &cols, MsgDb::EventMsgTabRow &rowbuf)
	{
		cols["MsgFrom"] == rowbuf.field_MsgFrom;
		cols["MsgTo"] == rowbuf.field_MsgTo;
		cols["MsgSubject"] == rowbuf.field_MsgSubject;
		cols["ProcessVariables"] == rowbuf.field_ProcessVariables;
		cols["ReceivedAt"] == rowbuf.field_ReceivedAt;
		cols["ScheduledAt"] == rowbuf.field_ScheduledAt;
		cols["ProcessId"] == rowbuf.field_ProcessId;
		cols["ProcessInstanceId"] == rowbuf.field_ProcessInstanceId;
		cols["TaskId"] == rowbuf.field_TaskId;
		cols["TaskInstanceId"] == rowbuf.field_TaskInstanceId;
		cols["MsgId"] == rowbuf.field_MsgId;
		cols["InsertTime"] == rowbuf.field_InsertTime;
		cols["ModTime"] == rowbuf.field_ModTime;
		  
                                        
                       
    
	}
};


}                     



namespace dtl_msgdb {


class DtlEventMsgTabInsertBinder : public CustomBinder<MsgDb::EventMsgTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		MsgDb::EventMsgTabRow& rowbuf = *m_pRow;
		cols["MsgFrom"] << rowbuf.field_MsgFrom;
		cols["MsgTo"] << rowbuf.field_MsgTo;
		cols["MsgSubject"] << rowbuf.field_MsgSubject;
		cols["ProcessVariables"] << rowbuf.field_ProcessVariables;
		cols["ReceivedAt"] << rowbuf.field_ReceivedAt;
		cols["ScheduledAt"] << rowbuf.field_ScheduledAt;
		cols["ProcessId"] << rowbuf.field_ProcessId;
		cols["ProcessInstanceId"] << rowbuf.field_ProcessInstanceId;
		cols["TaskId"] << rowbuf.field_TaskId;
		cols["TaskInstanceId"] << rowbuf.field_TaskInstanceId;
		cols["InsertTime"] << rowbuf.field_InsertTime;
		cols["ModTime"] << rowbuf.field_ModTime;
	}
};

class DtlEventMsgTabDeleteBinder : public CustomBinder<MsgDb::EventMsgTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		MsgDb::EventMsgTabRow& rowbuf = *m_pRow;
		cols["MsgId"] == rowbuf.field_MsgId;
	}
};

class DtlEventMsgTab : public MsgDb::EventMsgTab {

public:
	DtlEventMsgTab(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlEventMsgTab() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		MsgDb::EventMsgTab::Edit();
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
	DtlEventMsgTabInsertBinder m_insertBinder;
	DtlEventMsgTabDeleteBinder m_deleteBinder;
	DtlDbRecordSet<MsgDb::EventMsgTab, MsgDb::EventMsgTabRow> m_dtlRecSet;

};


}                           


#endif

