/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */

#include <konga/Utils/FileUtils.h>
#include <konga/Utils/CrException.h>
#include <konga/Utils/StringUtils.h>

#include <konga/Xml/XmlUtils.h>
#include <konga/Xml/XmlConstants.h>
#include <konga/Xml/XmlNamespaceDeclarations.h>

#include <konga/TransformationLogging/ErrorLog.h>

#include "ErrorCodes/resource.h"
#include "XsdFromWsdlCreator.h"
#include "XsdFromWsdlBridgeSchema.h"
#include "WsdlUtils.h"
#include "misc.h"
#include "XsdFromWsdlMainSchema.h"

using namespace std;
using namespace konga::xml;
using konga::CrException;


unsigned long XsdFromWsdlMainSchema::Initialize(StructureMode structureMode,
												bool isHosted,
												DOMDocument2Ptr pWsdlDoc,
												const WsdlXsdCollection& wsdlSchemas,
												XsdConstants::SchemaVersion schemaVersion,
												const string& stSchemaNamespace,
												const string& stSchemaNamespacePrefix)
{
	m_isHosted = isHosted;
	                                                                                          
	m_xsdInfo.Reset();
	                                              
	if( m_pNodeCreator != 0 ) {
		delete m_pNodeCreator;
		m_pNodeCreator = 0;
	}

	m_structureMode = structureMode;
	m_pWsdlDoc = pWsdlDoc;
	m_schemaVersion = schemaVersion;
	m_stSchemaNamespace = stSchemaNamespace;
	m_stSchemaNamespacePrefix = stSchemaNamespacePrefix;

	m_pWsdlSchemas = &wsdlSchemas;

	unsigned long hr = CreateSchemaDocument();
	if( FAILED(hr) )
	{
		                                                                        
		return hr;
	}
	hr = CreateSchemaNode();
	if( FAILED(hr) )
	{
		                                                                        
		return hr;
	}

	m_pNodeCreator = new XsdUtils::SchemaNodeCreator(m_pSchemaDoc, m_schemaVersion, m_stSchemaNamespacePrefix);
	m_pNodeCreator->SetSchemaNode(m_pSchemaNode);

	m_xsdInfo.SetSchemaDocument(m_pSchemaDoc);
	m_xsdInfo.SetSchemaNode(m_pSchemaNode);

	return S_OK;
}

unsigned long XsdFromWsdlMainSchema::CreateSchemaDocument()
{
	std::string stError;
	if( FAILED(konga::XmlUtils::CreateDOMDocument2Instance(m_pSchemaDoc, true, stError)) )
	{
		unsigned long hr = m_pErrorLog->MakeError(__FILE__, __LINE__, ID_XSDFROMWSDL_CREATEMAINSCHEMADOC_FAILED, stError.c_str());
		m_stLastError += std::string((const char*)m_pErrorLog->GetLastErr())+"\n";
		return hr;
	}
	return S_OK;
}

unsigned long XsdFromWsdlMainSchema::CreateSchemaNode()
{
	try
	{
		string stSchemaNodeName = konga::XmlUtils::GetQualifiedName( m_stSchemaNamespacePrefix, XsdConstants::SCHEMA );
		m_pSchemaNode = m_pSchemaDoc->createNode( konga::xml::NODE_ELEMENT, 
												stSchemaNodeName, 
												m_stSchemaNamespace );
		
		                                                                                   
		string stTargetNameSpacePrefix;
		string stError;
		if( konga::XmlUtils::TransferNameSpace(m_pWsdlDoc->getDocumentElement(), 
										m_pSchemaNode, 
										false, 
										true, 
										stTargetNameSpacePrefix,
										stError) != 0 )
		{
			stError = "Failed to transfer the namespace declarations from the WSDL root to the new schema node: " + stError;
			unsigned long hr = m_pErrorLog->MakeError(__FILE__, __LINE__, ID_XSDFROMWSDL_CREATEMAINSCHEMANODE_FAILED, stError.c_str());
			m_stLastError += std::string((const char*)m_pErrorLog->GetLastErr())+"\n";
			return hr;
		}
		
		                                                     
		m_pSchemaNode->setAttribute( konga::XmlConstants::TARGET_NAMESPACE, XsdFromWsdlCreator::XSD_FROM_WSDL_NAMESPACE );

		                                        
		m_pSchemaNode->setAttribute( XsdConstants::ELEMENT_FORM_DEFAULT, XsdConstants::FORM_QUALIFIED );

		m_pSchemaDoc->appendChild(m_pSchemaNode);

		return S_OK;
	}
	catch( CrException& e )
	{
		unsigned long hr = m_pErrorLog->MakeError(__FILE__, __LINE__, ID_XSDFROMWSDL_CREATEMAINSCHEMANODE_FAILED,
			e.GetErrorString().c_str());
		m_stLastError += std::string((const char*)m_pErrorLog->GetLastErr())+"\n";
		return hr;
	}
}

