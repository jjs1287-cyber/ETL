/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DATE_UTILS_H
#define KONGA_DATE_UTILS_H

#ifdef max
	#undef max
#endif
#ifdef min
	#undef min
#endif

#include <time.h>
#ifdef GetCurrentTime
#undef GetCurrentTime
#endif
#include <boost/date_time/posix_time/posix_time.hpp>

namespace konga {

typedef boost::posix_time::time_duration CrTimeDuration;

int MonthDay2Day(const int d,const int month,const int year);
void Date2MonthDay(int&d,int&month,const int year);
int	DayInMonth(const int month,const int year);
int	FindYear(const int y);

class TZInfo
{
public:
	TZInfo(){}
	TZInfo(const TZInfo& info){m_time_zone_string=info.m_time_zone_string;}

	                                                   
	void set(const std::string& time_zone_string){m_time_zone_string=time_zone_string;}
	const std::string& toString() const {return m_time_zone_string;}

private:
	std::string m_time_zone_string;
};


                                                              
                                                                    
                                           
class CrDateTime
{
public:
	static const char *XML_DATE_FORMAT, *XML_TIME_FORMAT, *XML_DATE_TIME_FORMAT;

	                                  
	static CrDateTime GetCurrentTime();

	                                
	static CrDateTime GetCurrentUtcTime();

	                                                                            
	static CrDateTime UtcToLocal(const CrDateTime& utc);

	                                                      
	explicit CrDateTime(bool bTruncateFractionSeconds=false);

	                                                                                 
	                                          
    explicit CrDateTime(time_t t);

	                                                                
	CrDateTime(int nYear, int nMonth, int nDay,
		int nHour, int nMin, int nSec, int nMs = 0);

    int GetYear() const;
	                              
	int GetMonth() const;
	                      
	int GetDay() const;
	                     
	int GetHour() const;
	                        
	int GetMinute() const;
	                          
	int GetSecond() const;
    int GetMS() const;
	                                                          
	int GetDayOfWeek() const;
	                                           
	int GetDayOfYear() const;

	CrDateTime GetLastDayOfMonth() const;

	                                                                        
    time_t GetTime() const;

	bool operator==(const CrDateTime& date) const;
	bool operator!=(const CrDateTime& date) const; 
	bool operator<(const CrDateTime& date) const; 
	bool operator>(const CrDateTime& date) const;
	bool operator<=(const CrDateTime& date) const;
	bool operator>=(const CrDateTime& date) const;
	
	CrDateTime operator+(const CrTimeDuration& td) const;
	CrDateTime operator-(const CrTimeDuration& td) const;
	CrTimeDuration operator-(const CrDateTime& dt) const;

	CrDateTime& operator+=(const CrTimeDuration& td);
	CrDateTime& operator-=(const CrTimeDuration& td);
	
	                                                            
    void SetDateTime(time_t t);

	                                          
	void SetDateTime(int nYear, int nMonth, int nDay, int nHour, int nMin, int nSec, int nMs=0);

	void SetDate(int nYear, int nMonth, int nDay);
	void SetTime(int nHour, int nMin, int nSec, int nMs=0);

	                                                                                     
	                                                                                                     
	                                                                         
	std::string toString(const std::string& stFormat = "") const;

	bool IsValid() const;

	void setTZ(const std::string& time_zone_string){m_tz.set(time_zone_string);}
	std::string getTZString() const;
	std::string getDateString() const;
	std::string getTimeString() const;
	bool dateIsValid() const;
	bool timeIsValid() const;

                                                                    
            
	explicit CrDateTime(const boost::posix_time::ptime& pt) :
	m_date(pt.date()), m_time_duration(pt.time_of_day()), m_year_offset(0) {}

	boost::posix_time::ptime get_ptime() const {return boost::posix_time::ptime(m_date,m_time_duration);}

private:
	boost::gregorian::date m_date;
	boost::posix_time::time_duration m_time_duration;
	konga::TZInfo m_tz;

	                                                                                   
	                                               
	                                                                       
	                                                    
	int m_year_offset;

};

bool valid_strftime_format(const char* sFormat);

}
#endif                      


