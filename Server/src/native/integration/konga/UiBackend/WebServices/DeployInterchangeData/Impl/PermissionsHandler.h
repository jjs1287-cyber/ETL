/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_WS_DEPLOY_PERMISSIONS_HANDLER_H
#define KONGA_WS_DEPLOY_PERMISSIONS_HANDLER_H

#include <vector>
#include <string>

#include "ItemWarningHolder.h"

class PermissionsHolder;

namespace konga {

	class DbConnection;
	class UserAccess;

namespace interchange_deploy {

	class PermissionsHandler : public ItemWarningHolder {
	public:
		PermissionsHandler(DbConnection& dbConnection,
			UserAccess& currentUserAccess, GuidToIdMapper& guidToIdMapper)
			: m_dbConnection(dbConnection), m_currentUserAccess(currentUserAccess),
			m_guidToIdMapper(guidToIdMapper)
		{}
		~PermissionsHandler(void) {}

		void HandlePermissions(PermissionsHolder& permissionsHolder);

	private:
		DbConnection& m_dbConnection;
		UserAccess& m_currentUserAccess;
		GuidToIdMapper& m_guidToIdMapper;
	};

}                   
}                                


#endif                                         

