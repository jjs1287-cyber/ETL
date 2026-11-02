/*
 * Copyright (c) 1998-2006 Jitterbit, Inc.
 *
 */

#ifndef ETLFORGE_PIPELINE_PLUGIN_EXECUTOR
#define ETLFORGE_PIPELINE_PLUGIN_EXECUTOR

#include <string>
#include <vector>

#include "OutputFiles.h"

namespace konga {

	class PipelinePluginManifest;
	class OperationContext;
	class OperationDataElementHolder;
	class PipelinePosition;

	class PipelinePluginExecutor {
	public:
		PipelinePluginExecutor(void) {}
		~PipelinePluginExecutor(void) {}

		                                                              
		                            
		void ExecutePipelinePlugin(const PipelinePluginManifest& manifest,
			const PipelinePosition& pipelinePosition,
			const OperationContext& context,
			const OperationDataElementHolder& deHolder,
			const std::vector<std::string>& inputFiles,
			bool& bHasNewFiles, std::vector<OutputFile>& outputFiles);
	};
}

#endif                                     

