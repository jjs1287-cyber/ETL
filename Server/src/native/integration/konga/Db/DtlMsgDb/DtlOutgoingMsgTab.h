/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRMSGDB_OUTGOINGMSGTAB_H
#define KONGA_DTL_CRMSGDB_OUTGOINGMSGTAB_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/MsgDb/OutgoingMsgTab.h>

namespace dtl {

template<> class DefaultSelValidate<MsgDb::OutgoingMsgTabRow>
{
public:
	bool operator()(BoundIOs &cols, MsgDb::OutgoingMsgTabRow &r)
	{
		r.IsMsgIDNull = cols["MsgID"].IsNull();
		r.IsMsgFromNull = cols["MsgFrom"].IsNull();
		r.IsMsgToNull = cols["MsgTo"].IsNull();
		r.IsMsgSubjectNull = cols["MsgSubject"].IsNull();
		r.IsMessageFileNameNull = cols["MessageFileName"].IsNull();
		r.IsFormatNull = cols["Format"].IsNull();
		r.IsSentAtNull = cols["SentAt"].IsNull();
		r.IsURLNull = cols["URL"].IsNull();
		r.IsMsgCCNull = cols["MsgCC"].IsNull();
		r.IsMsgBCCNull = cols["MsgBCC"].IsNull();
		r.IsDocumentIdNull = cols["DocumentId"].IsNull();
		r.IsProcessIdNull = cols["ProcessId"].IsNull();
		r.IsProcessVersionNull = cols["ProcessVersion"].IsNull();
		r.IsMhtmlUrlNull = cols["MhtmlUrl"].IsNull();
		r.IsProcessInstanceIdNull = cols["ProcessInstanceId"].IsNull();
		r.IsTaskIdNull = cols["TaskId"].IsNull();
		r.IsTaskInstanceIdNull = cols["TaskInstanceId"].IsNull();
		r.IsMsgReplyToNull = cols["MsgReplyTo"].IsNull();
		r.IsMsgPriorityNull = cols["MsgPriority"].IsNull();
		r.IsOwnerNull = cols["Owner"].IsNull();
		r.IsInsertTimeNull = cols["InsertTime"].IsNull();
		r.IsModTimeNull = cols["ModTime"].IsNull();
		  
                                        
                       
    
		return true;
	}
};

template<> class DefaultInsValidate<MsgDb::OutgoingMsgTabRow>
{
public:
	bool operator()(BoundIOs &cols, MsgDb::OutgoingMsgTabRow &r)
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
		if( r.IsMessageFileNameNull )
			cols["MessageFileName"].SetNull();
		else
			cols["MessageFileName"].ClearNull();
		if( r.IsFormatNull )
			cols["Format"].SetNull();
		else
			cols["Format"].ClearNull();
		if( r.IsSentAtNull )
			cols["SentAt"].SetNull();
		else
			cols["SentAt"].ClearNull();
		if( r.IsURLNull )
			cols["URL"].SetNull();
		else
			cols["URL"].ClearNull();
		if( r.IsMsgCCNull )
			cols["MsgCC"].SetNull();
		else
			cols["MsgCC"].ClearNull();
		if( r.IsMsgBCCNull )
			cols["MsgBCC"].SetNull();
		else
			cols["MsgBCC"].ClearNull();
		if( r.IsDocumentIdNull )
			cols["DocumentId"].SetNull();
		else
			cols["DocumentId"].ClearNull();
		if( r.IsProcessIdNull )
			cols["ProcessId"].SetNull();
		else
			cols["ProcessId"].ClearNull();
		if( r.IsProcessVersionNull )
			cols["ProcessVersion"].SetNull();
		else
			cols["ProcessVersion"].ClearNull();
		if( r.IsMhtmlUrlNull )
			cols["MhtmlUrl"].SetNull();
		else
			cols["MhtmlUrl"].ClearNull();
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
		if( r.IsMsgReplyToNull )
			cols["MsgReplyTo"].SetNull();
		else
			cols["MsgReplyTo"].ClearNull();
		if( r.IsMsgPriorityNull )
			cols["MsgPriority"].SetNull();
		else
			cols["MsgPriority"].ClearNull();
		if( r.IsOwnerNull )
			cols["Owner"].SetNull();
		else
			cols["Owner"].ClearNull();
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

template<> class DefaultBCA<MsgDb::OutgoingMsgTabRow>
{
public:
	void operator()(BoundIOs &cols, MsgDb::OutgoingMsgTabRow &rowbuf)
	{
		cols["MsgID"] == rowbuf.field_MsgID;
		cols["MsgFrom"] == rowbuf.field_MsgFrom;
		cols["MsgTo"] == rowbuf.field_MsgTo;
		cols["MsgSubject"] == rowbuf.field_MsgSubject;
		cols["MessageFileName"] == rowbuf.field_MessageFileName;
		cols["Format"] == rowbuf.field_Format;
		cols["SentAt"] == rowbuf.field_SentAt;
		cols["URL"] == rowbuf.field_URL;
		cols["MsgCC"] == rowbuf.field_MsgCC;
		cols["MsgBCC"] == rowbuf.field_MsgBCC;
		cols["DocumentId"] == rowbuf.field_DocumentId;
		cols["ProcessId"] == rowbuf.field_ProcessId;
		cols["ProcessVersion"] == rowbuf.field_ProcessVersion;
		cols["MhtmlUrl"] == rowbuf.field_MhtmlUrl;
		cols["ProcessInstanceId"] == rowbuf.field_ProcessInstanceId;
		cols["TaskId"] == rowbuf.field_TaskId;
		cols["TaskInstanceId"] == rowbuf.field_TaskInstanceId;
		cols["MsgReplyTo"] == rowbuf.field_MsgReplyTo;
		cols["MsgPriority"] == rowbuf.field_MsgPriority;
		cols["Owner"] == rowbuf.field_Owner;
		cols["InsertTime"] == rowbuf.field_InsertTime;
		cols["ModTime"] == rowbuf.field_ModTime;
		  
                                        
                       
    
	}
};


}                     



namespace dtl_msgdb {


class DtlOutgoingMsgTabInsertBinder : public CustomBinder<MsgDb::OutgoingMsgTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		MsgDb::OutgoingMsgTabRow& rowbuf = *m_pRow;
		cols["MsgFrom"] << rowbuf.field_MsgFrom;
		cols["MsgTo"] << rowbuf.field_MsgTo;
		cols["MsgSubject"] << rowbuf.field_MsgSubject;
		cols["MessageFileName"] << rowbuf.field_MessageFileName;
		cols["Format"] << rowbuf.field_Format;
		cols["SentAt"] << rowbuf.field_SentAt;
		cols["URL"] << rowbuf.field_URL;
		cols["MsgCC"] << rowbuf.field_MsgCC;
		cols["MsgBCC"] << rowbuf.field_MsgBCC;
		cols["DocumentId"] << rowbuf.field_DocumentId;
		cols["ProcessId"] << rowbuf.field_ProcessId;
		cols["ProcessVersion"] << rowbuf.field_ProcessVersion;
		cols["MhtmlUrl"] << rowbuf.field_MhtmlUrl;
		cols["ProcessInstanceId"] << rowbuf.field_ProcessInstanceId;
		cols["TaskId"] << rowbuf.field_TaskId;
		cols["TaskInstanceId"] << rowbuf.field_TaskInstanceId;
		cols["MsgReplyTo"] << rowbuf.field_MsgReplyTo;
		cols["MsgPriority"] << rowbuf.field_MsgPriority;
		cols["Owner"] << rowbuf.field_Owner;
		cols["InsertTime"] << rowbuf.field_InsertTime;
		cols["ModTime"] << rowbuf.field_ModTime;
	}
};

class DtlOutgoingMsgTabDeleteBinder : public CustomBinder<MsgDb::OutgoingMsgTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		MsgDb::OutgoingMsgTabRow& rowbuf = *m_pRow;
		cols["MsgID"] == rowbuf.field_MsgID;
	}
};

class DtlOutgoingMsgTab : public MsgDb::OutgoingMsgTab {

public:
	DtlOutgoingMsgTab(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlOutgoingMsgTab() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		MsgDb::OutgoingMsgTab::Edit();
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
	DtlOutgoingMsgTabInsertBinder m_insertBinder;
	DtlOutgoingMsgTabDeleteBinder m_deleteBinder;
	DtlDbRecordSet<MsgDb::OutgoingMsgTab, MsgDb::OutgoingMsgTabRow> m_dtlRecSet;

};


}                           


#endif

