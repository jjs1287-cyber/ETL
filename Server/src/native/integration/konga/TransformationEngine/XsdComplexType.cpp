/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */

#include <konga/Utils/PortDefs.h>	                                                           

#include <konga/Xml/XsdConstants.h>
#include <konga/Xml/DOMNodeListPtr.h>
#include <konga/Xml/XmlUtils.h>

#include "XSDParser.h"
#include "XsdUtils.h"
#include "crom.h"
#include "XsdComplexType.h"

using namespace konga::xml;
using namespace std;


XsdComplexType::XsdComplexType(XSDParser& xsdParser, 
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
	AbortRecursiveTraversal();
}

XsdComplexType::~XsdComplexType(void)
{}

CROM* XsdComplexType::CreateTypeRoot() const {
	CROM* typeRoot = new CROM(getTreeMapper(), 0, 0, GetName().c_str(), "", 0, CROM::t_CROM_Xml);
	if (!IsAnonymousType()) {
		typeRoot->setSchemaType(m_xsdParser.GetTargetNamespace(), GetName());
	}
	if (XsdUtils::IsTrue(GetDOMNode(), XsdConstants::ABSTRACT_ATTRIBUTE)) {
		typeRoot->SetAbstract();
	}
	return typeRoot;
}

unsigned long XsdComplexType::Parse(string& stError) {
	unsigned long hr = S_OK;
	CROM* typeRoot = CreateTypeRoot();
	SetCROMRoot(typeRoot);
	XsdParserConstants::NodeType lastNodeType = XsdParserConstants::t_typeRoot;
	CROM* prevSibling = 0;
	DOMNodeListPtr pChildren = GetDOMNode()->getChildNodes();
	bool isMixed = konga::XmlUtils::IsValueTrue(GetDOMNodeAttribute("mixed"));
	for (int i = 0, nChildren = pChildren->getLength(); i < nChildren; ++i) {
		hr = m_xsdParser.BuildCROMTree( pChildren->item[i], typeRoot, prevSibling, lastNodeType );
		if (FAILED(hr)) {
			return hr;
		}
		prevSibling = typeRoot->GetLastChild();
	}

	                                                                                              
	if (IsAnonymousType()) {
		CROM* parent = GetCROMParent();
		CROM* prevSibling = GetPreviousSibling();
		CROM* child = typeRoot->GetFirstChild();
		while (child) {
			child->SetParent(parent);
			if (prevSibling == 0) {
				parent->SetFirstChild(child);
			}
			else {
				child->SetPreviousSibling(prevSibling);
				prevSibling->SetNextSibling(child);
			}
			prevSibling = child;
			child = child->GetNextSibling();
		}
		parent->SetComplexTypeDefinition(typeRoot->GetComplexTypeDefinition());
		parent->SetAllGroup(typeRoot->IsAllGroup());
		typeRoot->SetFirstChild(0);
		typeRoot->DeleteNode();
		typeRoot = parent;
	}
	if (isMixed) {
		                                                          
		                                                             
		CROM* first = typeRoot->GetFirstChild();
		if (first && first->IsOneOfNode()) {
			typeRoot = first;
			first = typeRoot->GetFirstChild();
		}
		CROM* valueNode = new CROM(getTreeMapper(), 0, 0, "", "", CROM_t_Value|CROM_t_Optional, CROM::t_CROM_Xml);
		valueNode->SetParent(typeRoot);
		typeRoot->SetFirstChild(valueNode);
		valueNode->SetNextSibling(first);
		if ( first )first->SetPreviousSibling(valueNode);
	}
	return hr;
}
