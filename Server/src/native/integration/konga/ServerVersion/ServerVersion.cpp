/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */

#include <string.h>		         

#include <iostream>		             

#include <konga/Utils/FileUtils.h>	                

#include "../Version/VersionDefs.h"

#include "ServerVersion.h"

using namespace konga;
using std::string;

namespace {
	                                          
	const int iMAJOR_VERSION = MAJOR_VERSION;
	const int iMINOR_VERSION = MINOR_VERSION;
	const int iPATCH_LEVEL = PATCH_LEVEL;
	const int iBUILD = BUILD_NUMBER;

	const string stVersionComment(VERSION_COMMENT);

	const string stBuildDate(__DATE__);
	const string stBuildTime(__TIME__);
}

Version konga::GetServerVersion() {
	                                                                   
	return Version(iMAJOR_VERSION, iMINOR_VERSION, iPATCH_LEVEL, iBUILD, stVersionComment);
                                                                
}


bool konga::CommandLineContainsVersion(int argc, char* argv[]) {
	using std::cout;
	using std::endl;
	const char pcVersion[] = "--version";

	for ( int i = 1; i < argc; ++i ) {
		if (strcmp(argv[i], pcVersion) == 0 ) {
			cout << GetLeafPath(argv[0]) << " Version "
				<< GetServerVersion().GetLongVersionString() << endl;
			cout << COPYRIGHT << endl;
			cout << "Built on " << stBuildDate << " " << stBuildTime << endl << endl;
			return true;
		}
	}
	return false;
}

