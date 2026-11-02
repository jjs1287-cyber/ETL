/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#include <string>
#include <vector>
#include <map>
#include <sstream>
#include <algorithm>	              
#include <memory>		           

#include <konga/Utils/CrException.h>
#include <konga/Utils/PortDefs.h>	                        

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>
#include <konga/Db/DbUtils/AutoDbCleanup.h>

#include <konga/DbRelations/EntityIdConstants.h>

#include <konga/UiBackend/GuidMappings/GuidToIdMapper.h>
#include <konga/UiBackend/GuidMappings/DbGuidToIdMapper.h>
#include <konga/UiBackend/GuidMappings/GuidMappings.h>

#include <konga/UiBackend/WebServices/Utils/WsAllocations.h>
#include <konga/UiBackend/WebServices/Utils/UiBackendLogging.h>		                  

#include "../UsersAndGroups.hpp"

#include "UserAdminUtils.h"

#include "UserAdminImpl.h"

#include <konga/Utils/PortDefs.h>	                      

using namespace konga;
using std::string;
using std::vector;
using std::map;
using std::stringstream;
using std::auto_ptr;

UserAdminImpl::~UserAdminImpl(void)
{
	if ( m_pGuidToIdMapper != 0 ) {
		delete m_pGuidToIdMapper;
		m_pGuidToIdMapper = 0;
	}
}

namespace {
	DbConnection* getDbConnection() {
		return konga::GetBackEndDbConnection("TranDb");
	}

	class UserCreator {
	public:
		UserCreator(const vector<impl::User>& users) : m_users(users) {}

		int size() const {
			return static_cast<int>(m_users.size());
		}

		User* create(int i) {
			const impl::User& user = m_users.at(i);
			auto_ptr<User> pUser(new User());

			pUser->setUserId( const_cast<xsd__string>(user.UserId.c_str()), true );
			pUser->setFirstName( const_cast<xsd__string>(user.FirstName.c_str()), true );
			pUser->setLastName( const_cast<xsd__string>(user.LastName.c_str()), true );
			pUser->setLogin( const_cast<xsd__string>(user.Login.c_str()), true );
			if ( user.Password != 0 ) {
				pUser->setPassword( const_cast<xsd__string>(user.Password.const_ref().c_str()), true );
			}
			pUser->setEmail( const_cast<xsd__string>(user.Email.c_str()), true );

			return pUser.release();
		}

	private:
		const vector<impl::User>& m_users;
	};

	class GroupCreator {
	public:
		GroupCreator(const vector<impl::Group>& groups) : m_groups(groups) {}

		int size() const {
			return static_cast<int>(m_groups.size());
		}

		Group* create(int i) {
			const impl::Group& group = m_groups.at(i);
			auto_ptr<Group> pGroup(new Group());

			pGroup->setGroupId( const_cast<xsd__string>( group.GroupId.c_str()), true );
			pGroup->setGroupName( const_cast<xsd__string>( group.GroupName.c_str()), true );
			pGroup->setDescription( const_cast<xsd__string>( group.Description.c_str()), true );

			return pGroup.release();
		}

	private:
		const vector<impl::Group>& m_groups;
	};

	class UserGroupCreator {
	public:
		UserGroupCreator(const vector<impl::UserGroup>& userGroups) : m_userGroups(userGroups) {}

		int size() const {
			return static_cast<int>(m_userGroups.size());
		}

		UserGroup* create(int i) {
			const impl::UserGroup& userGroup = m_userGroups.at(i);
			auto_ptr<UserGroup> pUserGroup(new UserGroup());

			pUserGroup->setGroupId(const_cast<xsd__string>(userGroup.GroupId.c_str()), true );
			pUserGroup->setUserIds( new ArrayOf_xsd_string() );
			allocate_xsd__string_Array(userGroup.UserIds, *pUserGroup->UserIds->item);

			return pUserGroup.release();
		}

