/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRMSGDB_POPACCOUNTSTAB_H
#define KONGA_DTL_CRMSGDB_POPACCOUNTSTAB_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/MsgDb/POPAccountsTab.h>

namespace dtl {

template<> class DefaultSelValidate<MsgDb::POPAccountsTabRow>
{
public:
	bool operator()(BoundIOs &cols, MsgDb::POPAccountsTabRow &r)
	{
		r.IsPOPUserNull = cols["POPUser"].IsNull();
		r.IsPOPPasswordNull = cols["POPPassword"].IsNull();
		r.IsPOPServerNull = cols["POPServer"].IsNull();
		r.IsInsertTimeNull = cols["InsertTime"].IsNull();
		r.IsModTimeNull = cols["ModTime"].IsNull();
		  
                                        
                       
    
		return true;
	}
};

template<> class DefaultInsValidate<MsgDb::POPAccountsTabRow>
{
public:
	bool operator()(BoundIOs &cols, MsgDb::POPAccountsTabRow &r)
	{
		if( r.IsPOPUserNull )
			cols["POPUser"].SetNull();
		else
			cols["POPUser"].ClearNull();
		if( r.IsPOPPasswordNull )
			cols["POPPassword"].SetNull();
		else
			cols["POPPassword"].ClearNull();
		if( r.IsPOPServerNull )
			cols["POPServer"].SetNull();
		else
			cols["POPServer"].ClearNull();
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

template<> class DefaultBCA<MsgDb::POPAccountsTabRow>
{
public:
	void operator()(BoundIOs &cols, MsgDb::POPAccountsTabRow &rowbuf)
	{
		cols["POPUser"] == rowbuf.field_POPUser;
		cols["POPPassword"] == rowbuf.field_POPPassword;
		cols["POPServer"] == rowbuf.field_POPServer;
		cols["InsertTime"] == rowbuf.field_InsertTime;
		cols["ModTime"] == rowbuf.field_ModTime;
		  
                                        
                       
    
	}
};


}                     



namespace dtl_msgdb {

class DtlPOPAccountsTabDeleteBinder : public CustomBinder<MsgDb::POPAccountsTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		MsgDb::POPAccountsTabRow& rowbuf = *m_pRow;
		cols["POPUser"] == rowbuf.field_POPUser;
	}
};

class DtlPOPAccountsTab : public MsgDb::POPAccountsTab {

public:
	DtlPOPAccountsTab(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlPOPAccountsTab() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		MsgDb::POPAccountsTab::Edit();
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
	DtlPOPAccountsTabDeleteBinder m_deleteBinder;
	DtlDbRecordSet<MsgDb::POPAccountsTab, MsgDb::POPAccountsTabRow> m_dtlRecSet;

};


}                           


#endif

