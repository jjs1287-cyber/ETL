/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#ifndef KONGA_FILE_TRANSFER_HTTP_PROXY_CONFIGURATION_INTERFACE_H
#define KONGA_FILE_TRANSFER_HTTP_PROXY_CONFIGURATION_INTERFACE_H

#include <string>

namespace konga {

	                                                                            
	  
	class HttpProxyConfiguration {
	protected:
		HttpProxyConfiguration(void) {}

	public:
		virtual ~HttpProxyConfiguration(void) {}

		                                                                                    
		virtual bool isProxyConfiguredForHost(const std::string& sHostName, std::string& sProxyHostName, long& lProxyPort, std::string& sProxyUserName, std::string& sProxyPassword) const = 0;

		virtual std::string getProxyExceptions() const = 0;
	};
}

#endif                                                            