	private:
		const vector<impl::UserGroup>& m_userGroups;
	};

	                                                                    
	UsersAndGroups* allocateUsersAndGroups(const vector<impl::User>& users,
		const vector<impl::Group>& groups,
		const vector<impl::UserGroup>& userGroups)
	{
		auto_ptr<UsersAndGroups> pUsersAndGroups(new UsersAndGroups());

		UserCreator userCreator(users);
		pUsersAndGroups->setUsers(
			allocateArrayOf<ArrayOfUser, User_Array, User, UserCreator>(userCreator)
			);

		GroupCreator groupCreator(groups);
		pUsersAndGroups->setGroups(
			allocateArrayOf<ArrayOfGroup, Group_Array, Group, GroupCreator>(groupCreator)
			);

		UserGroupCreator creator(userGroups);
		pUsersAndGroups->setUserGroups(
			allocateArrayOf<ArrayOfUserGroup, UserGroup_Array, UserGroup, UserGroupCreator>(creator)
			);

		return pUsersAndGroups.release();
	}
}

UsersAndGroups* UserAdminImpl::GetUsersAndGroups() {
	return GetUsersAndGroupsWithOptions(0);
}

namespace {
	bool GetWsKeyValueAsBool(WsKeyValuePairs* options, const string& key, bool bDefault) {
		ArrayOf_tns2_WsKeyValue* value = options->getkeyValues();
		if ( value == 0 )
			return bDefault;

		WsKeyValue_Array* wsKeyValue_Array = value->getitem();
		if ( wsKeyValue_Array == 0 )
			return bDefault;

		int size = 0;
		WsKeyValue** arr = wsKeyValue_Array->get(size);
		if ( size == 0 || arr == 0 )
			return bDefault;

		for( int i = 0; i < size; ++i ) {
			WsKeyValue* keyValue = arr[i];
			if ( strcasecmp(keyValue->getkey(), key.c_str()) == 0 )
				return strcmp(keyValue->getvalue(), "1") == 0;
		}
		return false;
	}
}

UsersAndGroups* UserAdminImpl::GetUsersAndGroupsWithOptions(WsKeyValuePairs* options) {
	if ( options ) {
		if ( m_UserInfo.IsAdministrator() ) {
			m_bIncludePasswords = GetWsKeyValueAsBool(options, "IncludePasswords", false);
		}
		else {
			m_bIncludePasswords = false;
		}
	}
	SmartDbPtr<DbConnection> pConnection( getDbConnection() );

	                                                                           
	m_pGuidToIdMapper = GetDefaultGuidToIdMapper();

	vector<impl::User> vUsers;
	vector<impl::Group> vGroups;
	vector<impl::UserGroup> vUserGroup;
	GetUsersAndGroupsFromDb(vUsers, vGroups, vUserGroup, *pConnection, *m_pGuidToIdMapper, m_bIncludePasswords);

	UIBACKEND_TRACE("About to return from GetUsersAndGroups");
	return allocateUsersAndGroups(vUsers, vGroups, vUserGroup);
}

namespace {
	typedef map<string, const impl::User*> UserMapT;
	typedef map<string, const impl::Group*> GroupMapT;

	                                                                           
	vector<string> getDifference(const vector<string>& vKept, UserMapT& userMap) {
		vector<string> retval;
		UserMapT::const_iterator cit = userMap.begin();
		for(; cit != userMap.end(); ++cit) {
			if ( std::find(vKept.begin(), vKept.end(), cit->second->UserId) == vKept.end() )
				retval.push_back(cit->second->UserId);
		}
		return retval;
	}

	vector<string> getDifference(const vector<string>& vKept, GroupMapT& groupMap) {
		vector<string> retval;
		GroupMapT::const_iterator cit = groupMap.begin();
		for(; cit != groupMap.end(); ++cit) {
			if ( std::find(vKept.begin(), vKept.end(), cit->second->GroupId) == vKept.end() )
				retval.push_back(cit->second->GroupId);
		}
		return retval;
	}
}

