/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <konga/Utils/FileUtils.h>
#include <konga/Utils/StringUtils.h>

#include "XmlNamespaceDeclarations.h"
#include "XmlSchemaValidator.h"
#include "XmlUtils.h"

using namespace std;
using namespace konga;
using namespace konga::xml;

int XmlUtils::TransferNameSpace(const xml::DOMElementPtr& pSourceElement,
								xml::DOMElementPtr& pTargetElement,
								bool bTransferDefaultNamespace,
								bool bTransferDuplicateURIs,
								string& stTargetNamespacePrefix,
								string& stError)
{
	try
	{
		XmlNamespaceDeclarations sourceNS(pSourceElement);
		XmlNamespaceDeclarations targetNS(pTargetElement);

		const string& targetElementNamespaceURI = pTargetElement.getNamespaceURI();
		StringVector vstSourceURIs;
		sourceNS.GetNamespaceURIs(vstSourceURIs);
		for( StringVector::const_iterator cit = vstSourceURIs.begin(); cit != vstSourceURIs.end(); ++cit ) {
			const string& stURI = *cit;
			if( stURI.empty() ) continue;	                                                                  
			if( stricmp(stURI.c_str(), targetElementNamespaceURI.c_str()) == 0 ) {
				const string& sourcePrefix = sourceNS.GetNamespacePrefix(stURI);
				if( !(sourcePrefix.length() > 0 && targetNS.IsDefaultNamespace(stURI)) ) {
					continue;
				}
			}

			if( targetNS.IsNamespaceDeclared(stURI) && !bTransferDuplicateURIs ) {
				continue;
			}

			StringVector vstPrefixes;
			sourceNS.GetNamespacePrefixes(stURI, vstPrefixes);

			                                                                              
			                                                                  
			bool bAdded = false;
			for( StringVector::const_iterator cit = vstPrefixes.begin(); cit != vstPrefixes.end(); ++cit )
			{
				const string& stPrefix = *cit;
				if (stPrefix.empty())
				{
					if (!bTransferDefaultNamespace)
					{
						continue;
					}
				}
				if( !targetNS.IsPrefixUsed(stPrefix) )
				{
					                                 
					const string stName("xmlns:" + stPrefix);
					if (stricmp(pTargetElement->lookupNamespaceURI(stPrefix).c_str(), stURI.c_str()) != 0)
					{
						pTargetElement->setAttribute(stName, stURI);
					}
					targetNS.AddNamespaceDeclaration(stURI, stPrefix);
					bAdded = true;
					if( !bTransferDuplicateURIs )
						break;
				}
			}
			if( !bAdded )
			{
				                                                                     
				                             
				string stPrefix("prefix");
				int nPrefix = 1;
				do 
				{
					stPrefix = "prefix" + toString(nPrefix++);
				} while( targetNS.IsPrefixUsed(stPrefix) );
				const string stName( "xmlns:" + stPrefix );
				pTargetElement->setAttribute(stName, stURI);
				targetNS.AddNamespaceDeclaration(stURI, stPrefix);
			}
		}

		const string& stTargetNamespace = sourceNS.GetTargetNamespace();
		if( !stTargetNamespace.empty() )
			stTargetNamespacePrefix = sourceNS.GetNamespacePrefix(stTargetNamespace);

		return 0;
	}
	catch( const CrXmlException& ex )
	{
		stError = ex.GetErrorString();
		return 1;
	}
}
void XmlUtils::GetNodeNameAndNamespace(const xml::DOMNodePtr& pNode,
									   string& stNodeName,
									   string& stNamespaceURI)
{
	if( pNode == 0 )
		return;
	
	stNodeName = GetNodeBaseName(pNode);
	stNamespaceURI = GetNodeNamespaceURI(pNode);
}

string XmlUtils::GetNodeBaseName(const xml::DOMNodePtr& pNode)
{
	return pNode.getBaseName();
}

string XmlUtils::GetNodeNamespaceURI(const xml::DOMNodePtr& pNode)
{
	if( pNode == 0 )
		return "";
	return pNode->getNamespaceURI();
}

