/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <assert.h>
#include <dtl/DTL.h>

#include <konga/Utils/StringUtils.h>

                                       
#include <konga/Db/DtlDbUtils/DtlDbConnection.h>
#include <konga/Db/DtlMsgDb/DtlArchiveAttachmentTab.h>

#include "ArchiveAttachmentTab.h"

using namespace konga;
using namespace MsgDb;

ArchiveAttachmentTab* ArchiveAttachmentTab::MakeInstance(DbConnection* pConn)
{
	DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
	assert(pDtlConn != 0);
	return new dtl_msgdb::DtlArchiveAttachmentTab(pDtlConn->GetDtlConnection());
}

void ArchiveAttachmentTab::Edit()
{
	m_stUpdateFilter = "WHERE (";
	m_stUpdateFilter += "ArchiveId=";
	m_stUpdateFilter += toString(GetArchiveId());
	m_stUpdateFilter += ")";
	m_row.SetDirty(false);
}

