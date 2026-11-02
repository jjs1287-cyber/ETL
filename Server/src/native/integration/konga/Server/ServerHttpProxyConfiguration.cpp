/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#include <konga/Utils/StringUtils.h>
#include <konga/Crypto/Crypto.h>

#include "Server.h"
#include "DefaultHttpProxyConfiguration.h"
#include "ServerHttpProxyConfiguration.h"

using namespace konga;
using std::string;

ServerHttpProxyConfiguration::ServerHttpProxyConfiguration(DbConnection* pTranDbConn) : DbConnectionHolder(pTranDbConn) {
	this->SetInternalDbName("TranDb");
}

ServerHttpProxyConfiguration::~ServerHttpProxyConfiguration(void) {}

namespace {
	const string HTTP_PROXY_USAGE("http_proxy_usage");
	const string HTTP_PROXY_USAGE_YES("1");
	const string HTTP_PROXY_USAGE_NO("0");

	const string HTTP_PROXY_HOST("http_proxy_host");
	const string HTTP_PROXY_PORT("http_proxy_port");
	const long NO_PORT = -1;

	const string HTTP_PROXY_USERNAME("http_proxy_username");
	const string HTTP_PROXY_PASSWORD("http_proxy_password");
	const string HTTP_PROXY_EXCEPTIONS("http_proxy_exceptions");
}

HttpProxyConfiguration* konga::GetHttpProxyConfiguration() {
	ServerHttpProxyConfiguration config;
	return config.GetHttpProxyConfiguration();
}

HttpProxyConfiguration* ServerHttpProxyConfiguration::GetHttpProxyConfiguration() {
	const string usage( konga::GetKeyValue(HTTP_PROXY_USAGE, GetDbConnection(), HTTP_PROXY_USAGE_NO) );
	if ( usage != HTTP_PROXY_USAGE_YES )
		return new DefaultHttpProxyConfiguration(false, "", NO_PORT, "", "", "");

	const string host( konga::GetKeyValue(HTTP_PROXY_HOST, GetDbConnection()) );
	const string port( konga::GetKeyValue(HTTP_PROXY_PORT, GetDbConnection()) );
	const long lPort = port.empty() ? NO_PORT : atol(port.c_str());

	const string user( konga::GetKeyValue(HTTP_PROXY_USERNAME, GetDbConnection()) );
	const string pwd( konga::DecryptString(konga::GetKeyValue(HTTP_PROXY_PASSWORD, GetDbConnection())) );

	const string exceptions ( konga::GetKeyValue(HTTP_PROXY_EXCEPTIONS, GetDbConnection()) );

	return new DefaultHttpProxyConfiguration(true, host, lPort, user, pwd, exceptions);
}

void ServerHttpProxyConfiguration::SetHttpProxyUsage(bool bYes) {
	konga::SetKeyValue(HTTP_PROXY_USAGE, bYes? HTTP_PROXY_USAGE_YES : HTTP_PROXY_USAGE_NO, GetDbConnection());
}

void ServerHttpProxyConfiguration::SetHttpProxyHost(const string& sProxyHost) {
	konga::SetKeyValue(HTTP_PROXY_HOST, sProxyHost, GetDbConnection());
}

void ServerHttpProxyConfiguration::SetHttpProxyPort(long lProxyPort) {
	konga::SetKeyValue(HTTP_PROXY_PORT, toString(lProxyPort), GetDbConnection());
}

void ServerHttpProxyConfiguration::SetHttpProxyUserName(const string& sProxyUserName) {
	konga::SetKeyValue(HTTP_PROXY_USERNAME, sProxyUserName, GetDbConnection());
}

void ServerHttpProxyConfiguration::SetHttpProxyPassword(const string& sProxyPassword) {
	konga::SetKeyValue(HTTP_PROXY_PASSWORD, konga::EncryptString(sProxyPassword), GetDbConnection());
}

void ServerHttpProxyConfiguration::SetHttpProxyExceptions(const std::string& sProxyExceptions) {
	konga::SetKeyValue(HTTP_PROXY_EXCEPTIONS, sProxyExceptions, GetDbConnection());
}

