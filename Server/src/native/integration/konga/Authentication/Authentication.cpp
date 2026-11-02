/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#include <iostream>
#include <string>

#include <cassert>

#include <konga/Db/DbUtils/DbUtils.h>	               
#include <konga/Db/TranDb/UsersTab.h>			                                 

#include <konga/Crypto/Crypto.h>

#include "Authentication.h"

using namespace konga;
using namespace konga::Authentication;
using std::string;

namespace {
	const int INTERNAL_ADMIN_UID = -1;
	const int ANONYMOUS_UID = -2;
}

namespace {

	bool IsValidUserImpl(const string& stUserName_in, const string& sMd5HashedPassword, int& iUserId, DbConnection& dbConnection) {
		if ( stUserName_in.empty() )
			throw CrException("No user name provided.");

		SmartDbPtr<TranDb::UsersTab> pUsersTab( TranDb::UsersTab::MakeInstance(&dbConnection) );
		const string stFilter = "WHERE UPPER(login)=UPPER('" + SqlEscape(stUserName_in) + "') AND password='" + SqlEscape(sMd5HashedPassword) + "'";

		try {
			pUsersTab->Select(stFilter);
			if ( pUsersTab->Next() ) {
				iUserId = pUsersTab->GetUser_ID();
				                                    
				return ( iUserId != ANONYMOUS_UID );
			}
			else
				return false;
		}
		catch( const DbException& e ) {
			throw InternalErrorException(e.GetPrettyErrorMessage());
		}
	}
}

bool konga::Authentication::IsValidUser(const string& stUserName_in, const string& sMd5HashedPassword, int& iUserId, DbConnection* pTranDbConnection) {
	if ( pTranDbConnection == 0 ) {
		SmartDbPtr<DbConnection> pTranDb( konga::GetBackEndDbConnection("TranDb") );
		return IsValidUserImpl(stUserName_in, sMd5HashedPassword, iUserId, *pTranDb);
	}
	else {
		return IsValidUserImpl(stUserName_in, sMd5HashedPassword, iUserId, *pTranDbConnection);
	}
}

bool konga::Authentication::IsValidUser(const string& stUserName, const string& sMd5HashedPassword) {
	int i;
	return IsValidUser(stUserName, sMd5HashedPassword, i);
}

namespace {
	UserInfo GetValidUserImpl(const string& stUserName, const string& sMd5HashedPassword, DbConnection& tranDbConnection) {
		int iUserId = 0;
		if ( !IsValidUserImpl(stUserName, sMd5HashedPassword, iUserId, tranDbConnection) )
			throw AuthenticationException("Invalid user name and/or password.", stUserName);

		return GetUserInfo(iUserId, tranDbConnection, true);
	}
}

UserInfo konga::Authentication::GetValidUser(const string& stUserName, const string& stPassword, DbConnection* pTranDbConnection_in) {
	if ( pTranDbConnection_in == 0 ) {
		SmartDbPtr<DbConnection> pTranDb( konga::GetBackEndDbConnection("TranDb") );
		return GetValidUserImpl(stUserName, stPassword, *pTranDb);
	}
	return GetValidUserImpl(stUserName, stPassword, *pTranDbConnection_in);
}