void UserAdminImpl::HandleUsers(map<string, const impl::User*>& userMap,
								User_Array& updatedUsers,
								DbConnection& dbConnection)
{
	int iUsers = -1;
	User** arrUsers = updatedUsers.get(iUsers);

	UIBACKEND_TRACE("HandleUsers called with iUsers = " + toString(iUsers));
	vector<string> vKeptUsers;
	for( int i = 0; i < iUsers; i++ ) {
		User& newUser = *arrUsers[i];

		                                     
		UserMapT::iterator it = userMap.find(newUser.getUserId());
		if ( it != userMap.end() )
			vKeptUsers.push_back(newUser.getUserId());
	}

	vector<string> vDeletedUsers = getDifference(vKeptUsers, userMap);
	DeleteUsers(vDeletedUsers, dbConnection, *m_pGuidToIdMapper);

	                      
	for( int i = 0; i < iUsers; i++ ) {
		User& newUser = *arrUsers[i];

		                                     
		UserMapT::iterator it = userMap.find(newUser.getUserId());
		if ( it != userMap.end() ) {
			                                                   
			const impl::User& oldUser = *it->second;
			if ( Changed(oldUser, newUser) ) {
				stringstream ssSql;
				if ( strcasecmp(newUser.Login, oldUser.Login.c_str()) != 0 ) {
					if ( DoesUserExist(newUser.Login, dbConnection) )
						throw CrException("The user " + string(newUser.Login) + " already exists on the system. Pick another name.");
				}
				UpdateUser(newUser, dbConnection, *m_pGuidToIdMapper);
			}
		}
		else {
			InsertUser(newUser, dbConnection, *m_pGuidToIdMapper);
		}
	}
}

void UserAdminImpl::HandleGroups(map<string, const impl::Group*>& groupMap,
				  Group_Array& updatedGroups,
				  DbConnection& dbConnection)
{
	int iGroups = -1;
	Group** arrGroups = updatedGroups.get(iGroups);
	UIBACKEND_TRACE("HandleGroups called with iGroups = " + toString(iGroups));

	vector<string> vKeptGroups;
	for( int i = 0; i < iGroups; i++ ) {
		Group& newGroup = *arrGroups[i];

		                                      
		GroupMapT::iterator it = groupMap.find(newGroup.getGroupId());
		if ( it != groupMap.end() )
			vKeptGroups.push_back(newGroup.getGroupId());
	}

	vector<string> vDeletedGroups = getDifference(vKeptGroups, groupMap);
	DeleteGroups(vDeletedGroups, dbConnection, *m_pGuidToIdMapper);

	                       
	for( int i = 0; i < iGroups; i++ ) {
		Group& newGroup = *arrGroups[i];

		                                      
		GroupMapT::iterator it = groupMap.find(newGroup.getGroupId());
		if ( it != groupMap.end() ) {
			                                                    
			const impl::Group& oldGroup = *it->second;
			if ( Changed(oldGroup, newGroup) ) {
				if ( strcasecmp(newGroup.GroupName, oldGroup.GroupName.c_str()) != 0 ) {
					if ( DoesGroupExist(newGroup.GroupName, dbConnection) )
						throw CrException("The group " + string(newGroup.GroupName) + " already exists on the system. Pick another name.");
				}
				UpdateGroup(newGroup, dbConnection, *m_pGuidToIdMapper);
			}
		}
		else {
			InsertGroup(newGroup, dbConnection, *m_pGuidToIdMapper);
		}
	}
}

