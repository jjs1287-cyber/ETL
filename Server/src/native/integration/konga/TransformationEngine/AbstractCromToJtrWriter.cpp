/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */

#include <sstream>
#include <konga/Utils/StringUtils.h>
#include <konga/Xml/XmlStringBuilder.h>
#include "AbstractCromToJtrWriter.h"

using namespace std;
using namespace konga::xml;

namespace {

	string ToString(int i, std::ios_base & (*f)(std::ios_base&)) {
		std::ostringstream oss;
		oss << f << i;
		return oss.str();
	}

	string ToHexString(int i) {
		return "0x" + ToString(i, std::hex);
	}

}

AbstractCromToJtrWriter::~AbstractCromToJtrWriter() {}

void AbstractCromToJtrWriter::AddAttribute(XmlStringBuilder &xml, const string &name, const char*value, bool index) {
	if (value!=0&&value[0]!=0) {
		const string s(value);
		AddAttribute(xml, name, s, index);
	}
}

void AbstractCromToJtrWriter::AddAttribute(XmlStringBuilder &xml, const string &name, const KString &value, bool index) {
	if (!value.IsEmpty()) {
		const string s(value);
		AddAttribute(xml, name, s, index);
	}
}

namespace {
	                                                                                                              
	                                                                                             
	string fixSpecialCharacters(const string& value) {
		if(value=="\t")return string("\\t");
		if(value=="\r")return string("\\r");
		if(value=="\n")return string("\\n");
		return value;
	}
}

void AbstractCromToJtrWriter::AddAttribute(XmlStringBuilder &xml, const string &name, const string &value, bool index) {
	if (!value.empty()) {
		if (index) {
			AddAttribute(xml, name, GetIndexForString(value), -1);
		}
		else {
			xml.AddAttribute(name, fixSpecialCharacters(value));
		}
	}
}

void AbstractCromToJtrWriter::AddAttribute(XmlStringBuilder& xml, const string& name, int value, int default_value       ) {
	if(value!=default_value)xml.AddAttribute(name, konga::toString(value));
}

void AbstractCromToJtrWriter::AddAttribute(XmlStringBuilder& xml, const string& name, bool value) {
	const string s(value ? "true" : "false");
	xml.AddAttribute(name, s);
}

void AbstractCromToJtrWriter::AddAttribute(XmlStringBuilder& xml, const string& name, bool value, bool defaultValue) {
	if (value != defaultValue) {
		AddAttribute(xml, name, value);
	}
}

void AbstractCromToJtrWriter::AddHexAttribute(XmlStringBuilder& xml, const string& name, int value) {
	xml.AddAttribute(name, ToHexString(value));
}

int AbstractCromToJtrWriter::GetIndexForString(const string& s) {
	int index = -1;
	if (s.empty()) {
		return index;
	}
	map<string, int>::const_iterator cit = m_indexedStrings.find(s);
	if (cit == m_indexedStrings.end()) {
		index = ++m_currentIndex;
		m_indexedStrings[s] = index;
	}
	else {
		index = cit->second;
	}
	return index;
}

void AbstractCromToJtrWriter::WriteStringIndexToXml(XmlStringBuilder& xml) {
	if (m_indexedStrings.empty()) {
		return;
	}
	xml.AddElement("Strings");
	for (map<string, int>::const_iterator cit = m_indexedStrings.begin(); cit != m_indexedStrings.end(); ++cit) {
		xml.AddElement("String");
		AddAttribute(xml, "idx", cit->second, -1);
		AddAttribute(xml, "val", cit->first, false);
		xml.CloseElement();
	}
	xml.CloseElement();
}