unsigned long XsdFromWsdlMainSchema::AddImportNodesDocument()
{
	for(size_t i = 0; i < m_pWsdlSchemas->Size(); ++i )
	{
		XsdInfo* pXsdInfo = m_pWsdlSchemas->GetXsdInfo(i);
		std::string stImportPath = konga::GetLeafPath(pXsdInfo->GetSchemaFilename());
		unsigned long hr = m_pNodeCreator->InsertImportNode( pXsdInfo->GetTargetNamespace(), stImportPath );
		if( FAILED(hr) )
		{
			hr = m_pErrorLog->MakeError(__FILE__, __LINE__, ID_XML_SCHEMA_INSERT_IMPORT_FAILED,
				pXsdInfo->GetTargetNamespace().c_str(), stImportPath.c_str());
            m_stLastError += std::string((const char*)m_pErrorLog->GetLastErr())+"\n";
			return hr;
		}
	}
	XmlNamespaceDeclarations ns(m_pSchemaNode);
	StringVector uris;
	ns.GetNamespaceURIs(uris);
	for (size_t n = 0, nmax = uris.size(); n < nmax; ++n) {
		string uri(uris[n]);
		for (size_t m = 0, mmax = m_pWsdlSchemas->Size(); m < mmax; ++m) {
			XsdInfo* pXsdInfo = m_pWsdlSchemas->GetXsdInfo(m);
			if (pXsdInfo->ImportsNamespace(uri)) {
				string stLocation(pXsdInfo->GetImportedSchemaLocation(uri));
				unsigned long hr = m_pNodeCreator->InsertImportNode(uri, stLocation);
				if( FAILED(hr) )
				{
					hr = m_pErrorLog->MakeError(__FILE__, __LINE__, ID_XML_SCHEMA_INSERT_IMPORT_FAILED,
						uri.c_str(), stLocation.c_str());
					m_stLastError += std::string((const char*)m_pErrorLog->GetLastErr())+"\n";
					return hr;
				}
			}
		}
	}
	return S_OK;
}

