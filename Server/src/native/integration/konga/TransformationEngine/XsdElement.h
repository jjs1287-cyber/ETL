/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#ifndef ETLFORGE_TRANSFORMATION_ENGINE_XSDELEMENT_H
#define ETLFORGE_TRANSFORMATION_ENGINE_XSDELEMENT_H

#include <string>

#include "XsdValueNode.h"
class TreeMapper;

class XsdElement :
	public XsdValueNode
{
	TreeMapper*m_pTreeMapper;
	TreeMapper*getTreeMapper(){return m_pTreeMapper;}
public:
	XsdElement(XSDParser& xsdParser, 
			   const konga::xml::DOMElementPtr& pDOMNode, 
			   CROM* pCROMParent,
			   CROM* pPreviousSibling,
			   XsdParserConstants::NodeType lastNodeType);
	virtual ~XsdElement(void);

	virtual unsigned long Parse(std::string& stError);
	virtual std::string	GetNamespace() const;

private:
	bool		m_isNillable;
	std::string	m_substitutionGroup;

	unsigned long InsertElementReference(CROM* element, const std::string& stNamespace, const std::string& stLocalName);
	void CreateSubstitutionGroupElements(CROM* parent, const std::string& stUniversalName);
	bool IsSubstitutionGroupElement() const {
		return !m_substitutionGroup.empty();
	}
	bool IsGlobal() const;
	void CheckNillable(CROM* root, CROM* valueNode = 0);
};

#endif                               

