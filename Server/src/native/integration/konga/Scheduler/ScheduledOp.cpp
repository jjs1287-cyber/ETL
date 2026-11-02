/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#include <cassert>
#include <map>

#include <konga/Utils/StringUtils.h>
#include <konga/Utils/StringTokenizer.h>
#include <konga/Utils/KeyValues.h>

#include <konga/Db/DbUtils/SmartDbPtr.h>
#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DbUtils/AutoDbCleanup.h>
#include <konga/Db/DbUtils/DynamicDbRecordSet.h>
#include <konga/DbRelations/EntityIdConstants.h>

#include <konga/Messaging/MessageSender.h>
#include <konga/Messaging/ProcessMessage.h>
#include <konga/Messaging/OperationMessageUtils.h>

#include <konga/ItemProperties/ItemProperties.h>
#include <konga/ItemProperties/Definitions.h>

#include <konga/Log/logging.h>

#include "ScheduledOp.h"
#include "Scheduler.h"

#ifdef _DEBUG
#define debug_scheduler 0
#else
#define debug_scheduler 0
#endif

using namespace konga;
using namespace boost::posix_time;
using namespace boost::gregorian;
using std::string;
using std::map;

std::vector<GREG_DATE> g_holiday_list;

namespace {
	GREG_DATE get_gdate(const string& ts)
	{
		                                    
		int m,d,y;
		if(sscanf(ts.c_str(),"%4d-%2d-%2d",&y,&m,&d)!=3){
			throw konga::CrException(string("Scheduler failed, get_date() input format must be YYYY-MM-DD. the input string is \"")+ts+"\"");
		}
		return GREG_DATE(y,m,d);
	}

	T_DURATION get_time(const string& ts)                                           
	{
		                            
		int H=0, M=0, S=0;
		if(sscanf(ts.c_str(),"%2d:%2d:%2d",&H, &M, &S)<2){
			throw konga::CrException(string("Scheduler failed, get_time() input format must be HH:MM or HH:MM::SS. the input string is \"")+ts+"\"");
		}
		return T_DURATION(H,M,S);
	}

	GREG_DATE GREG_DATE_(int y, int m, int d){
		if(d > 28){
			GREG_DATE tmp(y,m,1);
			int d_last=tmp.end_of_month().day().as_number();
			if(d_last < d)d=d_last;
		}
		return GREG_DATE(y,m,d);
	}

	GREG_DATE getValidYearMonth(int start_y, int start_m, int& now_y, int& now_m, int period_in_months){
		int delta_m=(now_y-start_y)*12+now_m-start_m;
		if(delta_m <= 0){
			now_y=start_y;
			now_m=start_m;
		}
		else {
			int k = delta_m%period_in_months;
			if(k>0){
				now_m+=period_in_months-k;
			}
		}
		now_y+=(now_m-1)/12;
		now_m=((now_m-1)%12)+1;
		return GREG_DATE(now_y, now_m, 1);
	}

	                                                                                       
	                                                                                  
	                                                                                  
	GREG_DATE getMonthly_B_date(const GREG_DATE &which_month, int Monthly_B_Num, int Monthly_B_Day)
	{
		unsigned short x=(unsigned short)which_month.day();
		GREG_DATE d_1=which_month-days(x-1), d_last=which_month.end_of_month();
		unsigned short wd_1=d_1.day_of_week().as_number();
		unsigned short wd=Monthly_B_Day;
		int a=1+(Monthly_B_Num-1)*7+wd-wd_1;
		if(wd_1>wd)a+=7;
		unsigned short b=d_last.day();
		if(a>b)a-=7;
		if(a>b)a-=7;                                        
		GREG_DATE monthly_B_date=d_1+days(a-1);
		return monthly_B_date;
	}

	                                                                     
	T_DURATION nextFrequencyTime(const T_DURATION& current_time, const T_DURATION& start_time, const T_DURATION& end_time, long period_in_seconds)
	{
		                                                                                                 
		                                                                                                                     
		if(current_time.is_not_a_date_time() || current_time < start_time)return start_time;
		if(current_time >= start_time && current_time < end_time){
			long delta_seconds = (current_time-start_time).total_seconds();
			int k = delta_seconds % period_in_seconds;
			if(k>=0) k=period_in_seconds-k;
			T_DURATION scheduled_time = current_time + seconds(k);
			if(scheduled_time <= end_time){
				                                                                                                       
				return scheduled_time;
			}
		}
		                                                            
		return T_DURATION(boost::date_time::not_a_date_time);
	}

	void load_holiday_list(const std::string& holiday_list)
	{
		StringTokenizer tk(holiday_list.c_str(),",");
		g_holiday_list.clear();
		while(tk.NextToken()){
			const char* token=tk.token();
			if(token[0]==',')continue;
			GREG_DATE holiday=get_gdate(token);
			if(!holiday.is_not_a_date())
				g_holiday_list.push_back(holiday);
		}
	}

	bool isHoliday(const GREG_DATE& current_date)
	{
		for(std::vector<GREG_DATE>::const_iterator it=g_holiday_list.begin(); it!=g_holiday_list.end(); ++it){
			if(*it == current_date)return true;
			if(*it > current_date)return false;
		}
		return false;
	}

	bool isWeekend(const GREG_DATE& current_date){
		int wkd=current_date.day_of_week().as_number();
		return wkd==0 || wkd==6;
	}

