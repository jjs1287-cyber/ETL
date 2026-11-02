/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_CRTRANDB_WEBSERVICESTAB_H
#define KONGA_DB_CRTRANDB_WEBSERVICESTAB_H

#ifdef WIN32
	#include <windows.h>
#endif
#include <string>
#include <sqltypes.h>

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/DbRecordSet.h>
#include <konga/Db/DbUtils/DbNullException.h>

namespace TranDb
{

class WebServicesTabRow
{
public:
	WebServicesTabRow() : 
			field_WebService_ID(0),
			IsWebService_IDNull(true),
			IsWebService_IDPrimaryKey(true),
			IsWebService_IDDirty(false),
			field_Wsdl_ID(0),
			IsWsdl_IDNull(true),
			IsWsdl_IDPrimaryKey(false),
			IsWsdl_IDDirty(false),
			IsWebService_NameNull(true),
			IsWebService_NamePrimaryKey(false),
			IsWebService_NameDirty(false),
			IsWebService_UrlNull(true),
			IsWebService_UrlPrimaryKey(false),
			IsWebService_UrlDirty(false),
			IsServiceNull(true),
			IsServicePrimaryKey(false),
			IsServiceDirty(false),
			IsPortNull(true),
			IsPortPrimaryKey(false),
			IsPortDirty(false),
			IsBindingNull(true),
			IsBindingPrimaryKey(false),
			IsBindingDirty(false),
			IsOperationNull(true),
			IsOperationPrimaryKey(false),
			IsOperationDirty(false),
			field_SoapVersion(0),
			IsSoapVersionNull(true),
			IsSoapVersionPrimaryKey(false),
			IsSoapVersionDirty(false),
			IsSoapActionNull(true),
			IsSoapActionPrimaryKey(false),
			IsSoapActionDirty(false),
			IsSoapStyleNull(true),
			IsSoapStylePrimaryKey(false),
			IsSoapStyleDirty(false),
			IsSoapBodyUseNull(true),
			IsSoapBodyUsePrimaryKey(false),
			IsSoapBodyUseDirty(false),
			IsInputXSDFileNameNull(true),
			IsInputXSDFileNamePrimaryKey(false),
			IsInputXSDFileNameDirty(false),
			IsOutputXSDFileNameNull(true),
			IsOutputXSDFileNamePrimaryKey(false),
			IsOutputXSDFileNameDirty(false),
			IsHttpUserNameNull(true),
			IsHttpUserNamePrimaryKey(false),
			IsHttpUserNameDirty(false),
			IsHttpPasswordNull(true),
			IsHttpPasswordPrimaryKey(false),
			IsHttpPasswordDirty(false),
			IsCertificateNull(true),
			IsCertificatePrimaryKey(false),
			IsCertificateDirty(false),
			IsTargetNamespaceNull(true),
			IsTargetNamespacePrimaryKey(false),
			IsTargetNamespaceDirty(false),
			IsOtherNamespacesNull(true),
			IsOtherNamespacesPrimaryKey(false),
			IsOtherNamespacesDirty(false),
			IsInsertTimeNull(true),
			IsInsertTimePrimaryKey(false),
			IsInsertTimeDirty(false),
			IsModTimeNull(true),
			IsModTimePrimaryKey(false),
			IsModTimeDirty(false)
	{
		field_InsertTime.year = 0;
		field_InsertTime.month = 0;
		field_InsertTime.day = 0;
		field_InsertTime.hour = 0;
		field_InsertTime.minute = 0;
		field_InsertTime.second = 0;
		field_InsertTime.fraction = 0;
		field_ModTime.year = 0;
		field_ModTime.month = 0;
		field_ModTime.day = 0;
		field_ModTime.hour = 0;
		field_ModTime.minute = 0;
		field_ModTime.second = 0;
		field_ModTime.fraction = 0;
	}

