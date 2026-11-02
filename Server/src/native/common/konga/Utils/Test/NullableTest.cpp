/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <iostream>
#include <string>

#include <konga/Utils/Nullable.h>
#include <konga/Utils/NullableString.h>

using namespace konga;
using namespace std;

void echo(const std::string& stString) {
	cout << stString << endl;
}

int testNullable() {
	Nullable<int> i(0);
	if ( i == null )
		cout << "should not be null..." << endl;

	Nullable<int> ii = null;
	if ( ii != null )
		cout << "Should be null..." << endl;

	Nullable<int> iii(null);
	if ( iii != null )
		cout << "Should be null..." << endl;

	i = ii;
	if ( i != null )
		cout << "Should be null..." << endl;

	iii = 123;
	ii = iii;
	if ( ii == iii )
		cout << ii << " = " << iii << endl;
	else
		cout << "Should be equal!!";

	iii = null;
	if ( iii != null )
		cout << "Should be null..." << endl;

	iii = 0;

	if ( *iii != 0 )
		cout << "Should be zero" << endl;


	Nullable<std::string> stTest(std::string("hello"));
	cout << (*stTest).c_str() << endl;
	cout << static_cast<std::string>(stTest).c_str() << endl;

	stTest = 0;
	if ( stTest == 0 ) {
		     
	}
	else
		cout << "error!" << endl;

	try {
		cout << (*stTest).c_str() << endl;
		cout << "This should throw an exception!" << endl;
	}
	catch ( const NullableException& ) {
		                    
	}

	stTest = string("world");
	cout << *stTest << endl;

	                            
	Nullable<std::string> stTest2(stTest);
	cout << *stTest2 << endl;

	stTest = string("new world");

	                           
	stTest2 = stTest;
	cout << *stTest2 << endl;

	                         
	NullableString s = string("hello");
	cout << *s << endl;

	if ( s == 0 )
		cout << "should not be null" << endl;

	if ( s != "hello" )
		cout << "should be equal" << endl;

	s = 0;
	if ( s != 0 )
		cout << "error" << endl;

	s = "";
	NullableString ss(s);
	NullableString sss = ss;

	sss = "Hello World";
	ss = sss;
	cout << *ss << endl;
	if ( ss != sss )
		cout << "The strings should be equal!!" << endl;

	NullableString& ref = ss;
	echo(*ref);

	const NullableString& const_ref = ref;
	echo(const_ref.const_ref());

	if ( ss != 0 ) {
		ss = 0;
	}

	if ( ss != 0 )
		cout << "should be null..." << endl;

	return 0;
}

