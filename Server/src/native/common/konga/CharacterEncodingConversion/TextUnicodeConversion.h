/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#ifndef KONGA_CharacterEncodingConversion_TextUnicodeConversion_H
#define KONGA_CharacterEncodingConversion_TextUnicodeConversion_H


#include <string>
namespace konga {
class TextUnicodeConversion
{
private:
	unsigned int codepoint;
	bool isinvalid;
	bool IsInValidSubsequentUtf8Byte(const unsigned char subsequentchar);
	int Utf8Bytes(const unsigned char leadingcharacter);
	int howmanyinvalidcharacter;
	std::string	ConvertUnicodeToUtf8	();
	std::wstring ConvertUnicodeToUtf16	();
public:
	char GetInvalidCharacter();
	std::string	ConvertUnicodeToUtf8	(const unsigned int codepoint);
	std::wstring ConvertUnicodeToUtf16	(const unsigned int codepoint);
	unsigned int ConvertUtf8ToUnicode	(const std::string& utf8string);
	unsigned int ConvertUtf16ToUnicode	(const std::wstring& Utf16string);
	std::string	ConvertLatin1ToUtf8	(const std::string& latin1string);
	std::string	ConvertUtf8ToLatin1	(const std::string& utf8string);
	std::string	ConvertUtf16ToUtf8	(const std::wstring& utf16string);
	std::wstring ConvertUtf8ToUtf16	(const std::string& utf8string);
	inline int InvalidCharacterConverted() const; 
	TextUnicodeConversion(void);
	~TextUnicodeConversion(void);
};
}

#endif                                                            
