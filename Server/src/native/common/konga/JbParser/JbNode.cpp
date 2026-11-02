/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <string>
#include <stack>
#include <cassert>

#include <konga/Utils/StringBuilder.h>
#include <konga/Utils/StringTokenizer.h>
#include <konga/Utils/FileUtils.h>
#include <konga/Utils/CrException.h>
#include <konga/Utils/ErrorResolver.h>

using namespace konga;
using std::string;

#include "JbParser.h"
#include "JbNode.h"

namespace {
	const unsigned char ef = 0xEF;
	const unsigned char bb = 0xBB;
	const unsigned char bf = 0xBF;
}

JbNode::JbNode(JbParser* parser0, JbNode* parent, const string& name, size_t pos1, size_t pos2) :
	m_pos(pos1), m_length(pos2-pos1), m_parent(parent), parser(parser0)
{
	if(m_parent)m_parent->m_children.push_back(this);
	assert(parser);
	m_name_index = parser->getStringIndex(name);

#ifdef _DEBUG
	m_name=name;
	m_contents= m_length==0 ? "" : parser->getStr(m_pos, m_length);
#endif
}

JbNode::JbNode(JbParser*parser, JbNode* parent, JbNode* sibling, const::string& xmlContents, JbNodeBase lcode) :
	m_pos(0), m_length(lcode), m_parent(parent), parser(parser)
{
	assert(parser);
	if(lcode == string_base){
		m_pos=parser->getStringIndex(xmlContents);
		string name=extractXmlNameFromContents(xmlContents);
		m_name_index=parser->getStringIndex(name);
	}
	else if(lcode == file_base){
		m_pos=parser->getStringIndex(xmlContents);
		m_name_index = 0;
	}

	if(parent==0)return;
	parent->modifyContents();

	if(sibling==0){
		parent->m_children.push_back(this);
	}
	else {
		if(parent!=sibling->getParent()){
			throw CrException("Insert JbNode constructor error. sibling's parent should be the same as this parent!"); 
		}
		std::vector<JbNode*>::iterator it=sibling->findIterator();
		if(*it==sibling){
			parent->m_children.insert(it, this);
		}
		else {
			throw CrException("Insert JbNode constructor error. cannot find the sibling among the children of the parent!"); 
		}
	}
#ifdef _DEBUG
	m_name="unknown";
	m_contents=xmlContents;
#endif
}

JbNode::~JbNode(){
	for(std::vector<JbNode*>::iterator it=m_children.begin();
		it!= m_children.end(); ++it){
		delete *it;
	}
}

string JbNode::extractXmlNameFromContents(const string& xmlContents){
#ifdef _DEBUG
	m_contents=xmlContents;
#endif
	if(!xmlContents.empty() && xmlContents.at(0)=='<'){
		for(int i=1, i0=1, n=xmlContents.size(); i<n; ++i){
			switch(xmlContents.at(i)){
				case ':': i0=i+1; break;
				case ' ':
				case '/':
				case '>':
					if(i>i0){
						string name=xmlContents.substr(i0, i-i0);
#ifdef _DEBUG
						m_name=name;
#endif
						return name;
					}
					break;
			}
		}
	}
	return string("");
}

void JbNode::modifyContents(){
	string contents=getContents();
	int len=contents.size();
	if(contents.at(len-2)=='/'){
		contents=contents.substr(0,len-2)+">";
#ifdef _DEBUG
		m_contents=contents;
#endif
		m_length=string_base;
		m_pos=parser->getStringIndex(contents);
	}
}

void JbNode::set(const::string& xmlContents, int lcode){
	string name=extractXmlNameFromContents(xmlContents);
	m_name_index = parser->getStringIndex(name);
	m_pos = parser->getStringIndex(xmlContents);
	m_length=lcode;
	for(std::vector<JbNode*>::iterator it=m_children.begin(); it!=m_children.end(); ++it)
		delete *it;
	m_children.clear();
}

bool JbNode::isChildOf(JbNode*parent0){
	JbNode*parent=this->getParent();
	while(parent){
		if(parent==parent0)return true;
		parent=parent->getParent();
	}
	return false;
}

