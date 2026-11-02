/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef TRANSFORMATION_UTILS_TRANSFORMATIONWEBSERVICEINFO_H
#define TRANSFORMATION_UTILS_TRANSFORMATIONWEBSERVICEINFO_H

#include <string>

#include <konga/WebService/WsdlConstants.h>
#include <konga/FileTransfer/HttpOptions.h>

namespace konga {

	class DbConnection;
	class OperationDataElementHolder;

	class TransformationWebServiceInfo {
	public:
		TransformationWebServiceInfo(void) : DelayMS(0), MaxSizeBytes(0), SchemaStatus(konga::WsdlConstants::Unknown) {}
		~TransformationWebServiceInfo(void) {}

		long DelayMS;
		long MaxSizeBytes;

		std::string	Url;
		std::string TargetNamespace;
		std::string	OtherNamespaces;
		std::string	SoapAction;
		std::string SoapStyle;
		std::string SoapBodyUse;
		std::string RequestXsd;
		std::string RequestJtr;
		std::string ResponseXsd;
		std::string ResponseJtr;
		std::string HttpUserName;
		std::string HttpPassword;

		konga::HttpOptions HttpPostOptions;
		konga::WsdlConstants::WsdlSchemaStatus SchemaStatus;
	};

	void GetWebServiceInfo(int iFunctionID, TransformationWebServiceInfo& wsInfo, const OperationDataElementHolder& deHolder, DbConnection& tranDbConn);
}

#endif                                                       

