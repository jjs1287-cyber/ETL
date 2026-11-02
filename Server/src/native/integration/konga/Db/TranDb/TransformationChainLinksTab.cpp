/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <assert.h>
#include <dtl/DTL.h>

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DtlDbUtils/DtlDbConnection.h>
#include <konga/Db/DtlTranDb/DtlTransformationChainLinksTab.h>

#include "TransformationChainLinksTab.h"

using namespace konga;
using namespace TranDb;

TransformationChainLinksTab* TransformationChainLinksTab::MakeInstance(DbConnection* pConn)
{
	DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
	assert(pDtlConn != 0);
	return new dtl_trandb::DtlTransformationChainLinksTab(pDtlConn->GetDtlConnection());
}

void TransformationChainLinksTab::Edit()
{
	m_stUpdateFilter = "WHERE (";
	m_stUpdateFilter += "TransformationChainLink_ID=";
	m_stUpdateFilter += toString(GetTransformationChainLink_ID());
	m_stUpdateFilter += ")";
	m_row.SetDirty(false);
}