void JbNode::moveToParent(JbNode* parent){
	m_parent=parent;
	m_parent->m_children.push_back(this);
}



std::vector<JbNode*>::iterator JbNode::findIterator(){
	JbNode* parent=getParent();
	if(parent==0)throw CrException("No parent, can't find iterator");
	std::vector<JbNode*>& children(parent->getChildren());
	for(std::vector<JbNode*>::iterator it=children.begin(); it!=children.end(); ++it)
		if(*it==this)return it;
	return children.end();
}

const string& JbNode::getName() const {
	return parser->getString(m_name_index);
}

string JbNode::getLocalName() const
{
	const string& name(parser->getString(m_name_index));
	int k=name.find(':');
	if(k==string::npos)return string(name);
	return name.substr(k+1);
}
string JbNode::getNamespaceURI() const
{
	const string& name(parser->getString(m_name_index));
	const string::size_type k=name.find(':');
	if(k==string::npos)return "";
	return getNamespaceFromPrefix(name.substr(0,k));
}

string JbNode::getNamespaceFromPrefix(const string& prefix) const
{
	const string xmlns_prefix("xmlns:"+prefix);
	return getNS(xmlns_prefix);
}

string JbNode::getNS(const string& xmlns_prefix) const
{
	bool found;
	const string& value=getAttributeValue(xmlns_prefix, found);
	if(found)return value;
	if(m_parent)return m_parent->getNS(xmlns_prefix);
	else return "";
}

namespace {
	void decode(string& value) {
		string::size_type k=value.find('&');
		if(k==string::npos)return;
		value=translate_html(value.c_str());
	}
}

string JbNode::getAttributeValue(const string& name, bool& found) const
{
	found=false;
	string atts=parser->getStr(m_pos, m_length);
	int n=atts.length(), na=name.length();
	for(string::size_type i=0, i1=0, i2=0; (i=atts.find(name, i))!=string::npos; i++){
		                                                                                 
		unsigned char c=atts.at(i-1);
		if(c!=' ' && c!='\t' && c!='\n' && c!='\r')continue;
		i+=na;
		if(i+2>=n)return "";
		if(atts.at(i)=='=' && atts.at(i+1)){
			char c=atts.at(i+1);
			if(c!='"' && c!='\'')
				throw konga::CrException("getAttributeValue failed for attribute name="+name+"."+
					"\nThe character right after the '=' for this attribute must be a quote character"+
					" The element is\n"+atts+"\n");
			i1=i+2;
			i2=atts.find(c,i1);
			if(i2!=string::npos){
				found=true;
				string value=atts.substr(i1, i2-i1);
				decode(value);
				return value;
			}
		}
	}
	return "";
}

string JbNode::getAttributeValue(const string& namespaceURI, const string& localName) const
{
	string attribute_v;
	if(namespaceURI.empty()){
		bool bFound;
		return getAttributeValue(localName, bFound);
	}
	string atts=parser->getStr(m_pos, m_length);
	konga::StringTokenizer tk(atts.c_str(), " \r\n\t:=\"'");
	while(tk.NextToken()){
		char c=tk.token()[0];
		if(iswspace(c)==false)break;
	}
	bool bNamespace=false;
	string prefix, name, ns;
	while(tk.NextToken()){
		const char* token=tk.token();
		switch(token[0]){
			case ' ':
			case '\r':
			case '\n':
			case '\t':
				bNamespace=false;
				prefix="";
				name="";
				break;
			case ':':
				prefix=name;
				name="";
				break;
			case '=':
				if(!prefix.empty()){
					if(name==localName && getNamespaceFromPrefix(prefix)==namespaceURI){
						char c_saved=0;
						for(int iq=0; tk.NextToken(); ){
							token=tk.token();
							char c=token[0];
							switch(iq){
							case 0:
								if(c=='"'||c=='\''){
									iq=1;
									c_saved=c;
								}
								break;
							case 1:
								if(c==c_saved){
									decode(attribute_v);
									return attribute_v;
								}
								else {
									attribute_v+=token;
								}
							}
						}
					}
				}
				break;
			case '"':
			case '\'':
				break;
			default:
				name=token;
				break;
		}
	}
	return attribute_v;
}

