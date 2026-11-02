/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef KONGA_BULK_LOAD_ENGINE_WRAPPER_CPP_H
#define KONGA_BULK_LOAD_ENGINE_WRAPPER_CPP_H

#include <string>

#include "BulkLoadEngine.hpp"

#include <konga/JavaBackend/BulkLoadEngine/BulkLoadEngineWrapper.h>

namespace konga {


	class BulkLoadEngineWrapperCPP : public  BulkLoadEngineWrapper {
	public:
		static BulkLoadEngineWrapper* createBulkLoadEngineWrapper(DbConnection& tranDbConn, const UserInfo& userInfo);

	public:
		BulkLoadEngineWrapperCPP(const std::string& stUserName, const std::string& stMD5Password);
		~BulkLoadEngineWrapperCPP(void);

		                                              
		void startNewSession();

		void closeSession();

		BulkLoadResult runBulkLoad(const std::string& stSFDCSessionId, const std::string& stSFDCUrl,
			long long sfdcBulkLoadId, long long operationInstanceId, long long lastOperationInstanceId,
			const std::string& stSourceFile);

		std::string getWarnings() const;

	private:
		void configureWsBulkLoadSessionId(WsBulkLoadSessionId& sessionId);

	private:
		BulkLoadEngine* m_wsImpl;
		std::string m_stUserName, m_stMD5Password;
		std::string m_stSessionId;

		std::string m_stWarnings;
	};

}

#endif                                        

