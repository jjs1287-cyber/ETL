/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_ITEM_ACCESS_GROUP_ACCESS_H
#define KONGA_ITEM_ACCESS_GROUP_ACCESS_H

#include <konga/Utils/CrException.h>

#include "GroupInfo.h"

#include "AccessControl.h"

namespace {
	const std::string stID_NAME("Group_ID");
}

namespace konga {

	class GroupAccess : public AccessControl
	{
	public:
		GroupAccess(int iGroupId, DbConnection& dbConnection)
			: AccessControl(dbConnection), m_GroupInfo(iGroupId) {}

		GroupAccess(GroupInfo groupInfo, DbConnection& dbConnection)
			: AccessControl(dbConnection), m_GroupInfo(groupInfo) {}

		virtual ~GroupAccess(void) {}

	protected:

		int GetId() const {
			return m_GroupInfo.GetId();
		}

		bool IsAdministrator() const {
			return m_GroupInfo.IsAdministrator();
		}

		bool IsAdminUser() const {
			                                       
			return false;
		}

		void VerifyAllowBits(AllowBits allowBits) const {
			if ( (allowBits &  ACCESS_BIT_OWNER) != 0 )
				throw CrException("A group can't be the owner of an item.");
		}

		void VerifyDenyBits(DenyBits denyBits) const {
			if ( (denyBits &  ACCESS_BIT_OWNER) != 0 )
				throw CrException("A group can't deny ownership.");
		}

		std::string GetTableName(int iEntityTypeId) const;

		const std::string& GetIdName() const {
			return stID_NAME;
		}

	private:
		GroupInfo m_GroupInfo;
	};
}
#endif                                    

