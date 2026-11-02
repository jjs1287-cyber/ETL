/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <sstream>

#include <konga/Utils/CrException.h>

#include <konga/Soap/SoapEncoding.h>

#include <konga/Xml/XmlUtils.h>
#include <konga/Xml/XmlNamespaceDeclarations.h>
#include <konga/Xml/DOMAttrPtr.h>
#include <konga/Xml/DOMNodeListPtr.h>
#include <konga/Xml/DOMNamedNodeMapPtr.h>
#include <konga/Xml/XsdConstants.h>

#include "SoapBodyArrayHandler.h"
#include "crom.h"

using namespace konga::xml;
using namespace konga::XmlUtils;
using konga::CrException;
using std::string;
using std::stringstream;

namespace {

	string getLocalElementName(DOMElementPtr e) {
		string name, prefix;
		GetNodeNameAndNamespacePrefix(e, name, prefix);
		return name;
	}

	bool hasArrayElementsImpl(CROM* node) {
		if (node->IsArray()) {
			return true;
		}
		CROM* child = node->GetFirstChild();
		while (child != 0) {
			if (hasArrayElementsImpl(child)) {
				return true;
			}
			child = child->GetNextSibling();
		}
		return false;
	}

	void addRefIdAttribute(DOMElementPtr e, int refId) {
		stringstream idAttributeValue;
		idAttributeValue << "id" << refId;
		e->setAttribute("id", idAttributeValue.str());
	}

	void addHrefAttribute(DOMElementPtr e, int refId) {
		stringstream hrefValue;
		hrefValue << "#id" << refId;
		e->setAttribute("href", hrefValue.str());
	}

	void linkElements(DOMElementPtr source, DOMElementPtr target, int refId) {
		addHrefAttribute(source, refId);
		addRefIdAttribute(target, refId);
	}

	void removeAllChildren(DOMElementPtr e) {
		DOMNodeListPtr children = e->getChildNodes();
		for (int n = children->getLength() - 1; n >= 0; --n) {
			e.removeChild(children->item[n]);
		}
		DOMNamedNodeMapPtr attrs = e->getAttributes();
		for (int n = attrs->getLength() - 1; n >= 0; --n) {
			DOMAttributePtr pa = attrs->item[n];
			e.removeAttributeNode(pa);
		}
	}

	string getNamespacePrefix(const ArrayAType& type, DOMElementPtr element, DOMElementPtr bodyNode = 0) {
		const string uri(type.getNamespace());
		                                                                             
		                  
		string prefix(GetNamespacePrefix(element, uri));
		if (prefix.empty() && bodyNode != 0) {
			                                                                                   
			                                                                               
			                                                                                
			                                 
			prefix = GetNamespacePrefix(bodyNode, uri);
		}
		if (prefix.empty()) {
			XmlNamespaceDeclarations elementNamespaces(element);
			                                                                              
			                                                                                  
			                                                                   
			prefix = FindNamespacePrefixInChildNodes(element, uri);
			if (prefix.empty()) {
				                                                                         
				                                                         
				prefix = elementNamespaces.GetNamespacePrefixCreateIfNotExist(uri, "ns");
			}
			else {
				                                                                          
				                                        
				int n = 0;
				string prefix2(prefix);
				while (elementNamespaces.IsPrefixUsed(prefix2)) {
					prefix2 = prefix + konga::toString(++n);
				}
				prefix = prefix2;
			}
			                                             
			AddNamespaceDeclaration(element, uri, prefix);
		}
		return prefix;
	}

	string getTypeAttributeValue(const ArrayAType& type, DOMElementPtr element, DOMElementPtr bodyNode = 0) {
		const string prefix(getNamespacePrefix(type, element, bodyNode));
		string typeValue;
		if (prefix.length() > 0) {
			typeValue = prefix + ":";
		}
		string localName(type.getLocalBaseTypeName());
		if (localName.empty()) {
			localName = type.getLocalName();
		}
		typeValue += localName;
		return typeValue;
	}

