/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <ctype.h>
#include <setjmp.h>
#include <float.h>
#include <stdexcept>
#include <cassert>
#include <memory>
#include <set>

#include <konga/Debugger/Debuggee.h>

#include <konga/Utils/AutoFileCleanup.h>
#include <konga/Utils/AutoPtrCleanup.h>
#include <konga/Utils/ErrorResolver.h>
#include <konga/Utils/HashTable.h>
#include <konga/Utils/DBConnectionInfo.h>
#include <konga/Utils/StringUtils.h>
#include <konga/Utils/StringTokenizer.h>
#include <konga/Utils/FileUtils.h>
#include <konga/Utils/Settings.h>

#include <konga/Debugger/Debuggee.h>

#include <konga/Db/DbUtils/DbLookup.h>
#include <konga/Db/DbUtils/DbUtils.h>

#include <konga/Crypto/Crypto.h>

#include <konga/DataElement/DataElement.h>
#include <konga/DataElement/TransException.h>

#include <konga/TransformationUtils/IntegrationBreakpointLocation.h>
#include <konga/TransformationUtils/IntegrationDebugUtils.h>

#include "ErrorCodes/resource.h"
#include "TreeMapper.h"
#include "Transform.h"
#include "LocalVariableValidation.h"
#include "ScriptBlocks.h"
#include "JavaScriptValues.h"

using std::string;

