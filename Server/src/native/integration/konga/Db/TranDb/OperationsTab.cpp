/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <assert.h>
#include <dtl/DTL.h>

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DtlDbUtils/DtlDbConnection.h>
#include <konga/Db/DtlTranDb/DtlOperationsTab.h>

#include "OperationsTab.h"

using namespace konga;
using namespace TranDb;

OperationsTab* OperationsTab::MakeInstance(DbConnection* pConn)
{
	DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
	assert(pDtlConn != 0);
	return new dtl_trandb::DtlOperationsTab(pDtlConn->GetDtlConnection());
}

void OperationsTab::Edit()
{
	m_stUpdateFilter = "WHERE (";
	m_stUpdateFilter += "Operation_ID=";
	m_stUpdateFilter += toString(GetOperation_ID());
	m_stUpdateFilter += ")";
	m_row.SetDirty(false);
}

