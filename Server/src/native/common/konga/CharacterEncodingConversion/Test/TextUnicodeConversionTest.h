/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#ifndef KONGA_CharacterEncodingConversion_TextUnicodeConversionTest_H
#define KONGA_CharacterEncodingConversion_TextUnicodeConversionTest_H

#include <konga/CharacterEncodingConversion/TextUnicodeConversion.h>


namespace konga {

class TextUnicodeConversionTest
{
public:
	TextUnicodeConversionTest(void);
public:
	~TextUnicodeConversionTest(void);
	static int TestSpecialUnicode();
	static int TestAllUnicodes();
};

}
#endif                                                                
