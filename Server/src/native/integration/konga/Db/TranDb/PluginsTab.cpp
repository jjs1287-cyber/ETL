/*
 * Copyright (c) 1998-2006 Jitterbit, Inc.
 *
 */
#include <assert.h>
#include <dtl/DTL.h>

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DtlDbUtils/DtlDbConnection.h>
#include <konga/Db/DtlTranDb/DtlPluginsTab.h>

#include "PluginsTab.h"

using namespace konga;
using namespace TranDb;

PluginsTab* PluginsTab::MakeInstance(DbConnection* pConn)
{
	DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
	assert(pDtlConn != 0);
	return new dtl_trandb::DtlPluginsTab(pDtlConn->GetDtlConnection());
}

void PluginsTab::Edit()
{
	m_stUpdateFilter = "WHERE (";
	m_stUpdateFilter += "PluginId=";
	m_stUpdateFilter += toString(GetPluginId());
	m_stUpdateFilter += ")";
	m_row.SetDirty(false);
}

