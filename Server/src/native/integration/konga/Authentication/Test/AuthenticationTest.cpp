/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
                                                                                
  

#include <string>
#include <iostream>

#include <konga/Authentication/Authentication.h>

using namespace konga;
using namespace konga::Authentication;
using namespace std;

int main(int argc, char* argv[])
{
	string stUser("Win_User");
	string stPassword("*****");

	if ( !konga::Authentication::IsValidUser(stUser, stPassword) ) {
		cout << "Failed to authenticate user " << stUser << endl;
		return 1;
	}
	cout << "Authentication succeeded" << endl;

	try {
		UserInfo ui = GetValidUser(stUser, stPassword);
	}
	catch( const AuthenticationException& ex ) {
		cout << ex.GetErrorString() << endl;
	}

	return 0;
}

