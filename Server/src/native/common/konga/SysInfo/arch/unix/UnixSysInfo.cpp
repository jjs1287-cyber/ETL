/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */

#include <sys/sysinfo.h>
#include <sys/utsname.h>                
#include <errno.h>
#include <stdio.h>

#include <konga/Utils/AutoFileCleanup.h>
#include <konga/Utils/StringUtils.h>
#include <konga/Utils/CrException.h>

#include "../../SysInfo.h"

using namespace konga;
using std::string;


string konga::GetOsName() {
	                                                 
	return konga::GetPlatform();
}

string konga::GetOsVersion() {
	struct utsname buf;
	if ( uname(&buf) >= 0 ) {
		if ( buf.version ) {
			return string( buf.release );
		}
	}
	return "unknown";
}


string konga::GetPlatform() {
	struct utsname buf;
	if ( uname(&buf) >= 0 ) {
		if ( buf.sysname ) {
			return string( buf.sysname );
		}
	}
	return "unknown-unix";
}

void konga::GetMemoryInfo(double& fGbRamInUse, double& fGbRamAvailable, double& fGbRamTotal,
			double& fGbSwapInUse, double& fGbSwapAvailable, double& fGbSwapTotal,
			double& fGbDiskInUse, double& fGbDiskAvailable, double& fGbDiskTotal)
{
	fGbRamInUse = 0, fGbRamAvailable = 0, fGbRamTotal = 0,
	fGbSwapInUse = 0, fGbSwapAvailable = 0, fGbSwapTotal = 0,
	fGbDiskInUse = 0, fGbDiskAvailable = 0, fGbDiskTotal = 0;

	struct sysinfo info;
	if ( sysinfo(&info) != 0) {
		return;
	}

	                                          
	                                                                        
	double f = info.mem_unit;
	f /= (1024*1024*1024);

	fGbRamTotal = f*info.totalram;
	fGbRamAvailable = f*info.freeram;
	fGbRamInUse = fGbRamTotal - fGbRamAvailable;

	fGbSwapTotal = f*info.totalswap;
	fGbSwapAvailable = f*info.freeswap;
	fGbSwapInUse = fGbSwapTotal - fGbSwapAvailable;
}

void konga::GetCpuInfo(string& sVendor, string& sArchitecture, string& sProduct, unsigned long& nCores) {
	sVendor.clear();
	sArchitecture.clear();
	sProduct.clear();
	nCores = 1;

#ifndef _DEBUG
	try {
#endif

	FILE* f = fopen("/proc/cpuinfo", "r");
	if ( f == 0 )
		return;
	AutoFileHandle adopt_f(f);
	
	char buf[256];
	int nProcs = 0;
	while ( fgets(buf, 255, f) != 0 ) {
		if ( buf == 0 || *buf == 0 )
			continue;

		string line(buf);
		if ( *(line.end()-1) == '\n' ) {
			line.erase(line.end()-1);
		}
		if ( line.empty() )
			continue;

		string::size_type ipos = line.find(":");
		if ( ipos == string::npos )
			continue;

		string key( TrimString(line.substr(0, ipos)) );
		key = TrimString(key, '\t');
		ipos = line.find_first_not_of(" \t", ipos+1);
		if ( ipos == string::npos )
			continue;

		string value( TrimString(line.substr(ipos)) );
		if ( value.empty() )
			continue;

		if ( key == "processor" ) {
			++nProcs;
		}
		else if ( sVendor.empty() && key == "vendor_id" ) {
			sVendor = value;
		}
		else if ( sProduct.empty() && key == "model name" ) {
			sProduct = value;
		}
	}
	if ( nProcs > 0 )
		nCores = nProcs;

#ifndef _DEBUG
	} catch ( ... ) {
		                                  
	}
#endif
}

string konga::getMachineName()
{
	char buf[512];
	if( gethostname(buf, sizeof(buf)) == 0 )
		return buf;
	else
		return "localhost";
}

std::string konga::getAccountName()
{
	char buf[512];
	buf[0] = 0;
	if( getlogin_r(buf, sizeof(buf)) == 0 )
		return string(buf);
	else {
		std::string stErr = "Failed to retrieve user name: ";
		char *pc = strerror(errno);
		char itoabuf[64];
		stErr += pc ? pc : konga::itoa(errno, itoabuf, 10);
		throw CrException(stErr);
	}	
}

