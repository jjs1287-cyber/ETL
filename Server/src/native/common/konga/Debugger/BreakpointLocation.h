/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#ifndef _KONGA_BREAK_LOCATION_H
#define _KONGA_BREAK_LOCATION_H

#include <string>

namespace konga {

	                                             
	class BreakpointLocation {
	public:
		BreakpointLocation() {}
		virtual ~BreakpointLocation(void) {}

		virtual std::string toString() const = 0;

		                                                                           
		virtual void wasHandled() = 0;
	};
}

#endif                           
