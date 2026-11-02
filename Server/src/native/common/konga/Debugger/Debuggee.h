/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#ifndef KONGA_DEBUGGER_DEBUGGEE_H
#define KONGA_DEBUGGER_DEBUGGEE_H

#include <boost/noncopyable.hpp>
#include <boost/thread/condition.hpp>
#include <boost/thread/mutex.hpp>

#include "BreakpointLocation.h"
#include "CallStack.h"

namespace konga {

	class Debuggee : boost::noncopyable {
	public:
		Debuggee();
		virtual ~Debuggee();
		
		virtual BreakpointLocation* WaitForDebugBreak();

		virtual void DebugBreakpointReset();
		virtual void DebugContinue();
		virtual bool isDebugging() const { return m_isDebugging; }

		                                                  
		                                                             
		                                      
		                                                                   
		                                                                 
		                                                                                                                                  
		                                                                               
		  
		                                                                                                 
		virtual int DebugBreakpointHit(BreakpointLocation* pBreakLocation);

		virtual bool shouldStop() const { return m_shouldStop; }
		virtual bool shouldStep() const { return m_shouldStep; }

		virtual void shouldStop(bool b) { m_shouldStop = b; }
		virtual void shouldStep(bool b) { m_shouldStep = b; }

		virtual bool breakOnWarning() const { return m_breakOnWarning; }
		virtual bool breakOnError() const { return m_breakOnError; }

		virtual void breakOnWarning(bool b) { m_breakOnWarning = b; }
		virtual void breakOnError(bool b) { m_breakOnError = b; }

		virtual std::string getError() const { return m_sError; }
		virtual std::string getWarning() const { return m_sWarning; }

		virtual void setError(const std::string& s) { m_sError = s; }
		virtual void setWarning(const std::string& s) { m_sWarning = s; }

		virtual void setObserveBreakpoints(bool b);
		virtual bool isObservingBreakpoints() const;

		CallStack& getCallStack() {
			return m_stack;
		}

		BreakpointLocation* getPreviousBreakLocation() const;

		void InitDebugging();

	private:
		                                                                                                
		BreakpointLocation* getBreakLocation() const;
		void ResetBreakpointLocation();

	private:
		bool m_isDebugBreaking, m_isDebugging, m_observeBreakpoints, m_shouldStop, m_shouldStep;
		bool m_breakOnWarning, m_breakOnError;
		konga::BreakpointLocation* m_pBreakLocation;
		konga::BreakpointLocation* m_pPreviousBreakLocation;
		std::string m_sError, m_sWarning;

		CallStack m_stack;
		boost::condition m_debugBreakCondition;
		boost::mutex m_debugBreakMutex;
	};

	                                                         
	class AutoPopCallStack {
	public:
		AutoPopCallStack(Debuggee* d) : m_d(d) {}
		~AutoPopCallStack(void) {
			if ( m_d ) m_d->getCallStack().pop();
		}

	private:
		Debuggee* m_d;
	};

}                   

#endif                             

