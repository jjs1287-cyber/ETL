/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */

#include <iostream>

#include <konga/SysInfo/SysInfo.h>

using namespace std;
using namespace konga;

int main(int argc, char* argv[])
{

	cout << GetPlatform() << endl;
	cout << GetOsName() << endl;
	cout << GetOsVersion() << endl;
	cout << GetPrettyOsString() << endl;
	cout << endl;
	cout << GetPrettySysInfoString() << endl;
	cout << getMachineName() << endl;
	cout << getAccountName() << endl;

	return 0;
}

