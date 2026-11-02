/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#ifndef ETLFORGE_MESSAGING_CHECK_OPERATION_QUEUE_H
#define ETLFORGE_MESSAGING_CHECK_OPERATION_QUEUE_H

#include <vector>

#include <konga/Utils/DateUtils.h>

#include <konga/ItemAccess/UserInfo.h>

namespace konga {

	class OperationQueueEntry {
	public:
		OperationQueueEntry(long lOperationId, long lMsgId, bool bIsExecuting, time_t receivedat);
		~OperationQueueEntry();

		bool isExecuting() const;

		long getMsgId() const;
		long getOperationId() const;

		CrDateTime receivedAt() const;

	private:
		bool m_bIsExecuting;
		long m_lMsgId;
		long m_lOperationId;

		CrDateTime m_ReceivedAt;
	};

	                                                                                                  
	                                                                         
	std::vector<OperationQueueEntry> GetOperationQueue(const UserInfo& userInfo, long lOperationId, DbConnection* pMsgDbConn, DbConnection* pTranDb);

	enum OPERATION_QUEUE_WAIT_RESULT {
		OPERATION_QUEUE_WAIT_RESULT_DONE = 0,
		OPERATION_QUEUE_WAIT_RESULT_NOT_THERE = 1,
		OPERATION_QUEUE_WAIT_RESULT_TIME_OUT = 2
	};

	                                                                                                         
	                                                                         
	OPERATION_QUEUE_WAIT_RESULT WaitForOperation(const UserInfo& userInfo, long lOperationId, long lPollTimeSeconds, long lTimeOutSeconds, DbConnection* pMsgDbConn, DbConnection* pTranDb);
}

#endif                                              