int find_END_KEY_position(const char*chars,int start){
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



using namespace std;
using namespace konga;
                                                                             
             
#ifdef DS_USE_COM
extern ICrComCallerPtr g_pICrComCaller;
int g_pICrComCaller_flag = -1;                                          
int g_ComCallerComTimeOut = -2;                                       

typedef std::map<std::string, long> ComCallerProgIdToTokenMap;
typedef ComCallerProgIdToTokenMap::iterator ComCallerProgIdToTokenMapIt;
typedef ComCallerProgIdToTokenMap::const_iterator ComCallerProgIdToTokenMapConstIt;
std::map<std::string, long> g_ComCallerProgIdToTokenMap;
#endif              

namespace konga {
template<>
std::string HashTable<const char*,DataElement*>::toString()
{
	std::string tmp="{";
	HashBucket<const char*, DataElement*> *lookAt;
	for(int i=0;i<size;++i){
		lookAt=data[i];
		while(lookAt!=0){
			tmp+="["+std::string(lookAt->key)+"="+std::string(lookAt->data->getName())+"];";
			lookAt=lookAt->next;
		}
	}
	tmp+="}";
	return tmp;
}
}                   



CTransform::CTransform(TreeMapper* pTreeMapper, CROM* pTargetNode, const std::set<std::string>*source_de_set, bool isFlatSource) :
m_pTreeMapper(pTreeMapper), m_pDebuggee(pTreeMapper->getOperationContext()->getDebuggee()),
m_pTargetNode(pTargetNode), m_pTargetAttribute(0), m_lScriptId(-1)
{
	Init();
	this->source_de_set=source_de_set;
	this->isFlatSource=isFlatSource;
}

CTransform::CTransform(TreeMapper* pTreeMapper, long lScriptId) :
m_pTreeMapper(pTreeMapper), m_pDebuggee(pTreeMapper->getOperationContext()->getDebuggee()),
m_pTargetNode(0), m_pTargetAttribute(0), m_lScriptId(lScriptId) {
	Init();
	source_de_set=0;
	isFlatSource=true;
}

void CTransform::Init() {
	                                                                                                              
	m_table=new konga::HashTable<const char*,DataElement*>(HASHTABLE_SIZE);
	m_TokenArrayList=0;
	m_AutoNumber=0;
	m_partial_err=0;
	m_bFileTarget=false;
	m_sourceData=0;
	m_sourceInstanceCount=0;
	m_pTrans_targetLoopNode=this;
	m_partial_err = 0;
	m_record = 0;
	n_comma = 0;
	n_data = 0;
	local_variable_map=new LocalVariables();
}

void CTransform::setCount(CROM*targetNode)
{
	CROM*targetLoopNode=targetNode->GetLoopNode();
	m_pTrans_targetLoopNode=targetLoopNode->getTrans();
	m_sourceInstanceCount=m_pTrans_targetLoopNode->m_sourceInstanceCount;
}

void CTransform::setCount(int iInstance)
{
	m_sourceInstanceCount=iInstance+1;
	m_pTrans_targetLoopNode=this;
}

FILE* CTransform::getDebugLog() const {
	return m_pTreeMapper->getViewLog()->GetDebugLog();
}

int CTransform::SetAutoNumber(){
	return SetAutoNumber(m_AutoNumber+1);
}

int CTransform::SetAutoNumber(int startIndex){
	m_AutoNumber=startIndex;
	if(m_pTargetNode){
		m_pTargetNode->setInstanceNumber(m_AutoNumber-1);
	}
	return m_AutoNumber;
}


namespace {
	const int buf_max=1024;
}

namespace {
	bool is_LHS_DE(Token::Type type){
		return type==Token::GlobalDE || type==Token::LocalDE;
	}
}

                                               
int CTransform::Tokenize(const char*str, std::vector<konga::Token*>& tokens){
                                                                  
	if(str[0]==0)return 0;
	                                                                             
	std::stack<bool>stack_valid_LHS;
	int i=0,n=(int)strlen(str);
	int err=0;
	bool isAtStart=true, hasValidLHS=false;
	bool bNoToken=true;
	konga::Token::Type last_token_type=Token::Unknown;
	while(i<n){
		std::auto_ptr<konga::Token> token(new Token(str,i,isAtStart,hasValidLHS,Token::fnc,source_de_set,isFlatSource,err));
		if(is_LHS_DE(last_token_type)){
			switch(token->getType()){
				case Token::PrePP: token->setType(Token::PostPP); break;
				case Token::PreMM: token->setType(Token::PostMM); break;
			}
		}
		konga::Token::Type type=token->getType();
		switch(type){
		case Token::WhiteSpaces:
		case Token::Comment:
			break;
		case Token::IDE:
			{
				const char*path=token->GetName().c_str();
				if(getTreeMapper())getTreeMapper()->linkTokenToSourceDE(this,token.get(),path);
			}
			bNoToken=false;
			break;
		case Token::Num:
		case Token::StrConst:
		case Token::BoolConst:
		case Token::LocalDE:
			token->DefineDE(local_variable_map);
			bNoToken=false;
			break;
		case Token::GlobalDE:
		case Token::SourceDE:
		case Token::ODBC:
		case Token::Set:
		case Token::PSet:
		case Token::MSet:
		case Token::And:
		case Token::Or:
		case Token::Add:
		case Token::Sub:
		case Token::Mul:
		case Token::Div:
		case Token::Not:
		case Token::NE:
		case Token::EQ:
		case Token::GT:
		case Token::LT:
		case Token::GE:
		case Token::LE:
		case Token::LP:
		case Token::RP:
		case Token::LBRACE:
		case Token::RBRACE:
		case Token::LBRACKET:
		case Token::RBRACKET:
		case Token::Neg:
		case Token::PostPP:
		case Token::PostMM:
		case Token::PrePP:
		case Token::PreMM:
		case Token::POW:
		case Token::Comma:
		case Token::Semicolon:
		case Token::Func:
			bNoToken=false;
			if(type==Token::LBRACKET){
				stack_valid_LHS.push(hasValidLHS);
				hasValidLHS=false;
			}
			else if(type==Token::RBRACKET && stack_valid_LHS.size()>0){
				hasValidLHS=stack_valid_LHS.top();
				stack_valid_LHS.pop();
			}
			break;
		default:
			err=TRANSFORM_TOKEN_UNKNOWN_INPUT;                           
			break;
		}
		if(token.get()!=0)tokens.push_back(token.release());
		if(err)break;
		if(type!=Token::WhiteSpaces&&type!=Token::Comment){
			if(type!=Token::RBRACKET)hasValidLHS=(type==Token::Set||type==Token::GlobalDE||type==Token::LocalDE);
			isAtStart=(type==Token::Unknown||type==Token::Set||type==Token::PSet||type==Token::MSet||
				type==Token::LP||type==Token::Func||type==Token::LBRACE||
				type==Token::And||type==Token::Or||type==Token::NE||type==Token::EQ||type==Token::GT||type==Token::GE||type==Token::LT||
				type==Token::LE||type==Token::Comma||type==Token::Semicolon
				);
			
			if((last_token_type==Token::PrePP || last_token_type==Token::PreMM) && !(type==Token::GlobalDE ||type==Token::LocalDE)){
				err = TRANSFORM_TOKEN_UNKNOWN_INPUT;
				return err;
			}				
			last_token_type=type;
		}
	}
	if(bNoToken)return NO_TOKENS;
	return err;
}

                                                                     
                                                                     
                                                    
                                                                                             
                                     
int CTransform::RPNOrdering(const std::vector<konga::Token*>&tokens, std::vector<konga::Token*>&rpn_tokens, std::vector<konga::Token*>&markers, std::vector<konga::Token*>&unused_tokens){
                                                           
	int nToken=tokens.size();
	stack<Token*>tmp_op_stack;
	n_data=0;
	ParenthesisStack parenthesis_stack(this, n_data, rpn_tokens, unused_tokens);
	Token*token=0, *op;
	Token::Type type=Token::Unknown, op_type=Token::Unknown, last_type=Token::Unknown;
	int err=0;
	bool isAtStart=true, hasValidLHS=false;
	for(int i=0;i<nToken;++i){
		token=tokens[i];
		                                                                                              
		type=token->getType();
		if(type==Token::WhiteSpaces || type==Token::Comment){
			unused_tokens.push_back(token);
			continue;
		}
		switch(type){
		case Token::IDE:                       
		case Token::GlobalDE:
		case Token::LocalDE:
		case Token::SourceDE:
		case Token::Num:
		case Token::StrConst:
		case Token::BoolConst:
		case Token::ODBC:
			rpn_tokens.push_back(token);
			n_data+=token->AddNumberOfData(parenthesis_stack.get_nComma(),last_type);
			                                                                     
			if(tmp_op_stack.size()>0){
				op=tmp_op_stack.top();
				op_type=op->getType();
				if(op_type==Token::Neg||op_type==Token::Not){
					rpn_tokens.push_back(op);
					n_data+=op->AddNumberOfData(parenthesis_stack.get_nComma(),last_type);
					                                                                              
					tmp_op_stack.pop();
				}
			}
			break;
		case Token::Set:
		case Token::PSet:
		case Token::MSet:
		case Token::Neg:
		case Token::Not:
		case Token::PrePP:
		case Token::PreMM:
			tmp_op_stack.push(token);
			break;
		case Token::PostPP:
		case Token::PostMM:
			rpn_tokens.push_back(token);
			break;
		case Token::And:                                                   
		case Token::Or:
		case Token::Add:
		case Token::Sub:
		case Token::Mul:
		case Token::Div:
		case Token::NE:
		case Token::EQ:
		case Token::GT:
		case Token::LT:
		case Token::GE:
		case Token::LE:
		case Token::POW:
			while(tmp_op_stack.size()>0){
				op=tmp_op_stack.top();
				if(op->priority()<token->priority())break;
				if(op->getType()!=Token::Comma){
					rpn_tokens.push_back(op);
					n_data+=op->AddNumberOfData(parenthesis_stack.get_nComma(),last_type);
					                                                                 
					tmp_op_stack.pop();
				}
			}
			if(type==Token::And||type==Token::Or){                                                                           
				markers.push_back( op=new Token(Token::Marker,"["));
				rpn_tokens.push_back(op);
			}
			tmp_op_stack.push(token);
			break;
		case Token::Func:
		case Token::LBRACE:
			markers.push_back( op=new Token(Token::Marker,"["));
			rpn_tokens.push_back(op);
			           
		case Token::LP:
			if(type==Token::LP)unused_tokens.push_back(token);
			tmp_op_stack.push(token);
			parenthesis_stack.push(token);
			break;
		case Token::Comma:
			{
				int n_data_start=n_data;
				while(tmp_op_stack.size()>0){
					op=tmp_op_stack.top();
					if(op->getType()==Token::Comma||op->priority()<token->priority())break;
					if(op->getType()!=Token::Comma){
						rpn_tokens.push_back(op);
						n_data+=op->AddNumberOfData(parenthesis_stack.get_nComma(),last_type);
						                                                                     
						tmp_op_stack.pop();
					}
				}
				if((n_data-n_data_start)>1){
					err=EXPRESSION_COMMA_YIELD_MULTI_DATA;
				}
				op=rpn_tokens.back();
				if(n_data==n_data_start && op->getType()==konga::Token::Semicolon){
					rpn_tokens.pop_back();
					unused_tokens.push_back(op);
					n_data++;
					                                                                            
				}
				                                                                     
			}
			rpn_tokens.push_back(token);
			parenthesis_stack.add_comma();
			break;
		case Token::RP:                 
			{
				unused_tokens.push_back(token);
				bool done=false;
				int n_data_start=n_data;
				while(!done && tmp_op_stack.size()>0){
					op=tmp_op_stack.top();
					konga::Token::Type op_type=op->getType();
					tmp_op_stack.pop();
					switch(op_type){
						case Token::Comma:
							break;
						case Token::Func:
						case Token::LP:
							err=parenthesis_stack.checkSemicolon(token, last_type);
							if(err)return err;
							if(op_type==Token::Func)rpn_tokens.push_back(op);
							n_data+=op->AddNumberOfData(parenthesis_stack.get_nComma(), last_type);
							                                                                 
							parenthesis_stack.pop();
							done=true;
							break;
						default:
							rpn_tokens.push_back(op);
							n_data+=op->AddNumberOfData(parenthesis_stack.get_nComma(), last_type);
							                                                                 
							break;
					}
				}
				if(err)break;
				if(!done){
					err=TRANSFORM_TOKEN_UNBALANCE_PAREN;                                     
				}
				token=0;
				if((n_data-n_data_start)>1){
					err=EXPRESSION_RF_YIELD_MULTI_DATA;
				}
				op=rpn_tokens.back();
				if(n_data==n_data_start && op->getType()==konga::Token::Semicolon){
					rpn_tokens.pop_back();
					unused_tokens.push_back(op);
					n_data++;
					                                                                           
				}
				                                                  
			}
			break;
		case Token::RBRACE:
			{
				unused_tokens.push_back(token);
				bool done=false;
				while(!done && tmp_op_stack.size()>0){
					op=tmp_op_stack.top();
					konga::Token::Type op_type=op->getType();
					tmp_op_stack.pop();
					switch(op_type){
						case Token::Comma:
							break;
						case Token::LBRACE:
							err=parenthesis_stack.checkSemicolon(token, last_type);
							if(err)return err;
							rpn_tokens.push_back(op);
							n_data+=op->AddNumberOfData(parenthesis_stack.get_nComma(), last_type);
							                                                               
							parenthesis_stack.pop();
							done=true;
							break;
						default:
							rpn_tokens.push_back(op);
							n_data+=op->AddNumberOfData(parenthesis_stack.get_nComma(), last_type);
							                                                               
							break;
					}
				}
				if(err)break;
				if(!done){
					err=TRANSFORM_TOKEN_UNBALANCE_BRACE;                                     
				}
				token=0;
			}
			break;
		case Token::LBRACKET:
			{
				std::vector<Token*>sub_tokens;
				                                    
				int i1=i+1, i2, nBrace=0;
				for(i2=i1; i2<nToken; ++i2){
					konga::Token*sub_token=tokens[i2];
					switch(sub_token->getType()){
						case konga::Token::LBRACKET: nBrace++; break;
						case konga::Token::RBRACKET: nBrace--; break;
					}
					if(nBrace<0)break;
					Token::Type type_=sub_token->getType();
					if(type_== Token::WhiteSpaces || type_==Token::Comment)
						unused_tokens.push_back(sub_token);
					else
						sub_tokens.push_back(sub_token);
				}
				if(nBrace>=0){
					err=TRANSFORM_TOKEN_UNBALANCE_BRACKET;
					break;
				}
				                                                                                               
				std::vector<Token*>end_array_tokens;
				if(last_type==Token::LBRACKET){
					for(konga::Token* last_token=rpn_tokens.back(); last_token && last_token->getType()==konga::Token::EndArray; ){
						end_array_tokens.push_back(last_token);
						rpn_tokens.pop_back();
						last_token=rpn_tokens.back();
					}
				}
				konga::Token*token1=tokens[i], *token2=tokens[i2];
				std::vector<konga::Token*>sub_rpn_tokens;
				if(sub_tokens.empty()){      
					token1->setType(Token::StartArray);
					token2->setType(Token::EndArray);
					rpn_tokens.push_back(token1);
				}
				else {       
					int n_data_saved=n_data;
					parenthesis_stack.push(token);
					err=RPNOrdering(sub_tokens, sub_rpn_tokens, markers, unused_tokens);
					if(err!=0)break;
					parenthesis_stack.pop();
					n_data=n_data_saved;
					unused_tokens.push_back(token1);
				}
				for(std::vector<Token*>::iterator it=sub_rpn_tokens.begin(); it!=sub_rpn_tokens.end(); ++it){
					rpn_tokens.push_back(*it);
				}
				rpn_tokens.push_back(token2);
				                               
				for(std::vector<Token*>::iterator it=end_array_tokens.begin(); it!=end_array_tokens.end(); ++it){
					rpn_tokens.push_back(*it);
				}
				i=i2;
			}
			break;
		case Token::Semicolon:
			while(tmp_op_stack.size()>0){
				op=tmp_op_stack.top();
				if(op->getType()==Token::Comma||op->priority()<token->priority())break;
				rpn_tokens.push_back(op);
				n_data+=op->AddNumberOfData(parenthesis_stack.get_nComma(),last_type);
				                                                                 
				tmp_op_stack.pop();
			}
			rpn_tokens.push_back(token);
			n_data--;
			                                                                     
			break;
		default:
			assert(0);
			unused_tokens.push_back(token);
			break;
		}
		if(err){
			break;
		}
		if(type!=Token::WhiteSpaces&&type!=Token::Comment){
			hasValidLHS=(type==Token::Set||type==Token::GlobalDE);
			isAtStart=(type==Token::Unknown||type==Token::Set||
				type==Token::LP||type==Token::Func||type==Token::And||
				type==Token::Or||type==Token::NE||type==Token::EQ||
				type==Token::GT||type==Token::GE||type==Token::LT||
				type==Token::LE||type==Token::Comma||type==Token::Semicolon);
		}
		last_type=type;
	}
	                                                      
	while(tmp_op_stack.size()>0){
		op=tmp_op_stack.top();
		op_type=op->getType();
		rpn_tokens.push_back(op);
		n_data+=op->AddNumberOfData(parenthesis_stack.get_nComma(),last_type);
		                                                               
		tmp_op_stack.pop();
	}
	if(n_data==0){                                                                                           
		int len=rpn_tokens.size();
		if(len>1 ){
			token=rpn_tokens.back();
			if(token->getType() == Token::Semicolon){
				rpn_tokens.pop_back();
				unused_tokens.push_back(token);
				n_data=1;
				                                                                              
			}
		}
	}
	if(n_data==0){
		err=EXPRESSION_YIELD_NO_DATA;
		getTreeMapper()->getViewLog()->DebugString("Tokenize n_data=%d\n",n_data);
	}
	if(n_data>1){
		err=EXPRESSION_YIELD_TOO_MANY_DATA;
		getTreeMapper()->getViewLog()->DebugString("Tokenize n_data=%d\n",n_data);
	}
	return err;
}

Token**CTransform::TokenizeToRPN(const char*str,int&err_code){
	if(str[0]==0)
		return 0;
	CViewLog* view=	getTreeMapper()->getViewLog();
	if(!view->IsDebugLogEnabled())view=0;

	                                                                             
	vector<konga::Token*>tokens;
	AutoPtrDeleteVector<konga::Token> adopt_tokens(tokens);
	err_code=Tokenize(str,tokens);
    if(!err_code) etlforge::validateLocalVariables(tokens, validatedLocals);
	if(err_code==NO_TOKENS){
		if(view)view->DebugString("TokenizeToRPN: Having no tokens is OK, just return an empty array");
		Token**ts = new Token*[1];
		ts[0]=0;
		return ts;
	}

	                        
	if(err_code){
		if(view)view->DebugString("TokenizeToRPN: expression: %s,\nTokenize() failed, err_code=%d\n", str, err_code);
		return 0;
	}

	                                    
	  

	                                                             
	                            
	vector<konga::Token*>markers;
	vector<konga::Token*>unused_tokens;
	AutoPtrDeleteVector<konga::Token> adopt_markers(markers);
	AutoPtrDeleteVector<konga::Token> adopt_unused_tokens(unused_tokens);

	vector<konga::Token*>rpn_tokens;	                                             
	err_code=RPNOrdering(tokens,rpn_tokens,markers,unused_tokens);
	if(err_code){
		if(view)view->DebugString("TokenizeToRPN: expression: %s,\nRPNOrdering() failed, err_code=%d\n", str, err_code);
		unused_tokens.clear();                                                                     
		return 0;
	}
	else if(view){
		int i=0;
		for(std::vector<konga::Token*>::iterator it=rpn_tokens.begin(); it!=rpn_tokens.end(); ++it, ++i){
			konga::Token* t=*it;
			view->DebugString("%4d %s\n", i, t->GetName().c_str());
		}
	}

	                                                         
	assert(rpn_tokens.size()+unused_tokens.size()==tokens.size()+markers.size());

	const int n=(int)rpn_tokens.size();
	Token**ts=new Token*[n+1];
	for(int i=0;i<n;++i){
		ts[i]=rpn_tokens[i];
	}
	ts[n]=0;

	                                                              
	                                      
	                                         
	tokens.clear();
	markers.clear();
	return ts;
}

  
                                                                  
                                                     


                               
            
                     
 

                            
 
                   
 
  

CTransform::~CTransform()
{
	int n=(int)m_InputDEList.size();
	for(int i=0;i<n;++i){
		delete m_InputDEList.at(i);
	}
	m_InputDEList.clear();

	delete local_variable_map;

	if(m_TokenArrayList){
		for(int i=0,n=(int)m_TokenArrayList->size();i<n;++i)delete m_TokenArrayList->at(i);
		delete m_TokenArrayList;
		m_TokenArrayList = 0;
	}

	delete m_table;
}

void CTransform::createTAListItemForCrom(const char*source_expr, CROM*pAttribute, int&err)
{
	if((err=TokenizeTemplate(source_expr))!=0)
	{
		getTreeMapper()->getViewLog()->LogMessage( "    Called from CTransform::createTokenArrayListItem(\"%s\")\n", source_expr);
		return;
	}

	TokenArrayListItem* pTokenArrayListItem=new TokenArrayListItem(source_expr, retrieveTokenArrayList());
	pAttribute->setTokenArrayListItem(pTokenArrayListItem);
	m_TokenArrayList=0;

	return;
}

konga::DataElement* CTransform::Evaluate(Token*ts[], bool& bFormatApplied, int&err){
	err=0;

	                              
	if(ts==0)return new konga::DataElement();
	int n=0;
	for(;ts[n]!=0;++n){;}
	if(n==0)return new konga::DataElement();

	Token* last_token=n>0?ts[n-1]:0;
	bFormatApplied=(last_token && (
		stricmp(last_token->GetName().c_str(),"Format(")==0 ||
		stricmp(last_token->GetName().c_str(),"CVTDate(")==0 ||
		stricmp(last_token->GetName().c_str(),"FormatDate(")==0));
	return eval(ts,n,err);
}

namespace {
	const char USER_DEFINED_NEXTVAL[] = "<NEXTVAL>";
	size_t USER_DEFINED_NEXTVAL_SIZE = 9;
	const char USER_DEFINED_CURRVAL[] = "<CURRVAL>";
	size_t USER_DEFINED_CURRVAL_SIZE = 9;
	const char USER_DEFINED_SQLIDENTITY[] = "<SQLIDENTITY>";

	bool isDbPostProcessString(const KString& sResult) {
		if(sResult.IsEmpty() || sResult.GetAt(0) != '<' ) return false;

		const char* p = (const char*)sResult;
		return
			strncmp(p, USER_DEFINED_CURRVAL, USER_DEFINED_CURRVAL_SIZE) == 0 ||
			strncmp(p, USER_DEFINED_NEXTVAL, USER_DEFINED_NEXTVAL_SIZE) == 0 ||
			strcmp(p, USER_DEFINED_SQLIDENTITY) == 0;
	}
}

   
            
                                   
         
                       
                                                                                         
                         
                                                         
  
int CTransform::Evaluate_dbg(const char* expression, TALIST*ta_list, const char* sDataTypeStr, int sqlDataType, const char* sFormat, KString& sResult, CROM* pTargetAttribute)
{
	m_pTargetAttribute = pTargetAttribute;
	assert(ta_list);
	int err0=0;
	m_lastErr.Empty();
	DataElement::Type target_data_type=DataElement::get_type(sDataTypeStr, sqlDataType);

	bool debugStackWasPushed = false;
	if ( m_pDebuggee && m_pDebuggee->isDebugging() ) {
		                                                                
		const string stTargetAttributePath( m_pTargetAttribute != 0 ? (const char*)m_pTargetAttribute->get_Path() : "" );
		if ( !stTargetAttributePath.empty() ) {
			m_pDebuggee->getCallStack().push(
				new IntegrationBreakpointLocation(-1, -1, -1, 0, stTargetAttributePath)
				);
			debugStackWasPushed = true;
		}
	}
	AutoPopCallStack pop_stack( debugStackWasPushed ? m_pDebuggee : 0);
	Evaluate(expression, ta_list, target_data_type, sFormat, m_pTargetAttribute,sResult, err0);

	                                              
	if ( m_pDebuggee && m_pDebuggee->isDebugging() && m_pDebuggee->shouldStep() ) {
		IntegrationDebugBreakpointHitHelper(0, (const char*)sResult);
	}
	return err0;
}

void CTransform::Evaluate(const char* expression, TALIST*ta_list,  DataElement::Type target_data_type, const char* sFormat, CROM* pTargetAttribute,KString& sResult, int&err0){
	int err=0;
	DataElement*de=0;
	sResult.Empty();
	bool bFormatApplied=false;
	bool isUDF=pTargetAttribute ? pTargetAttribute->IsUDF() : false;

	try {
		std::unique_ptr<etlforge::javascript::Invocation> javascript;
		TokenArray* activeJavaScript=0;
		for(size_t b=0;b<ta_list->size();b++)if(ta_list->at(b)->m_javascript){
            etlforge::javascript::Limits limits;
            int heap=konga::GetOptionAsInt("JavaScript","HeapLimitMiB",64),result=konga::GetOptionAsInt("JavaScript","ResultLimitMiB",16),timeout=konga::GetOptionAsInt("JavaScript","ExecutionTimeoutMs",5000);
            if(heap<4||heap>1024||result<1||result>64||timeout<1||timeout>300000)throw CrException("JavaScript limits are outside supported configuration ranges");
            limits.heapBytes=static_cast<size_t>(heap)*1024*1024;limits.valueBytes=static_cast<size_t>(result)*1024*1024;limits.deadlineMs=timeout;
			javascript.reset(new etlforge::javascript::Invocation([this,&activeJavaScript](const std::string& name,const std::vector<etlforge::javascript::Value>& args){return callJavaScriptHost(name,args,activeJavaScript);},limits));break;
		}
		const int n=(int)ta_list->size(),len=(int)strlen(expression);
		int i0=0,i1,i2;

		                                                                                                                          
		if(n==1 && strncmp(expression,"<trans>",7)==0 && strncmp(&expression[len-8],"</trans>",8)==0){
			TokenArray*ta=ta_list->at(0);
			Token**ts=ta->m_ts;
			if(ts==0||*ts==0){
				err0=TARGET_MAPPED_TO_NULL;
				return;
			}
			if(ts[1]==0){
				Token* t=ts[0];
				Token::Type token_type=t->getType();
				if(token_type==Token::IDE){
					de=t->GetDE();
					de->setToken(t);
				}
				else if(token_type==Token::Num || token_type==Token::StrConst || token_type==Token::BoolConst){
					de=t->GetDE();
				}
				else if(token_type==Token::GlobalDE){
					const char* pchName = t->GetName().c_str();
					de=getTreeMapper()->getGlobalDataElement(&pchName[1]);
					de->setName(pchName);
					de->setToken(t);
				}
				if(de){
					if(de->IsNull()){
						err0=TARGET_MAPPED_TO_NULL;
						return;
					}
					if (!bFormatApplied && !isUDF){
						konga::DataElement targetDe(*de);
						targetDe.redefineType(target_data_type, sFormat);
						sResult+=targetDe.GetString(err);
					}
					else {
						sResult+=de->GetString(err);
					}
				}
			}
		}
		if(de==0){
			for(int i=0;i<n;++i){
				TokenArray*ta=ta_list->at(i);
				err=0;
				i1=ta->m_start_pos;
				i2=ta->m_end_pos;
				if(i0<i1)sResult+=KString(&expression[i0],i1-i0);
				if(de!=0&&!de->isTokenDE())delete de;

				if(ta->m_javascript){
					activeJavaScript=ta;
					try{auto value=javascript->evaluate(ta->m_javascriptSource);if(value.type==etlforge::javascript::Value::Object)throw std::runtime_error("Use JSON.stringify to emit an object as target text");de=etlforge::javascript::toDataElement(value).release();}
					catch(const std::exception& e){size_t line=1,column=1;for(int position=0;position<ta->m_start_pos;position++){if(expression[position]=='\n'){line++;column=1;}else column++;}throw CrException(std::string("JavaScript block at template line ")+std::to_string(line)+", column "+std::to_string(column)+": "+e.what());}
				}else {de=Evaluate(ta->m_ts,bFormatApplied,err);if(javascript)javascript->refreshGlobals();}
				if(de->getError()!=0){
					err=de->getError();
					if(err==TRANSFORM_INPUT_EMPTY)err=TARGET_MAPPED_TO_NULL;
				}
				else if(de->IsNull())err=TARGET_MAPPED_TO_NULL;
				if (err)
				{
					err0=err;
					                                                                                            
					if(err != TARGET_MAPPED_TO_NULL && err != NULL_DATA_ELEMENT && err!=-1)
					{
						KString tmp1,tmp2;
						if(m_lastErr.IsEmpty()&&tmp2.LoadString(err))m_lastErr=tmp2;
						tmp1.Format("CTransform::Evaluate failed - %s in evaluating \"%s\" ", (const char*)m_lastErr,expression);
						m_lastErr=tmp1;
						getTreeMapper()->getViewLog()->LogMessage("Warning : %s\n",(const char*)tmp1);
					}
				}
				if(!m_lastErr.IsEmpty()){
					throw CrException((const char*)m_lastErr);
				}
				if(err==0){
					if (!bFormatApplied && !isUDF){
						konga::DataElement targetDe(*de);
						targetDe.redefineType(target_data_type, sFormat);
						sResult+=targetDe.GetString(err);
					}
					else {
						sResult+=de->GetString(err);
					}
				}
				i0=i2;
			}
			if(i0<len)sResult+=KString(&expression[i0]);
			if(!sResult.IsEmpty()&&err0==TARGET_MAPPED_TO_NULL)err0=0;
			if(de!=0&&!de->isTokenDE()){
				delete de;
				de=0;
			}
		}
	}
	catch(const CrException& e){
		string err;
		if ( pTargetAttribute != 0 ) {
			err.assign("Failed to evaluate target: " + pTargetAttribute->get_Path() + ".\n");
		}
		err.append(e.GetErrorString());
		m_pTreeMapper->getViewLog()->LogMessage("%s", err.c_str());
		m_lastErr = err;
		if(err0==0)err0=-1;
	}
}



DataElement* CTransform::find_de(const std::string& name){
	std::map<std::string,konga::DataElement*>::iterator it=source_de_map.find(name);
	if(it!=source_de_map.end())return it->second;
	else return 0;
}

void CTransform::ResetDE(){
	int i,n=(int)m_InputDEList.size();
	for(i=0;i<n;++i)m_InputDEList.at(i)->setError(-1);
}


int CTransform::DefineSourceDE(const char*SourceDE_str){
	int err=0;
	int k=0;
	m_record=0;
	char*buf=new char[buf_max+1];

	for(int i=0,n1=(int)m_InputDEList.size();i<n1;++i)delete m_InputDEList.at(i);
	m_InputDEList.clear();

	m_table->emptyTable();
	                                                                                                
	int n=(int)strlen(SourceDE_str);

	while(k<n){
		if(SourceDE_str[k]=='<')break;
		DataElement*de=new DataElement(SourceDE_str,k,err);
		if(err!=0){
			delete de;
			KString tmp;
			tmp.LoadString(err);
  			getTreeMapper()->getErrorLog()->MakeError(__FILE__, __LINE__, ID_CTRANSFORM_DEFINESOURCEDE_ERROR, (const char*)tmp,(const char*)KString(&SourceDE_str[k],20));
			m_lastErr += getTreeMapper()->getErrorLog()->GetLastErr();
			getTreeMapper()->getViewLog()->LogMessage( "ERROR: %s\n", (const char*)m_lastErr);

			break;
		}
		m_table->put(de->getName(),de);
		m_InputDEList.push_back(de);
	}
	if(k<n){
		StringTokenizer tk(&SourceDE_str[k],"<,>");
		int i=0,pos=0,k1=0,k2=0;
		DataElement*de;
		while(tk.NextToken()){
			const char* token=tk.token();
			switch(token[0]){
			case '<':
				pos=0;
				k1=-1;
				k2=0;
				break;
			case ',':
				pos=1;
				break;
			case '>':
				de=m_InputDEList[i++];
				de->setPosBegin(k1);
				de->setPosLength(k2);
				pos=2;
				break;
			default:
				switch(pos){
				case 0:
					k1=atoi(token);
					break;
				case 1:
					k2=atoi(token);
				}
			}
		}
	}
	delete[]buf;

	return err;
}


int CTransform::TokenizeTemplate(const char*strTemplate){
    validatedLocals.clear();
	int err=0;
	int i0=0,i1=0,i2=0;
	const char *t;
	if(strTemplate==0){
		                              
		return 0;
	}
	m_TokenArrayList=new TALIST();
	if(strstr(strTemplate,"<javascript>")!=0){
		try{
			for(const auto& block:etlforge::javascript::parseBlocks(strTemplate)){
				if(block.kind==etlforge::javascript::Block::JavaScript){
#if !ETLFORGE_NATIVE_JAVASCRIPT_ENABLED
                    m_lastErr="Native JavaScript blocks are disabled in this build";return -1;
#endif
                    if(!konga::GetOptionAsBoolean("JavaScript","Enabled",true)){m_lastErr="Native JavaScript blocks are disabled in this instance";return -1;}
					std::string source(strTemplate+block.body,block.close-block.body);
					etlforge::javascript::Invocation::validate(source);
					std::unique_ptr<TokenArray> item(new TokenArray((int)block.start,(int)block.end,source));
					for(const auto& literal:etlforge::javascript::sourcePathLiterals(source)){
						etlforge::javascript::Invocation decoder;
						std::string path=decoder.evaluate("SetScriptResult("+literal+");").text;
						if(path.find('\0')!=std::string::npos||!Token::isSourceDE(source_de_set,path,isFlatSource))throw std::runtime_error("Unknown JavaScript source path: "+path);
						if(item->m_javascriptSources.count(path))continue;
						Token** binding=TokenizeToRPN(path.c_str(),err);
						if(err||!binding)throw std::runtime_error("Cannot bind JavaScript source path: "+path);
						item->m_javascriptSources[path]=binding;
						if(!binding[0]||binding[1]||binding[0]->getType()!=Token::IDE)throw std::runtime_error("GetSource accepts a source path, not an expression");
					}
					m_TokenArrayList->push_back(item.release());
				}else m_TokenArrayList->push_back(new TokenArray(this,strTemplate,(int)block.start,(int)block.end,err));
			}
			return err;
		}catch(const std::exception& e){m_lastErr=e.what();return -1;}
	}
	while((t=strstr(&strTemplate[i0],TRANS))!=0){
		i2=find_END_KEY_position(strTemplate, t-strTemplate);
		if(i2>=0){
			i1=(int)(t-strTemplate);                                    
			i2+=strlen(END_TRANS);                                   
			m_TokenArrayList->push_back(new TokenArray(this,strTemplate,i1,i2,err));
			i0=i2;
			if(err&&err!=NO_TOKENS)break;
			err=0;
		} else {
			i1=i0, i2=(int)strlen(strTemplate);
			err=TRANSFORM_UNBALANCED_TRANS_TAG;
			break;
		}
	}
	if(err){
		KString tmp1,tmp2;
		tmp1.LoadString(err);
		if(i1>=i2)
		{
			getTreeMapper()->getErrorLog()->MakeError(__FILE__, __LINE__, ID_CTRANSFORM_DEFINESOURCEDE_ERROR1, (const char*)tmp1);
		}
		else 
		{
			getTreeMapper()->getErrorLog()->MakeError(__FILE__, __LINE__, ID_CTRANSFORM_DEFINESOURCEDE_ERROR2, (const char*)tmp1,(const char*)KString(&strTemplate[i1],i2-i1));
		}
		m_lastErr += getTreeMapper()->getErrorLog()->GetLastErr();

		getTreeMapper()->getViewLog()->LogMessage( "ERROR: %s\n",(const char*)tmp2);
	}

	return err;
}

TokenArray::TokenArray(int start_pos,int end_pos,const std::string& source)
 :m_start_pos(start_pos),m_end_pos(end_pos),m_ts(new Token*[1]()),m_javascript(true),m_javascriptSource(source){}

TokenArray::TokenArray(CTransform*detrans,const char*buf,int start_pos,int end_pos,int&err)
{
	KString tmp(&buf[start_pos+strlen(TRANS)],(end_pos-strlen(END_TRANS))-(start_pos+strlen(TRANS)));
	m_ts=detrans->TokenizeToRPN(tmp,err);
	if(err!=0&&err!=NO_TOKENS){
		if(m_ts){
			for(int i=0;m_ts[i]!=0;++i)delete m_ts[i];
			delete[]m_ts;
			m_ts=0;
		}
		throw CrException(std::string("Tokenize the following expression failed,\n")+(const char*)tmp);
	}
	m_start_pos=start_pos;
	m_end_pos=end_pos;
}

TokenArray::~TokenArray()
{
	for(auto& item:m_javascriptSources){if(item.second){for(size_t i=0;item.second[i];i++)delete item.second[i];delete[] item.second;}}
	if(m_ts){
		for(int i=0; m_ts[i]!=0; ++i)delete m_ts[i];
		delete[] m_ts;
		m_ts=0;
	}
}

etlforge::javascript::Value CTransform::callJavaScriptHost(const std::string& name,const std::vector<etlforge::javascript::Value>& args,TokenArray* block){
    using namespace etlforge::javascript;
    if(name=="__ConnectorOverlay"){
        if(args.size()!=3||args[0].type!=Value::String||args[1].type!=Value::Array||args[2].type!=Value::Object)throw std::runtime_error("Invalid native connector overlay");
        const std::string& function=args[0].text;
        if(function!="ReadFile"&&function!="WriteFile"&&function!="DbExecute"&&function!="DbLookup"&&function!="WriteToOperationLog")throw std::runtime_error("Connector overlay function is not allowed");
        struct Restore {
            struct Entry{DataElement* target;std::unique_ptr<DataElement> before;};
            std::vector<Entry> entries;
            ~Restore(){for(auto& entry:entries){try{*entry.target=*entry.before;}catch(...){}}}
        } restore;
        NativeValueBudget budget;
        for(const auto& item:args[2].object){
            DataElement* target=getTreeMapper()->getGlobalDataElement(item.first.c_str());
            auto before=cloneNative(target,0,budget);before->setName(item.first.c_str());
            restore.entries.push_back(Restore::Entry{target,std::move(before)});
            auto value=toDataElement(item.second);*target=*value;target->setName(item.first.c_str());
        }
        Value packet;packet.type=Value::Object;
        packet.object["result"]=callJavaScriptHost(function,args[1].array,block);
        Value changes;changes.type=Value::Object;
        for(const auto& item:args[2].object){Value after=fromDataElement(getTreeMapper()->getGlobalDataElement(item.first.c_str()));if(!equalValues(after,item.second))changes.object.emplace(item.first,std::move(after));}
        packet.object["changes"]=std::move(changes);return packet;
    }
    if(name=="GetSource"){
        if(!block||args.size()!=1||args[0].type!=Value::String)throw std::runtime_error("GetSource requires a literal source path");
        auto found=block->m_javascriptSources.find(args[0].text);
        if(found==block->m_javascriptSources.end())throw std::runtime_error("JavaScript source path was not bound when the mapping was compiled");
        int err=0;bool formatted=false;DataElement* value=Evaluate(found->second,formatted,err);
        std::unique_ptr<DataElement> owned(value&&!value->isTokenDE()?value:0);
        if(err||!value)throw std::runtime_error("Cannot read current JavaScript source row");return fromDataElement(value);
    }
    if(name=="GetVar"||name=="SetVar"){
        if(args.empty()||args[0].type!=Value::String||args[0].text.empty()||args[0].text.find('\0')!=std::string::npos)throw std::runtime_error("Invalid operation global name");
        const std::string& key=args[0].text;
        DataElement* de=getTreeMapper()->getGlobalDataElement(key.c_str());
        if(name=="SetVar"){auto value=toDataElement(args.at(1));*de=*value;de->setName(key.c_str());return args[1];}
        return fromDataElement(de);
    }
    const char* function=0;
    if(name=="ReadFile")function="ReadFile";
    else if(name=="WriteFile")function="WriteFile";
    else if(name=="DbExecute")function="DbExecute";
    else if(name=="DbLookup")function="DBLookup";
    else if(name=="WriteToOperationLog")function="WriteToOperationLog";
    else if(name=="FlushFile")function="FlushFile";
    else throw std::runtime_error("JavaScript host function is not permitted");
    if(args.empty()||args[0].type!=Value::String)throw std::runtime_error("First host argument must be a string");
    std::string expression=function;expression+='(';
    for(size_t i=0;i<args.size();i++){if(i)expression+=',';expression+='0';}expression+=')';
    int err=0;Token** tokens=TokenizeToRPN(expression.c_str(),err);
    struct Cleanup{Token** p;~Cleanup(){if(p){for(size_t i=0;p[i];i++)delete p[i];delete[] p;}}} cleanup{tokens};
    if(err||!tokens)throw std::runtime_error("Cannot prepare native JavaScript host call");
    size_t argument=0;
    for(size_t i=0;tokens[i];i++)if(tokens[i]->getType()==Token::Num){
        auto value=toDataElement(args.at(argument++));value->setToken(tokens[i]);delete tokens[i]->GetDE();tokens[i]->setDataElement(value.release());
    }
    if(argument!=args.size())throw std::runtime_error("Native JavaScript host argument mismatch");
    bool formatted=false;DataElement* result=Evaluate(tokens,formatted,err);
    std::unique_ptr<DataElement> owned(result&&!result->isTokenDE()?result:0);
    if(err||!result)throw std::runtime_error("Native host call failed: "+std::string((const char*)m_lastErr));
    Value value=fromDataElement(result);
    if(name=="WriteFile"){
        std::vector<Value> flush{args[0]};if(args.size()==3)flush.push_back(args[2]);
        callJavaScriptHost("FlushFile",flush,block);
    }
    return value;
}

TokenArrayListItem::~TokenArrayListItem(){
	for(TALIST::iterator it=talist->begin(); it!=talist->end(); ++it){
		if(*it!=0)delete *it;
	}
	delete talist;
}


                                                                          
                                                                             
                                                                             
                                                                        
void CTransform::HandleMarker(int& i, const int n, Token*& t, Token*ts[],
				DataElement*& de, DataElement*& de1, DataElement*& de2,
				std::stack<DataElement*>& st,
				konga::AutoDeleteStack<DataElement>& tmp, int& err)
{
	int level=0;
	std::vector<int> ind;
	ind.push_back(i);
	for(i++;i<n&&(t=ts[i])!=0;++i){
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
		case Token::LBRACE:
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
					                                                       
					if ( de->getType()==DataElement::t_collection ) {
						de1=de;
						const int i0=ind.at(0)+1;
						de2=eval(&ts[i0],ind.at(1)-i0,err);
						if(!de2->isTokenDE())tmp.push(de2);
						tmp.push(de=new DataElement(de1,de2,t));
					}
					                                                        
					else if( (t->getType()==Token::And && de->GetBool(err)) ||
							 (t->getType()==Token::Or && !de->GetBool(err)) )
					{
						const int i0=ind.at(0)+1;
						de=eval(&ts[i0],ind.at(1)-i0,err);
						if(!de->isTokenDE())tmp.push(de);
					}
					break;
				case Token::LBRACE:
					createArray(t, de, ts, ind, tmp);
					break;
				default:
					ExecFunction(t,de,ts,ind,tmp);
					break;
			};
			st.push(de);
			break;
		}
	}
}

