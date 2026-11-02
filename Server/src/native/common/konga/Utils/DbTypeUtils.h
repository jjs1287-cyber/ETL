/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_TYPE_UTILS_H
#define KONGA_DB_TYPE_UTILS_H

#include <time.h>
#ifdef WIN32
	#include <windows.h>
#endif
#include <sqltypes.h>

namespace konga {
	class CrDateTime;

	void setTimeStruct(TIMESTAMP_STRUCT& t, const time_t tm);
	void setTimeStruct(TIMESTAMP_STRUCT& t, const CrDateTime& crdatetime);

	time_t get_time_t(const TIMESTAMP_STRUCT& ts);

	                                                         
    std::string GetISO8601TimeStampString(const TIMESTAMP_STRUCT& ts);
}
#endif                         

