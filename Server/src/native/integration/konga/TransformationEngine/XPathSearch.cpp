/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#include <math.h>

#include <string>
#include <stack>
#include <cassert>

#include <boost/scoped_array.hpp>

#include <konga/Utils/StringBuilder.h>
#include <konga/Utils/StringTokenizer.h>
#include <konga/Utils/CrException.h>
#include <konga/Utils/ErrorResolver.h>
#include <konga/Utils/util.h>

#include "XPathSearch.h"

using namespace konga;
using std::string;

namespace {
	Token::FNC xpath_fcn[]={
		{"position(","",1},
		{"last(","",2},
		{"number(","t",3},
		{"abs(","i",4},
		{"ceiling(","f",5},
		{"floor(","f",6},
		{"round(","f",7},
		{"round-half-to-even(","f",8},
		{"string(","t",9},
		{"codepoints-to-string(",".",10},
		{"string-to-codepoints(","s",11},
		{"codepoint-equal(,","ss",12},
		{"compare(","ss",13},
		{"concat(","ss.",14},
		{"string-join(","ts",15},
		{"substring(","sii",16},
		{"string-length(","s",17},
		{"normalize-space(","s",18},
		{"normalize-unicode(","",19},
		{"upper-case(","s",20},
		{"lower-case(","s",21},
		{"translate(","sss",22},
		{"escape-uri(","ss",23},
		{"contains(","ss",24},
		{"starts-with(","ss",25},
		{"ends-with(","ss",26},
		{"substring-before(","ss",27},
		{"substring-after(","ss",28},
		{"matches(","sp",29},
		{"replace(","sss",30},
		{"tokenize(","ss",31},
		{"resolve-uri(","ss",32},
		{"boolean(","t",33},
		{"not(","b",34},
		{"true(","",35},
		{"false(","",36},
		{0,0,0}
	};
}

XPathSearch::XPathSearch(const string& xmlFileName) : XmlReader(xmlFileName)
{
	root=parse_all();
}

XPathSearch::~XPathSearch(){
	for(std::vector<JbNode*>::iterator it=m_attributes.begin(); it!=m_attributes.end(); ++it){
		JbNode* attribute=*it;
		delete attribute;
	}
}

JbNode*XPathSearch::selectSingleNode(const string& xpath){
	std::vector<JbNode*> nodeList=selectNodes(xpath);
	return nodeList.at(0);
}

std::vector<JbNode*> XPathSearch::selectNodes(const string& xpath){
	std::vector<JbNode*> node_list;
	JbNode* node=0;
	return selectNodes(xpath, node_list, node);
}

std::vector<JbNode*> XPathSearch::selectNodes(const string& xpath, std::vector<JbNode*>& nodeList, JbNode*& node){
	StringTokenizer tk(xpath.c_str(),"/ *@[]");
	COMMAND cmd=c_unknown;
	while(tk.NextToken()){
		string token=tk.token();
		switch(token.at(0)){
		case ' ': break;
		case '/':
			switch(cmd){
				case c_unknown:
				case c_continue: cmd=c_child; break;
				case c_child: cmd=c_any; break;
				case c_any: throw CrException("xpath syntax error. /// encontered. xpath:"+xpath);
			}
			break;
		case '@':
			{
				switch(cmd){
					case c_unknown:
					case c_any:
						{
							std::vector<JbNode*> nodeList1;
							for(std::vector<JbNode*>::const_iterator it=nodeList.begin(); it!=nodeList.end(); ++it){
								node=*it;
								findAllNodes(node, nodeList1);
							}
							nodeList.swap(nodeList1);
						}
						break;
				}
				cmd=c_attribute;
				break;
			}
		case '[':
			{
				tk.SpanTo(']', false);
				if(tk.NextToken()){
					string filter=tk.token();
					std::vector<JbNode*> nodeList1;
					for(std::vector<JbNode*>::const_iterator it=nodeList.begin(); it!=nodeList.end(); ++it){
						node=*it;
						if(IsAttributeNode(node) || node->IsValueNode())node=node->getParent();
						if(testFilter(node, filter))nodeList1.push_back(*it);
					}
					nodeList.swap(nodeList1);
				}
				else nodeList.clear();
				cmd=c_continue;
			}
			break;
		case ']': throw konga::CrException("XPATH syntax error, extra ']' uncountered in "+xpath);
		default:
			{
				std::vector<JbNode*> nodeList1;
				switch(cmd){
					case c_continue:
						throw CrException("xpath command error, two consecutive c_continue command.");
					case c_attribute:
						for(std::vector<JbNode*>::const_iterator it=nodeList.begin(); it!=nodeList.end(); ++it){
							node=*it;
							JbNode* attr=createAttributeNode(node, token);
							if(attr!=0)nodeList1.push_back(attr);
						}
						break;
					case c_unknown:
					case c_any:
						if(node==0)findSubNodesRecursive(root, nodeList1, token);
						else {
							for(std::vector<JbNode*>::const_iterator it=nodeList.begin(); it!=nodeList.end(); ++it){
								node=*it;
								findSubNodesRecursive(node, nodeList1, token);
							}
						}
						break;
					case c_child:
						if(node==0 && token==root->getName()){
							nodeList1.push_back(root);
						}
						else {
							for(std::vector<JbNode*>::const_iterator it=nodeList.begin(); it!=nodeList.end(); ++it){
								node=*it;
								findSubNodes(node, nodeList1, token);
							}
						}
						break;
				}
				nodeList.swap(nodeList1);
				cmd=c_continue;
			}
		}
	}
	return nodeList;
}

