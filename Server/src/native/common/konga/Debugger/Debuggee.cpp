/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#include <string>
#include <iostream>

#include <boost/thread/mutex.hpp>
#include <boost/thread/xtime.hpp>

#include "BreakpointLocation.h"
#include "DebugExceptions.h"		                             
#include "Debuggee.h"

using namespace konga;
using namespace std;

Debuggee::Debuggee() :
	m_isDebugBreaking(false), m_isDebugging(false), m_observeBreakpoints(true),
	m_shouldStop(false), m_shouldStep(false), m_breakOnWarning(false), m_breakOnError(false),
	m_pBreakLocation(0), m_pPreviousBreakLocation(0)
{
}

Debuggee::~Debuggee() {
	                                      
}

BreakpointLocation* Debuggee::WaitForDebugBreak() {
	if( m_isDebugBreaking ) return getBreakLocation();
	boost::xtime xt;
	boost::xtime_get(&xt, boost::TIME_UTC_);
	xt.sec += 600;               
	boost::unique_lock<boost::mutex> lock(m_debugBreakMutex);
	while( !m_isDebugBreaking ) {
		m_debugBreakCondition.timed_wait(lock, xt);
	}
	return getBreakLocation();
}

int Debuggee::DebugBreakpointHit(BreakpointLocation* pBreakLocation) {
	if( !m_isDebugging ) return 1;
	if( m_isDebugBreaking ) return -1;
	if( !m_observeBreakpoints ) return 2;
	m_isDebugBreaking = true;

	ResetBreakpointLocation();
	m_pBreakLocation = pBreakLocation;

	boost::xtime xt;
	boost::xtime_get(&xt, boost::TIME_UTC_);
	xt.sec += 3600;              

	                                                   
	m_debugBreakCondition.notify_one();

	                                                                   
	boost::unique_lock<boost::mutex> lock(m_debugBreakMutex);
#ifdef _DEBUG
	std::cout << "Waiting at breakpoint.";
#endif
	while( m_isDebugBreaking ) {
#ifdef _DEBUG
		std::cout << ".";
#endif
		if( !m_debugBreakCondition.timed_wait(lock, xt) )
			throw DebugSessionExpired();                                                                            
	}
	pBreakLocation->wasHandled();
	if ( shouldStop() ) {
#ifdef _DEBUG
		std::cout << ". Cancelling" << std::endl ;
#endif
		throw CancelDebugSession();
	}
#ifdef _DEBUG
	std::cout << ". Continuing" << std::endl ;
#endif
	return 0;
}

void Debuggee::DebugContinue() {
	m_isDebugBreaking = false;
	m_debugBreakCondition.notify_all();
}

void Debuggee::DebugBreakpointReset() {
	ResetBreakpointLocation();
	m_isDebugBreaking = true;
	m_debugBreakCondition.notify_all();
}

void Debuggee::InitDebugging() {
	m_isDebugging = true;
	m_isDebugBreaking = false;                                               
}

void Debuggee::setObserveBreakpoints(bool b) {
	m_observeBreakpoints = b;
}
bool Debuggee::isObservingBreakpoints() const {
	return m_observeBreakpoints;
}

konga::BreakpointLocation* Debuggee::getBreakLocation() const {
	return m_pBreakLocation;
}

konga::BreakpointLocation* Debuggee::getPreviousBreakLocation() const {
	return m_pPreviousBreakLocation;
}

                                                                                   
void Debuggee::ResetBreakpointLocation() {
	if ( m_pBreakLocation ) {
		m_pPreviousBreakLocation = m_pBreakLocation;
		m_pBreakLocation = 0;
	}
}
