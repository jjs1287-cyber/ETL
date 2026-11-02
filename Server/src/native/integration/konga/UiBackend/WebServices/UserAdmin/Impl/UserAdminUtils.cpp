/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#include <sstream>
#include <algorithm>	            

#include <konga/Utils/StringUtils.h>	             
#include <konga/Utils/DBConnectionInfo.h>

#include <konga/ItemAccess/UserAccess.h>
#include <konga/ItemAccess/GroupAccess.h>

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/DbUtils.h>

#include <konga/Utils/PortDefs.h>	                                                         
#include <konga/Db/DbUtils/SmartDbPtr.h>
#include <konga/Db/DbUtils/DynamicDbRecordSet.h>
#include <konga/Db/TranDb/UsersTab.h>

#include <konga/DbRelations/EntityIdConstants.h>

#include <konga/UiBackend/GuidMappings/GuidToIdMapper.h>

using namespace konga;
using std::string;
using std::vector;
using std::stringstream;

#include "UserAdminUtils.h"

string konga::GetUserGuid(long lUserId, GuidToIdMapper& guidToIdMapper) {
	const string stGuid = guidToIdMapper.GetGuid(lUserId, UserEntityId);
	if ( stGuid.empty() )
		throw CrException("Failed to get the GUID for the user with databse id = " + toString(lUserId));
	return stGuid;
}

string konga::GetGroupGuid(long lGroupId, GuidToIdMapper& guidToIdMapper) {
	const string stGuid = guidToIdMapper.GetGuid(lGroupId, GroupEntityId);
	if ( stGuid.empty() )
		throw CrException("Failed to get the GUID for the group with databse id = " + toString(lGroupId));
	return stGuid;
}

long konga::GetUserId(const string& stGuid, GuidToIdMapper& guidToIdMapper) {
	const long lUserId = guidToIdMapper.GetId(stGuid, UserEntityId);
	if ( lUserId == GuidToIdMapper::invalid_id )
		throw CrException("Failed to find the database id for User with GUID = " + stGuid);
	return lUserId;
}

long konga::GetGroupId(const string& stGuid, GuidToIdMapper& guidToIdMapper) {
	const long lGroupId = guidToIdMapper.GetId(stGuid, GroupEntityId);
	if ( lGroupId == GuidToIdMapper::invalid_id )
		throw CrException("Failed to find the database id for Group with GUID = " + stGuid);
	return lGroupId;
}


