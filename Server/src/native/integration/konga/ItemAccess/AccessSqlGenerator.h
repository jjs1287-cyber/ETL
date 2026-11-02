/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <string>

namespace konga {

	class UserInfo;

	                                                 
	std::string GetOwnerJoin(const UserInfo& userInfo, int iEntityType, const std::string& stMainIdName);

	std::string GetUserReadAllowJoin(const UserInfo& userInfo, int iEntityType, const std::string& stMainIdName);

	std::string GetUserReadDenyJoin(const UserInfo& userInfo, int iEntityType, const std::string& stMainIdName);

	std::string GetGroupReadAllowJoin(const UserInfo& userInfo, int iEntityType, const std::string& stMainIdName);

	std::string GetGroupReadDenyJoin(const UserInfo& userInfo, int iEntityType, const std::string& stMainIdName);

	std::string GetSelectSqlWithAccessJoin_old(const UserInfo& userInfo, const std::string& stMainSql, const std::string& stJoinIdName, const int iEntityTypeId);

	std::string GetOwnerSelect(const UserInfo& userInfo, const std::string& stMainIdName, const int iEntityTypeId);

	std::string GetUserAllowSelect(const UserInfo& userInfo, const std::string& stMainIdName, const int iEntityTypeId);
	std::string GetUserDenySelect(const UserInfo& userInfo, const std::string& stMainIdName, const int iEntityTypeId);

	std::string GetGroupAllowSelect(const UserInfo& userInfo, const std::string& stMainIdName, const int iEntityTypeId);
	std::string GetGroupDenySelect(const UserInfo& userInfo, const std::string& stMainIdName, const int iEntityTypeId);

	std::string GetAllowSelect(const UserInfo& userInfo, const std::string& stMainIdName, const int iEntityTypeId);
	std::string GetDenySelect(const UserInfo& userInfo, const std::string& stMainIdName, const int iEntityTypeId);

	                                                                     
	std::string GetAccessControlWhereClause(const UserInfo& userInfo, const std::string& stMainIdName, const int iEntityTypeId);
}                   

