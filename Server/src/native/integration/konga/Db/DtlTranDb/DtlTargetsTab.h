/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRTRANDB_TARGETSTAB_H
#define KONGA_DTL_CRTRANDB_TARGETSTAB_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/TranDb/TargetsTab.h>

namespace dtl {

template<> class DefaultSelValidate<TranDb::TargetsTabRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::TargetsTabRow &r)
	{
		r.IsTarget_IDNull = cols["Target_ID"].IsNull();
		r.IsTarget_NameNull = cols["Target_Name"].IsNull();
		r.IsSource_Type_IDNull = cols["Source_Type_ID"].IsNull();
		r.IsDocument_IDNull = cols["Document_ID"].IsNull();
		r.IsLocatorNull = cols["Locator"].IsNull();
		r.IsPathNull = cols["Path"].IsNull();
		r.IsFile_NameNull = cols["File_Name"].IsNull();
		r.IsTimeoutNull = cols["Timeout"].IsNull();
		r.IsLoginNull = cols["Login"].IsNull();
		r.IsPasswordNull = cols["Password"].IsNull();
		r.IsSMTP_MessageNull = cols["SMTP_Message"].IsNull();
		r.IsDSNNull = cols["DSN"].IsNull();
		r.IsTransfer_TypeNull = cols["Transfer_Type"].IsNull();
		r.IsEncrypt_textNull = cols["Encrypt_text"].IsNull();
		r.IsEncrypt_choiceNull = cols["Encrypt_choice"].IsNull();
		r.IsDriverNull = cols["Driver"].IsNull();
		r.IsDriverTypeNull = cols["DriverType"].IsNull();
		r.IsUser_ClientNull = cols["User_Client"].IsNull();
		r.IsUser_PasswordNull = cols["User_Password"].IsNull();
		r.IsUser_IDNull = cols["User_ID"].IsNull();
		r.IsDeveloper_PasswordNull = cols["Developer_Password"].IsNull();
		r.IsDeveloper_ClientNull = cols["Developer_Client"].IsNull();
		r.IsDeveloper_IDNull = cols["Developer_ID"].IsNull();
		r.IsSystem_NumberNull = cols["System_Number"].IsNull();
		r.IsApp_ServerNull = cols["App_Server"].IsNull();
		r.IsLangNull = cols["Lang"].IsNull();
		r.IsPortNull = cols["Port"].IsNull();
		r.IsPassive_ModeNull = cols["Passive_Mode"].IsNull();
		r.IsDBNameNull = cols["DBName"].IsNull();
		r.IsTarget_Target_IDNull = cols["Target_Target_ID"].IsNull();
		r.IsConnectStringNull = cols["ConnectString"].IsNull();
		r.IsSecuredNull = cols["Secured"].IsNull();
		r.IsHTTPURLNull = cols["HTTPURL"].IsNull();
		r.IsCertIFicateNameNull = cols["CertIFicateName"].IsNull();
		r.IsOptional_ConnectStringNull = cols["Optional_ConnectString"].IsNull();
		r.IsODBC_UNC_UserIDNull = cols["ODBC_UNC_UserID"].IsNull();
		r.IsODBC_UNC_PasswordNull = cols["ODBC_UNC_Password"].IsNull();
		r.IsHttpVerbNull = cols["Http_Verb"].IsNull();
                                                     
                                               
		  
                                        
                       
    
		return true;
	}
};

