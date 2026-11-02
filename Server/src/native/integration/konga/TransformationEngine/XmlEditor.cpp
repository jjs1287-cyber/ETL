/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#include <konga/Utils/PortDefs.h>		                                    
#include <konga/Utils/AutoFileCleanup.h>
#include <konga/Utils/CrException.h>
#include <konga/Utils/StringTokenizer.h>
#include <konga/Utils/ErrorResolver.h>

#include "XmlEditor.h"
#include "XPathSearch.h"

using namespace konga;

XmlEditor::XmlEditor() 
{
	reader=0;
	node_list=0;
}

XmlEditor::~XmlEditor(){
	cleanup();
}

void XmlEditor::cleanup(){
	for(std::vector<XmlEditorCommand*>::iterator it=command_list.begin(); it!=command_list.end(); ++it){
		delete *it;
	}
	command_list.clear();

	while(reader_stack.size()>0){
		delete reader_stack.top();
		reader_stack.pop();
	}
	if(reader!=0)delete reader;
	reader=0;

	while(node_list_stack.size()>0){
		delete node_list_stack.top();
		node_list_stack.pop();
	}
	if(node_list!=0)delete node_list;
	node_list=0;
}

std::string XmlEditor::eval(const char*expr){
	StringTokenizer tk(expr,"+");
	std::string tmp;
	while(tk.NextToken()){
		const char* token=tk.token();
		switch(*token){
			case '+':
				break;
			default:
				if(*token=='$'){
					std::map<std::string, std::string>::const_iterator it=dataelement_map.find(token);
					if(it!=dataelement_map.end()){
						tmp+=it->second;
						break;
					}
				}
				tmp+=token;
				break;
		}
	}
	return tmp;
}

namespace {
	void addNodeList(std::vector<JbNode*>& list1, std::vector<JbNode*>&list2){
		for(std::vector<JbNode*>::const_iterator it2=list2.begin(); it2!=list2.end(); ++it2){
			bool found=false;
			for(std::vector<JbNode*>::const_iterator it1=list1.begin(); it1!=list1.end(); ++it1){
				if(*it2==*it1){
					found=true;
					break;
				}
			}
			if(found==false)list1.push_back(*it2);
		}
	}
}

void XmlEditor::exec(XCMD command, const char* xpath0, const std::string& nodeString){
	command_list.push_back(new XmlEditorCommand(command, xpath0, nodeString));
	std::string xpath(xpath0);
	if(command==XCMD_end){
		cleanup();
		return;
	}
	StringBuilder sb;

	FILE*out=0;
	AutoFileHandle adopt_out(out);

	switch(command){
		                                                              
		                                                                
		                    
	case XCMD_close:
		closeReader();
		return;
	case XCMD_define:
		dataelement_map[xpath]=eval(nodeString.c_str());
		return;
	case XCMD_save_nodes:
	case XCMD_save_values:
		break;
	case XCMD_write_nodes:
	case XCMD_write_values:
		{
			std::string fileName=eval(nodeString.c_str());
			out=fopen(fileName.c_str(), "w+b");
			if(out==0){
				long lErr = ::GetLastError();
				std::string errmsg=std::string("Cannot open output file ") +
					fileName + " for " +
					(command==XCMD_write_nodes?"XCMD_write_nodes":"XCMD_write_values")+
					". Reason: " + ErrorResolver().Resolve(lErr);
				throw CrException(errmsg);
			}
		}
		break;
	case XCMD_write_all:
		if(reader==0){
			throw CrException("execute XCMD_write_all command but the reader is undefined");
		}
		{
			std::string fileName=eval(nodeString.c_str());
			out=fopen(fileName.c_str(), "w+b");
			if(out==0){
				long lErr = ::GetLastError();
				std::string errmsg=std::string("Cannot open output file ") +
					fileName + " for XCMD_write_all" +
					". Reason: " + ErrorResolver().Resolve(lErr);
				throw CrException(errmsg);
			}
		}
		node_list->clear();
		if(reader->xml_header!=0)
			node_list->push_back(reader->xml_header);
		if(reader->doc_type!=0)node_list->push_back(reader->doc_type);
		if(reader->root!=0)node_list->push_back(reader->root);
		break;
	case XCMD_open:
		openReader(nodeString);
		if(!xpath.empty()){
			*node_list=reader->selectNodes(xpath);
		}
		return;
	case XCMD_select:
		if(xpath.empty())throw CrException("exec select command must have valid xpath.");
	default:
		if(!xpath.empty()){
			if(node_list==0 || node_list->size()==0 || xpath.at(0)!='.'){
				*node_list=reader->selectNodes(xpath);
			}
			else {
				xpath=xpath.substr(1);
				JbNode* node=node_list->size()>0?node_list->at(0): 0;
				*node_list=reader->selectNodes(xpath, *node_list, node);
			}
		}
		break;
	}
	if((node_list==0||node_list->size()==0) && reader->root==0 && command==XCMD_add){
		if(node_list==0)node_list=new std::vector<JbNode*>();
		node_list->push_back(0);
	}
	if(node_list==0)return;
	std::vector<JbNode*> node_list1;
	for(std::vector<JbNode*>::iterator it1=node_list->begin(); it1!=node_list->end(); ++it1){
		JbNode* node=*it1;
		switch(command){
			case XCMD_save_values:
				node=node->getValueNode();
			case XCMD_save_nodes:
				reader->buildXmlString(sb, node,"");
				break;
			case XCMD_write_values:
				node=node->getValueNode();
			case XCMD_write_nodes:
			case XCMD_write_all:
				reader->writeXml(out,node,"");
				break;
			case XCMD_delete:
				{
					std::vector<JbNode*>::iterator it2=node->findIterator();
					JbNode* child=*it2;
					if(child==node){
						node->getParent()->getChildren().erase(it2);
						delete child;
					}
				}
				break;
			case XCMD_update:
			case XCMD_update_file:
				{
					std::vector<JbNode*>::iterator it2=node->findIterator();
					JbNode*child=*it2;
					if(command==XCMD_update){
						child=reader->replaceNode(child, eval(nodeString.c_str()));
					}
					else {
						std::string fileName=eval(nodeString.c_str());
						child->set(fileName, JbNode::file_base);
					}
					if(child!=0)node_list1.push_back(child);
				}
				break;
			case XCMD_add:
				node_list1.push_back(reader->createNode(node, 0, eval(nodeString.c_str())));
				break;
			case XCMD_add_file:
				{
					std::string fileName=eval(nodeString.c_str());
					node_list1.push_back(new JbNode(reader, node, 0, fileName, JbNode::file_base));
				}
				break;
			case XCMD_insert:
				node_list1.push_back(reader->createNode(node->getParent(), node, eval(nodeString.c_str())));
				break;
			case XCMD_insert_file:
				{
					std::string fileName=eval(nodeString.c_str());
					node_list1.push_back(new JbNode(reader, node->getParent(), node, fileName, JbNode::file_base));
				}
				break;
		default:
			break;
		}
	}
	if(command==XCMD_save_values || command==XCMD_save_nodes){
		dataelement_map[xpath]=sb.toString();
	}
	node_list->swap(node_list1);
}
