/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#ifdef WIN32
#include <windows.h>
#endif

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>
#include <konga/Db/DbUtils/DynamicDbRecordSet.h>

#include <konga/Db/TranDb/OperationsTab.h>
#include <konga/Db/TranDb/TransformationChainLinksTab.h>

#include <konga/DbRelations/FunctionIdMapper.h>

#include <konga/OperationLog/OperationLog.h>
#include <konga/OperationLog/OperationInstance.h>
#include <konga/OperationObjects/OperationType.h>

#include "OperationUtils.h"

using namespace konga;
using std::string;

namespace {

void LogOperation(long lOperationId, long long llOperationInstanceId, const string& sMsg_in, bool bError) {
	if ( lOperationId < 1 && llOperationInstanceId < 1 ) throw InternalErrorException("Failed to log to the operation log. Both OperationId and OperationInstanceId are invalid.");

	try {
		SmartDbPtr<DbConnection> pConn( konga::GetBackEndDbConnection("TranDb") );
		string sMsg;
		OperationLogStatus status = OperationLogStatus_Success;
		if ( bError ) {
			status = OperationLogStatus_Error;
			sMsg = "Failed to handle web service call.";
		}
		else {
			sMsg = "Successfully handled web service call.";
			if ( !sMsg_in.empty() ) {
				status = OperationLogStatus_Success_With_Info;
			}
		}

		OperationLog opLog(*pConn);
		if ( llOperationInstanceId < 1 ) {
			llOperationInstanceId = konga::NewOperationInstance(*pConn, lOperationId, "Back End", -1);
		}
		opLog.LogMessageUpdateStatus(llOperationInstanceId, status, sMsg, sMsg_in);
	}
	catch(...) {
		                  
	}
}

}                       

void konga::LogOperationSuccess(long long llOperationInstanceId, const string& sWarnings) {
	LogOperation(-1, llOperationInstanceId, sWarnings, false);
}

void konga::LogOperationError(long lOperationId, long long llOperationInstanceId, const string& sError) {
	LogOperation(lOperationId, llOperationInstanceId, sError, true);
}

namespace {
	long GetWebServiceTransformationId(DbConnection* pTranDb, const long lOperationId, string& sOperationName) {
		                             
		  
		SmartDbPtr<TranDb::OperationsTab> pOperationsTab( TranDb::OperationsTab::MakeInstance(pTranDb) );
		pOperationsTab->Select("WHERE Operation_Id = " + toString(lOperationId));
		if ( !pOperationsTab->Next() )
			throw CrException("Invalid operation id: " + toString(lOperationId));

		sOperationName = pOperationsTab->IsOperation_NameNull() ? "" : pOperationsTab->GetOperation_Name();
		const int iOperationType = pOperationsTab->IsOperation_Type_IDNull() ? -1 : pOperationsTab->GetOperation_Type_ID();
		if ( iOperationType != OPERATION_HOSTED_WEBSERVICE )
			throw CrException("The operation '" + sOperationName + "' does not implement a hosted web service.");

		long lTransformationId = -1;
		if ( pOperationsTab->IsTransformation_IDNull() ) {
			SmartDbPtr<DynamicDbRecordSet> pCommonOptsTab( DynamicDbRecordSet::MakeInstance(pTranDb) );
			pCommonOptsTab->SetSqlStatement("SELECT siblingoperation_id FROM CommonSourceOperationsTab WHERE Operation_ID = "
				+ toString(lOperationId) + " ORDER BY ID");
			pCommonOptsTab->Open();
			if ( !pCommonOptsTab->Next() )
				throw CrException("The hosted web service '" + sOperationName + "' does not have any transformations associated with it" \
				" and no sibling operation have been defined.");

			                                                                    
			const long lSiblingOperationId = toLong(pCommonOptsTab->GetFieldValue(0));

			pOperationsTab->Select("WHERE Operation_Id = " + toString(lSiblingOperationId));
			if ( !pOperationsTab->Next() )
				throw InternalErrorException("There is no sibling operation with Operation_ID = " + toString(lSiblingOperationId)
				+ " for hosted web service '" + sOperationName + "'.");

			if ( pOperationsTab->IsTransformation_IDNull() )
				throw CrException("The sibling operation does not have any transformation.");

			lTransformationId = pOperationsTab->GetTransformation_ID();
		}
		else {
			lTransformationId = pOperationsTab->GetTransformation_ID();
		}

		                                                                    
		  
		SmartDbPtr<TranDb::TransformationChainLinksTab> pTransformationChainLinksTab( TranDb::TransformationChainLinksTab::MakeInstance(pTranDb) );
		pTransformationChainLinksTab->Select("WHERE NextTransformation_ID = " + toString(lTransformationId));
		if ( !pTransformationChainLinksTab->Next() )
			throw CrException("The operation '" + sOperationName + "' does not seem to be a valid hosted web service operation.");

		return pTransformationChainLinksTab->GetTransformation_ID();
	}
}

long konga::GetWebServiceId(DbConnection* pTranDb, const long lHostedWebServiceOperationId,
							string& sOperationName)
{
	const long lWSTransformationId = GetWebServiceTransformationId(pTranDb, lHostedWebServiceOperationId, sOperationName);
	FunctionIdMapper functionIdMapper(*pTranDb);
	return functionIdMapper.GetWebServiceIdForTransformation(lWSTransformationId);
}

