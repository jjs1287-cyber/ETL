/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#include <konga/UiBackend/GuidMappings/GuidToIdMapper.h>
#include <konga/DbRelations/EntityIdConstants.h>

#include "OperationTreeCheckoutSpecialHandlingForOperation.h"

using namespace konga;
using namespace konga::interchange_checkout;
using std::string;

void OperationTreeCheckoutSpecialHandlingForOperation::HandleColumn(vColumnValuesT& vColumnValues) {
	DefaultCheckoutSpecialHandling::HandleColumn(vColumnValues);

	NullableString& scheduleId = GetColumnValueRef("schedule_id", vColumnValues);
	if ( scheduleId != 0 ) {
		const long lItemId = atol(scheduleId.str().c_str());
		const string& stGuid = m_myGuidToIdMapper.GetGuid(lItemId, ScheduleEntityId);
		if ( !stGuid.empty() ) {
			scheduleId = stGuid;
		}
		else {
			                                                        
			scheduleId = 0;
		}
	}
}