void konga::GetUsersAndGroupsFromDb(std::vector<impl::User>& vUsers,
						std::vector<impl::Group>& vGroups,
						std::vector<impl::UserGroup>& vUserGroups,
						DbConnection& dbConnection,
						GuidToIdMapper& guidToIdMapper,
						const bool bIncludePasswords)
{
	SmartDbPtr<TranDb::UsersTab> pUsersTab( TranDb::UsersTab::MakeInstance(&dbConnection) );
	pUsersTab->Select("WHERE user_id > -1 OR user_id = -2");

	        
	  
	while ( pUsersTab->Next() ) {
		impl::User user;

		const int user_id = pUsersTab->GetUser_ID();
		user.UserId = GetUserGuid(pUsersTab->GetUser_ID(), guidToIdMapper);
		user.FirstName = pUsersTab->IsFirst_NameNull() ? "" : pUsersTab->GetFirst_Name();
		user.LastName = pUsersTab->IsLast_NameNull() ? "" : pUsersTab->GetLast_Name();
		user.Login = pUsersTab->IsLoginNull() ? "" : pUsersTab->GetLogin();

		                                                
		if ( bIncludePasswords && user_id > 0 && !pUsersTab->IsPasswordNull() ) {
			user.Password = NullableString(pUsersTab->GetPassword());
		}
		else {
			user.Password = NullableString(0);
		}
		user.Email = pUsersTab->IsEmailNull() ? "" : pUsersTab->GetEmail();

		vUsers.push_back(user);
	}
	pUsersTab->Close();

	         
	  
	SmartDbPtr<DynamicDbRecordSet> pGroupsTab(DynamicDbRecordSet::MakeInstance(&dbConnection));
	pGroupsTab->SetSqlStatement("SELECT group_id, group_name, description FROM GroupsTab");
	pGroupsTab->Open();

	while ( pGroupsTab->Next() ) {
		impl::Group group;
		const long lGroupId = atol(pGroupsTab->GetFieldValue(0).c_str());
		group.GroupId = GetGroupGuid(lGroupId, guidToIdMapper);
		group.GroupName = pGroupsTab->IsFieldNull(1) ? "" : pGroupsTab->GetFieldValue(1);
		group.Description = pGroupsTab->IsFieldNull(2) ? "" : pGroupsTab->GetFieldValue(2);

		vGroups.push_back(group);
	}
	pGroupsTab->Close();

	                        
	  
	SmartDbPtr<DynamicDbRecordSet> pUserGroupTab(DynamicDbRecordSet::MakeInstance(&dbConnection));
	pUserGroupTab->SetSqlStatement("SELECT user_id, group_id from User_GroupTab ORDER BY group_id");
	pUserGroupTab->Open();

	string stCurrentGroupId;

	vector<string> vUserIds;
	while( pUserGroupTab->Next() ) {
		const string stUserId( pUserGroupTab->GetFieldValue(0) );
		const string stGroupId( pUserGroupTab->GetFieldValue(1) );

		if ( stGroupId != stCurrentGroupId && !vUserIds.empty() ) {
			                                  
			impl::UserGroup ug;
			const long lGroupId = atol( stCurrentGroupId.c_str() );
			ug.GroupId = GetGroupGuid(lGroupId, guidToIdMapper);
			ug.UserIds = vUserIds;
			vUserGroups.push_back(ug);
			vUserIds.clear();
		}

		stCurrentGroupId = stGroupId;
		const long lUserId = atol( stUserId.c_str() );
		vUserIds.push_back( GetUserGuid(lUserId, guidToIdMapper) );
	}

	                         
	if ( !vUserIds.empty() ) {
		impl::UserGroup ug;
		const long lGroupId = atol( stCurrentGroupId.c_str() );
		ug.GroupId = GetGroupGuid(lGroupId, guidToIdMapper);
		ug.UserIds = vUserIds;
		vUserGroups.push_back(ug);
	}
}

bool konga::DoesUserExist(const std::string& stLogin, DbConnection& dbConnection) {
	SmartDbPtr<TranDb::UsersTab> pUsersTab( TranDb::UsersTab::MakeInstance(&dbConnection) );

	pUsersTab->Select("WHERE UPPER(Login) = UPPER('" + SqlEscape(stLogin) + "')");
	return pUsersTab->Next();
}

bool konga::DoesGroupExist(const std::string& stGroupName, DbConnection& dbConnection) {
	SmartDbPtr<DynamicDbRecordSet> pGroupTab( DynamicDbRecordSet::MakeInstance(&dbConnection) );
	pGroupTab->SetSqlStatement("SELECT group_id FROM GroupsTab WHERE UPPER(group_name) = UPPER('" + SqlEscape(stGroupName) + "')");

	pGroupTab->Open();
	return pGroupTab->Next();
}

bool konga::Changed(const impl::User& oldUser, const User& newUser) {
	if ( oldUser.UserId != newUser.UserId )
		throw CrException("Internal Error: cannot compare two Users with different UserId.");

	if ( oldUser.FirstName != string(newUser.FirstName==0 ? "" : newUser.FirstName) )
		return true;

	if ( oldUser.LastName != string(newUser.LastName==0 ? "" : newUser.LastName) )
		return true;

	if ( oldUser.Login != string(newUser.Login==0 ? "" : newUser.Login) )
		return true;

	if ( oldUser.Email != string(newUser.Email==0 ? "" : newUser.Email) )
		return true;

	                                                                        
	if ( newUser.Password != 0 )
		return true;

	return false;
}