string XPathSearch::getSingleValue(const string& xpath, bool& exists) {
	exists = false;
	std::vector<JbNode*> nodeList=selectNodes(xpath);
	if(nodeList.empty()) return "";
	exists = true;
	for(std::vector<JbNode*>::iterator it=nodeList.begin(); it!=nodeList.end(); ++it){
		JbNode* node=*it;
		string value=node->getValue();
		if(!value.empty())return value;
	}
	return "";
}

std::vector<string> XPathSearch::getValues(const string& xpath, std::vector<JbNode*>& nodeList1){
	std::vector<JbNode*> nodeList=selectNodes(xpath);
	std::vector<string> valueList;
	for(std::vector<JbNode*>::iterator it=nodeList.begin(); it!=nodeList.end(); ++it){
		string value=(*it)->getValue();
	}
	return valueList;
}



void XPathSearch::findSubNodes(JbNode* start, std::vector<JbNode*>& nodeList, const string& name){
	for(std::vector<JbNode*>::const_iterator it=start->getChildrenRef().begin();
		it!=start->getChildrenRef().end(); ++it){
		JbNode* child=*it;
		if(child->getName()==name || name=="*")nodeList.push_back(child);
	}
}

void XPathSearch::findSubNodesRecursive(JbNode* start, std::vector<JbNode*>& nodeList, const string& name){
	if(start==0)return;
	if(start->getName()==name || name=="*")nodeList.push_back(start);
	for(std::vector<JbNode*>::const_iterator it=start->getChildrenRef().begin();
		it!=start->getChildrenRef().end(); ++it){
		JbNode* child=*it;
		findSubNodesRecursive(child, nodeList, name);
	}
}

void XPathSearch::findAllNodes(JbNode* start, std::vector<JbNode*>& nodeList){
	nodeList.push_back(start);
	for(std::vector<JbNode*>::const_iterator it=start->getChildrenRef().begin();
		it!=start->getChildrenRef().end(); ++it){
		JbNode* child=*it;
		if(child->IsValueNode()==false)findAllNodes(child, nodeList);
	}
}

void XPathSearch::findAttributes(std::vector<JbNode*>& startList, std::vector<JbNode*>& nodeList, const string& name){
	for(std::vector<JbNode*>::const_iterator it=startList.begin();
		it!=startList.end(); ++it){
		JbNode* child=*it;
		JbNode* attr=createAttributeNode(child, name);
		if(attr!=0)nodeList.push_back(attr);
	}
}
JbNode* XPathSearch::createAttributeNode(JbNode*node, const string& name)
{
	string atts=node->getContents();
	StringTokenizer tk(atts.c_str(), "< \r\n\t=\"\'>");
	size_t pos1, pos2;
	enum STATUS {s_unknown, s_element, s_name, s_name_selected, s_value, s_value_selected, s_selected};
	STATUS status=s_unknown;
	while(tk.NextToken()){
		const char* token=tk.token();
		switch(token[0]){
			case '<':
				status=s_name;
				break;
			case ' ':
			case '\r':
			case '\n':
			case '\t':
			case '>':
				if(status==s_value_selected){
					JbNode* attr=new JbNode(this, 0, name, pos1+1, pos2-1);
					if(attr){
						m_attributes.push_back(attr);
						attr->setParent(node);
					}
					return attr;
				}
				break;
			case '=':
				if(status==s_name_selected){
					pos1=node->getPos()+tk.getNextPos();
					status=s_value;
				}
				break;
			case '"':
			case'\'':
				if(status==s_value){
					tk.SpanTo(token[0], false);
					tk.NextToken();
					pos2=node->getPos()+tk.getNextPos();
				}
				status=s_value_selected;
				break;
			default:
				if(status==s_name && (name=="*" || name==token)){
					status=s_name_selected;
				}
				break;
		}
	}
	return 0;
}

