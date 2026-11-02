/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */

#include <stdio.h>
#include <time.h>
#include <sys/timeb.h>

#include <string>
#include <boost/thread/mutex.hpp>

#include <konga/Utils/AutoFileCleanup.h>	                 
#include <konga/Utils/Settings.h>
#include <konga/Utils/FileUtils.h>

#include "../ComponentInfo.h"

#include "IncomingMsg.h"
#include "EngineDebugUtils.h"

  
                                                                               
  
static std::string GetDumpFileName(IncomingMsg* pmsg)
{
	static std::string stDebugDumpDir;
	if ( stDebugDumpDir.empty() ) {
		stDebugDumpDir = ::GetOptionAsString(stPROCESSENGINE_COMPONENT_NAME, "DebugDumpDir","");
		if( stDebugDumpDir.empty() )
			stDebugDumpDir = konga::getTmpDir();
		konga::AppendPathSeparator(stDebugDumpDir);
	}

	std::string stFileName(stDebugDumpDir);
	stFileName += "WFDump_";
	stFileName += pmsg->GetProcessName();
	stFileName += "_";
	stFileName += toString(pmsg->m_piid);
	stFileName += ".txt";
	return stFileName;
}

  
                                                                   
  
static std::string GetTimeStamp()
{
	struct timeb timebuffer;
	ftime( &timebuffer );
	char *timeline = ctime( & ( timebuffer.time ) );
	char buf[128];
	sprintf(buf, "%.8s.%.3hu", &timeline[11], timebuffer.millitm);
	return buf;
}

boost::mutex g_DumpLock;

void DumpDataElements(IncomingMsg* pmsg, const std::string& stHeader, bool bPrintDataElements           )
{
	if( !pmsg->IsDebugEnabled() ) return;
	boost::mutex::scoped_lock adopt_lock(g_DumpLock);

	const std::string& stFileName = GetDumpFileName(pmsg);
	FILE* f = fopen(stFileName.c_str(), "a+");
	if( !f ) return;
	AutoFileHandle adopt_f(f);

	std::string stTimeStamp = GetTimeStamp();

	                
	  
	fprintf(f, "[%s] - (%s) - %s - %s\n",
		stTimeStamp.c_str(),
		pmsg->GetRouteId().c_str(),
		pmsg->GetTaskName(),
		stHeader.c_str()
		);

	  
	                       
	  
	                                                             
	for(IncomingMsg::name2de_constit it = pmsg->m_elements.begin(); it != pmsg->m_elements.end(); ++it)
	{
		switch(it->second->m_type)
		{
		case t_text:
			{
			long len = it->second->m_value.length();
			if( len <= 50 )
				fprintf(f, "%s = \"%s\"\n", it->second->m_name.c_str(), 
					it->second->m_value.empty()?"":it->second->m_value.c_str());
			else
				fprintf(f, "%s is of type long string. Its length is %ld. The first 50 charcters are \"%s\".\n",
					it->second->m_name.c_str(), len, it->second->m_value.substr(0,49).c_str());
			break;
			}
		case t_string:
			fprintf(f, "%s = \"%s\"\n", it->second->m_name.c_str(), it->second->m_value.empty()?"":it->second->m_value.c_str());
			break;
		default:
			fprintf(f, "%s = %s\n", it->second->m_name.c_str(), it->second->m_value.empty()?"":it->second->m_value.c_str());
		}
	}
}

void DumpOneDataElement(IncomingMsg* pmsg, const TaskDataElement& tde)
{
	if( !pmsg->IsDebugEnabled() ) return;
	boost::mutex::scoped_lock adopt_lock(g_DumpLock);

	const std::string& stFileName = GetDumpFileName(pmsg);
	FILE* f = fopen(stFileName.c_str(), "a+");
	if( !f ) return;
	AutoFileHandle adopt_f(f);

	std::string stTimeStamp = GetTimeStamp();

	switch(tde.m_type)
	{
	case t_text:
			{
			long len = tde.m_value.length();
			if( len <= 50 )
				fprintf(f, "[%s] - (%s) - %s = \"%s\"\n", stTimeStamp.c_str(), pmsg->GetRouteId().c_str(),
					tde.m_name.c_str(), tde.m_value.empty()?"":tde.m_value.c_str());
			else
				fprintf(f, "[%s] - (%s) - %s is of type long string. Its length is %ld. The first 50 charcters are \"%s\".\n",
					stTimeStamp.c_str(), pmsg->GetRouteId().c_str(),
					tde.m_name.c_str(), len, tde.m_value.substr(0,49).c_str());
			break;
			}
	case t_string:
		fprintf(f, "[%s] - (%s) - %s = \"%s\"\n", stTimeStamp.c_str(), pmsg->GetRouteId().c_str(),
			tde.m_name.c_str(), tde.m_value.empty()?"":tde.m_value.c_str());
		break;
	default:
		fprintf(f, "[%s] - (%s) - %s = %s\n", stTimeStamp.c_str(), pmsg->GetRouteId().c_str(),
			tde.m_name.c_str(), tde.m_value.empty()?"":tde.m_value.c_str());
	}
}

void DumpActivity(IncomingMsg* pmsg, const std::string& stHeader, double totalTime       )
{
	static int lEnableDump = -1;
	if( lEnableDump == 0 ) return;
	if ( lEnableDump == -1 ) {
		lEnableDump = (::GetOptionAsInt(stPROCESSENGINE_COMPONENT_NAME, "EnableDebugDump",0)==0) ? 0 : 1;
		if( lEnableDump == 0 ) return;
	}

	boost::mutex::scoped_lock adopt_lock(g_DumpLock);

	const std::string& stFileName = GetDumpFileName(pmsg);
	FILE* f = fopen(stFileName.c_str(), "a+");
	if( !f ) return;
	AutoFileHandle adopt_f(f);

	std::string stTimeStamp = GetTimeStamp();

	fprintf(f, "[%s] - (%s) - %s - %s %f \n",
		stTimeStamp.c_str(),
		pmsg->GetRouteId().c_str(),
		pmsg->GetTaskName(),
		stHeader.c_str(),
		totalTime);
}
