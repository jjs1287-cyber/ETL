/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>

#include <konga/Db/TranDb/SpInsertOperationInstanceTab.h>

#include "OperationInstance.h"

using namespace konga;

long long konga::NewOperationInstance(DbConnection& tranDbConn, long lOperationId, const std::string& stFrom, long long llParentOperationInstanceId) {
	SmartDbPtr<TranDb::SpInsertOperationInstanceTab> spOperationInstance( TranDb::SpInsertOperationInstanceTab::MakeInstance( &tranDbConn ) );
	spOperationInstance->SetOperationId(lOperationId);
	spOperationInstance->SetParentOperationInstanceId(llParentOperationInstanceId);
	spOperationInstance->SetReceivedFrom(stFrom);
	spOperationInstance->Execute();
	return spOperationInstance->GetOperationInstanceId();
}

void konga::UpdateOperationInstance(DbConnection& tranDbConn, long long llOperationInstanceId, long long llMsgId) {
	tranDbConn.ExecuteSql( "UPDATE OperationInstanceTab SET msgid = " + toString(llMsgId)
		+ " WHERE operation_instance_id = " + toString(llOperationInstanceId) );
}

