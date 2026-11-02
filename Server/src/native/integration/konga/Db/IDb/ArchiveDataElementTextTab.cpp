/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <assert.h>
#include <dtl/DTL.h>

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DtlDbUtils/DtlDbConnection.h>
#include <konga/Db/DtlIDb/DtlArchiveDataElementTextTab.h>

#include "ArchiveDataElementTextTab.h"

using namespace konga;
using namespace IDb;

ArchiveDataElementTextTab* ArchiveDataElementTextTab::MakeInstance(DbConnection* pConn)
{
	DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
	assert(pDtlConn != 0);
	return new dtl_idb::DtlArchiveDataElementTextTab(pDtlConn->GetDtlConnection());
}

void ArchiveDataElementTextTab::Edit()
{
	m_stUpdateFilter = "WHERE (";
	m_stUpdateFilter += "ArchiveId=";
	m_stUpdateFilter += toString(GetArchiveId());
	m_stUpdateFilter += ")";
	m_row.SetDirty(false);
}

