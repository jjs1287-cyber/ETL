/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef ETLFORGE_OPERATION_ENGINE_SOURCE_FROM_HOSTED_WEBSERVICE_CALL_H
#define ETLFORGE_OPERATION_ENGINE_SOURCE_FROM_HOSTED_WEBSERVICE_CALL_H

#include <string>

#include "SourceDataLocation.h"
                                                     
namespace konga {

	class SourceFromHostedWebServiceCall : public SourceDataLocation {
	public:
		static const std::string GLOBAL_DE_NAME_FOR_REQUEST_PATH;

	public:
		SourceFromHostedWebServiceCall(const std::string& stRequestAsLocalFile) : m_stRequestAsLocalFile(stRequestAsLocalFile) {}
		virtual ~SourceFromHostedWebServiceCall(void) {}

		void doTransfer(const OperationEngineContext& context, ProcessedFiles& files, OperationProgress& progress) const;

		void doPostTransformation(const OperationEngineContext& context, ProcessedFiles& files) const {
			                 
		}

		void getFirstSourceFile(const OperationEngineContext& context, ProcessedFiles& files) const;

	private:
		std::string m_stRequestAsLocalFile;
	};
}

#endif                                                                  
