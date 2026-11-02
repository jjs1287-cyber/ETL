/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#ifndef KONGA_WS_WEBSERVICE_INFOPROVIDER_IMPL_H
#define KONGA_WS_WEBSERVICE_INFOPROVIDER_IMPL_H

#include <string>

#include <axis/AxisUserAPI.hpp>
#include <axis/AxisUserAPIArrays.hpp>

#include <konga/UiBackend/WebServices/Utils/WsImplBaseAuthenticator.h>

AXIS_CPP_NAMESPACE_USE

class CWsdlFile;
class CWsdlFiles;
class CWsdlFileUploadResult;
class CWsdlFileDownloadResult;
class WebServiceJtr;
class WebServiceOperationData;

namespace konga {

	class DbConnection;
	class GuidToIdMapper;
	class UserAccess;

	class WebServiceInfoProviderImpl : public WsImplBaseAuthenticator
	{
	public:
		WebServiceInfoProviderImpl(const char* const user, const char* const password) :
		  WsImplBaseAuthenticator(user, password), m_lWsdlId(-1) {}
		~WebServiceInfoProviderImpl(void) {}

		CWsdlFileUploadResult* UploadWsdl(CWsdlFile* const wsdlFile, CWsdlFile* const parentWsdlFile);

		CWsdlFileDownloadResult* DownloadWsdl(CWsdlFile* const wsdlFile);

		xsd__string_Array* DeleteWsdl(CWsdlFile* const wsdlFile, bool bForce);

		CWsdlFiles* GetWsdlFiles();

		WebServiceJtr* GetJtrs(const WebServiceOperationData* const pOperationData);

	private:
		long m_lWsdlId;
		std::string m_stLocator;

		std::string m_stName;
		std::string m_stUrl;
		std::string m_stGuid;

	};
}	                  

#endif                                           
