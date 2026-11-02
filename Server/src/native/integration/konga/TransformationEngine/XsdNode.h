/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef ETLFORGE_TRANSFORMATION_ENGINE_XSDNODE_H
#define ETLFORGE_TRANSFORMATION_ENGINE_XSDNODE_H

#include <string>
#include <konga/Xml/DOMElementPtr.h>
#include "XsdParserConstants.h"

class XSDParser;
class CROM;

class XsdNode
{
public:
	virtual ~XsdNode(void) {};

	virtual unsigned long Parse(std::string& stError) = 0;
	virtual std::string	GetNamespace() const;

	CROM* GetTreeRoot() const { 
		return m_pCROMRoot; 
	}
	XsdParserConstants::NodeType GetLastNodeType() const { 
		return m_lastNodeType; 
	}

	bool ContinueRecursiveTraversal() const { 
		return m_bContinueRecursiveTraversal; 
	}

protected:
	XsdNode(XSDParser& xsdParser, 
			const konga::xml::DOMElementPtr& pDOMNode, 
			CROM* pCROMParent = 0, 
			CROM* pPreviousSibling = 0,
			XsdParserConstants::NodeType lastNodeType = XsdParserConstants::t_unknown);

	XSDParser&					m_xsdParser;

	konga::xml::DOMElementPtr	GetDOMNode() const { return m_pDOMNode; }
	CROM*						GetCROMParent() const { return m_pCROMParent; }
	CROM*						GetPreviousSibling() const { return m_pPreviousSibling; }
	void						SetCROMRoot(CROM* root) { m_pCROMRoot = root; }
	std::string					GetDOMPath() const { return m_stDOMPath; }
	std::string					GetName() const { return m_stName; }

	void	SetLastNodeType(XsdParserConstants::NodeType lastNodeType) { 
		m_lastNodeType = lastNodeType; 
	}
	std::string	GetDOMNodeAttribute(const std::string& stAttributeName) const;

	void	AbortRecursiveTraversal() { 
		m_bContinueRecursiveTraversal = false; 
	}

	void GetNamespaceAndLocalName(const std::string& stQualifiedName, std::string& stNamespaceURI, std::string& stLocalName) const;

private:
	konga::xml::DOMElementPtr	m_pDOMNode;
	CROM*						m_pCROMParent;
	CROM*						m_pPreviousSibling;
	XsdParserConstants::NodeType		m_lastNodeType;
	CROM*						m_pCROMRoot;
	bool						m_bContinueRecursiveTraversal;
	std::string					m_stDOMPath;
	std::string					m_stName;
};

#endif

