/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_OPERATION_Target_WS_INPUT_DATA_STRUCTURE
#define KONGA_OPERATION_Target_WS_INPUT_DATA_STRUCTURE

#include "TargetXmlDataStructure.h"

namespace konga {

	class TargetWebServiceInputDataStructure : public TargetXmlDataStructure {
	private:
		TargetWebServiceInputDataStructure(void) { }
	public:
		~TargetWebServiceInputDataStructure(void) { }

		friend class LocationCreatorDb;
	};
}
#endif                                                  

