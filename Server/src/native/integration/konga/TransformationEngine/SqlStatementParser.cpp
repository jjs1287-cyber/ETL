/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#include <string.h>			                    

#ifndef WIN32
#include <konga/Utils/PortDefs.h>		                                      
#endif
#include <konga/Utils/CrException.h>

#include "SqlStatementParser.h"

namespace {
	bool is_whitespace(char c){
		return c==' ' || c=='\t' || c=='\r' || c=='\n';
	}
	std::string trim(const std::string& str){
		int pos1=0, pos2=str.size();
		for(pos1=0; pos1<pos2 && is_whitespace(str.at(pos1)); ++pos1);
		for(; pos2>0 && is_whitespace(str.at(pos2-1)); --pos2);
		return str.substr(pos1, pos2-pos1);
	}

}
	
SqlStatementParser::SqlStatementParser(const std::string& sql) :
	select(0), from(0), where(0), order(0), group(0), end(0)
{
	m_Sql=sql;
	konga::StringTokenizer tk(m_Sql.c_str()," \"][()`',.\t\r\n;");
	int pos0=0, pos1=0;
	char c;
	while(tk.NextToken()){
		std::string token(tk.token());
		pos0=pos1;
		pos1=tk.getNextPos();
		switch(c=token.at(0)){
		case ' ':
		case '\r':
		case '\n':
		case '\t':
			{
				const char*tmp;
				while((tmp=tk.peek_next_token())!=0 && is_whitespace(*tmp)){
					tk.NextToken();
					++pos1;
				}
			}
			m_tokens.push_back(new SqlToken(pos0, pos1, t_ws));
			break;
		case '[':
			c=']';
		case '"':
		case '\'':
		case '`':
			                    
			findMatchingToken(tk, c);
			pos1=tk.getNextPos();
			m_tokens.push_back(new SqlToken(pos0, pos1, (c=='\''? t_value: t_name)));
			break;
		case '.':
			m_tokens.push_back(new SqlToken(pos0, pos1, t_dot));
			break;
		case ',':
			m_tokens.push_back(new SqlToken(pos0, pos1, t_comma));
			break;
		case '(':
			findMatchingParenthesis(tk);
			pos1=tk.getNextPos();
			m_tokens.push_back(new SqlToken(pos0,pos1, t_phrase));
			break;
		case ')':
		case ']':
			throw konga::CrException("Misplaced token "+token);
		case ';':
			m_tokens.push_back(end=new SqlToken(pos0, pos1, t_end));
			break;
		default:
			if(strcmp(token.c_str(),"SELECT")==0){
				if(select!=0)throw konga::CrException("More than one SELECT key words");
				m_tokens.push_back(select=new SqlToken(pos0, pos1, t_select));
			}
			else if(strcmp(token.c_str(),"FROM")==0){
				if(select==0)throw konga::CrException("FROM key word does not follow SELECT key word.");
				if(from!=0)throw konga::CrException("More than one FROM key words");
				m_tokens.push_back(from=new SqlToken(pos0, pos1, t_from));
			}
			else if(strcmp(token.c_str(),"WHERE")==0){
				if(select==0 || from==0)throw konga::CrException("This is not a valid SELECT statement.");
				if(where!=0)throw konga::CrException("More than one WHERE key words");
				if(order!=0 || group!=0)throw konga::CrException("WHERE key word should be placed before ORDER OR GROUP.");
				m_tokens.push_back(where=new SqlToken(pos0, pos1, t_where));
			}
			else if(strcmp(token.c_str(),"ORDER")==0){
				findToken(tk, "BY");
				pos1=tk.getNextPos();
				if(select==0 || from==0)throw konga::CrException("This is not a valid SELECT statement.");
				if(order!=0)throw konga::CrException("More than one order key words");
				m_tokens.push_back(order=new SqlToken(pos0, pos1, t_order));
			}
			else if(strcmp(token.c_str(),"GROUP")==0){
				findToken(tk, "BY");
				pos1=tk.getNextPos();
				if(select==0 || from==0)throw konga::CrException("This is not a valid SELECT statement.");
				m_tokens.push_back(group=new SqlToken(pos0, pos1, t_group));
			}
			else {
				if(isdigit(c)){
					m_tokens.push_back(new SqlToken(pos0, pos1, t_value));
				}
				else {
					m_tokens.push_back(new SqlToken(pos0, pos1, t_name));
				}
			}
			break;
		}
		if(end!=0)break;
	}
}

