/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <dtl/DTL.h>

#include <konga/Db/DtlDbUtils/DtlDbConnection.h>

#include "DbConnection.h"
#include "DbException.h"

using namespace konga;

DbConnection* DbConnection::MakeInstance(const std::string& stConnectString           )
{
    DtlDbConnection* pConn = new DtlDbConnection();
    if( !stConnectString.empty() )
    {
        pConn->Open(stConnectString);
    }
    return pConn;

}
