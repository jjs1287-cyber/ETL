/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */

#include <konga/Utils/CrException.h>

#include <konga/DbRelations/EntityIdConstants.h>
#include <konga/Db/DbUtils/DbConnection.h>

#include <konga/ItemProperties/ItemProperties.h>
#include <konga/ItemProperties/Definitions.h>

#include "UpdateScheduler.h"
#include "EnableDisableSchedules.h"

using namespace konga;
using std::vector;

void konga::SetSchedulesEnabled(const vector<long>& scheduleIds, const bool bEnabled, DbConnection& dbConnection) {
	if ( scheduleIds.empty() )
		return;

	ItemProperties itemProps(&dbConnection);
	bool bSchedulerNeedsUpdate = false;
	for( vector<long>::const_iterator cit = scheduleIds.begin(); cit != scheduleIds.end(); ++cit ) {
		if ( itemProps.Update(ScheduleEntityId, *cit, SCHEDULE_ENABLED, bEnabled ? "1" : "0") ) {
			bSchedulerNeedsUpdate = true;
		}
	}

	                                                                         
	dbConnection.Commit();
	try {
		if ( bSchedulerNeedsUpdate ) UpdateScheduler();
	}
	catch( const CrException & ) {
		                                                                                       
	}
}

void konga::SetOperationSchedulesEnabled(const vector<long>& operationIds, const bool bEnabled, DbConnection& dbConnection) {
	if ( operationIds.empty() )
		return;

	ItemProperties itemProps(&dbConnection);
	bool bSchedulerNeedsUpdate = false;
	for( vector<long>::const_iterator cit = operationIds.begin(); cit != operationIds.end(); ++cit ) {
		if ( itemProps.Update(OperationEntityId, *cit, OPERATION_SCHEDULE_ENABLED, bEnabled ? "1" : "0") ) {
			bSchedulerNeedsUpdate = true;
		}
	}

	                                                                         
	dbConnection.Commit();
	try {
		if ( bSchedulerNeedsUpdate ) UpdateScheduler();
	}
	catch( const CrException & ) {
		                                                                                       
	}
}
