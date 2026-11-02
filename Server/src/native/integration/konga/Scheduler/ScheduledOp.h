/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#ifndef ETLFORGE_SCHEDULER_SCHEDULEDOP_H
#define ETLFORGE_SCHEDULER_SCHEDULEDOP_H

#include <string>

#include <boost/date_time/posix_time/posix_time.hpp>
#include <boost/date_time/gregorian/gregorian.hpp>
#include <boost/date_time/local_time_adjustor.hpp>
#include <boost/date_time/c_local_time_adjustor.hpp>

#include <konga/Utils/DBConnectionInfo.h>
#include <konga/Db/TranDb/ScheduledOperationView.h>
#include <konga/Db/TranDb/ScheduleTab.h>

typedef boost::gregorian::date GREG_DATE;
typedef boost::posix_time::ptime PTIME;
typedef boost::posix_time::time_duration T_DURATION;

namespace konga {
  
                          
                               
                              
                           
                              
                             
                           
            
                                    
                                   
            
                                    
                                   
  

class Logger;
class Scheduler;
class KeyValues;

class ScheduledOp {

public:
	                                                                 
	                                                                      
	                           
	ScheduledOp(const PTIME& current_time, Scheduler* pScheduler, TranDb::ScheduledOperationView& sov,
		konga::DbConnection* pTranDbConn, Logger* logger, FILE* debug_log);

	                                                                                                                       
	ScheduledOp(const PTIME& current_time, const KeyValues& scheduleProperties, FILE* debug_log);
	ScheduledOp(const PTIME& current_time, const long schedule_id, konga::DbConnection* pTranDbConn, FILE* debug_log);

	PTIME getNextScheduledDateTime(const PTIME& now) const;

	bool checkOp(PTIME now);
	bool testOp(PTIME now);
	void sendOpMessage() const;
	void sendWorkFlowMessage() const;

	long getScheduleId() const { return m_Schedule_ID; }
	long getOperationId() const { return m_Operation_ID; }

	std::string toString() const;

	const PTIME& getScheduledDateTime() const {
		return m_scheduled_date_time;
	}

	std::string getScheduleName(){return m_ScheduleName;}
private:
	void ctor_common(const PTIME& current_time);
	void load_properties(const konga::KeyValues& properties);

	bool isValidScheduledDate(const GREG_DATE& current_date) const;
	GREG_DATE getNextScheduledDate(const GREG_DATE& current_date) const;
	T_DURATION getNextScheduledTime(const T_DURATION& current_time) const;

	GREG_DATE getMonthly_A_start_date() const;
	GREG_DATE getMonthly_B_start_date() const;
	GREG_DATE final_check(const GREG_DATE& gd) const;
	GREG_DATE find_next_Monthly_A_day(const GREG_DATE& current_date) const;

	std::string getScheduleNameForErrorDisplay() const;

private:
	Scheduler* m_pScheduler;
	Logger* m_pLogger;

	FILE* m_debug_log;

	PTIME m_scheduled_date_time;

	enum OccurType {Unknown_case=0, Daily_case=1, Weekly_case, Monthly_case} m_Occurs;
	enum Type {Daily_Once=1, Daily_Every, Weekly_Once, Weekly_Every, Monthly_A_Once, Monthly_A_Every, Monthly_B_Once, Monthly_B_Every } m_type;

	bool m_bStartEvenIfPreviouslyScheduledOpIsStillRunning;

	int m_Operation_ID, m_Schedule_ID, m_Daily, m_Weekly,
		m_Monthly, m_Monthly_A_Day, m_Monthly_A_of_every_X_month,
		m_Monthly_B_Num, m_Monthly_B_Day, m_Monthly_B_of_every_X_month,
		m_Frequency, m_Frequency_every_num, m_Frequency_every_HMS, m_Frequency_Unit_in_Seconds,
		m_Priority;

	std::string m_ScheduleName;

	bool m_WeekdaySelected[7];
	int m_Duration_Start_date_weekday;

	T_DURATION m_Frequency_once_time, m_Frequency_Start_time, m_Frequency_End_time;
	GREG_DATE m_Duration_Start_date, m_Duration_End_date;
	int m_Duration_yesNo_END;
	bool m_bFrequencyExtendNextDay;
	bool m_bNewMonthly_A_Day;
	                                                                                                   
	int m_monthly_a_day_working;

};

}

#endif                                    

