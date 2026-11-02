/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#ifndef ETLFORGE_JAVA_BACKEND_CONFIGURATION_CONFIG_UTILS_H
#define ETLFORGE_JAVA_BACKEND_CONFIGURATION_CONFIG_UTILS_H

#include <string>

#ifdef USE_AXIS_CPP
#include <axis/client/Stub.hpp>
#endif

namespace konga {

	class DbConnection;
	class UserInfo;

	std::string GetEndpointUrlForService(const std::string& serviceName);

#ifdef USE_AXIS_CPP
	void ConfigureStub(AXIS_CPP_NAMESPACE_PREFIX Stub& stub, const std::string& serviceName);
#endif

	void GetCredentials(DbConnection& tranDbConn, const UserInfo& userInfo, std::string& userName, std::string& md5pwd);

	std::string GetAXIS2C_HOME();
}

#endif                                                      

