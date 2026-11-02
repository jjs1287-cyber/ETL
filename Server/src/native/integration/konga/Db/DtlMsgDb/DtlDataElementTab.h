/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRMSGDB_DATAELEMENTTAB_H
#define KONGA_DTL_CRMSGDB_DATAELEMENTTAB_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/MsgDb/DataElementTab.h>

namespace dtl {

template<> class DefaultSelValidate<MsgDb::DataElementTabRow>
{
public:
	bool operator()(BoundIOs &cols, MsgDb::DataElementTabRow &r)
	{
		r.IsMsgIdNull = cols["MsgId"].IsNull();
		r.IsDataElementNameNull = cols["DataElementName"].IsNull();
		r.IsDataTypeNull = cols["DataType"].IsNull();
		r.IsIntValueNull = cols["IntValue"].IsNull();
		r.IsDoubleValueNull = cols["DoubleValue"].IsNull();
		r.IsStringValueNull = cols["StringValue"].IsNull();
		r.IsInsertTimeNull = cols["InsertTime"].IsNull();
		r.IsModTimeNull = cols["ModTime"].IsNull();
		  
                                        
                       
    
		return true;
	}
};

template<> class DefaultInsValidate<MsgDb::DataElementTabRow>
{
public:
	bool operator()(BoundIOs &cols, MsgDb::DataElementTabRow &r)
	{
		if( r.IsMsgIdNull )
			cols["MsgId"].SetNull();
		else
			cols["MsgId"].ClearNull();
		if( r.IsDataElementNameNull )
			cols["DataElementName"].SetNull();
		else
			cols["DataElementName"].ClearNull();
		if( r.IsDataTypeNull )
			cols["DataType"].SetNull();
		else
			cols["DataType"].ClearNull();
		if( r.IsIntValueNull )
			cols["IntValue"].SetNull();
		else
			cols["IntValue"].ClearNull();
		if( r.IsDoubleValueNull )
			cols["DoubleValue"].SetNull();
		else
			cols["DoubleValue"].ClearNull();
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

template<> class DefaultBCA<MsgDb::DataElementTabRow>
{
public:
	void operator()(BoundIOs &cols, MsgDb::DataElementTabRow &rowbuf)
	{
		cols["MsgId"] == rowbuf.field_MsgId;
		cols["DataElementName"] == rowbuf.field_DataElementName;
		cols["DataType"] == rowbuf.field_DataType;
		cols["IntValue"] == rowbuf.field_IntValue;
		cols["DoubleValue"] == rowbuf.field_DoubleValue;
		cols["StringValue"] == rowbuf.field_StringValue;
		cols["InsertTime"] == rowbuf.field_InsertTime;
		cols["ModTime"] == rowbuf.field_ModTime;
		  
                                        
                       
    
	}
};


}                     



namespace dtl_msgdb {

class DtlDataElementTabDeleteBinder : public CustomBinder<MsgDb::DataElementTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		MsgDb::DataElementTabRow& rowbuf = *m_pRow;
		cols["MsgId"] == rowbuf.field_MsgId;
		cols["DataElementName"] == rowbuf.field_DataElementName;
	}
};

class DtlDataElementTab : public MsgDb::DataElementTab {

public:
	DtlDataElementTab(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlDataElementTab() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		MsgDb::DataElementTab::Edit();
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
	DtlDataElementTabDeleteBinder m_deleteBinder;
	DtlDbRecordSet<MsgDb::DataElementTab, MsgDb::DataElementTabRow> m_dtlRecSet;

};


}                           


#endif

