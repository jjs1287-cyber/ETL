/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <assert.h>
#include <dtl/DTL.h>

#include <konga/Utils/StringUtils.h>
#include <konga/Utils/DbTypeUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DtlDbUtils/DtlDbConnection.h>
#include <konga/Db/DtlTranDb/DtlScheduledOperationView.h>

#include "ScheduledOperationView.h"

using namespace konga;
using namespace TranDb;

ScheduledOperationView* ScheduledOperationView::MakeInstance(DbConnection* pConn)
{
	DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
	assert(pDtlConn != 0);
	return new dtl_trandb::DtlScheduledOperationView(pDtlConn->GetDtlConnection());
}

void ScheduledOperationView::Edit()
{
	m_stUpdateFilter = "WHERE (";
	if( IsOperation_IDNull() )
	{
		m_stUpdateFilter += "Operation_ID IS NULL";
	}
	else
	{
		m_stUpdateFilter += "Operation_ID=";
		m_stUpdateFilter += toString(GetOperation_ID());
	}
	if( IsSchedule_IDNull() )
	{
		m_stUpdateFilter += " AND Schedule_ID IS NULL";
	}
	else
	{
		m_stUpdateFilter += " AND Schedule_ID=";
		m_stUpdateFilter += toString(GetSchedule_ID());
	}
	if( IsSchedule_NameNull() )
	{
		m_stUpdateFilter += " AND Schedule_Name IS NULL";
	}
	else
	{
		m_stUpdateFilter += " AND Schedule_Name=";
		m_stUpdateFilter += "'" + SqlEscape(GetSchedule_Name()) + "'";
	}
	if( IsOccursNull() )
	{
		m_stUpdateFilter += " AND Occurs IS NULL";
	}
	else
	{
		m_stUpdateFilter += " AND Occurs=";
		m_stUpdateFilter += toString(GetOccurs());
	}
	if( IsDailyNull() )
	{
		m_stUpdateFilter += " AND Daily IS NULL";
	}
	else
	{
		m_stUpdateFilter += " AND Daily=";
		m_stUpdateFilter += toString(GetDaily());
	}
	if( IsWeeklyNull() )
	{
		m_stUpdateFilter += " AND Weekly IS NULL";
	}
	else
	{
		m_stUpdateFilter += " AND Weekly=";
		m_stUpdateFilter += toString(GetWeekly());
	}
	if( IsMonNull() )
	{
		m_stUpdateFilter += " AND Mon IS NULL";
	}
	else
	{
		m_stUpdateFilter += " AND Mon=";
		m_stUpdateFilter += toString(GetMon());
	}
	if( IsTueNull() )
	{
		m_stUpdateFilter += " AND Tue IS NULL";
	}
	else
	{
		m_stUpdateFilter += " AND Tue=";
		m_stUpdateFilter += toString(GetTue());
	}
	if( IsWedNull() )
	{
		m_stUpdateFilter += " AND Wed IS NULL";
	}
	else
	{
		m_stUpdateFilter += " AND Wed=";
		m_stUpdateFilter += toString(GetWed());
	}
	if( IsThuNull() )
	{
		m_stUpdateFilter += " AND Thu IS NULL";
	}
	else
	{
		m_stUpdateFilter += " AND Thu=";
		m_stUpdateFilter += toString(GetThu());
	}
	if( IsFriNull() )
	{
		m_stUpdateFilter += " AND Fri IS NULL";
	}
	else
	{
		m_stUpdateFilter += " AND Fri=";
		m_stUpdateFilter += toString(GetFri());
	}
	if( IsSatNull() )
	{
		m_stUpdateFilter += " AND Sat IS NULL";
	}
	else
	{
		m_stUpdateFilter += " AND Sat=";
		m_stUpdateFilter += toString(GetSat());
	}
	if( IsSunNull() )
	{
		m_stUpdateFilter += " AND Sun IS NULL";
	}
	else
	{
		m_stUpdateFilter += " AND Sun=";
		m_stUpdateFilter += toString(GetSun());
	}
	if( IsMonthlyNull() )
	{
		m_stUpdateFilter += " AND Monthly IS NULL";
	}
	else
	{
		m_stUpdateFilter += " AND Monthly=";
		m_stUpdateFilter += toString(GetMonthly());
	}
	if( IsMonthly_A_DayNull() )
	{
		m_stUpdateFilter += " AND Monthly_A_Day IS NULL";
	}
	else
	{
		m_stUpdateFilter += " AND Monthly_A_Day=";
		m_stUpdateFilter += toString(GetMonthly_A_Day());
	}
	if( IsMonthly_A_of_every_X_monthNull() )
	{
		m_stUpdateFilter += " AND Monthly_A_of_every_X_month IS NULL";
	}
	else
	{
		m_stUpdateFilter += " AND Monthly_A_of_every_X_month=";
		m_stUpdateFilter += toString(GetMonthly_A_of_every_X_month());
	}
	if( IsMonthly_B_NumNull() )
	{
		m_stUpdateFilter += " AND Monthly_B_Num IS NULL";
	}
	else
	{
		m_stUpdateFilter += " AND Monthly_B_Num=";
		m_stUpdateFilter += toString(GetMonthly_B_Num());
	}
	if( IsMonthly_B_DayNull() )
	{
		m_stUpdateFilter += " AND Monthly_B_Day IS NULL";
	}
	else
	{
		m_stUpdateFilter += " AND Monthly_B_Day=";
		m_stUpdateFilter += toString(GetMonthly_B_Day());
	}
	if( IsMonthly_B_of_every_x_monthNull() )
	{
		m_stUpdateFilter += " AND Monthly_B_of_every_x_month IS NULL";
	}
	else
	{
		m_stUpdateFilter += " AND Monthly_B_of_every_x_month=";
		m_stUpdateFilter += toString(GetMonthly_B_of_every_x_month());
	}
	if( IsFrequencyNull() )
	{
		m_stUpdateFilter += " AND Frequency IS NULL";
	}
	else
	{
		m_stUpdateFilter += " AND Frequency=";
		m_stUpdateFilter += toString(GetFrequency());
	}
	if( IsFrequency_once_timeNull() )
	{
		m_stUpdateFilter += " AND Frequency_once_time IS NULL";
	}
	else
	{
		m_stUpdateFilter += " AND Frequency_once_time=";
		m_stUpdateFilter += "'" + SqlEscape(GetFrequency_once_time()) + "'";
	}
	if( IsFrequency_every_numNull() )
	{
		m_stUpdateFilter += " AND Frequency_every_num IS NULL";
	}
	else
	{
		m_stUpdateFilter += " AND Frequency_every_num=";
		m_stUpdateFilter += toString(GetFrequency_every_num());
	}
	if( IsFrequency_every_HMNull() )
	{
		m_stUpdateFilter += " AND Frequency_every_HM IS NULL";
	}
	else
	{
		m_stUpdateFilter += " AND Frequency_every_HM=";
		m_stUpdateFilter += toString(GetFrequency_every_HM());
	}
	if( IsFrequency_Start_timeNull() )
	{
		m_stUpdateFilter += " AND Frequency_Start_time IS NULL";
	}
	else
	{
		m_stUpdateFilter += " AND Frequency_Start_time=";
		m_stUpdateFilter += "'" + SqlEscape(GetFrequency_Start_time()) + "'";
	}
	if( IsFrequency_End_timeNull() )
	{
		m_stUpdateFilter += " AND Frequency_End_time IS NULL";
	}
	else
	{
		m_stUpdateFilter += " AND Frequency_End_time=";
		m_stUpdateFilter += "'" + SqlEscape(GetFrequency_End_time()) + "'";
	}
	if( IsDuration_Start_dateNull() )
	{
		m_stUpdateFilter += " AND Duration_Start_date IS NULL";
	}
	else
	{
		m_stUpdateFilter += " AND Duration_Start_date=";
		m_stUpdateFilter += "'" + SqlEscape(GetDuration_Start_date()) + "'";
	}
	if( IsDuration_End_dateNull() )
	{
		m_stUpdateFilter += " AND Duration_End_date IS NULL";
	}
	else
	{
		m_stUpdateFilter += " AND Duration_End_date=";
		m_stUpdateFilter += "'" + SqlEscape(GetDuration_End_date()) + "'";
	}
	if( IsDuration_YesNo_ENDNull() )
	{
		m_stUpdateFilter += " AND Duration_YesNo_END IS NULL";
	}
	else
	{
		m_stUpdateFilter += " AND Duration_YesNo_END=";
		m_stUpdateFilter += toString(GetDuration_YesNo_END());
	}
	if( IsPriorityNull() )
	{
		m_stUpdateFilter += " AND Priority IS NULL";
	}
	else
	{
		m_stUpdateFilter += " AND Priority=";
		m_stUpdateFilter += toString(GetPriority());
	}
	if( IsInsertTimeNull() )
	{
		m_stUpdateFilter += " AND InsertTime IS NULL";
	}
	else
	{
		m_stUpdateFilter += " AND InsertTime=";
		m_stUpdateFilter += "'" + GetISO8601TimeStampString(GetInsertTime()) + "'";
	}
	if( IsModTimeNull() )
	{
		m_stUpdateFilter += " AND ModTime IS NULL";
	}
	else
	{
		m_stUpdateFilter += " AND ModTime=";
		m_stUpdateFilter += "'" + GetISO8601TimeStampString(GetModTime()) + "'";
	}
	m_stUpdateFilter += ")";
	m_row.SetDirty(false);
}

