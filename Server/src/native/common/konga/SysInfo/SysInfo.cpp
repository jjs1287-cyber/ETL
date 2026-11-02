/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */

#include <konga/Utils/StringUtils.h>

#include "SysInfo.h"

using namespace konga;
using std::string;

string konga::GetPrettyOsString()
{
	return string(GetOsName() + " " + GetOsVersion());
}

string konga::GetPrettySysInfoString() {
	string sVendor, sArchitecture, sProduct;
	unsigned long nCores=0;
	GetCpuInfo(sVendor, sArchitecture, sProduct, nCores);
	string s;
	if ( !sVendor.empty() || !sArchitecture.empty() || !sProduct.empty() ) {
		s = "CPU: " + sVendor + " " + sArchitecture + " " + sProduct + ", " + toString(nCores) + " cores total.\n";
	}

	double fGbRamInUse = 0, fGbRamAvailable = 0, fGbRamTotal = 0, 
		fGbSwapInUse = 0, fGbSwapAvailable = 0, fGbSwapTotal = 0,
		fGbDiskInUse = 0, fGbDiskAvailable = 0, fGbDiskTotal = 0;

	GetMemoryInfo(fGbRamInUse, fGbRamAvailable, fGbRamTotal,
				fGbSwapInUse, fGbSwapAvailable, fGbSwapTotal,
				fGbDiskInUse, fGbDiskAvailable, fGbDiskTotal);

	if ( fGbRamInUse != 0 ) {
		s += "RAM: " + toString(fGbRamInUse, 4) + " GB in use, " + toString(fGbRamAvailable, 4) + " GB available, " + toString(fGbRamTotal, 4) + " GB total.\n";
	}
	if ( fGbSwapInUse != 0 ) {
		s += "Swap: " + toString(fGbSwapInUse, 4) + " GB in use, " + toString(fGbSwapAvailable, 4) + " GB available, " + toString(fGbSwapTotal, 4) + " GB total.\n";
	}
	if ( fGbDiskInUse != 0 ) {
		s += "Disk (where ETLForge is installed): " + toString(fGbDiskInUse, 4) + " GB in use, " + toString(fGbDiskAvailable, 4) + " GB available, " + toString(fGbDiskTotal, 4) + " GB total.\n";
	}
	return s;
}

