/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#include <konga/Utils/FileUtils.h>
#include <konga/Utils/StringUtils.h>
#include <konga/Utils/CrException.h>

#include <konga/Xml/XmlUtils.h>
#include <konga/Xml/XmlNamespaceDeclarations.h>

#include <konga/Soap/SoapEncoding.h>

#include <konga/TransformationLogging/ErrorLog.h>

#include "ErrorCodes/resource.h"
#include "WsdlUtils.h"
#include "XsdUtils.h"
#include "misc.h"
#include "XsdFromWsdlBridgeSchema.h"

using namespace std;
using namespace konga::xml;
using konga::CrException;

  
                          
           
                             
                                                     
                                                                            
                                                                                          
                                                                                         
                                                     
                                                                                                 
                                                       
                                                           
                                                                                                             
  
unsigned long XsdFromWsdlBridgeSchema::Create(InputOutputEnum io,
										const string& stBinding,
										const string& stOperation,
										const vector<DOMElementPtr>& messageParts,
										const vector<DOMElementPtr>& soapHeaders,
										const string& stElementName,
										const string& stFilenameBase,
										const DOMDocument2Ptr& pWsdlDoc,
										const WsdlXsdCollection& wsdlSchemas,
										XsdConstants::SchemaVersion schemaVersion)
{
	                                                                                     
	             
	  
	                                              
	                    
	                   
	  
	                 
	                                   
	                 
	               
	                                     
	                                                                                          
	                
	                  
	             
	   
	                  
	                                                           
	                              
	             
	   
	        
	                                                                                                       
	                                                                                                        
	      
	  
	                                                                       
	                                                                 

	m_stElementName = stElementName;
	m_pWsdlDoc = pWsdlDoc;
	m_stBinding = stBinding;
	m_stOperation = stOperation;
	m_io = io;
	m_schemaVersion = schemaVersion;

	                                                                                              
	                                               
	bool bSchemaNamespaceIsDeclared = SetSchemaNamespaceInfo(schemaVersion);

	                                                  
	unsigned long hr = CreateSchemaDocumentAndSchemaNode(bSchemaNamespaceIsDeclared);
	if( FAILED(hr) )
		return hr;

	                                                                     
	string stTargetNamespacePrefix = m_xsdInfo.AddNamespaceDeclaration(m_stTargetNamespace, "tns");

	                                                                                     
	XsdUtils::SchemaNodeCreator nodeCreator( m_xsdInfo.GetSchemaDocument(), schemaVersion, m_stSchemaDefinitionNamespacePrefix );
	nodeCreator.SetSchemaNode( m_xsdInfo.GetSchemaNode() );

	                                                                                                
	XsdInfoVector vSchemasToInclude;
	wsdlSchemas.GetXsdInfo(m_stTargetNamespace, vSchemasToInclude);
	for(size_t i = 0; i < vSchemasToInclude.size(); ++i )
	{
		std::string stIncludePath = konga::GetLeafPath(vSchemasToInclude[i]->GetSchemaFilename());
		hr = nodeCreator.InsertIncludeNode( stIncludePath, m_stLastError);
		if( FAILED(hr) )
		{
			hr = m_pErrorLog->MakeError(__FILE__, __LINE__, ID_XML_SCHEMA_INSERT_INCLUDE_FAILED, 
				m_stTargetNamespace.c_str(), stIncludePath.c_str());
			                                                                                    
			                                                                                    
			hr = m_pErrorLog->MakeError(__FILE__, __LINE__, ID_XSDFROMWSDL_BRIDGESCHEMA_IMPORTS_FAILED, (const char*)m_pErrorLog->GetLastErr());
			m_stLastError += std::string((const char*)m_pErrorLog->GetLastErr())+"\n";
			return hr;
		}
	}

	try
	{
		                           
		DOMElementPtr pSequence = CreateSchemaRoot(nodeCreator);

		                                        
		hr = AttachMessageParts(messageParts, pSequence, wsdlSchemas, nodeCreator);
		if (FAILED(hr)) {
			hr = m_pErrorLog->MakeError(__FILE__, __LINE__, ID_XSDFROMWSDL_BRIDGESCHEMA_MSGPARTS_FAILED, m_stLastError.c_str());
			m_stLastError += std::string((const char*)m_pErrorLog->GetLastErr())+"\n";
			return hr;
		}

		                                          
		hr = AttachMessageParts(soapHeaders, m_xsdInfo.GetSchemaNode(), wsdlSchemas, nodeCreator);
		if (FAILED(hr)) {
			hr = m_pErrorLog->MakeError(__FILE__, __LINE__, ID_XSDFROMWSDL_BRIDGESCHEMA_MSGPARTS_FAILED, m_stLastError.c_str());
			m_stLastError += std::string((const char*)m_pErrorLog->GetLastErr())+"\n";
			return hr;
		}

		string stFilename = stFilenameBase + string("_bridge");
		stFilename += (io == WS_INPUT) ? "_in" : "_out";
		stFilename += ".xsd";
		m_xsdInfo.SetSchemaFilename(stFilename);
		
		                          
		string stError;
		if( FAILED(m_xsdInfo.SaveSchema(stError)) )
		{
			hr = m_pErrorLog->MakeError(__FILE__, __LINE__, ID_XSDFROMWSDL_SAVEBRIDGESCHEMA_FAILED, stError.c_str());
			m_stLastError += std::string((const char*)m_pErrorLog->GetLastErr())+"\n";
			return hr;
		}
	}
	catch( CrException& e )
	{
		hr = m_pErrorLog->MakeError(__FILE__, __LINE__, ID_XSDFROMWSDL_CREATEBRIDGESCHEMA_COM_ERROR,
			(io == WS_INPUT) ? "input" : "output", e.GetErrorString().c_str());
		m_stLastError += std::string((const char*)m_pErrorLog->GetLastErr())+"\n";
		return hr;
	}

	return S_OK;
}


  
                                                                                                     
                                                                                                     
                                                                                                         
                                                                                                 
                                                                           
  
bool XsdFromWsdlBridgeSchema::SetSchemaNamespaceInfo(XsdConstants::SchemaVersion schemaVersion)
{
	                                                                                  
	m_stSchemaDefinitionNamespace = XsdUtils::GetSchemaDefinitionNamespace(m_schemaVersion);

	                                                                          
	konga::xml::XmlNamespaceDeclarations wsdlNamespaces(m_pWsdlDoc->getDocumentElement());
	if( wsdlNamespaces.IsNamespaceDeclared(m_stSchemaDefinitionNamespace) )
	{
		if( !wsdlNamespaces.IsDefaultNamespace(m_stSchemaDefinitionNamespace) )
		{
			m_stSchemaDefinitionNamespacePrefix = wsdlNamespaces.GetNamespacePrefixCreateIfNotExist(m_stSchemaDefinitionNamespace, "xsd");
		}
		return true;
	}
	return false;
}


