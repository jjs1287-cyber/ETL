/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_TRANDB_TEMPLATE_SCHEDULETAB_H
#define KONGA_DB_TRANDB_TEMPLATE_SCHEDULETAB_H

#ifdef WIN32
	#include <windows.h>
#endif
#include <string>
#include <sqltypes.h>

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/DbRecordSet.h>
#include <konga/Db/DbUtils/DbNullException.h>

namespace TranDb
{

class ScheduleTabRow
{
public:
	ScheduleTabRow() : 
			field_Schedule_ID(0),
			IsSchedule_IDNull(true),
			IsSchedule_IDPrimaryKey(true),
			IsSchedule_IDDirty(false),
			IsSchedule_NameNull(true),
			IsSchedule_NamePrimaryKey(false),
			IsSchedule_NameDirty(false),
			field_Occurs(0),
			IsOccursNull(true),
			IsOccursPrimaryKey(false),
			IsOccursDirty(false),
			field_Daily(0),
			IsDailyNull(true),
			IsDailyPrimaryKey(false),
			IsDailyDirty(false),
			field_Weekly(0),
			IsWeeklyNull(true),
			IsWeeklyPrimaryKey(false),
			IsWeeklyDirty(false),
			field_Mon(0),
			IsMonNull(true),
			IsMonPrimaryKey(false),
			IsMonDirty(false),
			field_Tue(0),
			IsTueNull(true),
			IsTuePrimaryKey(false),
			IsTueDirty(false),
			field_Wed(0),
			IsWedNull(true),
			IsWedPrimaryKey(false),
			IsWedDirty(false),
			field_Thu(0),
			IsThuNull(true),
			IsThuPrimaryKey(false),
			IsThuDirty(false),
			field_Fri(0),
			IsFriNull(true),
			IsFriPrimaryKey(false),
			IsFriDirty(false),
			field_Sat(0),
			IsSatNull(true),
			IsSatPrimaryKey(false),
			IsSatDirty(false),
			field_Sun(0),
			IsSunNull(true),
			IsSunPrimaryKey(false),
			IsSunDirty(false),
			field_Monthly(0),
			IsMonthlyNull(true),
			IsMonthlyPrimaryKey(false),
			IsMonthlyDirty(false),
			field_Monthly_A_Day(0),
			IsMonthly_A_DayNull(true),
			IsMonthly_A_DayPrimaryKey(false),
			IsMonthly_A_DayDirty(false),
			field_Monthly_A_of_every_X_month(0),
			IsMonthly_A_of_every_X_monthNull(true),
			IsMonthly_A_of_every_X_monthPrimaryKey(false),
			IsMonthly_A_of_every_X_monthDirty(false),
			field_Monthly_B_Num(0),
			IsMonthly_B_NumNull(true),
			IsMonthly_B_NumPrimaryKey(false),
			IsMonthly_B_NumDirty(false),
			field_Monthly_B_Day(0),
			IsMonthly_B_DayNull(true),
			IsMonthly_B_DayPrimaryKey(false),
			IsMonthly_B_DayDirty(false),
			field_Monthly_B_of_every_x_month(0),
			IsMonthly_B_of_every_x_monthNull(true),
			IsMonthly_B_of_every_x_monthPrimaryKey(false),
			IsMonthly_B_of_every_x_monthDirty(false),
			field_Frequency(0),
			IsFrequencyNull(true),
			IsFrequencyPrimaryKey(false),
			IsFrequencyDirty(false),
			IsFrequency_once_timeNull(true),
			IsFrequency_once_timePrimaryKey(false),
			IsFrequency_once_timeDirty(false),
			field_Frequency_every_num(0),
			IsFrequency_every_numNull(true),
			IsFrequency_every_numPrimaryKey(false),
			IsFrequency_every_numDirty(false),
			field_Frequency_every_HM(0),
			IsFrequency_every_HMNull(true),
			IsFrequency_every_HMPrimaryKey(false),
			IsFrequency_every_HMDirty(false),
			IsFrequency_Start_timeNull(true),
			IsFrequency_Start_timePrimaryKey(false),
			IsFrequency_Start_timeDirty(false),
			IsFrequency_End_timeNull(true),
			IsFrequency_End_timePrimaryKey(false),
			IsFrequency_End_timeDirty(false),
			IsDuration_Start_dateNull(true),
			IsDuration_Start_datePrimaryKey(false),
			IsDuration_Start_dateDirty(false),
			IsDuration_End_dateNull(true),
			IsDuration_End_datePrimaryKey(false),
			IsDuration_End_dateDirty(false),
			field_Duration_YesNo_END(0),
			IsDuration_YesNo_ENDNull(true),
			IsDuration_YesNo_ENDPrimaryKey(false),
			IsDuration_YesNo_ENDDirty(false),
			field_Priority(0),
			IsPriorityNull(true),
			IsPriorityPrimaryKey(false),
			IsPriorityDirty(false),
			IsInsertTimeNull(true),
			IsInsertTimePrimaryKey(false),
			IsInsertTimeDirty(false),
			IsModTimeNull(true),
			IsModTimePrimaryKey(false),
			IsModTimeDirty(false)
	{
		field_InsertTime.year = 0;
		field_InsertTime.month = 0;
		field_InsertTime.day = 0;
		field_InsertTime.hour = 0;
		field_InsertTime.minute = 0;
		field_InsertTime.second = 0;
		field_InsertTime.fraction = 0;
		field_ModTime.year = 0;
		field_ModTime.month = 0;
		field_ModTime.day = 0;
		field_ModTime.hour = 0;
		field_ModTime.minute = 0;
		field_ModTime.second = 0;
		field_ModTime.fraction = 0;
	}

