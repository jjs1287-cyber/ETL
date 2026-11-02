/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRTRANDB_DBFIELDSTAB_H
#define KONGA_DTL_CRTRANDB_DBFIELDSTAB_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/TranDb/DBFieldsTab.h>

namespace dtl {

template<> class DefaultSelValidate<TranDb::DBFieldsTabRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::DBFieldsTabRow &r)
	{
		r.IsDBFieldIDNull = cols["DBFieldID"].IsNull();
		r.IsTransformation_IDNull = cols["Transformation_ID"].IsNull();
		r.IsKeyFieldsNull = cols["KeyFields"].IsNull();
		r.IsKeyFieldsTypeNull = cols["KeyFieldsType"].IsNull();
		r.IsInsertTimeNull = cols["InsertTime"].IsNull();
		r.IsModTimeNull = cols["ModTime"].IsNull();
		  
                                        
                       
    
		return true;
	}
};

template<> class DefaultInsValidate<TranDb::DBFieldsTabRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::DBFieldsTabRow &r)
	{
		if( r.IsTransformation_IDNull )
			cols["Transformation_ID"].SetNull();
		else
			cols["Transformation_ID"].ClearNull();
		if( r.IsKeyFieldsNull )
			cols["KeyFields"].SetNull();
		else
			cols["KeyFields"].ClearNull();
		if( r.IsKeyFieldsTypeNull )
			cols["KeyFieldsType"].SetNull();
		else
			cols["KeyFieldsType"].ClearNull();
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

template<> class DefaultBCA<TranDb::DBFieldsTabRow>
{
public:
	void operator()(BoundIOs &cols, TranDb::DBFieldsTabRow &rowbuf)
	{
		cols["DBFieldID"] == rowbuf.field_DBFieldID;
		cols["Transformation_ID"] == rowbuf.field_Transformation_ID;
		cols["KeyFields"] == rowbuf.field_KeyFields;
		cols["KeyFieldsType"] == rowbuf.field_KeyFieldsType;
		cols["InsertTime"] == rowbuf.field_InsertTime;
		cols["ModTime"] == rowbuf.field_ModTime;
		  
                                        
                       
    
	}
};


}                     



namespace dtl_trandb {


class DtlDBFieldsTabInsertBinder : public CustomBinder<TranDb::DBFieldsTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		TranDb::DBFieldsTabRow& rowbuf = *m_pRow;
		cols["Transformation_ID"] << rowbuf.field_Transformation_ID;
		cols["KeyFields"] << rowbuf.field_KeyFields;
		cols["KeyFieldsType"] << rowbuf.field_KeyFieldsType;
		cols["InsertTime"] << rowbuf.field_InsertTime;
		cols["ModTime"] << rowbuf.field_ModTime;
	}
};

class DtlDBFieldsTabDeleteBinder : public CustomBinder<TranDb::DBFieldsTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		TranDb::DBFieldsTabRow& rowbuf = *m_pRow;
		cols["DBFieldID"] == rowbuf.field_DBFieldID;
	}
};

class DtlDBFieldsTab : public TranDb::DBFieldsTab {

public:
	DtlDBFieldsTab(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlDBFieldsTab() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		TranDb::DBFieldsTab::Edit();
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
	DtlDBFieldsTabInsertBinder m_insertBinder;
	DtlDBFieldsTabDeleteBinder m_deleteBinder;
	DtlDbRecordSet<TranDb::DBFieldsTab, TranDb::DBFieldsTabRow> m_dtlRecSet;

};


}                            


#endif

