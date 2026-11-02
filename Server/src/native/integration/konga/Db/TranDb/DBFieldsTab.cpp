/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <assert.h>
#include <dtl/DTL.h>

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DtlDbUtils/DtlDbConnection.h>
#include <konga/Db/DtlTranDb/DtlDBFieldsTab.h>

#include "DBFieldsTab.h"

using namespace konga;
using namespace TranDb;

DBFieldsTab* DBFieldsTab::MakeInstance(DbConnection* pConn)
{
	DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
	assert(pDtlConn != 0);
	return new dtl_trandb::DtlDBFieldsTab(pDtlConn->GetDtlConnection());
}

void DBFieldsTab::Edit()
{
	m_stUpdateFilter = "WHERE (";
	m_stUpdateFilter += "DBFieldID=";
	m_stUpdateFilter += toString(GetDBFieldID());
	m_stUpdateFilter += ")";
	m_row.SetDirty(false);
}

