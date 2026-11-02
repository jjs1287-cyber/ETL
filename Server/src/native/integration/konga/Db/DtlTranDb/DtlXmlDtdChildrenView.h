/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRTRANDB_XMLDTDCHILDRENVIEW_H
#define KONGA_DTL_CRTRANDB_XMLDTDCHILDRENVIEW_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/TranDb/XmlDtdChildrenView.h>

namespace dtl {

template<> class DefaultSelValidate<TranDb::XmlDtdChildrenViewRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::XmlDtdChildrenViewRow &r)
	{
		r.IsXML_IDNull = cols["XML_ID"].IsNull();
		r.IsChild_IDNull = cols["Child_ID"].IsNull();
		r.IsXML_PathNull = cols["XML_Path"].IsNull();
		return true;
	}
};

template<> class DefaultInsValidate<TranDb::XmlDtdChildrenViewRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::XmlDtdChildrenViewRow &r)
	{
		if( r.IsXML_IDNull )
			cols["XML_ID"].SetNull();
		else
			cols["XML_ID"].ClearNull();
		if( r.IsChild_IDNull )
			cols["Child_ID"].SetNull();
		else
			cols["Child_ID"].ClearNull();
		if( r.IsXML_PathNull )
			cols["XML_Path"].SetNull();
		else
			cols["XML_Path"].ClearNull();
		return true;
	}
};

template<> class DefaultBCA<TranDb::XmlDtdChildrenViewRow>
{
public:
	void operator()(BoundIOs &cols, TranDb::XmlDtdChildrenViewRow &rowbuf)
	{
		cols["XML_ID"] == rowbuf.field_XML_ID;
		cols["Child_ID"] == rowbuf.field_Child_ID;
		cols["XML_Path"] == rowbuf.field_XML_Path;
	}
};


}                     



namespace dtl_trandb {

class DtlXmlDtdChildrenViewDeleteBinder : public CustomBinder<TranDb::XmlDtdChildrenViewRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		TranDb::XmlDtdChildrenViewRow& rowbuf = *m_pRow;
		cols["XML_ID"] == rowbuf.field_XML_ID;
		cols["Child_ID"] == rowbuf.field_Child_ID;
		cols["XML_Path"] == rowbuf.field_XML_Path;
	}
};

class DtlXmlDtdChildrenView : public TranDb::XmlDtdChildrenView {

public:
	DtlXmlDtdChildrenView(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlXmlDtdChildrenView() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		TranDb::XmlDtdChildrenView::Edit();
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
	DtlXmlDtdChildrenViewDeleteBinder m_deleteBinder;
	DtlDbRecordSet<TranDb::XmlDtdChildrenView, TranDb::XmlDtdChildrenViewRow> m_dtlRecSet;

};


}                            


#endif