void JbNode::writeDebugTree(FILE*debug_log, const string& header){
	if(IsValueNode()){
		fprintf(debug_log, "%s[V] \"%s\"\n", header.c_str(), getText().c_str());
	}
	else {
		fprintf(debug_log, "%s[E %d] %s \n", header.c_str(), m_name_index, parser->getAttributeString(this).c_str());
	}
	string header1=header+"  ";
	for(std::vector<JbNode*>::const_iterator it=m_children.begin(); it!=m_children.end(); ++it){
		JbNode* child=*it;
		child->writeDebugTree(debug_log, header1);
	}
}

bool JbNode::IsValueNode() const {
	                                                
	return m_name_index==0;
}

string JbNode::getText() const{
	if(IsValueNode()){
		string text = parser->getStr(m_pos, m_length);
		if(konga::require_translate_html(text.c_str()))
			text=konga::translate_html(text.c_str());
		return konga::TrimCharsFromString(text, " \r\n\t");
	}
	else return string();

}

string JbNode::getContents() {
	if(m_length==-1){
		return parser->getString(m_pos);
	}
	else if(m_length==-2){
		return "<<getContents() with fileName=\""+parser->getString(m_pos)+"\">>";
	}
#ifdef _DEBUG
	m_contents=parser->getStr(m_pos, m_length);
	return m_contents;
#else
	return parser->getStr(m_pos, m_length);
#endif
}

string JbNode::getXmlString(){
	std::string str=parser->getStr(m_pos, parser->findEndOfElementPosition(m_pos)-m_pos);
	konga::convertCRLN2LN(str);
	return str;
}

void JbNode::setEndPos(size_t pos2){
	m_length=pos2-m_pos;
#ifdef _DEBUG
	m_contents=parser->getStr(m_pos, m_length);
#endif
}

bool JbNode::changeElementName(FILE*out,
	const std::map<unsigned short,unsigned short>& oldToNewNameIndex_map,
	const std::map<unsigned short, string>& nameIndexValue_map,
	const string& header)
{
	const string& name=getName();
	string content(getContents());
	if(name.empty()){
		unsigned short nameIndex=getParent()->getNameIndex();
		std::map<unsigned short, string>::const_iterator it=nameIndexValue_map.find(nameIndex);
		if(it!=nameIndexValue_map.end()){
			fprintf(out, "%s", it->second.c_str());
		}
		else {
			fprintf(out, "%s", content.c_str());
		}
		return false;
	}
	bool endElement=content.at(m_length-2)=='/';
	std::map<unsigned short,unsigned short>::const_iterator it=oldToNewNameIndex_map.find(m_name_index);
	bool nameChanged=it!=oldToNewNameIndex_map.end();
	int k1=1;                                
	string::size_type k2=content.find(' ');                              
	if(k2==string::npos){
		k2=content.find('>');
	}
	if(k2==string::npos){
		k2=content.find('/');
	}
	assert(k2!=string::npos);
	string qname=content.substr(1,k2-1);
	if(nameChanged){
		const string& newName=parser->getString(it->second);
		string::size_type k=content.find(':');
		string prefix;
		if(k<k2 && k!=string::npos){
			k1=k+1;
			prefix=content.substr(1,k);
		}
		assert(name==content.substr(k1,k2-k1));
		content=content.substr(0,k1)+newName+content.substr(k2);
		qname=prefix+newName;
	}
	fprintf(out, "%s%s", header.c_str(), content.c_str());
	if(endElement)return true;
	const string& header1(header+"  ");
	bool addHeaderForEndElement=false, lineFeedAdded=false;
	for(std::vector<JbNode*>::const_iterator it=m_children.begin(); it!=m_children.end(); ++it){
		JbNode*child=*it;
		if(!lineFeedAdded && !child->IsValueNode()){
			fputc('\n', out);
			lineFeedAdded=true;
		}
		if(child->changeElementName(out, oldToNewNameIndex_map, nameIndexValue_map, header1)){
			fputc('\n', out);
			addHeaderForEndElement=true;
		}
	}
	if(addHeaderForEndElement){
		fprintf(out, "%s", header.c_str());
	}
	fprintf(out, "</%s>", qname.c_str());
	return true;
}