	bool isWeekendHoliday(const GREG_DATE& current_date)
	{
		return isWeekend(current_date) || isHoliday(current_date);
	}
}

namespace {

	const string FROM_NAME("Scheduler");

	const int FREQUENCY_EVERY_S = 2, FREQUENCY_EVERY_M = 1, FREQUENCY_EVERY_H = 60;

	template<typename T>
	void FillPropertiesFromDb(T& dbTab, konga::KeyValues& properties) {
		if(dbTab.IsSchedule_IDNull())
			throw InternalErrorException("Column Schedule_ID is NULL.");

		properties.AddLong("schedule_id",dbTab.GetSchedule_ID());
		properties.AddString("schedule_name",dbTab.GetSchedule_Name());
		properties.AddInt("occurs",dbTab.GetOccurs());
		properties.AddInt("daily",dbTab.GetDaily());
		properties.AddInt("weekly",dbTab.GetWeekly());

		properties.AddInt("mon",dbTab.GetMon());
		properties.AddInt("tue",dbTab.GetTue());
		properties.AddInt("wed",dbTab.GetWed());
		properties.AddInt("thu",dbTab.GetThu());
		properties.AddInt("fri",dbTab.GetFri());
		properties.AddInt("sat",dbTab.GetSat());
		properties.AddInt("sun",dbTab.GetSun());

		properties.AddInt("monthly",dbTab.GetMonthly());
		properties.AddInt("monthly_a_day",dbTab.GetMonthly_A_Day());
		properties.AddInt("monthly_a_of_every_x_month",dbTab.GetMonthly_A_of_every_X_month());
		properties.AddInt("monthly_b_num",dbTab.GetMonthly_B_Num());
		properties.AddInt("monthly_b_day",dbTab.GetMonthly_B_Day());
		properties.AddInt("monthly_b_of_every_x_month",dbTab.GetMonthly_B_of_every_x_month());

		properties.AddInt("frequency",dbTab.GetFrequency());
		properties.AddString("frequency_once_time", dbTab.GetFrequency_once_time());
		properties.AddInt("frequency_every_num",dbTab.GetFrequency_every_num());
		properties.AddInt("frequency_every_hm",dbTab.GetFrequency_every_HM());
		properties.AddString("frequency_start_time", dbTab.GetFrequency_Start_time());
		properties.AddString("frequency_end_time", dbTab.GetFrequency_End_time());

		properties.AddString("duration_start_date", dbTab.GetDuration_Start_date());
		properties.AddString("duration_end_date", dbTab.GetDuration_End_date());
		properties.AddInt("duration_yesno_end", dbTab.GetDuration_YesNo_END());
		properties.AddInt("priority", dbTab.GetPriority());
	}
}

ScheduledOp::ScheduledOp(const PTIME &current_time, Scheduler* pScheduler, TranDb::ScheduledOperationView& sov,
						 konga::DbConnection* pTranDbConnection, Logger* logger, FILE* debug_log) :
	m_pScheduler(pScheduler), m_pLogger(logger), m_debug_log(debug_log), m_bStartEvenIfPreviouslyScheduledOpIsStillRunning(false),
	m_Operation_ID(-1), m_Schedule_ID(-1), m_bFrequencyExtendNextDay(false), m_bNewMonthly_A_Day(false)
{
	assert(m_pScheduler);
	assert(m_pLogger);
	if(sov.IsOperation_IDNull())
		throw InternalErrorException("Database view ScheduledOperationView: the column Operation_ID is undefined");

	konga::KeyValues properties;
	FillPropertiesFromDb<TranDb::ScheduledOperationView>(sov, properties);
	properties.AddLong("operation_id",sov.GetOperation_ID());
	load_properties(properties);
	
	                            
	try {
		ItemProperties ip(pTranDbConnection);
		string sSchedulePolicy;
		if ( ip.GetItemProperty(OperationEntityId, m_Operation_ID, OPERATION_PROPERTY_SCHEDULE_POLICY, sSchedulePolicy) ) {
			if ( sSchedulePolicy=="1" )
				m_bStartEvenIfPreviouslyScheduledOpIsStillRunning = true;
		}
	}
	catch( const CrException& ex ) {
		const string sErr1("Failed to get schedule proberty '" + OPERATION_PROPERTY_SCHEDULE_POLICY + "'. "
			+ ex.GetErrorString());
		m_pLogger->LogWarning(sErr1);
	}

	ctor_common(current_time);
}

ScheduledOp::ScheduledOp(const PTIME& current_time, const long ScheduleId, konga::DbConnection* pTranDbConn, FILE* debug_log) :
	m_pScheduler(0), m_pLogger(0), m_debug_log(debug_log), m_bStartEvenIfPreviouslyScheduledOpIsStillRunning(false),
	m_Operation_ID(-1), m_Schedule_ID(-1), m_bFrequencyExtendNextDay(false), m_bNewMonthly_A_Day(false)
{
	SmartDbPtr<TranDb::ScheduleTab> schedTab( TranDb::ScheduleTab::MakeInstance(pTranDbConn) );
	std::string stSchedTabFilter = "WHERE Schedule_ID = " + konga::toString(ScheduleId);
	schedTab->Select(stSchedTabFilter);
	if(!schedTab->Next()) {
		throw CrException("The selected schedule does not exist on this server.");
	}

	konga::KeyValues properties;
	FillPropertiesFromDb<TranDb::ScheduleTab>(*schedTab, properties);
	load_properties(properties);

	ctor_common(current_time);
}

                                                                                                                       
ScheduledOp::ScheduledOp(const PTIME& current_time, const KeyValues& properties, FILE* debug_log) :
	m_pScheduler(0), m_pLogger(0), m_debug_log(debug_log), m_bStartEvenIfPreviouslyScheduledOpIsStillRunning(false),
	m_Operation_ID(-1), m_Schedule_ID(-1), m_bFrequencyExtendNextDay(false), m_bNewMonthly_A_Day(false)
{
	load_properties(properties);
	ctor_common(current_time);
}

