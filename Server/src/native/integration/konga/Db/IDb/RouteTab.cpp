/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <assert.h>
#include <dtl/DTL.h>

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DtlDbUtils/DtlDbConnection.h>
#include <konga/Db/DtlIDb/DtlRouteTab.h>

#include "RouteTab.h"

using namespace konga;
using namespace IDb;

RouteTab* RouteTab::MakeInstance(DbConnection* pConn)
{
	DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
	assert(pDtlConn != 0);
	return new dtl_idb::DtlRouteTab(pDtlConn->GetDtlConnection());
}

void RouteTab::Edit()
{
	m_stUpdateFilter = "WHERE (";
	m_stUpdateFilter += "ProcessId=";
	m_stUpdateFilter += toString(GetProcessId());
	m_stUpdateFilter += " AND ProcessInstanceId=";
	m_stUpdateFilter += toString(GetProcessInstanceId());
	m_stUpdateFilter += " AND DestinationTaskId=";
	m_stUpdateFilter += toString(GetDestinationTaskId());
	m_stUpdateFilter += " AND DestinationTaskInstanceId=";
	m_stUpdateFilter += toString(GetDestinationTaskInstanceId());
	m_stUpdateFilter += " AND OriginProcessId=";
	m_stUpdateFilter += toString(GetOriginProcessId());
	m_stUpdateFilter += " AND OriginProcessInstanceId=";
	m_stUpdateFilter += toString(GetOriginProcessInstanceId());
	m_stUpdateFilter += " AND OriginTaskId=";
	m_stUpdateFilter += toString(GetOriginTaskId());
	m_stUpdateFilter += " AND OriginTaskInstanceId=";
	m_stUpdateFilter += toString(GetOriginTaskInstanceId());
	m_stUpdateFilter += ")";
	m_row.SetDirty(false);
}

