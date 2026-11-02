/*
 * Copyright (c) 1998-2006 Jitterbit, Inc.
 *
 */
#include <assert.h>
#include <dtl/DTL.h>

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DtlDbUtils/DtlDbConnection.h>
#include <konga/Db/DtlIDb/DtlOperationDataElementTab.h>

#include "OperationDataElementTab.h"

using namespace konga;
using namespace IDb;

OperationDataElementTab* OperationDataElementTab::MakeInstance(DbConnection* pConn)
{
	DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
	assert(pDtlConn != 0);
	return new dtl_idb::DtlOperationDataElementTab(pDtlConn->GetDtlConnection());
}

void OperationDataElementTab::Edit()
{
	m_stUpdateFilter = "WHERE (";
	m_stUpdateFilter += "SessionId=";
	m_stUpdateFilter += toString(GetSessionId());
	m_stUpdateFilter += " AND DataElementName='";
	m_stUpdateFilter += SqlEscape(GetDataElementName());
	m_stUpdateFilter += " AND ArrayIndex='";
	m_stUpdateFilter += SqlEscape(GetArrayIndex());
	m_stUpdateFilter += "')";
	m_row.SetDirty(false);
}