	bool isElement(DOMNodePtr node) {
		return (node != 0) && (node->getNodeType() == konga::xml::NODE_ELEMENT);
	}

	DOMElementPtr copyElementRemoveNamespace(DOMDocument2Ptr document, DOMElementPtr parent, DOMElementPtr original) {
		string localName(getLocalElementName(original));
		DOMElementPtr copy = document->createElement(localName);
		parent->appendChild(copy);
		DOMNodePtr child = original->getFirstChild();
		while (child != 0) {
			DOMNodePtr next = child->getNextSibling();
			if (isElement(child)) {
				child = copyElementRemoveNamespace(document, copy, child);
			}
			copy->appendChild(child);
			child = next;
		}
		DOMNamedNodeMapPtr attrs = original->getAttributes();
		for (int n = 0, nmax = attrs->getLength(); n < nmax; ++n) {
			DOMAttributePtr a = attrs->item[n];
			copy->setAttribute(a->getName(), a->getValue());
		}
		return copy;
	}

	string InsertNamespaceDeclaration(DOMElementPtr element, const string& uri, const string& prefixBase) {
		XmlNamespaceDeclarations namespaces(element);
		string prefix(prefixBase);
		int count = 0;
		while (namespaces.IsPrefixUsed(prefix)) {
			prefix = prefixBase + konga::toString(++count);
		}
		AddNamespaceDeclaration(element, uri, prefix);
		return prefix;
	}

	CROM* findCromChild(CROM* parent, DOMElementPtr element, const ArrayAType* type = 0, const string& postfix = "") {
		string name;
		if (type != 0) {
			name = type->getLocalBaseTypeName();
		}
		if (name.empty()) {
			name = getLocalElementName(element);
		}
		if (!postfix.empty()) {
			name += postfix;
		}
		return parent->FindChild(CROM_t_Element, name);
	}
}

string ArrayAType::toString(DOMElementPtr domBody, int numberOfOuterItems) const {
	stringstream ss;
	ss << getTypeAttributeValue(*this, domBody);
	for (int n = 0; n < (m_levels - 1); ++n) {
		ss << "[]";
	}
	ss << "[" << numberOfOuterItems << "]";
	return ss.str();
}

void ArrayAType::unnest() {
	if (m_levels == 1) {
		throw CrException("An attempt was made to unnest a one-level SOAP array atype. This is a bug.");
	}
	--m_levels;
}

bool SoapBodyArrayHandler::hasArrayElements() {
	return hasArrayElementsImpl(m_bodyCromNode);
}

void SoapBodyArrayHandler::handleArrays() {
	lookupNamespacePrefixes();
	handleDomElement(m_bodyElement, m_bodyCromNode);
	insertNewElements();
}

void SoapBodyArrayHandler::handleDomElement(DOMElementPtr domElement, CROM* cromNode) {
	if (cromNode->IsArray()) {
		ArrayAType atype;
		getArrayType(cromNode, atype);
		handleArrayNode(domElement, cromNode, atype);
	}
	else {
		DOMNodeListPtr children = domElement->getChildNodes();
		for (unsigned int n = 0, nmax = children->getLength(); n < nmax; ++n) {
			DOMElementPtr child = children->item[n];
			if (isElement(child)) {
				CROM* cromChild = findCromChild(cromNode, child);
				                                                     
				if (cromChild != 0) {
					handleDomElement(child, cromChild);
				}
			}
		}
	}
}

void SoapBodyArrayHandler::getArrayType(CROM* node, ArrayAType& atype) {
	CROM* child = node->GetFirstChild();
	if (child->IsPrecondition() || child->m_Name == "xsi:nil") {
		child = child->GetNextSibling();
	}
	if (child->IsArray()) {
		atype.addLevel();
		getArrayType(child, atype);
	}
	else {
		CROM* typeNode = child;
		string baseType;
		if (child->IsDerivedTypeHolder()) {
			baseType = child->GetSchemaType();
			typeNode = child->GetFirstChild();
		}
		atype.setType(typeNode->GetSchemaTypeNamespace(), typeNode->GetSchemaType(), baseType);
	}
}

