/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#include "TextUnicodeConversion.h"
#include "EncodingStringUtils.h"
#include <konga/Utils/CrException.h>


namespace {
static const unsigned int INVALIDCODEPOINT=0xFFFFFFFF;

static const unsigned int UTF8_1=0x7F;
static const unsigned int UTF8_2=0x7FF;
static const unsigned int UTF8_3=0xFFFF;
static const unsigned int UTF8_4=0x10FFFF;
static const unsigned int UTF8MASK_1=0x3F;
static const unsigned int UTF8MASK_2=0x0FC0;
static const unsigned int UTF8MASK_3=0x3F000;
static const unsigned int UTF8MASK_4=0x1C0000;
static const unsigned int UTF8Byte=0x80;

static const unsigned int UTF16=0x10FFFD;
static char InvalidCharacter='?';
}

namespace konga {
TextUnicodeConversion::TextUnicodeConversion(void)
{
}

TextUnicodeConversion::~TextUnicodeConversion(void)
{
}

int TextUnicodeConversion::InvalidCharacterConverted() const
{
	return howmanyinvalidcharacter;
}
std::string	TextUnicodeConversion::ConvertUnicodeToUtf8	(const unsigned int codepoint)
{                                 
	this->codepoint = codepoint;
	std::string utf8string=ConvertUnicodeToUtf8();
	return utf8string;
}
std::string	TextUnicodeConversion::ConvertUnicodeToUtf8	()
{                                 
	std::string utf8string;
	                           
	isinvalid = !(codepoint <=UTF8_4);
	if (codepoint <=UTF8_1) 
	{        
			utf8string.resize (1);
			utf8string[0] = codepoint;
			isinvalid=false;
	}
	else if (codepoint <=UTF8_2)
	{                      
		utf8string.resize (2);
		utf8string[0] = ((codepoint & (UTF8MASK_2) ) >>6 )+ (0x40) + UTF8Byte;
		utf8string[1] = ((codepoint & (UTF8MASK_1) )) + UTF8Byte;
	}
	else if (codepoint <=UTF8_3)
	{
		utf8string.resize (3);
		utf8string[0] = ((codepoint & (UTF8MASK_3) ) >>12 ) + (0x60) + UTF8Byte;
		utf8string[1] = ((codepoint & (UTF8MASK_2) ) >>6 )+ UTF8Byte;
		utf8string[2] = ((codepoint & (UTF8MASK_1) ) )+ UTF8Byte;
	}
	else if (codepoint <=UTF8_4)
	{
		int j=(codepoint & (UTF8MASK_4) ) >> 18 ; 
		j+=(0x70) + UTF8Byte;
		utf8string.resize (4);
		utf8string[0] = ((codepoint & (UTF8MASK_4) ) >> 18)+ (0x70) + UTF8Byte;
		utf8string[1] = ((codepoint & (UTF8MASK_3) ) >> 12) + UTF8Byte;
		utf8string[2] = ((codepoint & (UTF8MASK_2) ) >> 6) + UTF8Byte;
		utf8string[3] = ((codepoint & (UTF8MASK_1) )) + UTF8Byte;
	}
	else
	{
		utf8string.resize (1);
		utf8string[0] = InvalidCharacter;	
	}
	return utf8string;
}
std::wstring TextUnicodeConversion::ConvertUnicodeToUtf16	(const unsigned int codepoint)
{
	this->codepoint =codepoint;
	std::wstring utf16string=ConvertUnicodeToUtf16();
	return utf16string;
}
std::wstring TextUnicodeConversion::ConvertUnicodeToUtf16	()
{
	std::wstring utf16string;
	utf16string.resize (1);
	isinvalid = true;
	if (codepoint<=0xFFFF)
	{
		isinvalid=(codepoint>=0xD800 && codepoint<=0xDFFF);
		utf16string[0]=(isinvalid)? (wchar_t) InvalidCharacter : codepoint;
	}
	else if (codepoint <=UTF16)
	{
		isinvalid=false;
		utf16string.resize (2);
		unsigned int utf16value = codepoint - 0x10000;
		unsigned int pair0 = ((utf16value) >>10 ) + 0xD800;
		unsigned int pair1 = ((utf16value & 0x3FF))+ 0xDC00;
		utf16string[0] = pair0;
		utf16string[1] = pair1;
	}
	else
	{
		utf16string[0]=(wchar_t) InvalidCharacter;
	}
	return utf16string;
}
unsigned int TextUnicodeConversion::ConvertUtf8ToUnicode	(const std::string& utf8string)
{                

	codepoint=INVALIDCODEPOINT;         
	isinvalid = true;
	if (utf8string.size() <1 ) return codepoint;

	const unsigned char leadingcharacter = static_cast<const unsigned char>(utf8string[0]);
	if (leadingcharacter<=UTF8_1)
	{
		codepoint=leadingcharacter;
		isinvalid = false;
		return codepoint;
	}
	int howmanybytes = Utf8Bytes(leadingcharacter);
	if (utf8string.size()<howmanybytes || howmanybytes<1 ) return codepoint;

	switch(howmanybytes)
	{                           
		case 2:
			if (IsInValidSubsequentUtf8Byte(utf8string[1])) return codepoint;
			break;
		case 3:
			if (IsInValidSubsequentUtf8Byte(utf8string[1])) return codepoint;
			if (IsInValidSubsequentUtf8Byte(utf8string[2])) return codepoint;
			break;
		case 4:
			if (IsInValidSubsequentUtf8Byte(utf8string[1])) return codepoint;
			if (IsInValidSubsequentUtf8Byte(utf8string[2])) return codepoint;
			if (IsInValidSubsequentUtf8Byte(utf8string[3])) return codepoint;
			break;
		default:
			return codepoint;
			break;
	}
	                           
	isinvalid = false;
	unsigned int part1, part2,part3,part4;
	switch(howmanybytes)
	{
		case 2:
			part1 = ((utf8string[0] & 0x1F) <<6 );
			part2 = (utf8string[1] & 0x3F) ;
			codepoint = part1 + part2;
			break;
		case 3:
			part1 = ((utf8string[0] & 0x0F) <<12 );
			part2 = (utf8string[1] & 0x3F) <<6;
			part3 = (utf8string[2] & 0x3F) ;
			codepoint = part1 + part2 + part3;
			break;
		case 4:
			part1 = ((utf8string[0] & 0x07) <<18 );
			part2 = (utf8string[1] & 0x3F) <<12;
			part3 = (utf8string[2] & 0x3F) <<6;
			part4 = (utf8string[3] & 0x3F) ;
			codepoint = part1 + part2 + part3 + part4;
			break;
		default:                         
			isinvalid=true;
			break;
	}
	return codepoint;
}
bool TextUnicodeConversion::IsInValidSubsequentUtf8Byte(const unsigned char subsequentchar)
{          
	
	const unsigned int subsquentmask=0xC0;
	const unsigned int subsquentvaluemask=0x80;
	const unsigned int maskedvalue = subsquentmask & subsequentchar;
	bool isinvalidchar = (subsquentvaluemask != maskedvalue);
	return isinvalidchar;
}

int TextUnicodeConversion::Utf8Bytes(const unsigned char leadingcharacter)
{
	if (leadingcharacter<=UTF8_1) return 1;
	if (leadingcharacter>=0xC0 && leadingcharacter<0xE0) return 2;
	if (leadingcharacter>=0xE0 && leadingcharacter<0xF0) return 3;
	if (leadingcharacter>=0xF0 && leadingcharacter<0xF8) return 4;
	return 4; 
	                          
}

unsigned int TextUnicodeConversion::ConvertUtf16ToUnicode	(const std::wstring& utf16string)
{
	isinvalid = true;
	codepoint = INVALIDCODEPOINT;
	if (utf16string.size()<1) return codepoint;
	unsigned int pair0 = utf16string[0];
	if ((pair0<0xD800|| pair0>0xDFFF) )             
	{
		codepoint = pair0;
		isinvalid=false;
		return codepoint;
	}
	if (utf16string.size() < 2  ) return codepoint;
	                                                               
	if ( (pair0<0xD800) ||  (pair0 >= 0xDC00) ) return codepoint;
	unsigned int pair1 = utf16string[1];
	if ( (pair1<0xDC00) ||  (pair1 >0xDFFF) ) return codepoint;
	isinvalid=false;
	codepoint = (pair0 - 0xD800) ;
	codepoint <<=10;
	codepoint += (pair1 - 0xDC00);
	codepoint += 0x10000;
	return codepoint;
}

std::string	TextUnicodeConversion::ConvertLatin1ToUtf8	(const std::string& latin1string)
{
	std::string utf8string;
	utf8string.reserve (latin1string.size ());
	for (size_t i=0;i<latin1string.size ();i++)
	{
		codepoint=static_cast<unsigned char>(latin1string[i]);
		utf8string.append (ConvertUnicodeToUtf8());
	}
#ifdef _DEBUG
	size_t originalsize=latin1string.size();
	size_t calculatedsize=EncodingStringUtils::Utf8CharacterSize(utf8string);
	if (originalsize!=calculatedsize)
	{		
			std::stringstream error;
			error<<"File="<<__FILE__<<"Line="<<__LINE__<<std::endl;
			error<<"Unicode convert function or length calculation is wrong! Please fix it"<<std::endl;
			throw konga::CrException(error.str());
	}
#endif
	return utf8string;
}
char TextUnicodeConversion::GetInvalidCharacter()
{
	howmanyinvalidcharacter++;
	return InvalidCharacter;
}

std::string	TextUnicodeConversion::ConvertUtf8ToLatin1	(const std::string& utf8string)
{
	howmanyinvalidcharacter=0;
	std::string latin1string;
	latin1string.reserve(utf8string.size ());
	unsigned char latin1character;
	unsigned char leadingcharacter;
	for (size_t i=0;i<utf8string.size ();i++)
	{
		leadingcharacter = utf8string[i];
		if (leadingcharacter<=UTF8_1)
		{
			latin1character = leadingcharacter;
		}
		else
		{
			int howmanybytes = Utf8Bytes(leadingcharacter);
			if (howmanybytes>1)
			{
				std::string oneutf8character=utf8string.substr (i,howmanybytes);
				i +=(howmanybytes - 1);      
				codepoint = ConvertUtf8ToUnicode(oneutf8character);
				latin1character = (codepoint>0xFF) ? GetInvalidCharacter() : codepoint;
			}
			else
			{
				latin1character=GetInvalidCharacter();
			}
		}
		latin1string.append(1,latin1character);
	}
	return latin1string;

}
std::string	TextUnicodeConversion::ConvertUtf16ToUtf8	(const std::wstring& utf16string)
{
	std::string utf8string;
	howmanyinvalidcharacter=0;
	wchar_t utf16character;
	for (size_t i=0;i<utf16string.size ();i++)
	{
		utf16character = utf16string[i];
		if (utf16character < 0xD800 || utf16character >0xDFFF)
		{
			codepoint=utf16character;
		}
		else
		{
			codepoint=ConvertUtf16ToUnicode(utf16string.substr (i,2));
			i++;
		}

		if (codepoint >UTF8_4)
		{
			utf8string.append (1,GetInvalidCharacter());
		}
		else
		{
			utf8string.append (ConvertUnicodeToUtf8(codepoint));
		}
	}

#ifdef _DEBUG
	size_t originalsize=utf16string.size();
	size_t calculatedsize=EncodingStringUtils::Utf8CharacterSize(utf8string);
	if (originalsize!=calculatedsize)
	{		
			std::stringstream error;
			error<<"File="<<__FILE__<<"Line="<<__LINE__<<std::endl;
			error<<"Unicode convert function or length calculation is wrong! Please fix it"<<std::endl;
			throw konga::CrException(error.str());
	}
#endif
	return utf8string;
}
std::wstring TextUnicodeConversion::ConvertUtf8ToUtf16	(const std::string& utf8string)
{
	std::wstring utf16string;
	howmanyinvalidcharacter=0;
	utf16string.reserve(utf8string.size ());
	unsigned char leadingcharacter;
	for (size_t i=0;i<utf8string.size ();i++)
	{
		leadingcharacter = utf8string[i];
		if (leadingcharacter<=UTF8_1)
		{
			utf16string.append (1,leadingcharacter);
		}
		else
		{
			int howmanybytes = Utf8Bytes(leadingcharacter);
			if (howmanybytes>1)
			{
				std::string oneutf8character=utf8string.substr (i,howmanybytes);
				i +=(howmanybytes - 1);
				codepoint = ConvertUtf8ToUnicode(oneutf8character);
				std::wstring oneutf16character = ConvertUnicodeToUtf16(codepoint);
				if (isinvalid)
				{
					utf16string.append (1,GetInvalidCharacter());
				}
				else
				{
					utf16string.append (oneutf16character);
				}
			}
			else
			{
				utf16string.append (1,GetInvalidCharacter());
			}
		}
		                                                      
	}
	return utf16string;
}


}
