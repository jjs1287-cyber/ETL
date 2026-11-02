/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRPDB_TASKDATAELEMENTSTAB_H
#define KONGA_DTL_CRPDB_TASKDATAELEMENTSTAB_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/PDb/TaskDataElementsTab.h>

namespace dtl {

template<> class DefaultSelValidate<PDb::TaskDataElementsTabRow>
{
public:
	bool operator()(BoundIOs &cols, PDb::TaskDataElementsTabRow &r)
	{
		r.IsProcessIdNull = cols["ProcessId"].IsNull();
		r.IsProcessVersionNull = cols["ProcessVersion"].IsNull();
		r.IsTaskIdNull = cols["TaskId"].IsNull();
		r.IsDataElementIdNull = cols["DataElementId"].IsNull();
		r.IsDataElementNameNull = cols["DataElementName"].IsNull();
		r.IsDataElementDescriptionNull = cols["DataElementDescription"].IsNull();
		r.IsDataElementTypeNull = cols["DataElementType"].IsNull();
		r.IsOriginTypeNull = cols["OriginType"].IsNull();
		r.IsInitialValueNull = cols["InitialValue"].IsNull();
		r.IsIsRequiredNull = cols["IsRequired"].IsNull();
		r.IsDataElementDisplayNameNull = cols["DataElementDisplayName"].IsNull();
		r.IsInsertTimeNull = cols["InsertTime"].IsNull();
		r.IsModTimeNull = cols["ModTime"].IsNull();
		  
                                        
                       
    
		return true;
	}
};

template<> class DefaultInsValidate<PDb::TaskDataElementsTabRow>
{
public:
	bool operator()(BoundIOs &cols, PDb::TaskDataElementsTabRow &r)
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
		if( r.IsDataElementIdNull )
			cols["DataElementId"].SetNull();
		else
			cols["DataElementId"].ClearNull();
		if( r.IsDataElementNameNull )
			cols["DataElementName"].SetNull();
		else
			cols["DataElementName"].ClearNull();
		if( r.IsDataElementDescriptionNull )
			cols["DataElementDescription"].SetNull();
		else
			cols["DataElementDescription"].ClearNull();
		if( r.IsDataElementTypeNull )
			cols["DataElementType"].SetNull();
		else
			cols["DataElementType"].ClearNull();
		if( r.IsOriginTypeNull )
			cols["OriginType"].SetNull();
		else
			cols["OriginType"].ClearNull();
		if( r.IsInitialValueNull )
			cols["InitialValue"].SetNull();
		else
			cols["InitialValue"].ClearNull();
		if( r.IsIsRequiredNull )
			cols["IsRequired"].SetNull();
		else
			cols["IsRequired"].ClearNull();
		if( r.IsDataElementDisplayNameNull )
			cols["DataElementDisplayName"].SetNull();
		else
			cols["DataElementDisplayName"].ClearNull();
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

template<> class DefaultBCA<PDb::TaskDataElementsTabRow>
{
public:
	void operator()(BoundIOs &cols, PDb::TaskDataElementsTabRow &rowbuf)
	{
		cols["ProcessId"] == rowbuf.field_ProcessId;
		cols["ProcessVersion"] == rowbuf.field_ProcessVersion;
		cols["TaskId"] == rowbuf.field_TaskId;
		cols["DataElementId"] == rowbuf.field_DataElementId;
		cols["DataElementName"] == rowbuf.field_DataElementName;
		cols["DataElementDescription"] == rowbuf.field_DataElementDescription;
		cols["DataElementType"] == rowbuf.field_DataElementType;
		cols["OriginType"] == rowbuf.field_OriginType;
		cols["InitialValue"] == rowbuf.field_InitialValue;
		cols["IsRequired"] == rowbuf.field_IsRequired;
		cols["DataElementDisplayName"] == rowbuf.field_DataElementDisplayName;
		cols["InsertTime"] == rowbuf.field_InsertTime;
		cols["ModTime"] == rowbuf.field_ModTime;
		  
                                        
                       
    
	}
};


}                     



namespace dtl_pdb {

class DtlTaskDataElementsTabDeleteBinder : public CustomBinder<PDb::TaskDataElementsTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		PDb::TaskDataElementsTabRow& rowbuf = *m_pRow;
		cols["ProcessId"] == rowbuf.field_ProcessId;
		cols["ProcessVersion"] == rowbuf.field_ProcessVersion;
		cols["TaskId"] == rowbuf.field_TaskId;
		cols["DataElementId"] == rowbuf.field_DataElementId;
	}
};

class DtlTaskDataElementsTab : public PDb::TaskDataElementsTab {

public:
	DtlTaskDataElementsTab(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlTaskDataElementsTab() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		PDb::TaskDataElementsTab::Edit();
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
	DtlTaskDataElementsTabDeleteBinder m_deleteBinder;
	DtlDbRecordSet<PDb::TaskDataElementsTab, PDb::TaskDataElementsTabRow> m_dtlRecSet;

};


}                         


#endif

