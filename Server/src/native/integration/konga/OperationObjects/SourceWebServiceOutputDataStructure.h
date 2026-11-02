/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_OPERATION_SOURCE_WEB_SERVICE_OUTPUT_DATA_STRUCTURE_H
#define KONGA_OPERATION_SOURCE_WEB_SERVICE_OUTPUT_DATA_STRUCTURE_H

#include "SourceXmlDataStructure.h"

namespace konga {

	class SourceWebServiceOutputDataStructure : public SourceXmlDataStructure {
	private:
		SourceWebServiceOutputDataStructure(void) { }
	public:
		~SourceWebServiceOutputDataStructure(void) { }

		friend class LocationCreatorDb;
	};
}

#endif                                                              

