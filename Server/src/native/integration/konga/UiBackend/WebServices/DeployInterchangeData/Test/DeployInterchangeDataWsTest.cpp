/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#include <iostream>

#include "../Impl/DeployInterchangeDataImpl.h"

using namespace konga;
using namespace interchange_deploy;
using namespace std;

#ifdef WIN32
const string stUser("Win_User");
#else
const string stUser("emanuel");
#endif
const string stPassword("****");


int testDeployLink();

int main (int argc, char *argv[])
{
	return testDeployLink();
}

int testDeployLink() {
	                                             
	DeployInterchangeDataImpl impl("emanuel", "pwd");
	impl.DeployInterchangeData(0, 0);
	return 0;
}

