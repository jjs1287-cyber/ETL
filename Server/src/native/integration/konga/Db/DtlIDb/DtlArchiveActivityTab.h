/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRIDB_ARCHIVEACTIVITYTAB_H
#define KONGA_DTL_CRIDB_ARCHIVEACTIVITYTAB_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/IDb/ArchiveActivityTab.h>

namespace dtl {

template<> class DefaultSelValidate<IDb::ArchiveActivityTabRow>
{
public:
	bool operator()(BoundIOs &cols, IDb::ArchiveActivityTabRow &r)
	{
		r.IsArchiveIdNull = cols["ArchiveId"].IsNull();
		r.IsProcessIdNull = cols["ProcessId"].IsNull();
		r.IsTaskIdNull = cols["TaskId"].IsNull();
		r.IsActivityIdNull = cols["ActivityId"].IsNull();
		r.IsProcessInstanceIdNull = cols["ProcessInstanceId"].IsNull();
		r.IsTaskInstanceIdNull = cols["TaskInstanceId"].IsNull();
		r.IsActivityInstanceIdNull = cols["ActivityInstanceId"].IsNull();
		r.IsStartTimeNull = cols["StartTime"].IsNull();
		r.IsEndTimeNull = cols["EndTime"].IsNull();
		r.IsStatusTimeNull = cols["StatusTime"].IsNull();
		r.IsStatusNull = cols["Status"].IsNull();
		r.IsInsertTimeNull = cols["InsertTime"].IsNull();
		r.IsModTimeNull = cols["ModTime"].IsNull();
		  
                                        
                       
    
		return true;
	}
};

template<> class DefaultInsValidate<IDb::ArchiveActivityTabRow>
{
public:
	bool operator()(BoundIOs &cols, IDb::ArchiveActivityTabRow &r)
	{
		if( r.IsProcessIdNull )
			cols["ProcessId"].SetNull();
		else
			cols["ProcessId"].ClearNull();
		if( r.IsTaskIdNull )
			cols["TaskId"].SetNull();
		else
			cols["TaskId"].ClearNull();
		if( r.IsActivityIdNull )
			cols["ActivityId"].SetNull();
		else
			cols["ActivityId"].ClearNull();
		if( r.IsProcessInstanceIdNull )
			cols["ProcessInstanceId"].SetNull();
		else
			cols["ProcessInstanceId"].ClearNull();
		if( r.IsTaskInstanceIdNull )
			cols["TaskInstanceId"].SetNull();
		else
			cols["TaskInstanceId"].ClearNull();
		if( r.IsActivityInstanceIdNull )
			cols["ActivityInstanceId"].SetNull();
		else
			cols["ActivityInstanceId"].ClearNull();
		if( r.IsStartTimeNull )
			cols["StartTime"].SetNull();
		else
			cols["StartTime"].ClearNull();
		if( r.IsEndTimeNull )
			cols["EndTime"].SetNull();
		else
			cols["EndTime"].ClearNull();
		if( r.IsStatusTimeNull )
			cols["StatusTime"].SetNull();
		else
			cols["StatusTime"].ClearNull();
		if( r.IsStatusNull )
			cols["Status"].SetNull();
		else
			cols["Status"].ClearNull();
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

template<> class DefaultBCA<IDb::ArchiveActivityTabRow>
{
public:
	void operator()(BoundIOs &cols, IDb::ArchiveActivityTabRow &rowbuf)
	{
		cols["ArchiveId"] == rowbuf.field_ArchiveId;
		cols["ProcessId"] == rowbuf.field_ProcessId;
		cols["TaskId"] == rowbuf.field_TaskId;
		cols["ActivityId"] == rowbuf.field_ActivityId;
		cols["ProcessInstanceId"] == rowbuf.field_ProcessInstanceId;
		cols["TaskInstanceId"] == rowbuf.field_TaskInstanceId;
		cols["ActivityInstanceId"] == rowbuf.field_ActivityInstanceId;
		cols["StartTime"] == rowbuf.field_StartTime;
		cols["EndTime"] == rowbuf.field_EndTime;
		cols["StatusTime"] == rowbuf.field_StatusTime;
		cols["Status"] == rowbuf.field_Status;
		cols["InsertTime"] == rowbuf.field_InsertTime;
		cols["ModTime"] == rowbuf.field_ModTime;
		  
                                        
                       
    
	}
};


}                     



namespace dtl_idb {


class DtlArchiveActivityTabInsertBinder : public CustomBinder<IDb::ArchiveActivityTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		IDb::ArchiveActivityTabRow& rowbuf = *m_pRow;
		cols["ProcessId"] << rowbuf.field_ProcessId;
		cols["TaskId"] << rowbuf.field_TaskId;
		cols["ActivityId"] << rowbuf.field_ActivityId;
		cols["ProcessInstanceId"] << rowbuf.field_ProcessInstanceId;
		cols["TaskInstanceId"] << rowbuf.field_TaskInstanceId;
		cols["ActivityInstanceId"] << rowbuf.field_ActivityInstanceId;
		cols["StartTime"] << rowbuf.field_StartTime;
		cols["EndTime"] << rowbuf.field_EndTime;
		cols["StatusTime"] << rowbuf.field_StatusTime;
		cols["Status"] << rowbuf.field_Status;
		cols["InsertTime"] << rowbuf.field_InsertTime;
		cols["ModTime"] << rowbuf.field_ModTime;
	}
};

class DtlArchiveActivityTabDeleteBinder : public CustomBinder<IDb::ArchiveActivityTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		IDb::ArchiveActivityTabRow& rowbuf = *m_pRow;
		cols["ArchiveId"] == rowbuf.field_ArchiveId;
	}
};

class DtlArchiveActivityTab : public IDb::ArchiveActivityTab {

public:
	DtlArchiveActivityTab(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlArchiveActivityTab() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		IDb::ArchiveActivityTab::Edit();
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
	DtlArchiveActivityTabInsertBinder m_insertBinder;
	DtlArchiveActivityTabDeleteBinder m_deleteBinder;
	DtlDbRecordSet<IDb::ArchiveActivityTab, IDb::ArchiveActivityTabRow> m_dtlRecSet;

};


}                         


#endif

