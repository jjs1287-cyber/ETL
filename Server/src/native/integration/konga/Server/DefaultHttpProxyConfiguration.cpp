/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#include <algorithm>	            

#include <konga/Utils/StringUtils.h>

#include "DefaultHttpProxyConfiguration.h"

using namespace konga;
using std::string;
using std::vector;

DefaultHttpProxyConfiguration::DefaultHttpProxyConfiguration(bool bIsConfigured, const string& sProxyHostName, long lProxyPort,
															 const string& sProxyUserName, const string& sProxyPassword,
															 const string& sExceptions)
															 :
m_bIsConfigured(bIsConfigured), m_lPort(lProxyPort),
m_sHostName(sProxyHostName), m_sUserName(sProxyUserName), m_sPassword(sProxyPassword),
m_sExceptions(sExceptions)
{}

bool DefaultHttpProxyConfiguration::isProxyConfiguredForHost(const string& sHostName, string& sProxyHostName, long& lProxyPort,
													  string& sProxyUserName, string& sProxyPassword) const {
	if ( !m_bIsConfigured || m_sHostName.empty() )
		return false;

	                              
	if ( !sHostName.empty() && !m_sExceptions.empty() ) {
		const vector<string> vExceptions( konga::SplitString(m_sExceptions, ",") );
		if ( std::find(vExceptions.begin(), vExceptions.end(), sHostName) != vExceptions.end() ) return false;
	}

	sProxyHostName = m_sHostName;
	lProxyPort = m_lPort;
	sProxyUserName = m_sUserName;
	sProxyPassword = m_sPassword;
	return true;
}

string DefaultHttpProxyConfiguration::getProxyExceptions() const {
	return m_sExceptions;
}

