/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRTRANDB_PROJECTOPERATIONSVIEW_H
#define KONGA_DTL_CRTRANDB_PROJECTOPERATIONSVIEW_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/TranDb/ProjectOperationsView.h>

namespace dtl {

template<> class DefaultSelValidate<TranDb::ProjectOperationsViewRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::ProjectOperationsViewRow &r)
	{
		r.Isinterchangeproject_idNull = cols["interchangeproject_id"].IsNull();
		r.IsOperation_IDNull = cols["Operation_ID"].IsNull();
		r.IsOperation_NameNull = cols["Operation_Name"].IsNull();
		return true;
	}
};

template<> class DefaultInsValidate<TranDb::ProjectOperationsViewRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::ProjectOperationsViewRow &r)
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
		return true;
	}
};

template<> class DefaultBCA<TranDb::ProjectOperationsViewRow>
{
public:
	void operator()(BoundIOs &cols, TranDb::ProjectOperationsViewRow &rowbuf)
	{
		cols["interchangeproject_id"] == rowbuf.field_interchangeproject_id;
		cols["Operation_ID"] == rowbuf.field_Operation_ID;
		cols["Operation_Name"] == rowbuf.field_Operation_Name;
	}
};


}                     



namespace dtl_trandb {

class DtlProjectOperationsViewDeleteBinder : public CustomBinder<TranDb::ProjectOperationsViewRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		TranDb::ProjectOperationsViewRow& rowbuf = *m_pRow;
		cols["interchangeproject_id"] == rowbuf.field_interchangeproject_id;
		cols["Operation_ID"] == rowbuf.field_Operation_ID;
		cols["Operation_Name"] == rowbuf.field_Operation_Name;
	}
};

class DtlProjectOperationsView : public TranDb::ProjectOperationsView {

public:
	DtlProjectOperationsView(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlProjectOperationsView() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		TranDb::ProjectOperationsView::Edit();
		m_dtlRecSet.Edit();
	}
	void AddNew() {
		m_dtlRecSet.AddNew();
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
	DtlProjectOperationsViewDeleteBinder m_deleteBinder;
	DtlDbRecordSet<TranDb::ProjectOperationsView, TranDb::ProjectOperationsViewRow> m_dtlRecSet;

};


}                            


#endif

