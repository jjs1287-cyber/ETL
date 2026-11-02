/*
 * Copyright (c) 1998-2006 Jitterbit, Inc.
 *
 */

#ifndef KONGA_PLUGIN_SERIALIZER_H
#define KONGA_PLUGIN_SERIALIZER_H

#include <string>

#include "PipelinePluginInput.h"

namespace konga {

	                                                  
	class PLUGIN_MANAGER_API PluginSerializer {
	public:
		PluginSerializer(void) {}
		virtual ~PluginSerializer(void) {}

		std::string SerializePipelinePluginInput(const PipelinePluginInput& input) const;

	private:
	};
}

#endif                             

