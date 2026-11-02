/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#ifndef ETLFORGE_TRANSFORMATION_ENGINE_XSDALLNODE_H
#define ETLFORGE_TRANSFORMATION_ENGINE_XSDALLNODE_H

#include <string>

#include "XsdSequence.h"
#include "crom.h"

class XsdAllNode : public XsdSequence
{
public:
	XsdAllNode(XSDParser& xsdParser, 
			   const konga::xml::DOMElementPtr& pDOMNode, 
			   CROM* pCROMParent,
			   CROM* pPreviousSibling,
			   XsdParserConstants::NodeType lastNodeType) : 
										XsdSequence(xsdParser, 
													pDOMNode, 
													pCROMParent, 
													pPreviousSibling,
													lastNodeType) 
	{};
	virtual ~XsdAllNode(void) {};

	virtual unsigned long	Parse(std::string& stError)
	{
		unsigned long hr = XsdSequence::Parse(stError);
		if( SUCCEEDED(hr) )
		{
			GetTreeRoot()->SetAllGroup(true);
		}
		return hr;
	}
};

#endif                               
