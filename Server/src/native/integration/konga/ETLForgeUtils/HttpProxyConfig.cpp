/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#include <memory>

#include <konga/Utils/StringUtils.h>		           

#include <konga/Server/ServerHttpProxyConfiguration.h>

#include "HttpProxyConfig.h"

using namespace konga;
using std::string;

void konga::ETLForgeUtils::SetProxyConfiguration(bool bHost, const string& sHost,
												  bool bUser, const string& sUser,
												  bool bPwd, const string& sPwd,
												  bool bExceptions, const string& stExceptions_in)
{
	if ( !bHost && !bUser && !bPwd && !bExceptions )
		return;

	ServerHttpProxyConfiguration config;
	
	if ( bHost && !sHost.empty() ) {
		                    
		const string::size_type ipos = sHost.find(":");
		string host;
		long port = -1;
		if ( ipos == string::npos ) {
			host = sHost;
		}
		else {
			host = sHost.substr(0, ipos);
			const string sPort(sHost.substr(ipos+1));
			if ( !sPort.empty() ) {
				port = atol(sPort.c_str());
			}
		}

		config.SetHttpProxyHost(host);
		config.SetHttpProxyPort(port);
	}

	if ( bUser ) {
		config.SetHttpProxyUserName(sUser);
	}
	if ( bPwd ) {
		config.SetHttpProxyPassword(sPwd);
	}
	if ( bExceptions ) {
		string stExceptions;
		konga::ReplaceCharWithString(stExceptions_in, stExceptions, ' ', "");
		config.SetHttpProxyExceptions( stExceptions );
	}

	config.SetHttpProxyUsage(true);
}

void konga::ETLForgeUtils::EnableProxyConfiguration() {
	ServerHttpProxyConfiguration config;
	config.SetHttpProxyUsage(true);
}

void konga::ETLForgeUtils::DisableProxyConfiguration() {
	ServerHttpProxyConfiguration config;
	config.SetHttpProxyUsage(false);
}

string konga::ETLForgeUtils::GetProxyConfiguration() {
	std::auto_ptr<HttpProxyConfiguration> config( konga::GetHttpProxyConfiguration() );

	string host, user, pwd;
	long port = -1;
	const bool bIsEnabled = config->isProxyConfiguredForHost("", host, port, user, pwd);

	string info(bIsEnabled ? "Http proxy is enabled.\n" : "Http proxy is not enabled.\n");
	if ( !host.empty() ) {
		info += "Host: " + host;
		if ( port > 0 )
			info += (":" + toString(port));
		info += "\n";
	}
	if ( !user.empty() ) {
		info += "Username: " + user + "\n";
		if ( !pwd.empty() )
			info += "Password is set\n";
		else
			info += "No password specified\n";
	}
	const string& exceptions = config->getProxyExceptions();
	if ( !exceptions.empty() ) {
		info += "Exceptions: " + exceptions + "\n";
	}
	return info;
}
