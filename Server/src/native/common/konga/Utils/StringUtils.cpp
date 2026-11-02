/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#include <stdlib.h>                  

#include <algorithm>	                       
#include <cassert>

#include <boost/algorithm/string.hpp>		                 

#include "AutoCleanup.h"
#include "AutoPtrCleanup.h"
#include "CrException.h"
#include "FileLineReadWriter.h"
#include "StringTokenizer.h"
#include "StringBuilder.h"

#include "FileUtils.h"
#include "StringUtils.h"
#include "StringBuilder.h"
#include "PortDefs.h"		                         

using std::string;
using std::vector;

namespace konga {

bool toBool(const std::string& s) {
	string S(s);
	ToUpper(S);
	return S=="TRUE" || S=="YES" || toLong(s) != 0;
}

long toLong(const std::string& stLong, int base) {
	if ( base < 2 || base > 36 )
		throw CrException("Illegal base specified when converting '" + stLong
		+ "' to a long. Base has to be between 2 and 36 inclusive.");
	if ( stLong.empty() )
		return 0;

	char* pChar = 0;
	long l = strtol(stLong.c_str(), &pChar, base);
	if ( pChar != 0 ) {
		const string stInvalid(pChar);
		if ( !stInvalid.empty() ) {
			throw CrException("The entire string '" + stLong + "' could not be converted to a long" \
				" using base " + toString(base) +
				". The invalid part of the string is '" + stInvalid + "'.");
		}
	}
	return l;
}

long long toLongLong(const std::string& stLongLong, int base) {
	if ( base < 2 || base > 36 )
		throw CrException("Illegal base specified when converting '" + stLongLong
		+ "' to a long long (usually 64 bit integer). Base has to be between 2 and 36 inclusive.");
	if ( stLongLong.empty() )
		return 0;

	char* pChar = 0;
	long long ll = strtoll(stLongLong.c_str(), &pChar, base);
	if ( pChar != 0 ) {
		const string stInvalid(pChar);
		if ( !stInvalid.empty() ) {
			throw CrException("The entire string '" + stLongLong + "' could not be converted to a long long" \
				" using base " + toString(base) +
				". The invalid part of the string is '" + stInvalid + "'.");
		}
	}
	return ll;
}

double toDouble(const std::string& stDouble) {
	if ( stDouble.empty() )
		return 0;

	char* pChar = 0;
	double d = strtod(stDouble.c_str(), &pChar);
	if ( pChar != 0 ) {
		const string stInvalid(pChar);
		if ( !stInvalid.empty() ) {
			throw CrException("The entire string'" + stDouble + "' could not be converted to a double" \
				". The invalid part of the string is '" + stInvalid + "'.");
		}
	}
	return d;
}

char* strsave(const char*str, unsigned int n){
	assert(str);
	char*s=new char[n+1];
	strncpy(s,str,n);
	s[n]=0;
	return s;
}

int strcspn(const char*str,const char c0){
	if(str==0)return 0;
	char c;
	int i;
	for(i=0;(c=str[i])!=0&&c!=c0;i++);
	return i;
}

int strcspn_(const char*str,const char c0){
	if(str==0)return 0;
	int q=0,p=0,r=0,s=0;
	char c;
	int i;
	for(i=0;(c=str[i])!=0;i++){
		switch(c){
		case '"': q=q?0:1; break;
		case '(': p++; break;
		case ')': p--; break;
		case '[': s++; break;
		case ']': s--; break;
		case '{': r++; break;
		case '}': r--; break;
		}
		if(c==c0&&p==0&&q==0&&r==0&&s==0)break;
	}
	return i;
}

int strcspn_(const char*str,const char*c0){
	if(str==0)return 0;
	int q=0,p=0,r=0,s=0;
	char c;
	int i;
	size_t k=strlen(c0);
	for(i=0;(c=str[i])!=0;i++){
		switch(c){
		case '"': q=q?0:1; break;
		case '(': p++; break;
		case ')': p--; break;
		case '[': s++; break;
		case ']': s--; break;
		case '{': r++; break;
		case '}': r--; break;
		}
		if(p==0&&q==0&&r==0&&s==0){
			for(unsigned int j=0;j<k;j++)if(c==c0[j])goto Exit;
		}
	}
Exit:
	return i;
}

                                                                           
                                                                           
                                                   
int strcspn_backward(const char*str,const char*c0)
{
	if(str==0)return 0;
	char c;
	int i,j;
	int n=static_cast<int>(strlen(str)),m=static_cast<int>(strlen(c0));
	for(i=n-1;i>=0;i--){
		c=str[i];
		for(j=0;j<m;j++)if(c==c0[j])goto Exit;
	}
Exit:
	return i;
}

std::string Collapse(const std::string& inString)
{
	std::string outString;
	bool bLastCharWasWhiteSpace = false;
	for(string::size_type i = 0, nLen = inString.length(); i < nLen; ++i )
	{
		char c = inString[i];
		if( c == ' ' || c == '\t' )
		{
			if( bLastCharWasWhiteSpace )
				continue;
			bLastCharWasWhiteSpace = true;
		}
		else
		{
			bLastCharWasWhiteSpace = false;
		}
		outString += c;
	}

	ReplaceCharWithChar(outString, '\t', ' ');
	return outString;
}

void ToLower(std::string& stString) {
	const string::size_type N = stString.size();
	for( string::size_type i = 0; i < N; ++i ) {
		stString[i] = tolower(stString[i]);
	}
}

void ToUpper(std::string& stString) {
	const string::size_type N = stString.size();
	for( string::size_type i = 0; i < N; ++i ) {
		stString[i] = toupper(stString[i]);
	}
}

namespace {
	void handle_mc(string& in) {
		const string::size_type size = in.size();
		string::size_type ipos = 0;
		while( (ipos = in.find(" Mc", ipos)) != string::npos) {
			if ( ipos < size - 3 ) {
				char c = in.at(ipos + 3);
				if ( isalpha(c) ) {
					in.at(ipos + 3) = toupper(c);
				}
				ipos += 4;
			}
			else {
				break;
			}
		}
	}
}

void ToProper(string& in) {
	bool prevIsAlpha = false;
	for( string::iterator it = in.begin(); it != in.end(); ++it ) {
		const char c = *it;
		if ( prevIsAlpha ) {
			*it = tolower(c);
		}
		else {
			*it = toupper(c);
		}
		prevIsAlpha = isalpha(c)!=0;
	}
	handle_mc(in);
}

void AddEscapeSequences(std::string& s, const std::string& stCharsToEscape, char cEscapeChar           )
{
	                                                                  
	std::string stEsc(stCharsToEscape);
	if ( stEsc.find(cEscapeChar) == std::string::npos )
		stEsc += cEscapeChar;

	std::string::size_type iPos = 0;
	while ( (iPos = s.find_first_of(stEsc, iPos)) != std::string::npos )
	{
		s.insert(iPos, 1, cEscapeChar);
		iPos += 2;
	}
}

void RemoveEscapeSequences(std::string& s, const std::string& stCharsToEscape, char cEscapeChar           )
{
	std::string t;
	t.swap(s);
	s.reserve(t.length());
	std::string::iterator sit = t.begin();
	for(;sit!=t.end();++sit)
	{
		if( *sit==cEscapeChar && stCharsToEscape.find( *(sit+1) ) != std::string::npos )
			s += *(++sit);
		else
			s += *sit;
	}
}

string RemoveUnquotedWhitespace(const std::string& s, const std::string& stCuoteChar) {
	std::stringstream ss;
	const string tokens(" \t" + stCuoteChar);
	bool bIsQuoted = false;
	string::size_type startpos = 0, endpos = 0, size = s.size();
	while ( (endpos = s.find_first_of(tokens, startpos)) != string::npos ) {
		switch(s[endpos]) {
			case '\"':	                     
			case '\'':
				ss << s.substr(startpos, endpos-startpos+1);
				bIsQuoted = !bIsQuoted;
				break;
			case ' ':	                      
			case '\t':
				if ( bIsQuoted )
					ss << s.substr(startpos, endpos-startpos+1);
				else {
					if ( startpos != endpos )
						ss << s.substr(startpos, endpos-startpos);
				}
				break;
			default:
				break;	                           
		}
		startpos = endpos + 1;
	}
	if ( startpos < size )
		ss << s.substr(startpos);

	return ss.str();
}

  
                                                      
  
char* strnewdup(const char*s)
{
	char*str;
	if(s!=0)
	{
		str=new char[strlen(s)+1];
		strcpy(str,s);
	}
	else
	{
		str=new char[strlen("(null)")+1];
		strcpy(str,"(null)");
	}
	return str;
}

char* strsave(const char*str){
	assert(str);
	size_t k=strlen(str);
	char*newstr=new char[k+1];
	strcpy(newstr,str);
	return newstr;
}

  
                                                                  
                                                                  
                      
  
int findn(const char*str,char c0)
{
	if(str==0)return -1;
	int i=0;
	char c;
	for(;(c=str[i])!=0&&c!=c0;i++);
	return c==c0?i:-1;
}

  
                                                                      
  
int findk(const char*str,char c0)
{
	if(str==0)return 0;
	int i=0;
	for(char c;(c=str[i])!=0&&c!=c0;i++);
	return i;
}

int findk(const char*str,char*c0){
	if(str==0)return 0;
	size_t i=0,k=strlen(c0);
	for(char c;(c=str[i])!=0;i++){
		for(size_t j=0;j<k;j++)if(c==c0[j])goto Exit;
	}
Exit:
	return (int)i;
}

int findk(const char*str,char c0, int max){
	if(str==0)return 0;
	int i=0;
	for(char c;(c=str[i])!=0&&c!=c0&&i<max;i++);
	return i;
}


int findk(const wchar_t*str, wchar_t c0)
{
	if(str==0)return 0;
	int i=0;
	for(wchar_t c;(c=str[i])!=0&&c!=c0;i++);
	return i;
}

int findk(const wchar_t*str,wchar_t*c0){
	if(str==0)return 0;
	size_t i=0,k=wcslen(c0);
	for(wchar_t c;(c=str[i])!=0;i++){
		for(size_t j=0;j<k;j++)if(c==c0[j])goto Exit;
	}
Exit:
	return (int)i;
}

void UrlEscape(std::string& str, bool bEncodeReservedChars, bool bEncodeTilde)
{
	const std::string stOrig(str);
	str.clear();

	char safeChars[] = "$-_.+!*'(),";
	char reservedChars[] = ";/?:@=&";
	char tilde = '~';
	for( string::const_iterator it = stOrig.begin(); it != stOrig.end(); ++it)
	{
		if ( isalnum_safe(*it) ||
			strchr(safeChars,*it) ||
			( !bEncodeReservedChars && strchr(reservedChars,*it) ) ||
			( !bEncodeTilde && *it == tilde )
			)
		{
			str.append(1, *it);
		}
		else
		{
			char buf[20];
			sprintf(buf,"%%%02X",static_cast<unsigned char>(*it));
			str += buf;
		}
	}
}

void konga::UrlEncode(std::string& str_in, bool bEncodeReserved, bool bEncodeUnsafe) {
	char safe[] = "$-_.+!*'(),";
	char reserved[] = ";/?:@&=";
	char unsafe[] = " \"<>#%{}|\\^~[]`";

	const string orig(str_in);
	string str;
	char buf[4];
	for( string::const_iterator it = orig.begin(); it != orig.end(); ++it) {
		char c = *it;
		if ( isalnum_safe(c) || strchr(safe, c) ) {
			                              
			str.append(1, c);
		}
		else if ( !bEncodeReserved && strchr(reserved, c) ) {
			str.append(1, c);
		}
		else if ( !bEncodeUnsafe && strchr(unsafe, c) ) {
			str.append(1, c);
		}
		else {
			                                       
			sprintf(buf, "%%%X", (unsigned char)c);
			buf[3] = 0;
			str.append(buf);
		}
	}
	str_in = str;
}

void konga::EncodeQueryString(std::string& str_in, bool encodeSpaceAsPlus) {
	char unreserved[] = "-_.~";

	const string orig(str_in);
	string str_out;
	char buf[4];
	for( string::const_iterator it = orig.begin(); it != orig.end(); ++it) {
		const char c = *it;
		if( isalnum_safe(c) || strchr(unreserved, c) ) {
			str_out.append(1, c);
		}
		else if( encodeSpaceAsPlus && c == ' ' ) {
			str_out.append(1, '+');
		}
		else {
			                                     
			sprintf(buf, "%%%X", (unsigned char)c);
			buf[3] = 0;
			str_out.append(buf);
		}
	}
	str_in = str_out;
}

void konga::DecodeQueryString(std::string& str_in) {
	const string orig(str_in);
	string str_out;
	char buf[3];
	for( string::const_iterator it = orig.begin(); it != orig.end(); ++it ) {
		const char c = *it;
		if( c == '%' ) {
			                                               
			++it;
			if( it == orig.end() ) {
				                    
				return;
			}
			buf[0] = *it;
			++it;
			if( it == orig.end() ) {
				                    
				return;
			}
			buf[1] = *it;
			buf[2] = 0;
			int hex;
			if(sscanf(buf, "%2x", &hex)==1){
				str_out.append(1, hex);
			}
			else {
				                             
				str_out.append(1, '%');
				str_out.append(buf);
			}
		}
		else if( c == '+' ) {
			str_out.append(1, ' ');
		}
		else {
			str_out.append(1, c);
		}
	}
	str_in = str_out;
}

std::string strtok(std::string& stString, char cTok, char cEsc)
{
	std::string stRet;
	std::string::size_type lPos1=0, lPos2;
	char pcEscapeables[3] = { cTok, cEsc, 0 };
	while( (lPos2=stString.find_first_of( pcEscapeables, lPos1 )) != std::string::npos )
	{
		if( stString[ lPos2 ] == cTok )
		{
			stRet += stString.substr(lPos1,lPos2-lPos1);
			break;
		}
		else
			stRet += stString.substr(lPos1,lPos2-lPos1) + stString[ lPos2+1 ];                                        
		lPos1 = lPos2 + 2;
	}
	stString.erase(0,lPos2==std::string::npos?lPos1:lPos2+1);
	return stRet;
}

namespace {
	struct CharMatchPred {
		CharMatchPred(char c) : m_c(c) {}
		bool operator()(char c) { return c == m_c; }
		char m_c;
	};
}

long ReplaceCharWithChar(std::string& s, char cToFind, char cToReplace)
{
	CharMatchPred chmatch(cToFind);
	const long lNumReplaced = std::count_if(s.begin(), s.end(), chmatch);
	if ( lNumReplaced == 0 || cToFind == cToReplace )
		return lNumReplaced;

	std::replace_if(s.begin(), s.end(), chmatch, cToReplace);
	return lNumReplaced;
}

void ReplaceCharWithString(const string& in, string& out, char c, const char* s)
{
	out.clear();
	out.reserve(in.size());
	for( string::const_iterator it = in.begin(); it != in.end(); ++it)
	{
		if (*it == c)
			out += s;
		else
			out += *it;
	}
}

int ReplaceStringWithString(const string& stIn, string& stOut, const string& search, const string& replace)
{
	                          
	if ( search.empty() ) {
		stOut = stIn;
		return 0;
	}

	string::size_type pos = 0, oldpos = 0;
	const string::size_type serlen = search.length();

	stOut.clear();
	stOut.reserve( stIn.length() );

	int count = 0;
	for(; (pos=stIn.find(search,pos)) != string::npos; )
	{
		++count;
		stOut += stIn.substr(oldpos,pos-oldpos);
		stOut += replace;
		pos += serlen;
		oldpos = pos;
	}
	stOut += stIn.substr(oldpos);
	return count;
}

void WrapLines(std::string& str, unsigned int n) {
	if ( n == 0 )
		throw CrException("Invalid Input: Cannot wrap lines to zero length.");

	                                                                             
	if ( str.size() <= n )
		return;

	                                                
	string origBrakeSequence;
	if ( str.find("\r\n") != string::npos )
		origBrakeSequence = "\r\n";
	else if ( str.find("\n") != string::npos )
		origBrakeSequence = "\n";
	else if ( str.find("\r") != string::npos )
		origBrakeSequence = "\r";

	string out;
	out.reserve(str.size() + str.size()/n + 1);	                                                   
	if ( !origBrakeSequence.empty() )
		ReplaceStringWithString(str, out, origBrakeSequence, "");
	else
		out = str;

	                                              
	string::size_type size = out.size();
	const string br("\n");
	for( std::string::size_type ipos = n; ipos < size; ipos += n ) {
		out.insert(ipos, br);
		++ipos;
		++size;
	}
	str = out;
}

                                                               
                                                               
                               
                                                                    
                                                                   
                                                                  
  
char* get_token(char*& str, char*& token, const char* tok_chrs)
{
	if( !str || !*str ) return token=0;
	char* pc = strpbrk(str,tok_chrs);
	if( pc ) *pc=0;
	if( token ) strcpy(token,str);
	else if(str) token = strnewdup(str);
	else return token = 0;
	if( pc ) str=pc+1;
	else str=0;
	return token;
}

char* get_token(char*& str, const char* tok_chrs)
{
	char*token=0;
	if( !str || !*str ) return 0;
	char* pc = strpbrk(str,tok_chrs);
	if( pc ) *pc=0;
	if(str) token = strnewdup(str);
	else return 0;
	if( pc ) str=pc+1;
	else str=0;
	return token;
}

                                                             
                                                         
char*stristr(const char*str,const char*substr){
	static char cc[3]={'a','A',0};
	cc[0]=tolower(*substr), cc[1]=toupper(*substr);
	size_t k,n=strlen(substr);
	for(k=findk(str,cc);str[k]!=0;k=findk(str,cc)){
		if(k==0||isalpha(str[k-1])==0){
			if(strncasecmp(&str[k],substr,n)==0)return (char*)(&str[k]);
		}
		str=&str[k+1];
	}
	return 0;
}

vector<string> SplitString(const string& inString, const string& delimiter) {
	if ( delimiter.empty() )
		throw CrException("Illegal argument to SplitString. Delimiter is empty.");
	vector<string> strArray;
	string::size_type startPos = 0, endPos = 0;
	const string::size_type inStringLen = inString.size();
	while (endPos != string::npos && startPos < inStringLen ) {
		endPos = inString.find(delimiter, startPos);
		if (endPos != string::npos) {
			strArray.push_back(inString.substr(startPos, endPos-startPos));
			startPos = endPos + delimiter.size();                                       
		}
	}
	if (startPos < inStringLen) {
		strArray.push_back(inString.substr(startPos));
	}
	return strArray;
}

string trim(const string& str){
	string s(str);
	boost::algorithm::trim(s);
	return s;
}

string ltrim(const string& str){
	string s(str);
	boost::algorithm::trim_left(s);
	return s;
}

string rtrim(const string& str){
	string s(str);
	boost::algorithm::trim_right(s);
	return s;
}

string trim_chars(const string& str, const string& chars) {
	string tmp(str);
	boost::algorithm::trim_if(tmp, boost::algorithm::is_any_of(chars));
	return tmp;
}

string ltrim_chars(const string& str, const string& chars) {
	string tmp(str);
	boost::algorithm::trim_left_if(tmp, boost::algorithm::is_any_of(chars));
	return tmp;
}

string rtrim_chars(const string& str, const string& chars) {
	string tmp(str);
	boost::algorithm::trim_right_if(tmp, boost::algorithm::is_any_of(chars));
	return tmp;
}

std::string TrimString(const std::string& inString, char trimChar)
{
	string retStr(inString);
	string::size_type index;
	if ( (index = retStr.find_first_not_of(trimChar)) != string::npos)
		retStr = retStr.erase(0, index);
	if ( (index = retStr.find_last_not_of(trimChar)) != string::npos)
		retStr = retStr.erase(index+1);
	return retStr;
}

string TrimCharsFromString(const string& inString, const string& trimChars) {
	if ( trimChars.empty() )
		return inString;
	return trim_chars(inString, trimChars);
}

std::string UnquoteString(const std::string& inString)
{
	std::string retStr = inString;
	if ( (retStr[0] == '\'' && retStr[retStr.size()-1] == '\'') ||
		(retStr[0] == '"' && retStr[retStr.size()-1] == '"') ){
		retStr.erase(0, 1);
		retStr.erase(retStr.size()-1);
	}
	return retStr;
}

std::string LPadChar(const std::string& in_string, char pad_char, std::string::size_type size) {
	if ( in_string.size() >= size )
		return in_string.substr(0, size);

	string out_string;
	const string::size_type N = size-in_string.size();
	for( string::size_type i = 0; i < N; ++i ) {
		out_string += pad_char;
	}
	return out_string.append(in_string);
}

std::string RPadChar(const std::string& in_string, char pad_char, std::string::size_type size) {
	if ( in_string.size() >= size )
		return in_string.substr(0, size);

	string out_string(in_string);
	const string::size_type N = size-in_string.size();
	for( string::size_type i = 0; i < N; ++i ) {
		out_string += pad_char;
	}
	return out_string;
}

char* itoa(int i, char* buf, int radix) {
	assert(radix == 10 || radix == 16);

	switch(radix) {
	case 10:
		sprintf(buf, "%d", i);
		return buf;
	case 16:
		sprintf(buf, "%x", i);
		return buf;
	default:
		string stErr = "itoa called with unsupported radix = ";
		char itoabuf[64];
		sprintf(itoabuf, "%d", 10);
		stErr += itoabuf;
		throw CrException(stErr);
	}
}

bool isalnum_safe(char c) {
	if(c < 48 ) return false;
	return isalnum((int)c)!=0;
}

bool isspace_safe(char c) {
	if(c & 128) return false;
	return isspace((int)c)!=0;
}

int inArray(const char*arr[],const char*str)
{
	const char* tmp;
	for(int i=0;(tmp=arr[i]);i++){
		if(stricmp(tmp,str)==0)
			return i;
	}
	return -1;
}

bool EqualsIgnoreCase(const std::string& lhs, const std::string& rhs) {
	                                                                                      
	return strcasecmp(lhs.c_str(), rhs.c_str()) == 0;
}

namespace {
	class ReplaceInFile : public FileLineReadWriter {
	public:
		ReplaceInFile(const char* oldStr, const char* newStr)
			: FileLineReadWriter(getSystemTmpDir()), m_oldStr(oldStr),
			m_stOld(oldStr), m_stNew(newStr), m_iCount(0) {}
		virtual ~ReplaceInFile() {}