void XmlUtils::GetNodeNameAndNamespacePrefix(const xml::DOMNodePtr& pNode,
											 string& stNodeName,
											 string& stNamespacePrefix)
{
	if( pNode == 0 )
		return;
	GetPrefixAndLocalName(GetNodeName(pNode), stNamespacePrefix, stNodeName);
}

namespace {
	void split(const string& input, const string::size_type ipos, string& pre, string& post) {
		if (ipos != string::npos) {
			if (ipos == 0) {	          
				pre.clear();
				post = input;
			}
			else if (ipos == (input.size()-1)) {	            
				pre = input;
				post.clear();
			}
			else {	                
				pre = input.substr(0, ipos);
				post = input.substr(ipos+1);
			}
		}
		else {	                      
			pre.clear();
			post = input;
		}
	}
}

void XmlUtils::GetPrefixAndLocalName(const string& stQualifiedName,
								     string& stNamespacePrefix,
								     string& stLocalName)
{
	const string::size_type ipos = stQualifiedName.find_first_of(":");
	split(stQualifiedName, ipos, stNamespacePrefix, stLocalName);
}

string XmlUtils::GetNodePrefix(const xml::DOMNodePtr& pNode)
{
	if( pNode == 0 )
		return "";
	return pNode.getPrefix();
}

long XmlUtils::CopyAttributes(xml::DOMDocumentPtr& pDOMDoc,
							  const xml::DOMElementPtr& pSource,
							  xml::DOMElementPtr& pTarget)
{
	xml::DOMNamedNodeMapPtr pAttribs = pSource.getAttributes();
	if ( pAttribs != 0 ) {
		unsigned int length = pAttribs.getLength();
		for( int i = 0, n = length; i < n; ++i ) {
			xml::DOMAttrPtr attr = pAttribs->item[i];
			xml::DOMAttrPtr newAttr = pDOMDoc->createAttribute(attr->getNamespaceURI(), attr->getNodeName());
			newAttr.setValue(attr.getNodeValue());
            pTarget.setAttributeNode(newAttr);
		}
		pAttribs = 0;
	}

	return 0;
}

void XmlUtils::GetDeclaredNamespaces(const xml::DOMElementPtr& pRoot,
									map<string, string>& mapPrefixToUri,
									map<string, string>& mapUriToPrefix)
{
	xml::DOMNamedNodeMapPtr pAttribs = pRoot->getAttributes();
	if( pAttribs == 0 )
		return;

	for( int i = 0, n = pAttribs->getLength(); i < n; ++i )
	{
		xml::DOMNodePtr pA = pAttribs->item[i];
		string stName = GetNodeName(pA);
		if( stricmp(stName.c_str(), "xmlns") == 0 )
		{
			string stUri = pA->getNodeValue();
			mapPrefixToUri[""] = stUri;
			mapUriToPrefix[stUri] = "";
		}
		else if( stName.find("xmlns:") == 0 )
		{
			string stUri = pA->getNodeValue();
			string stPrefix = stName.substr(6);
			mapPrefixToUri[stPrefix] = stUri;
			mapUriToPrefix[stUri] = stPrefix;
		}
	}
}

string XmlUtils::GetNodeName(const xml::DOMNodePtr& pNode)
{
	return pNode->getNodeName();
}

string XmlUtils::GetAttributeValue(const xml::DOMAttributePtr& pAttribute) {
	return pAttribute->getNodeValue();
}

string XmlUtils::GetAttributeValue(const xml::DOMElementPtr& pElement, const string& strAttributeName) {
	return pElement->getAttribute(strAttributeName);
}

std::string XmlUtils::GetAttributeValueNS(const xml::DOMElementPtr& pElement,
	const std::string& stNamespaceUri, const std::string& stLocalName)
{
	return pElement->getAttributeNS(stNamespaceUri, stLocalName);
}