void UserAdminImpl::HandleUserGroups(map<string, const impl::UserGroup*> userGroupMap,
									 UserGroup_Array& updatedUserGroups, DbConnection& dbConnection)
{
	typedef std::map<std::string, const impl::UserGroup*> UserGroupMapT;
	int nUserGroups = -1;
	UserGroup** arrUserGroups = updatedUserGroups.get(nUserGroups);
	UIBACKEND_TRACE("HandleUserGroups called with nUserGroups = " + toString(nUserGroups));

	vector<string> vHandledGroups;

	                                     
	for( int i = 0; i < nUserGroups; ++i ) {
		UserGroup& updatedUserGroup = *arrUserGroups[i];
		if ( updatedUserGroup.GroupId == 0 )
			throw CrException("Invalid Argument: GroupId is NULL");
		string stGroupId( updatedUserGroup.getGroupId() );
		bool bUpdate = false;

		int iUserIds = -1;
		const xsd__string* arUserIds = updatedUserGroup.UserIds->item->get(iUserIds);
		const unsigned int nUserIds = iUserIds;

		                                            
		map<string, const impl::UserGroup*>::const_iterator citUserGroup = userGroupMap.find(stGroupId);
		const vector<string>* pOldUserIds = 0;
		if ( citUserGroup == userGroupMap.end() ) {
			bUpdate = true;
		}
		else {
			pOldUserIds = &(citUserGroup->second->UserIds);
			                                          
			if ( pOldUserIds->size() != nUserIds )
				bUpdate = true;
		}

		                                                          
		vector<string> vUserIds;
		for( unsigned int j = 0; j < nUserIds; ++j ) {
			string stUserId( arUserIds[j] );

			                                                               
			if( !bUpdate && pOldUserIds != 0 ) {
				if ( find(pOldUserIds->begin(), pOldUserIds->end(), stUserId) == pOldUserIds->end() ) {
					                                     
					bUpdate = true;
				}
			}

			vUserIds.push_back(stUserId);
		}
		if ( bUpdate )
			UpdateUserGroup(stGroupId, vUserIds, dbConnection, *m_pGuidToIdMapper);

		vHandledGroups.push_back(stGroupId);
	}

	                                                                               
	UserGroupMapT::const_iterator cit = userGroupMap.begin();
	vector<string> vEmpty(0);
	for(; cit != userGroupMap.end(); ++cit ) {
		const string& stGroupId = cit->first;
		if ( std::find(vHandledGroups.begin(), vHandledGroups.end(), stGroupId) == vHandledGroups.end() ) {
			                                                    
			                                                                                 
			if ( m_pGuidToIdMapper->GetId(stGroupId, GroupEntityId) != GuidToIdMapper::invalid_id ) {
				UpdateUserGroup(stGroupId, vEmpty, dbConnection, *m_pGuidToIdMapper);
			}
		}
	}
}

void UserAdminImpl::SetUsersAndGroups(const UsersAndGroups* const pUsersAndGroups) {
	if ( pUsersAndGroups == 0 )
		throw CrException("Illegal Argument: Null was passed as argument to SetUsersAndGroups.");

	if ( !m_UserInfo.IsAdministrator() )
		throw CrException("Permission denied: Only Administrators can change properties for users and groups.\n" \
		"Regular users can change their login information in the Update User Profile dialog.");

	SmartDbPtr<DbConnection> pConnection( getDbConnection() );
	pConnection->SetTransactionIsolationLevel(konga::SERIALIZABLE);
	pConnection->SetAutoCommit(false);
	AutoRollbackTransaction adopt_transaction(pConnection);

	m_pGuidToIdMapper = new DbGuidToIdMapper(pConnection);

	vector<impl::User> vUsers;
	vector<impl::Group> vGroups;
	vector<impl::UserGroup> vUserGroup;
	UIBACKEND_TRACE("Calling GetUsersAndGroupsFromDb");
	GetUsersAndGroupsFromDb(vUsers, vGroups, vUserGroup, *pConnection, *m_pGuidToIdMapper, false);
	UIBACKEND_TRACE("Got UsersAndGroups from the database.");

	                                                   
	map<string, const impl::User*> userMap;
	map<string, const impl::Group*> groupMap;
	map<string, const impl::UserGroup*> userGroupMap;	                            

	vector<impl::User>::const_iterator citUser = vUsers.begin();
	for(; citUser != vUsers.end(); ++citUser ) {
		userMap[citUser->UserId] = &(*citUser);
	}

	vector<impl::Group>::const_iterator citGroup = vGroups.begin();
	for(; citGroup != vGroups.end(); ++citGroup ) {
		groupMap[citGroup->GroupId] = &(*citGroup);
	}

	vector<impl::UserGroup>::const_iterator citUserGroup = vUserGroup.begin();
	for(; citUserGroup != vUserGroup.end(); ++citUserGroup ) {
		userGroupMap[citUserGroup->GroupId] = &(*citUserGroup);
	}

	HandleUsers(userMap, *pUsersAndGroups->Users->item, *pConnection);
	HandleGroups(groupMap, *pUsersAndGroups->Groups->item, *pConnection);
	HandleUserGroups(userGroupMap, *pUsersAndGroups->UserGroups->item, *pConnection);
 
	adopt_transaction.Commit();
	auditLog(m_UserInfo.GetLogin() + " uploaded new user/group information");
	UIBACKEND_TRACE("SetUsersAndGroups - Committed to the database");
}

