/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#ifndef KONGA_SERVER_HTTP_PROXY_CONFIGURATION_INTERFACE_H
#define KONGA_SERVER_HTTP_PROXY_CONFIGURATION_INTERFACE_H

#include <string>

#include <konga/Db/DbUtils/DbConnectionHolder.h>

#include <konga/FileTransfer/HttpProxyConfiguration.h>

namespace konga {

	                                                   
	HttpProxyConfiguration* GetHttpProxyConfiguration();

	class ServerHttpProxyConfiguration : DbConnectionHolder {
	public:
		ServerHttpProxyConfiguration(DbConnection* pTranDbConn = 0);
		~ServerHttpProxyConfiguration(void);

		                                                   
		HttpProxyConfiguration* GetHttpProxyConfiguration();

		void SetHttpProxyUsage(bool bYes);

		void SetHttpProxyHost(const std::string& sProxyHost);
		void SetHttpProxyPort(long lProxyPort);

		void SetHttpProxyUserName(const std::string& sProxyUserName);
		void SetHttpProxyPassword(const std::string& sProxyPassword);

		                                                                                
		void SetHttpProxyExceptions(const std::string& sProxyExceptions);
	};

}

#endif                                                     

