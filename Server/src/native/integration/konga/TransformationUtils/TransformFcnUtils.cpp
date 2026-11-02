/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */

#include <vector>
#include <memory>		                
#include <boost/scoped_array.hpp>

#include <konga/Utils/StringUtils.h>
#include <konga/Utils/CrException.h>

#include "TransformFcnUtils.h"

#include <konga/CharacterEncodingConversion/EncodingStringUtils.h>

using namespace konga;
using std::string;
using std::vector;

KString konga::lpad(const KString& str,int ncolumn){
	if(ncolumn<0)
		return konga::rpad(str,-ncolumn);
	boost::scoped_array<char>s(new char[ncolumn+1]);
	int l=(int)str.GetLength();
	if(ncolumn<l)l=ncolumn;
	int i0=ncolumn-l;
	for(int i=0;i<i0;i++)s[i]=' ';
	strncpy(&s[i0],(const char*)str,l);
	s[ncolumn]=0;
	return KString(s.get(),ncolumn);
}

KString konga::rpad(const KString& str,int ncolumn){
	if(ncolumn<0)
		return konga::lpad(str,-ncolumn);
	boost::scoped_array<char>s(new char[ncolumn+1]);
	int l=(int)str.GetLength();
	if(ncolumn<l)l=ncolumn;
	strncpy(s.get(),(const char*)str,l);
	for(int i=l;i<ncolumn;i++)s[i]=' ';
	s[ncolumn]=0;
	return KString(s.get(),ncolumn);
}

int konga::CountSubString(const char*str,const char*sub_str){
	if(str==0||str[0]==0||sub_str==0||sub_str[0]==0)
		return 0;
	const char*str0,*str1;
	int n=0;
	for(str0=str-1;true;n++,str0=str1){
		str1=strstr(str0+1,sub_str);
		if(str1==0)break;
	}
	return n;
}

int konga::IndexOfStringUtf8Character(const char* utf8_str,const char* utf8_sub_str,int n){
	                  
	int byteindex=IndexOfString(utf8_str,utf8_sub_str,n);
	if (byteindex<1) return byteindex;
	int bytelength = byteindex+1;
	string utf8substring(utf8_str,bytelength);
	int characterindex = EncodingStringUtils::Utf8CharacterSize(utf8substring);
	characterindex--;
	return characterindex;

}

int konga::IndexOfString(const char*str,const char*sub_str,int n){
	if(sub_str==0||sub_str[0]==0){
		if(n>0)return 0;
		else return strlen(str);
	}
	if(n==0)n=1;
	string s(str);
	string::size_type last;
	int i=abs(n);
	if(n<0){
		last=s.length();
		while(true){
			last=s.rfind(sub_str,last);
			if(last==string::npos)return -1;
			i--;
			if(i>0){
				if(last==0)return -1;
				last--;
			}
			else return (int)last;
		}
	}
	else {
		last=0;
		while(true){
			last=s.find(sub_str,last);
			if(last==string::npos)return -1;
			i--;
			if(i>0)last++;
			else return (int)last;
		}
	}
}

const char* konga::find_st(const char*str,int k){
	while(k>0&&str[k]!=' ')k--;
	if(str[k]==' ')k++;
	return &str[k];
}

DataElement* konga::SplitStringReturnDataElementArray(const std::string& s, const std::string& delim) {
	if ( delim.empty() )
		throw CrException("Illegal argument. Delimiter is empty.");
	vector<string> arr = konga::SplitString(s, delim);
	std::auto_ptr<DataElement> deArr( new DataElement(DataElement::t_collection) );
	for( vector<string>::const_iterator cit = arr.begin(); cit != arr.end(); ++cit ) {
		deArr->addToCollection( new DataElement(DataElement::t_string, cit->c_str()) );
	}	
	return deArr.release();
}
