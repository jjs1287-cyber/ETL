/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#include "StringBuilder.h"
#include "StringUtils.h"

namespace konga {

StringBuilder::StringBuilder(size_t n){
	if(n==0)length=20;
	else length=n;
	buf=new char[length];
	pos=0;
}

StringBuilder::~StringBuilder(){
	delete[] buf;
}

void StringBuilder::reset(){
	pos=0;
}

void StringBuilder::expand(size_t add){
	char*buf_saved=buf;
	buf=new char[length+add];
	strncpy(buf, buf_saved, pos);
	delete[]buf_saved;
	length+=add;
}

StringBuilder& StringBuilder::append(char c){
	if(pos+1>=length){
		expand(length);
	}
	buf[pos++]=c;
	return *this;
}

StringBuilder& StringBuilder::append(const std::string& s){
	size_t n=s.size();
	if((pos+n) >= length){
		size_t pos_=pos+n, length_=length*2;
		while(pos_ > length_)length_+=length_;
		expand(length_-length);
	}
	strncpy(&buf[pos], s.c_str(), n);
	pos+=n;
	return *this;
}

StringBuilder& StringBuilder::append(const char* s){
	size_t n=strlen(s);
	if((pos+n) >= length){
		size_t pos_=pos+n, length_=length*2;
		while(pos_>length_)length_+=length_;
		expand(length_-length);
	}
	strncpy(&buf[pos], s, n);
	pos+=n;
	return *this;
}

StringBuilder& StringBuilder::append(const KString& s){
	size_t n=s.GetLength();
	if((pos+n) >= length){
		size_t pos_=pos+n, length_=length*2;
		while(pos_>length_)length_+=length_;
		expand(length_-length);
	}
	strncpy(&buf[pos], (const char*)s, n);
	pos+=n;
	return *this;
}

StringBuilder& StringBuilder::append(int i){
	std::string s(konga::toString(i));
	return append(s);
}

StringBuilder& StringBuilder::append(long l){
	std::string s(konga::toString(l));
	return append(s);
}

StringBuilder& StringBuilder::append(long long ll){
	std::string s(konga::toString(ll));
	return append(s);
}

StringBuilder& StringBuilder::append(double ff){
	std::string s(konga::toString(ff));
	return append(s);
}

StringBuilder& StringBuilder::append(float f){
	std::string s(konga::toString(f));
	return append(s);
}

StringBuilder& StringBuilder::append(size_t l){
	std::string s(konga::toString(l));
	return append(s);
}

std::string StringBuilder::toString(){
	return std::string(buf, pos);
}

size_t StringBuilder::size(){return pos;}

char* StringBuilder::getCurrentPointer(){
	if(pos>=length){
		expand(10);
	}
	buf[pos]=0;
	return buf;
}

void StringBuilder::write(FILE*out){
	for(size_t i=0; i<pos; ++i)fputc(buf[i], out);
}


}                       

