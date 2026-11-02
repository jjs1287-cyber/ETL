/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <assert.h>
#include <dtl/DTL.h>

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DtlDbUtils/DtlDbConnection.h>
#include <konga/Db/DtlPDb/DtlRoutingRuleTab.h>

#include "RoutingRuleTab.h"

using namespace konga;
using namespace PDb;

RoutingRuleTab* RoutingRuleTab::MakeInstance(DbConnection* pConn)
{
	DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
	assert(pDtlConn != 0);
	return new dtl_pdb::DtlRoutingRuleTab(pDtlConn->GetDtlConnection());
}

void RoutingRuleTab::Edit()
{
	m_stUpdateFilter = "WHERE (";
	m_stUpdateFilter += "ProcessId=";
	m_stUpdateFilter += toString(GetProcessId());
	m_stUpdateFilter += " AND ProcessVersion=";
	m_stUpdateFilter += toString(GetProcessVersion());
	m_stUpdateFilter += " AND RoutingRuleId=";
	m_stUpdateFilter += toString(GetRoutingRuleId());
	m_stUpdateFilter += ")";
	m_row.SetDirty(false);
}