void ScheduledOp::ctor_common(const PTIME& current_time) {
	m_scheduled_date_time=getNextScheduledDateTime(current_time);
	if(m_debug_log) {
		fprintf(m_debug_log, "Configured Schedule '%s'\n", m_ScheduleName.c_str());
		fprintf(m_debug_log, "%s\n", this->toString().c_str());
		fprintf(m_debug_log, "Next Start Time: %s\n", to_simple_string(getScheduledDateTime()).c_str());
		fflush(m_debug_log);
	}
}

void ScheduledOp::load_properties(const konga::KeyValues& properties)
{
	m_Operation_ID=properties.GetInt("operation_id");
	m_Schedule_ID=properties.GetInt("schedule_id");	
	m_ScheduleName=properties.GetString("schedule_name");

	                                                                               
	m_bNewMonthly_A_Day=properties.GetInt("new_monthly_a_day", 0)==1;
	m_monthly_a_day_working=properties.GetInt("monthly_a_day_working", 0);
	                                                           
	std::string holidat_list=properties.GetString("holiday_list");
	load_holiday_list(holidat_list);
	
	m_Occurs=(OccurType)properties.GetInt("occurs");
	m_Frequency=properties.GetInt("frequency");

	string sErr;
	try {
		string Duration_Start_date=properties.GetString("duration_start_date");
		string Duration_End_date=properties.GetString("duration_end_date");
		if(Duration_Start_date.length()>0)
			m_Duration_Start_date=get_gdate(Duration_Start_date);
		m_Duration_yesNo_END=properties.GetInt("duration_yesno_end");
		if(m_Duration_yesNo_END&&Duration_End_date.length()>0){
			m_Duration_End_date=get_gdate(Duration_End_date);
		}

		switch(m_Occurs){
			case Daily_case:
				{
					m_Daily=properties.GetInt("daily");
					m_type=(m_Frequency==0)?Daily_Every:Daily_Once;
				}
				break;
			case Weekly_case:
				{
					m_Weekly=properties.GetInt("weekly");
					m_WeekdaySelected[0]=properties.GetInt("sun")==1;
					m_WeekdaySelected[1]=properties.GetInt("mon")==1;
					m_WeekdaySelected[2]=properties.GetInt("tue")==1;
					m_WeekdaySelected[3]=properties.GetInt("wed")==1;
					m_WeekdaySelected[4]=properties.GetInt("thu")==1;
					m_WeekdaySelected[5]=properties.GetInt("fri")==1;
					m_WeekdaySelected[6]=properties.GetInt("sat")==1;
					m_Duration_Start_date_weekday=m_Duration_Start_date.day_of_week().as_number();
					m_type=(m_Frequency==0)?Weekly_Every:Weekly_Once;
				}
				break;
			case Monthly_case:
				{
					m_Monthly=properties.GetInt("monthly");
					if(m_Monthly==0){
						m_Monthly_A_Day=properties.GetInt("monthly_a_day");
						m_Monthly_A_of_every_X_month=properties.GetInt("monthly_a_of_every_x_month");
						m_type=(m_Frequency==0)?Monthly_A_Every:Monthly_A_Once;
						if(!m_bNewMonthly_A_Day && m_Monthly_A_Day>31)
							throw InternalErrorException("Inconsistent entries in database table ScheduleTab, Occurs = 3 and Monthly = 0, Monthly_A_Day > 31 for " + getScheduleNameForErrorDisplay());
						if(m_Monthly_A_Day==-1||m_Monthly_A_of_every_X_month==-1)
							throw InternalErrorException("Inconsistent entries in database table ScheduleTab, Occurs = 3 and Monthly = 0, but at least one of the columns Monthly_A_Day, Monthly_A_of_every_X_month is undefined for schedule " + getScheduleNameForErrorDisplay());
						if(m_Monthly_A_of_every_X_month==0)
							throw InternalErrorException("Illegal parameter for the schedule: monthly_a_of_every_x_month==0 for " + getScheduleNameForErrorDisplay());
					} else {
						m_Monthly_B_Num=properties.GetInt("monthly_b_num");
						m_Monthly_B_Day=properties.GetInt("monthly_b_day");
						m_Monthly_B_of_every_X_month=properties.GetInt("monthly_b_of_every_x_month");
						m_type=(m_Frequency==0)?Monthly_B_Every:Monthly_B_Once;
						if(m_Monthly_B_Num==-1||m_Monthly_B_Day==-1||m_Monthly_B_of_every_X_month==-1)
							throw InternalErrorException("Inconsistent entries in database table ScheduleTab, Occurs = 3 and Monthly != 0 but at least one of the columns Monthly_B_Num, Monthly_B_Day or Monthly_B_of_every_x_month is undefined for schedule " + getScheduleNameForErrorDisplay());
						if(m_Monthly_B_of_every_X_month==0)
							throw CrException("Illegal parameter for the schedule: monthly_b_of_every_x_month==0 for schedule " + getScheduleNameForErrorDisplay());
					}
				}
				break;
			case Unknown_case:
				break;
		};

		switch(m_type){
			case Daily_Once:
			case Weekly_Once:
			case Monthly_A_Once:
			case Monthly_B_Once:
				{
					string Frequency_once_time=properties.GetString("frequency_once_time");
					if(Frequency_once_time.length()==0)
						throw InternalErrorException("Inconsistent entries in database table ScheduleTab: column Frequency != 0, but the column Frequency_once_time is undefined for " + getScheduleNameForErrorDisplay());
					m_Frequency_once_time=get_time(Frequency_once_time);
					if(m_Frequency_once_time.is_not_a_date_time())
						throw CrException("Illegal parameter for the schedule: frequency_once_time is not date time for schedule " + getScheduleNameForErrorDisplay());
				}
				break;
			case Daily_Every:
			case Weekly_Every:
			case Monthly_A_Every:
			case Monthly_B_Every:
				{
					string Frequency_Start_time=properties.GetString("frequency_start_time");
					m_Frequency_every_num=properties.GetInt("frequency_every_num");
					m_Frequency_every_HMS=properties.GetInt("frequency_every_hm");
					if(m_Frequency_every_num==-1||m_Frequency_every_HMS==-1||Frequency_Start_time.length()==0)
						throw InternalErrorException("Inconsistent entries in database table ScheduleTab: column Frequency = 0, but one of the columns Frequency_every_num, Frequency_Start_time or Frequency_every_HM is undefined for " + getScheduleNameForErrorDisplay());
					m_Frequency_Unit_in_Seconds=(m_Frequency_every_HMS==FREQUENCY_EVERY_H)?3600:((m_Frequency_every_HMS==FREQUENCY_EVERY_M)?60:1);
					m_Frequency_Start_time=get_time(Frequency_Start_time);
					string Frequency_End_time=properties.GetString("frequency_end_time");
					if(Frequency_End_time.length()>0){
						m_Frequency_End_time=get_time(Frequency_End_time);
					} else {
						m_Frequency_End_time=T_DURATION(boost::date_time::not_a_date_time);
					}
					if(!m_Frequency_End_time.is_not_a_date_time()){
						m_bFrequencyExtendNextDay=m_Frequency_Start_time>m_Frequency_End_time;
					}

					if(m_Frequency_every_num==46908) {
						                                                                                    
						                                                                                                       
						m_type = Type(m_type-1);                            
						                                                                                                
						if( m_Frequency_Start_time > m_Frequency_End_time ) {
							T_DURATION diff = T_DURATION(24,0,0) + m_Frequency_End_time - m_Frequency_Start_time;
							T_DURATION rand_diff(0,0,diff.total_seconds() * rand() / RAND_MAX);
							m_Frequency_Start_time += rand_diff;
							if( m_Frequency_Start_time.hours() >= 24 )
								m_Frequency_Start_time -= T_DURATION(24,0,0);
							                                                                                                                             
						} else {
							T_DURATION diff = m_Frequency_End_time - m_Frequency_Start_time;
							T_DURATION rand_diff(0,0,diff.total_seconds() * rand() / RAND_MAX);
							m_Frequency_Start_time += rand_diff;
						}
					}
					else if(m_Frequency_every_HMS!=FREQUENCY_EVERY_S && m_Frequency_every_HMS!=FREQUENCY_EVERY_M && m_Frequency_every_HMS!=FREQUENCY_EVERY_H)
						throw CrException("Illegal parameter for the schedule: m_Frequency_every_HMS="+konga::toString(m_Frequency_every_HMS)
						+" for schedule "+getScheduleNameForErrorDisplay()+". It should be 60 (for hours), 1 (for minutes) or 2 (for seconds).");
					if(m_Frequency_every_num==0)
						throw CrException("Illegal parameter for the schedule: frequency_every_num==0 for schedule "+getScheduleNameForErrorDisplay());
					if(m_Frequency_Start_time.is_not_a_date_time())
						throw CrException("Illegal parameter for the schedule: m_Frequency_Start_time is not date time for schedule "+getScheduleNameForErrorDisplay());
					
				}
				break;
		}
		m_Priority=properties.GetInt("priority");
#ifdef _DEBUG
		if(debug_scheduler>0)printf("%s",toString().c_str());
#endif
		return;
	} catch(boost::gregorian::bad_year& ex1){
		sErr=ex1.what();
	} catch(boost::gregorian::bad_month& ex2){
		sErr=ex2.what();
	} catch(boost::gregorian::bad_day_of_month& ex3){
		sErr=ex3.what();
	} catch(boost::gregorian::bad_day_of_year& ex4){
		sErr=ex4.what();
	} catch(boost::gregorian::bad_weekday& ex5){
		sErr=ex5.what();
	}

	throw CrException("Schedule "+getScheduleNameForErrorDisplay()+" contains bad date format\n"+sErr);
}

