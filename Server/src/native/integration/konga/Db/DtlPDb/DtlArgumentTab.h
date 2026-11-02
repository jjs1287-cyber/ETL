/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRPDB_ARGUMENTTAB_H
#define KONGA_DTL_CRPDB_ARGUMENTTAB_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/PDb/ArgumentTab.h>

namespace dtl {

template<> class DefaultSelValidate<PDb::ArgumentTabRow>
{
public:
	bool operator()(BoundIOs &cols, PDb::ArgumentTabRow &r)
	{
		r.IsProcessIdNull = cols["ProcessId"].IsNull();
		r.IsProcessVersionNull = cols["ProcessVersion"].IsNull();
		r.IsTaskIdNull = cols["TaskId"].IsNull();
		r.IsCallIdNull = cols["CallId"].IsNull();
		r.IsArgumentIdNull = cols["ArgumentId"].IsNull();
		r.IsDataTypeNull = cols["DataType"].IsNull();
		r.IsInputParamNull = cols["InputParam"].IsNull();
		r.IsOutputParamNull = cols["OutputParam"].IsNull();
		r.IsReturnValueNull = cols["ReturnValue"].IsNull();
		r.IsOptionalParamNull = cols["OptionalParam"].IsNull();
		r.IsInsertTimeNull = cols["InsertTime"].IsNull();
		r.IsModTimeNull = cols["ModTime"].IsNull();
		  
                                        
                       
    
		return true;
	}
};

template<> class DefaultInsValidate<PDb::ArgumentTabRow>
{
public:
	bool operator()(BoundIOs &cols, PDb::ArgumentTabRow &r)
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
		if( r.IsCallIdNull )
			cols["CallId"].SetNull();
		else
			cols["CallId"].ClearNull();
		if( r.IsArgumentIdNull )
			cols["ArgumentId"].SetNull();
		else
			cols["ArgumentId"].ClearNull();
		if( r.IsDataTypeNull )
			cols["DataType"].SetNull();
		else
			cols["DataType"].ClearNull();
		if( r.IsInputParamNull )
			cols["InputParam"].SetNull();
		else
			cols["InputParam"].ClearNull();
		if( r.IsOutputParamNull )
			cols["OutputParam"].SetNull();
		else
			cols["OutputParam"].ClearNull();
		if( r.IsReturnValueNull )
			cols["ReturnValue"].SetNull();
		else
			cols["ReturnValue"].ClearNull();
		if( r.IsOptionalParamNull )
			cols["OptionalParam"].SetNull();
		else
			cols["OptionalParam"].ClearNull();
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

template<> class DefaultBCA<PDb::ArgumentTabRow>
{
public:
	void operator()(BoundIOs &cols, PDb::ArgumentTabRow &rowbuf)
	{
		cols["ProcessId"] == rowbuf.field_ProcessId;
		cols["ProcessVersion"] == rowbuf.field_ProcessVersion;
		cols["TaskId"] == rowbuf.field_TaskId;
		cols["CallId"] == rowbuf.field_CallId;
		cols["ArgumentId"] == rowbuf.field_ArgumentId;
		cols["DataType"] == rowbuf.field_DataType;
		cols["InputParam"] == rowbuf.field_InputParam;
		cols["OutputParam"] == rowbuf.field_OutputParam;
		cols["ReturnValue"] == rowbuf.field_ReturnValue;
		cols["OptionalParam"] == rowbuf.field_OptionalParam;
		cols["InsertTime"] == rowbuf.field_InsertTime;
		cols["ModTime"] == rowbuf.field_ModTime;
		  
                                        
                       
    
	}
};


}                     



namespace dtl_pdb {

class DtlArgumentTabDeleteBinder : public CustomBinder<PDb::ArgumentTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		PDb::ArgumentTabRow& rowbuf = *m_pRow;
		cols["ProcessId"] == rowbuf.field_ProcessId;
		cols["ProcessVersion"] == rowbuf.field_ProcessVersion;
		cols["TaskId"] == rowbuf.field_TaskId;
		cols["CallId"] == rowbuf.field_CallId;
		cols["ArgumentId"] == rowbuf.field_ArgumentId;
	}
};

class DtlArgumentTab : public PDb::ArgumentTab {

public:
	DtlArgumentTab(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlArgumentTab() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		PDb::ArgumentTab::Edit();
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
	DtlArgumentTabDeleteBinder m_deleteBinder;
	DtlDbRecordSet<PDb::ArgumentTab, PDb::ArgumentTabRow> m_dtlRecSet;

};


}                         


#endif