namespace {
	int findNumberOfTokenToEval(Token*ts[]){
		int iStartArray=1;
		Token*t;
		for(int i=0; (t=ts[i])!=0; ++i){
			switch(t->getType()){
				case Token::StartArray: iStartArray++; break;
				case Token::EndArray:   iStartArray--; break;
			}
			if(iStartArray==0)return i;
		}
		throw konga::EvalException("Can't find the balanced closing token EndArray.");
	}
}

DataElement* CTransform::eval(Token*ts[],const int n,int&err, konga::DataElement*de0){
	konga::AutoDeleteStack<DataElement> tmp;
	std::stack<DataElement*>st;
	if(de0)st.push(de0);
	Token*t=0;
	DataElement*de=0,*de1=0,*de2=0;
	try{
		for(int i=0;i<n&&(t=ts[i])!=0;++i){
			if(m_pDebuggee && m_pDebuggee->isDebugging() && m_pDebuggee->shouldStep() ){
				                                                                                           
				long lScriptId = -1;
				string stTargetAttributePath;
				if ( m_pTargetAttribute ) {
					stTargetAttributePath = (const char*)m_pTargetAttribute->get_Path();
				}
				else {
					lScriptId = m_lScriptId;
				}

				const IntegrationBreakpointLocation* const pPreviousBreakLocation = static_cast<IntegrationBreakpointLocation*>(m_pDebuggee->getPreviousBreakLocation());
				if ( pPreviousBreakLocation != 0 ) {
					const bool shouldBreak = pPreviousBreakLocation->getLineNumber() != t->line_number() &&
						t->getType() != Token::Marker &&
						t->getType() != Token::Semicolon &&
						pPreviousBreakLocation->getAttributePath() == stTargetAttributePath &&
						pPreviousBreakLocation->getScriptId() == lScriptId;

					if(shouldBreak){
						IntegrationDebugBreakpointHitHelper(t);
					}
				}
			}

			switch(t->getType()){
			case Token::IDE:
				de=t->GetDE();
				de->setToken(t);
				st.push(de);
				break;
			case Token::LocalDE:
				de=getLocalVariable(t->m_stName);
                if(!t->localWriteTarget && !local_variable_map->isAssigned(de) && !etlforge::isScriptArgument(t->m_stName))
                    throw EvalException("Local variable " + t->m_stName + " has not been assigned before use " + t->source_location());
                st.push(de);
				break;
			case Token::GlobalDE:
				                              
				{
				const char* pchName = t->GetName().c_str();
				de=getTreeMapper()->getGlobalDataElement(&pchName[1]);
				de->setName(pchName);
				de->setToken(t);
				st.push(de);
				}
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
				HandleMarker(i,n,t,ts,de,de1,de2,st,tmp,err);
				break;
			case Token::Add:
			case Token::Sub:
			case Token::Mul:
			case Token::Div:
			case Token::NE:
			case Token::EQ:
			case Token::GT:
			case Token::GE:
			case Token::LT:
			case Token::LE:
			case Token::POW:
			case Token::And:
			case Token::Or:
				de2=st.top();
				st.pop();
				de1=st.top();
				st.pop();
				tmp.push(de=new DataElement(de1,de2,t));
				st.push(de);
				break;
			case Token::Not:
			case Token::Neg:
				de=st.top();
				st.pop();
				tmp.push(de=new DataElement(de,t));
				st.push(de);
				break;
			case Token::PrePP:
			case Token::PreMM:
				de=st.top();
				de->doToken(t);
				break;
			case Token::PostPP:
			case Token::PostMM:
				de=st.top();
				tmp.push(de1=new DataElement(*de));
				de->doToken(t);
				st.pop();
				st.push(de1);
				break;
			case Token::PSet:
			case Token::MSet:
				{
					de2=st.top();
					st.pop();
					if ( st.empty() ) {
						throw EvalException("Assignment to " + string(de2->getName()) + " is missing the left hand operator.");
					}
					de1=st.top();
					tmp.push(de2=new DataElement(de1,de2,t));
					st.push(de2);
				}
			case Token::Set:
				{
					de2=st.top();
					st.pop();
					if ( st.empty() ) {
						throw EvalException("Assignment to " + string(de2->getName()) + " is missing the left hand operator.");
					}
					de1=st.top();
					if(de1!=de2){
						const std::string name_saved(de1->getName());
						de1->set(de2);
						de1->setName(name_saved.c_str());
					}
                    local_variable_map->markAssigned(de1);
				}
				break;
			case Token::RBRACKET:
				{
					de2=st.top();
					st.pop();
					de1=st.top();
					st.pop();
					if(de1->isCollection()){
						const int index=de2->GetInteger(err);
						if(err){
							throw EvalException("Evaluation of array index error.");
						}
						if ( index < -1 ) {
							throw EvalException("Invalid array index: " + konga::toString(index));
						}
						if ( de1->IsNull() ) {
							de1->initCollection();
						}
						if( de1->isCollection() ){
							const size_t size = de1->getDECollectionSize();
							const size_t uindex = index;
							if ( index == -1 || uindex == size ) {
								                                                
								DataElement* de = new DataElement();
								de1->addToCollection(de);
								st.push(de);
							}
							else {
								st.push(de1->getDECollectionOf(index));
							}
						}
					}
					else if(de1->isMap()){
						const std::string key( (const char*)de2->GetString(err) );
						if(err){
							throw EvalException("Evaluation of map key error.");
						}
						std::map<std::string, konga::DataElement*>* map=de1->GetMap(err);
						std::map<std::string, konga::DataElement*>::iterator it=map->find(key);
						if(it==map->end()){
							de=new konga::DataElement();                                    
							(*map)[key]=de;
						}
						else {
							de=it->second;
						}
						st.push(de);
					}
					else {
						throw EvalException("[] operator applied to a " + string(DataElement::get_type_name(de1->getType()))
							+ " data element.");
					}
				}
				break;
			case Token::StartArray:                                          
				{
					de1=st.top();
					st.pop();
					int nTokens=findNumberOfTokenToEval(&ts[i+1]);
					if(de1->isCollection()){
						de2=new DataElement();
						de2->initCollection();
						for(int j=0, m=de1->getDECollectionSize(); j<m; ++j){
							DataElement* de3=de1->getDECollectionOf(j);
							if(de3==0)de3=new DataElement();
							de3=eval(&ts[i+1], nTokens, err, de3);
							if(err){
								throw EvalException("Evaluation of array error.");
							}
							de2->addToCollection(new DataElement(*de3));
						}
						st.push(de2);
					}
					else {
						st.push(new DataElement());
					}
					i+=nTokens+1;
				}
				break;
			case Token::Func:                        
			case Token::Comma:
			case Token::LBRACE:
			default:
				string sErr("Unknown syntax at the end of the expression. ");
				if ( t ) sErr += t->source_location();
				throw konga::EvalException(sErr);
			}
		}
		if(err!=0||(de!=0&&de->getType()== konga::DataElement::t_unknown)&&!de->IsNull()){
			throw konga::EvalException("Encounter a data element with unknown data type.");
		}
		if(st.size()!=1){
			throw konga::EvalException("Expression does not evaluate to a data element.");
		} else if(err==0){
			de=st.top();
			st.pop();
		}
	} catch(DbException& e){
		std::string err_msg=sourceTargetDescription()+e.GetPrettyErrorMessage();
		throw CrException(err_msg);
	} catch(const CrFatalException& e){
		throw e;
	} catch(const EvalException& e){
		string sErr(sourceTargetDescription());
		if ( !sErr.empty() ) sErr += "\n";
		sErr += "An error occurred while evaluating the expression";
		if ( t ) {
			sErr += " " + t->source_location();
		}
		else {
			sErr += ".";
		}
		sErr += "\nReason: " + e.GetErrorString();
		throw CrException(sErr);

	} catch(const CrException& e){
		throw e;
	}
	if ( de ){
		if(de->isTokenDE())return de;                        
		return new DataElement(*de);
	}
	else {
		throw InternalErrorException(sourceTargetDescription()+"\nEvaluating the expression produced no result. This is a bug, please report it.");
	}
}

