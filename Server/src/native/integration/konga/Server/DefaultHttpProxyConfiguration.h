/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DEFAULT_HTTP_PROXY_CONFIGURATION_INTERFACE_H
#define KONGA_DEFAULT_HTTP_PROXY_CONFIGURATION_INTERFACE_H

#include <string>

#include <konga/FileTransfer/HttpProxyConfiguration.h>

namespace konga {
	class DefaultHttpProxyConfiguration : public HttpProxyConfiguration {
	public:
		DefaultHttpProxyConfiguration(bool bIsConfigured, const std::string& sProxyHostName, long lProxyPort,
			const std::string& sProxyUserName, const std::string& sProxyPassword, const std::string& sExceptions);

		DefaultHttpProxyConfiguration(void) {}

		bool isProxyConfiguredForHost(const std::string& sHostName, std::string& sProxyHostName, long& lProxyPort, std::string& sProxyUserName, std::string& sProxyPassword) const;

		std::string getProxyExceptions() const;

	private:
		bool m_bIsConfigured;
		long m_lPort;
		std::string m_sHostName, m_sUserName, m_sPassword, m_sExceptions;
	};
}

#endif                                                      

