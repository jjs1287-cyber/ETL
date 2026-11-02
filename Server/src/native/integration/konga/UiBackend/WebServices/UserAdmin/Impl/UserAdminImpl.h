/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
#ifndef KONGA_WS_USERADMIN_USERADMINIMPL_H
#define KONGA_WS_USERADMIN_USERADMINIMPL_H

#include <map>

#include <konga/ItemAccess/UserInfo.h>

#include <konga/UiBackend/WebServices/Utils/WsImplBaseAuthenticator.h>

#include "../User.hpp"
#include "../User_Array.hpp"
#include "../Group_Array.hpp"
#include "../UserGroup_Array.hpp"
#include "../WsKeyValuePairs.hpp"

class UsersAndGroups;

namespace impl {
	struct User;
	struct Group;
	struct UserGroup;
}

namespace konga {

	class DbConnection;
	class GuidToIdMapper;

	class UserAdminImpl : public WsImplBaseAuthenticator {
	public:
		UserAdminImpl(const char* const user, const char* const password)
			: WsImplBaseAuthenticator(user, password), m_bIncludePasswords(false), m_pGuidToIdMapper(0) {}
		~UserAdminImpl(void);

		UsersAndGroups* GetUsersAndGroups();

		UsersAndGroups* GetUsersAndGroupsWithOptions(WsKeyValuePairs* options);

		void SetUsersAndGroups(const UsersAndGroups* const pUsersAndGroups);

		void SetUserInfo(const User* const user);

		User* GetUserInfo();

	protected:
		void HandleUsers(std::map<std::string, const impl::User*>& userMap,
			User_Array& updatedUsers, DbConnection& dbConnection);

		void HandleGroups(std::map<std::string, const impl::Group*>& groupMap,
			Group_Array& updatedGroups, DbConnection& dbConnection);

		void HandleUserGroups(std::map<std::string, const impl::UserGroup*> userGroupMap,
			UserGroup_Array& updatedUserGroups, DbConnection& dbConnection);


	private:
		bool m_bIncludePasswords;
		GuidToIdMapper* m_pGuidToIdMapper;
	};

}                   

#endif                                      

