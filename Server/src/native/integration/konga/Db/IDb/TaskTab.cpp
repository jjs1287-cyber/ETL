/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <assert.h>
#include <dtl/DTL.h>

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DtlDbUtils/DtlDbConnection.h>
#include <konga/Db/DtlIDb/DtlTaskTab.h>

#include "TaskTab.h"

using namespace konga;
using namespace IDb;

TaskTab* TaskTab::MakeInstance(DbConnection* pConn)
{
	DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
	assert(pDtlConn != 0);
	return new dtl_idb::DtlTaskTab(pDtlConn->GetDtlConnection());
}

void TaskTab::Edit()
{
	m_stUpdateFilter = "WHERE (";
	m_stUpdateFilter += "ProcessId=";
	m_stUpdateFilter += toString(GetProcessId());
	m_stUpdateFilter += " AND ProcessInstanceId=";
	m_stUpdateFilter += toString(GetProcessInstanceId());
	m_stUpdateFilter += " AND TaskId=";
	m_stUpdateFilter += toString(GetTaskId());
	m_stUpdateFilter += " AND TaskInstanceId=";
	m_stUpdateFilter += toString(GetTaskInstanceId());
	m_stUpdateFilter += ")";
	m_row.SetDirty(false);
}

