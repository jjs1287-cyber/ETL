/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRTRANDB_OPERATIONLOGSVIEW_H
#define KONGA_DTL_CRTRANDB_OPERATIONLOGSVIEW_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/TranDb/OperationLogsView.h>

namespace dtl {

template<> class DefaultSelValidate<TranDb::OperationLogsViewRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::OperationLogsViewRow &r)
	{
		r.Isinterchangeproject_idNull = cols["interchangeproject_id"].IsNull();
		r.IsOperation_IDNull = cols["Operation_ID"].IsNull();
		r.IsOperation_NameNull = cols["Operation_Name"].IsNull();
		r.IsLog_IDNull = cols["Log_ID"].IsNull();
		r.IsTimestampNull = cols["Timestamp"].IsNull();
		r.IsShort_MessageNull = cols["Short_Message"].IsNull();
		r.IsSeverityNull = cols["Severity"].IsNull();
		return true;
	}
};

template<> class DefaultInsValidate<TranDb::OperationLogsViewRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::OperationLogsViewRow &r)
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
		if( r.IsTimestampNull )
			cols["Timestamp"].SetNull();
		else
			cols["Timestamp"].ClearNull();
		if( r.IsShort_MessageNull )
			cols["Short_Message"].SetNull();
		else
			cols["Short_Message"].ClearNull();
		if( r.IsSeverityNull )
			cols["Severity"].SetNull();
		else
			cols["Severity"].ClearNull();
		return true;
	}
};

template<> class DefaultBCA<TranDb::OperationLogsViewRow>
{
public:
	void operator()(BoundIOs &cols, TranDb::OperationLogsViewRow &rowbuf)
	{
		cols["interchangeproject_id"] == rowbuf.field_interchangeproject_id;
		cols["Operation_ID"] == rowbuf.field_Operation_ID;
		cols["Operation_Name"] == rowbuf.field_Operation_Name;
		cols["Log_ID"] == rowbuf.field_Log_ID;
		cols["Timestamp"] == rowbuf.field_Timestamp;
		cols["Short_Message"] == rowbuf.field_Short_Message;
		cols["Severity"] == rowbuf.field_Severity;
	}
};


}                     



namespace dtl_trandb {


class DtlOperationLogsViewInsertBinder : public CustomBinder<TranDb::OperationLogsViewRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		TranDb::OperationLogsViewRow& rowbuf = *m_pRow;
		cols["interchangeproject_id"] << rowbuf.field_interchangeproject_id;
		cols["Operation_ID"] << rowbuf.field_Operation_ID;
		cols["Operation_Name"] << rowbuf.field_Operation_Name;
		cols["Timestamp"] << rowbuf.field_Timestamp;
		cols["Short_Message"] << rowbuf.field_Short_Message;
		cols["Severity"] << rowbuf.field_Severity;
	}
};

class DtlOperationLogsViewDeleteBinder : public CustomBinder<TranDb::OperationLogsViewRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		TranDb::OperationLogsViewRow& rowbuf = *m_pRow;
		cols["interchangeproject_id"] == rowbuf.field_interchangeproject_id;
		cols["Operation_ID"] == rowbuf.field_Operation_ID;
		cols["Operation_Name"] == rowbuf.field_Operation_Name;
		cols["Log_ID"] == rowbuf.field_Log_ID;
		cols["Timestamp"] == rowbuf.field_Timestamp;
		cols["Short_Message"] == rowbuf.field_Short_Message;
		cols["Severity"] == rowbuf.field_Severity;
	}
};

class DtlOperationLogsView : public TranDb::OperationLogsView {

public:
	DtlOperationLogsView(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlOperationLogsView() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		TranDb::OperationLogsView::Edit();
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
	DtlOperationLogsViewInsertBinder m_insertBinder;
	DtlOperationLogsViewDeleteBinder m_deleteBinder;
	DtlDbRecordSet<TranDb::OperationLogsView, TranDb::OperationLogsViewRow> m_dtlRecSet;

};


}                            


#endif

