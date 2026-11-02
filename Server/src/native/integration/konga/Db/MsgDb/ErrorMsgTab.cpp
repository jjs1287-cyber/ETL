/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <assert.h>
#include <dtl/DTL.h>

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DtlDbUtils/DtlDbConnection.h>
#include <konga/Db/DtlMsgDb/DtlErrorMsgTab.h>

#include "ErrorMsgTab.h"

using namespace konga;
using namespace MsgDb;

ErrorMsgTab* ErrorMsgTab::MakeInstance(DbConnection* pConn)
{
	DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
	assert(pDtlConn != 0);
	return new dtl_msgdb::DtlErrorMsgTab(pDtlConn->GetDtlConnection());
}

void ErrorMsgTab::Edit()
{
	m_stUpdateFilter = "WHERE (";
	m_stUpdateFilter += "ErrorMsgId=";
	m_stUpdateFilter += toString(GetErrorMsgId());
	m_stUpdateFilter += ")";
	m_row.SetDirty(false);
}

