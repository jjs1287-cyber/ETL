/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
#include <konga/Utils/CrException.h>

#include <konga/Xml/XmlUtils.h>
#include <konga/Xml/XmlNamespaceDeclarations.h>

#include "XsdUtils.h"
#include "XsdNamespaceSupport.h"

using namespace std;
using namespace konga;
using namespace konga::xml;

XsdNamespaceSupport::XsdNamespaceSupport(const string& xsdFilename) {
	string error;
	DOMDocument2Ptr doc = XmlUtils::LoadDoc(xsdFilename, error, false);
	if (doc == 0) {
		throw CrException("Failed to parse the schema document. " + error);
	}
	DOMElementPtr schemaNode(XsdUtils::FindSchemaElement(doc->getFirstChild()));
	if (schemaNode == 0) {
		throw CrException("Failed to locate the schema element in the XSD file " +
			xsdFilename);
	}
	m_namespaces = new XmlNamespaceDeclarations(schemaNode);
	m_targetNamespace = m_namespaces->GetTargetNamespace();
	m_version = XsdUtils::GetSchemaVersion(*m_namespaces);
	if (m_version == XsdConstants::SchemaVersion_Unknown) {
		m_version = XsdUtils::GetDefaultSchemaVersion();
	}
	m_definitionNamespaceUri = XsdUtils::GetSchemaDefinitionNamespace(m_version);
	m_instanceNamespaceUri = XsdUtils::GetSchemaInstanceNamespace(m_version);
}

XsdNamespaceSupport::~XsdNamespaceSupport() {
	delete m_namespaces;
}

string XsdNamespaceSupport::GetSchemaDefinitionNamespacePrefix() const {
	return m_namespaces->GetNamespacePrefix(m_definitionNamespaceUri);
}

string XsdNamespaceSupport::GetSchemaInstanceNamespacePrefix() const {
	return m_namespaces->GetNamespacePrefix(m_instanceNamespaceUri);
}

string XsdNamespaceSupport::GetNamespacePrefix(const string& namespaceUri) const {
	return m_namespaces->GetNamespacePrefix(namespaceUri);
}

