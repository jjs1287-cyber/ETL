/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef KONGA_OPERATION_FTP_SOURCE_H
#define KONGA_OPERATION_FTP_SOURCE_H

#include <string>

#include "FtpLocation.h"
#include "FileTypeSourceLocation.h"

namespace konga {

	class FtpSource : public FileTypeSourceLocation, public FtpLocation {
	public:
		FtpSource(void);
		~FtpSource(void);

		virtual void doPostTransformation(const OperationEngineContext& context, ProcessedFiles& files) const;

		virtual void getFirstSourceFile(const OperationEngineContext& context, ProcessedFiles& files) const;

		virtual std::vector<std::string> getFileList() const;

		virtual long getTransferTimeOutSeconds() const;

	protected:
		bool listFileNamesOnly() const;

		std::string getFileFilter() const;

		std::string getRenameFilePattern() const {
			return replace_de( m_SourceData.m_stRenameFileTo );
		}

		void doTransferImpl(const OperationEngineContext& context, ProcessedFiles& files, OperationProgress& progress) const;

		void getFilteredFileList(Ftp& ftp, vdirentry& filelist) const;
	};
}
#endif                                