string XmlUtils::GetNodeValue(const xml::DOMNodePtr& pNode) {
	if ( pNode == 0 )
		return "";

	konga::xml::DOMNodeType nodeType = pNode->getNodeType();
	if( nodeType == ELEMENT_NODE ) {
		DOMNodeListPtr pChildren = pNode->getChildNodes();
		if ( pChildren != 0 ) {
			for( int i = 0, n = pChildren->getLength(); i < n; ++i )
			{
				DOMNodePtr pChild = pChildren->item[i];
				if( pChild->getNodeType() == TEXT_NODE )
					return GetNodeValue(pChild);
			}
		}
	}
	else if( nodeType == TEXT_NODE || nodeType == ATTRIBUTE_NODE ||
			 nodeType == CDATA_SECTION_NODE || nodeType == COMMENT_NODE ||
			 nodeType == PROCESSING_INSTRUCTION_NODE )
	{
		return pNode->getNodeValue();
	}
	return "";
}

string XmlUtils::GetLastError(const xml::DOMDocumentPtr& pDOMDocument, const string& stHeader) {
	string stError(stHeader);
	if ( !stError.empty() )
		stError += "\n";

	std::string str( pDOMDocument->getLastError() );
	if ( !str.empty() ) {
		stError += str;
		stError += "\n";
	}

	str = pDOMDocument->getParseErrors();
	if ( !str.empty() ) {
		stError += str;
		stError += "\n";
	}

	str = pDOMDocument->getParseWarnings();
	if ( !str.empty() ) {
		stError += str;
	}
	if ( !stError.empty() && *(stError.end()-1) == '\n' )
		stError.erase(stError.end()-1);
	return stError;
}


xml::DOMElementPtr XmlUtils::FindElementByTypeAndName(const xml::DOMElementPtr& pElement, const char* sType, const char* sName) {
	if ( pElement == 0)
		return 0;

	xml::DOMNodeListPtr pChildren = pElement->getChildNodes();
	if ( pChildren != 0 ) {
		for( unsigned long i=0, n=pChildren->getLength(); i<n; i++ ) {
			xml::DOMNodePtr pChild = pChildren->getItem(i);
			if( pChild == 0 || pChild->isElementNode() )
				continue;
			if( pChild->getBaseName() != sType )
				continue;
			if( strcmp(sName, GetAttributeValue(pChild, "name").c_str()) == 0)
				return pChild;
		}
	}
	return 0;
}

xml::DOMDocumentPtr XmlUtils::LoadDoc(const string& stFileName, string& stErr, bool bValidateOnParse         ) {
	stErr.clear();
	if( !FileExists(stFileName) ) {
		stErr = "The input file \"" + stFileName + "\" does not exist.";
		return 0;
	}

	xml::DOMDocumentPtr pDoc;
	pDoc->validateOnParse = bValidateOnParse;
	pDoc->async = false;
	if ( !pDoc->loadFile(stFileName) ) {
		stErr = GetLastError(pDoc);
	}
	return pDoc;
}

xml::DOMDocumentPtr XmlUtils::LoadDoc(const std::string& stXmlFileName, const std::string& stSchemaFileName,
									  const std::string& stNamespace, std::string& stErr)
{
	stErr.clear();
	if( !FileExists(stXmlFileName) ) {
		stErr = "The input file \"" + stXmlFileName + "\" does not exist.";
		return 0;
	}

	                                                                                             

	xml::DOMDocumentPtr pDoc;
	pDoc->validateOnParse = true;
	pDoc->async = false;
	if ( !pDoc.loadFile(stXmlFileName, stSchemaFileName, stNamespace) ) {
		stErr = GetLastError(pDoc);
		pDoc=0;
	}
	return pDoc;
}

xml::DOMDocumentPtr XmlUtils::LoadDocumentFromString(const std::string& stXmlString,
	std::string& stErr, bool bValidate            )
{
	DOMDocumentPtr pDoc;
	pDoc.validateOnParse = bValidate;
	if ( !pDoc.loadString(stXmlString) ) {
		stErr = GetLastError(pDoc);
		pDoc = 0;
	}
	return pDoc;
}

