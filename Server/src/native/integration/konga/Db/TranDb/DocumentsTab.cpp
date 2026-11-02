/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <assert.h>
#include <dtl/DTL.h>

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DtlDbUtils/DtlDbConnection.h>
#include <konga/Db/DtlTranDb/DtlDocumentsTab.h>

#include "DocumentsTab.h"

using namespace konga;
using namespace TranDb;

DocumentsTab* DocumentsTab::MakeInstance(DbConnection* pConn)
{
	DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
	assert(pDtlConn != 0);
	return new dtl_trandb::DtlDocumentsTab(pDtlConn->GetDtlConnection());
}

void DocumentsTab::Edit()
{
	m_stUpdateFilter = "WHERE (";
	m_stUpdateFilter += "Document_ID=";
	m_stUpdateFilter += toString(GetDocument_ID());
	m_stUpdateFilter += ")";
	m_row.SetDirty(false);
}

