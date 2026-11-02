/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef ETLFORGE_UTILS_TRACELOG_H
#define ETLFORGE_UTILS_TRACELOG_H

                  

#ifdef TRACE_ON
#include <stdio.h>	          
#include <stdarg.h>	                    

                                   
#include <konga/Utils/DateUtils.h>
#include <konga/Utils/FileUtils.h>
#include <konga/Utils/ErrorResolver.h>
#include <konga/Utils/CrException.h>
#endif

namespace konga {
class TraceLog {
public:
	TraceLog(const std::string& stMethodName) : m_log(0) {
#ifdef TRACE_ON
		const konga::CrDateTime SysTime;
		char buf[200];
		sprintf(buf,"%s_%02d_%02d_%02d_%06d.log", stMethodName.c_str(), SysTime.GetHour(), SysTime.GetMinute(), SysTime.GetSecond(), SysTime.GetMS());
		std::string stTempDir=konga::getTmpDir();
		std::string stLogFileName=stTempDir+buf;
		m_log=fopen(stLogFileName.c_str(),"w+");
		if(m_log==0){
			throw CrFatalException(E_FAIL, "can't open log file for write\nLogFileName="+stLogFileName+"\nError:"+ErrorResolver().Resolve(errno));
		}
		trace("%s %04d-%02d-%02d %02d:%02d:%02d.%06d\n",stMethodName.c_str(),SysTime.GetYear(),SysTime.GetMonth(),SysTime.GetDay(),SysTime.GetHour(),SysTime.GetMinute(),SysTime.GetSecond(),SysTime.GetMS());
#endif            
	}

	~TraceLog(){
		if(m_log)fclose(m_log);
	}

	void trace(const char*sMessage, ...)
	{
#ifdef TRACE_ON
		if(m_log==0)return;
		va_list argList;
		va_start(argList, sMessage);

		if(vfprintf(m_log, sMessage, argList) < 0)
		{
			throw CrFatalException(E_FAIL, ErrorResolver().Resolve(GetLastError()));
		}
		fflush(m_log);
		va_end(argList);
#endif             
	}

private:
	FILE* m_log;

};
}

#endif                                     

