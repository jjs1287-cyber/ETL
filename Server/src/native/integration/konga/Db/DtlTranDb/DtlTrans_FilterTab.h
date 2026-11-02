/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRTRANDB_TRANS_FILTERTAB_H
#define KONGA_DTL_CRTRANDB_TRANS_FILTERTAB_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/TranDb/Trans_FilterTab.h>

namespace dtl {

template<> class DefaultSelValidate<TranDb::Trans_FilterTabRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::Trans_FilterTabRow &r)
	{
		r.IsTrans_Filter_IDNull = cols["Trans_Filter_ID"].IsNull();
		r.IsTransformation_IDNull = cols["Transformation_ID"].IsNull();
		r.IsGroup_IDNull = cols["Group_ID"].IsNull();
		r.IsTNameNull = cols["TName"].IsNull();
		r.IsCNameNull = cols["CName"].IsNull();
		r.IsConditionNull = cols["Condition"].IsNull();
		r.IsSortByNull = cols["SortBy"].IsNull();
		r.IsInsertTimeNull = cols["InsertTime"].IsNull();
		r.IsModTimeNull = cols["ModTime"].IsNull();
		  
                                        
                       
    
		return true;
	}
};

template<> class DefaultInsValidate<TranDb::Trans_FilterTabRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::Trans_FilterTabRow &r)
	{
		if( r.IsTransformation_IDNull )
			cols["Transformation_ID"].SetNull();
		else
			cols["Transformation_ID"].ClearNull();
		if( r.IsGroup_IDNull )
			cols["Group_ID"].SetNull();
		else
			cols["Group_ID"].ClearNull();
		if( r.IsTNameNull )
			cols["TName"].SetNull();
		else
			cols["TName"].ClearNull();
		if( r.IsCNameNull )
			cols["CName"].SetNull();
		else
			cols["CName"].ClearNull();
		if( r.IsConditionNull )
			cols["Condition"].SetNull();
		else
			cols["Condition"].ClearNull();
		if( r.IsSortByNull )
			cols["SortBy"].SetNull();
		else
			cols["SortBy"].ClearNull();
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

template<> class DefaultBCA<TranDb::Trans_FilterTabRow>
{
public:
	void operator()(BoundIOs &cols, TranDb::Trans_FilterTabRow &rowbuf)
	{
		cols["Trans_Filter_ID"] == rowbuf.field_Trans_Filter_ID;
		cols["Transformation_ID"] == rowbuf.field_Transformation_ID;
		cols["Group_ID"] == rowbuf.field_Group_ID;
		cols["TName"] == rowbuf.field_TName;
		cols["CName"] == rowbuf.field_CName;
		cols["Condition"] == rowbuf.field_Condition;
		cols["SortBy"] == rowbuf.field_SortBy;
		cols["InsertTime"] == rowbuf.field_InsertTime;
		cols["ModTime"] == rowbuf.field_ModTime;
		  
                                        
                       
    
	}
};


}                     



namespace dtl_trandb {


class DtlTrans_FilterTabInsertBinder : public CustomBinder<TranDb::Trans_FilterTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		TranDb::Trans_FilterTabRow& rowbuf = *m_pRow;
		cols["Transformation_ID"] << rowbuf.field_Transformation_ID;
		cols["Group_ID"] << rowbuf.field_Group_ID;
		cols["TName"] << rowbuf.field_TName;
		cols["CName"] << rowbuf.field_CName;
		cols["Condition"] << rowbuf.field_Condition;
		cols["SortBy"] << rowbuf.field_SortBy;
		cols["InsertTime"] << rowbuf.field_InsertTime;
		cols["ModTime"] << rowbuf.field_ModTime;
	}
};

class DtlTrans_FilterTabDeleteBinder : public CustomBinder<TranDb::Trans_FilterTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		TranDb::Trans_FilterTabRow& rowbuf = *m_pRow;
		cols["Trans_Filter_ID"] == rowbuf.field_Trans_Filter_ID;
	}
};

class DtlTrans_FilterTab : public TranDb::Trans_FilterTab {

public:
	DtlTrans_FilterTab(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlTrans_FilterTab() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		TranDb::Trans_FilterTab::Edit();
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
	DtlTrans_FilterTabInsertBinder m_insertBinder;
	DtlTrans_FilterTabDeleteBinder m_deleteBinder;
	DtlDbRecordSet<TranDb::Trans_FilterTab, TranDb::Trans_FilterTabRow> m_dtlRecSet;

};


}                            


#endif

