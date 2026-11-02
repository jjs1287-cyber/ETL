/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */

                                                                                 
                                                                              
                                                         

#ifndef XSD_FROM_WSDL_MAIN_SCHEMA_H
#define XSD_FROM_WSDL_MAIN_SCHEMA_H

#include <konga/Xml/DOMNodeListPtr.h>

#include "XsdUtils.h"
#include "XsdFromWsdlUtils.h"

class XsdFromWsdlBridgeSchema;
class CErrorLog;

class XsdFromWsdlMainSchema
{
public:
	XsdFromWsdlMainSchema(CErrorLog*pErrorLog) : m_isHosted(false), m_pNodeCreator(0), m_pErrorLog(pErrorLog) {};
	virtual ~XsdFromWsdlMainSchema(void)
	{
		if( m_pNodeCreator != 0 )
			delete m_pNodeCreator;
	}

	unsigned long	Initialize(StructureMode structureMode,
							   bool isHosted,
							   konga::xml::DOMDocument2Ptr pWsdlDoc,
							   const WsdlXsdCollection& wsdlSchemas,
							   XsdConstants::SchemaVersion schemaVersion,
							   const std::string& stSchemaNamespace,
							   const std::string& stSchemaNamespacePrefix);

	unsigned long CreateDocumentStyle(const std::string& stSchemaFilename,
								konga::xml::DOMNodeListPtr pMessagePartNodes,
								konga::xml::DOMNodeListPtr pHeaderNodes,
								konga::xml::DOMNodeListPtr pRequestMessagePartNodes = 0,
								konga::xml::DOMNodeListPtr pRequestHeaderNodes = 0);

	unsigned long CreateRpcStyle(const std::string& stSchemaFilename,
								 konga::xml::DOMNodeListPtr pHeaderNodes,
								 konga::xml::DOMNodeListPtr pRequestHeaderNodes,
								 XsdFromWsdlBridgeSchema* pInputBridgeSchema, 
								 XsdFromWsdlBridgeSchema* pOutputBridgeSchema);

	unsigned long Save();
	                                           
	unsigned long Validate();
	
	std::string	GetLastError() const { return m_stLastError; }
	std::string	GetValidationError() const { return m_stValidationError; }

	konga::xml::DOMDocument2Ptr	GetSchemaDocument() const { return m_pSchemaDoc; }

	std::string GetSchemaPath() const {
		return m_xsdInfo.GetSchemaFilename();
	}

private:
	konga::xml::DOMDocument2Ptr	m_pSchemaDoc;
	konga::xml::DOMElementPtr	m_pSchemaNode;
	XsdInfo		m_xsdInfo;

	StructureMode	m_structureMode;
	bool			m_isHosted;

	konga::xml::DOMDocument2Ptr	m_pWsdlDoc;
	XsdConstants::SchemaVersion				m_schemaVersion;
	std::string						m_stSchemaNamespace;
	std::string						m_stSchemaNamespacePrefix;

	                                                                                       
	                                                                                                       
	                                        
	                                                                                            
	                                                                                                 
	                     
	std::string	m_arstBridgeSchemaNamespacePrefix[2];

	const WsdlXsdCollection*	m_pWsdlSchemas;

	XsdUtils::SchemaNodeCreator*	m_pNodeCreator;

	CErrorLog *const	m_pErrorLog;
	std::string		m_stLastError;
	std::string		m_stValidationError;

	unsigned long		CreateSchemaDocument();
	unsigned long		CreateSchemaNode();

	unsigned long		AddImportNodesDocument();
	unsigned long		AddImportNodesRpc(XsdFromWsdlBridgeSchema* pInputBridgeSchema, 
								  XsdFromWsdlBridgeSchema* pOutputBridgeSchema);

	unsigned long		CreateHeaderAndBodyDocument(konga::xml::DOMElementPtr pParent,
											konga::xml::DOMNodeListPtr pMessageParts,
											konga::xml::DOMNodeListPtr pHeaders);

	unsigned long		InsertBridgeSchemaReference(konga::xml::DOMElementPtr pParent,
											const std::string& stReference,
											const std::string& stPrefix);

	unsigned long		CreateTopStructure(konga::xml::DOMElementPtr& pLastSequenceNode          );
	konga::xml::DOMElementPtr	LookupHeaderPart(konga::xml::DOMElementPtr pHeaderNode);

	unsigned long CreateHeaderNode(konga::xml::DOMElementPtr pParent, konga::xml::DOMNodeListPtr pHeaders);
};

#endif                               
