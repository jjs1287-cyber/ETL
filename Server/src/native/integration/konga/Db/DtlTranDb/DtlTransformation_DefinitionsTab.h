/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRTRANDB_TRANSFORMATION_DEFINITIONSTAB_H
#define KONGA_DTL_CRTRANDB_TRANSFORMATION_DEFINITIONSTAB_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/TranDb/Transformation_DefinitionsTab.h>

namespace dtl {

template<> class DefaultSelValidate<TranDb::Transformation_DefinitionsTabRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::Transformation_DefinitionsTabRow &r)
	{
		r.IsTranDiffIDNull = cols["TranDiffID"].IsNull();
		r.IsTransformation_IDNull = cols["Transformation_ID"].IsNull();
		r.IsTarget_Field_IDNull = cols["Target_Field_ID"].IsNull();
		r.IsTransformationNull = cols["Transformation"].IsNull();
		r.IsInsertTimeNull = cols["InsertTime"].IsNull();
		r.IsModTimeNull = cols["ModTime"].IsNull();
		  
                                        
                       
    
		return true;
	}
};

template<> class DefaultInsValidate<TranDb::Transformation_DefinitionsTabRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::Transformation_DefinitionsTabRow &r)
	{
		if( r.IsTransformation_IDNull )
			cols["Transformation_ID"].SetNull();
		else
			cols["Transformation_ID"].ClearNull();
		if( r.IsTarget_Field_IDNull )
			cols["Target_Field_ID"].SetNull();
		else
			cols["Target_Field_ID"].ClearNull();
		if( r.IsTransformationNull )
			cols["Transformation"].SetNull();
		else
			cols["Transformation"].ClearNull();
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

template<> class DefaultBCA<TranDb::Transformation_DefinitionsTabRow>
{
public:
	void operator()(BoundIOs &cols, TranDb::Transformation_DefinitionsTabRow &rowbuf)
	{
		cols["TranDiffID"] == rowbuf.field_TranDiffID;
		cols["Transformation_ID"] == rowbuf.field_Transformation_ID;
		cols["Target_Field_ID"] == rowbuf.field_Target_Field_ID;
		cols["Transformation"] == rowbuf.field_Transformation;
		cols["InsertTime"] == rowbuf.field_InsertTime;
		cols["ModTime"] == rowbuf.field_ModTime;
		  
                                        
                       
    
	}
};


}                     



namespace dtl_trandb {


class DtlTransformation_DefinitionsTabInsertBinder : public CustomBinder<TranDb::Transformation_DefinitionsTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		TranDb::Transformation_DefinitionsTabRow& rowbuf = *m_pRow;
		cols["Transformation_ID"] << rowbuf.field_Transformation_ID;
		cols["Target_Field_ID"] << rowbuf.field_Target_Field_ID;
		cols["Transformation"] << rowbuf.field_Transformation;
		cols["InsertTime"] << rowbuf.field_InsertTime;
		cols["ModTime"] << rowbuf.field_ModTime;
	}
};

class DtlTransformation_DefinitionsTabDeleteBinder : public CustomBinder<TranDb::Transformation_DefinitionsTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		TranDb::Transformation_DefinitionsTabRow& rowbuf = *m_pRow;
		cols["TranDiffID"] == rowbuf.field_TranDiffID;
	}
};

class DtlTransformation_DefinitionsTab : public TranDb::Transformation_DefinitionsTab {

public:
	DtlTransformation_DefinitionsTab(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlTransformation_DefinitionsTab() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		TranDb::Transformation_DefinitionsTab::Edit();
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
	DtlTransformation_DefinitionsTabInsertBinder m_insertBinder;
	DtlTransformation_DefinitionsTabDeleteBinder m_deleteBinder;
	DtlDbRecordSet<TranDb::Transformation_DefinitionsTab, TranDb::Transformation_DefinitionsTabRow> m_dtlRecSet;

};


}                            


#endif

