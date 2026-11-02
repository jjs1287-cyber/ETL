/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */

#include <konga/Utils/CrException.h>
#include <konga/Utils/StringUtils.h>
#include <konga/Utils/util.h>
#include <konga/Utils/PortDefs.h>	                                                           

#include <konga/Soap/SoapConstants.h>
#include <konga/Soap/SoapEnvelope.h>

#include <konga/Xml/XmlUtils.h>
#include <konga/Xml/DOMTextPtr.h>

#include "WebServiceResponseSynchronizer.h"
#include "XsdFromWsdlCreator.h"
#include "crom.h"
#include "CromUtils.h"
#include "XsdNamespaceSupport.h"
#include "XsdUtils.h"

using namespace konga;
using namespace konga::xml;
using namespace std;


namespace {

void removeSoapEncodingStyleAttribute(DOMElementPtr pElement)
{
	DOMNamedNodeMapPtr pAttributes = pElement->getAttributes();
	if( pAttributes != 0 )
	{
		for( int i = 0, n = pAttributes->getLength(); i < n; ++i )
		{
			DOMAttributePtr pa = pAttributes->item[i];
			string stAttributeName, stAttributeNamespaceURI;
			XmlUtils::GetNodeNameAndNamespace(pa, stAttributeName, stAttributeNamespaceURI);
			if( (strcmp(stAttributeNamespaceURI.c_str(), SoapConstants::NAMESPACE_V11.c_str()) == 0 ||
				strcmp(stAttributeNamespaceURI.c_str(), SoapConstants::NAMESPACE_V12.c_str()) == 0 ) &&
				stricmp(stAttributeName.c_str(), "encodingStyle") == 0 )
			{
				pElement->removeAttributeNode(pa);
			}
		}
	}
}

bool IsNotValueContent(CROM* node) {
	return node != 0 && !node->IsElement() && !node->IsAttribute();
}

bool IsValueContent(CROM* node) {
	return node != 0 && (node->IsElement() || node->IsAttribute());
}

void CheckIfParentIsAGroup(CROM*& pCromParent) {
	                                                                                          
	                                                                                     
	                                                                                      
	                              
	if (pCromParent == 0) {
		return;
	}
	CROM* child = pCromParent->GetFirstChild();
	while (IsNotValueContent(child)) {
		CROM* grandChild = child->GetFirstChild();
		if (IsValueContent(grandChild)) {
			pCromParent = child;
			break;
		}
		child = grandChild;
	}
}

string GetXmlNodeNameFromCromNode(const std::string& originalDomNodeName, CROM* cromNode)
{
	if (CromUtils::isTypeDefinition(cromNode) || cromNode->IsAnyElement()) {
		return originalDomNodeName;
	}
	else {
		return (const char*)cromNode->GetDisplayName();
	}
}

}

unsigned long WebServiceResponseSynchronizer::Initialize()
{
	CollectNamespaceAndVersionInfo();
	                                                                                                    
	m_mapProcessedNodes.clear();
	return LoadDocuments();
}

void WebServiceResponseSynchronizer::CollectNamespaceAndVersionInfo() {
	m_schemaVersion = m_xsdNamespaces->GetSchemaVersion();
	m_stSchemaInstanceNamespace = m_xsdNamespaces->GetSchemaInstanceNamespace();
	m_stSchemaDefinitionNamespacePrefix = m_xsdNamespaces->GetSchemaDefinitionNamespacePrefix();
	m_stSchemaInstanceNamespacePrefix = m_xsdNamespaces->GetSchemaInstanceNamespacePrefix();
}

unsigned long WebServiceResponseSynchronizer::LoadDocuments()
{
	string stError;
	m_pInputDoc = XmlUtils::LoadDoc(m_stInputFilePath, stError, false);
	if( m_pInputDoc == 0 || stError.length() > 0 )
	{
		m_stLastError += stError+"\n";
		return E_FAIL;
	}
	unsigned long hr = XmlUtils::CreateDOMDocument2Instance(m_pOutputDoc, true, stError);
	if( FAILED(hr) )
	{
		m_stLastError += stError+"\n";
	}
	return hr;
}