bool JbNode::changeElementName(StringBuilder& sb, const std::map<unsigned short,unsigned short>&oldToNewNameIndex_map, const string& header){
	const string& name=getName();
	string content(getContents());
	if(name.empty()){
		sb.append(content);
		return false;
	}
	bool endElement=content.at(m_length-2)=='/';
	std::map<unsigned short,unsigned short>::const_iterator it=oldToNewNameIndex_map.find(m_name_index);
	bool nameChanged=it!=oldToNewNameIndex_map.end();
	int k1=1;                                
	string::size_type k2=content.find(' ');                              
	if(k2==string::npos){
		k2=content.find('>');
	}
	if(k2==string::npos){
		k2=content.find('/');
	}
	assert(k2!=string::npos);
	string qname=content.substr(1,k2-1);
	if(nameChanged){
		const string& newName=parser->getString(it->second);
		string::size_type k=content.find(':');
		string prefix;
		if(k<k2 && k!=string::npos){
			k1=k+1;
			prefix=content.substr(1,k);
		}
		assert(name==content.substr(k1,k2-k1));
		content=content.substr(0,k1)+newName+content.substr(k2);
		qname=prefix+newName;
	}
	sb.append(header).append(content);
	if(endElement){
		                  
		return true;
	}
	const string& header1(header+"  ");
	bool addHeaderForEndElement=false, lineFeedAdded=false;
	for(std::vector<JbNode*>::const_iterator it=m_children.begin(); it!=m_children.end(); ++it){
		JbNode*child=*it;
		if(!lineFeedAdded && !child->IsValueNode()){
			sb.append('\n');
			lineFeedAdded=true;
		}
		if(child->changeElementName(sb, oldToNewNameIndex_map, header1)){
			sb.append('\n');
			addHeaderForEndElement=true;
		}
	}
	if(addHeaderForEndElement){
		sb.append(header);
	}
	sb.append("</").append(qname).append(">");
	return true;
}

JbNode* JbNode::getValueNode() const{
	for(std::vector<JbNode*>::const_iterator it=m_children.begin();
		it!=m_children.end(); ++it){
		JbNode*child=*it;
		if(child->IsValueNode())return child;
	}
	return 0;
}


string JbNode::getValue(){
	std::string value;
	if(IsValueNode()){
		value=getContents();
	}
	else {
		JbNode*valueNode=getValueNode();
		if(valueNode!=0)value=valueNode->getContents();
	}
	return konga::xml_text_restore(value.c_str());
}

JbNode*JbNode::getChildByName(const string&name) const{
	int nameIndex=parser->getStringIndex(name);
	for(std::vector<JbNode*>::const_iterator it=m_children.begin(); it!=m_children.end(); ++it){
		if(nameIndex==(*it)->getNameIndex())return *it;
	}
	return 0;
}

bool JbNode::isComplete(const std::string&name, const std::string& contents) const{
	if(m_length==file_base)return true;
	int len=contents.size();
	for(int i=len-1; i>=0; --i){
		if(iswspace(contents.at(i))==false){
			len=i+1;
			break;
		}
	}
	if(len<2 || contents.at(0)!='<' || (contents.at(len-2)=='/' && contents.at(len-1)=='>'))return true;
	std::string tag="</"+name+">";
	if(contents.find(tag)!=std::string::npos)return true;
	return false;
}

