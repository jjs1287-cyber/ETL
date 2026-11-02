/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <assert.h>
#include <dtl/DTL.h>

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DtlDbUtils/DtlDbConnection.h>
#include <konga/Db/DtlTranDb/DtlTargetsTab.h>

#include "TargetsTab.h"

using namespace konga;
using namespace TranDb;

TargetsTab* TargetsTab::MakeInstance(DbConnection* pConn)
{
	DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
	assert(pDtlConn != 0);
	return new dtl_trandb::DtlTargetsTab(pDtlConn->GetDtlConnection());
}

void TargetsTab::Edit()
{
	m_stUpdateFilter = "WHERE (";
	m_stUpdateFilter += "Target_ID=";
	m_stUpdateFilter += toString(GetTarget_ID());
	m_stUpdateFilter += ")";
	m_row.SetDirty(false);
}

