/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef KONGA_LOG_OPERATION_LOG_STATUS_H
#define KONGA_LOG_OPERATION_LOG_STATUS_H

enum OperationLogStatus {
	OperationLogStatus_Pending = 0,
	OperationLogStatus_Cancelled = 1,
	OperationLogStatus_Running = 2,
	OperationLogStatus_Success = 3,
	OperationLogStatus_Success_With_Info = 4,
	OperationLogStatus_Success_With_Warning = 5,
	OperationLogStatus_Error = 6
};

namespace {
	bool IsCompletedStatus(OperationLogStatus status) {
		switch(status) {
			case OperationLogStatus_Pending:
			case OperationLogStatus_Running:
				return false;
			default:
				return true;
		}
	}

	const int NUMBER_OF_VALID_STATUSES = 7;
}

#endif                                    
