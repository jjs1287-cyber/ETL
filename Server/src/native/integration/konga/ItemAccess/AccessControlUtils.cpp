/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#include <string>

#include <konga/Utils/StringUtils.h>	           

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/DynamicDbRecordSet.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>

#include <konga/DbRelations/DatabaseTableNameMappings.h>

#include "AccessControlUtils.h"
#include "AccessControl.h"

using namespace konga;
using std::vector;
using std::string;

namespace {
	vector<PermissionInfo> GetPermissionInfoForItems(bool isUser, int iEntityType, const vector<long>& vItemId, DbConnection& dbConnection) {
		vector<PermissionInfo> retVal;
		if ( vItemId.empty() )
			return retVal;

		string sql("SELECT item_id, " + string(isUser?"user_id":"group_id") + ", allowbits, denybits FROM ");
		sql += isUser ? GetUserPermissionTableName(iEntityType) : GetGroupPermissionTableName(iEntityType);
		sql += " WHERE item_id IN (";

		for( vector<long>::const_iterator cit = vItemId.begin(); cit != vItemId.end(); ++cit ) {
			if ( cit != vItemId.begin() ) sql += ",";
			sql += toString(*cit);
		}
		sql += ")";

		SmartDbPtr<DynamicDbRecordSet> rs( DynamicDbRecordSet::MakeInstance(&dbConnection, sql) );
		rs->Open();
		while ( rs->Next() ) {
			const PermissionInfo pi(atoi(rs->GetFieldValue(0).c_str()), atoi(rs->GetFieldValue(1).c_str()),
				atoi(rs->GetFieldValue(2).c_str()), atoi(rs->GetFieldValue(3).c_str()));
			if ( pi.allowBits == AccessControl::ACCESS_BIT_OWNER && pi.denyBits == AccessControl::ACCESS_LEVEL_DENIED )
				continue;	                                                             
			retVal.push_back(pi);
		}
		return retVal;
	}
}

vector<PermissionInfo> konga::GetUserPermissionInfoForItems(int iEntityType, const vector<long>& vItemId, DbConnection& dbConnection) {
	return GetPermissionInfoForItems(true, iEntityType, vItemId, dbConnection);
}

vector<PermissionInfo> konga::GetGroupPermissionInfoForItems(int iEntityType, const vector<long>& vItemId, DbConnection& dbConnection) {
	return GetPermissionInfoForItems(false, iEntityType, vItemId, dbConnection);
}
