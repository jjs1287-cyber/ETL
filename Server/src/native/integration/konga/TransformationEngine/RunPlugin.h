/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#ifndef TRANSFORMATION_ENGINE_RUN_PLUGIN_IMPL_H
#define TRANSFORMATION_ENGINE_RUN_PLUGIN_IMPL_H

#include <string>

namespace konga {

	class TransformationDataHolder;
	class OperationDataElementHolder;

	void RunPlugin(const std::string& plugin_tag, TransformationDataHolder& tranDataHolder, std::string& stError);
}

#endif                                           

