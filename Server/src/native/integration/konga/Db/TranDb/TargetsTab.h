/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_CRTRANDB_TARGETSTAB_H
#define KONGA_DB_CRTRANDB_TARGETSTAB_H

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

class TargetsTabRow
{
public:
	TargetsTabRow() : 
			field_Target_ID(0),
			IsTarget_IDNull(true),
			IsTarget_IDPrimaryKey(true),
			IsTarget_IDDirty(false),
			IsTarget_NameNull(true),
			IsTarget_NamePrimaryKey(false),
			IsTarget_NameDirty(false),
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
			field_SMTP_Message(0),
			IsSMTP_MessageNull(true),
			IsSMTP_MessagePrimaryKey(false),
			IsSMTP_MessageDirty(false),
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
			IsUser_ClientNull(true),
			IsUser_ClientPrimaryKey(false),
			IsUser_ClientDirty(false),
			IsUser_PasswordNull(true),
			IsUser_PasswordPrimaryKey(false),
			IsUser_PasswordDirty(false),
			IsUser_IDNull(true),
			IsUser_IDPrimaryKey(false),
			IsUser_IDDirty(false),
			IsDeveloper_PasswordNull(true),
			IsDeveloper_PasswordPrimaryKey(false),
			IsDeveloper_PasswordDirty(false),
			IsDeveloper_ClientNull(true),
			IsDeveloper_ClientPrimaryKey(false),
			IsDeveloper_ClientDirty(false),
			IsDeveloper_IDNull(true),
			IsDeveloper_IDPrimaryKey(false),
			IsDeveloper_IDDirty(false),
			IsSystem_NumberNull(true),
			IsSystem_NumberPrimaryKey(false),
			IsSystem_NumberDirty(false),
			IsApp_ServerNull(true),
			IsApp_ServerPrimaryKey(false),
			IsApp_ServerDirty(false),
			IsLangNull(true),
			IsLangPrimaryKey(false),
			IsLangDirty(false),
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
			field_Target_Target_ID(0),
			IsTarget_Target_IDNull(true),
			IsTarget_Target_IDPrimaryKey(false),
			IsTarget_Target_IDDirty(false),
			IsConnectStringNull(true),
			IsConnectStringPrimaryKey(false),
			IsConnectStringDirty(false),
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
			field_HttpVerb(2),
			IsHttpVerbNull(true),
			IsHttpVerbPrimaryKey(false),
			IsHttpVerbDirty(false)
			  
                          
                                 
                            
                       
                              
                        
     
	{
		  
                            
                             
                           
                            
                              
                              
                                
                         
                          
                        
                         
                           
                           
                             
    
	}

