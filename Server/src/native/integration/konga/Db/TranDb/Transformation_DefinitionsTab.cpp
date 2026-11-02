/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <assert.h>
#include <dtl/DTL.h>

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DtlDbUtils/DtlDbConnection.h>
#include <konga/Db/DtlTranDb/DtlTransformation_DefinitionsTab.h>

#include "Transformation_DefinitionsTab.h"

using namespace konga;
using namespace TranDb;

Transformation_DefinitionsTab* Transformation_DefinitionsTab::MakeInstance(DbConnection* pConn)
{
	DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
	assert(pDtlConn != 0);
	return new dtl_trandb::DtlTransformation_DefinitionsTab(pDtlConn->GetDtlConnection());
}

void Transformation_DefinitionsTab::Edit()
{
	m_stUpdateFilter = "WHERE (";
	m_stUpdateFilter += "TranDiffID=";
	m_stUpdateFilter += toString(GetTranDiffID());
	m_stUpdateFilter += ")";
	m_row.SetDirty(false);
}

