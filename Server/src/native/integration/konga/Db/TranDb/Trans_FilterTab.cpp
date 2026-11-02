/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <assert.h>
#include <dtl/DTL.h>

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DtlDbUtils/DtlDbConnection.h>
#include <konga/Db/DtlTranDb/DtlTrans_FilterTab.h>

#include "Trans_FilterTab.h"

using namespace konga;
using namespace TranDb;

Trans_FilterTab* Trans_FilterTab::MakeInstance(DbConnection* pConn)
{
	DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
	assert(pDtlConn != 0);
	return new dtl_trandb::DtlTrans_FilterTab(pDtlConn->GetDtlConnection());
}

void Trans_FilterTab::Edit()
{
	m_stUpdateFilter = "WHERE (";
	m_stUpdateFilter += "Trans_Filter_ID=";
	m_stUpdateFilter += toString(GetTrans_Filter_ID());
	m_stUpdateFilter += ")";
	m_row.SetDirty(false);
}

