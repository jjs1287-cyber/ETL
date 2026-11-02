/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_CRTRANDB_WEBSERVICEVIEW2_H
#define KONGA_DB_CRTRANDB_WEBSERVICEVIEW2_H

#ifdef WIN32
	#include <windows.h>
#endif
#include <string>

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/DbRecordSet.h>
#include <konga/Db/DbUtils/DbNullException.h>

namespace TranDb
{

class WebServiceView2Row
{
public:
	WebServiceView2Row() : 
			field_Function_ID(0),
			IsFunction_IDNull(true),
			IsFunction_IDPrimaryKey(false),
			IsFunction_IDDirty(false),
			field_WebService_ID(0),
			IsWebService_IDNull(true),
			IsWebService_IDPrimaryKey(false),
			IsWebService_IDDirty(false),
			IsWebService_UrlNull(true),
			IsWebService_UrlPrimaryKey(false),
			IsWebService_UrlDirty(false),
			IsSoapActionNull(true),
			IsSoapActionPrimaryKey(false),
			IsSoapActionDirty(false),
			IsSoapStyleNull(true),
			IsSoapStylePrimaryKey(false),
			IsSoapStyleDirty(false),
			IsSoapBodyUseNull(true),
			IsSoapBodyUsePrimaryKey(false),
			IsSoapBodyUseDirty(false),
			IsTargetNamespaceNull(true),
			IsTargetNamespacePrimaryKey(false),
			IsTargetNamespaceDirty(false),
			IsOtherNamespacesNull(true),
			IsOtherNamespacesPrimaryKey(false),
			IsOtherNamespacesDirty(false),
			IsInputXSDFileNameNull(true),
			IsInputXSDFileNamePrimaryKey(false),
			IsInputXSDFileNameDirty(false),
			IsOutputXSDFileNameNull(true),
			IsOutputXSDFileNamePrimaryKey(false),
			IsOutputXSDFileNameDirty(false),
			field_FunctionType_ID(0),
			IsFunctionType_IDNull(true),
			IsFunctionType_IDPrimaryKey(false),
			IsFunctionType_IDDirty(false),
			IsHttpUserNameNull(true),
			IsHttpUserNamePrimaryKey(false),
			IsHttpUserNameDirty(false),
			IsHttpPasswordNull(true),
			IsHttpPasswordPrimaryKey(false),
			IsHttpPasswordDirty(false),
			field_MinCallDelay_ms(0),
			IsMinCallDelay_msNull(true),
			IsMinCallDelay_msPrimaryKey(false),
			IsMinCallDelay_msDirty(false),
			field_MaxSize_bytes(0L),
			IsMaxSize_bytesNull(true),
			IsMaxSize_bytesPrimaryKey(false),
			IsMaxSize_bytesDirty(false),
			field_Wsdl_ID(0),
			IsWsdl_IDNull(true),
			IsWsdl_IDPrimaryKey(false),
			IsWsdl_IDDirty(false)
	{
	}

