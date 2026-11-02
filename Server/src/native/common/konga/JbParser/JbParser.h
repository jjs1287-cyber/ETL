/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#ifndef __JbParser_H_
#define __JbParser_H_

#include <string>
#include <vector>
#include <map>
#include <stack>

#include <konga/Utils/FileContents.h>
#include <konga/Utils/FileTokenizer.h>

#include "JbNode.h"

namespace konga {

class JbParser {
	friend class JbNode;
	friend class TransacParser;
public:
	JbParser(const std::string& xml_file_name);
	virtual ~JbParser();

	std::string getStr(size_t pos, size_t len){return in->getStr(pos, len);}
protected:
	std::string getAttributeString(JbNode*node);
	virtual size_t findEndOfElementPosition(size_t start);

protected:
	const std::string fileName;
	konga::FileContents*in;

	std::string err_msg;

	JbNode*node;
	JbNode*root;

	std::map<std::string, unsigned short> m_stringMap;
	std::vector<const std::string*> m_stringTable;

public:
	unsigned short getStringIndex(const std::string&str);
	const std::string& getString(unsigned short) const;
	konga::FileContents* getFileContents() {return in;}
	konga::JbNode* getRoot() const {return root;}
	const JbNode* getCurrentNode(){return node;}
	void close();

};
}

#endif                 

