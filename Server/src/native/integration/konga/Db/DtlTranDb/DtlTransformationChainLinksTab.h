/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRTRANDB_TRANSFORMATIONCHAINLINKSTAB_H
#define KONGA_DTL_CRTRANDB_TRANSFORMATIONCHAINLINKSTAB_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/TranDb/TransformationChainLinksTab.h>

namespace dtl {

template<> class DefaultSelValidate<TranDb::TransformationChainLinksTabRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::TransformationChainLinksTabRow &r)
	{
		r.IsTransformationChainLink_IDNull = cols["TransformationChainLink_ID"].IsNull();
		r.IsOperation_IDNull = cols["Operation_ID"].IsNull();
		r.IsTransformation_IDNull = cols["Transformation_ID"].IsNull();
		r.IsNextTransformation_IDNull = cols["NextTransformation_ID"].IsNull();
		return true;
	}
};

template<> class DefaultInsValidate<TranDb::TransformationChainLinksTabRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::TransformationChainLinksTabRow &r)
	{
		if( r.IsTransformationChainLink_IDNull )
			cols["TransformationChainLink_ID"].SetNull();
		else
			cols["TransformationChainLink_ID"].ClearNull();
		if( r.IsOperation_IDNull )
			cols["Operation_ID"].SetNull();
		else
			cols["Operation_ID"].ClearNull();
		if( r.IsTransformation_IDNull )
			cols["Transformation_ID"].SetNull();
		else
			cols["Transformation_ID"].ClearNull();
		if( r.IsNextTransformation_IDNull )
			cols["NextTransformation_ID"].SetNull();
		else
			cols["NextTransformation_ID"].ClearNull();
		return true;
	}
};

template<> class DefaultBCA<TranDb::TransformationChainLinksTabRow>
{
public:
	void operator()(BoundIOs &cols, TranDb::TransformationChainLinksTabRow &rowbuf)
	{
		cols["TransformationChainLink_ID"] == rowbuf.field_TransformationChainLink_ID;
		cols["Operation_ID"] == rowbuf.field_Operation_ID;
		cols["Transformation_ID"] == rowbuf.field_Transformation_ID;
		cols["NextTransformation_ID"] == rowbuf.field_NextTransformation_ID;
	}
};


}                     



namespace dtl_trandb {

class DtlTransformationChainLinksTabDeleteBinder : public CustomBinder<TranDb::TransformationChainLinksTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		TranDb::TransformationChainLinksTabRow& rowbuf = *m_pRow;
		cols["TransformationChainLink_ID"] == rowbuf.field_TransformationChainLink_ID;
	}
};

class DtlTransformationChainLinksTab : public TranDb::TransformationChainLinksTab {

public:
	DtlTransformationChainLinksTab(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlTransformationChainLinksTab() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		TranDb::TransformationChainLinksTab::Edit();
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
	DtlTransformationChainLinksTabDeleteBinder m_deleteBinder;
	DtlDbRecordSet<TranDb::TransformationChainLinksTab, TranDb::TransformationChainLinksTabRow> m_dtlRecSet;

};


}                            


#endif

