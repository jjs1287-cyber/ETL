/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#ifndef KONGA_CharacterEncodingConversion_EncodingStringUtilsTest_H
#define KONGA_CharacterEncodingConversion_EncodingStringUtilsTest_H

#include <konga/CharacterEncodingConversion/EncodingStringUtils.h>

namespace konga {

class EncodingStringUtilsTest
{
public:
	EncodingStringUtilsTest(void);
	~EncodingStringUtilsTest(void);
	static bool Test_Utf8CharacterSize(const std::string& utf8string,const std::size_t originalsize);
	static bool Test_Utf8ByteLengthFromFile(const std::string& filename);
                                                                   
};
}

#endif                                                              