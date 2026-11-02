/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */

#include <fstream>
#include <sstream>
#include <vector>
#include <konga/Xml/XmlStringBuilder.h>
#include <konga/Xml/ExpandedName.h>
#include "crom.h"
#include "SchemaCollection.h"
#include "XmlCromToJtrWriter.h"
#include "JtrReader.h"

using namespace std;
using namespace konga::xml;

namespace {

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
}

void WriteXmlToOutputFile(const XmlStringBuilder& xml, const string& filePath) {
	ofstream out;
	out.open(filePath.c_str(), fstream::out | fstream::trunc);
	xml.PrintXml(out);
	out.close();
}

void XmlCromToJtrWriter::GetXml(CROM* root, bool isRootExploded, string& receiver) {
	NodeMap types;
	NodeMap substitutions;
	GetXml(root, isRootExploded, types, substitutions, receiver);
}

void XmlCromToJtrWriter::GetXml(CROM* root, bool isRootExploded, const NodeMap& types, const NodeMap& substitutions, string& receiver) {
	XmlStringBuilder xml;
	CreateXml(root, isRootExploded, types, substitutions, xml);
	xml.GetXml(receiver);
}

void XmlCromToJtrWriter::WriteXml(CROM* root, bool isRootExploded, const string& outputFilePath) {
	NodeMap types;
	NodeMap substitutions;
	WriteXml(root, isRootExploded, types, substitutions, outputFilePath);
}

void XmlCromToJtrWriter::WriteXml(CROM* root, bool isRootExploded, const NodeMap& typeMap, const NodeMap& substitutions, const string& outputFilePath) {
	XmlStringBuilder xml;
	CreateXml(root, isRootExploded, typeMap, substitutions, xml);
	WriteXmlToOutputFile(xml, outputFilePath);
}

void XmlCromToJtrWriter::CreateXml(CROM* root, bool isRootExploded, const NodeMap& typeMap, const NodeMap& substitutionMap, XmlStringBuilder& xml) {
	m_substitutions = &substitutionMap;
	xml.AddElement(JTR_ELEMENT);
	xml.AddAttribute(JTR_TYPE, "Xml");
	xml.AddAttribute(JTR_VERSION, konga::toString(getCurrentVersion("Xml")));
	if (isRootExploded) {
		xml.AddAttribute("IsExploded", "true");
	}
	WriteSubstitutionGroupMap(substitutionMap, xml);
	WriteTypeMap(typeMap, xml);
	CromToJtrWriter::WriteCromTree(root, xml);
	WriteStringIndexToXml(xml);
	xml.CloseElement();
}

void XmlCromToJtrWriter::WriteSubstitutionGroupMap(const NodeMap& substitutionMap, konga::xml::XmlStringBuilder& xml) {
	if (substitutionMap.empty()) {
		return;
	}
	xml.AddElement("Elements");
	for (NodeMap::const_iterator cit = substitutionMap.begin(); cit != substitutionMap.end(); ++cit) {
		if (cit->second != 0) {
			CromToJtrWriter::WriteCromTree(cit->second, xml);
		}
	}
	xml.CloseElement();
}

void XmlCromToJtrWriter::WriteTypeMap(const NodeMap& typeMap, XmlStringBuilder& xml) {
	if (typeMap.empty()) {
		return;
	}
	xml.AddElement("ElementTypes");
	for (NodeMap::const_iterator cit = typeMap.begin(); cit != typeMap.end(); ++cit) {
		WriteTypeDefinition(cit->first, cit->second, xml);
	}
	xml.CloseElement();
}

void XmlCromToJtrWriter::WriteTypeDefinition(const string& typeName, CROM* typeDef, XmlStringBuilder& xml) {
	xml.AddElement("ElementType");
	AddAttribute(xml, "Name", GetQualifiedTypeName(typeDef), true);
	CromToJtrWriter::WriteCromTree(typeDef, xml);
	xml.CloseElement();
}

