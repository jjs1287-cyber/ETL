/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */

                                                          
  
                                                                      

#ifndef THREAD_MANAGER_H
#define THREAD_MANAGER_H

#include <map>
#include <set>
#include <string>
#include <vector>

#include <boost/thread/condition.hpp>
#include <boost/thread/mutex.hpp>
#include <boost/thread/thread.hpp>

#include "ui_enums.h"	                

class IncomingMsg;

class ThreadManager  
{
public:
	ThreadManager();
	virtual ~ThreadManager();

	void AddThread(boost::thread* pThread, IncomingMsg* pmsg);
	bool DeleteThread(boost::thread* pThread);
	void PrintThreadInfo();

	boost::thread** GetHandles(int& nThreads);
	int GetNumberOfThreads() { return static_cast<int>(m_threads.size()); }
	int GetNumberOfTasks() { return static_cast<int>(m_tasks.size()); }

	void AddTask(IncomingMsg* pmsg);
	bool ReleaseTask(IncomingMsg* pmsg);
	void KillTasks(const std::set<int>& msgids);

	void GetThreadInfo(std::vector<std::string>& vStartTimes,
					  std::vector<std::string>& vRouteIds,
					  std::vector<std::string>& vDataElements,
					  std::vector<std::string>& vDescriptions,
					  std::vector<long>& vMsgIds,
					  std::vector<long>& vPriorities);
	bool KillThread(std::string stRoute, KillTask ktKillType);
	bool KillAllThreads();
	bool SwitchDatabaseServers(const std::string& stDatabaseServerName);

	std::string GetLastError() const { return m_stLastError; }
private:
	typedef std::map<boost::thread*, IncomingMsg*> THREAD_SET_T;
	typedef THREAD_SET_T::iterator THREAD_SET_IT;
	THREAD_SET_T m_threads;

	typedef std::map<IncomingMsg*, bool> TASK_SET_T;
	typedef TASK_SET_T::iterator TASK_SET_IT;
	TASK_SET_T m_tasks;

	boost::condition m_all_released;

	boost::mutex m_thread_set_lock, m_task_set_lock, m_all_released_lock;

	std::string m_stLastError;
};

#endif                    

