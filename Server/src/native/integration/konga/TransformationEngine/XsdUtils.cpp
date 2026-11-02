/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
#include <map>
#include <string>
#include <vector>

#include <boost/thread/mutex.hpp>

#include <konga/Utils/ErrorResolver.h>
#include <konga/Utils/CrException.h>
#include <konga/Utils/Settings.h>
#include <konga/Utils/StringUtils.h>
#include <konga/Utils/util.h>
#include <konga/Utils/FileUtils.h>	                
#include <konga/Utils/PortDefs.h>	                                

#include <konga/Xml/XmlUtils.h>
#include <konga/Xml/ExpandedName.h>
#include <konga/Xml/XmlNamespaceDeclarations.h>
#include <konga/Xml/DOMNodeListPtr.h>
#include <konga/Xml/XmlConstants.h>

#include <konga/Soap/SoapEncoding.h>

#include "XSDParser.h"
#include "SchemaCollection.h"
#include "WsdlUtils.h"
#include "CromConstants.h"
#include "XsdUtils.h"

using namespace XsdConstants;
using namespace std;
using namespace konga;
using namespace konga::xml;

namespace {
	const int	s_numberOfSchemaTypes = 44;
	const char*  s_szSchemaTypes[] = {"string", "normalizedString", "token", "Name", "QName", "NCName",
											"anyURI", "language", "ID", "IDREF", "IDREFS", "ENTITY",
											"ENTITIES", "NOTATION", "NMTOKEN", "NMTOKENS",	                    
											"byte", "unsignedByte", "integer", "positiveInteger",
											"negativeInteger", "nonNegativeInteger", "nonPositiveInteger",
											"int", "unsignedInt", "short", "unsignedShort",                  
											"long", "unsignedLong",               
											"decimal",              
											"float",             
											"double",              
											"boolean",               
											"time", "dateTime", "date",               
											"duration",                
											"gMonth", "gYear", "gYearMonth", "gDay", "gMonthDay",                              
											"base64Binary", "hexBinary", 0};                              

	map<string, string> s_mapSchemaTypeToCrType;	                                               
}

int		ReplaceSoapArrayTypesImpl(XSDParser* parser, string& stError);
int		SetAnyElementProcessContentsAttributeImpl(XSDParser* parser,
												  ProcessContentsValue value,
												  string& stError);

XsdConstants::XsdNodeType XsdUtils::GetXsdNode(const konga::xml::DOMElementPtr& pDOMNode)
{
	const string& stName = konga::XmlUtils::GetNodeBaseName(pDOMNode);
	if( konga::Equals( stName, ELEMENT ) )
		return ElementNode;
	else if( konga::Equals( stName, ATTRIBUTE ) )
		return AttributeNode;
	else if( konga::Equals( stName, COMPLEX_TYPE ) )
		return ComplexTypeNode;
	else if( konga::Equals( stName, SEQUENCE ) )
		return SequenceNode;
	else if( konga::Equals( stName, ALL ) )
		return AllNode;
	else if( konga::Equals( stName, COMPLEX_CONTENT ) )
		return ComplexContentNode;
	else if( konga::Equals( stName, CHOICE ) )
		return ChoiceNode;
	else if( konga::Equals( stName, SIMPLE_TYPE ) )
		return SimpleTypeNode;
	else if( konga::Equals( stName, RESTRICTION ) )
		return RestrictionNode;
	else if( konga::Equals( stName, EXTENSION ) )
		return ExtensionNode;
	else if( konga::Equals( stName, ANY ) )
		return AnyNode;
	else if( konga::Equals( stName, ENUMERATION ) )
		return EnumerationNode;
	else if( konga::Equals( stName, ANY_ATTRIBUTE ) )
		return AnyAttributeNode;
	else if( konga::Equals( stName, ANNOTATION ) )
		return AnnotationNode;
	else if( konga::Equals( stName, XsdConstants::GROUP ) )
		return GroupNode;
	else if( konga::Equals( stName, ATTRIBUTE_GROUP ) )
		return AttributeGroupNode;

	return UnknownNode;
}


konga::xml::DOMNodePtr XsdUtils::FindSchemaElement(const konga::xml::DOMNodePtr& pNode0)
{
    konga::xml::DOMNodePtr pNode = pNode0;
	if(pNode == 0){
		return 0;
	}
	while(pNode != 0)
	{
		if(pNode->getNodeType() == konga::xml::NODE_ELEMENT && stricmp(pNode->getBaseName().c_str(), SCHEMA.c_str()) == 0){
			return (konga::xml::DOMElementPtr)pNode;
		}
		pNode = pNode->getNextSibling();
	}
	return 0;
}

bool XsdUtils::IsTrue(DOMElementPtr pNode, const string& stAttributeName)
{
	string value = XmlUtils::GetAttributeValue(pNode, stAttributeName);
	return konga::EqualsIgnoreCase(value, XsdConstants::VALUE_TRUE) || konga::Equals(value, "1");
}