	void Copy(const WebServicesTabRow& r)
	{
		field_WebService_ID = r.field_WebService_ID;
		IsWebService_IDNull = r.IsWebService_IDNull;
		IsWebService_IDDirty = r.IsWebService_IDDirty;

		field_Wsdl_ID = r.field_Wsdl_ID;
		IsWsdl_IDNull = r.IsWsdl_IDNull;
		IsWsdl_IDDirty = r.IsWsdl_IDDirty;

		field_WebService_Name = r.field_WebService_Name;
		IsWebService_NameNull = r.IsWebService_NameNull;
		IsWebService_NameDirty = r.IsWebService_NameDirty;

		field_WebService_Url = r.field_WebService_Url;
		IsWebService_UrlNull = r.IsWebService_UrlNull;
		IsWebService_UrlDirty = r.IsWebService_UrlDirty;

		field_Service = r.field_Service;
		IsServiceNull = r.IsServiceNull;
		IsServiceDirty = r.IsServiceDirty;

		field_Port = r.field_Port;
		IsPortNull = r.IsPortNull;
		IsPortDirty = r.IsPortDirty;

		field_Binding = r.field_Binding;
		IsBindingNull = r.IsBindingNull;
		IsBindingDirty = r.IsBindingDirty;

		field_Operation = r.field_Operation;
		IsOperationNull = r.IsOperationNull;
		IsOperationDirty = r.IsOperationDirty;

		field_SoapVersion = r.field_SoapVersion;
		IsSoapVersionNull = r.IsSoapVersionNull;
		IsSoapVersionDirty = r.IsSoapVersionDirty;

		field_SoapAction = r.field_SoapAction;
		IsSoapActionNull = r.IsSoapActionNull;
		IsSoapActionDirty = r.IsSoapActionDirty;

		field_SoapStyle = r.field_SoapStyle;
		IsSoapStyleNull = r.IsSoapStyleNull;
		IsSoapStyleDirty = r.IsSoapStyleDirty;

		field_SoapBodyUse = r.field_SoapBodyUse;
		IsSoapBodyUseNull = r.IsSoapBodyUseNull;
		IsSoapBodyUseDirty = r.IsSoapBodyUseDirty;

		field_InputXSDFileName = r.field_InputXSDFileName;
		IsInputXSDFileNameNull = r.IsInputXSDFileNameNull;
		IsInputXSDFileNameDirty = r.IsInputXSDFileNameDirty;

		field_OutputXSDFileName = r.field_OutputXSDFileName;
		IsOutputXSDFileNameNull = r.IsOutputXSDFileNameNull;
		IsOutputXSDFileNameDirty = r.IsOutputXSDFileNameDirty;

		field_HttpUserName = r.field_HttpUserName;
		IsHttpUserNameNull = r.IsHttpUserNameNull;
		IsHttpUserNameDirty = r.IsHttpUserNameDirty;

		field_HttpPassword = r.field_HttpPassword;
		IsHttpPasswordNull = r.IsHttpPasswordNull;
		IsHttpPasswordDirty = r.IsHttpPasswordDirty;

		field_Certificate = r.field_Certificate;
		IsCertificateNull = r.IsCertificateNull;
		IsCertificateDirty = r.IsCertificateDirty;

		field_TargetNamespace = r.field_TargetNamespace;
		IsTargetNamespaceNull = r.IsTargetNamespaceNull;
		IsTargetNamespaceDirty = r.IsTargetNamespaceDirty;

		field_OtherNamespaces = r.field_OtherNamespaces;
		IsOtherNamespacesNull = r.IsOtherNamespacesNull;
		IsOtherNamespacesDirty = r.IsOtherNamespacesDirty;

		field_InsertTime = r.field_InsertTime;
		IsInsertTimeNull = r.IsInsertTimeNull;
		IsInsertTimeDirty = r.IsInsertTimeDirty;

		field_ModTime = r.field_ModTime;
		IsModTimeNull = r.IsModTimeNull;
		IsModTimeDirty = r.IsModTimeDirty;

	}

	void CopyPKs(const WebServicesTabRow& r)
	{
		field_WebService_ID = r.field_WebService_ID;
		IsWebService_IDNull = r.IsWebService_IDNull;
		IsWebService_IDDirty = r.IsWebService_IDDirty;

	}

