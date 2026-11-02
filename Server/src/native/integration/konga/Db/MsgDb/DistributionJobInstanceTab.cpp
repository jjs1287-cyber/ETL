/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <assert.h>
#include <dtl/DTL.h>

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DtlDbUtils/DtlDbConnection.h>
#include <konga/Db/DtlMsgDb/DtlDistributionJobInstanceTab.h>

#include "DistributionJobInstanceTab.h"

using namespace konga;
using namespace MsgDb;

DistributionJobInstanceTab* DistributionJobInstanceTab::MakeInstance(DbConnection* pConn)
{
	DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
	assert(pDtlConn != 0);
	return new dtl_msgdb::DtlDistributionJobInstanceTab(pDtlConn->GetDtlConnection());
}

void DistributionJobInstanceTab::Edit()
{
	m_stUpdateFilter = "WHERE (";
	m_stUpdateFilter += "JobInstanceId=";
	m_stUpdateFilter += toString(GetJobInstanceId());
	m_stUpdateFilter += ")";
	m_row.SetDirty(false);
}