	void Copy(const ScheduleTabRow& r)
	{
		field_Schedule_ID = r.field_Schedule_ID;
		IsSchedule_IDNull = r.IsSchedule_IDNull;
		IsSchedule_IDDirty = r.IsSchedule_IDDirty;

		field_Schedule_Name = r.field_Schedule_Name;
		IsSchedule_NameNull = r.IsSchedule_NameNull;
		IsSchedule_NameDirty = r.IsSchedule_NameDirty;

		field_Occurs = r.field_Occurs;
		IsOccursNull = r.IsOccursNull;
		IsOccursDirty = r.IsOccursDirty;

		field_Daily = r.field_Daily;
		IsDailyNull = r.IsDailyNull;
		IsDailyDirty = r.IsDailyDirty;

		field_Weekly = r.field_Weekly;
		IsWeeklyNull = r.IsWeeklyNull;
		IsWeeklyDirty = r.IsWeeklyDirty;

		field_Mon = r.field_Mon;
		IsMonNull = r.IsMonNull;
		IsMonDirty = r.IsMonDirty;

		field_Tue = r.field_Tue;
		IsTueNull = r.IsTueNull;
		IsTueDirty = r.IsTueDirty;

		field_Wed = r.field_Wed;
		IsWedNull = r.IsWedNull;
		IsWedDirty = r.IsWedDirty;

		field_Thu = r.field_Thu;
		IsThuNull = r.IsThuNull;
		IsThuDirty = r.IsThuDirty;

		field_Fri = r.field_Fri;
		IsFriNull = r.IsFriNull;
		IsFriDirty = r.IsFriDirty;

		field_Sat = r.field_Sat;
		IsSatNull = r.IsSatNull;
		IsSatDirty = r.IsSatDirty;

		field_Sun = r.field_Sun;
		IsSunNull = r.IsSunNull;
		IsSunDirty = r.IsSunDirty;

		field_Monthly = r.field_Monthly;
		IsMonthlyNull = r.IsMonthlyNull;
		IsMonthlyDirty = r.IsMonthlyDirty;

		field_Monthly_A_Day = r.field_Monthly_A_Day;
		IsMonthly_A_DayNull = r.IsMonthly_A_DayNull;
		IsMonthly_A_DayDirty = r.IsMonthly_A_DayDirty;

		field_Monthly_A_of_every_X_month = r.field_Monthly_A_of_every_X_month;
		IsMonthly_A_of_every_X_monthNull = r.IsMonthly_A_of_every_X_monthNull;
		IsMonthly_A_of_every_X_monthDirty = r.IsMonthly_A_of_every_X_monthDirty;

		field_Monthly_B_Num = r.field_Monthly_B_Num;
		IsMonthly_B_NumNull = r.IsMonthly_B_NumNull;
		IsMonthly_B_NumDirty = r.IsMonthly_B_NumDirty;

		field_Monthly_B_Day = r.field_Monthly_B_Day;
		IsMonthly_B_DayNull = r.IsMonthly_B_DayNull;
		IsMonthly_B_DayDirty = r.IsMonthly_B_DayDirty;

		field_Monthly_B_of_every_x_month = r.field_Monthly_B_of_every_x_month;
		IsMonthly_B_of_every_x_monthNull = r.IsMonthly_B_of_every_x_monthNull;
		IsMonthly_B_of_every_x_monthDirty = r.IsMonthly_B_of_every_x_monthDirty;

		field_Frequency = r.field_Frequency;
		IsFrequencyNull = r.IsFrequencyNull;
		IsFrequencyDirty = r.IsFrequencyDirty;

		field_Frequency_once_time = r.field_Frequency_once_time;
		IsFrequency_once_timeNull = r.IsFrequency_once_timeNull;
		IsFrequency_once_timeDirty = r.IsFrequency_once_timeDirty;

		field_Frequency_every_num = r.field_Frequency_every_num;
		IsFrequency_every_numNull = r.IsFrequency_every_numNull;
		IsFrequency_every_numDirty = r.IsFrequency_every_numDirty;

		field_Frequency_every_HM = r.field_Frequency_every_HM;
		IsFrequency_every_HMNull = r.IsFrequency_every_HMNull;
		IsFrequency_every_HMDirty = r.IsFrequency_every_HMDirty;

		field_Frequency_Start_time = r.field_Frequency_Start_time;
		IsFrequency_Start_timeNull = r.IsFrequency_Start_timeNull;
		IsFrequency_Start_timeDirty = r.IsFrequency_Start_timeDirty;

		field_Frequency_End_time = r.field_Frequency_End_time;
		IsFrequency_End_timeNull = r.IsFrequency_End_timeNull;
		IsFrequency_End_timeDirty = r.IsFrequency_End_timeDirty;

		field_Duration_Start_date = r.field_Duration_Start_date;
		IsDuration_Start_dateNull = r.IsDuration_Start_dateNull;
		IsDuration_Start_dateDirty = r.IsDuration_Start_dateDirty;

		field_Duration_End_date = r.field_Duration_End_date;
		IsDuration_End_dateNull = r.IsDuration_End_dateNull;
		IsDuration_End_dateDirty = r.IsDuration_End_dateDirty;

		field_Duration_YesNo_END = r.field_Duration_YesNo_END;
		IsDuration_YesNo_ENDNull = r.IsDuration_YesNo_ENDNull;
		IsDuration_YesNo_ENDDirty = r.IsDuration_YesNo_ENDDirty;

		field_Priority = r.field_Priority;
		IsPriorityNull = r.IsPriorityNull;
		IsPriorityDirty = r.IsPriorityDirty;

		field_InsertTime = r.field_InsertTime;
		IsInsertTimeNull = r.IsInsertTimeNull;
		IsInsertTimeDirty = r.IsInsertTimeDirty;

		field_ModTime = r.field_ModTime;
		IsModTimeNull = r.IsModTimeNull;
		IsModTimeDirty = r.IsModTimeDirty;

	}

