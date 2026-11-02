/*
 * Copyright (c) 1998-2006 Jitterbit, Inc.
 *
 */

#ifndef KONGA_PIPELINE_PLUGIN_MANAGER_H
#define KONGA_PIPELINE_PLUGIN_MANAGER_H

#include <string>
#include <vector>

#include "PluginManagerApi.h"

namespace konga {

namespace PluginManager {

	std::string GetPluginsBaseDir();

	                                                                        
	                                        
	PLUGIN_MANAGER_API bool UpdatePluginsInDatabase(std::string& sWarnings);

	PLUGIN_MANAGER_API void GetPipelinePlugins(std::vector<std::string>& pipelinePlugins);

}                           
}                   

#endif                                   

