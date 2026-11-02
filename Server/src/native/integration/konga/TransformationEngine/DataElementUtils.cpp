/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#include <boost/scoped_array.hpp>

#include <konga/Utils/CrException.h>
#include <konga/Utils/CsvUtils.h>
#include <konga/Utils/StringUtils.h>

#include <konga/CharacterEncodingConversion/Utf8Conv.h>

#include <konga/DataElement/DataElement.h>

#include <konga/TransformationUtils/Definitions.h>

#include "resource.h"
#include "CROMData.h"
#include "DataElementUtils.h"
#include "SystemVariableDefs.h"

using namespace konga;
using namespace std;

int GetNumEscapedChars(const string& s){
	int ex=0;
	for(int i=0,n=(int)s.size();i<n;i++){
		if(s.at(i)=='\\'){
			i++;
			ex++;
		}
	}
	return ex;
}

string TrimAndRestoreEscapedChars(const string& s0, const char qual){
	string s(s0);
	restoreEscapedCharacters(s);
	TrimString(s, qual);
	return s;
}

int GetNumCharsToEscape(const string& s, const char qual){
	int ex=0;
	char c;
	for(int i=0;(c=s[i])!=0;i++){
		if(c=='\r'||c=='\n'||c==qual)ex++;
	}
	return ex;
}

bool RequireEscape(const string&s0, const char qual){
	char c;
	for(int i=0, n=(int)s0.size(); i<n; i++){
		switch(c=s0.at(i))
		{
		case '\b': case '\f': case '\n': case '\r':
		case '\t': case '\v': case '\\': return true;
		default:
			if(c==qual)return true;
		}
	}
	return false;
}

string EscapeString(const string& s0, const char qual){
	int n=(int)s0.size(),j=0;
	char*s=new char[2*n+1];
	boost::scoped_array<char> adopt_s(s);
	for(int i=0; i<n; i++){
		char c=s0.at(i);
		switch(c)
		{
		case '\b': s[j++]='\\'; s[j++]='b'; break;
		case '\f': s[j++]='\\'; s[j++]='f'; break;
		case '\n': s[j++]='\\'; s[j++]='n'; break;
		case '\r': s[j++]='\\'; s[j++]='r'; break;
		case '\t': s[j++]='\\'; s[j++]='t'; break;
		case '\v': s[j++]='\\'; s[j++]='v'; break;
		case '\\': s[j++]='\\'; s[j++]='\\'; break;
		default:
			{
				if(c==qual)s[j++]='\\';
				s[j++]=c; break;
			}
		}
	}
	s[j]=0;
	return string(s);
}

string readDelimitedText(const char*buf, int&k, DataElement::Type type, char str_qual, char num_qual, char delimiter, bool&bIsNull, int&err)
{
	err=0;
	bIsNull=false;
	string s;

	if(delimiter==0){
		throw konga::CrException("Calling readDelimitedText() with delimiter==0.");
	}
	int k1=k,k2=0,kk1=0,kk2=0;                                                              
	k2=k1+strcspn(&buf[k1],delimiter);
	char qual=(type==DataElement::t_string||type==DataElement::t_date)?str_qual:num_qual;
	if(qual){
		kk1=k1+strcspn(&buf[k1],qual);
		k=k2;
		if(kk1>k||buf[kk1]==0){                   
			kk1=k1;
			kk2=k2;
			k=k2;
		} else {                     
			kk1++;                
			char c;
			                                                                     
			                                                                        
			for(kk2=kk1;(c=buf[kk2])!=0;kk2++){
				if(c==qual)break;
				if(c=='\\')kk2++;
			}
			if(buf[kk2]==0){
				err=TRANSFORM_STR_DELIMITER_MISSING;
				k=k2+1;
				return s;
			}
		}
		if(k<kk2)k=kk2+strcspn(&buf[kk2],delimiter);
	} else {
		kk1=k1;
		kk2=k2;
		k=k2;
	}
	if(buf[k]==delimiter)k++;
	if(kk2>kk1){
		s=string(&buf[kk1],(kk2-kk1));
	} else {
		err=TRANSFORM_INPUT_EMPTY;
		return s;
	}

	if(GetNumEscapedChars(s)>0){
		s=TrimAndRestoreEscapedChars(s,str_qual);
	}
	return s;
}


string readFixedFieldText(const char*buf, const int len, int pos_begin, int pos_len, bool& bIsNull, int& err)
{
	err=0;
	bIsNull=false;

	if(pos_begin<0||pos_len<=0){                    
		throw konga::CrException("Calling readFixedFieldText() with pos_begin<0 or pos_len<=0");
	}
	if(pos_begin>=len){
		err=TRANSFORM_INPUT_FIXFIELD_LEN_ERROR;
		return "";
	}

	string s(&buf[pos_begin],pos_len);

	if (s==DB_NULL_VALUE){
		bIsNull=true;
		s="";
	}
	else {
		s=konga::trim(s);
	}
	return s;
}

                                                       
void DataElementRead(DataElement& de, FILE* in, char* buf, const int buf_len, int&len, int& k, char str_qual, char num_qual, char delimiter, bool isDatabase, bool bTrim, std::string*pRecord, konga::Utf8Conv* conv)
{
	int err=TRANSFORM_INPUT_EMPTY;
	bool bIsNull=true;
	string s;
	const char*null_value_str=isDatabase?DB_NULL_VALUE:"";
	if(k>=0&&k<len){
		if(delimiter!=0){
			bool bIsCsv=str_qual=='"'&&num_qual==0;
			if(bIsCsv){
				s=konga::readCsvData(in, buf, buf_len, len, k, bIsNull, delimiter, null_value_str, pRecord);
				if(s.length()>0)err=0;
			}
			else {
				s=readDelimitedText(buf, k, de.getType(), str_qual, num_qual, delimiter, bIsNull, err);
				if(err>=20&&err<=32)throw err;
			}
		}
		else {
			s=readFixedFieldText(buf, len, de.getPosBegin()-1, de.getPosLength(), bIsNull, err);
			if(err>=30&&err<=32)throw err;
		}
	}
	if(bTrim && bIsNull==false)s=konga::trim(s);
	if( conv != 0 && de.IsAString() ) {
		s = conv->from(s);
	}
	de.setValue(s.c_str());
	if(err)de.setError(err);
	de.setNull(bIsNull);
}

                                                                     
                    
void CromDataRead(CROMData* de, FILE* in, char* buf, const int buf_len, int&len, int& k,char str_qual, char num_qual, char delimiter)
{
	int err=TRANSFORM_INPUT_EMPTY;
	bool bIsNull=true;
	string s;
	DataElement::Type type=de->GetDTDNode()->get_DataElementType();

	if(k>=0&&k<len){
		if(delimiter!=0){
			bool bIsCsv=str_qual=='"'&&num_qual==0;
			if(bIsCsv){
				s=konga::readCsvData(in, buf, buf_len, len, k, bIsNull, delimiter, DB_NULL_VALUE);
				if(s.length()>0)err=0;
			}
			else {
				s=readDelimitedText(buf, k, type, str_qual, num_qual, delimiter, bIsNull, err);
                                    
			}
		}
		else {
			s=readFixedFieldText(buf, len, de->GetFieldPosition()-1, de->GetFieldLength(), bIsNull, err);
                                   
		}
	}
	de->SetValue(s.c_str());
	de->SetNullFlag(bIsNull);
}

