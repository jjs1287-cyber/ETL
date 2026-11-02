/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
#include <string>

#include <konga/Utils/CrException.h>
#include <konga/Utils/StringUtils.h>
#include <konga/Utils/PortDefs.h>	                                                           

#include <konga/Xml/DOMNodeListPtr.h>

#include "XsdSequence.h"
#include "XSDParser.h"
#include "XsdUtils.h"
#include "crom.h"

using namespace std;
using namespace konga;
using namespace konga::xml;

XsdSequence::XsdSequence(XSDParser& xsdParser, 
					     konga::xml::DOMElementPtr pDOMNode, 
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
	AbortRecursiveTraversal();
}

XsdSequence::~XsdSequence(void)
{}

unsigned long XsdSequence::Parse(string& stError) {
	unsigned long hr = S_OK;
	CROM* root = new CROM(getTreeMapper(), 0, 0, "", "", GetCROMCardinalityType(), CROM::t_CROM_Xml);
	root->SetMinUse( GetMinOccurs() );
	root->SetMaxUse( GetMaxOccurs() );
	
	                                                                                           
	DOMNodeListPtr pChildren = GetDOMNode()->getChildNodes();
	CROM* prevSibling = 0;
	XsdParserConstants::NodeType lastNodeType = GetLastNodeType();
	for (int i = 0, nChildren = pChildren->getLength(); i < nChildren; ++i) {
		hr = m_xsdParser.BuildCROMTree( pChildren->item[i], root, prevSibling, lastNodeType );
		if (FAILED(hr)) {
			return hr;
		}
		prevSibling = root->GetLastChild();
	}

	                                                                                          
	                                                                     
	                                                                              
	CROM* parent = GetCROMParent();
	if (parent == 0) {
		stError = "Parse error while parsing sequence node: The structure node does not have a parent.";
		throw CrException(stError);
	}
	prevSibling = GetPreviousSibling();
	if (GetMinOccurs() != 1 || GetMaxOccurs() != 1 || 
			(GetLastNodeType() == XsdParserConstants::t_group && parent->IsOneOfNode())) {
		int nodeNumber = m_xsdParser.IncreaseAndGetAnonymousGroupNumber();
		string stNodeName(toString(nodeNumber));
		root->m_Name = stNodeName.c_str();
		root->m_Type |= CROM_t_AnonymousGroup;

		root->SetParent(parent);
		if (prevSibling == 0) {
			parent->SetFirstChild(root);
		}
		else {
			root->SetPreviousSibling(prevSibling);
			prevSibling->SetNextSibling(root);
		}
		SetLastNodeType(XsdParserConstants::t_group);
		SetCROMRoot(root);
	}
	else {
		CROM* child = root->GetFirstChild();
		while (child != 0) {
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
		root->SetFirstChild(0);
		root->DeleteNode();
		SetCROMRoot(parent);
		SetLastNodeType(lastNodeType);
	}
	return hr;
}
