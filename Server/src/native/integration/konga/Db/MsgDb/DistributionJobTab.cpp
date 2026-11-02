/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <assert.h>
#include <dtl/DTL.h>

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DtlDbUtils/DtlDbConnection.h>
#include <konga/Db/DtlMsgDb/DtlDistributionJobTab.h>

#include "DistributionJobTab.h"

using namespace konga;
using namespace MsgDb;

DistributionJobTab* DistributionJobTab::MakeInstance(DbConnection* pConn)
{
	DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
	assert(pDtlConn != 0);
	return new dtl_msgdb::DtlDistributionJobTab(pDtlConn->GetDtlConnection());
}

void DistributionJobTab::Edit()
{
	m_stUpdateFilter = "WHERE (";
	m_stUpdateFilter += "JobId=";
	m_stUpdateFilter += toString(GetJobId());
	m_stUpdateFilter += ")";
	m_row.SetDirty(false);
}

