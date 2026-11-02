/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */

#ifndef KONGA_CALL_STACK_H
#define KONGA_CALL_STACK_H

#include <vector>

#include "BreakpointLocation.h"

namespace konga {
	
	class CallStack {
	public:
		CallStack(void) {}
		virtual ~CallStack(void);

		const std::vector<BreakpointLocation*>& getBreakpointLocations() const;

		                                                      
		                                     
		virtual void push(BreakpointLocation* bl);

		                                                                             
		virtual BreakpointLocation* top() const;

		                                            
		                                                     
		virtual void pop();

	private:
		std::vector<BreakpointLocation*> m_stack;
		std::vector<BreakpointLocation*> m_trash;
	};
}

#endif                      

