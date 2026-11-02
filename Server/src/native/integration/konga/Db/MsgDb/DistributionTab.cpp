/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <assert.h>
#include <dtl/DTL.h>

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DtlDbUtils/DtlDbConnection.h>
#include <konga/Db/DtlMsgDb/DtlDistributionTab.h>

#include "DistributionTab.h"

using namespace konga;
using namespace MsgDb;

DistributionTab* DistributionTab::MakeInstance(DbConnection* pConn)
{
	DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
	assert(pDtlConn != 0);
	return new dtl_msgdb::DtlDistributionTab(pDtlConn->GetDtlConnection());
}

void DistributionTab::Edit()
{
	m_stUpdateFilter = "WHERE (";
	m_stUpdateFilter += "JobDescId=";
	m_stUpdateFilter += toString(GetJobDescId());
	m_stUpdateFilter += ")";
	m_row.SetDirty(false);
}

