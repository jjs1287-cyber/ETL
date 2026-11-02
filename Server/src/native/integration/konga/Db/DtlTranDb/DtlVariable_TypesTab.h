/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRTRANDB_VARIABLE_TYPESTAB_H
#define KONGA_DTL_CRTRANDB_VARIABLE_TYPESTAB_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/TranDb/Variable_TypesTab.h>

namespace dtl {

template<> class DefaultSelValidate<TranDb::Variable_TypesTabRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::Variable_TypesTabRow &r)
	{
		r.IsVar_Type_IDNull = cols["Var_Type_ID"].IsNull();
		r.IsVar_TypeNull = cols["Var_Type"].IsNull();
		r.IsInsertTimeNull = cols["InsertTime"].IsNull();
		r.IsModTimeNull = cols["ModTime"].IsNull();
		  
                                        
                       
    
		return true;
	}
};

template<> class DefaultInsValidate<TranDb::Variable_TypesTabRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::Variable_TypesTabRow &r)
	{
		if( r.IsVar_Type_IDNull )
			cols["Var_Type_ID"].SetNull();
		else
			cols["Var_Type_ID"].ClearNull();
		if( r.IsVar_TypeNull )
			cols["Var_Type"].SetNull();
		else
			cols["Var_Type"].ClearNull();
		if( r.IsInsertTimeNull )
			cols["InsertTime"].SetNull();
		else
			cols["InsertTime"].ClearNull();
		if( r.IsModTimeNull )
			cols["ModTime"].SetNull();
		else
			cols["ModTime"].ClearNull();
		  
                                        
                       
    
		return true;
	}
};

template<> class DefaultBCA<TranDb::Variable_TypesTabRow>
{
public:
	void operator()(BoundIOs &cols, TranDb::Variable_TypesTabRow &rowbuf)
	{
		cols["Var_Type_ID"] == rowbuf.field_Var_Type_ID;
		cols["Var_Type"] == rowbuf.field_Var_Type;
		cols["InsertTime"] == rowbuf.field_InsertTime;
		cols["ModTime"] == rowbuf.field_ModTime;
		  
                                        
                       
    
	}
};


}                     



namespace dtl_trandb {

class DtlVariable_TypesTabDeleteBinder : public CustomBinder<TranDb::Variable_TypesTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		TranDb::Variable_TypesTabRow& rowbuf = *m_pRow;
		cols["Var_Type_ID"] == rowbuf.field_Var_Type_ID;
	}
};

class DtlVariable_TypesTab : public TranDb::Variable_TypesTab {

public:
	DtlVariable_TypesTab(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlVariable_TypesTab() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		TranDb::Variable_TypesTab::Edit();
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
	DtlVariable_TypesTabDeleteBinder m_deleteBinder;
	DtlDbRecordSet<TranDb::Variable_TypesTab, TranDb::Variable_TypesTabRow> m_dtlRecSet;

};


}                            


#endif

