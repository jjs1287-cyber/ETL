/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRMSGDB_ARCHIVEMSGTAB_H
#define KONGA_DTL_CRMSGDB_ARCHIVEMSGTAB_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/MsgDb/ArchiveMsgTab.h>

namespace dtl {

template<> class DefaultSelValidate<MsgDb::ArchiveMsgTabRow>
{
public:
	bool operator()(BoundIOs &cols, MsgDb::ArchiveMsgTabRow &r)
	{
		r.IsArchiveIdNull = cols["ArchiveId"].IsNull();
		r.IsMsgFromNull = cols["MsgFrom"].IsNull();
		r.IsMsgToNull = cols["MsgTo"].IsNull();
		r.IsMsgSubjectNull = cols["MsgSubject"].IsNull();
		r.IsProcessVariablesNull = cols["ProcessVariables"].IsNull();
		r.IsReceivedAtNull = cols["ReceivedAt"].IsNull();
		r.IsMsgIdNull = cols["MsgId"].IsNull();
		r.IsMsgReplyToNull = cols["MsgReplyTo"].IsNull();
		r.IsMsgPriorityNull = cols["MsgPriority"].IsNull();
		r.IsOwnerNull = cols["Owner"].IsNull();
		r.IsInsertTimeNull = cols["InsertTime"].IsNull();
		r.IsModTimeNull = cols["ModTime"].IsNull();
		  
                                        
                       
    
		return true;
	}
};

template<> class DefaultInsValidate<MsgDb::ArchiveMsgTabRow>
{
public:
	bool operator()(BoundIOs &cols, MsgDb::ArchiveMsgTabRow &r)
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

template<> class DefaultBCA<MsgDb::ArchiveMsgTabRow>
{
public:
	void operator()(BoundIOs &cols, MsgDb::ArchiveMsgTabRow &rowbuf)
	{
		cols["ArchiveId"] == rowbuf.field_ArchiveId;
		cols["MsgFrom"] == rowbuf.field_MsgFrom;
		cols["MsgTo"] == rowbuf.field_MsgTo;
		cols["MsgSubject"] == rowbuf.field_MsgSubject;
		cols["ProcessVariables"] == rowbuf.field_ProcessVariables;
		cols["ReceivedAt"] == rowbuf.field_ReceivedAt;
		cols["MsgId"] == rowbuf.field_MsgId;
		cols["MsgReplyTo"] == rowbuf.field_MsgReplyTo;
		cols["MsgPriority"] == rowbuf.field_MsgPriority;
		cols["Owner"] == rowbuf.field_Owner;
		cols["InsertTime"] == rowbuf.field_InsertTime;
		cols["ModTime"] == rowbuf.field_ModTime;
		  
                                        
                       
    
	}
};


}                     



namespace dtl_msgdb {


class DtlArchiveMsgTabInsertBinder : public CustomBinder<MsgDb::ArchiveMsgTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		MsgDb::ArchiveMsgTabRow& rowbuf = *m_pRow;
		cols["MsgFrom"] << rowbuf.field_MsgFrom;
		cols["MsgTo"] << rowbuf.field_MsgTo;
		cols["MsgSubject"] << rowbuf.field_MsgSubject;
		cols["ProcessVariables"] << rowbuf.field_ProcessVariables;
		cols["ReceivedAt"] << rowbuf.field_ReceivedAt;
		cols["MsgId"] << rowbuf.field_MsgId;
		cols["MsgReplyTo"] << rowbuf.field_MsgReplyTo;
		cols["MsgPriority"] << rowbuf.field_MsgPriority;
		cols["Owner"] << rowbuf.field_Owner;
		cols["InsertTime"] << rowbuf.field_InsertTime;
		cols["ModTime"] << rowbuf.field_ModTime;
	}
};

class DtlArchiveMsgTabDeleteBinder : public CustomBinder<MsgDb::ArchiveMsgTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		MsgDb::ArchiveMsgTabRow& rowbuf = *m_pRow;
		cols["ArchiveId"] == rowbuf.field_ArchiveId;
	}
};

class DtlArchiveMsgTab : public MsgDb::ArchiveMsgTab {

public:
	DtlArchiveMsgTab(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlArchiveMsgTab() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		MsgDb::ArchiveMsgTab::Edit();
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
	DtlArchiveMsgTabInsertBinder m_insertBinder;
	DtlArchiveMsgTabDeleteBinder m_deleteBinder;
	DtlDbRecordSet<MsgDb::ArchiveMsgTab, MsgDb::ArchiveMsgTabRow> m_dtlRecSet;

};


}                           


#endif