void SoapBodyArrayHandler::handleArrayNode(DOMElementPtr arrayDomHolder, 
										   CROM* arrayCromHolder,
										   const ArrayAType& atype) {
    if (atype.isNested()) {
		handleNestedArray(arrayDomHolder, arrayCromHolder, atype);
	}
	else {
		                                                                                 
		                                   
		int refId = getNextRefId();
		DOMElementPtr arrayElement = handleSimpleArray(arrayDomHolder, arrayCromHolder, atype);
		removeAllChildren(arrayDomHolder);
		linkElements(arrayDomHolder, arrayElement, refId);
	}
}

DOMElementPtr SoapBodyArrayHandler::handleSimpleArray(DOMElementPtr arrayDomHolder, 
													  CROM* arrayCromHolder,
													  const ArrayAType& atype) {
	int items = 0;
	DOMElementPtr arrayDomElement = createArrayElement();
	DOMNodeListPtr children = arrayDomHolder->getChildNodes();
	for (unsigned int n = 0, nmax = children->getLength(); n < nmax; ++n) {
		DOMElementPtr child = children->item[n];
		if (isElement(child)) {
			DOMElementPtr item = createArrayItem(child, atype);
			insertArrayItemLink(arrayDomElement, item, getNextRefId());
			CROM* crom = findCromChild(arrayCromHolder, item, &atype, "_Item");
			handleDomElement(item, crom);
			++items;
		}
	}
	setArrayType(arrayDomElement, atype, items);
	return arrayDomElement;
}

DOMElementPtr SoapBodyArrayHandler::handleNestedArray(DOMElementPtr arrayDomHolder, 
													  CROM* arrayCromHolder,
													  const ArrayAType& atype) {
	int items = 0;
	                                                                                 
	                                   
	int refId = getNextRefId();
	DOMElementPtr arrayDomElement = createArrayElement();
	DOMNodeListPtr children = arrayDomHolder->getChildNodes();
	for (unsigned int n = 0, nmax = children->getLength(); n < nmax; ++n) {
		DOMElementPtr child = children->item[n];
		if (isElement(child)) {
			int itemRefId = getNextRefId();
			ArrayAType childType(atype);
			childType.unnest();
			DOMElementPtr item;
			if (childType.isNested()) {
				CROM* cromChild = arrayCromHolder->FindChild(CROM_t_Element, child->getLocalName());
				                                                     
				item = handleNestedArray(child, cromChild, childType);
			}
			else {
				CROM* crom = arrayCromHolder->FindChild(CROM_t_Element, child->getNodeName());
				                                                
				item = handleSimpleArray(child, crom, childType);
			}
			insertArrayItemLink(arrayDomElement, item, itemRefId);
			++items;
		}
	}
	setArrayType(arrayDomElement, atype, items);
	removeAllChildren(arrayDomHolder);
	linkElements(arrayDomHolder, arrayDomElement, refId);
	return arrayDomElement;
}

DOMElementPtr SoapBodyArrayHandler::createArrayElement() {
	DOMElementPtr arrayElement = m_document->createElement(m_stSoapEncodingNamespacePrefix + ":Array");
	m_arrayElements.push_back(arrayElement);
	return arrayElement;
}

