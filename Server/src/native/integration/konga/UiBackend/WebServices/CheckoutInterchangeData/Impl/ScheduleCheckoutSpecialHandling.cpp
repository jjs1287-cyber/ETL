/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <konga/Utils/date.h>
#include <konga/Utils/DateUtils.h>
#include <konga/Utils/StringTokenizer.h>

                                                        

#include "ScheduleCheckoutSpecialHandling.h"

using namespace konga::interchange_checkout;
using std::vector;
using std::string;

ScheduleCheckoutSpecialHandling::ScheduleCheckoutSpecialHandling(void) {
	assert(0);	                                                                            
	m_vTimeStampColumnNames.push_back("frequency_once_time");
	m_vTimeStampColumnNames.push_back("frequency_start_time");
	m_vTimeStampColumnNames.push_back("frequency_end_time");
	m_vTimeStampColumnNames.push_back("duration_start_date");
	m_vTimeStampColumnNames.push_back("duration_end_date");
}

namespace {
	const char dateSep[] = "-";
	const char timeSep[] = ":";
}

void ScheduleCheckoutSpecialHandling::HandleColumn(vColumnValuesT& vColumnValues) {
	vector<string>::const_iterator cit = m_vTimeStampColumnNames.begin();
	for(; cit != m_vTimeStampColumnNames.end(); ++cit ) {
		const string& stColumnName = *cit;
		NullableString& valRef = GetColumnValueRef(stColumnName, vColumnValues);
		if ( valRef != 0 ) {
			const string& stVal = valRef.str();
			UIBACKEND_TRACE("ScheduleCheckoutSpecialHandling::HandleColumn " + stColumnName + " = " + stVal);
			string::size_type ipos = stVal.find_first_of(' ');
			string stDate, stTime;
			if ( ipos == string::npos ) {
				stDate = stVal;
			}
			else {
				stDate = stVal.substr(0, ipos);
				stTime = stVal.substr(ipos+1);
			}
			if ( stDate.empty() )
				throw CrException("Invalid date format in database '" + stVal + "'.");

			                      
			StringTokenizer dateTokenizer(stDate.c_str(), dateSep);
			if ( !dateTokenizer.NextToken() )
				throw CrException("Invalid date format in database '" + stVal + "'.");
			const string stMonth(dateTokenizer.token());
			int month = findMonth(stMonth.c_str());
			if ( month == 0 )
				throw CrException(stMonth + " is not a valid month.");
			dateTokenizer.NextToken();
			if ( !dateTokenizer.NextToken() )
				throw CrException("Invalid date format in database '" + stVal + "'.");
			int day = atoi(dateTokenizer.token());
			dateTokenizer.NextToken();
			if ( !dateTokenizer.NextToken() )
				throw CrException("Invalid date format in database '" + stVal + "'.");
			int year = atoi(dateTokenizer.token());

			                               
			int hour = 0, minute = 0, second = 0;
			if ( !stTime.empty() ) {
				StringTokenizer timeTokenizer(stTime.c_str(), timeSep);
				if ( !timeTokenizer.NextToken() )
					throw CrException("Invalid date format in database '" + stVal + "'.");
				hour = atoi(timeTokenizer.token());
				timeTokenizer.NextToken();
				if ( !timeTokenizer.NextToken() )
					throw CrException("Invalid date format in database '" + stVal + "'.");
				minute = atoi(timeTokenizer.token());
				timeTokenizer.NextToken();
				if ( !timeTokenizer.NextToken() )
					throw CrException("Invalid date format in database '" + stVal + "'.");
				second = atoi(timeTokenizer.token());
			}
			CrDateTime dateTime(year, month, day, hour, minute, second, 0);
			long long llTime = dateTime.GetTime();
			llTime *= 1000;

			valRef = toString(llTime);
		}
	}
}
