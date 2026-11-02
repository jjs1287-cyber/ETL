/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef ETLFORGE_JDBC_UTILS_WRITE_TARGET_H
#define ETLFORGE_JDBC_UTILS_WRITE_TARGET_H

#include <string>

namespace konga {
	class DbConnection;
	class UserInfo;
	struct DbConnectionInfoStruct;

	void WriteJdbcTarget(const UserInfo& userInfo, long lTargetId,
		const DbConnectionInfoStruct& connectionParams, int insertUpdateModeFlag, bool bTransaction,
		const std::string& sPreTargetSql, bool bIncludeNulls, const std::string& sJtrFileName, 
		const std::string& sDataFileName, const std::string& sStructureFileName,
		const std::string& sDebugLogFile, std::string& sWarnings);
}

#endif                                      

