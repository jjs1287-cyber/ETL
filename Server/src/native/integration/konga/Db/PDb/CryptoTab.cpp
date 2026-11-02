/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <assert.h>
#include <dtl/DTL.h>

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DtlDbUtils/DtlDbConnection.h>
#include <konga/Db/DtlPDb/DtlCryptoTab.h>

#include "CryptoTab.h"

using namespace konga;
using namespace PDb;

CryptoTab* CryptoTab::MakeInstance(DbConnection* pConn)
{
	DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
	assert(pDtlConn != 0);
	return new dtl_pdb::DtlCryptoTab(pDtlConn->GetDtlConnection());
}

void CryptoTab::Edit()
{
	m_stUpdateFilter = "WHERE (";
	m_stUpdateFilter += "CryptoInfoId=";
	m_stUpdateFilter += toString(GetCryptoInfoId());
	m_stUpdateFilter += ")";
	m_row.SetDirty(false);
}