	void Copy(const TargetsTabRow& r)
	{
		field_Target_ID = r.field_Target_ID;
		IsTarget_IDNull = r.IsTarget_IDNull;
		IsTarget_IDDirty = r.IsTarget_IDDirty;

		field_Target_Name = r.field_Target_Name;
		IsTarget_NameNull = r.IsTarget_NameNull;
		IsTarget_NameDirty = r.IsTarget_NameDirty;

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

		field_SMTP_Message = r.field_SMTP_Message;
		IsSMTP_MessageNull = r.IsSMTP_MessageNull;
		IsSMTP_MessageDirty = r.IsSMTP_MessageDirty;

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

		field_User_Client = r.field_User_Client;
		IsUser_ClientNull = r.IsUser_ClientNull;
		IsUser_ClientDirty = r.IsUser_ClientDirty;

		field_User_Password = r.field_User_Password;
		IsUser_PasswordNull = r.IsUser_PasswordNull;
		IsUser_PasswordDirty = r.IsUser_PasswordDirty;

		field_User_ID = r.field_User_ID;
		IsUser_IDNull = r.IsUser_IDNull;
		IsUser_IDDirty = r.IsUser_IDDirty;

		field_Developer_Password = r.field_Developer_Password;
		IsDeveloper_PasswordNull = r.IsDeveloper_PasswordNull;
		IsDeveloper_PasswordDirty = r.IsDeveloper_PasswordDirty;

		field_Developer_Client = r.field_Developer_Client;
		IsDeveloper_ClientNull = r.IsDeveloper_ClientNull;
		IsDeveloper_ClientDirty = r.IsDeveloper_ClientDirty;

		field_Developer_ID = r.field_Developer_ID;
		IsDeveloper_IDNull = r.IsDeveloper_IDNull;
		IsDeveloper_IDDirty = r.IsDeveloper_IDDirty;

		field_System_Number = r.field_System_Number;
		IsSystem_NumberNull = r.IsSystem_NumberNull;
		IsSystem_NumberDirty = r.IsSystem_NumberDirty;

		field_App_Server = r.field_App_Server;
		IsApp_ServerNull = r.IsApp_ServerNull;
		IsApp_ServerDirty = r.IsApp_ServerDirty;

		field_Lang = r.field_Lang;
		IsLangNull = r.IsLangNull;
		IsLangDirty = r.IsLangDirty;

		field_Port = r.field_Port;
		IsPortNull = r.IsPortNull;
		IsPortDirty = r.IsPortDirty;

		field_Passive_Mode = r.field_Passive_Mode;
		IsPassive_ModeNull = r.IsPassive_ModeNull;
		IsPassive_ModeDirty = r.IsPassive_ModeDirty;

		field_DBName = r.field_DBName;
		IsDBNameNull = r.IsDBNameNull;
		IsDBNameDirty = r.IsDBNameDirty;

		field_Target_Target_ID = r.field_Target_Target_ID;
		IsTarget_Target_IDNull = r.IsTarget_Target_IDNull;
		IsTarget_Target_IDDirty = r.IsTarget_Target_IDDirty;

		field_ConnectString = r.field_ConnectString;
		IsConnectStringNull = r.IsConnectStringNull;
		IsConnectStringDirty = r.IsConnectStringDirty;

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

		  
                                        
                                        
                                          

                                  
                                  
                                    
    
	}

	void CopyPKs(const TargetsTabRow& r)
	{
		field_Target_ID = r.field_Target_ID;
		IsTarget_IDNull = r.IsTarget_IDNull;
		IsTarget_IDDirty = r.IsTarget_IDDirty;

	}

	void SetDirty(bool b)
	{
		IsTarget_IDDirty = b;
		IsTarget_NameDirty = b;
		IsSource_Type_IDDirty = b;
		IsDocument_IDDirty = b;
		IsLocatorDirty = b;
		IsPathDirty = b;
		IsFile_NameDirty = b;
		IsTimeoutDirty = b;
		IsLoginDirty = b;
		IsPasswordDirty = b;
		IsSMTP_MessageDirty = b;
		IsDSNDirty = b;
		IsTransfer_TypeDirty = b;
		IsEncrypt_textDirty = b;
		IsEncrypt_choiceDirty = b;
		IsDriverDirty = b;
		IsDriverTypeDirty = b;
		IsUser_ClientDirty = b;
		IsUser_PasswordDirty = b;
		IsUser_IDDirty = b;
		IsDeveloper_PasswordDirty = b;
		IsDeveloper_ClientDirty = b;
		IsDeveloper_IDDirty = b;
		IsSystem_NumberDirty = b;
		IsApp_ServerDirty = b;
		IsLangDirty = b;
		IsPortDirty = b;
		IsPassive_ModeDirty = b;
		IsDBNameDirty = b;
		IsTarget_Target_IDDirty = b;
		IsConnectStringDirty = b;
		IsSecuredDirty = b;
		IsHTTPURLDirty = b;
		IsCertIFicateNameDirty = b;
		IsOptional_ConnectStringDirty = b;
		IsODBC_UNC_UserIDDirty = b;
		IsODBC_UNC_PasswordDirty = b;
		IsHttpVerbDirty = b;
                          
                       
	}

