/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRPDB_ROUTETAB_H
#define KONGA_DTL_CRPDB_ROUTETAB_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/PDb/RouteTab.h>

namespace dtl {

template<> class DefaultSelValidate<PDb::RouteTabRow>
{
public:
	bool operator()(BoundIOs &cols, PDb::RouteTabRow &r)
	{
		r.IsProcessIdNull = cols["ProcessId"].IsNull();
		r.IsProcessVersionNull = cols["ProcessVersion"].IsNull();
		r.IsOriginTaskIdNull = cols["OriginTaskId"].IsNull();
		r.IsDestinationTaskIdNull = cols["DestinationTaskId"].IsNull();
		r.IsDataElementListNull = cols["DataElementList"].IsNull();
		r.IsInsertTimeNull = cols["InsertTime"].IsNull();
		r.IsModTimeNull = cols["ModTime"].IsNull();
		  
                                        
                       
    
		return true;
	}
};

template<> class DefaultInsValidate<PDb::RouteTabRow>
{
public:
	bool operator()(BoundIOs &cols, PDb::RouteTabRow &r)
	{
		if( r.IsProcessIdNull )
			cols["ProcessId"].SetNull();
		else
			cols["ProcessId"].ClearNull();
		if( r.IsProcessVersionNull )
			cols["ProcessVersion"].SetNull();
		else
			cols["ProcessVersion"].ClearNull();
		if( r.IsOriginTaskIdNull )
			cols["OriginTaskId"].SetNull();
		else
			cols["OriginTaskId"].ClearNull();
		if( r.IsDestinationTaskIdNull )
			cols["DestinationTaskId"].SetNull();
		else
			cols["DestinationTaskId"].ClearNull();
		if( r.IsDataElementListNull )
			cols["DataElementList"].SetNull();
		else
			cols["DataElementList"].ClearNull();
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

template<> class DefaultBCA<PDb::RouteTabRow>
{
public:
	void operator()(BoundIOs &cols, PDb::RouteTabRow &rowbuf)
	{
		cols["ProcessId"] == rowbuf.field_ProcessId;
		cols["ProcessVersion"] == rowbuf.field_ProcessVersion;
		cols["OriginTaskId"] == rowbuf.field_OriginTaskId;
		cols["DestinationTaskId"] == rowbuf.field_DestinationTaskId;
		cols["DataElementList"] == rowbuf.field_DataElementList;
		cols["InsertTime"] == rowbuf.field_InsertTime;
		cols["ModTime"] == rowbuf.field_ModTime;
		  
                                        
                       
    
	}
};


}                     



namespace dtl_pdb {

class DtlRouteTabDeleteBinder : public CustomBinder<PDb::RouteTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		PDb::RouteTabRow& rowbuf = *m_pRow;
		cols["ProcessId"] == rowbuf.field_ProcessId;
		cols["ProcessVersion"] == rowbuf.field_ProcessVersion;
		cols["OriginTaskId"] == rowbuf.field_OriginTaskId;
		cols["DestinationTaskId"] == rowbuf.field_DestinationTaskId;
	}
};

class DtlRouteTab : public PDb::RouteTab {

public:
	DtlRouteTab(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlRouteTab() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		PDb::RouteTab::Edit();
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
	DtlRouteTabDeleteBinder m_deleteBinder;
	DtlDbRecordSet<PDb::RouteTab, PDb::RouteTabRow> m_dtlRecSet;

};


}                         


#endif

