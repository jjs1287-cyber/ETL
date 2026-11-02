/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#ifndef ETLFORGE_TRANSFORMATION_ENGINE_XSDATTRIBUTE_H
#define ETLFORGE_TRANSFORMATION_ENGINE_XSDATTRIBUTE_H

#include <string>

#include "XsdValueNode.h"

class XsdAttribute : public XsdValueNode
{
public:
	XsdAttribute(XSDParser& xsdParser, 
			     const konga::xml::DOMElementPtr& pDOMNode, 
				 CROM* pCROMParent,
				 CROM* pPreviousSibling,
				 XsdParserConstants::NodeType lastNodeType);
	virtual ~XsdAttribute(void);

	virtual unsigned long Parse(std::string& stError);
	virtual std::string	GetNamespace() const;
	TreeMapper*getTreeMapper() const {
		return m_pTreeMapper;
	}

private:
	TreeMapper *m_pTreeMapper;

	unsigned long InsertAttributeRef(CROM*& pAttribute,
								   CROM* pParent,
								   CROM* pPrevSibling,
								   const std::string& stNamespace,
								   const std::string& stLocalName);
	bool IsGlobal() const;

};
#endif                                 

