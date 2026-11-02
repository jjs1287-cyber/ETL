/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <assert.h>
#include <dtl/DTL.h>

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DtlDbUtils/DtlDbConnection.h>
#include <konga/Db/DtlMsgDb/DtlPOPAccountsTab.h>

#include "POPAccountsTab.h"

using namespace konga;
using namespace MsgDb;

POPAccountsTab* POPAccountsTab::MakeInstance(DbConnection* pConn)
{
	DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
	assert(pDtlConn != 0);
	return new dtl_msgdb::DtlPOPAccountsTab(pDtlConn->GetDtlConnection());
}

void POPAccountsTab::Edit()
{
	m_stUpdateFilter = "WHERE (";
	m_stUpdateFilter += "POPUser=";
	m_stUpdateFilter += "'" + SqlEscape(GetPOPUser()) + "'";
	m_stUpdateFilter += ")";
	m_row.SetDirty(false);
}

