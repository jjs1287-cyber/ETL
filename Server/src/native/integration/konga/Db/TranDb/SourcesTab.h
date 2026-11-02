/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_CRTRANDB_SOURCESTAB_H
#define KONGA_DB_CRTRANDB_SOURCESTAB_H

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

class SourcesTabRow
{
public:
	SourcesTabRow() : 
			field_Source_ID(0),
			IsSource_IDNull(true),
			IsSource_IDPrimaryKey(true),
			IsSource_IDDirty(false),
			IsSource_NameNull(true),
			IsSource_NamePrimaryKey(false),
			IsSource_NameDirty(false),
			field_Source_Type_ID(0),
			IsSource_Type_IDNull(true),
			IsSource_Type_IDPrimaryKey(false),
			IsSource_Type_IDDirty(false),
			field_Document_ID(0),
			IsDocument_IDNull(true),
			IsDocument_IDPrimaryKey(false),
			IsDocument_IDDirty(false),
			IsLocatorNull(true),
			IsLocatorPrimaryKey(false),
			IsLocatorDirty(false),
			IsPathNull(true),
			IsPathPrimaryKey(false),
			IsPathDirty(false),
			IsFile_NameNull(true),
			IsFile_NamePrimaryKey(false),
			IsFile_NameDirty(false),
			field_Timeout(0),
			IsTimeoutNull(true),
			IsTimeoutPrimaryKey(false),
			IsTimeoutDirty(false),
			IsLoginNull(true),
			IsLoginPrimaryKey(false),
			IsLoginDirty(false),
			IsPasswordNull(true),
			IsPasswordPrimaryKey(false),
			IsPasswordDirty(false),
			IsFilterNull(true),
			IsFilterPrimaryKey(false),
			IsFilterDirty(false),
			field_Ignore_First(0),
			IsIgnore_FirstNull(true),
			IsIgnore_FirstPrimaryKey(false),
			IsIgnore_FirstDirty(false),
			field_Ignore_Last(0),
			IsIgnore_LastNull(true),
			IsIgnore_LastPrimaryKey(false),
			IsIgnore_LastDirty(false),
			IsDSNNull(true),
			IsDSNPrimaryKey(false),
			IsDSNDirty(false),
			field_Transfer_Type(0),
			IsTransfer_TypeNull(true),
			IsTransfer_TypePrimaryKey(false),
			IsTransfer_TypeDirty(false),
			IsEncrypt_textNull(true),
			IsEncrypt_textPrimaryKey(false),
			IsEncrypt_textDirty(false),
			field_Encrypt_choice(0),
			IsEncrypt_choiceNull(true),
			IsEncrypt_choicePrimaryKey(false),
			IsEncrypt_choiceDirty(false),
			IsDriverNull(true),
			IsDriverPrimaryKey(false),
			IsDriverDirty(false),
			field_DriverType(0),
			IsDriverTypeNull(true),
			IsDriverTypePrimaryKey(false),
			IsDriverTypeDirty(false),
			field_DBType_Id(0),
			IsDBType_IdNull(true),
			IsDBType_IdPrimaryKey(false),
			IsDBType_IdDirty(false),
			field_Port(0),
			IsPortNull(true),
			IsPortPrimaryKey(false),
			IsPortDirty(false),
			field_Passive_Mode(0),
			IsPassive_ModeNull(true),
			IsPassive_ModePrimaryKey(false),
			IsPassive_ModeDirty(false),
			IsDBNameNull(true),
			IsDBNamePrimaryKey(false),
			IsDBNameDirty(false),
			IsConnectStringNull(true),
			IsConnectStringPrimaryKey(false),
			IsConnectStringDirty(false),
			IsRenameFileToNull(true),
			IsRenameFileToPrimaryKey(false),
			IsRenameFileToDirty(false),
			field_Secured(0),
			IsSecuredNull(true),
			IsSecuredPrimaryKey(false),
			IsSecuredDirty(false),
			IsHTTPURLNull(true),
			IsHTTPURLPrimaryKey(false),
			IsHTTPURLDirty(false),
			IsCertIFicateNameNull(true),
			IsCertIFicateNamePrimaryKey(false),
			IsCertIFicateNameDirty(false),
			IsOptional_ConnectStringNull(true),
			IsOptional_ConnectStringPrimaryKey(false),
			IsOptional_ConnectStringDirty(false),
			IsODBC_UNC_UserIDNull(true),
			IsODBC_UNC_UserIDPrimaryKey(false),
			IsODBC_UNC_UserIDDirty(false),
			IsODBC_UNC_PasswordNull(true),
			IsODBC_UNC_PasswordPrimaryKey(false),
			IsODBC_UNC_PasswordDirty(false),
			IsHttpVerbNull(true),
			IsHttpVerbPrimaryKey(false),
			IsHttpVerbDirty(false),
			field_HttpVerb(0),
			IsJms_Message_SelectorNull(true),
			IsJms_Message_SelectorPrimaryKey(false),
			IsJms_Message_SelectorDirty(false)
	{
	}