	void CopyPKs(const ScheduleTabRow& r)
	{
		field_Schedule_ID = r.field_Schedule_ID;
		IsSchedule_IDNull = r.IsSchedule_IDNull;
		IsSchedule_IDDirty = r.IsSchedule_IDDirty;

	}

	void SetDirty(bool b)
	{
		IsSchedule_IDDirty = b;
		IsSchedule_NameDirty = b;
		IsOccursDirty = b;
		IsDailyDirty = b;
		IsWeeklyDirty = b;
		IsMonDirty = b;
		IsTueDirty = b;
		IsWedDirty = b;
		IsThuDirty = b;
		IsFriDirty = b;
		IsSatDirty = b;
		IsSunDirty = b;
		IsMonthlyDirty = b;
		IsMonthly_A_DayDirty = b;
		IsMonthly_A_of_every_X_monthDirty = b;
		IsMonthly_B_NumDirty = b;
		IsMonthly_B_DayDirty = b;
		IsMonthly_B_of_every_x_monthDirty = b;
		IsFrequencyDirty = b;
		IsFrequency_once_timeDirty = b;
		IsFrequency_every_numDirty = b;
		IsFrequency_every_HMDirty = b;
		IsFrequency_Start_timeDirty = b;
		IsFrequency_End_timeDirty = b;
		IsDuration_Start_dateDirty = b;
		IsDuration_End_dateDirty = b;
		IsDuration_YesNo_ENDDirty = b;
		IsPriorityDirty = b;
		IsInsertTimeDirty = b;
		IsModTimeDirty = b;
	}

	bool IsDirty() const
	{
		return IsSchedule_IDDirty ||
			IsSchedule_NameDirty ||
			IsOccursDirty ||
			IsDailyDirty ||
			IsWeeklyDirty ||
			IsMonDirty ||
			IsTueDirty ||
			IsWedDirty ||
			IsThuDirty ||
			IsFriDirty ||
			IsSatDirty ||
			IsSunDirty ||
			IsMonthlyDirty ||
			IsMonthly_A_DayDirty ||
			IsMonthly_A_of_every_X_monthDirty ||
			IsMonthly_B_NumDirty ||
			IsMonthly_B_DayDirty ||
			IsMonthly_B_of_every_x_monthDirty ||
			IsFrequencyDirty ||
			IsFrequency_once_timeDirty ||
			IsFrequency_every_numDirty ||
			IsFrequency_every_HMDirty ||
			IsFrequency_Start_timeDirty ||
			IsFrequency_End_timeDirty ||
			IsDuration_Start_dateDirty ||
			IsDuration_End_dateDirty ||
			IsDuration_YesNo_ENDDirty ||
			IsPriorityDirty ||
			IsInsertTimeDirty ||
			IsModTimeDirty;
	}

	int field_Schedule_ID;
	bool IsSchedule_IDNull;
	bool IsSchedule_IDDirty;

	std::string field_Schedule_Name;
	bool IsSchedule_NameNull;
	bool IsSchedule_NameDirty;

	int field_Occurs;
	bool IsOccursNull;
	bool IsOccursDirty;

	int field_Daily;
	bool IsDailyNull;
	bool IsDailyDirty;

	int field_Weekly;
	bool IsWeeklyNull;
	bool IsWeeklyDirty;

	int field_Mon;
	bool IsMonNull;
	bool IsMonDirty;

	int field_Tue;
	bool IsTueNull;
	bool IsTueDirty;

	int field_Wed;
	bool IsWedNull;
	bool IsWedDirty;

	int field_Thu;
	bool IsThuNull;
	bool IsThuDirty;

	int field_Fri;
	bool IsFriNull;
	bool IsFriDirty;

	int field_Sat;
	bool IsSatNull;
	bool IsSatDirty;

	int field_Sun;
	bool IsSunNull;
	bool IsSunDirty;

	int field_Monthly;
	bool IsMonthlyNull;
	bool IsMonthlyDirty;

	int field_Monthly_A_Day;
	bool IsMonthly_A_DayNull;
	bool IsMonthly_A_DayDirty;

