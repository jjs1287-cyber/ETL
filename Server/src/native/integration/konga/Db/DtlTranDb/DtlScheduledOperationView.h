/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_TRANDB_TEMPLATE_SCHEDULEDOPERATIONVIEW_H
#define KONGA_DTL_TRANDB_TEMPLATE_SCHEDULEDOPERATIONVIEW_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/TranDb/ScheduledOperationView.h>

namespace dtl {

template<> class DefaultSelValidate<TranDb::ScheduledOperationViewRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::ScheduledOperationViewRow &r)
	{
		r.IsOperation_IDNull = cols["Operation_ID"].IsNull();
		r.IsSchedule_IDNull = cols["Schedule_ID"].IsNull();
		r.IsSchedule_NameNull = cols["Schedule_Name"].IsNull();
		r.IsOccursNull = cols["Occurs"].IsNull();
		r.IsDailyNull = cols["Daily"].IsNull();
		r.IsWeeklyNull = cols["Weekly"].IsNull();
		r.IsMonNull = cols["Mon"].IsNull();
		r.IsTueNull = cols["Tue"].IsNull();
		r.IsWedNull = cols["Wed"].IsNull();
		r.IsThuNull = cols["Thu"].IsNull();
		r.IsFriNull = cols["Fri"].IsNull();
		r.IsSatNull = cols["Sat"].IsNull();
		r.IsSunNull = cols["Sun"].IsNull();
		r.IsMonthlyNull = cols["Monthly"].IsNull();
		r.IsMonthly_A_DayNull = cols["Monthly_A_Day"].IsNull();
		r.IsMonthly_A_of_every_X_monthNull = cols["Monthly_A_of_every_X_month"].IsNull();
		r.IsMonthly_B_NumNull = cols["Monthly_B_Num"].IsNull();
		r.IsMonthly_B_DayNull = cols["Monthly_B_Day"].IsNull();
		r.IsMonthly_B_of_every_x_monthNull = cols["Monthly_B_of_every_x_month"].IsNull();
		r.IsFrequencyNull = cols["Frequency"].IsNull();
		r.IsFrequency_once_timeNull = cols["Frequency_once_time"].IsNull();
		r.IsFrequency_every_numNull = cols["Frequency_every_num"].IsNull();
		r.IsFrequency_every_HMNull = cols["Frequency_every_HM"].IsNull();
		r.IsFrequency_Start_timeNull = cols["Frequency_Start_time"].IsNull();
		r.IsFrequency_End_timeNull = cols["Frequency_End_time"].IsNull();
		r.IsDuration_Start_dateNull = cols["Duration_Start_date"].IsNull();
		r.IsDuration_End_dateNull = cols["Duration_End_date"].IsNull();
		r.IsDuration_YesNo_ENDNull = cols["Duration_YesNo_END"].IsNull();
		r.IsPriorityNull = cols["Priority"].IsNull();
		r.IsInsertTimeNull = cols["InsertTime"].IsNull();
		r.IsModTimeNull = cols["ModTime"].IsNull();
		  
                                        
                       
    
		return true;
	}
};

