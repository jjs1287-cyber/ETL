/*
 * Copyright (c) 1998-2006 Jitterbit, Inc.
 *
 */

#ifndef KONGA_PIPELINE_PLUGIN_H
#define KONGA_PIPELINE_PLUGIN_H

#include <konga/Utils/Version.h>

#include "PluginManagerApi.h"

namespace konga {

	                                                  
	class PLUGIN_MANAGER_API PipelinePlugin {
	public:
		PipelinePlugin(void) : m_PluginSpecVersion(1,0,0) {}
		virtual ~PipelinePlugin(void) {}

		Version GetPluginSpecVersion() const {
			return m_PluginSpecVersion;
		}

	private:
		Version m_PluginSpecVersion;
	};
}

#endif                           
