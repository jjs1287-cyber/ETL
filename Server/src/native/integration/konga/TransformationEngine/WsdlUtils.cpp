/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */

#include <konga/Utils/CrException.h>
#include <konga/Utils/FileUtils.h>
#include <konga/Utils/StringUtils.h>
#include <konga/Utils/Settings.h>
#include <konga/Utils/util.h>

#include <konga/Xml/DOMNodeListPtr.h>
#include <konga/Xml/XmlUtils.h>
#include <konga/Xml/XmlNamespaceDeclarations.h>
#include <konga/Xml/XmlConstants.h>

#include <konga/Soap/SoapEncoding.h>

#include "SchemaCollection.h"
#include "WsdlUtils.h"

using namespace konga;
using namespace konga::xml;
using namespace std;

const string WsdlUtils::WSDL_SOAP_NAMESPACE_11 = "http://schemas.xmlsoap.org/wsdl/soap/";
const string WsdlUtils::WSDL_SOAP_NAMESPACE_12 = "http://schemas.xmlsoap.org/wsdl/soap12/";
const string WsdlUtils::WSDL_NAMESPACE = "http://schemas.xmlsoap.org/wsdl/";

const string WsdlUtils::PORT_TYPE = "portType";
const string WsdlUtils::OPERATION = "operation";
const string WsdlUtils::MESSAGE = "message";
const string WsdlUtils::PART = "part";

const string WsdlUtils::NAME_ATTRIBUTE = "name";
const string WsdlUtils::TYPE_ATTRIBUTE = "type";
const string WsdlUtils::ELEMENT_ATTRIBUTE = "element";


string WsdlUtils::GetImportNodesXPathQuery(const string& stWSDLPrefix, const string& stXSDPrefix)
{
	std::string stXPath = "//" + stWSDLPrefix;
	stXPath += ":definitions/" + stWSDLPrefix;
	stXPath += ":types/" + stXSDPrefix;
	stXPath += ":schema/" + stXSDPrefix;
	stXPath += ":import";
	return stXPath;
}

int WsdlUtils::SetKnownSchemaLocations(DOMDocument2Ptr pWSDLDoc,
									   const string& stWSDLPrefix,
									   const string& stXSDPrefix,
									   string& stError)
{
	try
	{
		DOMNodeListPtr pImportNodes = pWSDLDoc->selectNodes(
			GetImportNodesXPathQuery(stWSDLPrefix, stXSDPrefix));
		for( int i = 0, iMax = pImportNodes->getLength(); i < iMax; ++i )
		{
			DOMElementPtr pImport = pImportNodes->item[i];
			string stNamespace = XmlUtils::GetAttributeValue(pImport, XmlConstants::NAMESPACE);
			if( Equals(stNamespace, WSDL_NAMESPACE) )
			{
				string stSchemaLocation;
				if( GetWsdlDefinitionSchemaLocation(stSchemaLocation) )
					pImport->setAttribute(XsdConstants::SCHEMALOCATION_ATTRIBUTE, stSchemaLocation);
				else
				{
					stError = "Failed to get the location of the cached WSDL standard schema.";
					return 1;
				}
			}
			else if( Equals(stNamespace, SoapEncoding::VERSION_11_NAMESPACE) )
			{
				string stSchemaLocation;
				if( konga::SoapEncoding::GetSchemaLocation(konga::SoapEncoding::SoapEncoding_Version_11, stSchemaLocation) )
					pImport->setAttribute(XsdConstants::SCHEMALOCATION_ATTRIBUTE, stSchemaLocation);
				else
				{
					stError = "Failed to get the location of the cached SOAP-Encoding (version 1.1) standard schema.";
					return 2;
				}
			}
			  
                                                                                                        
                                                                     
    
                            
                                                                                                  
                                                                                 
        
     
                         
                   
     
    
     
		}
		return 0;
	}
	catch( CrException& e )
	{
		stError = e.GetErrorString();
		return 4;
	}
}

bool WsdlUtils::GetWsdlDefinitionSchemaLocation(string& stSchemaLocation)
{
	const string stWsdlSchemaLocation(
		getAbsolutePath(GetOptionAsString("Settings/XMLSchemas", "WSDL",
		"DataInterchange/xml_dtd/Schemas/WSDL.xsd"))
	);
	if ( konga::FileExists(stWsdlSchemaLocation) ) {
		stSchemaLocation = stWsdlSchemaLocation;
		return true;
	}
	return false;
}


                                                                                          
                                                                                    
                                                      
                                                                                                
                       
                                                                    
                                         
int WsdlUtils::SetSchemaTargetNamespace(DOMDocument2Ptr pWSDLDoc, 
										DOMElementPtr pSchemaNode,
										const string& stBinding,
										const string& stOperation,
										bool bIsInput,
										string& stTargetNamespace        ,
										string& stError)
{
	try
	{
		                                                                                
		if( !XmlUtils::GetAttributeValue(pSchemaNode, XmlConstants::TARGET_NAMESPACE).empty() )
			return 0;

		                                                                  
		                                               
		stTargetNamespace = GetNamespaceFromSoapBody(pWSDLDoc, stBinding, stOperation, bIsInput);
		if (stTargetNamespace.empty()) {
			                                                                                   
			                                                                                 
			                                                                                   
			                                                                                   
			                                                                        
			stTargetNamespace = GetNamespaceFromSoapBody(pWSDLDoc, stBinding, stOperation, !bIsInput);
		}
		if (stTargetNamespace.empty()) {
			                                                                                
			                                                                   
			stError = "There is no namespace defined on the body node in the operation '";
			stError += stOperation;
			stError += "' in the binding '";
			stError += stBinding;
			stError += "'.";
			return 1;
		}
		pSchemaNode->setAttribute( XmlConstants::TARGET_NAMESPACE, stTargetNamespace );
	}
	catch( CrException& e )
	{
		stError = e.GetErrorString();
		return 2;
	}
	return 0;
}


string WsdlUtils::GetNamespaceFromSoapBody(DOMDocument2Ptr pWSDLDoc, 
										   const string& stBinding,
										   const string& stOperation,
										   bool bIsInput)
{
	std::string stQuery = "//wsdl:definitions/wsdl:binding[@name='";
	stQuery += stBinding;
	stQuery += "']/wsdl:operation[@name='";
	stQuery += stOperation;
	stQuery += bIsInput ? "']/wsdl:input" : "']/wsdl:output";
	stQuery += "/soap:body";
	DOMElementPtr pBodyNode = pWSDLDoc->selectSingleNode(stQuery);
	return pBodyNode == 0 ? "" : XmlUtils::GetAttributeValue(pBodyNode, XmlConstants::NAMESPACE);
}