template<> class DefaultInsValidate<TranDb::ScheduledOperationViewRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::ScheduledOperationViewRow &r)
	{
		if( r.IsOperation_IDNull )
			cols["Operation_ID"].SetNull();
		else
			cols["Operation_ID"].ClearNull();
		if( r.IsSchedule_IDNull )
			cols["Schedule_ID"].SetNull();
		else
			cols["Schedule_ID"].ClearNull();
		if( r.IsSchedule_NameNull )
			cols["Schedule_Name"].SetNull();
		else
			cols["Schedule_Name"].ClearNull();
		if( r.IsOccursNull )
			cols["Occurs"].SetNull();
		else
			cols["Occurs"].ClearNull();
		if( r.IsDailyNull )
			cols["Daily"].SetNull();
		else
			cols["Daily"].ClearNull();
		if( r.IsWeeklyNull )
			cols["Weekly"].SetNull();
		else
			cols["Weekly"].ClearNull();
		if( r.IsMonNull )
			cols["Mon"].SetNull();
		else
			cols["Mon"].ClearNull();
		if( r.IsTueNull )
			cols["Tue"].SetNull();
		else
			cols["Tue"].ClearNull();
		if( r.IsWedNull )
			cols["Wed"].SetNull();
		else
			cols["Wed"].ClearNull();
		if( r.IsThuNull )
			cols["Thu"].SetNull();
		else
			cols["Thu"].ClearNull();
		if( r.IsFriNull )
			cols["Fri"].SetNull();
		else
			cols["Fri"].ClearNull();
		if( r.IsSatNull )
			cols["Sat"].SetNull();
		else
			cols["Sat"].ClearNull();
		if( r.IsSunNull )
			cols["Sun"].SetNull();
		else
			cols["Sun"].ClearNull();
		if( r.IsMonthlyNull )
			cols["Monthly"].SetNull();
		else
			cols["Monthly"].ClearNull();
		if( r.IsMonthly_A_DayNull )
			cols["Monthly_A_Day"].SetNull();
		else
			cols["Monthly_A_Day"].ClearNull();
		if( r.IsMonthly_A_of_every_X_monthNull )
			cols["Monthly_A_of_every_X_month"].SetNull();
		else
			cols["Monthly_A_of_every_X_month"].ClearNull();
		if( r.IsMonthly_B_NumNull )
			cols["Monthly_B_Num"].SetNull();
		else
			cols["Monthly_B_Num"].ClearNull();
		if( r.IsMonthly_B_DayNull )
			cols["Monthly_B_Day"].SetNull();
		else
			cols["Monthly_B_Day"].ClearNull();
		if( r.IsMonthly_B_of_every_x_monthNull )
			cols["Monthly_B_of_every_x_month"].SetNull();
		else
			cols["Monthly_B_of_every_x_month"].ClearNull();
		if( r.IsFrequencyNull )
			cols["Frequency"].SetNull();
		else
			cols["Frequency"].ClearNull();
		if( r.IsFrequency_once_timeNull )
			cols["Frequency_once_time"].SetNull();
		else
			cols["Frequency_once_time"].ClearNull();
		if( r.IsFrequency_every_numNull )
			cols["Frequency_every_num"].SetNull();
		else
			cols["Frequency_every_num"].ClearNull();
		if( r.IsFrequency_every_HMNull )
			cols["Frequency_every_HM"].SetNull();
		else
			cols["Frequency_every_HM"].ClearNull();
		if( r.IsFrequency_Start_timeNull )
			cols["Frequency_Start_time"].SetNull();
		else
			cols["Frequency_Start_time"].ClearNull();
		if( r.IsFrequency_End_timeNull )
			cols["Frequency_End_time"].SetNull();
		else
			cols["Frequency_End_time"].ClearNull();
		if( r.IsDuration_Start_dateNull )
			cols["Duration_Start_date"].SetNull();
		else
			cols["Duration_Start_date"].ClearNull();
		if( r.IsDuration_End_dateNull )
			cols["Duration_End_date"].SetNull();
		else
			cols["Duration_End_date"].ClearNull();
		if( r.IsDuration_YesNo_ENDNull )
			cols["Duration_YesNo_END"].SetNull();
		else
			cols["Duration_YesNo_END"].ClearNull();
		if( r.IsPriorityNull )
			cols["Priority"].SetNull();
		else
			cols["Priority"].ClearNull();
		if( r.IsInsertTimeNull )
			cols["InsertTime"].SetNull();
		else
			cols["InsertTime"].ClearNull();
		if( r.IsModTimeNull )
			cols["ModTime"].SetNull();
		else
			cols["ModTime"].ClearNull();
		  
                                        
                       
    
		return true;
	}
};

template<> class DefaultBCA<TranDb::ScheduledOperationViewRow>
{
public:
	void operator()(BoundIOs &cols, TranDb::ScheduledOperationViewRow &rowbuf)
	{
		cols["Operation_ID"] == rowbuf.field_Operation_ID;
		cols["Schedule_ID"] == rowbuf.field_Schedule_ID;
		cols["Schedule_Name"] == rowbuf.field_Schedule_Name;
		cols["Occurs"] == rowbuf.field_Occurs;
		cols["Daily"] == rowbuf.field_Daily;
		cols["Weekly"] == rowbuf.field_Weekly;
		cols["Mon"] == rowbuf.field_Mon;
		cols["Tue"] == rowbuf.field_Tue;
		cols["Wed"] == rowbuf.field_Wed;
		cols["Thu"] == rowbuf.field_Thu;
		cols["Fri"] == rowbuf.field_Fri;
		cols["Sat"] == rowbuf.field_Sat;
		cols["Sun"] == rowbuf.field_Sun;
		cols["Monthly"] == rowbuf.field_Monthly;
		cols["Monthly_A_Day"] == rowbuf.field_Monthly_A_Day;
		cols["Monthly_A_of_every_X_month"] == rowbuf.field_Monthly_A_of_every_X_month;
		cols["Monthly_B_Num"] == rowbuf.field_Monthly_B_Num;
		cols["Monthly_B_Day"] == rowbuf.field_Monthly_B_Day;
		cols["Monthly_B_of_every_x_month"] == rowbuf.field_Monthly_B_of_every_x_month;
		cols["Frequency"] == rowbuf.field_Frequency;
		cols["Frequency_once_time"] == rowbuf.field_Frequency_once_time;
		cols["Frequency_every_num"] == rowbuf.field_Frequency_every_num;
		cols["Frequency_every_HM"] == rowbuf.field_Frequency_every_HM;
		cols["Frequency_Start_time"] == rowbuf.field_Frequency_Start_time;
		cols["Frequency_End_time"] == rowbuf.field_Frequency_End_time;
		cols["Duration_Start_date"] == rowbuf.field_Duration_Start_date;
		cols["Duration_End_date"] == rowbuf.field_Duration_End_date;
		cols["Duration_YesNo_END"] == rowbuf.field_Duration_YesNo_END;
		cols["Priority"] == rowbuf.field_Priority;
		cols["InsertTime"] == rowbuf.field_InsertTime;
		cols["ModTime"] == rowbuf.field_ModTime;
		  
                                        
                       
    
	}
};


}                     