int CTransform::IntegrationDebugBreakpointHitHelper(Token* pCurrentToken, const string& stResult) const {
	if ( !(m_pDebuggee != 0 && m_pDebuggee->isDebugging() && m_pDebuggee->isObservingBreakpoints()) ) return 0;

	long lScriptId = -1;
	string stTargetAttributePath, stGeneratorPath, stInstanceString;
	if ( m_pTrans_targetLoopNode != 0 ) {
		stInstanceString = m_pTrans_targetLoopNode->getInstanceString(stGeneratorPath);
	}
	if(m_pTargetAttribute) {
		stTargetAttributePath = (const char*)m_pTargetAttribute->get_Path();
	}
	else
		lScriptId = m_lScriptId;

	                                    
	std::set<DataElement*> source_de;
	if ( m_pTargetAttribute ) {
		TALIST*ta_list=m_pTargetAttribute->getTAList();
		for(size_t ita=0, nta=ta_list->size(); ita<nta; ++ita){
			konga::Token** ta=ta_list->at(ita)->m_ts;
			konga::Token* t;
			for(size_t it=0; (t=ta[it])!=0; ++it){
				if(t->getType()==konga::Token::IDE){
					source_de.insert(t->GetDE());
				}
			}
		}
	}

	const long line_number = (pCurrentToken != 0) ? pCurrentToken->line_number() : -1;
	return konga::IntegrationDebugBreakpointHit(m_pDebuggee, stTargetAttributePath, lScriptId, line_number, stInstanceString, stGeneratorPath, source_de, stResult);
}

