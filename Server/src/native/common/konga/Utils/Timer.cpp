/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include "Timer.h"

using namespace konga;

Timer::~Timer() {
	ShouldExit(true);

	TIMER_FUNCTION_CONTAINER::iterator it = m_Functions.begin();
	for(; it != m_Functions.end(); ++it)
		delete it->second;

	m_functionsToRunCond.notify_all();
	m_timerFunctionCond.notify_all();

	if( m_pTimerThread ) {
		m_pTimerThread->join();
		delete m_pTimerThread;
	}

	m_bStarted = false;
}

void Timer::Start() {
	m_bStarted = false;
	if( m_bStarted ) return;
	m_bStarted = true;
	m_pTimerThread = new boost::thread( TimerFunctionStruct(this) );
}

void Timer::ShouldExit(bool b)	 { 
	boost::mutex::scoped_lock lock(m_exitLock);
	m_bShouldExit = b;
}

bool Timer::ShouldExit() const { 
	boost::mutex::scoped_lock lock(m_exitLock);
	return m_bShouldExit;
}

int Timer::AddTimerFunction(const boost::function0<void> f, unsigned long lFreqInMilliSeconds) {
	boost::mutex::scoped_lock lock(m_timerFunctionLock);
	int iFuncId = m_Functions.size();
	m_Functions[ iFuncId ] = new TimerFunctionHelper(f, lFreqInMilliSeconds);
	m_timerFunctionCond.notify_one();
	return iFuncId;
}

void Timer::RemoveTimerFunction(int iFuncId) {
	boost::mutex::scoped_lock lock(m_timerFunctionLock);
	TIMER_FUNCTION_CONTAINER::iterator itFunc = m_Functions.find(iFuncId);
	if( itFunc != m_Functions.end() )
		m_Functions.erase(itFunc);
}

const boost::function0<void> Timer::WaitForTimerEvent() {
	boost::mutex::scoped_lock lock(m_functionsToRunLock);
	if( ShouldExit() ) return m_funcDoNothing;

	std::list<TimerFunctionHelper*>::iterator it = m_FunctionsToRun.begin();
	while( it == m_FunctionsToRun.end() ) {
		m_functionsToRunCond.wait(lock);
		if( ShouldExit() ) return m_funcDoNothing;

		it = m_FunctionsToRun.begin();
	}

	const boost::function0<void> f = (*it)->f;
	m_FunctionsToRun.erase(it);
	return f;
}

void Timer::TimerFunction() {
	m_lMillisToSleep = 0;

	while( !ShouldExit() ) {
		boost::mutex::scoped_lock lock(m_timerFunctionLock);
		while( m_Functions.size() == 0 )
			m_timerFunctionCond.wait(lock);
		if( ShouldExit() ) return;

		TIMER_FUNCTION_CONTAINER::const_iterator citTimerFuncHelp = m_Functions.begin();
		unsigned long lMin = std::numeric_limits<unsigned long>::max();
		for(; citTimerFuncHelp != m_Functions.end(); ++citTimerFuncHelp) {

			citTimerFuncHelp->second->lMilliSecondsToWait -= m_lMillisToSleep;

			if( citTimerFuncHelp->second->lMilliSecondsToWait <= m_lMillisToSleep ) {
				citTimerFuncHelp->second->lMilliSecondsToWait = citTimerFuncHelp->second->lFreqInMilliSeconds;

				boost::mutex::scoped_lock lock(m_functionsToRunLock);
				if( std::find(m_FunctionsToRun.begin(), m_FunctionsToRun.end(), citTimerFuncHelp->second) == m_FunctionsToRun.end() )
					m_FunctionsToRun.push_back(citTimerFuncHelp->second);
				m_functionsToRunCond.notify_one();
			}

			if( citTimerFuncHelp->second->lMilliSecondsToWait < lMin )
				lMin = citTimerFuncHelp->second->lMilliSecondsToWait;
		}

		if( std::numeric_limits<unsigned long>::max() != lMin ) {
		m_lMillisToSleep = lMin;
		#ifdef WIN32
			Sleep(m_lMillisToSleep);
		#else
			usleep(m_lMillisToSleep * 1000);
		#endif
		}
	}
}
