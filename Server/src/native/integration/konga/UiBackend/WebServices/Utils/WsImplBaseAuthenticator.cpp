/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
#include <konga/Authentication/Authentication.h>

#include "WsUtils.h"
#include "UiBackendLogging.h"
#include "WsImplBaseAuthenticator.h"

using namespace konga;

WsImplBaseAuthenticator::WsImplBaseAuthenticator(const char* user, const char* password) {
	const string& stUser = toString(user);
	try {
		if ( stUser.empty() )
			throw Authentication::AuthenticationException("No user name was specified.", "unknown");

		const string& stPassword = toString(password);
		m_UserInfo = konga::Authentication::GetValidUser(stUser, stPassword);
	}
	catch( const Authentication::AuthenticationException& e ) {
		auditLog("Authentication failed for user " + e.GetUserName() + ". " + e.GetErrorString());
		throw;
	}
	catch( const CrException& e ) {
		auditLog("Failed to authenticate user " + stUser + ". " + e.GetErrorString());
		throw;
	}
}
