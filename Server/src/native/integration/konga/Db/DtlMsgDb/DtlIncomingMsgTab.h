/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRMSGDB_INCOMINGMSGTAB_H
#define KONGA_DTL_CRMSGDB_INCOMINGMSGTAB_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/MsgDb/IncomingMsgTab.h>

namespace dtl {

template<> class DefaultSelValidate<MsgDb::IncomingMsgTabRow>
{
public:
	bool operator()(BoundIOs &cols, MsgDb::IncomingMsgTabRow &r)
	{
		r.IsMsgFromNull = cols["MsgFrom"].IsNull();
		r.IsMsgToNull = cols["MsgTo"].IsNull();
		r.IsMsgSubjectNull = cols["MsgSubject"].IsNull();
		r.IsProcessVariablesNull = cols["ProcessVariables"].IsNull();
		r.IsReceivedAtNull = cols["ReceivedAt"].IsNull();
		r.IsMsgIdNull = cols["MsgId"].IsNull();
		r.IsIsExecutingNull = cols["IsExecuting"].IsNull();
		r.IsOwnerNull = cols["Owner"].IsNull();
		r.IsMsgPriorityNull = cols["MsgPriority"].IsNull();
		r.IsNumThreadsNull = cols["NumThreads"].IsNull();
		r.IsMsgTypeNull = cols["MsgType"].IsNull();
		r.IsInsertTimeNull = cols["InsertTime"].IsNull();
		r.IsModTimeNull = cols["ModTime"].IsNull();
		  
                                        
                       
    
		return true;
	}
};

template<> class DefaultInsValidate<MsgDb::IncomingMsgTabRow>
{
public:
	bool operator()(BoundIOs &cols, MsgDb::IncomingMsgTabRow &r)
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
		if( r.IsIsExecutingNull )
			cols["IsExecuting"].SetNull();
		else
			cols["IsExecuting"].ClearNull();
		if( r.IsOwnerNull )
			cols["Owner"].SetNull();
		else
			cols["Owner"].ClearNull();
		if( r.IsMsgPriorityNull )
			cols["MsgPriority"].SetNull();
		else
			cols["MsgPriority"].ClearNull();
		if( r.IsNumThreadsNull )
			cols["NumThreads"].SetNull();
		else
			cols["NumThreads"].ClearNull();
		if( r.IsMsgTypeNull )
			cols["MsgType"].SetNull();
		else
			cols["MsgType"].ClearNull();
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

template<> class DefaultBCA<MsgDb::IncomingMsgTabRow>
{
public:
	void operator()(BoundIOs &cols, MsgDb::IncomingMsgTabRow &rowbuf)
	{
		cols["MsgFrom"] == rowbuf.field_MsgFrom;
		cols["MsgTo"] == rowbuf.field_MsgTo;
		cols["MsgSubject"] == rowbuf.field_MsgSubject;
		cols["ProcessVariables"] == rowbuf.field_ProcessVariables;
		cols["ReceivedAt"] == rowbuf.field_ReceivedAt;
		cols["MsgId"] == rowbuf.field_MsgId;
		cols["IsExecuting"] == rowbuf.field_IsExecuting;
		cols["Owner"] == rowbuf.field_Owner;
		cols["MsgPriority"] == rowbuf.field_MsgPriority;
		cols["NumThreads"] == rowbuf.field_NumThreads;
		cols["MsgType"] == rowbuf.field_MsgType;
		cols["InsertTime"] == rowbuf.field_InsertTime;
		cols["ModTime"] == rowbuf.field_ModTime;
		  
                                        
                       
    
	}
};


}                     



namespace dtl_msgdb {


class DtlIncomingMsgTabInsertBinder : public CustomBinder<MsgDb::IncomingMsgTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		MsgDb::IncomingMsgTabRow& rowbuf = *m_pRow;
		cols["MsgFrom"] << rowbuf.field_MsgFrom;
		cols["MsgTo"] << rowbuf.field_MsgTo;
		cols["MsgSubject"] << rowbuf.field_MsgSubject;
		cols["ProcessVariables"] << rowbuf.field_ProcessVariables;
		cols["ReceivedAt"] << rowbuf.field_ReceivedAt;
		cols["IsExecuting"] << rowbuf.field_IsExecuting;
		cols["Owner"] << rowbuf.field_Owner;
		cols["MsgPriority"] << rowbuf.field_MsgPriority;
		cols["NumThreads"] << rowbuf.field_NumThreads;
		cols["MsgType"] << rowbuf.field_MsgType;
		cols["InsertTime"] << rowbuf.field_InsertTime;
		cols["ModTime"] << rowbuf.field_ModTime;
	}
};

class DtlIncomingMsgTabDeleteBinder : public CustomBinder<MsgDb::IncomingMsgTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		MsgDb::IncomingMsgTabRow& rowbuf = *m_pRow;
		cols["MsgId"] == rowbuf.field_MsgId;
	}
};

class DtlIncomingMsgTab : public MsgDb::IncomingMsgTab {

public:
	DtlIncomingMsgTab(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlIncomingMsgTab() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		MsgDb::IncomingMsgTab::Edit();
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
	DtlIncomingMsgTabInsertBinder m_insertBinder;
	DtlIncomingMsgTabDeleteBinder m_deleteBinder;
	DtlDbRecordSet<MsgDb::IncomingMsgTab, MsgDb::IncomingMsgTabRow> m_dtlRecSet;

};


}                           


#endif