void XsdUtils::GetMinMaxOccurs(konga::xml::DOMElementPtr pNode, int& iMinUse, int& iMaxUse)
{
	iMinUse = iMaxUse = 1;
	if(pNode == 0 || pNode->getNodeType() != konga::xml::NODE_ELEMENT)
		return;

	string stMinOccurs = konga::XmlUtils::GetAttributeValue(pNode, MIN_OCCURS);
	string stMaxOccurs = konga::XmlUtils::GetAttributeValue(pNode, MAX_OCCURS);
	if( !stMinOccurs.empty() )
	{
		if( stricmp( stMinOccurs.c_str(), UNBOUNDED.c_str() ) == 0 || stricmp( stMinOccurs.c_str(), "*") == 0 )
			iMinUse = INT_MAX;
		else
			iMinUse = atoi(stMinOccurs.c_str());
	}
	if( !stMaxOccurs.empty() )
	{
		if( stricmp( stMaxOccurs.c_str(), UNBOUNDED.c_str() ) == 0 || stricmp( stMaxOccurs.c_str(), "*") == 0 )
			iMaxUse = INT_MAX;
		else
			iMaxUse = atoi(stMaxOccurs.c_str());
	}
}

string XsdUtils::GetArrayItemName(const string& stArrayType) {
	string prefix;
	string local;
	XmlUtils::GetPrefixAndLocalName(stArrayType, prefix, local);
	return local + string("_Item");
}


int XsdUtils::SetAnyElementProcessContentsAttribute(const string& stInputXsdFilePath,
													const string& stOutputXsdFilePath,
													ProcessContentsValue value,
													string& stError)
{
	XSDParser xsdParser(getTreeMapper());
	SchemaCollection collection;
	                                                                                      
	unsigned long hr = xsdParser.LoadSchema(stInputXsdFilePath, collection, false, false);
	if( FAILED(hr) )
	{
		stError = xsdParser.GetLastError();
		return 1;
	}

	hr = SetAnyElementProcessContentsAttributeImpl(&xsdParser, value, stError);
	if( FAILED(hr) )
		return 2;

	hr = xsdParser.GetSchemaDOMDocument()->save(stOutputXsdFilePath);
	if( FAILED(hr) )
	{
		stError = "Failed to write the schema file '";
		stError += stOutputXsdFilePath;
		stError += "' to disk. Reported error: ";
		stError += konga::XmlUtils::GetLastError( xsdParser.GetSchemaDOMDocument() );
		return 3;
	}

	return 0;
}

int XsdUtils::SetAnyElementProcessContentsAttribute(konga::xml::DOMDocument2Ptr pSchemaDoc,
													ProcessContentsValue value,
													string& stError)
{
	XSDParser xsdParser(getTreeMapper());
	SchemaCollection collection;
	                                                                                      
	unsigned long hr = xsdParser.LoadSchema(pSchemaDoc, collection, false, "");
	if( FAILED(hr) )
	{
		stError = xsdParser.GetLastError();
		return 1;
	}

	return SetAnyElementProcessContentsAttributeImpl(&xsdParser, value, stError);
}

int SetAnyElementProcessContentsAttributeImpl(XSDParser* parser,
													ProcessContentsValue value,
													string& stError)
{
	XsdUtils xsdutils(parser->getTreeMapper());
	try
	{
		XsdConstants::SchemaVersion schemaVersion = parser->GetSchemaVersion();
		if( schemaVersion == XsdConstants::SchemaVersion_Unknown )
			schemaVersion = xsdutils.GetDefaultSchemaVersion();

		konga::xml::DOMDocument2Ptr pDoc = parser->GetSchemaDOMDocument();

		const string& stSchemaDefinitionNamespace = xsdutils.GetSchemaDefinitionNamespace(schemaVersion);
		pDoc->addSelectionNamespace("xsd", stSchemaDefinitionNamespace);

		string stProcessContents;
		switch( value )
		{
		case Lax:
			stProcessContents = PROCESS_CONTENTS_LAX;
			break;
		case Skip:
			stProcessContents = PROCESS_CONTENTS_SKIP;
			break;
		case Strict:
			stProcessContents = PROCESS_CONTENTS_STRICT;
			break;
		}
		
		string stQuery = "//xsd:any";
		konga::xml::DOMNodeListPtr pAnyNodes = pDoc->selectNodes(stQuery);
		for( int i = 0, nNodes = pAnyNodes->getLength(); i < nNodes; ++i )
		{
			konga::xml::DOMElementPtr pAnyNode = pAnyNodes->item[i];
			pAnyNode->setAttribute( PROCESS_CONTENTS, stProcessContents );
		}
	}
	catch( CrException& e )
	{
		stError = e.GetErrorString();
		return 1;
	}
	return 0;
}