void JbNode::collectNodes(const std::string& path, std::vector<JbNode*>&list0){
	std::vector<JbNode*>list;
	list.push_back(this);
	StringTokenizer tk(path.c_str(),".$/");
	while(tk.NextToken()){
		const char* token=tk.token();
		switch(token[0]){
			case '.':
			case '$':
			case '/':
				break;
			default:
			{
				std::vector<JbNode*>children;
				for(std::vector<JbNode*>::iterator it=list.begin(); it!=list.end(); ++it){
					JbNode*node=*it;
					node->addToChildren(token,children);
				}
				list.swap(children);
			}
		}
	}
	list0.swap(list);
}

bool JbNode::isComplete(){
	if(m_length==file_base)return true;
	if(m_length==string_base){
		const string& contents=getContents();
		int len=contents.size();
		return len<2 || contents.at(0)!='<' || contents.at(len-2)=='/';
	}
	return getContents().at(m_length-2)=='/';
}

void JbNode::addToChildren(const std::string& name, std::vector<JbNode*>&children){
	for(std::vector<JbNode*>::iterator it=m_children.begin(); it!=m_children.end(); ++it){
		JbNode*child=*it;
		if(child->getName()==name)children.push_back(child);
	}
}

std::string JbNode::getPrefix() const{
	std::string str=parser->getStr(m_pos, m_length);
	size_t n=str.length();
	const char*s=str.c_str();
	if(s[0]!='<')throw konga::CrException("JbNode::getPrefix error, input string must begin with '<'. str=\""+str+"\"");
	size_t pos=1;
	while(pos<n){
		int c=s[pos];
		if(c==':')return str.substr(1,pos);
		if(iswspace(c) || c=='/' || c=='>')return "";
		++pos;
	}
	throw konga::CrException("JbNode::getPrefix error, input string does not close properly. str=\""+str+"\"");
}

std::string JbNode::getPrefixedName() const{
	std::string str=parser->getStr(m_pos, m_length);
	size_t n=str.length();
	const char*s=str.c_str();
	if(s[0]!='<')throw konga::CrException("JbNode::getPrefixedName error, input string must begin with '<'. str=\""+str+"\"");
	size_t pos=1;
	while(pos<n){
		int c=s[pos];
		if(iswspace(c) || c=='/' || c=='>')return str.substr(1,pos);
		++pos;
	}
	throw konga::CrException("JbNode::getPrefixedName error, input string does not close properly. str=\""+str+"\"");
}

void JbNode::getAttributeMap(std::map<std::string,std::string>& attributeMap){
	std::string attrs=parser->getStr(m_pos, m_length);
	size_t n=attrs.length();
	const char*s=attrs.c_str();
	if(s[0]!='<')throw konga::CrException("JbNode::getAttributeMap error, input string must begin with '<'. str=\""+attrs+"\"");
	size_t pos=1, pos1=0, pos2=0, pos3=0, n1, n2;

	while(pos<n){
		if(iswspace(s[pos]))break;
		++pos;
	}
	if(pos==1)throw konga::CrException("JbNode::getAttributeMap error, no element name. str=\""+attrs+"\"");

	while(s[pos]!='/'&&s[pos]!='>'&&pos<n){
		while(pos<n){
			if(!iswspace(s[pos]))break;
			++pos;
		}

		int c;
		pos1=pos;
		while(pos<n){
			if(s[pos]=='=')break;
		}
		if(s[pos]!='=')throw konga::CrException("JbNode::getAttributeMap error missing '=' character for attribute name value pair. error occurred at end of \""+attrs.substr(0, pos)+"...\"");
		n1=pos-pos1;

		if((c=s[++pos])!='"'||c!='\'')throw konga::CrException("JbNode::getAttributeMap error missing begin quote for attribute value. error occurred at end of \""+attrs.substr(0, pos)+"...\"");
		pos2=pos;
		while(pos<n){
			if(s[pos]==c)break;
			++pos;
		}
		if(s[pos]!=c)throw konga::CrException("JbNode::getAttributeMap error missing end quote for attribute value. error occurred at end of \""+attrs.substr(0, pos)+"...\"");
		pos3=pos;
		n2=pos3-pos2-2;

		std::string name=attrs.substr(pos1,n1), value=attrs.substr(pos2+1,n2);
		decode(value);
		attributeMap[name]=value;
		++pos;
	}
}
