/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#ifndef ETLFORGE_TRANSFORMATION_ENGINE_XML_CROM_TO_JTR_WRITER_H
#define ETLFORGE_TRANSFORMATION_ENGINE_XML_CROM_TO_JTR_WRITER_H

#include <vector>
#include <map>
#include <string>
#include "CromToJtrWriter.h"

class CROM;
class SchemaCollection;

namespace konga {
	namespace xml {
		class XmlStringBuilder;
	}
}

class XmlCromToJtrWriter : CromToJtrWriter {
private:
	typedef std::map<std::string, CROM*>	NodeMap;

public:
	XmlCromToJtrWriter(SchemaCollection* sc = 0) : m_schemaCollection(sc), m_substitutions(0) 
	{}
	~XmlCromToJtrWriter() {}

	void GetXml(CROM* root, bool isRootExploded, std::string& receiver) ;
	void GetXml(CROM* root, bool isRootExploded, const NodeMap& typeMap, const NodeMap& substitutionMap, std::string& receiver) ;
	void WriteXml(CROM* root, bool isRootExploded, const std::string& outputFilePath) ;
	void WriteXml(CROM* root, bool isRootExploded, const NodeMap& typeMap, const NodeMap& substitutionMap, const std::string& outputFilePath) ;

protected:
	virtual void WriteExtensionData(CROM* node, konga::xml::XmlStringBuilder& xml);

private:
	SchemaCollection* const	m_schemaCollection;
	const NodeMap*			m_substitutions;

	void CreateXml(CROM* root, bool isRootExploded, const NodeMap& typeMap, const NodeMap& substitutionMap, konga::xml::XmlStringBuilder& xml);
	void WriteSubstitutionGroupMap(const NodeMap& substitutionMap, konga::xml::XmlStringBuilder& xml);
	void WriteTypeMap(const NodeMap& typeMap, konga::xml::XmlStringBuilder& xml) ;
	void WriteTypeDefinition(const std::string& typeName, CROM* typeDef, konga::xml::XmlStringBuilder& xml) ;
	void FillXmlExtensionAttributes(CROM* node, konga::xml::XmlStringBuilder& xml) ;
	void AddTypeExtensionElements(CROM* node, konga::xml::XmlStringBuilder& xml) ;
	void AddSubstitutionGroupElements(CROM* node, konga::xml::XmlStringBuilder& xml);
	void AddStringIndexList(const std::vector<std::string>& values, konga::xml::XmlStringBuilder& xml, const std::string attributeName);
};

#endif
