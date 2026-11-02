/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#ifndef __TextParser_H_
#define __TextParser_H_

#include <string>
#include <konga/JbParser/JbParser.h>


class CROM;

class TextParser : public konga::JbParser {
	friend class TransacParser;

public:
	TextParser(const std::string& fileName, CROM*dtdRoot);
	TextParser(const std::string& fileName, char delimiter, char qualifier, bool isCsv, bool isFlat, int field_pos, int field_len);
	konga::JbNode* parse_all();
	virtual ~TextParser();
protected:
	size_t parseRecord(size_t pos);
	std::string TextParser::getId();
	size_t skipToBeginNextRecord(size_t pos);

protected:
	char delimiter, qualifier;
	bool isCsv, isFlat, isFixedField;
	int field_pos, field_len;
	size_t record_begin_pos, record_end_pos, id_field_end_pos;
};

#endif                   
