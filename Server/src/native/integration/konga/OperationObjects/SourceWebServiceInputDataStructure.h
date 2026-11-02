/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_OPERATION_SOURCE_WS_INPUT_DATA_STRUCTURE
#define KONGA_OPERATION_SOURCE_WS_INPUT_DATA_STRUCTURE

#include "SourceXmlDataStructure.h"

namespace konga {

	class SourceWebServiceInputDataStructure : public SourceXmlDataStructure {
	private:
		SourceWebServiceInputDataStructure(void) { }
	public:
		~SourceWebServiceInputDataStructure(void) { }

		friend class LocationCreatorDb;
	};
}
#endif                                                  

