/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_OPERATION_NO_DATA_STRUCTURE_H
#define KONGA_OPERATION_NO_DATA_STRUCTURE_H

#include "DataStructure.h"
#include "SourceDataStructure.h"
#include "TargetDataStructure.h"

namespace konga {

	class NoDataStructure : public DataStructure, public SourceDataStructure, public TargetDataStructure {
	private:
		NoDataStructure(void) { }
	public:
		~NoDataStructure(void) { }

		void doPreTransformation(const OperationEngineContext &context) const {
			                 
		}

		friend class LocationCreatorDb;
	};
}
#endif                                       