int XsdUtils::ReplaceSoapArrayTypes(konga::xml::DOMDocument2Ptr pSchemaDoc, string& stError)
{
	XSDParser xsdParser(getTreeMapper());
	SchemaCollection collection;
	                                                                                      
	unsigned long hr = xsdParser.LoadSchema(pSchemaDoc, collection, false, "");
	if( FAILED(hr) )
	{
		stError = xsdParser.GetLastError();
		return 1;
	}

	return ReplaceSoapArrayTypesImpl(&xsdParser, stError);
}

int XsdUtils::ReplaceSoapArrayTypes(const string& stInputXsdFilePath,
									const string& stOutputXsdFilePath,
									string& stError)
{
	XSDParser xsdParser(getTreeMapper());
	SchemaCollection collection;
	                                                                                      
	unsigned long hr = xsdParser.LoadSchema(stInputXsdFilePath, collection, false, false);
	if( FAILED(hr) )
	{
		stError = xsdParser.GetLastError();
		return 1;
	}

	int iErr = ReplaceSoapArrayTypesImpl(&xsdParser, stError);
	if( iErr != 0 )
		return iErr;

	try
	{
		hr = xsdParser.GetSchemaDOMDocument()->save(stOutputXsdFilePath);
		if( FAILED(hr) )
		{
			stError = konga::XmlUtils::GetLastError( xsdParser.GetSchemaDOMDocument() );
			return 2;
		}
	}
	catch( CrException& e )
	{
		stError = e.GetErrorString();
		return 3;
	}

	                                                                                                              
	return 0;
}

int ReplaceSoapArrayTypesImpl(XSDParser* parser, string& stError)
{
	try
	{
		XsdUtils xsdutils(parser->getTreeMapper());
		XsdConstants::SchemaVersion schemaVersion = parser->GetSchemaVersion();
		if( schemaVersion == XsdConstants::SchemaVersion_Unknown )
			schemaVersion = xsdutils.GetDefaultSchemaVersion();

		konga::xml::DOMDocument2Ptr pDoc = parser->GetSchemaDOMDocument();
		konga::xml::DOMElementPtr pSchemaNode = pDoc->getDocumentElement();
		konga::xml::XmlNamespaceDeclarations schemaNamespaces(pSchemaNode);

		const string& stSchemaDefinitionNamespace = xsdutils.GetSchemaDefinitionNamespace(schemaVersion);
		string stSoapEncNamespacePrefix = schemaNamespaces.GetNamespacePrefix(SoapEncoding::VERSION_11_NAMESPACE);

		pDoc->addSelectionNamespace("xsd", stSchemaDefinitionNamespace);
		pDoc->addSelectionNamespace(stSoapEncNamespacePrefix, SoapEncoding::VERSION_11_NAMESPACE);

		                                                                                  
		string stSchemaPrefix = "";
		if( !schemaNamespaces.IsDefaultNamespace(stSchemaDefinitionNamespace) )
			stSchemaPrefix = schemaNamespaces.GetNamespacePrefix(stSchemaDefinitionNamespace);

		const string& stRestrictionNodeName = konga::XmlUtils::GetQualifiedName(stSchemaPrefix, RESTRICTION);
		const string& stSequenceNodeName = konga::XmlUtils::GetQualifiedName(stSchemaPrefix, SEQUENCE);
		const string& stElementNodeName = konga::XmlUtils::GetQualifiedName(stSchemaPrefix, ELEMENT);
		const string& stAttributeNodeName = konga::XmlUtils::GetQualifiedName(stSchemaPrefix, ATTRIBUTE);
		const string& stAnyTypeNodeName = konga::XmlUtils::GetQualifiedName(stSchemaPrefix, ANY_TYPE);

		StringVector vstSoapEncPrefixes;
		schemaNamespaces.GetNamespacePrefixes(konga::SoapEncoding::VERSION_11_NAMESPACE, vstSoapEncPrefixes);
		for(size_t i = 0, iMax = vstSoapEncPrefixes.size(); i < iMax; ++i )
		{
			stSoapEncNamespacePrefix = vstSoapEncPrefixes[i];
			const string& stSoapEncArrayType = stSoapEncNamespacePrefix + ":arrayType";

			const string& stXPath_Restriction = "//xsd:restriction[@base='" + stSoapEncNamespacePrefix + string(":Array']");
			const string& stXPath_ArrayTypeAttribute = "xsd:attribute[@ref='" + stSoapEncArrayType + string("']");

			konga::xml::DOMNodeListPtr pRestrictionNodes = pDoc->selectNodes(stXPath_Restriction);
			string stArrayTypeAttribName = WsdlUtils::WSDL_NAMESPACE + string(":arrayType");
			for( int i = 0, n = pRestrictionNodes->getLength(); i < n; ++i )
			{
				konga::xml::DOMElementPtr pRestrictionNode = pRestrictionNodes->item[i];
				konga::xml::DOMElementPtr pTypeNode = pRestrictionNode->selectSingleNode(stXPath_ArrayTypeAttribute);
				if( pTypeNode == 0 )
					continue;

				konga::xml::DOMNamedNodeMapPtr pAttributes = pTypeNode->getAttributes();
				for( int j = 0, m = pAttributes->getLength(); j < m; ++j )
				{
					konga::xml::DOMAttributePtr pA = pAttributes->item[j];
					string stBaseName, stNamespaceURI;
					konga::XmlUtils::GetNodeNameAndNamespace(pA, stBaseName, stNamespaceURI);
					if( strcmp(stNamespaceURI.c_str(), WsdlUtils::WSDL_NAMESPACE.c_str()) == 0 && stricmp(stBaseName.c_str(), "arrayType") == 0 )
					{
						                                                                                      
						                                                                                                    
						                                                                                                   
						                                                                                              
						konga::xml::DOMElementPtr pNewRestriction = pDoc->createNode(
							konga::xml::NODE_ELEMENT, stRestrictionNodeName, stSchemaDefinitionNamespace);
						pNewRestriction->setAttribute( BASE_ATTRIBUTE, stAnyTypeNodeName );
						konga::xml::DOMElementPtr pSequence = pDoc->createNode(
							konga::xml::NODE_ELEMENT, stSequenceNodeName, stSchemaDefinitionNamespace);
						konga::xml::DOMElementPtr pElement = pDoc->createNode(
							konga::xml::NODE_ELEMENT, stElementNodeName, stSchemaDefinitionNamespace);
						konga::xml::DOMElementPtr pNewAttribute = pDoc->createNode(
							konga::xml::NODE_ELEMENT, stAttributeNodeName, stSchemaDefinitionNamespace);
						pNewAttribute->setAttribute( REF_ATTRIBUTE, stSoapEncArrayType);

						pNewRestriction->appendChild(pSequence);
						pSequence->appendChild(pElement);
						pNewRestriction->appendChild(pNewAttribute);
						
						const string& stArrayType = konga::XmlUtils::GetAttributeValue(pA);
						size_t pos = stArrayType.find('[');
						const string& stArrayType2 = stArrayType.substr(0, pos);
						pElement->setAttribute( NAME_ATTRIBUTE, xsdutils.GetArrayItemName(stArrayType2) );
						pElement->setAttribute( TYPE_ATTRIBUTE, stArrayType2 );
						pElement->setAttribute( MIN_OCCURS , "0" );
						pElement->setAttribute( MAX_OCCURS, UNBOUNDED );

						konga::xml::DOMNodePtr pParent = pRestrictionNode->getParentNode();
						pParent->replaceChild(pNewRestriction, pRestrictionNode);
						break;
					}
				}
			}
		}
	}
	catch( CrException& e )
	{
		stError = e.GetErrorString();
		return 1;
	}
	return 0;
}

