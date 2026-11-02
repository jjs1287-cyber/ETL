/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <assert.h>
#include <dtl/DTL.h>

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DtlDbUtils/DtlDbConnection.h>
#include <konga/Db/DtlMsgDb/DtlTransientErrorMsgTab.h>

#include "TransientErrorMsgTab.h"

using namespace konga;
using namespace MsgDb;

TransientErrorMsgTab* TransientErrorMsgTab::MakeInstance(DbConnection* pConn)
{
	DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
	assert(pDtlConn != 0);
	return new dtl_msgdb::DtlTransientErrorMsgTab(pDtlConn->GetDtlConnection());
}

void TransientErrorMsgTab::Edit()
{
	m_stUpdateFilter = "WHERE (";
	m_stUpdateFilter += "TranErrMsgId=";
	m_stUpdateFilter += toString(GetTranErrMsgId());
	m_stUpdateFilter += ")";
	m_row.SetDirty(false);
}

