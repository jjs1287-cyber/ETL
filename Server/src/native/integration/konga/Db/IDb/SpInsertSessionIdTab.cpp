/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#include <assert.h>

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DtlDbUtils/DtlDbConnection.h>

#include <konga/Db/DtlIDb/DtlSpInsertSessionIdTab.h>

#include "SpInsertSessionIdTab.h"

IDb::SpInsertSessionIdTab* IDb::SpInsertSessionIdTab::MakeInstance(konga::DbConnection* pConn)
{
    DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
    assert(pDtlConn != 0);
    return new dtl_idb::DtlSpInsertSessionIdTab(pDtlConn->GetDtlConnection());
}