	void Copy(const SourcesTabRow& r)
	{
		field_Source_ID = r.field_Source_ID;
		IsSource_IDNull = r.IsSource_IDNull;
		IsSource_IDDirty = r.IsSource_IDDirty;

		field_Source_Name = r.field_Source_Name;
		IsSource_NameNull = r.IsSource_NameNull;
		IsSource_NameDirty = r.IsSource_NameDirty;

		field_Source_Type_ID = r.field_Source_Type_ID;
		IsSource_Type_IDNull = r.IsSource_Type_IDNull;
		IsSource_Type_IDDirty = r.IsSource_Type_IDDirty;

		field_Document_ID = r.field_Document_ID;
		IsDocument_IDNull = r.IsDocument_IDNull;
		IsDocument_IDDirty = r.IsDocument_IDDirty;

		field_Locator = r.field_Locator;
		IsLocatorNull = r.IsLocatorNull;
		IsLocatorDirty = r.IsLocatorDirty;

		field_Path = r.field_Path;
		IsPathNull = r.IsPathNull;
		IsPathDirty = r.IsPathDirty;

		field_File_Name = r.field_File_Name;
		IsFile_NameNull = r.IsFile_NameNull;
		IsFile_NameDirty = r.IsFile_NameDirty;

		field_Timeout = r.field_Timeout;
		IsTimeoutNull = r.IsTimeoutNull;
		IsTimeoutDirty = r.IsTimeoutDirty;

		field_Login = r.field_Login;
		IsLoginNull = r.IsLoginNull;
		IsLoginDirty = r.IsLoginDirty;

		field_Password = r.field_Password;
		IsPasswordNull = r.IsPasswordNull;
		IsPasswordDirty = r.IsPasswordDirty;

		field_Filter = r.field_Filter;
		IsFilterNull = r.IsFilterNull;
		IsFilterDirty = r.IsFilterDirty;

		field_Ignore_First = r.field_Ignore_First;
		IsIgnore_FirstNull = r.IsIgnore_FirstNull;
		IsIgnore_FirstDirty = r.IsIgnore_FirstDirty;

		field_Ignore_Last = r.field_Ignore_Last;
		IsIgnore_LastNull = r.IsIgnore_LastNull;
		IsIgnore_LastDirty = r.IsIgnore_LastDirty;

		field_DSN = r.field_DSN;
		IsDSNNull = r.IsDSNNull;
		IsDSNDirty = r.IsDSNDirty;

		field_Transfer_Type = r.field_Transfer_Type;
		IsTransfer_TypeNull = r.IsTransfer_TypeNull;
		IsTransfer_TypeDirty = r.IsTransfer_TypeDirty;

		field_Encrypt_text = r.field_Encrypt_text;
		IsEncrypt_textNull = r.IsEncrypt_textNull;
		IsEncrypt_textDirty = r.IsEncrypt_textDirty;

		field_Encrypt_choice = r.field_Encrypt_choice;
		IsEncrypt_choiceNull = r.IsEncrypt_choiceNull;
		IsEncrypt_choiceDirty = r.IsEncrypt_choiceDirty;

		field_Driver = r.field_Driver;
		IsDriverNull = r.IsDriverNull;
		IsDriverDirty = r.IsDriverDirty;

		field_DriverType = r.field_DriverType;
		IsDriverTypeNull = r.IsDriverTypeNull;
		IsDriverTypeDirty = r.IsDriverTypeDirty;

		field_DBType_Id = r.field_DBType_Id;
		IsDBType_IdNull = r.IsDBType_IdNull;
		IsDBType_IdDirty = r.IsDBType_IdDirty;

		field_Port = r.field_Port;
		IsPortNull = r.IsPortNull;
		IsPortDirty = r.IsPortDirty;

		field_Passive_Mode = r.field_Passive_Mode;
		IsPassive_ModeNull = r.IsPassive_ModeNull;
		IsPassive_ModeDirty = r.IsPassive_ModeDirty;

		field_DBName = r.field_DBName;
		IsDBNameNull = r.IsDBNameNull;
		IsDBNameDirty = r.IsDBNameDirty;

		field_ConnectString = r.field_ConnectString;
		IsConnectStringNull = r.IsConnectStringNull;
		IsConnectStringDirty = r.IsConnectStringDirty;

		field_RenameFileTo = r.field_RenameFileTo;
		IsRenameFileToNull = r.IsRenameFileToNull;
		IsRenameFileToDirty = r.IsRenameFileToDirty;

		field_Secured = r.field_Secured;
		IsSecuredNull = r.IsSecuredNull;
		IsSecuredDirty = r.IsSecuredDirty;

		field_HTTPURL = r.field_HTTPURL;
		IsHTTPURLNull = r.IsHTTPURLNull;
		IsHTTPURLDirty = r.IsHTTPURLDirty;

		field_CertIFicateName = r.field_CertIFicateName;
		IsCertIFicateNameNull = r.IsCertIFicateNameNull;
		IsCertIFicateNameDirty = r.IsCertIFicateNameDirty;

		field_Optional_ConnectString = r.field_Optional_ConnectString;
		IsOptional_ConnectStringNull = r.IsOptional_ConnectStringNull;
		IsOptional_ConnectStringDirty = r.IsOptional_ConnectStringDirty;

		field_ODBC_UNC_UserID = r.field_ODBC_UNC_UserID;
		IsODBC_UNC_UserIDNull = r.IsODBC_UNC_UserIDNull;
		IsODBC_UNC_UserIDDirty = r.IsODBC_UNC_UserIDDirty;

		field_ODBC_UNC_Password = r.field_ODBC_UNC_Password;
		IsODBC_UNC_PasswordNull = r.IsODBC_UNC_PasswordNull;
		IsODBC_UNC_PasswordDirty = r.IsODBC_UNC_PasswordDirty;

		field_HttpVerb = r.field_HttpVerb;
		IsHttpVerbNull = r.IsHttpVerbNull;
		IsHttpVerbDirty = r.IsHttpVerbDirty;

		field_Jms_Message_Selector = r.field_Jms_Message_Selector;
		IsJms_Message_SelectorNull = r.IsJms_Message_SelectorNull;
		IsJms_Message_SelectorDirty = r.IsJms_Message_SelectorDirty;
	}

