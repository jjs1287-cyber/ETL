/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include "XsdAttributeGroup.h"
#include "XSDParser.h"

using namespace std;

XsdAttributeGroup::XsdAttributeGroup(XSDParser& xsdParser, 
									 const konga::xml::DOMElementPtr& pDOMNode, 
									 CROM* pCROMParent, 
									 CROM* pPreviousSibling,
									 XsdParserConstants::NodeType lastNodeType) : 
															  XsdGroup(xsdParser,
																	   pDOMNode,
																	   pCROMParent,
																	   pPreviousSibling,
																	   lastNodeType)
{}

XsdAttributeGroup::~XsdAttributeGroup(void)
{}

CROM* XsdAttributeGroup::GetGroupDefinition(const string& stQualifiedName, string& stError) const {
	string stNamespace, stLocalName;
	GetNamespaceAndLocalName(stQualifiedName, stNamespace, stLocalName);
	CROM* pGroupDef = m_xsdParser.GetAttributeGroupDefinition(stNamespace, stLocalName);
	if (pGroupDef == 0) {
		stError = m_xsdParser.GetLastError();
	}
	return pGroupDef;
}
