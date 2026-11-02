/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <konga/Utils/CrException.h>
#include <konga/Utils/StringTokenizer.h>
#include <konga/DbRelations/TransformationTypeIdConstants.h>

#include "TextParser.h"
#include "XmlReader.h"
#include "TransacParser.h"

using namespace konga;

TransacParser::TransacParser(const std::string&fileName, int typeId, CROM*pDtdRoot, CROM*transac0, std::vector<konga::JbNode*>&transac_list) :
	fileName(fileName), transac(transac0), footers(0), beforeTransac(true), node_combining(0), parser(0), isXml(false)
{
	transac_list.clear();
	switch(typeId){
		case DbTypeId:
		case TextTypeId:
		case ComplexTextTypeId:
		case LdapTypeId:
			isXml=false;
			text_parse_all(pDtdRoot, transac_list);
			break;
		case XmlTypeId:
		case WebServiceInputTypeId:
		case WebServiceOutputTypeId:
			isXml=true;
			xml_parse_all(pDtdRoot, transac_list);
			break;
		default:
			throw CrException("Chunking is only available for database, text, XML and Ldap sources. Disable chunking for the current operation.");
	}
}

TransacParser::~TransacParser(){
	if(footers)delete[]footers;
	if(parser)delete parser;
}

TransacParser::CompareCromResult TransacParser::compare2Croms(CROM*dtd){
	if(dtd==transac)return r_is_transac;
	const KString& dtd_path(dtd->get_Path());
	const KString& transac_path(transac->get_Path());
	if(strncmp((const char*)dtd_path, (const char*)transac_path, transac_path.GetLength())==0)return r_is_transac_child;
	if(strncmp((const char*)dtd_path, (const char*)transac_path, dtd_path.GetLength())==0)return r_is_transac_parent;
	return r_is_diff_branch;
}

void TransacParser::combineNode(size_t pos2, konga::JbNode* node1){
	node1->setEndPos(pos2);
	std::vector<JbNode*>& children(node1->getChildren());
	for(std::vector<JbNode*>::iterator it=children.begin(); it!=children.end(); ++it){
		konga::JbNode* child=*it;
		delete child;
	}
	children.clear();
}

void TransacParser::xml_parse_all(CROM*pDtdRoot, std::vector<JbNode*>&transac_list){
	std::stack<CROM*> dtd_stack;
	CROM*dtd=0;
	konga::XmlReader* parser=new konga::XmlReader(fileName);
	this->parser=parser;
	konga::FileContents*in=parser->in;
	konga::Tokenizer* tk=((konga::XmlReader*)parser)->getTokenizer();
	konga::JbNode*start_element_node_saved=0;
	beforeTransac=true;
	                                                                                    
	while(parser->hasNext()){
		switch(parser->next()){
		case konga::XmlReader::START_ELEMENT:
			{
				size_t pos2=tk->getNextPos();
				if(parser->node->getEndPos()==pos2){
					char c;
					while(iswspace(c=in->at(pos2)))++pos2;
					parser->node->setEndPos(pos2);
				}
				if(parser->getCurrentNode()==parser->root){
					                                                              
					if(pDtdRoot->GetDisplayName()!=parser->node->getName())
					{
						throw konga::CrException(
							"Unexpected element in data file :"+fileName +
							"Expected element "+std::string(pDtdRoot->GetDisplayName()) + 
							"Unexpected element "+std::string(parser->node->getName()) + 
							"\nThe xml file may be invalid!"
							);

					}
					dtd=pDtdRoot;
					node_combining=parser->node;
					start_element_node_saved=0;
				}
				else {
					if(start_element_node_saved==0){
						CROM*dtd_saved=dtd;
						dtd=dtd->FindChildFolder(parser->node->getName().c_str());
						switch(compare2Croms(dtd)){
							case r_is_transac_parent:
								beforeTransac=true;
								node_combining=parser->node;
								break;
							case r_is_transac_child:
								if(compare2Croms(dtd_saved)!=r_is_transac_parent)break;
							case r_is_transac:
								beforeTransac=false;
								node_combining=parser->node;
								start_element_node_saved=parser->node;
								break;
							case r_is_diff_branch:
								if(beforeTransac==false&& node_combining==0)node_combining=parser->node;
								start_element_node_saved=parser->node;
								break;
						}
					}
				}
				dtd_stack.push(dtd);
			}
			break;
		case konga::XmlReader::END_ELEMENT:
			{
				if(parser->end_element_node==start_element_node_saved){
					size_t pos2=tk->getNextPos();
					char c;
					while(iswspace(c=in->at(pos2)))++pos2;
					CompareCromResult r1=compare2Croms(dtd);
					if(r1== r_is_transac_child || r1==r_is_transac){
						transac_list.push_back(node_combining);
						combineNode(pos2, node_combining);
						node_combining=0;
					}
					if(node_combining!=0){
						if(beforeTransac==false){
							if(node_combining->getParent()!=parser->end_element_node->getParent())
								node_combining=parser->end_element_node;
							else {
								combineNode(pos2, node_combining);
								if(node_combining!=parser->end_element_node){
									parser->node->m_children.pop_back();
									delete parser->end_element_node;
								}
							}
						}
					}
					start_element_node_saved=0;
				}
				dtd_stack.pop();
				if(dtd_stack.size()>0)
					dtd=dtd_stack.top();
				else
					dtd=0;
			}
			break;
		case konga::XmlReader::CHARACTERS:
			if(start_element_node_saved==0 && node_combining!=0){
				size_t pos2=tk->getNextPos();
				combineNode(pos2, node_combining);
			}
			break;
		}
	}
	jbRoot=parser->root;
}

