/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRTRANDB_DOCUMENTSTAB_H
#define KONGA_DTL_CRTRANDB_DOCUMENTSTAB_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/TranDb/DocumentsTab.h>

namespace dtl {

template<> class DefaultSelValidate<TranDb::DocumentsTabRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::DocumentsTabRow &r)
	{
		r.IsDocument_IDNull = cols["Document_ID"].IsNull();
		r.IsDocument_NameNull = cols["Document_Name"].IsNull();
		r.IsFormat_Type_IDNull = cols["Format_Type_ID"].IsNull();
		r.IsDelimiterNull = cols["Delimiter"].IsNull();
		r.IsText_QualIFierNull = cols["Text_QualIFier"].IsNull();
		r.IsNumeric_QualIFierNull = cols["Numeric_QualIFier"].IsNull();
		r.IsInsertTimeNull = cols["InsertTime"].IsNull();
		r.IsModTimeNull = cols["ModTime"].IsNull();
		  
                                        
                       
    
		return true;
	}
};

template<> class DefaultInsValidate<TranDb::DocumentsTabRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::DocumentsTabRow &r)
	{
		if( r.IsDocument_IDNull )
			cols["Document_ID"].SetNull();
		else
			cols["Document_ID"].ClearNull();
		if( r.IsDocument_NameNull )
			cols["Document_Name"].SetNull();
		else
			cols["Document_Name"].ClearNull();
		if( r.IsFormat_Type_IDNull )
			cols["Format_Type_ID"].SetNull();
		else
			cols["Format_Type_ID"].ClearNull();
		if( r.IsDelimiterNull )
			cols["Delimiter"].SetNull();
		else
			cols["Delimiter"].ClearNull();
		if( r.IsText_QualIFierNull )
			cols["Text_QualIFier"].SetNull();
		else
			cols["Text_QualIFier"].ClearNull();
		if( r.IsNumeric_QualIFierNull )
			cols["Numeric_QualIFier"].SetNull();
		else
			cols["Numeric_QualIFier"].ClearNull();
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

template<> class DefaultBCA<TranDb::DocumentsTabRow>
{
public:
	void operator()(BoundIOs &cols, TranDb::DocumentsTabRow &rowbuf)
	{
		cols["Document_ID"] == rowbuf.field_Document_ID;
		cols["Document_Name"] == rowbuf.field_Document_Name;
		cols["Format_Type_ID"] == rowbuf.field_Format_Type_ID;
		cols["Delimiter"] == rowbuf.field_Delimiter;
		cols["Text_QualIFier"] == rowbuf.field_Text_QualIFier;
		cols["Numeric_QualIFier"] == rowbuf.field_Numeric_QualIFier;
		cols["InsertTime"] == rowbuf.field_InsertTime;
		cols["ModTime"] == rowbuf.field_ModTime;
		  
                                        
                       
    
	}
};


}                     



namespace dtl_trandb {

class DtlDocumentsTabDeleteBinder : public CustomBinder<TranDb::DocumentsTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		TranDb::DocumentsTabRow& rowbuf = *m_pRow;
		cols["Document_ID"] == rowbuf.field_Document_ID;
	}
};

class DtlDocumentsTab : public TranDb::DocumentsTab {

public:
	DtlDocumentsTab(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlDocumentsTab() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		TranDb::DocumentsTab::Edit();
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
	DtlDocumentsTabDeleteBinder m_deleteBinder;
	DtlDbRecordSet<TranDb::DocumentsTab, TranDb::DocumentsTabRow> m_dtlRecSet;

};


}                            


#endif

