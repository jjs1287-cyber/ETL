/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRMSGDB_ERRORDATAELEMENTTEXTTAB_H
#define KONGA_DTL_CRMSGDB_ERRORDATAELEMENTTEXTTAB_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/MsgDb/ErrorDataElementTextTab.h>

namespace dtl {

template<> class DefaultSelValidate<MsgDb::ErrorDataElementTextTabRow>
{
public:
	bool operator()(BoundIOs &cols, MsgDb::ErrorDataElementTextTabRow &r)
	{
		r.IsMsgIdNull = cols["MsgId"].IsNull();
		r.IsDataElementNameNull = cols["DataElementName"].IsNull();
		r.IsStringValueNull = cols["StringValue"].IsNull();
		r.IsErrorIdNull = cols["ErrorId"].IsNull();
		r.IsInsertTimeNull = cols["InsertTime"].IsNull();
		r.IsModTimeNull = cols["ModTime"].IsNull();
		  
                                        
           
    
		return true;
	}
};

template<> class DefaultInsValidate<MsgDb::ErrorDataElementTextTabRow>
{
public:
	bool operator()(BoundIOs &cols, MsgDb::ErrorDataElementTextTabRow &r)
	{
		if( r.IsMsgIdNull )
			cols["MsgId"].SetNull();
		else
			cols["MsgId"].ClearNull();
		if( r.IsDataElementNameNull )
			cols["DataElementName"].SetNull();
		else
			cols["DataElementName"].ClearNull();
		if( r.IsStringValueNull )
			cols["StringValue"].SetNull();
		else
			cols["StringValue"].ClearNull();
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

template<> class DefaultBCA<MsgDb::ErrorDataElementTextTabRow>
{
public:
	void operator()(BoundIOs &cols, MsgDb::ErrorDataElementTextTabRow &rowbuf)
	{
		cols["MsgId"] == rowbuf.field_MsgId;
		cols["DataElementName"] == rowbuf.field_DataElementName;
		cols["StringValue"] == rowbuf.field_StringValue;
		cols["ErrorId"] == rowbuf.field_ErrorId;
		cols["InsertTime"] == rowbuf.field_InsertTime;
		cols["ModTime"] == rowbuf.field_ModTime;
		  
                                        
           
    
	}
};


}                     



namespace dtl_msgdb {


class DtlErrorDataElementTextTabInsertBinder : public CustomBinder<MsgDb::ErrorDataElementTextTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		MsgDb::ErrorDataElementTextTabRow& rowbuf = *m_pRow;
		cols["MsgId"] << rowbuf.field_MsgId;
		cols["DataElementName"] << rowbuf.field_DataElementName;
		cols["StringValue"] << rowbuf.field_StringValue;
		cols["InsertTime"] << rowbuf.field_InsertTime;
		cols["ModTime"] << rowbuf.field_ModTime;
	}
};

class DtlErrorDataElementTextTabDeleteBinder : public CustomBinder<MsgDb::ErrorDataElementTextTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		MsgDb::ErrorDataElementTextTabRow& rowbuf = *m_pRow;
		cols["ErrorId"] == rowbuf.field_ErrorId;
	}
};

class DtlErrorDataElementTextTab : public MsgDb::ErrorDataElementTextTab {

public:
	DtlErrorDataElementTextTab(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlErrorDataElementTextTab() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		MsgDb::ErrorDataElementTextTab::Edit();
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
	DtlErrorDataElementTextTabInsertBinder m_insertBinder;
	DtlErrorDataElementTextTabDeleteBinder m_deleteBinder;
	DtlDbRecordSet<MsgDb::ErrorDataElementTextTab, MsgDb::ErrorDataElementTextTabRow> m_dtlRecSet;

};


}                           


#endif

