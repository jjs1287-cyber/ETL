/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <konga/Utils/CrException.h>
#include <konga/Utils/StringUtils.h>	           
#include <konga/Utils/util.h>			               

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DbUtils/AutoDbCleanup.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>
#include <konga/Db/DbUtils/DbException.h>

#include <konga/Db/MsgDb/IncomingMsgTab.h>

#include <konga/DbRelations/EntityIdConstants.h>

#include <konga/DataElement/DataElement.h>

#include <konga/ItemAccess/UserInfo.h>

#include <konga/OperationLog/OperationLogStatus.h>
#include <konga/OperationLog/OperationInstance.h>

#include <konga/OperationObjects/OverrideFileTypeSourceLocation.h>	                          
#include <konga/OperationContext/OperationDataElementHolder.h>
#include <konga/OperationDataElements/OperationDataElementsManager.h>
#include <konga/OperationLog/OperationLog.h>

#include <konga/UiBackend/GuidMappings/GuidMappings.h>

#include "MessageSender.h"
#include "ProcessMessage.h"
#include "OperationMessageUtils.h"

using namespace konga;
using std::string;

long long konga::AddOperationToQueue(long lOperationId, long lSessionId, long long llParentOperationInstanceId,
								const string& stFrom, const string& stSubject, long runAsUserId)
{
	return konga::AddOperationToQueue(0, 0, lOperationId, lSessionId, llParentOperationInstanceId, stFrom, stSubject, runAsUserId);
}

string konga::GetProcessVariablesForOperation(long lOperationId) {
	return string("{Interchange;0;1;0;OperationID=" + konga::toString(lOperationId) + ";}");
}

long long konga::AddOperationToQueue(DbConnection* pMsgDbConn, DbConnection* pTranDbConn,
									 long lOperationId, long lSessionId, long long llParentOperationInstanceId,
									 const string& stFrom, const string& stSubject, long runAsUserId)
{
	if ( lOperationId < 1 )
		throw InternalErrorException("Invalid operation Id: " + toString(lOperationId));

	SmartDbPtr<DbConnection> myMsgDbConn(0);
	if ( pMsgDbConn == 0 ) {
		myMsgDbConn.Attach( pMsgDbConn = konga::GetBackEndDbConnection("MsgDb") );
	}

	SmartDbPtr<DbConnection> myTranDbConn(0);
	if ( pTranDbConn == 0 ) {
		myTranDbConn.Attach( pTranDbConn = konga::GetBackEndDbConnection("TranDb") );
	}

	AutoRollbackTransaction msgdb_trans(pMsgDbConn);
	AutoRollbackTransaction trandb_trans(pTranDbConn);

	const long long llOperationInstanceId = konga::NewOperationInstance(*pTranDbConn, lOperationId, stFrom, llParentOperationInstanceId);

	ProcessMessage message;
	message.SetProcessVariables(GetProcessVariablesForOperation(lOperationId));

	message.SetFrom(stFrom);
	message.SetTo("ProcessEngine");	
	message.SetSubject(stSubject);

	                                                         
	                                                       
	const string stOperationInstanceId(toString(llOperationInstanceId));
	message.AddDataElement("__OperationInstanceID", stOperationInstanceId, konga::DET_STRING);

	if ( lSessionId > 0 ) {
		message.AddDataElement("__SessionID", toString(lSessionId), konga::DET_INT);
	}

	if ( runAsUserId != UNDEFINED_UID ) {
		const string sUserGuid = GetGuidForId(runAsUserId, UserEntityId);
		if ( IsGUIDString(sUserGuid) )
			message.AddDataElement("__UserID", sUserGuid, konga::DET_STRING);
		else
			CrException("The requested user is not defined on this server (uid=" + toString(runAsUserId) + ").");
	}

	MessageSender sender(pMsgDbConn);
	const long long llMsgId = sender.AddMessageToQueue(message, false);

	konga::UpdateOperationInstance(*pTranDbConn, llOperationInstanceId, llMsgId);

	                                   
	OperationLog opLog(*pTranDbConn);
	opLog.CreatePendingEntry(lOperationId, llOperationInstanceId);

	trandb_trans.Commit();
	msgdb_trans.Commit();

	return llOperationInstanceId;
}

long long konga::AddOperationToQueueOverrideSource(long lOperationId, const string& stFrom, const string& stSubject,
											  const string& stSourceOverrideFileName, bool bFileIsExclusive,
											  OperationDataElementHolder& deHolder, long runAsUserId)
{
	UserInfo user(runAsUserId);
	DataElement* de = deHolder.getOperationDataElement(GLOBAL_DE_NAME_FOR_SOURCE_FILE_NAME);
	de->setType(DataElement::t_string);
	de->setValue(stSourceOverrideFileName.c_str());

	de = deHolder.getOperationDataElement(GLOBAL_DE_NAME_FOR_SOURCE_FILE_NAME_IS_EXCLUSIVE);
	de->setType(DataElement::t_bool);
	de->setTypedValue(bFileIsExclusive);

	OperationDataElementsManager mgr;
	const long lSessionId = mgr.AddDataElements(deHolder);
	return konga::AddOperationToQueue(lOperationId, lSessionId, UNDEFINED_OPERATION_INSTANCE_ID, stFrom, stSubject);
}

bool konga::IsOperationInQueue(long lOperationId, const std::string& sFrom,
							   DbConnection* pMsgDb) {

	try {

		SmartDbPtr<DbConnection> adopt_pMsgDb;
		if ( pMsgDb == 0 ) {
			adopt_pMsgDb.Attach(
				pMsgDb = konga::GetBackEndDbConnection("MsgDb")
			);
		}

		SmartDbPtr<MsgDb::IncomingMsgTab> pIncomingMsgTab(
			MsgDb::IncomingMsgTab::MakeInstance(pMsgDb)
			);

		string sProcessVariables(GetProcessVariablesForOperation(lOperationId));
		ToLower(sProcessVariables);
		string sqlWhere(" WHERE LOWER(ProcessVariables) = '" + sProcessVariables + "'");
		if ( !sFrom.empty() ) {
			string sFrom_lower(sFrom);
			ToLower(sFrom_lower);
			sqlWhere += " AND LOWER(MsgFrom)='" + SqlEscape(sFrom_lower) + "'";
		}

		pIncomingMsgTab->Select(sqlWhere);
		return pIncomingMsgTab->Next();
	}
	catch( const DbException& ex ) {
		throw CrException("Failed to check if operation is already in the queue. Reason:\n"
			+ ex.GetErrorString());
	}
}
