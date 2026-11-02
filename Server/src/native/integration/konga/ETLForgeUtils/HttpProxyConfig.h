/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#ifndef ETLFORGE_UTILS_HTTP_PROXY_CONFIG
#define ETLFORGE_UTILS_HTTP_PROXY_CONFIG

#include <string>

namespace konga {
	namespace ETLForgeUtils {

		void SetProxyConfiguration(bool bHost, const std::string& sHost,
			bool bUser, const std::string& sUser, bool bPwd, const std::string& sPwd, bool bExceptions, const std::string& stExceptions);

		void EnableProxyConfiguration();
		void DisableProxyConfiguration();
		
		std::string GetProxyConfiguration();

	}
}

#endif                                    
