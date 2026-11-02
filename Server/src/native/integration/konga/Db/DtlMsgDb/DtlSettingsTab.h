/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRMSGDB_SETTINGSTAB_H
#define KONGA_DTL_CRMSGDB_SETTINGSTAB_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/MsgDb/SettingsTab.h>

namespace dtl {

template<> class DefaultSelValidate<MsgDb::SettingsTabRow>
{
public:
	bool operator()(BoundIOs &cols, MsgDb::SettingsTabRow &r)
	{
		r.IsSettingsIdNull = cols["SettingsId"].IsNull();
		r.IsRootNull = cols["Root"].IsNull();
		r.IsIncomingRootNull = cols["IncomingRoot"].IsNull();
		r.IsSMTPServerNull = cols["SMTPServer"].IsNull();
		r.IsGarbageDirNull = cols["GarbageDir"].IsNull();
		r.IsInsertTimeNull = cols["InsertTime"].IsNull();
		r.IsModTimeNull = cols["ModTime"].IsNull();
		  
                                        
                       
    
		return true;
	}
};

template<> class DefaultInsValidate<MsgDb::SettingsTabRow>
{
public:
	bool operator()(BoundIOs &cols, MsgDb::SettingsTabRow &r)
	{
		if( r.IsRootNull )
			cols["Root"].SetNull();
		else
			cols["Root"].ClearNull();
		if( r.IsIncomingRootNull )
			cols["IncomingRoot"].SetNull();
		else
			cols["IncomingRoot"].ClearNull();
		if( r.IsSMTPServerNull )
			cols["SMTPServer"].SetNull();
		else
			cols["SMTPServer"].ClearNull();
		if( r.IsGarbageDirNull )
			cols["GarbageDir"].SetNull();
		else
			cols["GarbageDir"].ClearNull();
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

template<> class DefaultBCA<MsgDb::SettingsTabRow>
{
public:
	void operator()(BoundIOs &cols, MsgDb::SettingsTabRow &rowbuf)
	{
		cols["SettingsId"] == rowbuf.field_SettingsId;
		cols["Root"] == rowbuf.field_Root;
		cols["IncomingRoot"] == rowbuf.field_IncomingRoot;
		cols["SMTPServer"] == rowbuf.field_SMTPServer;
		cols["GarbageDir"] == rowbuf.field_GarbageDir;
		cols["InsertTime"] == rowbuf.field_InsertTime;
		cols["ModTime"] == rowbuf.field_ModTime;
		  
                                        
                       
    
	}
};


}                     



namespace dtl_msgdb {


class DtlSettingsTabInsertBinder : public CustomBinder<MsgDb::SettingsTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		MsgDb::SettingsTabRow& rowbuf = *m_pRow;
		cols["Root"] << rowbuf.field_Root;
		cols["IncomingRoot"] << rowbuf.field_IncomingRoot;
		cols["SMTPServer"] << rowbuf.field_SMTPServer;
		cols["GarbageDir"] << rowbuf.field_GarbageDir;
		cols["InsertTime"] << rowbuf.field_InsertTime;
		cols["ModTime"] << rowbuf.field_ModTime;
	}
};

class DtlSettingsTabDeleteBinder : public CustomBinder<MsgDb::SettingsTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		MsgDb::SettingsTabRow& rowbuf = *m_pRow;
		cols["SettingsId"] == rowbuf.field_SettingsId;
	}
};

class DtlSettingsTab : public MsgDb::SettingsTab {

public:
	DtlSettingsTab(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlSettingsTab() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		MsgDb::SettingsTab::Edit();
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
	DtlSettingsTabInsertBinder m_insertBinder;
	DtlSettingsTabDeleteBinder m_deleteBinder;
	DtlDbRecordSet<MsgDb::SettingsTab, MsgDb::SettingsTabRow> m_dtlRecSet;

};


}                           


#endif

