/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */

#include <string>
#include <iostream>

#include <konga/Utils/StringUtils.h>
#include <konga/Utils/DBConnectionInfo.h>

#include <konga/Db/DbUtils/SmartDbPtr.h>
#include <konga/Db/DbUtils/AutoDbCleanup.h>
#include <konga/Db/TranDb/OperationsTab.h>

#include <konga/DbRelations/EntityIdConstants.h>

#include <konga/Messaging/MessageSender.h>
#include <konga/Messaging/ProcessMessage.h>
#include <konga/Messaging/OperationMessageUtils.h>

#include <konga/UiBackend/GuidMappings/DbGuidToIdMapper.h>


#include "ETLForgeUtilsOperation.h"

using namespace konga;
using std::string;
using std::cout;
using std::endl;

namespace {
	const long noOperations = -9999;

	void AddOperationToQueueImpl(long operationId, DbConnection* pMsgDbConn = 0) {

		                                                                                           
		                                                 
		if ( operationId < 1 )
			return;

		ProcessMessage message;
		message.SetProcessVariables(GetProcessVariablesForOperation(operationId));

		message.SetFrom("ETLForgeUtils");
		message.SetTo("ProcessEngine");	
		message.SetSubject("Added by ETLForgeUtils");

		MessageSender sender(pMsgDbConn);
		sender.AddMessageToQueue(message, pMsgDbConn==0);
	}
}

void konga::AddScheduledOperationsToQueue(bool bQuiet) {
	SmartDbPtr<DbConnection> pTranDbConn(DbConnection::MakeInstance(
		DBConnectionInfo("TranDb").connectionString())
		);

	using namespace TranDb;
	SmartDbPtr<OperationsTab> operationsTab(
		OperationsTab::MakeInstance(pTranDbConn)
		);

	SmartDbPtr<DbConnection> pMsgDbConn(DbConnection::MakeInstance(
		DBConnectionInfo("MsgDb").connectionString())
		);
	AutoRollbackTransaction msgDbTransaction(pMsgDbConn);

	operationsTab->Select("WHERE Schedule_Id IS NOT NULL");
	long operationId = noOperations;
	string msg;
	while( operationsTab->Next() ) {
		operationId = operationsTab->GetOperation_ID();
		const string sOperationName(
			operationsTab->IsOperation_NameNull() ? "":
			operationsTab->GetOperation_Name()
			);
		AddOperationToQueueImpl(operationId, pMsgDbConn);
		msg += "Added operation \"" + sOperationName
			+ "\" to the process queue\n";
	}

	msgDbTransaction.Commit();
	if ( !msg.empty() && !bQuiet ) {
		cout << msg;
	}

	if ( operationId == noOperations )
		cout << "No scheduled operations have been defined" << endl;
}

namespace {
	string GetOperationName(TranDb::OperationsTab& operationsTab) {
		return string (
			operationsTab.IsOperation_NameNull()?"":
			operationsTab.GetOperation_Name()
			);
	}
}

void konga::AddOperationToQueue(const string& operation, bool bQuiet) {
	                                
	                   
	                   
	                 

	SmartDbPtr<DbConnection> tranDbConn(DbConnection::MakeInstance(
		DBConnectionInfo("TranDb").connectionString())
		);

	using namespace TranDb;
	SmartDbPtr<OperationsTab> operationsTab(
		OperationsTab::MakeInstance(tranDbConn)
		);

	operationsTab->Select("WHERE Operation_Name = '" + operation + "'");
	if ( operationsTab->Next() ) {
		long operationId = operationsTab->GetOperation_ID();
		AddOperationToQueueImpl(operationId);
		if ( !bQuiet ) {
			cout << "Added operation \"" << operation
				<< "\" to the process queue" << endl;
		}
		return;
	}

	                                                        
	DbGuidToIdMapper guidMapper(tranDbConn);
	long operationId = guidMapper.GetId(operation, OperationEntityId);
	if ( operationId == GuidToIdMapper::invalid_id ) {
		                                    
		try {
			operationId = toLong(operation);
		}
		catch( const CrException& ) {
			                                           
		}
	}

	if ( operationId != GuidToIdMapper::invalid_id ) {
		operationsTab->Select("WHERE Operation_Id = " + toString(operationId));
		if ( operationsTab->Next() ) {
			AddOperationToQueueImpl(operationId);
			if ( !bQuiet ) {
				cout << "Added operation \"" << GetOperationName(*operationsTab)
					<< "\" to the process queue" << endl;
			}
			return;
		}
	}

	                              
	throw CrException("Failed to find the operation \"" + operation
		+ "\". Specify the operation name, operation GUID or the numerical operation Id.");
}

