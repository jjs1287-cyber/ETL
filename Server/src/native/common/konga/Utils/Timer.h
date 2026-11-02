/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_TIMER_H
#define KONGA_TIMER_H

#include <algorithm>
#include <list>
#include <map>
#include <limits>

#include <boost/function.hpp>		                  
#include <boost/utility.hpp>		                    

#include <boost/thread/condition.hpp>
#include <boost/thread/mutex.hpp>
#include <boost/thread/thread.hpp>

                          

#ifdef WIN32
	#include <windows.h>
	#ifdef min
		#undef min
	#endif
	#ifdef max
		#undef max
	#endif
#endif         

namespace konga {

class Timer : private boost::noncopyable {
public:
	Timer() : m_bStarted(false), m_bShouldExit(false), m_lMillisToSleep(0), m_pTimerThread(0) {}
	~Timer();

	void Start();

	void ShouldExit(bool b);
	bool ShouldExit() const;

	int AddTimerFunction(const boost::function0<void> f, unsigned long lFreqInMilliSeconds);
	void RemoveTimerFunction(int iFuncId);

	const boost::function0<void> WaitForTimerEvent();

	struct doNothing { void operator()() {}	};

private:

	struct TimerFunctionHelper {
		TimerFunctionHelper(const boost::function0<void>& f0, unsigned long freq) : 
			lFreqInMilliSeconds(freq), lMilliSecondsToWait(0), f(f0) {}

		unsigned long lFreqInMilliSeconds;
		unsigned long lMilliSecondsToWait;

		const boost::function0<void> f;
	};

	struct TimerFunctionStruct {           
		TimerFunctionStruct(Timer* pt0) : pt(pt0) {}
		void operator()() {
			pt->TimerFunction();
		}
		Timer* pt;
	};

	void TimerFunction();

private:
	bool m_bStarted;
	bool m_bShouldExit;
	unsigned long m_lMillisToSleep;


	mutable boost::mutex m_exitLock;
	mutable boost::mutex m_timerFunctionLock;
	boost::condition m_timerFunctionCond;

	boost::mutex m_functionsToRunLock;
	boost::condition m_functionsToRunCond;

	boost::thread* m_pTimerThread;

	typedef std::map<int, TimerFunctionHelper*> TIMER_FUNCTION_CONTAINER;
	TIMER_FUNCTION_CONTAINER m_Functions;

	std::list<TimerFunctionHelper*> m_FunctionsToRun;
	doNothing m_funcDoNothing;
};

}                   

#endif                 

