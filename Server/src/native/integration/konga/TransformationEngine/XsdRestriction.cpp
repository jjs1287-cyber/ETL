/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#include <string>

#include <konga/Utils/PortDefs.h>

#include <konga/Xml/XmlUtils.h>
#include <konga/Xml/ExpandedName.h>
#include <konga/Xml/XsdConstants.h>

#include "XsdRestriction.h"
#include "XSDParser.h"
#include "XsdParserConstants.h"
#include "XsdParserException.h"
#include "XsdUtils.h"
#include "crom.h"
#include "TreeMapper.h"


using namespace std;
using namespace konga;
using namespace konga::xml;

XsdRestriction::XsdRestriction(XSDParser& xsdParser, 
							   const DOMElementPtr& pDOMNode, 
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
}

XsdRestriction::~XsdRestriction(void)
{}

unsigned long XsdRestriction::Parse(string& stError) {
	unsigned long hr = S_OK;
	if (m_stBase.length() > 0) {
		ExpandedName expandedBaseName(m_xsdParser.GetExpandedName(m_stBase));
		if (m_xsdParser.IsAnyType(expandedBaseName)) {
			return S_OK;
		} else {
			return ParseBaseType();
		}
	} else {
		                        
	}
	return hr;
}

unsigned long XsdRestriction::ParseBaseType() {
	if (m_stBase.length() == 0) {
		throw XsdParserException("Extension node with missing base attribute");
	}
	string stTypeNamespace, stLocalTypeName;
	GetNamespaceAndLocalName(m_stBase, stTypeNamespace, stLocalTypeName);
	if (m_xsdParser.IsComplexType(stTypeNamespace, stLocalTypeName)) {
		return ParseComplexBaseType(stTypeNamespace, stLocalTypeName);
	} else {
		return ParseSimpleBaseType(stTypeNamespace, stLocalTypeName);
	}
}

namespace {

	void AnalyzeRestrictedContent(CROM* restrictionContentRoot, map<string, bool>& attributes, bool& attributesOnly) {
		attributesOnly = true;
		map<CROM*, bool> restrictedContentAttributes;
		CROM* restrictedChild = restrictionContentRoot->GetFirstChild();
		while (restrictedChild != 0) {
			if (restrictedChild->IsAttribute()) {
				ExpandedName expName(restrictedChild->GetNamespace(), (const char*) restrictedChild->GetName());
				attributes[expName.toString()] = true;
			} else {
				attributesOnly = false;
			}
			restrictedChild = restrictedChild->GetNextSibling();
		}
	}

	void CopyBaseTypeContents(CROM* baseType, CROM* restriction, bool restrictionHasAttributesOnly, const map<string, bool>& restrictedAttributes) {
		CROM* baseChild = baseType->GetFirstChild();
		while (baseChild != 0) {
			bool copyToRestriction = false;
			if (baseChild->IsAttribute()) {
				ExpandedName expName(baseChild->GetNamespace(), (const char*) baseChild->GetName());
				if (restrictedAttributes.find(expName.toString()) == restrictedAttributes.end()) {
					                                                                              
					                                                                 
					copyToRestriction = true;
				}
			} else if (restrictionHasAttributesOnly) {
				                                                                                         
				                                                                                     
				             
				copyToRestriction = true;
			}
			if (copyToRestriction) {
				CROM* clone = new CROM(baseChild);
				restriction->addChild(clone);
			}
			baseChild = baseChild->GetNextSibling();
		}
	}
}

