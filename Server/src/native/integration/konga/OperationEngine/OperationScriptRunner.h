/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
#ifndef OPERATION_SCRIPT_RUNNER_H
#define OPERATION_SCRIPT_RUNNER_H

#include <vector>

#include <boost/shared_ptr.hpp>

#include <konga/TransformationUtils/ScriptCache.h>

#include <konga/OperationPipeline/OperationPipeline.h>
#include <konga/OperationPipeline/ActivityType.h>

class TransformationEngine;

namespace konga {

	class OperationContext;
	class OperationDataElementHolder;
	class DbConnection;

	class OperationScriptRunner {
	public:
		OperationScriptRunner(boost::shared_ptr<OperationPipeline> pipeline, OperationContext&, OperationDataElementHolder& deHolder,
			DbConnection& dbConnection);
		~OperationScriptRunner(void);

		                                         
		void RunScriptsBefore(ActivityType targetActivity);

		                           
		void RunFinalScripts();

		                                                                      
		                              
		void CollectScriptWarnings(std::string& sWarnings);

	private:
		void RunScripts(const std::vector<OperationActivity>& scriptsToRun);
		void RunScript(long scriptId);
		void DeleteLogs();

	private:
		TransformationEngine* m_pTransformationEngine;
		FILE* m_debug_log;
		OperationContext& m_context;
		OperationDataElementHolder& m_deHolder;
		boost::shared_ptr<OperationPipeline> m_pipeline;
		ScriptCache m_scriptCache;
		std::string m_sLogFileName;
	};
}

#endif                             

