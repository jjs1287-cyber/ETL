/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRTRANDB_LOGAUXTAB_H
#define KONGA_DTL_CRTRANDB_LOGAUXTAB_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/TranDb/LogAuxTab.h>

namespace dtl {

template<> class DefaultSelValidate<TranDb::LogAuxTabRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::LogAuxTabRow &r)
	{
		r.IsLogAuxIDNull = cols["LogAuxID"].IsNull();
		r.IsLog_IDNull = cols["Log_ID"].IsNull();
		r.IsField_NameNull = cols["Field_Name"].IsNull();
		r.IsLong_MessageNull = cols["Long_Message"].IsNull();
		r.IsInsertTimeNull = cols["InsertTime"].IsNull();
		r.IsModTimeNull = cols["ModTime"].IsNull();
		  
                                        
                       
    
		return true;
	}
};

template<> class DefaultInsValidate<TranDb::LogAuxTabRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::LogAuxTabRow &r)
	{
		if( r.IsLog_IDNull )
			cols["Log_ID"].SetNull();
		else
			cols["Log_ID"].ClearNull();
		if( r.IsField_NameNull )
			cols["Field_Name"].SetNull();
		else
			cols["Field_Name"].ClearNull();
		if( r.IsLong_MessageNull )
			cols["Long_Message"].SetNull();
		else
			cols["Long_Message"].ClearNull();
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

template<> class DefaultBCA<TranDb::LogAuxTabRow>
{
public:
	void operator()(BoundIOs &cols, TranDb::LogAuxTabRow &rowbuf)
	{
		cols["LogAuxID"] == rowbuf.field_LogAuxID;
		cols["Log_ID"] == rowbuf.field_Log_ID;
		cols["Field_Name"] == rowbuf.field_Field_Name;
		cols["Long_Message"] == rowbuf.field_Long_Message;
		cols["InsertTime"] == rowbuf.field_InsertTime;
		cols["ModTime"] == rowbuf.field_ModTime;
		  
                                        
                       
    
	}
};


}                     



namespace dtl_trandb {


class DtlLogAuxTabInsertBinder : public CustomBinder<TranDb::LogAuxTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		TranDb::LogAuxTabRow& rowbuf = *m_pRow;
		cols["Log_ID"] << rowbuf.field_Log_ID;
		cols["Field_Name"] << rowbuf.field_Field_Name;
		cols["Long_Message"] << rowbuf.field_Long_Message;
		cols["InsertTime"] << rowbuf.field_InsertTime;
		cols["ModTime"] << rowbuf.field_ModTime;
	}
};

class DtlLogAuxTabDeleteBinder : public CustomBinder<TranDb::LogAuxTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		TranDb::LogAuxTabRow& rowbuf = *m_pRow;
		cols["LogAuxID"] == rowbuf.field_LogAuxID;
	}
};

class DtlLogAuxTab : public TranDb::LogAuxTab {

public:
	DtlLogAuxTab(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlLogAuxTab() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		TranDb::LogAuxTab::Edit();
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
	DtlLogAuxTabInsertBinder m_insertBinder;
	DtlLogAuxTabDeleteBinder m_deleteBinder;
	DtlDbRecordSet<TranDb::LogAuxTab, TranDb::LogAuxTabRow> m_dtlRecSet;

};


}                            


#endif

