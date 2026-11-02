/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
#ifndef ETLFORGE_TRANSFORMATION_ENGINE_XSDIMPORTNODE_H
#define ETLFORGE_TRANSFORMATION_ENGINE_XSDIMPORTNODE_H

#include <string>

#include <konga/Utils/PortDefs.h>        

#include <konga/Xml/XmlUtils.h>
#include <konga/Xml/XmlConstants.h>

#include "XsdNode.h"

class XsdImportNode : public XsdNode
{
public:
	XsdImportNode(XSDParser& xsdParser, 
				  const konga::xml::DOMElementPtr& pDOMNode) : XsdNode(xsdParser,
															   pDOMNode)
	{
		m_stImportedNamespace = GetDOMNodeAttribute(konga::XmlConstants::NAMESPACE);
		m_stSchemaLocation = GetDOMNodeAttribute(XsdConstants::SCHEMALOCATION_ATTRIBUTE);
	}
	virtual ~XsdImportNode(void) {};

	virtual unsigned long Parse(std::string& stError) { return S_OK; }

	std::string	GetImportedNamespace() const { return m_stImportedNamespace; }
	std::string	GetSchemaLocation() const { return m_stSchemaLocation; }

private:
	std::string	m_stImportedNamespace;
	std::string	m_stSchemaLocation;
};

#endif 

