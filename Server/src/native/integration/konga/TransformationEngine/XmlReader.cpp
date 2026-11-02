/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <string>
#include <stack>
#include <vector>
#include <cassert>

#include <konga/Utils/PortDefs.h>		                                          
#include <konga/Utils/AutoFileCleanup.h>
#include <konga/Utils/FileTokenizer.h>
#include <konga/Utils/StringBuilder.h>
#include <konga/Utils/CrException.h>
#include <konga/Utils/StringTokenizer.h>
#include <konga/Utils/ErrorResolver.h>

#include "XmlReader.h"

using namespace konga;

namespace {
	const unsigned char ef = 0xEF;
	const unsigned char bb = 0xBB;
	const unsigned char bf = 0xBF;
	const char* xml_tokens = "<> =\"";
	std::string blank;
}

	XmlReader::XmlReader(const std::string& xml_file_name,const std::string& source_encoding) : JbParser(xml_file_name)
	{
		xml_header = 0;
		doc_type = 0;
		last_status = t_unknown;
		root=0;
		if(!xml_file_name.empty()) {
			try {
				m_utf8Input.reset(new etlforge::Utf8TextInput(xml_file_name,source_encoding));
				if(m_utf8Input->path()!=xml_file_name) {
					delete in;in=0;
					in=new FileContents(m_utf8Input->path());
				}
			} catch(const std::exception& failure) {throw CrException(failure.what());}
		}
		if(in)tk=new FileTokenizer(in, xml_tokens);
		else tk=0;
	}

	XmlReader::~XmlReader(){
		if(xml_header!=0)delete xml_header;
		if(doc_type!=0)delete doc_type;
		if(tk)delete tk;
		close();
	}

	std::string XmlReader::getLocalName() {
		return node? node->getLocalName(): std::string();
	}

	std::string XmlReader::getNamespaceURI() {
		return node? node->getNamespaceURI(): std::string();
	}

	bool XmlReader::hasNext(){
		return last_status!=t_end_document;
	}
	                                                                       
	                                          
	XmlReader::EVENT_ID XmlReader::next() {
		if(last_status==t_start_end){
			last_status=t_end;
			end_element_node=node;
			node=node->getParent();
			return END_ELEMENT;
		}
		if(last_status==t_unknown){
			last_status=t_start_document;
			if(fileName.empty()==false){
				if((unsigned char)in->at(0)==ef && (unsigned char)in->at(1)==bb && (unsigned char)in->at(2)==bf){
					tk->setNextPos(3);
				}
			}
			return START_DOCUMENT;
		}

		for(size_t pos1=tk->getNextPos(), pos2=0; tk->NextToken(); pos1=tk->getNextPos()){
			const char* token=tk->token();
			switch(token[0]){
			case '<':                            
				{
					if (in->getStr(pos1, 4) == "<!--")
					{
						while(tk->NextToken()){
							if(tk->token()[0]=='>'){
								pos1=tk->getNextPos();
								if(in->getStr(pos1-3, 3) == "-->"){
									break;
								}
							}
						}
						break;
					}
					else if(in->getStr(pos1, 9) == "<![CDATA["){
						while(tk->NextToken()){
							if(tk->token()[0]=='>'){
								pos2=tk->getNextPos();
								if(in->getStr(pos2-3, 3) == "]]>"){
									break;
								}
							}
						}
						if(node){
							pos2=tk->getNextPos();
							value_node=new konga::JbNode(this, node, "", pos1, pos2);
							return CHARACTERS;
						}
						break;
					}
					while(tk->NextToken()){
						char c=tk->token()[0];
						if(c=='"')tk->SpanTo('"', false);
						else if(c=='>'){
							pos2=tk->getNextPos();
							break;
						}
					}
					int len=pos2-pos1-2;
					assert(len>0);
					std::string str=in->getStr(pos1+1, len), name;                                           
					assert(in->at(pos1)=='<');
					assert(in->at(pos2-1)=='>');
					last_status=parseBetween(str, name);                                    
					switch(last_status){
						case t_xml_header:
							if(xml_header!=0)delete xml_header;
							xml_header=new konga::JbNode(this, 0, name, pos1, pos2);
							return XML_HEADER;
						case t_doctype:
							if(doc_type!=0)delete doc_type;
							doc_type=new konga::JbNode(this, 0, name, pos1, pos2);
							return XML_DOCTYPE;
						case t_start:
						case t_start_end:
							node=new konga::JbNode(this, node, name, pos1, pos2);
							if(root==0)root=node;
							return START_ELEMENT;
						case t_end:
							end_element_node=node;
							node=node->getParent();
							return END_ELEMENT;
						default:
							                   
							throw konga::CrException("Invalid XML: "+err_msg);
					}
				}
				        
			case '>':
				err_msg="Unexpected character '>' encountered on line "+konga::toString(tk->getLineNumber());
				throw konga::CrException(err_msg);
			default:
				if(node){
					while(tk->NextToken()){
						token=tk->token();
						if(token[0]=='<'){
							pos2=tk->getNextPos();
							if(in->getStr(pos2-1, 9) == "<![CDATA["){
								while(tk->NextToken()){
									if(tk->token()[0]=='>'){
										pos2=tk->getNextPos();
										if(in->getStr(pos2-3, 3) == "]]>"){
											break;
										}
									}
								}
							}
							else {
								tk->StepBack();
								break;
							}
						}
					}
					pos2=tk->getNextPos();
					for(size_t pos=pos1; pos<pos2; ++pos){
						if(!isspace_safe(in->at(pos))){
							pos1=pos;
							break;
						}
					}
					for(size_t pos=pos2; pos>=pos1; --pos){
						if(!isspace_safe(in->at(pos-1))){
							pos2=pos;
							if(pos2>pos1)value_node=new konga::JbNode(this, node, "", pos1, pos2);
							else value_node=0;
							return CHARACTERS;
						}
					}
				}
				return IGNORABLE_CHARACTERS;
			}
		}
		last_status = t_end_document;
		return END_DOCUMENT;
	}

	                               
	XmlReader::Status XmlReader::parseBetween(const std::string& str, std::string& name){
		char c1=str.at(0), c2=str.at(str.size()-1);
		if(c1=='/'){
			name=str.substr(1);
			std::string::size_type k0;
			if((k0=name.find(':'))!=std::string::npos)name=name.substr(k0+1);
			if(node!=0 && name==node->getName()){
				return t_end;
			}
			else {
				err_msg="End element name is not consistent. node name="+
					node->getName()+" name="+name;
				return t_error;
			}
		}
		else {
			Status stat=(c1=='?')? t_xml_header:
				((c1=='!')? t_doctype :((c2=='/')? t_start_end: t_start));
			std::string::size_type k=str.find(' '), k0;
			if(k==std::string::npos)k=str.length();
			if(str.at(k-1)=='/')k--;
			name=str.substr(0,k);
			if((k0=name.find(':'))!=std::string::npos)name=name.substr(k0+1);
			switch(stat)
			{
			case t_xml_header:
			case t_doctype:
			case t_start:
			case t_start_end:
				return stat;
			default:
				break;
			}
		}
		return t_error;
	}

	JbNode* XmlReader::parse_all(){
		if(tk==0){
			root=0;
			return 0;
		}
		while(hasNext())next();
		return root;
	}

	JbNode*XmlReader::parse(const std::string& s){
		if(tk!=0){
			delete tk;
			tk=0;
		}
		if(in!=0){
			delete in;
			in=0;
		}
		if(root!=0){
			delete root;
			root=0;
		}
		if(xml_header!=0){
			delete xml_header;
			xml_header=0;
		}
		if(doc_type!=0){
			delete doc_type;
			doc_type=0;
		}

		tk=new StringTokenizer(s.c_str(), xml_tokens);
		parse_all();
		return root;
	}

	size_t XmlReader::findEndOfElementPosition(size_t start){
		if(tk==0)tk=new FileTokenizer(in, xml_tokens);
		last_status = t_end;
		tk->setNextPos(start);
		JbNode* root_saved=root;
		root=0;
		while(hasNext()){
			switch(next()){
				case START_ELEMENT:
					break;
				case END_ELEMENT:
					if(end_element_node==root){
						delete root;
						root=root_saved;
						return tk->getNextPos();
					}
			}
		}
		throw konga::CrException("findEndOfElementPosition("+konga::toString(start)+") failed, cannot find end of element. string:\""+in->getStr(start, 50)+" ...\"");
	}


	         
	void XmlReader::changeElementNames(
		const std::string& newXmlFileName,
		const std::map<std::string, std::string>& oldToNewName_map,
		const std::map<std::string, std::string>& nameValue_map)
	{
		                                                     
		std::map<unsigned short,unsigned short> oldToNewNameIndex_map;
		for(std::map<std::string, std::string>::const_iterator it=oldToNewName_map.begin();
			it!=oldToNewName_map.end(); ++it){
			unsigned short i=getStringIndex(it->first);
			unsigned short k=getStringIndex(it->second);
			oldToNewNameIndex_map[i]=k;
		}
		std::map<unsigned short,std::string> nameIndexValue_map;
		for(std::map<std::string, std::string>::const_iterator it=nameValue_map.begin();
			it!=nameValue_map.end(); ++it){
			unsigned short i=getStringIndex(it->first);
			nameIndexValue_map[i]=it->second;
		}

		if(fileName==newXmlFileName){
			                                                                                      
			StringBuilder sb;
			if(xml_header!=0){
				sb.append(xml_header->getContents()).append('\n');
			}
			if(doc_type!=0){
				sb.append(doc_type->getContents()).append('\n');
			}

			root->changeElementName(sb, oldToNewNameIndex_map, "");
			close();

			FILE* out=fopen(newXmlFileName.c_str(),"w+");
			if(out==0){
				const long lErr=GetLastError();
				const std::string sErr("Failed to open xmlfile '" + fileName + "'.\nReason: " + ErrorResolver(lErr).Resolve(lErr));
				throw konga::CrException(sErr);
			}
			AutoFileHandle adopt_out(out);
			sb.write(out);
			fclose(out);
		}
		else {
			FILE* out=fopen(newXmlFileName.c_str(),"w+");
			if(out==0){
				const long lErr=GetLastError();
				const std::string sErr("Failed to open xmlfile '" + fileName + "'.\nReason: " + ErrorResolver(lErr).Resolve(lErr));
				throw konga::CrException(sErr);
			}
			AutoFileHandle adopt_out(out);

			if(xml_header!=0){
				fprintf(out, "%s\n", xml_header->getContents().c_str());
			}
			if(doc_type!=0){
				fprintf(out, "%s\n", doc_type->getContents().c_str());
			}
			root->changeElementName(out, oldToNewNameIndex_map, nameIndexValue_map, "");
			close();
			fclose(out);
		}
	}

	bool XmlReader::buildXmlString(StringBuilder& sb, JbNode*node, const std::string& header){
		if(node->getLength() == -2){
			const std::string& fileName=getString(node->getNameIndex());
			FileContents fc(fileName);
			for(int j=0, n=fc.size(); j<n; ++j)sb.append(fc.at(j));
			sb.append('\n');
			return true;
		}
		std::string contents=node->getContents();
		if(node==xml_header || node==doc_type){
			sb.append(contents).append('\n');
			return true;
		}
		const std::string& name(node->getName());
		if(name.empty()){
			sb.append(contents);
			return false;
		}
		bool endElement=node->isComplete();
		sb.append(header).append(contents);
		if(endElement)return true;

		const std::string& header1(header+"  ");
		bool addHeaderForEndElement=false, lineFeedAdded=false;
		for(std::vector<JbNode*>::const_iterator it=node->getChildrenRef().begin(); it!=node->getChildrenRef().end(); ++it){
			JbNode*child=*it;
			if(!lineFeedAdded && !child->IsValueNode()){
				sb.append('\n');
				lineFeedAdded=true;
			}
			if(buildXmlString(sb, child, header1)){
				sb.append('\n');
				addHeaderForEndElement=true;
			}
		}
		if(addHeaderForEndElement){
			sb.append(header);
		}
		for(int i=1, n=contents.size(); i<n; ++i){
			char c=contents.at(i);
			if(c==' '||c=='/'||c=='>'){
				sb.append("</").append(contents.substr(1,i-1)).append('>');
				break;
			}
		}
		return true;
	}

	bool XmlReader::writeXml(FILE*out, JbNode*node, const std::string& header){
		if(node->getLength() == -2){
			const std::string& fileName=getString(node->getPos());
			FileContents fc(fileName);
			for(int j=0, n=fc.size(); j<n; ++j)fputc(fc.at(j), out);
			fputc('\n', out);
			return true;
		}
		std::string contents=node->getContents();
		if(node==xml_header || node==doc_type){
			fprintf(out, "%s\n",contents.c_str());
			return true;
		}
		const std::string& name(node->getName());
		if(name.empty()){
			fprintf(out, "%s",contents.c_str());
			return false;
		}
		bool endElement=node->isComplete();
		fprintf(out,"%s%s", header.c_str(), contents.c_str());
		if(endElement)return true;

		const std::string& header1(header+"  ");
		bool addHeaderForEndElement=false, lineFeedAdded=false;
		for(std::vector<JbNode*>::const_iterator it=node->getChildrenRef().begin(); it!=node->getChildrenRef().end(); ++it){
			JbNode*child=*it;
			if(!lineFeedAdded && !child->IsValueNode()){
				fputc('\n',out);
				lineFeedAdded=true;
			}
			if(writeXml(out, child, header1)){
				fputc('\n',out);
				addHeaderForEndElement=true;
			}
		}
		if(addHeaderForEndElement)fprintf(out,"%s",header.c_str());

		for(int i=1, n=contents.size(); i<n; ++i){
			char c=contents.at(i);
			if(c==' '||c=='/'||c=='>'){
				fprintf(out,"</%s>",contents.substr(1,i-1).c_str());
				break;
			}
		}
		return true;
	}

	std::string XmlReader::toString(){
		StringBuilder sb;
		if(xml_header!=0){
			sb.append(xml_header->getContents()).append('\n');
		}
		if(doc_type!=0){
			sb.append(doc_type->getContents()).append('\n');
		}
		buildXmlString(sb, root, "");
		return sb.toString();
	}

	JbNode* XmlReader::createValueNode(JbNode* parent, const std::string& value){
		std::string v=konga::trim(value);
		if(v.empty())return 0;
		konga::convertCRLN2LN(v);
		return new konga::JbNode(this, parent, 0, v);
	}

	JbNode*XmlReader::createNode(JbNode*parent, JbNode*sibling, const std::string& str){
		if(!str.empty() && str.at(0)!='<'){
			return createValueNode(parent, str);
		}
		StringTokenizer tk(str.c_str(),"<>");
		enum STATUS {unknown, begin, end} status=unknown;
		std::string body, name, value;
		JbNode*node0=0;
		node=parent;
		JbNode* child=sibling;
		while(tk.NextToken()){
			const char*token=tk.token();
			switch(token[0]){
				case '<':
					if(!value.empty()){
						createValueNode(node, value);
					}
					status=begin;
					value.clear();
					break;
				case '>':
					if(!body.empty()){
						std::string contents="<"+body+">";
						switch(parseBetween(body, name)){
							case t_xml_header:
								if(xml_header!=0)delete xml_header;
								xml_header=new konga::JbNode(this, 0, 0, contents);
								break;
							case t_doctype:
								if(doc_type!=0)delete doc_type;
								doc_type=new konga::JbNode(this, 0, 0, contents);
								break;
							case t_start:
								node=new konga::JbNode(this, node, child, contents);
								child=0;
								if(root==0)root=node;
								if(node0==0)node0=node;
								break;
							case t_start_end:
								node=new konga::JbNode(this, node, child, contents);
								child=0;
								if(node0==0)node0=node;
								if(root==0)root=node;
								node=node->getParent();
								break;
							case t_end:
								node=node->getParent();
								break;
							default:
								                   
								throw konga::CrException("Invalid XML: "+err_msg);
						}
					}
					status=end;
					body.clear();
					break;
				default:
					switch(status){
						case begin: body+=token; break;
						case end: value+=token;
					}
					break;
			}
		}
		return node0;
	}
	JbNode* XmlReader::replaceNode(JbNode*node0, const std::string& nodeString){
		assert(node0!=0);
		JbNode*node=0;
		if(node0==root){
			root=0;
			node=createNode(0, 0, nodeString);
		}
		else {
			JbNode*parent=node0->getParent();
			assert(parent!=0);
			node=createNode(parent, node0, nodeString);
			std::vector<JbNode*>::iterator it=node0->findIterator();
			parent->getChildrenRef().erase(it);
			delete node0;
		}
		return node;
	}
