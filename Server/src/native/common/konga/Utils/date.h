/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */

#ifndef CR_DATE_H
#define CR_DATE_H

#include "KString.h"
#include "DateUtils.h"

#define AM_PRESENT 0
#define PM_PRESENT 1
#define AP_NOTPRESENT 2

namespace konga
{
    int findYear(const int y);
	int findMonth(const char* month);	                                                      
    int DayInMonth(const int month,const int year);
    void Date2MonthDay(int&d,int&month,const int year);
    int MonthDay2Day(const int d,const int month,const int year);
	KString parseDateFormat(const char*format0, KString& sFlags);
    int GetNumb(const char*input_data_str,const char c,const char*input_form_str, const char* input_form_flag);
    void SetNumb(const char*output_form_str,const char c,char*output_data_str, const char* input_form_flag,int n);
    KString fixup_input_string(const char*data_str, const char*form_str, const char*flags); 
    KString fixup_output_string(const char*data_str, const char*form_str, const char*flags);
	const char* strstrWithDateFormatFlags(const char*form_str, const char*pattern, const char*flags);

    int CheckDateError(int year,int month,int day, bool bCheckValidYear, std::string&stErrMsg);                        
    std::string CVTDate(const CrDateTime&t,const char*output_form_str);
    std::string CVTDate(const KString&input_data_str0,const KString&input_form_str,const KString&output_form_str, std::string&stErrMsg);
    std::string ToOracleDate(const char* date_str, bool& bHasSeconds, std::string& stErrMsg);
    void DMY(KString&s,int&year,int&month,int&day,int&hour,int&min,int&sec,int&day_of_week,int&AMPM,bool col_case, bool& bCheckValidYear, std::string&stErrMsg);
    int ReadDate(const char*str,int&year,int&month,int&day,int&hour,int&min,int&sec,int&day_of_week,int&tz_shift_minutes, bool&bCheckValidYear,std::string&stErrMsg);
	int ReadDate_From_strftime_format(const char*str,const char*format,int&year,int&month,int&day,int&hour,int&min,int&sec,std::string&tz);
    CrDateTime GetDate(const char*str,std::string&stErrMsg);
    
    #ifdef DS_USE_CTIME
    CTime CrDateTimeToCTime(const CrDateTime& oleDate, std::string&stErrMag);
    CrDateTime CTimeToCrDateTime(const CTime& objTime, std::string&stErrMag);
    #endif                
}

#endif             


