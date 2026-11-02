/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRPDB_DOCUMENTTAB_H
#define KONGA_DTL_CRPDB_DOCUMENTTAB_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/PDb/DocumentTab.h>

namespace dtl {

template<> class DefaultSelValidate<PDb::DocumentTabRow>
{
public:
	bool operator()(BoundIOs &cols, PDb::DocumentTabRow &r)
	{
		r.IsProcessIdNull = cols["ProcessId"].IsNull();
		r.IsProcessVersionNull = cols["ProcessVersion"].IsNull();
		r.IsTaskIdNull = cols["TaskId"].IsNull();
		r.IsDocumentIdNull = cols["DocumentId"].IsNull();
		r.IsMsgTemplateNameNull = cols["MsgTemplateName"].IsNull();
		r.IsMsgTemplateLocationNull = cols["MsgTemplateLocation"].IsNull();
		r.IsURLNull = cols["URL"].IsNull();
		r.IsSendFormatNull = cols["SendFormat"].IsNull();
		r.IsToDocDataElementListNull = cols["ToDocDataElementList"].IsNull();
		r.IsFromDocDataElementListNull = cols["FromDocDataElementList"].IsNull();
		r.IsMsgSubjectNull = cols["MsgSubject"].IsNull();
		r.IsMsgCCNull = cols["MsgCC"].IsNull();
		r.IsMsgBCCNull = cols["MsgBCC"].IsNull();
		r.IsMsgPriorityNull = cols["MsgPriority"].IsNull();
		r.IsMsgReplyToNull = cols["MsgReplyTo"].IsNull();
		r.IsMsgFromNull = cols["MsgFrom"].IsNull();
		r.IsInsertTimeNull = cols["InsertTime"].IsNull();
		r.IsModTimeNull = cols["ModTime"].IsNull();
		  
                                        
                       
    
		return true;
	}
};

template<> class DefaultInsValidate<PDb::DocumentTabRow>
{
public:
	bool operator()(BoundIOs &cols, PDb::DocumentTabRow &r)
	{
		if( r.IsProcessIdNull )
			cols["ProcessId"].SetNull();
		else
			cols["ProcessId"].ClearNull();
		if( r.IsProcessVersionNull )
			cols["ProcessVersion"].SetNull();
		else
			cols["ProcessVersion"].ClearNull();
		if( r.IsTaskIdNull )
			cols["TaskId"].SetNull();
		else
			cols["TaskId"].ClearNull();
		if( r.IsDocumentIdNull )
			cols["DocumentId"].SetNull();
		else
			cols["DocumentId"].ClearNull();
		if( r.IsMsgTemplateNameNull )
			cols["MsgTemplateName"].SetNull();
		else
			cols["MsgTemplateName"].ClearNull();
		if( r.IsMsgTemplateLocationNull )
			cols["MsgTemplateLocation"].SetNull();
		else
			cols["MsgTemplateLocation"].ClearNull();
		if( r.IsURLNull )
			cols["URL"].SetNull();
		else
			cols["URL"].ClearNull();
		if( r.IsSendFormatNull )
			cols["SendFormat"].SetNull();
		else
			cols["SendFormat"].ClearNull();
		if( r.IsToDocDataElementListNull )
			cols["ToDocDataElementList"].SetNull();
		else
			cols["ToDocDataElementList"].ClearNull();
		if( r.IsFromDocDataElementListNull )
			cols["FromDocDataElementList"].SetNull();
		else
			cols["FromDocDataElementList"].ClearNull();
		if( r.IsMsgSubjectNull )
			cols["MsgSubject"].SetNull();
		else
			cols["MsgSubject"].ClearNull();
		if( r.IsMsgCCNull )
			cols["MsgCC"].SetNull();
		else
			cols["MsgCC"].ClearNull();
		if( r.IsMsgBCCNull )
			cols["MsgBCC"].SetNull();
		else
			cols["MsgBCC"].ClearNull();
		if( r.IsMsgPriorityNull )
			cols["MsgPriority"].SetNull();
		else
			cols["MsgPriority"].ClearNull();
		if( r.IsMsgReplyToNull )
			cols["MsgReplyTo"].SetNull();
		else
			cols["MsgReplyTo"].ClearNull();
		if( r.IsMsgFromNull )
			cols["MsgFrom"].SetNull();
		else
			cols["MsgFrom"].ClearNull();
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

template<> class DefaultBCA<PDb::DocumentTabRow>
{
public:
	void operator()(BoundIOs &cols, PDb::DocumentTabRow &rowbuf)
	{
		cols["ProcessId"] == rowbuf.field_ProcessId;
		cols["ProcessVersion"] == rowbuf.field_ProcessVersion;
		cols["TaskId"] == rowbuf.field_TaskId;
		cols["DocumentId"] == rowbuf.field_DocumentId;
		cols["MsgTemplateName"] == rowbuf.field_MsgTemplateName;
		cols["MsgTemplateLocation"] == rowbuf.field_MsgTemplateLocation;
		cols["URL"] == rowbuf.field_URL;
		cols["SendFormat"] == rowbuf.field_SendFormat;
		cols["ToDocDataElementList"] == rowbuf.field_ToDocDataElementList;
		cols["FromDocDataElementList"] == rowbuf.field_FromDocDataElementList;
		cols["MsgSubject"] == rowbuf.field_MsgSubject;
		cols["MsgCC"] == rowbuf.field_MsgCC;
		cols["MsgBCC"] == rowbuf.field_MsgBCC;
		cols["MsgPriority"] == rowbuf.field_MsgPriority;
		cols["MsgReplyTo"] == rowbuf.field_MsgReplyTo;
		cols["MsgFrom"] == rowbuf.field_MsgFrom;
		cols["InsertTime"] == rowbuf.field_InsertTime;
		cols["ModTime"] == rowbuf.field_ModTime;
		  
                                        
                       
    
	}
};


}                     



namespace dtl_pdb {

class DtlDocumentTabDeleteBinder : public CustomBinder<PDb::DocumentTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		PDb::DocumentTabRow& rowbuf = *m_pRow;
		cols["ProcessId"] == rowbuf.field_ProcessId;
		cols["ProcessVersion"] == rowbuf.field_ProcessVersion;
		cols["TaskId"] == rowbuf.field_TaskId;
		cols["DocumentId"] == rowbuf.field_DocumentId;
	}
};

class DtlDocumentTab : public PDb::DocumentTab {

public:
	DtlDocumentTab(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlDocumentTab() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		PDb::DocumentTab::Edit();
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
	DtlDocumentTabDeleteBinder m_deleteBinder;
	DtlDbRecordSet<PDb::DocumentTab, PDb::DocumentTabRow> m_dtlRecSet;

};


}                         


#endif

