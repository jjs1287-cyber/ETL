/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */

#include <sstream>	               
#include <string>

#include <boost/scoped_array.hpp>

#include <boost/date_time/gregorian/gregorian.hpp>
#include <boost/date_time/posix_time/posix_time.hpp>	                                  
#include <boost/date_time/local_time_adjustor.hpp>
#include <boost/date_time/c_local_time_adjustor.hpp>

#include <konga/Utils/date.h>
#include <konga/Utils/PortDefs.h>

#include "StringUtils.h"
#include "CrException.h"

#include "DateUtils.h"

const char* konga::CrDateTime::XML_DATE_FORMAT="date";
const char* konga::CrDateTime::XML_TIME_FORMAT="time";
const char* konga::CrDateTime::XML_DATE_TIME_FORMAT="dateTime";

using namespace boost::posix_time;
using namespace std;

namespace {
	                                                   
	const long MIN_BOOST_YEAR = 1400;
	const long MAX_BOOST_YEAR = 9999;
	const long DEFAULT_YEAR = 2000;
}

namespace konga {
	const char* strftime_format_char_set="aAbBcdHIjmMpSUwWxXyYzZ%#";

int DayInMonth(const int month,const int year){
	switch(month){
	case 1:
	case 3:
	case 5:
	case 7:
	case 8:
	case 10:
	case 12:
		return 31;
		break;
	case 2:
		if((year%4)==0&&((year%100)!=0||(year%400)==0)){
			return 29;
		} else {
			return 28;
		}
		break;
	case 4:
	case 6:
	case 9:
	case 11:
		return 30;
		break;
	}
	return 0;
}

bool valid_strftime_format(const char* sFormat){
	bool valid=false;
	int k=0, n=strlen(sFormat);
	while(k<n){
		int k1=k+findk(&sFormat[k],'%');
		if(sFormat[k1]==0)return valid;
		k=k1+1;
		int l=findk(strftime_format_char_set,sFormat[k]);
		if(strftime_format_char_set[l]==0)return false;
		valid=true;
	}
	return false;
}


void Date2MonthDay(int&d,int&month,const int year){
	int day_in_month;
	for(month=1;month<=12;month++){
		if((day_in_month=DayInMonth(month,year))>=d)break;
		d-=day_in_month;
	}
}

int MonthDay2Day(const int d,const int month,const int year){
	int d365=0;
	for(int m=1;m<month;m++){
		d365+=DayInMonth(m,year);
	}
	return d365+d;
}

int FindYear(const int y)
{
	if(y<=38)return y+2000;
	if(y<100)return y+1900;
	return y;
}

typedef boost::date_time::c_local_adjustor<boost::posix_time::ptime> local_adj_ptime;

CrDateTime CrDateTime::GetCurrentUtcTime() {
	return CrDateTime( second_clock::universal_time() );
}

CrDateTime CrDateTime::GetCurrentTime() {
	return CrDateTime();
}

CrDateTime CrDateTime::UtcToLocal(const CrDateTime& utc) {
	boost::posix_time::ptime pt=utc.get_ptime();
	boost::posix_time::ptime pt1=local_adj_ptime::utc_to_local(pt);
	return CrDateTime( pt1 );
}

                           
  
CrDateTime::CrDateTime(bool bTruncateFractionSeconds) : m_year_offset(0)
{
	boost::posix_time::ptime pt(microsec_clock::local_time());
	m_time_duration=pt.time_of_day();
	if(bTruncateFractionSeconds){
		m_time_duration=boost::posix_time::time_duration(m_time_duration.hours(),m_time_duration.minutes(),m_time_duration.seconds());
	}
	m_date=pt.date();
}

CrDateTime::CrDateTime(time_t t) : m_year_offset(0)
{
	SetDateTime(t);
}

CrDateTime::CrDateTime(int nYear, int nMonth, int nDay, int nHour, int nMin, int nSec, int nMs      ) : m_year_offset(0)
{
    SetDateTime(nYear,nMonth,nDay,nHour,nMin,nSec,nMs);
}
  
           

int CrDateTime::GetYear() const 
{
	if(m_date.is_not_a_date())return 0;
    return m_date.year()+m_year_offset;
}
                              
int CrDateTime::GetMonth() const 
{
	if(m_date.is_not_a_date())return 0;
    return m_date.month();
}
                      
int CrDateTime::GetDay() const 
{
	if(m_date.is_not_a_date())return 0;
    return m_date.day();
}
                     
int CrDateTime::GetHour() const 
{
	if(m_time_duration.is_not_a_date_time())return 0;
    return m_time_duration.hours();
}
                            
int CrDateTime::GetMinute() const 
{
	if(m_time_duration.is_not_a_date_time())return 0;
    return m_time_duration.minutes();
}
                          
int CrDateTime::GetSecond() const 
{
	if(m_time_duration.is_not_a_date_time())return 0;
    return m_time_duration.seconds();
}

int CrDateTime::GetMS() const 
{
	if(m_time_duration.is_not_a_date_time())return 0;
    return static_cast<int>(m_time_duration.fractional_seconds());
}

                                                          
int CrDateTime::GetDayOfWeek() const 
{
	if(m_date.is_not_a_date())return 0;
    return m_date.day_of_week();
}
                                           
int CrDateTime::GetDayOfYear() const {
    return (int)m_date.day_of_year();
}

time_t CrDateTime::GetTime() const 
{
	if(m_time_duration.is_not_a_date_time()||m_date.is_not_a_date())return 0;

	                                                            
	                                                                     
	                                                   
	const ptime& pt = get_ptime();
	const ptime& pt1=local_adj_ptime::utc_to_local(pt);

	struct tm atm = to_tm(pt1);
	return mktime(&atm);
}

bool CrDateTime::operator==(const CrDateTime& dt) const 
{
	const bool b_date=m_date.is_not_a_date();
	const bool b_time=m_time_duration.is_not_a_date_time();
	if(b_date!=dt.m_date.is_not_a_date()||b_time!=dt.m_time_duration.is_not_a_date_time())return false;
	if(!b_date&&m_date!=dt.m_date)return false;
	if(!b_time&&m_time_duration!=dt.m_time_duration)return false;
	if(m_year_offset!=dt.m_year_offset)return false;
	return m_tz.toString() == dt.m_tz.toString();
}

bool CrDateTime::operator!=(const CrDateTime& dt) const 
{
	return !(*this==dt);
}

bool CrDateTime::operator<(const CrDateTime& dt) const 
{
	const bool b_date=!m_date.is_not_a_date();
	const bool b_time=!m_time_duration.is_not_a_date_time();
	if ( b_date != !dt.m_date.is_not_a_date() ||
		 b_time != !dt.m_time_duration.is_not_a_date_time() ||
		 (!b_date && !b_time) )
		throw CrException("Comparision between a date and a time is not defined.");

	if ( b_date ) {
		                                               
		const int y1 = GetYear();
		const int y2 = dt.GetYear();
		if ( y1 != y2 )
			return y1 < y2;
		if ( m_date != dt.m_date )
			return m_date < dt.m_date;
	}

	if ( b_time ) {
		if ( m_time_duration != dt.m_time_duration )
			return m_time_duration < dt.m_time_duration;
	}
	return false;
}

bool CrDateTime::operator>(const CrDateTime& dt) const 
{
    return !(*this<dt) && !(*this==dt);
}

bool CrDateTime::operator<=(const CrDateTime& dt) const 
{
    return (*this<dt) || (*this==dt);
}

bool CrDateTime::operator>=(const CrDateTime& dt) const 
{
    return !(*this < dt);
}

CrDateTime CrDateTime::operator+(const CrTimeDuration& td) const
{
	boost::posix_time::ptime pt(m_date,m_time_duration+td);
	return CrDateTime(pt);
}

CrDateTime CrDateTime::operator-(const CrTimeDuration& td) const
{
	boost::posix_time::ptime pt(m_date,m_time_duration-td);
	return CrDateTime(pt);
}

CrTimeDuration CrDateTime::operator-(const CrDateTime& dt) const
{
	if ( m_year_offset == dt.m_year_offset )
		return get_ptime() - dt.get_ptime();

	                                                                        
	return get_ptime() - dt.get_ptime()
		+ CrTimeDuration(8760*(m_year_offset-dt.m_year_offset),0,0,0);
}

CrDateTime& CrDateTime::operator+=(const CrTimeDuration& td) {
	return (*this = CrDateTime(this->get_ptime() + td));
}

CrDateTime& CrDateTime::operator-=(const CrTimeDuration& td) {
	return (*this = CrDateTime(this->get_ptime() - td));
}

void CrDateTime::SetDateTime(time_t t)
{
	boost::posix_time::ptime pt(from_time_t(t));
	m_date=pt.date();
	m_time_duration=pt.time_of_day();
}

void CrDateTime::SetDateTime(int nYear0, int nMonth, int nDay, int nHour, int nMin, int nSec, int nMs)
{
	m_year_offset=0;
	try {
		int nYear=nYear0;
		if(nYear!=-1 && (nYear<MIN_BOOST_YEAR||nYear>MAX_BOOST_YEAR)){
			nYear=DEFAULT_YEAR;
			m_year_offset=nYear0-nYear;
		}

		if(!(nYear==-1&&nMonth==-1&&nDay==-1))
			m_date=boost::gregorian::date(nYear,nMonth,nDay);
		if(!(nHour==-1&&nMin==-1&&nSec==-1))
			m_time_duration=boost::posix_time::time_duration(nHour,nMin,nSec,nMs);
	}
	catch ( const std::exception& ex ) {
		stringstream sDate;
		sDate << nYear0 << "-" << nMonth << "-" << nDay
			<< " " << nHour << ":" << nMin << ":" << nSec << "." << nMs;
		throw CrException("Failed to create date '" + sDate.str() + "'. Reason: " + string(ex.what()));
	}
}

void CrDateTime::SetDate(int nYear0, int nMonth, int nDay)
{
	try {
		m_year_offset=0;
		int nYear=nYear0;
		if(nYear!=-1 && (nYear<MIN_BOOST_YEAR||nYear>MAX_BOOST_YEAR)){
			nYear=DEFAULT_YEAR;
			m_year_offset=nYear0-nYear;
		}

		m_date=boost::gregorian::date(nYear,nMonth,nDay);
	}
	catch ( const std::exception& ex ) {
		stringstream sDate;
		sDate << nYear0 << "-" << nMonth << "-" << nDay;
		throw CrException("Failed to create date '" + sDate.str() + "'. Reason: " + string(ex.what()));
	}
}

void CrDateTime::SetTime(int nHour, int nMin, int nSec, int nMs)
{
	try {
		m_time_duration=boost::posix_time::time_duration(nHour,nMin,nSec,nMs);
	}
	catch( const std::exception& ex ) {
		stringstream sTime;
		sTime << nHour << ":" << nMin << ":" << nSec << "." << nMs;
		throw CrException("Failed to create date '" + sTime.str() + "'. Reason: " + string(ex.what()));
	}
}

std::string CrDateTime::getDateString() const {
	if(m_date.is_not_a_date())return "";

	stringstream sDate;
	sDate << setfill('0') << setw(4) << GetYear() << '-';
	sDate << setfill('0') << setw(2) << GetMonth() << '-';
	sDate << setfill('0') << setw(2) << GetDay();
	return sDate.str();
}

std::string CrDateTime::getTimeString() const {
	if(m_time_duration.is_not_a_date_time())
		return "";

	stringstream sTime;
	sTime << setfill('0') << setw(2) << GetHour() << ':';
	sTime << setfill('0') << setw(2) << GetMinute() << ':';
	sTime << setfill('0') << setw(2) << GetSecond();
	
	int fs=m_time_duration.fractional_seconds();
	if ( fs != 0 ) {
		const int ndigits=time_duration::num_fractional_digits();
		boost::scoped_array<char> buf( new char[ndigits+1] );
		buf[ndigits]=0;
		for(int k=0;k<ndigits;k++){
			int r=fs%10;
			fs/=10;
			buf[ndigits-k-1]='0'+r;
		}
		sTime << '.' << buf.get();
	}
	return sTime.str();
}


std::string CrDateTime::toString(const std::string& stFormat) const 
{
	if(stFormat.empty()){
		stringstream s;
		if ( !m_date.is_not_a_date() ) {
			s << m_date.day_of_week().as_short_string() << " "
				<< m_date.month().as_short_string() << " "
				<< m_date.day().as_number();
		}

		if ( !m_time_duration.is_not_a_date_time() ) {
			if ( !m_date.is_not_a_date() )
				s << " ";
			s << setfill('0') << setw(2) << m_time_duration.hours() << ':'
				<< setfill('0') << setw(2) << m_time_duration.minutes() << ':'
				<< setfill('0') << setw(2) << m_time_duration.seconds();
			if( !m_date.is_not_a_date() )
				s << " " << GetYear();
		}
		return s.str();
	} else if(stFormat==XML_DATE_FORMAT){
		return getDateString()+getTZString();
	} else if(stFormat==XML_TIME_FORMAT){
		return getTimeString()+getTZString();
	} else if(stFormat==XML_DATE_TIME_FORMAT){
		return getDateString()+"T"+getTimeString()+getTZString();
	}

	if(konga::valid_strftime_format(stFormat.c_str())){
		struct tm atm = to_tm(get_ptime());
		atm.tm_year += m_year_offset;
		char buf[256];
		size_t nSize = strftime(buf,256,stFormat.c_str(),&atm);
		if ( nSize == 0 && !stFormat.empty() )
			throw CrException("Invalid time format, or format was too long (more than 255 characters): "
			+ stFormat);
		return string(buf);
	} else {
		return CVTDate(*this,stFormat.c_str());
	}
}

bool CrDateTime::IsValid() const {
	return !m_date.is_not_a_date()||!m_time_duration.is_not_a_date_time();
}

std::string CrDateTime::getTZString() const {
	return m_tz.toString();
}

bool CrDateTime::dateIsValid() const
{
	return !m_date.is_not_a_date();
}
bool CrDateTime::timeIsValid() const
{
	return !m_time_duration.is_not_a_date_time();
}

CrDateTime CrDateTime::GetLastDayOfMonth() const
{
	if(m_date.is_not_a_date()){
		throw CrException("CrDateTime::GetLastDayOfMonth(), date is not valid.");
	}
	if ( m_year_offset != 0 )
		throw CrException("Getting the last day of the month is only implemented for years in the range "
		+ konga::toString(MIN_BOOST_YEAR) + "..." + konga::toString(MAX_BOOST_YEAR) + ".");
	boost::gregorian::date end_of_month=m_date.end_of_month();
	return CrDateTime(end_of_month.year(),end_of_month.month(),end_of_month.day(),0,0,0);
}
}                   

