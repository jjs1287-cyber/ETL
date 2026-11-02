/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <assert.h>
#include <dtl/DTL.h>

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DtlDbUtils/DtlDbConnection.h>
#include <konga/Db/DtlIDb/DtlActivityTab.h>

#include "ActivityTab.h"

using namespace konga;
using namespace IDb;

ActivityTab* ActivityTab::MakeInstance(DbConnection* pConn)
{
	DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
	assert(pDtlConn != 0);
	return new dtl_idb::DtlActivityTab(pDtlConn->GetDtlConnection());
}

void ActivityTab::Edit()
{
	m_stUpdateFilter = "WHERE (";
	m_stUpdateFilter += "ProcessId=";
	m_stUpdateFilter += toString(GetProcessId());
	m_stUpdateFilter += " AND TaskId=";
	m_stUpdateFilter += toString(GetTaskId());
	m_stUpdateFilter += " AND ActivityId=";
	m_stUpdateFilter += toString(GetActivityId());
	m_stUpdateFilter += " AND ProcessInstanceId=";
	m_stUpdateFilter += toString(GetProcessInstanceId());
	m_stUpdateFilter += " AND TaskInstanceId=";
	m_stUpdateFilter += toString(GetTaskInstanceId());
	m_stUpdateFilter += " AND ActivityInstanceId=";
	m_stUpdateFilter += toString(GetActivityInstanceId());
	m_stUpdateFilter += ")";
	m_row.SetDirty(false);
}

