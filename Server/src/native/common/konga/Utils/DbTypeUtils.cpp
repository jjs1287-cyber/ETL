/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */

#include <sstream>		               

#include "DateUtils.h"
#include "StringUtils.h"
#include "DbTypeUtils.h"

using namespace konga;
using namespace std;

void konga::setTimeStruct(TIMESTAMP_STRUCT& t, const time_t tm) {
	CrDateTime crtime(tm);
	setTimeStruct(t, crtime);
}

void konga::setTimeStruct(TIMESTAMP_STRUCT& t, const CrDateTime& crtime) {
	t.year = crtime.GetYear();
	t.month = crtime.GetMonth();
	t.day = crtime.GetDay();
	t.hour = crtime.GetHour();
	t.minute = crtime.GetMinute();
	t.second = crtime.GetSecond();
	t.fraction = 0;
}

time_t konga::get_time_t(const TIMESTAMP_STRUCT& ts) {
	struct tm _tm;
	memset(&_tm, 0, sizeof(struct tm));

	_tm.tm_sec = ts.second;
	_tm.tm_min = ts.minute;
	_tm.tm_hour = ts.hour;
	_tm.tm_mday = ts.day;

	                                                                                           
	_tm.tm_mon = ts.month - 1;

	if ( ts.year < 1970 ) {
		                                                                           
		_tm.tm_year = 70;
	}
	else {
		                                                
		_tm.tm_year = ts.year - 1900;
	}

	                                                           
	_tm.tm_isdst = -1;

	return mktime(&_tm);
}

std::string konga::GetISO8601TimeStampString(const TIMESTAMP_STRUCT& ts)
{
	stringstream s;
	s << ts.year << "-";
	s << setfill('0') << setw(2) << ts.month << "-";
	s << setfill('0') << setw(2) << ts.day << " ";
	s << setfill('0') << setw(2) << ts.hour << ":";
	s << setfill('0') << setw(2) << ts.minute << ":";
	s << setfill('0') << setw(2) << ts.second << "." << ts.fraction;
	return s.str();
}