DOMElementPtr WebServiceResponseSynchronizer::GetInputRoot()
{
	DOMElementPtr pInputRoot = m_pInputDoc->getDocumentElement();
	string stNodeName, stNamespacePrefix;
	XmlUtils::GetNodeNameAndNamespacePrefix(pInputRoot, stNodeName, stNamespacePrefix);
	while( stricmp(stNodeName.c_str(), m_stOutputRootName.c_str()) != 0 )
	{
		pInputRoot = pInputRoot->getFirstChild();
		if( pInputRoot == 0 )
			break;
		XmlUtils::GetNodeNameAndNamespacePrefix(pInputRoot, stNodeName, stNamespacePrefix);
	}
	return pInputRoot;
}

unsigned long WebServiceResponseSynchronizer::Synchronize()
{
	Initialize();
	unsigned long hr = S_OK;

	try
	{
		DOMElementPtr pInputRoot = GetInputRoot();
		if( pInputRoot == 0 )
		{
			        
			return E_FAIL;
		}

		hr = CopyDocument( m_pOutputDoc, 
						   m_pOutputDoc, 
						   0, 
						   pInputRoot, 
						   m_xsdNamespaces->GetTargetNamespace() );
		if( FAILED(hr) )
		{
			return hr;
		}

		hr = m_pOutputDoc->save( m_stOutputFilePath.c_str() );
		if( FAILED(hr) )
		{
			                              
			string stHeader = "Failed to save the output XML document \"";
			stHeader += m_stOutputFilePath;
			stHeader += "\".";
			m_stLastError += XmlUtils::GetLastError(m_pOutputDoc, stHeader)+"\n";
			return E_FAIL;
		}
	}
	catch( CrException& e )
	{
		m_stLastError += std::string("Error while synchronizing XML.\n")+ e.GetErrorString()+"\n";
		return E_FAIL;
	}
	return S_OK;
}


unsigned long WebServiceResponseSynchronizer::CopyDocument(DOMDocument2Ptr pDoc, 
													 DOMNodePtr domParent, 
													 CROM* cromParent,
													 DOMNodePtr original,
													 const string& stTargetNamespace)
{
	string uri(original.getNamespaceURI());
	if (konga::Equals(uri, XsdFromWsdlCreator::XSD_FROM_WSDL_NAMESPACE)) {
		if (konga::Equals(XmlUtils::GetNodeBaseName(original), "request")) {
			domParent->appendChild(pDoc->importNode(original, true));
			return S_OK;
		}
	}
	string stDOMParent = XmlUtils::GetNodeBaseName(domParent);
	if (original->getNodeType() == konga::xml::NODE_ELEMENT) {
		bool abort = false;
		unsigned long ret = CopyElement(pDoc, original, domParent, cromParent, abort);
		if (abort) {
			return ret;
		}
	}
	else {
		domParent->appendChild(pDoc->importNode(original, false));
	}
	DOMNodePtr child = original->getFirstChild();
	while (child != 0) {
		if (!AlreadyProcessed(child)) {
			unsigned long hr = CopyDocument(pDoc, domParent, cromParent, child, stTargetNamespace);
			if (FAILED(hr)) {
				return hr;
			}
		}
		child = child->getNextSibling();
	}
	return S_OK;
}


unsigned long WebServiceResponseSynchronizer::CopyElement(DOMDocument2Ptr pDoc,
														  DOMNodePtr original,
														  DOMNodePtr& domParent, 
														  CROM*& cromParent,
														  bool& abort)
{
	abort = false;
	string stNodeName, stNamespace;
	XmlUtils::GetNodeNameAndNamespace(original, stNodeName, stNamespace);

	DOMNodeMapKey key(original);
	m_mapProcessedNodes[key] = true;

	CheckIfParentIsAGroup(cromParent);

	CROM* cromNode = GetCromNodeForElement(original, cromParent, stNodeName, stNamespace);
	if (cromNode == 0)
	{
		                                                                            
		                                                                           
		                                                                           
		                                                                           
		                                                               
		                                                                          
		               
		                                           
		domParent->appendChild(pDoc->importNode(original, true));
		abort = true;
		return S_OK;
	}

	                                                                                        
	                                      
	string stNewNodeName = GetXmlNodeNameFromCromNode(stNodeName, cromNode);
	string stNamespacePrefix = original->getPrefix();
	if (!stNamespacePrefix.empty()) {
		stNewNodeName = stNamespacePrefix + ":" + stNewNodeName;
	}
	DOMElementPtr element = pDoc->createNode(konga::xml::NODE_ELEMENT, stNewNodeName.c_str(), cromNode->GetNamespace().c_str());
	domParent->appendChild(element);
	XmlUtils::CopyAttributes(pDoc, original, element);

	                                                                                               
	                                                                                                     
	removeSoapEncodingStyleAttribute(element);

	string stInstancePrefix = XmlUtils::GetNamespacePrefix(original, m_stSchemaInstanceNamespace);
	string stTypeAttribName = XmlUtils::GetQualifiedName(stInstancePrefix, XsdConstants::TYPE_ATTRIBUTE);
	bool bIsAnyType = XsdUtils::IsAnyType(cromNode->GetSchemaType(), cromNode->GetSchemaTypeNamespace(), m_schemaVersion);		
	if( bIsAnyType )
	{
		HandleAnyType(original, element, cromNode, stTypeAttribName, pDoc);
		                                                                                       
		abort = true;
		return S_OK;
	}

	string stAttribValue = XmlUtils::GetAttributeValue(element, stTypeAttribName);
	if( stAttribValue.length() )
	{
		HandleTypeAttribute(original, element, cromNode, stTypeAttribName, stAttribValue);
	}

	domParent = element;
	cromParent = cromNode;
	return S_OK;
}


