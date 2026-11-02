/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */

#ifndef ETLFORGE_TRANSFORMATION_ENGINE_NO_VALUE_CONTENT_XML_CREATOR_H
#define ETLFORGE_TRANSFORMATION_ENGINE_NO_VALUE_CONTENT_XML_CREATOR_H

#include <string>
#include <konga/Xml/DOMDocumentPtr.h>
#include <konga/Xml/DOMElementPtr.h>
#include <konga/Xml/DOMNodePtr.h>

class CROM;
class CErrorLog;
class XsdNamespaceSupport;

class NoValueContentXmlCreator {
public:
	NoValueContentXmlCreator(CErrorLog* errorLog, const XsdNamespaceSupport& ns);
	~NoValueContentXmlCreator() {}
	void CreateNoValueContentXML(CROM* structureRoot,
								 const std::string& stNameOfRootToUse, 
								 const std::string& stNamespaceUri, 
								 const std::string& stOutputFileName);
	static bool HasValueContent(CROM* root);

private:
	CErrorLog* const			m_errorLog;
	const XsdNamespaceSupport&	m_xsdNamespaces;

	konga::xml::DOMElementPtr CreateNoValueContentDOMTree(konga::xml::DOMDocument2Ptr& pDoc, CROM* structureNode, konga::xml::DOMNodePtr pParent);
	std::string GetNamespacePrefix(const std::string& uri) const;
	std::string GetSchemaInstanceNamespace() const;
	std::string GetSchemaInstanceNamespacePrefix() const;
};

#endif

