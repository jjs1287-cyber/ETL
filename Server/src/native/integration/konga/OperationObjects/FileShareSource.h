/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef KONGA_OPERATION_FILE_SHARE_SOURCE_H
#define KONGA_OPERATION_FILE_SHARE_SOURCE_H

#include <string>

#include "FileShareLocation.h"
#include "FileTypeSourceLocation.h"

namespace konga {

	class FileShareSource : public FileShareLocation, public FileTypeSourceLocation {
	public:
		FileShareSource(void){
		}
		~FileShareSource(void) {
		}

		virtual std::vector<std::string> getFileList() const;

	protected:
		std::string getFileFilter() const;

		std::string getRenameFilePattern() const;

		int getFileShareListLimit(const OperationDataElementHolder* pDeHolder) const;

		virtual void doPostTransformation(const OperationEngineContext& context, ProcessedFiles& files) const;
		
		virtual void getFirstSourceFile(const OperationEngineContext& context, ProcessedFiles& files) const;

		void doTransferImpl(const OperationEngineContext& context, ProcessedFiles& files, OperationProgress& progress) const;

		void getFilteredDirectoryList(Samba& smb, vdirentry& dirlist) const;
	};
}
#endif                                       

