/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <assert.h>
#include <dtl/DTL.h>

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DtlDbUtils/DtlDbConnection.h>
#include <konga/Db/DtlMsgDb/DtlArchiveDistributionJobInstanceTab.h>

#include "ArchiveDistributionJobInstanceTab.h"

using namespace konga;
using namespace MsgDb;

ArchiveDistributionJobInstanceTab* ArchiveDistributionJobInstanceTab::MakeInstance(DbConnection* pConn)
{
	DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
	assert(pDtlConn != 0);
	return new dtl_msgdb::DtlArchiveDistributionJobInstanceTab(pDtlConn->GetDtlConnection());
}

void ArchiveDistributionJobInstanceTab::Edit()
{
	m_stUpdateFilter = "WHERE (";
	m_stUpdateFilter += "Arc_ID=";
	m_stUpdateFilter += toString(GetArc_ID());
	m_stUpdateFilter += ")";
	m_row.SetDirty(false);
}