#define bit_k(k) 1<<(k-1)

                                                                                        
                                                                                  
                                                                                              
GREG_DATE ScheduledOp::getMonthly_A_start_date() const
{
	if(!m_bNewMonthly_A_Day){
		const int d=m_Duration_Start_date.day();
		int d0=0;
		if(m_Monthly_A_Day < d)d0=m_Duration_Start_date.end_of_month().day();
		return m_Duration_Start_date+days(m_Monthly_A_Day-d+d0);
	}
	else {
		const int d=m_Duration_Start_date.day();
		int dmax=m_Duration_Start_date.end_of_month().day();
		for(int k=d; k<=31; k++){
			if(m_Monthly_A_Day & bit_k(k)){
				if(k>dmax)k=dmax;
				return m_Duration_Start_date+days(k-d);
			}
		}
		for(int k=1; k<d; k++){
			if(m_Monthly_A_Day & bit_k(k)){
				return m_Duration_Start_date+days(dmax+k-d);
			}
		}
		return GREG_DATE();
	}
}

GREG_DATE ScheduledOp::find_next_Monthly_A_day(const GREG_DATE& current_date) const
{
	GREG_DATE start_date=getMonthly_A_start_date();
	if(start_date > current_date)return start_date;
	int start_y=start_date.year(), start_m=start_date.month(), start_d=start_date.day();
	int now_y=current_date.year(), now_m=current_date.month(), now_d=current_date.day();
	int period_in_months=m_Monthly_A_of_every_X_month,
		delta_months=now_m-start_m+(now_y-start_y)*12;
	int k= delta_months % period_in_months;

	GREG_DATE scheduled_date;
	if(!m_bNewMonthly_A_Day){
		if(k>0)now_m+=(period_in_months-k);
		if(k==0 && now_d>=start_d)now_m+=period_in_months;
		while(now_m>12){
			now_m-=12;
			now_y++;
		}

		GREG_DATE tmp(now_y, now_m, 1);
		int days_in_the_month=tmp.end_of_month().day();
		now_d=m_Monthly_A_Day;
		if(days_in_the_month<now_d)now_d=days_in_the_month;
		scheduled_date=GREG_DATE(now_y, now_m, now_d);
	}
	else {
		if(k==0){
			GREG_DATE tmp(now_y, now_m, 1);
			int days_in_the_month=tmp.end_of_month().day();
			for(int k1=now_d+1; k1<days_in_the_month; k1++){
				if((m_Monthly_A_Day & bit_k(k1))!=0){
					scheduled_date = GREG_DATE(now_y, now_m, k1);
					break;
				}
			}
			if(scheduled_date.is_not_a_date()){
				if((m_Monthly_A_Day & bit_k(31))!=0 && now_d!=days_in_the_month){
					scheduled_date = GREG_DATE(now_y, now_m, days_in_the_month);
				}
				else {
					now_m++;
					if(now_m>12){
						now_m-=12;
						now_y++;
					}
					k=1;
					now_d=0;
				}
			}
		}

		if(k==1){
			for(int k1=now_d+1; k1<start_d; k1++){
				if((m_Monthly_A_Day & bit_k(k1))!=0){
					scheduled_date = GREG_DATE(now_y, now_m, k1);
					break;
				}
			}
		}
		if(scheduled_date.is_not_a_date()){
			now_m+=(period_in_months-k);
			while(now_m>12){
				now_m-=12;
				now_y++;
			}
			if((m_Monthly_A_Day & bit_k(31))!=0){
				GREG_DATE tmp(now_y, now_m, 1);
				start_d=tmp.end_of_month().day();
			}
			scheduled_date=GREG_DATE(now_y, now_m, start_d);
		}
	}
	return scheduled_date;
}



                                                                                        
                                                                                  
                                                                                  
GREG_DATE ScheduledOp::getMonthly_B_start_date() const
{
	return getMonthly_B_date(m_Duration_Start_date,m_Monthly_B_Num,m_Monthly_B_Day);
}

