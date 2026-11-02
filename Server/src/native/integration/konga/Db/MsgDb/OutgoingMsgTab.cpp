/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <assert.h>
#include <dtl/DTL.h>

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DtlDbUtils/DtlDbConnection.h>
#include <konga/Db/DtlMsgDb/DtlOutgoingMsgTab.h>

#include "OutgoingMsgTab.h"

using namespace konga;
using namespace MsgDb;

OutgoingMsgTab* OutgoingMsgTab::MakeInstance(DbConnection* pConn)
{
	DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
	assert(pDtlConn != 0);
	return new dtl_msgdb::DtlOutgoingMsgTab(pDtlConn->GetDtlConnection());
}

void OutgoingMsgTab::Edit()
{
	m_stUpdateFilter = "WHERE (";
	m_stUpdateFilter += "MsgID=";
	m_stUpdateFilter += toString(GetMsgID());
	m_stUpdateFilter += ")";
	m_row.SetDirty(false);
}

