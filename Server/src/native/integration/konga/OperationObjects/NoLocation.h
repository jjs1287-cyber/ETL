/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_OPERATION_NO_LOCATION_H
#define KONGA_OPERATION_NO_LOCATION_H

#include "DataLocation.h"

namespace konga {

	class NoLocation : public DataLocation {
	protected:
		NoLocation(void) { }
	public:
		~NoLocation(void) { }
	};
}
#endif                                 

