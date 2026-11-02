/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <assert.h>

#include <konga/Db/DbUtils/DbConnection.h>

#include <konga/Db/DtlDbUtils/DtlDbConnection.h>

#include <konga/Db/DtlMsgDb/DtlSpInsertIncomingMsgTab.h>

#include "SpInsertIncomingMsgTab.h"


MsgDb::SpInsertIncomingMsgTab* MsgDb::SpInsertIncomingMsgTab::MakeInstance(konga::DbConnection* pConn)
{
    DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
    assert(pDtlConn != 0);
    return new dtl_msgdb::DtlSpInsertIncomingMsgTab(pDtlConn->GetDtlConnection());
}


