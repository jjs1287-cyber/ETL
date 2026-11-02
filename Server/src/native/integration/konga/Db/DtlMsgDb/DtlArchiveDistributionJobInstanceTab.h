/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRMSGDB_ARCHIVEDISTRIBUTIONJOBINSTANCETAB_H
#define KONGA_DTL_CRMSGDB_ARCHIVEDISTRIBUTIONJOBINSTANCETAB_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/MsgDb/ArchiveDistributionJobInstanceTab.h>

namespace dtl {

template<> class DefaultSelValidate<MsgDb::ArchiveDistributionJobInstanceTabRow>
{
public:
	bool operator()(BoundIOs &cols, MsgDb::ArchiveDistributionJobInstanceTabRow &r)
	{
		r.IsArc_IDNull = cols["Arc_ID"].IsNull();
		r.IsJobInstanceIdNull = cols["JobInstanceId"].IsNull();
		r.IsJobIdNull = cols["JobId"].IsNull();
		r.IsMachineNameNull = cols["MachineName"].IsNull();
		r.IsCompletionStatusNull = cols["CompletionStatus"].IsNull();
		r.IsErrorTextNull = cols["ErrorText"].IsNull();
		r.IsMsgIdNull = cols["MsgId"].IsNull();
		r.IsInsertTimeNull = cols["InsertTime"].IsNull();
		r.IsModTimeNull = cols["ModTime"].IsNull();
		  
                                        
                       
    
		return true;
	}
};

template<> class DefaultInsValidate<MsgDb::ArchiveDistributionJobInstanceTabRow>
{
public:
	bool operator()(BoundIOs &cols, MsgDb::ArchiveDistributionJobInstanceTabRow &r)
	{
		if( r.IsJobInstanceIdNull )
			cols["JobInstanceId"].SetNull();
		else
			cols["JobInstanceId"].ClearNull();
		if( r.IsJobIdNull )
			cols["JobId"].SetNull();
		else
			cols["JobId"].ClearNull();
		if( r.IsMachineNameNull )
			cols["MachineName"].SetNull();
		else
			cols["MachineName"].ClearNull();
		if( r.IsCompletionStatusNull )
			cols["CompletionStatus"].SetNull();
		else
			cols["CompletionStatus"].ClearNull();
		if( r.IsErrorTextNull )
			cols["ErrorText"].SetNull();
		else
			cols["ErrorText"].ClearNull();
		if( r.IsMsgIdNull )
			cols["MsgId"].SetNull();
		else
			cols["MsgId"].ClearNull();
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

template<> class DefaultBCA<MsgDb::ArchiveDistributionJobInstanceTabRow>
{
public:
	void operator()(BoundIOs &cols, MsgDb::ArchiveDistributionJobInstanceTabRow &rowbuf)
	{
		cols["Arc_ID"] == rowbuf.field_Arc_ID;
		cols["JobInstanceId"] == rowbuf.field_JobInstanceId;
		cols["JobId"] == rowbuf.field_JobId;
		cols["MachineName"] == rowbuf.field_MachineName;
		cols["CompletionStatus"] == rowbuf.field_CompletionStatus;
		cols["ErrorText"] == rowbuf.field_ErrorText;
		cols["MsgId"] == rowbuf.field_MsgId;
		cols["InsertTime"] == rowbuf.field_InsertTime;
		cols["ModTime"] == rowbuf.field_ModTime;
		  
                                        
                       
    
	}
};


}                     



namespace dtl_msgdb {


class DtlArchiveDistributionJobInstanceTabInsertBinder : public CustomBinder<MsgDb::ArchiveDistributionJobInstanceTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		MsgDb::ArchiveDistributionJobInstanceTabRow& rowbuf = *m_pRow;
		cols["JobInstanceId"] << rowbuf.field_JobInstanceId;
		cols["JobId"] << rowbuf.field_JobId;
		cols["MachineName"] << rowbuf.field_MachineName;
		cols["CompletionStatus"] << rowbuf.field_CompletionStatus;
		cols["ErrorText"] << rowbuf.field_ErrorText;
		cols["MsgId"] << rowbuf.field_MsgId;
		cols["InsertTime"] << rowbuf.field_InsertTime;
		cols["ModTime"] << rowbuf.field_ModTime;
	}
};

class DtlArchiveDistributionJobInstanceTabDeleteBinder : public CustomBinder<MsgDb::ArchiveDistributionJobInstanceTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		MsgDb::ArchiveDistributionJobInstanceTabRow& rowbuf = *m_pRow;
		cols["Arc_ID"] == rowbuf.field_Arc_ID;
	}
};

class DtlArchiveDistributionJobInstanceTab : public MsgDb::ArchiveDistributionJobInstanceTab {

public:
	DtlArchiveDistributionJobInstanceTab(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlArchiveDistributionJobInstanceTab() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		MsgDb::ArchiveDistributionJobInstanceTab::Edit();
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
	DtlArchiveDistributionJobInstanceTabInsertBinder m_insertBinder;
	DtlArchiveDistributionJobInstanceTabDeleteBinder m_deleteBinder;
	DtlDbRecordSet<MsgDb::ArchiveDistributionJobInstanceTab, MsgDb::ArchiveDistributionJobInstanceTabRow> m_dtlRecSet;

};


}                           


#endif

