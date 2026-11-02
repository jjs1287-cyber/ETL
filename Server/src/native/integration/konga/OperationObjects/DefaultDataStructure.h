/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#ifndef KONGA_OPERATION_DEFAULT_DATA_STRUCTURE_H
#define KONGA_OPERATION_DEFAULT_DATA_STRUCTURE_H

#include "DataStructure.h"
#include "SourceDataStructure.h"
#include "TargetDataStructure.h"

namespace konga {

	                                                                   
	                             
	class DefaultDataStructure : public DataStructure, public SourceDataStructure, public TargetDataStructure {
	private:
		DefaultDataStructure(void) { }
	public:
		~DefaultDataStructure(void) { }

		void doPreTransformation(const OperationEngineContext &context) const {
			                 
		}

		friend class LocationCreatorDb;
	};
}
#endif                                            

