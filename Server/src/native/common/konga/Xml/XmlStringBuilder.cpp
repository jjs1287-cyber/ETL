/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */

#include <sstream>
#include "XmlStringBuilder.h"
#include <konga/Utils/CrException.h>
#include <konga/Utils/StringUtils.h>	              

using namespace std;
using namespace konga::xml;
using konga::xml_text;

namespace {

	void print_escaped(ostream& out, const std::string& s, bool isAttribute) {
		out << xml_text(s.c_str(), isAttribute);
	}
}

string XmlStringBuilder::INDENT("    ");

XmlStringBuilder::Element::~Element() {
	for (vector<Element*>::iterator it = m_children.begin(); it != m_children.end(); ++it) {
		delete *it;
	}
	m_children.clear();
}

void XmlStringBuilder::Element::PrintXml(ostream& out, const string& indent) const {
	out << indent;
	out << "<" << m_name.c_str();
	PrintAttributes(out);
	if (HasContent()) {
		out << ">";
		PrintValue(out);
		PrintChildren(out, indent);
		out << "</" << m_name.c_str() << ">";
	}
	else {
		out << " />";
	}
	out << endl;
}

void XmlStringBuilder::Element::PrintAttributes(ostream& out) const {
	if (m_attributes.empty()) {
		return;
	}
	for (Attributes::const_iterator cit = m_attributes.begin(); cit != m_attributes.end(); ++cit) {
		out << " " << cit->first.c_str() << "=\"";
		print_escaped(out, cit->second, true);
		out << "\"";
	}
}

void XmlStringBuilder::Element::PrintValue(ostream& out) const {
	if (m_hasValue) {
		print_escaped(out, m_value, false);
	}
}

void XmlStringBuilder::Element::PrintChildren(ostream& out, const string& indent) const {
	if (!HasChildren()) {
		return;
	}
	const string newIndent(INDENT + indent);
	out << endl;
	for (vector<Element*>::const_iterator cit = m_children.begin(); cit != m_children.end(); ++cit) {
		(*cit)->PrintXml(out, newIndent);
	}
	out << indent;
}

XmlStringBuilder::~XmlStringBuilder() {
	if (m_root != 0) {
		delete m_root;
		m_root = 0;
	}
}

void XmlStringBuilder::AddElement(const string& name) {
	if (name.empty()) {
		throw CrException("The element name may not be empty.");
	}
	if (m_elements.empty() && (m_root != 0)) {
		throw CrException("Illegal XML: only one root is allowed.");
	}
	Element* e = new Element(name);
	if (m_root == 0) {
		m_root = e;
	}
	if (!m_elements.empty()) {
		m_elements.top()->AddChild(e);
	}
	m_elements.push(e);
}

void XmlStringBuilder::CloseElement() {
	if (m_elements.empty()) {
		throw CrException("There is no element to close.");
	}
	m_elements.pop();
}

void XmlStringBuilder::CloseAllElements() {
	if (m_elements.empty()) {
		throw CrException("There are no elements to close.");
	}
	while (!m_elements.empty()) {
		m_elements.pop();
	}
}

void XmlStringBuilder::AddAttribute(const string& name, const string& value) {
	if (name.empty()) {
		throw CrException("The attribute name may not be empty.");
	}
	if (m_elements.empty()) {
		throw CrException("There is no open element to add the attribute to.");
	}
	m_elements.top()->AddAttribute(name, value);
}

void XmlStringBuilder::SetElementValue(const string& value) {
	if (m_elements.empty()) {
		throw CrException("There is no open element to set the value for.");
	}
	m_elements.top()->SetValue(value);
}

void XmlStringBuilder::PrintXml(ostream& out) const {
	if (!m_elements.empty()) {
		throw CrException("There are still open elements.");
	}
	if ( !m_processingInstruction.empty() ) out << m_processingInstruction.c_str() << endl;
	if (m_root != 0) {
		m_root->PrintXml(out, "");
	}
}

void XmlStringBuilder::GetXml(string& xml) const {
	stringstream out;
	PrintXml(out);
	xml = out.str();
}
