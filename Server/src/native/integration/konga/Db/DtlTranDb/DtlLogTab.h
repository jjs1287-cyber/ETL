/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRTRANDB_LOGTAB_H
#define KONGA_DTL_CRTRANDB_LOGTAB_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/TranDb/LogTab.h>

namespace dtl {

template<> class DefaultSelValidate<TranDb::LogTabRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::LogTabRow &r)
	{
		r.IsLog_IDNull = cols["Log_ID"].IsNull();
		r.IsOperation_IDNull = cols["Operation_ID"].IsNull();
		r.IsTimestampNull = cols["Timestamp"].IsNull();
		r.IsLog_SourceNull = cols["Log_Source"].IsNull();
		r.IsShort_MessageNull = cols["Short_Message"].IsNull();
		r.IsSeverityNull = cols["Severity"].IsNull();
		r.IsError_CodeNull = cols["Error_Code"].IsNull();
		r.IsInsertTimeNull = cols["InsertTime"].IsNull();
		r.IsModTimeNull = cols["ModTime"].IsNull();
		  
                                        
                       
    
		return true;
	}
};

template<> class DefaultInsValidate<TranDb::LogTabRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::LogTabRow &r)
	{
		if( r.IsOperation_IDNull )
			cols["Operation_ID"].SetNull();
		else
			cols["Operation_ID"].ClearNull();
		if( r.IsTimestampNull )
			cols["Timestamp"].SetNull();
		else
			cols["Timestamp"].ClearNull();
		if( r.IsLog_SourceNull )
			cols["Log_Source"].SetNull();
		else
			cols["Log_Source"].ClearNull();
		if( r.IsShort_MessageNull )
			cols["Short_Message"].SetNull();
		else
			cols["Short_Message"].ClearNull();
		if( r.IsSeverityNull )
			cols["Severity"].SetNull();
		else
			cols["Severity"].ClearNull();
		if( r.IsError_CodeNull )
			cols["Error_Code"].SetNull();
		else
			cols["Error_Code"].ClearNull();
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

template<> class DefaultBCA<TranDb::LogTabRow>
{
public:
	void operator()(BoundIOs &cols, TranDb::LogTabRow &rowbuf)
	{
		cols["Log_ID"] == rowbuf.field_Log_ID;
		cols["Operation_ID"] == rowbuf.field_Operation_ID;
		cols["Timestamp"] == rowbuf.field_Timestamp;
		cols["Log_Source"] == rowbuf.field_Log_Source;
		cols["Short_Message"] == rowbuf.field_Short_Message;
		cols["Severity"] == rowbuf.field_Severity;
		cols["Error_Code"] == rowbuf.field_Error_Code;
		cols["InsertTime"] == rowbuf.field_InsertTime;
		cols["ModTime"] == rowbuf.field_ModTime;
		  
                                        
                       
    
	}
};


}                     



namespace dtl_trandb {


class DtlLogTabInsertBinder : public CustomBinder<TranDb::LogTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		TranDb::LogTabRow& rowbuf = *m_pRow;
		cols["Operation_ID"] << rowbuf.field_Operation_ID;
		cols["Timestamp"] << rowbuf.field_Timestamp;
		cols["Log_Source"] << rowbuf.field_Log_Source;
		cols["Short_Message"] << rowbuf.field_Short_Message;
		cols["Severity"] << rowbuf.field_Severity;
		cols["Error_Code"] << rowbuf.field_Error_Code;
		cols["InsertTime"] << rowbuf.field_InsertTime;
		cols["ModTime"] << rowbuf.field_ModTime;
	}
};

class DtlLogTabDeleteBinder : public CustomBinder<TranDb::LogTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		TranDb::LogTabRow& rowbuf = *m_pRow;
		cols["Log_ID"] == rowbuf.field_Log_ID;
	}
};

class DtlLogTab : public TranDb::LogTab {

public:
	DtlLogTab(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlLogTab() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		TranDb::LogTab::Edit();
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
	DtlLogTabInsertBinder m_insertBinder;
	DtlLogTabDeleteBinder m_deleteBinder;
	DtlDbRecordSet<TranDb::LogTab, TranDb::LogTabRow> m_dtlRecSet;

};


}                            


#endif

