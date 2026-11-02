/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRTRANDB_TRANSFORMATIONFUNCTIONTAB_H
#define KONGA_DTL_CRTRANDB_TRANSFORMATIONFUNCTIONTAB_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/TranDb/TransformationFunctionTab.h>

namespace dtl {

template<> class DefaultSelValidate<TranDb::TransformationFunctionTabRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::TransformationFunctionTabRow &r)
	{
		r.IsTransformation_IDNull = cols["Transformation_ID"].IsNull();
		r.IsFunction_IDNull = cols["Function_ID"].IsNull();
		return true;
	}
};

template<> class DefaultInsValidate<TranDb::TransformationFunctionTabRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::TransformationFunctionTabRow &r)
	{
		if( r.IsTransformation_IDNull )
			cols["Transformation_ID"].SetNull();
		else
			cols["Transformation_ID"].ClearNull();
		if( r.IsFunction_IDNull )
			cols["Function_ID"].SetNull();
		else
			cols["Function_ID"].ClearNull();
		return true;
	}
};

template<> class DefaultBCA<TranDb::TransformationFunctionTabRow>
{
public:
	void operator()(BoundIOs &cols, TranDb::TransformationFunctionTabRow &rowbuf)
	{
		cols["Transformation_ID"] == rowbuf.field_Transformation_ID;
		cols["Function_ID"] == rowbuf.field_Function_ID;
	}
};


}                     



namespace dtl_trandb {

class DtlTransformationFunctionTabDeleteBinder : public CustomBinder<TranDb::TransformationFunctionTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		TranDb::TransformationFunctionTabRow& rowbuf = *m_pRow;
		cols["Transformation_ID"] == rowbuf.field_Transformation_ID;
	}
};

class DtlTransformationFunctionTab : public TranDb::TransformationFunctionTab {

public:
	DtlTransformationFunctionTab(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlTransformationFunctionTab() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		TranDb::TransformationFunctionTab::Edit();
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
	DtlTransformationFunctionTabDeleteBinder m_deleteBinder;
	DtlDbRecordSet<TranDb::TransformationFunctionTab, TranDb::TransformationFunctionTabRow> m_dtlRecSet;

};


}                            


#endif