string XPathSearch::getAttributeValue(JbNode*node, const string& name){
	string atts=node->getContents();
	StringTokenizer tk(atts.c_str(), "< \r\n\t=\"\'>");
	size_t pos1, pos2;
	enum STATUS {s_unknown, s_element, s_name, s_name_selected, s_value, s_value_selected, s_selected};
	STATUS status=s_unknown;
	while(tk.NextToken()){
		const char* token=tk.token();
		switch(token[0]){
			case '<':
				status=s_name;
				break;
			case ' ':
			case '\r':
			case '\n':
			case '\t':
			case '>':
				if(status==s_value_selected){
					return getStr(pos1+1, pos2-pos1-2);
				}
				break;
			case '=':
				if(status==s_name_selected){
					pos1=node->getPos()+tk.getNextPos();
					status=s_value;
				}
				break;
			case '"':
			case'\'':
				if(status==s_value){
					tk.SpanTo(token[0], false);
					tk.NextToken();
					pos2=node->getPos()+tk.getNextPos();
				}
				status=s_value_selected;
				break;
			default:
				if(status==s_name && (name=="*" || name==token)){
					status=s_name_selected;
				}
				break;
		}
	}
	return "";
}

string XPathSearch::getNodeValue(JbNode*node){
	if(node==0)return "";
	return node->getValue();
}

bool XPathSearch::IsAttributeNode(JbNode*node){
	for(std::vector<JbNode*>::iterator it=m_attributes.begin(); it!=m_attributes.end(); ++it){
		if(*it == node)return true;
	}
	return false;
}

string XPathSearch::getPath(JbNode*node){
	std::stack<JbNode*> stack;
	for(JbNode* parent=node->getParent(); parent!=0; parent=parent->getParent()){
		stack.push(parent);
	}
	StringBuilder sb;
	while(stack.size()>0){
		JbNode* parent=stack.top();
		sb.append('/').append(parent->getName());
		stack.pop();
	}
	if(IsAttributeNode(node)){
		sb.append('@').append(node->getName());
	}
	else {
		sb.append('/').append(node->getName());
	}
	return sb.toString();
}

int XPathSearch::parseXPathFilter(const char* filter, std::vector<konga::Token*>& tokens){
	if(filter[0]==0)return 0;
	std::stack<bool>stack_valid_LHS;
	std::map<std::string, konga::DataElement*> local_variable_map;

	int i=0,n=(int)strlen(filter);
	int err=0;
	bool isAtStart=true;
	bool bNoToken=true;
	while(i<n){
		std::auto_ptr<Token> token(new Token(filter, i, isAtStart, false, xpath_fcn, 0, true, err));
		konga::Token::Type type=token->getType();
		switch(type){
		case Token::Num:
		case Token::StrConst:
		case Token::BoolConst:
			token->DefineDE(0);
		case Token::IDE:
		case Token::GlobalDE:
		case Token::And:
		case Token::Or:
		case Token::Add:
		case Token::Sub:
		case Token::Mul:
		case Token::Div:
		case Token::Mod:
		case Token::NE:
		case Token::EQ:
		case Token::GT:
		case Token::LT:
		case Token::GE:
		case Token::LE:
		case Token::POW:
		case Token::Neg:
		case Token::Not:
		case Token::LP:
		case Token::Comma:
		case Token::Func:
		case Token::RP:
			bNoToken=false;
			break;
		default:
			break;
		}
		if(token.get()!=0)tokens.push_back(token.release());
		if(err)break;
		if(type!=Token::WhiteSpaces&&type!=Token::Comment){
			isAtStart=(type==Token::Unknown||
				type==Token::LP||type==Token::Func||
				type==Token::And||type==Token::Or||type==Token::NE||type==Token::EQ||type==Token::GT||type==Token::GE||type==Token::LT||
				type==Token::LE||type==Token::Comma
				);
		}
	}
	if(bNoToken)return -2;
	return err;
}