template<> class DefaultInsValidate<TranDb::TargetsTabRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::TargetsTabRow &r)
	{
		if( r.IsTarget_IDNull )
			cols["Target_ID"].SetNull();
		else
			cols["Target_ID"].ClearNull();
		if( r.IsTarget_NameNull )
			cols["Target_Name"].SetNull();
		else
			cols["Target_Name"].ClearNull();
		if( r.IsSource_Type_IDNull )
			cols["Source_Type_ID"].SetNull();
		else
			cols["Source_Type_ID"].ClearNull();
		if( r.IsDocument_IDNull )
			cols["Document_ID"].SetNull();
		else
			cols["Document_ID"].ClearNull();
		if( r.IsLocatorNull )
			cols["Locator"].SetNull();
		else
			cols["Locator"].ClearNull();
		if( r.IsPathNull )
			cols["Path"].SetNull();
		else
			cols["Path"].ClearNull();
		if( r.IsFile_NameNull )
			cols["File_Name"].SetNull();
		else
			cols["File_Name"].ClearNull();
		if( r.IsTimeoutNull )
			cols["Timeout"].SetNull();
		else
			cols["Timeout"].ClearNull();
		if( r.IsLoginNull )
			cols["Login"].SetNull();
		else
			cols["Login"].ClearNull();
		if( r.IsPasswordNull )
			cols["Password"].SetNull();
		else
			cols["Password"].ClearNull();
		if( r.IsSMTP_MessageNull )
			cols["SMTP_Message"].SetNull();
		else
			cols["SMTP_Message"].ClearNull();
		if( r.IsDSNNull )
			cols["DSN"].SetNull();
		else
			cols["DSN"].ClearNull();
		if( r.IsTransfer_TypeNull )
			cols["Transfer_Type"].SetNull();
		else
			cols["Transfer_Type"].ClearNull();
		if( r.IsEncrypt_textNull )
			cols["Encrypt_text"].SetNull();
		else
			cols["Encrypt_text"].ClearNull();
		if( r.IsEncrypt_choiceNull )
			cols["Encrypt_choice"].SetNull();
		else
			cols["Encrypt_choice"].ClearNull();
		if( r.IsDriverNull )
			cols["Driver"].SetNull();
		else
			cols["Driver"].ClearNull();
		if( r.IsDriverTypeNull )
			cols["DriverType"].SetNull();
		else
			cols["DriverType"].ClearNull();
		if( r.IsUser_ClientNull )
			cols["User_Client"].SetNull();
		else
			cols["User_Client"].ClearNull();
		if( r.IsUser_PasswordNull )
			cols["User_Password"].SetNull();
		else
			cols["User_Password"].ClearNull();
		if( r.IsUser_IDNull )
			cols["User_ID"].SetNull();
		else
			cols["User_ID"].ClearNull();
		if( r.IsDeveloper_PasswordNull )
			cols["Developer_Password"].SetNull();
		else
			cols["Developer_Password"].ClearNull();
		if( r.IsDeveloper_ClientNull )
			cols["Developer_Client"].SetNull();
		else
			cols["Developer_Client"].ClearNull();
		if( r.IsDeveloper_IDNull )
			cols["Developer_ID"].SetNull();
		else
			cols["Developer_ID"].ClearNull();
		if( r.IsSystem_NumberNull )
			cols["System_Number"].SetNull();
		else
			cols["System_Number"].ClearNull();
		if( r.IsApp_ServerNull )
			cols["App_Server"].SetNull();
		else
			cols["App_Server"].ClearNull();
		if( r.IsLangNull )
			cols["Lang"].SetNull();
		else
			cols["Lang"].ClearNull();
		if( r.IsPortNull )
			cols["Port"].SetNull();
		else
			cols["Port"].ClearNull();
		if( r.IsPassive_ModeNull )
			cols["Passive_Mode"].SetNull();
		else
			cols["Passive_Mode"].ClearNull();
		if( r.IsDBNameNull )
			cols["DBName"].SetNull();
		else
			cols["DBName"].ClearNull();
		if( r.IsTarget_Target_IDNull )
			cols["Target_Target_ID"].SetNull();
		else
			cols["Target_Target_ID"].ClearNull();
		if( r.IsConnectStringNull )
			cols["ConnectString"].SetNull();
		else
			cols["ConnectString"].ClearNull();
		if( r.IsSecuredNull )
			cols["Secured"].SetNull();
		else
			cols["Secured"].ClearNull();
		if( r.IsHTTPURLNull )
			cols["HTTPURL"].SetNull();
		else
			cols["HTTPURL"].ClearNull();
		if( r.IsCertIFicateNameNull )
			cols["CertIFicateName"].SetNull();
		else
			cols["CertIFicateName"].ClearNull();
		if( r.IsOptional_ConnectStringNull )
			cols["Optional_ConnectString"].SetNull();
		else
			cols["Optional_ConnectString"].ClearNull();
		if( r.IsODBC_UNC_UserIDNull )
			cols["ODBC_UNC_UserID"].SetNull();
		else
			cols["ODBC_UNC_UserID"].ClearNull();
		if( r.IsODBC_UNC_PasswordNull )
			cols["ODBC_UNC_Password"].SetNull();
		else
			cols["ODBC_UNC_Password"].ClearNull();
		if( r.IsHttpVerbNull )
			cols["Http_Verb"].SetNull();
		else
			cols["Http_Verb"].ClearNull();
		  
                          
                                
      
                                  
                       
                             
      
                               
     
		  
                                        
                       
    
		return true;
	}
};

