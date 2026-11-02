/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <vector>
#include <sstream>	               

#include <konga/DbRelations/DatabaseTableNameMappings.h>

#include "UserInfo.h"
#include "AccessControl.h"

#include "AccessSqlGenerator.h"

using namespace konga;
using std::string;
using std::vector;
using std::stringstream;

string konga::GetOwnerJoin(const UserInfo& userInfo, int iEntityType, const string& stMainIdName) {
	if ( !userInfo.IsAdministrator() ) {
		const string stUserAccessTab(GetUserPermissionTableName(iEntityType));
		const string stUserId(stUserAccessTab + ".User_ID");
		const string stItemId(stUserAccessTab + ".Item_ID");
		const string stAllowBits(stUserAccessTab + ".AllowBits");

		stringstream ssSql;
		ssSql << " INNER JOIN " << stUserAccessTab << " ON " << stMainIdName << " = " << stItemId
			<< " AND " << stUserId << " = " << userInfo.GetId()
			<< " AND (" << stAllowBits << "&" << AccessControl::ACCESS_BIT_OWNER << ")<>0";

		return ssSql.str();
	}
	return "";
}

string konga::GetUserReadAllowJoin(const UserInfo& userInfo, int iEntityType, const string& stMainIdName) {
	if ( !userInfo.IsAdministrator() ) {
		const string stUserAccessTab(GetUserPermissionTableName(iEntityType));
		const string stUserId(stUserAccessTab + ".User_ID");
		const string stItemId(stUserAccessTab + ".Item_ID");
		const string stAllowBits(stUserAccessTab + ".AllowBits");

		stringstream ssSql;
		ssSql << " INNER JOIN " << stUserAccessTab << " ON " << stMainIdName << " = " << stItemId
			<< " AND " << stUserId << " = " << userInfo.GetId()
			<< " AND (" << stAllowBits << "&" << AccessControl::ACCESS_BIT_READ << ")<>0";

		return ssSql.str();
	}
	return "";
}

string konga::GetUserReadDenyJoin(const UserInfo& userInfo, int iEntityType, const string& stMainIdName) {
	if ( !userInfo.IsAdministrator() ) {
		const string stUserAccessTab(GetUserPermissionTableName(iEntityType));
		const string stUserId(stUserAccessTab + ".User_ID");
		const string stItemId(stUserAccessTab + ".Item_ID");
		const string stDenyBits(stUserAccessTab + ".DenyBits");

		stringstream ssSql;
		ssSql << " INNER JOIN " << stUserAccessTab << " ON " << stMainIdName << " = " << stItemId
			<< " AND " << stUserId << " = " << userInfo.GetId()
			<< " AND (" << stDenyBits << "&" << AccessControl::ACCESS_BIT_READ << ")<>0";

		return ssSql.str();
	}
	return "";
}

namespace {
	string getGroupInClause(const UserInfo& userInfo) {
		vector<long> vGroupIds = userInfo.GetGroupIds();
		if ( vGroupIds.empty() )
			return " IS NULL";	                                
		else if ( vGroupIds.size() ==1 ) {
			stringstream ssEq;
			ssEq << "=";
			ssEq << vGroupIds[0];
			return ssEq.str();
		}

		vector<long>::const_iterator cit = vGroupIds.begin();
		stringstream ssIn;
		ssIn << " IN(";
		for(; cit != vGroupIds.end(); ++cit ) {
			if ( cit != vGroupIds.begin() )
				ssIn << ",";
			ssIn << *cit;
		}
		ssIn << ")";
		return ssIn.str();
	}
}

string konga::GetGroupReadAllowJoin(const UserInfo& userInfo, int iEntityType, const string& stMainIdName) {
	if ( !userInfo.IsAdministrator() && userInfo.GetNumberOfGroups() > 0 ) {
		const string stGroupAccessTab( GetGroupPermissionTableName(iEntityType) );
		const string stGroupId(stGroupAccessTab + ".Group_ID");
		const string stItemId(stGroupAccessTab + ".Item_ID");
		const string stAllowBits(stGroupAccessTab + ".AllowBits");

		stringstream ssSql;
		ssSql << " INNER JOIN " << stGroupAccessTab << " ON " << stMainIdName << " = " << stItemId
			  << " AND " << stGroupId << getGroupInClause(userInfo)
			  << " AND (" << stAllowBits << "&" << AccessControl::ACCESS_BIT_READ << ")<>0";

		return ssSql.str();
	}
	return "";
}