void XPathSearch::rpnOrdering(std::vector<Token*>& tokens){
	int nToken=tokens.size();
	std::stack<Token*>tmp_op_stack;
	std::vector<Token*> rpn_tokens, unused;
	AutoPtrDeleteVector<Token> adapt(unused);
	std::stack<int>fcn_n_data, n_comma_stack;
	int n_data=0, n_comma=0;
	Token*token=0, *op;
	Token::Type type=Token::Unknown, op_type=Token::Unknown, last_type=Token::Unknown;
	int err=0;
	bool isAtStart=true;
	for(int i=0;i<nToken;++i){
		token=tokens[i];
		type=token->getType();
		switch(type){
		case Token::IDE:                       
		case Token::GlobalDE:
		case Token::Num:
		case Token::StrConst:
		case Token::BoolConst:
			rpn_tokens.push_back(token);
			n_data+=1;
			break;
		case Token::And:                                                   
		case Token::Or:
		case Token::Add:
		case Token::Sub:
		case Token::Mul:
		case Token::Div:
		case Token::Mod:
		case Token::NE:
		case Token::EQ:
		case Token::GT:
		case Token::LT:
		case Token::GE:
		case Token::LE:
			while(tmp_op_stack.size()>0){
				op=tmp_op_stack.top();
				if(op->priority()<token->priority())break;
				if(op->getType()!=Token::Comma){
					rpn_tokens.push_back(op);
					n_data+=-1;
					                                                                 
					tmp_op_stack.pop();
				}
			}
			if(type==Token::And||type==Token::Or){                                                                           
				rpn_tokens.push_back(new Token(Token::Marker,"["));
			}
			tmp_op_stack.push(token);
			break;
		case Token::Func:
		case Token::LBRACE:
			rpn_tokens.push_back(new Token(Token::Marker,"["));
			tmp_op_stack.push(token);
			n_comma_stack.push(n_comma);
			n_comma=0;
			fcn_n_data.push(n_data);
			break;
		case Token::Comma:
			{
				int n_data_start=n_data;
				while(tmp_op_stack.size()>0){
					op=tmp_op_stack.top();
					if(op->getType()==Token::Comma||op->priority()<token->priority())break;
					if(op->getType()!=Token::Comma){
						rpn_tokens.push_back(op);
						n_data+=op->AddNumberOfData(n_comma,last_type,xpath_fcn);
						tmp_op_stack.pop();
					}
				}
				if((n_data-n_data_start)>1){
					throw CrException("expression delimited by comma, yield more than single value.");
				}
			}
			rpn_tokens.push_back(token);
			break;
		case Token::RP:
			{
				unused.push_back(token);
				bool done=false;
				while(!done && tmp_op_stack.size()>0){
					op=tmp_op_stack.top();
					konga::Token::Type op_type=op->getType();
					tmp_op_stack.pop();
					switch(op_type){
						case Token::Comma:
							unused.push_back(token);
							break;
						case Token::Func:
							rpn_tokens.push_back(op);
							done=true;
							break;
						default:
							rpn_tokens.push_back(op);
							break;
					}
				}
				if(!done){
					throw CrException("fcn does not have close parenthesis.");                                     
				}
				n_data=fcn_n_data.top()+1;
				fcn_n_data.pop();
				n_comma=n_comma_stack.top();
				n_comma_stack.pop();
				token=0;
			}
			break;
		case Token::WhiteSpaces:
		case Token::Comment:
			unused.push_back(token);
			break;
		default:
			assert(0);
			unused.push_back(token);
			break;
		}
		if(err){
			break;
		}
		if(type!=Token::WhiteSpaces&&type!=Token::Comment){
			isAtStart=(type==Token::Unknown||type==Token::Set||
				type==Token::LP||type==Token::Func||type==Token::And||
				type==Token::Or||type==Token::NE||type==Token::EQ||
				type==Token::GT||type==Token::GE||type==Token::LT||
				type==Token::LE||type==Token::Comma||type==Token::Semicolon);
		}
	}
	while(tmp_op_stack.size()>0){
		op=tmp_op_stack.top();
		op_type=op->getType();
		rpn_tokens.push_back(op);
		n_data+=op->AddNumberOfData(n_comma,last_type,xpath_fcn);
		tmp_op_stack.pop();
	}
	if(n_data==0){                                                                                           
		int len=rpn_tokens.size();
		if(len>1 ){
			token=rpn_tokens.back();
			if(token->getType() == Token::Semicolon){
				rpn_tokens.pop_back();
				unused.push_back(token);
				n_data=1;
			}
		}
	}
	if(n_data==0){
		throw CrException("Expression yield no data.");
	}
	if(n_data>1){
		throw CrException("Expression yield more than single data.");
	}
	tokens.swap(rpn_tokens);
}


