/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRTRANDB_WSDLTAB_H
#define KONGA_DTL_CRTRANDB_WSDLTAB_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/TranDb/WsdlTab.h>

namespace dtl {

template<> class DefaultSelValidate<TranDb::WsdlTabRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::WsdlTabRow &r)
	{
		r.IsWsdl_IDNull = cols["Wsdl_ID"].IsNull();
		r.IsWsdlNameNull = cols["WsdlName"].IsNull();
		r.IsWsdlURLNull = cols["WsdlURL"].IsNull();
		r.IsWsdlPathNull = cols["WsdlPath"].IsNull();
		r.IsStatusNull = cols["Status"].IsNull();
		r.IsApplicationMessageNull = cols["ApplicationMessage"].IsNull();
		r.IsUserCommentNull = cols["UserComment"].IsNull();
		r.IsWsdlTypeNull = cols["WsdlType"].IsNull();
		return true;
	}
};

template<> class DefaultInsValidate<TranDb::WsdlTabRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::WsdlTabRow &r)
	{
		if( r.IsWsdl_IDNull )
			cols["Wsdl_ID"].SetNull();
		else
			cols["Wsdl_ID"].ClearNull();

		if( r.IsWsdlNameNull )
			cols["WsdlName"].SetNull();
		else
			cols["WsdlName"].ClearNull();
		if( r.IsWsdlURLNull )
			cols["WsdlURL"].SetNull();
		else
			cols["WsdlURL"].ClearNull();
		if( r.IsWsdlPathNull )
			cols["WsdlPath"].SetNull();
		else
			cols["WsdlPath"].ClearNull();
		if( r.IsStatusNull )
			cols["Status"].SetNull();
		else
			cols["Status"].ClearNull();
		if( r.IsApplicationMessageNull )
			cols["ApplicationMessage"].SetNull();
		else
			cols["ApplicationMessage"].ClearNull();
		if( r.IsUserCommentNull )
			cols["UserComment"].SetNull();
		else
			cols["UserComment"].ClearNull();
		if( r.IsWsdlTypeNull )
			cols["WsdlType"].SetNull();
		else
			cols["WsdlType"].ClearNull();
		return true;
	}
};

template<> class DefaultBCA<TranDb::WsdlTabRow>
{
public:
	void operator()(BoundIOs &cols, TranDb::WsdlTabRow &rowbuf)
	{
		cols["Wsdl_ID"] == rowbuf.field_Wsdl_ID;
		cols["WsdlName"] == rowbuf.field_WsdlName;
		cols["WsdlURL"] == rowbuf.field_WsdlURL;
		cols["WsdlPath"] == rowbuf.field_WsdlPath;
		cols["Status"] == rowbuf.field_Status;
		cols["ApplicationMessage"] == rowbuf.field_ApplicationMessage;
		cols["UserComment"] == rowbuf.field_UserComment;
		cols["WsdlType"] == rowbuf.field_WsdlType;
	}
};


}                     



namespace dtl_trandb {


class DtlWsdlTabInsertBinder : public CustomBinder<TranDb::WsdlTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		TranDb::WsdlTabRow& rowbuf = *m_pRow;
		cols["Wsdl_ID"] << rowbuf.field_Wsdl_ID;
		cols["WsdlName"] << rowbuf.field_WsdlName;
		cols["WsdlURL"] << rowbuf.field_WsdlURL;
		cols["WsdlPath"] << rowbuf.field_WsdlPath;
		cols["Status"] << rowbuf.field_Status;
		cols["ApplicationMessage"] << rowbuf.field_ApplicationMessage;
		cols["UserComment"] << rowbuf.field_UserComment;
		cols["WsdlType"] << rowbuf.field_WsdlType;
	}
};

class DtlWsdlTabDeleteBinder : public CustomBinder<TranDb::WsdlTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		TranDb::WsdlTabRow& rowbuf = *m_pRow;
		cols["Wsdl_ID"] == rowbuf.field_Wsdl_ID;
	}
};

class DtlWsdlTab : public TranDb::WsdlTab {

public:
	DtlWsdlTab(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlWsdlTab() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		TranDb::WsdlTab::Edit();
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
	DtlWsdlTabInsertBinder m_insertBinder;
	DtlWsdlTabDeleteBinder m_deleteBinder;
	DtlDbRecordSet<TranDb::WsdlTab, TranDb::WsdlTabRow> m_dtlRecSet;

};


}                            


#endif

