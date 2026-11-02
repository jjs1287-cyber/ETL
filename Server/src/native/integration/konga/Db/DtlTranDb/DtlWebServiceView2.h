/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRTRANDB_WEBSERVICEVIEW2_H
#define KONGA_DTL_CRTRANDB_WEBSERVICEVIEW2_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/TranDb/WebServiceView2.h>

namespace dtl {

template<> class DefaultSelValidate<TranDb::WebServiceView2Row>
{
public:
	bool operator()(BoundIOs &cols, TranDb::WebServiceView2Row &r)
	{
		r.IsFunction_IDNull = cols["Function_ID"].IsNull();
		r.IsWebService_IDNull = cols["WebService_ID"].IsNull();
		r.IsWebService_UrlNull = cols["WebService_Url"].IsNull();
		r.IsSoapActionNull = cols["SoapAction"].IsNull();
		r.IsSoapStyleNull = cols["SoapStyle"].IsNull();
		r.IsSoapBodyUseNull = cols["SoapBodyUse"].IsNull();
		r.IsTargetNamespaceNull = cols["TargetNamespace"].IsNull();
		r.IsOtherNamespacesNull = cols["OtherNamespaces"].IsNull();
		r.IsInputXSDFileNameNull = cols["InputXSDFileName"].IsNull();
		r.IsOutputXSDFileNameNull = cols["OutputXSDFileName"].IsNull();
		r.IsFunctionType_IDNull = cols["FunctionType_ID"].IsNull();
		r.IsHttpUserNameNull = cols["HttpUserName"].IsNull();
		r.IsHttpPasswordNull = cols["HttpPassword"].IsNull();
		r.IsMinCallDelay_msNull = cols["MinCallDelay_ms"].IsNull();
		r.IsMaxSize_bytesNull = cols["MaxSize_bytes"].IsNull();
		r.IsWsdl_IDNull = cols["Wsdl_ID"].IsNull();
		return true;
	}
};

template<> class DefaultInsValidate<TranDb::WebServiceView2Row>
{
public:
	bool operator()(BoundIOs &cols, TranDb::WebServiceView2Row &r)
	{
		if( r.IsWebService_UrlNull )
			cols["WebService_Url"].SetNull();
		else
			cols["WebService_Url"].ClearNull();
		if( r.IsSoapActionNull )
			cols["SoapAction"].SetNull();
		else
			cols["SoapAction"].ClearNull();
		if( r.IsSoapStyleNull )
			cols["SoapStyle"].SetNull();
		else
			cols["SoapStyle"].ClearNull();
		if( r.IsSoapBodyUseNull )
			cols["SoapBodyUse"].SetNull();
		else
			cols["SoapBodyUse"].ClearNull();
		if( r.IsTargetNamespaceNull )
			cols["TargetNamespace"].SetNull();
		else
			cols["TargetNamespace"].ClearNull();
		if( r.IsOtherNamespacesNull )
			cols["OtherNamespaces"].SetNull();
		else
			cols["OtherNamespaces"].ClearNull();
		if( r.IsInputXSDFileNameNull )
			cols["InputXSDFileName"].SetNull();
		else
			cols["InputXSDFileName"].ClearNull();
		if( r.IsOutputXSDFileNameNull )
			cols["OutputXSDFileName"].SetNull();
		else
			cols["OutputXSDFileName"].ClearNull();
		if( r.IsFunctionType_IDNull )
			cols["FunctionType_ID"].SetNull();
		else
			cols["FunctionType_ID"].ClearNull();
		if( r.IsHttpUserNameNull )
			cols["HttpUserName"].SetNull();
		else
			cols["HttpUserName"].ClearNull();
		if( r.IsHttpPasswordNull )
			cols["HttpPassword"].SetNull();
		else
			cols["HttpPassword"].ClearNull();
		if( r.IsMinCallDelay_msNull )
			cols["MinCallDelay_ms"].SetNull();
		else
			cols["MinCallDelay_ms"].ClearNull();
		if( r.IsMaxSize_bytesNull )
			cols["MaxSize_bytes"].SetNull();
		else
			cols["MaxSize_bytes"].ClearNull();
		if( r.IsWsdl_IDNull )
			cols["Wsdl_ID"].SetNull();
		else
			cols["Wsdl_ID"].ClearNull();
		return true;
	}
};

template<> class DefaultBCA<TranDb::WebServiceView2Row>
{
public:
	void operator()(BoundIOs &cols, TranDb::WebServiceView2Row &rowbuf)
	{
		cols["Function_ID"] == rowbuf.field_Function_ID;
		cols["WebService_ID"] == rowbuf.field_WebService_ID;
		cols["WebService_Url"] == rowbuf.field_WebService_Url;
		cols["SoapAction"] == rowbuf.field_SoapAction;
		cols["SoapStyle"] == rowbuf.field_SoapStyle;
		cols["SoapBodyUse"] == rowbuf.field_SoapBodyUse;
		cols["TargetNamespace"] == rowbuf.field_TargetNamespace;
		cols["OtherNamespaces"] == rowbuf.field_OtherNamespaces;
		cols["InputXSDFileName"] == rowbuf.field_InputXSDFileName;
		cols["OutputXSDFileName"] == rowbuf.field_OutputXSDFileName;
		cols["FunctionType_ID"] == rowbuf.field_FunctionType_ID;
		cols["HttpUserName"] == rowbuf.field_HttpUserName;
		cols["HttpPassword"] == rowbuf.field_HttpPassword;
		cols["MinCallDelay_ms"] == rowbuf.field_MinCallDelay_ms;
		cols["MaxSize_bytes"] == rowbuf.field_MaxSize_bytes;
		cols["Wsdl_ID"] == rowbuf.field_Wsdl_ID;
	}
};


}                     



