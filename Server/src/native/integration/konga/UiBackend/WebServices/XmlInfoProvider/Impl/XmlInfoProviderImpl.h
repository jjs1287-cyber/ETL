/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_WS_XML_INFO_PROVIDER_IMPL_H
#define KONGA_WS_XML_INFO_PROVIDER_IMPL_H

#include <string>

#include <axis/AxisUserAPI.hpp>
AXIS_CPP_NAMESPACE_USE

#include "../CXmlStructureFile_Array.hpp"
#include "../CXmlStructureFileDownloadResult.hpp"

#include <konga/UiBackend/WebServices/Utils/WsImplBaseAuthenticator.h>

class CXmlStructureFile;
class CXmlStructureFileUploadResult;

namespace konga {
	class XmlInfoProviderImpl : public WsImplBaseAuthenticator
	{
	public:
		XmlInfoProviderImpl (const char* user, const char* password) :
		  WsImplBaseAuthenticator(user, password) {}
		~XmlInfoProviderImpl(void);

		CXmlStructureFile_Array* GetXmlStructureFiles();

		CXmlStructureFileDownloadResult* GetXmlStructureFile(CXmlStructureFile* const pXmlStructureFile);

		xsd__string_Array* GetPossibleRootNames(CXmlStructureFile* const pXmlStructureFile);

		CXmlStructureFileUploadResult* UploadStructureFile(CXmlStructureFile* const pPstructureFile,
			CXmlStructureFile* const pParentStructureFile);
	};
}

#endif                                     

