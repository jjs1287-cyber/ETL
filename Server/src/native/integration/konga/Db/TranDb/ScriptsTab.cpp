/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
#include <assert.h>
#include <dtl/DTL.h>

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DtlDbUtils/DtlDbConnection.h>
#include <konga/Db/DtlTranDb/DtlScriptsTab.h>

#include "ScriptsTab.h"

using namespace konga;
using namespace TranDb;

ScriptsTab* ScriptsTab::MakeInstance(DbConnection* pConn)
{
	DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
	assert(pDtlConn != 0);
	return new dtl_trandb::DtlScriptsTab(pDtlConn->GetDtlConnection());
}

void ScriptsTab::Edit()
{
	m_stUpdateFilter = "WHERE (";
	m_stUpdateFilter += "Script_ID=";
	m_stUpdateFilter += toString(GetScript_ID());
	m_stUpdateFilter += ")";
	m_row.SetDirty(false);
}

