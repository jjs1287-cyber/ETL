#include "TextUnicodeConversionTest.h"

namespace konga {

TextUnicodeConversionTest::TextUnicodeConversionTest(void)
{
}

TextUnicodeConversionTest::~TextUnicodeConversionTest(void)
{
}

int TextUnicodeConversionTest::SpecialUnicodeTest()
{
	int errorcode=0;
	TextUnicodeConversion tuc;

	unsigned int codepoint;
	codepoint=0;
	                                        
	std::string utf8=tuc.ConvertUnicodeToUtf8 (codepoint);
	std::wstring utf16=tuc.ConvertUnicodeToUtf16 (codepoint);
	if (utf8.size()!=1) return 1;
	if (utf16.size ()!=1) return 2;
	if (utf8[0]!=codepoint) return 3;
	if (utf16[0]!=codepoint) return 4;

	codepoint=0x7A;
	utf8=tuc.ConvertUnicodeToUtf8 (codepoint);
	utf16=tuc.ConvertUnicodeToUtf16 (codepoint);
	if (utf8.size()!=1) return 5;
	if (utf16.size ()!=1) return 5;
	if (utf8[0]!=codepoint) return 5;
	if (utf16[0]!=codepoint) return 5;

	codepoint=0x7F;
	utf8=tuc.ConvertUnicodeToUtf8 (codepoint);
	utf16=tuc.ConvertUnicodeToUtf16 (codepoint);
	if (utf8.size()!=1) return 6;
	if (utf16.size ()!=1) return 7;
	if (utf8[0]!=codepoint) return 8;
	if (utf16[0]!=codepoint) return 9;

	codepoint=0x80;
	utf8=tuc.ConvertUnicodeToUtf8 (codepoint);
	if (utf8.size()!=2) return 10;
	if ( ((unsigned char)utf8[0])!=0xC2) return 11;
	if ( ((unsigned char)utf8[1])!=0x80) return 12;
	utf16=tuc.ConvertUnicodeToUtf16 (codepoint);
	if (utf16.size ()!=1) return 15;
	if (utf16[0]!=codepoint) return 16;

	codepoint=0xA2;
	utf8=tuc.ConvertUnicodeToUtf8 (codepoint);
	if (utf8.size()!=2) return 30;
	if ( ((unsigned char)utf8[0])!=0xC2) return 31;
	if ( ((unsigned char)utf8[1])!=0xA2) return 32;
	utf16=tuc.ConvertUnicodeToUtf16 (codepoint);
	if (utf16.size ()!=1) return 35;
	if (utf16[0]!=codepoint) return 36;

	codepoint=0x7FF;
	utf8=tuc.ConvertUnicodeToUtf8 (codepoint);
	if (utf8.size()!=2) return 30;
	if ( ((unsigned char)utf8[0])!=0xDF) return 31;
	if ( ((unsigned char)utf8[1])!=0xBF) return 32;
	utf16=tuc.ConvertUnicodeToUtf16 (codepoint);
	if (utf16.size ()!=1) return 35;
	if (utf16[0]!=codepoint) return 36;

	codepoint=0x800;
	utf8=tuc.ConvertUnicodeToUtf8 (codepoint);
	if (utf8.size()!=3) return 100;
	if ( ((unsigned char)utf8[0])!=0xE0) return 101;
	if ( ((unsigned char)utf8[1])!=0xA0) return 102;
	if ( ((unsigned char)utf8[2])!=0x80) return 103;
	utf16=tuc.ConvertUnicodeToUtf16 (codepoint);
	if (utf16.size ()!=1) return 105;
	if (utf16[0]!=codepoint) return 106;

	codepoint=0x20ac;
	utf8=tuc.ConvertUnicodeToUtf8 (codepoint);
	if (utf8.size()!=3) return 110;
	if ( ((unsigned char)utf8[0])!=0xE2) return 111;
	if ( ((unsigned char)utf8[1])!=0x82) return 112;
	if ( ((unsigned char)utf8[2])!=0xAC) return 113;
	utf16=tuc.ConvertUnicodeToUtf16 (codepoint);
	if (utf16.size ()!=1) return 115;
	if (utf16[0]!=codepoint) return 116;

	codepoint=0xD7FF;
	utf8=tuc.ConvertUnicodeToUtf8 (codepoint);
	if (utf8.size()!=3) return 120;
	if ( ((unsigned char)utf8[0])!=0xEd) return 121;
	if ( ((unsigned char)utf8[1])!=0x9F) return 121;
	if ( ((unsigned char)utf8[2])!=0xBF) return 123;
	utf16=tuc.ConvertUnicodeToUtf16 (codepoint);
	if (utf16.size ()!=1) return 125;
	if (utf16[0]!=codepoint) return 126;

	const wchar_t invalidcharacter16=tuc.GetInvalidCharacter ();
	const char invalidcharacter=tuc.GetInvalidCharacter ();

	codepoint=0xD800;
	utf8=tuc.ConvertUnicodeToUtf8 (codepoint);
	if (utf8.size()!=3) return 131;
	if ( ((unsigned char)utf8[0])!=0xED) return 131;
	if ( ((unsigned char)utf8[1])!=0xA0) return 132;
	if ( ((unsigned char)utf8[2])!=0x80) return 133;
	utf16=tuc.ConvertUnicodeToUtf16 (codepoint);
	if (utf16.size ()!=1) return 135;
	if (utf16[0]!=invalidcharacter16) return 136;

	codepoint=0xFFFF;
	utf8=tuc.ConvertUnicodeToUtf8 (codepoint);
	if (utf8.size()!=3) return 140;
	if ( ((unsigned char)utf8[0])!=0xEF) return 141;
	if ( ((unsigned char)utf8[1])!=0xBF) return 142;
	if ( ((unsigned char)utf8[2])!=0xBF) return 143;
	utf16=tuc.ConvertUnicodeToUtf16 (codepoint);
	if (utf16.size ()!=1) return 145;
	if (utf16[0]!=codepoint) return 146;

	codepoint=0x10000;
	utf8=tuc.ConvertUnicodeToUtf8 (codepoint);
	if (utf8.size()!=4) return 200;
	if ( ((unsigned char)utf8[0])!=0xF0) return 201;
	if ( ((unsigned char)utf8[1])!=0x90) return 202;
	if ( ((unsigned char)utf8[2])!=0x80) return 203;
	if ( ((unsigned char)utf8[3])!=0x80) return 204;
	utf16=tuc.ConvertUnicodeToUtf16 (codepoint);
	if (utf16.size ()!=2) return 205;
	if (utf16[0]!=0xD800) return 206;
	if (utf16[1]!=0xDC00) return 207;
	                                      

	return errorcode;
}
int TextUnicodeConversionTest::AllUnicodeTest()
{
	TextUnicodeConversion tuc;
	         
	unsigned int codepoint=0x10FFFF;


	for (unsigned int c=0; c<=codepoint;c++)
	{
		std::string utf8=tuc.ConvertUnicodeToUtf8(c);
		unsigned int cb= tuc.ConvertUtf8ToUnicode(utf8);
		if (cb!=c)
		{
			return c+1;
		}
	}	
	codepoint=0x10FFFD;
	for (unsigned int c=0; c<=codepoint;c++)
	{
		if (c>=0xD800 && c<=0xDFFF) continue;                     
		std::wstring utf16=tuc.ConvertUnicodeToUtf16(c);
		unsigned int cb= tuc.ConvertUtf16ToUnicode(utf16);
		if (cb!=c )
		{
			return c+1;
		}
	}	
	;;
	;;
	return 0;
}

}