unsigned long XsdFromWsdlBridgeSchema::CreateSchemaDocumentAndSchemaNode(bool bSchemaNamespaceIsDeclared)
{
	try
	{
		string stError;
		                                       
		konga::xml::DOMDocument2Ptr pXsdDoc;
		unsigned long hr = konga::XmlUtils::CreateDOMDocument2Instance(pXsdDoc, true, stError);
		if( FAILED(hr) )
		{
			hr = m_pErrorLog->MakeError(__FILE__, __LINE__, ID_XSDFROMWSDL_CREATEBRIDGESCHEMADOCUMENT_FAILED, stError.c_str());
			m_stLastError += std::string((const char*)m_pErrorLog->GetLastErr())+"\n";
			return hr;
		}

		                          
		string stSchemaNodeName = konga::XmlUtils::GetQualifiedName( m_stSchemaDefinitionNamespacePrefix, XsdConstants::SCHEMA );
		DOMElementPtr pSchemaNode = pXsdDoc->createNode( konga::xml::NODE_ELEMENT, 
														 stSchemaNodeName.c_str(), 
														 m_stSchemaDefinitionNamespace.c_str() );
		
		                                                                                   
		string stTargetNameSpacePrefix;
		if( konga::XmlUtils::TransferNameSpace(m_pWsdlDoc->getDocumentElement(), 
										pSchemaNode, 
										false, 
										true, 
										stTargetNameSpacePrefix,
										stError) != 0 )
		{
			stError = "Failed to transfer the declared namespaces from the WSDL root to the new schema node. " + stError;
			hr = m_pErrorLog->MakeError(__FILE__, __LINE__, ID_XSDFROMWSDL_CREATEBRIDGESCHEMADOCUMENT_FAILED, stError.c_str());
			m_stLastError += std::string((const char*)m_pErrorLog->GetLastErr())+"\n";
			return hr;
		}
		if( !bSchemaNamespaceIsDeclared )
		{
			                                                                                       
			                                                                                      
			                                                  
			                                                             
			XmlNamespaceDeclarations namespaces(pSchemaNode);
			if (!namespaces.IsExistingNamespaceMapping(m_stSchemaDefinitionNamespacePrefix, m_stSchemaDefinitionNamespace)) {
				konga::XmlUtils::AddNamespaceDeclaration(pSchemaNode, m_stSchemaDefinitionNamespace, m_stSchemaDefinitionNamespacePrefix);
			}
		}

		                           
		if( WsdlUtils::SetSchemaTargetNamespace(m_pWsdlDoc, 
												pSchemaNode, 
												m_stBinding, 
												m_stOperation, 
												m_io == WS_INPUT,
												m_stTargetNamespace,
												stError) != 0 )
		{
			stError = "Failed to set the schema's targetnamespace. " + stError;
			hr = m_pErrorLog->MakeError(__FILE__, __LINE__, ID_XSDFROMWSDL_CREATEBRIDGESCHEMADOCUMENT_FAILED, stError.c_str());
			m_stLastError += std::string((const char*)m_pErrorLog->GetLastErr())+"\n";
			return hr;
		}

		pXsdDoc->appendChild(pSchemaNode);

		m_xsdInfo.SetSchemaDocument(pXsdDoc);
		m_xsdInfo.SetSchemaNode(pSchemaNode);

		return S_OK;
	}
	catch( CrException& e )
	{
		unsigned long hr = m_pErrorLog->MakeError(__FILE__, __LINE__, ID_XSDFROMWSDL_CREATEBRIDGESCHEMADOCUMENT_FAILED, 
			e.GetErrorString().c_str());
        m_stLastError += std::string((const char*)m_pErrorLog->GetLastErr())+"\n";
		return hr;
	}
}

  
                                             
                                  
                
              
                                                                                          
               
  
