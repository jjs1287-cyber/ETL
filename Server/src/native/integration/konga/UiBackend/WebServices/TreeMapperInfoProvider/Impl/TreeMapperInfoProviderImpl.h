/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef KONGA_ws_TREE_MAPPER_INFO_PROVIDER_H
#define KONGA_ws_TREE_MAPPER_INFO_PROVIDER_H

#include <map>
#include <string>

#include <boost/thread.hpp>
#include <boost/thread/mutex.hpp>

#include <konga/UiBackend/WebServices/Utils/WsImplBaseAuthenticator.h>

#include <axis/AxisUserAPI.hpp>
#include <axis/AxisUserAPIArrays.hpp>

#include "../DebuggingParameters.hpp"
#include "../WsTestResult.hpp"
#include "../WebServiceMethodData.hpp"
#include "../WsLoadSourceDataInput.hpp"
#include "../WsLoadSourceResult.hpp"
#include "../WsDataFileIdentifier.hpp"
#include "../WsTestTransformationResult.hpp"
#include "../WsTestOperationResult.hpp"
#include "../WsDataFile.hpp"

AXIS_CPP_NAMESPACE_USE 

namespace konga {

	class TreeMapperInfoProviderImpl : public WsImplBaseAuthenticator
	{
	public:
		                                          
		TreeMapperInfoProviderImpl(const char* user, const char* password) :
		  WsImplBaseAuthenticator(user, password) {}
		~TreeMapperInfoProviderImpl(void) {}

		xsd__string getJtrForStructure(const std::string& stUploadedFilePath, const std::string& stRootName, bool bIsSource);

		xsd__string getJtrForWS(WebServiceMethodData* const wsMethodData);

		xsd__string_Array* getWSDLJtrs(const char* const wsdl, const char* const binding, const char* const operationName,
			const char* const operationStyle, const char* const soapBodyUse);

		WsLoadSourceResult* loadSourceData(WsLoadSourceDataInput* const pLoadSourceDataInput);

		WsDataFile* downloadDataFile(const char* const guid, const char* transformationGuid, const char* operationGuid, WsDataFileIdentifier* pDataFileIdentifier);

		WsTestResult* testExpression(const std::string& stNameValuePairs, const std::string& stExpression, DebuggingParameters* pDebuggingParams);
		
		WsTestTransformationResult* testTransformation(const char* const pTranGuid, const char* const pSourceDataGuid, const char* const pZippedBase64EncodedData, DebuggingParameters* pDebuggingParams);

		WsTestOperationResult* testOperation(const char* OperationGuid, const char* TranGuid, DebuggingParameters* pDebuggingParams);

		void stop(const std::string& sDebugSessionId);

	};
}

#endif                                        