unsigned long XsdFromWsdlMainSchema::AddImportNodesRpc(XsdFromWsdlBridgeSchema* pInputBridgeSchema,
												 XsdFromWsdlBridgeSchema* pOutputBridgeSchema)
{
	                                                                 
	string stSchemaLocation = (m_structureMode.IO == WS_INPUT) ? 
		pInputBridgeSchema->GetSchemaLocation() : pOutputBridgeSchema->GetSchemaLocation();
	stSchemaLocation = konga::GetLeafPath(stSchemaLocation);
	string stTargetNamespace = (m_structureMode.IO == WS_INPUT) ? 
		pInputBridgeSchema->GetTargetNamespace() : pOutputBridgeSchema->GetTargetNamespace();

	unsigned long hr = m_pNodeCreator->InsertImportNode(stTargetNamespace, stSchemaLocation);
	if( FAILED(hr) )
	{
		hr = m_pErrorLog->MakeError(__FILE__, __LINE__, ID_XML_SCHEMA_INSERT_IMPORT_FAILED,
			stTargetNamespace.c_str(), stSchemaLocation.c_str());
		m_stLastError += std::string((const char*)m_pErrorLog->GetLastErr())+"\n";
		return hr;
	}

	                                                               
	m_arstBridgeSchemaNamespacePrefix[0] = m_xsdInfo.AddNamespaceDeclaration(stTargetNamespace, "bridge");

	if( m_structureMode.IO == WS_OUTPUT && pInputBridgeSchema != 0 )
	{
		                                                                               
		std::string stImportPath = konga::GetLeafPath(pInputBridgeSchema->GetSchemaLocation());
		hr = m_pNodeCreator->InsertImportNode( pInputBridgeSchema->GetTargetNamespace(), 
											   stImportPath);
		if( FAILED(hr) )
		{
			hr = m_pErrorLog->MakeError(__FILE__, __LINE__, ID_XML_SCHEMA_INSERT_IMPORT_FAILED,
				pInputBridgeSchema->GetTargetNamespace().c_str(), stImportPath.c_str());
            m_stLastError += std::string((const char*)m_pErrorLog->GetLastErr())+"\n";
			return hr;
		}
		                                                               
		m_arstBridgeSchemaNamespacePrefix[1] = m_xsdInfo.AddNamespaceDeclaration(pInputBridgeSchema->GetTargetNamespace(), "bridge");
	}

	return S_OK;
}

unsigned long XsdFromWsdlMainSchema::CreateRpcStyle(const string& stSchemaFilename,
													DOMNodeListPtr pHeaderNodes,
													DOMNodeListPtr pRequestHeaderNodes,
													XsdFromWsdlBridgeSchema* pInputBridgeSchema,
													XsdFromWsdlBridgeSchema* pOutputBridgeSchema)
{
	m_xsdInfo.SetSchemaFilename(stSchemaFilename);

	unsigned long hr = AddImportNodesRpc(pInputBridgeSchema, pOutputBridgeSchema);
	if( FAILED(hr) )
	{
		hr = m_pErrorLog->MakeError(__FILE__, __LINE__, ID_XSDFROMWSDL_RPCSTYLE_FAILED, m_stLastError.c_str());
		m_stLastError += std::string((const char*)m_pErrorLog->GetLastErr())+"\n";
		return hr;
	}

	DOMElementPtr pSequenceNode;
	hr = CreateTopStructure(pSequenceNode);
	if( FAILED(hr) )
	{
		hr = m_pErrorLog->MakeError(__FILE__, __LINE__, ID_XSDFROMWSDL_RPCSTYLE_FAILED, m_stLastError.c_str());
		m_stLastError += std::string((const char*)m_pErrorLog->GetLastErr())+"\n";
		return hr;
	}

	                                                                                                    
	                                                                                                
	                                                    
	DOMElementPtr pHeaderBodyParent = pSequenceNode;

	                                                                                               
	                                 
	if( m_structureMode.CreateRequestResponseNodes() )
	{
		DOMElementPtr pResponse = 
			m_pNodeCreator->CreateSchemaElementNode( XsdFromWsdlCreator::RESPONSE, pSequenceNode );
		DOMElementPtr pCmplxSeq = 
			m_pNodeCreator->CreateComplexTypeSequenceNode(pResponse);
		pHeaderBodyParent = pCmplxSeq->getFirstChild();
	}

	                         
	hr = CreateHeaderNode(pHeaderBodyParent, pHeaderNodes);
	if (FAILED(hr)) {
		return hr;
	}

	                       
	DOMElementPtr pBody = 
		m_pNodeCreator->CreateSchemaElementNode( XsdFromWsdlCreator::BODY, pHeaderBodyParent );
	                                                  
	string stRef = (m_structureMode.IO == WS_INPUT) ? pInputBridgeSchema->GetElementName() : 
												   pOutputBridgeSchema->GetElementName();
	hr = InsertBridgeSchemaReference(pBody, stRef, m_arstBridgeSchemaNamespacePrefix[0]);
	if (FAILED(hr)) {
		hr = m_pErrorLog->MakeError(__FILE__, __LINE__, ID_XSDFROMWSDL_RPCSTYLE_FAILED, m_stLastError.c_str());
		m_stLastError += std::string((const char*)m_pErrorLog->GetLastErr())+"\n";
		return hr;
	}

	                                                                                                  
	                                           
	if(m_structureMode.CreateRequestResponseNodes() && pInputBridgeSchema != 0) {
		DOMElementPtr pRequest = m_pNodeCreator->CreateSchemaElementNode(XsdFromWsdlCreator::REQUEST, pSequenceNode);
		DOMElementPtr pCmplxSeq = m_pNodeCreator->CreateComplexTypeSequenceNode(pRequest);
		                         
		hr = CreateHeaderNode(pCmplxSeq->getFirstChild(), pRequestHeaderNodes);
		if (FAILED(hr)) {
			return hr;
		}
		                       
		DOMElementPtr pReqBody = m_pNodeCreator->CreateSchemaElementNode( XsdFromWsdlCreator::BODY, pCmplxSeq->getFirstChild() );
		                                                  
		string stRef = pInputBridgeSchema->GetElementName();
		if( FAILED(InsertBridgeSchemaReference( pReqBody, stRef, m_arstBridgeSchemaNamespacePrefix[1] )) )
		{
			hr = m_pErrorLog->MakeError(__FILE__, __LINE__, ID_XSDFROMWSDL_RPCSTYLE_FAILED, m_stLastError.c_str());
            m_stLastError += std::string((const char*)m_pErrorLog->GetLastErr())+"\n";
			return hr;
		}
	}

	return S_OK;
}

