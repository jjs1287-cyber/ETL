/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */

#include <string>
#include "DBConnectionInfo.h"	                         

#ifdef WIN32

#import "DBInfo.exe" no_namespace

  
                                                
  
void LoadPDBConnectionInfo(DBConnectionInfo& db_connect, const ICRDBInfoPtr& pDBInfo, variant_t varServer)
{
	BSTR dsnName=0, uid=0, pwd=0, dbServer=0, catalog=0, oledbProvider=0;
	pDBInfo->GetProcessDBConnectionInfo(&dsnName, &uid, &pwd, &dbServer, &catalog, &oledbProvider, varServer);
	db_connect.setInfo(dsnName?(LPCTSTR)_bstr_t(dsnName):"", uid?(LPCTSTR)_bstr_t(uid):"", pwd?(LPCTSTR)_bstr_t(pwd):"",
		dbServer?(LPCTSTR)_bstr_t(dbServer):"", catalog?(LPCTSTR)_bstr_t(catalog):"", oledbProvider?(LPCTSTR)_bstr_t(oledbProvider):"");
	::SysFreeString(dsnName); ::SysFreeString(uid); ::SysFreeString(pwd); ::SysFreeString(dbServer);
	::SysFreeString(catalog); ::SysFreeString(oledbProvider);
}

  
                                                   
  
void LoadTranDBConnectionInfo(DBConnectionInfo& db_connect, const ICRDBInfoPtr& pDBInfo, variant_t varServer)
{
	BSTR dsnName, uid, pwd, dbServer, catalog, oledbProvider;
	pDBInfo->GetTranDBConnectionInfo(&dsnName, &uid, &pwd, &dbServer, &catalog, &oledbProvider, varServer);
	db_connect.setInfo((LPCTSTR)_bstr_t(dsnName), (LPCTSTR)_bstr_t(uid), (LPCTSTR)_bstr_t(pwd),
		(LPCTSTR)_bstr_t(dbServer), (LPCTSTR)_bstr_t(catalog), (LPCTSTR)_bstr_t(oledbProvider));
	::SysFreeString(dsnName); ::SysFreeString(uid); ::SysFreeString(pwd); ::SysFreeString(dbServer);
	::SysFreeString(catalog); ::SysFreeString(oledbProvider);
}

  
                                                
  
void LoadIDBConnectionInfo(DBConnectionInfo& db_connect, const ICRDBInfoPtr& pDBInfo, variant_t varServer)
{
	BSTR dsnName, uid, pwd, dbServer, catalog, oledbProvider;
	pDBInfo->GetInstanceDBConnectionInfo(&dsnName, &uid, &pwd, &dbServer, &catalog, &oledbProvider, varServer);
	db_connect.setInfo((LPCTSTR)_bstr_t(dsnName), (LPCTSTR)_bstr_t(uid), (LPCTSTR)_bstr_t(pwd),
		(LPCTSTR)_bstr_t(dbServer), (LPCTSTR)_bstr_t(catalog), (LPCTSTR)_bstr_t(oledbProvider));
	::SysFreeString(dsnName); ::SysFreeString(uid); ::SysFreeString(pwd); ::SysFreeString(dbServer);
	::SysFreeString(catalog); ::SysFreeString(oledbProvider);
}

  
                                                  
  
void LoadMsgDBConnectionInfo(DBConnectionInfo& db_connect, const ICRDBInfoPtr& pDBInfo, variant_t varServer)
{
	BSTR dsnName, uid, pwd, dbServer, catalog, oledbProvider;
	pDBInfo->GetMessageDBConnectionInfo(&dsnName, &uid, &pwd, &dbServer, &catalog, &oledbProvider, varServer);
	db_connect.setInfo((LPCTSTR)_bstr_t(dsnName), (LPCTSTR)_bstr_t(uid), (LPCTSTR)_bstr_t(pwd),
		(LPCTSTR)_bstr_t(dbServer), (LPCTSTR)_bstr_t(catalog), (LPCTSTR)_bstr_t(oledbProvider));
	::SysFreeString(dsnName); ::SysFreeString(uid); ::SysFreeString(pwd); ::SysFreeString(dbServer);
	::SysFreeString(catalog); ::SysFreeString(oledbProvider);
}

  
                                                
  
void LoadBDBConnectionInfo(DBConnectionInfo& db_connect, const ICRDBInfoPtr& pDBInfo, variant_t varServer = "")
{
	BSTR dsnName, uid, pwd, dbServer, catalog, oledbProvider;
	pDBInfo->GetDBConnectionInfo("b2bi", &dsnName, &uid, &pwd, &dbServer, &catalog, &oledbProvider, varServer);
	db_connect.setInfo((LPCTSTR)_bstr_t(dsnName), (LPCTSTR)_bstr_t(uid), (LPCTSTR)_bstr_t(pwd),
		(LPCTSTR)_bstr_t(dbServer), (LPCTSTR)_bstr_t(catalog), (LPCTSTR)_bstr_t(oledbProvider));
	::SysFreeString(dsnName); ::SysFreeString(uid); ::SysFreeString(pwd); ::SysFreeString(dbServer);
	::SysFreeString(catalog); ::SysFreeString(oledbProvider);
}

  
                                               
  
void LoadDBConnectionInfo(DBConnectionInfo& db_connect, const ICRDBInfoPtr& pDBInfo, const std::string& stDbName, variant_t varServer = "")
{
	BSTR dsnName, uid, pwd, dbServer, catalog, oledbProvider;
	pDBInfo->GetDBConnectionInfo(stDbName.c_str(), &dsnName, &uid, &pwd, &dbServer, &catalog, &oledbProvider, varServer);
	db_connect.setInfo((LPCTSTR)_bstr_t(dsnName), (LPCTSTR)_bstr_t(uid), (LPCTSTR)_bstr_t(pwd),
		(LPCTSTR)_bstr_t(dbServer), (LPCTSTR)_bstr_t(catalog), (LPCTSTR)_bstr_t(oledbProvider));
	::SysFreeString(dsnName); ::SysFreeString(uid); ::SysFreeString(pwd); ::SysFreeString(dbServer);
	::SysFreeString(catalog); ::SysFreeString(oledbProvider);
}

