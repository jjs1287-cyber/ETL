/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef KONGA_ACCESS_CONTROL_UTILS_H
#define KONGA_ACCESS_CONTROL_UTILS_H

#include <vector>

namespace konga {

	class DbConnection;

	                                                                     
	struct PermissionInfo {
		PermissionInfo(long itemId_, long roleId_, int allowBits_, int denyBits_)
			: itemId(itemId_), roleId(roleId_), allowBits(allowBits_), denyBits(denyBits_)
		{}
		long itemId, roleId;	                                              
		int allowBits, denyBits;
	};

	std::vector<PermissionInfo> GetUserPermissionInfoForItems(int iEntityType, const std::vector<long>& vItemId, DbConnection& dbConnection);
	std::vector<PermissionInfo> GetGroupPermissionInfoForItems(int iEntityType, const std::vector<long>& vItemId, DbConnection& dbConnection);
}

#endif                                

