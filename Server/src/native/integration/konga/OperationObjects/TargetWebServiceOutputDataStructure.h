/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_OPERATION_Target_WEB_SERVICE_OUTPUT_DATA_STRUCTURE_H
#define KONGA_OPERATION_Target_WEB_SERVICE_OUTPUT_DATA_STRUCTURE_H

#include "TargetXmlDataStructure.h"

namespace konga {

	class TargetWebServiceOutputDataStructure : public TargetXmlDataStructure {
	private:
		TargetWebServiceOutputDataStructure(void) { }
	public:
		~TargetWebServiceOutputDataStructure(void) { }

		friend class LocationCreatorDb;
	};
}

#endif                                                              

