/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#include <map>

#include <xercesc/parsers/SAXParser.hpp>
#include <xercesc/sax/SAXException.hpp>

#include <konga/Utils/MapUtils.h>
#include <konga/Utils/FileUtils.h>
#include <konga/Utils/PortDefs.h>	                         
#include <konga/Utils/XmlInit.h>

#include "XsdConstants.h"
#include "XmlStrings.h"
#include "SimpleSAXHandler.h"
#include "XmlUtils.h"
#include "XmlSchemaUtils.h"

XERCES_CPP_NAMESPACE_USE

using namespace konga;
using namespace konga::xml;

using std::string;
using std::vector;
using std::map;

                     
namespace {
	const string WSDL_NAMESPACE("http://schemas.xmlsoap.org/wsdl/");

	const string XSD_SELECT_PREFIX("xsd");
	const string WSDL_SELECT_PREFIX("wsdl");

	const string XSD_INCLUDE_XPATH("//" + XSD_SELECT_PREFIX + ":" + XsdConstants::INCLUDE);
	const string XSD_IMPORT_XPATH("//" + XSD_SELECT_PREFIX + ":" + XsdConstants::IMPORT);

	const string WSDL_INCLUDE_XPATH("//" + WSDL_SELECT_PREFIX + ":" + XsdConstants::INCLUDE);
	const string WSDL_IMPORT_XPATH("//" + WSDL_SELECT_PREFIX + ":" + XsdConstants::IMPORT);

	const string WSDLLOCATION_ATTRIBUTE("location");

	                                                                                
	                                                                    
	void AddLocations(const DOMNodeListPtr& pNodeList, const string& stBranchPath,
		vector<string>& vExistingIncludes, vector<string>& vMissingIncludes,
		const string& stNameOfLocationAttribute)
	{
		const unsigned long nIncludes = pNodeList->getLength();
		if ( nIncludes == 0  )
			return;

		for( unsigned long index = 0; index < nIncludes; ++index ) {
			const DOMNodePtr node( pNodeList->item[index] );

			const string& stSchemaLocation =
				XmlUtils::GetAttributeValue(node, stNameOfLocationAttribute);

			                                          
			if ( stSchemaLocation.empty() ||
				strncasecmp(stSchemaLocation.c_str(), "http://", 7) == 0 ||
				strncasecmp(stSchemaLocation.c_str(), "https://", 8) == 0 ) continue;

			                                                                                                 
			const string stRelativePath( stBranchPath + stSchemaLocation );
			if ( FileExists(stRelativePath) ) {
				vExistingIncludes.push_back( stSchemaLocation );
				continue;
			}

			                                              
			if ( FileExists(stSchemaLocation) ) {
				vExistingIncludes.push_back( stSchemaLocation );
				continue;
			}

			                   
			vMissingIncludes.push_back( stSchemaLocation );
		}
	}

	                                 
	void FindIncludesAndImports(const string& stFileName,
		vector<string>& vExistingIncludes, vector<string>& vMissingIncludes,
		bool IsXsd)
	{
		vExistingIncludes.clear();
		vMissingIncludes.clear();

		                                             
		string stError;
		DOMDocumentPtr pDoc = XmlUtils::LoadDoc(stFileName, stError, false);
		if ( !stError.empty() )
			throw CrException(stError);

		const string& stBranchPath = GetBranchPath(stFileName);

		pDoc->addSelectionNamespace(XSD_SELECT_PREFIX, XsdConstants::SCHEMA_DEFINITION_NAMESPACE_2001);
		if ( !IsXsd ) {
			pDoc->addSelectionNamespace(WSDL_SELECT_PREFIX, WSDL_NAMESPACE);
		}

		                         
		DOMNodeListPtr pNodeList( pDoc->selectNodes(XSD_INCLUDE_XPATH) );
		AddLocations(pNodeList, stBranchPath, vExistingIncludes, vMissingIncludes, XsdConstants::SCHEMALOCATION_ATTRIBUTE);

		                       
		pNodeList = pDoc->selectNodes(XSD_IMPORT_XPATH);
		AddLocations(pNodeList, stBranchPath, vExistingIncludes, vMissingIncludes, XsdConstants::SCHEMALOCATION_ATTRIBUTE);

		                                                                       
		                         
		if ( !IsXsd ) {
			pNodeList = pDoc->selectNodes(WSDL_IMPORT_XPATH);
			AddLocations(pNodeList, stBranchPath, vExistingIncludes, vMissingIncludes, WSDLLOCATION_ATTRIBUTE);

			pNodeList = pDoc->selectNodes(WSDL_INCLUDE_XPATH);
			AddLocations(pNodeList, stBranchPath, vExistingIncludes, vMissingIncludes, WSDLLOCATION_ATTRIBUTE);
		}

	}
}                          

void konga::XmlSchemaUtils::FindXsdIncludesAndImports(const string& stXsdFileName,
													  vector<string>& vExistingIncludes,
													  vector<string>& vMissingIncludes)
{
	FindIncludesAndImports(stXsdFileName, vExistingIncludes, vMissingIncludes, true);
}

void XmlSchemaUtils::FindWsdlIncludesAndImports(const std::string& stWsdlFileName,
	std::vector<std::string>& vExistingIncludes, std::vector<std::string>& vMissingIncludes )
{
	FindIncludesAndImports(stWsdlFileName, vExistingIncludes, vMissingIncludes, false);
}

