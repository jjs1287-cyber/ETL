/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */

#include <konga/Utils/PortDefs.h>	                                                           

#include "XsdAnyAttribute.h"

#include "XsdUtils.h"
#include "XSDParser.h"
#include "crom.h"

using namespace konga;
using namespace konga::xml;
using namespace std;

XsdAnyAttribute::XsdAnyAttribute(XSDParser& xsdParser, 
								 DOMElementPtr pDOMNode, 
								 CROM* pCROMParent, 
								 CROM* pPreviousSibling,
								 XsdParserConstants::NodeType lastNodeType) : 
														XsdValueNode(xsdParser, 
																	 pDOMNode, 
																	 pCROMParent, 
																	 pPreviousSibling,
																	 lastNodeType),
				 m_pTreeMapper(xsdParser.getTreeMapper())
{
	SetMinOccurs(0);
	SetMaxOccurs(1);
}

XsdAnyAttribute::~XsdAnyAttribute(void)
{
}

unsigned long XsdAnyAttribute::Parse(string& stError)
{
	int iType = CROM_t_Attribute;
	iType |= XsdUtils::GetCROMCardinalityType( GetMinOccurs(), GetMaxOccurs() );
	CROM* root = new CROM(getTreeMapper(), GetCROMParent(), GetPreviousSibling(), "any_attribute", "", iType, CROM::t_CROM_Xml);
	root->SetValueType("String");
	SetCROMRoot(root);
	SetLastNodeType(XsdParserConstants::t_attribute);
	return S_OK;
}
