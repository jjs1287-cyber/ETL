/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
#ifndef KONGA_SCRIPT_OPERATION_H
#define KONGA_SCRIPT_OPERATION_H

#include "Operation.h"

namespace konga {

	                                     
	class ScriptOperation : public Operation {
	private:
		ScriptOperation(void) {}
	public:
		~ScriptOperation(void) {}

		void doOperation(ProcessedFiles& files, OperationEngineContext& context, OperationDataElementHolder& deHolder) {
			               
		}

		friend class OperationCreatorDb;	                                
	};
}
#endif                            

