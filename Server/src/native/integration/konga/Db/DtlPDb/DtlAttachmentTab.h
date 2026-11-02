/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRPDB_ATTACHMENTTAB_H
#define KONGA_DTL_CRPDB_ATTACHMENTTAB_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/PDb/AttachmentTab.h>

namespace dtl {

template<> class DefaultSelValidate<PDb::AttachmentTabRow>
{
public:
	bool operator()(BoundIOs &cols, PDb::AttachmentTabRow &r)
	{
		r.IsAttachmentIdNull = cols["AttachmentId"].IsNull();
		r.IsProcessIdNull = cols["ProcessId"].IsNull();
		r.IsProcessVersionNull = cols["ProcessVersion"].IsNull();
		r.IsDocumentIdNull = cols["DocumentId"].IsNull();
		r.IsTransferIdNull = cols["TransferId"].IsNull();
		r.IsMimeContentTypeNull = cols["MimeContentType"].IsNull();
		r.IsFileNameNull = cols["FileName"].IsNull();
		r.IsInsertTimeNull = cols["InsertTime"].IsNull();
		r.IsModTimeNull = cols["ModTime"].IsNull();
		  
                                        
                       
    
		return true;
	}
};

template<> class DefaultInsValidate<PDb::AttachmentTabRow>
{
public:
	bool operator()(BoundIOs &cols, PDb::AttachmentTabRow &r)
	{
		if( r.IsAttachmentIdNull )
			cols["AttachmentId"].SetNull();
		else
			cols["AttachmentId"].ClearNull();
		if( r.IsProcessIdNull )
			cols["ProcessId"].SetNull();
		else
			cols["ProcessId"].ClearNull();
		if( r.IsProcessVersionNull )
			cols["ProcessVersion"].SetNull();
		else
			cols["ProcessVersion"].ClearNull();
		if( r.IsDocumentIdNull )
			cols["DocumentId"].SetNull();
		else
			cols["DocumentId"].ClearNull();
		if( r.IsTransferIdNull )
			cols["TransferId"].SetNull();
		else
			cols["TransferId"].ClearNull();
		if( r.IsMimeContentTypeNull )
			cols["MimeContentType"].SetNull();
		else
			cols["MimeContentType"].ClearNull();
		if( r.IsFileNameNull )
			cols["FileName"].SetNull();
		else
			cols["FileName"].ClearNull();
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

template<> class DefaultBCA<PDb::AttachmentTabRow>
{
public:
	void operator()(BoundIOs &cols, PDb::AttachmentTabRow &rowbuf)
	{
		cols["AttachmentId"] == rowbuf.field_AttachmentId;
		cols["ProcessId"] == rowbuf.field_ProcessId;
		cols["ProcessVersion"] == rowbuf.field_ProcessVersion;
		cols["DocumentId"] == rowbuf.field_DocumentId;
		cols["TransferId"] == rowbuf.field_TransferId;
		cols["MimeContentType"] == rowbuf.field_MimeContentType;
		cols["FileName"] == rowbuf.field_FileName;
		cols["InsertTime"] == rowbuf.field_InsertTime;
		cols["ModTime"] == rowbuf.field_ModTime;
		  
                                        
                       
    
	}
};


}                     



namespace dtl_pdb {

class DtlAttachmentTabDeleteBinder : public CustomBinder<PDb::AttachmentTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		PDb::AttachmentTabRow& rowbuf = *m_pRow;
		cols["AttachmentId"] == rowbuf.field_AttachmentId;
		cols["ProcessId"] == rowbuf.field_ProcessId;
		cols["ProcessVersion"] == rowbuf.field_ProcessVersion;
		cols["DocumentId"] == rowbuf.field_DocumentId;
	}
};

class DtlAttachmentTab : public PDb::AttachmentTab {

public:
	DtlAttachmentTab(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlAttachmentTab() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		PDb::AttachmentTab::Edit();
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
	DtlAttachmentTabDeleteBinder m_deleteBinder;
	DtlDbRecordSet<PDb::AttachmentTab, PDb::AttachmentTabRow> m_dtlRecSet;

};


}                         


#endif

