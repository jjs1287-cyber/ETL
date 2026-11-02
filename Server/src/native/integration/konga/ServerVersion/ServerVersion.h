/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */

#ifndef KONGA_SERVER_VERSION_H
#define KONGA_SERVER_VERSION_H

#include <konga/Utils/Version.h>

namespace konga {

	                                                                                          
	Version GetServerVersion();

	                                                                    
	// If so prints version and copyright information to cout and returns true.
	bool CommandLineContainsVersion(int argc, char* argv[]);
}

#endif                          

