/*
 * Copyright (c) 1998-2006 Jitterbit, Inc.
 *
 */
#ifndef KONGA_SOURCE_TARGET_PIPELINE_PLUGIN_EXECUTION_H
#define KONGA_SOURCE_TARGET_PIPELINE_PLUGIN_EXECUTION_H

#include <string>

namespace konga {

	class OperationContext;
	class OperationDataElementHolder;
	class ProcessedFiles;
	class PipelinePluginSelector;

	void ExecutePipelinePluginAtSourceOrTarget(bool bIsSource,
		long lEntityId, const std::string& sItemName,
		const OperationContext& context, OperationDataElementHolder& deHolder,
		ProcessedFiles& files,
		PipelinePluginSelector& selector, int iRelativePositionId);
}

#endif                                                   