GREG_DATE ScheduledOp::final_check(const GREG_DATE& date) const
{
	
	if(!date.is_not_a_date() && m_Duration_yesNo_END !=0){
		if(date > m_Duration_End_date){
			return GREG_DATE(boost::date_time::not_a_date_time);
		}
	}
	return date;
}

bool ScheduledOp::checkOp(PTIME now)
{
	if(m_scheduled_date_time.is_not_a_date_time())return false;
	if(now >= m_scheduled_date_time){
		sendOpMessage();
		if(m_debug_log){
			fprintf(m_debug_log,"sendOpMessage(), opid=%d, scid=%d, scheduled_date_time=\"%s\", now=\"%s\"\n", 
				m_Operation_ID, m_Schedule_ID, to_simple_string(m_scheduled_date_time).c_str(), to_simple_string(now).c_str());
			fflush(m_debug_log);
		}
		PTIME next=m_scheduled_date_time+seconds(1);
		m_scheduled_date_time=getNextScheduledDateTime(next);
		return true;
	}
	return false;
}

bool ScheduledOp::testOp(PTIME now)
{
	if(m_scheduled_date_time.is_not_a_date_time())return false;
	if(now >= m_scheduled_date_time){
		if(m_debug_log){
			fprintf(m_debug_log,"sendOpMessage(), opid=%d, scid=%d, scheduled_date_time=\"%s\", now=\"%s\"\n", 
				m_Operation_ID, m_Schedule_ID, to_simple_string(m_scheduled_date_time).c_str(), to_simple_string(now).c_str());
			fflush(m_debug_log);
		}
		PTIME next=m_scheduled_date_time+seconds(1);
		m_scheduled_date_time=getNextScheduledDateTime(next);
		return true;
	}
	return false;
}



