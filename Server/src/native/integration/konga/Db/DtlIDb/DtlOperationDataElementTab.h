/*
 * Copyright (c) 1998-2006 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRIDB_OPERATIONDATAELEMENTTAB_H
#define KONGA_DTL_CRIDB_OPERATIONDATAELEMENTTAB_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/IDb/OperationDataElementTab.h>

namespace dtl {

template<> class DefaultSelValidate<IDb::OperationDataElementTabRow>
{
public:
	bool operator()(BoundIOs &cols, IDb::OperationDataElementTabRow &r)
	{
		r.IsSessionIdNull = cols["SessionId"].IsNull();
		r.IsDataElementNameNull = cols["DataElementName"].IsNull();
		r.IsArrayIndexNull = cols["ArrayIndex"].IsNull();
		r.IsStringValueNull = cols["StringValue"].IsNull();
		return true;
	}
};

template<> class DefaultInsValidate<IDb::OperationDataElementTabRow>
{
public:
	bool operator()(BoundIOs &cols, IDb::OperationDataElementTabRow &r)
	{
		if( r.IsSessionIdNull )
			cols["SessionId"].SetNull();
		else
			cols["SessionId"].ClearNull();
		if( r.IsDataElementNameNull )
			cols["DataElementName"].SetNull();
		else
			cols["DataElementName"].ClearNull();
		if( r.IsArrayIndexNull )
			cols["ArrayIndex"].SetNull();
		else
			cols["ArrayIndex"].ClearNull();
		if( r.IsStringValueNull )
			cols["StringValue"].SetNull();
		else
			cols["StringValue"].ClearNull();
		return true;
	}
};

template<> class DefaultBCA<IDb::OperationDataElementTabRow>
{
public:
	void operator()(BoundIOs &cols, IDb::OperationDataElementTabRow &rowbuf)
	{
		cols["SessionId"] == rowbuf.field_SessionId;
		cols["DataElementName"] == rowbuf.field_DataElementName;
		cols["ArrayIndex"] == rowbuf.field_ArrayIndex;
		cols["StringValue"] == rowbuf.field_StringValue;
	}
};


}                     



namespace dtl_idb {

class DtlOperationDataElementTabDeleteBinder : public CustomBinder<IDb::OperationDataElementTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		IDb::OperationDataElementTabRow& rowbuf = *m_pRow;
		cols["SessionId"] == rowbuf.field_SessionId;
		cols["DataElementName"] == rowbuf.field_DataElementName;
		cols["ArrayIndex"] == rowbuf.field_ArrayIndex;
	}
};

class DtlOperationDataElementTab : public IDb::OperationDataElementTab {

public:
	DtlOperationDataElementTab(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlOperationDataElementTab() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		IDb::OperationDataElementTab::Edit();
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
	DtlOperationDataElementTabDeleteBinder m_deleteBinder;
	DtlDbRecordSet<IDb::OperationDataElementTab, IDb::OperationDataElementTabRow> m_dtlRecSet;

};


}                         


#endif                                             