CROM* WebServiceResponseSynchronizer::GetCromNodeForElement(DOMNodePtr domNode,
															CROM* cromParent, 
															const std::string& stNodeName,
															const std::string& stNamespace)

{
	CROM* cromNode = 0;
	if (cromParent == 0) {
		cromNode = m_root;
	}
	else {
		                                                                                              
		                                                                                
		cromNode = cromParent->FindChild(CROM_t_Element, stNodeName                 );
		if (CromUtils::isDerivedTypesHolder(cromNode)) {
			string stType = XmlUtils::GetAttributeValue(domNode, "xsi:type");
			if (stType.empty()) {
				                       
				return 0;
			}
			string stNamespacePrefix, stLocalName;
			XmlUtils::GetPrefixAndLocalName(stType, stNamespacePrefix, stLocalName);
			string stTypeNamespace = domNode->lookupNamespaceURI(stNamespacePrefix);
			cromNode = CromUtils::findSchemaTypeDefinition(cromNode, stTypeNamespace, stLocalName);
		}
	}

	if( cromNode == 0 )
	{
		                                                                                                  
		                                                          
		if( cromParent->CountChildren(CROM_t_Element) != 1 )
		{
			cromNode=cromParent->FindChild(CROM_t_Element, "Any");
			if(cromNode != 0 && cromNode->IsAnyElement()) {
				return cromNode;
			}
			OnStructureMisMatch(cromNode, cromParent, stNodeName, stNamespace);
			return 0;
		}
		cromNode = cromParent->GetFirstChild();
		while( !cromNode->IsElement() )
			cromNode = cromNode->GetNextSibling();	                                                                           
	}
	return cromNode;
}

DOMElementPtr WebServiceResponseSynchronizer::FindAnyElement(DOMElementPtr parentXmlNode, CROM* parentCromNode)
{
	DOMNodeListPtr pCandidates = parentXmlNode->getChildNodes();
	for( int i = 0, nCandidates = pCandidates->getLength(); i < nCandidates; ++i )
	{
		DOMNodePtr pCandidate = pCandidates->item[i];
		std::string stBaseName = pCandidate->getBaseName();
		if (parentCromNode->FindChild(CROM_t_Element, stBaseName) == 0)
		{
			                                                           
			                                                                 
			DOMNodeMapKey candidateKey(pCandidate);
			m_mapProcessedNodes[candidateKey] = true;
			return pCandidate;
		}
	}
	return 0;
}

void WebServiceResponseSynchronizer::HandleAnyElement(DOMNodePtr original,
													  DOMElementPtr domParent, 
													  CROM* cromParent,
													  CROM* cromNode,
													  DOMDocument2Ptr pDoc)
{
	                                                                                           
	                                                                                            
	                                                            
	                                                                                            
	                                                                                         
	                                               
	                                                                                               
	                                                                     
	DOMElementPtr pAnyElement = FindAnyElement(original->getParentNode(), cromParent);
	if( pAnyElement != 0 )
	{
		                                                                             
		string stNamespaceURI = XmlUtils::GetNodeNamespaceURI(pAnyElement);
		DOMElementPtr pAnyNode = pDoc->createNode(
			konga::xml::NODE_ELEMENT, (const char*)cromNode->GetDisplayName(), stNamespaceURI.c_str());

		                                                                                              
		                                   
		string stXml(pAnyElement->getXml());
		DOMTextPtr pTextNode = pDoc->createNode(konga::xml::NODE_TEXT, "", stNamespaceURI.c_str());
		pTextNode->setData(pAnyElement->getXml());
		pAnyNode->appendChild(pTextNode);

		                                    
		domParent->appendChild(pAnyNode);
	}
	else
	{
		                                                     
		                                                                                                
		                                        
	}
}

