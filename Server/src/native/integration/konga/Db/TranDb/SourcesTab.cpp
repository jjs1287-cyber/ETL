/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <assert.h>
#include <dtl/DTL.h>

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DtlDbUtils/DtlDbConnection.h>
#include <konga/Db/DtlTranDb/DtlSourcesTab.h>

#include "SourcesTab.h"

using namespace konga;
using namespace TranDb;

SourcesTab* SourcesTab::MakeInstance(DbConnection* pConn)
{
	DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
	assert(pDtlConn != 0);
	return new dtl_trandb::DtlSourcesTab(pDtlConn->GetDtlConnection());
}

void SourcesTab::Edit()
{
	m_stUpdateFilter = "WHERE (";
	m_stUpdateFilter += "Source_ID=";
	m_stUpdateFilter += toString(GetSource_ID());
	m_stUpdateFilter += ")";
	m_row.SetDirty(false);
}

