/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#ifndef ETLFORGE_TRANSFORMATION_ENGINE_XSDSIMPLETYPE_H
#define ETLFORGE_TRANSFORMATION_ENGINE_XSDSIMPLETYPE_H

#include <string>
#include <vector>

#include "XsdNode.h"

class TreeMapper;

class XsdSimpleType : public XsdNode
{
public:
	XsdSimpleType(XSDParser& xsdParser, 
				  konga::xml::DOMElementPtr pDOMNode, 
				  CROM* pCROMParent, 
				  CROM* pPreviousSibling,
				  XsdParserConstants::NodeType lastNodeType);
	virtual ~XsdSimpleType(void);

	virtual unsigned long Parse(std::string& stError);
	std::string	GetCRBaseType() const { return m_stCRBaseType; }
	std::string GetFormatString();
	TreeMapper*getTreeMapper() const { 
		return m_pTreeMapper;
	}

private:
	TreeMapper*		m_pTreeMapper;
	std::string					m_stCRBaseType;
	std::vector<std::string>	m_vstEnumerationValues;

	std::string GetEnumerationXPathQuery(bool bIncludeRestriction);
	std::string GetCRType(konga::xml::DOMElementPtr pDOMNode);
};

#endif                                  


