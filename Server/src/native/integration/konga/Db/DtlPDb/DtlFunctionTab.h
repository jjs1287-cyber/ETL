/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRPDB_FUNCTIONTAB_H
#define KONGA_DTL_CRPDB_FUNCTIONTAB_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/PDb/FunctionTab.h>

namespace dtl {

template<> class DefaultSelValidate<PDb::FunctionTabRow>
{
public:
	bool operator()(BoundIOs &cols, PDb::FunctionTabRow &r)
	{
		r.IsIDNull = cols["ID"].IsNull();
		r.IsFunctionNull = cols["Function"].IsNull();
		r.IsInsertTimeNull = cols["InsertTime"].IsNull();
		r.IsModTimeNull = cols["ModTime"].IsNull();
		  
                                        
                       
    
		return true;
	}
};

template<> class DefaultInsValidate<PDb::FunctionTabRow>
{
public:
	bool operator()(BoundIOs &cols, PDb::FunctionTabRow &r)
	{
		if( r.IsIDNull )
			cols["ID"].SetNull();
		else
			cols["ID"].ClearNull();
		if( r.IsFunctionNull )
			cols["Function"].SetNull();
		else
			cols["Function"].ClearNull();
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

template<> class DefaultBCA<PDb::FunctionTabRow>
{
public:
	void operator()(BoundIOs &cols, PDb::FunctionTabRow &rowbuf)
	{
		cols["ID"] == rowbuf.field_ID;
		cols["Function"] == rowbuf.field_Function;
		cols["InsertTime"] == rowbuf.field_InsertTime;
		cols["ModTime"] == rowbuf.field_ModTime;
		  
                                        
                       
    
	}
};


}                     



namespace dtl_pdb {

class DtlFunctionTabDeleteBinder : public CustomBinder<PDb::FunctionTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		PDb::FunctionTabRow& rowbuf = *m_pRow;
		cols["ID"] == rowbuf.field_ID;
	}
};

class DtlFunctionTab : public PDb::FunctionTab {

public:
	DtlFunctionTab(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlFunctionTab() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		PDb::FunctionTab::Edit();
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
	DtlFunctionTabDeleteBinder m_deleteBinder;
	DtlDbRecordSet<PDb::FunctionTab, PDb::FunctionTabRow> m_dtlRecSet;

};


}                         


#endif

