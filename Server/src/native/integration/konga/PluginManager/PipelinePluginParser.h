/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */

#ifndef KONGA_PIPELINE_PLUGIN_PARSER_H
#define KONGA_PIPELINE_PLUGIN_PARSER_H

#include <string>

#include <boost/shared_ptr.hpp>

#include "PipelinePluginInput.h"
#include "PipelinePluginOutput.h"

#include "PluginManagerApi.h"

namespace konga {

	                                                                            
	class PLUGIN_MANAGER_API PipelinePluginParser {
	public:
		PipelinePluginParser(void) {}
		virtual ~PipelinePluginParser(void) {}

		                                        
                                                                                                       

		boost::shared_ptr<PipelinePluginOutput> ParsePipelinePluginOutput(const std::string& sOutputFileName);

	private:


	};
}

#endif                                  

