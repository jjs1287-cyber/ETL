/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef KONGA_ITEMACCESS_USER_INFO_H
#define KONGA_ITEMACCESS_USER_INFO_H

#include <string>
#include <vector>

#include "GroupInfo.h"

namespace konga {
	class DbConnection;

	class UserInfo {
	public:
		static const long UNDEFINED_UID = -999999;
		static const long ANONYMOUS_UID = -2;
	public:
		UserInfo(void) : m_iUserId(UNDEFINED_UID) {}
		explicit UserInfo(long iUserId, const std::string& stLogin = "", const std::string& stFirstName = "",
			const std::string& stLastName = "", const std::string& stEmail = "");
		UserInfo(long iUserId, const std::vector<GroupInfo>& vGroups, const std::string& stLogin = "",
			const std::string& stFirstName = "", const std::string& stLastName = "",
			const std::string& stEmail = "");
		virtual ~UserInfo(void);

		bool isInitialized() const {
			return m_iUserId != UNDEFINED_UID;
		}

		long GetId() const;

		   
                                                                             
                                                  
     
		bool IsAdministrator() const;

		   
                                                               
     
		bool IsAdminUser() const;

		std::string GetLogin() const;

		std::string GetFirstName() const;

		std::string GetLastName() const;

		std::string GetEmail() const;

		int GetNumberOfGroups() const;

		std::vector<GroupInfo> GetGroups() const;

		std::vector<long> GetGroupIds() const;

	private:
		long m_iUserId;
		std::string m_stLogin;
		std::string m_stFirstName;
		std::string m_stLastName;
		std::string m_stEmail;
		std::vector<GroupInfo> m_vGroups;
	};

	                                                  
	UserInfo GetUserInfo(long iUserId, DbConnection& dbConnection, bool bIncludeGroupInfo = true);

	                                                    
	UserInfo GetAdminUserInfo(DbConnection& dbConnection, bool bIncludeGroupInfo = true);

	                                                    
	GroupInfo GetGroupInfo(long lGroupId, DbConnection& dbConnection);

}

#endif                                

