/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#ifndef KONGA_PLUGIN_MANIFEST_STORE_H
#define KONGA_PLUGIN_MANIFEST_STORE_H

#include <boost/shared_ptr.hpp>

#include "PluginManagerApi.h"
#include "PluginManifest.h"

namespace konga {

	                                                         
	class PLUGIN_MANAGER_API PluginManifestStore {
	public:
		PluginManifestStore(void) {}
		virtual ~PluginManifestStore(void) {}

		virtual boost::shared_ptr<PluginManifest> GetManifest(long lPluginId) = 0;

		virtual boost::shared_ptr<PluginManifest> GetManifest(const std::string& stUniqueId) = 0;

	};
}

#endif                                 