unsigned long XsdFromWsdlMainSchema::CreateDocumentStyle(const string& stSchemaFilename,
												   DOMNodeListPtr pMessagePartNodes,
												   DOMNodeListPtr pHeaderNodes,
												   DOMNodeListPtr pRequestMessagePartNodes        ,
												   DOMNodeListPtr pRequestHeaderNodes        )
{
	m_xsdInfo.SetSchemaFilename(stSchemaFilename);

	unsigned long hr = AddImportNodesDocument();
	if( FAILED(hr) )
	{
		hr = m_pErrorLog->MakeError(__FILE__, __LINE__, ID_XSDFROMWSDL_DOCSTYLE_FAILED, m_stLastError.c_str());
		m_stLastError += std::string((const char*)m_pErrorLog->GetLastErr())+"\n";
		return hr;
	}

	DOMElementPtr pSequenceNode;
	hr = CreateTopStructure(pSequenceNode);
	if( FAILED(hr) )
	{
		hr = m_pErrorLog->MakeError(__FILE__, __LINE__, ID_XSDFROMWSDL_DOCSTYLE_FAILED, m_stLastError.c_str());
		m_stLastError += std::string((const char*)m_pErrorLog->GetLastErr())+"\n";
		return hr;
	}

	                                                                                                    
	                                                                                                
	                                                    
	DOMElementPtr pHeaderBodyParent = pSequenceNode;

	                                                                                               
	                                 
	if( m_structureMode.CreateRequestResponseNodes() )
	{
		DOMElementPtr pResponse = 
			m_pNodeCreator->CreateSchemaElementNode( XsdFromWsdlCreator::RESPONSE, pSequenceNode );
		DOMElementPtr pCmplxSeq = 
			m_pNodeCreator->CreateComplexTypeSequenceNode(pResponse);
		pHeaderBodyParent = pCmplxSeq->getFirstChild();
	}

	                                                  
	hr = CreateHeaderAndBodyDocument(pHeaderBodyParent, pMessagePartNodes, pHeaderNodes);
	if( FAILED(hr) )
	{
		hr = m_pErrorLog->MakeError(__FILE__, __LINE__, ID_XSDFROMWSDL_DOCSTYLE_FAILED, m_stLastError.c_str());
		m_stLastError += std::string((const char*)m_pErrorLog->GetLastErr())+"\n";
		return hr;
	}

	                                                                                                  
	                                           
	if( m_structureMode.CreateRequestResponseNodes() &&
		( (pRequestMessagePartNodes != 0 && pRequestMessagePartNodes->getLength() > 0) ||
		  (pRequestHeaderNodes != 0 && pRequestHeaderNodes->getLength() > 0) ) )
	{
		DOMElementPtr pRequest = 
			m_pNodeCreator->CreateSchemaElementNode( XsdFromWsdlCreator::REQUEST, pSequenceNode );
		DOMElementPtr pCmplxSeq = 
			m_pNodeCreator->CreateComplexTypeSequenceNode(pRequest);

		                                                                  
		hr = CreateHeaderAndBodyDocument(pCmplxSeq->getFirstChild(), pRequestMessagePartNodes, pRequestHeaderNodes);
		if( FAILED(hr) )
		{
			hr = m_pErrorLog->MakeError(__FILE__, __LINE__, ID_XSDFROMWSDL_DOCSTYLE_FAILED, m_stLastError.c_str());
            m_stLastError += std::string((const char*)m_pErrorLog->GetLastErr())+"\n";
			return hr;
		}
	}

	return S_OK;
}

