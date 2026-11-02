/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#ifndef ETLFORGE_TRANSFORMATION_ENGINE_XSDRESTRICTION_H
#define ETLFORGE_TRANSFORMATION_ENGINE_XSDRESTRICTION_H

#include <string>

#include "XsdNode.h"

class TreeMapper;

class XsdRestriction : public XsdNode
{
public:
	XsdRestriction(XSDParser& xsdParser, 
				   const konga::xml::DOMElementPtr& pDOMNode, 
				   CROM* pCROMParent, 
				   CROM* pPreviousSibling,
				   XsdParserConstants::NodeType lastNodeType);
	virtual ~XsdRestriction(void);

	virtual unsigned long Parse(std::string& stError);
	virtual std::string	GetNamespace() const;

private:
	TreeMapper*	m_pTreeMapper;
	std::string				m_stBase;

	unsigned long ParseBaseType();
	unsigned long ParseComplexBaseType(const std::string& stTypeNamespace, const std::string& stLocalTypeName);
	unsigned long ParseSimpleBaseType(const std::string& stTypeNamespace, const std::string& stLocalTypeName);
	CROM* InsertSimpleType(CROM* parent, 
						   const std::string& stTypeNamespace, 
						   const std::string& stLocalTypeName);
	CROM* InsertSimpleCRType(CROM* parent, const std::string& stCRType);
	CROM* BuildRestrictionContent();
	void  MoveRestrictionContent(CROM* parent, CROM* restrictionContentRoot);
	std::string GetCRType(const std::string& stUniversalTypeName);
};
#endif                                   