	void Copy(const WebServiceView2Row& r)
	{
		field_Function_ID = r.field_Function_ID;
		IsFunction_IDNull = r.IsFunction_IDNull;
		IsFunction_IDDirty = r.IsFunction_IDDirty;

		field_WebService_ID = r.field_WebService_ID;
		IsWebService_IDNull = r.IsWebService_IDNull;
		IsWebService_IDDirty = r.IsWebService_IDDirty;

		field_WebService_Url = r.field_WebService_Url;
		IsWebService_UrlNull = r.IsWebService_UrlNull;
		IsWebService_UrlDirty = r.IsWebService_UrlDirty;

		field_SoapAction = r.field_SoapAction;
		IsSoapActionNull = r.IsSoapActionNull;
		IsSoapActionDirty = r.IsSoapActionDirty;

		field_SoapStyle = r.field_SoapStyle;
		IsSoapStyleNull = r.IsSoapStyleNull;
		IsSoapStyleDirty = r.IsSoapStyleDirty;

		field_SoapBodyUse = r.field_SoapBodyUse;
		IsSoapBodyUseNull = r.IsSoapBodyUseNull;
		IsSoapBodyUseDirty = r.IsSoapBodyUseDirty;

		field_TargetNamespace = r.field_TargetNamespace;
		IsTargetNamespaceNull = r.IsTargetNamespaceNull;
		IsTargetNamespaceDirty = r.IsTargetNamespaceDirty;

		field_OtherNamespaces = r.field_OtherNamespaces;
		IsOtherNamespacesNull = r.IsOtherNamespacesNull;
		IsOtherNamespacesDirty = r.IsOtherNamespacesDirty;

		field_InputXSDFileName = r.field_InputXSDFileName;
		IsInputXSDFileNameNull = r.IsInputXSDFileNameNull;
		IsInputXSDFileNameDirty = r.IsInputXSDFileNameDirty;

		field_OutputXSDFileName = r.field_OutputXSDFileName;
		IsOutputXSDFileNameNull = r.IsOutputXSDFileNameNull;
		IsOutputXSDFileNameDirty = r.IsOutputXSDFileNameDirty;

		field_FunctionType_ID = r.field_FunctionType_ID;
		IsFunctionType_IDNull = r.IsFunctionType_IDNull;
		IsFunctionType_IDDirty = r.IsFunctionType_IDDirty;

		field_HttpUserName = r.field_HttpUserName;
		IsHttpUserNameNull = r.IsHttpUserNameNull;
		IsHttpUserNameDirty = r.IsHttpUserNameDirty;

		field_HttpPassword = r.field_HttpPassword;
		IsHttpPasswordNull = r.IsHttpPasswordNull;
		IsHttpPasswordDirty = r.IsHttpPasswordDirty;

		field_MinCallDelay_ms = r.field_MinCallDelay_ms;
		IsMinCallDelay_msNull = r.IsMinCallDelay_msNull;
		IsMinCallDelay_msDirty = r.IsMinCallDelay_msDirty;

		field_MaxSize_bytes = r.field_MaxSize_bytes;
		IsMaxSize_bytesNull = r.IsMaxSize_bytesNull;
		IsMaxSize_bytesDirty = r.IsMaxSize_bytesDirty;

		field_Wsdl_ID = r.field_Wsdl_ID;
		IsWsdl_IDNull = r.IsWsdl_IDNull;
		IsWsdl_IDDirty = r.IsWsdl_IDDirty;

	}

	void CopyPKs(const WebServiceView2Row& r)
	{
		Copy(r);
	}

	void SetDirty(bool b)
	{
		IsFunction_IDDirty = b;
		IsWebService_IDDirty = b;
		IsWebService_UrlDirty = b;
		IsSoapActionDirty = b;
		IsSoapStyleDirty = b;
		IsSoapBodyUseDirty = b;
		IsTargetNamespaceDirty = b;
		IsOtherNamespacesDirty = b;
		IsInputXSDFileNameDirty = b;
		IsOutputXSDFileNameDirty = b;
		IsFunctionType_IDDirty = b;
		IsHttpUserNameDirty = b;
		IsHttpPasswordDirty = b;
		IsMinCallDelay_msDirty = b;
		IsMaxSize_bytesDirty = b;
		IsWsdl_IDDirty = b;
	}

	bool IsDirty() const
	{
		return IsFunction_IDDirty ||
			IsWebService_IDDirty ||
			IsWebService_UrlDirty ||
			IsSoapActionDirty ||
			IsSoapStyleDirty ||
			IsSoapBodyUseDirty ||
			IsTargetNamespaceDirty ||
			IsOtherNamespacesDirty ||
			IsInputXSDFileNameDirty ||
			IsOutputXSDFileNameDirty ||
			IsFunctionType_IDDirty ||
			IsHttpUserNameDirty ||
			IsHttpPasswordDirty ||
			IsMinCallDelay_msDirty ||
			IsMaxSize_bytesDirty ||
			IsWsdl_IDDirty;
	}

