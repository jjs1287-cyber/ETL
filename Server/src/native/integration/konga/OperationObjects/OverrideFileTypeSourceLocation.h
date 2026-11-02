/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef KONGA_OPERATION_OVERRIDE_FILE_TYPE_SOURCE_LOCATION_H
#define KONGA_OPERATION_OVERRIDE_FILE_TYPE_SOURCE_LOCATION_H

#include "FileTypeSourceLocation.h"

namespace {
	const std::string GLOBAL_DE_NAME_FOR_SOURCE_FILE_NAME = "__OverrideSourceFileName";
	const std::string GLOBAL_DE_NAME_FOR_SOURCE_FILE_NAME_IS_EXCLUSIVE = "__OverrideSourceFileNameIsExclusive";
}

namespace konga {

	class OverrideFileTypeSourceLocation : public FileTypeSourceLocation {
	public:
		static const long HTTP_ENDOINT_SOURCE_ID;	                                     

	public:
		OverrideFileTypeSourceLocation(const std::string& overrideFileName, bool isExclusive) :
		  m_bIsExclusive(isExclusive), m_sOverrideFileName(overrideFileName) {}
		~OverrideFileTypeSourceLocation(void);

		virtual std::vector<std::string> getFileList() const;

		virtual void doPostTransformation(const OperationEngineContext& context, ProcessedFiles& files) const {
			                
		}

		virtual void getFirstSourceFile(const OperationEngineContext& context, ProcessedFiles& files) const {
			                
		}

	protected:
		virtual std::string getFileFilter() const;

		virtual void doTransferImpl(const OperationEngineContext& context, ProcessedFiles& files, OperationProgress& progress) const;

	private:
		bool m_bIsExclusive;
		const std::string m_sOverrideFileName;

	};
}

#endif                                                        

