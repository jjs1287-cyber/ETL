/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_OPERATION_NO_TARGET_H
#define KONGA_OPERATION_NO_TARGET_H

#include "NoLocation.h"
#include "TargetDataLocation.h"
#include "ProcessedFiles.h"

namespace konga {

	class NoTarget : public NoLocation, public TargetDataLocation {
	private:
		NoTarget(void) {}
	public:
		~NoTarget(void) {}

		void doTransfer(const OperationDataElementHolder& deHolder, ProcessedFiles& files) const {
			                
		}

		friend class LocationCreatorDb;
	};
}
#endif                               

