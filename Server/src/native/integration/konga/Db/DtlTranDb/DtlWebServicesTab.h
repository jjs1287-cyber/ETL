/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRTRANDB_WEBSERVICESTAB_H
#define KONGA_DTL_CRTRANDB_WEBSERVICESTAB_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/TranDb/WebServicesTab.h>

namespace dtl {

template<> class DefaultSelValidate<TranDb::WebServicesTabRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::WebServicesTabRow &r)
	{
		r.IsWebService_IDNull = cols["WebService_ID"].IsNull();
		r.IsWsdl_IDNull = cols["Wsdl_ID"].IsNull();
		r.IsWebService_NameNull = cols["WebService_Name"].IsNull();
		r.IsWebService_UrlNull = cols["WebService_Url"].IsNull();
		r.IsServiceNull = cols["Service"].IsNull();
		r.IsPortNull = cols["Port"].IsNull();
		r.IsBindingNull = cols["Binding"].IsNull();
		r.IsOperationNull = cols["Operation"].IsNull();
		r.IsSoapVersionNull = cols["SoapVersion"].IsNull();
		r.IsSoapActionNull = cols["SoapAction"].IsNull();
		r.IsSoapStyleNull = cols["SoapStyle"].IsNull();
		r.IsSoapBodyUseNull = cols["SoapBodyUse"].IsNull();
		r.IsInputXSDFileNameNull = cols["InputXSDFileName"].IsNull();
		r.IsOutputXSDFileNameNull = cols["OutputXSDFileName"].IsNull();
		r.IsHttpUserNameNull = cols["HttpUserName"].IsNull();
		r.IsHttpPasswordNull = cols["HttpPassword"].IsNull();
		r.IsCertificateNull = cols["Certificate"].IsNull();
		r.IsTargetNamespaceNull = cols["TargetNamespace"].IsNull();
		r.IsOtherNamespacesNull = cols["OtherNamespaces"].IsNull();
		r.IsInsertTimeNull = cols["InsertTime"].IsNull();
		r.IsModTimeNull = cols["ModTime"].IsNull();
		  
                                        
                       
    
		return true;
	}
};

template<> class DefaultInsValidate<TranDb::WebServicesTabRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::WebServicesTabRow &r)
	{
		if( r.IsWsdl_IDNull )
			cols["Wsdl_ID"].SetNull();
		else
			cols["Wsdl_ID"].ClearNull();
		if( r.IsWebService_NameNull )
			cols["WebService_Name"].SetNull();
		else
			cols["WebService_Name"].ClearNull();
		if( r.IsWebService_UrlNull )
			cols["WebService_Url"].SetNull();
		else
			cols["WebService_Url"].ClearNull();
		if( r.IsServiceNull )
			cols["Service"].SetNull();
		else
			cols["Service"].ClearNull();
		if( r.IsPortNull )
			cols["Port"].SetNull();
		else
			cols["Port"].ClearNull();
		if( r.IsBindingNull )
			cols["Binding"].SetNull();
		else
			cols["Binding"].ClearNull();
		if( r.IsOperationNull )
			cols["Operation"].SetNull();
		else
			cols["Operation"].ClearNull();
		if( r.IsSoapVersionNull )
			cols["SoapVersion"].SetNull();
		else
			cols["SoapVersion"].ClearNull();
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
		if( r.IsInputXSDFileNameNull )
			cols["InputXSDFileName"].SetNull();
		else
			cols["InputXSDFileName"].ClearNull();
		if( r.IsOutputXSDFileNameNull )
			cols["OutputXSDFileName"].SetNull();
		else
			cols["OutputXSDFileName"].ClearNull();
		if( r.IsHttpUserNameNull )
			cols["HttpUserName"].SetNull();
		else
			cols["HttpUserName"].ClearNull();
		if( r.IsHttpPasswordNull )
			cols["HttpPassword"].SetNull();
		else
			cols["HttpPassword"].ClearNull();
		if( r.IsCertificateNull )
			cols["Certificate"].SetNull();
		else
			cols["Certificate"].ClearNull();
		if( r.IsTargetNamespaceNull )
			cols["TargetNamespace"].SetNull();
		else
			cols["TargetNamespace"].ClearNull();
		if( r.IsOtherNamespacesNull )
			cols["OtherNamespaces"].SetNull();
		else
			cols["OtherNamespaces"].ClearNull();
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

template<> class DefaultBCA<TranDb::WebServicesTabRow>
{
public:
	void operator()(BoundIOs &cols, TranDb::WebServicesTabRow &rowbuf)
	{
		cols["WebService_ID"] == rowbuf.field_WebService_ID;
		cols["Wsdl_ID"] == rowbuf.field_Wsdl_ID;
		cols["WebService_Name"] == rowbuf.field_WebService_Name;
		cols["WebService_Url"] == rowbuf.field_WebService_Url;
		cols["Service"] == rowbuf.field_Service;
		cols["Port"] == rowbuf.field_Port;
		cols["Binding"] == rowbuf.field_Binding;
		cols["Operation"] == rowbuf.field_Operation;
		cols["SoapVersion"] == rowbuf.field_SoapVersion;
		cols["SoapAction"] == rowbuf.field_SoapAction;
		cols["SoapStyle"] == rowbuf.field_SoapStyle;
		cols["SoapBodyUse"] == rowbuf.field_SoapBodyUse;
		cols["InputXSDFileName"] == rowbuf.field_InputXSDFileName;
		cols["OutputXSDFileName"] == rowbuf.field_OutputXSDFileName;
		cols["HttpUserName"] == rowbuf.field_HttpUserName;
		cols["HttpPassword"] == rowbuf.field_HttpPassword;
		cols["Certificate"] == rowbuf.field_Certificate;
		cols["TargetNamespace"] == rowbuf.field_TargetNamespace;
		cols["OtherNamespaces"] == rowbuf.field_OtherNamespaces;
		cols["InsertTime"] == rowbuf.field_InsertTime;
		cols["ModTime"] == rowbuf.field_ModTime;
		  
                                        
                       
    
	}
};


}                     



