/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef WSDLUTILS_H
#define WSDLUTILS_H

#include <string>
#include <vector>
#include <map>

#include <konga/Xml/DOMElementPtr.h>
#include <konga/Xml/DOMDocumentPtr.h>
#include <konga/Xml/DOMNodePtr.h>

typedef std::vector<int>	IntVector;

                                                           

class WsdlUtils
{

public:
	WsdlUtils(void) {}
	~WsdlUtils(void) {}

	static const std::string WSDL_SOAP_NAMESPACE_11;
	static const std::string WSDL_SOAP_NAMESPACE_12;
	static const std::string WSDL_NAMESPACE;

	static const std::string PORT_TYPE;
	static const std::string OPERATION;
	static const std::string MESSAGE;
	static const std::string PART;

	static const std::string NAME_ATTRIBUTE;
	static const std::string TYPE_ATTRIBUTE;
	static const std::string ELEMENT_ATTRIBUTE;

	static std::string	GetImportNodesXPathQuery(const std::string& stWSDLPrefix, const std::string& stXSDPrefix);

	                                                                                        
	                                                                                                
	                                                                                 
	                                             
	static int	SetKnownSchemaLocations(konga::xml::DOMDocument2Ptr pWSDLDoc, 
										const std::string& stWSDLPrefix, 
										const std::string& stXSDPrefix,
										std::string& stError);

	static bool GetWsdlDefinitionSchemaLocation(std::string& stSchemaLocation);

	                                                                                              
	                                                                                    
	                                                      
	                                                                                                 
	                                                                    
	                                         
	static int SetSchemaTargetNamespace(konga::xml::DOMDocument2Ptr pWSDLDoc, 
										konga::xml::DOMElementPtr pSchemaNode, 
										const std::string& stBinding,
										const std::string& stOperation,
										bool bIsInput,
										std::string& stTargetNamespace        ,
										std::string& stError);


private:

	                                                                                                
	                                                                    
	                                         
	static std::string GetNamespaceFromSoapBody(konga::xml::DOMDocument2Ptr pWSDLDoc, 
										   const std::string& stBinding,
										   const std::string& stOperation,
										   bool bIsInput);
};

#endif                              