bool konga::Changed(const impl::Group& oldGroup, const Group& newGroup) {
	if ( oldGroup.GroupId != newGroup.GroupId )
		throw CrException("Internal Error: cannot compare two Users with different UserId.");

	if ( oldGroup.GroupName != string(newGroup.GroupName==0 ? "" : newGroup.GroupName) )
		return true;

	if ( oldGroup.Description != string(newGroup.Description==0 ? "" : newGroup.Description) )
		return true;

	return false;
}

namespace konga {
	vector<string> getUserTabFieldNames() {
		vector<string> vFieldNames;
		vFieldNames.push_back("first_name");
		vFieldNames.push_back("last_name");
		vFieldNames.push_back("login");
		vFieldNames.push_back("password");
		vFieldNames.push_back("email");
		return vFieldNames;
	}

	vector<string> getGroupTabFieldNames() {
		vector<string> vFieldNames;
		vFieldNames.push_back("group_name");
		vFieldNames.push_back("description");
		return vFieldNames;
	}
}
void konga::UpdateUser(const User& user, DbConnection& dbConnection, GuidToIdMapper& guidToIdMapper) {
	vector<string> v;
	UpdateUser(user, dbConnection, guidToIdMapper, v);
}

void konga::UpdateUser(const User& user, DbConnection& dbConnection, GuidToIdMapper& guidToIdMapper, const std::vector<std::string>& vFieldsToSkip) {
	const string stUserId(user.UserId==0 ? "" : user.UserId);
	if ( stUserId.empty() )
		throw CrException("Illegal Argument: No user id was specified.");

	const vector<string>& vFieldNames = getUserTabFieldNames();

	vector<NullableString> vFieldValues(5);
	vFieldValues[0] = NullableString( user.FirstName );
	vFieldValues[1] = NullableString( user.LastName );
	vFieldValues[2] = NullableString( user.Login );
	if ( user.Password != 0 && *user.Password ) {
		vFieldValues[3] = NullableString( user.Password );
	}
	else
		vFieldValues[3] = NullableString(0);
	vFieldValues[4] = user.Email;

	stringstream ssSql;
	ssSql << "UPDATE UsersTab SET ";
	bool bFirst = true;
	vector<string>::const_iterator citNames = vFieldNames.begin();
	vector<NullableString>::const_iterator citValues = vFieldValues.begin();
	for(; citValues != vFieldValues.end(); ++citNames, ++citValues ) {
		if ( *citValues != 0 && std::find(vFieldsToSkip.begin(), vFieldsToSkip.end(), *citNames ) == vFieldsToSkip.end() ) {
			if ( bFirst )
				bFirst = false;
			else
				ssSql << ",";
			ssSql << *citNames << "='" << SqlEscape(citValues->str()) << "'";
		}
	}

	if ( bFirst )
		return;                                               

	long lUserId = GetUserId(stUserId, guidToIdMapper);
	ssSql << " WHERE User_Id = " << lUserId;
	dbConnection.ExecuteSql(ssSql.str());
}

void konga::UpdateGroup(const Group& group, DbConnection& dbConnection, GuidToIdMapper& guidToIdMapper) {
	const string stGroupId( group.GroupId==0? "" : group.GroupId );
	if ( stGroupId.empty() )
		throw CrException("Illegal Argument: NULL or empty GroupId specified.");

	const vector<string>& vFieldNames = getGroupTabFieldNames();
	vector<NullableString> vFieldValues(2);
	vFieldValues[0] = NullableString( group.GroupName );
	vFieldValues[1] = NullableString( group.Description );

	stringstream ssSql;
	ssSql << "UPDATE GroupsTab SET ";
	bool bFirst = true;
	vector<string>::const_iterator citNames = vFieldNames.begin();
	vector<NullableString>::const_iterator citValues = vFieldValues.begin();
	for(; citValues != vFieldValues.end(); ++citNames, ++citValues ) {
		if ( *citValues != 0 ) {
			if ( bFirst )
				bFirst = false;
			else
				ssSql << ",";
			ssSql << *citNames << " = '" << SqlEscape(citValues->str()) << "'";
		}
	}

	long lGroupId = GetGroupId(stGroupId, guidToIdMapper);
	ssSql << " WHERE group_id = " << lGroupId;

	dbConnection.ExecuteSql(ssSql.str());
}

