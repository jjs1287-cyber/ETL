/*
 * Copyright (c) 1998-2006 Jitterbit, Inc.
 *
 */

#include <vector>
#include <string>
#include <iostream>

#include <konga/UiBackend/WebServices/Utils/WsUtils.h>

using namespace konga;
using namespace std;

int main(int argc, char* argv[])
{
	vector<string> v;
	for( int i=0; i < 20; ++i ) {
		v.push_back("This is a string!!");
	}

	xsd__string_Array* pAxisArr = allocate_xsd__string_Array(v);
	int n = 0;
	const xsd__string* pArr = pAxisArr->get(n);

	for( int i = 0; i < n ; ++i ) {
		cout << pArr[i] << endl;
	}

	delete pAxisArr;

	cout << "done" << endl;

	return 0;
}

