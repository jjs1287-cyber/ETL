/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#include <string>

#include <konga/Utils/StringUtils.h>
#include <konga/Utils/PortDefs.h>		       
#include <konga/Xml/XmlUtils.h>
#include <konga/Xml/ExpandedName.h>

#include "XsdSimpleType.h"
#include "XSDParser.h"
#include "crom.h"
#include "XsdUtils.h"

using namespace std;

XsdSimpleType::XsdSimpleType(XSDParser& xsdParser, 
							 konga::xml::DOMElementPtr pDOMNode, 
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
	AbortRecursiveTraversal();
}

XsdSimpleType::~XsdSimpleType(void)
{}

unsigned long XsdSimpleType::Parse(string& stError) {
	unsigned long hr = S_OK;
	konga::xml::DOMElementPtr pNode = GetDOMNode();

	                          
	const string& stQuery = GetEnumerationXPathQuery(true);
	konga::xml::DOMNodeListPtr pEnumerationNodes = pNode->selectNodes(stQuery);
	for (int i = 0, nNodes = pEnumerationNodes->getLength(); i < nNodes; ++i) {
		const string& stValue = konga::XmlUtils::GetAttributeValue( pEnumerationNodes->item[i], XsdConstants::VALUE_ATTRIBUTE );
		if (!stValue.empty()) {
			m_vstEnumerationValues.push_back(stValue);
		}
	}
	
	                                                                         
	m_stCRBaseType = GetCRType(pNode);
	if (m_stCRBaseType.empty()) {
		                                                                                         
		m_stCRBaseType = "String";
	}

	                                                                   
	if (!GetName().empty()) {
		m_xsdParser.AddSimpleTypeMapping( XsdUtils::GetExpandedNameAsString(m_xsdParser.GetTargetNamespace(), GetName()), m_stCRBaseType );
	}

	XsdParserConstants::NodeType lastNodeType = GetLastNodeType();
	if ((lastNodeType == XsdParserConstants::t_element) || (lastNodeType == XsdParserConstants::t_root)) {
		                                                    
		CROM* node = new CROM(getTreeMapper(), GetCROMParent(), 0, "", "", CROM_t_Value, CROM::t_CROM_Xml);
		node->SetValueType( m_stCRBaseType.c_str() );
		node->set_Domain( GetFormatString().c_str() );
		SetLastNodeType(XsdParserConstants::t_value);
	}
	else if (lastNodeType == XsdParserConstants::t_attribute) {
		                                                    
		GetCROMParent()->SetValueType( m_stCRBaseType.c_str() );
		GetCROMParent()->set_Domain( GetFormatString().c_str() );
	}
	return hr;
}


string XsdSimpleType::GetEnumerationXPathQuery(bool bIncludeRestriction) {
	const string& stSchemaDefNamespacePrefix = m_xsdParser.GetSchemaDefinitionNamespacePrefix();
	string stQuery;
	if (bIncludeRestriction) {
		stQuery = konga::XmlUtils::GetQualifiedName(stSchemaDefNamespacePrefix, XsdConstants::RESTRICTION) + std::string("/");
	}
	stQuery += konga::XmlUtils::GetQualifiedName(stSchemaDefNamespacePrefix, XsdConstants::ENUMERATION);
	return stQuery;
}

string XsdSimpleType::GetCRType(konga::xml::DOMElementPtr pDOMNode) {
	if (pDOMNode == 0) {
		return "";
	}
	pDOMNode = konga::XmlUtils::GetFirstChildElement(pDOMNode);
	if (pDOMNode == 0) {
		return "";
	}

rp:
	std::string stNodeName = konga::XmlUtils::GetNodeBaseName(pDOMNode);
	if (konga::Equals(stNodeName, XsdConstants::RESTRICTION)) {
		konga::xml::DOMElementPtr pNode = pDOMNode;
		std::string stRestrictionBase = konga::XmlUtils::GetAttributeValue(pNode, XsdConstants::BASE_ATTRIBUTE);
		if (stRestrictionBase.length() > 0) {
			return m_xsdParser.GetCRDataType(m_xsdParser.GetExpandedName(stRestrictionBase));
		}
	}
	else if (konga::Equals(stNodeName, "list")) {
		return "String";
	}
	else if (konga::Equals(stNodeName, "union")) {
		return "String";
	}
	else if (konga::Equals(stNodeName, XsdConstants::ANNOTATION)) {
		pDOMNode = pDOMNode->getNextSibling();
		goto rp;
	}

	konga::xml::DOMNodePtr pChild = pDOMNode->getFirstChild();
	while (pChild != 0) {
		std::string stCRType = GetCRType(pChild);
		if (stCRType.length() > 0) {
			return stCRType;
		}
		pChild = pChild->getNextSibling();
	}

	return "";
}

string XsdSimpleType::GetFormatString() {
	string stFormat;
	for (size_t i = 0, nEnums = m_vstEnumerationValues.size(); i < nEnums; ++i) {
		if (!stFormat.empty()) {
			stFormat += "|";
		}
		stFormat += m_vstEnumerationValues[i];
	}
	return stFormat;
}
