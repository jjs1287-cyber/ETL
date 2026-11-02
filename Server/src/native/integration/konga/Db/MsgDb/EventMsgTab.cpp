/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <assert.h>
#include <dtl/DTL.h>

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DtlDbUtils/DtlDbConnection.h>
#include <konga/Db/DtlMsgDb/DtlEventMsgTab.h>

#include "EventMsgTab.h"

using namespace konga;
using namespace MsgDb;

EventMsgTab* EventMsgTab::MakeInstance(DbConnection* pConn)
{
	DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
	assert(pDtlConn != 0);
	return new dtl_msgdb::DtlEventMsgTab(pDtlConn->GetDtlConnection());
}

void EventMsgTab::Edit()
{
	m_stUpdateFilter = "WHERE (";
	m_stUpdateFilter += "MsgId=";
	m_stUpdateFilter += toString(GetMsgId());
	m_stUpdateFilter += ")";
	m_row.SetDirty(false);
}

