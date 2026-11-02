/*
 * Copyright (c) 1998-2006 Jitterbit, Inc.
 *
 */

#ifndef KONGA_PLUGIN_MANIFEST_PARSER_H
#define KONGA_PLUGIN_MANIFEST_PARSER_H

#include <string>

#include <boost/shared_ptr.hpp>

#include "PluginManifest.h"

namespace konga {

	class PipelinePluginManifest;

	namespace xml {
		class DOMElementPtr;
	}

	                                      
	class PLUGIN_MANAGER_API PluginManifestParser {
	public:
		PluginManifestParser(void) {};
		~PluginManifestParser(void) {};

		boost::shared_ptr<PluginManifest> ParseManifest(const std::string& sManifestFile);

	private:
		boost::shared_ptr<PluginManifest> ParsePipelinePlugin(const xml::DOMElementPtr& pPluginElement);
		void AddDataElements(const xml::DOMElementPtr& pDataElements, PipelinePluginManifest& manifest);
		void AddDataElement(const xml::DOMElementPtr& pDataElement, PipelinePluginManifest& manifest);
		void AddPipelinePosition(const xml::DOMElementPtr& pElement, PipelinePluginManifest& manifest);
	};
}

#endif                                  

