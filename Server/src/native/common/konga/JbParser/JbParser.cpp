/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <string>
#include <stack>
#include <assert.h>

#include <konga/Utils/CrException.h>

#include "JbParser.h"

using namespace konga;
using std::string;

namespace {
	const unsigned char ef = 0xEF;
	const unsigned char bb = 0xBB;
	const unsigned char bf = 0xBF;
	const char* xml_tokens = "<> =\"";
}

JbParser::JbParser(const string& fileName0) :
	fileName(fileName0),
	in(0), node(0), root(0)
{
	if(!fileName.empty()){
		in=new FileContents(fileName0);
	}

	m_stringMap[""]=0;
	m_stringTable.push_back(&m_stringMap.begin()->first);
}

JbParser::~JbParser(){
	close();
	delete root;
}

unsigned short JbParser::getStringIndex(const string&str){
	std::map<string, unsigned short>::iterator it=m_stringMap.find(str);
	if(it==m_stringMap.end()){
		const unsigned short index=(unsigned short)m_stringMap.size();
		m_stringMap[str]=index;
		it=m_stringMap.find(str);
		m_stringTable.push_back(&it->first);
		return index;
	}
	return it->second;
}

const string& JbParser::getString(unsigned short index) const {
	if(index<m_stringTable.size())return *m_stringTable[index];
	throw konga::CrException("JBXmlParser error: Unknown string index encountered! index="+konga::toString(index));
}

string JbParser::getAttributeString(JbNode*node){
	return getStr(node->m_pos, node->m_length);
}


void JbParser::close(){
	if(in!=0) {
		in->close();
		delete in;
		in = 0;
	}
}

size_t JbParser::findEndOfElementPosition(size_t start){
	return start;
}