	int field_Function_ID;
	bool IsFunction_IDNull;
	bool IsFunction_IDDirty;

	int field_WebService_ID;
	bool IsWebService_IDNull;
	bool IsWebService_IDDirty;

	std::string field_WebService_Url;
	bool IsWebService_UrlNull;
	bool IsWebService_UrlDirty;

	std::string field_SoapAction;
	bool IsSoapActionNull;
	bool IsSoapActionDirty;

	std::string field_SoapStyle;
	bool IsSoapStyleNull;
	bool IsSoapStyleDirty;

	std::string field_SoapBodyUse;
	bool IsSoapBodyUseNull;
	bool IsSoapBodyUseDirty;

	std::string field_TargetNamespace;
	bool IsTargetNamespaceNull;
	bool IsTargetNamespaceDirty;

	std::string field_OtherNamespaces;
	bool IsOtherNamespacesNull;
	bool IsOtherNamespacesDirty;

	std::string field_InputXSDFileName;
	bool IsInputXSDFileNameNull;
	bool IsInputXSDFileNameDirty;

	std::string field_OutputXSDFileName;
	bool IsOutputXSDFileNameNull;
	bool IsOutputXSDFileNameDirty;

	int field_FunctionType_ID;
	bool IsFunctionType_IDNull;
	bool IsFunctionType_IDDirty;

	std::string field_HttpUserName;
	bool IsHttpUserNameNull;
	bool IsHttpUserNameDirty;

	std::string field_HttpPassword;
	bool IsHttpPasswordNull;
	bool IsHttpPasswordDirty;

	int field_MinCallDelay_ms;
	bool IsMinCallDelay_msNull;
	bool IsMinCallDelay_msDirty;

	long field_MaxSize_bytes;
	bool IsMaxSize_bytesNull;
	bool IsMaxSize_bytesDirty;

	int field_Wsdl_ID;
	bool IsWsdl_IDNull;
	bool IsWsdl_IDDirty;


private:
	bool IsFunction_IDPrimaryKey;
	bool IsWebService_IDPrimaryKey;
	bool IsWebService_UrlPrimaryKey;
	bool IsSoapActionPrimaryKey;
	bool IsSoapStylePrimaryKey;
	bool IsSoapBodyUsePrimaryKey;
	bool IsTargetNamespacePrimaryKey;
	bool IsOtherNamespacesPrimaryKey;
	bool IsInputXSDFileNamePrimaryKey;
	bool IsOutputXSDFileNamePrimaryKey;
	bool IsFunctionType_IDPrimaryKey;
	bool IsHttpUserNamePrimaryKey;
	bool IsHttpPasswordPrimaryKey;
	bool IsMinCallDelay_msPrimaryKey;
	bool IsMaxSize_bytesPrimaryKey;
	bool IsWsdl_IDPrimaryKey;

};


class WebServiceView2 : public konga::DbRecordSet {
public:
	WebServiceView2() {}

	static WebServiceView2* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "WebServiceView2";
	}

	void DoFieldExchange(const WebServiceView2Row& row) {
		m_row.Copy(row);
	}

	void Edit();

	std::string GetUpdateFilter() {
		return m_stUpdateFilter;
	}

	int GetFunction_ID() {
		if( m_row.IsFunction_IDNull )
			throw konga::DbNullException("Function_ID");
		return m_row.field_Function_ID;
	}
	void SetFunction_ID(int i) {
		m_row.field_Function_ID = i;
		m_row.IsFunction_IDNull = false;
		m_row.IsFunction_IDDirty = true;
	}
	bool IsFunction_IDNull() const {
		return m_row.IsFunction_IDNull;
	}
	void SetFunction_IDToNull() {
		m_row.IsFunction_IDNull = true;
		m_row.IsFunction_IDDirty = true;
	}