	void CopyPKs(const SourcesTabRow& r)
	{
		field_Source_ID = r.field_Source_ID;
		IsSource_IDNull = r.IsSource_IDNull;
		IsSource_IDDirty = r.IsSource_IDDirty;

	}

	void SetDirty(bool b)
	{
		IsSource_IDDirty = b;
		IsSource_NameDirty = b;
		IsSource_Type_IDDirty = b;
		IsDocument_IDDirty = b;
		IsLocatorDirty = b;
		IsPathDirty = b;
		IsFile_NameDirty = b;
		IsTimeoutDirty = b;
		IsLoginDirty = b;
		IsPasswordDirty = b;
		IsFilterDirty = b;
		IsIgnore_FirstDirty = b;
		IsIgnore_LastDirty = b;
		IsDSNDirty = b;
		IsTransfer_TypeDirty = b;
		IsEncrypt_textDirty = b;
		IsEncrypt_choiceDirty = b;
		IsDriverDirty = b;
		IsDriverTypeDirty = b;
		IsDBType_IdDirty = b;
		IsPortDirty = b;
		IsPassive_ModeDirty = b;
		IsDBNameDirty = b;
		IsConnectStringDirty = b;
		IsRenameFileToDirty = b;
		IsSecuredDirty = b;
		IsHTTPURLDirty = b;
		IsCertIFicateNameDirty = b;
		IsOptional_ConnectStringDirty = b;
		IsODBC_UNC_UserIDDirty = b;
		IsODBC_UNC_PasswordDirty = b;
		IsJms_Message_SelectorDirty = b;
	}

	bool IsDirty() const
	{
		return IsSource_IDDirty ||
			IsSource_NameDirty ||
			IsSource_Type_IDDirty ||
			IsDocument_IDDirty ||
			IsLocatorDirty ||
			IsPathDirty ||
			IsFile_NameDirty ||
			IsTimeoutDirty ||
			IsLoginDirty ||
			IsPasswordDirty ||
			IsFilterDirty ||
			IsIgnore_FirstDirty ||
			IsIgnore_LastDirty ||
			IsDSNDirty ||
			IsTransfer_TypeDirty ||
			IsEncrypt_textDirty ||
			IsEncrypt_choiceDirty ||
			IsDriverDirty ||
			IsDriverTypeDirty ||
			IsDBType_IdDirty ||
			IsPortDirty ||
			IsPassive_ModeDirty ||
			IsDBNameDirty ||
			IsConnectStringDirty ||
			IsRenameFileToDirty ||
			IsSecuredDirty ||
			IsHTTPURLDirty ||
			IsCertIFicateNameDirty ||
			IsOptional_ConnectStringDirty ||
			IsODBC_UNC_UserIDDirty ||
			IsODBC_UNC_PasswordDirty ||
			IsHttpVerbDirty ||
			IsJms_Message_SelectorDirty;
	}

	int field_Source_ID;
	bool IsSource_IDNull;
	bool IsSource_IDDirty;

	std::string field_Source_Name;
	bool IsSource_NameNull;
	bool IsSource_NameDirty;

	int field_Source_Type_ID;
	bool IsSource_Type_IDNull;
	bool IsSource_Type_IDDirty;

	int field_Document_ID;
	bool IsDocument_IDNull;
	bool IsDocument_IDDirty;

	std::string field_Locator;
	bool IsLocatorNull;
	bool IsLocatorDirty;

	std::string field_Path;
	bool IsPathNull;
	bool IsPathDirty;

	std::string field_File_Name;
	bool IsFile_NameNull;
	bool IsFile_NameDirty;

	int field_Timeout;
	bool IsTimeoutNull;
	bool IsTimeoutDirty;

	std::string field_Login;
	bool IsLoginNull;
	bool IsLoginDirty;

	std::string field_Password;
	bool IsPasswordNull;
	bool IsPasswordDirty;

	std::string field_Filter;
	bool IsFilterNull;
	bool IsFilterDirty;

	int field_Ignore_First;
	bool IsIgnore_FirstNull;
	bool IsIgnore_FirstDirty;

	int field_Ignore_Last;
	bool IsIgnore_LastNull;
	bool IsIgnore_LastDirty;

	std::string field_DSN;
	bool IsDSNNull;
	bool IsDSNDirty;

	int field_Transfer_Type;
	bool IsTransfer_TypeNull;
	bool IsTransfer_TypeDirty;

	std::string field_Encrypt_text;
	bool IsEncrypt_textNull;
	bool IsEncrypt_textDirty;

	int field_Encrypt_choice;
	bool IsEncrypt_choiceNull;
	bool IsEncrypt_choiceDirty;

	std::string field_Driver;
	bool IsDriverNull;
	bool IsDriverDirty;

	int field_DriverType;
	bool IsDriverTypeNull;
	bool IsDriverTypeDirty;

	int field_DBType_Id;
	bool IsDBType_IdNull;
	bool IsDBType_IdDirty;

	int field_Port;
	bool IsPortNull;
	bool IsPortDirty;

	int field_Passive_Mode;
	bool IsPassive_ModeNull;
	bool IsPassive_ModeDirty;

	std::string field_DBName;
	bool IsDBNameNull;
	bool IsDBNameDirty;

