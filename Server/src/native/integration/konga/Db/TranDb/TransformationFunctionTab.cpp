/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <assert.h>
#include <dtl/DTL.h>

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DtlDbUtils/DtlDbConnection.h>
#include <konga/Db/DtlTranDb/DtlTransformationFunctionTab.h>

#include "TransformationFunctionTab.h"

using namespace konga;
using namespace TranDb;

TransformationFunctionTab* TransformationFunctionTab::MakeInstance(DbConnection* pConn)
{
	DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
	assert(pDtlConn != 0);
	return new dtl_trandb::DtlTransformationFunctionTab(pDtlConn->GetDtlConnection());
}

void TransformationFunctionTab::Edit()
{
	m_stUpdateFilter = "WHERE (";
	m_stUpdateFilter += "Transformation_ID=";
	m_stUpdateFilter += toString(GetTransformation_ID());
	m_stUpdateFilter += ")";
	m_row.SetDirty(false);
}

