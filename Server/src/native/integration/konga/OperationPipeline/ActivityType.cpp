/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <konga/Utils/StringUtils.h>	           
#include <konga/Utils/CrException.h>

#include <konga/UiBackend/GuidMappings/GuidToIdMapper.h>

#include <konga/DbRelations/EntityIdConstants.h>

#include "ActivityType.h"

using namespace konga;
using std::string;

int konga::getEntityTypeId(ActivityType activityType) {
	switch( activityType ) {
		case ActivityType_XsltTransform: return XsltTransformEntityId;
        case ActivityType_Script: return ScriptEntityId;
		case ActivityType_Source:
			return SourceEntityId;
		case ActivityType_Target:
			return TargetEntityId;
		case ActivityType_Transformation:
			return TransformationEntityId;
		case ActivityType_WebServiceCall:
			return WebServiceEntityId;
		case ActivityType_BulkLoad:
			return SalesforceBulkLoadEntityId;
		default:
			return InvalidEntityId;
	}
}

std::string konga::getActivityTypeAsString(ActivityType activityType) {
	switch( activityType ) {
		case ActivityType_XsltTransform: return "XSLT Transform";
        case ActivityType_Script: return "Script";
		case ActivityType_Plugin:
			return "Plugin";
		case ActivityType_Source:
			return "Source";
		case ActivityType_Target:
			return "Target";
		case ActivityType_Transformation:
			return "Transformation";
		case ActivityType_WebServiceCall:
			return "Web Service Method";
		case ActivityType_BulkLoad:
			return "Salesforce Bulk Load";
		default:
			return string("Invalid (" + konga::toString(activityType) + ")");
	}
}

ActivityType konga::getActivityTypeFromClientActivityType(int i) {
	if ( i < 1 || i > 8) {
		throw InternalErrorException("Unknown client activity type sent by the client.");
	}
	return static_cast<ActivityType>(i);
}

long konga::getActivityId(const std::string& activity_guid, ActivityType activityType, GuidToIdMapper& guidToIdMapper) {

	switch(activityType) {
		case ActivityType_Invalid:
			throw InternalErrorException("It is not possible to get the activity id for an invalid activity type.");
		case ActivityType_Plugin:
			throw InternalErrorException("Plugin activities have not been implemented yet.");
		default:
			{
				const int entityTypeId = getEntityTypeId(activityType);
				const long activity_id = guidToIdMapper.GetId(activity_guid, entityTypeId);
				if ( activity_id == GuidToIdMapper::invalid_id )
					throw InternalErrorException("There is no activity with GUID = '" + activity_guid
					+ "' and activity type " + toString(activityType) + " (" + konga::getActivityTypeAsString(activityType) + ").");
				return activity_id;
			}
	};

	throw InternalErrorException("konga::getActivityId has not been implemented for activity type " + toString(activityType));
}

