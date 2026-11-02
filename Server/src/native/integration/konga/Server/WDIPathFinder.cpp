/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */

#include <konga/Utils/FileUtils.h>
#include <konga/Utils/Settings.h>
#include <konga/Utils/KString.h>

#include "WDIPathFinder.h"

using namespace konga;
using std::string;

bool konga::WDIPathFinder::GetWDIPath(KString& WDIPath) {
    WDIPath=konga::GetServerHomeWithSlash();
	return konga::IsDirectory((const char*)WDIPath);
}

bool konga::WDIPathFinder::GetWDIPath(string& WDIPath) {
    WDIPath=konga::GetServerHomeWithSlash();
	return konga::IsDirectory(WDIPath);
}

bool konga::WDIPathFinder::GetWDIDebugPath(KString& WDIDebugPath) {
	string stWDIDebugPath;
	bool bIsDir = GetWDIDebugPath(stWDIDebugPath);
	WDIDebugPath = stWDIDebugPath;
	return bIsDir;
}

bool konga::WDIPathFinder::GetWDIDebugPath(string& WDIDebugPath) {
	string tmp(konga::GetOptionAsString("TransformationEngine", "WriteLogDir", ""));
	if( tmp.empty() ) {
		if ( !GetWDIPath(tmp) )
			return false;
		tmp += "DataInterchange/Temp/Debug/";
	}
	else {
		konga::AppendPathSeparator(tmp);
	}

	try {
		if( !konga::IsDirectory(tmp) ) konga::kCreateDirectory(tmp);
		if ( konga::IsDirectory(tmp) ) {
			WDIDebugPath = tmp;
			return true;
		}
	}
	catch(...) {
		                            
	}
	return false;
}