int XPathSearch::execFunction(JbNode*node, Token*t, DataElement*& de,
							 Token* ts[],
							 std::vector<int>& ind,
							 AutoDeleteStack<DataElement>& tmp)
{
	const Token::FNC*f=&xpath_fcn[t->data()];

	de=0;
	int err=0;
	int argn=(int)ind.size()-1;
	if(argn==1){
		if((1+ind.at(0))==ind.at(1))argn=0;
	}
	if(f==0)throw CrException("TRANSFORM_FUNCTION_DOES_NOT_EXIST");
	const int argn0=(int)strlen(f->fnc_args);
	if(f->isFixedArgumentCount() && argn!=argn0 )
		throw CrException("Wrong number of arguments, argn="+konga::toString(argn)+". It should be "+konga::toString(argn0));
	const int ifcn=f->iFunction;
	switch(ifcn)
	{
		case 1:                      
			tmp.push(de=new DataElement(DataElement::t_int, getPosition(node)));
			break;
		case 2:                  
			tmp.push(de=new DataElement(DataElement::t_int, getLastPosition(node)));
			break;
		case 3:                     
			{
				DataElement* de1=getDataElement(node,1,f,ts,ind,tmp);
				tmp.push(de=new DataElement(DataElement::t_double, de1->GetDouble(err)));
			}
			break;		
		case 4:                  
			{
				DataElement* de1=getDataElement(node,1,f,ts,ind,tmp);
				const double d=fabs(de1->GetDouble(err));
				tmp.push(de=new DataElement(DataElement::t_double, d));
			}
			break;
		case 5:                      
			{
				DataElement* de1=getDataElement(node,1,f,ts,ind,tmp);
				const double d=ceil(de1->GetDouble(err));
				tmp.push(de=new DataElement(DataElement::t_double, d));
			}
			break;
		case 6:                    
			{
				DataElement* de1=getDataElement(node,1,f,ts,ind,tmp);
				const double d=floor(de1->GetDouble(err));
				tmp.push(de=new DataElement(DataElement::t_double, d));
			}
			break;
		case 7:                    
			{
				DataElement* de1=getDataElement(node,1,f,ts,ind,tmp);
				const double d = de1->GetDouble(err);
				tmp.push(de=new DataElement(DataElement::t_double, roundDouble(d)));
			}
			break;
		case 8:                                 
			{
				DataElement* de1=getDataElement(node,1,f,ts,ind,tmp);
				double d=de1->GetDouble(err)+0.5;
				long long l=d;
				l &= ~1;
				d=l;
				tmp.push(de=new DataElement(DataElement::t_double, d));
			}
			break;
		case 9:                     
			{
				DataElement* de1=getDataElement(node,1,f,ts,ind,tmp);
				const char* s=(const char*)de1->GetString(err);
				tmp.push(de=new DataElement(DataElement::t_string, s));
			}
			break;
		case 10:                                    
			throw CrException("function codepoints-to-string(.) is not implemented yet.");
		case 11:                                    
			throw CrException("function string-to-codepoints(s) is not implemented yet.");
		case 12:                                 
			throw CrException("function codepoint-equal(s,s) is not implemented yet.");
		case 13:                       
			{
				DataElement* de1=getDataElement(node,1,f,ts,ind,tmp), *de2=getDataElement(node,2,f,ts,ind,tmp);
				const char* s1=(const char*)de1->GetString(err), *s2=(const char*)de2->GetString(err);
				tmp.push(de=new DataElement(DataElement::t_int, strcmp(s1,s2)));
			}
			break;
		case 14:                        
			{
				DataElement* de1=getDataElement(node,1,f,ts,ind,tmp);
				string s1=(const char*)de1->GetString(err), s;
				for(int i=2; i<=argn; ++i){
					de1=getDataElement(node,i,f,ts,ind,tmp);
					s=(const char*)de1->GetString(err);
					s1+=s;
				}
				tmp.push(de=new DataElement(DataElement::t_string, s1.c_str()));
			}
			break;
		case 15:                            
			throw CrException("function string-join(t,s) is not implemented yet.");
		case 16:                           
			{
				DataElement* de1=getDataElement(node,1,f,ts,ind,tmp), *de2=getDataElement(node,2,f,ts,ind,tmp), *de3=getDataElement(node,3,f,ts,ind,tmp);
				string s1=(const char*)de1->GetString(err);
				int i2=de2->GetInteger(err), i3=de3->GetInteger(err);
				tmp.push(de=new DataElement(DataElement::t_string, s1.substr(i2,i3).c_str()));
			}
			break;
		case 17:                             
			{
				DataElement* de1=getDataElement(node,1,f,ts,ind,tmp);
				const char* s=(const char*)de1->GetString(err);
				tmp.push(de=new DataElement(DataElement::t_int, (int)strlen(s)));
			}
			break;
		case 18:                                   
			{
				DataElement* de1=getDataElement(node,1,f,ts,ind,tmp);
				const char* s=(const char*)de1->GetString(err);
				tmp.push(de=new DataElement(DataElement::t_string, normalizeSpace(s).c_str()));
			}
			break;
		case 19:                                 
			throw CrException("function normalize-unicode(t,s) is not implemented yet.");
		case 20:                           
			{
				DataElement* de1=getDataElement(node,1,f,ts,ind,tmp);
				string s=(const char*)de1->GetString(err);
				ToUpper(s);
				tmp.push(de=new DataElement(DataElement::t_string, s.c_str()));
			}
			break;
		case 21:                           
			{
				DataElement* de1=getDataElement(node,1,f,ts,ind,tmp);
				string s=(const char*)de1->GetString(err);
				ToLower(s);
				tmp.push(de=new DataElement(DataElement::t_string, s.c_str()));
			}
			break;
		case 22:                            
			{
				DataElement *de1=getDataElement(node,1,f,ts,ind,tmp),
					*de2=getDataElement(node,2,f,ts,ind,tmp),
					*de3=getDataElement(node,3,f,ts,ind,tmp);
				if(de1->IsNull()){
					de=new DataElement();
				}
				else {
					KString s1=de1->GetString(err);
					if(err)s1.Empty();
					KString s2=de2->GetString(err);
					if(err)s2.Empty();
					KString s3=de3->GetString(err);
					if(err)s3.Empty();
					s1.Replace((const char*)s2,(const char*)s3);
					de=new DataElement(DataElement::t_string,(const char*)s1);
				}
				
				tmp.push(de);
			}
			break;
		case 23:                            
			throw CrException("function escape-uri(s,b) is not implemented yet.");
		case 24:                        
			{
				DataElement *de1=getDataElement(node,1,f,ts,ind,tmp),
					*de2=getDataElement(node,2,f,ts,ind,tmp);
				KString s1=de1->GetString(err);
				if(err)s1.Empty();
				KString s2=de2->GetString(err);
				if(err)s2.Empty();
				bool b=strstr((const char*)s1, (const char*)s2)!=0;
				de=new DataElement(DataElement::t_bool,b);					
				tmp.push(de);
			}
			break;
		case 25:                             
			{
				DataElement *de1=getDataElement(node,1,f,ts,ind,tmp),
					*de2=getDataElement(node,2,f,ts,ind,tmp);
				KString s1=de1->GetString(err);
				if(err)s1.Empty();
				KString s2=de2->GetString(err);
				if(err)s2.Empty();
				bool b=strncmp((const char*)s1, (const char*)s2, s2.GetLength())==0;
				de=new DataElement(DataElement::t_bool,b);					
				tmp.push(de);
			}
			break;
		case 26:                           
			{
				DataElement *de1=getDataElement(node,1,f,ts,ind,tmp),
					*de2=getDataElement(node,2,f,ts,ind,tmp);
				KString s1=de1->GetString(err);
				if(err)s1.Empty();
				KString s2=de2->GetString(err);
				if(err)s2.Empty();
				int n1=s1.GetLength(), n2=s2.GetLength();
				bool b=false;
				if(n1>n2){
					b=strncmp(&(((const char*)s1)[n1-n2]), (const char*)s2, s2.GetLength())==0;
				}
				de=new DataElement(DataElement::t_bool,b);					
				tmp.push(de);
			}
			break;
		case 27:                                  
			{
				DataElement *de1=getDataElement(node,1,f,ts,ind,tmp),
					*de2=getDataElement(node,2,f,ts,ind,tmp);
				KString s1=de1->GetString(err);
				if(err)s1.Empty();
				KString s2=de2->GetString(err);
				if(err)s2.Empty();
				int n3=0;
				const char* s3=strstr((const char*)s1,(const char*)s2);
				if(s3!=0){
					assert(s3>s1);
					n3=s3-(const char*)s1;

				}
				de=new DataElement(DataElement::t_string, s1.Left(n3));					
				tmp.push(de);
			}
			break;
		case 28:                                 
			{
				DataElement *de1=getDataElement(node,1,f,ts,ind,tmp),
					*de2=getDataElement(node,2,f,ts,ind,tmp);
				KString s1=de1->GetString(err);
				if(err)s1.Empty();
				KString s2=de2->GetString(err);
				if(err)s2.Empty();
				int n1=s1.GetLength(), n2=s2.GetLength(), n3=0;
				const char* s3=strstr((const char*)s1,(const char*)s2);
				if(s3!=0){
					assert(s3>s1);
					n3=n1-n2-(s3-(const char*)s1);

				}
				de=new DataElement(DataElement::t_string, s1.Right(n3));					
				tmp.push(de);
			}
			break;
		case 29:                         
			throw CrException("function matches(s,p) is not implemented yet.");
		case 30:                          
			throw CrException("function replace(s,s,s) is not implemented yet.");
		case 31:                          
			throw CrException("function tokenize(s,s) is not implemented yet.");
		case 32:                             
			throw CrException("function resolve-uri(s,s) is not implemented yet.");
		case 33:                        
			{
				DataElement* de1=getDataElement(node,1,f,ts,ind,tmp);
				bool b=de1->GetBool(err);
				tmp.push(de=new DataElement(DataElement::t_bool, b));
			}
			break;
		case 34:                    
			{
				DataElement* de1=getDataElement(node,1,f,ts,ind,tmp);
				bool b=de1->GetBool(err);
				tmp.push(de=new DataElement(DataElement::t_bool, !b));
			}
		case 35:                    
			{
				tmp.push(de=new DataElement(DataElement::t_bool, true));
			}
		case 36:                     
			{
				tmp.push(de=new DataElement(DataElement::t_bool, false));
			}
	}
	return err;
}