void konga::InsertUserGroup(long lGroupId, const vector<string>& vAddedUserIds,
							DbConnection& dbConnection, GuidToIdMapper& guidToIdMapper) {
	if ( vAddedUserIds.empty() )
		return;

	vector<string>::const_iterator cit = vAddedUserIds.begin();
	for(; cit != vAddedUserIds.end(); ++cit ) {
		const string& stUserId = *cit;
		const long lUserId = GetUserId(stUserId, guidToIdMapper);
		stringstream ssSql;
		ssSql << "INSERT INTO User_GroupTab (user_id, group_id) VALUES (" << lUserId << ", " << lGroupId << ")";
		dbConnection.ExecuteSql(ssSql.str());
	}
}

void konga::UpdateUserGroup(const std::string& stGroupId,
					 const std::vector<std::string>& vUserIds, DbConnection& dbConnection,
					 GuidToIdMapper& guidToIdMapper)
{
	const long lGroupId = GetGroupId(stGroupId, guidToIdMapper);
	dbConnection.ExecuteSql( "DELETE FROM User_GroupTab WHERE Group_Id = " + toString(lGroupId) );

	InsertUserGroup(lGroupId, vUserIds, dbConnection, guidToIdMapper);
}

namespace {
	long getMaxId(const string& stIdName, const string stTableName, DbConnection& dbConnection) {
		stringstream ssSql;
		ssSql << "SELECT MAX(" << stIdName << ") FROM " << stTableName;

		SmartDbPtr<DynamicDbRecordSet> pSelectMax( DynamicDbRecordSet::MakeInstance(&dbConnection, ssSql.str()) );
		pSelectMax->Open();
		pSelectMax->Next();
		return atol(pSelectMax->GetFieldValue(0).c_str());
	}
}

void konga::InsertUser(const User& user, DbConnection& dbConnection, GuidToIdMapper& guidToIdMapper) {
	const string stUserId( user.UserId==0? "" : user.UserId );
	if ( stUserId.empty() )
		throw CrException("Invalid User: No UserId was specified.");

	const long lUserId = getMaxId("user_id", "UsersTab", dbConnection) + 1;

	const vector<string>& vFieldNames = getUserTabFieldNames();
	vector<NullableString> vFieldValues(5);
	vFieldValues[0] = NullableString( user.FirstName );
	vFieldValues[1] = NullableString( user.LastName );
	vFieldValues[2] = NullableString( user.Login );
	if ( user.Password != 0 ) {
		vFieldValues[3] = NullableString( user.Password );
	}
	else {
		vFieldValues[3] = NullableString( user.Password );
	}
	vFieldValues[4] = user.Email;

	stringstream ssSqlFields, ssSqlValues;
	ssSqlFields << "user_id";
	ssSqlValues << lUserId;
	vector<string>::const_iterator citFields = vFieldNames.begin();
	vector<NullableString>::const_iterator citValues = vFieldValues.begin();
	for(; citFields != vFieldNames.end(); ++citFields, ++citValues ) {
		if ( *citValues != 0 ) {
			ssSqlFields << "," << *citFields;
			ssSqlValues << ",'" << SqlEscape(citValues->str()) << "'";
		}
	}
	const string stSql( "INSERT INTO UsersTab(" + ssSqlFields.str() + ") VALUES(" + ssSqlValues.str() + ")");
	dbConnection.ExecuteSql(stSql);

	guidToIdMapper.SetGuidForItem(lUserId, UserEntityId, stUserId);
}