konga::xml::DOMElementPtr XsdUtils::CreateSchemaElementNode(konga::xml::DOMDocument2Ptr pDoc,
														   XsdConstants::SchemaVersion schemaVersion,
														   const string& stName,
														   const string& stSchemaNamespacePrefix)
{
	if( schemaVersion == XsdConstants::SchemaVersion_Unknown )
		schemaVersion = XsdUtils::GetDefaultSchemaVersion();
	string stNodeName = konga::XmlUtils::GetQualifiedName(stSchemaNamespacePrefix, ELEMENT);
	konga::xml::DOMElementPtr pElement = pDoc->createNode(konga::xml::NODE_ELEMENT, stNodeName, GetSchemaDefinitionNamespace(schemaVersion));
	if( stName.length() > 0 )
	{
		pElement->setAttribute( NAME_ATTRIBUTE, stName );
	}
	return pElement;
}

konga::xml::DOMElementPtr XsdUtils::CreateSchemaComplexTypeNode(konga::xml::DOMDocument2Ptr pDoc,
															   XsdConstants::SchemaVersion schemaVersion,
															   const string& stTypeName,
															   const string& stSchemaNamespacePrefix)
{
	if( schemaVersion == XsdConstants::SchemaVersion_Unknown )
		schemaVersion = XsdUtils::GetDefaultSchemaVersion();
	string stNodeName = konga::XmlUtils::GetQualifiedName(stSchemaNamespacePrefix, COMPLEX_TYPE);
	konga::xml::DOMElementPtr pType = pDoc->createNode(konga::xml::NODE_ELEMENT, stNodeName, GetSchemaDefinitionNamespace(schemaVersion));
	if( stTypeName.length() > 0 )
	{
		pType->setAttribute( NAME_ATTRIBUTE, stTypeName);
	}
	return pType;
}

konga::xml::DOMElementPtr XsdUtils::CreateSchemaSequenceNode(konga::xml::DOMDocument2Ptr pDoc,
															XsdConstants::SchemaVersion schemaVersion,
															const string& stSchemaNamespacePrefix)
{
	if( schemaVersion == XsdConstants::SchemaVersion_Unknown )
		schemaVersion = XsdUtils::GetDefaultSchemaVersion();
	string stNodeName = konga::XmlUtils::GetQualifiedName(stSchemaNamespacePrefix, SEQUENCE);
	return pDoc->createNode(konga::xml::NODE_ELEMENT, stNodeName, GetSchemaDefinitionNamespace(schemaVersion));
}

