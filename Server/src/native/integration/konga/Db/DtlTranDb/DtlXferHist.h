/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRTRANDB_XFERHIST_H
#define KONGA_DTL_CRTRANDB_XFERHIST_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/TranDb/XferHist.h>

namespace dtl {

template<> class DefaultSelValidate<TranDb::XferHistRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::XferHistRow &r)
	{
		r.IsIDNull = cols["ID"].IsNull();
		r.IsDirEntryNull = cols["DirEntry"].IsNull();
		r.IsFileNameNull = cols["FileName"].IsNull();
		r.IsOperationIDNull = cols["OperationID"].IsNull();
		r.IsFileSizeNull = cols["FileSize"].IsNull();
		r.IsFileMonthNull = cols["FileMonth"].IsNull();
		r.IsFileDayNull = cols["FileDay"].IsNull();
		r.IsFileTimeOrYearNull = cols["FileTimeOrYear"].IsNull();
		r.IsXferDateTimeNull = cols["XferDateTime"].IsNull();
		r.IsProgressStatusNull = cols["ProgressStatus"].IsNull();
		r.IsRetryCountNull = cols["RetryCount"].IsNull();
		r.IsLocalFileNameNull = cols["LocalFileName"].IsNull();
		r.IsInsertTimeNull = cols["InsertTime"].IsNull();
		r.IsModTimeNull = cols["ModTime"].IsNull();
		  
                                        
                       
    
		return true;
	}
};

template<> class DefaultInsValidate<TranDb::XferHistRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::XferHistRow &r)
	{
		if( r.IsDirEntryNull )
			cols["DirEntry"].SetNull();
		else
			cols["DirEntry"].ClearNull();
		if( r.IsFileNameNull )
			cols["FileName"].SetNull();
		else
			cols["FileName"].ClearNull();
		if( r.IsOperationIDNull )
			cols["OperationID"].SetNull();
		else
			cols["OperationID"].ClearNull();
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

template<> class DefaultBCA<TranDb::XferHistRow>
{
public:
	void operator()(BoundIOs &cols, TranDb::XferHistRow &rowbuf)
	{
		cols["ID"] == rowbuf.field_ID;
		cols["DirEntry"] == rowbuf.field_DirEntry;
		cols["FileName"] == rowbuf.field_FileName;
		cols["OperationID"] == rowbuf.field_OperationID;
		cols["FileSize"] == rowbuf.field_FileSize;
		cols["FileMonth"] == rowbuf.field_FileMonth;
		cols["FileDay"] == rowbuf.field_FileDay;
		cols["FileTimeOrYear"] == rowbuf.field_FileTimeOrYear;
		cols["XferDateTime"] == rowbuf.field_XferDateTime;
		cols["ProgressStatus"] == rowbuf.field_ProgressStatus;
		cols["RetryCount"] == rowbuf.field_RetryCount;
		cols["LocalFileName"] == rowbuf.field_LocalFileName;
		cols["InsertTime"] == rowbuf.field_InsertTime;
		cols["ModTime"] == rowbuf.field_ModTime;
		  
                                        
                       
    
	}
};


}                     



namespace dtl_trandb {


class DtlXferHistInsertBinder : public CustomBinder<TranDb::XferHistRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		TranDb::XferHistRow& rowbuf = *m_pRow;
		cols["DirEntry"] << rowbuf.field_DirEntry;
		cols["FileName"] << rowbuf.field_FileName;
		cols["OperationID"] << rowbuf.field_OperationID;
		cols["FileSize"] << rowbuf.field_FileSize;
		cols["FileMonth"] << rowbuf.field_FileMonth;
		cols["FileDay"] << rowbuf.field_FileDay;
		cols["FileTimeOrYear"] << rowbuf.field_FileTimeOrYear;
		cols["XferDateTime"] << rowbuf.field_XferDateTime;
		cols["ProgressStatus"] << rowbuf.field_ProgressStatus;
		cols["RetryCount"] << rowbuf.field_RetryCount;
		cols["LocalFileName"] << rowbuf.field_LocalFileName;
		cols["InsertTime"] << rowbuf.field_InsertTime;
		cols["ModTime"] << rowbuf.field_ModTime;
	}
};

class DtlXferHistDeleteBinder : public CustomBinder<TranDb::XferHistRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		TranDb::XferHistRow& rowbuf = *m_pRow;
		cols["ID"] == rowbuf.field_ID;
		cols["DirEntry"] == rowbuf.field_DirEntry;
		cols["FileName"] == rowbuf.field_FileName;
		cols["OperationID"] == rowbuf.field_OperationID;
		cols["FileSize"] == rowbuf.field_FileSize;
		cols["FileMonth"] == rowbuf.field_FileMonth;
		cols["FileDay"] == rowbuf.field_FileDay;
		cols["FileTimeOrYear"] == rowbuf.field_FileTimeOrYear;
		cols["XferDateTime"] == rowbuf.field_XferDateTime;
		cols["ProgressStatus"] == rowbuf.field_ProgressStatus;
		cols["RetryCount"] == rowbuf.field_RetryCount;
		cols["LocalFileName"] == rowbuf.field_LocalFileName;
		cols["InsertTime"] == rowbuf.field_InsertTime;
		cols["ModTime"] == rowbuf.field_ModTime;
	}
};

class DtlXferHist : public TranDb::XferHist {

public:
	DtlXferHist(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlXferHist() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		TranDb::XferHist::Edit();
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
	DtlXferHistInsertBinder m_insertBinder;
	DtlXferHistDeleteBinder m_deleteBinder;
	DtlDbRecordSet<TranDb::XferHist, TranDb::XferHistRow> m_dtlRecSet;

};


}                            


#endif

