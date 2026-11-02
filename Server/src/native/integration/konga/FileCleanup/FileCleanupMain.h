/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
#ifndef CLEANUPSERVICE_H
#define CLEANUPSERVICE_H

#include <time.h>
#include <atomic>
#include <boost/thread.hpp>
#include <vector>

namespace konga {

	class CleanupRule;

class FileCleanupMain {
private:
	std::atomic<bool> m_bIsRunning;
	std::atomic<bool> m_stopped{true};
	std::atomic<bool> m_Dirty;

	               
	boost::thread* m_pLoopThread;
	std::atomic<time_t> m_lastExeTime;
	std::atomic<time_t> m_wakeup;
	double m_nFreq;

	std::string m_stXmlCleanupRules;
	std::vector<CleanupRule*> m_rules;

	void eraseRules();

public:
	FileCleanupMain();
	~FileCleanupMain();
	void clear();
	bool IsRunning(){return m_bIsRunning;}
	bool IsStopped(){return m_stopped;}

	void start();
	void stopping(){m_bIsRunning=false;}
	void killThread();
	void finished(){m_stopped=true;}

	void init();
	void execute();
	void cancel();
	void update();

	time_t getWakeupTime(){return m_wakeup;}
	time_t nextScheduledTime();
	bool isDirty(){return m_Dirty;}
	void setDirty(){m_Dirty=true; m_lastExeTime=0;}
};

}                   
#endif                    
