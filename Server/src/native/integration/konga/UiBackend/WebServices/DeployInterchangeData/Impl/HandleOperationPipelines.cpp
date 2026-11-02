/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */

#include <cassert>

#include <konga/Utils/CrException.h>

#include <konga/DbRelations/EntityIdConstants.h>
#include <konga/UiBackend/GuidMappings/GuidToIdMapper.h>

#include <konga/OperationPipeline/OperationPipeline.h>

#include "InterchangeDataStructs.h"
#include "DeployInterchangeDataImpl.h"

using namespace konga;
using namespace konga::interchange_deploy;

void DeployInterchangeDataImpl::HandleOperationPipelines(const InterchangeDataTable* const pOperationPipelines) {
	assert(pOperationPipelines);
	if ( pOperationPipelines == 0 ) return;

	InterchangeDataTableStruct operationPipelines;
	FillInterchangeDataTableStruct(*pOperationPipelines, operationPipelines);

	long currentOperation = -123456789;
	vector<OperationActivity> activities;
	vInterchangeDataRowStructT::const_iterator cit = operationPipelines.m_vRows.begin();
	for(; cit != operationPipelines.m_vRows.end(); ++cit ) {
		if ( cit->m_vFields.size() != 3 )
			continue;
		if ( cit->m_vFields.at(0) == 0 ||
			cit->m_vFields.at(1) == 0 ||
			cit->m_vFields.at(2) == 0 )
			continue;

		const string& operation_guid = cit->m_vFields.at(0).str();
		const long operationId = m_pGuidToIdMapper->GetId(operation_guid, OperationEntityId);
		if ( operationId == GuidToIdMapper::invalid_id )
			throw InternalErrorException("The operation with GUID '" + operation_guid + "' has not been deployed and " \
			"the operation pipeline for this operation should not have been included by the client in the deploy.");

		if ( operationId != currentOperation ) {
			if ( currentOperation > 0 ) {
				OperationPipeline::setOperationPipeline(currentOperation, activities, *m_pTranDb);
				activities.clear();
			}
			currentOperation = operationId;
		}

		const int activityTypeId = atoi(cit->m_vFields.at(1).str().c_str());
		ActivityType activityType = konga::getActivityTypeFromClientActivityType(activityTypeId);

		const string& activity_guid = cit->m_vFields.at(2).str();
		long activityId = konga::getActivityId(activity_guid, activityType, *m_pGuidToIdMapper);

		activities.push_back( OperationActivity(activityType, activityId) );
	}
	if ( currentOperation > 0 ) {
		OperationPipeline::setOperationPipeline(currentOperation, activities, *m_pTranDb);
	}
}

