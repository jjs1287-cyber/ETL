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
#include <konga/Db/DtlTranDb/DtlOperationLogsView.h>

#include "OperationLogsView.h"

using namespace konga;
using namespace TranDb;

OperationLogsView* OperationLogsView::MakeInstance(DbConnection* pConn)
{
	DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
	assert(pDtlConn != 0);
	return new dtl_trandb::DtlOperationLogsView(pDtlConn->GetDtlConnection());
}

void OperationLogsView::Edit()
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
	if( IsLog_IDNull() )
	{
		m_stUpdateFilter += " AND Log_ID IS NULL";
	}
	else
	{
		m_stUpdateFilter += " AND Log_ID=";
		m_stUpdateFilter += toString(GetLog_ID());
	}
	if( IsTimestampNull() )
	{
		m_stUpdateFilter += " AND Timestamp IS NULL";
	}
	else
	{
		m_stUpdateFilter += " AND Timestamp=";
		m_stUpdateFilter += "'" + GetISO8601TimeStampString(GetTimestamp()) + "'";
	}
	if( IsShort_MessageNull() )
	{
		m_stUpdateFilter += " AND Short_Message IS NULL";
	}
	else
	{
		m_stUpdateFilter += " AND Short_Message=";
		m_stUpdateFilter += "'" + SqlEscape(GetShort_Message()) + "'";
	}
	if( IsSeverityNull() )
	{
		m_stUpdateFilter += " AND Severity IS NULL";
	}
	else
	{
		m_stUpdateFilter += " AND Severity=";
		m_stUpdateFilter += "'" + SqlEscape(GetSeverity()) + "'";
	}
	m_stUpdateFilter += ")";
	m_row.SetDirty(false);
}