	int field_Monthly_A_of_every_X_month;
	bool IsMonthly_A_of_every_X_monthNull;
	bool IsMonthly_A_of_every_X_monthDirty;

	int field_Monthly_B_Num;
	bool IsMonthly_B_NumNull;
	bool IsMonthly_B_NumDirty;

	int field_Monthly_B_Day;
	bool IsMonthly_B_DayNull;
	bool IsMonthly_B_DayDirty;

	int field_Monthly_B_of_every_x_month;
	bool IsMonthly_B_of_every_x_monthNull;
	bool IsMonthly_B_of_every_x_monthDirty;

	int field_Frequency;
	bool IsFrequencyNull;
	bool IsFrequencyDirty;

	std::string field_Frequency_once_time;
	bool IsFrequency_once_timeNull;
	bool IsFrequency_once_timeDirty;

	int field_Frequency_every_num;
	bool IsFrequency_every_numNull;
	bool IsFrequency_every_numDirty;

	int field_Frequency_every_HM;
	bool IsFrequency_every_HMNull;
	bool IsFrequency_every_HMDirty;

	std::string field_Frequency_Start_time;
	bool IsFrequency_Start_timeNull;
	bool IsFrequency_Start_timeDirty;

	std::string field_Frequency_End_time;
	bool IsFrequency_End_timeNull;
	bool IsFrequency_End_timeDirty;

	std::string field_Duration_Start_date;
	bool IsDuration_Start_dateNull;
	bool IsDuration_Start_dateDirty;

	std::string field_Duration_End_date;
	bool IsDuration_End_dateNull;
	bool IsDuration_End_dateDirty;

	int field_Duration_YesNo_END;
	bool IsDuration_YesNo_ENDNull;
	bool IsDuration_YesNo_ENDDirty;

	int field_Priority;
	bool IsPriorityNull;
	bool IsPriorityDirty;

	TIMESTAMP_STRUCT field_InsertTime;
	bool IsInsertTimeNull;
	bool IsInsertTimeDirty;

	TIMESTAMP_STRUCT field_ModTime;
	bool IsModTimeNull;
	bool IsModTimeDirty;


private:
	bool IsSchedule_IDPrimaryKey;
	bool IsSchedule_NamePrimaryKey;
	bool IsOccursPrimaryKey;
	bool IsDailyPrimaryKey;
	bool IsWeeklyPrimaryKey;
	bool IsMonPrimaryKey;
	bool IsTuePrimaryKey;
	bool IsWedPrimaryKey;
	bool IsThuPrimaryKey;
	bool IsFriPrimaryKey;
	bool IsSatPrimaryKey;
	bool IsSunPrimaryKey;
	bool IsMonthlyPrimaryKey;
	bool IsMonthly_A_DayPrimaryKey;
	bool IsMonthly_A_of_every_X_monthPrimaryKey;
	bool IsMonthly_B_NumPrimaryKey;
	bool IsMonthly_B_DayPrimaryKey;
	bool IsMonthly_B_of_every_x_monthPrimaryKey;
	bool IsFrequencyPrimaryKey;
	bool IsFrequency_once_timePrimaryKey;
	bool IsFrequency_every_numPrimaryKey;
	bool IsFrequency_every_HMPrimaryKey;
	bool IsFrequency_Start_timePrimaryKey;
	bool IsFrequency_End_timePrimaryKey;
	bool IsDuration_Start_datePrimaryKey;
	bool IsDuration_End_datePrimaryKey;
	bool IsDuration_YesNo_ENDPrimaryKey;
	bool IsPriorityPrimaryKey;
	bool IsInsertTimePrimaryKey;
	bool IsModTimePrimaryKey;

};


class ScheduleTab : public konga::DbRecordSet {
public:
	ScheduleTab() {}

	static ScheduleTab* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "ScheduleTab";
	}

	void DoFieldExchange(const ScheduleTabRow& row) {
		m_row.Copy(row);
	}

	void Edit();

	std::string GetUpdateFilter() {
		return m_stUpdateFilter;
	}

	int GetSchedule_ID() {
		if( m_row.IsSchedule_IDNull )
			throw konga::DbNullException("Schedule_ID");
		return m_row.field_Schedule_ID;
	}
	void SetSchedule_ID(int i) {
		m_row.field_Schedule_ID = i;
		m_row.IsSchedule_IDNull = false;
		m_row.IsSchedule_IDDirty = true;
	}
	bool IsSchedule_IDNull() const {
		return m_row.IsSchedule_IDNull;
	}
	void SetSchedule_IDToNull() {
		m_row.IsSchedule_IDNull = true;
		m_row.IsSchedule_IDDirty = true;
	}

	std::string GetSchedule_Name() {
		if( m_row.IsSchedule_NameNull )
			throw konga::DbNullException("Schedule_Name");
		return m_row.field_Schedule_Name;
	}
	void SetSchedule_Name(const std::string& st) {
		m_row.field_Schedule_Name = st;
		m_row.IsSchedule_NameNull = false;
		m_row.IsSchedule_NameDirty = true;
	}
	bool IsSchedule_NameNull() const {
		return m_row.IsSchedule_NameNull;
	}
	void SetSchedule_NameToNull() {
		m_row.field_Schedule_Name = "";
		m_row.IsSchedule_NameNull = true;
		m_row.IsSchedule_NameDirty = true;
	}