namespace dtl_trandb {


class DtlWebServiceView2InsertBinder : public CustomBinder<TranDb::WebServiceView2Row>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		TranDb::WebServiceView2Row& rowbuf = *m_pRow;
		cols["WebService_Url"] << rowbuf.field_WebService_Url;
		cols["SoapAction"] << rowbuf.field_SoapAction;
		cols["SoapStyle"] << rowbuf.field_SoapStyle;
		cols["SoapBodyUse"] << rowbuf.field_SoapBodyUse;
		cols["TargetNamespace"] << rowbuf.field_TargetNamespace;
		cols["OtherNamespaces"] << rowbuf.field_OtherNamespaces;
		cols["InputXSDFileName"] << rowbuf.field_InputXSDFileName;
		cols["OutputXSDFileName"] << rowbuf.field_OutputXSDFileName;
		cols["FunctionType_ID"] << rowbuf.field_FunctionType_ID;
		cols["HttpUserName"] << rowbuf.field_HttpUserName;
		cols["HttpPassword"] << rowbuf.field_HttpPassword;
		cols["MinCallDelay_ms"] << rowbuf.field_MinCallDelay_ms;
		cols["MaxSize_bytes"] << rowbuf.field_MaxSize_bytes;
		cols["Wsdl_ID"] << rowbuf.field_Wsdl_ID;
	}
};

class DtlWebServiceView2DeleteBinder : public CustomBinder<TranDb::WebServiceView2Row>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		TranDb::WebServiceView2Row& rowbuf = *m_pRow;
		cols["Function_ID"] == rowbuf.field_Function_ID;
		cols["WebService_ID"] == rowbuf.field_WebService_ID;
		cols["WebService_Url"] == rowbuf.field_WebService_Url;
		cols["SoapAction"] == rowbuf.field_SoapAction;
		cols["SoapStyle"] == rowbuf.field_SoapStyle;
		cols["SoapBodyUse"] == rowbuf.field_SoapBodyUse;
		cols["TargetNamespace"] == rowbuf.field_TargetNamespace;
		cols["OtherNamespaces"] == rowbuf.field_OtherNamespaces;
		cols["InputXSDFileName"] == rowbuf.field_InputXSDFileName;
		cols["OutputXSDFileName"] == rowbuf.field_OutputXSDFileName;
		cols["FunctionType_ID"] == rowbuf.field_FunctionType_ID;
		cols["HttpUserName"] == rowbuf.field_HttpUserName;
		cols["HttpPassword"] == rowbuf.field_HttpPassword;
		cols["MinCallDelay_ms"] == rowbuf.field_MinCallDelay_ms;
		cols["MaxSize_bytes"] == rowbuf.field_MaxSize_bytes;
		cols["Wsdl_ID"] == rowbuf.field_Wsdl_ID;
	}
};

class DtlWebServiceView2 : public TranDb::WebServiceView2 {

public:
	DtlWebServiceView2(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlWebServiceView2() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		TranDb::WebServiceView2::Edit();
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
	DtlWebServiceView2InsertBinder m_insertBinder;
	DtlWebServiceView2DeleteBinder m_deleteBinder;
	DtlDbRecordSet<TranDb::WebServiceView2, TranDb::WebServiceView2Row> m_dtlRecSet;

};


}                            


#endif

