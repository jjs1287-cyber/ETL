/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#ifndef KONGA_CharacterEncodingConversion_EncodingStringUtils_H
#define KONGA_CharacterEncodingConversion_EncodingStringUtils_H


#include <string>

namespace konga {

class EncodingStringUtils
{
	static bool IsUtf8LeadingByte(const unsigned char c)  ;
	static std::size_t Utf8ByteLength(const std::string& utf8string,const std::size_t startbyteposition,const std::size_t characterlength) ;
	static std::size_t Utf8ByteLengthRight(const std::string& utf8string,const std::size_t characterlength) ;
public:
	EncodingStringUtils(void);
	~EncodingStringUtils(void);
	static std::size_t Utf8CharacterSize(const std::string& utf8string) ;
	static std::string Utf8SubString(const std::string& utf8string,const int startcharacterposition,const int characterlength) ;
	static std::string Utf8Truncate(const std::string& utf8string,const int nfirstcharacter,const int nlastcharacter) ;
	static std::string Utf8Right(const std::string& utf8string, const int characterlength) ;
	static std::string Utf8Left(const std::string& utf8string,const int characterlength) ;

};
}

#endif                                                          

