/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#include <sstream>

#include <konga/Utils/StringUtils.h>	              

#include "XMLOstreamWriter.h"

using namespace konga::xml;
using konga::xml_text;
using std::string;
using std::ostream;
using std::endl;

XmlAttributes::XmlAttributes(void) {}
XmlAttributes::~XmlAttributes(void) {}

namespace {
	typedef std::pair<std::string,std::string> strpairT;
}

string& XmlAttributes::operator[](const string& name) {
	for( AttrContainerT::iterator it = m_attrs.begin(); it != m_attrs.end(); ++it ) {
		if ( it->first == name )
			return it->second;
	}
	                          
	m_attrs.push_back(strpairT(name, ""));
	return (m_attrs.end()-1)->second;
}

void XmlAttributes::clear() {
	m_attrs.clear();
}

XmlAttributes::const_iterator XmlAttributes::begin() const {
	return m_attrs.begin();
}

XmlAttributes::const_iterator XmlAttributes::end() const {
	return m_attrs.end();
}

bool XmlAttributes::empty() const {
	return m_attrs.empty();
}

namespace {

	const string sLevelSep("\t");
	void indent(ostream& os, int level) {
		if ( level < 1 )
			return;
		for( int i = 0; i < level; ++i ) {
			os << sLevelSep;
		}
	}

	void WriteAttributes(ostream& os, const AttrT& attr) {
		if ( attr.empty() )
			return;

		for( AttrT::const_iterator cit = attr.begin();
			cit != attr.end(); ++cit) {
				os << " " << cit->first << "=" << "\"" << xml_text(cit->second.c_str(), true) << "\"";
		}
	}

}

void konga::xml::WriteXmlPI(std::ostream& os, const std::string& encoding) {
	os << "<?xml version=\"1.0\"";
	if ( !encoding.empty() ) {
		os << " encoding=\"" << encoding << "\"";
	}
	os << "?>" << endl;
}

void konga::xml::WriteStartElement(std::ostream& os, int level, const std::string& name, const AttrT& attributes) {
	indent(os, level);
	os << "<" << name;
	WriteAttributes(os, attributes);
	os << ">" << endl;
}

void konga::xml::WriteStartElement(std::ostream& os, int level, const std::string& name) {
	AttrT attr;
	WriteStartElement(os, level, name, attr);
}


AutoWriteEndElement::AutoWriteEndElement(std::ostream& os, int level, const std::string& name) :
m_os(os), m_level(level), m_sName(name) {}

AutoWriteEndElement::~AutoWriteEndElement(void) {
	indent(m_os, m_level);
	m_os << "</" << m_sName << ">" << endl;
}

void konga::xml::WriteEmptyElement(std::ostream& os, int level, const std::string& name, const AttrT& attributes) {
	indent(os, level);
	os << "<" << name;
	WriteAttributes(os, attributes);
	os << " />" << endl;
}

void konga::xml::WriteValueElement(std::ostream& os, int level, const std::string& name, const std::string& value, const AttrT& attributes) {
	indent(os, level);
	os << "<" << name;
	WriteAttributes(os, attributes);
	os << ">" << konga::xml_text(value.c_str(), false) << "</" << name << ">" << endl;
}

