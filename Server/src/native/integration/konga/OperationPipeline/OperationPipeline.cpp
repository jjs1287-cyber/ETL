/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */

#include <memory>

#include <konga/Utils/StringUtils.h>	           

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/DynamicDbRecordSet.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>

#include "OperationPipeline.h"

using namespace konga;
using std::string;
using std::vector;

namespace {
	const string TABLE_NAME("OperationPipelineActivitiesTab");
	const string COLUMN_NAMES("Operation_ID, ActivityType_ID, Activity_ID");
	const string ORDER_BY_COLUMN("OperationActivity_ID");
}

OperationPipeline* OperationPipeline::getOperationPipeline(long operationId, DbConnection& dbConnection) {

	const string sql("SELECT " + COLUMN_NAMES + " FROM " + TABLE_NAME
		+ " WHERE Operation_ID = " + toString(operationId)
		+ " ORDER BY " + ORDER_BY_COLUMN);
	SmartDbPtr<DynamicDbRecordSet> pActivitiesTab( DynamicDbRecordSet::MakeInstance(&dbConnection, sql) );
	pActivitiesTab->Open();
	std::auto_ptr<OperationPipeline> op( new OperationPipeline() );
	while( pActivitiesTab->Next() ) {
		ActivityType type = static_cast<ActivityType>( atoi(pActivitiesTab->GetFieldValue(1).c_str()) );
		const long id = atol( pActivitiesTab->GetFieldValue(2).c_str() );
		op->m_pipeline.push_back( OperationActivity(type, id) );
	}
	return op.release();
}

void OperationPipeline::setOperationPipeline(long operationId, const vector<OperationActivity>& activities, DbConnection& dbConnection) {
	const string& sOperationId = toString(operationId);
	dbConnection.ExecuteSql("DELETE FROM " + TABLE_NAME + " WHERE Operation_ID = " + sOperationId);

	for( vector<OperationActivity>::const_iterator cit = activities.begin(); cit != activities.end(); ++cit ) {
		const OperationActivity& activity = *cit;
		dbConnection.ExecuteSql("INSERT INTO " + TABLE_NAME + " (" + COLUMN_NAMES + ") " \
			"VALUES(" + sOperationId + ", " + toString(activity.getActivityType())
			+ ", " + toString(activity.getActivityId()) + ")");
	}
}

bool OperationPipeline::empty() const {
	return m_pipeline.empty();
}

OperationActivity OperationPipeline::getFirstActivity() const {
	if ( empty() )
		throw CrException("This operation pipeline has not activities.");
	m_index = 0;
	return m_pipeline.at(m_index);
}

bool OperationPipeline::hasMoreActivities() const {
	return ( m_index <  m_pipeline.size() - 1 );
}

OperationActivity OperationPipeline::getNextActivity() const {
	if ( !hasMoreActivities() )
		throw CrException("This operation pipeline has no more activities.");
	return m_pipeline.at(++m_index);
}

