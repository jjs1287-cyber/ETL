/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef KONGA_AUTHENTICATION_H
#define KONGA_AUTHENTICATION_H

#include <string>

#include <konga/Utils/CrException.h>
#include <konga/ItemAccess/UserInfo.h>

namespace konga {

	class DbConnection;

	namespace Authentication {

	class AuthenticationException : public CrException {
	public:
		AuthenticationException(const std::string& stMsg, const std::string& stUserName) : 
			CrException(stMsg), m_stUserName(stUserName) {}
		virtual ~AuthenticationException(void) {}

		std::string GetUserName() const {
			return m_stUserName;
		}

	private:
		std::string m_stUserName;
	};

	                                                                                 
	bool IsValidUser(const std::string& stUserName, const std::string& sMd5HashedPassword, int& iUserId, DbConnection* pTranDbConnection = 0);

	bool IsValidUser(const std::string& stUserName, const std::string& sMd5HashedPassword);

	                                                                           
	                                                                   
	UserInfo GetValidUser(const std::string& stUserName, const std::string& stPassword, DbConnection* pTranDbConnection = 0);

	}                            
}                   

#endif                          