	public:
		int GetCount() {
			return m_iCount;
		}

	protected:
		const char* ChangeLine(const char* line) {
			if ( line == 0 ) {
				                                                          
				if ( m_iCount == 0 )
					throw AbortRead();
				return 0;
			}
			if ( strstr(line, m_oldStr) ) {
				const string stOldLine(line);
				m_stNewLine.clear();
				m_iCount += ReplaceStringWithString(stOldLine, m_stNewLine, m_stOld, m_stNew);
				return m_stNewLine.c_str();	                                                           
			}
			return line;
		}

	private:
		const char *m_oldStr;
		const string m_stOld, m_stNew;
		string m_stNewLine;
		int m_iCount;
	};
}

                                                            
                              
  
int replace_in_file(const char* filename, const char* oldStr, const char* newStr)
{
	                                  
	ReplaceInFile replacer(oldStr, newStr);
	replacer.Read(filename);
	return replacer.GetCount();
}

bool BeginsWith(const std::string& theString, char c) {
	return theString.length() > 0 && *theString.begin() == c;
}

bool EndsWith(const std::string& theString, char c) {
	return theString.length() > 0 && *(theString.end()-1) == c;
}

bool BeginsWith(const std::string& theString, const char* s) {
	if(s==0)return true;
	return strncmp(theString.c_str(),s,strlen(s))==0;
}

bool EndsWith(const std::string& theString, const char* s) {
	if(s==0)return true;
	const char*s1=theString.c_str();
	int n1=theString.length(), n=strlen(s);

	return (n1>=n)&&strcmp(&s1[n1-n],s)==0;
}


bool EndsWithPathSeparator(const std::string& stPath) {
	return EndsWith(stPath, '/') || EndsWith(stPath, '\\');
}

namespace {
	bool isWSP(char c) {
		switch(c) {
			case ' ':
			case '\t':
				return true;
			default:
				return false;
		}
	}
}

void ParseRFC2822Headers(const string& stHeaders, vector<string>& vHeaders) {
	StringTokenizer tokenizer(stHeaders.c_str(), "\r\n");

	vHeaders.clear();
	string header;
	while( tokenizer.NextToken() ) {
		const char* token = tokenizer.token();
		const char firstChar = token[0];
		if( strlen(token) == 1 && (firstChar == '\r' || firstChar == '\n') ) {
			                                        
		}
		else {
			                            
			if ( isWSP(firstChar) ) {
				                                                                                          
				if ( !header.empty() ) {
					header += "\r\n";
				}
				header += token;
			}
			else {
				                     
				if ( !header.empty() ) {
					vHeaders.push_back(header);
				}
				header = token;
			}
		}
	}

	                   
	if ( !header.empty() ) {
		vHeaders.push_back(header);
	}
}

string xml_text(const char*str0, bool isAttribute){
	string str;
	if(isAttribute==false){
		const char* str1=strstr(str0,"<![CDATA[");
		while(str1!=0){
			int html_len=str1-str0;
			const char*str2=strstr(str1,"]]>");
			if(str2==0){                                         
				break;
			}
			str2+=3;                                         
			int CDATA_len=str2-str1;
			if(html_len>0)str+=html_string(str0, html_len, false);
			str+=string(str1,CDATA_len);
			str0=str2;
			str1=strstr(str0,"<![CDATA[");
		}
	}
	str+=html_string(str0, strlen(str0), isAttribute);
	return str;
}

string xml_text_restore(const char*str0){
	string str;
	const char* str1=strstr(str0,"<![CDATA[");
	string tmp;
	while(str1!=0){
		int html_len=str1-str0;
		const char*str2=strstr(str1,"]]>");
		if(str2==0){                                         
			break;
		}
		str2+=3;                                         
		int CDATA_len=str2-str1-12;
		if(html_len>0){
			tmp=string(str0,html_len);
			str+=translate_html(tmp.c_str());
		}
		if(CDATA_len-12)str+=string(str1+9,CDATA_len);
		str0=str2;
		str1=strstr(str0,"<![CDATA[");
	}
	int len=strlen(str0);
	if(len>0){
		tmp=string(str0,len);
		str+=translate_html(tmp.c_str());
	}
	return str;
}

string html_string(const char*str, int len, bool isForAtttribute){
	assert(str!=0);
	if(!isForAtttribute){
		const char*h1=strstr(str,"<![CDATA[");
		if(h1!=0){
			const char*h2=strstr(str,"]]>");
			if(h2>h1)return string(str);
		}
	}
	string tmp,token;
	StringTokenizer tokens(str, len, "<>&\"");
	while(tokens.NextToken()){
		token=tokens.token();
		switch(token[0]){
		case '<':
			tmp+="&lt;"; break;
		case '>':
			tmp+="&gt;"; break;
		case '&':
			tmp+="&amp;"; break;
		case '"':
			if(isForAtttribute){
				tmp+="&quot;";
				break;
			}
		            
		                        
		default:
			tmp+=token; break;
		}
	}
	return tmp;
}

string html_string(const std::string& str) {
	return html_string(str.c_str(), str.size(), false);
}

namespace {
bool isHtmlEncoded(const char*s){
	if(strnicmp(s,"&#",2)==0) {
		if(strnicmp(s,"&#x",3)==0) {
			if(s[5]==';') {
				                                                              
				return true;
			}
		}
		else if(isdigit(s[2])&&isdigit(s[3])){
			if(s[4]==';')return true;
			if(isdigit(s[4])&&s[5]==';')return true;
		}
	}
	return false;
}
}

bool require_translate_html(const char*s){
	for(int i=0, n=strlen(s); i<n; ++i){
		i=strcspn(&s[i],'&')+i;
		if(s[i]==0)return false;
		if(strnicmp(&s[i],"&lt;",4)==0 || 
			strnicmp(&s[i],"&gt;",4)==0 ||
			strnicmp(&s[i],"&amp;",5)==0 ||
			strnicmp(&s[i],"&quot;",6)==0 ||
			strnicmp(&s[i],"&apos;",6)==0){
			return true;
		}
	}
	return false;
}

string translate_html(const char*s){
	string tmp;
	const int l=strlen(s);

	int ip0=0,ip=0;
	while(ip<l){
		ip=strcspn(&s[ip],'&')+ip;
		if(s[ip]==0||ip+4>l)break;
		if(strnicmp(&s[ip],"&lt;",4)==0){
			if(ip>ip0)tmp.append(&s[ip0], ip-ip0);
			tmp.append(1, '<');
			ip+=4;
			ip0=ip;
		} else if(strnicmp(&s[ip],"&gt;",4)==0){
			if(ip>ip0)tmp.append(&s[ip0],ip-ip0);
			tmp.append(1, '>');
			ip+=4;
			ip0=ip;
		} else if(strnicmp(&s[ip],"&amp;",5)==0){
			if(ip>ip0)tmp.append(&s[ip0],ip-ip0);
			tmp.append(1, '&');
			ip+=5;
			ip0=ip;
		} else if(strnicmp(&s[ip],"&quot;",6)==0){
			if(ip>ip0)tmp.append(&s[ip0],ip-ip0);
			tmp.append(1, '"');
			ip+=6;
			ip0=ip;
		} else if(strnicmp(&s[ip],"&apos;",6)==0){
			if(ip>ip0)tmp.append(&s[ip0],ip-ip0);
			tmp.append(1, '\'');
			ip+=6;
			ip0=ip;
		} else if(s[ip+1]=='#'){
			if(ip+5>l)break;
			if(ip>ip0)tmp.append(&s[ip0],ip-ip0);
			if(s[ip+2]=='x') {
				                
				if(s[ip+5]==';') {
					int hex;
					if(sscanf(&s[ip+3],"%2x",&hex)==1){
						tmp.append(1, (char)hex);
						ip+=6;
					}
					else {
						tmp.append("&#x");
						ip+=3;
					}
				}
				else {
					tmp.append("&#x");
					ip+=3;
				}
			}
			else {
				                  
				int d;
				if(s[ip+4]==';'){
					if(sscanf(&s[ip+2],"%2d",&d)==1){
						tmp.append(1, (char)d);
						ip+=5;
					}
					else {
						tmp.append("&#");
						ip+=2;
					}
				}
				else if(s[ip+5]==';'){
					if(sscanf(&s[ip+2],"%3d",&d)==1){
						tmp.append(1, (char)d);
						ip+=6;
					}
					else {
						tmp.append("&#");
						ip+=2;
					}
				}
				else {
					tmp.append("&#");
					ip+=2;
				}
			}
			ip0=ip;
		} else {
			ip++;
		}
	}

	if(ip0==0) {
		return string(s);
	}
	else {
		tmp.append(&s[ip0]);
	}
	return tmp;
}

string translate_html_sb(const char*s){
	konga::StringBuilder tmp;
	int ip0=0,ip=0,l=strlen(s);
	while(ip<l){
		ip=strcspn(&s[ip],'&')+ip;
		if(s[ip]==0)break;
		if(strnicmp(&s[ip],"&lt;",4)==0){
			if(ip>ip0)tmp.append(string(&s[ip0],ip-ip0));
			tmp.append('<');
			ip+=4;
			ip0=ip;
		} else if(strnicmp(&s[ip],"&gt;",4)==0){
			if(ip>ip0)tmp.append(string(&s[ip0],ip-ip0));
			tmp.append('>');
			ip+=4;
			ip0=ip;
		} else if(strnicmp(&s[ip],"&amp;",5)==0){
			if(ip>ip0)tmp.append(string(&s[ip0],ip-ip0));
			tmp.append('&');
			ip+=5;
			ip0=ip;
		} else if(strnicmp(&s[ip],"&quot;",6)==0){
			if(ip>ip0)tmp.append(string(&s[ip0],ip-ip0));
			tmp.append('"');
			ip+=6;
			ip0=ip;
		} else if(strnicmp(&s[ip],"&apos;",6)==0){
			if(ip>ip0)tmp.append(string(&s[ip0],ip-ip0));
			tmp.append('\'');
			ip+=6;
			ip0=ip;
		} else if(s[ip+1]=='#'){
			if(ip>ip0)tmp.append(string(&s[ip0],ip-ip0));
			int d;
			if(s[ip+4]==';'){
				if(sscanf(&s[ip+2],"%2d",&d)==1){
					tmp.append((char)d);
				}
				ip+=5;
			}
			else if(s[ip+5]==';'){
				if(sscanf(&s[ip+2],"%3d",&d)==1){
					tmp.append(char(d));
				}
				ip+=6;
			}
			else {
				tmp.append('&');
				ip++;
			}
			ip0=ip;
		} else {
			ip++;
		}
	}
	if(ip0==0)return string(s);
	else tmp.append(&s[ip0]);
	return tmp.toString();
}

bool hasSpecialCharsToEacape(const char*str){
	                                         
	const size_t i=::strcspn(str, "\b\f\n\r\t\v\"'\\");
	return str[i]!=0;
}

std::string escapeSpecialChars(const char*str){
	std::string tmp,token;
	StringTokenizer tokens(str,"\b\f\n\r\t\v\"'\\");
	while(tokens.NextToken()){
		token=tokens.token();
		switch(token[0]){
		case '\b':
			tmp+="\\b";
			break;
		case '\f':
			tmp+="\\f";
			break;
		case '\n':
			tmp+="\\n";
			break;
		case '\r':
			tmp+="\\r";
			break;
		case '\t':
			tmp+="\\t";
			break;
		case '\v':
			tmp+="\\v";
			break;
		case '"':
		case '\'':
		case '\\':
			tmp+='\\';
		default:
			tmp+=token; break;
		}
	}
	return tmp;
}


bool hasEscapedCharacter(const std::string&tmp){
	return tmp.find('\\')!=std::string::npos;
}

std::string restoreEscapedCharacters(const std::string&s0)
{
	int j=0,n=s0.size();
	char*buf=new char[n+1];
	for(int i=0;i<n;i++){
		char c=s0.at(i);
		if(c=='\\' && (i+1)<n){
			i++;
			c=s0.at(i);
			if(isdigit(c) && (i+2)<n && isdigit(s0.at(i+1)) && isdigit(s0.at(i+2))){
				int k;
				sscanf(&(s0.c_str()[i]),"%3o",&k);
				c=char(k);
				i+=2;
			} else {
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
						break;					                                  
					default:
						buf[j++]='\\'; break;
				}
			}
		}
		buf[j++]=c;
	}
	buf[j]=0;
	std::string s(buf);
	delete[]buf;
	return s;
}

