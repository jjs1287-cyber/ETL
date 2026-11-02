/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef KONGA_STRING_UTILS_H
#define KONGA_STRING_UTILS_H

#ifdef min
	#undef min
#endif
#ifdef max
	#undef max
#endif

#include <string.h>

#include <sstream>	               
#include <string>
#include <vector>
#include <limits>

namespace konga {
	char*	strsave(const char*str,	unsigned int n);
	int		strcspn_backward(const char*str,const char*c0);
	int		strcspn(const char*str,const char c0);
	int		strcspn_(const char*str,const char c0);
	int		strcspn_(const char*str,const char*c0);

	char* itoa(int i, char* buf, int radix);

	                                                                                        
	bool isalnum_safe(char c);

	                                     
	bool isspace_safe(char c);

	                                                                                           
    int inArray(const char*arr[],const char*str);

	bool EqualsIgnoreCase(const std::string& lhs, const std::string& rhs);

	                                               
	namespace {
		inline char* strupr(char* const s){
			for(size_t i=0,n=strlen(s);i<n;i++)s[i]=toupper(s[i]);
			return s;
		}

		inline char* strlower(char* const s){
			for(size_t i=0,n=strlen(s);i<n;i++)s[i]=tolower(s[i]);
			return s;
		}

		                                                                                 
		template<class C>
		std::string toString(const C& toConvert, int precision = 0) {
			std::stringstream s;
			if ( precision > 0 )
				s.precision(precision);
			s << toConvert;
			return s.str();
		}

		inline std::string toString(const char* p) {
			return std::string( p==0 ? "" : p );
		}

		inline std::string toString(size_t i) {
			char buf[128];
			return std::string(itoa(static_cast<int>(i), buf, 10));
		}

		inline std::string toString(int i) {
			return toString<int>(i);
		}

		inline std::string toString(long l) {
			return toString<long>(l);
		}

		inline std::string toString(long long ll) {
			return toString<long long>(ll);
		}

		inline std::string toString(float f) {
			return toString<float>(f, std::numeric_limits<float>::digits10);
		}

		inline std::string toString(double d) {
			return toString<double>(d, std::numeric_limits<double>::digits10);
		}

		inline bool Equals(const std::string& lhs, const std::string& rhs) { return strcmp(lhs.c_str(), rhs.c_str()) == 0; }

		inline bool Differs(const std::string& lhs, const std::string& rhs) { return !Equals(lhs, rhs); }
		inline bool DiffersIgnoreCase(const std::string& lhs, const std::string& rhs) { return !EqualsIgnoreCase(lhs, rhs); }
	}

	                                       
	                                                                         
	long toLong(const std::string& stLong, int base = 10);

	                                                                  
	long long toLongLong(const std::string& stLongLong, int base = 10);

	double toDouble(const std::string& stDouble);

	                                                          
	bool toBool(const std::string& s);

	                                                                                  
	std::string Collapse(const std::string& inString);
	  
	                                                                      
	  
	long ReplaceCharWithChar(std::string& s, char cToFind, char cToReplace);

	                                                
	void ToLower(std::string& stString);

	                                                 
	void ToUpper(std::string& stString);

	                                                                                                      
	                                                                           
	void ToProper(std::string& stString);

	                                                                                      
	                                            
	void ReplaceCharWithString(const std::string& in, std::string& out, char c, const char* s);

	                                                                                           
	                                           
	int ReplaceStringWithString(const std::string& in, std::string& out, const std::string& search, const std::string& replace);

	                                                                    
	                                                       
	void WrapLines(std::string& str, unsigned int n);

	                  
	                                                                                                                     
	                                                         
	                                                               
	                                  
	void UrlEscape(std::string& str, bool bEncodeReservedChars = false, bool bEncodeTilde = true);

	                                                                                                                 
	                                            
	                                                                        
	void UrlEncode(std::string& str, bool bEncodeReserved = true, bool bEncodeUnsafe = true);

	                                                     
	                                            
	void EncodeQueryString(std::string& str, bool encodeSpaceAsPlus = false);

	                                                     
	                                            
	void DecodeQueryString(std::string& str);

