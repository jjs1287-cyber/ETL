/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#ifndef ETLFORGE_TRANSFORMATION_ENGINE_XSDATTRIBUTEGROUP_H
#define ETLFORGE_TRANSFORMATION_ENGINE_XSDATTRIBUTEGROUP_H

#include <string>

#include "XsdGroup.h"

class XsdAttributeGroup : public XsdGroup
{
public:
	XsdAttributeGroup(XSDParser& xsdParser, 
					  const konga::xml::DOMElementPtr& pDOMNode, 
					  CROM* pCROMParent, 
					  CROM* pPreviousSibling,
					  XsdParserConstants::NodeType lastNodeType);
	virtual ~XsdAttributeGroup(void);

protected:
	virtual CROM*	GetGroupDefinition(const std::string& stQualifiedName, std::string& stError) const;
};   
#endif 

