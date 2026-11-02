/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <konga/Utils/CrException.h>
#include <konga/CharacterEncodingConversion/EncodingStringUtils.h>

#include "crom.h"
#include "TextParser.h"

TextParser::TextParser(const std::string& fileName, CROM*dtd) : konga::JbParser(fileName)
{
	CROM* pRootTable=dtd->GetFirstChild();
	delimiter=pRootTable->get_delimiter();
	qualifier=pRootTable->get_strQual();
	isFlat=pRootTable->IsFlat();
	isCsv=delimiter==','&&qualifier=='"';
	field_pos=pRootTable->get_fieldPos();
	field_len=pRootTable->get_fieldLength();
	isFixedField=delimiter==0;
}

TextParser::TextParser(const std::string& fileName, char delimiter, char qualifier, bool isCsv, bool isFlat, int field_pos, int field_len) : konga::JbParser(fileName)
{
	this->delimiter=delimiter;
	this->qualifier=qualifier;
	this->isFlat=isFlat;
	this->isCsv=isCsv;
	this->field_pos=field_pos;
	this->field_len=field_len;
	isFixedField=delimiter==0;
}

TextParser::~TextParser(){
}

namespace {
	const unsigned char ef = 0xEF;
	const unsigned char bb = 0xBB;
	const unsigned char bf = 0xBF;
}

konga::JbNode* TextParser::parse_all(){
	root=new konga::JbNode(this, 0, "", 0, 0);
	if(in==0)return root;

	size_t i1=0;
	if(in->size()>=3 && (unsigned char)in->at(0)==ef && (unsigned char)in->at(1)==bb && (unsigned char)in->at(2)==bf)i1=3;
	for(size_t n=in->size(), i2=0; i1<n; i1=i2){
		i2=parseRecord(i1);
		int k=i2;
		i2=skipToBeginNextRecord(i2);
		std::string id(getId());
		konga::JbNode*node=new konga::JbNode(this, root, id, i1, k);
	}
	return root;
}

std::string TextParser::getId(){
	if(isFlat)return ""; 
	if(isFixedField){
		std::string record=in->getStr(record_begin_pos,record_end_pos-record_begin_pos);
		std::string id=konga::EncodingStringUtils::Utf8SubString(record,field_pos-1,field_len);
		id=konga::trim (id);
		return id; 
	}
	else {
		std::string id=in->getStr(record_begin_pos, (int)(id_field_end_pos-record_begin_pos));
		int len=id.length();
		if(len>2 && id.at(0)==qualifier && id.at(len-1)==qualifier){
			return id.substr(1, len-2);
		}
		return id;
	}
}

size_t TextParser::parseRecord(size_t pos){
	char c;
	if(isCsv){
		record_begin_pos=pos;
		id_field_end_pos=0;
		while((c=in->at(pos))!='\r' && c!='\n' && c!=0){
			pos=in->findCsvEndOfField(pos, delimiter);
			if(isFlat==false && id_field_end_pos==0)id_field_end_pos=pos;
			if((c=in->at(pos))==delimiter)++pos;
		}
		record_end_pos=pos;
		return pos;
	}
	else {
		record_begin_pos=pos;
		id_field_end_pos=0;
		while((c=in->at(pos))!='\n' && c!='\r' && c!=0){
			if(isFlat==false && c==delimiter && id_field_end_pos==0)id_field_end_pos=pos;
			++pos;
		}
	}
	record_end_pos=pos;
	return pos;
}
size_t TextParser::skipToBeginNextRecord(size_t pos){
	char c;
	while((c=in->at(pos))=='\r' || c=='\n')++pos;
	return pos;
}


