/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef XSDNODEFACTORY_H
#define XSDNODEFACTORY_H

#include <konga/Xml/DOMElementPtr.h>
#include <konga/Xml/DOMDocumentPtr.h>
#include <konga/Xml/DOMNodePtr.h>

#include "XsdUtils.h"

#include "XsdElement.h"
#include "XsdAttribute.h"
#include "XsdAnyNode.h"
#include "XsdSequence.h"
#include "XsdAllNode.h"
#include "XsdSimpleType.h"
#include "XsdComplexType.h"
#include "XsdExtensionNode.h"
#include "XsdGroup.h"
#include "XsdAttributeGroup.h"
#include "XsdRestriction.h"
#include "XsdChoiceNode.h"
#include "XsdAnyAttribute.h"

class XsdParser;
class XsdNode;
class CROM;

class XsdNodeFactory
{
public:
	XsdNodeFactory(void) {};
	virtual ~XsdNodeFactory(void) = 0;

	static XsdNode* CreateXsdNode(XSDParser& xsdParser,
								  const konga::xml::DOMElementPtr& pDOMNode, 
								  CROM* pCROMParent = 0, 
								  CROM* pPreviousSibling = 0,
								  XsdParserConstants::NodeType lastNodeType = XsdParserConstants::t_unknown)
	{
		XsdConstants::XsdNodeType xsdNodeType = XsdUtils::GetXsdNode(pDOMNode);
		switch( xsdNodeType )
		{
		case XsdConstants::SequenceNode:
			return new XsdSequence(xsdParser, pDOMNode, pCROMParent, pPreviousSibling, lastNodeType);
		case XsdConstants::AllNode:
			return new XsdAllNode(xsdParser, pDOMNode, pCROMParent, pPreviousSibling, lastNodeType);
		case XsdConstants::ChoiceNode:
			return new XsdChoiceNode(xsdParser, pDOMNode, pCROMParent, pPreviousSibling, lastNodeType);
		case XsdConstants::SimpleTypeNode:
			return new XsdSimpleType(xsdParser, pDOMNode, pCROMParent, pPreviousSibling, lastNodeType);
		case XsdConstants::ComplexTypeNode:
			return new XsdComplexType(xsdParser, pDOMNode, pCROMParent, pPreviousSibling, lastNodeType);
		case XsdConstants::ExtensionNode:
			return new XsdExtensionNode(xsdParser, pDOMNode, pCROMParent, pPreviousSibling, lastNodeType);
		case XsdConstants::RestrictionNode:
			return new XsdRestriction(xsdParser, pDOMNode, pCROMParent, pPreviousSibling, lastNodeType);
		case XsdConstants::GroupNode:
			return new XsdGroup(xsdParser, pDOMNode, pCROMParent, pPreviousSibling, lastNodeType);
		case XsdConstants::AttributeGroupNode:
			return new XsdAttributeGroup(xsdParser, pDOMNode, pCROMParent, pPreviousSibling, lastNodeType);
		case XsdConstants::ElementNode:
			return new XsdElement(xsdParser, pDOMNode, pCROMParent, pPreviousSibling, lastNodeType);
		case XsdConstants::AttributeNode:
			return new XsdAttribute(xsdParser, pDOMNode, pCROMParent, pPreviousSibling, lastNodeType);
		case XsdConstants::AnyNode:
			return new XsdAnyNode(xsdParser, pDOMNode, pCROMParent, pPreviousSibling, lastNodeType);
		case XsdConstants::AnyAttributeNode:
			return new XsdAnyAttribute(xsdParser, pDOMNode, pCROMParent, pPreviousSibling, lastNodeType);
		default:
			return 0;                                                      
		}
	}
	
};

#endif                                   

