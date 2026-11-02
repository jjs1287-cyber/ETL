/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#ifndef __TEXTDOCREADER_H_
#define __TEXTDOCREADER_H_

#include <string>
#include <vector>
#include <map>
#include <stack>
#include <boost/scoped_ptr.hpp>
#include <konga/CharacterEncodingConversion/Utf8TextInput.h>

#include "TextParser.h"

namespace konga {
	class Utf8Conv;
}

class CROM;

class TextDocReader : public TextParser {
public:
	TextDocReader(const std::string& file_name, CROM*dtdRoot, bool bIsFlat, bool isTarget=false);
	~TextDocReader();
	konga::JbNode* parseDocumentWithStructure();
	void parseJbNode(konga::JbNode* record, CROM*dtd, std::vector<std::string>&values, std::vector<bool>&bIsNulls);
	void setUtf8Conv(konga::Utf8Conv*conv){m_conv=conv;}

private:
	void decent(std::string& value);


private:
	CROM* dtdRoot;
	boost::scoped_ptr<etlforge::Utf8TextInput> m_utf8Input;
	konga::Utf8Conv* m_conv;
	bool trim_source_data;
	bool bCsvNullable;
	bool bIsDB, bIsFlat;
};


#endif                      

