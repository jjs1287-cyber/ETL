/*
 * Copyright (c) 1998-2006 Jitterbit, Inc.
 *
 */

#include <algorithm>		              

#include "PipelinePluginInput.h"

using namespace konga;
using std::string;

void PipelinePluginInput::SetPipelinePosition(const PipelinePosition& pos) {
	m_PipelinePosition = pos;
}

PipelinePosition PipelinePluginInput::GetPipelinePosition() const {
	return m_PipelinePosition;
}



void PipelinePluginInput::AddInputFile(const std::string& sInputFile) {
	                              
	if ( std::find(m_vInputFiles.begin(), m_vInputFiles.end(), sInputFile) == m_vInputFiles.end() )
		m_vInputFiles.push_back(sInputFile);
}

const std::vector<std::string>& PipelinePluginInput::GetInputFiles() const {
	return m_vInputFiles;
}