	int GetWebService_ID() {
		if( m_row.IsWebService_IDNull )
			throw konga::DbNullException("WebService_ID");
		return m_row.field_WebService_ID;
	}
	void SetWebService_ID(int i) {
		m_row.field_WebService_ID = i;
		m_row.IsWebService_IDNull = false;
		m_row.IsWebService_IDDirty = true;
	}
	bool IsWebService_IDNull() const {
		return m_row.IsWebService_IDNull;
	}
	void SetWebService_IDToNull() {
		m_row.IsWebService_IDNull = true;
		m_row.IsWebService_IDDirty = true;
	}

	std::string GetWebService_Url() {
		if( m_row.IsWebService_UrlNull )
			throw konga::DbNullException("WebService_Url");
		return m_row.field_WebService_Url;
	}
	void SetWebService_Url(const std::string& st) {
		m_row.field_WebService_Url = st;
		m_row.IsWebService_UrlNull = false;
		m_row.IsWebService_UrlDirty = true;
	}
	bool IsWebService_UrlNull() const {
		return m_row.IsWebService_UrlNull;
	}
	void SetWebService_UrlToNull() {
		m_row.field_WebService_Url = "";
		m_row.IsWebService_UrlNull = true;
		m_row.IsWebService_UrlDirty = true;
	}

	std::string GetSoapAction() {
		if( m_row.IsSoapActionNull )
			throw konga::DbNullException("SoapAction");
		return m_row.field_SoapAction;
	}
	void SetSoapAction(const std::string& st) {
		m_row.field_SoapAction = st;
		m_row.IsSoapActionNull = false;
		m_row.IsSoapActionDirty = true;
	}
	bool IsSoapActionNull() const {
		return m_row.IsSoapActionNull;
	}
	void SetSoapActionToNull() {
		m_row.field_SoapAction = "";
		m_row.IsSoapActionNull = true;
		m_row.IsSoapActionDirty = true;
	}

	std::string GetSoapStyle() {
		if( m_row.IsSoapStyleNull )
			throw konga::DbNullException("SoapStyle");
		return m_row.field_SoapStyle;
	}
	void SetSoapStyle(const std::string& st) {
		m_row.field_SoapStyle = st;
		m_row.IsSoapStyleNull = false;
		m_row.IsSoapStyleDirty = true;
	}
	bool IsSoapStyleNull() const {
		return m_row.IsSoapStyleNull;
	}
	void SetSoapStyleToNull() {
		m_row.field_SoapStyle = "";
		m_row.IsSoapStyleNull = true;
		m_row.IsSoapStyleDirty = true;
	}

	std::string GetSoapBodyUse() {
		if( m_row.IsSoapBodyUseNull )
			throw konga::DbNullException("SoapBodyUse");
		return m_row.field_SoapBodyUse;
	}
	void SetSoapBodyUse(const std::string& st) {
		m_row.field_SoapBodyUse = st;
		m_row.IsSoapBodyUseNull = false;
		m_row.IsSoapBodyUseDirty = true;
	}
	bool IsSoapBodyUseNull() const {
		return m_row.IsSoapBodyUseNull;
	}
	void SetSoapBodyUseToNull() {
		m_row.field_SoapBodyUse = "";
		m_row.IsSoapBodyUseNull = true;
		m_row.IsSoapBodyUseDirty = true;
	}

	std::string GetTargetNamespace() {
		if( m_row.IsTargetNamespaceNull )
			throw konga::DbNullException("TargetNamespace");
		return m_row.field_TargetNamespace;
	}
	void SetTargetNamespace(const std::string& st) {
		m_row.field_TargetNamespace = st;
		m_row.IsTargetNamespaceNull = false;
		m_row.IsTargetNamespaceDirty = true;
	}
	bool IsTargetNamespaceNull() const {
		return m_row.IsTargetNamespaceNull;
	}
	void SetTargetNamespaceToNull() {
		m_row.field_TargetNamespace = "";
		m_row.IsTargetNamespaceNull = true;
		m_row.IsTargetNamespaceDirty = true;
	}

