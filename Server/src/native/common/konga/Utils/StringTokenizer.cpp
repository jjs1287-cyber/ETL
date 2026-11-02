/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */

#include <string.h>

#include "StringTokenizer.h"

using namespace konga;

StringTokenizer::StringTokenizer(const char*str,const char*terminators)
{
	m_str=str;
	m_terminators=terminators;
	m_k=0;
	m_length=strlen(str);
	m_nmax=16;
	m_token=new char[m_nmax+1];
	m_token[0]=0;
	m_c[0]=0;
	m_c[1]=0;
}

StringTokenizer::StringTokenizer(const char*str, int length, const char*terminators)
{
	m_str=str;
	m_terminators=terminators;
	m_k=0;
	m_length=length;
	m_nmax=16;
	m_token=new char[m_nmax+1];
	m_token[0]=0;
	m_c[0]=0;
	m_c[1]=0;
}


StringTokenizer::~StringTokenizer()
{
	delete[]m_token;
}

bool StringTokenizer::NextToken()
{
	if(m_c[0]!=0){
		m_c[0]=0;
		return true;
	}
	if(m_k>=m_length){
		m_token[0]=0;
		return false;
	}

	int n=strcspn(&m_str[m_k],m_terminators);
	if(n==0)n=1;
	if((n+m_k)>m_length)n=m_length-m_k;
	if(n>m_nmax){
		while(n>m_nmax)m_nmax*=2;
		delete[]m_token;
		m_token=new char[m_nmax+1];
	}
	strncpy(m_token,&m_str[m_k],n);
	m_token[n]=0;
	m_k+=n;
	return true;
}

const char* StringTokenizer::getStringAtCurrentPosition(){
	unsigned int n=strlen(m_token);
	return m_str+m_k-n;
}

const char* StringTokenizer::peek_next_token()
{
	if(m_k>=m_length){
		return 0;
	}
	int n=strcspn(&m_str[m_k], m_terminators);
	if(n == 0)n = 1;
	if((n+m_k) > m_length)n = m_length-m_k;
	m_peek_next_token=std::string(&m_str[m_k],n);
	return m_peek_next_token.c_str();
}


void StringTokenizer::SpanTo(char c, bool useBackSlashEscape){
	m_c[0]=c;
	if(m_str[m_k]==0){
		m_token[0]=0;
		return;
	}
	int n=0;
	if(c=='"'||c=='\''){
		std::string tmp;
		char c1;
		for(int i=0, i0=0;(c1=m_str[m_k+i])!=0;i++){
			if(c1=='\\' && useBackSlashEscape){
				i++;
				if(m_str[m_k+i]==0)break;
			}
			else if(c1==c){
				if(m_str[m_k+i+1]==c){
					i++;
					tmp+=std::string(&m_str[m_k+i0],(i-i0));
					i0=i+1;
				}
				else {
					if(i0==0){
						n=i;
						break;
					}
					tmp+=std::string(&m_str[m_k+i0],(i-i0));
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
		n=strcspn(&m_str[m_k],m_c);
	}
	if(n>m_nmax){
		while(n>m_nmax)m_nmax*=2;
		delete[]m_token;
		m_token=new char[m_nmax+1];
	}
	if(n>0)strncpy(m_token,&m_str[m_k],n);
	m_token[n]=0;
	m_k+=n+1;
}

void StringTokenizer::spanTo_CSV_String(){
	m_c[0]='"';
	if(m_str[m_k]==0){
		m_token[0]=0;
		return;
	}
	unsigned char cc;
	int n;
	for(n=0;(cc=m_str[m_k+n])!=0;n++){
		if(cc=='"'){
			if(m_str[m_k+n+1]=='"')n++;
			else break;
		}
	}

	if(n>m_nmax){
		while(n>m_nmax)m_nmax*=2;
		delete[]m_token;
		m_token=new char[m_nmax+1];
	}
	int j=0;
	if(n>0){
		for(int i=0; i<n; ++i){
			if((m_token[j++]=m_str[m_k+i])=='"')i++;
		}
	}
	m_token[j]=0;
	m_k+=n+1;
}


bool StringTokenizer::StepBack(){
	char c;
	int n=m_k,j,m=strlen(m_terminators);
	for(int i=n-1;i>=0;i--){
		c=m_str[i];
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
