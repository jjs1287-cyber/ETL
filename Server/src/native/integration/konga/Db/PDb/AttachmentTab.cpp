/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <assert.h>
#include <dtl/DTL.h>

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DtlDbUtils/DtlDbConnection.h>
#include <konga/Db/DtlPDb/DtlAttachmentTab.h>

#include "AttachmentTab.h"

using namespace konga;
using namespace PDb;

AttachmentTab* AttachmentTab::MakeInstance(DbConnection* pConn)
{
	DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
	assert(pDtlConn != 0);
	return new dtl_pdb::DtlAttachmentTab(pDtlConn->GetDtlConnection());
}

void AttachmentTab::Edit()
{
	m_stUpdateFilter = "WHERE (";
	m_stUpdateFilter += "AttachmentId=";
	m_stUpdateFilter += toString(GetAttachmentId());
	m_stUpdateFilter += " AND ProcessId=";
	m_stUpdateFilter += toString(GetProcessId());
	m_stUpdateFilter += " AND ProcessVersion=";
	m_stUpdateFilter += toString(GetProcessVersion());
	m_stUpdateFilter += " AND DocumentId=";
	m_stUpdateFilter += toString(GetDocumentId());
	m_stUpdateFilter += ")";
	m_row.SetDirty(false);
}

