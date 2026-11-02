/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#include <string>

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>
#include <konga/Db/DbUtils/DynamicDbRecordSet.h>

#include "DbMaintenance.h"

using namespace konga;
using std::string;

void konga::CleanOperationDataElementTab(DbConnection& IDbConnection, DbConnection& MsgDbConnection) {

	                                                                                      
	const string countMsgSql("select count(*) from incomingmsgtab");
	SmartDbPtr<DynamicDbRecordSet> incomingMsgTab( DynamicDbRecordSet::MakeInstance(&MsgDbConnection, countMsgSql) );
	incomingMsgTab->Open();
	if ( incomingMsgTab->Next() ) {
		const string& count = incomingMsgTab->GetFieldValue(0);
		if ( count != "0" )
			return;
	}

	IDbConnection.ExecuteSql("truncate table operationdataelementtab");
	IDbConnection.ExecuteSql("truncate table sessionidtab");
}
