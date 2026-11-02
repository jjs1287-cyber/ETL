/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRIDB_ROUTEDATAELEMENTTEXTTAB_H
#define KONGA_DTL_CRIDB_ROUTEDATAELEMENTTEXTTAB_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/IDb/RouteDataElementTextTab.h>

namespace dtl {

template<> class DefaultSelValidate<IDb::RouteDataElementTextTabRow>
{
public:
	bool operator()(BoundIOs &cols, IDb::RouteDataElementTextTabRow &r)
	{
		r.IsRouteInstanceIdNull = cols["RouteInstanceId"].IsNull();
		r.IsDataElementIdNull = cols["DataElementId"].IsNull();
		r.IsStringValueNull = cols["StringValue"].IsNull();
		r.IsInsertTimeNull = cols["InsertTime"].IsNull();
		r.IsModTimeNull = cols["ModTime"].IsNull();
		  
                                        
           
    
		return true;
	}
};

template<> class DefaultInsValidate<IDb::RouteDataElementTextTabRow>
{
public:
	bool operator()(BoundIOs &cols, IDb::RouteDataElementTextTabRow &r)
	{
		if( r.IsRouteInstanceIdNull )
			cols["RouteInstanceId"].SetNull();
		else
			cols["RouteInstanceId"].ClearNull();
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

template<> class DefaultBCA<IDb::RouteDataElementTextTabRow>
{
public:
	void operator()(BoundIOs &cols, IDb::RouteDataElementTextTabRow &rowbuf)
	{
		cols["RouteInstanceId"] == rowbuf.field_RouteInstanceId;
		cols["DataElementId"] == rowbuf.field_DataElementId;
		cols["StringValue"] == rowbuf.field_StringValue;
		cols["InsertTime"] == rowbuf.field_InsertTime;
		cols["ModTime"] == rowbuf.field_ModTime;
		  
                                        
           
    
	}
};


}                     



namespace dtl_idb {

class DtlRouteDataElementTextTabDeleteBinder : public CustomBinder<IDb::RouteDataElementTextTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		IDb::RouteDataElementTextTabRow& rowbuf = *m_pRow;
		cols["RouteInstanceId"] == rowbuf.field_RouteInstanceId;
		cols["DataElementId"] == rowbuf.field_DataElementId;
	}
};

class DtlRouteDataElementTextTab : public IDb::RouteDataElementTextTab {

public:
	DtlRouteDataElementTextTab(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlRouteDataElementTextTab() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		IDb::RouteDataElementTextTab::Edit();
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
	DtlRouteDataElementTextTabDeleteBinder m_deleteBinder;
	DtlDbRecordSet<IDb::RouteDataElementTextTab, IDb::RouteDataElementTextTabRow> m_dtlRecSet;

};


}                         


#endif

