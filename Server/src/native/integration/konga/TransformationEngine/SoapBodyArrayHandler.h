/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef ETLFORGE_TRANSFORMATION_ENGINE_SOAPBODYARRAYHEADER_H
#define ETLFORGE_TRANSFORMATION_ENGINE_SOAPBODYARRAYHEADER_H

#include <string>
#include <vector>

#include <konga/Xml/DOMDocumentPtr.h>
#include <konga/Xml/DOMElementPtr.h>

class CROM;

class ArrayAType {

public:
	ArrayAType(const ArrayAType& t) : m_namespace(t.getNamespace()),
									  m_localName(t.getLocalName()), 
									  m_localBaseTypeName(t.getLocalBaseTypeName()),
									  m_levels(t.getLevels()) 
	{}
	ArrayAType() : m_levels(1) {}

	void setType(const std::string& namespaceUri, const std::string& localName, const std::string& localBaseTypeName) {
		m_namespace = namespaceUri;
		m_localName = localName;
		m_localBaseTypeName = localBaseTypeName;
	}

	std::string getNamespace() const {
		return m_namespace;
	}

	std::string getLocalName() const { 
		return m_localName;
	}

	std::string getLocalBaseTypeName() const {
		return m_localBaseTypeName;
	}

	void addLevel() {
		++m_levels;
	}

	bool isNested() const {
		return m_levels > 1;
	}

	void unnest();

	int getLevels() const {
		return m_levels;
	}

	std::string toString(konga::xml::DOMElementPtr domBody, int numberOfOuterItems) const;

private:
	std::string m_namespace;
	std::string	m_localName;
	std::string m_localBaseTypeName;
	int			m_levels;
};


class SoapBodyArrayHandler
{
public:
	SoapBodyArrayHandler(konga::xml::DOMDocument2Ptr document, 
						 konga::xml::DOMElementPtr bodyElement,
						 CROM* bodyCromNode,
						 const std::string& stSchemaTypeAttributeName) : 
			m_document(document),
			m_bodyElement(bodyElement),
			m_bodyCromNode(bodyCromNode),
			m_idCounter(0), 
			m_stSchemaTypeAttributeName(stSchemaTypeAttributeName)
	{}
	bool hasArrayElements();
	void handleArrays();

private:
	konga::xml::DOMDocument2Ptr				m_document;
	konga::xml::DOMElementPtr				m_bodyElement;
	CROM*									m_bodyCromNode;
	int										m_idCounter;
	std::string								m_stSoapEncodingNamespacePrefix;
	std::string								m_stSchemaNamespacePrefix;
	std::string								m_stSchemaTypeAttributeName;
	std::vector<konga::xml::DOMElementPtr>	m_arrayElements;
	std::vector<konga::xml::DOMElementPtr>	m_arrayItemElements;

	void handleDomElement(konga::xml::DOMElementPtr domElement, CROM* cromNode);
	void getArrayType(CROM* node, ArrayAType& atype);
	void handleArrayNode(konga::xml::DOMElementPtr arrayDomHolder, 
						 CROM* arrayCromHolder,
						 const ArrayAType& atype);
	konga::xml::DOMElementPtr handleSimpleArray(konga::xml::DOMElementPtr arrayDomHolder, 
												CROM* arrayCromHolder,
												const ArrayAType& atype);
	konga::xml::DOMElementPtr handleNestedArray(konga::xml::DOMElementPtr arrayDomHolder,
												CROM* arrayCromHolder,
												const ArrayAType& atype);
	konga::xml::DOMElementPtr createArrayElement();
	konga::xml::DOMElementPtr createArrayItem(konga::xml::DOMElementPtr originalItem,
											  const ArrayAType& arrayType);
	void insertArrayItemLink(konga::xml::DOMElementPtr parent,
							 konga::xml::DOMElementPtr target,
							 int refId);
	void insertNewElements();
	int getNextRefId() { 
		return ++m_idCounter; 
	}
	std::string getArrayItemName(konga::xml::DOMElementPtr original);
	void lookupNamespacePrefixes();
	bool isBuiltInSchemaType(const std::string& typeName);
	void setArrayType(konga::xml::DOMElementPtr arrayElement, const ArrayAType& atype, int numberOfItems);
};

#endif
