/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef ETLFORGE_JDBC_ENGINE_WRAPPER_H
#define ETLFORGE_JDBC_ENGINE_WRAPPER_H

#include <string>
#include <vector>

#include <konga/Db/TranDbUtils/DbConnectionInfoStruct.h>

namespace konga {

	struct DbRowSetData;

	class JdbcEngineWrapper_Impl;

	class JdbcEngineWrapper {
	public:
		JdbcEngineWrapper(const std::string& userName, const std::string& md5Pwd);
		~JdbcEngineWrapper();

		void readSource(const DbConnectionInfoStruct& connectionParams, const std::string& sourceGUID, const std::string& adHocFilter,
			const std::string& sJtrFileName, const std::string& sDataFileName, const std::string& sStructureFileName,
			const std::string& sDebugLogFile);

		void writeTarget(const DbConnectionInfoStruct& connectionParams, const std::string& sTargetGUID,
			int insertUpdateModeFlag, bool bTransaction, const std::string& sPreTargetSql, bool bIncludeNulls, 
			const std::string& sJtrFileName, const std::string& sDataFileName,
			const std::string& sDebugLogFile, std::string& sWarnings);

		std::string dbLookup(const DbConnectionInfoStruct& connectionParams, const std::string& locationGUID,
			const bool isSource, const bool bAutoCommit, const std::string& sql, int& rowCount, bool& nullRetVal);

		void dbExecute(const DbConnectionInfoStruct& connectionParams, const std::string& locationGUID, const bool isSource, const bool bAutoCommit, const std::string& sql, const int max_rows,
			DbRowSetData& rowset, int& rowCount, bool& nullRetVal);

		void commitTransactions();

		void rollBackTransactions();

	private:
		void Check();

	private:
		JdbcEngineWrapper_Impl* m_pImpl;
	};
}

#endif                                  

