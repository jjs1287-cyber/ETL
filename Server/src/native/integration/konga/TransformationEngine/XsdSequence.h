/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#ifndef ETLFORGE_TRANSFORMATION_ENGINE_XSDSEQUENCE_H
#define ETLFORGE_TRANSFORMATION_ENGINE_XSDSEQUENCE_H

#include <string>

#include "XsdNodeWithCardinalityConstraints.h"
class TreeMapper;

class XsdSequence :
	public XsdNodeWithCardinalityConstraints
{
	TreeMapper*m_pTreeMapper;
	TreeMapper*getTreeMapper() const {
		return m_pTreeMapper;
	}
public:
	XsdSequence(XSDParser& xsdParser, 
			    konga::xml::DOMElementPtr pDOMNode, 
			    CROM* pCROMParent,
			    CROM* pPreviousSibling,
				XsdParserConstants::NodeType lastNodeType);
	virtual ~XsdSequence(void);

	virtual unsigned long Parse(std::string& stError);

};
#endif 

