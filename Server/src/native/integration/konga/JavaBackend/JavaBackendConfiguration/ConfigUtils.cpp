/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#include <exception>

#include <konga/Utils/StringUtils.h>
#include <konga/Utils/CrException.h>	                        
#include <konga/Utils/Settings.h>		                                         

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>
#include <konga/Db/TranDb/UsersTab.h>

#include <konga/ItemAccess/UserInfo.h>

#include "ConfigUtils.h"

#ifdef USE_AXIS_CPP
AXIS_CPP_NAMESPACE_USE
#endif

using namespace konga;
using std::string;


string konga::GetEndpointUrlForService(const std::string& serviceName) {
	const long port = konga::GetOptionAsInt("JavaBackend", "Port", 46912);
	const string host = konga::GetOptionAsString("JavaBackend", "Host", "127.0.0.1");
	return string("http://" + host + ":" + toString(port) + "/axis/services/" + serviceName);
}

#ifdef USE_AXIS_CPP
void konga::ConfigureStub(AXIS_CPP_NAMESPACE_PREFIX Stub& stub, const string& serviceName) {
	const long port = konga::GetOptionAsInt("JavaBackend", "Port", 46912);
	const string& url = GetEndpointUrlForService(serviceName);

	try {
		stub.setEndPoint(url.c_str());
		stub.setTransportTimeout(0);
	}
	catch( std::exception& ex ) {
		throw CrException("Failed to configure Java back-end endpoint " + url + ": " + string(ex.what()));
	}
}
#endif

void konga::GetCredentials(DbConnection& dbConn, const UserInfo& userInfo, std::string& userName, std::string& md5pwd) {
	long userId = userInfo.GetId();

	using namespace TranDb;
	SmartDbPtr<UsersTab> pUsersTab( UsersTab::MakeInstance(&dbConn) );

	pUsersTab->SetWhereClause("WHERE User_ID = " + toString(userId));
	pUsersTab->Select();
	if( !pUsersTab->Next() ) {
		throw InternalErrorException("Failed to look up credentials for user '" + userInfo.GetLogin() + "'.");
	}

	if ( pUsersTab->IsPasswordNull() ) {
		throw InternalErrorException("Password for user '" + userInfo.GetLogin() + "' is NULL.");
	}
	if ( pUsersTab->IsLoginNull() ) {
		throw InternalErrorException("Login for user '" + userInfo.GetLogin() + "' is NULL.");
	}

	userName = pUsersTab->GetLogin();
	md5pwd = pUsersTab->GetPassword();
}

string konga::GetAXIS2C_HOME() {
	string home(konga::GetServerHomeWithSlash());
	home += "axis2";
	return home;
}