void LoadRemoteDbInfo(const std::string& stMachineName, ICRDBInfoPtr& pDBInfo)
{
	USES_CONVERSION;
	COSERVERINFO serverInfo;
	serverInfo.dwReserved1 = serverInfo.dwReserved2 = 0;
	serverInfo.pAuthInfo = 0;
	serverInfo.pwszName = A2W(stMachineName.c_str());
								
	const IID IID_ICRDBInfo = {0xA24D3AC3,0xC7A7,0x11D3,{0x80,0xD2,0x00,0xC0,0x4F,0x60,0x7B,0x9C}};
	MULTI_QI multi_qi[] = { &IID_ICRDBInfo, 0, 0 } ;

	HRESULT hr = ::CoCreateInstanceEx(__uuidof(CRDBInfo),0,CLSCTX_SERVER,&serverInfo,1,multi_qi);
	if( FAILED(hr) ) throw _com_error(hr);
	if( FAILED(multi_qi[0].hr) ) throw _com_error(multi_qi[0].hr);

	pDBInfo = multi_qi[0].pItf;
}

#else

void LoadPDBConnectionInfo(DBConnectionInfo& db_connect, const std::string& stServer           ) {
}

void LoadIDBConnectionInfo(DBConnectionInfo& db_connect, const std::string& stServer           ) {
}

void LoadMsgDBConnectionInfo(DBConnectionInfo& db_connect, const std::string& stServer           ) {
}

void LoadTranDBConnectionInfo(DBConnectionInfo& db_connect, const std::string& stServer           ) {
}

void LoadBDBConnectionInfo(DBConnectionInfo& db_connect, const std::string& stServer           ) {
}

void LoadDBConnectionInfo(DBConnectionInfo& db_connect, const std::string& stDatabaseName, const std::string& stServer           ) {
}

#endif