void ScheduledOp::sendOpMessage() const
{
#if debug_scheduler==0
	SmartDbPtr<DbConnection> pMsgDb(
		konga::GetBackEndDbConnection("MsgDb")
		);

    pMsgDb->SetTransactionIsolationLevel(READ_COMMITTED);
    AutoRollbackTransaction occurrenceTransaction(pMsgDb);
    pMsgDb->ExecuteSql("SELECT pg_advisory_xact_lock(427043," + konga::toString(m_Operation_ID) + ")");
    const bool overlap = !m_bStartEvenIfPreviouslyScheduledOpIsStillRunning && IsOperationInQueue(m_Operation_ID, FROM_NAME, pMsgDb);
    const std::string occurrenceSql = "INSERT INTO etlforge_cluster.schedule_occurrence(schedule_id,operation_id,scheduled_local,outcome) VALUES(" +
        konga::toString(m_Schedule_ID) + "," + konga::toString(m_Operation_ID) + ",'" +
        boost::posix_time::to_iso_extended_string(m_scheduled_date_time) + "','" + (overlap ? "skipped_overlap" : "enqueued") +
        "') ON CONFLICT DO NOTHING RETURNING schedule_id";
    SmartDbPtr<DynamicDbRecordSet> occurrence(CreateDynamicDbRecordSet(pMsgDb,occurrenceSql));
    occurrence->Open();
    const bool acquired = occurrence->Next();
    occurrence->Close();
    if(!acquired) { occurrenceTransaction.Commit(); return; }
    if ( overlap ) {
        occurrenceTransaction.Commit();
		const PTIME now=m_pScheduler->getCurrentTime();
		string*sMsg=new string("Still in queue: OperationID="+konga::toString(m_Operation_ID)+" at "+boost::posix_time::to_simple_string(now));
		m_pScheduler->addLatestMessage(sMsg);
		m_pLogger->Log(*sMsg, EVENTLOG_INFORMATION_TYPE);
		if(m_debug_log){
			fprintf(m_debug_log, "%s\n",sMsg->c_str());
			fflush(m_debug_log);
		}
		return;
	}

	konga::AddOperationToQueue(pMsgDb, 0, m_Operation_ID, UNDEFINED_SESSION_ID, UNDEFINED_OPERATION_INSTANCE_ID, FROM_NAME, "Executing operation for schedule " + m_ScheduleName);
    occurrenceTransaction.Commit();
#endif
	const PTIME now=m_pScheduler->getCurrentTime();
	string*sMsg=new string("Added to queue: OperationID="+konga::toString(m_Operation_ID)+" at "+boost::posix_time::to_simple_string(now));
	m_pScheduler->addLatestMessage(sMsg);
	m_pLogger->Log(*sMsg, EVENTLOG_INFORMATION_TYPE);
	if(m_debug_log){
		fprintf(m_debug_log, "%s\n",sMsg->c_str());
		fflush(m_debug_log);
	}
}

void ScheduledOp::sendWorkFlowMessage() const
{
	konga::ProcessMessage message;
	message.SetProcessName("Interchange");
	message.SetTaskName("1");
	message.SetFrom(FROM_NAME);
	message.SetTo("ProcessEngine");
	message.SetSubject("Executing workflow for scheduler " + m_ScheduleName);
	message.AddDataElement("OperationID", konga::toString(m_Operation_ID), konga::DET_INT);

	MessageSender sender;
	sender.AddMessageToQueue(message);
}

string WeekDaySelected(bool wds[]){
	char buf[7];
	for(int i=0;i<7;i++)buf[i]=wds[i]?'T':'F';
	return string(buf,7);
}