DOMElementPtr SoapBodyArrayHandler::createArrayItem(DOMElementPtr originalItem, const ArrayAType& arrayType) {
	string itemName(getArrayItemName(originalItem));
	if (itemName.empty()) {
		itemName = arrayType.getLocalName();
	}
	const string typePrefix(getNamespacePrefix(arrayType, originalItem, m_bodyElement));
	const string namespaceURI(arrayType.getNamespace());
	DOMElementPtr item;
	if (namespaceURI.length() == 0) {
		item = m_document->createElement(itemName);
	} else {
		item = m_document->createElementNS(namespaceURI, typePrefix + ":" + itemName);
	}
	item->setAttribute(m_stSchemaTypeAttributeName, typePrefix + ":" + arrayType.getLocalName());
	DOMNodePtr child = originalItem->getFirstChild();
	while (child != 0) {
		DOMNodePtr next = child->getNextSibling();
		if (isElement(child)) {
			copyElementRemoveNamespace(m_document, item, child);
		} else {
			item->appendChild(child);
		}
		child = next;
	}
	m_arrayItemElements.push_back(item);
	return item;
}

void SoapBodyArrayHandler::insertArrayItemLink(DOMElementPtr parent, DOMElementPtr target, int refId) {
	DOMElementPtr item = m_document->createElement("Item");
	linkElements(item, target, refId);
	parent->appendChild(item);
}

string SoapBodyArrayHandler::getArrayItemName(DOMElementPtr original) {
	string typeName(original->getAttribute(m_stSchemaTypeAttributeName));
	if (isBuiltInSchemaType(typeName)) {
		string prefix;
		string localName;
		GetPrefixAndLocalName(typeName, prefix, localName);
		return m_stSoapEncodingNamespacePrefix + ":" + localName;
	}
	return "";
}

void SoapBodyArrayHandler::lookupNamespacePrefixes() {
	DOMNodePtr node = m_bodyElement;
	while (node != 0) {
		if (isElement(node)) {
			XmlNamespaceDeclarations ns(node);
			if (m_stSoapEncodingNamespacePrefix.empty()) {
				m_stSoapEncodingNamespacePrefix = ns.GetNamespacePrefix(konga::SoapEncoding::VERSION_11_NAMESPACE);
			}
			if (m_stSchemaNamespacePrefix.empty()) {
				m_stSchemaNamespacePrefix = ns.GetNamespacePrefix(XsdConstants::SCHEMA_DEFINITION_NAMESPACE_2001);
			}
			if (!m_stSoapEncodingNamespacePrefix.empty() && !m_stSchemaNamespacePrefix.empty()) {
				return;
			}
		}
		node = node->getParentNode();
	}
	if (m_stSoapEncodingNamespacePrefix.empty()) {
		m_stSoapEncodingNamespacePrefix = InsertNamespaceDeclaration(m_bodyElement, konga::SoapEncoding::VERSION_11_NAMESPACE, "soap-enc");
	}
	if (m_stSchemaNamespacePrefix.empty()) {
		m_stSchemaNamespacePrefix = InsertNamespaceDeclaration(m_bodyElement, XsdConstants::SCHEMA_DEFINITION_NAMESPACE_2001, "xsd");
	}
}

bool SoapBodyArrayHandler::isBuiltInSchemaType(const string& typeName) {
	if (m_stSchemaNamespacePrefix.empty()) {
		return typeName.find(":") == string::npos;
	} else {
		return typeName.find(m_stSchemaNamespacePrefix + ":") == 0;
	}
}

void SoapBodyArrayHandler::insertNewElements() {
	for (size_t n = 0, nmax = m_arrayElements.size(); n < nmax; ++n) {
		m_bodyElement->appendChild(m_arrayElements[n]);
	}
	for (size_t n = 0, nmax = m_arrayItemElements.size(); n < nmax; ++n) {
		m_bodyElement->appendChild(m_arrayItemElements[n]);
	}
}

void SoapBodyArrayHandler::setArrayType(DOMElementPtr e, const ArrayAType& atype, int numberOfItems) {
	const string type(atype.toString(m_bodyElement, numberOfItems));
	e->setAttribute(m_stSoapEncodingNamespacePrefix + ":arrayType", type);
}