	int GetOccurs() {
		if( m_row.IsOccursNull )
			throw konga::DbNullException("Occurs");
		return m_row.field_Occurs;
	}
	void SetOccurs(int i) {
		m_row.field_Occurs = i;
		m_row.IsOccursNull = false;
		m_row.IsOccursDirty = true;
	}
	bool IsOccursNull() const {
		return m_row.IsOccursNull;
	}
	void SetOccursToNull() {
		m_row.IsOccursNull = true;
		m_row.IsOccursDirty = true;
	}

	int GetDaily() {
		if( m_row.IsDailyNull )
			throw konga::DbNullException("Daily");
		return m_row.field_Daily;
	}
	void SetDaily(int i) {
		m_row.field_Daily = i;
		m_row.IsDailyNull = false;
		m_row.IsDailyDirty = true;
	}
	bool IsDailyNull() const {
		return m_row.IsDailyNull;
	}
	void SetDailyToNull() {
		m_row.IsDailyNull = true;
		m_row.IsDailyDirty = true;
	}

	int GetWeekly() {
		if( m_row.IsWeeklyNull )
			throw konga::DbNullException("Weekly");
		return m_row.field_Weekly;
	}
	void SetWeekly(int i) {
		m_row.field_Weekly = i;
		m_row.IsWeeklyNull = false;
		m_row.IsWeeklyDirty = true;
	}
	bool IsWeeklyNull() const {
		return m_row.IsWeeklyNull;
	}
	void SetWeeklyToNull() {
		m_row.IsWeeklyNull = true;
		m_row.IsWeeklyDirty = true;
	}

	int GetMon() {
		if( m_row.IsMonNull )
			throw konga::DbNullException("Mon");
		return m_row.field_Mon;
	}
	void SetMon(int i) {
		m_row.field_Mon = i;
		m_row.IsMonNull = false;
		m_row.IsMonDirty = true;
	}
	bool IsMonNull() const {
		return m_row.IsMonNull;
	}
	void SetMonToNull() {
		m_row.IsMonNull = true;
		m_row.IsMonDirty = true;
	}

	int GetTue() {
		if( m_row.IsTueNull )
			throw konga::DbNullException("Tue");
		return m_row.field_Tue;
	}
	void SetTue(int i) {
		m_row.field_Tue = i;
		m_row.IsTueNull = false;
		m_row.IsTueDirty = true;
	}
	bool IsTueNull() const {
		return m_row.IsTueNull;
	}
	void SetTueToNull() {
		m_row.IsTueNull = true;
		m_row.IsTueDirty = true;
	}

	int GetWed() {
		if( m_row.IsWedNull )
			throw konga::DbNullException("Wed");
		return m_row.field_Wed;
	}
	void SetWed(int i) {
		m_row.field_Wed = i;
		m_row.IsWedNull = false;
		m_row.IsWedDirty = true;
	}
	bool IsWedNull() const {
		return m_row.IsWedNull;
	}
	void SetWedToNull() {
		m_row.IsWedNull = true;
		m_row.IsWedDirty = true;
	}

	int GetThu() {
		if( m_row.IsThuNull )
			throw konga::DbNullException("Thu");
		return m_row.field_Thu;
	}
	void SetThu(int i) {
		m_row.field_Thu = i;
		m_row.IsThuNull = false;
		m_row.IsThuDirty = true;
	}
	bool IsThuNull() const {
		return m_row.IsThuNull;
	}
	void SetThuToNull() {
		m_row.IsThuNull = true;
		m_row.IsThuDirty = true;
	}

	int GetFri() {
		if( m_row.IsFriNull )
			throw konga::DbNullException("Fri");
		return m_row.field_Fri;
	}
	void SetFri(int i) {
		m_row.field_Fri = i;
		m_row.IsFriNull = false;
		m_row.IsFriDirty = true;
	}
	bool IsFriNull() const {
		return m_row.IsFriNull;
	}
	void SetFriToNull() {
		m_row.IsFriNull = true;
		m_row.IsFriDirty = true;
	}

	int GetSat() {
		if( m_row.IsSatNull )
			throw konga::DbNullException("Sat");
		return m_row.field_Sat;
	}
	void SetSat(int i) {
		m_row.field_Sat = i;
		m_row.IsSatNull = false;
		m_row.IsSatDirty = true;
	}
	bool IsSatNull() const {
		return m_row.IsSatNull;
	}
	void SetSatToNull() {
		m_row.IsSatNull = true;
		m_row.IsSatDirty = true;
	}

	int GetSun() {
		if( m_row.IsSunNull )
			throw konga::DbNullException("Sun");
		return m_row.field_Sun;
	}
	void SetSun(int i) {
		m_row.field_Sun = i;
		m_row.IsSunNull = false;
		m_row.IsSunDirty = true;
	}
	bool IsSunNull() const {
		return m_row.IsSunNull;
	}
	void SetSunToNull() {
		m_row.IsSunNull = true;
		m_row.IsSunDirty = true;
	}

