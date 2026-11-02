/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#ifndef __JBXMLREADER_H_
#define __JBXMLREADER_H_

#include <string>
#include <vector>
#include <map>
#include <stack>
#include <boost/scoped_ptr.hpp>
#include <konga/CharacterEncodingConversion/Utf8TextInput.h>

#include <konga/JbParser/JbNode.h>
#include <konga/JbParser/JbParser.h>

namespace konga {

	class XmlReader : public konga::JbParser {
	boost::scoped_ptr<etlforge::Utf8TextInput> m_utf8Input;
	friend class JbNode;
	friend class XmlEditor;
	friend class TransacParser;

	public:
		enum EVENT_ID {
			START_DOCUMENT,
			XML_HEADER,
			XML_DOCTYPE,
			START_ELEMENT,
			END_ELEMENT,
			CHARACTERS,
			IGNORABLE_CHARACTERS,
			END_DOCUMENT
		};
		enum Status {
			t_unknown,
			t_xml_header,
			t_doctype,
			t_start_document,
			t_end_document,
			t_start,
			t_start_end,
			t_end,
			t_value,
			t_error
		};

		enum COMMAND {c_unknown,
					c_any,
					c_child,
					c_continue,
					c_attribute,
					c_filter};

public:
	XmlReader(const std::string& xml_file_name,const std::string& source_encoding="");
	virtual ~XmlReader();

	EVENT_ID next();
	bool hasNext();

	virtual JbNode* parse_all();
	JbNode* parse(const std::string& s);

	std::string getLocalName();
	std::string getNamespaceURI();
	EVENT_ID getEventType();
	int getAttributeCount();
	konga::JbNode* getXmlHeader(){return xml_header;}
	konga::JbNode* getDocType(){return doc_type;}

	void changeElementNames(
							const std::string& newXmlFileName,
							const std::map<std::string, std::string>&oldToNewName_map,
							const std::map<std::string, std::string>& nameValueMap);

	bool buildXmlString(StringBuilder& sb, JbNode*node, const std::string& header);
	bool writeXml(FILE*out, JbNode*node, const std::string& header);
	std::string toString();
protected:
	JbNode* createValueNode(JbNode* parent, const std::string& value);
	JbNode* createNode(JbNode*parent, JbNode*sibling, const std::string& nodeString);
	JbNode* replaceNode(JbNode*node, const std::string& nodeString);
	konga::Tokenizer* getTokenizer(){return tk;}
	virtual size_t findEndOfElementPosition(size_t start);

private:
	Status parseBetween(const std::string& str, std::string& name);

protected:

	JbNode*xml_header;
	JbNode*doc_type;
	Status last_status;
	JbNode*end_element_node, *value_node;
	konga::Tokenizer*tk;
};
}

#endif                    