bool CTransform::useInstanceResolvingFunction(TALIST* talist){
	for(int i=0, n=talist->size(); i<n; ++i){
		for(const auto& source:talist->at(i)->m_javascriptSources)if(source.first.find('#')!=std::string::npos)return true;
		konga::Token*token, **ts=talist->at(i)->m_ts;
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

ParenthesisStack::ParenthesisStack(CTransform* pTrans0,
	int n_data_start0, 
	std::vector<konga::Token*>&rpn_tokens0,
	std::vector<konga::Token*>&unused_tokens0)
	:	pTrans(pTrans0),
		token_start(0),
		n_comma(0), 
		n_data_start(n_data_start0),
		rpn_tokens(rpn_tokens0),
		unused_tokens(unused_tokens0){}

void ParenthesisStack::push(konga::Token* token_start1){
	token_start_stack.push(token_start);
	n_comma_stack.push(n_comma);
	n_data_start_stack.push(n_data_start);
	token_start=token_start1;
	n_comma=0;
	n_data_start = pTrans->n_data;
}

void ParenthesisStack::pop(){
	token_start=token_start_stack.top();
	n_comma=n_comma_stack.top();
	n_data_start=n_data_start_stack.top();
	token_start_stack.pop();
	n_comma_stack.pop();
	n_data_start_stack.pop();
}

int ParenthesisStack::checkSemicolon(konga::Token* token_end, konga::Token::Type lastId)
{
	if(token_start==0)return SYNTAX_ERROR_MISPLACED_RIGHT_PARENTHESIS;
	konga::Token::Type end_type=token_end->getType(), start_type=token_start->getType();
	assert(end_type==konga::Token::Comma || end_type==konga::Token::RP || end_type==konga::Token::RBRACE);
	if(end_type==konga::Token::Comma){
		if(start_type!=konga::Token::Func && start_type!=konga::Token::LBRACE)
			return SYNTAX_ERROR_MISPLACED_COMMA;
	}
	else if(end_type==konga::Token::RBRACE && start_type!=konga::Token::LBRACE){
		return SYNTAX_ERROR_MISPLACED_RBRACE;
	}
	                                                         
	if(end_type==konga::Token::RP && start_type==konga::Token::Func){
		int argc = n_comma + ((lastId!=konga::Token::Func)? 1: 0);
		int argc0 = 1-token_start->AddNumberOfData(n_comma, lastId);
		if(argc!=argc0){
			return SYNTAX_ERROR_FUNCTION_ARGUMENT_COUNT;
		}
	}
	              
	if(lastId==konga::Token::Func)return 0;
	                                 
	int nData_expected=get_expected_nData();
	if(lastId!=konga::Token::LBRACE)nData_expected++;
	if(pTrans->n_data == nData_expected)return 0;
	if(pTrans->n_data == (nData_expected-1)){
		int nRpnTokens=rpn_tokens.size();
		Token* last_token=nRpnTokens>0? rpn_tokens.back(): 0;
		if(last_token!=0 && last_token->getType()==konga::Token::Semicolon){
			                         
			rpn_tokens.pop_back();
			unused_tokens.push_back(last_token);
			pTrans->n_data++;
			return 0;
		}
		else {
			return EXPRESSION_YIELD_NO_DATA;
		}
	}
	return EXPRESSION_YIELD_TOO_MANY_DATA;
}

string CTransform::getInstanceString(string& stGeneratorPath) const {
	string stInstanceStr;
	stGeneratorPath.clear();
	if( m_pTargetNode == 0 || m_pTargetNode->m_Generator == 0 ) return string("");
	CROM* const generator=m_pTargetNode->m_Generator;

	TreeMapper*pTreeMapper=getTreeMapper();
	if(pTreeMapper->IsFlatSource()){
		if(pTreeMapper->IsNormalizationTransformation()) {
			stInstanceStr=konga::toString(getSourceCRData()->GetInstanceNumber());
		}
		else {
			if(pTreeMapper->doStreaming())
				stInstanceStr=konga::toString(getInstanceNumber());
			else {
				CRData*source=getSourceCRData();
				stGeneratorPath=(const char*)generator->get_Path();
				stInstanceStr=source?source->getInstanceListing(): "";
			}
		}
	}
	else if(pTreeMapper->doStreaming()){
		stGeneratorPath=(const char*)generator->get_Path();
		stInstanceStr=generator->getInstanceListing();
	}
	else {
		CRData*source=getSourceCRData();
		stGeneratorPath=(const char*)generator->get_Path();
		stInstanceStr=source? source->getInstanceListing(): "";
	}
	return stInstanceStr;
}

string CTransform::sourceTargetDescription(){
	string str;
	if(m_pTargetAttribute){
		str.append("[Target]:").append((const char*)m_pTargetAttribute->get_Path()).append("\n");
		str.append("Expr:").append(m_pTargetAttribute->getSourceExpr()).append("\n");
		if(m_sourceData && m_sourceData->GetDTDNode()!=0){
			str.append("[Source]:").append((const char*)m_sourceData->GetDTDNode()->get_Path()).append(" ").append(m_sourceData->getInstanceListing()).append("\n");
		}
		else {
			str.append("[Source]: none\n");
		}
	}
	return str;
}

DataElement* CTransform::getLocalVariable(const string& deName){
	return local_variable_map->get(deName);
}

void CTransform::push_LocalVariables(){
	std::auto_ptr<LocalVariables> next(new LocalVariables());
    local_variable_map_stack.push(local_variable_map);
    local_variable_map=next.release();
}

void CTransform::pop_LocalVariables(){
	assert(local_variable_map_stack.size()>0);
	delete local_variable_map;
	local_variable_map=local_variable_map_stack.top();
	local_variable_map_stack.pop();
}
