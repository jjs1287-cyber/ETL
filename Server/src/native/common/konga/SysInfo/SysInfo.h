/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */

#ifndef KONGA_SYS_INFO_H
#define KONGA_SYS_INFO_H

#include <string>

namespace konga {

	                                                                                           
	std::string GetPlatform();

	                                                                                              
	                                               
	std::string GetOsName();

	                                                                                         
	std::string GetOsVersion();

	                                                                                                      
	std::string GetPrettyOsString();

	                                                                      
	std::string GetPrettySysInfoString();

	void GetMemoryInfo(double& fGbRamInUse, double& fGbRamAvailable, double& fGbRamTotal,
					double& fGbSwapInUse, double& fGbSwapAvailable, double& fGbSwapTotal,
					double& fGbDiskInUse, double& fGbDiskAvailable, double& fGbDiskTotal);

	void GetCpuInfo(std::string& sVendor, std::string& sArchitecture, std::string& sProduct, unsigned long& nCores);

	std::string getMachineName();
	std::string getAccountName();

}                   

#endif                    
