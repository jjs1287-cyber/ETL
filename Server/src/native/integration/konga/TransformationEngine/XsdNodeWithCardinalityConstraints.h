/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#ifndef ETLFORGE_TRANSFORMATION_ENGINE_XSDNODEWITHCARDINALITYCONSTRAINTS_H
#define ETLFORGE_TRANSFORMATION_ENGINE_XSDNODEWITHCARDINALITYCONSTRAINTS_H

#include <string>

#include "XsdNode.h"
#include "XsdUtils.h"

class XsdNodeWithCardinalityConstraints :
	public XsdNode
{
public:
	XsdNodeWithCardinalityConstraints(XSDParser& xsdParser, 
									  const konga::xml::DOMElementPtr& pDOMNode, 
									  CROM* pCROMParent, 
									  CROM* pPreviousSibling,
									  XsdParserConstants::NodeType lastNodeType) : 
																	   XsdNode(xsdParser,
																	   pDOMNode,
																	   pCROMParent,
																	   pPreviousSibling,
																	   lastNodeType),
																m_iMinOccurs(1),
																m_iMaxOccurs(1) {};
	virtual ~XsdNodeWithCardinalityConstraints(void) {};

	virtual unsigned long Parse(std::string& stError) = 0;

protected:
	int		GetMinOccurs() const { return m_iMinOccurs; }
	void	SetMinOccurs(int iMinOccurs) { m_iMinOccurs = iMinOccurs; }
	int		GetMaxOccurs() const { return m_iMaxOccurs; }
	void	SetMaxOccurs(int iMaxOccurs) { m_iMaxOccurs = iMaxOccurs; }

	int		GetCROMCardinalityType() const { return XsdUtils::GetCROMCardinalityType(m_iMinOccurs, m_iMaxOccurs); }

private:
	int	m_iMinOccurs, m_iMaxOccurs;
};

#endif


