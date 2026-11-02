/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <assert.h>
#include <dtl/DTL.h>

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DtlDbUtils/DtlDbConnection.h>
#include <konga/Db/DtlPDb/DtlTransferTab.h>

#include "TransferTab.h"

using namespace konga;
using namespace PDb;

TransferTab* TransferTab::MakeInstance(DbConnection* pConn)
{
	DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
	assert(pDtlConn != 0);
	return new dtl_pdb::DtlTransferTab(pDtlConn->GetDtlConnection());
}

void TransferTab::Edit()
{
	m_stUpdateFilter = "WHERE (";
	m_stUpdateFilter += "TransferId=";
	m_stUpdateFilter += toString(GetTransferId());
	m_stUpdateFilter += ")";
	m_row.SetDirty(false);
}

