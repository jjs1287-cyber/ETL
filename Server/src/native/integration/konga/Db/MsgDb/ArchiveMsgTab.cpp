/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <assert.h>
#include <dtl/DTL.h>

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DtlDbUtils/DtlDbConnection.h>
#include <konga/Db/DtlMsgDb/DtlArchiveMsgTab.h>

#include "ArchiveMsgTab.h"

using namespace konga;
using namespace MsgDb;

ArchiveMsgTab* ArchiveMsgTab::MakeInstance(DbConnection* pConn)
{
	DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
	assert(pDtlConn != 0);
	return new dtl_msgdb::DtlArchiveMsgTab(pDtlConn->GetDtlConnection());
}

void ArchiveMsgTab::Edit()
{
	m_stUpdateFilter = "WHERE (";
	m_stUpdateFilter += "ArchiveId=";
	m_stUpdateFilter += toString(GetArchiveId());
	m_stUpdateFilter += ")";
	m_row.SetDirty(false);
}

