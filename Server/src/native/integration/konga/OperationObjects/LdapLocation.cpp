/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#include <konga/Ldap/konga_ldap.h>

#include "LdapLocation.h"

using namespace konga;
using std::string;

void LdapLocation::testConnection() const {
	Ldap ldap;

	const string& sServer = getServer();
	const string& sUid = getUid();
	const string& sDecryptedPassword = getDecryptedPassword();
	const int iReferralMode = getReferralMode();
	const int iConnectionMode = getConnectionMode();
	const long nPort = getPort();
	ldap.Connect(sServer, sUid, sDecryptedPassword, 
		iConnectionMode, nPort, static_cast<INT_PTR>(iReferralMode) == reinterpret_cast<INT_PTR>(LDAP_OPT_ON));
}
