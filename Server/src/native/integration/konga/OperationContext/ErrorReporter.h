/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */

#ifndef KONGA_OPERATION_CONTEXT_LAST_ERROR_REPORTER_H
#define KONGA_OPERATION_CONTEXT_LAST_ERROR_REPORTER_H

#include <string>

namespace konga {

	                                                     
	class ErrorReporter {
	public:
		ErrorReporter(void) {}
		virtual ~ErrorReporter(void) {}

		virtual std::string getError() const = 0;
		virtual std::string getLastError() const = 0;
	};
}

#endif                                                 