unsigned long XsdFromWsdlMainSchema::CreateHeaderNode(DOMElementPtr pParent, DOMNodeListPtr pHeaders) {
	if (pHeaders == 0 || pHeaders->getLength() == 0) {
		return S_OK;
	}
	                                                        
	DOMElementPtr pHeader = m_pNodeCreator->CreateSchemaElementNode(XsdFromWsdlCreator::HEADER, pParent);
	pHeader->setAttribute(XsdConstants::MIN_OCCURS, "0");
	DOMElementPtr pCmplxSeq = m_pNodeCreator->CreateComplexTypeSequenceNode(pHeader);

	                                                               
	for (int i = 0, n = pHeaders->getLength(); i < n; ++i) {
		DOMElementPtr pPartNode = LookupHeaderPart(pHeaders->item[i]);
		if (pPartNode != 0) {
			string elementRef;
			const string element(konga::XmlUtils::GetAttributeValue(pPartNode, WsdlUtils::ELEMENT_ATTRIBUTE));
			if (element.size()) {
				                                                                                             
				                    
				elementRef = element;
			}
			else {
				const string name(konga::XmlUtils::GetAttributeValue(pPartNode, WsdlUtils::NAME_ATTRIBUTE));
				if (name.size()) {
					                                                                                                
					int prefixIndex = m_structureMode.CreateRequestResponseNodes() ? 1 : 0;
					const string prefix(m_arstBridgeSchemaNamespacePrefix[prefixIndex]);
					elementRef = konga::XmlUtils::GetQualifiedName(m_arstBridgeSchemaNamespacePrefix[prefixIndex], name);
				}
				else {
					                               
					throw CrException("Header part with unknown content encountered in the WSDL.");
				}
			}
			DOMElementPtr pElement = m_pNodeCreator->CreateSchemaElementNode("", pCmplxSeq->getFirstChild());
			pElement->setAttribute(XsdConstants::REF_ATTRIBUTE, elementRef);
		}
		else {
			string stHeaderName = konga::XmlUtils::GetLocalName(
				konga::XmlUtils::GetAttributeValue(pHeaders->item[i], WsdlUtils::NAME_ATTRIBUTE));
			string stMessageName = konga::XmlUtils::GetLocalName(
				konga::XmlUtils::GetAttributeValue(pHeaders->item[i], WsdlUtils::MESSAGE));
			string stPartName = konga::XmlUtils::GetLocalName(
				konga::XmlUtils::GetAttributeValue(pHeaders->item[i], WsdlUtils::PART));
			string stError = "Failed to locate the part '";
			stError += stPartName;
			stError += "' in the message '";
			stError += stMessageName;
			stError += "' that is referred to by the header '";
			stError += stHeaderName;
			stError += "'.";
			unsigned long hr = m_pErrorLog->MakeError(__FILE__, __LINE__, ID_XSDFROMWSDL_DOCSTYLE_MISSING_HEADERPART, stError.c_str());
            m_stLastError += std::string((const char*)m_pErrorLog->GetLastErr())+"\n";
			return hr;
		}
	}
	return S_OK;
}


