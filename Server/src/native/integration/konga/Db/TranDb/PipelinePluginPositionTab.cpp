/*
 * Copyright (c) 1998-2006 Jitterbit, Inc.
 *
 */
#include <assert.h>
#include <dtl/DTL.h>

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DtlDbUtils/DtlDbConnection.h>
#include <konga/Db/DtlTranDb/DtlPipelinePluginPositionTab.h>

#include "PipelinePluginPositionTab.h"

using namespace konga;
using namespace TranDb;

PipelinePluginPositionTab* PipelinePluginPositionTab::MakeInstance(DbConnection* pConn)
{
	DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
	assert(pDtlConn != 0);
	return new dtl_trandb::DtlPipelinePluginPositionTab(pDtlConn->GetDtlConnection());
}

void PipelinePluginPositionTab::Edit()
{
	m_stUpdateFilter = "WHERE (";
	m_stUpdateFilter += "PipelinePluginPositionId=";
	m_stUpdateFilter += toString(GetPipelinePluginPositionId());
	m_stUpdateFilter += ")";
	m_row.SetDirty(false);
}

