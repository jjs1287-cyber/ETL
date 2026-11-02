/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef SCHEDULER_H
#define SCHEDULER_H

#include <vector>
#include <string>

#include <boost/thread/condition.hpp>
#include <boost/thread/thread.hpp>

#include "ScheduledOp.h"

#define NUM_MESSAGES_STORED 5
                     

namespace konga {

class Logger;

typedef std::vector<ScheduledOp*> SOVector;
class Scheduler {
public:
	Scheduler(konga::Logger* pLogger, FILE* debug_log);
	~Scheduler();
	void clear();
	void setRunning(bool flag){m_bIsRunning=flag;}
	bool IsRunning(){return m_bIsRunning;}
	bool IsStopped(){return m_pSchedulerThread==0;}

	void start();
	void stopping(){m_bIsRunning=false;}
	void killThread();

	                                               
	unsigned long init(const std::string& schedule_tab);                                                                                 
	void init();
	void init(PTIME now);

	void update();
	PTIME check(PTIME now);                                                                             
	PTIME nextScheduledTime();
	bool isDirty(){return m_bIsDirty;}
	void setDirty(){m_bIsDirty=true;}
	std::string GetLastErr(){return m_lastErr;}
	void ResetLastErr(){m_lastErr="";}
	void addLatestMessage(std::string*psLastMessage);
	std::string GetLastMessages();

	PTIME getCurrentTime();

public:
	const SOVector& getSOVectorRef() const {return m_ScheduledOps;}

private:
	konga::Logger* m_pLogger;
	FILE* m_debug_log;
	SOVector m_ScheduledOps;
	std::string m_lastErr;
	DBConnectionInfo m_TranDbinfo;
	bool m_bIsRunning;
	bool m_bIsDirty;
	               
	boost::thread* m_pSchedulerThread;
	void check();
	std::string** m_spLastMessages;
	PTIME m_start;
};
}

#endif                       

