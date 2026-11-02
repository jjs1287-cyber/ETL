/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef KONGA_BULK_LOAD_ENGINE_WRAPPER_H
#define KONGA_BULK_LOAD_ENGINE_WRAPPER_H

#include <string>

namespace konga {

	class DbConnection;
	class UserInfo;

	struct BulkLoadResult {
		bool allBatchesSent;
		std::string warnings;
		std::string logFile;
	};

	class BulkLoadEngineWrapper {
	public:
		static BulkLoadEngineWrapper* createBulkLoadEngineWrapper(DbConnection& tranDbConn, const UserInfo& userInfo);

	public:
		BulkLoadEngineWrapper(void) {}
		virtual ~BulkLoadEngineWrapper(void) {}

		virtual void startNewSession() = 0;

		virtual void closeSession() = 0;

		virtual BulkLoadResult runBulkLoad(const std::string& stSFDCSessionId, const std::string& stSFDCUrl,
			long long sfdcBulkLoadId, long long operationInstanceId, long long lastOperationInstanceId,
			const std::string& stSourceFile) = 0;

		virtual std::string getWarnings() const = 0;

	};
}

#endif                                    

