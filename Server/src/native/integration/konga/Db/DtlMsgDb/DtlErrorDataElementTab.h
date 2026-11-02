/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRMSGDB_ERRORDATAELEMENTTAB_H
#define KONGA_DTL_CRMSGDB_ERRORDATAELEMENTTAB_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/MsgDb/ErrorDataElementTab.h>

namespace dtl {

template<> class DefaultSelValidate<MsgDb::ErrorDataElementTabRow>
{
public:
	bool operator()(BoundIOs &cols, MsgDb::ErrorDataElementTabRow &r)
	{
		r.IsMsgIdNull = cols["MsgId"].IsNull();
		r.IsDataElementNameNull = cols["DataElementName"].IsNull();
		r.IsDataTypeNull = cols["DataType"].IsNull();
		r.IsIntValueNull = cols["IntValue"].IsNull();
		r.IsDoubleValueNull = cols["DoubleValue"].IsNull();
		r.IsStringValueNull = cols["StringValue"].IsNull();
		r.IsErrorIdNull = cols["ErrorId"].IsNull();
		r.IsInsertTimeNull = cols["InsertTime"].IsNull();
		r.IsModTimeNull = cols["ModTime"].IsNull();
		  
                                        
                       
    
		return true;
	}
};

template<> class DefaultInsValidate<MsgDb::ErrorDataElementTabRow>
{
public:
	bool operator()(BoundIOs &cols, MsgDb::ErrorDataElementTabRow &r)
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

template<> class DefaultBCA<MsgDb::ErrorDataElementTabRow>
{
public:
	void operator()(BoundIOs &cols, MsgDb::ErrorDataElementTabRow &rowbuf)
	{
		cols["MsgId"] == rowbuf.field_MsgId;
		cols["DataElementName"] == rowbuf.field_DataElementName;
		cols["DataType"] == rowbuf.field_DataType;
		cols["IntValue"] == rowbuf.field_IntValue;
		cols["DoubleValue"] == rowbuf.field_DoubleValue;
		cols["StringValue"] == rowbuf.field_StringValue;
		cols["ErrorId"] == rowbuf.field_ErrorId;
		cols["InsertTime"] == rowbuf.field_InsertTime;
		cols["ModTime"] == rowbuf.field_ModTime;
		  
                                        
                       
    
	}
};


}                     



namespace dtl_msgdb {


class DtlErrorDataElementTabInsertBinder : public CustomBinder<MsgDb::ErrorDataElementTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		MsgDb::ErrorDataElementTabRow& rowbuf = *m_pRow;
		cols["MsgId"] << rowbuf.field_MsgId;
		cols["DataElementName"] << rowbuf.field_DataElementName;
		cols["DataType"] << rowbuf.field_DataType;
		cols["IntValue"] << rowbuf.field_IntValue;
		cols["DoubleValue"] << rowbuf.field_DoubleValue;
		cols["StringValue"] << rowbuf.field_StringValue;
		cols["InsertTime"] << rowbuf.field_InsertTime;
		cols["ModTime"] << rowbuf.field_ModTime;
	}
};

class DtlErrorDataElementTabDeleteBinder : public CustomBinder<MsgDb::ErrorDataElementTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		MsgDb::ErrorDataElementTabRow& rowbuf = *m_pRow;
		cols["ErrorId"] == rowbuf.field_ErrorId;
	}
};

class DtlErrorDataElementTab : public MsgDb::ErrorDataElementTab {

public:
	DtlErrorDataElementTab(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlErrorDataElementTab() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		MsgDb::ErrorDataElementTab::Edit();
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
	DtlErrorDataElementTabInsertBinder m_insertBinder;
	DtlErrorDataElementTabDeleteBinder m_deleteBinder;
	DtlDbRecordSet<MsgDb::ErrorDataElementTab, MsgDb::ErrorDataElementTabRow> m_dtlRecSet;

};


}                           


#endif

