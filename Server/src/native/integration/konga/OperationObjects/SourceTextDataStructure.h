/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_OPERATION_SourceTextDataStructure_H
#define KONGA_OPERATION_SourceTextDataStructure_H

#include "SourceDataStructure.h"
#include "TextDataStructure.h"

namespace konga {

	class SourceTextDataStructure : public TextDataStructure, public SourceDataStructure {
	public:
		SourceTextDataStructure(void) { }
		~SourceTextDataStructure(void) { }

		void doPreTransformation(const OperationEngineContext& context) const {
			                 
		}

		friend class LocationCreatorDb;
	};
}
#endif                                             

