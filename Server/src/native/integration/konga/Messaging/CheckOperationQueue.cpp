/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#include <string>
#include <vector>

#include <konga/Utils/ProcessUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>

#include <konga/Db/MsgDb/IncomingMsgTab.h>

#include <konga/DbRelations/EntityIdConstants.h>	                 

#include <konga/ItemAccess/UserAccess.h>

#include "CheckOperationQueue.h"

using namespace konga;
using std::vector;
using std::string;

OperationQueueEntry::OperationQueueEntry(long lOperationId, long lMsgId, bool bIsExecuting, time_t receivedat) :
m_bIsExecuting(bIsExecuting), m_lMsgId(lMsgId), m_lOperationId(lOperationId), m_ReceivedAt(receivedat)
{}

OperationQueueEntry::~OperationQueueEntry() {}

bool OperationQueueEntry::isExecuting() const {
	return m_bIsExecuting;
}

long OperationQueueEntry::getMsgId() const {
	return m_lMsgId;
}

long OperationQueueEntry::getOperationId() const {
	return m_lOperationId;
}

CrDateTime OperationQueueEntry::receivedAt() const {
	return m_ReceivedAt;
}

vector<OperationQueueEntry> konga::GetOperationQueue(const UserInfo& userInfo, long lOperationId, DbConnection* pMsgDbConn, DbConnection* pTranDbConn) {

	SmartDbPtr<DbConnection> adopt_pMsgDbConn(0);
	if ( pMsgDbConn == 0 ) {
		adopt_pMsgDbConn.Attach( pMsgDbConn = konga::GetBackEndDbConnection("MsgDb") );
	}

	vector<OperationQueueEntry> queue;

	string stWhere;
	if ( lOperationId > 0 ) {
		stWhere = "WHERE processvariables = '{Interchange;0;1;0;OperationID=" + konga::toString(lOperationId) + ";}'";
	}

	SmartDbPtr<DbConnection> adopt_pTranDbConn(0);
	if ( pTranDbConn == 0 ) {
		adopt_pTranDbConn.Attach( pTranDbConn = konga::GetBackEndDbConnection("TranDb") );
	}
	const UserAccess& userAccess = UserAccess::GetUserAccessWithGroup(userInfo.GetId(), *pTranDbConn);

	SmartDbPtr<MsgDb::IncomingMsgTab> pIncomingMsgTab( MsgDb::IncomingMsgTab::MakeInstance(pMsgDbConn) );
	pIncomingMsgTab->Select(stWhere);
	while( pIncomingMsgTab->Next() ) {
		long thisOperationId = -1;
		if ( lOperationId > 0 ) {
			thisOperationId = lOperationId;
		}
		else {
			const string& processVariables = pIncomingMsgTab->GetProcessVariables();
			const string::size_type startPos = processVariables.find(";OperationID=");
			if ( startPos == string::npos ) continue;
			const string::size_type endPos = processVariables.find(";}", startPos + 1);
			if ( endPos == string::npos ) continue;
			const string sOperationId( processVariables.substr(startPos + 13, endPos - startPos - 13) );
			thisOperationId = konga::toLong(sOperationId);
		}
		if( userAccess.HasReadPermission(thisOperationId, OperationEntityId) ) {
			OperationQueueEntry entry(thisOperationId, pIncomingMsgTab->GetMsgId(), pIncomingMsgTab->GetIsExecuting()!=0, pIncomingMsgTab->GetReceivedAt());
			queue.push_back(entry);
		}
	}

	return queue;
}

OPERATION_QUEUE_WAIT_RESULT konga::WaitForOperation(const UserInfo& userInfo, long lOperationId, long lPollTimeSeconds, long lTimeOutSeconds, DbConnection* pMsgDbConn, DbConnection* pTranDb) {
	const CrDateTime startTime( CrDateTime::GetCurrentTime() );

	if ( lPollTimeSeconds < 1 ) lPollTimeSeconds = 1;
	if ( lTimeOutSeconds < 1 ) lTimeOutSeconds = 1;

	SmartDbPtr<DbConnection> adopt_pMsgDbConn(0);
	if ( pMsgDbConn == 0 ) {
		adopt_pMsgDbConn.Attach( pMsgDbConn = konga::GetBackEndDbConnection("MsgDb") );
	}

	vector<OperationQueueEntry> queue = GetOperationQueue(userInfo, lOperationId, pMsgDbConn, pTranDb);
	if ( queue.empty() ) {
		return OPERATION_QUEUE_WAIT_RESULT_NOT_THERE;
	}

	string stWhereClause("WHERE msgid in (");
	for( vector<OperationQueueEntry>::const_iterator cit = queue.begin(); cit != queue.end(); ++cit ) {
		const OperationQueueEntry entry = *cit;
		if( cit != queue.begin() ) stWhereClause += ",";
		stWhereClause += konga::toString(entry.getMsgId());
	}
	stWhereClause += ")";

	while (true) {
		konga::MilliSleep(lPollTimeSeconds*1000);
		SmartDbPtr<MsgDb::IncomingMsgTab> pIncomingMsgTab( MsgDb::IncomingMsgTab::MakeInstance(pMsgDbConn) );
		pIncomingMsgTab->Select(stWhereClause);
		if( !pIncomingMsgTab->Next() ) return OPERATION_QUEUE_WAIT_RESULT_DONE;

		                 
		const CrDateTime now( CrDateTime::GetCurrentTime() );
		const time_t diff = now.GetTime() - startTime.GetTime();
		if ( diff > lTimeOutSeconds ) return OPERATION_QUEUE_WAIT_RESULT_TIME_OUT;
	}
}

