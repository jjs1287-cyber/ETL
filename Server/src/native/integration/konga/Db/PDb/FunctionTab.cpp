/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <assert.h>
#include <dtl/DTL.h>

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DtlDbUtils/DtlDbConnection.h>
#include <konga/Db/DtlPDb/DtlFunctionTab.h>

#include "FunctionTab.h"

using namespace konga;
using namespace PDb;

FunctionTab* FunctionTab::MakeInstance(DbConnection* pConn)
{
	DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
	assert(pDtlConn != 0);
	return new dtl_pdb::DtlFunctionTab(pDtlConn->GetDtlConnection());
}

void FunctionTab::Edit()
{
	m_stUpdateFilter = "WHERE (";
	m_stUpdateFilter += "ID=";
	m_stUpdateFilter += toString(GetID());
	m_stUpdateFilter += ")";
	m_row.SetDirty(false);
}