SqlStatementParser::~SqlStatementParser(){
	if(select!=0)delete select;
	if(from!=0)delete from;
	if(where!=0)delete where;
	if(order!=0)delete order;
	if(group!=0)delete group;
	if(end!=0)delete end;
	for(std::vector<SqlToken*>::iterator it=m_tokens.begin(); it!=m_tokens.end(); ++it){
		delete *it;
	}
}

std::string SqlStatementParser::insertNoneFilter(){
	if(where != 0){
		int pos=where->endPos;
		return m_Sql.substr(0, pos)+" (1=2) AND"+m_Sql.substr(pos);
	}
	else {
		int pos=getInsrtFilterPosition();
		char c=m_Sql.at(pos-1);
		std::string s=m_Sql.substr(0, pos);
		if(c!=' ' && c!='\t' && c!='\r' && c!='\n')s+=" ";
		s+="WHERE (1=2)";
		if(pos<m_Sql.size()){
			if(end!=0 && end->beginPos!=pos)s+=" ";
			s+=m_Sql.substr(pos);
		}
		return s;
	}
}

int SqlStatementParser::getInsrtFilterPosition(){
	int pos=m_Sql.size();
	if(group!=0 && group->beginPos<pos)pos=group->beginPos;
	if(order!=0 && order->beginPos<pos)pos=order->beginPos;
	if(end!=0 && end->beginPos<pos)pos=end->beginPos;
	return pos;
}

std::string SqlStatementParser::getFilter(){
	if(where==0)return "";
	std::string filter = m_Sql.substr(where->endPos, getInsrtFilterPosition()-where->endPos);
	return trim(filter);
}

std::string SqlStatementParser::replaceFilter(const std::string& newFilter0){
	std::string newFilter=trim(newFilter0);
	if(!newFilter.empty() && strnicmp(newFilter.c_str(), "WHERE", 5)!=0)newFilter="WHERE "+newFilter;
	int pos1=getInsrtFilterPosition();
	std::string newSql;
	if(where!=0){
		newSql=m_Sql.substr(0, where->beginPos)+newFilter;
	}
	else {
		newSql=m_Sql.substr(0, pos1);
		if(pos1>0 && newSql.at(pos1-1)!=' ')newSql+=" ";
		newSql+=newFilter;
	}
	if(pos1==m_Sql.size())return newSql;
	if(end!=0 && end->beginPos!=pos1)newSql+=" ";
	return newSql+m_Sql.substr(pos1);
}

void SqlStatementParser::findToken(konga::StringTokenizer& tk, std::string token){
	const char*compare=token.c_str();
	while(tk.NextToken()){
		if(stricmp(tk.token(),compare)==0)break;
	}
}

void SqlStatementParser::findMatchingToken(konga::StringTokenizer& tk, char c){
	while(tk.NextToken()){
		if(*tk.token()==c)break;
	}
}

void SqlStatementParser::findMatchingParenthesis(konga::StringTokenizer& tk){
	while(tk.NextToken()){
		char c=*tk.token();
		if(c==')')break;
		if(c=='\''|| c=='"'|| c=='`'|| c=='['){
			if(c=='[')c=']';
			findMatchingToken(tk, c);
		}
		else if(c=='('){
			findMatchingParenthesis(tk);
		}
	}
}
