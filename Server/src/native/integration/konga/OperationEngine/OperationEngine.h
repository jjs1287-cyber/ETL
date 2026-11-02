/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef OPERATION_OPERATION_ENGINE_H
#define OPERATION_OPERATION_ENGINE_H

#include <string>
#include <vector>
#include <memory>	                
#include <boost/noncopyable.hpp>

#include <konga/Db/DbUtils/DbConnectionHolder.h>

#include <konga/Debugger/BreakpointLocation.h>
#include <konga/Debugger/Debuggee.h>

#include <konga/ItemAccess/UserInfo.h>

#include <konga/Log/logging.h>

#include <konga/FileTransfer/HttpUtils.h>	                                        

#include <konga/ItemAccess/UserInfo.h>

#include <konga/OperationLog/OperationLogStatus.h>

#include <konga/OperationPipeline/OperationPipeline.h>
#include <konga/OperationContext/DefaultErrorReporter.h>

#include "TestOperationDataFile.h"

namespace konga {

	class OperationEngineException;
	class OperationDataElementHolder;
	class OperationContext;
	class OperationEngineContext;
	class OperationScriptRunner;
	class PipelinePluginSelector;
	class ProcessedFile;
	class ProcessedFiles;
	class Operation;
	class Logger;
	class Debuggee;

	class OperationEngine : private DbConnectionHolder {
	public:
		                                                           
		OperationEngine(const UserInfo& currentUser, konga::Debuggee* pDebuggee, DbConnection* pTranDbConnection = 0);
		virtual ~OperationEngine(void);

		    
                                                          
                                           
     
		void RunOperation(long lOperationId, long lSessionId, long long llOperationInstanceId, OperationDataElementHolder* pDeHolder = 0,
			bool bSynchronous = false);

		                                                                                                            
		void RunOperationAsync(long lOperationId, OperationDataElementHolder* pDeHolder, const std::string& subject);

		                                                                 
		                                                       
		void TestOperation(long lOperationId, OperationDataElementHolder* pDeHolder, int& iSourceType, std::string& stWarnings);

		                                                         
		                                                                               
		void HandleHostedWebServiceCall(long lOperationId, const HttpRequest& request, HttpResponse& response, std::string& sWarning);

		static void SetNetworkingInfo(const HttpRequest& request, OperationDataElementHolder& deHolder);

		                                                                                
		void saveTargetToThisFile(const std::string& fileName);

		OperationDataElementHolder& getOperationDataElementHolder() const;

		                                                      
		void saveFinalTargetToThisFile(const std::string& fileName);

		                                              
		                                                                                                    
		void overrideSourceFile(const std::string& fileName, bool isExclusive);

		int getFinalTargetType() const;

		std::string getOperationChainInstanceGuid() const;

	private:
		static const std::string stOP_LOG_SOURCE;

		void RunOperationInit(long lOperationId, long lSessionId, long long llOperationInstanceId,
			OperationDataElementHolder* pDeHolder, std::auto_ptr<Operation>& pOperation,
			boost::shared_ptr<OperationPipeline>& pOperationPipeline);

		void CheckForSourceOverride() const;

		void initContext(OperationDataElementHolder* pDeHolder);

		OperationContext& getOperationContext() const;

		void SetCurrentUserIfNeeded();

		void GetOperationDataElements(const long lSessionId);
		void SetOperationDataElements();

		void DoPostSourceProcessing(const ProcessedFiles& files);
		void DoPostTransformationProcessing(const ProcessedFiles& files);

		Operation* getOperation(long lOperationId);
		long getOperationId() const;

		                                         
		                                                 
		void AddToTransferHistory(ProcessedFiles& file);
		void AddToTransferHistory(const ProcessedFile& file);

		void FinishOperation(ProcessedFiles& files, OperationEngineException* const pEx = 0);
		void SendOperationNotification(bool bIsSuccessOperation);

		                                                                                                       
		                          
		void StartNextOperation(bool bIsSuccessOperation, const ProcessedFiles& files, const std::string& sError);

		                                                 
		void HandleHostedWebServiceCallImpl(const HttpRequest& request, HttpResponse& response, std::string& sWarning);

		void RunOnRequestPluginsForHostedWebServiceCall(std::string& sRequest);
		void RunOnResponsePluginsForHostedWebServiceCall(std::string& sResponse);
		void RunPluginsForHostedWebServiceCall(std::string& sInput, int iRelativePosition);

		void StartSiblingOperations(long hostedWebServiceOperationId, const std::string& sRequest, const bool bSiblingImplementsWS, std::string& sResponse);

		                                                                                       
		std::string replace_de(const std::string& s);

		void Check(bool skip = false);

		bool IsGlobalDebugLoggingEnabled() const;
		void EnableDebugging(bool bDebug);

		bool shouldWriteHistory() const;
		bool shouldLog(OperationLogStatus status) const;

	private:
		bool m_bIsDebugEnabled;
		bool m_bSynchronous;
		bool m_bStartedOtherOperation;
		bool m_bExclusiveOverrideSourceFileName;

		long m_lOperationId;
		long m_lSessionId;

		long long m_llOperationInstanceId;

		UserInfo m_CurrentUser;

		std::string m_stAlsoWriteTargetToThisFile;
		std::string m_stAlsoWriteFinalTargetToThisFile;
		std::string m_stOverrideSourceFileName;

		Operation* m_pOperation;	                                  
		Logger m_Logger;

		OperationDataElementHolder* m_pDeHolder;
		bool m_bCreatedDeHolder;

		OperationEngineContext* m_pContext;
		OperationScriptRunner* m_pScriptRunner;

		konga::Debuggee* m_pDebuggee;

		DefaultErrorReporter m_ErrorReporter;
		PipelinePluginSelector* m_pPipelinePluginSelector;

		mutable std::string m_stLocalTmpDir;
		mutable std::string m_sPreTranWarnings, m_sPostTranWarnings;
	};
}

#endif                                

