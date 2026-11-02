/*
 * Copyright (c) 1998-2006 Jitterbit, Inc.
 *
 */

#ifndef KONGA_PIPELINE_PLUGIN_MANIFEST_H
#define KONGA_PIPELINE_PLUGIN_MANIFEST_H

#include <string>
#include <vector>
#include <map>

#include <konga/DataElement/DataElement.h>

#include "PluginDataElement.h"
#include "PipelinePosition.h"
#include "PluginManifest.h"

namespace konga {

	                                               
	class PLUGIN_MANAGER_API PipelinePluginManifest : public PluginManifest {
	public:
		PipelinePluginManifest(void) : m_PipelinePluginSpecVersion(1, 0, 0),
			m_PipelinePluginVersion(0),
			m_bRequestAllDataElements(false) {}
		virtual ~PipelinePluginManifest(void) {}

		Version GetPipelinePluginSpecVersion() const {
			return m_PipelinePluginSpecVersion;
		}

		Version GetPipelinePluginVersion() const {
			return m_PipelinePluginVersion;
		}

		std::string GetCommand() const {
			return m_sCommand;
		}

		std::string GetArguments() const {
			return m_sArguments;
		}

		typedef std::map<std::string, boost::shared_ptr<PluginDataElement> > DEMapT;

		bool GetRequestsAllDataElements() const {
			return m_bRequestAllDataElements;
		}

		const DEMapT& GetDataElements() const;

		std::vector<PipelinePosition> GetPipelinePositions() const;

		bool ExecuteAtPipelinePosition(int iEntityType, int iRelativePipelinePosition);

	private:
		friend class PluginManifestParser;

		Version m_PipelinePluginSpecVersion;
		Version m_PipelinePluginVersion;

		std::string m_sCommand, m_sArguments;

		DEMapT m_DataElements;
		bool m_bRequestAllDataElements;
		std::vector<PipelinePosition> m_PipelinePositions;
	};
}

#endif                                    
