/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef KONGA_OPERATION_TEMPORARY_STORAGE_SOURCE_H
#define KONGA_OPERATION_TEMPORARY_STORAGE_SOURCE_H

#include <string>
#include <vector>

#include "TemporaryStorageLocation.h"
#include "FileTypeSourceLocation.h"

namespace konga {

	class TemporaryStorageSource : public TemporaryStorageLocation, public FileTypeSourceLocation {
	public:
		TemporaryStorageSource(void){
		}
		~TemporaryStorageSource(void) {
		}

		virtual std::vector<std::string> getFileList() const;

	protected:
		std::string getFileFilter() const;

		std::string getRenameFilePattern() const;
		
		void getFilteredDirectoryListImpl(vdirentry& dirlist, int nMaxFilesToList) const;

		virtual void doPostTransformation(const OperationEngineContext& context, ProcessedFiles& files) const;
		
		virtual void getFirstSourceFile(const OperationEngineContext& context, ProcessedFiles& files) const;

		void doTransferImpl(const OperationEngineContext& context, ProcessedFiles& files, OperationProgress& progress) const;

	private:
		void ValidateRelativeFileName(const std::string& stRelativePath) const;
	};
}
#endif                                              

