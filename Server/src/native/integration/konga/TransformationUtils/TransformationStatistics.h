/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef KONGA_TRANSFORMATION_ENGINE_TRANSFORMATION_STATISTICS_H
#define KONGA_TRANSFORMATION_ENGINE_TRANSFORMATION_STATISTICS_H

namespace konga {

	struct TransformationStatistics {
		TransformationStatistics(void) : TransformationTimeMs(0),
			SourceSizeBytes(0), TargetSizeBytes(0),
			SourceRecords(0), TargetRecords(0)
		{}

		void Update(const TransformationStatistics& s);
		
		long long TransformationTimeMs;
		long long SourceSizeBytes, TargetSizeBytes;
		long long SourceRecords, TargetRecords;
	};

}

#endif                                                           
