/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <assert.h>
#include <dtl/DTL.h>

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DtlDbUtils/DtlDbConnection.h>
#include <konga/Db/DtlTranDb/DtlLogTab.h>

#include "LogTab.h"

using namespace konga;
using namespace TranDb;

LogTab* LogTab::MakeInstance(DbConnection* pConn)
{
	DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
	assert(pDtlConn != 0);
	return new dtl_trandb::DtlLogTab(pDtlConn->GetDtlConnection());
}

void LogTab::Edit()
{
	m_stUpdateFilter = "WHERE (";
	m_stUpdateFilter += "Log_ID=";
	m_stUpdateFilter += toString(GetLog_ID());
	m_stUpdateFilter += ")";
	m_row.SetDirty(false);
}

