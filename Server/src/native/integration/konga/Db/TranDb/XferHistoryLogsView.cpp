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
#include <konga/Db/DtlTranDb/DtlXferHistoryLogsView.h>

#include "XferHistoryLogsView.h"

using namespace konga;
using namespace TranDb;

XferHistoryLogsView* XferHistoryLogsView::MakeInstance(DbConnection* pConn)
{
	DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
	assert(pDtlConn != 0);
	return new dtl_trandb::DtlXferHistoryLogsView(pDtlConn->GetDtlConnection());
}

void XferHistoryLogsView::Edit()
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
	if( IsFileNameNull() )
	{
		m_stUpdateFilter += " AND FileName IS NULL";
	}
	else
	{
		m_stUpdateFilter += " AND FileName=";
		m_stUpdateFilter += "'" + SqlEscape(GetFileName()) + "'";
	}
	if( IsFileSizeNull() )
	{
		m_stUpdateFilter += " AND FileSize IS NULL";
	}
	else
	{
		m_stUpdateFilter += " AND FileSize=";
		m_stUpdateFilter += toString(GetFileSize());
	}
	if( IsXferDateTimeNull() )
	{
		m_stUpdateFilter += " AND XferDateTime IS NULL";
	}
	else
	{
		m_stUpdateFilter += " AND XferDateTime=";
		m_stUpdateFilter += "'" + GetISO8601TimeStampString(GetXferDateTime()) + "'";
	}
	m_stUpdateFilter += ")";
	m_row.SetDirty(false);
}

