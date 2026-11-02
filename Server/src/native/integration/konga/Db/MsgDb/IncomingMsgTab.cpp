/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <assert.h>
#include <dtl/DTL.h>

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DtlDbUtils/DtlDbConnection.h>
#include <konga/Db/DtlMsgDb/DtlIncomingMsgTab.h>

#include "IncomingMsgTab.h"

using namespace konga;
using namespace MsgDb;

IncomingMsgTab* IncomingMsgTab::MakeInstance(DbConnection* pConn)
{
	DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
	assert(pDtlConn != 0);
	return new dtl_msgdb::DtlIncomingMsgTab(pDtlConn->GetDtlConnection());
}

void IncomingMsgTab::Edit()
{
	m_stUpdateFilter = "WHERE (";
	m_stUpdateFilter += "MsgId=";
	m_stUpdateFilter += toString(GetMsgId());
	m_stUpdateFilter += ")";
	m_row.SetDirty(false);
}

