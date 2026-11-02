/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#ifndef ETLFORGE_TRANSFORMATION_ENGINE_XSDCOMPLEXTYPE_H
#define ETLFORGE_TRANSFORMATION_ENGINE_XSDCOMPLEXTYPE_H

#include <string>

#include "XsdNode.h"

class TreeMapper;

class XsdComplexType : public XsdNode
{
public:
	XsdComplexType(XSDParser& xsdParser, 
				   const konga::xml::DOMElementPtr& pDOMNode, 
				   CROM* pCROMParent, 
				   CROM* pPreviousSibling,
				   XsdParserConstants::NodeType lastNodeType);
	virtual ~XsdComplexType(void);

	unsigned long Parse(std::string& stError);
	std::string	GetArrayType() const { 
		return m_stArrayType; 
	}
	TreeMapper* getTreeMapper() const {
		return m_pTreeMapper;
	}

private:
	CROM* CreateTypeRoot() const;
	bool IsAnonymousType() const { 
		return GetName().empty(); 
	}

	std::string	m_stArrayType;
	TreeMapper*m_pTreeMapper;
};

#endif
