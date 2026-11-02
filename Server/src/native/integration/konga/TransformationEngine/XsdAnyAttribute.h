/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#ifndef ETLFORGE_TRANSFORMATION_ENGINE_XSDANYATTRIBUTE_H
#define ETLFORGE_TRANSFORMATION_ENGINE_XSDANYATTRIBUTE_H

#include <string>

#include "XsdValueNode.h"
class TreeMapper;

class XsdAnyAttribute :
	public XsdValueNode
{
	TreeMapper*m_pTreeMapper;
	TreeMapper*getTreeMapper(){return m_pTreeMapper;}
public:
	XsdAnyAttribute(XSDParser& xsdParser, 
			        konga::xml::DOMElementPtr pDOMNode, 
					CROM* pCROMParent,
					CROM* pPreviousSibling,
					XsdParserConstants::NodeType lastNodeType);
	virtual ~XsdAnyAttribute(void);

	virtual unsigned long Parse(std::string& stError);
};

#endif                                    

