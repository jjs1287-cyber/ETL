/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */

#include <konga/Utils/PortDefs.h>		                                    
#include <konga/Utils/KString.h>
#include <konga/Utils/StringUtils.h>

#include "RetVal.h"
#include "DTDTokenize.h"

DTDTokenize::DTDTokenize(const char*str,const char*terminators)
{
	init(str,terminators);
}

void DTDTokenize::init(const char*str, const char*terminators){
	str0=str;
	m_str=str0.c_str();
	m_terminators=terminators;
	m_ExtractFlag = false;
	m_k=0;
	token=0;
	m_nmax=0;
	m_TokenLen=0;
	m_NextTokenPos=0;
	m_link=0;
	m_name=0;
	m_c[0]=0;
	m_c[1]=0;
}

DTDTokenize::~DTDTokenize(){
	if(token)delete[]token;
	while(m_link){
		TokenizeBucket*saved=m_link->m_link;
		delete m_link;
		m_link=saved;
	}
	while(string_saved.empty()==false){
		const char*str=string_saved.top();
		if(str)delete[](char*)str;
		string_saved.pop();
	}
}

namespace {
	const int TABLE_HEADER_LENGTH=(int)strlen(TABLE_HEADER);
}

bool DTDTokenize::NextToken()
{
	if(m_c[0]!=0){
		m_c[0]=0;
		return true;
	}
	if(m_str[m_k]==0&&m_link==0){
		if(token==0)token=new char[m_nmax+1];
		token[0]=0;
		return false;
	}
	if(m_str[m_k]==0){
		m_k=m_link->m_k;
		m_str=m_link->m_str;
		m_name=m_link->m_name;
		TokenizeBucket*saved=m_link;
		m_link=m_link->m_link;
		delete saved;
	}
	int n,n1=0;
	if (strnicmp(&m_str[m_k], TABLE_HEADER, TABLE_HEADER_LENGTH) == 0)
	{
		n=TABLE_HEADER_LENGTH;
	}
	else if (true == m_ExtractFlag)
	{
		n=m_TokenLen;
		n1=m_NextTokenPos;
	} else {
		n=(int)strcspn(&m_str[m_k],m_terminators);
		if(n==0)n=1;
	}
	if(n>m_nmax){
		if(m_nmax<15)m_nmax=15;
		while(n>m_nmax)m_nmax=m_nmax*2;
		if(token)delete[]token;
		token=new char[m_nmax+1];
	}
	                            
	strncpy(token,&m_str[m_k],n);
	token[n]=0;

	m_k+=n+n1;
	return true;
}

void DTDTokenize::push_macro(const char*name,const char*str,const char*next_str)
{
	m_link=new TokenizeBucket(m_name,m_str,(int)(next_str-m_str),m_link);
	m_k=0;
	m_name=konga::strsave(name);
	m_str=konga::strsave(str);
	string_saved.push(m_name);
	string_saved.push(m_str);
}


void DTDTokenize::SetExtract(bool ExtractFlag, int TokenLen, int NextTokenPos)
{
	m_ExtractFlag = ExtractFlag;
	m_TokenLen = TokenLen;
	m_NextTokenPos = NextTokenPos;
}
