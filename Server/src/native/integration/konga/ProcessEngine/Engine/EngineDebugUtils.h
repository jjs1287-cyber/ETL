/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef ENGINE_DEBUG_UTILS_H
#define ENGINE_DEBUG_UTILS_H

#include <konga/Utils/util.h>

#include "IncomingMsg.h"

bool CheckBreakpoint(IncomingMsg* pmsg, long activityId );

void DumpDataElements(IncomingMsg* pmsg, const std::string& stHeader, bool bPrintDataElements = true);
void DumpOneDataElement(IncomingMsg* pmsg, const TaskDataElement& tde);
void DumpActivity(IncomingMsg* pmsg, const std::string& stHeader, double totalTime = 0);

#define DUMPTIMINGS

class AutoDebugDumper {
public:
	AutoDebugDumper(IncomingMsg* pmsg, const std::string& s)
		#ifdef DUMPTIMINGS
		 : m_s(s), m_pmsg(pmsg)
		#endif               
	{
		#ifdef DUMPTIMINGS
		DumpActivity(m_pmsg, m_s + " BEGIN");
		#endif               
	}
	~AutoDebugDumper()
	{
		#ifdef DUMPTIMINGS
		DUMPTIMINGS DumpActivity(m_pmsg, m_s + " END", m_clock.getSeconds());
		#endif               
	}
private:
	#ifdef DUMPTIMINGS
	std::string m_s;
	IncomingMsg* m_pmsg;
	konga::Clock m_clock;
	#endif               
};

#endif                        

