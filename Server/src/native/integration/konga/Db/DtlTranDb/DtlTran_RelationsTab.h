/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRTRANDB_TRAN_RELATIONSTAB_H
#define KONGA_DTL_CRTRANDB_TRAN_RELATIONSTAB_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/TranDb/Tran_RelationsTab.h>

namespace dtl {

template<> class DefaultSelValidate<TranDb::Tran_RelationsTabRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::Tran_RelationsTabRow &r)
	{
		r.IsRelation_IDNull = cols["Relation_ID"].IsNull();
		r.IsTransformation_IDNull = cols["Transformation_ID"].IsNull();
		r.IsDoc_TypeNull = cols["Doc_Type"].IsNull();
		r.IsParent_NameNull = cols["Parent_Name"].IsNull();
		r.IsParent_FieldNull = cols["Parent_Field"].IsNull();
		r.IsChild_NameNull = cols["Child_Name"].IsNull();
		r.IsChild_FieldNull = cols["Child_Field"].IsNull();
		r.IsOuter_JoinNull = cols["Outer_Join"].IsNull();
		r.IsOneToOneNull = cols["OneToOne"].IsNull();
		r.IsInsertTimeNull = cols["InsertTime"].IsNull();
		r.IsModTimeNull = cols["ModTime"].IsNull();
		  
                                        
                       
    
		return true;
	}
};

template<> class DefaultInsValidate<TranDb::Tran_RelationsTabRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::Tran_RelationsTabRow &r)
	{
		if( r.IsTransformation_IDNull )
			cols["Transformation_ID"].SetNull();
		else
			cols["Transformation_ID"].ClearNull();
		if( r.IsDoc_TypeNull )
			cols["Doc_Type"].SetNull();
		else
			cols["Doc_Type"].ClearNull();
		if( r.IsParent_NameNull )
			cols["Parent_Name"].SetNull();
		else
			cols["Parent_Name"].ClearNull();
		if( r.IsParent_FieldNull )
			cols["Parent_Field"].SetNull();
		else
			cols["Parent_Field"].ClearNull();
		if( r.IsChild_NameNull )
			cols["Child_Name"].SetNull();
		else
			cols["Child_Name"].ClearNull();
		if( r.IsChild_FieldNull )
			cols["Child_Field"].SetNull();
		else
			cols["Child_Field"].ClearNull();
		if( r.IsOuter_JoinNull )
			cols["Outer_Join"].SetNull();
		else
			cols["Outer_Join"].ClearNull();
		if( r.IsOneToOneNull )
			cols["OneToOne"].SetNull();
		else
			cols["OneToOne"].ClearNull();
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

template<> class DefaultBCA<TranDb::Tran_RelationsTabRow>
{
public:
	void operator()(BoundIOs &cols, TranDb::Tran_RelationsTabRow &rowbuf)
	{
		cols["Relation_ID"] == rowbuf.field_Relation_ID;
		cols["Transformation_ID"] == rowbuf.field_Transformation_ID;
		cols["Doc_Type"] == rowbuf.field_Doc_Type;
		cols["Parent_Name"] == rowbuf.field_Parent_Name;
		cols["Parent_Field"] == rowbuf.field_Parent_Field;
		cols["Child_Name"] == rowbuf.field_Child_Name;
		cols["Child_Field"] == rowbuf.field_Child_Field;
		cols["Outer_Join"] == rowbuf.field_Outer_Join;
		cols["OneToOne"] == rowbuf.field_OneToOne;
		cols["InsertTime"] == rowbuf.field_InsertTime;
		cols["ModTime"] == rowbuf.field_ModTime;
		  
                                        
                       
    
	}
};


}                     



namespace dtl_trandb {


class DtlTran_RelationsTabInsertBinder : public CustomBinder<TranDb::Tran_RelationsTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		TranDb::Tran_RelationsTabRow& rowbuf = *m_pRow;
		cols["Transformation_ID"] << rowbuf.field_Transformation_ID;
		cols["Doc_Type"] << rowbuf.field_Doc_Type;
		cols["Parent_Name"] << rowbuf.field_Parent_Name;
		cols["Parent_Field"] << rowbuf.field_Parent_Field;
		cols["Child_Name"] << rowbuf.field_Child_Name;
		cols["Child_Field"] << rowbuf.field_Child_Field;
		cols["Outer_Join"] << rowbuf.field_Outer_Join;
		cols["OneToOne"] << rowbuf.field_OneToOne;
		cols["InsertTime"] << rowbuf.field_InsertTime;
		cols["ModTime"] << rowbuf.field_ModTime;
	}
};

class DtlTran_RelationsTabDeleteBinder : public CustomBinder<TranDb::Tran_RelationsTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		TranDb::Tran_RelationsTabRow& rowbuf = *m_pRow;
		cols["Relation_ID"] == rowbuf.field_Relation_ID;
		cols["Transformation_ID"] == rowbuf.field_Transformation_ID;
	}
};

class DtlTran_RelationsTab : public TranDb::Tran_RelationsTab {

public:
	DtlTran_RelationsTab(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlTran_RelationsTab() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		TranDb::Tran_RelationsTab::Edit();
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
	DtlTran_RelationsTabInsertBinder m_insertBinder;
	DtlTran_RelationsTabDeleteBinder m_deleteBinder;
	DtlDbRecordSet<TranDb::Tran_RelationsTab, TranDb::Tran_RelationsTabRow> m_dtlRecSet;

};


}                            


#endif

