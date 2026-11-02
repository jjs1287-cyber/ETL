/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <assert.h>
#include <dtl/DTL.h>

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DtlDbUtils/DtlDbConnection.h>
#include <konga/Db/DtlPDb/DtlTaskDataElementsTab.h>

#include "TaskDataElementsTab.h"

using namespace konga;
using namespace PDb;

TaskDataElementsTab* TaskDataElementsTab::MakeInstance(DbConnection* pConn)
{
	DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
	assert(pDtlConn != 0);
	return new dtl_pdb::DtlTaskDataElementsTab(pDtlConn->GetDtlConnection());
}

void TaskDataElementsTab::Edit()
{
	m_stUpdateFilter = "WHERE (";
	m_stUpdateFilter += "ProcessId=";
	m_stUpdateFilter += toString(GetProcessId());
	m_stUpdateFilter += " AND ProcessVersion=";
	m_stUpdateFilter += toString(GetProcessVersion());
	m_stUpdateFilter += " AND TaskId=";
	m_stUpdateFilter += toString(GetTaskId());
	m_stUpdateFilter += " AND DataElementId=";
	m_stUpdateFilter += toString(GetDataElementId());
	m_stUpdateFilter += ")";
	m_row.SetDirty(false);
}