void XmlCromToJtrWriter::WriteExtensionData(CROM* node, konga::xml::XmlStringBuilder& xml) {
	if(node->IsLeaf()){
		xml.AddElement(CROM_ATTR_ELEMENT);
		AddAttribute(xml, CROM_ATTR_NAMESPACE_QUALIFIED, node->IsNamespaceQualified(), false);
		                                                               
		AddAttribute(xml, CROM_ATTR_VALUE_TYPE, node->GetValueType(), true);
		AddAttribute(xml, CROM_ATTR_DEFAULT_VALUE, node->get_DefaultValue(), false);
		AddAttribute(xml, CROM_ATTR_FORMAT, node->get_Format(), false);
		AddAttribute(xml, CROM_ATTR_DOMAIN, node->get_Domain(), false);
		AddAttribute(xml, CROM_ATTR_NAMESPACE, node->GetNamespace(), true);
		AddAttribute(xml, CROM_ATTR_SCHEMA_TYPE, node->GetSchemaType(), true);
		AddAttribute(xml, CROM_ATTR_SCHEMA_TYPE_NAMESPACE, node->GetSchemaTypeNamespace(), true);
		xml.CloseElement();
		return;
	}

	xml.AddElement("Xml");
	FillXmlExtensionAttributes(node, xml);
	AddTypeExtensionElements(node, xml);
	AddSubstitutionGroupElements(node, xml);
	xml.CloseElement();
}

void XmlCromToJtrWriter::FillXmlExtensionAttributes(CROM* node, XmlStringBuilder& xml) {
	AddAttribute(xml, "IsAbstract", node->IsAbstract(), false);
	AddAttribute(xml, "NSQual", node->IsNamespaceQualified(), false);
	AddAttribute(xml, "Min", node->GetMinUse(),1);
	AddAttribute(xml, "Max", node->GetMaxUse(),1);
	if (node->IsArray()) {
		AddAttribute(xml, "ArrayType", node->GetArrayType(), true);
	}
	AddAttribute(xml, "NS", node->GetNamespace(), true);
	const string schemaType(node->GetSchemaType());
	if (!schemaType.empty()) {
		AddAttribute(xml, "TypeNS", node->GetSchemaTypeNamespace(), true);
		AddAttribute(xml, "Type", schemaType, true);
	}
	const string anyElementNamespace(node->GetAnyElementNamespace());
	if (!anyElementNamespace.empty()) {
		AddAttribute(xml, "AnyNS", anyElementNamespace, true);
	}
}

void XmlCromToJtrWriter::AddTypeExtensionElements(CROM* node, XmlStringBuilder& xml) {
	if (!node->hasExtension()) {
		return;
	}
	vector<string> derivedTypeNames;
	node->GetAllDerivedTypes(derivedTypeNames);
	if (derivedTypeNames.empty()) {
		return;
	}
	xml.AddElement("Extensions");
	AddStringIndexList(derivedTypeNames, xml, "Types");
	xml.CloseElement();
}

void XmlCromToJtrWriter::AddSubstitutionGroupElements(CROM* node, XmlStringBuilder& xml) {
	if (m_schemaCollection == 0) {
		return;
	}
	if (!node->IsElement()) {
		return;
	}
	vector<string> v;
	node->GetSubstitutionGroupElements(v);
	if (v.empty()) {
		return;
	}
	xml.AddElement("Substitutions");
	AddStringIndexList(v, xml, "Names");
	xml.CloseElement();
}

void XmlCromToJtrWriter::AddStringIndexList(const vector<string>& values, XmlStringBuilder& xml, const string attributeName) {
	stringstream ss;
	int appendComma = false;
	for (vector<string>::const_iterator cit = values.begin(); cit != values.end(); ++cit) {
		int index = GetIndexForString(*cit);
		if (index < 0) {
			continue;
		}
		if (appendComma) {
			ss << ",";
		}
		else {
			appendComma = true;
		}
		ss << index;
	}
	AddAttribute(xml, attributeName, ss.str(), false);
}

