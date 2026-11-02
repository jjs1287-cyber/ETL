/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRTRANDB_HISTORYLOGSVIEW_H
#define KONGA_DTL_CRTRANDB_HISTORYLOGSVIEW_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/TranDb/HistoryLogsView.h>

namespace dtl {

template<> class DefaultSelValidate<TranDb::HistoryLogsViewRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::HistoryLogsViewRow &r)
	{
		r.Isinterchangeproject_idNull = cols["interchangeproject_id"].IsNull();
		r.IsOperation_IDNull = cols["Operation_ID"].IsNull();
		r.IsOperation_NameNull = cols["Operation_Name"].IsNull();
		r.IsIDNull = cols["ID"].IsNull();
		r.IsStart_DateNull = cols["Start_Date"].IsNull();
		r.IsEnd_DateNull = cols["End_Date"].IsNull();
		r.IsTotal_FilesNull = cols["Total_Files"].IsNull();
		r.IsTotal_SizeNull = cols["Total_Size"].IsNull();
		return true;
	}
};

template<> class DefaultInsValidate<TranDb::HistoryLogsViewRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::HistoryLogsViewRow &r)
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
		if( r.IsStart_DateNull )
			cols["Start_Date"].SetNull();
		else
			cols["Start_Date"].ClearNull();
		if( r.IsEnd_DateNull )
			cols["End_Date"].SetNull();
		else
			cols["End_Date"].ClearNull();
		if( r.IsTotal_FilesNull )
			cols["Total_Files"].SetNull();
		else
			cols["Total_Files"].ClearNull();
		if( r.IsTotal_SizeNull )
			cols["Total_Size"].SetNull();
		else
			cols["Total_Size"].ClearNull();
		return true;
	}
};

template<> class DefaultBCA<TranDb::HistoryLogsViewRow>
{
public:
	void operator()(BoundIOs &cols, TranDb::HistoryLogsViewRow &rowbuf)
	{
		cols["interchangeproject_id"] == rowbuf.field_interchangeproject_id;
		cols["Operation_ID"] == rowbuf.field_Operation_ID;
		cols["Operation_Name"] == rowbuf.field_Operation_Name;
		cols["ID"] == rowbuf.field_ID;
		cols["Start_Date"] == rowbuf.field_Start_Date;
		cols["End_Date"] == rowbuf.field_End_Date;
		cols["Total_Files"] == rowbuf.field_Total_Files;
		cols["Total_Size"] == rowbuf.field_Total_Size;
	}
};


}                     



namespace dtl_trandb {


class DtlHistoryLogsViewInsertBinder : public CustomBinder<TranDb::HistoryLogsViewRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		TranDb::HistoryLogsViewRow& rowbuf = *m_pRow;
		cols["interchangeproject_id"] << rowbuf.field_interchangeproject_id;
		cols["Operation_ID"] << rowbuf.field_Operation_ID;
		cols["Operation_Name"] << rowbuf.field_Operation_Name;
		cols["Start_Date"] << rowbuf.field_Start_Date;
		cols["End_Date"] << rowbuf.field_End_Date;
		cols["Total_Files"] << rowbuf.field_Total_Files;
		cols["Total_Size"] << rowbuf.field_Total_Size;
	}
};

class DtlHistoryLogsViewDeleteBinder : public CustomBinder<TranDb::HistoryLogsViewRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		TranDb::HistoryLogsViewRow& rowbuf = *m_pRow;
		cols["interchangeproject_id"] == rowbuf.field_interchangeproject_id;
		cols["Operation_ID"] == rowbuf.field_Operation_ID;
		cols["Operation_Name"] == rowbuf.field_Operation_Name;
		cols["ID"] == rowbuf.field_ID;
		cols["Start_Date"] == rowbuf.field_Start_Date;
		cols["End_Date"] == rowbuf.field_End_Date;
		cols["Total_Files"] == rowbuf.field_Total_Files;
		cols["Total_Size"] == rowbuf.field_Total_Size;
	}
};

class DtlHistoryLogsView : public TranDb::HistoryLogsView {

public:
	DtlHistoryLogsView(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlHistoryLogsView() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		TranDb::HistoryLogsView::Edit();
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
	DtlHistoryLogsViewInsertBinder m_insertBinder;
	DtlHistoryLogsViewDeleteBinder m_deleteBinder;
	DtlDbRecordSet<TranDb::HistoryLogsView, TranDb::HistoryLogsViewRow> m_dtlRecSet;

};


}                            


#endif

