/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRIDB_ROUTETAB_H
#define KONGA_DTL_CRIDB_ROUTETAB_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/IDb/RouteTab.h>

namespace dtl {

template<> class DefaultSelValidate<IDb::RouteTabRow>
{
public:
	bool operator()(BoundIOs &cols, IDb::RouteTabRow &r)
	{
		r.IsProcessIdNull = cols["ProcessId"].IsNull();
		r.IsProcessInstanceIdNull = cols["ProcessInstanceId"].IsNull();
		r.IsDestinationTaskIdNull = cols["DestinationTaskId"].IsNull();
		r.IsDestinationTaskInstanceIdNull = cols["DestinationTaskInstanceId"].IsNull();
		r.IsOriginProcessIdNull = cols["OriginProcessId"].IsNull();
		r.IsOriginProcessInstanceIdNull = cols["OriginProcessInstanceId"].IsNull();
		r.IsOriginTaskIdNull = cols["OriginTaskId"].IsNull();
		r.IsOriginTaskInstanceIdNull = cols["OriginTaskInstanceId"].IsNull();
		r.IsRouteUserNull = cols["RouteUser"].IsNull();
		r.IsSentTimeNull = cols["SentTime"].IsNull();
		r.IsStatusNull = cols["Status"].IsNull();
		r.IsRouteInstanceIdNull = cols["RouteInstanceId"].IsNull();
		r.IsInsertTimeNull = cols["InsertTime"].IsNull();
		r.IsModTimeNull = cols["ModTime"].IsNull();
		  
                                        
                       
    
		return true;
	}
};

template<> class DefaultInsValidate<IDb::RouteTabRow>
{
public:
	bool operator()(BoundIOs &cols, IDb::RouteTabRow &r)
	{
		if( r.IsProcessIdNull )
			cols["ProcessId"].SetNull();
		else
			cols["ProcessId"].ClearNull();
		if( r.IsProcessInstanceIdNull )
			cols["ProcessInstanceId"].SetNull();
		else
			cols["ProcessInstanceId"].ClearNull();
		if( r.IsDestinationTaskIdNull )
			cols["DestinationTaskId"].SetNull();
		else
			cols["DestinationTaskId"].ClearNull();
		if( r.IsDestinationTaskInstanceIdNull )
			cols["DestinationTaskInstanceId"].SetNull();
		else
			cols["DestinationTaskInstanceId"].ClearNull();
		if( r.IsOriginProcessIdNull )
			cols["OriginProcessId"].SetNull();
		else
			cols["OriginProcessId"].ClearNull();
		if( r.IsOriginProcessInstanceIdNull )
			cols["OriginProcessInstanceId"].SetNull();
		else
			cols["OriginProcessInstanceId"].ClearNull();
		if( r.IsOriginTaskIdNull )
			cols["OriginTaskId"].SetNull();
		else
			cols["OriginTaskId"].ClearNull();
		if( r.IsOriginTaskInstanceIdNull )
			cols["OriginTaskInstanceId"].SetNull();
		else
			cols["OriginTaskInstanceId"].ClearNull();
		if( r.IsRouteUserNull )
			cols["RouteUser"].SetNull();
		else
			cols["RouteUser"].ClearNull();
		if( r.IsSentTimeNull )
			cols["SentTime"].SetNull();
		else
			cols["SentTime"].ClearNull();
		if( r.IsStatusNull )
			cols["Status"].SetNull();
		else
			cols["Status"].ClearNull();
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

template<> class DefaultBCA<IDb::RouteTabRow>
{
public:
	void operator()(BoundIOs &cols, IDb::RouteTabRow &rowbuf)
	{
		cols["ProcessId"] == rowbuf.field_ProcessId;
		cols["ProcessInstanceId"] == rowbuf.field_ProcessInstanceId;
		cols["DestinationTaskId"] == rowbuf.field_DestinationTaskId;
		cols["DestinationTaskInstanceId"] == rowbuf.field_DestinationTaskInstanceId;
		cols["OriginProcessId"] == rowbuf.field_OriginProcessId;
		cols["OriginProcessInstanceId"] == rowbuf.field_OriginProcessInstanceId;
		cols["OriginTaskId"] == rowbuf.field_OriginTaskId;
		cols["OriginTaskInstanceId"] == rowbuf.field_OriginTaskInstanceId;
		cols["RouteUser"] == rowbuf.field_RouteUser;
		cols["SentTime"] == rowbuf.field_SentTime;
		cols["Status"] == rowbuf.field_Status;
		cols["RouteInstanceId"] == rowbuf.field_RouteInstanceId;
		cols["InsertTime"] == rowbuf.field_InsertTime;
		cols["ModTime"] == rowbuf.field_ModTime;
		  
                                        
                       
    
	}
};


}                     



namespace dtl_idb {


class DtlRouteTabInsertBinder : public CustomBinder<IDb::RouteTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		IDb::RouteTabRow& rowbuf = *m_pRow;
		cols["ProcessId"] << rowbuf.field_ProcessId;
		cols["ProcessInstanceId"] << rowbuf.field_ProcessInstanceId;
		cols["DestinationTaskId"] << rowbuf.field_DestinationTaskId;
		cols["DestinationTaskInstanceId"] << rowbuf.field_DestinationTaskInstanceId;
		cols["OriginProcessId"] << rowbuf.field_OriginProcessId;
		cols["OriginProcessInstanceId"] << rowbuf.field_OriginProcessInstanceId;
		cols["OriginTaskId"] << rowbuf.field_OriginTaskId;
		cols["OriginTaskInstanceId"] << rowbuf.field_OriginTaskInstanceId;
		cols["RouteUser"] << rowbuf.field_RouteUser;
		cols["SentTime"] << rowbuf.field_SentTime;
		cols["Status"] << rowbuf.field_Status;
		cols["InsertTime"] << rowbuf.field_InsertTime;
		cols["ModTime"] << rowbuf.field_ModTime;
	}
};

class DtlRouteTabDeleteBinder : public CustomBinder<IDb::RouteTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		IDb::RouteTabRow& rowbuf = *m_pRow;
		cols["ProcessId"] == rowbuf.field_ProcessId;
		cols["ProcessInstanceId"] == rowbuf.field_ProcessInstanceId;
		cols["DestinationTaskId"] == rowbuf.field_DestinationTaskId;
		cols["DestinationTaskInstanceId"] == rowbuf.field_DestinationTaskInstanceId;
		cols["OriginProcessId"] == rowbuf.field_OriginProcessId;
		cols["OriginProcessInstanceId"] == rowbuf.field_OriginProcessInstanceId;
		cols["OriginTaskId"] == rowbuf.field_OriginTaskId;
		cols["OriginTaskInstanceId"] == rowbuf.field_OriginTaskInstanceId;
	}
};

class DtlRouteTab : public IDb::RouteTab {

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
		IDb::RouteTab::Edit();
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
	DtlRouteTabInsertBinder m_insertBinder;
	DtlRouteTabDeleteBinder m_deleteBinder;
	DtlDbRecordSet<IDb::RouteTab, IDb::RouteTabRow> m_dtlRecSet;

};


}                         


#endif

