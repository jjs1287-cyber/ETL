/*
 * Copyright (c) 1998-2006 Jitterbit, Inc.
 *
 */
#include <assert.h>
#include <dtl/DTL.h>

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DtlDbUtils/DtlDbConnection.h>
#include <konga/Db/DtlTranDb/DtlMessagesTab.h>

#include "MessagesTab.h"

using namespace konga;
using namespace TranDb;

MessagesTab* MessagesTab::MakeInstance(DbConnection* pConn)
{
	DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
	assert(pDtlConn != 0);
	return new dtl_trandb::DtlMessagesTab(pDtlConn->GetDtlConnection());
}

void MessagesTab::Edit()
{
	m_stUpdateFilter = "WHERE (";
	m_stUpdateFilter += "Message_ID=";
	m_stUpdateFilter += toString(GetMessage_ID());
	m_stUpdateFilter += ")";
	m_row.SetDirty(false);
}

