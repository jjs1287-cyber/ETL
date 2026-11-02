/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <assert.h>
#include <dtl/DTL.h>

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DtlDbUtils/DtlDbConnection.h>
#include <konga/Db/DtlTranDb/DtlFolderItemTab.h>

#include "FolderItemTab.h"

using namespace konga;
using namespace TranDb;

FolderItemTab* FolderItemTab::MakeInstance(DbConnection* pConn)
{
	DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
	assert(pDtlConn != 0);
	return new dtl_trandb::DtlFolderItemTab(pDtlConn->GetDtlConnection());
}

void FolderItemTab::Edit()
{
	m_stUpdateFilter = "WHERE (";
	m_stUpdateFilter += "FolderItem_ID=";
	m_stUpdateFilter += toString(GetFolderItem_ID());
	m_stUpdateFilter += ")";
	m_row.SetDirty(false);
}

