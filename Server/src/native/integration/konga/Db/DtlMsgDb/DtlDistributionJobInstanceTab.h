/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRMSGDB_DISTRIBUTIONJOBINSTANCETAB_H
#define KONGA_DTL_CRMSGDB_DISTRIBUTIONJOBINSTANCETAB_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/MsgDb/DistributionJobInstanceTab.h>

namespace dtl {

template<> class DefaultSelValidate<MsgDb::DistributionJobInstanceTabRow>
{
public:
	bool operator()(BoundIOs &cols, MsgDb::DistributionJobInstanceTabRow &r)
	{
		r.IsJobInstanceIdNull = cols["JobInstanceId"].IsNull();
		r.IsJobIdNull = cols["JobId"].IsNull();
		r.IsMachineNameNull = cols["MachineName"].IsNull();
		r.IsCompletionStatusNull = cols["CompletionStatus"].IsNull();
		r.IsErrorTextNull = cols["ErrorText"].IsNull();
		r.IsMsgIdNull = cols["MsgId"].IsNull();
		r.IsFlagsNull = cols["Flags"].IsNull();
		r.IsInsertTimeNull = cols["InsertTime"].IsNull();
		r.IsModTimeNull = cols["ModTime"].IsNull();
		  
                                        
                       
    
		return true;
	}
};

template<> class DefaultInsValidate<MsgDb::DistributionJobInstanceTabRow>
{
public:
	bool operator()(BoundIOs &cols, MsgDb::DistributionJobInstanceTabRow &r)
	{
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
		if( r.IsFlagsNull )
			cols["Flags"].SetNull();
		else
			cols["Flags"].ClearNull();
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

template<> class DefaultBCA<MsgDb::DistributionJobInstanceTabRow>
{
public:
	void operator()(BoundIOs &cols, MsgDb::DistributionJobInstanceTabRow &rowbuf)
	{
		cols["JobInstanceId"] == rowbuf.field_JobInstanceId;
		cols["JobId"] == rowbuf.field_JobId;
		cols["MachineName"] == rowbuf.field_MachineName;
		cols["CompletionStatus"] == rowbuf.field_CompletionStatus;
		cols["ErrorText"] == rowbuf.field_ErrorText;
		cols["MsgId"] == rowbuf.field_MsgId;
		cols["Flags"] == rowbuf.field_Flags;
		cols["InsertTime"] == rowbuf.field_InsertTime;
		cols["ModTime"] == rowbuf.field_ModTime;
		  
                                        
                       
    
	}
};


}                     



namespace dtl_msgdb {


class DtlDistributionJobInstanceTabInsertBinder : public CustomBinder<MsgDb::DistributionJobInstanceTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		MsgDb::DistributionJobInstanceTabRow& rowbuf = *m_pRow;
		cols["JobId"] << rowbuf.field_JobId;
		cols["MachineName"] << rowbuf.field_MachineName;
		cols["CompletionStatus"] << rowbuf.field_CompletionStatus;
		cols["ErrorText"] << rowbuf.field_ErrorText;
		cols["MsgId"] << rowbuf.field_MsgId;
		cols["Flags"] << rowbuf.field_Flags;
		cols["InsertTime"] << rowbuf.field_InsertTime;
		cols["ModTime"] << rowbuf.field_ModTime;
	}
};

class DtlDistributionJobInstanceTabDeleteBinder : public CustomBinder<MsgDb::DistributionJobInstanceTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		MsgDb::DistributionJobInstanceTabRow& rowbuf = *m_pRow;
		cols["JobInstanceId"] == rowbuf.field_JobInstanceId;
	}
};

class DtlDistributionJobInstanceTab : public MsgDb::DistributionJobInstanceTab {

public:
	DtlDistributionJobInstanceTab(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlDistributionJobInstanceTab() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		MsgDb::DistributionJobInstanceTab::Edit();
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
	DtlDistributionJobInstanceTabInsertBinder m_insertBinder;
	DtlDistributionJobInstanceTabDeleteBinder m_deleteBinder;
	DtlDbRecordSet<MsgDb::DistributionJobInstanceTab, MsgDb::DistributionJobInstanceTabRow> m_dtlRecSet;

};


}                           


#endif

