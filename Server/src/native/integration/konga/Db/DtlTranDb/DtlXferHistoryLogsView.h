/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRTRANDB_XFERHISTORYLOGSVIEW_H
#define KONGA_DTL_CRTRANDB_XFERHISTORYLOGSVIEW_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/TranDb/XferHistoryLogsView.h>

namespace dtl {

template<> class DefaultSelValidate<TranDb::XferHistoryLogsViewRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::XferHistoryLogsViewRow &r)
	{
		r.Isinterchangeproject_idNull = cols["interchangeproject_id"].IsNull();
		r.IsOperation_IDNull = cols["Operation_ID"].IsNull();
		r.IsOperation_NameNull = cols["Operation_Name"].IsNull();
		r.IsIDNull = cols["ID"].IsNull();
		r.IsFileNameNull = cols["FileName"].IsNull();
		r.IsFileSizeNull = cols["FileSize"].IsNull();
		r.IsXferDateTimeNull = cols["XferDateTime"].IsNull();
		return true;
	}
};

template<> class DefaultInsValidate<TranDb::XferHistoryLogsViewRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::XferHistoryLogsViewRow &r)
	{
		if( r.Isinterchangeproject_idNull )
			cols["interchangeproject_id"].SetNull();
		else
			cols["interchangeproject_id"].ClearNull();
		if( r.IsOperation_IDNull )
			cols["Operation_ID"].SetNull();
		else
			cols["Operation_ID"].ClearNull();
		if( r.IsOperation_NameNull )
			cols["Operation_Name"].SetNull();
		else
			cols["Operation_Name"].ClearNull();
		if( r.IsFileNameNull )
			cols["FileName"].SetNull();
		else
			cols["FileName"].ClearNull();
		if( r.IsFileSizeNull )
			cols["FileSize"].SetNull();
		else
			cols["FileSize"].ClearNull();
		if( r.IsXferDateTimeNull )
			cols["XferDateTime"].SetNull();
		else
			cols["XferDateTime"].ClearNull();
		return true;
	}
};

template<> class DefaultBCA<TranDb::XferHistoryLogsViewRow>
{
public:
	void operator()(BoundIOs &cols, TranDb::XferHistoryLogsViewRow &rowbuf)
	{
		cols["interchangeproject_id"] == rowbuf.field_interchangeproject_id;
		cols["Operation_ID"] == rowbuf.field_Operation_ID;
		cols["Operation_Name"] == rowbuf.field_Operation_Name;
		cols["ID"] == rowbuf.field_ID;
		cols["FileName"] == rowbuf.field_FileName;
		cols["FileSize"] == rowbuf.field_FileSize;
		cols["XferDateTime"] == rowbuf.field_XferDateTime;
	}
};


}                     



namespace dtl_trandb {


class DtlXferHistoryLogsViewInsertBinder : public CustomBinder<TranDb::XferHistoryLogsViewRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		TranDb::XferHistoryLogsViewRow& rowbuf = *m_pRow;
		cols["interchangeproject_id"] << rowbuf.field_interchangeproject_id;
		cols["Operation_ID"] << rowbuf.field_Operation_ID;
		cols["Operation_Name"] << rowbuf.field_Operation_Name;
		cols["FileName"] << rowbuf.field_FileName;
		cols["FileSize"] << rowbuf.field_FileSize;
		cols["XferDateTime"] << rowbuf.field_XferDateTime;
	}
};

class DtlXferHistoryLogsViewDeleteBinder : public CustomBinder<TranDb::XferHistoryLogsViewRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		TranDb::XferHistoryLogsViewRow& rowbuf = *m_pRow;
		cols["interchangeproject_id"] == rowbuf.field_interchangeproject_id;
		cols["Operation_ID"] == rowbuf.field_Operation_ID;
		cols["Operation_Name"] == rowbuf.field_Operation_Name;
		cols["ID"] == rowbuf.field_ID;
		cols["FileName"] == rowbuf.field_FileName;
		cols["FileSize"] == rowbuf.field_FileSize;
		cols["XferDateTime"] == rowbuf.field_XferDateTime;
	}
};

class DtlXferHistoryLogsView : public TranDb::XferHistoryLogsView {

public:
	DtlXferHistoryLogsView(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlXferHistoryLogsView() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		TranDb::XferHistoryLogsView::Edit();
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
	DtlXferHistoryLogsViewInsertBinder m_insertBinder;
	DtlXferHistoryLogsViewDeleteBinder m_deleteBinder;
	DtlDbRecordSet<TranDb::XferHistoryLogsView, TranDb::XferHistoryLogsViewRow> m_dtlRecSet;

};


}                            


#endif

