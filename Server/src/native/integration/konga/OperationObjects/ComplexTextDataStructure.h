/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_OPERATION_COMPLEX_TEXT_DATA_STRUCTURE_H
#define KONGA_OPERATION_COMPLEX_TEXT_DATA_STRUCTURE_H

#include "DataStructure.h"
#include "SourceDataStructure.h"
#include "TargetDataStructure.h"

namespace konga {

	                                                                                                                 
	class ComplexTextDataStructure : public DataStructure, public SourceDataStructure, public TargetDataStructure {
	private:
		ComplexTextDataStructure(void) { }
	public:
		virtual ~ComplexTextDataStructure(void) {}

		void doPreTransformation(const OperationEngineContext& context) const {
			                 
		}

		friend class LocationCreatorDb;
	};
}
#endif                                                 