XmlSchemaUtils::XmlSchemaType XmlSchemaUtils::GetSchemaType(const std::string& stXmlSchemaFileName) {
	                                                                       
	const string stExt( GetFileExtension(stXmlSchemaFileName) );
	if ( strcasecmp(stExt.c_str(), "xsd") == 0 )
		return XML_SCHEMA;
	else                                                                                                              
		return XML_DTD;

	                                                                                                                    
}

namespace {

	class CancelException {
	public:
		CancelException(const string& stError = "") : m_stError(stError) {}
		~CancelException(void) {}

		bool hasError() const {
			return !m_stError.empty();
		}

		string getError() const {
			return m_stError;
		}

	private:
		string m_stError;
	};

	void AddNamespaces(const AttributeList& attrs, map<string,string>& nsMap) {
		const unsigned int size = attrs.getLength();
		for( unsigned int index = 0; index < size; ++index ) {
			const string stQualifiedName( XMLChToString(attrs.getName(index)) );
			string stPrefix, stLocalName;
			XmlUtils::GetPrefixAndLocalName(stQualifiedName, stPrefix, stLocalName);
			if ( stPrefix == XsdConstants::XMLNS ) {
				const string stValue( XMLChToString(attrs.getValue(index)) );
				AddOrUpdate< map<string,string> , string, string>(nsMap, stLocalName, stValue);
			}
			else if ( stLocalName == XsdConstants::XMLNS ) {
				                                                                                        
				const string stValue( XMLChToString(attrs.getValue(index)) );
				AddOrUpdate< map<string,string> , string, string>(nsMap, "", stValue);
			}
		}
	}

	class HandlerWithNS : public SimpleSAXHandler {
	public:
		HandlerWithNS(void) : SimpleSAXHandler(){}
		virtual ~HandlerWithNS(void) {}

		virtual void startElement (const XMLCh* const name, AttributeList& attrs)
		{
			AddNamespaces(attrs, m_namespaces);
			const string stQualifiedName(XMLChToString(name));
			string stPrefix, stLocalName;
			XmlUtils::GetPrefixAndLocalName(stQualifiedName, stPrefix, stLocalName);
			startElement(stPrefix, stLocalName);
		}

		virtual void startElement(const string& prefix, const string& localName) = 0;

	protected:
		map<string,string> m_namespaces;
	};

	class IsSchemaFileHandler : public HandlerWithNS {
	public:
		IsSchemaFileHandler(void) : HandlerWithNS(), m_IsSchema(false) {}
		~IsSchemaFileHandler(void) {}

		void startElement(const string& stPrefix, const string& stLocalName) {
			if ( stLocalName == XsdConstants::SCHEMA ) {
				                                                           
				map<string,string>::const_iterator cit = m_namespaces.find(stPrefix);
				if ( cit != m_namespaces.end() ) {
					if ( cit->second == XsdConstants::SCHEMA_DEFINITION_NAMESPACE_2001
						|| cit->second == XsdConstants::SCHEMA_DEFINITION_NAMESPACE_1999 ) {
						m_IsSchema = true;
						throw CancelException();
					}
				}
			}
		}

		bool IsSchema() const {
			return m_IsSchema;
		}

	private:
		bool m_IsSchema;
	};

	const string WSDL_DEFINITIONS("definitions");

	class IsWsdlFileHandler : public HandlerWithNS {
	public:
		IsWsdlFileHandler(void) : HandlerWithNS(), m_IsWsdl(false) {}
		~IsWsdlFileHandler(void) {}

		void startElement(const string& stPrefix, const string& stLocalName) {
			if ( stLocalName == WSDL_DEFINITIONS ) {
				                                                           
				map<string,string>::const_iterator cit = m_namespaces.find(stPrefix);
				if ( cit != m_namespaces.end() ) {
					if ( cit->second == WSDL_NAMESPACE ) {
						m_IsWsdl = true;
						throw CancelException();
					}
				}
			}
		}

		bool IsWsdl() const {
			return m_IsWsdl;
		}

	private:
		bool m_IsWsdl;
	};

	void HandleDocument(const string& stFileName, string& stError, SimpleSAXHandler& handler)
	{
		try {
			konga::XmlInitializer::InitXerces();
			SAXParser parser;
			parser.setErrorHandler(&handler);
			parser.setDocumentHandler(&handler);
			parser.setDoSchema(true);
			parser.setDoNamespaces(true);
			parser.setValidationScheme(SAXParser::Val_Never);

			parser.parse(stFileName.c_str());
			if ( handler.hasErrors() ) {
				stError = "Error: " + handler.getErrors();
				const string& stWarning = handler.getWarnings();
				if ( !stWarning.empty() ) {
					stError += "\nWarning: " + stWarning;
				}
				return;
			}
			stError.clear();
		}
		catch( const CancelException & ) {
			                        
		}
		catch( const SAXException& e) {
			stError = XMLChToString( e.getMessage() );
		}
		catch( const XMLException& e) {
			stError = XMLChToString( e.getMessage() );
		}
		catch( ... ) {
			stError = "Unknown error.";
		}
	}
}

bool XmlSchemaUtils::IsSchemaFile(const string& stFileName, string& stError) {

	IsSchemaFileHandler handler;
	HandleDocument(stFileName, stError, handler);
	if ( !stError.empty() )
		return false;
	return handler.IsSchema();
}

bool XmlSchemaUtils::IsWsdlFile(const std::string& stFileName, string& stError) {
	IsWsdlFileHandler handler;
	HandleDocument(stFileName, stError, handler);
	if ( !stError.empty() )
		return false;
	return handler.IsWsdl();
}
