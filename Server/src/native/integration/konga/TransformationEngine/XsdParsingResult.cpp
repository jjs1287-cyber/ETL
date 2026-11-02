/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */

                     
#include <konga/Xml/ExpandedName.h>
#include "XsdParsingResult.h"
#include "crom.h"
#include "SchemaCollection.h"
#include "XmlCromToJtrWriter.h"
#include "XSDParser.h"
#include "XsdParserArrayHandler.h"

using namespace std;
using namespace konga::xml;

namespace {


	bool IsTypeDefinition(CROM* node) {
		return (node->m_Type & CROM_t_TypeDefinition) != 0;
	}

	string GetQualifiedTypeName(CROM* node) {
		const string uri(node->GetSchemaTypeNamespace());
		const string local(node->GetSchemaType());
		return ExpandedName(uri, local).toString();
	}

	string GetQualifiedElementName(CROM* node) {
		const string uri(node->GetNamespace());
		const string local(node->m_Name);
		return ExpandedName(uri, local).toString();
	}

	void ClearComplexTypesImpl(CROM* node) {
		node->SetComplexTypeDefinition(0);
		for (CROM* child = node->GetFirstChild(); child != 0; child = child->GetNextSibling()) {
			ClearComplexTypesImpl(child);
		}
	}
}

void XsdParsingResult::Clear() {
	m_referencedTypes.clear();
	m_substitutionElements.clear();
}

void XsdParsingResult::Build() {
	CROM* const root = m_parser.GetRoot();
	CollectReferences(root);
	root->SetNormalOrder();
	root->SetNamespace(m_parser.GetTargetNamespace());
	HandleArrays();
	ClearComplexTypes();
}

void XsdParsingResult::CollectReferences(CROM *node) {
	if (HasNodeBeenProcessed(node)) {
		return;
	}
	m_processedNodes[node] = true;
	for (CROM* child = node->GetFirstChild(); child != 0; child = child->GetNextSibling()) {
		if (!m_hasValueContent && (child->IsValueLeaf() || child->IsAttribute())) {
			m_hasValueContent = true;
		}
		else {
			CollectReferences(child);
		}
	}
	HandleSubstitutionGroups(node);
	                                                                            
	HandleComplexTypes(node);
}

void XsdParsingResult::HandleSubstitutionGroups(CROM *node) {
	if (!node->IsElement()) {
		return;
	}
	const string elementName(GetQualifiedElementName(node));
	vector<string> v;
	node->GetSubstitutionGroupElements(v);
	for (vector<string>::const_iterator cit = v.begin(); cit != v.end(); ++cit) {
		if (strcmp(elementName.c_str(), (*cit).c_str()) == 0) {
			m_substitutionElements[*cit] = node;
			continue;
		}
		if (m_substitutionElements.find(*cit) != m_substitutionElements.end()) {
			continue;
		}
		ExpandedName expName(ExpandedName::fromString(*cit));
		CROM* sub = m_parser.GetSchemaCollection()->GetElementDefinition(expName);
		if (sub != 0) {
			CheckEmptySubstitutionElement(node, sub);
			CollectReferences(sub);
		}
		m_substitutionElements[*cit] = sub;
	}
}

void XsdParsingResult::HandleComplexTypes(CROM* node) {
	if (IsTypeDefinition(node)) {
		HandleDerivedTypes(node);
	}
	else {
		CROM* typeDef = node->GetComplexTypeDefinition();
		if (typeDef != 0) {
			const string schemaType(GetQualifiedTypeName(typeDef));
			                                                                   
			m_referencedTypes[schemaType] = typeDef;
			CollectReferences(typeDef);
			HandleDerivedTypes(typeDef);
		}
	}
}

void XsdParsingResult::CheckEmptySubstitutionElement(CROM* head, CROM* sub) {
	if ((sub->GetFirstChild() == 0) && (sub->GetComplexTypeDefinition() == 0)) {
		                                                               
		                                                        
		                           
		sub->SetComplexTypeDefinition(head->GetComplexTypeDefinition());
		sub->setSchemaType(head->GetSchemaTypeNamespace(), head->GetSchemaType());
		sub->SetAllGroup(head->IsAllGroup());
		CROM::CloneCROMChildren(head, sub);
	}
}

bool XsdParsingResult::IsAlreadyProcessed(CROM* node) {
	if (IsTypeDefinition(node)) {
		return m_referencedTypes.find(GetQualifiedTypeName(node)) != m_referencedTypes.end();
	}
	return false;
}

void XsdParsingResult::HandleDerivedTypes(CROM* baseType) {
	if (!baseType->hasExtension()) {
		return;
	}
	vector<string> derivedTypeNames;
	baseType->GetAllDerivedTypes(derivedTypeNames);
	for (vector<string>::const_iterator cit = derivedTypeNames.begin(); cit != derivedTypeNames.end(); ++cit) {
		CROM* derived = m_parser.GetSchemaCollection()->GetComplexTypeDefinition(
			ExpandedName::fromString(*cit));
		if (derived == 0) {
			                                                           
			continue;
		}
		                                                                   
		CollectReferences(derived);
		m_referencedTypes[*cit] = derived;
	}
}

bool XsdParsingResult::HasTypeBeenProcessed(const string& typeName) const {
	return m_referencedTypes.find(typeName) != m_referencedTypes.end();
}

bool XsdParsingResult::HasNodeBeenProcessed(CROM* node) const {
	return m_processedNodes.find(node) != m_processedNodes.end();
}

void XsdParsingResult::WriteXml(const string& stOutputFilePath) const {
	XmlCromToJtrWriter writer(m_parser.GetSchemaCollection());
	bool isRootExploded = false;
	writer.WriteXml(m_parser.GetRoot(), isRootExploded, m_referencedTypes, m_substitutionElements, stOutputFilePath);
}

void XsdParsingResult::GetXml(string& receiver) const {
	XmlCromToJtrWriter writer(m_parser.GetSchemaCollection());
	bool isRootExploded = false;
	writer.GetXml(m_parser.GetRoot(), isRootExploded, m_referencedTypes, m_substitutionElements, receiver);
}

namespace {
	typedef std::map<std::string, CROM*>::const_iterator NodeMapCit;
}

void XsdParsingResult::HandleArrays() {
	XsdParserArrayHandler handler;
	handler.HandleArraysInCromTree(m_parser.GetRoot());
	for (NodeMapCit cit = m_referencedTypes.begin(); cit != m_referencedTypes.end(); ++cit) {
		handler.HandleArraysInCromTree(cit->second);
	}
}

CROM* XsdParsingResult::GetRoot() const {
	return m_parser.GetRoot();
}

void XsdParsingResult::ClearComplexTypes() const {
	ClearComplexTypesImpl(GetRoot());
	for (NodeMapCit cit = m_referencedTypes.begin(); cit != m_referencedTypes.end(); ++cit) {
		ClearComplexTypesImpl(cit->second);
	}
	for (NodeMapCit cit = m_substitutionElements.begin(); cit != m_substitutionElements.end(); ++cit) {
		ClearComplexTypesImpl(cit->second);
	}
}
