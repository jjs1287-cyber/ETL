/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#include <cassert>

#include "KString.h"
#include "CsvUtils.h"
#include "ErrorResolver.h"
#include "AutoPtrCleanup.h"	                    
#include "AutoFileCleanup.h"
#include "FileUtils.h"
#include "util.h"
#include "CrException.h"
#include "PortDefs.h"			                                                      

#define RECORD_SIZE (64*1024)

using namespace konga;
using std::string;

int konga::get_length(char*buf){
	int len=(int)strlen(buf);
	while(buf[len-1]=='\n'||buf[len-1]=='\r'){
		buf[len-1]=0;
		len--;
	}
	return len;
}

   
                                                                                                        
                                                             
                                                                                 
    
int konga::getCSVFieldText(const char*sBuffer,int iStart, string& text, char cDelimiter){
	bool inside_qualifier=iStart<0;
	if(!inside_qualifier)text="";
	int pos=inside_qualifier?0:iStart, last_pos=pos;
	if(!inside_qualifier){
		if(sBuffer[pos]=='"'){
			pos++;
			last_pos=pos;
			inside_qualifier=true;
		}
	}
	char c;
	for(;(c=sBuffer[pos]);pos++){
		if(inside_qualifier){
			if(c=='"'){
				if(sBuffer[pos+1]==c){
					if(pos>last_pos)text+=string(&sBuffer[last_pos],pos-last_pos);
					text+='"';
					pos++;
					last_pos=pos+1;
				}
				else {
					if(pos>last_pos)text+=string(&sBuffer[last_pos],pos-last_pos);
					last_pos=pos+1;
					inside_qualifier=false;
				}
			}
		} else {
			if(c==cDelimiter){
				if(pos>last_pos)text+=string(&sBuffer[last_pos],pos-last_pos);
				return pos+1;
			}
		}
	}
	if(pos>last_pos)text+=string(&sBuffer[last_pos],pos-last_pos);
	if(!inside_qualifier)return pos;
	if(sBuffer[pos-1]!='\n'&&sBuffer[pos-1]!='\r')text+="\n";
	return -1;
}

void konga::CSV_escape(string& s){
	string::size_type pos=0;
	while((pos=s.find('"',pos))!=string::npos){
		s.insert(pos,1,'"');
		pos+=2;
	}
}

void konga::CSV_restore(std::string& s){
	if(s.empty())return;
	const string::size_type n=s.size();
	if(s.at(0)=='"' && s.at(n-1)=='"'){
		s=s.substr(1,n-2);
		string::size_type pos1=0,pos2;
		while((pos2=s.find('"',pos1))!=string::npos){
			if(s.at(pos2+1)=='"'){
				s=s.substr(0,pos2)+s.substr(pos2+1);
			}
			pos1=pos2+1;
		}
	}
}

namespace {
	void qualifyCsvStringImpl(string&s) {
		const char specialChars[] ="\"\r";
		string::size_type startPos=0,foundPos;
		while((foundPos=s.find_first_of(specialChars,startPos))!=string::npos){
			if ( s.at(foundPos) == '\"' ) {
				s.insert(foundPos, 1, '\"');
				startPos = foundPos + 2;
			}
			else {
				if ( foundPos == s.size() - 1 )		                                                   
					break;
				if ( s.at(foundPos+1) == '\n' ) {	                                                          
					s.erase(foundPos, 1);
					startPos=foundPos;
				}
				else								                                                               
					startPos=foundPos + 1;
			}
		}
		s.insert(s.size(), 1, '\"').insert(0, 1, '\"');
	}
}

namespace {
	bool requireQualifier(const char*s, char delimiter){
		assert(s);
		static char special_chars[4]={'\r','\n',0,0};
		                                
		if(s[0]=='"')return true;
		                                                  
		special_chars[2]=delimiter;
		int pos=strcspn(s,special_chars);
		return s[pos]!=0;
	}
}

void konga::qualifyCsvString(string& s, char delimiter, bool bForceQuote) {
	if ( bForceQuote || requireQualifier(s.c_str(), delimiter) ) {
		qualifyCsvStringImpl(s);
	}
}

void konga::qualifyCsvStringK(KString& s, char delimiter, bool bForceQuote) {
	if ( bForceQuote || requireQualifier((const char*)s, delimiter) ) {
		string tmp((const char*)s);
		qualifyCsvStringImpl(tmp);
		s=tmp;
	}
}

