/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#ifndef ETLFORGE_TRANSFORMATION_ENGINE_XSDEXTENSIONNODE_H
#define ETLFORGE_TRANSFORMATION_ENGINE_XSDEXTENSIONNODE_H

#include <string>

#include "XsdNode.h"

class TreeMapper;

class XsdExtensionNode : public XsdNode
{
public:
	XsdExtensionNode(XSDParser& xsdParser, 
					 konga::xml::DOMElementPtr pDOMNode, 
					 CROM* pCROMParent, 
					 CROM* pPreviousSibling,
					 XsdParserConstants::NodeType lastNodeType);
	virtual ~XsdExtensionNode(void);

	virtual unsigned long Parse(std::string& stError);
	virtual std::string	GetNamespace() const;
	TreeMapper*getTreeMapper() const {
		return m_pTreeMapper;
	}

private:
	TreeMapper*m_pTreeMapper;
	std::string	m_stBase;

	CROM* ParseBaseType(CROM* parent);
	unsigned long AddExtensionElements(CROM* parent, CROM* prevSibling);
	CROM* ParseComplexBaseType(CROM* parent, 
							   const std::string& stTypeNamespace, 
							   const std::string& stLocalTypeName);
	CROM* ParseSimpleBaseType(CROM* parent, 
							  const std::string& stTypeNamespace, 
							  const std::string& stLocalTypeName);
	CROM* InsertSimpleType(CROM* parent, 
						   const std::string& stTypeNamespace, 
						   const std::string& stLocalTypeName);
	CROM* InsertSimpleCRType(CROM* parent, const std::string& stCRType);
	std::string GetCRType(const std::string& stUniversalTypeName);
	void AddToExtendedTypesMap(CROM* p);
};

#endif 

