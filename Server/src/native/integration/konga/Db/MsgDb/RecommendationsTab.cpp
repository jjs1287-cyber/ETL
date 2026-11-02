/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <assert.h>
#include <dtl/DTL.h>

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DtlDbUtils/DtlDbConnection.h>
#include <konga/Db/DtlMsgDb/DtlRecommendationsTab.h>

#include "RecommendationsTab.h"

using namespace konga;
using namespace MsgDb;

RecommendationsTab* RecommendationsTab::MakeInstance(DbConnection* pConn)
{
	DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
	assert(pDtlConn != 0);
	return new dtl_msgdb::DtlRecommendationsTab(pDtlConn->GetDtlConnection());
}

void RecommendationsTab::Edit()
{
	m_stUpdateFilter = "WHERE (";
	m_stUpdateFilter += "EngineErrId=";
	m_stUpdateFilter += toString(GetEngineErrId());
	m_stUpdateFilter += ")";
	m_row.SetDirty(false);
}