namespace {
	bool CSV_requireQualifier(const char*s, const char* delimiters){
		std::string special_chars("\r\n");
		special_chars+=delimiters;
		                                
		if(s[0]=='"')return true;
		                                                  
		int pos=strcspn(s,special_chars.c_str());
		return s[pos]!=0;
	}
}

void konga::qualifyCsvString(std::string& s, const char* delimiters, bool bForceQuote) {
	if ( bForceQuote || CSV_requireQualifier(s.c_str(), delimiters) ) {
		qualifyCsvStringImpl(s);
	}
}

string konga::readCsvData(FILE*in, char*buf, const int buf_len, int& len, int& pos, bool& bIsNull, const unsigned char delimiter, const char* nullValue, string*pRecord){
	string text;
	const string sNullValue(nullValue==0 ? "<null>" : nullValue);
	bool bIsQualified = buf[pos]=='"';
	pos=konga::getCSVFieldText(buf, pos, text, delimiter);
	while(pos==-1){
		if(fgets(buf, buf_len, in)){
			if(pRecord) {
				*pRecord+=buf;
			}
			len=konga::get_length(buf);
			pos=konga::getCSVFieldText(buf, pos, text, delimiter);
		}
		else {
			bIsNull= !bIsQualified && text==sNullValue;
			pos=-1;
			return text;
		}
	}
	bIsNull= !bIsQualified && (text==sNullValue || text=="");
	if(pos==0)len=0;
	return text;
}

void konga::writeCsvData(FILE*out, const char*data, char delimiter, bool qualified){
	if(requireQualifier(data,delimiter)||qualified){
		string s(data);
		qualifyCsvStringImpl(s);
		fprintf(out,"%s",s.c_str());
	}
	else {
		fprintf(out,"%s",data);
	}
}

bool konga::fileHasNullChars(const string& fileName){	
	FILE* in = fopen(fileName.c_str(), "rb");
	if ( in == 0 ) {
		const long lErr = GetLastError();
		throw CrException("Failed to open the file '" + fileName + "' when checking for null characters. Reason: " +
			ErrorResolver(lErr).Resolve(lErr));
	}
	konga::AutoFileHandle adopt_in(in);
	const int buf_max = RECORD_SIZE;
	int buf_len = buf_max;
	char*buf0;
	konga::AutoArrayDelete<char> buf( buf0=new char[buf_len+1] );
	size_t len;
	while((len=fread(buf0,1, buf_len, in))>0){
		buf0[len]=0;
		for(size_t pos=0; pos<len; ++pos){
			if(buf0[pos]==0)return true;
		}
	}
	return false;
}

                                                                        
bool konga::removeNullCharsFromFile(const string& fileName){	
	FILE* in = fopen(fileName.c_str(), "rb");
	if ( in == 0 ) {
		const long lErr = GetLastError();
		throw CrException("Failed to open the file '" + fileName + "' when checking for null characters. Reason: " +
			ErrorResolver(lErr).Resolve(lErr));
	}
	konga::AutoFileHandle adopt_in(in);
	const int buf_max = RECORD_SIZE;
	int buf_len = buf_max;
	char* buf0;
	konga::AutoArrayDelete<char> buf( buf0=new char[buf_len+1] );
	size_t len;

	bool hasZeroByteChars=false;
	while(!hasZeroByteChars && (len=fread(buf0,1, buf_len, in))>0){
		buf0[len]=0;
		for(size_t pos=0; pos<len; ++pos){
			if(buf0[pos]==0){
				hasZeroByteChars=true;
				break;
			}
		}
	}
	if(!hasZeroByteChars)return false;
	rewind(in);

	std::string tmp;
	for(int i=fileName.size()-1; i>=0; --i){
		unsigned char c=fileName.at(i);
		if(c=='\\' || c=='/'){
			tmp=fileName.substr(0, i+1);
			break;
		}
	}
	std::string guid(konga::GetGUIDString());
	tmp+=guid;
	FILE*out=fopen(tmp.c_str(), "wb");
	if(out == 0){
		const long lErr = GetLastError();
		throw CrException("Failed to open the tem file '" + tmp + "'. Reason: " +
			ErrorResolver(lErr).Resolve(lErr));
	}
	konga::AutoFileHandle adopt_out(out);
	while((len=fread(buf0,1,buf_len,in))>0){
		buf0[len]=0;
		for(size_t start=0, end; start<len; start=end+1){
			for(end=start; buf0[end]; ++end);
			if(end>start){
				fwrite(&buf0[start],1,end-start,out);
			}
		}
	}
	adopt_in.Close();
	adopt_out.Close();
	konga::kMoveFile(tmp, fileName, true);
	return true;
}