	int GetMonthly() {
		if( m_row.IsMonthlyNull )
			throw konga::DbNullException("Monthly");
		return m_row.field_Monthly;
	}
	void SetMonthly(int i) {
		m_row.field_Monthly = i;
		m_row.IsMonthlyNull = false;
		m_row.IsMonthlyDirty = true;
	}
	bool IsMonthlyNull() const {
		return m_row.IsMonthlyNull;
	}
	void SetMonthlyToNull() {
		m_row.IsMonthlyNull = true;
		m_row.IsMonthlyDirty = true;
	}

	int GetMonthly_A_Day() {
		if( m_row.IsMonthly_A_DayNull )
			throw konga::DbNullException("Monthly_A_Day");
		return m_row.field_Monthly_A_Day;
	}
	void SetMonthly_A_Day(int i) {
		m_row.field_Monthly_A_Day = i;
		m_row.IsMonthly_A_DayNull = false;
		m_row.IsMonthly_A_DayDirty = true;
	}
	bool IsMonthly_A_DayNull() const {
		return m_row.IsMonthly_A_DayNull;
	}
	void SetMonthly_A_DayToNull() {
		m_row.IsMonthly_A_DayNull = true;
		m_row.IsMonthly_A_DayDirty = true;
	}

	int GetMonthly_A_of_every_X_month() {
		if( m_row.IsMonthly_A_of_every_X_monthNull )
			throw konga::DbNullException("Monthly_A_of_every_X_month");
		return m_row.field_Monthly_A_of_every_X_month;
	}
	void SetMonthly_A_of_every_X_month(int i) {
		m_row.field_Monthly_A_of_every_X_month = i;
		m_row.IsMonthly_A_of_every_X_monthNull = false;
		m_row.IsMonthly_A_of_every_X_monthDirty = true;
	}
	bool IsMonthly_A_of_every_X_monthNull() const {
		return m_row.IsMonthly_A_of_every_X_monthNull;
	}
	void SetMonthly_A_of_every_X_monthToNull() {
		m_row.IsMonthly_A_of_every_X_monthNull = true;
		m_row.IsMonthly_A_of_every_X_monthDirty = true;
	}

	int GetMonthly_B_Num() {
		if( m_row.IsMonthly_B_NumNull )
			throw konga::DbNullException("Monthly_B_Num");
		return m_row.field_Monthly_B_Num;
	}
	void SetMonthly_B_Num(int i) {
		m_row.field_Monthly_B_Num = i;
		m_row.IsMonthly_B_NumNull = false;
		m_row.IsMonthly_B_NumDirty = true;
	}
	bool IsMonthly_B_NumNull() const {
		return m_row.IsMonthly_B_NumNull;
	}
	void SetMonthly_B_NumToNull() {
		m_row.IsMonthly_B_NumNull = true;
		m_row.IsMonthly_B_NumDirty = true;
	}

	int GetMonthly_B_Day() {
		if( m_row.IsMonthly_B_DayNull )
			throw konga::DbNullException("Monthly_B_Day");
		return m_row.field_Monthly_B_Day;
	}
	void SetMonthly_B_Day(int i) {
		m_row.field_Monthly_B_Day = i;
		m_row.IsMonthly_B_DayNull = false;
		m_row.IsMonthly_B_DayDirty = true;
	}
	bool IsMonthly_B_DayNull() const {
		return m_row.IsMonthly_B_DayNull;
	}
	void SetMonthly_B_DayToNull() {
		m_row.IsMonthly_B_DayNull = true;
		m_row.IsMonthly_B_DayDirty = true;
	}

	int GetMonthly_B_of_every_x_month() {
		if( m_row.IsMonthly_B_of_every_x_monthNull )
			throw konga::DbNullException("Monthly_B_of_every_x_month");
		return m_row.field_Monthly_B_of_every_x_month;
	}
	void SetMonthly_B_of_every_x_month(int i) {
		m_row.field_Monthly_B_of_every_x_month = i;
		m_row.IsMonthly_B_of_every_x_monthNull = false;
		m_row.IsMonthly_B_of_every_x_monthDirty = true;
	}
	bool IsMonthly_B_of_every_x_monthNull() const {
		return m_row.IsMonthly_B_of_every_x_monthNull;
	}
	void SetMonthly_B_of_every_x_monthToNull() {
		m_row.IsMonthly_B_of_every_x_monthNull = true;
		m_row.IsMonthly_B_of_every_x_monthDirty = true;
	}

	int GetFrequency() {
		if( m_row.IsFrequencyNull )
			throw konga::DbNullException("Frequency");
		return m_row.field_Frequency;
	}
	void SetFrequency(int i) {
		m_row.field_Frequency = i;
		m_row.IsFrequencyNull = false;
		m_row.IsFrequencyDirty = true;
	}
	bool IsFrequencyNull() const {
		return m_row.IsFrequencyNull;
	}
	void SetFrequencyToNull() {
		m_row.IsFrequencyNull = true;
		m_row.IsFrequencyDirty = true;
	}

