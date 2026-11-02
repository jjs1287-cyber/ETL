/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <assert.h>
#include <dtl/DTL.h>

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DtlDbUtils/DtlDbConnection.h>
#include <konga/Db/DtlIDb/DtlTemporaryFileTab.h>

#include "TemporaryFileTab.h"

using namespace konga;
using namespace IDb;

TemporaryFileTab* TemporaryFileTab::MakeInstance(DbConnection* pConn)
{
	DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
	assert(pDtlConn != 0);
	return new dtl_idb::DtlTemporaryFileTab(pDtlConn->GetDtlConnection());
}

void TemporaryFileTab::Edit()
{
	m_stUpdateFilter = "WHERE (";
	m_stUpdateFilter += "FileId=";
	m_stUpdateFilter += toString(GetFileId());
	m_stUpdateFilter += ")";
	m_row.SetDirty(false);
}