	std::string GetOtherNamespaces() {
		if( m_row.IsOtherNamespacesNull )
			throw konga::DbNullException("OtherNamespaces");
		return m_row.field_OtherNamespaces;
	}
	void SetOtherNamespaces(const std::string& st) {
		m_row.field_OtherNamespaces = st;
		m_row.IsOtherNamespacesNull = false;
		m_row.IsOtherNamespacesDirty = true;
	}
	bool IsOtherNamespacesNull() const {
		return m_row.IsOtherNamespacesNull;
	}
	void SetOtherNamespacesToNull() {
		m_row.field_OtherNamespaces = "";
		m_row.IsOtherNamespacesNull = true;
		m_row.IsOtherNamespacesDirty = true;
	}

	std::string GetInputXSDFileName() {
		if( m_row.IsInputXSDFileNameNull )
			throw konga::DbNullException("InputXSDFileName");
		return m_row.field_InputXSDFileName;
	}
	void SetInputXSDFileName(const std::string& st) {
		m_row.field_InputXSDFileName = st;
		m_row.IsInputXSDFileNameNull = false;
		m_row.IsInputXSDFileNameDirty = true;
	}
	bool IsInputXSDFileNameNull() const {
		return m_row.IsInputXSDFileNameNull;
	}
	void SetInputXSDFileNameToNull() {
		m_row.field_InputXSDFileName = "";
		m_row.IsInputXSDFileNameNull = true;
		m_row.IsInputXSDFileNameDirty = true;
	}

	std::string GetOutputXSDFileName() {
		if( m_row.IsOutputXSDFileNameNull )
			throw konga::DbNullException("OutputXSDFileName");
		return m_row.field_OutputXSDFileName;
	}
	void SetOutputXSDFileName(const std::string& st) {
		m_row.field_OutputXSDFileName = st;
		m_row.IsOutputXSDFileNameNull = false;
		m_row.IsOutputXSDFileNameDirty = true;
	}
	bool IsOutputXSDFileNameNull() const {
		return m_row.IsOutputXSDFileNameNull;
	}
	void SetOutputXSDFileNameToNull() {
		m_row.field_OutputXSDFileName = "";
		m_row.IsOutputXSDFileNameNull = true;
		m_row.IsOutputXSDFileNameDirty = true;
	}

	int GetFunctionType_ID() {
		if( m_row.IsFunctionType_IDNull )
			throw konga::DbNullException("FunctionType_ID");
		return m_row.field_FunctionType_ID;
	}
	void SetFunctionType_ID(int i) {
		m_row.field_FunctionType_ID = i;
		m_row.IsFunctionType_IDNull = false;
		m_row.IsFunctionType_IDDirty = true;
	}
	bool IsFunctionType_IDNull() const {
		return m_row.IsFunctionType_IDNull;
	}
	void SetFunctionType_IDToNull() {
		m_row.IsFunctionType_IDNull = true;
		m_row.IsFunctionType_IDDirty = true;
	}

	std::string GetHttpUserName() {
		if( m_row.IsHttpUserNameNull )
			throw konga::DbNullException("HttpUserName");
		return m_row.field_HttpUserName;
	}
	void SetHttpUserName(const std::string& st) {
		m_row.field_HttpUserName = st;
		m_row.IsHttpUserNameNull = false;
		m_row.IsHttpUserNameDirty = true;
	}
	bool IsHttpUserNameNull() const {
		return m_row.IsHttpUserNameNull;
	}
	void SetHttpUserNameToNull() {
		m_row.field_HttpUserName = "";
		m_row.IsHttpUserNameNull = true;
		m_row.IsHttpUserNameDirty = true;
	}

	std::string GetHttpPassword() {
		if( m_row.IsHttpPasswordNull )
			throw konga::DbNullException("HttpPassword");
		return m_row.field_HttpPassword;
	}
	void SetHttpPassword(const std::string& st) {
		m_row.field_HttpPassword = st;
		m_row.IsHttpPasswordNull = false;
		m_row.IsHttpPasswordDirty = true;
	}
	bool IsHttpPasswordNull() const {
		return m_row.IsHttpPasswordNull;
	}
	void SetHttpPasswordToNull() {
		m_row.field_HttpPassword = "";
		m_row.IsHttpPasswordNull = true;
		m_row.IsHttpPasswordDirty = true;
	}