	std::string field_ConnectString;
	bool IsConnectStringNull;
	bool IsConnectStringDirty;

	std::string field_RenameFileTo;
	bool IsRenameFileToNull;
	bool IsRenameFileToDirty;

	int field_Secured;
	bool IsSecuredNull;
	bool IsSecuredDirty;

	std::string field_HTTPURL;
	bool IsHTTPURLNull;
	bool IsHTTPURLDirty;

	std::string field_CertIFicateName;
	bool IsCertIFicateNameNull;
	bool IsCertIFicateNameDirty;

	std::string field_Optional_ConnectString;
	bool IsOptional_ConnectStringNull;
	bool IsOptional_ConnectStringDirty;

	std::string field_ODBC_UNC_UserID;
	bool IsODBC_UNC_UserIDNull;
	bool IsODBC_UNC_UserIDDirty;

	std::string field_ODBC_UNC_Password;
	bool IsODBC_UNC_PasswordNull;
	bool IsODBC_UNC_PasswordDirty;

	int field_HttpVerb;
	bool IsHttpVerbNull;
	bool IsHttpVerbDirty;

	std::string field_Jms_Message_Selector;
	bool IsJms_Message_SelectorNull;
	bool IsJms_Message_SelectorDirty;

private:
	bool IsSource_IDPrimaryKey;
	bool IsSource_NamePrimaryKey;
	bool IsSource_Type_IDPrimaryKey;
	bool IsDocument_IDPrimaryKey;
	bool IsLocatorPrimaryKey;
	bool IsPathPrimaryKey;
	bool IsFile_NamePrimaryKey;
	bool IsTimeoutPrimaryKey;
	bool IsLoginPrimaryKey;
	bool IsPasswordPrimaryKey;
	bool IsFilterPrimaryKey;
	bool IsIgnore_FirstPrimaryKey;
	bool IsIgnore_LastPrimaryKey;
	bool IsDSNPrimaryKey;
	bool IsTransfer_TypePrimaryKey;
	bool IsEncrypt_textPrimaryKey;
	bool IsEncrypt_choicePrimaryKey;
	bool IsDriverPrimaryKey;
	bool IsDriverTypePrimaryKey;
	bool IsDBType_IdPrimaryKey;
	bool IsPortPrimaryKey;
	bool IsPassive_ModePrimaryKey;
	bool IsDBNamePrimaryKey;
	bool IsConnectStringPrimaryKey;
	bool IsRenameFileToPrimaryKey;
	bool IsSecuredPrimaryKey;
	bool IsHTTPURLPrimaryKey;
	bool IsCertIFicateNamePrimaryKey;
	bool IsOptional_ConnectStringPrimaryKey;
	bool IsODBC_UNC_UserIDPrimaryKey;
	bool IsODBC_UNC_PasswordPrimaryKey;
	bool IsHttpVerbPrimaryKey;
	bool IsJms_Message_SelectorPrimaryKey;
};


class SourcesTab : public konga::DbRecordSet {
public:
	SourcesTab() {}