	void SetDirty(bool b)
	{
		IsWebService_IDDirty = b;
		IsWsdl_IDDirty = b;
		IsWebService_NameDirty = b;
		IsWebService_UrlDirty = b;
		IsServiceDirty = b;
		IsPortDirty = b;
		IsBindingDirty = b;
		IsOperationDirty = b;
		IsSoapVersionDirty = b;
		IsSoapActionDirty = b;
		IsSoapStyleDirty = b;
		IsSoapBodyUseDirty = b;
		IsInputXSDFileNameDirty = b;
		IsOutputXSDFileNameDirty = b;
		IsHttpUserNameDirty = b;
		IsHttpPasswordDirty = b;
		IsCertificateDirty = b;
		IsTargetNamespaceDirty = b;
		IsOtherNamespacesDirty = b;
		IsInsertTimeDirty = b;
		IsModTimeDirty = b;
	}

	bool IsDirty() const
	{
		return IsWebService_IDDirty ||
			IsWsdl_IDDirty ||
			IsWebService_NameDirty ||
			IsWebService_UrlDirty ||
			IsServiceDirty ||
			IsPortDirty ||
			IsBindingDirty ||
			IsOperationDirty ||
			IsSoapVersionDirty ||
			IsSoapActionDirty ||
			IsSoapStyleDirty ||
			IsSoapBodyUseDirty ||
			IsInputXSDFileNameDirty ||
			IsOutputXSDFileNameDirty ||
			IsHttpUserNameDirty ||
			IsHttpPasswordDirty ||
			IsCertificateDirty ||
			IsTargetNamespaceDirty ||
			IsOtherNamespacesDirty ||
			IsInsertTimeDirty ||
			IsModTimeDirty;
	}

	int field_WebService_ID;
	bool IsWebService_IDNull;
	bool IsWebService_IDDirty;

	int field_Wsdl_ID;
	bool IsWsdl_IDNull;
	bool IsWsdl_IDDirty;

	std::string field_WebService_Name;
	bool IsWebService_NameNull;
	bool IsWebService_NameDirty;

	std::string field_WebService_Url;
	bool IsWebService_UrlNull;
	bool IsWebService_UrlDirty;

	std::string field_Service;
	bool IsServiceNull;
	bool IsServiceDirty;

	std::string field_Port;
	bool IsPortNull;
	bool IsPortDirty;

	std::string field_Binding;
	bool IsBindingNull;
	bool IsBindingDirty;

	std::string field_Operation;
	bool IsOperationNull;
	bool IsOperationDirty;

	int field_SoapVersion;
	bool IsSoapVersionNull;
	bool IsSoapVersionDirty;

	std::string field_SoapAction;
	bool IsSoapActionNull;
	bool IsSoapActionDirty;

	std::string field_SoapStyle;
	bool IsSoapStyleNull;
	bool IsSoapStyleDirty;

	std::string field_SoapBodyUse;
	bool IsSoapBodyUseNull;
	bool IsSoapBodyUseDirty;

	std::string field_InputXSDFileName;
	bool IsInputXSDFileNameNull;
	bool IsInputXSDFileNameDirty;

	std::string field_OutputXSDFileName;
	bool IsOutputXSDFileNameNull;
	bool IsOutputXSDFileNameDirty;

	std::string field_HttpUserName;
	bool IsHttpUserNameNull;
	bool IsHttpUserNameDirty;

	std::string field_HttpPassword;
	bool IsHttpPasswordNull;
	bool IsHttpPasswordDirty;

	std::string field_Certificate;
	bool IsCertificateNull;
	bool IsCertificateDirty;

	std::string field_TargetNamespace;
	bool IsTargetNamespaceNull;
	bool IsTargetNamespaceDirty;

	std::string field_OtherNamespaces;
	bool IsOtherNamespacesNull;
	bool IsOtherNamespacesDirty;

	TIMESTAMP_STRUCT field_InsertTime;
	bool IsInsertTimeNull;
	bool IsInsertTimeDirty;

