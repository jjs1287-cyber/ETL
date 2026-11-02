/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <konga/Utils/FileUtils.h>
#include <konga/Utils/StringTokenizer.h>
#include <konga/Utils/CrException.h>
#include <konga/Utils/ErrorResolver.h>
#include <konga/Utils/AutoFileCleanup.h>

#include <konga/DbRelations/TransformationTypeIdConstants.h>

#include "TextParser.h"
#include "XmlReader.h"
#include "CombineFiles.h"
#include "TreeMapper.h"


CombineFiles::CombineFiles(TreeMapper*pTreeMapper) :
	m_pTreeMapper(pTreeMapper),
	typeId(pTreeMapper->GetTargetType()),
	pDtdRoot(pTreeMapper->GetTargetDTDParser()->m_root),
	pDtdTransac(pTreeMapper->getTargetTransacCrom()),
	last(0)
{
	if(pDtdTransac==0&&pDtdRoot->IsFlat()){
		pDtdTransac=pDtdRoot->GetFirstChild();
	}

	std::string transac_path=(const char*)pDtdTransac->get_Path();
	konga::StringTokenizer tk(transac_path.c_str(),".$/");

	                
	size_t i=0, i0=0;
	while(tk.NextToken()){
		std::string token(tk.token());
		switch(token.at(0)){
			case '.':
			case '$':
			case '/':
				i++;
				break;
			default:
				i+=token.length();
				if(token=="_Root")i0=i+1;
				                                           
				bool isoutofbound=(transac_path.size()<=(i+1));
				path_map[token]=isoutofbound? "" : transac_path.substr(i+1);
		}
	}
	path_map[""]=transac_path.substr(i0);                                     
}

CombineFiles::~CombineFiles()
{
}

std::string CombineFiles::getFooter(konga::JbNode*parent, konga::JbNode*transac){
	konga::JbNode* lastChild=parent->getChildren().back();
	std::string name=lastChild->getName();
	if(pDtdTransac->IsElement()){
		if(name==transac->getName())return "";
	} else {
		CROM*group_transac_child=pDtdTransac->FindChildFolder(name.c_str());
		if(group_transac_child)return "";
	}
	return lastChild->getContents();
}

namespace {
bool compare_ignore_wspace(const std::string& S1, const std::string& S2){
    const char* s1=S1.c_str(), *s2=S2.c_str();
    int c1,c2;
    while(true){
        while((c1=*s1++) && iswspace(c1));
        while((c2=*s2++) && iswspace(c2));
        if(c1!=c2 || c1==0 || c2==0)break;
    }
    return c1==c2;
}
}

void CombineFiles::updateParents(FILE*out, std::map<std::string, std::vector<std::string> >& map, konga::JbNode*transac, bool isXml){
	std::vector<konga::JbNode*> parents;
	for(konga::JbNode*parent=transac->getParent(); parent; parent=parent->getParent()){
		parents.push_back(parent);
	}
	if(parents.size()==0)return;
	int n=parents.size(), i0=n-1;
	if(map.size()>0){                                                       
		i0=-1;
		                        
		for(int i=n-1; i>=0; --i){
			konga::JbNode*parent=parents.at(i);
			konga::JbNode*child= (i==0)? transac: parents.at(i-1);
			std::string name=parent->getName();
			std::map<std::string, std::vector<std::string> >::iterator it=map.find(name);
			const std::vector<std::string>& v=it->second;
			std::string header=parent->getContents(), footer=getFooter(parent, child);
			if(!compare_ignore_wspace(header,v[0]) || !compare_ignore_wspace(footer,v[1])){
				i0=i;
				break;
			}
		}
		                                 
		for(int i=0; i<=i0; ++i){
			konga::JbNode*parent=parents.at(i);
			std::string name=parent->getName();
			std::map<std::string, std::vector<std::string> >::iterator it=map.find(name);
			assert(it!=map.end());
			const std::vector<std::string>& v=it->second;
			std::string footer=v[1];
			if(footer.empty()){
				if(isXml)TransacParser::writeEndTagFromHeader(out, v[0]);
			}
			else {
				fputs(footer.c_str(),out);
			}
		}
		             
		for(int i=i0; i>=0; --i){
			konga::JbNode*parent=parents.at(i);
			konga::JbNode*child= (i==0)? transac: parents.at(i-1);
			std::string name=parent->getName();
			std::string header=parent->getContents(), footer=getFooter(parent, child);
			std::vector<std::string> v;
			v.push_back(header);
			v.push_back(footer);
			map[name]=v;
			if(header.empty()==false)fprintf(out,"%s",header.c_str());
		}
	}
	else {
		                 
		for(int i=n-1; i>=0; --i){
			konga::JbNode*parent=parents.at(i);
			konga::JbNode*child= (i==0)? transac: parents.at(i-1);
			std::string name=parent->getName();
			parent_name_stack.push(name);
			std::string header=parent->getContents(), footer=getFooter(parent, child);
			std::vector<std::string> v;
			v.push_back(header);
			v.push_back(footer);
			map[name]=v;
			if(header.empty()==false)fprintf(out,"%s",header.c_str());
		}
	}
}

void CombineFiles::writeTrailer(FILE*out, std::map<std::string, std::vector<std::string> >& map, bool isXml){
	while(parent_name_stack.size()>0){
		const std::string& name(parent_name_stack.top());
		const std::vector<std::string>& v=map[name];
		const std::string& footer=v[1];
		if(footer.empty()){
			if(isXml)TransacParser::writeEndTagFromHeader(out, v[0]);
		}
		else {
			fputs(footer.c_str(),out);
		}
		parent_name_stack.pop();
	}
}

void CombineFiles::processFiles(const std::string& combinedFileName, int nChunk)
{

	bool isXml=pDtdRoot->IsXml();
	std::map<std::string,std::vector<std::string> > parentMap;

	FILE* out=fopen(combinedFileName.c_str(), "wb+");
	if(out==0){
		throw konga::CrException("can't open output file for combine chunking files. file_name:"+combinedFileName);
	}
	
	konga::AutoFileHandle adopt(out);

	if(isXml)fputs("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n",out);
	
	konga::JbNode *lastParent=0;
	for(int iChunk=0; iChunk<nChunk; ++iChunk){
		m_pTreeMapper->m_iChunk=iChunk;
		std::string stFileName_=m_pTreeMapper->getTargetDataFileName();
		TransacParser parser(stFileName_, typeId, pDtdRoot, pDtdTransac, transacs);
		std::vector<konga::JbNode*>* nodeList=&transacs;
		lastParent=0;
		for(std::vector<konga::JbNode*>::iterator it=nodeList->begin(); it!=nodeList->end(); ++it){
			konga::JbNode*node=*it;
			konga::JbNode*parent=node->getParent();
			if(parent!=lastParent){
				updateParents(out, parentMap, node, isXml);
			}
			fprintf(out,"%s", node->getContents().c_str());
			lastParent=parent;
		}
	}
	writeTrailer(out, parentMap, isXml);
}

TransacParser* CombineFiles::open(const std::string& fileName){
	return new TransacParser(fileName, typeId, pDtdRoot, pDtdTransac, transacs);
}

