/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
#ifndef KONGA_ARCHIVE_OPERATION_H
#define KONGA_ARCHIVE_OPERATION_H

#include "Operation.h"

namespace konga {

	                                                                    
	class ArchiveOperation : public Operation {
	private:
		ArchiveOperation(void) {}
	public:
		~ArchiveOperation(void) {}

		void doOperation(ProcessedFiles& files, OperationEngineContext& context, OperationDataElementHolder& deHolder);

		friend class OperationCreatorDb;	                                
	};
}
#endif                             

