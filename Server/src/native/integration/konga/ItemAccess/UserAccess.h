/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
#ifndef KONGA_WS_USER_ACCESS_H
#define KONGA_WS_USER_ACCESS_H

#include <vector>
#include <string>

#include <konga/Utils/CrException.h>

#include "UserInfo.h"

#include "AccessControl.h"

namespace {
	const std::string stID_NAME_USER("User_ID");
}

namespace konga {

	UserInfo GetItemOwner(DbConnection& dbConnection, long lItemId, int iEntityTypeId);

	class UserAccess : public AccessControl
	{
	public:
		   
                                                              
     
		static UserAccess GetUserAccessWithGroup(int iUserId, DbConnection& dbConnection);

		                                    
		static UserAccess* GetUserAccessWithGroup(int iUserId, DbConnection* pDbConnection);

	public:
		UserAccess(const UserInfo& userInfo, DbConnection& dbConnection);
		~UserAccess(void) {}

		   
                                                                       
     
		void GetPermissions(long iItemId, int iEntityTypeId, AllowBits& allowBits, DenyBits& denyBits) const;

		   
                                                                  
                                                                        
     
		void GetPermissions(long iItemId, int iEntityTypeId, AllowBits& allowBits, DenyBits& denyBits, bool bIncludeGroups) const;

		   
                        
                                                                               
     
		void AddItemOwner(long iItemId, int iEntityTypeId);

		   
                        
                                                                                  
     
		void ChangeOwner(long iItemId, int iEntityTypeId);

		   
                        
                                                                            
     
		bool IsOwner(long iItemId, int iEntityTypeId) const;

		std::vector<long> GetGroups() const {
			return m_UserInfo.GetGroupIds();
		}

		                                                                                                     
		                                  
		std::string GetSelectSql(const std::string& stMainSql, const std::string& stJoinIdName, const int iEntityTypeId) const;

	protected:
		int GetId() const {
			return m_UserInfo.GetId();
		}

		bool IsAdministrator() const {
			return m_UserInfo.IsAdministrator();
		}

		bool IsAdminUser() const {
			return m_UserInfo.IsAdminUser();
		}

		void VerifyAllowBits(AllowBits allowBits) const {
			                   
		}

		void VerifyDenyBits(DenyBits denyBits) const {
			if ( (denyBits &  ACCESS_BIT_OWNER) != 0 )
				throw CrException("The owner bit can not be explicitly denied.");
		}

		std::string GetTableName(int iEntityId) const;
		
		const std::string& GetIdName() const {
			return stID_NAME_USER;
		}

	private:
		UserInfo m_UserInfo;
		mutable std::string m_stGroupInClause;
	};

	                       
	                                                                                   
	  
	bool UserHasReadPermission(long iUserId, long iItemId, int iEntityTypeId);
	bool UserHasExecutePermission(long iUserId, long iItemId, int iEntityTypeId);
	bool UserHasWritePermission(long iUserId, long iItemId, int iEntityTypeId);
}

#endif                          