void WebServiceResponseSynchronizer::HandleAnyType(DOMNodePtr pOriginalNode, 
												   DOMElementPtr pCopy,
												   CROM* pCromNode,
												   const std::string& stTypeAttributeName,
												   DOMDocument2Ptr pDoc)
{
	                                                                                                     
	string stTextValue = XmlUtils::GetNodeValue(pOriginalNode);
	DOMNodeListPtr pChildren = pOriginalNode->getChildNodes();
	for( int i = 0, nChildren = pChildren->getLength(); i < nChildren; ++i )
	{
		string stNodeText( pChildren->item[i]->getXml() );
		stTextValue += stNodeText;
	}
	DOMTextPtr textPtr = pDoc->createNode(konga::xml::NODE_TEXT, "", pCromNode->GetNamespace().c_str());
	textPtr->setData(stTextValue);
	pCopy->appendChild(textPtr);

	                                                                                               
	                              
	pCopy->removeAttribute( stTypeAttributeName.c_str() );
}

void WebServiceResponseSynchronizer::HandleTypeAttribute(konga::xml::DOMNodePtr pOriginalNode, 
														 konga::xml::DOMElementPtr pCopy,
														 CROM* pCromNode,
														 const std::string& stTypeAttributeName,
														 const std::string& stTypeAttributeValue)
{
	XmlNamespaceDeclarations elementNamespaces(pCopy);

	size_t pos = stTypeAttributeValue.find(':');
	string stPrefix = pos==std::string::npos? std::string(""):stTypeAttributeValue.substr(0, pos);
	string stUri = XmlUtils::GetNamespaceURI(pOriginalNode, stPrefix);
	if( stUri.empty() )
	{
		                                                                               
		                   
		stUri = elementNamespaces.GetNamespaceURI(stPrefix);
	}
	string stBase = pos==std::string::npos? stTypeAttributeValue:stTypeAttributeValue.substr(pos);
	string stUniversalName = stUri + stBase;
	if( XsdUtils::IsSoapArrayType(stUniversalName) )
	{
		                                                                       
		string stTypeNamespaceURI = pCromNode->GetSchemaTypeNamespace();
		stPrefix = XmlUtils::GetNamespacePrefix(pOriginalNode, stTypeNamespaceURI);
		if( stPrefix.empty() )
		{
			                                                                                 
			                                 
			stPrefix = elementNamespaces.GetNamespacePrefix(stTypeNamespaceURI);
			if( stPrefix.empty() )
			{
				                                                    
				stPrefix = "tns";
				int nPrefix = 0;
				while( elementNamespaces.IsPrefixUsed(stPrefix) )
					stPrefix = "tns" + konga::toString(++nPrefix);
				elementNamespaces.AddNamespaceDeclaration(stTypeNamespaceURI, stPrefix);
				string stNamespaceDeclAttr = "xmlns:" + stPrefix;
				pCopy->setAttribute( stNamespaceDeclAttr.c_str(), stTypeNamespaceURI.c_str() );
			}
		}
		
		string stNewType = XmlUtils::GetQualifiedName(stPrefix, pCromNode->GetSchemaType());
		pCopy->setAttribute(stTypeAttributeName.c_str(), stNewType.c_str());
	}
	else
	{
		pCopy->removeAttribute(stTypeAttributeName.c_str());
		pCopy->setAttribute(stTypeAttributeName.c_str(), stTypeAttributeValue.c_str());
	}
}



bool WebServiceResponseSynchronizer::AlreadyProcessed(DOMNodePtr pNode)
{
	DOMNodeMapKey key(pNode);
	return m_mapProcessedNodes.find(key) != m_mapProcessedNodes.end();
}


void WebServiceResponseSynchronizer::OnStructureMisMatch(CROM* pCromNode, 
														 CROM* pCromParent,
														 const std::string& stNodeName, 
														 const std::string& stNamespace)
{
	                                                                               
                                 

                                                                                                           
                                                                                               
                             
                                         
                              
                                  
                                                            
                                             
                                                                              
                     
                       
     
  
                        
                                          
            
             
   
                
                          
                                                                   
                             
   
                       
  
   
}

