/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#include <iostream>

#include "TextUnicodeConversionTest.h"
#include "Utf8Tests.h"

using namespace konga;
using namespace std;

int test1();

int main(int argc, char* argv[]) {
                           
                              
	printHtmlDocForEncodings("c:/tmp/SupportedEncodings.html");
}

int test1() {
	int ret=TextUnicodeConversionTest::TestAllUnicodes();
	cout<<"ret="<<ret<<endl;

	return 0;
}

