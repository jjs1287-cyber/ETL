/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <assert.h>

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DtlDbUtils/DtlDbConnection.h>

#include <konga/Db/DtlIDb/DtlSpInsertRouteTab.h>

#include "SpInsertRouteTab.h"

IDb::SpInsertRouteTab* IDb::SpInsertRouteTab::MakeInstance(konga::DbConnection* pConn)
{
    DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
    assert(pDtlConn != 0);
    return new dtl_idb::DtlSpInsertRouteTab(pDtlConn->GetDtlConnection());
}
