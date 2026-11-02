/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRPDB_TASKDISPLAYATTRIBUTES_H
#define KONGA_DTL_CRPDB_TASKDISPLAYATTRIBUTES_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/PDb/TaskDisplayAttributes.h>

namespace dtl {

template<> class DefaultSelValidate<PDb::TaskDisplayAttributesRow>
{
public:
	bool operator()(BoundIOs &cols, PDb::TaskDisplayAttributesRow &r)
	{
		r.IsProcessIdNull = cols["ProcessId"].IsNull();
		r.IsProcessVersionNull = cols["ProcessVersion"].IsNull();
		r.IsTaskIdNull = cols["TaskId"].IsNull();
		r.IsTopXNull = cols["TopX"].IsNull();
		r.IsTopYNull = cols["TopY"].IsNull();
		r.IsBottomXNull = cols["BottomX"].IsNull();
		r.IsBottomYNull = cols["BottomY"].IsNull();
		r.IsInsertTimeNull = cols["InsertTime"].IsNull();
		r.IsModTimeNull = cols["ModTime"].IsNull();
		  
                                        
                       
    
		return true;
	}
};

template<> class DefaultInsValidate<PDb::TaskDisplayAttributesRow>
{
public:
	bool operator()(BoundIOs &cols, PDb::TaskDisplayAttributesRow &r)
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
		if( r.IsTopXNull )
			cols["TopX"].SetNull();
		else
			cols["TopX"].ClearNull();
		if( r.IsTopYNull )
			cols["TopY"].SetNull();
		else
			cols["TopY"].ClearNull();
		if( r.IsBottomXNull )
			cols["BottomX"].SetNull();
		else
			cols["BottomX"].ClearNull();
		if( r.IsBottomYNull )
			cols["BottomY"].SetNull();
		else
			cols["BottomY"].ClearNull();
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

template<> class DefaultBCA<PDb::TaskDisplayAttributesRow>
{
public:
	void operator()(BoundIOs &cols, PDb::TaskDisplayAttributesRow &rowbuf)
	{
		cols["ProcessId"] == rowbuf.field_ProcessId;
		cols["ProcessVersion"] == rowbuf.field_ProcessVersion;
		cols["TaskId"] == rowbuf.field_TaskId;
		cols["TopX"] == rowbuf.field_TopX;
		cols["TopY"] == rowbuf.field_TopY;
		cols["BottomX"] == rowbuf.field_BottomX;
		cols["BottomY"] == rowbuf.field_BottomY;
		cols["InsertTime"] == rowbuf.field_InsertTime;
		cols["ModTime"] == rowbuf.field_ModTime;
		  
                                        
                       
    
	}
};


}                     



namespace dtl_pdb {

class DtlTaskDisplayAttributesDeleteBinder : public CustomBinder<PDb::TaskDisplayAttributesRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		PDb::TaskDisplayAttributesRow& rowbuf = *m_pRow;
		cols["ProcessId"] == rowbuf.field_ProcessId;
		cols["ProcessVersion"] == rowbuf.field_ProcessVersion;
		cols["TaskId"] == rowbuf.field_TaskId;
	}
};

class DtlTaskDisplayAttributes : public PDb::TaskDisplayAttributes {

public:
	DtlTaskDisplayAttributes(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlTaskDisplayAttributes() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		PDb::TaskDisplayAttributes::Edit();
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
	DtlTaskDisplayAttributesDeleteBinder m_deleteBinder;
	DtlDbRecordSet<PDb::TaskDisplayAttributes, PDb::TaskDisplayAttributesRow> m_dtlRecSet;

};


}                         


#endif

