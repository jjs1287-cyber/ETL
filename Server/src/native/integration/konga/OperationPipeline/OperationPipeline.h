/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
#ifndef KONGA_OPERATION_PIPELINE_H
#define KONGA_OPERATION_PIPELINE_H

#include <vector>

#include "ActivityType.h"

namespace konga {

	class DbConnection;

	class OperationActivity {
	public:
		OperationActivity(ActivityType activityType, long activityId) :
		  m_ActivityType(activityType), m_lActivityId(activityId) {}

		~OperationActivity(void) {}

		ActivityType getActivityType() const {
			return m_ActivityType;
		}

		long getActivityId() const {
			return m_lActivityId;
		}

	private:
		ActivityType m_ActivityType;
		long m_lActivityId;
	};

	class OperationPipeline {
	public:
		                                                                    
		static OperationPipeline* getOperationPipeline(long operationId, DbConnection& dbConnection);

		                                                                 
		                                                 
		static void setOperationPipeline(long operationId, const std::vector<OperationActivity>& activities, DbConnection& dbConnection);

	public:
		~OperationPipeline(void) {}

		bool empty() const;

		OperationActivity getFirstActivity() const;
		bool hasMoreActivities() const;
		OperationActivity getNextActivity() const;

	private:
		OperationPipeline(void) : m_index(0) {}

	private:
		mutable std::vector<OperationActivity>::size_type m_index;
		std::vector<OperationActivity> m_pipeline;
	};
}

#endif                              

