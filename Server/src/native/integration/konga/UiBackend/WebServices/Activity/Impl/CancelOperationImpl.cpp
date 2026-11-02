/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <konga/Utils/Settings.h>
#include <konga/Utils/ProcessUtils.h>	             

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/DynamicDbRecordSet.h>
#include <konga/Db/DbUtils/AutoDbCleanup.h>
#include <konga/Db/DbUtils/DbUtils.h>

#include <konga/Db/MsgDb/IncomingMsgTab.h>
#include <konga/Db/MsgDb/DataElementTab.h>

#include <konga/Networking/BasicCommands.h>

#include <konga/ProcessEngine/ComponentInfo.h>

#include <konga/OperationLog/OperationLog.h>

#include <konga/UiBackend/WebServices/Utils/UiBackendLogging.h>
#include <konga/UiBackend/WebServices/Utils/WsAllocations.h>

#include "ActivityInfoProviderImpl.h"

using namespace konga;
using namespace MsgDb;
using std::string;
using std::vector;
using std::map;

namespace {
	void ValidateMessagePermission(const vector<long>& msgIds, vector<long>& allowed, vector<long>& notAllowed,
		DbConnection& msgDbConn)
	{
		for( vector<long>::const_iterator cit = msgIds.begin(); cit != msgIds.end(); ++cit) {
			const long msgId = *cit;

		}
	}

	void AddUniqueId(vector<long long>& v, long long id) {
		if ( std::find(v.begin(), v.end(), id) != v.end() ) return;
		v.push_back(id);
	}

	void AddUniqueIds(vector<long long>& target, const vector<long long>& source) {
		for( vector<long long>::const_iterator cit = source.begin(); cit != source.end(); ++cit ) {
			AddUniqueId(target, *cit);
		}
	}

	                                                                                                                              
	void GetMsgsToCancel(const vector<long long>& operationInstanceIds, vector<long long>& parentOperationInstanceIds, vector<long long>& msgIds, DbConnection& tranDbConn) {
		                                                          
		string msgIdSql("SELECT msgid, parent_operation_instance_id FROM operationinstancetab WHERE operation_instance_id IN (");
		for( vector<long long>::const_iterator cit = operationInstanceIds.begin(); cit != operationInstanceIds.end(); ++cit ) {
			if ( cit != operationInstanceIds.begin() ) {
				msgIdSql.append(1, ',');
			}
			msgIdSql.append( toString(*cit) );
		}
		msgIdSql.append(1, ')');

		SmartDbPtr<DynamicDbRecordSet> rsMsgIds( DynamicDbRecordSet::MakeInstance(&tranDbConn, msgIdSql) );
		rsMsgIds->Open();

		vector<long long> parentInstances_local;
		while( rsMsgIds->Next() ) {
			const long long msgId = toLongLong( rsMsgIds->GetFieldValue(0) );
			if ( msgId > 0 ) {
				AddUniqueId(msgIds, msgId);
			}
			else {
				const long long parent_operation_instance_id = toLongLong( rsMsgIds->GetFieldValue(1) );
				if ( parent_operation_instance_id > 0 ) {
					parentInstances_local.push_back(parent_operation_instance_id);
				}
			}
		}
		rsMsgIds.CloseAndDelete();

		if ( !parentInstances_local.empty() ) {
			GetMsgsToCancel(parentInstances_local, parentOperationInstanceIds, msgIds, tranDbConn);
			AddUniqueIds(parentOperationInstanceIds, parentInstances_local);
		}
	}

	string toCommaSeparatedList(const vector<long long>& v) {
		string s;
		for( vector<long long>::const_iterator cit = v.begin(); cit != v.end(); ++cit ) {
			if ( cit != v.begin() ) s.append(1, ',');
			s.append(toString(*cit));
		}
		return s;
	}

