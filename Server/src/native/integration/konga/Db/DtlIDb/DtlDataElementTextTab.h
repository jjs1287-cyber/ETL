/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRIDB_DATAELEMENTTEXTTAB_H
#define KONGA_DTL_CRIDB_DATAELEMENTTEXTTAB_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/IDb/DataElementTextTab.h>

namespace dtl {

template<> class DefaultSelValidate<IDb::DataElementTextTabRow>
{
public:
	bool operator()(BoundIOs &cols, IDb::DataElementTextTabRow &r)
	{
		r.IsProcessIdNull = cols["ProcessId"].IsNull();
		r.IsProcessInstanceIdNull = cols["ProcessInstanceId"].IsNull();
		r.IsTaskIdNull = cols["TaskId"].IsNull();
		r.IsTaskInstanceIdNull = cols["TaskInstanceId"].IsNull();
		r.IsDataElementIdNull = cols["DataElementId"].IsNull();
		r.IsStringValueNull = cols["StringValue"].IsNull();
		r.IsInsertTimeNull = cols["InsertTime"].IsNull();
		r.IsModTimeNull = cols["ModTime"].IsNull();
		  
                                        
           
    
		return true;
	}
};

template<> class DefaultInsValidate<IDb::DataElementTextTabRow>
{
public:
	bool operator()(BoundIOs &cols, IDb::DataElementTextTabRow &r)
	{
		if( r.IsProcessIdNull )
			cols["ProcessId"].SetNull();
		else
			cols["ProcessId"].ClearNull();
		if( r.IsProcessInstanceIdNull )
			cols["ProcessInstanceId"].SetNull();
		else
			cols["ProcessInstanceId"].ClearNull();
		if( r.IsTaskIdNull )
			cols["TaskId"].SetNull();
		else
			cols["TaskId"].ClearNull();
		if( r.IsTaskInstanceIdNull )
			cols["TaskInstanceId"].SetNull();
		else
			cols["TaskInstanceId"].ClearNull();
		if( r.IsDataElementIdNull )
			cols["DataElementId"].SetNull();
		else
			cols["DataElementId"].ClearNull();
		if( r.IsStringValueNull )
			cols["StringValue"].SetNull();
		else
			cols["StringValue"].ClearNull();
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

template<> class DefaultBCA<IDb::DataElementTextTabRow>
{
public:
	void operator()(BoundIOs &cols, IDb::DataElementTextTabRow &rowbuf)
	{
		cols["ProcessId"] == rowbuf.field_ProcessId;
		cols["ProcessInstanceId"] == rowbuf.field_ProcessInstanceId;
		cols["TaskId"] == rowbuf.field_TaskId;
		cols["TaskInstanceId"] == rowbuf.field_TaskInstanceId;
		cols["DataElementId"] == rowbuf.field_DataElementId;
		cols["StringValue"] == rowbuf.field_StringValue;
		cols["InsertTime"] == rowbuf.field_InsertTime;
		cols["ModTime"] == rowbuf.field_ModTime;
		  
                                        
           
    
	}
};


}                     



namespace dtl_idb {

class DtlDataElementTextTabDeleteBinder : public CustomBinder<IDb::DataElementTextTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		IDb::DataElementTextTabRow& rowbuf = *m_pRow;
		cols["ProcessId"] == rowbuf.field_ProcessId;
		cols["ProcessInstanceId"] == rowbuf.field_ProcessInstanceId;
		cols["TaskId"] == rowbuf.field_TaskId;
		cols["TaskInstanceId"] == rowbuf.field_TaskInstanceId;
		cols["DataElementId"] == rowbuf.field_DataElementId;
	}
};

class DtlDataElementTextTab : public IDb::DataElementTextTab {

public:
	DtlDataElementTextTab(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlDataElementTextTab() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		IDb::DataElementTextTab::Edit();
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
	DtlDataElementTextTabDeleteBinder m_deleteBinder;
	DtlDbRecordSet<IDb::DataElementTextTab, IDb::DataElementTextTabRow> m_dtlRecSet;

};


}                         


#endif

