/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <memory>
#include <cassert>

#include <konga/Utils/Settings.h>

#include <konga/Crypto/Crypto.h>

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/DbException.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>

#include <konga/Db/TranDb/OperationsTab.h>
#include <konga/Db/TranDb/SourcesTab.h>
#include <konga/Db/TranDb/TargetsTab.h>

#include <konga/DbRelations/TransformationTypeIdConstants.h>
#include <konga/DbRelations/SourceTypeIdConstants.h>
#include <konga/DbRelations/EntityIdConstants.h>

#include <konga/ItemProperties/ItemProperties.h>

#include <konga/FileTransfer/HttpVerbEnum.h>
#include <konga/OperationContext/OperationContext.h>

#include <konga/DataElement/DataElement.h>

#include <konga/OperationContext/OperationDataElementHolder.h>
#include <konga/OperationContext/OperationContext.h>

#include <konga/OperationObjects/XsltOperation.h>
#include <konga/OperationObjects/ArchiveOperation.h>
#include <konga/OperationObjects/ScriptOperation.h>
#include <konga/OperationObjects/SalesforceBulkLoadOperation.h>

#include <konga/OperationObjects/Transformation.h>
#include <konga/OperationObjects/WebServiceTransformation.h>
#include <konga/OperationObjects/OperationEngineException.h>
#include <konga/OperationObjects/SourceDataLocation.h>
#include <konga/OperationObjects/TargetDataLocation.h>

#include "TransformationOperation.h"
#include "OperationCreatorDb.h"


namespace konga { void PrepareSalesforceOperationSession(long,DbConnection&,OperationDataElementHolder&,const UserInfo&); }
using namespace konga;
using namespace std;

Operation* OperationCreatorDb::getOperation(long lOperationId, DbConnection& dbConnection,
												  OperationDataElementHolder& deHolder,
												  OperationContext& context,
												  PipelinePluginSelector* pPluginSelector,
												  bool bDeep)
{
	try {
		
		SmartDbPtr<TranDb::OperationsTab> pOperationTab( TranDb::OperationsTab::MakeInstance(&dbConnection) );
		const string stWhere("WHERE Operation_ID = " + toString(lOperationId));
		pOperationTab->Select(stWhere);

		if ( !pOperationTab->Next() ) {
			stringstream sErr;
			sErr << "Failed to get the Operation with OperationID = " << lOperationId << ". No Operation with that ID exists. Has the operation been deployed on this server?";
			throw OperationEngineException(sErr.str(), lOperationId);
		}
		
		OperationType ot = static_cast<OperationType>(pOperationTab->GetOperation_Type_ID());
		auto_ptr<Operation> pOperation;
		bool bArchive = false;
		bool bTran = false;
		bool bWsCall = false;
		if (ot == OPERATION_XSLT) { pOperation.reset(new XsltOperation()); bArchive = true; }
        else if ( ot == OPERATION_ARCHIVE) {
			pOperation.reset( new ArchiveOperation() );
			bArchive = true;
		}
		else if ( ot == OPERATION_TRANSFORMATION ) {
			pOperation.reset( new TransformationOperation() );
			bTran = true;
		}
		else if ( ot == OPERATION_WEBSERVICE_CALL ||
			      ot == OPERATION_HOSTED_WEBSERVICE ) 
		{
			                                                     
			pOperation.reset( new TransformationOperation() );
			bWsCall = true;
		}
		else if ( ot == OPERATION_SCRIPT ) {
			pOperation.reset( new ScriptOperation() );
		}
		else if ( ot == OPERATION_SALESFORCE_BULK_LOAD ) {
			pOperation.reset( SalesforceBulkLoadOperation::create(lOperationId, context.getOperationInstanceId(), dbConnection) );
		}
		else {
			stringstream sErr;
			sErr << "Failed to create Operation with OperationId = " << lOperationId;
			sErr << ". The OperationType " << ot << " is unknown.";
			throw OperationEngineException(sErr.str());
		}

		if (bDeep && (ot == OPERATION_WEBSERVICE_CALL || ot == OPERATION_SALESFORCE_BULK_LOAD)) PrepareSalesforceOperationSession(lOperationId, dbConnection, deHolder, context.getCurrentUser());
		pOperation->m_OperationData.m_lOperationID = lOperationId;
		pOperation->m_OperationData.m_OperationType = ot;
		pOperation->m_OperationData.m_stOperationName = pOperationTab->GetOperation_Name();
		pOperation->m_OperationData.m_lSuccessOperationID = pOperationTab->IsSuccess_Operation_IDNull() ? 0 : pOperationTab->GetSuccess_Operation_ID();
		pOperation->m_OperationData.m_lFailureOperationID = pOperationTab->IsFailure_Operation_IDNull() ? 0 : pOperationTab->GetFailure_Operation_ID();
		pOperation->m_OperationData.m_lSuccessMessageID = pOperationTab->IsSuccess_Message_IDNull() ? 0 : pOperationTab->GetSuccess_Message_ID();
		pOperation->m_OperationData.m_lFailureMessageID = pOperationTab->IsFailure_Message_IDNull() ? 0 : pOperationTab->GetFailure_Message_ID();

		pOperation->m_OperationData.m_bIgnoreHistory = pOperationTab->IsIgnore_HistoryNull() ? false : pOperationTab->GetIgnore_History() != 0;
		pOperation->m_OperationData.m_bDiff = pOperationTab->IsDIFfNull() ? false : pOperationTab->GetDIFf() != 0;
		pOperation->m_OperationData.m_bTrace = pOperationTab->IsTraceNull() ? false : pOperationTab->GetTrace() != 0;

		ItemProperties itemProperties(&dbConnection);
		itemProperties.GetItemProperties(OperationEntityId, lOperationId, pOperation->m_extendedProperties);

		const long lSourceId = pOperationTab->IsSource_IDNull() ? -1 : pOperationTab->GetSource_ID();
		pOperation->m_OperationData.m_lSourceId = lSourceId;
		if ( bDeep )
			pOperation->m_pSource.reset( getSourceLocation(lSourceId, dbConnection, deHolder, pPluginSelector, true) );

		if ( bTran || bWsCall ) {
			const long lTransformationId = pOperationTab->IsTransformation_IDNull() ? 0 : pOperationTab->GetTransformation_ID();
			pOperation->m_OperationData.m_lTransformationId = lTransformationId;
			if ( lTransformationId > 0 && bDeep ) {
				pOperation->m_pTransformation.reset( getTransformation(lTransformationId, dbConnection, deHolder, pPluginSelector) );
			}
		}

		const long lTargetId = pOperationTab->IsTarget_IDNull() ? -1 : pOperationTab->GetTarget_ID();
		pOperation->m_OperationData.m_lTargetId = lTargetId;
		if ( bDeep )
			pOperation->m_pTarget.reset( getTargetLocation(lTargetId, dbConnection, deHolder, pPluginSelector, pOperation->getTransformation()) );

		return pOperation.release();
	}
	catch ( const DbException& ex ) {
		stringstream sErr;
		sErr << "Failed to get the operation with OperationID = " << lOperationId << "." << endl;
		sErr << "A database exception occurred. The reported error was:" << endl << ex.GetErrorString();
		throw OperationEngineException(sErr.str(), lOperationId);
	}
}