	bool IsDirty() const
	{
		return IsTarget_IDDirty ||
			IsTarget_NameDirty ||
			IsSource_Type_IDDirty ||
			IsDocument_IDDirty ||
			IsLocatorDirty ||
			IsPathDirty ||
			IsFile_NameDirty ||
			IsTimeoutDirty ||
			IsLoginDirty ||
			IsPasswordDirty ||
			IsSMTP_MessageDirty ||
			IsDSNDirty ||
			IsTransfer_TypeDirty ||
			IsEncrypt_textDirty ||
			IsEncrypt_choiceDirty ||
			IsDriverDirty ||
			IsDriverTypeDirty ||
			IsUser_ClientDirty ||
			IsUser_PasswordDirty ||
			IsUser_IDDirty ||
			IsDeveloper_PasswordDirty ||
			IsDeveloper_ClientDirty ||
			IsDeveloper_IDDirty ||
			IsSystem_NumberDirty ||
			IsApp_ServerDirty ||
			IsLangDirty ||
			IsPortDirty ||
			IsPassive_ModeDirty ||
			IsDBNameDirty ||
			IsTarget_Target_IDDirty ||
			IsConnectStringDirty ||
			IsSecuredDirty ||
			IsHTTPURLDirty ||
			IsCertIFicateNameDirty ||
			IsOptional_ConnectStringDirty ||
			IsODBC_UNC_UserIDDirty ||
			IsODBC_UNC_PasswordDirty ||
			IsHttpVerbDirty;
                         
                    
	}

	int field_Target_ID;
	bool IsTarget_IDNull;
	bool IsTarget_IDDirty;

	std::string field_Target_Name;
	bool IsTarget_NameNull;
	bool IsTarget_NameDirty;

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

	int field_SMTP_Message;
	bool IsSMTP_MessageNull;
	bool IsSMTP_MessageDirty;

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

	std::string field_User_Client;
	bool IsUser_ClientNull;
	bool IsUser_ClientDirty;

	std::string field_User_Password;
	bool IsUser_PasswordNull;
	bool IsUser_PasswordDirty;

	std::string field_User_ID;
	bool IsUser_IDNull;
	bool IsUser_IDDirty;

	std::string field_Developer_Password;
	bool IsDeveloper_PasswordNull;
	bool IsDeveloper_PasswordDirty;

	std::string field_Developer_Client;
	bool IsDeveloper_ClientNull;
	bool IsDeveloper_ClientDirty;

	std::string field_Developer_ID;
	bool IsDeveloper_IDNull;
	bool IsDeveloper_IDDirty;

	std::string field_System_Number;
	bool IsSystem_NumberNull;
	bool IsSystem_NumberDirty;

	std::string field_App_Server;
	bool IsApp_ServerNull;
	bool IsApp_ServerDirty;

	std::string field_Lang;
	bool IsLangNull;
	bool IsLangDirty;

	int field_Port;
	bool IsPortNull;
	bool IsPortDirty;

	int field_Passive_Mode;
	bool IsPassive_ModeNull;
	bool IsPassive_ModeDirty;

	std::string field_DBName;
	bool IsDBNameNull;
	bool IsDBNameDirty;

	int field_Target_Target_ID;
	bool IsTarget_Target_IDNull;
	bool IsTarget_Target_IDDirty;

