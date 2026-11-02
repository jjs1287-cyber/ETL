/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <assert.h>
#include <dtl/DTL.h>

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DtlDbUtils/DtlDbConnection.h>
#include <konga/Db/DtlTranDb/DtlKey_Values.h>

#include "Key_Values.h"

using namespace konga;
using namespace TranDb;

Key_Values* Key_Values::MakeInstance(DbConnection* pConn)
{
	DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
	assert(pDtlConn != 0);
	return new dtl_trandb::DtlKey_Values(pDtlConn->GetDtlConnection());
}

void Key_Values::Edit()
{
	m_stUpdateFilter = "WHERE (";
	m_stUpdateFilter += "Key_ValuesID=";
	m_stUpdateFilter += toString(GetKey_ValuesID());
	m_stUpdateFilter += ")";
	m_row.SetDirty(false);
}

