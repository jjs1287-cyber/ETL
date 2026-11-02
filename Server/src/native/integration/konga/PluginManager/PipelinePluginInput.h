/*
 * Copyright (c) 1998-2006 Jitterbit, Inc.
 *
 */

#ifndef KONGA_PIPELINE_PLUGIN_INPUT_H
#define KONGA_PIPELINE_PLUGIN_INPUT_H

#include <string>

#include "PluginDataElementHolder.h"
#include "PipelinePosition.h"
#include "PipelinePlugin.h"

namespace konga {

	                                                         
	class PLUGIN_MANAGER_API PipelinePluginInput : public PipelinePlugin, public PluginDataElementHolder {
	public:
		PipelinePluginInput(
			const Version& serverVersion,
			const std::string& sOpSystemName,
			const std::string& sOpSystemVersion,
			const std::string& sServerHome,
			const std::string& sOperationName,
			const std::string& sOperationGuid,
			long lOpId,
			const std::string& sPluginHome
			) : PipelinePlugin(), m_PipelinePluginSpecVersion(1,0,0),
			m_sOperatingSystemName(sOpSystemName),
			m_sOperatingSystemVersion(sOpSystemVersion),
			m_ETLForgeServerVersion(serverVersion),
			m_sETLForgeServerHome(sServerHome),
			m_sOperationName(sOperationName),
			m_sOperationId(sOperationGuid),
			m_lOperationId(lOpId),
			m_sPluginHome(sPluginHome)
		{}

		virtual ~PipelinePluginInput(void) {}

		std::string GetOperatingSystemName() const {
			return m_sOperatingSystemName;
		}

		std::string GetOperatingSystemVersion() const {
			return m_sOperatingSystemVersion;
		}

		Version GetETLForgeServerVersion() const {
			return m_ETLForgeServerVersion;
		}

		std::string GetETLForgeServerHome() const {
			return m_sETLForgeServerHome;
		}

		std::string GetOperationName() const {
			return m_sOperationName;
		}

		std::string GetOperationGuid() const {
			return m_sOperationId;
		}

		long GetOperationId() const {
			return m_lOperationId;
		}

		std::string GetPluginHome() const {
			return m_sPluginHome;
		}

		void SetPipelinePosition(const PipelinePosition& pos);

		PipelinePosition GetPipelinePosition() const;

		void AddInputFile(const std::string& sInputFile);

		const std::vector<std::string>& GetInputFiles() const;

	private:
		Version m_PipelinePluginSpecVersion;
		std::string m_sOperatingSystemName;
		std::string m_sOperatingSystemVersion;

		Version m_ETLForgeServerVersion;
		std::string m_sETLForgeServerHome;
		std::string m_sOperationName, m_sOperationId;
		long m_lOperationId;

		std::string m_sPluginHome;

		PipelinePosition m_PipelinePosition;

		std::vector<std::string> m_vInputFiles;
	};
}

#endif                                 