string konga::GetGroupReadDenyJoin(const UserInfo& userInfo, int iEntityType, const string& stMainIdName) {
	if ( !userInfo.IsAdministrator() && userInfo.GetNumberOfGroups() > 0 ) {
		const string stGroupAccessTab( GetGroupPermissionTableName(iEntityType) );
		const string stGroupId(stGroupAccessTab + ".Group_ID");
		const string stItemId(stGroupAccessTab + ".Item_ID");
		const string stDenyBits(stGroupAccessTab + ".DenyBits");

		stringstream ssSql;
		ssSql << " INNER JOIN " << stGroupAccessTab << " ON " << stMainIdName << " = " << stItemId
			  << " AND " << stGroupId << getGroupInClause(userInfo)
			  << " AND (" << stDenyBits << "&" << AccessControl::ACCESS_BIT_READ << ")<>0";

		return ssSql.str();
	}
	return "";
}

string konga::GetSelectSqlWithAccessJoin_old(const UserInfo& userInfo, const string& stMainSql, const string& stJoinIdName, const int iEntityTypeId) {
	if ( userInfo.IsAdministrator() )
		return stMainSql;

	const string stOwnerJoin( GetOwnerJoin(userInfo, iEntityTypeId, stJoinIdName) );
	const string stUserAllowJoin( GetUserReadAllowJoin(userInfo, iEntityTypeId, stJoinIdName) );
	const string stUserDenyJoin( GetUserReadDenyJoin(userInfo, iEntityTypeId, stJoinIdName) );
	const string stGroupAllowJoin( GetGroupReadAllowJoin(userInfo, iEntityTypeId, stJoinIdName) );
	const string stGroupDenyJoin( GetGroupReadDenyJoin(userInfo, iEntityTypeId, stJoinIdName) );

	                                   
	stringstream ssAllow;
	ssAllow << "(" << stMainSql << stUserAllowJoin;
	if ( !stUserAllowJoin.empty())
		ssAllow << " UNION " << stMainSql << stGroupAllowJoin;
	ssAllow << ")";

	                                   
	stringstream ssDeny;
	ssDeny << "(" << stMainSql << stUserDenyJoin;
	if ( !stGroupDenyJoin.empty() )
		ssDeny << " UNION " << stMainSql << stGroupDenyJoin;
	ssDeny << ")";

	                                  
	const string stAllowEffective( ssAllow.str() + " EXCEPT " + ssDeny.str());

	                                            
	stringstream ssSql;
	ssSql << stMainSql << stOwnerJoin;
	ssSql << " UNION (" << stAllowEffective << ")";

	return ssSql.str();
}

string konga::GetOwnerSelect(const UserInfo& userInfo, const string& stMainIdName, const int iEntityTypeId) {
	const string stUserAccessTab(GetUserPermissionTableName(iEntityTypeId));
	const string stUserId(stUserAccessTab + ".User_ID");
	const string stItemId(stUserAccessTab + ".Item_ID");
	const string stAllowBits(stUserAccessTab + ".AllowBits");

	stringstream ssOwnerSql;
	ssOwnerSql << "SELECT " << stItemId << " FROM " << stUserAccessTab
			   << " WHERE " << stUserId << " = " << userInfo.GetId()
			   << " AND (" << stAllowBits << "&" << AccessControl::ACCESS_BIT_OWNER << ")<>0";

	return ssOwnerSql.str();
}

string konga::GetUserAllowSelect(const UserInfo& userInfo, const string& stMainIdName, const int iEntityTypeId) {
	const string stUserAccessTab(GetUserPermissionTableName(iEntityTypeId));
	const string stUserId(stUserAccessTab + ".User_ID");
	const string stItemId(stUserAccessTab + ".Item_ID");
	const string stAllowBits(stUserAccessTab + ".AllowBits");

	stringstream ssUserAllowSql;
	ssUserAllowSql << "SELECT " << stItemId << " FROM " << stUserAccessTab
				   << " WHERE " << stUserId << " = " << userInfo.GetId()
				   << " AND (" << stAllowBits << "&" << AccessControl::ACCESS_BIT_READ << ")<>0";

	return ssUserAllowSql.str();
}