template<> class DefaultBCA<TranDb::TargetsTabRow>
{
public:
	void operator()(BoundIOs &cols, TranDb::TargetsTabRow &rowbuf)
	{
		cols["Target_ID"] == rowbuf.field_Target_ID;
		cols["Target_Name"] == rowbuf.field_Target_Name;
		cols["Source_Type_ID"] == rowbuf.field_Source_Type_ID;
		cols["Document_ID"] == rowbuf.field_Document_ID;
		cols["Locator"] == rowbuf.field_Locator;
		cols["Path"] == rowbuf.field_Path;
		cols["File_Name"] == rowbuf.field_File_Name;
		cols["Timeout"] == rowbuf.field_Timeout;
		cols["Login"] == rowbuf.field_Login;
		cols["Password"] == rowbuf.field_Password;
		cols["SMTP_Message"] == rowbuf.field_SMTP_Message;
		cols["DSN"] == rowbuf.field_DSN;
		cols["Transfer_Type"] == rowbuf.field_Transfer_Type;
		cols["Encrypt_text"] == rowbuf.field_Encrypt_text;
		cols["Encrypt_choice"] == rowbuf.field_Encrypt_choice;
		cols["Driver"] == rowbuf.field_Driver;
		cols["DriverType"] == rowbuf.field_DriverType;
		cols["User_Client"] == rowbuf.field_User_Client;
		cols["User_Password"] == rowbuf.field_User_Password;
		cols["User_ID"] == rowbuf.field_User_ID;
		cols["Developer_Password"] == rowbuf.field_Developer_Password;
		cols["Developer_Client"] == rowbuf.field_Developer_Client;
		cols["Developer_ID"] == rowbuf.field_Developer_ID;
		cols["System_Number"] == rowbuf.field_System_Number;
		cols["App_Server"] == rowbuf.field_App_Server;
		cols["Lang"] == rowbuf.field_Lang;
		cols["Port"] == rowbuf.field_Port;
		cols["Passive_Mode"] == rowbuf.field_Passive_Mode;
		cols["DBName"] == rowbuf.field_DBName;
		cols["Target_Target_ID"] == rowbuf.field_Target_Target_ID;
		cols["ConnectString"] == rowbuf.field_ConnectString;
		cols["Secured"] == rowbuf.field_Secured;
		cols["HTTPURL"] == rowbuf.field_HTTPURL;
		cols["CertIFicateName"] == rowbuf.field_CertIFicateName;
		cols["Optional_ConnectString"] == rowbuf.field_Optional_ConnectString;
		cols["ODBC_UNC_UserID"] == rowbuf.field_ODBC_UNC_UserID;
		cols["ODBC_UNC_Password"] == rowbuf.field_ODBC_UNC_Password;
		cols["Http_Verb"] == rowbuf.field_HttpVerb;
                                                  
                                            
		  
                                        
                       
    
	}
};


}                     



namespace dtl_trandb {

class DtlTargetsTabDeleteBinder : public CustomBinder<TranDb::TargetsTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		TranDb::TargetsTabRow& rowbuf = *m_pRow;
		cols["Target_ID"] == rowbuf.field_Target_ID;
	}
};

class DtlTargetsTab : public TranDb::TargetsTab {

public:
	DtlTargetsTab(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlTargetsTab() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		TranDb::TargetsTab::Edit();
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
	DtlTargetsTabDeleteBinder m_deleteBinder;
	DtlDbRecordSet<TranDb::TargetsTab, TranDb::TargetsTabRow> m_dtlRecSet;

};


}                            


#endif

