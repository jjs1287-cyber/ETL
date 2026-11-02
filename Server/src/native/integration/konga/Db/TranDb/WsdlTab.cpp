/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <assert.h>
#include <dtl/DTL.h>

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DtlDbUtils/DtlDbConnection.h>
#include <konga/Db/DtlTranDb/DtlWsdlTab.h>

#include "WsdlTab.h"

using namespace konga;
using namespace TranDb;

WsdlTab* WsdlTab::MakeInstance(DbConnection* pConn)
{
	DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
	assert(pDtlConn != 0);
	return new dtl_trandb::DtlWsdlTab(pDtlConn->GetDtlConnection());
}

void WsdlTab::Edit()
{
	m_stUpdateFilter = "WHERE (";
	m_stUpdateFilter += "Wsdl_ID=";
	m_stUpdateFilter += toString(GetWsdl_ID());
	m_stUpdateFilter += ")";
	m_row.SetDirty(false);
}