	void DeleteFromIncomingMsgTab(const vector<long long>& msgs, DbConnection& msgDbConn) {
		if (msgs.empty() ) return;
		const string& sMsgToDelete = toCommaSeparatedList(msgs);

		const string stInClause2(" WHERE MsgId IN (" + sMsgToDelete + ")");
		msgDbConn.ExecuteSql("DELETE FROM IncomingMsgTab" + stInClause2);
		msgDbConn.ExecuteSql("DELETE FROM DataElementTab" + stInClause2);
		msgDbConn.ExecuteSql("DELETE FROM DataElementTextTab" + stInClause2);
	}

	                                                                                              
	bool CancelPendingOperationsDbTransaction(const string& sql, DbConnection& tranDbConn, DbConnection& msgDbConn) {
		try {
			AutoRollbackTransaction msgDbTran(&msgDbConn);
			AutoRollbackTransaction tranDbTran(&tranDbConn);
			msgDbConn.SetTransactionIsolationLevel(SERIALIZABLE);
			tranDbConn.SetTransactionIsolationLevel(SERIALIZABLE);

			SmartDbPtr<DynamicDbRecordSet> msgTab( DynamicDbRecordSet::MakeInstance(&msgDbConn, sql) );
			msgTab->Open();
			vector<long long> msgsNotExec;
			while ( msgTab->Next() ) {
				const string& stMsgId = msgTab->GetFieldValue(0);
				msgsNotExec.push_back( konga::toLongLong(stMsgId) );
			}
			msgTab.CloseAndDelete();

			if ( !msgsNotExec.empty() ) {
				DeleteFromIncomingMsgTab(msgsNotExec, msgDbConn);

				OperationLog opLog(tranDbConn);
				for( vector<long long>::const_iterator cit = msgsNotExec.begin(); cit != msgsNotExec.end(); ++cit ) {
					const long long thisMsgId = *cit;
					opLog.OperationMsgCancelled(thisMsgId);
				}
			}

			msgDbTran.Commit();
			tranDbTran.Commit();
			return true;
		}
		catch( const DbException& ex ) {
			if ( konga::IsSerializationFailure(ex) ) {
				return false;
			}
			else {
				throw;
			}
		}
	}

	void CancelPendingOperations(const string& stInClause, DbConnection& tranDbConn, DbConnection& msgDbConn) {
		string sql("SELECT MsgId FROM IncomingMsgTab" + stInClause + " AND isexecuting = 0");
		while( !CancelPendingOperationsDbTransaction(sql, tranDbConn, msgDbConn) ) {}
	}

	void Cancel(const vector<long long>& operationInstances, DbConnection& tranDbConn) {
		OperationLog opLog(tranDbConn);
		for( vector<long long>::const_iterator cit = operationInstances.begin(); cit != operationInstances.end(); ++cit ) {
			opLog.OperationCancelled(*cit);
		}
	}
}

