/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */

#ifndef ETLFORGE_TRANSFORMATION_ENGINE_ABSTRACT_CROM_TO_JTR_WRITER_H
#define ETLFORGE_TRANSFORMATION_ENGINE_ABSTRACT_CROM_TO_JTR_WRITER_H

#include <string>
#include <map>
#include <konga/Utils/KString.h>

namespace konga {
	namespace xml {
		class XmlStringBuilder;
	}
}

class AbstractCromToJtrWriter {
public:
	AbstractCromToJtrWriter() : m_currentIndex(0) {}
	virtual ~AbstractCromToJtrWriter() = 0;

public:
	void AddAttribute(konga::xml::XmlStringBuilder& xml, const std::string& name, const std::string& value, bool index);
	void AddAttribute(konga::xml::XmlStringBuilder& xml, const std::string& name, const char* value, bool index);
	void AddAttribute(konga::xml::XmlStringBuilder& xml, const std::string& name, const KString& value, bool index);
	void AddAttribute(konga::xml::XmlStringBuilder& xml, const std::string& name, int value, int default_value=0);
	void AddAttribute(konga::xml::XmlStringBuilder& xml, const std::string& name, bool value);
	void AddAttribute(konga::xml::XmlStringBuilder& xml, const std::string& name, bool value, bool defaultValue);
	void AddHexAttribute(konga::xml::XmlStringBuilder& xml, const std::string& name, int value);
	int	 GetIndexForString(const std::string& s);
	void WriteStringIndexToXml(konga::xml::XmlStringBuilder& xml);

private:
	std::map<std::string, int>	m_indexedStrings;
	int							m_currentIndex;
};

#endif