void XPathSearch::handleMarker(JbNode*node, int& i, const int n, Token*& t, Token* ts[],
				DataElement*& de, DataElement*& de1, DataElement*& de2,
				std::stack<DataElement*>& st,
				AutoDeleteStack<DataElement>& tmp,
				int& err)
{
	int level=0;
	std::vector<int> ind;
	ind.push_back(i);
	for(i++;i<n;++i){
		t=ts[i];
		switch(t->getType()){
		case Token::Marker:
			level++;
			break;
		case Token::Comma:
			if(level==0)
				ind.push_back(i);
			break;
		case Token::Func:
		case Token::And:
		case Token::Or:
			level--;
			if(level<0)
				ind.push_back(i);
			break;
		default:
			break;
		};
		if(level<0){
			switch(t->getType()) {
				case Token::And:
				case Token::Or:
					de=st.top();
					st.pop();
					                                                        
					if( (t->getType()==Token::And && de->GetBool(err)) ||
						(t->getType()==Token::Or && !de->GetBool(err)) )
					{
						const int i0=ind.at(0)+1;
						de=eval(node, &ts[i0],ind.at(1)-i0);
						if(!de->isTokenDE())tmp.push(de);
					}
					break;
				default:
					execFunction(node,t,de,ts,ind,tmp);
					break;
			};
			st.push(de);
			break;
		}
	}
}

