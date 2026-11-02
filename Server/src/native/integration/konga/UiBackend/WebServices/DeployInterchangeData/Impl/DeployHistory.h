/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef KONGA_WS_DEPLOY_DEPLOY_HISTORY_H
#define KONGA_WS_DEPLOY_DEPLOY_HISTORY_H

namespace konga {
	class DbConnection;
	class UserInfo;
	class GuidToIdMapper;

	void AddToDeployHistory(int iEntityType, long lItemId, const UserInfo& userInfo,
		DbConnection& dbConn, const GuidToIdMapper& guidToIdMapper);

	void GetLastDeployHistory(int iEntityType, long lItemId, std::string& stUserGuid, std::string& stISO8601TimeStamp,
		DbConnection& dbConn, const GuidToIdMapper& guidToIdMapper);

}

#endif                                    

