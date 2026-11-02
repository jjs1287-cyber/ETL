/*
 * Copyright (c) 1998-2006 Jitterbit, Inc.
 *
 */

#ifndef KONGA_PIPELINE_PLUGIN_OUTPUT_H
#define KONGA_PIPELINE_PLUGIN_OUTPUT_H

#include "PipelinePlugin.h"
#include "PluginDataElementHolder.h"

namespace konga {

	                                              
	class PLUGIN_MANAGER_API PipelinePluginOutput : public PipelinePlugin, public PluginDataElementHolder {
	public:
		PipelinePluginOutput(void) :
			m_PipelinePluginOutputVersion(1,0,0),
			m_bHasFilesSection(false)
			{}

		virtual ~PipelinePluginOutput(void) {}

		Version GetPipelinePluginOutputVersion() const {
			return m_PipelinePluginOutputVersion;
		}

		struct OutputWarning {
			std::string Warning;
			std::string Severity;
		};

		struct OutputFile {
			std::string Name;
			std::vector<std::string> OriginFiles;
			OutputWarning Warning;
		};

		typedef std::vector<OutputFile> OutputFilesT;

		void AddOutputFile(const OutputFile& outputFile);

		const OutputFilesT& GetOutputFiles() const;

		bool GetHasFilesSection() const {
			return m_bHasFilesSection;
		}

		void SetHasFilesSection() {
			m_bHasFilesSection = true;
		}

		void SetWarning(const std::string& sWarning, const std::string& sSeverity);

		void GetWarning(std::string& sWarning, std::string& sSeverity);

	private:
		Version m_PipelinePluginOutputVersion;
		bool m_bHasFilesSection;
		OutputFilesT m_vOutputFiles;
		OutputWarning m_Warning;
	};

}

#endif                                  

