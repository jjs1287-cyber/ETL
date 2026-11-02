#pragma once
#include "TextUnicodeConversion.h"

namespace konga {

class TextUnicodeConversionTest
{
public:
	TextUnicodeConversionTest(void);
public:
	~TextUnicodeConversionTest(void);
	int SpecialUnicodeTest();
	int AllUnicodeTest();
};

}
