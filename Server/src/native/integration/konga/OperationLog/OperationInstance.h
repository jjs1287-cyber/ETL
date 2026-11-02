/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef KONGA_LOG_OPERATION_INSTANCE_H
#define KONGA_LOG_OPERATION_INSTANCE_H

namespace konga {

	class DbConnection;

	long long NewOperationInstance(DbConnection& tranDbConn, long lOperationId, const std::string& stFrom, long long llParentOperationInstanceId);

	void UpdateOperationInstance(DbConnection& tranDbConn,long long llOperationInstanceId, long long llMsgId);

	long long DeleteOperationInstances(DbConnection& tranDbConn, long lOperationId);
}

#endif                                  