konga::xml::DOMElementPtr XsdUtils::CreateSchemaImportNode(konga::xml::DOMDocument2Ptr pDoc,
														  XsdConstants::SchemaVersion schemaVersion,
														  const string& stImportNamespace,
														  const string& stSchemaLocation,
														  const string& stSchemaNamespacePrefix)
{
	if( schemaVersion == XsdConstants::SchemaVersion_Unknown )
		schemaVersion = XsdUtils::GetDefaultSchemaVersion();
	const string& stNodeName = konga::XmlUtils::GetQualifiedName(stSchemaNamespacePrefix, IMPORT);
	konga::xml::DOMElementPtr pImportNode = pDoc->createNode(
		konga::xml::NODE_ELEMENT, stNodeName, GetSchemaDefinitionNamespace(schemaVersion) );
	pImportNode->setAttribute( konga::XmlConstants::NAMESPACE, stImportNamespace );
	if( !stSchemaLocation.empty() )
		pImportNode->setAttribute( SCHEMALOCATION_ATTRIBUTE, stSchemaLocation );
	return pImportNode;
}

konga::xml::DOMElementPtr XsdUtils::CreateSchemaIncludeNode(konga::xml::DOMDocument2Ptr pDoc,
														   XsdConstants::SchemaVersion schemaVersion,
														   const string& stSchemaLocation,
														   const string& stSchemaNamespacePrefix)
{
	if( schemaVersion == XsdConstants::SchemaVersion_Unknown )
		schemaVersion = XsdUtils::GetDefaultSchemaVersion();
	const string& stNodeName = konga::XmlUtils::GetQualifiedName(stSchemaNamespacePrefix, INCLUDE);
	konga::xml::DOMElementPtr pIncludeNode = pDoc->createNode(konga::xml::NODE_ELEMENT, stNodeName, GetSchemaDefinitionNamespace(schemaVersion));
	if( !stSchemaLocation.empty() )
		pIncludeNode->setAttribute( SCHEMALOCATION_ATTRIBUTE, stSchemaLocation);
	return pIncludeNode;
}

                                                                                                
konga::xml::DOMElementPtr XsdUtils::CreateComplexTypeSequenceNode(konga::xml::DOMDocument2Ptr pDoc,
																 XsdConstants::SchemaVersion schemaVersion,
																 const string& stSchemaNamespacePrefix)
{
	if( schemaVersion == XsdConstants::SchemaVersion_Unknown )
		schemaVersion = XsdUtils::GetDefaultSchemaVersion();
	const string& stNamespace = GetSchemaDefinitionNamespace( schemaVersion );
	string stNodeName = konga::XmlUtils::GetQualifiedName( stSchemaNamespacePrefix, COMPLEX_TYPE );
	konga::xml::DOMElementPtr pComplexTypeNode = 
		pDoc->createNode( konga::xml::NODE_ELEMENT, stNodeName, stNamespace );

	stNodeName = konga::XmlUtils::GetQualifiedName( stSchemaNamespacePrefix, SEQUENCE );
	pComplexTypeNode->appendChild(
		pDoc->createNode( konga::xml::NODE_ELEMENT, stNodeName, stNamespace ) );
	return pComplexTypeNode;
}

string XsdUtils::GetSchemaDefinitionNamespace(XsdConstants::SchemaVersion version                         )
{
	switch( version )
	{
	case SchemaVersion_2001:
		return SCHEMA_DEFINITION_NAMESPACE_2001;
	case SchemaVersion_1999:
		return SCHEMA_DEFINITION_NAMESPACE_1999;
	default:
		return "";
	}
}

string XsdUtils::GetSchemaInstanceNamespace(XsdConstants::SchemaVersion version                         )
{
	switch( version )
	{
	case SchemaVersion_2001:
		return SCHEMA_INSTANCE_NAMESPACE_2001;
	case SchemaVersion_1999:
		return SCHEMA_INSTANCE_NAMESPACE_1999;
	default:
		return "";
	}
}

XsdConstants::SchemaVersion XsdUtils::GetSchemaVersion(const konga::xml::XmlNamespaceDeclarations& namespaceDeclarations)
{
	if( namespaceDeclarations.IsNamespaceDeclared(SCHEMA_DEFINITION_NAMESPACE_2001) )
		return SchemaVersion_2001;
	else if( namespaceDeclarations.IsNamespaceDeclared(SCHEMA_DEFINITION_NAMESPACE_1999) )
		return SchemaVersion_1999;
	else
		return XsdConstants::SchemaVersion_Unknown;
}

