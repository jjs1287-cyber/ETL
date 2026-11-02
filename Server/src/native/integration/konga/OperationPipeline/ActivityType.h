/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef KONGA_OPERATION_PIPELINE_ACTIVITY_TYPE_H
#define KONGA_OPERATION_PIPELINE_ACTIVITY_TYPE_H

#include <string>

namespace konga {

	class DbConnection;
	class GuidToIdMapper;

	enum ActivityType {
		ActivityType_Invalid = 0,
		ActivityType_Script = 1,
		ActivityType_Plugin = 2,
		ActivityType_Source = 3,
		ActivityType_Target = 4,
		ActivityType_Transformation = 5,
		ActivityType_WebServiceCall = 6,
		ActivityType_XsltTransform = 8,
        ActivityType_BulkLoad = 7
	};

	ActivityType getActivityTypeFromClientActivityType(int i);

	std::string getActivityTypeAsString(ActivityType activityType);

	long getActivityId(const std::string& activity_guid, ActivityType activityType, GuidToIdMapper& guidToIdMapper);

	                                                                                     
	int getEntityTypeId(ActivityType activityType);
}

#endif                                            