unsigned long XsdRestriction::ParseComplexBaseType(const string& stTypeNamespace, const string& stLocalTypeName) {
	CROM* parent = GetCROMParent();
	bool isGlobal = parent->m_Name.GetLength() > 0;
	XsdParserConstants::ComplexTypeConsumer consumer = isGlobal ? 
		XsdParserConstants::GlobalRestrictedType : XsdParserConstants::LocalRestrictedType;
	unsigned long hr = m_xsdParser.InsertComplexTypeDefinition(parent, stTypeNamespace, stLocalTypeName, consumer);
	if (FAILED(hr)) {
		return hr;
	}
	CROM* restrictionContentRoot = BuildRestrictionContent();
	CROM* baseType = parent->GetComplexTypeDefinition();
	bool restrictionHasAttributesOnly = false;
	map<string, bool> restrictedAttributes;
	AnalyzeRestrictedContent(restrictionContentRoot, restrictedAttributes, restrictionHasAttributesOnly);
	CopyBaseTypeContents(baseType, restrictionContentRoot, restrictionHasAttributesOnly, restrictedAttributes);
	MoveRestrictionContent(parent, restrictionContentRoot);
	AbortRecursiveTraversal();
	return S_OK;
}

CROM* XsdRestriction::BuildRestrictionContent() {
	DOMElementPtr pNode = GetDOMNode();
	DOMNodeListPtr pChildren = pNode->getChildNodes();
	CROM* root = new CROM(m_pTreeMapper);
	CROM* prevSibling = 0;
	XsdParserConstants::NodeType lastNodeType = XsdParserConstants::t_root;
	for (int i = 0, nChildren = pChildren->getLength(); i < nChildren; ++i) {
		unsigned long hr = m_xsdParser.BuildCROMTree( pChildren->item[i], root, prevSibling, lastNodeType );
		if (FAILED(hr)) {
			throw XsdParserException(m_xsdParser.GetLastError());
		}
		prevSibling = root->GetLastChild();
	}
	return root;
}

void XsdRestriction::MoveRestrictionContent(CROM* parent, CROM* restrictionContentRoot) {
	CROM* child = restrictionContentRoot->GetFirstChild();
	CROM* prevSibling = parent->GetLastChild();
	child->SetPreviousSibling(prevSibling);
	if (prevSibling != 0) {
		prevSibling->SetNextSibling(child);
	} else {
		parent->SetFirstChild(child);
	}
	while (child != 0) {
		child->SetParent(parent);
		child = child->GetNextSibling();
	}
	restrictionContentRoot->SetFirstChild(0);
	restrictionContentRoot->DeleteNode();
}

unsigned long XsdRestriction::ParseSimpleBaseType(const string& stTypeNamespace, const string& stLocalTypeName) {
	string stUniversalTypeName = XsdUtils::GetExpandedNameAsString(stTypeNamespace, stLocalTypeName);
	string stCRType(GetCRType(stUniversalTypeName));
	CROM* parent = GetCROMParent();
	if (stCRType.length() > 0) {
		InsertSimpleCRType(parent, stCRType);
	} else {
		InsertSimpleType(parent, stTypeNamespace, stLocalTypeName);
	}
	SetLastNodeType(XsdParserConstants::t_value);
	return S_OK;
}

string XsdRestriction::GetCRType(const string& stUniversalTypeName) {
	string stCRType = XsdUtils::GetCRType(stUniversalTypeName);
	if (stCRType.empty()) {
		                                                                   
		stCRType = m_xsdParser.GetCRTypeFromSimpleType(stUniversalTypeName);
	}
	return stCRType;
}

CROM* XsdRestriction::InsertSimpleType(CROM* parent, 
									   const string& stTypeNamespace, 
									   const string& stLocalTypeName) {
	                                     
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


CROM* XsdRestriction::InsertSimpleCRType(CROM* parent, const string& stCRType) {
	CROM* node = new CROM(m_pTreeMapper, parent, 0, "", "", CROM_t_Value, CROM::t_CROM_Xml);
	node->SetValueType(stCRType.c_str());
	return node;
}

string XsdRestriction::GetNamespace() const {
	if (!m_stBase.empty()) {
		string stNamespace, stLocalName;
		GetNamespaceAndLocalName(m_stBase, stNamespace, stLocalName);
		return stNamespace;
	} else {
		return m_xsdParser.GetTargetNamespace();
	}
}
