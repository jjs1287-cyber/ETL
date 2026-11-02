/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#ifndef KONGA_SERVER_LMCHECK_INTERFACE_H
#define KONGA_SERVER_LMCHECK_INTERFACE_H

#include <string>

namespace konga {

	class KeyValues;
	class DbConnection;

	class lm {
	public:
		lm(void) {}
		virtual ~lm(void) {}

		virtual void inspect(KeyValues& props) = 0;

		virtual void check(std::string& warning, int startWarningDaysRemaining) = 0;

		virtual void install() = 0;

		virtual void installCloudTrial() = 0;

	};

	lm* get_lm(DbConnection*);

}

#endif                                    
