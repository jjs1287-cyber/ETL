/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */

#ifndef TRANSFORMATION_UTILS_DATE_FORMAT_UTILS_H
#define TRANSFORMATION_UTILS_DATE_FORMAT_UTILS_H

#include <string>

namespace konga {

	class CrDateTime;

	std::string FormatDate(const konga::CrDateTime& t, const char* sFormat);

	std::string GetDateString(const char* driverNameStr, const char* inputDateStr, int iSQLColType);

}                   

#endif                                            