	static SourcesTab* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "SourcesTab";
	}

	void DoFieldExchange(const SourcesTabRow& row) {
		m_row.Copy(row);
	}

	void Edit();

	std::string GetUpdateFilter() {
		return m_stUpdateFilter;
	}

	int GetSource_ID() {
		if( m_row.IsSource_IDNull )
			throw konga::DbNullException("Source_ID");
		return m_row.field_Source_ID;
	}
	void SetSource_ID(int i) {
		m_row.field_Source_ID = i;
		m_row.IsSource_IDNull = false;
		m_row.IsSource_IDDirty = true;
	}
	bool IsSource_IDNull() const {
		return m_row.IsSource_IDNull;
	}
	void SetSource_IDToNull() {
		m_row.IsSource_IDNull = true;
		m_row.IsSource_IDDirty = true;
	}

	std::string GetSource_Name() {
		if( m_row.IsSource_NameNull )
			throw konga::DbNullException("Source_Name");
		return m_row.field_Source_Name;
	}
	void SetSource_Name(const std::string& st) {
		m_row.field_Source_Name = st;
		m_row.IsSource_NameNull = false;
		m_row.IsSource_NameDirty = true;
	}
	bool IsSource_NameNull() const {
		return m_row.IsSource_NameNull;
	}
	void SetSource_NameToNull() {
		m_row.field_Source_Name = "";
		m_row.IsSource_NameNull = true;
		m_row.IsSource_NameDirty = true;
	}

	int GetSource_Type_ID() {
		if( m_row.IsSource_Type_IDNull )
			throw konga::DbNullException("Source_Type_ID");
		return m_row.field_Source_Type_ID;
	}
	void SetSource_Type_ID(int i) {
		m_row.field_Source_Type_ID = i;
		m_row.IsSource_Type_IDNull = false;
		m_row.IsSource_Type_IDDirty = true;
	}
	bool IsSource_Type_IDNull() const {
		return m_row.IsSource_Type_IDNull;
	}
	void SetSource_Type_IDToNull() {
		m_row.IsSource_Type_IDNull = true;
		m_row.IsSource_Type_IDDirty = true;
	}

	int GetDocument_ID() {
		if( m_row.IsDocument_IDNull )
			throw konga::DbNullException("Document_ID");
		return m_row.field_Document_ID;
	}
	void SetDocument_ID(int i) {
		m_row.field_Document_ID = i;
		m_row.IsDocument_IDNull = false;
		m_row.IsDocument_IDDirty = true;
	}
	bool IsDocument_IDNull() const {
		return m_row.IsDocument_IDNull;
	}
	void SetDocument_IDToNull() {
		m_row.IsDocument_IDNull = true;
		m_row.IsDocument_IDDirty = true;
	}

	std::string GetLocator() {
		if( m_row.IsLocatorNull )
			throw konga::DbNullException("Locator");
		return m_row.field_Locator;
	}
	void SetLocator(const std::string& st) {
		m_row.field_Locator = st;
		m_row.IsLocatorNull = false;
		m_row.IsLocatorDirty = true;
	}
	bool IsLocatorNull() const {
		return m_row.IsLocatorNull;
	}
	void SetLocatorToNull() {
		m_row.field_Locator = "";
		m_row.IsLocatorNull = true;
		m_row.IsLocatorDirty = true;
	}

	std::string GetPath() {
		if( m_row.IsPathNull )
			throw konga::DbNullException("Path");
		return m_row.field_Path;
	}
	void SetPath(const std::string& st) {
		m_row.field_Path = st;
		m_row.IsPathNull = false;
		m_row.IsPathDirty = true;
	}
	bool IsPathNull() const {
		return m_row.IsPathNull;
	}
	void SetPathToNull() {
		m_row.field_Path = "";
		m_row.IsPathNull = true;
		m_row.IsPathDirty = true;
	}

	std::string GetFile_Name() {
		if( m_row.IsFile_NameNull )
			throw konga::DbNullException("File_Name");
		return m_row.field_File_Name;
	}
	void SetFile_Name(const std::string& st) {
		m_row.field_File_Name = st;
		m_row.IsFile_NameNull = false;
		m_row.IsFile_NameDirty = true;
	}
	bool IsFile_NameNull() const {
		return m_row.IsFile_NameNull;
	}
	void SetFile_NameToNull() {
		m_row.field_File_Name = "";
		m_row.IsFile_NameNull = true;
		m_row.IsFile_NameDirty = true;
	}

	int GetTimeout() {
		if( m_row.IsTimeoutNull )
			throw konga::DbNullException("Timeout");
		return m_row.field_Timeout;
	}
	void SetTimeout(int i) {
		m_row.field_Timeout = i;
		m_row.IsTimeoutNull = false;
		m_row.IsTimeoutDirty = true;
	}
	bool IsTimeoutNull() const {
		return m_row.IsTimeoutNull;
	}
	void SetTimeoutToNull() {
		m_row.IsTimeoutNull = true;
		m_row.IsTimeoutDirty = true;
	}

	std::string GetLogin() {
		if( m_row.IsLoginNull )
			throw konga::DbNullException("Login");
		return m_row.field_Login;
	}
	void SetLogin(const std::string& st) {
		m_row.field_Login = st;
		m_row.IsLoginNull = false;
		m_row.IsLoginDirty = true;
	}
	bool IsLoginNull() const {
		return m_row.IsLoginNull;
	}
	void SetLoginToNull() {
		m_row.field_Login = "";
		m_row.IsLoginNull = true;
		m_row.IsLoginDirty = true;
	}

	std::string GetPassword() {
		if( m_row.IsPasswordNull )
			throw konga::DbNullException("Password");
		return m_row.field_Password;
	}
	void SetPassword(const std::string& st) {
		m_row.field_Password = st;
		m_row.IsPasswordNull = false;
		m_row.IsPasswordDirty = true;
	}
	bool IsPasswordNull() const {
		return m_row.IsPasswordNull;
	}
	void SetPasswordToNull() {
		m_row.field_Password = "";
		m_row.IsPasswordNull = true;
		m_row.IsPasswordDirty = true;
	}

	std::string GetFilter() {
		if( m_row.IsFilterNull )
			throw konga::DbNullException("Filter");
		return m_row.field_Filter;
	}
	void SetFilter(const std::string& st) {
		m_row.field_Filter = st;
		m_row.IsFilterNull = false;
		m_row.IsFilterDirty = true;
	}
	bool IsFilterNull() const {
		return m_row.IsFilterNull;
	}
	void SetFilterToNull() {
		m_row.field_Filter = "";
		m_row.IsFilterNull = true;
		m_row.IsFilterDirty = true;
	}

	int GetIgnore_First() {
		if( m_row.IsIgnore_FirstNull )
			throw konga::DbNullException("Ignore_First");
		return m_row.field_Ignore_First;
	}
	void SetIgnore_First(int i) {
		m_row.field_Ignore_First = i;
		m_row.IsIgnore_FirstNull = false;
		m_row.IsIgnore_FirstDirty = true;
	}
	bool IsIgnore_FirstNull() const {
		return m_row.IsIgnore_FirstNull;
	}
	void SetIgnore_FirstToNull() {
		m_row.IsIgnore_FirstNull = true;
		m_row.IsIgnore_FirstDirty = true;
	}

	int GetIgnore_Last() {
		if( m_row.IsIgnore_LastNull )
			throw konga::DbNullException("Ignore_Last");
		return m_row.field_Ignore_Last;
	}
	void SetIgnore_Last(int i) {
		m_row.field_Ignore_Last = i;
		m_row.IsIgnore_LastNull = false;
		m_row.IsIgnore_LastDirty = true;
	}
	bool IsIgnore_LastNull() const {
		return m_row.IsIgnore_LastNull;
	}
	void SetIgnore_LastToNull() {
		m_row.IsIgnore_LastNull = true;
		m_row.IsIgnore_LastDirty = true;
	}

	std::string GetDSN() {
		if( m_row.IsDSNNull )
			throw konga::DbNullException("DSN");
		return m_row.field_DSN;
	}
	void SetDSN(const std::string& st) {
		m_row.field_DSN = st;
		m_row.IsDSNNull = false;
		m_row.IsDSNDirty = true;
	}
	bool IsDSNNull() const {
		return m_row.IsDSNNull;
	}
	void SetDSNToNull() {
		m_row.field_DSN = "";
		m_row.IsDSNNull = true;
		m_row.IsDSNDirty = true;
	}

	int GetTransfer_Type() {
		if( m_row.IsTransfer_TypeNull )
			throw konga::DbNullException("Transfer_Type");
		return m_row.field_Transfer_Type;
	}
	void SetTransfer_Type(int i) {
		m_row.field_Transfer_Type = i;
		m_row.IsTransfer_TypeNull = false;
		m_row.IsTransfer_TypeDirty = true;
	}
	bool IsTransfer_TypeNull() const {
		return m_row.IsTransfer_TypeNull;
	}
	void SetTransfer_TypeToNull() {
		m_row.IsTransfer_TypeNull = true;
		m_row.IsTransfer_TypeDirty = true;
	}

	std::string GetEncrypt_text() {
		if( m_row.IsEncrypt_textNull )
			throw konga::DbNullException("Encrypt_text");
		return m_row.field_Encrypt_text;
	}
	void SetEncrypt_text(const std::string& st) {
		m_row.field_Encrypt_text = st;
		m_row.IsEncrypt_textNull = false;
		m_row.IsEncrypt_textDirty = true;
	}
	bool IsEncrypt_textNull() const {
		return m_row.IsEncrypt_textNull;
	}
	void SetEncrypt_textToNull() {
		m_row.field_Encrypt_text = "";
		m_row.IsEncrypt_textNull = true;
		m_row.IsEncrypt_textDirty = true;
	}

	int GetEncrypt_choice() {
		if( m_row.IsEncrypt_choiceNull )
			throw konga::DbNullException("Encrypt_choice");
		return m_row.field_Encrypt_choice;
	}
	void SetEncrypt_choice(int i) {
		m_row.field_Encrypt_choice = i;
		m_row.IsEncrypt_choiceNull = false;
		m_row.IsEncrypt_choiceDirty = true;
	}
	bool IsEncrypt_choiceNull() const {
		return m_row.IsEncrypt_choiceNull;
	}
	void SetEncrypt_choiceToNull() {
		m_row.IsEncrypt_choiceNull = true;
		m_row.IsEncrypt_choiceDirty = true;
	}

	std::string GetDriver() {
		if( m_row.IsDriverNull )
			throw konga::DbNullException("Driver");
		return m_row.field_Driver;
	}
	void SetDriver(const std::string& st) {
		m_row.field_Driver = st;
		m_row.IsDriverNull = false;
		m_row.IsDriverDirty = true;
	}
	bool IsDriverNull() const {
		return m_row.IsDriverNull;
	}
	void SetDriverToNull() {
		m_row.field_Driver = "";
		m_row.IsDriverNull = true;
		m_row.IsDriverDirty = true;
	}

	int GetDriverType() {
		if( m_row.IsDriverTypeNull )
			throw konga::DbNullException("DriverType");
		return m_row.field_DriverType;
	}
	void SetDriverType(int type) {
		m_row.field_DriverType = type;
		m_row.IsDriverTypeNull = false;
		m_row.IsDriverTypeDirty = true;
	}
	bool IsDriverTypeNull() const {
		return m_row.IsDriverTypeNull;
	}
	void SetDriverTypeToNull() {
		m_row.IsDriverTypeNull = true;
		m_row.IsDriverTypeDirty = true;
	}

	int GetDBType_Id() {
		if( m_row.IsDBType_IdNull )
			throw konga::DbNullException("DBType_Id");
		return m_row.field_DBType_Id;
	}
	void SetDBType_Id(int i) {
		m_row.field_DBType_Id = i;
		m_row.IsDBType_IdNull = false;
		m_row.IsDBType_IdDirty = true;
	}
	bool IsDBType_IdNull() const {
		return m_row.IsDBType_IdNull;
	}
	void SetDBType_IdToNull() {
		m_row.IsDBType_IdNull = true;
		m_row.IsDBType_IdDirty = true;
	}

	int GetPort() {
		if( m_row.IsPortNull )
			throw konga::DbNullException("Port");
		return m_row.field_Port;
	}
	void SetPort(int i) {
		m_row.field_Port = i;
		m_row.IsPortNull = false;
		m_row.IsPortDirty = true;
	}
	bool IsPortNull() const {
		return m_row.IsPortNull;
	}
	void SetPortToNull() {
		m_row.IsPortNull = true;
		m_row.IsPortDirty = true;
	}

	int GetPassive_Mode() {
		if( m_row.IsPassive_ModeNull )
			throw konga::DbNullException("Passive_Mode");
		return m_row.field_Passive_Mode;
	}
	void SetPassive_Mode(int i) {
		m_row.field_Passive_Mode = i;
		m_row.IsPassive_ModeNull = false;
		m_row.IsPassive_ModeDirty = true;
	}
	bool IsPassive_ModeNull() const {
		return m_row.IsPassive_ModeNull;
	}
	void SetPassive_ModeToNull() {
		m_row.IsPassive_ModeNull = true;
		m_row.IsPassive_ModeDirty = true;
	}

	std::string GetDBName() {
		if( m_row.IsDBNameNull )
			throw konga::DbNullException("DBName");
		return m_row.field_DBName;
	}
	void SetDBName(const std::string& st) {
		m_row.field_DBName = st;
		m_row.IsDBNameNull = false;
		m_row.IsDBNameDirty = true;
	}
	bool IsDBNameNull() const {
		return m_row.IsDBNameNull;
	}
	void SetDBNameToNull() {
		m_row.field_DBName = "";
		m_row.IsDBNameNull = true;
		m_row.IsDBNameDirty = true;
	}

	std::string GetConnectString() {
		if( m_row.IsConnectStringNull )
			throw konga::DbNullException("ConnectString");
		return m_row.field_ConnectString;
	}
	void SetConnectString(const std::string& st) {
		m_row.field_ConnectString = st;
		m_row.IsConnectStringNull = false;
		m_row.IsConnectStringDirty = true;
	}
	bool IsConnectStringNull() const {
		return m_row.IsConnectStringNull;
	}
	void SetConnectStringToNull() {
		m_row.field_ConnectString = "";
		m_row.IsConnectStringNull = true;
		m_row.IsConnectStringDirty = true;
	}

	std::string GetRenameFileTo() {
		if( m_row.IsRenameFileToNull )
			throw konga::DbNullException("RenameFileTo");
		return m_row.field_RenameFileTo;
	}
	void SetRenameFileTo(const std::string& st) {
		m_row.field_RenameFileTo = st;
		m_row.IsRenameFileToNull = false;
		m_row.IsRenameFileToDirty = true;
	}
	bool IsRenameFileToNull() const {
		return m_row.IsRenameFileToNull;
	}
	void SetRenameFileToToNull() {
		m_row.field_RenameFileTo = "";
		m_row.IsRenameFileToNull = true;
		m_row.IsRenameFileToDirty = true;
	}

	int GetSecured() {
		if( m_row.IsSecuredNull )
			throw konga::DbNullException("Secured");
		return m_row.field_Secured;
	}
	void SetSecured(int i) {
		m_row.field_Secured = i;
		m_row.IsSecuredNull = false;
		m_row.IsSecuredDirty = true;
	}
	bool IsSecuredNull() const {
		return m_row.IsSecuredNull;
	}
	void SetSecuredToNull() {
		m_row.IsSecuredNull = true;
		m_row.IsSecuredDirty = true;
	}

	std::string GetHTTPURL() {
		if( m_row.IsHTTPURLNull )
			throw konga::DbNullException("HTTPURL");
		return m_row.field_HTTPURL;
	}
	void SetHTTPURL(const std::string& st) {
		m_row.field_HTTPURL = st;
		m_row.IsHTTPURLNull = false;
		m_row.IsHTTPURLDirty = true;
	}
	bool IsHTTPURLNull() const {
		return m_row.IsHTTPURLNull;
	}
	void SetHTTPURLToNull() {
		m_row.field_HTTPURL = "";
		m_row.IsHTTPURLNull = true;
		m_row.IsHTTPURLDirty = true;
	}

	std::string GetCertIFicateName() {
		if( m_row.IsCertIFicateNameNull )
			throw konga::DbNullException("CertIFicateName");
		return m_row.field_CertIFicateName;
	}
	void SetCertIFicateName(const std::string& st) {
		m_row.field_CertIFicateName = st;
		m_row.IsCertIFicateNameNull = false;
		m_row.IsCertIFicateNameDirty = true;
	}
	bool IsCertIFicateNameNull() const {
		return m_row.IsCertIFicateNameNull;
	}
	void SetCertIFicateNameToNull() {
		m_row.field_CertIFicateName = "";
		m_row.IsCertIFicateNameNull = true;
		m_row.IsCertIFicateNameDirty = true;
	}

	std::string GetOptional_ConnectString() {
		if( m_row.IsOptional_ConnectStringNull )
			throw konga::DbNullException("Optional_ConnectString");
		return m_row.field_Optional_ConnectString;
	}
	void SetOptional_ConnectString(const std::string& st) {
		m_row.field_Optional_ConnectString = st;
		m_row.IsOptional_ConnectStringNull = false;
		m_row.IsOptional_ConnectStringDirty = true;
	}
	bool IsOptional_ConnectStringNull() const {
		return m_row.IsOptional_ConnectStringNull;
	}
	void SetOptional_ConnectStringToNull() {
		m_row.field_Optional_ConnectString = "";
		m_row.IsOptional_ConnectStringNull = true;
		m_row.IsOptional_ConnectStringDirty = true;
	}

	std::string GetODBC_UNC_UserID() {
		if( m_row.IsODBC_UNC_UserIDNull )
			throw konga::DbNullException("ODBC_UNC_UserID");
		return m_row.field_ODBC_UNC_UserID;
	}
	void SetODBC_UNC_UserID(const std::string& st) {
		m_row.field_ODBC_UNC_UserID = st;
		m_row.IsODBC_UNC_UserIDNull = false;
		m_row.IsODBC_UNC_UserIDDirty = true;
	}
	bool IsODBC_UNC_UserIDNull() const {
		return m_row.IsODBC_UNC_UserIDNull;
	}
	void SetODBC_UNC_UserIDToNull() {
		m_row.field_ODBC_UNC_UserID = "";
		m_row.IsODBC_UNC_UserIDNull = true;
		m_row.IsODBC_UNC_UserIDDirty = true;
	}

	std::string GetODBC_UNC_Password() {
		if( m_row.IsODBC_UNC_PasswordNull )
			throw konga::DbNullException("ODBC_UNC_Password");
		return m_row.field_ODBC_UNC_Password;
	}
	void SetODBC_UNC_Password(const std::string& st) {
		m_row.field_ODBC_UNC_Password = st;
		m_row.IsODBC_UNC_PasswordNull = false;
		m_row.IsODBC_UNC_PasswordDirty = true;
	}
	bool IsODBC_UNC_PasswordNull() const {
		return m_row.IsODBC_UNC_PasswordNull;
	}
	void SetODBC_UNC_PasswordToNull() {
		m_row.field_ODBC_UNC_Password = "";
		m_row.IsODBC_UNC_PasswordNull = true;
		m_row.IsODBC_UNC_PasswordDirty = true;
	}

	int GetHttpVerb() {
		if( m_row.IsHttpVerbNull )
			throw konga::DbNullException("Http_Verb");
		return m_row.field_HttpVerb;
	}
	void SetHttpVerb(int i) {
		m_row.field_HttpVerb = i;
		m_row.IsHttpVerbNull = false;
		m_row.IsHttpVerbDirty = true;
	}
	bool IsHttpVerbNull() const {
		return m_row.IsHttpVerbNull;
	}
	void SetHttpVerbNull() {
		m_row.IsHttpVerbNull = true;
		m_row.IsHttpVerbDirty = true;
	}

	std::string GetJms_Message_Selector() {
		if( m_row.IsJms_Message_SelectorNull )
			throw konga::DbNullException("Jms_Message_Selector");
		return m_row.field_Jms_Message_Selector;
	}
	void SetJms_Message_Selector(const std::string& st) {
		m_row.field_Jms_Message_Selector = st;
		m_row.IsJms_Message_SelectorNull = false;
		m_row.IsJms_Message_SelectorDirty = true;
	}
	bool IsJms_Message_SelectorNull() const {
		return m_row.IsJms_Message_SelectorNull;
	}
	void SetJms_Message_SelectorToNull() {
		m_row.field_Jms_Message_Selector = "";
		m_row.IsJms_Message_SelectorNull = true;
		m_row.IsJms_Message_SelectorDirty = true;
	}

