/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
#ifndef KONGA_WS_USERADMIN_UTILS_H
#define KONGA_WS_USERADMIN_UTILS_H

#include <string>
#include <vector>

#include <konga/Utils/NullableString.h>

#include "../User.hpp"
#include "../Group.hpp"

namespace impl {
	struct User {
		std::string UserId;
		std::string FirstName;
		std::string LastName;
		std::string Login;
		konga::NullableString Password;
		std::string Email;
	};

	struct Group {
		std::string GroupId;
		std::string GroupName;
		std::string Description;
	};

	struct UserGroup {
		std::string GroupId;
		std::vector<std::string> UserIds;
	};
}

namespace konga {

	class DbConnection;
	class GuidToIdMapper;

	std::string GetUserGuid(long lUserId, GuidToIdMapper& guidToIdMapper);
	std::string GetGroupGuid(long lGroupId, GuidToIdMapper& guidToIdMapper);

	long GetUserId(const string& stGuid, GuidToIdMapper& guidToIdMapper);
	long GetGroupId(const string& stGuid, GuidToIdMapper& guidToIdMapper);

	                                                                                    
	void GetUsersAndGroupsFromDb(std::vector<impl::User>& vUsers,
							std::vector<impl::Group>& vGroups,
							std::vector<impl::UserGroup>& vUserGroups,
							DbConnection& dbConnection,
							GuidToIdMapper& guidToIdMapper,
							const bool bIncludePasswords);

	bool DoesUserExist(const std::string& stLogin, DbConnection& dbConnection);

	bool DoesGroupExist(const std::string& stGroupName, DbConnection& dbConnection);

	                                                         
	bool Changed(const impl::User& oldUser, const User& newUser);

	                                                            
	bool Changed(const impl::Group& oldGroup, const Group& newGroup);

	void UpdateUser(const User& user, DbConnection& dbConnection, GuidToIdMapper& guidToIdMapper, const std::vector<std::string>& vFieldsToSkip);

	void UpdateUser(const User& user, DbConnection& dbConnection, GuidToIdMapper& guidToIdMapper);

	void UpdateGroup(const Group& group, DbConnection& dbConnection, GuidToIdMapper& guidToIdMapper);

	void UpdateUserGroup(const std::string& stGroupId, const std::vector<std::string>& stUserIds,
		DbConnection& dbConnection, GuidToIdMapper& guidToIdMapper);

	void InsertUserGroup(long lGroupId, const std::vector<std::string>& vAddedUserIds,
		DbConnection& dbConnection, GuidToIdMapper& guidToIdMapper);

	                      
	void InsertUser(const User& user, DbConnection& dbConnection, GuidToIdMapper& guidToIdMapper);

	                      
	void InsertGroup(const Group& group, DbConnection& dbConnection, GuidToIdMapper& guidToIdMapper);

	void DeleteUsers(const std::vector<std::string>& vIds, DbConnection& dbConnection, GuidToIdMapper& guidToIdMapper);

	void DeleteGroups(const std::vector<std::string>& vIds, DbConnection& dbConnection, GuidToIdMapper& guidToIdMapper);
}

#endif                              

