/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <string>
#include <vector>

#include <konga/Utils/CrException.h>
#include <konga/Utils/StringUtils.h>
#include <konga/Utils/PortDefs.h>	                                                           

#include <konga/Xml/DOMNodeListPtr.h>

#include "XsdGroup.h"
#include "XSDParser.h"
#include "XsdUtils.h"
#include "crom.h"

using namespace std;
using namespace konga;
using konga::CrException;

XsdGroup::XsdGroup(XSDParser& xsdParser, 
				   const konga::xml::DOMElementPtr pDOMNode, 
				   CROM* pCROMParent, 
				   CROM* pPreviousSibling,
				   XsdParserConstants::NodeType lastNodeType) : 
											XsdNode(xsdParser,
													pDOMNode,
													pCROMParent,
													pPreviousSibling,
													lastNodeType),
					m_pTreeMapper(xsdParser.getTreeMapper()),
					m_iMinUse(0),
					m_iMaxUse(0)
{
	m_stReference = GetDOMNodeAttribute(XsdConstants::REF_ATTRIBUTE);
	AbortRecursiveTraversal();
}

XsdGroup::~XsdGroup(void)
{}

unsigned long XsdGroup::Parse(string& stError) {
	                                                                    
	                                                                               
	if (GetName().length() > 0) {
		return ParseGlobalGroup(GetName(), stError);
	} else {
		return ParseLocalGroup(stError);
	}
}

unsigned long XsdGroup::ParseGlobalGroup(const std::string& stName, std::string& stError) {
	CROM* pRoot = new CROM(getTreeMapper(), 0, 0, stName.c_str(), "", 0, CROM::t_CROM_Xml);
	konga::xml::DOMNodeListPtr pChildren = GetDOMNode()->getChildNodes();
	CROM* pPrevSibling = 0;
	for (int i = 0, nChildren = pChildren->getLength(); i < nChildren; ++i) {
		unsigned long hr = m_xsdParser.BuildCROMTree( pChildren->item[i], pRoot, pPrevSibling, XsdParserConstants::t_group);
		if (FAILED(hr)) {
			return hr;
		}
		pPrevSibling = pRoot->GetLastChild();
	}
	SetLastNodeType(XsdParserConstants::t_group);
	SetCROMRoot(pRoot);
	return S_OK;
}

unsigned long XsdGroup::ParseLocalGroup(std::string& stError) {
	CROM* pGroupDef = GetGroupDefinition( GetReference(), stError );
	if (pGroupDef == 0) {
		return E_FAIL;
	}
	
	XsdUtils::GetMinMaxOccurs( GetDOMNode(), m_iMinUse, m_iMaxUse );
	CROM* pParent = GetCROMParent();
	if (pParent == 0) {
		stError = "Parse error while parsing group node: The structure node does not have a parent.";
		stError += " The parsed node was a reference to the group " + GetReference();
		throw CrException(stError);
	}
	CROM* pPrevSibling = GetPreviousSibling();
	                                                                                            
	                                                                                           
	                                                                                
	if (IsGroupRootRequired(pParent)) {
		InsertGroupRoot(pParent, pPrevSibling);
	}
	CloneChildren(pGroupDef, pParent, pPrevSibling);
	SetLastNodeType(XsdParserConstants::t_group);
	return S_OK;
}

bool XsdGroup::IsGroupRootRequired(CROM* pParent) {
	return m_iMinUse != 1 || m_iMaxUse != 1 || 
		(GetLastNodeType() == XsdParserConstants::t_group && pParent->IsOneOfNode());
}

void XsdGroup::InsertGroupRoot(CROM*& pParent, CROM*& pPrevSibling)
{
	int iCROMType = XsdUtils::GetCROMCardinalityType(m_iMinUse, m_iMaxUse);
	CROM* node = new CROM(getTreeMapper(), pParent, pPrevSibling, "", "", iCROMType, CROM::t_CROM_Xml);
	unsigned int groupNumber = m_xsdParser.IncreaseAndGetAnonymousGroupNumber();
	node->m_Name = toString(groupNumber).c_str();
	node->SetMinUse(m_iMinUse);
	node->SetMaxUse(m_iMaxUse);
	pParent = node;
	pPrevSibling = 0;
}

void XsdGroup::CloneChildren(CROM* pGroupDef, CROM* pParent, CROM* pPrevSibling)
{
	CROM* pChild = pGroupDef->GetFirstChild();
	while (pChild != 0) {
		CROM* clone = new CROM(pChild);
		clone->SetParent(pParent);
		if (pPrevSibling == 0) {
			pParent->SetFirstChild(clone);
		} else {
			clone->SetPreviousSibling(pPrevSibling);
			pPrevSibling->SetNextSibling(clone);
		}
		pPrevSibling = clone;
		pChild = pChild->GetNextSibling();
	}
}

CROM* XsdGroup::GetGroupDefinition(const string& stQualifiedName, string& stError) const {
	string stNamespace, stLocalName;
	GetNamespaceAndLocalName(stQualifiedName, stNamespace, stLocalName);
	CROM* pGroupDef = m_xsdParser.GetGroupDefinition(stNamespace, stLocalName);
	if (pGroupDef == 0) {
		stError = m_xsdParser.GetLastError();
	}
	return pGroupDef;
}

string XsdGroup::GetNamespace() const {
	if (HasReference()) {
		string stNamespace, stLocalName;
		GetNamespaceAndLocalName(GetReference(), stNamespace, stLocalName);
		return stNamespace;
	} else {
		return m_xsdParser.GetTargetNamespace();
	}
}
