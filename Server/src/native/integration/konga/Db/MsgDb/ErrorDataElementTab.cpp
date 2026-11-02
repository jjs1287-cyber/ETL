/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <assert.h>
#include <dtl/DTL.h>

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DtlDbUtils/DtlDbConnection.h>
#include <konga/Db/DtlMsgDb/DtlErrorDataElementTab.h>

#include "ErrorDataElementTab.h"

using namespace konga;
using namespace MsgDb;

ErrorDataElementTab* ErrorDataElementTab::MakeInstance(DbConnection* pConn)
{
	DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
	assert(pDtlConn != 0);
	return new dtl_msgdb::DtlErrorDataElementTab(pDtlConn->GetDtlConnection());
}

void ErrorDataElementTab::Edit()
{
	m_stUpdateFilter = "WHERE (";
	m_stUpdateFilter += "ErrorId=";
	m_stUpdateFilter += toString(GetErrorId());
	m_stUpdateFilter += ")";
	m_row.SetDirty(false);
}