namespace dtl_trandb {

class DtlScheduledOperationViewDeleteBinder : public CustomBinder<TranDb::ScheduledOperationViewRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		TranDb::ScheduledOperationViewRow& rowbuf = *m_pRow;
		cols["Operation_ID"] == rowbuf.field_Operation_ID;
		cols["Schedule_ID"] == rowbuf.field_Schedule_ID;
		cols["Schedule_Name"] == rowbuf.field_Schedule_Name;
		cols["Occurs"] == rowbuf.field_Occurs;
		cols["Daily"] == rowbuf.field_Daily;
		cols["Weekly"] == rowbuf.field_Weekly;
		cols["Mon"] == rowbuf.field_Mon;
		cols["Tue"] == rowbuf.field_Tue;
		cols["Wed"] == rowbuf.field_Wed;
		cols["Thu"] == rowbuf.field_Thu;
		cols["Fri"] == rowbuf.field_Fri;
		cols["Sat"] == rowbuf.field_Sat;
		cols["Sun"] == rowbuf.field_Sun;
		cols["Monthly"] == rowbuf.field_Monthly;
		cols["Monthly_A_Day"] == rowbuf.field_Monthly_A_Day;
		cols["Monthly_A_of_every_X_month"] == rowbuf.field_Monthly_A_of_every_X_month;
		cols["Monthly_B_Num"] == rowbuf.field_Monthly_B_Num;
		cols["Monthly_B_Day"] == rowbuf.field_Monthly_B_Day;
		cols["Monthly_B_of_every_x_month"] == rowbuf.field_Monthly_B_of_every_x_month;
		cols["Frequency"] == rowbuf.field_Frequency;
		cols["Frequency_once_time"] == rowbuf.field_Frequency_once_time;
		cols["Frequency_every_num"] == rowbuf.field_Frequency_every_num;
		cols["Frequency_every_HM"] == rowbuf.field_Frequency_every_HM;
		cols["Frequency_Start_time"] == rowbuf.field_Frequency_Start_time;
		cols["Frequency_End_time"] == rowbuf.field_Frequency_End_time;
		cols["Duration_Start_date"] == rowbuf.field_Duration_Start_date;
		cols["Duration_End_date"] == rowbuf.field_Duration_End_date;
		cols["Duration_YesNo_END"] == rowbuf.field_Duration_YesNo_END;
		cols["Priority"] == rowbuf.field_Priority;
		cols["InsertTime"] == rowbuf.field_InsertTime;
		cols["ModTime"] == rowbuf.field_ModTime;
	}
};

class DtlScheduledOperationView : public TranDb::ScheduledOperationView {

public:
	DtlScheduledOperationView(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlScheduledOperationView() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		TranDb::ScheduledOperationView::Edit();
		m_dtlRecSet.Edit();
	}
	void AddNew() {
		m_dtlRecSet.AddNew();
	}
	int Delete() {
		return m_dtlRecSet.Delete();
	}
	bool IsEOF() {
		return m_dtlRecSet.IsEOF();
	}
	bool Next() {
		return m_dtlRecSet.Next();
	}
	void Close() {
		m_dtlRecSet.Close();
	}
	void SetWhereClause(const std::string& stWhereClause) {
		m_dtlRecSet.SetWhereClause(stWhereClause);
	}
	std::string GetWhereClause() const {
		return m_dtlRecSet.GetWhereClause();
	}

	std::string GetLastError() const {
		return m_dtlRecSet.GetLastError();
	}
	long GetLastErrorNumber() const {
		return m_dtlRecSet.GetLastErrorNumber();
	}

private:
	DtlScheduledOperationViewDeleteBinder m_deleteBinder;
	DtlDbRecordSet<TranDb::ScheduledOperationView, TranDb::ScheduledOperationViewRow> m_dtlRecSet;

};


}                            


#endif

