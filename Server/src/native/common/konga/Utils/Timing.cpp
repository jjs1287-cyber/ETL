/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#include <cassert>

#include <konga/Utils/Settings.h>
#include <konga/Utils/StringUtils.h>
#include <konga/Utils/FileUtils.h>

#include "Timing.h"

using namespace konga;
using std::string;
using std::vector;

konga::TimingEntry::TimingEntry(const string& sWhere0)
	: sWhere(sWhere0),
	pt(boost::posix_time::microsec_clock::local_time()) { }

ProfilingKit::ProfilingKit(const string& file_name)
	: m_timing(0), stFileName(file_name)
{
	if(konga::GetOptionAsBoolean("TransformationEngine","TimingOn",false)){
		initFile();
	}
}

ProfilingKit::ProfilingKit(konga::ProfilingKit*p, int iThread) : m_timing(0) {
	if(p->m_timing){
		stFileName=konga::GetSequenceFileName(p->stFileName, "_T", iThread);
		initFile();
	}
}

void ProfilingKit::initFile() {
	if(m_timing==0) {
		m_timing=fopen(stFileName.c_str(),"w+");
		fprintf(m_timing, "%s", "<?xml version=\"1.0\"?>\n");
		fprintf(m_timing, "%s", "<ProfilingResult>\n");
		fprintf(m_timing, "%s", "<Timings>\n");
		fflush(m_timing);
	}
}

void ProfilingKit::closeFile() {
	if(m_timing) {
		fprintf(m_timing, "%s", "</ProfilingResult>\n");
		fclose(m_timing);
		m_timing=0;
	}
}

namespace {
	struct AccumulativeTimingsSortPred {
		AccumulativeTimingsSortPred(void) {}

		bool operator()(const AccumulativeTimingEntry* lhs, const AccumulativeTimingEntry* rhs) {
			return lhs->m_total_ms > rhs->m_total_ms;
		}
	};
}

konga::ProfilingKit::~ProfilingKit()
{
	if(m_timing!=0){
		for(vector<konga::TimingEntry*>::iterator it=m_timing_stack.begin();it!=m_timing_stack.end();it++) {
			delete *it;
		}
		m_timing_stack.clear();

		fprintf(m_timing, "%s", "</Timings>\n");
		fprintf(m_timing, "%s", "<AccumulativeTimings>\n");

		for(AccumulativeTimingGroupsMapT::const_iterator groupIt = m_accumulative_timings.begin();
			groupIt != m_accumulative_timings.end(); ++groupIt)
		{
			const string& group_name = groupIt->first;

			fprintf(m_timing, "  <%s>\n", group_name.c_str());

			AccumulativeTimingsMapT& timingsMap = *(groupIt->second);

			                                                                   
			vector<AccumulativeTimingEntry*> sortedEntries;
			for(AccumulativeTimingsMapT::iterator it=timingsMap.begin(); it != timingsMap.end(); ++it) {
				if ( it->second == 0 )
					continue;
				sortedEntries.push_back(it->second);
			}
			timingsMap.clear();

			AccumulativeTimingsSortPred sortRule;
			std::sort(sortedEntries.begin(), sortedEntries.end(), sortRule);

			for( vector<AccumulativeTimingEntry*>::iterator it = sortedEntries.begin(); it != sortedEntries.end(); ++it ) {
				AccumulativeTimingEntry& entry = *(*it);
				const string& sScopeNameHtml = html_string(entry.m_sScopeName.c_str(), entry.m_sScopeName.size(), false);
				fprintf(m_timing, "    <%s NumberOfEntries=\"%d\" TotalTimeMilliSeconds=\"%d\"/>\n",
					sScopeNameHtml.c_str(), entry.m_nEntries, entry.m_total_ms);
				if ( entry.m_entry_time != 0 ) {
					delete entry.m_entry_time;
					entry.m_entry_time = 0;
				}

				delete *it;
				*it = 0;
			}
			sortedEntries.clear();

			fprintf(m_timing, "  </%s>\n", group_name.c_str());
			delete groupIt->second;
		}

		m_accumulative_timings.clear();

		fprintf(m_timing, "%s", "</AccumulativeTimings>\n");
		closeFile();
	}
}

void konga::ProfilingKit::printIndention() {
	if( m_timing==0 || m_timing_stack.size() <= 1 ) return;
	for(int i=0, n=m_timing_stack.size()-1;i<n;i++){
		fprintf(m_timing,"  ");
	}
}

void konga::ProfilingKit::start(const string& sWhere, const string& sExtra_) {
	if(m_timing==0)return;

	m_timing_stack.push_back( new TimingEntry(sWhere) );
	const konga::TimingEntry* const entry=m_timing_stack.back();

	const boost::posix_time::time_duration start=entry->pt.time_of_day();
	printIndention();

	const string sExtra( sExtra_.empty() ? " " : " " + sExtra_ + " ");
	fprintf(m_timing,"<%s%sstart=\"%02d:%02d:%02d.%06d\">\n",sWhere.c_str(),sExtra.c_str(),start.hours(),start.minutes(),start.seconds(),start.fractional_seconds());
	fflush(m_timing);
}

