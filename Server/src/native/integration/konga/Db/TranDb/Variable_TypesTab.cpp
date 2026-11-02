/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <assert.h>
#include <dtl/DTL.h>

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DtlDbUtils/DtlDbConnection.h>
#include <konga/Db/DtlTranDb/DtlVariable_TypesTab.h>

#include "Variable_TypesTab.h"

using namespace konga;
using namespace TranDb;

Variable_TypesTab* Variable_TypesTab::MakeInstance(DbConnection* pConn)
{
	DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
	assert(pDtlConn != 0);
	return new dtl_trandb::DtlVariable_TypesTab(pDtlConn->GetDtlConnection());
}

void Variable_TypesTab::Edit()
{
	m_stUpdateFilter = "WHERE (";
	m_stUpdateFilter += "Var_Type_ID=";
	m_stUpdateFilter += toString(GetVar_Type_ID());
	m_stUpdateFilter += ")";
	m_row.SetDirty(false);
}

