/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#include <konga/SystemVariables/SystemVariables.h>
#include <konga/CharacterEncodingConversion/Utf8Conv.h>
#include <konga/CharacterEncodingConversion/EncodingStringUtils.h>

#include "SystemVariableDefs.h"
#include "crom.h"
#include "TextDocReader.h"
#include "TreeMapper.h"

TextDocReader::TextDocReader(const std::string& file_name, CROM*dtdRoot, bool isFlat, bool isTarget) :
	TextParser(file_name,dtdRoot), dtdRoot(dtdRoot), trim_source_data(true), bIsFlat(isFlat),m_conv(0)
{
	TreeMapper*pTreeMapper=dtdRoot->getTreeMapper();
	                                        
	if (!isTarget) {
		const int sourceType = pTreeMapper->GetSourceType();
		if (sourceType!=DbTypeId && sourceType!=LdapTypeId) {
			try {
				m_utf8Input.reset(new etlforge::Utf8TextInput(file_name, konga::GetSystemVariable(TEXT_SOURCE_ENC, pTreeMapper->getOperationDataElementHolder(), "")));
				if (m_utf8Input->path()!=file_name) {
					delete in; in=0;
					in=new konga::FileContents(m_utf8Input->path());
				}
			} catch (const std::exception& failure) { throw konga::CrException(failure.what()); }
		}
		trim_source_data=pTreeMapper->isTrimSourceWs();
	}
	bCsvNullable = konga::GetSystemVariableAsBool(CSV_NULLABLE, pTreeMapper->getOperationDataElementHolder(), true);
	bIsDB = dtdRoot->isDB() || dtdRoot->IsLdap();
}

TextDocReader::~TextDocReader(){
	delete m_conv;
	close();
}

namespace {
	konga::JbNode* findJbParent(std::map<CROM*,konga::JbNode*>&map, CROM*dtd){
		CROM*parent=dtd->GetParent();
		if(parent==0)return 0;
		std::map<CROM*,konga::JbNode*>::iterator it=map.find(parent);
		assert(it!=map.end());
		return it->second;
	}

}

konga::JbNode* TextDocReader::parseDocumentWithStructure(){
	konga::JbNode* root0=parse_all();
	if(isFlat)return root;
	std::vector<konga::JbNode*>& node_list=root0->getChildren();
	std::vector<konga::JbNode*>::iterator it=node_list.begin();
	if(dtdRoot->IsLdap()){
		root=new konga::JbNode(this, 0, dtdRoot->get_Name(), 0L, 0L);
	}
	else {
		root=new konga::JbNode(this, 0, "", 0L, 0L);
	}
	CROM*dtd=dtdRoot;
	std::map<CROM*, konga::JbNode*>map;
	map[dtdRoot]=root;
	for(; it!=node_list.end(); ++it){
		konga::JbNode*jbNode=*it;
		std::string id=jbNode->getName();
		dtd=dtd->findSegment(id);
		konga::JbNode* jbParent=findJbParent(map,dtd);
		if(jbParent==0)continue;
		jbNode->moveToParent(jbParent);
		map[dtd]=jbNode;
	}
	root0->clearChildren();
	delete root0;
	return root;
}

void TextDocReader::decent(std::string& value){
	if(value.empty())return;
	if(!isCsv){
		if(konga::hasEscapedCharacter(value)){
			value=konga::restoreEscapedCharacters(value);
		}
	}
	if(trim_source_data)value=konga::trim(value);
	konga::convertCRLN2LN(value);

	if ( m_conv ) {
		value=m_conv->from(value);
	}
}


void TextDocReader::parseJbNode(konga::JbNode* record, CROM*dtd, std::vector<std::string>&values, std::vector<bool>&bIsNulls)
{
	size_t begin_pos=record->getPos(), len=record->getLength();
	values.clear();
	bIsNulls.clear();
	if(len==0)return;
	if(isCsv){
		bool bIsNull;
		size_t pos=begin_pos, end_pos=begin_pos+len;
		if(!bIsFlat && bIsDB){
			std::string value = in->readCsvData(pos, delimiter, bIsNull);
			values.push_back(value);
			bIsNulls.push_back(bIsNull);
			++pos;
		}
		for(CROM* pAttribute=dtd->GetFirstChild(); pAttribute && pos<end_pos; pAttribute=pAttribute->GetNextSibling(), ++pos){
			std::string value = in->readCsvData(pos, delimiter, bIsNull);
			if ( bIsNull && !bIsDB) {
				if(	bCsvNullable){
					const char*defaultStr=pAttribute->get_DefaultValue();
					if(defaultStr && defaultStr[0]){
						value=defaultStr;
						bIsNull=false;
					}
				}
				else {
					value="";
					bIsNull=false;
				}
			}
			decent(value);
			values.push_back(value);
			bIsNulls.push_back(bIsNull);
		}
	}
	else if(isFixedField){
		const std::string& str = in->getStr(begin_pos, len);
		if(str.empty())return;
		for(CROM* pAttribute=dtd->GetFirstChild(); pAttribute; pAttribute=pAttribute->GetNextSibling()){
			if(pAttribute->IsLeaf()){
				bool bIsNull=false;
				int pos=pAttribute->GetFieldPosition()-1, len=pAttribute->GetFieldLength();
				std::string value;
				if(pos>=0 && static_cast<size_t>(pos)<konga::EncodingStringUtils::Utf8CharacterSize(str)){
					value = konga::EncodingStringUtils::Utf8SubString(str,pos,len);
					decent(value);
				}
				else {
					bIsNull=true;
				}
				values.push_back(value);
				bIsNulls.push_back(bIsNull);
			}
		}
	}
	else {
		size_t pos=begin_pos, end_pos=begin_pos+len;
		for(CROM* pAttribute=dtd->GetFirstChild(); pAttribute && pos<end_pos; pAttribute=pAttribute->GetNextSibling(), ++pos){
			std::string value = in->readNormalData(pos, qualifier, delimiter);
			decent(value);
			values.push_back(value);
			bIsNulls.push_back(false);
		}
	}
}

