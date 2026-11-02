/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <assert.h>
#include <dtl/DTL.h>

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DtlDbUtils/DtlDbConnection.h>
#include <konga/Db/DtlMsgDb/DtlSettingsTab.h>

#include "SettingsTab.h"

using namespace konga;
using namespace MsgDb;

SettingsTab* SettingsTab::MakeInstance(DbConnection* pConn)
{
	DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
	assert(pDtlConn != 0);
	return new dtl_msgdb::DtlSettingsTab(pDtlConn->GetDtlConnection());
}

void SettingsTab::Edit()
{
	m_stUpdateFilter = "WHERE (";
	m_stUpdateFilter += "SettingsId=";
	m_stUpdateFilter += toString(GetSettingsId());
	m_stUpdateFilter += ")";
	m_row.SetDirty(false);
}

