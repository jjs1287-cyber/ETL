/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <assert.h>
#include <dtl/DTL.h>

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DtlDbUtils/DtlDbConnection.h>
#include <konga/Db/DtlPDb/DtlActivityRuleTab.h>

#include "ActivityRuleTab.h"

using namespace konga;
using namespace PDb;

ActivityRuleTab* ActivityRuleTab::MakeInstance(DbConnection* pConn)
{
	DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
	assert(pDtlConn != 0);
	return new dtl_pdb::DtlActivityRuleTab(pDtlConn->GetDtlConnection());
}

void ActivityRuleTab::Edit()
{
	m_stUpdateFilter = "WHERE (";
	m_stUpdateFilter += "ProcessId=";
	m_stUpdateFilter += toString(GetProcessId());
	m_stUpdateFilter += " AND ProcessVersion=";
	m_stUpdateFilter += toString(GetProcessVersion());
	m_stUpdateFilter += " AND ActivityRuleId=";
	m_stUpdateFilter += toString(GetActivityRuleId());
	m_stUpdateFilter += ")";
	m_row.SetDirty(false);
}

