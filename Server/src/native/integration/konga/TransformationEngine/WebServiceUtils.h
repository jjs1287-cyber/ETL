/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef WEBSERVICEUTILS_H
#define WEBSERVICEUTILS_H  

#include <string>
#include <vector>
#include <map>

#include <konga/Xml/DOMElementPtr.h>
#include <konga/Xml/DOMDocumentPtr.h>
#include <konga/Xml/DOMNodePtr.h>
#include <konga/Xml/XsdConstants.h>

#include <konga/Soap/SoapConstants.h>

#include <konga/TransformationLogging/ErrorLog.h>

class XSDParser;
class CROM;

namespace konga {
	class HttpOptions;
}

class WebServiceUtils
{
public:
	WebServiceUtils(void) {};
	~WebServiceUtils(void) {};

	static const char NAMESPACE_SEPARATOR;

	static unsigned long CallWebService(const std::string& stUrl,
										const std::string& stSoapAction,
										const std::string& stRequestXmlFileName,
										const std::string& stResponseXmlFileName,
										long lMaxSizeBytes,
										const std::string& stCookiejarFileName,
										std::string& stLastErr,
										const std::string& stHttpUserName,
										const std::string& stHttpDecryptedPassword,
										const konga::HttpOptions& httpOptions,
										int follow_redirs,
										int numberOfRetries,
										int retryWaitInSeconds,
										long timeOutSeconds);

	static unsigned long GetOperationList(const std::string&stWsdlFilePath,
									std::vector<std::string>&vMethodNames,
									std::string&stErr);


	static unsigned long SetSchemaTypeAttribute(konga::xml::DOMElementPtr pDOMNode, 
										  CROM* pCROMNode, 
										  const std::string& stAttributeName,
										  std::string& stError);

	static unsigned long ResolveHREFs(konga::xml::DOMDocument2Ptr pDoc, 
								konga::xml::DOMElementPtr pEnvelopeNode,
								konga::SoapConstants::Version soapVersion,
								XsdConstants::SchemaVersion schemaVersion,
								std::string& stError);

private:
	static unsigned long FindAndReplaceHREFs(konga::xml::DOMDocument2Ptr pDoc,
									   konga::xml::DOMElementPtr pElement,
									   const std::string& stSchemaInstancePrefix,
									   std::string& stError);
	static std::string  GetIDNodeXPath(const std::string& stID);

};

#endif                                    
