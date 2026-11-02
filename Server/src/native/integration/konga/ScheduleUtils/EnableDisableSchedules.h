/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
#ifndef ETLFORGE_SCHEDULE_UTILS_ENABLE_DISABLE_SCHEDULES_H
#define ETLFORGE_SCHEDULE_UTILS_ENABLE_DISABLE_SCHEDULES_H

#include <vector>

namespace konga {

	class DbConnection;

	void SetSchedulesEnabled(const std::vector<long>& scheduleIds, const bool bEnabled, DbConnection& dbConnection);

	void SetOperationSchedulesEnabled(const std::vector<long>& operationIds, const bool bEnabled, DbConnection& dbConnection);
}

#endif                                                      

