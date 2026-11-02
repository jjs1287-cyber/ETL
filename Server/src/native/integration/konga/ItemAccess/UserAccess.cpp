/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#include <sstream>	               

#include <konga/Utils/StringUtils.h>		           

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DbUtils/DynamicDbRecordSet.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>

#include <konga/DbRelations/DatabaseTableNameMappings.h>

#include "UserAccess.h"

using namespace konga;
using std::stringstream;
using std::vector;
using std::string;

UserInfo konga::GetItemOwner(DbConnection& dbConnection, long lItemId, int iEntityTypeId) {
	const string sql("SELECT AllowBits, User_Id FROM " + GetUserPermissionTableName(iEntityTypeId)
		+ " WHERE Item_Id = " + toString(lItemId));
	SmartDbPtr<DynamicDbRecordSet> rs( DynamicDbRecordSet::MakeInstance(&dbConnection, sql) );
	rs->Open();
	while( rs->Next() ) {
		AccessControl::AllowBits allowBits = static_cast<AccessControl::AllowBits>(atoi(rs->GetFieldValue(0).c_str()));
		if ( (allowBits & AccessControl::ACCESS_BIT_OWNER) != 0 ) {
			const long lUserId = atol(rs->GetFieldValue(1).c_str());
			return konga::GetUserInfo(lUserId, dbConnection, true);
		}
	}

	                                      
	return konga::GetUserInfo(0, dbConnection, true);
}

UserAccess UserAccess::GetUserAccessWithGroup(int iUserId, DbConnection& dbConnection) {
	UserInfo user = GetUserInfo(iUserId, dbConnection, true);
	return UserAccess(user, dbConnection);
}

UserAccess* UserAccess::GetUserAccessWithGroup(int iUserId, DbConnection* pDbConnection) {
	UserInfo user = GetUserInfo(iUserId, *pDbConnection, true);
	return new UserAccess(user, *pDbConnection);
}

UserAccess::UserAccess(const UserInfo& userInfo, DbConnection& dbConnection)
	: AccessControl(dbConnection), m_UserInfo(userInfo)
{
	vector<long> vGroups = m_UserInfo.GetGroupIds();
	if ( !vGroups.empty() ) {
		stringstream ssSql;
		ssSql << " IN (";
		std::vector<long>::const_iterator cit = vGroups.begin();
		for(; cit != vGroups.end(); ++cit) {
			if ( cit != vGroups.begin() )
				ssSql << ",";
			ssSql << *cit;
		}
		ssSql << ")";
		m_stGroupInClause = ssSql.str();
	}
}

void UserAccess::GetPermissions(long iItemId, int iEntityTypeId, AllowBits& allowBits, DenyBits& denyBits) const {
	GetPermissions(iItemId, iEntityTypeId, allowBits, denyBits, true);
}

void UserAccess::GetPermissions(long iItemId, int iEntityTypeId, AllowBits& allowBits, DenyBits& denyBits, bool bIncludeGroups) const {
	AccessControl::GetPermissions(iItemId, iEntityTypeId, allowBits, denyBits);
	if ( !bIncludeGroups || m_UserInfo.GetNumberOfGroups() == 0 )
		return;

	                                        
	                                                                               
	  
	stringstream ssSql;
	ssSql << "SELECT AllowBits, DenyBits FROM " << GetGroupPermissionTableName(iEntityTypeId) << " WHERE Item_ID = " << iItemId
		  << " AND Group_ID " << m_stGroupInClause;
	
	SmartDbPtr<DynamicDbRecordSet> rsGroupSelect( DynamicDbRecordSet::MakeInstance(&GetDbConnection(), ssSql.str()) );
	rsGroupSelect->Open();
	while ( rsGroupSelect->Next() ) {
		AllowBits groupAllowBits = static_cast<AllowBits>(atoi(rsGroupSelect->GetFieldValue(0).c_str()));
		allowBits |= groupAllowBits;

		DenyBits groupDenyBits = static_cast<DenyBits>(atoi(rsGroupSelect->GetFieldValue(1).c_str()));
		denyBits |= groupDenyBits;
	}
}

void UserAccess::AddItemOwner(long iItemId, int iEntityTypeId) {
	AddItem(iItemId, iEntityTypeId, ACCESS_LEVEL_OWNER);
}

void UserAccess::ChangeOwner(long iItemId, int iEntityTypeId) {
	ChangeItem(iItemId, iEntityTypeId, ACCESS_LEVEL_OWNER, 0);
}

bool UserAccess::IsOwner(long iItemId, int iEntityTypeId) const {
	AllowBits allowBits;
	DenyBits denyBits;
	GetPermissions(iItemId, iEntityTypeId, allowBits, denyBits);

	return (allowBits & ACCESS_BIT_OWNER) != 0;
}

string UserAccess::GetTableName(int iEntityId) const {
	return GetUserPermissionTableName(iEntityId);
}

namespace {
	DbConnection* getDbConnection() {
		return konga::GetBackEndDbConnection("TranDb");
	}
}

bool konga::UserHasReadPermission(long iUserId, long iItemId, int iEntityTypeId) {
	SmartDbPtr<DbConnection> pConn( getDbConnection() );
	UserAccess ua(UserAccess::GetUserAccessWithGroup(iUserId, *pConn));
	return ua.HasReadPermission(iItemId, iEntityTypeId);
}

bool konga::UserHasExecutePermission(long iUserId, long iItemId, int iEntityTypeId) {
	SmartDbPtr<DbConnection> pConn( getDbConnection() );
	UserAccess ua(UserAccess::GetUserAccessWithGroup(iUserId, *pConn));
	return ua.HasExecutePermission(iItemId, iEntityTypeId);
}

bool konga::UserHasWritePermission(long iUserId, long iItemId, int iEntityTypeId) {
	SmartDbPtr<DbConnection> pConn( getDbConnection() );
	UserAccess ua(UserAccess::GetUserAccessWithGroup(iUserId, *pConn));
	return ua.HasWritePermission(iItemId, iEntityTypeId);
}