	TIMESTAMP_STRUCT field_ModTime;
	bool IsModTimeNull;
	bool IsModTimeDirty;


private:
	bool IsWebService_IDPrimaryKey;
	bool IsWsdl_IDPrimaryKey;
	bool IsWebService_NamePrimaryKey;
	bool IsWebService_UrlPrimaryKey;
	bool IsServicePrimaryKey;
	bool IsPortPrimaryKey;
	bool IsBindingPrimaryKey;
	bool IsOperationPrimaryKey;
	bool IsSoapVersionPrimaryKey;
	bool IsSoapActionPrimaryKey;
	bool IsSoapStylePrimaryKey;
	bool IsSoapBodyUsePrimaryKey;
	bool IsInputXSDFileNamePrimaryKey;
	bool IsOutputXSDFileNamePrimaryKey;
	bool IsHttpUserNamePrimaryKey;
	bool IsHttpPasswordPrimaryKey;
	bool IsCertificatePrimaryKey;
	bool IsTargetNamespacePrimaryKey;
	bool IsOtherNamespacesPrimaryKey;
	bool IsInsertTimePrimaryKey;
	bool IsModTimePrimaryKey;

};


class WebServicesTab : public konga::DbRecordSet {
public:
	WebServicesTab() {}

	static WebServicesTab* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "WebServicesTab";
	}

	void DoFieldExchange(const WebServicesTabRow& row) {
		m_row.Copy(row);
	}

	void Edit();

