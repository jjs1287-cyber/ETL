/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#include <assert.h>

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DtlDbUtils/DtlDbConnection.h>
#include <konga/Db/DtlTranDb/DtlSpInsertOperationInstanceTab.h>

#include "SpInsertOperationInstanceTab.h"

TranDb::SpInsertOperationInstanceTab* TranDb::SpInsertOperationInstanceTab::MakeInstance(konga::DbConnection* pConn)
{
    DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
    assert(pDtlConn != 0);
    return new dtl_trandb::DtlSpInsertOperationInstanceTab(pDtlConn->GetDtlConnection());
}