int findNextDelimiterColumn(const char*sBuffer,int iStart,char cDelimiter, char cStrQual, char cNumQual){
	char c;
	int i=iStart;
	for(;(c=sBuffer[i])!=0;i++){
		if(c==cDelimiter)return i;
		if(c==cStrQual||c==cNumQual){
			char c1;
			for(i++;(c1=sBuffer[i])!=0;i++){
				if(c1==c)break;
			}
		}
	}
	return i;
}
void removeQual(std::string&value, char cQual){
	int n=value.length();
	for(int i1=0;i1<n;i1++){
		if(value.at(i1)==cQual){
			for(int i2=i1+1;i2<n;i2++){
				if(value.at(i2)==cQual){
					value=value.substr(i1+1,i2-i1-1);
					return;
				}
			}
		} else if(value.at(i1)!=' '){
			return;
		}
	}
}

namespace {
	const string& endl() {
		static std::string s0("\n");
		return s0;
	}
}

char getDelimiterChar(const char*cs){
	if(cs==0||cs[0]==0)return ',';
	int len=strlen(cs);
	if(strcmp(cs,"\\t")==0)return '\t';
	if(len==4 && cs[0]=='\\' && isdigit(cs[1]) && isdigit(cs[2]) && isdigit(cs[3])){
		int k;
		sscanf(&cs[1],"%3o",&k);
		return char(k);
	}
	return cs[0];
}