	std::string GetUpdateFilter() {
		return m_stUpdateFilter;
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

	std::string GetWebService_Name() {
		if( m_row.IsWebService_NameNull )
			throw konga::DbNullException("WebService_Name");
		return m_row.field_WebService_Name;
	}
	void SetWebService_Name(const std::string& st) {
		m_row.field_WebService_Name = st;
		m_row.IsWebService_NameNull = false;
		m_row.IsWebService_NameDirty = true;
	}
	bool IsWebService_NameNull() const {
		return m_row.IsWebService_NameNull;
	}
	void SetWebService_NameToNull() {
		m_row.field_WebService_Name = "";
		m_row.IsWebService_NameNull = true;
		m_row.IsWebService_NameDirty = true;
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

	std::string GetService() {
		if( m_row.IsServiceNull )
			throw konga::DbNullException("Service");
		return m_row.field_Service;
	}
	void SetService(const std::string& st) {
		m_row.field_Service = st;
		m_row.IsServiceNull = false;
		m_row.IsServiceDirty = true;
	}
	bool IsServiceNull() const {
		return m_row.IsServiceNull;
	}
	void SetServiceToNull() {
		m_row.field_Service = "";
		m_row.IsServiceNull = true;
		m_row.IsServiceDirty = true;
	}

	std::string GetPort() {
		if( m_row.IsPortNull )
			throw konga::DbNullException("Port");
		return m_row.field_Port;
	}
	void SetPort(const std::string& st) {
		m_row.field_Port = st;
		m_row.IsPortNull = false;
		m_row.IsPortDirty = true;
	}
	bool IsPortNull() const {
		return m_row.IsPortNull;
	}
	void SetPortToNull() {
		m_row.field_Port = "";
		m_row.IsPortNull = true;
		m_row.IsPortDirty = true;
	}

	std::string GetBinding() {
		if( m_row.IsBindingNull )
			throw konga::DbNullException("Binding");
		return m_row.field_Binding;
	}
	void SetBinding(const std::string& st) {
		m_row.field_Binding = st;
		m_row.IsBindingNull = false;
		m_row.IsBindingDirty = true;
	}
	bool IsBindingNull() const {
		return m_row.IsBindingNull;
	}
	void SetBindingToNull() {
		m_row.field_Binding = "";
		m_row.IsBindingNull = true;
		m_row.IsBindingDirty = true;
	}

	std::string GetOperation() {
		if( m_row.IsOperationNull )
			throw konga::DbNullException("Operation");
		return m_row.field_Operation;
	}
	void SetOperation(const std::string& st) {
		m_row.field_Operation = st;
		m_row.IsOperationNull = false;
		m_row.IsOperationDirty = true;
	}
	bool IsOperationNull() const {
		return m_row.IsOperationNull;
	}
	void SetOperationToNull() {
		m_row.field_Operation = "";
		m_row.IsOperationNull = true;
		m_row.IsOperationDirty = true;
	}

	int GetSoapVersion() {
		if( m_row.IsSoapVersionNull )
			throw konga::DbNullException("SoapVersion");
		return m_row.field_SoapVersion;
	}
	void SetSoapVersion(int i) {
		m_row.field_SoapVersion = i;
		m_row.IsSoapVersionNull = false;
		m_row.IsSoapVersionDirty = true;
	}
	bool IsSoapVersionNull() const {
		return m_row.IsSoapVersionNull;
	}
	void SetSoapVersionToNull() {
		m_row.IsSoapVersionNull = true;
		m_row.IsSoapVersionDirty = true;
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

	std::string GetCertificate() {
		if( m_row.IsCertificateNull )
			throw konga::DbNullException("Certificate");
		return m_row.field_Certificate;
	}
	void SetCertificate(const std::string& st) {
		m_row.field_Certificate = st;
		m_row.IsCertificateNull = false;
		m_row.IsCertificateDirty = true;
	}
	bool IsCertificateNull() const {
		return m_row.IsCertificateNull;
	}
	void SetCertificateToNull() {
		m_row.field_Certificate = "";
		m_row.IsCertificateNull = true;
		m_row.IsCertificateDirty = true;
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

	TIMESTAMP_STRUCT GetInsertTime() {
		if( m_row.IsInsertTimeNull )
			throw konga::DbNullException("InsertTime");
		return m_row.field_InsertTime;
	}
	void SetInsertTime(const TIMESTAMP_STRUCT& ts) {
		m_row.field_InsertTime = ts;
		m_row.IsInsertTimeNull = false;
		m_row.IsInsertTimeDirty = true;
	}
	bool IsInsertTimeNull() const {
		return m_row.IsInsertTimeNull;
	}
	void SetInsertTimeToNull() {
		m_row.IsInsertTimeNull = true;
		m_row.IsInsertTimeDirty = true;
	}

	TIMESTAMP_STRUCT GetModTime() {
		if( m_row.IsModTimeNull )
			throw konga::DbNullException("ModTime");
		return m_row.field_ModTime;
	}
	void SetModTime(const TIMESTAMP_STRUCT& ts) {
		m_row.field_ModTime = ts;
		m_row.IsModTimeNull = false;
		m_row.IsModTimeDirty = true;
	}
	bool IsModTimeNull() const {
		return m_row.IsModTimeNull;
	}
	void SetModTimeToNull() {
		m_row.IsModTimeNull = true;
		m_row.IsModTimeDirty = true;
	}

protected:
	WebServicesTabRow m_row;

private:
	bool IsWebService_IDDirty() const {
		return m_row.IsWebService_IDDirty;
	};
	bool IsWsdl_IDDirty() const {
		return m_row.IsWsdl_IDDirty;
	};
	bool IsWebService_NameDirty() const {
		return m_row.IsWebService_NameDirty;
	};
	bool IsWebService_UrlDirty() const {
		return m_row.IsWebService_UrlDirty;
	};
	bool IsServiceDirty() const {
		return m_row.IsServiceDirty;
	};
	bool IsPortDirty() const {
		return m_row.IsPortDirty;
	};
	bool IsBindingDirty() const {
		return m_row.IsBindingDirty;
	};
	bool IsOperationDirty() const {
		return m_row.IsOperationDirty;
	};
	bool IsSoapVersionDirty() const {
		return m_row.IsSoapVersionDirty;
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
	bool IsInputXSDFileNameDirty() const {
		return m_row.IsInputXSDFileNameDirty;
	};
	bool IsOutputXSDFileNameDirty() const {
		return m_row.IsOutputXSDFileNameDirty;
	};
	bool IsHttpUserNameDirty() const {
		return m_row.IsHttpUserNameDirty;
	};
	bool IsHttpPasswordDirty() const {
		return m_row.IsHttpPasswordDirty;
	};
	bool IsCertificateDirty() const {
		return m_row.IsCertificateDirty;
	};
	bool IsTargetNamespaceDirty() const {
		return m_row.IsTargetNamespaceDirty;
	};
	bool IsOtherNamespacesDirty() const {
		return m_row.IsOtherNamespacesDirty;
	};
	bool IsInsertTimeDirty() const {
		return m_row.IsInsertTimeDirty;
	};
	bool IsModTimeDirty() const {
		return m_row.IsModTimeDirty;
	};


	std::string m_stUpdateFilter;

};

}                         


#endif