string XsdUtils::GetSchemaForSchema(XsdConstants::SchemaVersion version                                       )
{
	string key = "";
	if (version == XsdConstants::SchemaVersion_2001) {
		key = "Schema_2001";
	}
	else if (version == XsdConstants::SchemaVersion_1999) {
		key = "Schema_1999";
	}
	return getAbsolutePath(GetOptionAsString("Settings/XMLSchemas", key, "DataInterchange/xml_dtd/Schemas/"));
}

string XsdUtils::GetExpandedNameAsString(const string& stNamespace, const string& stLocalName)
{
	if ( stNamespace.empty() ) {
		return stLocalName;
	}
	else {
		ExpandedName en(stNamespace, stLocalName);
		return en.toString();
	}
}

unsigned long XsdUtils::InsertImportNode(konga::xml::DOMDocument2Ptr pDoc,
								   konga::xml::DOMElementPtr pSchemaNode,
								   XsdConstants::SchemaVersion schemaVersion,
								   const string& stSchemaNamespacePrefix,
								   const string& stImportedNamespace,
								   const string& stSchemaLocation,
								   string& stError)
{
	konga::xml::DOMElementPtr pImportNode = CreateSchemaImportNode(pDoc, schemaVersion, stImportedNamespace, stSchemaLocation, stSchemaNamespacePrefix);
	return konga::XmlUtils::InsertAsFirstChild(pSchemaNode, pImportNode, stError);
}

unsigned long XsdUtils::InsertIncludeNode(konga::xml::DOMDocument2Ptr pDoc,
								    konga::xml::DOMElementPtr pSchemaNode,
								    XsdConstants::SchemaVersion schemaVersion,
								    const string& stSchemaNamespacePrefix,
								    const string& stSchemaLocation,
									string& stError)
{
	konga::xml::DOMElementPtr pIncludeNode = CreateSchemaIncludeNode(pDoc, schemaVersion, stSchemaLocation, stSchemaNamespacePrefix);
	return konga::XmlUtils::InsertAsFirstChild(pSchemaNode, pIncludeNode, stError);
}

bool XsdUtils::IsAnyType(const string& stTypeName, const string& stTypeNamespace, XsdConstants::SchemaVersion schemaVersion)
{
	string stSchemaDefNamespace = GetSchemaDefinitionNamespace(schemaVersion);
	return ( stricmp(stTypeName.c_str(), ANY_TYPE.c_str()) == 0 && strcmp(stTypeNamespace.c_str(), stSchemaDefNamespace.c_str()) == 0 );
}

konga::xml::DOMElementPtr XsdUtils::FindAnyElement(konga::xml::DOMElementPtr pDOMNode,
												  const string& stTargetNamespace,
												  const string& stAnyElementNamespace)
{
	XsdConstants::AnyElementNamespace elementNamespace = GetAnyElementNamespace(stAnyElementNamespace);
	if( elementNamespace == List )
	{
		string stNamespaceList = konga::TrimString( konga::Collapse(stAnyElementNamespace) );
		stNamespaceList += " ";
		return FindAnyElement(pDOMNode, stTargetNamespace, List, stNamespaceList);
	}
	else
	{
		return FindAnyElement( pDOMNode, stTargetNamespace, GetAnyElementNamespace(stAnyElementNamespace) );
	}
}

konga::xml::DOMElementPtr XsdUtils::FindAnyElement(konga::xml::DOMElementPtr pDOMNode,
												  const string& stTargetNamespace,
												  XsdConstants::AnyElementNamespace elementNamespace,
												  const string& stNamespaceList          )
{
	string stNodeNamespaceURI = konga::XmlUtils::GetNodeNamespaceURI(pDOMNode);
	switch( elementNamespace )
	{
	case Any:
		if( !stNodeNamespaceURI.empty() )
			return pDOMNode;
		break;
	case Local:
		if( stNodeNamespaceURI.empty() )
			return pDOMNode;
		break;
	case Other:
		if( strcmp( stTargetNamespace.c_str(), stNodeNamespaceURI.c_str() ) != 0 )
			return pDOMNode;
		break;
	case List:
		string stNamespaceListCopy(stNamespaceList);
		while( stNamespaceListCopy.length() > 0 )
		{
			string stNamespace = konga::strtok(stNamespaceListCopy, ' ', 0);
			if( konga::Equals(stNamespace, stNodeNamespaceURI) || 
				(konga::Equals(stNamespace, TARGET_NAMESPACE) && 
					konga::Equals(stNodeNamespaceURI, stTargetNamespace)) )
			{
				return pDOMNode;
			}
		}
		break;
	}

	konga::xml::DOMNodeListPtr pChildren = pDOMNode->getChildNodes();
	for( int i = 0, nChildren = pChildren->getLength(); i < nChildren; ++i )
	{
		konga::xml::DOMNodePtr pChild = pChildren->item[i];
		if( pChild->getNodeType() == konga::xml::NODE_ELEMENT )
		{
			konga::xml::DOMElementPtr pFind = FindAnyElement(pChild, stTargetNamespace, elementNamespace, stNamespaceList);
			if( pFind != 0 )
				return pFind;
		}
	}

	return 0;
}