string ScheduledOp::toString() const {
	const char*type_str[]={"Daily_Once","Daily_Every","Weekly_Once","Weekly_Every","Monthly_A_Once","Monthly_A_Every","Monthly_B_Once","Monthly_B_Every"};
	const char*type_str_monthly_b[]={"1st","2nd","3rd","4th","last"};
	const char*type_str_weekday[]={"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
	const char*type_str_HMS[]={"Hour(s)","Minute(s)","Second(s)"};
	string str("---- ScheduledOp ----\n");
	str+="Operation_ID="+konga::toString(m_Operation_ID);
	str+=", Schedule_ID="+konga::toString(m_Schedule_ID);
	str+=", "+string(type_str[m_type-1]);
	str+="\nOccurence: ";
	switch(m_Occurs){
		case Daily_case:
			{
				str+="Every "+konga::toString(m_Daily)+" Day(s)";
			}
			break;
		case Weekly_case:
			{
				str+=string("Every ")+konga::toString(m_Weekly)+" Week(s) select ";
				bool bStart=true;
				for(int i=0;i<7;i++){
					if(m_WeekdaySelected[i]){
						if(!bStart)str+=",";
						bStart=false;
						str+=type_str_weekday[i];
					}
				}
				if(bStart)str+="Nonselected";
			}
			break;
		case Monthly_case:
			{
				if(m_Monthly==0){
					str+=string("Day ")+konga::toString(m_Monthly_A_Day)
						+string(" of every ")+konga::toString(m_Monthly_A_of_every_X_month)+string(" Month");
				} else {
					str+=string("The ")+string(type_str_monthly_b[m_Monthly_B_Num-1])+string(" ")+string(type_str_weekday[m_Monthly_B_Day])
						+string(" of every ")+konga::toString(m_Monthly_B_of_every_X_month)+string(" Month");
				}
			}
			break;
		case Unknown_case:
			                  
			break;
	};

	str+="\nDaily Frequency: ";
	switch(m_type){
		case Daily_Once:
		case Weekly_Once:
		case Monthly_A_Once:
		case Monthly_B_Once:
			{
				str+=string("Occurs once at: ")+to_simple_string(m_Frequency_once_time);
			}
			break;
		case Daily_Every:
		case Weekly_Every:
		case Monthly_A_Every:
		case Monthly_B_Every:
			{
				str+=string("Occurs every: ")+konga::toString(m_Frequency_every_num)+string(" ")+string(type_str_HMS[m_Frequency_every_HMS]);
				str+=", Starting at:"+to_simple_string(m_Frequency_Start_time);
				if(!m_Frequency_End_time.is_not_a_date_time())str+=", Ending at:"+to_simple_string(m_Frequency_End_time);
			}
			break;
	}
	str+="\nDuration: ";
	str+="Start_date:"+to_simple_string(m_Duration_Start_date);
	if(!m_Duration_End_date.is_not_a_date())
		str+=", End_date:"+to_simple_string(m_Duration_End_date);

	str+="\n---------------------\n";
	return str;
}

PTIME ScheduledOp::getNextScheduledDateTime(const PTIME& now) const {
	if(debug_scheduler)printf("    getNextScheduledDateTime(), now=%s\n", to_simple_string(now).c_str());
	if(now.is_not_a_date_time())return PTIME();

	GREG_DATE current_date=now.date(), scheduled_date(boost::date_time::not_a_date_time);
	T_DURATION current_time=now.time_of_day(), scheduled_time(boost::date_time::not_a_date_time);

                                                                                                                                                                         
	if(m_bFrequencyExtendNextDay && current_time < m_Frequency_End_time){
		current_time += hours(24);
		current_date -= days(1);
	}

	if(isValidScheduledDate(current_date)){
		scheduled_date=current_date;
		scheduled_time=getNextScheduledTime(current_time);
		if(scheduled_time.is_not_a_date_time()){
			scheduled_date=getNextScheduledDate(current_date);
			if(scheduled_date.is_not_a_date())return PTIME();
			scheduled_time=getNextScheduledTime(scheduled_time);
		}
	}
	else {
		scheduled_date=getNextScheduledDate(current_date);
		if(scheduled_date.is_not_a_date())return PTIME();
		scheduled_time=getNextScheduledTime(scheduled_time);
	}
	if(scheduled_time >= hours(24)){
		scheduled_date += days(1);
		scheduled_time -=hours(24);
	}
	PTIME scheduled_date_time(scheduled_date, scheduled_time);
	if(debug_scheduler)printf("    return scheduled_date_time=%s\n", to_simple_string(scheduled_date_time).c_str());
	return scheduled_date_time;
}


bool ScheduledOp::isValidScheduledDate(const GREG_DATE& current_date) const
{
	if(debug_scheduler)printf("\tisValidScheduledDate(), current_date=%s", to_simple_string(current_date).c_str());
	bool bValid=false;
	if(!current_date.is_not_a_date() &&
		!(current_date < m_Duration_Start_date) &&
		!(m_Duration_yesNo_END==1 && current_date > m_Duration_End_date)){
		switch(m_type){
			case Daily_Once:
			case Daily_Every:
			{
				long period_in_days=m_Daily, delta_days=(current_date-m_Duration_Start_date).days();
				long k=delta_days%period_in_days;
				bValid = (k==0);
				break;
			}
			case Weekly_Once:
			case Weekly_Every:                                                                                                     
			{
				long period_in_days=m_Weekly*7, delta_days=(current_date-m_Duration_Start_date).days();
				long k = delta_days%period_in_days;
				if(k<7)bValid = (m_WeekdaySelected[(k+m_Duration_Start_date_weekday)%7]);
				break;
			}
			case Monthly_A_Once:
			case Monthly_A_Every:
			{
				GREG_DATE start_date=getMonthly_A_start_date();
				if(debug_scheduler)printf(", start_date=%s", to_simple_string(start_date).c_str());
				if(current_date < start_date)break;
				GREG_DATE next_date;
				if(current_date == start_date){
					next_date=start_date;
				}
				else {
					int shift_days=-1;                                                                                
					switch(m_monthly_a_day_working){
					case -1:
						if(isWeekendHoliday(current_date))return false;
						while(isWeekendHoliday(current_date+days(shift_days))){
							shift_days--;
						}
						break;
					case 1:
						if(isWeekendHoliday(current_date))return false;
						while(isWeekendHoliday(current_date+days(shift_days))){
							shift_days--;
						}
						break;
					}
					next_date=find_next_Monthly_A_day(current_date+days(shift_days));
				}
				switch(m_monthly_a_day_working){
				case -1:
					while(isWeekendHoliday(next_date))next_date-=days(1);
					break;
				case 1:
					while(isWeekendHoliday(next_date))next_date+=days(1);
					break;
				}
				bValid = (next_date==current_date);
				break;
			}
			case Monthly_B_Once:
			case Monthly_B_Every:
			{
				GREG_DATE start_date=getMonthly_B_start_date();
				if(current_date < start_date)break;
				if(current_date == start_date){
					bValid = true;
				}
				else {
					int start_m=start_date.month(), start_y=start_date.year();
					int now_m=current_date.month(), now_y=current_date.year();
					GREG_DATE cycle_date=getValidYearMonth(start_y, start_m, now_y, now_m, m_Monthly_B_of_every_X_month);
					cycle_date=getMonthly_B_date(cycle_date, m_Monthly_B_Num, m_Monthly_B_Day);
					bValid = current_date==cycle_date;
				}
				break;
			}
		}
	}
	if(debug_scheduler)printf(" -> return %s\n", bValid?"true":"false");
	return bValid;
}

GREG_DATE ScheduledOp::getNextScheduledDate(const GREG_DATE& current_date) const {
	if(debug_scheduler)printf("\tgetNextScheduledDate(), current_date=%s", to_simple_string(current_date).c_str());
	GREG_DATE scheduled_date(boost::date_time::not_a_date_time);
	if(!current_date.is_not_a_date() &&
		!(m_Duration_yesNo_END==1 && current_date > m_Duration_End_date)){
		switch(m_type){
			case Daily_Once:
			case Daily_Every:
			{
				if(current_date < m_Duration_Start_date)return m_Duration_Start_date;
				long period_in_days=m_Daily, delta_days = (current_date-m_Duration_Start_date).days();
				long k=delta_days%period_in_days;
				if(k>=0)k=period_in_days-k;
				scheduled_date = current_date+days(k);
				break;
			}
			case Weekly_Once:
			case Weekly_Every:                                                                                                     
			{
				long period_in_days=m_Weekly*7, delta_days=(current_date-m_Duration_Start_date).days();
				GREG_DATE current_date_;
				if(delta_days<0){
					current_date_=m_Duration_Start_date;
					delta_days=0;
				}
				else {
					current_date_ = current_date+days(1);
					delta_days++;
				}
				long k = delta_days%period_in_days;
				for(int i=k; i<7; i++){
					if(m_WeekdaySelected[(i+m_Duration_Start_date_weekday)%7]){
						scheduled_date = current_date_ + days(i-k);
						break;
					}
				}
				if(scheduled_date.is_not_a_date()){
					                                 
					current_date_ += days(period_in_days-k);
					for(int i=0; i<7; ++i){
						if(m_WeekdaySelected[(i+m_Duration_Start_date_weekday)%7]){
							scheduled_date = current_date_ + days(i);
							break;
						}
					}
				}
				break;
			}
			case Monthly_A_Once:
			case Monthly_A_Every:
				{
					int shift_days=0;
					if(m_monthly_a_day_working == -1){
						while(isWeekendHoliday(current_date+days(shift_days+1))){
							shift_days++;
						}
					}
					scheduled_date=find_next_Monthly_A_day(current_date+days(shift_days));
					switch(m_monthly_a_day_working){
						case -1:
							while(isWeekendHoliday(scheduled_date))scheduled_date-=days(1);
							break;
						case 1:
							while(isWeekendHoliday(scheduled_date))scheduled_date+=days(1);
							break;
					}
				}
				break;
			case Monthly_B_Once:
			case Monthly_B_Every:
				{
					GREG_DATE start_date=getMonthly_B_start_date();

					if(current_date < start_date){
						scheduled_date = start_date;
					}
					else {
						int start_m=start_date.month(), start_y=start_date.year();
						int now_m=current_date.month(), now_y=current_date.year();
						GREG_DATE cycle_date=getValidYearMonth(start_y, start_m, now_y, now_m, m_Monthly_B_of_every_X_month);
						cycle_date=getMonthly_B_date(cycle_date, m_Monthly_B_Num, m_Monthly_B_Day);
						if(cycle_date <= current_date){
							++now_m;
							cycle_date=getValidYearMonth(start_y, start_m, now_y, now_m, m_Monthly_B_of_every_X_month);
							cycle_date=getMonthly_B_date(cycle_date, m_Monthly_B_Num, m_Monthly_B_Day);
						}
						scheduled_date = cycle_date;
					}
					break;
				}
		}
	}
	scheduled_date=final_check(scheduled_date);
	if(debug_scheduler)printf(" -> return %s\n", to_simple_string(scheduled_date).c_str());
	return scheduled_date;
}

                                                        
T_DURATION ScheduledOp::getNextScheduledTime(const T_DURATION &current_time) const
{
	if(debug_scheduler)printf("\tgetNextScheduledTime(), current_time=%s", to_simple_string(current_time).c_str());
	T_DURATION schedule_time(boost::date_time::not_a_date_time);
	switch(m_type){
		case Daily_Once:
		case Weekly_Once:
		case Monthly_A_Once:
		case Monthly_B_Once:
			if(current_time.is_not_a_date_time())
				schedule_time = m_Frequency_once_time;
			else if(current_time < m_Frequency_once_time )
				schedule_time = m_Frequency_once_time;
			break;
		case Daily_Every:
		case Weekly_Every:
		case Monthly_A_Every:
		case Monthly_B_Every:
			if(current_time.is_not_a_date_time()){
				schedule_time = m_Frequency_Start_time;
			}
			else {
				T_DURATION end_time = m_Frequency_End_time;
				if(m_bFrequencyExtendNextDay)end_time += hours(24);
				schedule_time = nextFrequencyTime(current_time, m_Frequency_Start_time, end_time, 	m_Frequency_every_num * m_Frequency_Unit_in_Seconds);
			}
			break;
	}
	if(debug_scheduler)printf(" -> return %s\n",to_simple_string(schedule_time).c_str());
	return schedule_time;
}

string ScheduledOp::getScheduleNameForErrorDisplay() const {
	return "\"" + m_ScheduleName + "\" (Schedule_Id=" + konga::toString(m_Schedule_ID) + ")";
}