unsigned long XsdFromWsdlMainSchema::CreateHeaderAndBodyDocument(DOMElementPtr pParent, 
																 DOMNodeListPtr pMessageParts, 
																 DOMNodeListPtr pHeaders)
{
	try {
		unsigned long hr = CreateHeaderNode(pParent, pHeaders);
		if (FAILED(hr)) {
			return hr;
		}

		                                                      
		DOMElementPtr pBody = m_pNodeCreator->CreateSchemaElementNode(XsdFromWsdlCreator::BODY, pParent);
		DOMElementPtr pPartParent = m_pNodeCreator->CreateComplexTypeSequenceNode(pBody)->getFirstChild();
		                                                                                                  
		                                                                                                       
		if (pMessageParts != 0) {
			for (int i = 0, nParts = pMessageParts->getLength(); i < nParts; ++i) {
				DOMElementPtr pPart = pMessageParts->item[i];
				string stRef(konga::XmlUtils::GetAttributeValue(pPart, WsdlUtils::ELEMENT_ATTRIBUTE));
				string prefix, localName;
				konga::XmlUtils::GetPrefixAndLocalName(stRef, prefix, localName);
				if (!prefix.empty()) {
					const string namespaceURI(konga::XmlUtils::GetNamespaceURI(pPart, prefix));
					if (!namespaceURI.empty()) {
						const string existingPrefix(m_xsdInfo.GetNamespacePrefix(namespaceURI));
						if (existingPrefix.empty()) {
							                                                                           
							                                                                            
							                                    
							const string prefixToUse(m_xsdInfo.AddNamespaceDeclaration(namespaceURI, prefix));
							konga::XmlUtils::AddNamespaceDeclaration(m_pSchemaNode, namespaceURI, prefixToUse);
							stRef = prefixToUse + ":" + localName;
						} else if (!konga::Equals(prefix, existingPrefix)) {
							stRef = existingPrefix + ":" + localName;
						}
					}
				}
				DOMElementPtr pElement = m_pNodeCreator->CreateSchemaElementNode("", pPartParent);
				pElement->setAttribute( XsdConstants::REF_ATTRIBUTE.c_str(), stRef.c_str() );
			}
		}
		return S_OK;
	}
	catch (CrException& e) {
		unsigned long hr = m_pErrorLog->MakeError(__FILE__, __LINE__, ID_XSDFROMWSDL_DOCSTYLE_CREATEHEADERBODY_FAILED,
			e.GetErrorString().c_str());
		m_stLastError += std::string((const char*) m_pErrorLog->GetLastErr()) + "\n";
		return hr;
	}
}


DOMElementPtr XsdFromWsdlMainSchema::LookupHeaderPart(DOMElementPtr pHeaderNode)
{
	string stMessageName = konga::XmlUtils::GetLocalName(
		konga::XmlUtils::GetAttributeValue( pHeaderNode, WsdlUtils::MESSAGE ) );
	string stPartName = konga::XmlUtils::GetLocalName(
		konga::XmlUtils::GetAttributeValue( pHeaderNode, WsdlUtils::PART ) );
	string stQuery = "//wsdl:definitions/wsdl:message[@name='";
	stQuery += stMessageName;
	stQuery += "']/wsdl:part[@name='";
	stQuery += stPartName;
	stQuery += "']";
	return m_pWsdlDoc->selectSingleNode( stQuery );
}