	int GetMinCallDelay_ms() {
		if( m_row.IsMinCallDelay_msNull )
			throw konga::DbNullException("MinCallDelay_ms");
		return m_row.field_MinCallDelay_ms;
	}
	void SetMinCallDelay_ms(int i) {
		m_row.field_MinCallDelay_ms = i;
		m_row.IsMinCallDelay_msNull = false;
		m_row.IsMinCallDelay_msDirty = true;
	}
	bool IsMinCallDelay_msNull() const {
		return m_row.IsMinCallDelay_msNull;
	}
	void SetMinCallDelay_msToNull() {
		m_row.IsMinCallDelay_msNull = true;
		m_row.IsMinCallDelay_msDirty = true;
	}

	long GetMaxSize_bytes() {
		if( m_row.IsMaxSize_bytesNull )
			throw konga::DbNullException("MaxSize_bytes");
		return m_row.field_MaxSize_bytes;
	}
	void SetMaxSize_bytes(long l) {
		m_row.field_MaxSize_bytes = l;
		m_row.IsMaxSize_bytesNull = false;
		m_row.IsMaxSize_bytesDirty = true;
	}
	bool IsMaxSize_bytesNull() const {
		return m_row.IsMaxSize_bytesNull;
	}
	void SetMaxSize_bytesToNull() {
		m_row.IsMaxSize_bytesNull = true;
		m_row.IsMaxSize_bytesDirty = true;
	}

	int GetWsdl_ID() {
		if( m_row.IsWsdl_IDNull )
			throw konga::DbNullException("Wsdl_ID");
		return m_row.field_Wsdl_ID;
	}
	void SetWsdl_ID(int i) {
		m_row.field_Wsdl_ID = i;
		m_row.IsWsdl_IDNull = false;
		m_row.IsWsdl_IDDirty = true;
	}
	bool IsWsdl_IDNull() const {
		return m_row.IsWsdl_IDNull;
	}
	void SetWsdl_IDToNull() {
		m_row.IsWsdl_IDNull = true;
		m_row.IsWsdl_IDDirty = true;
	}

protected:
	WebServiceView2Row m_row;

private:
	bool IsFunction_IDDirty() const {
		return m_row.IsFunction_IDDirty;
	};
	bool IsWebService_IDDirty() const {
		return m_row.IsWebService_IDDirty;
	};
	bool IsWebService_UrlDirty() const {
		return m_row.IsWebService_UrlDirty;
	};
	bool IsSoapActionDirty() const {
		return m_row.IsSoapActionDirty;
	};
	bool IsSoapStyleDirty() const {
		return m_row.IsSoapStyleDirty;
	};
	bool IsSoapBodyUseDirty() const {
		return m_row.IsSoapBodyUseDirty;
	};
	bool IsTargetNamespaceDirty() const {
		return m_row.IsTargetNamespaceDirty;
	};
	bool IsOtherNamespacesDirty() const {
		return m_row.IsOtherNamespacesDirty;
	};
	bool IsInputXSDFileNameDirty() const {
		return m_row.IsInputXSDFileNameDirty;
	};
	bool IsOutputXSDFileNameDirty() const {
		return m_row.IsOutputXSDFileNameDirty;
	};
	bool IsFunctionType_IDDirty() const {
		return m_row.IsFunctionType_IDDirty;
	};
	bool IsHttpUserNameDirty() const {
		return m_row.IsHttpUserNameDirty;
	};
	bool IsHttpPasswordDirty() const {
		return m_row.IsHttpPasswordDirty;
	};
	bool IsMinCallDelay_msDirty() const {
		return m_row.IsMinCallDelay_msDirty;
	};
	bool IsMaxSize_bytesDirty() const {
		return m_row.IsMaxSize_bytesDirty;
	};
	bool IsWsdl_IDDirty() const {
		return m_row.IsWsdl_IDDirty;
	};


	std::string m_stUpdateFilter;

};

}                         


#endif
