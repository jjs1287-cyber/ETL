/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRTRANDB_TARGETDTDVIEW_H
#define KONGA_DTL_CRTRANDB_TARGETDTDVIEW_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/TranDb/TargetDtdView.h>

namespace dtl {

template<> class DefaultSelValidate<TranDb::TargetDtdViewRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::TargetDtdViewRow &r)
	{
		r.IsTransformation_IDNull = cols["Transformation_ID"].IsNull();
		r.IsXML_PathNull = cols["XML_Path"].IsNull();
		r.IsXML_IDNull = cols["XML_ID"].IsNull();
		return true;
	}
};

template<> class DefaultInsValidate<TranDb::TargetDtdViewRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::TargetDtdViewRow &r)
	{
		if( r.IsTransformation_IDNull )
			cols["Transformation_ID"].SetNull();
		else
			cols["Transformation_ID"].ClearNull();
		if( r.IsXML_PathNull )
			cols["XML_Path"].SetNull();
		else
			cols["XML_Path"].ClearNull();
		if( r.IsXML_IDNull )
			cols["XML_ID"].SetNull();
		else
			cols["XML_ID"].ClearNull();
		return true;
	}
};

template<> class DefaultBCA<TranDb::TargetDtdViewRow>
{
public:
	void operator()(BoundIOs &cols, TranDb::TargetDtdViewRow &rowbuf)
	{
		cols["Transformation_ID"] == rowbuf.field_Transformation_ID;
		cols["XML_Path"] == rowbuf.field_XML_Path;
		cols["XML_ID"] == rowbuf.field_XML_ID;
	}
};


}                     



namespace dtl_trandb {

class DtlTargetDtdViewDeleteBinder : public CustomBinder<TranDb::TargetDtdViewRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		TranDb::TargetDtdViewRow& rowbuf = *m_pRow;
		cols["Transformation_ID"] == rowbuf.field_Transformation_ID;
		cols["XML_Path"] == rowbuf.field_XML_Path;
		cols["XML_ID"] == rowbuf.field_XML_ID;
	}
};

class DtlTargetDtdView : public TranDb::TargetDtdView {

public:
	DtlTargetDtdView(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlTargetDtdView() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		TranDb::TargetDtdView::Edit();
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
	DtlTargetDtdViewDeleteBinder m_deleteBinder;
	DtlDbRecordSet<TranDb::TargetDtdView, TranDb::TargetDtdViewRow> m_dtlRecSet;

};


}                            


#endif

