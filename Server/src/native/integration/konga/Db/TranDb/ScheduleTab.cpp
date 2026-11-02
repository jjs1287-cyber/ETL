/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <assert.h>
#include <dtl/DTL.h>

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DtlDbUtils/DtlDbConnection.h>
#include <konga/Db/DtlTranDb/DtlScheduleTab.h>

#include "ScheduleTab.h"

using namespace konga;
using namespace TranDb;

ScheduleTab* ScheduleTab::MakeInstance(DbConnection* pConn)
{
	DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
	assert(pDtlConn != 0);
	return new dtl_trandb::DtlScheduleTab(pDtlConn->GetDtlConnection());
}

void ScheduleTab::Edit()
{
	m_stUpdateFilter = "WHERE (";
	m_stUpdateFilter += "Schedule_ID=";
	m_stUpdateFilter += toString(GetSchedule_ID());
	m_stUpdateFilter += ")";
	m_row.SetDirty(false);
}

