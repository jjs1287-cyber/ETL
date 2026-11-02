/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRMSGDB_DISTRIBUTIONJOBTAB_H
#define KONGA_DTL_CRMSGDB_DISTRIBUTIONJOBTAB_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/MsgDb/DistributionJobTab.h>

namespace dtl {

template<> class DefaultSelValidate<MsgDb::DistributionJobTabRow>
{
public:
	bool operator()(BoundIOs &cols, MsgDb::DistributionJobTabRow &r)
	{
		r.IsJobIdNull = cols["JobId"].IsNull();
		r.IsActivationStatusNull = cols["ActivationStatus"].IsNull();
		r.IsInsertTimeNull = cols["InsertTime"].IsNull();
		r.IsModTimeNull = cols["ModTime"].IsNull();
		  
                                        
                       
    
		return true;
	}
};

template<> class DefaultInsValidate<MsgDb::DistributionJobTabRow>
{
public:
	bool operator()(BoundIOs &cols, MsgDb::DistributionJobTabRow &r)
	{
		if( r.IsActivationStatusNull )
			cols["ActivationStatus"].SetNull();
		else
			cols["ActivationStatus"].ClearNull();
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

template<> class DefaultBCA<MsgDb::DistributionJobTabRow>
{
public:
	void operator()(BoundIOs &cols, MsgDb::DistributionJobTabRow &rowbuf)
	{
		cols["JobId"] == rowbuf.field_JobId;
		cols["ActivationStatus"] == rowbuf.field_ActivationStatus;
		cols["InsertTime"] == rowbuf.field_InsertTime;
		cols["ModTime"] == rowbuf.field_ModTime;
		  
                                        
                       
    
	}
};


}                     



namespace dtl_msgdb {


class DtlDistributionJobTabInsertBinder : public CustomBinder<MsgDb::DistributionJobTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		MsgDb::DistributionJobTabRow& rowbuf = *m_pRow;
		cols["ActivationStatus"] << rowbuf.field_ActivationStatus;
		cols["InsertTime"] << rowbuf.field_InsertTime;
		cols["ModTime"] << rowbuf.field_ModTime;
	}
};

class DtlDistributionJobTabDeleteBinder : public CustomBinder<MsgDb::DistributionJobTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		MsgDb::DistributionJobTabRow& rowbuf = *m_pRow;
		cols["JobId"] == rowbuf.field_JobId;
	}
};

class DtlDistributionJobTab : public MsgDb::DistributionJobTab {

public:
	DtlDistributionJobTab(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlDistributionJobTab() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		MsgDb::DistributionJobTab::Edit();
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
	DtlDistributionJobTabInsertBinder m_insertBinder;
	DtlDistributionJobTabDeleteBinder m_deleteBinder;
	DtlDbRecordSet<MsgDb::DistributionJobTab, MsgDb::DistributionJobTabRow> m_dtlRecSet;

};


}                           


#endif

