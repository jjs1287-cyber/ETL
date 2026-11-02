/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include "TransformationStatistics.h"

using namespace konga;

void TransformationStatistics::Update(const TransformationStatistics& s) {
	this->SourceRecords += s.SourceRecords;
	this->SourceSizeBytes += s.SourceSizeBytes;

	this->TargetRecords += s.TargetRecords;
	this->TargetSizeBytes += s.TargetSizeBytes;

	this->TransformationTimeMs += s.TransformationTimeMs;
}

