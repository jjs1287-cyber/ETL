/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_TRANDB_TEMPLATE_SQLSCRIPTTAB_H
#define KONGA_DTL_TRANDB_TEMPLATE_SQLSCRIPTTAB_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/TranDb/SqlScriptTab.h>

namespace dtl {

template<> class DefaultSelValidate<TranDb::SqlScriptTabRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::SqlScriptTabRow &r)
	{
		r.IsSqlIdNull = cols["SqlID"].IsNull();
		r.IsTransformation_IdNull = cols["Transformation_ID"].IsNull();
		r.IsSqlScriptNull = cols["SqlScript"].IsNull();
		r.IsSqlScriptTypeNull = cols["SqlScriptType"].IsNull();
		r.IsSqlWhereNull = cols["SqlWhere"].IsNull();
		return true;
	}
};

template<> class DefaultInsValidate<TranDb::SqlScriptTabRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::SqlScriptTabRow &r)
	{
		if( r.IsSqlIdNull )
			cols["SqlId"].SetNull();
		else
			cols["SqlId"].ClearNull();

		if( r.IsTransformation_IdNull )
			cols["Transformation_ID"].SetNull();
		else
			cols["Transformation_ID"].ClearNull();

		if( r.IsSqlScriptNull )
			cols["SqlScript"].SetNull();
		else
			cols["SqlScript"].ClearNull();

		if( r.IsSqlScriptTypeNull )
			cols["SqlScriptType"].SetNull();
		else
			cols["SqlScriptType"].ClearNull();

		if( r.IsSqlWhereNull )
			cols["SqlWhere"].SetNull();
		else
			cols["SqlWhere"].ClearNull();
		return true;
	}
};

template<> class DefaultBCA<TranDb::SqlScriptTabRow>
{
public:
	void operator()(BoundIOs &cols, TranDb::SqlScriptTabRow &rowbuf)
	{
		cols["SqlId"] == rowbuf.field_SqlId;
		cols["Transformation_ID"] == rowbuf.field_Transformation_Id;
		cols["SqlScript"] == rowbuf.field_SqlScript;
		cols["SqlScriptType"] == rowbuf.field_SqlScriptType;
		cols["SqlWhere"] == rowbuf.field_SqlWhere;
	}
};

}                     

namespace dtl_trandb {

class DtlSqlScriptTabDeleteBinder : public CustomBinder<TranDb::SqlScriptTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		TranDb::SqlScriptTabRow& rowbuf = *m_pRow;
		cols["SqlId"] == rowbuf.field_SqlId;
	}
};

class DtlSqlScriptTab : public TranDb::SqlScriptTab {

public:
	DtlSqlScriptTab(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlSqlScriptTab() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		TranDb::SqlScriptTab::Edit();
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
	DtlSqlScriptTabDeleteBinder m_deleteBinder;
	DtlDbRecordSet<TranDb::SqlScriptTab, TranDb::SqlScriptTabRow> m_dtlRecSet;

};


}                            


#endif                                            

