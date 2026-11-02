/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <assert.h>
#include <dtl/DTL.h>

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DtlDbUtils/DtlDbConnection.h>
#include <konga/Db/DtlIDb/DtlRouteDataElementTextTab.h>

#include "RouteDataElementTextTab.h"

using namespace konga;
using namespace IDb;

RouteDataElementTextTab* RouteDataElementTextTab::MakeInstance(DbConnection* pConn)
{
	DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
	assert(pDtlConn != 0);
	return new dtl_idb::DtlRouteDataElementTextTab(pDtlConn->GetDtlConnection());
}

void RouteDataElementTextTab::Edit()
{
	m_stUpdateFilter = "WHERE (";
	m_stUpdateFilter += "RouteInstanceId=";
	m_stUpdateFilter += toString(GetRouteInstanceId());
	m_stUpdateFilter += " AND DataElementId=";
	m_stUpdateFilter += toString(GetDataElementId());
	m_stUpdateFilter += ")";
	m_row.SetDirty(false);
}

