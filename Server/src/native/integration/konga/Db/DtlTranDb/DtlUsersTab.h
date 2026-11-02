/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRTRANDB_USERSTAB_H
#define KONGA_DTL_CRTRANDB_USERSTAB_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/TranDb/UsersTab.h>

namespace dtl {

template<> class DefaultSelValidate<TranDb::UsersTabRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::UsersTabRow &r)
	{
		r.IsUser_IDNull = cols["User_ID"].IsNull();
		r.IsFirst_NameNull = cols["First_Name"].IsNull();
		r.IsLast_NameNull = cols["Last_Name"].IsNull();
		r.IsLoginNull = cols["Login"].IsNull();
		r.IsPasswordNull = cols["Password"].IsNull();
		r.IsEmailNull = cols["Email"].IsNull();
		r.IsAccess_LevelNull = cols["Access_Level"].IsNull();
		r.IsInsertTimeNull = cols["InsertTime"].IsNull();
		r.IsModTimeNull = cols["ModTime"].IsNull();
		  
                                        
                       
    
		return true;
	}
};

template<> class DefaultInsValidate<TranDb::UsersTabRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::UsersTabRow &r)
	{
		if( r.IsUser_IDNull )
			cols["User_ID"].SetNull();
		else
			cols["User_ID"].ClearNull();
		if( r.IsFirst_NameNull )
			cols["First_Name"].SetNull();
		else
			cols["First_Name"].ClearNull();
		if( r.IsLast_NameNull )
			cols["Last_Name"].SetNull();
		else
			cols["Last_Name"].ClearNull();
		if( r.IsLoginNull )
			cols["Login"].SetNull();
		else
			cols["Login"].ClearNull();
		if( r.IsPasswordNull )
			cols["Password"].SetNull();
		else
			cols["Password"].ClearNull();
		if( r.IsEmailNull )
			cols["Email"].SetNull();
		else
			cols["Email"].ClearNull();
		if( r.IsAccess_LevelNull )
			cols["Access_Level"].SetNull();
		else
			cols["Access_Level"].ClearNull();
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

template<> class DefaultBCA<TranDb::UsersTabRow>
{
public:
	void operator()(BoundIOs &cols, TranDb::UsersTabRow &rowbuf)
	{
		cols["User_ID"] == rowbuf.field_User_ID;
		cols["First_Name"] == rowbuf.field_First_Name;
		cols["Last_Name"] == rowbuf.field_Last_Name;
		cols["Login"] == rowbuf.field_Login;
		cols["Password"] == rowbuf.field_Password;
		cols["Email"] == rowbuf.field_Email;
		cols["Access_Level"] == rowbuf.field_Access_Level;
		cols["InsertTime"] == rowbuf.field_InsertTime;
		cols["ModTime"] == rowbuf.field_ModTime;
		  
                                        
                       
    
	}
};


}                     



namespace dtl_trandb {

class DtlUsersTabDeleteBinder : public CustomBinder<TranDb::UsersTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		TranDb::UsersTabRow& rowbuf = *m_pRow;
		cols["User_ID"] == rowbuf.field_User_ID;
	}
};

class DtlUsersTab : public TranDb::UsersTab {

public:
	DtlUsersTab(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlUsersTab() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		TranDb::UsersTab::Edit();
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
	DtlUsersTabDeleteBinder m_deleteBinder;
	DtlDbRecordSet<TranDb::UsersTab, TranDb::UsersTabRow> m_dtlRecSet;

};


}                            


#endif