XsdConstants::AnyElementNamespace XsdUtils::GetAnyElementNamespace(const string& stNamespaceString)
{
	if( konga::EqualsIgnoreCase(stNamespaceString, ANY_NAMESPACE) )
		return Any;
	else if( konga::EqualsIgnoreCase( stNamespaceString, LOCAL_NAMESPACE) )
		return Local;
	else if( konga::EqualsIgnoreCase(stNamespaceString, OTHER_NAMESPACE) )
		return Other;
	else
		return List;
}

void XsdUtils::SynchronizeNilAndTypeAttributes(konga::xml::DOMElementPtr pRoot, const string& stSchemaInstancePrefix)
{
	string stNilAttributeName = konga::XmlUtils::GetQualifiedName(stSchemaInstancePrefix, NIL);
	string stTypeAttributeName = konga::XmlUtils::GetQualifiedName(stSchemaInstancePrefix, TYPE);
	SynchronizeNulAndTypeAttributes(pRoot, stNilAttributeName, stTypeAttributeName);
}

void XsdUtils::SynchronizeNulAndTypeAttributes(konga::xml::DOMElementPtr pRoot,
											   const string& stNilAttributeName,
											   const string& stTypeAttributeName)
{
	if (IsTrue(pRoot, stNilAttributeName))
	{
		pRoot->removeAttribute(stTypeAttributeName);
	}
	konga::xml::DOMNodeListPtr pChildren = pRoot->getChildNodes();
	for( int i = 0, nChildren = pChildren->getLength(); i < nChildren; ++i )
	{
		konga::xml::DOMNodePtr pChild = pChildren->item[i];
		if( pChild->getNodeType() == konga::xml::NODE_ELEMENT )
		{
			SynchronizeNulAndTypeAttributes(pChild, stNilAttributeName, stTypeAttributeName);
		}
	}
}

namespace {
	bool bSchemaTypeMapInitialized = false;                                      
	boost::mutex SchemaTypeMapInitMutex;

	void InitializeSchemaTypeMap()
	{
		boost::mutex::scoped_lock lock(SchemaTypeMapInitMutex);
		if( !bSchemaTypeMapInitialized )
		{
			bSchemaTypeMapInitialized = true;
			for( int i = 0; i < s_numberOfSchemaTypes; ++i )
			{
				string stCRType;
				switch( i )
				{
				case 0:
				case 1:
				case 2:
				case 3:
				case 4:
				case 5:
				case 6:
				case 7:
				case 8:
				case 9:
				case 10:
				case 11:
				case 12:
				case 13:
				case 14:
				case 15:
					stCRType = "String";
					break;
				case 16:
				case 17:
				case 18:
				case 19:
				case 20:
				case 21:
				case 22:
				case 23:
				case 24:
				case 25:
				case 26:
					stCRType = "Integer";
					break;
				case 27:
				case 28:
					stCRType = "Long";
					break;
				case 29:                                    
					stCRType = "Double";
					break;
				case 30:
					stCRType = "Float";
					break;
				case 31:
					stCRType = "Double";
					break;
				case 32:
					stCRType = "Boolean";
					break;
				case 33:
				case 34:
				case 35:
					stCRType = "Date";
					break;
				case 36:
					stCRType = "String";                                
					break;
				case 37:
				case 38:
				case 39:
				case 40:
				case 41:
				case 42:
				case 43:
					stCRType = "String";
					break;
				default: 
					stCRType = "String";
				}
				s_mapSchemaTypeToCrType[XsdUtils::GetExpandedNameAsString(SCHEMA_DEFINITION_NAMESPACE_1999, s_szSchemaTypes[i])] = stCRType;
				s_mapSchemaTypeToCrType[XsdUtils::GetExpandedNameAsString(SCHEMA_DEFINITION_NAMESPACE_2001, s_szSchemaTypes[i])] = stCRType;
			}
		}
	}
}

string XsdUtils::GetCRType(const string& stUniversalSchemaTypeName)
{
	InitializeSchemaTypeMap();
	map<string, string>::const_iterator cit = s_mapSchemaTypeToCrType.find(stUniversalSchemaTypeName);
	return (cit == s_mapSchemaTypeToCrType.end()) ? "" : cit->second;
}

string XsdUtils::GetCRType(const ExpandedName& expandedName) {
	return GetCRType(expandedName.toString());
}


bool XsdUtils::IsSoapArrayType(const string& stUniversalTypeName)
{
	string stArrayType = XsdUtils::GetExpandedNameAsString(SoapEncoding::VERSION_11_NAMESPACE, TYPE_ARRAY );
	return konga::EqualsIgnoreCase(stUniversalTypeName, stArrayType);;
}

