/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef KONGA_OPERATION_DATABASE_SOURCE_LOCATION_H
#define KONGA_OPERATION_DATABASE_SOURCE_LOCATION_H

#include "SourceDataLocation.h"
#include "DatabaseLocation.h"

namespace konga {

	class DatabaseSourceLocation : public SourceDataLocation, public DatabaseLocation {
	public:
		DatabaseSourceLocation(void) { }
		~DatabaseSourceLocation(void) {}

		void doTransfer(const OperationEngineContext& context, ProcessedFiles& files, OperationProgress& progress) const {
			                                                                      
			files.add(ProcessedFile());
		}

		void doPostTransformation(const OperationEngineContext& context, ProcessedFiles& files) const {
			                
		}

		void getFirstSourceFile(const OperationEngineContext& context, ProcessedFiles& files) const {
			                                                                      
			files.add(ProcessedFile());
		}
	};
}
#endif                                              

