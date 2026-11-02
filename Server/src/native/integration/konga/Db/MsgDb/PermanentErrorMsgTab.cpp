/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <assert.h>
#include <dtl/DTL.h>

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DtlDbUtils/DtlDbConnection.h>
#include <konga/Db/DtlMsgDb/DtlPermanentErrorMsgTab.h>

#include "PermanentErrorMsgTab.h"

using namespace konga;
using namespace MsgDb;

PermanentErrorMsgTab* PermanentErrorMsgTab::MakeInstance(DbConnection* pConn)
{
	DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
	assert(pDtlConn != 0);
	return new dtl_msgdb::DtlPermanentErrorMsgTab(pDtlConn->GetDtlConnection());
}

void PermanentErrorMsgTab::Edit()
{
	m_stUpdateFilter = "WHERE (";
	m_stUpdateFilter += "PermErrMsgId=";
	m_stUpdateFilter += toString(GetPermErrMsgId());
	m_stUpdateFilter += ")";
	m_row.SetDirty(false);
}