	std::string GetFrequency_once_time() {
		if( m_row.IsFrequency_once_timeNull )
			throw konga::DbNullException("Frequency_once_time");
		return m_row.field_Frequency_once_time;
	}
	void SetFrequency_once_time(const std::string& ts) {
		m_row.field_Frequency_once_time = ts;
		m_row.IsFrequency_once_timeNull = false;
		m_row.IsFrequency_once_timeDirty = true;
	}
	bool IsFrequency_once_timeNull() const {
		return m_row.IsFrequency_once_timeNull;
	}
	void SetFrequency_once_timeToNull() {
		m_row.IsFrequency_once_timeNull = true;
		m_row.IsFrequency_once_timeDirty = true;
	}

	int GetFrequency_every_num() {
		if( m_row.IsFrequency_every_numNull )
			throw konga::DbNullException("Frequency_every_num");
		return m_row.field_Frequency_every_num;
	}
	void SetFrequency_every_num(int i) {
		m_row.field_Frequency_every_num = i;
		m_row.IsFrequency_every_numNull = false;
		m_row.IsFrequency_every_numDirty = true;
	}
	bool IsFrequency_every_numNull() const {
		return m_row.IsFrequency_every_numNull;
	}
	void SetFrequency_every_numToNull() {
		m_row.IsFrequency_every_numNull = true;
		m_row.IsFrequency_every_numDirty = true;
	}

	int GetFrequency_every_HM() {
		if( m_row.IsFrequency_every_HMNull )
			throw konga::DbNullException("Frequency_every_HM");
		return m_row.field_Frequency_every_HM;
	}
	void SetFrequency_every_HM(int i) {
		m_row.field_Frequency_every_HM = i;
		m_row.IsFrequency_every_HMNull = false;
		m_row.IsFrequency_every_HMDirty = true;
	}
	bool IsFrequency_every_HMNull() const {
		return m_row.IsFrequency_every_HMNull;
	}
	void SetFrequency_every_HMToNull() {
		m_row.IsFrequency_every_HMNull = true;
		m_row.IsFrequency_every_HMDirty = true;
	}

	std::string GetFrequency_Start_time() {
		if( m_row.IsFrequency_Start_timeNull )
			throw konga::DbNullException("Frequency_Start_time");
		return m_row.field_Frequency_Start_time;
	}
	void SetFrequency_Start_time(const std::string& ts) {
		m_row.field_Frequency_Start_time = ts;
		m_row.IsFrequency_Start_timeNull = false;
		m_row.IsFrequency_Start_timeDirty = true;
	}
	bool IsFrequency_Start_timeNull() const {
		return m_row.IsFrequency_Start_timeNull;
	}
	void SetFrequency_Start_timeToNull() {
		m_row.IsFrequency_Start_timeNull = true;
		m_row.IsFrequency_Start_timeDirty = true;
	}

	std::string GetFrequency_End_time() {
		if( m_row.IsFrequency_End_timeNull )
			throw konga::DbNullException("Frequency_End_time");
		return m_row.field_Frequency_End_time;
	}
	void SetFrequency_End_time(const std::string& ts) {
		m_row.field_Frequency_End_time = ts;
		m_row.IsFrequency_End_timeNull = false;
		m_row.IsFrequency_End_timeDirty = true;
	}
	bool IsFrequency_End_timeNull() const {
		return m_row.IsFrequency_End_timeNull;
	}
	void SetFrequency_End_timeToNull() {
		m_row.IsFrequency_End_timeNull = true;
		m_row.IsFrequency_End_timeDirty = true;
	}

	std::string GetDuration_Start_date() {
		if( m_row.IsDuration_Start_dateNull )
			throw konga::DbNullException("Duration_Start_date");
		return m_row.field_Duration_Start_date;
	}
	void SetDuration_Start_date(const std::string& ts) {
		m_row.field_Duration_Start_date = ts;
		m_row.IsDuration_Start_dateNull = false;
		m_row.IsDuration_Start_dateDirty = true;
	}
	bool IsDuration_Start_dateNull() const {
		return m_row.IsDuration_Start_dateNull;
	}
	void SetDuration_Start_dateToNull() {
		m_row.IsDuration_Start_dateNull = true;
		m_row.IsDuration_Start_dateDirty = true;
	}

	std::string GetDuration_End_date() {
		if( m_row.IsDuration_End_dateNull )
			throw konga::DbNullException("Duration_End_date");
		return m_row.field_Duration_End_date;
	}
	void SetDuration_End_date(const std::string& ts) {
		m_row.field_Duration_End_date = ts;
		m_row.IsDuration_End_dateNull = false;
		m_row.IsDuration_End_dateDirty = true;
	}
	bool IsDuration_End_dateNull() const {
		return m_row.IsDuration_End_dateNull;
	}
	void SetDuration_End_dateToNull() {
		m_row.IsDuration_End_dateNull = true;
		m_row.IsDuration_End_dateDirty = true;
	}

	int GetDuration_YesNo_END() {
		if( m_row.IsDuration_YesNo_ENDNull )
			throw konga::DbNullException("Duration_YesNo_END");
		return m_row.field_Duration_YesNo_END;
	}
	void SetDuration_YesNo_END(int i) {
		m_row.field_Duration_YesNo_END = i;
		m_row.IsDuration_YesNo_ENDNull = false;
		m_row.IsDuration_YesNo_ENDDirty = true;
	}
	bool IsDuration_YesNo_ENDNull() const {
		return m_row.IsDuration_YesNo_ENDNull;
	}
	void SetDuration_YesNo_ENDToNull() {
		m_row.IsDuration_YesNo_ENDNull = true;
		m_row.IsDuration_YesNo_ENDDirty = true;
	}

