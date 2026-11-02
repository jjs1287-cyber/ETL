/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#include "PortDefs.h"	                                                                                 
#include <string.h>
#include <cassert>

#include "ErrorResolver.h"
#include "CrException.h"
#include "FileContents.h"


using namespace konga;

namespace {
	const size_t buffer_length=10000;
	const int nbuf = 2;
}

FileContents::FileContents(const std::string& fileName0):
	current_buf(0), current_offset(0), current_length(0), current_end_pos(0),
	start_pos(0), length(0), current(-1), buf(0),
	in(0), size0(0), fileName(fileName0)
{
	if(fileName0.empty())return;
	initBuf();
	open();
	size();
}

void FileContents::initBuf() {
	                                                                          
	assert(start_pos==0);
	if(start_pos!=0)return;
	start_pos = new long long[nbuf];
	length = new long long[nbuf];
	buf = new char*[nbuf];
	for(int i=0; i < nbuf; ++i){
		buf[i] = new char[buffer_length+1];
		start_pos[i] = 0;
		length[i] = 0;
	}
}

void FileContents::init(){
	close();
	current_buf = 0;
	current_offset = 0;
	current_length = 0;
	current_end_pos = 0;
	current = -1;
	size0 = 0;
	for(int i=0; i < nbuf; ++i){
		start_pos[i] = 0;
		length[i] = 0;
	}
	open();
	size();
}

FileContents::~FileContents()
{
	if(in)fclose(in);
	for(int i=0; i<nbuf; ++i){
		if(start_pos[i]>=0)delete[] buf[i];
	}
	delete[] buf;
	delete[] length;
	delete[] start_pos;
}

bool FileContents::load(size_t i){
	if(in==0){
		open();
		if(in==0)return true;
	}
	current_offset = (i/buffer_length)*buffer_length;
	fseek(in, current_offset, SEEK_SET);
	current_length = fread(buf[current], 1, buffer_length, in);
	current_end_pos = current_offset+current_length;
	start_pos[current] = current_offset;
	length[current] = current_length;
	current_buf = buf[current];
	current_buf[current_length] = 0;
	return current_length < buffer_length;
}

bool FileContents::findBuffer(size_t i){
	if(i>=current_offset && i < current_end_pos)return true;
	if(current == -1){
		current = 0;
		return false;
	}
	for(int ibuf=1; ibuf<nbuf ; ibuf++){
		current=(current+1)%nbuf;
		if(i>=start_pos[current] && i<(start_pos[current]+length[current])){
			current_offset = start_pos[current];
			current_length = length[current];
			current_end_pos = current_offset+current_length;
			current_buf = buf[current];
			return true;
		}
	}
	return false;
}

char FileContents::at(size_t i){
	if(!findBuffer(i))load(i);
	return current_buf[i-current_offset];
}


size_t FileContents::size(){
	if(size0==0 && !fileName.empty()){
		fseek(in, 0, SEEK_END);
		size0 = ftell(in);
	}
	return size0;
}

const std::string& FileContents::getDicStr(size_t pos, size_t len){
	std::string s=getStr(pos, len);
	return getDicStr(s);
}
const std::string& FileContents::getDicStr(const std::string& str){
	std::map<std::string, bool>::const_iterator it=dic.find(str);
	if(it==dic.end()){
		dic[str]=true;
		it=dic.find(str);
	}
	if(it==dic.end())throw konga::CrException("Can't enter \""+str+"\" to dictionary.");
	return it->first;
}
std::string FileContents::getStr(size_t pos, size_t len){
	open();
	if(!findBuffer(pos)){
		load(pos);
	}
	size_t end_pos = pos+len;
	if(end_pos > size0)end_pos = size0;
	if(end_pos <= current_end_pos){
		return std::string(&current_buf[pos-current_offset], end_pos-pos);
	}
	std::string tmp="";
	while(end_pos >= current_end_pos){
		tmp += std::string(&current_buf[pos-current_offset], current_end_pos-pos);
		pos = current_end_pos;
		if(!findBuffer(pos)){
			bool eof=load(pos);
			if(eof)break;
		}
	}
	if(end_pos > pos){
		tmp += std::string(&current_buf[pos-current_offset], end_pos-pos);
	}
	return tmp;
}

size_t FileContents::findNextTokenLength(size_t begin_pos, size_t end_pos, const char* terminators)
{
	size_t pos=begin_pos;
	bool eof=false;
	while(pos < end_pos){
		if(!findBuffer(pos)){
			eof=load(pos);
		}
		pos += ::strcspn(&current_buf[pos-current_offset], terminators);
		if(eof || pos < current_end_pos)break;
	}
	if(pos > end_pos)pos = end_pos;
	return pos-begin_pos;
}

