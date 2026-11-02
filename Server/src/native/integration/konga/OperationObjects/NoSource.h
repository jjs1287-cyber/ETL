/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef KONGA_OPERATION_NO_SOURCE_H
#define KONGA_OPERATION_NO_SOURCE_H

#include "NoLocation.h"
#include "SourceDataLocation.h"
#include "ProcessedFiles.h"

namespace konga {

	class NoSource : public NoLocation, public SourceDataLocation {
	private:
		NoSource(void) {}
	public:
		~NoSource(void) {}

		void doTransfer(const OperationEngineContext& context, ProcessedFiles& files, OperationProgress& progress) const;

		void doPostTransformation(const OperationEngineContext& context, ProcessedFiles& files) const {
			                 
		}

		void getFirstSourceFile(const OperationEngineContext& context, ProcessedFiles& files) const;

		friend class LocationCreatorDb;
	};
}
#endif                               

