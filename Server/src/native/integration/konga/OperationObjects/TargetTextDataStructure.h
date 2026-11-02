/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_OPERATION_TEXT_TARGET_H
#define KONGA_OPERATION_TEXT_TARGET_H

#include "TargetDataStructure.h"
#include "TextDataStructure.h"

namespace konga {

	class TargetTextDataStructure : public TextDataStructure, public TargetDataStructure {
	private:
		TargetTextDataStructure(void) { }
	public:
		~TargetTextDataStructure(void) { }

		void doPreTransformation(const OperationEngineContext& context) const {
			                 
		}

		friend class LocationCreatorDb;
	};
}
#endif                                 

