/*
 * Copyright (c) 1998-2006 Jitterbit, Inc.
 *
 */

#include "PipelinePluginOutput.h"

using namespace konga;
using std::string;


const PipelinePluginOutput::OutputFilesT& PipelinePluginOutput::GetOutputFiles() const {
	return m_vOutputFiles;
}

void PipelinePluginOutput::AddOutputFile(const OutputFile& outputFile)
{
	m_vOutputFiles.push_back(outputFile);
}

void PipelinePluginOutput::SetWarning(const std::string& sWarning, const std::string& sSeverity) {
	m_Warning.Warning = sWarning;
	m_Warning.Severity = sSeverity;
}

void PipelinePluginOutput::GetWarning(std::string& sWarning, std::string& sSeverity) {
	sWarning = m_Warning.Warning;
	sSeverity = m_Warning.Severity;
}