DOMElementPtr XsdFromWsdlBridgeSchema::CreateSchemaRoot(XsdUtils::SchemaNodeCreator& nodeCreator)
{
	DOMElementPtr pElement = nodeCreator.CreateSchemaElementNode(m_stElementName, m_xsdInfo.GetSchemaNode());
	                                                                                             
	                                                                                                          
	                                          
	return nodeCreator.CreateComplexTypeSequenceNode(pElement)->getFirstChild();
}


unsigned long XsdFromWsdlBridgeSchema::AttachMessageParts(const vector<DOMElementPtr>& parts, 
														  DOMElementPtr parent, 
														  const WsdlXsdCollection& wsdlSchemas,
														  XsdUtils::SchemaNodeCreator& nodeCreator)
{
	unsigned long hr = S_OK;
	try {
		                                                                             
		map<string, bool> mapImportedNamespaces;
		                                                                                        
		                                                                      
		for (vector<DOMElementPtr>::const_iterator it = parts.begin(); it != parts.end(); ++it) {
			DOMElementPtr pPartNode = *it;
			string stName = konga::XmlUtils::GetAttributeValue(pPartNode, WsdlUtils::NAME_ATTRIBUTE);
			string stType = konga::XmlUtils::GetAttributeValue(pPartNode, WsdlUtils::TYPE_ATTRIBUTE);

			                                                                                     
			             

			                                            
			string stPrefix = konga::XmlUtils::GetNamespacePrefix(stType);
			string stTypeNamespace = m_xsdInfo.GetNamespaceURI(stPrefix);
			if (stTypeNamespace.empty()) {
				stTypeNamespace = konga::XmlUtils::GetNamespaceURI(pPartNode, stPrefix);
				if (stTypeNamespace.size()) {
					                                                                                         
					                                                                                     
					                                                                                      
					                   
					string stNewPrefix = m_xsdInfo.AddNamespaceDeclaration(stTypeNamespace, stPrefix);
					if (stNewPrefix.size()) {
						stType = stNewPrefix + ":" + konga::XmlUtils::GetLocalName(stType);
					}
				}
			}
			if(konga::Differs(stTypeNamespace, m_stSchemaDefinitionNamespace) && konga::Differs(stTypeNamespace, m_stTargetNamespace)) {
				                                                                                    
				                                                                 
				if (mapImportedNamespaces.find(stTypeNamespace) == mapImportedNamespaces.end()) {
					if (konga::Equals(stTypeNamespace, konga::SoapEncoding::VERSION_11_NAMESPACE)) {
						                                                                         
						                                        
						string stSoapEncLocation;
						konga::SoapEncoding::GetSchemaLocation( konga::SoapEncoding::SoapEncoding_Version_11, stSoapEncLocation );
						hr = nodeCreator.InsertImportNode(stTypeNamespace, stSoapEncLocation);
						if (FAILED(hr)) {
							hr = m_pErrorLog->MakeError(__FILE__, __LINE__, ID_XML_SCHEMA_INSERT_IMPORT_FAILED, stTypeNamespace.c_str(), stSoapEncLocation.c_str());
                            m_stLastError += std::string((const char*)m_pErrorLog->GetLastErr())+"\n";
							return hr;
						}
					}
					else if (konga::Equals(stTypeNamespace, WsdlUtils::WSDL_NAMESPACE)) {
						                                                                         
						                       
						string stWsdlSchemaLocation;
						WsdlUtils::GetWsdlDefinitionSchemaLocation(stWsdlSchemaLocation);
						hr = nodeCreator.InsertImportNode(stTypeNamespace, stWsdlSchemaLocation);
						if (FAILED(hr)) {
							hr = m_pErrorLog->MakeError(__FILE__, __LINE__, ID_XML_SCHEMA_INSERT_IMPORT_FAILED, stTypeNamespace.c_str(), stWsdlSchemaLocation.c_str());
                            m_stLastError += std::string((const char*)m_pErrorLog->GetLastErr())+"\n";
							return hr;
						}
					}
					else {
						                                                                                       
						                                         
						XsdInfoVector vXsdInfo;
						wsdlSchemas.GetXsdInfo(stTypeNamespace, vXsdInfo);
						for (size_t i = 0; i < vXsdInfo.size(); ++i) {
							std::string stImportPath = konga::GetLeafPath(vXsdInfo[i]->GetSchemaFilename());
							hr = nodeCreator.InsertImportNode( stTypeNamespace, stImportPath );
							if (FAILED(hr)) {
								hr = m_pErrorLog->MakeError(__FILE__, __LINE__, ID_XML_SCHEMA_INSERT_IMPORT_FAILED, stTypeNamespace.c_str(), stImportPath.c_str());
                                m_stLastError += std::string((const char*)m_pErrorLog->GetLastErr())+"\n";
								return hr;
							}
						}
						                                                                                     
						                                                                   
					}

					                                                                                        
					mapImportedNamespaces[stTypeNamespace] = true;
				}
			}

			DOMElementPtr pPartElement = nodeCreator.CreateSchemaElementNode(stName, parent);
			pPartElement->setAttribute(XsdConstants::TYPE_ATTRIBUTE.c_str(), stType.c_str());
		}
	}
	catch (CrException& e) {
		m_stLastError += e.GetErrorString()+"\n";
		return e.GetErrorNumber();
	}
	return S_OK;
}


unsigned long XsdFromWsdlBridgeSchema::Validate()
{
	                              
	m_stValidationError = "";
	if( !konga::XmlUtils::ValidateXmlSchema( m_xsdInfo.GetSchemaFilename(), m_stValidationError ) )
	{
		unsigned long hr = m_pErrorLog->MakeError(__FILE__, __LINE__, ID_XSDFROMWSDL_BRIDGESCHEMA_INVALID, m_stValidationError.c_str());
        m_stLastError += std::string((const char*)m_pErrorLog->GetLastErr())+"\n";
		return hr;
	}
	return S_OK;
}
