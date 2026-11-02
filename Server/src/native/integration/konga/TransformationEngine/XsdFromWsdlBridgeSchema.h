/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef XSD_FROM_WSDL_BRIDGE_SCHEMA_H
#define XSD_FROM_WSDL_BRIDGE_SCHEMA_H

#include <vector>

#include <konga/Xml/DOMElementPtr.h>

#include "XsdFromWsdlUtils.h"

                                                                                       
                                                                                    
                                       
  
                                                                                           

class CErrorLog;

class XsdFromWsdlBridgeSchema
{
public:
	XsdFromWsdlBridgeSchema(CErrorLog* pErrorLog) : m_pErrorLog(pErrorLog) {};
	virtual ~XsdFromWsdlBridgeSchema(void) {};

	  
                           
            
                              
                                                      
                                                                             
                                                                                           
                                                                                          
                                                      
                                                                                                  
                                                        
                                                            
                                                                                                              
   
	unsigned long	Create(InputOutputEnum io,
				   const std::string& stBinding,
				   const std::string& stOperation,
				   const std::vector<konga::xml::DOMElementPtr>& messageParts,
				   const std::vector<konga::xml::DOMElementPtr>& soapHeaders,
				   const std::string& stElementName,
				   const std::string& stFilenameBase,
				   const konga::xml::DOMDocument2Ptr& pWsdlDoc,
				   const WsdlXsdCollection& wsdlSchemas,
				   XsdConstants::SchemaVersion schemaVersion);

	unsigned long Validate();

	                                                                                     
	                                                                                                    
	                            
	std::string	GetTargetNamespace() const { return m_stTargetNamespace; }

	                                                                     
	std::string	GetSchemaLocation() const { return m_xsdInfo.GetSchemaFilename(); }

	                                        
	std::string	GetElementName() const { return m_stElementName; }

	std::string	GetLastError() const { return m_stLastError; }
	std::string	GetValidationError() const { return m_stValidationError; }

private:
	InputOutputEnum m_io;
	XsdInfo m_xsdInfo;
	
	std::string m_stBinding;
	std::string m_stOperation;

	konga::xml::DOMDocument2Ptr m_pWsdlDoc;
	
	XsdConstants::SchemaVersion m_schemaVersion;
	std::string m_stSchemaDefinitionNamespace;
	std::string m_stSchemaDefinitionNamespacePrefix;
	std::string m_stTargetNamespace;

	std::string m_stElementName;
	std::string m_stLastError;
	std::string m_stValidationError;

	CErrorLog *const m_pErrorLog;

	  
                                                                                                     
                                                                                                     
                                                                                                         
                                                                                                 
                                                                           
   
	bool	SetSchemaNamespaceInfo(XsdConstants::SchemaVersion schemaVersion);

	unsigned long	CreateSchemaDocumentAndSchemaNode(bool bSchemaNamespaceIsDeclared);

	konga::xml::DOMElementPtr	CreateSchemaRoot(XsdUtils::SchemaNodeCreator& nodeCreator);

	unsigned long	AttachMessageParts(const std::vector<konga::xml::DOMElementPtr>& parts,
									   konga::xml::DOMElementPtr pParent, 
									   const WsdlXsdCollection& wsdlSchemas,
									   XsdUtils::SchemaNodeCreator& nodeCreator);
};

#endif                                 
