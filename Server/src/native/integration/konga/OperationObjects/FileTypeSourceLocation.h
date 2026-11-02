/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef KONGA_OPERATION_FILE_TYPE_SOURCE_LOCATION_H
#define KONGA_OPERATION_FILE_TYPE_SOURCE_LOCATION_H

#include <string>
#include <vector>

#include "SourceDataLocation.h"

namespace konga {

	class FileTypeSourceLocation : public SourceDataLocation {
	public:
		FileTypeSourceLocation(void) {}
		virtual ~FileTypeSourceLocation(void) {}

		                                             
		                                              
		virtual std::vector<std::string> getFileList() const = 0;

		void setOverrideFileFilter(const std::string& sOverrideFileFilter) {
			m_sOverrideFileFilter = sOverrideFileFilter;
		}

	protected:

		std::string getOverrideFileFilter() const {
			return m_sOverrideFileFilter;
		}

		virtual std::string getFileFilter() const = 0;

		bool deleteAfterProcessing() const;

		bool useCaseInsensitiveFileFilter() const;

		bool useRegExFileFilter() const;

		int getFileListLimit(const OperationDataElementHolder* pDeHolder) const;

		                                                    
		void doTransfer(const OperationEngineContext& context, ProcessedFiles& files, OperationProgress& progress) const;

		                                              
		virtual void doTransferImpl(const OperationEngineContext& context, ProcessedFiles& files, OperationProgress& progress) const = 0;

	private:
		std::string m_sOverrideFileFilter;
	};
}
#endif                                               