DataElement* XPathSearch::eval(JbNode*node, Token*ts[], int n){
	AutoDeleteStack<DataElement> tmp;
	std::stack<DataElement*>st;
	Token*t=0;
	DataElement*de=0,*de1=0,*de2=0;
	int err=0;

	for(int i=0;i<n;++i){
		t=ts[i];
		switch(t->getType()){
		case Token::IDE:
			{
				string name=t->GetName();
				if(name.at(0)=='@'){
					name=name.substr(1);
					tmp.push(de=new DataElement(konga::DataElement::t_string, getAttributeValue(node, name).c_str()));
				}
				else {
					JbNode*child=node->getChildByName(name);
					if(child!=0)
						tmp.push(de=new DataElement(konga::DataElement::t_string, getNodeValue(child).c_str()));
					else {
						de=new DataElement(konga::DataElement::t_bool, false);
						return de;
					}
				}
			}
			st.push(de);
			break;
		case Token::Num:
		case Token::StrConst:
		case Token::BoolConst:
			st.push(de=t->GetDE());
			break;
		case Token::Semicolon:
			st.pop();
			break;
		case Token::Marker:
			handleMarker(node, i,n,t,ts,de,de1,de2,st,tmp,err);
			break;
		case Token::Add:
		case Token::Sub:
		case Token::Mul:
		case Token::Div:
		case Token::Mod:
		case Token::NE:
		case Token::EQ:
		case Token::GT:
		case Token::GE:
		case Token::LT:
		case Token::LE:
		case Token::And:
		case Token::Or:
			de2=st.top();
			st.pop();
			de1=st.top();
			st.pop();
			tmp.push(de=new DataElement(de1,de2,t));
			st.push(de);
			break;
		case Token::Func:                        
		case Token::Comma:
		case Token::LBRACE:
		default:
			throw CrException("Unknown syntax at the end of the expression "+t->source_location());
		}
	}
	if(err!=0||(de!=0&&de->getType()== konga::DataElement::t_unknown)&&!de->IsNull()){
		throw CrException("Encounter a data element with unknown data type.");
	}
	if(st.size()!=1){
		throw CrException("Expression does not evaluate to a data element.");
	} else if(err==0){
		de=st.top();
		st.pop();
	}

	if ( de ){
		return new DataElement(*de);
	}
	else
		throw CrException("Evaluating the expression produced no result. This is a bug, please report it.");
}

                                                                                                   
bool XPathSearch::testFilter(JbNode*node, const string& filter){
	std::vector<konga::Token*> tokens;
	AutoPtrDeleteVector<konga::Token> adopt_tokens(tokens);
	parseXPathFilter(filter.c_str(), tokens);
	rpnOrdering(tokens);
	int n=tokens.size();

	Token**ts=new Token*[n];
	boost::scoped_array<Token*> adopt_ts(ts);
	for(int i=0; i<n; ++i)ts[i]=tokens.at(i);

	DataElement* de=eval(node, ts,n);

	std::auto_ptr<DataElement> adapt(de);
	DataElement::Type type=de->getType();
	bool result;
	int err=0;
	string errmsg;
	switch(type){
		case DataElement::t_binary:
		case DataElement::t_unknown:
		case DataElement::t_collection:
		case DataElement::t_date:
			errmsg="Unexpected data type for the result.";
			break;
		case DataElement::t_bool:
			result=de->GetBool(err); 
			if(err!=0){
				errmsg="Error in evaluating boolean result.";
			}
			break;
		case DataElement::t_int:
		case DataElement::t_long:
		case DataElement::t_float:
		case DataElement::t_double:
			{
				int pos=de->GetInteger(err);
				if(err!=0){
					errmsg="Error in getting the integer for position.";
					break;
				}
				result=isAtSiblingPosition(node, pos);
			}
			break;
		case DataElement::t_string:
			{
				const string value((const char*)de->GetString(err));
				result= err==0 && !value.empty();
			}
			break;
	}

	if(!errmsg.empty())throw CrException(errmsg);
	return result;
}


