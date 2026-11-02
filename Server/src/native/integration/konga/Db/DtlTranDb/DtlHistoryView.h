/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRTRANDB_HISTORYVIEW_H
#define KONGA_DTL_CRTRANDB_HISTORYVIEW_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/TranDb/HistoryView.h>

namespace dtl {

template<> class DefaultSelValidate<TranDb::HistoryViewRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::HistoryViewRow &r)
	{
		r.IsOperation_IDNull = cols["Operation_ID"].IsNull();
		r.IsProject_IDNull = cols["Project_ID"].IsNull();
		r.IsAccount_IDNull = cols["Account_ID"].IsNull();
		r.IsIDNull = cols["ID"].IsNull();
		r.IsDirEntryNull = cols["DirEntry"].IsNull();
		r.IsFileNameNull = cols["FileName"].IsNull();
		r.IsFileSizeNull = cols["FileSize"].IsNull();
		r.IsFileMonthNull = cols["FileMonth"].IsNull();
		r.IsFileDayNull = cols["FileDay"].IsNull();
		r.IsFileTimeOrYearNull = cols["FileTimeOrYear"].IsNull();
		r.IsXferDateTimeNull = cols["XferDateTime"].IsNull();
		r.IsProgressStatusNull = cols["ProgressStatus"].IsNull();
		r.IsRetryCountNull = cols["RetryCount"].IsNull();
		r.IsLocalFileNameNull = cols["LocalFileName"].IsNull();
		return true;
	}
};

template<> class DefaultInsValidate<TranDb::HistoryViewRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::HistoryViewRow &r)
	{
		if( r.IsOperation_IDNull )
			cols["Operation_ID"].SetNull();
		else
			cols["Operation_ID"].ClearNull();
		if( r.IsProject_IDNull )
			cols["Project_ID"].SetNull();
		else
			cols["Project_ID"].ClearNull();
		if( r.IsAccount_IDNull )
			cols["Account_ID"].SetNull();
		else
			cols["Account_ID"].ClearNull();
		if( r.IsDirEntryNull )
			cols["DirEntry"].SetNull();
		else
			cols["DirEntry"].ClearNull();
		if( r.IsFileNameNull )
			cols["FileName"].SetNull();
		else
			cols["FileName"].ClearNull();
		if( r.IsFileSizeNull )
			cols["FileSize"].SetNull();
		else
			cols["FileSize"].ClearNull();
		if( r.IsFileMonthNull )
			cols["FileMonth"].SetNull();
		else
			cols["FileMonth"].ClearNull();
		if( r.IsFileDayNull )
			cols["FileDay"].SetNull();
		else
			cols["FileDay"].ClearNull();
		if( r.IsFileTimeOrYearNull )
			cols["FileTimeOrYear"].SetNull();
		else
			cols["FileTimeOrYear"].ClearNull();
		if( r.IsXferDateTimeNull )
			cols["XferDateTime"].SetNull();
		else
			cols["XferDateTime"].ClearNull();
		if( r.IsProgressStatusNull )
			cols["ProgressStatus"].SetNull();
		else
			cols["ProgressStatus"].ClearNull();
		if( r.IsRetryCountNull )
			cols["RetryCount"].SetNull();
		else
			cols["RetryCount"].ClearNull();
		if( r.IsLocalFileNameNull )
			cols["LocalFileName"].SetNull();
		else
			cols["LocalFileName"].ClearNull();
		return true;
	}
};

template<> class DefaultBCA<TranDb::HistoryViewRow>
{
public:
	void operator()(BoundIOs &cols, TranDb::HistoryViewRow &rowbuf)
	{
		cols["Operation_ID"] == rowbuf.field_Operation_ID;
		cols["Project_ID"] == rowbuf.field_Project_ID;
		cols["Account_ID"] == rowbuf.field_Account_ID;
		cols["ID"] == rowbuf.field_ID;
		cols["DirEntry"] == rowbuf.field_DirEntry;
		cols["FileName"] == rowbuf.field_FileName;
		cols["FileSize"] == rowbuf.field_FileSize;
		cols["FileMonth"] == rowbuf.field_FileMonth;
		cols["FileDay"] == rowbuf.field_FileDay;
		cols["FileTimeOrYear"] == rowbuf.field_FileTimeOrYear;
		cols["XferDateTime"] == rowbuf.field_XferDateTime;
		cols["ProgressStatus"] == rowbuf.field_ProgressStatus;
		cols["RetryCount"] == rowbuf.field_RetryCount;
		cols["LocalFileName"] == rowbuf.field_LocalFileName;
	}
};


}                     



namespace dtl_trandb {


class DtlHistoryViewInsertBinder : public CustomBinder<TranDb::HistoryViewRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		TranDb::HistoryViewRow& rowbuf = *m_pRow;
		cols["Operation_ID"] << rowbuf.field_Operation_ID;
		cols["Project_ID"] << rowbuf.field_Project_ID;
		cols["Account_ID"] << rowbuf.field_Account_ID;
		cols["DirEntry"] << rowbuf.field_DirEntry;
		cols["FileName"] << rowbuf.field_FileName;
		cols["FileSize"] << rowbuf.field_FileSize;
		cols["FileMonth"] << rowbuf.field_FileMonth;
		cols["FileDay"] << rowbuf.field_FileDay;
		cols["FileTimeOrYear"] << rowbuf.field_FileTimeOrYear;
		cols["XferDateTime"] << rowbuf.field_XferDateTime;
		cols["ProgressStatus"] << rowbuf.field_ProgressStatus;
		cols["RetryCount"] << rowbuf.field_RetryCount;
		cols["LocalFileName"] << rowbuf.field_LocalFileName;
	}
};

class DtlHistoryViewDeleteBinder : public CustomBinder<TranDb::HistoryViewRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		TranDb::HistoryViewRow& rowbuf = *m_pRow;
		cols["Operation_ID"] == rowbuf.field_Operation_ID;
		cols["Project_ID"] == rowbuf.field_Project_ID;
		cols["Account_ID"] == rowbuf.field_Account_ID;
		cols["ID"] == rowbuf.field_ID;
		cols["DirEntry"] == rowbuf.field_DirEntry;
		cols["FileName"] == rowbuf.field_FileName;
		cols["FileSize"] == rowbuf.field_FileSize;
		cols["FileMonth"] == rowbuf.field_FileMonth;
		cols["FileDay"] == rowbuf.field_FileDay;
		cols["FileTimeOrYear"] == rowbuf.field_FileTimeOrYear;
		cols["XferDateTime"] == rowbuf.field_XferDateTime;
		cols["ProgressStatus"] == rowbuf.field_ProgressStatus;
		cols["RetryCount"] == rowbuf.field_RetryCount;
		cols["LocalFileName"] == rowbuf.field_LocalFileName;
	}
};

class DtlHistoryView : public TranDb::HistoryView {

public:
	DtlHistoryView(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlHistoryView() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		TranDb::HistoryView::Edit();
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
	DtlHistoryViewInsertBinder m_insertBinder;
	DtlHistoryViewDeleteBinder m_deleteBinder;
	DtlDbRecordSet<TranDb::HistoryView, TranDb::HistoryViewRow> m_dtlRecSet;

};


}                            


#endif