	void AddEscapeSequences(std::string& s, const std::string& stCharsToEscape, char cEscapeChar = '\\');
	void RemoveEscapeSequences(std::string& s, const std::string& stCharsToEscape, char cEscapeChar = '\\');

	std::string RemoveUnquotedWhitespace(const std::string& s, const std::string& stCuoteChar = "\"");

	                                                        
	                                                       
	                                                        
	                                  
	char* strnewdup(const char*s);

	                                                                          
	char* strsave(const char*s);
	
	  
	                                                                  
	                                                                  
	                      
	  
	int findn(const char*str,char c0);
	
	int findk(const char*str,char c0);
	int findk(const char*str,char c0, int max);
	int findk(const char*str,char*c0);
	
	int findk(const wchar_t *str, wchar_t c0);
	int findk(const wchar_t*str, wchar_t* c0);
	
	  
	                                                            
	                              
	  
	int replace_in_file(const char* filename, const char* s_old, const char* s_new);
	
	  
	                                                                     
	                                                                
	                                                                     
	  
	std::string strtok(std::string& stString, char cTok, char cEsc);
	
	  
	                                                               
	                                                               
	                               
	                                                                    
	                                                                   
	                                                                  
	  
	char* get_token(char*& str, char*& token, const char* tok_chrs);
	char* get_token(char*& str, const char* tok_chrs);
	
	char*stristr(const char*str,const char*substr);

	                                                                                              
	                                                               
	std::vector<std::string> SplitString(const std::string& inString, const std::string& delimiter);

	                                                                                                         
	std::string trim(const std::string& str);
	std::string ltrim(const std::string& str);
	std::string rtrim(const std::string& str);

	                                       
	std::string trim_chars(const std::string& str, const std::string& chars);
	std::string ltrim_chars(const std::string& str, const std::string& chars);
	std::string rtrim_chars(const std::string& str, const std::string& chars);

	                                                                                
	std::string TrimString(const std::string& inString, char trimChar=' ');

	                                                                                       
	                                                                                   
	std::string TrimCharsFromString(const std::string& inString, const std::string& trimChars);

	std::string UnquoteString(const std::string& inString);

	                                                                      
	                                                                                            
	std::string LPadChar(const std::string& in_string, char pad_char, std::string::size_type size);
	std::string RPadChar(const std::string& in_string, char pad_char, std::string::size_type size);

	bool BeginsWith(const std::string& theString, char c);
	bool EndsWith(const std::string& theString, char c);
	bool BeginsWith(const std::string& theString, const char* s);
	bool EndsWith(const std::string& theString, const char* s);
	bool EndsWithPathSeparator(const std::string& stPath);

	                           
	void ParseRFC2822Headers(const std::string& stHeaders, std::vector<std::string>& vHeaders);

	                                              
	std::string html_string(const char*str, int len, bool isForAttribute);
	std::string html_string(const std::string& str);

	                                                           
	std::string xml_text(const char*str, bool isAttribute);
	                           
	std::string xml_text_restore(const char*str0);

	                              
	std::string translate_html(const char*s);
	bool require_translate_html(const char*s);

	bool hasSpecialCharsToEacape(const char*str);
	std::string escapeSpecialChars(const char*str);
	bool hasEscapedCharacter(const std::string&tmp);
	std::string restoreEscapedCharacters(const std::string&s0);

	int findNextDelimiterColumn(const char*sBuffer,int iStart,char cDelimiter, char cStrQual, char cNumQual);
	void removeQual(std::string&value, char cQual);
	char getDelimiterChar(const char*cs);

	                                                            
	std::string getHexString(const char* pbd, const size_t len);

	                                                                                
	unsigned char* fromHexString(const std::string& hex, size_t& len);

	                                                                                     
	unsigned char* fromHexStringPtr(const char* hex, size_t& len);
	unsigned char* fromWideCharStringPtr(std::wstring);

	bool startsWith(const std::string& str, const char* pattern);
	bool endsWith(const std::string& str, const char* pattern);

	                                                                                             
	std::string normalizeSpace(const std::string& s0);
	                                                     
	std::string latin1_UTF8(const char*s0);
	                                                     
	std::string UTF8_latin1(const char*s0);
	                                        
	void convertCRLN2LN(std::string&value);
}                   

#endif                        