	int GetPriority() {
		if( m_row.IsPriorityNull )
			throw konga::DbNullException("Priority");
		return m_row.field_Priority;
	}
	void SetPriority(int i) {
		m_row.field_Priority = i;
		m_row.IsPriorityNull = false;
		m_row.IsPriorityDirty = true;
	}
	bool IsPriorityNull() const {
		return m_row.IsPriorityNull;
	}
	void SetPriorityToNull() {
		m_row.IsPriorityNull = true;
		m_row.IsPriorityDirty = true;
	}

	TIMESTAMP_STRUCT GetInsertTime() {
		if( m_row.IsInsertTimeNull )
			throw konga::DbNullException("InsertTime");
		return m_row.field_InsertTime;
	}
	void SetInsertTime(const TIMESTAMP_STRUCT& ts) {
		m_row.field_InsertTime = ts;
		m_row.IsInsertTimeNull = false;
		m_row.IsInsertTimeDirty = true;
	}
	bool IsInsertTimeNull() const {
		return m_row.IsInsertTimeNull;
	}
	void SetInsertTimeToNull() {
		m_row.IsInsertTimeNull = true;
		m_row.IsInsertTimeDirty = true;
	}

	TIMESTAMP_STRUCT GetModTime() {
		if( m_row.IsModTimeNull )
			throw konga::DbNullException("ModTime");
		return m_row.field_ModTime;
	}
	void SetModTime(const TIMESTAMP_STRUCT& ts) {
		m_row.field_ModTime = ts;
		m_row.IsModTimeNull = false;
		m_row.IsModTimeDirty = true;
	}
	bool IsModTimeNull() const {
		return m_row.IsModTimeNull;
	}
	void SetModTimeToNull() {
		m_row.IsModTimeNull = true;
		m_row.IsModTimeDirty = true;
	}

protected:
	ScheduleTabRow m_row;

private:
	bool IsSchedule_IDDirty() const {
		return m_row.IsSchedule_IDDirty;
	};
	bool IsSchedule_NameDirty() const {
		return m_row.IsSchedule_NameDirty;
	};
	bool IsOccursDirty() const {
		return m_row.IsOccursDirty;
	};
	bool IsDailyDirty() const {
		return m_row.IsDailyDirty;
	};
	bool IsWeeklyDirty() const {
		return m_row.IsWeeklyDirty;
	};
	bool IsMonDirty() const {
		return m_row.IsMonDirty;
	};
	bool IsTueDirty() const {
		return m_row.IsTueDirty;
	};
	bool IsWedDirty() const {
		return m_row.IsWedDirty;
	};
	bool IsThuDirty() const {
		return m_row.IsThuDirty;
	};
	bool IsFriDirty() const {
		return m_row.IsFriDirty;
	};
	bool IsSatDirty() const {
		return m_row.IsSatDirty;
	};
	bool IsSunDirty() const {
		return m_row.IsSunDirty;
	};
	bool IsMonthlyDirty() const {
		return m_row.IsMonthlyDirty;
	};
	bool IsMonthly_A_DayDirty() const {
		return m_row.IsMonthly_A_DayDirty;
	};
	bool IsMonthly_A_of_every_X_monthDirty() const {
		return m_row.IsMonthly_A_of_every_X_monthDirty;
	};
	bool IsMonthly_B_NumDirty() const {
		return m_row.IsMonthly_B_NumDirty;
	};
	bool IsMonthly_B_DayDirty() const {
		return m_row.IsMonthly_B_DayDirty;
	};
	bool IsMonthly_B_of_every_x_monthDirty() const {
		return m_row.IsMonthly_B_of_every_x_monthDirty;
	};
	bool IsFrequencyDirty() const {
		return m_row.IsFrequencyDirty;
	};
	bool IsFrequency_once_timeDirty() const {
		return m_row.IsFrequency_once_timeDirty;
	};
	bool IsFrequency_every_numDirty() const {
		return m_row.IsFrequency_every_numDirty;
	};
	bool IsFrequency_every_HMDirty() const {
		return m_row.IsFrequency_every_HMDirty;
	};
	bool IsFrequency_Start_timeDirty() const {
		return m_row.IsFrequency_Start_timeDirty;
	};
	bool IsFrequency_End_timeDirty() const {
		return m_row.IsFrequency_End_timeDirty;
	};
	bool IsDuration_Start_dateDirty() const {
		return m_row.IsDuration_Start_dateDirty;
	};
	bool IsDuration_End_dateDirty() const {
		return m_row.IsDuration_End_dateDirty;
	};
	bool IsDuration_YesNo_ENDDirty() const {
		return m_row.IsDuration_YesNo_ENDDirty;
	};
	bool IsPriorityDirty() const {
		return m_row.IsPriorityDirty;
	};
	bool IsInsertTimeDirty() const {
		return m_row.IsInsertTimeDirty;
	};
	bool IsModTimeDirty() const {
		return m_row.IsModTimeDirty;
	};


	std::string m_stUpdateFilter;

};

}                         


#endif