long XmlUtils::CreateDOMDocument2Instance(xml::DOMDocumentPtr& pDoc, 
											 bool bAddProcessingInstruction, 
											 string& stErr)
{
	long hr = pDoc.CreateInstance();
	if ( hr != 0 ) {
		stErr = pDoc->getLastError();
		return hr;
	}
	if( pDoc == 0 )
	{
		stErr = "Failed to create xml document node. CreateInstance() returned null but no error was reported.";
		return -1;
	}
	                                                                                                                
	if ( !bAddProcessingInstruction ) {
		            
	}
	return 0;
}

xml::DOMDocumentPtr XmlUtils::CreateDOMDocument(std::string& stErr, bool bAddProcessingInstruction) {
	xml::DOMDocumentPtr doc;
	if ( doc.CreateInstance() != 0 ) {
		stErr = doc.getLastError();
		return 0;
	}

	if ( !bAddProcessingInstruction ) {
		                                                                                                                
	}
	return doc;
}


long XmlUtils::InsertAsFirstChild(xml::DOMElementPtr pParent, xml::DOMElementPtr pToBeInserted, string& stError) {
	try
	{
		xml::DOMNodePtr pFirstChild = pParent->getFirstChild();
		xml::DOMNodePtr pInserted;
		if( pFirstChild == 0 )
			pInserted = pParent->appendChild(pToBeInserted);
		else
			pInserted = pParent->insertBefore(pToBeInserted, pFirstChild);
		return 0;
	}
	catch( const CrXmlException& e )
	{
		stError = e.GetErrorString();
		return ( e.GetErrorNumber() != 0 ) ? e.GetErrorNumber() : -1;
	}
}

                                                                                               
                                                                                                       
                                                                               
xml::DOMElementPtr XmlUtils::CloneElement(xml::DOMDocumentPtr& pDoc,
										  const xml::DOMElementPtr& pOriginal,
										  const string& stNamespaceURI,
										  string& stError)
{
	string stOriginalName, stOriginalNamespaceURI;
	GetNodeNameAndNamespace(pOriginal, stOriginalName, stOriginalNamespaceURI);
	xml::DOMElementPtr pClone = 0;
	if( stOriginalNamespaceURI.empty() )
	{
		pClone = pDoc->createElementNS(stNamespaceURI, stOriginalName);
	}
	else
	{
		pClone = pDoc->createElementNS(stOriginalNamespaceURI, stOriginalName );
	}

	long hr = CopyAttributes(pDoc, pOriginal, pClone);                                 
	if ( hr != 0 ) {
		return -1;
	}

	xml::DOMNodeListPtr pChildren = pOriginal->getChildNodes();
	if ( pChildren != 0 ) {
		for( int i = 0, n = pChildren->getLength(); i < n; ++i )
		{
			xml::DOMNodePtr pChild = pChildren->getItem(i);
			if( pChild->isElementNode() )
			{
				xml::DOMElementPtr pClonedChild = CloneElement(pDoc, pChild, stNamespaceURI, stError);
				if( pClonedChild == 0 )
					return 0;
				pClone->appendChild(pClonedChild);
			}
			else
			{
				pClone->appendChild( pChild->cloneNode(true) );
			}
		}
	}
	return pClone;
}

bool XmlUtils::ValidateXmlSchema(const std::string& stSchemaFilePath, std::string& stError) {
	XmlSchemaValidator validator;
	return validator.ValidateXmlSchema(stSchemaFilePath, stError);
}

bool XmlUtils::ValidateXmlInstanceAgainstSchema(const std::string& stXmlFilePath, const std::string& stSchemaFilePath, const std::string& stTargetNamespace, std::string& stError) {
	XmlSchemaValidator validator;
	return validator.ValidateXmlInstance(stXmlFilePath, stSchemaFilePath, stTargetNamespace, stError);
}

