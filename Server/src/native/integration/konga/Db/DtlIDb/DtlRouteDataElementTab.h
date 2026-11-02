/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRIDB_ROUTEDATAELEMENTTAB_H
#define KONGA_DTL_CRIDB_ROUTEDATAELEMENTTAB_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/IDb/RouteDataElementTab.h>

namespace dtl {

template<> class DefaultSelValidate<IDb::RouteDataElementTabRow>
{
public:
	bool operator()(BoundIOs &cols, IDb::RouteDataElementTabRow &r)
	{
		r.IsRouteInstanceIdNull = cols["RouteInstanceId"].IsNull();
		r.IsDataElementIdNull = cols["DataElementId"].IsNull();
		r.IsDataTypeNull = cols["DataType"].IsNull();
		r.IsDataElementNameNull = cols["DataElementName"].IsNull();
		r.IsIntValueNull = cols["IntValue"].IsNull();
		r.IsDoubleValueNull = cols["DoubleValue"].IsNull();
		r.IsStringValueNull = cols["StringValue"].IsNull();
		r.IsInsertTimeNull = cols["InsertTime"].IsNull();
		r.IsModTimeNull = cols["ModTime"].IsNull();
		  
                                        
                       
    
		return true;
	}
};

template<> class DefaultInsValidate<IDb::RouteDataElementTabRow>
{
public:
	bool operator()(BoundIOs &cols, IDb::RouteDataElementTabRow &r)
	{
		if( r.IsRouteInstanceIdNull )
			cols["RouteInstanceId"].SetNull();
		else
			cols["RouteInstanceId"].ClearNull();
		if( r.IsDataElementIdNull )
			cols["DataElementId"].SetNull();
		else
			cols["DataElementId"].ClearNull();
		if( r.IsDataTypeNull )
			cols["DataType"].SetNull();
		else
			cols["DataType"].ClearNull();
		if( r.IsDataElementNameNull )
			cols["DataElementName"].SetNull();
		else
			cols["DataElementName"].ClearNull();
		if( r.IsIntValueNull )
			cols["IntValue"].SetNull();
		else
			cols["IntValue"].ClearNull();
		if( r.IsDoubleValueNull )
			cols["DoubleValue"].SetNull();
		else
			cols["DoubleValue"].ClearNull();
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

template<> class DefaultBCA<IDb::RouteDataElementTabRow>
{
public:
	void operator()(BoundIOs &cols, IDb::RouteDataElementTabRow &rowbuf)
	{
		cols["RouteInstanceId"] == rowbuf.field_RouteInstanceId;
		cols["DataElementId"] == rowbuf.field_DataElementId;
		cols["DataType"] == rowbuf.field_DataType;
		cols["DataElementName"] == rowbuf.field_DataElementName;
		cols["IntValue"] == rowbuf.field_IntValue;
		cols["DoubleValue"] == rowbuf.field_DoubleValue;
		cols["StringValue"] == rowbuf.field_StringValue;
		cols["InsertTime"] == rowbuf.field_InsertTime;
		cols["ModTime"] == rowbuf.field_ModTime;
		  
                                        
                       
    
	}
};


}                     



namespace dtl_idb {

class DtlRouteDataElementTabDeleteBinder : public CustomBinder<IDb::RouteDataElementTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		IDb::RouteDataElementTabRow& rowbuf = *m_pRow;
		cols["RouteInstanceId"] == rowbuf.field_RouteInstanceId;
		cols["DataElementId"] == rowbuf.field_DataElementId;
	}
};

class DtlRouteDataElementTab : public IDb::RouteDataElementTab {

public:
	DtlRouteDataElementTab(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlRouteDataElementTab() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		IDb::RouteDataElementTab::Edit();
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
	DtlRouteDataElementTabDeleteBinder m_deleteBinder;
	DtlDbRecordSet<IDb::RouteDataElementTab, IDb::RouteDataElementTabRow> m_dtlRecSet;

};


}                         


#endif

