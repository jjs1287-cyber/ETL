/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRIDB_ARCHIVEPROCESSTAB_H
#define KONGA_DTL_CRIDB_ARCHIVEPROCESSTAB_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/IDb/ArchiveProcessTab.h>

namespace dtl {

template<> class DefaultSelValidate<IDb::ArchiveProcessTabRow>
{
public:
	bool operator()(BoundIOs &cols, IDb::ArchiveProcessTabRow &r)
	{
		r.IsArchiveIdNull = cols["ArchiveId"].IsNull();
		r.IsProcessIdNull = cols["ProcessId"].IsNull();
		r.IsProcessInstanceIdNull = cols["ProcessInstanceId"].IsNull();
		r.IsProcessVersionNull = cols["ProcessVersion"].IsNull();
		r.IsInitialTaskNull = cols["InitialTask"].IsNull();
		r.IsStartTimeNull = cols["StartTime"].IsNull();
		r.IsEndTimeNull = cols["EndTime"].IsNull();
		r.IsStatusNull = cols["Status"].IsNull();
		r.IsStatusTimeNull = cols["StatusTime"].IsNull();
		r.IsUpdateFlagNull = cols["UpdateFlag"].IsNull();
		r.IsProcessDataElementValueListNull = cols["ProcessDataElementValueList"].IsNull();
		r.IsInsertTimeNull = cols["InsertTime"].IsNull();
		r.IsModTimeNull = cols["ModTime"].IsNull();
		  
                                        
                       
    
		return true;
	}
};

template<> class DefaultInsValidate<IDb::ArchiveProcessTabRow>
{
public:
	bool operator()(BoundIOs &cols, IDb::ArchiveProcessTabRow &r)
	{
		if( r.IsProcessIdNull )
			cols["ProcessId"].SetNull();
		else
			cols["ProcessId"].ClearNull();
		if( r.IsProcessInstanceIdNull )
			cols["ProcessInstanceId"].SetNull();
		else
			cols["ProcessInstanceId"].ClearNull();
		if( r.IsProcessVersionNull )
			cols["ProcessVersion"].SetNull();
		else
			cols["ProcessVersion"].ClearNull();
		if( r.IsInitialTaskNull )
			cols["InitialTask"].SetNull();
		else
			cols["InitialTask"].ClearNull();
		if( r.IsStartTimeNull )
			cols["StartTime"].SetNull();
		else
			cols["StartTime"].ClearNull();
		if( r.IsEndTimeNull )
			cols["EndTime"].SetNull();
		else
			cols["EndTime"].ClearNull();
		if( r.IsStatusNull )
			cols["Status"].SetNull();
		else
			cols["Status"].ClearNull();
		if( r.IsStatusTimeNull )
			cols["StatusTime"].SetNull();
		else
			cols["StatusTime"].ClearNull();
		if( r.IsUpdateFlagNull )
			cols["UpdateFlag"].SetNull();
		else
			cols["UpdateFlag"].ClearNull();
		if( r.IsProcessDataElementValueListNull )
			cols["ProcessDataElementValueList"].SetNull();
		else
			cols["ProcessDataElementValueList"].ClearNull();
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

template<> class DefaultBCA<IDb::ArchiveProcessTabRow>
{
public:
	void operator()(BoundIOs &cols, IDb::ArchiveProcessTabRow &rowbuf)
	{
		cols["ArchiveId"] == rowbuf.field_ArchiveId;
		cols["ProcessId"] == rowbuf.field_ProcessId;
		cols["ProcessInstanceId"] == rowbuf.field_ProcessInstanceId;
		cols["ProcessVersion"] == rowbuf.field_ProcessVersion;
		cols["InitialTask"] == rowbuf.field_InitialTask;
		cols["StartTime"] == rowbuf.field_StartTime;
		cols["EndTime"] == rowbuf.field_EndTime;
		cols["Status"] == rowbuf.field_Status;
		cols["StatusTime"] == rowbuf.field_StatusTime;
		cols["UpdateFlag"] == rowbuf.field_UpdateFlag;
		cols["ProcessDataElementValueList"] == rowbuf.field_ProcessDataElementValueList;
		cols["InsertTime"] == rowbuf.field_InsertTime;
		cols["ModTime"] == rowbuf.field_ModTime;
		  
                                        
                       
    
	}
};


}                     



namespace dtl_idb {


class DtlArchiveProcessTabInsertBinder : public CustomBinder<IDb::ArchiveProcessTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		IDb::ArchiveProcessTabRow& rowbuf = *m_pRow;
		cols["ProcessId"] << rowbuf.field_ProcessId;
		cols["ProcessInstanceId"] << rowbuf.field_ProcessInstanceId;
		cols["ProcessVersion"] << rowbuf.field_ProcessVersion;
		cols["InitialTask"] << rowbuf.field_InitialTask;
		cols["StartTime"] << rowbuf.field_StartTime;
		cols["EndTime"] << rowbuf.field_EndTime;
		cols["Status"] << rowbuf.field_Status;
		cols["StatusTime"] << rowbuf.field_StatusTime;
		cols["UpdateFlag"] << rowbuf.field_UpdateFlag;
		cols["ProcessDataElementValueList"] << rowbuf.field_ProcessDataElementValueList;
		cols["InsertTime"] << rowbuf.field_InsertTime;
		cols["ModTime"] << rowbuf.field_ModTime;
	}
};

class DtlArchiveProcessTabDeleteBinder : public CustomBinder<IDb::ArchiveProcessTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		IDb::ArchiveProcessTabRow& rowbuf = *m_pRow;
		cols["ArchiveId"] == rowbuf.field_ArchiveId;
	}
};

class DtlArchiveProcessTab : public IDb::ArchiveProcessTab {

public:
	DtlArchiveProcessTab(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlArchiveProcessTab() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		IDb::ArchiveProcessTab::Edit();
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
	DtlArchiveProcessTabInsertBinder m_insertBinder;
	DtlArchiveProcessTabDeleteBinder m_deleteBinder;
	DtlDbRecordSet<IDb::ArchiveProcessTab, IDb::ArchiveProcessTabRow> m_dtlRecSet;

};


}                         


#endif