static char Hex[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
std::string getHexString(const char* pbd, const size_t len){
	string st;
	size_t idx;
	st.resize(len*2);
	int pos1,pos2;
	unsigned char c;
	                            
	for ( unsigned long i = 0; i<len;  i++){
		c=*((unsigned char*)pbd+i);
		pos1 = (c&0xF0) >>4;
		idx = i << 1;
		st[idx++ ] = Hex[pos1];
		pos2 = (c&0x0F);
		st[idx ] = Hex[pos2];
	}
	return st;
}

unsigned char* fromHexStringPtr(const char* s, size_t& lenOut) {
	                                       
	if ( s == 0 ) {
		lenOut = 0;
		return 0;
	}

	const size_t stringLen = strlen(s);
	if ( stringLen == 0 ) {
		lenOut = 0;
		return 0;
	}

	lenOut = stringLen>>1;
	if ( lenOut*2 != stringLen ) {
		throw CrException("Invalid hex string '" + string(s) + "'. The length of the hex string has to be even.");
	}

	unsigned char* pdb=new unsigned char[lenOut];
	AutoArrayDelete<unsigned char> adopt_pdb(pdb);	                        

	unsigned int k1, k2;
	
	if(strnicmp(s,"0x",2)==0)s+=2;
	
	for(int j=0; j<lenOut; ++j){
		k1=*s++;
		if(k1>='0' && k1<='9')k1=k1-'0';
		else if(k1>='A' && k1<='F')k1=k1-'A'+10;
		else if(k1>='a' && k1<='f')k1=k1-'a'+10;
		else {
			throw CrException("Invalid hex string '" + string(s) + "'. A hex string can only contain hexadecimal numbers (0 through F).");
		}
		k2=*s++;
		if(k2>='0' && k2<='9')k2=k2-'0';
		else if(k2>='A' && k2<='F')k2=k2-'A'+10;
		else if(k2>='a' && k2<='f')k2=k2-'a'+10;
		else {
			throw CrException("Invalid hex string '" + string(s) + "'. A hex string can only contain hexadecimal numbers (0 through F).");
		}
		pdb[j]=(k1<<4)|k2;
	}

	return adopt_pdb.Release();	                                
}

unsigned char* fromWideCharStringPtr(std::wstring ws) {
	                                       
	long size=ws.size()*2;
	unsigned char* pdb=new unsigned char[size];
	::memcpy(pdb,ws.c_str(),size);
	return pdb;
}
	
unsigned char* fromHexString(const std::string& hex, size_t& len) {
	return fromHexStringPtr(hex.c_str(), len);
}

bool startsWith(const std::string& str, const char* pattern){
	int n=strlen(pattern);
	return strncmp(str.c_str(), pattern, n)==0;
}

bool endsWith(const std::string& str, const char* pattern){
	int n=strlen(pattern);
	return strcmp(str.c_str()+str.size()-n, pattern)==0;
}

std::string normalizeSpace(const std::string& s0){
	const char*s=s0.c_str();
	StringBuilder sb;
	int pos=0, n=s0.size(), lastNoneSpacePos=-1, c;
	bool bSpace0=true, bSpace;
	for(int i=0; i<n; ++i, bSpace0=bSpace){
		bSpace=(c=s[i])==' '||c=='\t'||c=='\r'||c=='\n';
		if(bSpace && bSpace0){
			int i0=i, pos0=pos;
			for(++i; (c=s[i])!=0; ++i){
				bSpace=(c=s[i])==' '||c=='\t'||c=='\r'||c=='\n';
				if(bSpace==false){
					pos=i;
					break;
				}
			}
			if(bSpace){
				i0--;
				pos=n;
			}
			if(i0>pos0){
				for(int i1=pos0; i1<i0; ++i1){
					sb.append(s[i1]);
				}
			}
		}
		if(!bSpace){
			lastNoneSpacePos=i;
		}
	}
	for(int i=pos; i<=lastNoneSpacePos; ++i)sb.append(s[i]);
	return sb.toString();
}

std::string latin1_UTF8(const char*s0){
	if(s0==0)return "";
	std::string s;
	for(unsigned char c, cc[2];(c=*s0)!=0;++s0){
		if((c&0x80)==0){
			s.append((const char*)(&c),1);
		}
		else {
			cc[0]=(0xC0)|(c>>6);
			cc[1]=(0x80)|(c&0x3F);
			s.append((const char*)cc,2);
		}
	}
	return s;
}

std::string UTF8_latin1(const char*s0){
	if(s0==0)return "";
	std::string s;
	for(unsigned char c, cc[3];(c=*s0)!=0;++s0){
		if((c&0x80)==0){
			s.append((const char*)(&c),1);
		}
		else {
			cc[0]=*s0;
			cc[1]=*(s0+1);
			cc[2]=*(s0+2);
			if((cc[2]&0x80)!=0 || (cc[1]&0xDC)!=0){
				throw konga::CrException("UTF8_latin1 conversion failed, multibyte character encountered "+std::string(s0,20));
			}
			c=((cc[1]&3)<<6)|(cc[0]&0x3F);
			s.append((const char*)(&c),1);
			++s0;
		}
	}
	return s;
}
void convertCRLN2LN(std::string& value){
	if(value.find('\r')!=std::string::npos){
		std::string tmp=value;
		ReplaceStringWithString(tmp,value,"\r\n","\n");
	}
}

}                   
