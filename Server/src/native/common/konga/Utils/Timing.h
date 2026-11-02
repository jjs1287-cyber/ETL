/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#ifndef KONGA_TIMING_H
#define KONGA_TIMING_H

#include <stdio.h>

#include <string>
#include <vector>
#include <map>

#include <boost/date_time/posix_time/posix_time.hpp>

namespace konga {

struct TimingEntry
{
	TimingEntry(const std::string& sWhere);
	std::string sWhere;
	boost::posix_time::ptime pt;
};

struct AccumulativeTimingEntry
{
	AccumulativeTimingEntry(const std::string& scope_name)
		: m_nEntries(0), m_nesting(0), m_total_ms(0), m_entry_time(0), m_sScopeName(scope_name) {}

	long m_nEntries;
	long m_nesting;
	long m_total_ms;
	boost::posix_time::ptime* m_entry_time;
	std::string m_sScopeName;
};

class ProfilingKit
{
public:
	ProfilingKit(const std::string& file_name);
	ProfilingKit(ProfilingKit*p, int iThread);
	~ProfilingKit();

	void start(const std::string& sWhere, const std::string& sExtra = "");
	void end(const std::string& sWhere);

	void enterAccumulativeScope(const std::string& group_name, const std::string& scope_name);
	void exitAccumulativeScope(const std::string& group_name, const std::string& scope_name);

private:
	void printIndention();
	void initFile();
	void closeFile();

private:
	FILE* m_timing;
	std::string stFileName;

	std::vector<TimingEntry*> m_timing_stack;

	typedef std::map<std::string, AccumulativeTimingEntry*> AccumulativeTimingsMapT;
	typedef std::map<std::string, AccumulativeTimingsMapT*> AccumulativeTimingGroupsMapT;
	AccumulativeTimingGroupsMapT m_accumulative_timings;
};

class EnterAccumulativeScope {
public:
	EnterAccumulativeScope() : m_pProfilingKit(0) {}
	EnterAccumulativeScope(ProfilingKit* pProfilingKit, const std::string& scope_group, const std::string& scope_name);
	~EnterAccumulativeScope(void);

private:
	ProfilingKit* m_pProfilingKit;
	std::string m_sScopeGroup;
	std::string m_sScopeName;
};

}                   

#endif                  

