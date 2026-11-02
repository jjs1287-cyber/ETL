/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_ITEMACCESS_GROUP_INFO_H
#define KONGA_ITEMACCESS_GROUP_INFO_H

#include <string>

namespace konga {

	class DbConnection;

	class GroupInfo {
	public:
		explicit GroupInfo(long iGroupId, const std::string& stGroupName = "", const std::string& stDescription = "");
		virtual ~GroupInfo(void);

		long GetId() const;

		bool IsAdministrator() const;

		std::string GetName() const;

	private:
		long m_iGroupId;
		std::string m_stGroupName;
		std::string m_stDescription;
	};

	GroupInfo GetGroupInfo(long lGroupId, DbConnection& dbConnection);
}

#endif                                 

