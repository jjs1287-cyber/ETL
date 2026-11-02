/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <assert.h>

#include <konga/Db/DtlDbUtils/DtlDbConnection.h>
#include <konga/Db/DtlDbUtils/DtlDynamicDbRecordSet.h>

#include "DbUtils.h"
#include "DynamicDbRecordSet.h"

using namespace konga;
using namespace std;

DynamicDbRecordSet* DynamicDbRecordSet::MakeInstance(DbConnection* pConn, const string& stSql           )
{
    DtlDbConnection* pDtlConn = dynamic_cast<DtlDbConnection*>(pConn);
    assert(pDtlConn != 0);
    return new DtlDynamicDbRecordSet(pDtlConn->GetDtlConnection(), stSql);
}


string DynamicDbRecordSet::GetFieldValue(const string& stName) {
	return GetFieldValue(GetFieldIndexByName(stName));
}

