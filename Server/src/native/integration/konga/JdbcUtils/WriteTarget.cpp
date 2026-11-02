/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <konga/Utils/CrException.h>
#include <konga/Utils/FileUtils.h>

#include <konga/Db/DbUtils/SmartDbPtr.h>
#include <konga/Db/DbUtils/DbUtils.h>

#include <konga/DbRelations/EntityIdConstants.h>

#include <konga/JavaBackend/JavaBackendConfiguration/ConfigUtils.h>

#include <konga/UiBackend/GuidMappings/DbGuidToIdMapper.h>

#include "JdbcEngineWrapper.h"
#include "WriteTarget.h"

using namespace konga;
using std::string;

void konga::WriteJdbcTarget(const UserInfo& userInfo, long lTargetId,
							const DbConnectionInfoStruct& connectionParams,
							int insertUpdateModeFlag, bool bTransaction,
							const string& sPreTargetSql, bool bIncludeNulls,
							const string& sJtrFileName, const string& sDataFileName,
							const string& sStructureFileName, const std::string& sDebugLogFile,
							string& sWarnings)
{
	SmartDbPtr<DbConnection> pTranDbConn( konga::GetBackEndDbConnection("TranDb") );
	string user, md5pwd;
	GetCredentials(*pTranDbConn, userInfo, user, md5pwd);

	string targetGUID;
	{
		DbGuidToIdMapper guidMapper(pTranDbConn);
		if(insertUpdateModeFlag==MODE_SOURCE_UPDATE){
			targetGUID = guidMapper.GetGuid(lTargetId, SourceEntityId);
		}
		else {
			targetGUID = guidMapper.GetGuid(lTargetId, TargetEntityId);
		}
		if ( targetGUID.empty() ) {
			throw CrException("The target has not been deployed.");
		}
	}

	                                                            
	pTranDbConn.CloseAndDelete();

	JdbcEngineWrapper jdbc(user, md5pwd);
	jdbc.writeTarget(connectionParams, targetGUID, insertUpdateModeFlag, bTransaction, sPreTargetSql, bIncludeNulls, 
		sJtrFileName, sDataFileName, sDebugLogFile, sWarnings);
	std::string sDataFileName_new=sDataFileName+".final";
	if(konga::FileExists(sDataFileName_new)){
		if(konga::FileExists(sDataFileName))konga::kMoveFile(sDataFileName, sDataFileName+".bak", true);
		konga::kMoveFile(sDataFileName_new, sDataFileName, true);
	}
}