void UserAdminImpl::SetUserInfo(const User* const user) {
	if ( user == 0 )
		throw CrException("Invalid Argument: user cannot be NULL.");

	                                                          
	                                                                        
	                                                                           
	                    
	const char* userGuid = user->UserId;
	const string stUserGuid( userGuid==0 ? "" : userGuid );

	m_pGuidToIdMapper = GetDefaultGuidToIdMapper();
	const long lUserId = m_pGuidToIdMapper->GetId(stUserGuid, UserEntityId);
	if ( lUserId == GuidToIdMapper::invalid_id )
		throw CrException("The user does not exist on the server.");

	const char* userToChange = user->Login;
	const string stUserToChange(userToChange==0 ? "" : userToChange);
	if ( stUserToChange.empty() )
		throw CrException("The user name to change is empty.");

	const char* newPassword = user->Password;
	const string stNewPassword(newPassword==0 ? "" : newPassword);

	if ( !m_UserInfo.IsAdministrator() ) {
		                                                                                 
		if ( m_UserInfo.GetId() != lUserId )
			throw CrException("User " + m_UserInfo.GetLogin() + " does not have permission to change the information for user " + stUserToChange + ".");
		if ( strcasecmp(m_UserInfo.GetLogin().c_str(), stUserToChange.c_str()) != 0 )
			throw CrException("User " + m_UserInfo.GetLogin() + " does not have permission to change the information for user " + stUserToChange + ".");
	}

	SmartDbPtr<DbConnection> pConnection( getDbConnection() );

	vector<string> vSkipLogin(1);
	vSkipLogin[0] = "login";
	UpdateUser(*user, *pConnection, *m_pGuidToIdMapper, vSkipLogin);

	auditLog(m_UserInfo.GetLogin() + " changed user information for " + string(user->Login));
}

User* UserAdminImpl::GetUserInfo() {
	auto_ptr<User> pUser( new User() );

	m_pGuidToIdMapper = GetDefaultGuidToIdMapper();

	pUser->setUserId(
		const_cast<xsd__string>(m_pGuidToIdMapper->GetGuid(m_UserInfo.GetId(), UserEntityId).c_str()),
		true);
	pUser->setLogin(const_cast<xsd__string>(m_UserInfo.GetLogin().c_str()), true );
	pUser->setFirstName(const_cast<xsd__string>(m_UserInfo.GetFirstName().c_str()), true);
	pUser->setLastName(const_cast<xsd__string>(m_UserInfo.GetLastName().c_str()), true);
	pUser->setEmail(const_cast<xsd__string>(m_UserInfo.GetEmail().c_str()), true);
	pUser->Password = 0;
	return pUser.release();
}

