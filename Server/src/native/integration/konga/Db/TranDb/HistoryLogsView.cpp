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
#include <konga/Db/DtlTranDb/DtlHistoryLogsView.h>

#include "HistoryLogsView.h"

using namespace konga;
using namespace TranDb;

HistoryLogsView* HistoryLogsView::MakeInstance(DbConnection* pConn)
{
	DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
	assert(pDtlConn != 0);
	return new dtl_trandb::DtlHistoryLogsView(pDtlConn->GetDtlConnection());
}

void HistoryLogsView::Edit()
{
	m_stUpdateFilter = "WHERE (";
	if( Isinterchangeproject_idNull() )
	{
		m_stUpdateFilter += "interchangeproject_id IS NULL";
	}
	else
	{
		m_stUpdateFilter += "interchangeproject_id=";
		m_stUpdateFilter += toString(Getinterchangeproject_id());
	}
	if( IsOperation_IDNull() )
	{
		m_stUpdateFilter += " AND Operation_ID IS NULL";
	}
	else
	{
		m_stUpdateFilter += " AND Operation_ID=";
		m_stUpdateFilter += toString(GetOperation_ID());
	}
	if( IsOperation_NameNull() )
	{
		m_stUpdateFilter += " AND Operation_Name IS NULL";
	}
	else
	{
		m_stUpdateFilter += " AND Operation_Name=";
		m_stUpdateFilter += "'" + SqlEscape(GetOperation_Name()) + "'";
	}
	if( IsIDNull() )
	{
		m_stUpdateFilter += " AND ID IS NULL";
	}
	else
	{
		m_stUpdateFilter += " AND ID=";
		m_stUpdateFilter += toString(GetID());
	}
	if( IsStart_DateNull() )
	{
		m_stUpdateFilter += " AND Start_Date IS NULL";
	}
	else
	{
		m_stUpdateFilter += " AND Start_Date=";
		m_stUpdateFilter += "'" + GetISO8601TimeStampString(GetStart_Date()) + "'";
	}
	if( IsEnd_DateNull() )
	{
		m_stUpdateFilter += " AND End_Date IS NULL";
	}
	else
	{
		m_stUpdateFilter += " AND End_Date=";
		m_stUpdateFilter += "'" + GetISO8601TimeStampString(GetEnd_Date()) + "'";
	}
	if( IsTotal_FilesNull() )
	{
		m_stUpdateFilter += " AND Total_Files IS NULL";
	}
	else
	{
		m_stUpdateFilter += " AND Total_Files=";
		m_stUpdateFilter += toString(GetTotal_Files());
	}
	if( IsTotal_SizeNull() )
	{
		m_stUpdateFilter += " AND Total_Size IS NULL";
	}
	else
	{
		m_stUpdateFilter += " AND Total_Size=";
		m_stUpdateFilter += toString(GetTotal_Size());
	}
	m_stUpdateFilter += ")";
	m_row.SetDirty(false);
}

