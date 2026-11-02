/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#ifndef ETLFORGE_TRANSFORMATION_ENGINE_XSDCHOICENODE_H
#define ETLFORGE_TRANSFORMATION_ENGINE_XSDCHOICENODE_H

#include <string>

#include "XsdNodeWithCardinalityConstraints.h"

class XsdChoiceNode : public XsdNodeWithCardinalityConstraints
{
public:
	XsdChoiceNode(XSDParser& xsdParser, 
				  const konga::xml::DOMElementPtr& pDOMNode, 
				  CROM* pCROMParent, 
				  CROM* pPreviousSibling,
				  XsdParserConstants::NodeType lastNodeType);
	virtual ~XsdChoiceNode(void);

	unsigned long Parse(std::string& stError);
	TreeMapper*getTreeMapper() const {
		return m_pTreeMapper;
	}

private:
	TreeMapper*m_pTreeMapper;

};

#endif 