void TransacParser::text_parse_all(CROM*pDtdRoot, std::vector<JbNode*>& transac_list){
	TextParser* parser=new TextParser(fileName, pDtdRoot);
	this->parser=parser;
	konga::FileContents*in=parser->in;
	jbRoot=new JbNode(parser, 0, "", 0, 0);
	size_t n=in->size();
	if(n==0)return;
	CROM*dtd=pDtdRoot;

	const bool isFlat=((TextParser*)parser)->isFlat;

	size_t line_end;
	for(size_t line_begin=0; line_begin<n; line_begin=line_end){
		line_end=parser->parseRecord(line_begin);
		line_end=parser->skipToBeginNextRecord(line_end);
		std::string id(parser->getId());
		if(isFlat){
			transac_list.push_back(new konga::JbNode(parser, jbRoot, "", line_begin, line_end));
		}
		else {
			id=trim(id);
			if(!id.empty()){
				dtd=dtd->findSegment(id);
				process_line(dtd, id, line_begin, line_end, transac_list);
			}
		}
	}
}

void TransacParser::process_line(CROM*dtd, std::string&id, size_t i1, size_t i2, std::vector<JbNode*>& transac_list){
	if(footers==0){
		std::stack<CROM*>s;
		for(CROM* crom=transac; crom; crom=crom->GetParent())s.push(crom);
		int n=s.size();
		for(int i=0; i<n; ++i){
			CROM* crom=s.top();
			s.pop();
			dtd_index_map[crom]=i;
		}
		footers=new konga::JbNode*[n];
		beforeTransac=true;
	}
	CompareCromResult compare_transac=compare2Croms(dtd);
	switch(compare_transac){
		case r_is_transac_parent:
		case r_is_transac:
			{
				konga::JbNode* parent_node=findJbParent(dtd);
				node_combining=new JbNode(parser, parent_node, id, i1, i2);
				saveJbNodeToMap(dtd,node_combining);
				if(compare_transac==r_is_transac){
					beforeTransac=false;
					transac_list.push_back(node_combining);
				}
				else {
					beforeTransac=true;
					resetFooters(dtd);
				}
			}
			break;
		case r_is_transac_child:
			assert(node_combining!=0);
			node_combining->setEndPos(i2);
			break;
		case r_is_diff_branch:
			{
				konga::JbNode* parent_node=findJbParent(dtd);
				if(parent_node==0){
					assert(node_combining!=0);
					node_combining->setEndPos(i2);
				}
				else {
					if(beforeTransac){
						node_combining->setEndPos(i2);
					}
					else {
						konga::JbNode*footer=getFooter(dtd);
						if(footer==0){
							footer=new JbNode(parser, parent_node, id, i1, i2);
							saveFooter(dtd,footer);
							node_combining=footer;
						}
						else {
							node_combining->setEndPos(i2);
						}
					}
				}
			}
			break;
	}
}

konga::JbNode* TransacParser::findJbParent(CROM*dtd){
	CROM* parent=dtd->GetParent();
	if(parent==0)return 0;
	std::map<CROM*,konga::JbNode*>::iterator it=dtd_JbNode_map.find(parent);
	if(it==dtd_JbNode_map.end())return 0;
	else return it->second;
}

