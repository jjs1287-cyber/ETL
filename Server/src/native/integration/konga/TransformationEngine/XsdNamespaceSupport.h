/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
#ifndef ETLFORGE_TRANSFORMATION_ENGINE_XSD_NAMESPACE_SUPPORT_H
#define ETLFORGE_TRANSFORMATION_ENGINE_XSD_NAMESPACE_SUPPORT_H

#include <string>
#include <boost/noncopyable.hpp>

#include <konga/Xml/XsdConstants.h>


namespace konga {
	namespace xml {
		class XmlNamespaceDeclarations;
	}
}

class XsdNamespaceSupport : boost::noncopyable {

public:
	XsdNamespaceSupport(const std::string& xsdFilePath);
	~XsdNamespaceSupport();
	
	XsdConstants::SchemaVersion GetSchemaVersion() const {
		return m_version;
	}
	std::string GetTargetNamespace() const {
		return m_targetNamespace;
	}
	std::string GetSchemaDefinitionNamespace() const {
		return m_definitionNamespaceUri;
	}
	std::string GetSchemaInstanceNamespace() const {
		return m_instanceNamespaceUri;
	}
	std::string GetSchemaDefinitionNamespacePrefix() const;
	std::string GetSchemaInstanceNamespacePrefix() const;
	std::string GetNamespacePrefix(const std::string& namespaceUri) const;

private:
	konga::xml::XmlNamespaceDeclarations*	m_namespaces;
	XsdConstants::SchemaVersion				m_version;
	std::string								m_targetNamespace;
	std::string								m_instanceNamespaceUri;
	std::string								m_definitionNamespaceUri;
};

#endif

