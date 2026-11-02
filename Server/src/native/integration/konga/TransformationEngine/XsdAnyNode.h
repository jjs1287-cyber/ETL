/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#ifndef ETLFORGE_TRANSFORMATION_ENGINE_XSDANYNODE_H
#define ETLFORGE_TRANSFORMATION_ENGINE_XSDANYNODE_H

#include <string>

#include "XsdNodeWithCardinalityConstraints.h"
class TreeMapper;

class XsdAnyNode : public XsdNodeWithCardinalityConstraints 
{
	TreeMapper*m_pTreeMapper;
	TreeMapper*getTreeMapper() const {
		return m_pTreeMapper;
	}
public:
	XsdAnyNode(XSDParser& xsdParser, 
			   konga::xml::DOMElementPtr pDOMNode, 
			   CROM* pCROMParent,
			   CROM* pPreviousSibling,
			   XsdParserConstants::NodeType lastNodeType);
	virtual ~XsdAnyNode(void);

	virtual unsigned long Parse(std::string& stError);

private:
	std::string	m_stNamespace;
	std::string	m_stProcessContents;
};

#endif 