int XmlUtils::GetIndexOfChildNode(const xml::DOMNodePtr& pParent, const xml::DOMNodePtr& pChild) {
	if( pChild->getParentNode() == pParent )
	{
		xml::DOMNodeListPtr pChildren = pParent->getChildNodes();
		if ( pChildren != 0 ) {
			for( int i = 0, n = pChildren->getLength(); i < n; ++i )
			{
				if( pChildren->item[i] == pChild )
					return i;
			}
		}
	}
	return -1;
}


string XmlUtils::TransferNamespacePrefix(const xml::DOMElementPtr& pSource, 
										 xml::DOMElementPtr& pTarget,
										 const std::string& stNamespacePrefix)
{
	bool bIsDefaultNamespace = stNamespacePrefix.empty();

	                                                                                                     
	                                                                                                     
	                                   
	string stNamespaceURI;
	xml::DOMNodePtr pWorkingSource = pSource;
	while( pWorkingSource != 0 )
	{
		if ( pWorkingSource->isElementNode() ) {
			XmlNamespaceDeclarations namespaces(pWorkingSource);
			stNamespaceURI = bIsDefaultNamespace ? namespaces.GetDefaultNamespace() : namespaces.GetNamespaceURI(stNamespacePrefix);
		}
		if( !stNamespaceURI.empty() )
			break;
		pWorkingSource = pWorkingSource->getParentNode();
	}

	if( stNamespaceURI.empty() )
	{
		                                                                                   
	}
	else
	{
		                                                                                           
		                                                                                                 
		                                                                                                   
		                                                                                                      
		                                                                       
		bool bNewNamespaceDeclarationRequired = true;
		DOMElementPtr pWorkingTarget = pTarget;
		while( pWorkingTarget != 0 && bNewNamespaceDeclarationRequired )
		{
			if ( pWorkingTarget->isElementNode() ) {
				XmlNamespaceDeclarations namespaces(pWorkingTarget);
				string stMappedURI = bIsDefaultNamespace ? namespaces.GetDefaultNamespace() : namespaces.GetNamespaceURI(stNamespacePrefix);
				if( stMappedURI.empty() )
				{
					                       
					pWorkingTarget = pWorkingTarget->getParentNode();
				}
				else
				{
					                                                                            
					                                                                             
					                                                
					bNewNamespaceDeclarationRequired = !Equals(stNamespaceURI, stMappedURI);
					break;
				}
			}
			else
				break;
		}
		if( bNewNamespaceDeclarationRequired )
		{
			                                                                    
			const string stDeclAttr(bIsDefaultNamespace ? "xmlns" : "xmlns:" + stNamespacePrefix);
			pTarget->setAttribute( stDeclAttr, stNamespaceURI );
		}
	}
	return stNamespaceURI;
}

string XmlUtils::GetNamespacePrefix(const xml::DOMElementPtr& pElement,
									const string& stNamespaceURI)
{
	DOMNodePtr pWorkingNode = pElement;
	while( pWorkingNode != 0 )
	{
		if ( pWorkingNode.isElementNode() ) {
			XmlNamespaceDeclarations namespaces( pWorkingNode );
			string stPrefix = namespaces.GetNamespacePrefix(stNamespaceURI);
			if( !stPrefix.empty() )
				return stPrefix;
		}
		pWorkingNode = pWorkingNode->getParentNode();
	}
	return "";
}

string XmlUtils::GetNamespaceURI(const xml::DOMElementPtr& pElement,
								 const string& stPrefix)
{
	DOMNodePtr pWorkingNode = pElement;
	while( pWorkingNode != 0 )
	{
		if ( pWorkingNode.isElementNode() ) {
			XmlNamespaceDeclarations namespaces(pWorkingNode);
			string stURI = stPrefix.empty() ? namespaces.GetDefaultNamespace() : 
											namespaces.GetNamespaceURI(stPrefix);
			if( !stURI.empty() )
				return stURI;
		}
		pWorkingNode = pWorkingNode->getParentNode();
	}
	return string();
}

string XmlUtils::GetQualifiedName(const string& stPrefix, const string& stLocalName)
{
	return stPrefix.empty() ? stLocalName : stPrefix + string(":") + stLocalName;
}

