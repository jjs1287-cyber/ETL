/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */

#ifndef __CRCLEANUP_MISC__H
#define __CRCLEANUP_MISC__H

#include <string>

namespace CleanupUtils
{
	time_t GetCurrentTime();
	std::string toUTCString(time_t timeNum);
	std::string toLocalTimeString(time_t timeNum);
	bool CompareDate(std::string oldFileTime, std::string timeStr);
	time_t getDateTime(const std::string& stDateTime);
	double GetCleanupFrequency();
	std::string GetCleanupServiceDir();
}

#endif                       


