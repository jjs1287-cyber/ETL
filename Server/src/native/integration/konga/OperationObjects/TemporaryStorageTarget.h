/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#ifndef KONGA_OPERATION_TEMPORARY_STORAGE_TARGET_H
#define KONGA_OPERATION_TEMPORARY_STORAGE_TARGET_H

#include "TemporaryStorageLocation.h"
#include "FileTypeTargetLocation.h"

namespace konga {

	class TemporaryStorageTarget : public TemporaryStorageLocation, public FileTypeTargetLocation {
	public:
		TemporaryStorageTarget(void){
		}
		~TemporaryStorageTarget(void) {
		}

		virtual void doTransfer(const OperationDataElementHolder& deHolder, ProcessedFiles& files) const;

		virtual void testFileTargetConnection(bool bCopyTestFile) const;

		std::string getFileName() const;

	};
}
#endif                                              

