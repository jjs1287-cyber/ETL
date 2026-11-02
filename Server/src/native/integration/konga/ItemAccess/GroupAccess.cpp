/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <konga/DbRelations/DatabaseTableNameMappings.h>

#include "GroupAccess.h"

using namespace konga;

std::string GroupAccess::GetTableName(int iEntityId) const {
	return GetGroupPermissionTableName(iEntityId);
}