void TransacParser::saveJbNodeToMap(CROM*dtd, konga::JbNode*node){
	dtd_JbNode_map[dtd]=node;
}

void TransacParser::resetFooters(CROM*dtd){
	std::map<CROM*,int>::iterator it=dtd_index_map.find(dtd);
	if(it!=dtd_index_map.end()){	
		for(int i=it->second, n=dtd_index_map.size(); i<n; ++i){
			footers[i]=0;
		}
	}
}

konga::JbNode* TransacParser::getFooter(CROM*dtd){
	CROM* parent=dtd->GetParent();
	assert(parent!=0);
	std::map<CROM*,int>::iterator it=dtd_index_map.find(parent);
	assert(it!=dtd_index_map.end());
	int i=it->second;
	return footers[i];
}

void TransacParser::saveFooter(CROM*dtd, konga::JbNode*node){
	CROM* parent=dtd->GetParent();
	assert(parent!=0);
	std::map<CROM*,int>::iterator it=dtd_index_map.find(parent);
	assert(it!=dtd_index_map.end());
	footers[it->second]=node;
}


void TransacParser::write_transac_node(FILE*out, JbNode*transac_node, std::stack<JbNode*>&current_nodes, bool isXml){
                                         
	std::stack<JbNode*>new_nodes;
	for(JbNode* current=transac_node, *current_parent; current!=0; current=current_parent){
		current_parent=current->getParent();
		if(current_nodes.size()>0){
			JbNode*old_node=current_nodes.top();
			if(current==old_node)break;
			current_nodes.pop();
			JbNode*old_parent=old_node->getParent();
			if(old_parent!=0 && old_parent!=current_parent){                           
				JbNode* lastChild=old_parent->getChildren().back();
				if(lastChild->getNameIndex()!=old_node->getNameIndex()){
					std::string body=lastChild->getContents();
					fprintf(out, "%s", body.c_str());
				}
				else {
					if(isXml)writeEndTagFromHeader(out, old_parent->getContents());
				}
			}
			                    
		}
		new_nodes.push(current);
		current=current_parent;
	}
	JbNode*current_node=0;
	while(new_nodes.size()>0){
		current_node=new_nodes.top();
		new_nodes.pop();
		current_nodes.push(current_node);
		std::string str=current_node->getContents();

		                         
		fprintf(out, "%s", str.c_str());
		std::vector<JbNode*>&children(current_node->getChildren());
		if(children.size()>0){
			int check_name_index=-1;
			if(new_nodes.size()>0){
				JbNode*check_to_stop=new_nodes.top();
				check_name_index=check_to_stop->getNameIndex();
			}
			for(std::vector<JbNode*>::const_iterator it=children.begin(); it!=children.end(); ++it){
				JbNode*child=*it;
				int child_name_index=child->getNameIndex();
				if(check_name_index==child_name_index)break;
				std::string str_child=child->getContents();
				fprintf(out, "%s", str_child.c_str());
			}
		}
		                    
	}
}

void TransacParser::write_transac_trailer(FILE*out, JbNode*transac_node, std::stack<JbNode*>&current_nodes, bool isXml){
	while(current_nodes.size()>0){
		JbNode*node=current_nodes.top();
		current_nodes.pop();
		JbNode*parent=node->getParent();
		if(parent!=0){
			JbNode* lastChild=parent->getChildren().back();
			if(lastChild->getNameIndex()!=node->getNameIndex()){
				std::string body=lastChild->getContents();
				fprintf(out, "%s", body.c_str());
			}
			else {
				if(isXml)writeEndTagFromHeader(out, parent->getContents());
			}
		}
	}
}

void TransacParser::writeEndTagFromHeader(FILE*out, const std::string& header){
	if(header.empty()==false && header.at(0)=='<'){
		int c,i1=1,i2;
		for(i2=i1;  (iswspace(c=header.at(i2))||c=='/'||c=='>')==false; ++i2);
		fprintf(out, "</%s>\n", header.substr(i1, i2-i1).c_str());
	}
}

konga::JbNode* TransacParser::getXmlHeader(){
	if(isXml){
		return ((XmlReader*)parser)->getXmlHeader();
	}
	else {
		return 0;
	}
}

konga::JbNode* TransacParser::getDocType(){
	if(isXml){
		return ((XmlReader*)parser)->getDocType();
	}
	else {
		return 0;
	}
}

