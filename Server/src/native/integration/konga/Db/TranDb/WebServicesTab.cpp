/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <assert.h>
#include <dtl/DTL.h>

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DtlDbUtils/DtlDbConnection.h>
#include <konga/Db/DtlTranDb/DtlWebServicesTab.h>

#include "WebServicesTab.h"

using namespace konga;
using namespace TranDb;

WebServicesTab* WebServicesTab::MakeInstance(DbConnection* pConn)
{
	DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
	assert(pDtlConn != 0);
	return new dtl_trandb::DtlWebServicesTab(pDtlConn->GetDtlConnection());
}

void WebServicesTab::Edit()
{
	m_stUpdateFilter = "WHERE (";
	m_stUpdateFilter += "WebService_ID=";
	m_stUpdateFilter += toString(GetWebService_ID());
	m_stUpdateFilter += ")";
	m_row.SetDirty(false);
}

