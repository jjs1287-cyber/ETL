/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef KONGA_OPERATION_FILE_SHARE_Target_H
#define KONGA_OPERATION_FILE_SHARE_Target_H

#include "FileShareLocation.h"
#include "FileTypeTargetLocation.h"

namespace konga {

	class FileShareTarget : public FileShareLocation, public FileTypeTargetLocation {
	public:
		FileShareTarget(void){
		}
		~FileShareTarget(void) {
		}

		virtual void doTransfer(const OperationDataElementHolder& deHolder, ProcessedFiles& files) const;

		virtual void testFileTargetConnection(bool bCopyTestFile) const;

		std::string getFileName() const;

	private:
		bool doTargetDirectoryCheck(const OperationDataElementHolder& deHolder) const;

	};
}
#endif                                       

