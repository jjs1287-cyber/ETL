/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef SCHEDULEDOPCOLLECTOR_H
#define SCHEDULEDOPCOLLECTOR_H

#include "ScheduledOp.h"

namespace konga {
	class Logger;
	class Scheduler;
	class KeyValues;

	class ScheduledItem {
	public:
		ScheduledItem(PTIME qtime, ScheduledOp* pScheduledOp) : m_qtime(qtime), m_pScheduledOp(pScheduledOp){}

		PTIME m_qtime;
		ScheduledOp* m_pScheduledOp;
	};

	class ScheduledOpCollector {
	public:
		ScheduledOpCollector(void);
		~ScheduledOpCollector(void);

		void collect(std::vector<ScheduledItem>&v, PTIME start, PTIME end);

	private:
		konga::Logger* m_pLogger;
		konga::Scheduler* m_pScheduler;
	};

	void QueueTimeList(std::vector<PTIME>&time_list, const PTIME& start, const KeyValues& scheduleProperties, const int n);
	void QueueTimeList(std::vector<PTIME>&time_list, const PTIME& start, const long schedule_id, const int n);
}

#endif                          
