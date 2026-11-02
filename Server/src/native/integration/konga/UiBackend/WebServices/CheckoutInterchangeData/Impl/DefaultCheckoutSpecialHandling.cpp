/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
#include <konga/UiBackend/GuidMappings/GuidToIdMapper.h>
#include <konga/DbRelations/EntityIdConstants.h>

#include "DefaultCheckoutSpecialHandling.h"

using namespace konga::interchange_checkout;
using std::string;

void DefaultCheckoutSpecialHandling::HandleColumn(vColumnValuesT& vColumnValues) {
	if ( !m_sGuidColumnName.empty() ) {
		NullableString& guid = GetColumnValueRef("guid", vColumnValues);
		NullableString& id = GetColumnValueRef(m_sGuidColumnName, vColumnValues);
		if ( guid != 0 ) {
			id = guid;
			guid = 0;
		}
	}

	                                      
	NullableString& ipId = GetColumnValueRef("interchangeproject_id", vColumnValues);
	if ( ipId != 0 ) {
		const long lItemId = atol(ipId.str().c_str());
		const string& stGuid = m_guidToIdMapper.GetGuid(lItemId, InterchangeProjectEntityId);
		if ( stGuid.empty() )
			throw InternalErrorException("There is no GUID for Interchange Project with Id = " + ipId.str());
		ipId = stGuid;
	}
}
