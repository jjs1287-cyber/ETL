/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef KONGA_OPERATION_FTP_TARGET_H
#define KONGA_OPERATION_FTP_TARGET_H

#include "FileTypeTargetLocation.h"
#include "FtpLocation.h"

namespace konga {

	class FtpTarget : public FileTypeTargetLocation, public FtpLocation {
	public:
		FtpTarget(void);
		virtual ~FtpTarget(void);

		virtual void doTransfer(const OperationDataElementHolder& deHolder, ProcessedFiles& files) const;

		virtual void testFileTargetConnection(bool bCopyTestFile) const;

		std::string getFileName() const;

		virtual long getTransferTimeOutSeconds() const;

	protected:
		bool disableRename() const;
	};
}
#endif                                

