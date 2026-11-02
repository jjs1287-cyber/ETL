/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <konga/Utils/CrException.h>

#include <konga/JavaBackend/JavaBackendConfiguration/ConfigUtils.h>

#include <konga/DbRelations/EntityIdConstants.h>

#include <konga/UiBackend/GuidMappings/DbGuidToIdMapper.h>

#include "JdbcEngineWrapper.h"
#include "ReadSource.h"

using namespace konga;
using std::string;

void konga::ReadJDBCSource(DbConnection& dbConn, const UserInfo& userInfo, long lSourceId,
						   const DbConnectionInfoStruct& connParams,
						   const string& sAdHocFilter,
						   const string& sJtrFileName,
						   const string& sDataFileName,
						   const string& sStructureFileName,
						   const string& sDebugLogFile)
{
	string user, md5pwd;
	GetCredentials(dbConn, userInfo, user, md5pwd);

	DbGuidToIdMapper guidMapper(&dbConn);
	const string& sourceGUID = guidMapper.GetGuid(lSourceId, SourceEntityId);
	if ( sourceGUID.empty() ) {
		throw CrException("The source has not been deployed.");
	}

	JdbcEngineWrapper jdbc(user, md5pwd);
	jdbc.readSource(connParams, sourceGUID, sAdHocFilter, sJtrFileName, sDataFileName, sStructureFileName, sDebugLogFile);
}

