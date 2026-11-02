/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */

#include <konga/Utils/StringUtils.h>		           
#include <konga/Utils/DbTypeUtils.h>
#include <konga/Utils/DateUtils.h>

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/DynamicDbRecordSet.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>

#include <konga/DbRelations/EntityIdConstants.h>

#include <konga/ItemAccess/UserInfo.h>

#include <konga/UiBackend/GuidMappings/GuidToIdMapper.h>

#include "DeployHistory.h"

using namespace konga;
using std::string;

void konga::AddToDeployHistory(int iEntityType, long lItemId, const UserInfo& userInfo,
							   DbConnection& dbConn, const GuidToIdMapper& guidToIdMapper)
{
	const long lUserId = userInfo.GetId();
	const string& userGuid = guidToIdMapper.GetGuid(lUserId, UserEntityId);

	const CrDateTime& now = CrDateTime::GetCurrentUtcTime();

	const string sql("INSERT INTO DeployHistory (Item_Id, EntityType_Id, User_Guid, DeployTime) VALUES ("
		+ toString(lItemId) + "," + toString(iEntityType) + ",'" + userGuid + "','" + now.toString("yyyy-mm-dd HH:MM:SS") + "')");

	dbConn.ExecuteSql(sql);
}

void konga::GetLastDeployHistory(int iEntityTypeId, long lItemId, string& stUserGuid, string& stISO8601TimeStamp,
								 DbConnection& dbConn, const GuidToIdMapper& guidToIdMapper)
{
	const string sql("SELECT User_Guid, DeployTime FROM DeployHistory WHERE Item_Id = " + toString(lItemId)
		+ " AND EntityType_Id = " + toString(iEntityTypeId)
		+ " ORDER BY DeployHistory_Id DESC LIMIT 1");

	SmartDbPtr<DynamicDbRecordSet> rs( DynamicDbRecordSet::MakeInstance(&dbConn, sql) );
	rs->Open();
	if ( !rs->Next() )
		return;

	stUserGuid = rs->GetFieldValue(0);
	const TIMESTAMP_STRUCT t = rs->GetFieldValueAsTimeStampStruct(1);
	stISO8601TimeStamp = konga::GetISO8601TimeStampString(t);
}

