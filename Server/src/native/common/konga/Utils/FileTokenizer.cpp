/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#include <string.h>

#include "FileTokenizer.h"

using namespace konga;

FileTokenizer::FileTokenizer(konga::FileContents*fc0,const char*terminators) :
	fc(fc0), m_terminators(terminators), m_k(0), m_end_pos(fc->size()),
	m_nmax(16), m_token(new char[m_nmax+1])
{
	if(!fc->getFileName().empty())init();
}

FileTokenizer::FileTokenizer(konga::FileContents* fc0, size_t begin_pos, size_t end_pos, const char*terminators) :
	fc(fc0), m_terminators(terminators), m_k(begin_pos), m_end_pos(end_pos),
	m_nmax(16), m_token(new char[m_nmax+1])
{
	if(!fc->getFileName().empty())init();
}

FileTokenizer::~FileTokenizer()
{
	delete[]m_token;
}

void FileTokenizer::init() {
	m_token[0]=0;
	m_span_terminator[0]=0;
	m_span_terminator[1]=0;
}

bool FileTokenizer::NextToken()
{
	if(m_span_terminator[0]!=0){
		m_span_terminator[0]=0;
		return true;
	}
	if(m_k>=m_end_pos){
		m_token[0]=0;
		return false;
	}

	size_t n=fc->findNextTokenLength(m_k, m_end_pos, m_terminators);
	if(n==0)n=1;
	if((n+m_k)>m_end_pos)n=m_end_pos-m_k;
	if(n>m_nmax){
		while(n>m_nmax)m_nmax*=2;
		delete[]m_token;
		m_token=new char[m_nmax+1];
	}
	fc->fetchToken(m_token, m_k, n);
	m_token[n]=0;
	m_k+=n;
	return true;
}

const char* FileTokenizer::peek_next_token()
{
	if(m_k>=m_end_pos){
		return 0;
	}
	size_t n=fc->findNextTokenLength(m_k, m_end_pos, m_terminators);
	if(n == 0)n = 1;
	if((n+m_k) > m_end_pos)n = m_end_pos-m_k;
	m_peek_next_token=std::string(fc->getStr(m_k, n));
	return m_peek_next_token.c_str();
}


void FileTokenizer::SpanTo(char c, bool useBackSlashEscape){
	m_span_terminator[0]=c;
	if(fc->at(m_k)==0){
		m_token[0]=0;
		return;
	}
	size_t n=0;
	if(c=='"'){
		std::string tmp;
		char c1;
		for(size_t i=0, i0=0;(c1=fc->at(m_k+i))!=0;i++){
			if(c1=='\\' && useBackSlashEscape){
				i++;
				if(fc->at(m_k+i)==0)break;
			}
			else if(c1==c){
				if(fc->at(m_k+i+1)==c){
					i++;
					tmp+=fc->getStr(m_k+i0, i-i0);
					i0=i+1;
				}
				else {
					if(i0==0){
						n=i;
						break;
					}
					tmp+=fc->getStr(m_k+i0, i-i0);
					n=tmp.length();
					if(n>m_nmax){
						while(n>m_nmax)m_nmax*=2;
						delete[]m_token;
						m_token=new char[m_nmax+1];
					}
					if(n>0)strncpy(m_token,tmp.c_str(),n);
					m_token[n]=0;
					m_k+=i+1;
					return;
				}
			}
		}
	}
	else {
		n=fc->findNextTokenLength(m_k, m_end_pos, m_span_terminator);
	}
	if(n>m_nmax){
		while(n>m_nmax)m_nmax*=2;
		delete[]m_token;
		m_token=new char[m_nmax+1];
	}
	if(n>0)fc->fetchToken(m_token, m_k, n);
	m_token[n]=0;
	m_k+=n+1;
}

void FileTokenizer::spanTo_CSV_String(){
	m_span_terminator[0]='"';
	if(fc->at(m_k)==0){
		m_token[0]=0;
		return;
	}
	unsigned char cc;
	size_t n;
	for(n=0;(cc=fc->at(m_k+n))!=0;n++){
		if(cc=='"'){
			if(fc->at(m_k+n+1)=='"')n++;
			else break;
		}
	}

	if(n>m_nmax){
		while(n>m_nmax)m_nmax*=2;
		delete[]m_token;
		m_token=new char[m_nmax+1];
	}
	size_t j=0;
	if(n>0){
		for(size_t i=0; i<n; ++i){
			if((m_token[j++]=fc->at(m_k+i))=='"')i++;
		}
	}
	m_token[j]=0;
	m_k+=n+1;
}


bool FileTokenizer::StepBack(){
	char c;
	if(m_k==0)return false;
	size_t n=m_k, j, m=strlen(m_terminators);
	for(size_t i=n-1;i>=0;i--){
		c=fc->at(i);
		for(j=0;j<m;j++){
			if(c==m_terminators[j]){
				if(i<(n-1))i++;
				m_k=i;
				return true;
			}
		}
	}
	m_k=0;
	return n>0;
}


