/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRMSGDB_ERRORMSGTAB_H
#define KONGA_DTL_CRMSGDB_ERRORMSGTAB_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/MsgDb/ErrorMsgTab.h>

namespace dtl {

template<> class DefaultSelValidate<MsgDb::ErrorMsgTabRow>
{
public:
	bool operator()(BoundIOs &cols, MsgDb::ErrorMsgTabRow &r)
	{
		r.IsErrorMsgIdNull = cols["ErrorMsgId"].IsNull();
		r.IsMsgFromNull = cols["MsgFrom"].IsNull();
		r.IsMsgToNull = cols["MsgTo"].IsNull();
		r.IsMsgSubjectNull = cols["MsgSubject"].IsNull();
		r.IsProcessVariablesNull = cols["ProcessVariables"].IsNull();
		r.IsReceivedAtNull = cols["ReceivedAt"].IsNull();
		r.IsMsgIdNull = cols["MsgId"].IsNull();
		r.IsProcessIdNull = cols["ProcessId"].IsNull();
		r.IsProcessInstanceIdNull = cols["ProcessInstanceId"].IsNull();
		r.IsTaskIdNull = cols["TaskId"].IsNull();
		r.IsTaskInstanceIdNull = cols["TaskInstanceId"].IsNull();
		r.IsEngineErrorIdNull = cols["EngineErrorId"].IsNull();
		r.IsEngineErrorTextNull = cols["EngineErrorText"].IsNull();
		r.IsOwnerNull = cols["Owner"].IsNull();
		r.IsMsgPriorityNull = cols["MsgPriority"].IsNull();
		r.IsInsertTimeNull = cols["InsertTime"].IsNull();
		r.IsModTimeNull = cols["ModTime"].IsNull();
		  
                                        
           
    
		return true;
	}
};

template<> class DefaultInsValidate<MsgDb::ErrorMsgTabRow>
{
public:
	bool operator()(BoundIOs &cols, MsgDb::ErrorMsgTabRow &r)
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
		if( r.IsMsgIdNull )
			cols["MsgId"].SetNull();
		else
			cols["MsgId"].ClearNull();
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
		if( r.IsEngineErrorIdNull )
			cols["EngineErrorId"].SetNull();
		else
			cols["EngineErrorId"].ClearNull();
		if( r.IsEngineErrorTextNull )
			cols["EngineErrorText"].SetNull();
		else
			cols["EngineErrorText"].ClearNull();
		if( r.IsOwnerNull )
			cols["Owner"].SetNull();
		else
			cols["Owner"].ClearNull();
		if( r.IsMsgPriorityNull )
			cols["MsgPriority"].SetNull();
		else
			cols["MsgPriority"].ClearNull();
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

template<> class DefaultBCA<MsgDb::ErrorMsgTabRow>
{
public:
	void operator()(BoundIOs &cols, MsgDb::ErrorMsgTabRow &rowbuf)
	{
		cols["ErrorMsgId"] == rowbuf.field_ErrorMsgId;
		cols["MsgFrom"] == rowbuf.field_MsgFrom;
		cols["MsgTo"] == rowbuf.field_MsgTo;
		cols["MsgSubject"] == rowbuf.field_MsgSubject;
		cols["ProcessVariables"] == rowbuf.field_ProcessVariables;
		cols["ReceivedAt"] == rowbuf.field_ReceivedAt;
		cols["MsgId"] == rowbuf.field_MsgId;
		cols["ProcessId"] == rowbuf.field_ProcessId;
		cols["ProcessInstanceId"] == rowbuf.field_ProcessInstanceId;
		cols["TaskId"] == rowbuf.field_TaskId;
		cols["TaskInstanceId"] == rowbuf.field_TaskInstanceId;
		cols["EngineErrorId"] == rowbuf.field_EngineErrorId;
		cols["EngineErrorText"] == rowbuf.field_EngineErrorText;
		cols["Owner"] == rowbuf.field_Owner;
		cols["MsgPriority"] == rowbuf.field_MsgPriority;
		cols["InsertTime"] == rowbuf.field_InsertTime;
		cols["ModTime"] == rowbuf.field_ModTime;
		  
                                        
           
    
	}
};


}                     



namespace dtl_msgdb {


class DtlErrorMsgTabInsertBinder : public CustomBinder<MsgDb::ErrorMsgTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		MsgDb::ErrorMsgTabRow& rowbuf = *m_pRow;
		cols["MsgFrom"] << rowbuf.field_MsgFrom;
		cols["MsgTo"] << rowbuf.field_MsgTo;
		cols["MsgSubject"] << rowbuf.field_MsgSubject;
		cols["ProcessVariables"] << rowbuf.field_ProcessVariables;
		cols["ReceivedAt"] << rowbuf.field_ReceivedAt;
		cols["MsgId"] << rowbuf.field_MsgId;
		cols["ProcessId"] << rowbuf.field_ProcessId;
		cols["ProcessInstanceId"] << rowbuf.field_ProcessInstanceId;
		cols["TaskId"] << rowbuf.field_TaskId;
		cols["TaskInstanceId"] << rowbuf.field_TaskInstanceId;
		cols["EngineErrorId"] << rowbuf.field_EngineErrorId;
		cols["EngineErrorText"] << rowbuf.field_EngineErrorText;
		cols["Owner"] << rowbuf.field_Owner;
		cols["MsgPriority"] << rowbuf.field_MsgPriority;
		cols["InsertTime"] << rowbuf.field_InsertTime;
		cols["ModTime"] << rowbuf.field_ModTime;
	}
};

class DtlErrorMsgTabDeleteBinder : public CustomBinder<MsgDb::ErrorMsgTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		MsgDb::ErrorMsgTabRow& rowbuf = *m_pRow;
		cols["ErrorMsgId"] == rowbuf.field_ErrorMsgId;
	}
};

class DtlErrorMsgTab : public MsgDb::ErrorMsgTab {

public:
	DtlErrorMsgTab(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlErrorMsgTab() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		MsgDb::ErrorMsgTab::Edit();
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
	DtlErrorMsgTabInsertBinder m_insertBinder;
	DtlErrorMsgTabDeleteBinder m_deleteBinder;
	DtlDbRecordSet<MsgDb::ErrorMsgTab, MsgDb::ErrorMsgTabRow> m_dtlRecSet;

};


}                           


#endif

