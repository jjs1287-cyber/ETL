/*
 * Copyright (c) 1998-2006 Jitterbit, Inc.
 *
 */

#include "PipelinePluginManifest.h"

using namespace konga;
using std::vector;

const PipelinePluginManifest::DEMapT& PipelinePluginManifest::GetDataElements() const {
	return m_DataElements;
}

vector<PipelinePosition> PipelinePluginManifest::GetPipelinePositions() const {
	return m_PipelinePositions;
}

bool PipelinePluginManifest::ExecuteAtPipelinePosition(int iEntityType, int iRelativePipelinePosition) {
	vector<PipelinePosition>::const_iterator cit = m_PipelinePositions.begin();
	for(; cit != m_PipelinePositions.end(); ++cit ) {
		if ( cit->GetEntityTypeId() == iEntityType && cit->GetRelativePositionId() == iRelativePipelinePosition )
			return true;
	}
	return false;
}
