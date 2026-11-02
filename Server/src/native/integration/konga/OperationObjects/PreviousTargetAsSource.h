/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef ETLFORGE_OPERATION_ENGINE_PREVIOUS_TARGET_AS_SOURCE_H
#define ETLFORGE_OPERATION_ENGINE_PREVIOUS_TARGET_AS_SOURCE_H

#include <string>

#include "SourceDataLocation.h"

                                            
         
namespace konga {

	class PreviousTargetAsSource : public SourceDataLocation {
	public:
		static const std::string GLOBAL_DE_NAME_FOR_SOURCE_PATH;
		static const int SOURCE_ID;

	public:
		PreviousTargetAsSource(const std::string& stLocalDirectory) : m_stLocalDirectory(stLocalDirectory) {}
		virtual ~PreviousTargetAsSource(void);

		void doTransfer(const OperationEngineContext& context, ProcessedFiles& files, OperationProgress& progress) const;

		void doPostTransformation(const OperationEngineContext& context, ProcessedFiles& files) const {}

		void getFirstSourceFile(const OperationEngineContext& context, ProcessedFiles& files) const;

		void setLocalDirectory(const std::string& stLocalDirectory) {
			m_stLocalDirectory = stLocalDirectory;
		}

	private:
		std::string m_stLocalDirectory;

	};
}


#endif                                                         

