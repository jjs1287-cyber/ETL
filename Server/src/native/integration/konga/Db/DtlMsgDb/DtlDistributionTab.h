/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRMSGDB_DISTRIBUTIONTAB_H
#define KONGA_DTL_CRMSGDB_DISTRIBUTIONTAB_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/MsgDb/DistributionTab.h>

namespace dtl {

template<> class DefaultSelValidate<MsgDb::DistributionTabRow>
{
public:
	bool operator()(BoundIOs &cols, MsgDb::DistributionTabRow &r)
	{
		r.IsJobDescIdNull = cols["JobDescId"].IsNull();
		r.IsJobIdNull = cols["JobId"].IsNull();
		r.IsKeyNameNull = cols["KeyName"].IsNull();
		r.IsKeyValueNull = cols["KeyValue"].IsNull();
		r.IsInsertTimeNull = cols["InsertTime"].IsNull();
		r.IsModTimeNull = cols["ModTime"].IsNull();
		  
                                        
                       
    
		return true;
	}
};

template<> class DefaultInsValidate<MsgDb::DistributionTabRow>
{
public:
	bool operator()(BoundIOs &cols, MsgDb::DistributionTabRow &r)
	{
		if( r.IsJobIdNull )
			cols["JobId"].SetNull();
		else
			cols["JobId"].ClearNull();
		if( r.IsKeyNameNull )
			cols["KeyName"].SetNull();
		else
			cols["KeyName"].ClearNull();
		if( r.IsKeyValueNull )
			cols["KeyValue"].SetNull();
		else
			cols["KeyValue"].ClearNull();
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

template<> class DefaultBCA<MsgDb::DistributionTabRow>
{
public:
	void operator()(BoundIOs &cols, MsgDb::DistributionTabRow &rowbuf)
	{
		cols["JobDescId"] == rowbuf.field_JobDescId;
		cols["JobId"] == rowbuf.field_JobId;
		cols["KeyName"] == rowbuf.field_KeyName;
		cols["KeyValue"] == rowbuf.field_KeyValue;
		cols["InsertTime"] == rowbuf.field_InsertTime;
		cols["ModTime"] == rowbuf.field_ModTime;
		  
                                        
                       
    
	}
};


}                     



namespace dtl_msgdb {


class DtlDistributionTabInsertBinder : public CustomBinder<MsgDb::DistributionTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		MsgDb::DistributionTabRow& rowbuf = *m_pRow;
		cols["JobId"] << rowbuf.field_JobId;
		cols["KeyName"] << rowbuf.field_KeyName;
		cols["KeyValue"] << rowbuf.field_KeyValue;
		cols["InsertTime"] << rowbuf.field_InsertTime;
		cols["ModTime"] << rowbuf.field_ModTime;
	}
};

class DtlDistributionTabDeleteBinder : public CustomBinder<MsgDb::DistributionTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		MsgDb::DistributionTabRow& rowbuf = *m_pRow;
		cols["JobDescId"] == rowbuf.field_JobDescId;
	}
};

class DtlDistributionTab : public MsgDb::DistributionTab {

public:
	DtlDistributionTab(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlDistributionTab() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		MsgDb::DistributionTab::Edit();
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
	DtlDistributionTabInsertBinder m_insertBinder;
	DtlDistributionTabDeleteBinder m_deleteBinder;
	DtlDbRecordSet<MsgDb::DistributionTab, MsgDb::DistributionTabRow> m_dtlRecSet;

};


}                           


#endif

