/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRMSGDB_DATAELEMENTTEXTTAB_H
#define KONGA_DTL_CRMSGDB_DATAELEMENTTEXTTAB_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/MsgDb/DataElementTextTab.h>

namespace dtl {

template<> class DefaultSelValidate<MsgDb::DataElementTextTabRow>
{
public:
	bool operator()(BoundIOs &cols, MsgDb::DataElementTextTabRow &r)
	{
		r.IsMsgIdNull = cols["MsgId"].IsNull();
		r.IsDataElementNameNull = cols["DataElementName"].IsNull();
		r.IsStringValueNull = cols["StringValue"].IsNull();
		r.IsInsertTimeNull = cols["InsertTime"].IsNull();
		r.IsModTimeNull = cols["ModTime"].IsNull();
		  
                                        
           
    
		return true;
	}
};

template<> class DefaultInsValidate<MsgDb::DataElementTextTabRow>
{
public:
	bool operator()(BoundIOs &cols, MsgDb::DataElementTextTabRow &r)
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

template<> class DefaultBCA<MsgDb::DataElementTextTabRow>
{
public:
	void operator()(BoundIOs &cols, MsgDb::DataElementTextTabRow &rowbuf)
	{
		cols["MsgId"] == rowbuf.field_MsgId;
		cols["DataElementName"] == rowbuf.field_DataElementName;
		cols["StringValue"] == rowbuf.field_StringValue;
		cols["InsertTime"] == rowbuf.field_InsertTime;
		cols["ModTime"] == rowbuf.field_ModTime;
		  
                                        
           
    
	}
};


}                     



namespace dtl_msgdb {

class DtlDataElementTextTabDeleteBinder : public CustomBinder<MsgDb::DataElementTextTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		MsgDb::DataElementTextTabRow& rowbuf = *m_pRow;
		cols["MsgId"] == rowbuf.field_MsgId;
		cols["DataElementName"] == rowbuf.field_DataElementName;
	}
};

class DtlDataElementTextTab : public MsgDb::DataElementTextTab {

public:
	DtlDataElementTextTab(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlDataElementTextTab() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		MsgDb::DataElementTextTab::Edit();
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
	DtlDataElementTextTabDeleteBinder m_deleteBinder;
	DtlDbRecordSet<MsgDb::DataElementTextTab, MsgDb::DataElementTextTabRow> m_dtlRecSet;

};


}                           


#endif

