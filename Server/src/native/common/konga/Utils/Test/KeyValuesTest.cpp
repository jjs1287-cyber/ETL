/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <string>
#include <iostream>

#include <konga/Utils/KeyValues.h>

using namespace konga;
using namespace std;

int testKeyValues() {
	KeyValues kv;

	const string k1("k1"), v1("v1");
	kv.AddString(k1, "v1");
	if ( !kv.IsKeyDefined(k1) )
		cout << "Error 1 " << endl;
	if ( kv.GetString(k1) != v1 )
		cout << "Error 2 " << endl;

	kv.DeleteKey(k1);
	if ( kv.IsKeyDefined(k1) )
		cout << "Error 3 " << endl;
	if ( kv.GetString(k1) != "" )
		cout << "Error 4 " << endl;

	string k2("k2");
	int v2 = 123456789;

	kv.AddInt(k2, v2);
	if ( !kv.IsKeyDefined(k2) )
		cout << "Error 5 " << endl;
	if ( kv.GetInt(k2) != v2 )
		cout << "Error 6 " << endl;

	                          
	string k3("k3"), v3("v3");
	kv.AddString(k3, v3);
	int i = kv.GetInt(k3);
	cout << i << endl;
	cout << kv.GetLong(k3) << endl;

	return 0;
}

