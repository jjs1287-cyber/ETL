/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef KONGA_BULK_LOAD_ENGINE_WRAPPER_C_H
#define KONGA_BULK_LOAD_ENGINE_WRAPPER_C_H

#include <konga/JavaBackend/BulkLoadEngine/BulkLoadEngineWrapper.h>
#include <konga/JavaBackend/BulkLoadEngineC/axis2_stub_BulkLoadEngineService.h>

#include <konga/JavaBackend/JavaBackendConfiguration/Axis2CImpl.h>
#include <konga/JavaBackend/JavaBackendConfiguration/ServiceNames.h>

namespace konga {

	class BulkLoadEngineWrapperC : public BulkLoadEngineWrapper, private Axis2CImplBase {
	public:
		BulkLoadEngineWrapperC(const std::string& userName, const std::string& md5pwd);
		virtual ~BulkLoadEngineWrapperC(void);

		                                     
		axis2_stub_t* create_axis2_stub_t();

		                                             
		void startNewSession();

		void closeSession();

		BulkLoadResult runBulkLoad(const std::string& stSFDCSessionId, const std::string& stSFDCUrl,
			long long sfdcBulkLoadId, long long operationInstanceId, long long lastOperationInstanceId,
			const std::string& stSourceFile);

		std::string getWarnings() const;

	private:
		void closeSessionNoThrow();

		adb_WsBulkLoadSessionId_t* createWsBulkLoadSessionId();
		adb_WsBulkLoadParams_t* createWsBulkLoadParams(long long sfdcBulkLoadId, long long operationInstanceId, long long lastOperationInstanceId, const std::string& stSourceFile);
		adb_WsSalesforceEndpoint_t* createWsSalesforceEndpoint(const std::string& stSFDCSessionId, const std::string& stSFDCUrl);

	private:
		std::string m_stWarnings;

	};

}

#endif                                      
