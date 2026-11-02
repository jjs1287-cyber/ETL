/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <assert.h>
#include <dtl/DTL.h>

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DtlDbUtils/DtlDbConnection.h>
#include <konga/Db/DtlTranDb/DtlTran_RelationsTab.h>

#include "Tran_RelationsTab.h"

using namespace konga;
using namespace TranDb;

Tran_RelationsTab* Tran_RelationsTab::MakeInstance(DbConnection* pConn)
{
	DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
	assert(pDtlConn != 0);
	return new dtl_trandb::DtlTran_RelationsTab(pDtlConn->GetDtlConnection());
}

void Tran_RelationsTab::Edit()
{
	m_stUpdateFilter = "WHERE (";
	m_stUpdateFilter += "Relation_ID=";
	m_stUpdateFilter += toString(GetRelation_ID());
	m_stUpdateFilter += " AND Transformation_ID=";
	m_stUpdateFilter += toString(GetTransformation_ID());
	m_stUpdateFilter += ")";
	m_row.SetDirty(false);
}