int XsdUtils::GetCROMCardinalityType(int iMinUse, int iMaxUse)
{
	if(iMinUse == 0)
	{
		if(iMaxUse == 1)
			return CROM_t_Optional;
		else if(iMaxUse > 1)
			return CROM_t_Collection;
	}
	else
	{
		if(iMaxUse > 1)
			return CROM_t_AtLeastOne;
	}
	return CROM_t_Unknown;
}

unsigned long XsdUtils::CopyAttributes(konga::xml::DOMDocument2Ptr pDOMDoc,
								 konga::xml::DOMElementPtr pSource, 
								 konga::xml::DOMElementPtr pTarget,
								 const string& stSchemaInstancePrefix,
								 string& stError)
{
	try
	{
		konga::xml::DOMNamedNodeMapPtr pAttribs = pSource->getAttributes();
		if( pAttribs != 0 )
		{
			for( int i = 0, n = pAttribs->getLength(); i < n; ++i )
			{
				konga::xml::DOMAttributePtr attrib = pAttribs->item[i];
				konga::xml::DOMAttributePtr p = pDOMDoc->createNode(
					konga::xml::NODE_ATTRIBUTE, attrib->getName(), attrib->getNamespaceURI());
				p->setValue(attrib->getValue());
				pTarget->setAttributeNode(p);
			}
			string stXsiNilAttributeName = konga::XmlUtils::GetQualifiedName(stSchemaInstancePrefix, NIL);
			if (IsTrue(pTarget, stXsiNilAttributeName))
			{
				                                                                                                                  
				string stXsiTypeAttributeName = konga::XmlUtils::GetQualifiedName(stSchemaInstancePrefix, TYPE_ATTRIBUTE);
				pTarget->removeAttribute( stXsiTypeAttributeName );
			}
		}
	}
	catch( CrException& e )
	{
		stError = e.GetErrorString();
		return E_FAIL|e.GetErrorNumber();
	}
	return S_OK;
}

                                                                                                       
                                                 
konga::xml::DOMElementPtr XsdUtils::CloneElement(konga::xml::DOMDocument2Ptr pDoc,
												konga::xml::DOMElementPtr pOriginal,
												const string& stNamespaceURI,
												const string& stSchemaInstancePrefix,
												string& stError)
{
	try
	{
		string stOriginalName, stOriginalNamespaceURI;
		konga::XmlUtils::GetNodeNameAndNamespace(pOriginal, stOriginalName, stOriginalNamespaceURI);
		konga::xml::DOMElementPtr pClone = 0;
		if( stOriginalNamespaceURI.empty() )
		{
			pClone = pDoc->createNode( konga::xml::NODE_ELEMENT, stOriginalName, stNamespaceURI );
		}
		else
		{
			pClone = pDoc->createNode( konga::xml::NODE_ELEMENT, stOriginalName, stOriginalNamespaceURI );
		}

		unsigned long hr = CopyAttributes(pDoc, pOriginal, pClone, stSchemaInstancePrefix, stError);
		if( FAILED(hr) )
		{
			string stHeader = "Failed to copy the attributes for the element '";
			stHeader += stOriginalName;
			stHeader += "'. ";
			stError = stHeader + stError;
			return 0;
		}

		konga::xml::DOMNodeListPtr pChildren = pOriginal->getChildNodes();
		for( int i = 0, iMax = pChildren->getLength(); i < iMax; ++i )
		{
			konga::xml::DOMNodePtr pChild = pChildren->item[i];
			konga::xml::DOMNodeType childType = pChild->getNodeType();
			if( childType == konga::xml::NODE_ELEMENT )
			{
				konga::xml::DOMElementPtr pClonedChild = CloneElement(pDoc, pChild, stNamespaceURI, stSchemaInstancePrefix, stError);
				if( pClonedChild == 0 )
				{
					return 0;
				}
				pClone->appendChild(pClonedChild);
			}
			else
			{
				pClone->appendChild( pDoc->importNode(pChild, true) );
			}

		}
		return pClone;
	}
	catch( CrException& e )
	{
		stError = "Failed to clone the XML element. "+e.GetErrorString();
		return 0;
	}
}

void XsdUtils::GetImportNodes(konga::xml::DOMElementPtr pSchemaNode,
							  vector<konga::xml::DOMElementPtr>& vImportNodes        )
{
	string stNamespace = konga::XmlUtils::GetNodeNamespaceURI(pSchemaNode);
	konga::xml::DOMNodeListPtr pChildren = pSchemaNode->getChildNodes();
	for( int i = 0, nChildren = pChildren->getLength(); i < nChildren; ++i )
	{
		konga::xml::DOMNodePtr pChild = pChildren->item[i];
		if( pChild->getNodeType() == konga::xml::NODE_ELEMENT )
		{
			string stChildNamespace, stChildBaseName;
			konga::XmlUtils::GetNodeNameAndNamespace(pChild, stChildBaseName, stChildNamespace);
			if( konga::Equals(stChildBaseName, IMPORT) &&
				konga::Equals(stChildNamespace, stNamespace) )
			{
				vImportNodes.push_back(pChild);
			}
		}
	}
}

