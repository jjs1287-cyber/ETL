/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
#include <konga/Utils/PortDefs.h>	                                                           

#include <konga/Xml/XmlUtils.h>
#include <konga/Xml/XmlConstants.h>

#include "XsdAnyNode.h"
#include "XSDParser.h"
#include "XsdUtils.h"
#include "crom.h"

using namespace konga;
using namespace konga::xml;
using namespace std;

XsdAnyNode::XsdAnyNode(XSDParser& xsdParser, 
					   DOMElementPtr pDOMNode, 
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

	m_stNamespace = GetDOMNodeAttribute(XmlConstants::NAMESPACE);
	if (m_stNamespace.empty()) {
		m_stNamespace = XsdConstants::ANY_NAMESPACE;
	}
	m_stProcessContents = GetDOMNodeAttribute(XsdConstants::PROCESS_CONTENTS);
}

XsdAnyNode::~XsdAnyNode(void)
{}

unsigned long XsdAnyNode::Parse(string& stError) {
	unsigned long hr = S_OK;
	int iType = CROM_t_Element | GetCROMCardinalityType();
	CROM* root = new CROM(getTreeMapper(), GetCROMParent(), GetPreviousSibling(), "Any", "", iType, CROM::t_CROM_Xml);
	root->SetMinUse( GetMinOccurs() );
	root->SetMaxUse( GetMaxOccurs() );

	root->MarkAsAnyElement(m_stNamespace);
	if (m_xsdParser.ShouldLocalEntityBeQualified( GetDOMNode(), konga::xml::NODE_ELEMENT )) {
		root->SetNamespace( m_xsdParser.m_stTargetNamespace );
	}
	CROM* pValueNode = new CROM(getTreeMapper(), root, 0, "", "", CROM_t_Value, CROM::t_CROM_Xml);
	pValueNode->SetValueType("String");
	SetLastNodeType(XsdParserConstants::t_element);
	SetCROMRoot(root);
	return hr;
}
