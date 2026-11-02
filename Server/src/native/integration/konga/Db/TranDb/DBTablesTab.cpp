/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <assert.h>
#include <dtl/DTL.h>

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DtlDbUtils/DtlDbConnection.h>
#include <konga/Db/DtlTranDb/DtlDBTablesTab.h>

#include "DBTablesTab.h"

using namespace konga;
using namespace TranDb;

DBTablesTab* DBTablesTab::MakeInstance(DbConnection* pConn)
{
	DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
	assert(pDtlConn != 0);
	return new dtl_trandb::DtlDBTablesTab(pDtlConn->GetDtlConnection());
}

void DBTablesTab::Edit()
{
	m_stUpdateFilter = "WHERE (";
	m_stUpdateFilter += "DBTableID=";
	m_stUpdateFilter += toString(GetDBTableID());
	m_stUpdateFilter += ")";
	m_row.SetDirty(false);
}