void FileContents::fetchToken(char*token, size_t pos, size_t len)
{
	if(!findBuffer(pos)){
		load(pos);
	}
	size_t end_pos = pos+len;
	if(end_pos > size0)end_pos = size0;
	if(end_pos <= current_end_pos){
		strncpy(token, &current_buf[pos-current_offset], end_pos-pos);
	}
	else {
		while(end_pos >= current_end_pos){
			int n = current_end_pos-pos;
			strncpy(token, &current_buf[pos-current_offset], n);
			token += n;
			pos = current_end_pos;
			if(!findBuffer(pos)){
				bool eof=load(pos);
				if(eof)break;
			}
		}
		if(end_pos > pos){
			strncpy(token, &current_buf[pos-current_offset], end_pos-pos);
		}
	}
}

void FileContents::open(){
	if(in==0 && !fileName.empty()){
		in=fopen(fileName.c_str(), "rb");
		if(in==0) {
			const long lErr = GetLastError();
			throw CrException("Cannot open input file: \""+fileName+"\".\n" + ErrorResolver(lErr).Resolve(lErr) + "\n [class FileContents]");
		}
	}
}

void FileContents::close(){
	if(in) {
		fclose(in);
		in=0;
	}
}

size_t FileContents::findCsvEndOfField(size_t pos, char delimiter){
	char c=at(pos);
	if(c=='"'){
		do {
			for(++pos; (c=at(pos))!=0 && c!='"'; ++pos);
		} while(c!=0 && (c=at(++pos))=='"');
	}
	while(!(c==delimiter || c=='\r'|| c=='\n' || c==0)){
		c=at(++pos);
	}
	return pos;
}

size_t FileContents::findNormalEndOfField(size_t pos, char qualifier, char delimiter){
	char c=at(pos);
	if(c==qualifier){
		for(++pos; (c=at(pos))!=0 && c!=qualifier; ++pos){
			if(c=='\\')pos++;
		}
	}
	while(!(c==delimiter || c=='\r'|| c=='\n' || c==0)){
		c=at(++pos);
	}
	return pos;
}

                 
std::string FileContents::readCsvData(size_t& pos, char delimiter, bool& bIsNull){
	konga::StringBuilder sb(20);
	char c;
	if((c=at(pos))=='"'){
		bIsNull=false;
		while(1) {
			for(++pos; (c=at(pos))!=0 && c!='"'; ++pos){
				sb.append(c);
			}
			if(c && (c=at(++pos))=='"')sb.append(c);
			else break;
		}
		while(!((c=at(pos))==delimiter || c=='\r'|| c=='\n' || c==0)){
			++pos;
		}
	}
	else {
		size_t pos_save(pos);
		while(!((c=at(pos))==delimiter || c=='\r'|| c=='\n' || c==0)){
			sb.append(c);
			++pos;
		}
		bIsNull=pos_save==pos;
	}
	return sb.toString();
}



                     
std::string FileContents::readNormalData(size_t& pos, char qualifier, char delimiter){
	konga::StringBuilder sb(20);
	char c=at(pos);
	if(c==qualifier){
		for(++pos; (c=at(pos))!=0 && c!=qualifier; ++pos){
			if(c=='\\'){
				pos++;
				appendEscapedChar(pos, sb);
			}
			else {
				sb.append(c);
			}
		}
		while(!(c==delimiter || c=='\r'|| c=='\n' || c==0)){
			c=at(++pos);
		}
	}
	else {
		for(;!((c=at(pos))==delimiter || c=='\r'|| c=='\n' || c==0); ++pos){
			if(c=='\\'){
				pos++;
				appendEscapedChar(pos, sb);
			}
			else {
				sb.append(c);
			}
		}
	}
	return sb.toString();
}

void FileContents::appendEscapedChar(size_t& pos, konga::StringBuilder& sb){
	if(isdigit(at(pos)) && (pos+2)<size0 && isdigit(at(pos+1)) && isdigit(at(pos+2))){
		std::string s=getStr(pos, 3);
		int k;
		sscanf(s.c_str(), "%3o", &k);
		char c(k);
		sb.append(c);
		pos+=2;
	} else {
		char c=at(pos);
		switch(c){
			case 'b': c='\b'; break;	            
			case 'f': c='\f'; break;	            
			case 'n': c='\n'; break;	          
			case 'r': c='\r'; break;	                  
			case 't': c='\t'; break;	                 
			case 'v': c='\v'; break;	               
			case '"':					               
			case '\'':					               
			case '\\':					             
				sb.append(c);
				break;					                                  
			default:
				sb.append('\\');
				sb.append(c);
				break;
		}
	}
}

size_t FileContents::getLineNumber(size_t pos)
{
	int count=1;
	for(size_t i=0;i<pos;++i){
		if(at(i)=='\n')++count;
	}
	return count;
} 


