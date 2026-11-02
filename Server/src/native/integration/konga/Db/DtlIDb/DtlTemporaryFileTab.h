/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRIDB_TEMPORARYFILETAB_H
#define KONGA_DTL_CRIDB_TEMPORARYFILETAB_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/IDb/TemporaryFileTab.h>

namespace dtl {

template<> class DefaultSelValidate<IDb::TemporaryFileTabRow>
{
public:
	bool operator()(BoundIOs &cols, IDb::TemporaryFileTabRow &r)
	{
		r.IsFileIdNull = cols["FileId"].IsNull();
		r.IsProcessIdNull = cols["ProcessId"].IsNull();
		r.IsProcessInstanceIdNull = cols["ProcessInstanceId"].IsNull();
		r.IsFileNameNull = cols["FileName"].IsNull();
		r.IsInsertTimeNull = cols["InsertTime"].IsNull();
		r.IsModTimeNull = cols["ModTime"].IsNull();
		  
                                        
                       
    
		return true;
	}
};

template<> class DefaultInsValidate<IDb::TemporaryFileTabRow>
{
public:
	bool operator()(BoundIOs &cols, IDb::TemporaryFileTabRow &r)
	{
		if( r.IsProcessIdNull )
			cols["ProcessId"].SetNull();
		else
			cols["ProcessId"].ClearNull();
		if( r.IsProcessInstanceIdNull )
			cols["ProcessInstanceId"].SetNull();
		else
			cols["ProcessInstanceId"].ClearNull();
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

template<> class DefaultBCA<IDb::TemporaryFileTabRow>
{
public:
	void operator()(BoundIOs &cols, IDb::TemporaryFileTabRow &rowbuf)
	{
		cols["FileId"] == rowbuf.field_FileId;
		cols["ProcessId"] == rowbuf.field_ProcessId;
		cols["ProcessInstanceId"] == rowbuf.field_ProcessInstanceId;
		cols["FileName"] == rowbuf.field_FileName;
		cols["InsertTime"] == rowbuf.field_InsertTime;
		cols["ModTime"] == rowbuf.field_ModTime;
		  
                                        
                       
    
	}
};


}                     



namespace dtl_idb {


class DtlTemporaryFileTabInsertBinder : public CustomBinder<IDb::TemporaryFileTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		IDb::TemporaryFileTabRow& rowbuf = *m_pRow;
		cols["ProcessId"] << rowbuf.field_ProcessId;
		cols["ProcessInstanceId"] << rowbuf.field_ProcessInstanceId;
		cols["FileName"] << rowbuf.field_FileName;
		cols["InsertTime"] << rowbuf.field_InsertTime;
		cols["ModTime"] << rowbuf.field_ModTime;
	}
};

class DtlTemporaryFileTabDeleteBinder : public CustomBinder<IDb::TemporaryFileTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		IDb::TemporaryFileTabRow& rowbuf = *m_pRow;
		cols["FileId"] == rowbuf.field_FileId;
	}
};

class DtlTemporaryFileTab : public IDb::TemporaryFileTab {

public:
	DtlTemporaryFileTab(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlTemporaryFileTab() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		IDb::TemporaryFileTab::Edit();
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
	DtlTemporaryFileTabInsertBinder m_insertBinder;
	DtlTemporaryFileTabDeleteBinder m_deleteBinder;
	DtlDbRecordSet<IDb::TemporaryFileTab, IDb::TemporaryFileTabRow> m_dtlRecSet;

};


}                         


#endif