DataElement* XPathSearch::getDataElement(JbNode*node, int ia,const Token::FNC*f,Token*ts[],std::vector<int>&ind,konga::AutoDeleteStack<DataElement>&tmp)
{
	if(ia>= (int)ind.size()){
		throw CrException("error occurred in evaluating non existence argument ("+konga::toString(ia)+").\n");
	}
	const int i0=ind.at(ia-1)+1;
	int err=0;
	DataElement*de0=eval(node, &ts[i0], ind.at(ia)-i0);
	tmp.push(de0);

	if(err==-1)
		throw CrException("error occurred when evaluating argument "+konga::toString(ia)+", DataElement:"+de0->getName()+" not found.\n");
	if(err){
		throw CrException("error occurred when evaluating argument "+konga::toString(ia));
	}
	return de0;
}

int XPathSearch::getPosition(JbNode*node){
	assert(node->getParent()!=0);
	std::vector<JbNode*>& children=node->getParent()->getChildrenRef();
	int pos=0;
	unsigned short nameIndex=node->getNameIndex();
	for(std::vector<JbNode*>::const_iterator it=children.begin(); it!=children.end(); ++it){
		JbNode* child=*it;
		if(nameIndex==child->getNameIndex())pos++;
		if(child==node)return pos;
	}
	return 0;
}

int XPathSearch::getLastPosition(JbNode*node){
	assert(node->getParent()!=0);
	std::vector<JbNode*>& children=node->getParent()->getChildrenRef();
	int pos=0;
	unsigned short nameIndex=node->getNameIndex();
	for(std::vector<JbNode*>::const_iterator it=children.begin(); it!=children.end(); ++it){
		JbNode* child=*it;
		if(nameIndex==child->getNameIndex())pos++;
	}
	return pos;
}

bool XPathSearch::isAtSiblingPosition(JbNode*node, int pos0){
	if(node->getParent()==0 || pos0<=0)return false;
	std::vector<JbNode*>& children=node->getParent()->getChildrenRef();
	int pos=0;
	unsigned short nameIndex=node->getNameIndex();
	for(std::vector<JbNode*>::const_iterator it=children.begin(); it!=children.end(); ++it){
		JbNode*child=*it;
		if(nameIndex==child->getNameIndex())pos++;
		if(pos==pos0)return child==node;
	}
	return false;
}
