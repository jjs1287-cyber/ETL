/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */

#ifndef ETLFORGE_UTILS_OPERATIONS_H
#define ETLFORGE_UTILS_OPERATIONS_H

#include <string>

namespace konga {
	class DbConnection;

	void AddScheduledOperationsToQueue(bool bQuiet);
	void AddOperationToQueue(const std::string& operation, bool bQuiet);
}

#endif                               