protected:
	SourcesTabRow m_row;

private:
	bool IsSource_IDDirty() const {
		return m_row.IsSource_IDDirty;
	};
	bool IsSource_NameDirty() const {
		return m_row.IsSource_NameDirty;
	};
	bool IsSource_Type_IDDirty() const {
		return m_row.IsSource_Type_IDDirty;
	};
	bool IsDocument_IDDirty() const {
		return m_row.IsDocument_IDDirty;
	};
	bool IsLocatorDirty() const {
		return m_row.IsLocatorDirty;
	};
	bool IsPathDirty() const {
		return m_row.IsPathDirty;
	};
	bool IsFile_NameDirty() const {
		return m_row.IsFile_NameDirty;
	};
	bool IsTimeoutDirty() const {
		return m_row.IsTimeoutDirty;
	};
	bool IsLoginDirty() const {
		return m_row.IsLoginDirty;
	};
	bool IsPasswordDirty() const {
		return m_row.IsPasswordDirty;
	};
	bool IsFilterDirty() const {
		return m_row.IsFilterDirty;
	};
	bool IsIgnore_FirstDirty() const {
		return m_row.IsIgnore_FirstDirty;
	};
	bool IsIgnore_LastDirty() const {
		return m_row.IsIgnore_LastDirty;
	};
	bool IsDSNDirty() const {
		return m_row.IsDSNDirty;
	};
	bool IsTransfer_TypeDirty() const {
		return m_row.IsTransfer_TypeDirty;
	};
	bool IsEncrypt_textDirty() const {
		return m_row.IsEncrypt_textDirty;
	};
	bool IsEncrypt_choiceDirty() const {
		return m_row.IsEncrypt_choiceDirty;
	};
	bool IsDriverDirty() const {
		return m_row.IsDriverDirty;
	};
	bool IsDriverTypeDirty() const {
		return m_row.IsDriverTypeDirty;
	}
	bool IsDBType_IdDirty() const {
		return m_row.IsDBType_IdDirty;
	};
	bool IsPortDirty() const {
		return m_row.IsPortDirty;
	};
	bool IsPassive_ModeDirty() const {
		return m_row.IsPassive_ModeDirty;
	};
	bool IsDBNameDirty() const {
		return m_row.IsDBNameDirty;
	};
	bool IsConnectStringDirty() const {
		return m_row.IsConnectStringDirty;
	};
	bool IsRenameFileToDirty() const {
		return m_row.IsRenameFileToDirty;
	};
	bool IsSecuredDirty() const {
		return m_row.IsSecuredDirty;
	};
	bool IsHTTPURLDirty() const {
		return m_row.IsHTTPURLDirty;
	};
	bool IsCertIFicateNameDirty() const {
		return m_row.IsCertIFicateNameDirty;
	};
	bool IsOptional_ConnectStringDirty() const {
		return m_row.IsOptional_ConnectStringDirty;
	};
	bool IsODBC_UNC_UserIDDirty() const {
		return m_row.IsODBC_UNC_UserIDDirty;
	};
	bool IsODBC_UNC_PasswordDirty() const {
		return m_row.IsODBC_UNC_PasswordDirty;
	};
	bool IsHttpVerbDirty() const {
		return m_row.IsHttpVerbDirty;
	};

	bool IsJms_Message_SelectorDirty() const {
		return m_row.IsJms_Message_SelectorDirty;
	};

	std::string m_stUpdateFilter;

};

}                         


#endif
