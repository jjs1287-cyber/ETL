/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef ETLFORGE_JDBC_UTILS_READ_SOURCE_H
#define ETLFORGE_JDBC_UTILS_READ_SOURCE_H

#include <string>

namespace konga {
	class DbConnection;
	class UserInfo;
	struct DbConnectionInfoStruct;

	void ReadJDBCSource(DbConnection& dbConn, const UserInfo& userInfo, long lSourceId,
		const DbConnectionInfoStruct& connParams,
		const std::string& sAdHocFilter,
		const std::string& sJtrFileName, 
		const std::string& sDataFileName,
		const std::string& sStructureFileName,
		const std::string& sDebugLogFile);
}

#endif                                     

