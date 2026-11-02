/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <string>

#include <konga/Utils/CrException.h>
#include <konga/WinUtils/RegistryUtils.h>

#include "JavaVersionUtils.h"

using namespace konga;
using namespace std;

namespace {
	const string sJavaRegPath("SOFTWARE\\JavaSoft\\Java Runtime Environment");
}

string konga::GetMaxJavaVersion(bool bCheck64Bit) {
	const vector<string>& javaVersions = konga::EnumRegistryKeys(HKEY_LOCAL_MACHINE, sJavaRegPath, bCheck64Bit);
	string sMaxVersion;
	vector<string>::const_iterator citVersion = javaVersions.begin();
	for(; citVersion != javaVersions.end(); ++citVersion) {
		if( sMaxVersion.empty() || *citVersion > sMaxVersion ) {
			sMaxVersion = *citVersion;
		}
	}
	return sMaxVersion;
}

bool konga::IsJreVersionLargeEnough(const string& sRequiredVersion, bool bCheck64Bit) {
	string sMaxVersion;
	try {
		                                                           
		sMaxVersion = GetMaxJavaVersion(bCheck64Bit);
	} catch(CrException& e) {
		if( e.GetErrorNumber() != 2 )                                                                      
			throw;
	}

	return sMaxVersion >= sRequiredVersion;
}

string konga::GetJavaPath(string sJavaVersion, bool bCheck64Bit) {
	return konga::GetRegistryString(HKEY_LOCAL_MACHINE, sJavaRegPath + "\\" + sJavaVersion, "JavaHome", "", "", bCheck64Bit);
}

string konga::GetJavaRuntimeLib(string sJavaVersion, bool bCheck64Bit) {
	return konga::GetRegistryString(HKEY_LOCAL_MACHINE, sJavaRegPath + "\\" + sJavaVersion, "RuntimeLib", "", "", bCheck64Bit);
}

