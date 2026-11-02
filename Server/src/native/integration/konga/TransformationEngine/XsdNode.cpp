/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <konga/Xml/XmlUtils.h>
#include <konga/Xml/XsdConstants.h>

#include "XsdNode.h"
#include "XSDParser.h"

using std::string;

XsdNode::XsdNode(XSDParser& parser,
				 const konga::xml::DOMElementPtr& domElement,
				 CROM* cromParent,
				 CROM* previousSibling,
				 XsdParserConstants::NodeType lastNodeType) :
						m_xsdParser(parser),
						m_pDOMNode(domElement),
						m_pCROMParent(cromParent),
						m_pPreviousSibling(previousSibling),
						m_lastNodeType(lastNodeType),
						m_pCROMRoot(0),
						m_bContinueRecursiveTraversal(true) 
{
	parser.GetDOMNodePath(domElement, m_stDOMPath);
	m_stName = GetDOMNodeAttribute(XsdConstants::NAME_ATTRIBUTE);
}

string	XsdNode::GetNamespace() const {
	return m_xsdParser.GetTargetNamespace(); 
}

string	XsdNode::GetDOMNodeAttribute(const string& stAttributeName) const {
	return konga::XmlUtils::GetAttributeValue(m_pDOMNode, stAttributeName);
}

void XsdNode::GetNamespaceAndLocalName(const string& stQualifiedName, string& stNamespaceURI, string& stLocalName) const {
	return m_xsdParser.GetNamespaceAndLocalName(m_pDOMNode, stQualifiedName, stNamespaceURI, stLocalName);
}
