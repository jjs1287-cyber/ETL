/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */

#include <konga/Utils/PortDefs.h>	                                                           

#include "XsdChoiceNode.h"
#include "XSDParser.h"
#include "XsdUtils.h"
#include "crom.h"

#include <konga/Utils/StringUtils.h>

using namespace konga::xml;
using namespace konga;
using namespace std;

XsdChoiceNode::XsdChoiceNode(XSDParser& xsdParser, 
						     const DOMElementPtr& pDOMNode, 
							 CROM* pCROMParent, 
							 CROM* pPreviousSibling,
							 XsdParserConstants::NodeType lastNodeType) : 
										XsdNodeWithCardinalityConstraints(xsdParser, 
																		  pDOMNode, 
																		  pCROMParent, 
																		  pPreviousSibling, 
																		  lastNodeType),
				 m_pTreeMapper(xsdParser.getTreeMapper())
{
	int iMinOccurs, iMaxOccurs;
	XsdUtils::GetMinMaxOccurs(pDOMNode, iMinOccurs, iMaxOccurs);
	SetMinOccurs(iMinOccurs);
	SetMaxOccurs(iMaxOccurs);
}

XsdChoiceNode::~XsdChoiceNode(void)
{}

unsigned long XsdChoiceNode::Parse(string& stError) {
	int iType = CROM_t_OneOf;
	iType |= XsdUtils::GetCROMCardinalityType( GetMinOccurs(), GetMaxOccurs() );
	unsigned int choiceNumber = m_xsdParser.IncreaseAndGetAnonymousGroupNumber();
	std::string stNodeName(toString(choiceNumber));
	CROM* root = new CROM(getTreeMapper(), GetCROMParent(), GetPreviousSibling(), "", "", iType, CROM::t_CROM_Xml);
	root->m_Name = stNodeName.c_str();
	root->SetMinUse( GetMinOccurs() );
	root->SetMaxUse( GetMaxOccurs() );
	SetCROMRoot(root);
	SetLastNodeType(XsdParserConstants::t_group);
	return S_OK;
}
