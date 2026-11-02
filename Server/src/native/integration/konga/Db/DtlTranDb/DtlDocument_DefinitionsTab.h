/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRTRANDB_DOCUMENT_DEFINITIONSTAB_H
#define KONGA_DTL_CRTRANDB_DOCUMENT_DEFINITIONSTAB_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/TranDb/Document_DefinitionsTab.h>

namespace dtl {

template<> class DefaultSelValidate<TranDb::Document_DefinitionsTabRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::Document_DefinitionsTabRow &r)
	{
		r.IsDocument_IDNull = cols["Document_ID"].IsNull();
		r.IsField_IDNull = cols["Field_ID"].IsNull();
		r.IsParent_IDNull = cols["Parent_ID"].IsNull();
		r.IsField_NameNull = cols["Field_Name"].IsNull();
		r.IsSequenceNull = cols["Sequence"].IsNull();
		r.IsVar_Type_IDNull = cols["Var_Type_ID"].IsNull();
		r.IsStart_PosNull = cols["Start_Pos"].IsNull();
		r.IsEnd_PosNull = cols["End_Pos"].IsNull();
		r.IsRequiredNull = cols["Required"].IsNull();
		r.IsDefault_valueNull = cols["Default_value"].IsNull();
		r.IsFormatNull = cols["Format"].IsNull();
		r.IsUni_IDNull = cols["Uni_ID"].IsNull();
		r.IsInsertTimeNull = cols["InsertTime"].IsNull();
		r.IsModTimeNull = cols["ModTime"].IsNull();
		  
                                        
                       
    
		return true;
	}
};

template<> class DefaultInsValidate<TranDb::Document_DefinitionsTabRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::Document_DefinitionsTabRow &r)
	{
		if( r.IsDocument_IDNull )
			cols["Document_ID"].SetNull();
		else
			cols["Document_ID"].ClearNull();
		if( r.IsField_IDNull )
			cols["Field_ID"].SetNull();
		else
			cols["Field_ID"].ClearNull();
		if( r.IsParent_IDNull )
			cols["Parent_ID"].SetNull();
		else
			cols["Parent_ID"].ClearNull();
		if( r.IsField_NameNull )
			cols["Field_Name"].SetNull();
		else
			cols["Field_Name"].ClearNull();
		if( r.IsSequenceNull )
			cols["Sequence"].SetNull();
		else
			cols["Sequence"].ClearNull();
		if( r.IsVar_Type_IDNull )
			cols["Var_Type_ID"].SetNull();
		else
			cols["Var_Type_ID"].ClearNull();
		if( r.IsStart_PosNull )
			cols["Start_Pos"].SetNull();
		else
			cols["Start_Pos"].ClearNull();
		if( r.IsEnd_PosNull )
			cols["End_Pos"].SetNull();
		else
			cols["End_Pos"].ClearNull();
		if( r.IsRequiredNull )
			cols["Required"].SetNull();
		else
			cols["Required"].ClearNull();
		if( r.IsDefault_valueNull )
			cols["Default_value"].SetNull();
		else
			cols["Default_value"].ClearNull();
		if( r.IsFormatNull )
			cols["Format"].SetNull();
		else
			cols["Format"].ClearNull();
		if( r.IsUni_IDNull )
			cols["Uni_ID"].SetNull();
		else
			cols["Uni_ID"].ClearNull();
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

template<> class DefaultBCA<TranDb::Document_DefinitionsTabRow>
{
public:
	void operator()(BoundIOs &cols, TranDb::Document_DefinitionsTabRow &rowbuf)
	{
		cols["Document_ID"] == rowbuf.field_Document_ID;
		cols["Field_ID"] == rowbuf.field_Field_ID;
		cols["Parent_ID"] == rowbuf.field_Parent_ID;
		cols["Field_Name"] == rowbuf.field_Field_Name;
		cols["Sequence"] == rowbuf.field_Sequence;
		cols["Var_Type_ID"] == rowbuf.field_Var_Type_ID;
		cols["Start_Pos"] == rowbuf.field_Start_Pos;
		cols["End_Pos"] == rowbuf.field_End_Pos;
		cols["Required"] == rowbuf.field_Required;
		cols["Default_value"] == rowbuf.field_Default_value;
		cols["Format"] == rowbuf.field_Format;
		cols["Uni_ID"] == rowbuf.field_Uni_ID;
		cols["InsertTime"] == rowbuf.field_InsertTime;
		cols["ModTime"] == rowbuf.field_ModTime;
		  
                                        
                       
    
	}
};


}                     



namespace dtl_trandb {

class DtlDocument_DefinitionsTabDeleteBinder : public CustomBinder<TranDb::Document_DefinitionsTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		TranDb::Document_DefinitionsTabRow& rowbuf = *m_pRow;
		cols["Document_ID"] == rowbuf.field_Document_ID;
		cols["Field_ID"] == rowbuf.field_Field_ID;
		cols["Parent_ID"] == rowbuf.field_Parent_ID;
	}
};

class DtlDocument_DefinitionsTab : public TranDb::Document_DefinitionsTab {

public:
	DtlDocument_DefinitionsTab(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlDocument_DefinitionsTab() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		TranDb::Document_DefinitionsTab::Edit();
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
	DtlDocument_DefinitionsTabDeleteBinder m_deleteBinder;
	DtlDbRecordSet<TranDb::Document_DefinitionsTab, TranDb::Document_DefinitionsTabRow> m_dtlRecSet;

};


}                            


#endif