	std::string field_ConnectString;
	bool IsConnectStringNull;
	bool IsConnectStringDirty;

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

	  
                                   
                       
                        

                                
                    
                     
   

private:
	bool IsTarget_IDPrimaryKey;
	bool IsTarget_NamePrimaryKey;
	bool IsSource_Type_IDPrimaryKey;
	bool IsDocument_IDPrimaryKey;
	bool IsLocatorPrimaryKey;
	bool IsPathPrimaryKey;
	bool IsFile_NamePrimaryKey;
	bool IsTimeoutPrimaryKey;
	bool IsLoginPrimaryKey;
	bool IsPasswordPrimaryKey;
	bool IsSMTP_MessagePrimaryKey;
	bool IsDSNPrimaryKey;
	bool IsTransfer_TypePrimaryKey;
	bool IsEncrypt_textPrimaryKey;
	bool IsEncrypt_choicePrimaryKey;
	bool IsDriverPrimaryKey;
	bool IsDriverTypePrimaryKey;
	bool IsUser_ClientPrimaryKey;
	bool IsUser_PasswordPrimaryKey;
	bool IsUser_IDPrimaryKey;
	bool IsDeveloper_PasswordPrimaryKey;
	bool IsDeveloper_ClientPrimaryKey;
	bool IsDeveloper_IDPrimaryKey;
	bool IsSystem_NumberPrimaryKey;
	bool IsApp_ServerPrimaryKey;
	bool IsLangPrimaryKey;
	bool IsPortPrimaryKey;
	bool IsPassive_ModePrimaryKey;
	bool IsDBNamePrimaryKey;
	bool IsTarget_Target_IDPrimaryKey;
	bool IsConnectStringPrimaryKey;
	bool IsSecuredPrimaryKey;
	bool IsHTTPURLPrimaryKey;
	bool IsCertIFicateNamePrimaryKey;
	bool IsOptional_ConnectStringPrimaryKey;
	bool IsODBC_UNC_UserIDPrimaryKey;
	bool IsODBC_UNC_PasswordPrimaryKey;
	bool IsHttpVerbPrimaryKey;
                               
                            

};


class TargetsTab : public konga::DbRecordSet {
public:
	TargetsTab() {}

