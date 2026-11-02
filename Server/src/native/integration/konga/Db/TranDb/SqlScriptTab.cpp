/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#include <assert.h>
#include <dtl/DTL.h>

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DtlDbUtils/DtlDbConnection.h>
#include <konga/Db/DtlTranDb/DtlSqlScriptTab.h>

#include "SqlScriptTab.h"

using namespace konga;
using namespace TranDb;

SqlScriptTab* SqlScriptTab::MakeInstance(DbConnection* pConn)
{
	DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
	assert(pDtlConn != 0);
	return new dtl_trandb::DtlSqlScriptTab(pDtlConn->GetDtlConnection());
}

void SqlScriptTab::Edit()
{
	m_stUpdateFilter = "WHERE (";
	m_stUpdateFilter += "SqlId=";
	m_stUpdateFilter += toString(GetSqlId());
	m_stUpdateFilter += ")";
	m_row.SetDirty(false);
}

