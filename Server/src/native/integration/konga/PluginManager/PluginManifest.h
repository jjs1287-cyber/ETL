/*
 * Copyright (c) 1998-2006 Jitterbit, Inc.
 *
 */

#ifndef KONGA_PLUGIN_MANIFEST_H
#define KONGA_PLUGIN_MANIFEST_H

#include <string>

#include <konga/Utils/Version.h>

#include "PluginManagerApi.h"

namespace konga {

	                                  
	class PLUGIN_MANAGER_API PluginManifest {
	public:
		PluginManifest(void) : m_PluginSpecVersion(1, 0, 0) {}
		virtual ~PluginManifest(void) {}

		virtual Version GetPluginSpecVersion() const {
			return m_PluginSpecVersion;
		}

		virtual std::string GetUniqueName() const {
			return m_sUniqueName;
		}

		virtual std::string GetDisplayName() const {
			return m_sDisplayName;
		}

		virtual std::string GetAuthor() const {
			return m_sAuthor;
		}

		virtual std::string GetDescription() const {
			return m_sDescription;
		}

		                                                                                   
		virtual std::string GetParsedFrom() const {
			return m_sParsedFrom;
		}

	private:
		friend class PluginManifestParser;
		Version m_PluginSpecVersion;
		std::string m_sUniqueName, m_sDisplayName, m_sAuthor, m_sDescription;
		std::string m_sParsedFrom;
	};
}

#endif                           

