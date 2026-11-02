/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */

#ifndef ETLFORGE_OPCONTEXT_DEFAULT_ERROR_REPORTER_H
#define ETLFORGE_OPCONTEXT_DEFAULT_ERROR_REPORTER_H

#include <string>

#include "ErrorReporter.h"

namespace konga {
	
	class DefaultErrorReporter : public ErrorReporter {
	public:
		DefaultErrorReporter(void) {}
		virtual ~DefaultErrorReporter(void) {}

	public:
		                          
		virtual std::string getError() const;
		virtual std::string getLastError() const;

		                                                                   
		void addError(const std::string& sError);

	private:
		std::string m_sLastError;
		std::string m_sError;
	};

}

#endif                                               

