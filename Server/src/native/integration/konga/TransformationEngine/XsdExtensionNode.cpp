/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <vector>

#include <konga/Xml/DOMNodeListPtr.h>

#include "XsdExtensionNode.h"
#include "XSDParser.h"
#include "XsdUtils.h"
#include "crom.h"
#include "XsdParserException.h"

using namespace konga;
using namespace konga::xml;
using namespace std;

XsdExtensionNode::XsdExtensionNode(XSDParser& xsdParser, 
								   DOMElementPtr pDOMNode, 
								   CROM* pCROMParent, 
								   CROM* pPreviousSibling,
								   XsdParserConstants::NodeType lastNodeType) : 
															XsdNode(xsdParser,
																	pDOMNode,
																	pCROMParent,
																	pPreviousSibling,
																	lastNodeType),
					m_pTreeMapper(xsdParser.getTreeMapper())
{
	m_stBase = GetDOMNodeAttribute(XsdConstants::BASE_ATTRIBUTE);
	AbortRecursiveTraversal();
}

XsdExtensionNode::~XsdExtensionNode(void)
{}

unsigned long XsdExtensionNode::Parse(string& stError) {
	CROM* parent = GetCROMParent();
	CROM* prevSibling = ParseBaseType(parent);
	return AddExtensionElements(parent, prevSibling);
}


CROM* XsdExtensionNode::ParseBaseType(CROM* parent) {
	if (m_stBase.length() == 0) {
		throw XsdParserException("Extension node with missing base attribute");
	}
	string stTypeNamespace, stLocalTypeName;
	GetNamespaceAndLocalName(m_stBase, stTypeNamespace, stLocalTypeName);
	CROM* node = 0;
	if (m_xsdParser.IsComplexType(stTypeNamespace, stLocalTypeName)) {
		node = ParseComplexBaseType(parent, stTypeNamespace, stLocalTypeName);
	} else {
		node = ParseSimpleBaseType(parent, stTypeNamespace, stLocalTypeName);
	}
	return node;
}


unsigned long XsdExtensionNode::AddExtensionElements(CROM* parent, CROM* prevSibling) {
	                                                     
	XsdParserConstants::NodeType lastNodeType = GetLastNodeType();
	DOMElementPtr pNode = GetDOMNode();
	DOMNodeListPtr pChildren = pNode->getChildNodes();
	for (int i = 0, nChildren = pChildren->getLength(); i < nChildren; ++i) {
		unsigned long hr = m_xsdParser.BuildCROMTree( pChildren->item[i], parent, prevSibling, lastNodeType );
		if (FAILED(hr)) {
			return hr;
		}
		prevSibling = parent->GetLastChild();
	}
	return S_OK;
}


CROM* XsdExtensionNode::ParseComplexBaseType(CROM* parent, 
											 const std::string& stTypeNamespace, 
											 const std::string& stLocalTypeName) {
	bool isGlobal = parent->m_Name.GetLength() > 0;
	XsdParserConstants::ComplexTypeConsumer consumer = isGlobal ? 
		XsdParserConstants::GlobalExtendedType : XsdParserConstants::LocalExtendedType;
	unsigned long hr = m_xsdParser.InsertComplexTypeDefinition(parent, stTypeNamespace, stLocalTypeName, consumer);
	CROM* node = 0;
	if (SUCCEEDED(hr)) {
		node = parent->GetLastChild();
	}
	SetLastNodeType(XsdParserConstants::t_typeRoot);
	return node;
}



CROM* XsdExtensionNode::ParseSimpleBaseType(CROM* parent, 
										    const std::string& stTypeNamespace, 
										    const std::string& stLocalTypeName) {
	string stUniversalTypeName = XsdUtils::GetExpandedNameAsString(stTypeNamespace, stLocalTypeName);
	string stCRType(GetCRType(stUniversalTypeName));
	CROM* node;
	if (stCRType.length() > 0) {
		node = InsertSimpleCRType(parent, stCRType);
	} else {
		node = InsertSimpleType(parent, stTypeNamespace, stLocalTypeName);
	}
	SetLastNodeType(XsdParserConstants::t_value);
	return node;
}


std::string XsdExtensionNode::GetCRType(const std::string& stUniversalTypeName) {
	string stCRType = XsdUtils::GetCRType(stUniversalTypeName);
	if (stCRType.empty()) {
		                                                                   
		stCRType = m_xsdParser.GetCRTypeFromSimpleType(stUniversalTypeName);
	}
	return stCRType;
}


CROM* XsdExtensionNode::InsertSimpleType(CROM* parent, 
										 const std::string& stTypeNamespace, 
										 const std::string& stLocalTypeName) {
	                                     
	CROM* p = m_xsdParser.GetSimpleTypeDefinition(stTypeNamespace, stLocalTypeName);
	if (p != 0) {
		parent->SetFirstChild(p);
		p->SetParent(parent);
		return p;
	} else {
		string stError = "The simple type '";
		stError += stLocalTypeName;
		stError += "' in the namespace '";
		stError += stTypeNamespace;
		stError += "' does not exist in this schema.";
		throw XsdParserException(stError);
	}
}


CROM* XsdExtensionNode::InsertSimpleCRType(CROM* parent, const std::string& stCRType) {
	CROM* node = new CROM(getTreeMapper(), parent, 0, "", "", CROM_t_Value, CROM::t_CROM_Xml);
	node->SetValueType( stCRType.c_str() );
	return node;
}


string XsdExtensionNode::GetNamespace() const {
	if (!m_stBase.empty()) {
		string stNamespace, stLocalName;
		GetNamespaceAndLocalName(m_stBase, stNamespace, stLocalName);
		return stNamespace;
	} else {
		return m_xsdParser.GetTargetNamespace();
	}
}
