/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#include <boost/date_time/posix_time/posix_time.hpp>

#include <konga/Log/logging.h>
#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Utils/ProcessUtils.h>

#include "ScheduledOp.h"
#include "Scheduler.h"
#include "ScheduledOpCollector.h"

using namespace konga;
using namespace TranDb;
using namespace boost::posix_time;
using std::string;
using std::vector;

ScheduledOpCollector::ScheduledOpCollector() :
m_pLogger(new Logger("ScheduledOpCollector", default_log_severity, KSEVERITY_INFORMATION, report_trace)),
m_pScheduler(0) {
	m_pScheduler = new Scheduler(m_pLogger, 0);
}

ScheduledOpCollector::~ScheduledOpCollector(void) {
	delete m_pScheduler;
	delete m_pLogger;
}

namespace {
	                                                                                               
	const int max_to_collect = 100000;
}

void ScheduledOpCollector::collect(vector<ScheduledItem>& scheduledItems, PTIME start, PTIME end) {
	m_pScheduler->init(start);
	const SOVector& v0=m_pScheduler->getSOVectorRef();
	bool bStart=true;
	int icollected = 0;
	for(PTIME time=start; time <= end && icollected < max_to_collect; time=m_pScheduler->nextScheduledTime()){
		bool bSuccess=false;
		for(SOVector::const_iterator it=v0.begin(); it!=v0.end(); it++){
			ScheduledOp* pSO=*it;
			if(pSO->testOp(time)){
				bSuccess=true;
				scheduledItems.push_back(ScheduledItem(time, pSO));
			}
		}
		if(bStart)bStart=false;
		else if(!bSuccess)break;
#ifndef _DEBUG
		                                       
		++icollected;
#endif
	}
}

void konga::QueueTimeList(vector<PTIME>&time_list, const PTIME& start, const KeyValues& scheduleProperties, const int n) {
	ScheduledOp schedule(start, scheduleProperties, 0);

	for(int i=0; i<n && i<max_to_collect; i++){
		PTIME time=schedule.getScheduledDateTime();
		if(time.is_not_a_date_time())break;
		time_list.push_back(time);
		if(!schedule.testOp(time))break;
	}
}

void konga::QueueTimeList(vector<PTIME>&time_list, const PTIME& start, const long schedule_id, const int n) {
	SmartDbPtr<DbConnection> pTranDbConn( konga::GetBackEndDbConnection("TranDb") );

	ScheduledOp schedule(start, schedule_id, pTranDbConn, 0);
	for(int i=0; i<n && i<max_to_collect; i++){
		PTIME time=schedule.getScheduledDateTime();
		if(time.is_not_a_date_time())break;
		time_list.push_back(time);
		if(!schedule.testOp(time))break;
	}
}