string konga::GetUserDenySelect(const UserInfo& userInfo, const string& stMainIdName, const int iEntityTypeId) {
	const string stUserAccessTab(GetUserPermissionTableName(iEntityTypeId));
	const string stUserId(stUserAccessTab + ".User_ID");
	const string stItemId(stUserAccessTab + ".Item_ID");
	const string stDenyBits(stUserAccessTab + ".DenyBits");

	stringstream ssUserDenySql;
	ssUserDenySql << "SELECT " << stItemId << " FROM " << stUserAccessTab
				  << " WHERE " << stUserId << " = " << userInfo.GetId()
				  << " AND (" << stDenyBits << "&" << AccessControl::ACCESS_BIT_READ << ")<>0";
	return ssUserDenySql.str();
}

string konga::GetGroupAllowSelect(const UserInfo& userInfo, const string& stMainIdName, const int iEntityTypeId) {
	if ( userInfo.GetNumberOfGroups() > 0 ) {
		const string stGroupAccessTab( GetGroupPermissionTableName(iEntityTypeId) );
		const string stGroupId(stGroupAccessTab + ".Group_ID");
		const string stItemId(stGroupAccessTab + ".Item_ID");
		const string stAllowBits(stGroupAccessTab + ".AllowBits");

		stringstream ssGroupAllowSql;
		ssGroupAllowSql << "SELECT " << stItemId << " FROM " << stGroupAccessTab
			<< " WHERE " << stGroupId << getGroupInClause(userInfo)
			<< " AND (" << stAllowBits << "&" << AccessControl::ACCESS_BIT_READ << ")<>0";
		return ssGroupAllowSql.str();
	}
	return "";
}

string konga::GetGroupDenySelect(const UserInfo& userInfo, const string& stMainIdName, const int iEntityTypeId) {
	if ( userInfo.GetNumberOfGroups() > 0 ) {
		const string stGroupAccessTab( GetGroupPermissionTableName(iEntityTypeId) );
		const string stGroupId(stGroupAccessTab + ".Group_ID");
		const string stItemId(stGroupAccessTab + ".Item_ID");
		const string stDenyBits(stGroupAccessTab + ".DenyBits");

		stringstream ssGroupDenySql;
		ssGroupDenySql << "SELECT " << stItemId << " FROM " << stGroupAccessTab
			<< " WHERE " << stGroupId << getGroupInClause(userInfo)
			<< " AND (" << stDenyBits << "&" << AccessControl::ACCESS_BIT_READ << ")<>0";
		return ssGroupDenySql.str();
	}
	return "";
}

string konga::GetAllowSelect(const UserInfo& userInfo, const string& stMainIdName, const int iEntityTypeId) {
	const string stUserSelect( GetUserAllowSelect(userInfo, stMainIdName, iEntityTypeId) );
	const string stGroupSelect( GetGroupAllowSelect(userInfo, stMainIdName, iEntityTypeId) );

	if ( stGroupSelect.empty() )
		return stUserSelect;

	return (stUserSelect + " UNION " + stGroupSelect);
}

string konga::GetDenySelect(const UserInfo& userInfo, const string& stMainIdName, const int iEntityTypeId) {
	const string stUserSelect( GetUserDenySelect(userInfo, stMainIdName, iEntityTypeId) );
	const string stGroupSelect( GetGroupDenySelect(userInfo, stMainIdName, iEntityTypeId) );

	if ( stGroupSelect.empty() )
		return stUserSelect;

	return (stUserSelect + " UNION " + stGroupSelect);
}

string konga::GetAccessControlWhereClause(const UserInfo& userInfo, const string& stMainIdName, const int iEntityTypeId) {
	if ( userInfo.IsAdministrator()) {
		return stMainIdName + " > 0";	                                                          
	}

	const string stOwnerSelect( GetOwnerSelect(userInfo, stMainIdName, iEntityTypeId) );
	const string stAllowSql( GetAllowSelect(userInfo, stMainIdName, iEntityTypeId) );
	const string stDenySql( GetDenySelect(userInfo, stMainIdName, iEntityTypeId) );

	stringstream ssWhereClause;
	ssWhereClause << stMainIdName << " IN (" << stOwnerSelect << ") OR ("
				  << stMainIdName << " IN(" << stAllowSql << ") AND "
				  << stMainIdName << " NOT IN(" << stDenySql << "))";
	
	return ssWhereClause.str();
}

