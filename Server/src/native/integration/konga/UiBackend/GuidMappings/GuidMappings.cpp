/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <konga/Utils/StringUtils.h>
#include <konga/Utils/CrException.h>

#include "DbGuidToIdMapper.h"

#include "GuidMappings.h"

using namespace konga;
using std::string;

string konga::GetGuidForId(const int lItemId, const int iEntityType) {
	const string& stGuid = DbGuidToIdMapper().GetGuid(lItemId, iEntityType, "");
	if ( stGuid.empty() )
		throw CrException("No item exists with lItemId = " + toString(lItemId) + " for Entity_ID = " + toString(iEntityType));
	return stGuid;
}

long konga::GetItemIdForGuid(const string& stGuid, const int iEntityType, bool bThrowIfNotExisting, long lDefaultValue) {
	long lItemId = DbGuidToIdMapper().GetId(stGuid, iEntityType);
	if ( lItemId == GuidToIdMapper::invalid_id ) {
		if ( bThrowIfNotExisting )
			throw CrException("No item with Entity_ID = " + toString(iEntityType) + " and GUID = " + stGuid + " could be found.");
		else
			lItemId = lDefaultValue;
	}

	return lItemId;
}

GuidToIdMapper* konga::GetDefaultGuidToIdMapper() {
	return new DbGuidToIdMapper();
}