	static TargetsTab* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "TargetsTab";
	}

	void DoFieldExchange(const TargetsTabRow& row) {
		m_row.Copy(row);
	}

	void Edit();

	std::string GetUpdateFilter() {
		return m_stUpdateFilter;
	}

	int GetTarget_ID() {
		if( m_row.IsTarget_IDNull )
			throw konga::DbNullException("Target_ID");
		return m_row.field_Target_ID;
	}
	void SetTarget_ID(int i) {
		m_row.field_Target_ID = i;
		m_row.IsTarget_IDNull = false;
		m_row.IsTarget_IDDirty = true;
	}
	bool IsTarget_IDNull() const {
		return m_row.IsTarget_IDNull;
	}
	void SetTarget_IDToNull() {
		m_row.IsTarget_IDNull = true;
		m_row.IsTarget_IDDirty = true;
	}

	std::string GetTarget_Name() {
		if( m_row.IsTarget_NameNull )
			throw konga::DbNullException("Target_Name");
		return m_row.field_Target_Name;
	}
	void SetTarget_Name(const std::string& st) {
		m_row.field_Target_Name = st;
		m_row.IsTarget_NameNull = false;
		m_row.IsTarget_NameDirty = true;
	}
	bool IsTarget_NameNull() const {
		return m_row.IsTarget_NameNull;
	}
	void SetTarget_NameToNull() {
		m_row.field_Target_Name = "";
		m_row.IsTarget_NameNull = true;
		m_row.IsTarget_NameDirty = true;
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

	int GetSMTP_Message() {
		if( m_row.IsSMTP_MessageNull )
			throw konga::DbNullException("SMTP_Message");
		return m_row.field_SMTP_Message;
	}
	void SetSMTP_Message(int i) {
		m_row.field_SMTP_Message = i;
		m_row.IsSMTP_MessageNull = false;
		m_row.IsSMTP_MessageDirty = true;
	}
	bool IsSMTP_MessageNull() const {
		return m_row.IsSMTP_MessageNull;
	}
	void SetSMTP_MessageToNull() {
		m_row.IsSMTP_MessageNull = true;
		m_row.IsSMTP_MessageDirty = true;
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

	std::string GetUser_Client() {
		if( m_row.IsUser_ClientNull )
			throw konga::DbNullException("User_Client");
		return m_row.field_User_Client;
	}
	void SetUser_Client(const std::string& st) {
		m_row.field_User_Client = st;
		m_row.IsUser_ClientNull = false;
		m_row.IsUser_ClientDirty = true;
	}
	bool IsUser_ClientNull() const {
		return m_row.IsUser_ClientNull;
	}
	void SetUser_ClientToNull() {
		m_row.field_User_Client = "";
		m_row.IsUser_ClientNull = true;
		m_row.IsUser_ClientDirty = true;
	}

	std::string GetUser_Password() {
		if( m_row.IsUser_PasswordNull )
			throw konga::DbNullException("User_Password");
		return m_row.field_User_Password;
	}
	void SetUser_Password(const std::string& st) {
		m_row.field_User_Password = st;
		m_row.IsUser_PasswordNull = false;
		m_row.IsUser_PasswordDirty = true;
	}
	bool IsUser_PasswordNull() const {
		return m_row.IsUser_PasswordNull;
	}
	void SetUser_PasswordToNull() {
		m_row.field_User_Password = "";
		m_row.IsUser_PasswordNull = true;
		m_row.IsUser_PasswordDirty = true;
	}

	std::string GetUser_ID() {
		if( m_row.IsUser_IDNull )
			throw konga::DbNullException("User_ID");
		return m_row.field_User_ID;
	}
	void SetUser_ID(const std::string& st) {
		m_row.field_User_ID = st;
		m_row.IsUser_IDNull = false;
		m_row.IsUser_IDDirty = true;
	}
	bool IsUser_IDNull() const {
		return m_row.IsUser_IDNull;
	}
	void SetUser_IDToNull() {
		m_row.field_User_ID = "";
		m_row.IsUser_IDNull = true;
		m_row.IsUser_IDDirty = true;
	}

	std::string GetDeveloper_Password() {
		if( m_row.IsDeveloper_PasswordNull )
			throw konga::DbNullException("Developer_Password");
		return m_row.field_Developer_Password;
	}
	void SetDeveloper_Password(const std::string& st) {
		m_row.field_Developer_Password = st;
		m_row.IsDeveloper_PasswordNull = false;
		m_row.IsDeveloper_PasswordDirty = true;
	}
	bool IsDeveloper_PasswordNull() const {
		return m_row.IsDeveloper_PasswordNull;
	}
	void SetDeveloper_PasswordToNull() {
		m_row.field_Developer_Password = "";
		m_row.IsDeveloper_PasswordNull = true;
		m_row.IsDeveloper_PasswordDirty = true;
	}

	std::string GetDeveloper_Client() {
		if( m_row.IsDeveloper_ClientNull )
			throw konga::DbNullException("Developer_Client");
		return m_row.field_Developer_Client;
	}
	void SetDeveloper_Client(const std::string& st) {
		m_row.field_Developer_Client = st;
		m_row.IsDeveloper_ClientNull = false;
		m_row.IsDeveloper_ClientDirty = true;
	}
	bool IsDeveloper_ClientNull() const {
		return m_row.IsDeveloper_ClientNull;
	}
	void SetDeveloper_ClientToNull() {
		m_row.field_Developer_Client = "";
		m_row.IsDeveloper_ClientNull = true;
		m_row.IsDeveloper_ClientDirty = true;
	}

	std::string GetDeveloper_ID() {
		if( m_row.IsDeveloper_IDNull )
			throw konga::DbNullException("Developer_ID");
		return m_row.field_Developer_ID;
	}
	void SetDeveloper_ID(const std::string& st) {
		m_row.field_Developer_ID = st;
		m_row.IsDeveloper_IDNull = false;
		m_row.IsDeveloper_IDDirty = true;
	}
	bool IsDeveloper_IDNull() const {
		return m_row.IsDeveloper_IDNull;
	}
	void SetDeveloper_IDToNull() {
		m_row.field_Developer_ID = "";
		m_row.IsDeveloper_IDNull = true;
		m_row.IsDeveloper_IDDirty = true;
	}

	std::string GetSystem_Number() {
		if( m_row.IsSystem_NumberNull )
			throw konga::DbNullException("System_Number");
		return m_row.field_System_Number;
	}
	void SetSystem_Number(const std::string& st) {
		m_row.field_System_Number = st;
		m_row.IsSystem_NumberNull = false;
		m_row.IsSystem_NumberDirty = true;
	}
	bool IsSystem_NumberNull() const {
		return m_row.IsSystem_NumberNull;
	}
	void SetSystem_NumberToNull() {
		m_row.field_System_Number = "";
		m_row.IsSystem_NumberNull = true;
		m_row.IsSystem_NumberDirty = true;
	}

	std::string GetApp_Server() {
		if( m_row.IsApp_ServerNull )
			throw konga::DbNullException("App_Server");
		return m_row.field_App_Server;
	}
	void SetApp_Server(const std::string& st) {
		m_row.field_App_Server = st;
		m_row.IsApp_ServerNull = false;
		m_row.IsApp_ServerDirty = true;
	}
	bool IsApp_ServerNull() const {
		return m_row.IsApp_ServerNull;
	}
	void SetApp_ServerToNull() {
		m_row.field_App_Server = "";
		m_row.IsApp_ServerNull = true;
		m_row.IsApp_ServerDirty = true;
	}

	std::string GetLang() {
		if( m_row.IsLangNull )
			throw konga::DbNullException("Lang");
		return m_row.field_Lang;
	}
	void SetLang(const std::string& st) {
		m_row.field_Lang = st;
		m_row.IsLangNull = false;
		m_row.IsLangDirty = true;
	}
	bool IsLangNull() const {
		return m_row.IsLangNull;
	}
	void SetLangToNull() {
		m_row.field_Lang = "";
		m_row.IsLangNull = true;
		m_row.IsLangDirty = true;
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

	int GetTarget_Target_ID() {
		if( m_row.IsTarget_Target_IDNull )
			throw konga::DbNullException("Target_Target_ID");
		return m_row.field_Target_Target_ID;
	}
	void SetTarget_Target_ID(int i) {
		m_row.field_Target_Target_ID = i;
		m_row.IsTarget_Target_IDNull = false;
		m_row.IsTarget_Target_IDDirty = true;
	}
	bool IsTarget_Target_IDNull() const {
		return m_row.IsTarget_Target_IDNull;
	}
	void SetTarget_Target_IDToNull() {
		m_row.IsTarget_Target_IDNull = true;
		m_row.IsTarget_Target_IDDirty = true;
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
	void SetHttpVerbToNull() {
		m_row.IsHttpVerbNull = true;
		m_row.IsHttpVerbDirty = true;
	}

	  
                                   
                              
                                              
                                
  
                                                 
                              
                                 
                                 
  
                                
                                
  
                             
                                
                                 
  

                                
                           
                                           
                             
  
                                              
                           
                              
                              
  
                             
                             
  
                          
                             
                              
  
   

protected:
	TargetsTabRow m_row;

private:
	bool IsTarget_IDDirty() const {
		return m_row.IsTarget_IDDirty;
	};
	bool IsTarget_NameDirty() const {
		return m_row.IsTarget_NameDirty;
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
	bool IsSMTP_MessageDirty() const {
		return m_row.IsSMTP_MessageDirty;
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
	};
	bool IsUser_ClientDirty() const {
		return m_row.IsUser_ClientDirty;
	};
	bool IsUser_PasswordDirty() const {
		return m_row.IsUser_PasswordDirty;
	};
	bool IsUser_IDDirty() const {
		return m_row.IsUser_IDDirty;
	};
	bool IsDeveloper_PasswordDirty() const {
		return m_row.IsDeveloper_PasswordDirty;
	};
	bool IsDeveloper_ClientDirty() const {
		return m_row.IsDeveloper_ClientDirty;
	};
	bool IsDeveloper_IDDirty() const {
		return m_row.IsDeveloper_IDDirty;
	};
	bool IsSystem_NumberDirty() const {
		return m_row.IsSystem_NumberDirty;
	};
	bool IsApp_ServerDirty() const {
		return m_row.IsApp_ServerDirty;
	};
	bool IsLangDirty() const {
		return m_row.IsLangDirty;
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
	bool IsTarget_Target_IDDirty() const {
		return m_row.IsTarget_Target_IDDirty;
	};
	bool IsConnectStringDirty() const {
		return m_row.IsConnectStringDirty;
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
	  
                                 
                                 
   
                              
                              
   
   

	std::string m_stUpdateFilter;

};

}                         


#endif