void konga::InsertGroup(const Group& group, DbConnection& dbConnection, GuidToIdMapper& guidToIdMapper) {
	const string stGroupId( group.GroupId==0? "" : group.GroupId );
	if ( stGroupId.empty() )
		throw CrException("Invalid Group: No GroupId was specified.");

	const long lGroupId = getMaxId("group_id", "GroupsTab", dbConnection) + 1;

	const vector<string>& vFieldNames = getGroupTabFieldNames();
	vector<NullableString> vFieldValues(2);
	vFieldValues[0] = NullableString( group.GroupName );
	vFieldValues[1] = NullableString( group.Description );

	stringstream ssSqlFields, ssSqlValues;
	ssSqlFields << "group_id";
	ssSqlValues << lGroupId;
	vector<string>::const_iterator citFields = vFieldNames.begin();
	vector<NullableString>::const_iterator citValues = vFieldValues.begin();
	for(; citFields != vFieldNames.end(); ++citFields, ++citValues ) {
		if ( *citValues != 0 ) {
			ssSqlFields << "," << *citFields;
			ssSqlValues << ",'" << SqlEscape(citValues->str()) << "'";
		}
	}
	const string stSql( "INSERT INTO GroupsTab(" + ssSqlFields.str() + ") VALUES(" + ssSqlValues.str() + ")");
	dbConnection.ExecuteSql(stSql);

	guidToIdMapper.SetGuidForItem(lGroupId, GroupEntityId, stGroupId);
}

namespace {
	string getDeleteSql(const long lId, const string& stTableName, const string& stIdName) {
		stringstream ssSql;
		ssSql << "DELETE FROM " << stTableName << " WHERE " << stIdName << " = " << lId;
		return ssSql.str();
	}
}

void konga::DeleteUsers(const vector<string>& vIds, DbConnection& dbConnection, GuidToIdMapper& guidToIdMapper) {
	if ( vIds.empty() )
		return;

	vector<string>::const_iterator cit = vIds.begin();
	for(; cit != vIds.end(); ++cit ) {
		const string& stGuid = *cit;
		const long lUserId = GetUserId(stGuid, guidToIdMapper);

		                                                                                
		                                         
		if ( lUserId < 1 )
			throw CrException("The built in Administrative/Anonymous users cannot be deleted.");

		                        
		dbConnection.ExecuteSql(getDeleteSql(lUserId, "UsersTab", "user_id"));

		                            
		dbConnection.ExecuteSql(getDeleteSql(lUserId, "User_GroupTab", "user_id"));

		                                 
		UserAccess userAccess(UserInfo(lUserId), dbConnection);
		userAccess.DeleteItem();

		                            
		guidToIdMapper.DeleteItem(stGuid, UserEntityId);
	}
}

void konga::DeleteGroups(const vector<string>& vIds, DbConnection& dbConnection, GuidToIdMapper& guidToIdMapper) {
	if ( vIds.empty() )
		return;

	vector<string>::const_iterator cit = vIds.begin();
	for(; cit != vIds.end(); ++cit ) {
		const string& stGuid = *cit;
		const long lGroupId = GetGroupId(stGuid, guidToIdMapper);

		                                               
		                                                                                           
		if ( lGroupId < 1 )
			throw CrException("The Administrative group cannot be deleted.");

		                             
		dbConnection.ExecuteSql(getDeleteSql(lGroupId, "GroupsTab", "group_id"));

		                            
		dbConnection.ExecuteSql(getDeleteSql(lGroupId, "User_GroupTab", "group_id"));

		                              
		GroupAccess groupAccess(atol(cit->c_str()), dbConnection);
		groupAccess.DeleteItem();

		                           
		guidToIdMapper.DeleteItem(stGuid, GroupEntityId);
	}
}
