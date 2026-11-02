/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <assert.h>
#include <dtl/DTL.h>

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DtlDbUtils/DtlDbConnection.h>
#include <konga/Db/DtlTranDb/DtlLogAuxTab.h>

#include "LogAuxTab.h"

using namespace konga;
using namespace TranDb;

LogAuxTab* LogAuxTab::MakeInstance(DbConnection* pConn)
{
	DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
	assert(pDtlConn != 0);
	return new dtl_trandb::DtlLogAuxTab(pDtlConn->GetDtlConnection());
}

void LogAuxTab::Edit()
{
	m_stUpdateFilter = "WHERE (";
	m_stUpdateFilter += "LogAuxID=";
	m_stUpdateFilter += toString(GetLogAuxID());
	m_stUpdateFilter += ")";
	m_row.SetDirty(false);
}
	
