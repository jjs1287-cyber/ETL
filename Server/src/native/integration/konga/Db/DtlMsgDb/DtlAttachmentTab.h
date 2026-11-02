/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRMSGDB_ATTACHMENTTAB_H
#define KONGA_DTL_CRMSGDB_ATTACHMENTTAB_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/MsgDb/AttachmentTab.h>

namespace dtl {

template<> class DefaultSelValidate<MsgDb::AttachmentTabRow>
{
public:
	bool operator()(BoundIOs &cols, MsgDb::AttachmentTabRow &r)
	{
		r.IsAttachmentIdNull = cols["AttachmentId"].IsNull();
		r.IsProcessIdNull = cols["ProcessId"].IsNull();
		r.IsProcessInstanceIdNull = cols["ProcessInstanceId"].IsNull();
		r.IsTaskIdNull = cols["TaskId"].IsNull();
		r.IsTaskInstanceIdNull = cols["TaskInstanceId"].IsNull();
		r.IsMimeContentTypeNull = cols["MimeContentType"].IsNull();
		r.IsFileNameNull = cols["FileName"].IsNull();
		r.IsDeleteAfterAttachedNull = cols["DeleteAfterAttached"].IsNull();
		r.IsInsertTimeNull = cols["InsertTime"].IsNull();
		r.IsModTimeNull = cols["ModTime"].IsNull();
		  
                                        
                       
    
		return true;
	}
};

template<> class DefaultInsValidate<MsgDb::AttachmentTabRow>
{
public:
	bool operator()(BoundIOs &cols, MsgDb::AttachmentTabRow &r)
	{
		if( r.IsAttachmentIdNull )
			cols["AttachmentId"].SetNull();
		else
			cols["AttachmentId"].ClearNull();
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
		if( r.IsMimeContentTypeNull )
			cols["MimeContentType"].SetNull();
		else
			cols["MimeContentType"].ClearNull();
		if( r.IsFileNameNull )
			cols["FileName"].SetNull();
		else
			cols["FileName"].ClearNull();
		if( r.IsDeleteAfterAttachedNull )
			cols["DeleteAfterAttached"].SetNull();
		else
			cols["DeleteAfterAttached"].ClearNull();
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

template<> class DefaultBCA<MsgDb::AttachmentTabRow>
{
public:
	void operator()(BoundIOs &cols, MsgDb::AttachmentTabRow &rowbuf)
	{
		cols["AttachmentId"] == rowbuf.field_AttachmentId;
		cols["ProcessId"] == rowbuf.field_ProcessId;
		cols["ProcessInstanceId"] == rowbuf.field_ProcessInstanceId;
		cols["TaskId"] == rowbuf.field_TaskId;
		cols["TaskInstanceId"] == rowbuf.field_TaskInstanceId;
		cols["MimeContentType"] == rowbuf.field_MimeContentType;
		cols["FileName"] == rowbuf.field_FileName;
		cols["DeleteAfterAttached"] == rowbuf.field_DeleteAfterAttached;
		cols["InsertTime"] == rowbuf.field_InsertTime;
		cols["ModTime"] == rowbuf.field_ModTime;
		  
                                        
                       
    
	}
};


}                     



namespace dtl_msgdb {

class DtlAttachmentTabDeleteBinder : public CustomBinder<MsgDb::AttachmentTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		MsgDb::AttachmentTabRow& rowbuf = *m_pRow;
		cols["AttachmentId"] == rowbuf.field_AttachmentId;
		cols["ProcessId"] == rowbuf.field_ProcessId;
		cols["ProcessInstanceId"] == rowbuf.field_ProcessInstanceId;
		cols["TaskId"] == rowbuf.field_TaskId;
		cols["TaskInstanceId"] == rowbuf.field_TaskInstanceId;
	}
};

class DtlAttachmentTab : public MsgDb::AttachmentTab {

public:
	DtlAttachmentTab(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlAttachmentTab() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		MsgDb::AttachmentTab::Edit();
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
	DtlAttachmentTabDeleteBinder m_deleteBinder;
	DtlDbRecordSet<MsgDb::AttachmentTab, MsgDb::AttachmentTabRow> m_dtlRecSet;

};


}                           


#endif