unsigned long XsdFromWsdlMainSchema::InsertBridgeSchemaReference(DOMElementPtr pParent,
														   const string& stReference,
														   const string& stPrefix)
{
	try
	{
		DOMElementPtr pCmplxSeq =
			m_pNodeCreator->CreateComplexTypeSequenceNode(pParent);
		DOMElementPtr pElement =
			m_pNodeCreator->CreateSchemaElementNode("", pCmplxSeq->getFirstChild());
		pElement->setAttribute( XsdConstants::REF_ATTRIBUTE,
								konga::XmlUtils::GetQualifiedName(stPrefix, stReference) );
		return S_OK;
	}
	catch( CrException& e )
	{
		unsigned long hr = m_pErrorLog->MakeError(__FILE__, __LINE__, ID_XSDFROMWSDL_BRIDGESCHEMAREF_INSERT_FAILED, 
			e.GetErrorString().c_str());
		m_stLastError += std::string((const char*)m_pErrorLog->GetLastErr())+"\n";
		return hr;
	}
}


unsigned long XsdFromWsdlMainSchema::CreateTopStructure(DOMElementPtr& pLastSequenceNode          )
{
	                                                                                          
	                                                                                      
	  
	                        
	                   
	              
	                                                                               
	                      
	                 
	  
	                                                                          
	                                     
	try
	{
		DOMElementPtr pElement = 
			m_pNodeCreator->CreateSchemaElementNode( XsdFromWsdlCreator::ROOT, m_pSchemaNode );
		DOMElementPtr pCmplxSeq_1 = 
			m_pNodeCreator->CreateComplexTypeSequenceNode(pElement);
		if( m_structureMode.CreateTransactionNode() )
		{
			DOMElementPtr pTransaction = 
				m_pNodeCreator->CreateSchemaElementNode( XsdFromWsdlCreator::TRANSACTION, pCmplxSeq_1->getFirstChild() );
			string stMinOccurs = m_isHosted ? "1" : "0";
			pTransaction->setAttribute( XsdConstants::MIN_OCCURS, stMinOccurs );
			string stMaxOccurs = m_isHosted ? "1" : XsdConstants::UNBOUNDED;
			pTransaction->setAttribute( XsdConstants::MAX_OCCURS, stMaxOccurs );
			DOMElementPtr pCmplxSeq_2 = 
				m_pNodeCreator->CreateComplexTypeSequenceNode(pTransaction);
			pLastSequenceNode = pCmplxSeq_2->getFirstChild();
		}
		else
		{
			pLastSequenceNode = pCmplxSeq_1->getFirstChild();
		}
		return S_OK;
	}
	catch( CrException& e )
	{
		unsigned long hr = m_pErrorLog->MakeError(__FILE__, __LINE__, ID_XSDFROMWSDL_DOCSTYLE_TOPSTRUCTURE_FAILED, 
						e.GetErrorString().c_str());
		m_stLastError += std::string((const char*)m_pErrorLog->GetLastErr())+"\n";
		return hr;
	}
}

unsigned long XsdFromWsdlMainSchema::Save()
{
	std::string stError;
	if( FAILED(m_xsdInfo.SaveSchema(stError)) )
	{
		unsigned long hr = m_pErrorLog->MakeError(__FILE__, __LINE__, ID_XSDFROMWSDL_MAINSCHEMA_SAVE_FAILED, stError.c_str());
		m_stLastError += std::string((const char*)m_pErrorLog->GetLastErr())+"\n";
		return hr;
	}
	return S_OK;
}

unsigned long XsdFromWsdlMainSchema::Validate()
{
	m_stValidationError = "";
	if( !konga::XmlUtils::ValidateXmlSchema( m_xsdInfo.GetSchemaFilename(), m_stValidationError ) )
	{
		unsigned long hr = m_pErrorLog->MakeError(__FILE__, __LINE__, ID_XSDFROMWSDL_MAINSCHEMA_INVALID, m_stValidationError.c_str());
		m_stLastError += std::string((const char*)m_pErrorLog->GetLastErr())+"\n";
		return hr;
	}
	return S_OK;
}
