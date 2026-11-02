/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRTRANDB_SOURCESTAB_H
#define KONGA_DTL_CRTRANDB_SOURCESTAB_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/TranDb/SourcesTab.h>

namespace dtl {

template<> class DefaultSelValidate<TranDb::SourcesTabRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::SourcesTabRow &r)
	{
		r.IsSource_IDNull = cols["Source_ID"].IsNull();
		r.IsSource_NameNull = cols["Source_Name"].IsNull();
		r.IsSource_Type_IDNull = cols["Source_Type_ID"].IsNull();
		r.IsDocument_IDNull = cols["Document_ID"].IsNull();
		r.IsLocatorNull = cols["Locator"].IsNull();
		r.IsPathNull = cols["Path"].IsNull();
		r.IsFile_NameNull = cols["File_Name"].IsNull();
		r.IsTimeoutNull = cols["Timeout"].IsNull();
		r.IsLoginNull = cols["Login"].IsNull();
		r.IsPasswordNull = cols["Password"].IsNull();
		r.IsFilterNull = cols["Filter"].IsNull();
		r.IsIgnore_FirstNull = cols["Ignore_First"].IsNull();
		r.IsIgnore_LastNull = cols["Ignore_Last"].IsNull();
		r.IsDSNNull = cols["DSN"].IsNull();
		r.IsTransfer_TypeNull = cols["Transfer_Type"].IsNull();
		r.IsEncrypt_textNull = cols["Encrypt_text"].IsNull();
		r.IsEncrypt_choiceNull = cols["Encrypt_choice"].IsNull();
		r.IsDriverNull = cols["Driver"].IsNull();
		r.IsDriverTypeNull = cols["DriverType"].IsNull();
		r.IsDBType_IdNull = cols["DBType_Id"].IsNull();
		r.IsPortNull = cols["Port"].IsNull();
		r.IsPassive_ModeNull = cols["Passive_Mode"].IsNull();
		r.IsDBNameNull = cols["DBName"].IsNull();
		r.IsConnectStringNull = cols["ConnectString"].IsNull();
		r.IsRenameFileToNull = cols["RenameFileTo"].IsNull();
		r.IsSecuredNull = cols["Secured"].IsNull();
		r.IsHTTPURLNull = cols["HTTPURL"].IsNull();
		r.IsCertIFicateNameNull = cols["CertIFicateName"].IsNull();
		r.IsOptional_ConnectStringNull = cols["Optional_ConnectString"].IsNull();
		r.IsODBC_UNC_UserIDNull = cols["ODBC_UNC_UserID"].IsNull();
		r.IsODBC_UNC_PasswordNull = cols["ODBC_UNC_Password"].IsNull();
		r.IsHttpVerbNull = cols["Http_Verb"].IsNull();
		r.IsJms_Message_SelectorNull = cols["Jms_Message_Selector"].IsNull();

		return true;
	}
};

template<> class DefaultInsValidate<TranDb::SourcesTabRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::SourcesTabRow &r)
	{
		if( r.IsSource_IDNull )
			cols["Source_ID"].SetNull();
		else
			cols["Source_ID"].ClearNull();
		if( r.IsSource_NameNull )
			cols["Source_Name"].SetNull();
		else
			cols["Source_Name"].ClearNull();
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
		if( r.IsFilterNull )
			cols["Filter"].SetNull();
		else
			cols["Filter"].ClearNull();
		if( r.IsIgnore_FirstNull )
			cols["Ignore_First"].SetNull();
		else
			cols["Ignore_First"].ClearNull();
		if( r.IsIgnore_LastNull )
			cols["Ignore_Last"].SetNull();
		else
			cols["Ignore_Last"].ClearNull();
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
		if( r.IsDBType_IdNull )
			cols["DBType_Id"].SetNull();
		else
			cols["DBType_Id"].ClearNull();
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
		if( r.IsConnectStringNull )
			cols["ConnectString"].SetNull();
		else
			cols["ConnectString"].ClearNull();
		if( r.IsRenameFileToNull )
			cols["RenameFileTo"].SetNull();
		else
			cols["RenameFileTo"].ClearNull();
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
		if( r.IsJms_Message_SelectorNull )
			cols["Jms_Message_Selector"].SetNull();
		else
			cols["Jms_Message_Selector"].ClearNull();

		  
                                        
                       
    
		return true;
	}
};

template<> class DefaultBCA<TranDb::SourcesTabRow>
{
public:
	void operator()(BoundIOs &cols, TranDb::SourcesTabRow &rowbuf)
	{
		cols["Source_ID"] == rowbuf.field_Source_ID;
		cols["Source_Name"] == rowbuf.field_Source_Name;
		cols["Source_Type_ID"] == rowbuf.field_Source_Type_ID;
		cols["Document_ID"] == rowbuf.field_Document_ID;
		cols["Locator"] == rowbuf.field_Locator;
		cols["Path"] == rowbuf.field_Path;
		cols["File_Name"] == rowbuf.field_File_Name;
		cols["Timeout"] == rowbuf.field_Timeout;
		cols["Login"] == rowbuf.field_Login;
		cols["Password"] == rowbuf.field_Password;
		cols["Filter"] == rowbuf.field_Filter;
		cols["Ignore_First"] == rowbuf.field_Ignore_First;
		cols["Ignore_Last"] == rowbuf.field_Ignore_Last;
		cols["DSN"] == rowbuf.field_DSN;
		cols["Transfer_Type"] == rowbuf.field_Transfer_Type;
		cols["Encrypt_text"] == rowbuf.field_Encrypt_text;
		cols["Encrypt_choice"] == rowbuf.field_Encrypt_choice;
		cols["Driver"] == rowbuf.field_Driver;
		cols["DriverType"] == rowbuf.field_DriverType;
		cols["DBType_Id"] == rowbuf.field_DBType_Id;
		cols["Port"] == rowbuf.field_Port;
		cols["Passive_Mode"] == rowbuf.field_Passive_Mode;
		cols["DBName"] == rowbuf.field_DBName;
		cols["ConnectString"] == rowbuf.field_ConnectString;
		cols["RenameFileTo"] == rowbuf.field_RenameFileTo;
		cols["Secured"] == rowbuf.field_Secured;
		cols["HTTPURL"] == rowbuf.field_HTTPURL;
		cols["CertIFicateName"] == rowbuf.field_CertIFicateName;
		cols["Optional_ConnectString"] == rowbuf.field_Optional_ConnectString;
		cols["ODBC_UNC_UserID"] == rowbuf.field_ODBC_UNC_UserID;
		cols["ODBC_UNC_Password"] == rowbuf.field_ODBC_UNC_Password;
		cols["Http_Verb"] == rowbuf.field_HttpVerb;
		cols["Jms_Message_Selector"] == rowbuf.field_Jms_Message_Selector;
		  
                                        
                       
    
	}
};


}                     


namespace dtl_trandb {

class DtlSourcesTabDeleteBinder : public CustomBinder<TranDb::SourcesTabRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		TranDb::SourcesTabRow& rowbuf = *m_pRow;
		cols["Source_ID"] == rowbuf.field_Source_ID;
	}
};

class DtlSourcesTab : public TranDb::SourcesTab {

public:
	DtlSourcesTab(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlSourcesTab() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		TranDb::SourcesTab::Edit();
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
	DtlSourcesTabDeleteBinder m_deleteBinder;
	DtlDbRecordSet<TranDb::SourcesTab, TranDb::SourcesTabRow> m_dtlRecSet;

};


}                            


#endif