void konga::ProfilingKit::end(const string& sWhere){
	if(m_timing==0)return;
	if( m_timing_stack.empty() ) {
		fprintf(m_timing, "timing stack is empty(where=%s) - inconsistent calls to ProfilingKit::end\n", sWhere.c_str());
		return;
	}

	const boost::posix_time::ptime end_pt(boost::posix_time::microsec_clock::local_time());
	const boost::posix_time::time_duration end=end_pt.time_of_day();

	TimingEntry* entry = m_timing_stack.back();
	if(entry->sWhere==sWhere){
		const boost::posix_time::time_duration delta=end_pt-entry->pt;
		printIndention();

		fprintf(m_timing,"<!--end=\"%02d:%02d:%02d.%06d\"-->",
			end.hours(),end.minutes(),end.seconds(),end.fractional_seconds());
		fprintf(m_timing,"%d",delta.total_milliseconds());
		fprintf(m_timing,"</%s>\n",sWhere.c_str());

		m_timing_stack.pop_back();
		delete entry;
	}
	else {
		fprintf(m_timing,"%s can't find its match - inconsistent calls to ProfilingKit::end\n", sWhere.c_str());
	}
	fflush(m_timing);
}

void ProfilingKit::enterAccumulativeScope(const string& group_name, const string& scope_name) {
	if(m_timing==0)return;
	if(group_name.empty()||scope_name.empty()) {
		m_timing=0;	           
		return;
	}

	AccumulativeTimingsMapT* timingsMap = 0;
	AccumulativeTimingGroupsMapT::const_iterator groupIt = m_accumulative_timings.find(group_name);
	if ( groupIt == m_accumulative_timings.end() ) {
		               
		timingsMap = new AccumulativeTimingsMapT();
		m_accumulative_timings.insert(AccumulativeTimingGroupsMapT::value_type(group_name, timingsMap));
	}
	else {
		timingsMap = groupIt->second;
	}
	assert(timingsMap);

	AccumulativeTimingEntry* entry = 0;
	AccumulativeTimingsMapT::iterator entryit = timingsMap->find(scope_name);
	if ( entryit == timingsMap->end() ) {
		                                   
		entry = new AccumulativeTimingEntry(scope_name);
		timingsMap->insert(AccumulativeTimingsMapT::value_type(scope_name, entry));
	}
	else {
		entry = entryit->second;
	}
	assert(entry);

	if ( entry->m_entry_time == 0 ) {
		                                  
		entry->m_entry_time = new boost::posix_time::ptime(boost::posix_time::microsec_clock::local_time());
		entry->m_nesting = 1;
	}
	else {
		                                                                                                     
		++entry->m_nesting;
	}
}

void ProfilingKit::exitAccumulativeScope(const std::string& group_name, const std::string& scope_name) {
	if(m_timing==0)return;

	AccumulativeTimingGroupsMapT::iterator groupIt = m_accumulative_timings.find(group_name);
	assert(groupIt != m_accumulative_timings.end());
	if ( groupIt == m_accumulative_timings.end() ) {
		                                                                           
		return;
	}
	AccumulativeTimingsMapT& timingsMap = *(groupIt->second);

	AccumulativeTimingsMapT::iterator it = timingsMap.find(scope_name);
	assert(it != timingsMap.end());
	if ( it == timingsMap.end() ) {
		                                                                           
		return;
	}
	AccumulativeTimingEntry* entry = it->second;

	assert(entry && entry->m_entry_time!=0 && entry->m_nesting>0);
	if ( entry == 0 || entry->m_entry_time == 0 || entry->m_nesting <1 ) {
		                                   
		return;
	}

	                                                      
	if ( --entry->m_nesting == 0 ) {
		                                              
		boost::posix_time::ptime now(boost::posix_time::microsec_clock::local_time());
		const boost::posix_time::ptime& then = *entry->m_entry_time;
		const boost::posix_time::time_duration delta(now-then);
		if ( delta.total_milliseconds() > 0 ) {
			entry->m_total_ms += delta.total_milliseconds();
		}

		++entry->m_nEntries;
		delete entry->m_entry_time;
		entry->m_entry_time = 0;
	}
}

EnterAccumulativeScope::EnterAccumulativeScope(ProfilingKit* pProfilingKit, const string& scope_group, const string& scope_name)
: m_pProfilingKit(pProfilingKit), m_sScopeGroup(scope_group), m_sScopeName(scope_name)
{
	if ( m_pProfilingKit && !m_sScopeGroup.empty() && !m_sScopeName.empty() ) {
		m_pProfilingKit->enterAccumulativeScope(m_sScopeGroup, m_sScopeName);
	}
}

EnterAccumulativeScope::~EnterAccumulativeScope(void) {
	if ( m_pProfilingKit && !m_sScopeGroup.empty() && !m_sScopeName.empty() ) {
		m_pProfilingKit->exitAccumulativeScope(m_sScopeGroup, m_sScopeName);
	}
}