void XmlUtils::AddNamespaceDeclaration(xml::DOMElementPtr pElement,
									   const string& stNamespaceURI,
									   const string& stNamespacePrefix)
{
	string st = "xmlns";
	if( !stNamespacePrefix.empty() )
		st += ":" + stNamespacePrefix;
	pElement->setAttribute(st, stNamespaceURI);
}

string XmlUtils::FindNamespacePrefixInChildNodes(const xml::DOMElementPtr& pElement,
												 const string& stNamespaceURI)
{
	DOMNodeListPtr pChildren = pElement->getChildNodes();
	if ( pChildren != 0 ) {
		for( int i = 0, n = pChildren->getLength(); i < n; ++i )
		{
			DOMNodePtr pChild = pChildren->item[i];
			if( pChild->isElementNode() )
			{
				XmlNamespaceDeclarations namespaces(pChild);
				string stPrefix = namespaces.GetNamespacePrefix(stNamespaceURI);
				if( stPrefix.empty() )
					stPrefix = FindNamespacePrefixInChildNodes(pChild, stNamespaceURI);
				if( !stPrefix.empty() )
					return stPrefix;
			}
		}
	}
	return string();
}

string XmlUtils::GetLocalName(const string& stQualifiedName)
{
	const string::size_type pos = stQualifiedName.find_first_of(':');
	if ( pos == string::npos )
		return stQualifiedName;
	else
		return stQualifiedName.substr(pos+1);
}

string XmlUtils::GetNamespacePrefix(const string& stQualifiedName)
{
	const string::size_type pos = stQualifiedName.find_first_of(':');
	if ( pos == string::npos )
		return string();
	else
		return stQualifiedName.substr(0, pos);
}

bool XmlUtils::HasChildrenOfType(const xml::DOMElementPtr& pElement, xml::DOMNodeType nodeType) {
	xml::DOMNodeListPtr pChildren = pElement->getChildNodes();
	if ( pChildren != 0 ) {
		for( int i = 0, n = pChildren->getLength(); i < n; ++i ) {
			if( pChildren->item[i]->getNodeType() == nodeType )
				return true;
		}
	}
	return false;
}

xml::DOMElementPtr XmlUtils::GetChildElement(const xml::DOMElementPtr& pParent, 
												 const string& stChildBaseName, 
												 bool bIgnoreCase             )
{
	if( pParent != 0 )
	{
		xml::DOMNodeListPtr pChildren = pParent->getChildNodes();
		if ( pChildren != 0 ) {
			for( int i = 0, n = pChildren->getLength(); i < n; ++i )
			{
				xml::DOMNodePtr pChild = pChildren->item[i];
				if( pChild->isElementNode() )
				{
					if( bIgnoreCase )
					{
						if( EqualsIgnoreCase(stChildBaseName, GetNodeBaseName(pChild)) )
							return pChild;
					}
					else
					{
						if( Equals(stChildBaseName, GetNodeBaseName(pChild)) )
							return pChild;
					}
				}
			}
		}
	}
	return 0;
}

xml::DOMElementPtr XmlUtils::GetFirstChildElement(const xml::DOMNodePtr& parent) {
	DOMNodeListPtr kids = parent.getChildNodes();
	for( unsigned int i = 0, n = kids.getLength(); i < n; ++i ) {
		DOMElementPtr element( kids->item[i] );
		if ( element != 0 )
			return element;
	}
	return 0;
}

bool XmlUtils::IsValueTrue(const string& value) {
	return konga::EqualsIgnoreCase(value, "true") || konga::Equals(value, "1");
}

bool XmlUtils::IsTrue(konga::xml::DOMElementPtr e, const string& stAttributeName) {
	string value = XmlUtils::GetAttributeValue(e, stAttributeName);
	return IsValueTrue(value);
}

bool XmlUtils::IsTrue(konga::xml::DOMAttributePtr a) {
	string value = a->getNodeValue();
	return IsValueTrue(value);
}
