/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */

#include <konga/Xml/XmlUtils.h>
#include "XsdValueNode.h"
#include "XSDParser.h"

XsdValueNode::XsdValueNode(XSDParser& xsdParser, 
						   const konga::xml::DOMElementPtr& pDOMNode, 
						   CROM* pCROMParent, 
						   CROM* pPreviousSibling,
						   XsdParserConstants::NodeType lastNodeType) : 
											XsdNodeWithCardinalityConstraints(xsdParser, 
																			  pDOMNode, 
																			  pCROMParent, 
																			  pPreviousSibling, 
																			  lastNodeType)
{
	m_stType = konga::XmlUtils::GetAttributeValue(pDOMNode, XsdConstants::TYPE_ATTRIBUTE);
	m_stReference = konga::XmlUtils::GetAttributeValue(pDOMNode, XsdConstants::REF_ATTRIBUTE);
	m_stDefaultValue = konga::XmlUtils::GetAttributeValue(pDOMNode, XsdConstants::DEFAULT_ATTRIBUTE);
	m_stFixedValue = konga::XmlUtils::GetAttributeValue(pDOMNode, XsdConstants::FIXED_ATTRIBUTE);
}

void XsdValueNode::MarkAsAnyType() {
	m_stType = konga::XmlUtils::GetQualifiedName(m_xsdParser.GetSchemaDefinitionNamespacePrefix(), XsdConstants::ANY_TYPE);
}
