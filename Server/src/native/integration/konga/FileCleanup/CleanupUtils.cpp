/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <time.h>

#include <konga/Log/logging.h>
#include <konga/Utils/CrException.h>
#include <konga/Utils/ErrorResolver.h>
#include <konga/Utils/util.h>
#include <konga/Utils/FileUtils.h>
#include <konga/Utils/Settings.h>
#include <konga/Utils/StringUtils.h>
#include <konga/Utils/PortDefs.h>

#include "ComponentInfo.h"
#include "CleanupUtils.h"

using konga::CrException;

time_t CleanupUtils::GetCurrentTime()
{
	time_t now;
	time(&now);
	return now;
}

std::string CleanupUtils::toUTCString(time_t timeNum)
{
	struct tm* utcTime = gmtime(&timeNum);
	char temp[200];
	                                                    
	strftime(temp, 200, "%Y-%m-%d  %H:%M:%S", utcTime);
	return temp;
}

std::string CleanupUtils::toLocalTimeString(time_t timeNum)
{
	if(timeNum==0)timeNum=GetCurrentTime();
	struct tm* lTime = localtime(&timeNum);
	char temp[200];
	                                                    
	strftime(temp, 200, "%Y-%m-%d  %H:%M:%S", lTime);
	return temp;
}

time_t CleanupUtils::getDateTime(const std::string& stDateTime){
	struct tm t;
	int y,m,d,H,M,S;
	sscanf(stDateTime.c_str(),"%d-%d-%d %d:%d:%d",&y,&m,&d,&H,&M,&S);
	t.tm_year=y;
	t.tm_mon=m-1;
	t.tm_mday=d;
	t.tm_hour=H;
	t.tm_min=M;
	t.tm_sec=S;
	time_t r = mktime(&t);
	if(r == (time_t)(-1))throw CrException("CleanupUtils::getDateTime("+stDateTime+") failed");
	return r;
}

double CleanupUtils::GetCleanupFrequency()
{
	return konga::GetOptionAsDouble(konga::stFILE_CLEANUP_COMPONENT_NAME, "FrequencyInHours", 24);
}

std::string CleanupUtils::GetCleanupServiceDir()
{
	std::string sDir=konga::GetOptionAsString(konga::stFILE_CLEANUP_COMPONENT_NAME, "SetupDir", "");
	if(konga::IsDirectory(sDir))return sDir;
	return konga::getAbsolutePath(sDir);
}

bool CleanupUtils::CompareDate(std::string oldFileTime, std::string timeStr)
{
	std::string temp1, temp2;
	std::string::const_iterator a1=oldFileTime.begin(), a2= timeStr.begin();
	                                                                                
	while(a1 != oldFileTime.end()) {
		switch(*a1) {
			case ' ':	case '\t':	case '\n':
				a1++;	continue;
			default:
				temp1 += *a1;	a1++;	break;
		}
	}
	                                                                            
	while(a2 != timeStr.end()) {
		switch(*a2) {
			case ' ':	case '\t':	case '\n':
				a2++;	continue;
			default:
				temp2 += *a2;	a2++;	break;
		}
	}
	if (stricmp(temp1.c_str(), temp2.c_str()) != 0)
		return false;
	else 
		return true;
}


