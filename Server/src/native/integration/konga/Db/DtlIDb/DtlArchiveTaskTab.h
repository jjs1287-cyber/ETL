/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRIDB_ARCHIVETASKTAB_H
#define KONGA_DTL_CRIDB_ARCHIVETASKTAB_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/IDb/ArchiveTaskTab.h>

namespace dtl {

template<> class DefaultSelValidate<IDb::ArchiveTaskTabRow>
{
public:
	bool operator()(BoundIOs &cols, IDb::ArchiveTaskTabRow &r)
	{
		r.IsArchiveIdNull = cols["ArchiveId"].IsNull();
		r.IsProcessIdNull = cols["ProcessId"].IsNull();
		r.IsProcessInstanceIdNull = cols["ProcessInstanceId"].IsNull();
		r.IsTaskIdNull = cols["TaskId"].IsNull();
		r.IsTaskInstanceIdNull = cols["TaskInstanceId"].IsNull();
		r.IsTaskUserNull = cols["TaskUser"].IsNull();
		r.IsInitTimeNull = cols["InitTime"].IsNull();
		r.IsStartTimeNull = cols["StartTime"].IsNull();
		r.IsStatusNull = cols["Status"].IsNull();
		r.IsDataElementValueListNull = cols["DataElementValueList"].IsNull();
		r.IsFromDocDataElementValueListNull = cols["FromDocDataElementValueList"].IsNull();
		r.IsEscalationIdNull = cols["EscalationId"].IsNull();
		r.IsEndTimeNull = cols["EndTime"].IsNull();
		r.IsMsgIdNull = cols["MsgId"].IsNull();
		r.IsMsgPriorityNull = cols["MsgPriority"].IsNull();
		r.IsInsertTimeNull = cols["InsertTime"].IsNull();
		r.IsModTimeNull = cols["ModTime"].IsNull();
		  
                                        
                       
    
		return true;
	}
};

template<> class DefaultInsValidate<IDb::ArchiveTaskTabRow>
{
public:
	bool operator()(BoundIOs &cols, IDb::ArchiveTaskTabRow &r)
	{
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
		if( r.IsTaskUserNull )
			cols["TaskUser"].SetNull();
		else
			cols["TaskUser"].ClearNull();
		if( r.IsInitTimeNull )
			cols["InitTime"].SetNull();
		else
			cols["InitTime"].ClearNull();
		if( r.IsStartTimeNull )
			cols["StartTime"].SetNull();
		else
			cols["StartTime"].ClearNull();
		if( r.IsStatusNull )
			cols["Status"].SetNull();
		else
			cols["Status"].ClearNull();
		if( r.IsDataElementValueListNull )
			cols["DataElementValueList"].SetNull();
		else
			cols["DataElementValueList"].ClearNull();
		if( r.IsFromDocDataElementValueListNull )
			cols["FromDocDataElementValueList"].SetNull();
		else
			cols["FromDocDataElementValueList"].ClearNull();
		if( r.IsEscalationIdNull )
			cols["EscalationId"].SetNull();
		else
			cols["EscalationId"].ClearNull();
		if( r.IsEndTimeNull )
			cols["EndTime"].SetNull();
		else
			cols["EndTime"].ClearNull();
		if( r.IsMsgIdNull )
			cols["MsgId"].SetNull();
		else
			cols["MsgId"].ClearNull();
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

template<> class DefaultBCA<IDb::ArchiveTaskTabRow>
{
public:
	void operator()(BoundIOs &cols, IDb::ArchiveTaskTabRow &rowbuf)
	{
		cols["ArchiveId"] == rowbuf.field_ArchiveId;
		cols["ProcessId"] == rowbuf.field_ProcessId;
		cols["ProcessInstanceId"] == rowbuf.field_ProcessInstanceId;
		cols["TaskId"] == rowbuf.field_TaskId;
		cols["TaskInstanceId"] == rowbuf.field_TaskInstanceId;
		cols["TaskUser"] == rowbuf.field_TaskUser;
		cols["InitTime"] == rowbuf.field_InitTime;
		cols["StartTime"] == rowbuf.field_StartTime;
		cols["Status"] == rowbuf.field_Status;
		cols["DataElementValueList"] == rowbuf.field_DataElementValueList;
		cols["FromDocDataElementValueList"] == rowbuf.field_FromDocDataElementValueList;
		cols["EscalationId"] == rowbuf.field_EscalationId;
		cols["EndTime"] == rowbuf.field_EndTime;
		cols["MsgId"] == rowbuf.field_MsgId;
		cols["MsgPriority"] == rowbuf.field_MsgPriority;
		cols["InsertTime"] == rowbuf.field_InsertTime;
		cols["ModTime"] == rowbuf.field_ModTime;
		  
                                        
                       
    
	}
};


}                     



namespace dtl_idb {


class DtlArchiveTaskTabInsertBinder : public CustomBinder<IDb::ArchiveTaskTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		IDb::ArchiveTaskTabRow& rowbuf = *m_pRow;
		cols["ProcessId"] << rowbuf.field_ProcessId;
		cols["ProcessInstanceId"] << rowbuf.field_ProcessInstanceId;
		cols["TaskId"] << rowbuf.field_TaskId;
		cols["TaskInstanceId"] << rowbuf.field_TaskInstanceId;
		cols["TaskUser"] << rowbuf.field_TaskUser;
		cols["InitTime"] << rowbuf.field_InitTime;
		cols["StartTime"] << rowbuf.field_StartTime;
		cols["Status"] << rowbuf.field_Status;
		cols["DataElementValueList"] << rowbuf.field_DataElementValueList;
		cols["FromDocDataElementValueList"] << rowbuf.field_FromDocDataElementValueList;
		cols["EscalationId"] << rowbuf.field_EscalationId;
		cols["EndTime"] << rowbuf.field_EndTime;
		cols["MsgId"] << rowbuf.field_MsgId;
		cols["MsgPriority"] << rowbuf.field_MsgPriority;
		cols["InsertTime"] << rowbuf.field_InsertTime;
		cols["ModTime"] << rowbuf.field_ModTime;
	}
};

class DtlArchiveTaskTabDeleteBinder : public CustomBinder<IDb::ArchiveTaskTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		IDb::ArchiveTaskTabRow& rowbuf = *m_pRow;
		cols["ArchiveId"] == rowbuf.field_ArchiveId;
	}
};

class DtlArchiveTaskTab : public IDb::ArchiveTaskTab {

public:
	DtlArchiveTaskTab(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlArchiveTaskTab() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		IDb::ArchiveTaskTab::Edit();
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
	DtlArchiveTaskTabInsertBinder m_insertBinder;
	DtlArchiveTaskTabDeleteBinder m_deleteBinder;
	DtlDbRecordSet<IDb::ArchiveTaskTab, IDb::ArchiveTaskTabRow> m_dtlRecSet;

};


}                         


#endif

