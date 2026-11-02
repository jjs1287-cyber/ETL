/*
 * Copyright (c) 1998-2006 Jitterbit, Inc.
 *
 */
#include <assert.h>
#include <dtl/DTL.h>

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DtlDbUtils/DtlDbConnection.h>
#include <konga/Db/DtlTranDb/DtlPipelinePluginInstanceTab.h>

#include "PipelinePluginInstanceTab.h"

using namespace konga;
using namespace TranDb;

PipelinePluginInstanceTab* PipelinePluginInstanceTab::MakeInstance(DbConnection* pConn)
{
	DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
	assert(pDtlConn != 0);
	return new dtl_trandb::DtlPipelinePluginInstanceTab(pDtlConn->GetDtlConnection());
}

void PipelinePluginInstanceTab::Edit()
{
	m_stUpdateFilter = "WHERE (";
	m_stUpdateFilter += "PipelinePluginInstanceId=";
	m_stUpdateFilter += toString(GetPipelinePluginInstanceId());
	m_stUpdateFilter += ")";
	m_row.SetDirty(false);
}

