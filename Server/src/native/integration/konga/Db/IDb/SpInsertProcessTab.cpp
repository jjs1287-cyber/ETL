/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#include <assert.h>

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DtlDbUtils/DtlDbConnection.h>

#include <konga/Db/DtlIDb/DtlSpInsertProcessTab.h>

#include "SpInsertProcessTab.h"

IDb::SpInsertProcessTab* IDb::SpInsertProcessTab::MakeInstance(konga::DbConnection* pConn)
{
    DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
    assert(pDtlConn != 0);
    return new dtl_idb::DtlSpInsertProcessTab(pDtlConn->GetDtlConnection());
}