namespace dtl_trandb {


class DtlWebServicesTabInsertBinder : public CustomBinder<TranDb::WebServicesTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		TranDb::WebServicesTabRow& rowbuf = *m_pRow;
		cols["Wsdl_ID"] << rowbuf.field_Wsdl_ID;
		cols["WebService_Name"] << rowbuf.field_WebService_Name;
		cols["WebService_Url"] << rowbuf.field_WebService_Url;
		cols["Service"] << rowbuf.field_Service;
		cols["Port"] << rowbuf.field_Port;
		cols["Binding"] << rowbuf.field_Binding;
		cols["Operation"] << rowbuf.field_Operation;
		cols["SoapVersion"] << rowbuf.field_SoapVersion;
		cols["SoapAction"] << rowbuf.field_SoapAction;
		cols["SoapStyle"] << rowbuf.field_SoapStyle;
		cols["SoapBodyUse"] << rowbuf.field_SoapBodyUse;
		cols["InputXSDFileName"] << rowbuf.field_InputXSDFileName;
		cols["OutputXSDFileName"] << rowbuf.field_OutputXSDFileName;
		cols["HttpUserName"] << rowbuf.field_HttpUserName;
		cols["HttpPassword"] << rowbuf.field_HttpPassword;
		cols["Certificate"] << rowbuf.field_Certificate;
		cols["TargetNamespace"] << rowbuf.field_TargetNamespace;
		cols["OtherNamespaces"] << rowbuf.field_OtherNamespaces;
		cols["InsertTime"] << rowbuf.field_InsertTime;
		cols["ModTime"] << rowbuf.field_ModTime;
	}
};

class DtlWebServicesTabDeleteBinder : public CustomBinder<TranDb::WebServicesTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		TranDb::WebServicesTabRow& rowbuf = *m_pRow;
		cols["WebService_ID"] == rowbuf.field_WebService_ID;
	}
};

class DtlWebServicesTab : public TranDb::WebServicesTab {

public:
	DtlWebServicesTab(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlWebServicesTab() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		TranDb::WebServicesTab::Edit();
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
	DtlWebServicesTabInsertBinder m_insertBinder;
	DtlWebServicesTabDeleteBinder m_deleteBinder;
	DtlDbRecordSet<TranDb::WebServicesTab, TranDb::WebServicesTabRow> m_dtlRecSet;

};


}                            


#endif

