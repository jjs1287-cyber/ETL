/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <assert.h>
#include <dtl/DTL.h>

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DtlDbUtils/DtlDbConnection.h>
#include <konga/Db/DtlPDb/DtlArgumentTab.h>

#include "ArgumentTab.h"

using namespace konga;
using namespace PDb;

ArgumentTab* ArgumentTab::MakeInstance(DbConnection* pConn)
{
	DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
	assert(pDtlConn != 0);
	return new dtl_pdb::DtlArgumentTab(pDtlConn->GetDtlConnection());
}

void ArgumentTab::Edit()
{
	m_stUpdateFilter = "WHERE (";
	m_stUpdateFilter += "ProcessId=";
	m_stUpdateFilter += toString(GetProcessId());
	m_stUpdateFilter += " AND ProcessVersion=";
	m_stUpdateFilter += toString(GetProcessVersion());
	m_stUpdateFilter += " AND TaskId=";
	m_stUpdateFilter += toString(GetTaskId());
	m_stUpdateFilter += " AND CallId=";
	m_stUpdateFilter += toString(GetCallId());
	m_stUpdateFilter += " AND ArgumentId=";
	m_stUpdateFilter += toString(GetArgumentId());
	m_stUpdateFilter += ")";
	m_row.SetDirty(false);
}

