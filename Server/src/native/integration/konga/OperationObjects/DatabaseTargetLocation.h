/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_OPERATION_DATABASE_TARGET_LOCATION_H
#define KONGA_OPERATION_DATABASE_TARGET_LOCATION_H

#include "TargetDataLocation.h"
#include "DatabaseLocation.h"

namespace konga {

	class DatabaseTargetLocation : public TargetDataLocation, public DatabaseLocation {
	public:
		DatabaseTargetLocation(void) { }
		~DatabaseTargetLocation(void) {}

		void doTransfer(const OperationDataElementHolder& , ProcessedFiles& files) const {
			                                                           
		}

	};
}
#endif                                              

