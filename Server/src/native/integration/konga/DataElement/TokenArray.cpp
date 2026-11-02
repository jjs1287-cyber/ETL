/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <ctype.h>

#include <limits>

                                         
                                                       
#include <konga/Utils/StringUtils.h>
#include <konga/Utils/StringTokenizer.h>

                                             
#include <konga/TransformationUtils/Definitions.h>
#include <konga/TransformationEngine/resource.h>

#include "Token.h"
#include "TokenArray.h"
#include "DataElement.h"
#include "TransException.h"

#define TRANS "<trans>"
#define END_TRANS "</trans>"

using namespace konga;
using std::string;



TokenArray::TokenArray(const char*buf,int start_pos, int end_pos, const std::set<std::string>*source_de_set, bool isFlatSource, int&err_code)
{
	std::string err_msg;
	std::string tmp(&buf[start_pos+strlen(TRANS)],(end_pos-strlen(END_TRANS))-(start_pos+strlen(TRANS)));
	m_ts=Token::TokenizeToRPN(tmp, source_de_set, isFlatSource, err_code, err_msg);
	if(err_code!=0 && err_code!=NO_TOKENS){
		if(m_ts){
			for(int i=0;m_ts[i]!=0;++i)delete m_ts[i];
			delete[]m_ts;
			m_ts=0;
		}
		throw CrException("Tokenize the following expression failed,\n"+tmp+"\n"+err_msg);
	}
	m_start_pos=start_pos;
	m_end_pos=end_pos;
}

TokenArray::~TokenArray()
{
	if(m_ts){
		for(int i=0; m_ts[i]!=0; ++i)delete m_ts[i];
		delete[] m_ts;
		m_ts=0;
	}
}

int TokenArrayList::find_END_KEY_position(const char*chars,int start){
	int len=(int)strlen(chars), len_END_TRANS=strlen(END_TRANS);
	if(start>=len)return -1;

	konga::StringTokenizer tk(&chars[start],"'\"/*\n<");
	int pos=start;
	char c;
	while(tk.NextToken()){
		const char*token=tk.token();
		switch(c=token[0]){
		case '"':
		case '\'':
			tk.SpanTo(c);
			if(!tk.NextToken())return -1;
			token=tk.token();
			pos+=strlen(token)+2;
			break;
		case '/':
			pos++;
			if(tk.peek_next_token()[0]=='/'){
				while(tk.NextToken()){
					token=tk.token();
					pos+=strlen(token);
					if(token[0]=='\n')break;
				}
			}
			else if(tk.peek_next_token()[0]=='*'){
				while(tk.NextToken()){
					token=tk.token();
					pos+=strlen(token);
					if(token[0]=='*' && tk.peek_next_token()[0]=='/'){
						tk.NextToken();
						pos+=strlen(token);
						break;
					}
				}
			}
			break;
		case  '<':
			if(strncmp(&chars[pos],END_TRANS,len_END_TRANS)==0){
				return pos;
			}
			pos++;
			break;
		default:
			pos+=strlen(token);
			break;
		}
	}
	return -1;
}


TokenArrayList::TokenArrayList(const std::string& templateString, const std::set<std::string>*source_de_set, bool isFlatSource, int& err_code)
: m_lScriptId(-1), m_sScriptName(""), m_sScript(templateString), m_tokenArrayList(0)
{
	if(m_sScript.empty())return;

	err_code=0;

	int i0=0,i1=0,i2=0;
	const char* strTemplate=m_sScript.c_str();
	const char *t;
	m_tokenArrayList=new std::vector<TokenArray*>();
	while((t=strstr(&strTemplate[i0],TRANS))!=0){
		i2=find_END_KEY_position(strTemplate, t-strTemplate);
		if(i2>=0){
			i1=(int)(t-strTemplate);                                    
			i2+=strlen(END_TRANS);                                   
			m_tokenArrayList->push_back(new TokenArray(strTemplate, i1, i2, source_de_set, isFlatSource, err_code));
			i0=i2;
			if(err_code && err_code!=NO_TOKENS)break;
			err_code=0;
		} else {
			i1=i0, i2=(int)strlen(strTemplate);
			err_code=TRANSFORM_UNBALANCED_TRANS_TAG;
			break;
		}
	}
}

TokenArrayList::TokenArrayList(long lScriptId, const std::string& sScriptName, const std::string& sScript, int& err_code)
: m_lScriptId(lScriptId), m_sScriptName(sScriptName), m_sScript(sScript), m_tokenArrayList(0)
{
	if(m_sScript.empty())return;

	err_code=0;

	int i0=0,i1=0,i2=0;
	const char* strTemplate=m_sScript.c_str();
	const char *t;
	m_tokenArrayList=new std::vector<TokenArray*>();
	while((t=strstr(&strTemplate[i0],TRANS))!=0){
		i2=find_END_KEY_position(strTemplate, t-strTemplate);
		if(i2>=0){
			i1=(int)(t-strTemplate);                                    
			i2+=strlen(END_TRANS);                                   
			m_tokenArrayList->push_back(new TokenArray(strTemplate, i1, i2, 0, false, err_code));
			i0=i2;
			if(err_code && err_code!=NO_TOKENS)break;
			err_code=0;
		} else {
			i1=i0, i2=(int)strlen(strTemplate);
			err_code=TRANSFORM_UNBALANCED_TRANS_TAG;
			break;
		}
	}
}


TokenArrayList::~TokenArrayList(){
	for(std::vector<TokenArray*>::iterator it=m_tokenArrayList->begin(); it!=m_tokenArrayList->end(); ++it){
		TokenArray* ar=*it;
		if(ar!=0)delete ar;
	}
	delete m_tokenArrayList;
}

void konga::TokenArrayList::collect_IDE_tokens(std::vector<konga::Token*>& ide_tokens){
	std::vector<TokenArray*>*tokenArrayList=m_tokenArrayList;
	for(std::vector<konga::TokenArray*>::const_iterator it=tokenArrayList->begin();
		it!=tokenArrayList->end(); ++it){
		konga::TokenArray* ta=*it;
		konga::Token** ts=ta->m_ts;
		for(int i=0; ts[i]; ++i){
			if(ts[i]->getType()==konga::Token::IDE) ide_tokens.push_back(ts[i]);
		}
	}
}

bool konga::TokenArrayList::useInstanceResolvingFunction(){
	for(int i=0, n=m_tokenArrayList->size(); i<n; ++i){
		konga::Token*token, **ts=m_tokenArrayList->at(i)->m_ts;
		if(ts==0)continue;

		for(int j=0; (token=ts[j])!=0; ++j){
			switch(token->getType()){
				case Token::Func:
					{
						int k=token->data();
						if(strchr(token->fnc[k].fnc_args,'r')!=0 || stricmp(token->GetName().c_str(),"GetXmlString(")==0)return true;
					}
					break;
				case Token::IDE:
					{
						if(token->GetName().find('#')!=std::string::npos)return true;
					}
					break;
			}
		}
	}
	return false;
}
