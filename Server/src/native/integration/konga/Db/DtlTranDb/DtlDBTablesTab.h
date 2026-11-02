/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRTRANDB_DBTABLESTAB_H
#define KONGA_DTL_CRTRANDB_DBTABLESTAB_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/TranDb/DBTablesTab.h>

namespace dtl {

template<> class DefaultSelValidate<TranDb::DBTablesTabRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::DBTablesTabRow &r)
	{
		r.IsDBTableIDNull = cols["DBTableID"].IsNull();
		r.IsTransformation_IDNull = cols["Transformation_ID"].IsNull();
		r.IsTablesNull = cols["Tables"].IsNull();
		r.IsTablesTypeNull = cols["TablesType"].IsNull();
		r.IsInsertTimeNull = cols["InsertTime"].IsNull();
		r.IsModTimeNull = cols["ModTime"].IsNull();
		  
                                        
                       
    
		return true;
	}
};

template<> class DefaultInsValidate<TranDb::DBTablesTabRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::DBTablesTabRow &r)
	{
		if( r.IsTransformation_IDNull )
			cols["Transformation_ID"].SetNull();
		else
			cols["Transformation_ID"].ClearNull();
		if( r.IsTablesNull )
			cols["Tables"].SetNull();
		else
			cols["Tables"].ClearNull();
		if( r.IsTablesTypeNull )
			cols["TablesType"].SetNull();
		else
			cols["TablesType"].ClearNull();
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

template<> class DefaultBCA<TranDb::DBTablesTabRow>
{
public:
	void operator()(BoundIOs &cols, TranDb::DBTablesTabRow &rowbuf)
	{
		cols["DBTableID"] == rowbuf.field_DBTableID;
		cols["Transformation_ID"] == rowbuf.field_Transformation_ID;
		cols["Tables"] == rowbuf.field_Tables;
		cols["TablesType"] == rowbuf.field_TablesType;
		cols["InsertTime"] == rowbuf.field_InsertTime;
		cols["ModTime"] == rowbuf.field_ModTime;
		  
                                        
                       
    
	}
};


}                     



namespace dtl_trandb {


class DtlDBTablesTabInsertBinder : public CustomBinder<TranDb::DBTablesTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		TranDb::DBTablesTabRow& rowbuf = *m_pRow;
		cols["Transformation_ID"] << rowbuf.field_Transformation_ID;
		cols["Tables"] << rowbuf.field_Tables;
		cols["TablesType"] << rowbuf.field_TablesType;
		cols["InsertTime"] << rowbuf.field_InsertTime;
		cols["ModTime"] << rowbuf.field_ModTime;
	}
};

class DtlDBTablesTabDeleteBinder : public CustomBinder<TranDb::DBTablesTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		TranDb::DBTablesTabRow& rowbuf = *m_pRow;
		cols["DBTableID"] == rowbuf.field_DBTableID;
	}
};

class DtlDBTablesTab : public TranDb::DBTablesTab {

public:
	DtlDBTablesTab(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlDBTablesTab() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		TranDb::DBTablesTab::Edit();
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
	DtlDBTablesTabInsertBinder m_insertBinder;
	DtlDBTablesTabDeleteBinder m_deleteBinder;
	DtlDbRecordSet<TranDb::DBTablesTab, TranDb::DBTablesTabRow> m_dtlRecSet;

};


}                            


#endif