void ActivityInfoProviderImpl::CancelOperationQueueImpl(const vector<long long>& operationInstanceIds, DbConnection& tranDbConn, DbConnection& msgDbConn) {
	if ( operationInstanceIds.empty() ) return;

	                                            
	vector<long long> msgIds;
	vector<long long> parentOperationInstanceIds;
	GetMsgsToCancel(operationInstanceIds, parentOperationInstanceIds, msgIds, tranDbConn);
	if ( msgIds.empty() ) {
		return;
	}

	string stMsgList;	                                                                                   
	for( vector<long long>::const_iterator cit = msgIds.begin(); cit != msgIds.end(); ++cit) {
		if ( !stMsgList.empty() ) stMsgList += ",";
		stMsgList += toString( *cit );
	}

	                                                     
	const string stInClause(" WHERE MsgID IN (" + stMsgList + ")");
	CancelPendingOperations(stInClause, tranDbConn, msgDbConn);

	                                                       
	const string zero("0");
	string sql( "SELECT COUNT(*) FROM incomingmsgtab" + stInClause );
	SmartDbPtr<DynamicDbRecordSet> msgTab( DynamicDbRecordSet::MakeInstance(&msgDbConn, sql) );
	msgTab->Open();
	if ( !msgTab->Next() ) return;	                                                 
	if ( msgTab->GetFieldValue(0) == zero ) {
		                                                       
		Cancel(operationInstanceIds, tranDbConn);
		Cancel(parentOperationInstanceIds, tranDbConn);
		return;
	}
	msgTab.CloseAndDelete();

	                                                      
	  
	const long lPort = konga::GetOptionAsInt(stPROCESSENGINE_COMPONENT_NAME,
		"CommandPort", lPROCESSENGINE_DEFAULT_PORT);
	if ( lPort < 0 ) {
		throw CrException("Server Configuration Error: " + 
			stPROCESSENGINE_COMPONENT_NAME + "/CommandPort is negative");
	}

	bool bKillTasksFailed = false;
	try {
		vector<string> vArgs;	                            
		for(vector<long long>::const_iterator cit = msgIds.begin(); cit != msgIds.end(); ++cit ) { vArgs.push_back( toString(*cit) ); }
		networking::GenericCmdWithArgs("KillTasks", vArgs, m_UserInfo.GetLogin(), m_sPassword,
			static_cast<unsigned long>(lPort), stPROCESSENGINE_COMPONENT_NAME);
	}
	catch( const CrException& ex) {
		const string stErr("Failed to tell the process engine to cancel running jobs. Probably the engine is not running.\n" \
			"Check if the process engine is running. If it is not running then start it and refresh the queue." \
			"The reported error was: " + ex.GetErrorString());
		UIBACKEND_TRACE(stErr);
		bKillTasksFailed = true;
	}

	if ( bKillTasksFailed ) {
		                                                                            
		DeleteFromIncomingMsgTab(msgIds, msgDbConn);
	}

	konga::auditLog(m_UserInfo.GetLogin() + " cancelled the following messages: " + stMsgList);

	                                                     
	sql = "SELECT COUNT(*) FROM incomingmsgtab" + stInClause;
	for( int i = 0; i < 10; ++i ) {
		if ( i > 0 ) konga::MilliSleep(1000);

		msgTab.Attach( DynamicDbRecordSet::MakeInstance(&msgDbConn, sql) );
		msgTab->Open();
		if (msgTab->Next() ) {
			if ( msgTab->GetFieldValue(0) == zero ) break;
		}
		else {
			                            
			assert(false);
			break;
		}
	}

	                                     
	Cancel(operationInstanceIds, tranDbConn);
	Cancel(parentOperationInstanceIds, tranDbConn);
}

namespace {
	void GetEntireQueue(vector<long long>& vArgs, DbConnection& pMsgDbConn) {
		SmartDbPtr<IncomingMsgTab> pIncomingMsgTab( IncomingMsgTab::MakeInstance(&pMsgDbConn) );
		pIncomingMsgTab->Select();
		while(pIncomingMsgTab->Next()) {
			vArgs.push_back( pIncomingMsgTab->GetMsgId() );
		}
	}
}

xsd__long_Array* ActivityInfoProviderImpl::cancelOperationQueue(xsd__long_Array entryIDs)
{
	SmartDbPtr<DbConnection> pMsgDbConn( konga::GetBackEndDbConnection("MsgDb") );
	SmartDbPtr<DbConnection> pTranDbConn( konga::GetBackEndDbConnection("TranDb") );

	int entryIdSize = 0;
	const xsd__long** entryIdArr = entryIDs.get(entryIdSize);
	if ( entryIdSize > 0 ) {

		                                                             
		vector<long long> vArgs;
		for(int i=0,n=entryIdSize;i<n;i++){
			const long long operationInstanceId = *entryIdArr[i];
			if ( i == 0 && operationInstanceId == -1 ) {
				                                                                                           
				if ( !m_UserInfo.IsAdministrator() ) {
					throw CrException("Only administrators can delete all the entries in the queue.");
				}
				throw CrException("Deleting all operations not implemented yet.");
                                         
				break;
			}
			else if ( operationInstanceId < 1 ) {
				continue;
			}

			vArgs.push_back(operationInstanceId);
		}

		CancelOperationQueueImpl(vArgs, *pTranDbConn, *pMsgDbConn);
	}

	                                          
	return allocate_xsd__long_Array(vector<long>());
}

