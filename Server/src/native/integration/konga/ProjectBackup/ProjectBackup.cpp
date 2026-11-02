/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#include <vector>

#include <konga/Utils/FileUtils.h>
#include <konga/Utils/DateUtils.h>
#include <konga/Utils/util.h>		                  
#include <konga/Utils/CrException.h>
#include <konga/Utils/Settings.h>
#include <konga/Utils/ErrorResolver.h>
#include <konga/Utils/AutoFileCleanup.h>	                          

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>
#include <konga/Db/DbUtils/DynamicDbRecordSet.h>

#include <konga/Server/Server.h>

#include "ProjectBackup.h"

using namespace konga;
using std::string;
using std::vector;

namespace {
	const string PROJECT_BACKUP_TAB_NAME("ProjectBackupTab");

	const string PROJECT_BACKUP_TAB_COLS(
		"(InterchangeProject_Guid, UploadedBy_User_Guid, UploadTime, FileSize, Tag, UploadedFilename)"
		);

	const string ORDER_BY_CLAUSE(" ORDER BY ProjectBackup_Id DESC");

	string GetJpkFileName(const string& stUserGuid, const string& stProjectGuid) {
		string stFileName(GetUploadedProjectsBaseDirectoryWithSlash() + stUserGuid);
		if ( !IsDirectory(stFileName) ) kCreateDirectory(stFileName);
		AppendPathSeparator(stFileName);

		stFileName += stProjectGuid;
		if ( !IsDirectory(stFileName) ) kCreateDirectory(stFileName);
		AppendPathSeparator(stFileName);
		
		const CrDateTime d((CrDateTime::GetCurrentTime)());
		const string stDateTag(d.toString("yyyy-mm-dd"));
		stFileName += stDateTag;
		if ( !IsDirectory(stFileName) ) kCreateDirectory(stFileName);
		AppendPathSeparator(stFileName);
		stFileName += GetGUIDString();
		stFileName += ".jpk";
		return stFileName;
	}

	struct BackupPolicy {
		BackupPolicy(int MaxNumberOfBackups_)
			: MaxNumberOfBackups(MaxNumberOfBackups_) {}

		int MaxNumberOfBackups;
	};

	BackupPolicy GetBackupPolicy() {
		const string section("ProjectBackupPolicy");
		return BackupPolicy(
			GetOptionAsInt(section, "MaxNumberOfBackups", 50)
			);
	}

	void RemoveOldBackups(const std::string& projectGuid, const std::string& userGuid,
		const BackupPolicy& policy, DbConnection& dbConnection) {
		if ( policy.MaxNumberOfBackups <= 0 )
			return;	                     

		string sql("SELECT ProjectBackup_Id, UploadedFilename FROM " + PROJECT_BACKUP_TAB_NAME
			+ " WHERE interchangeproject_guid = '" + projectGuid
			+ "' AND uploadedby_user_guid = '" + userGuid
			+ "' AND (Tag IS NULL OR Tag = '')"
			+ ORDER_BY_CLAUSE);

		SmartDbPtr<DynamicDbRecordSet> rsBackups(DynamicDbRecordSet::MakeInstance(&dbConnection, sql));
		rsBackups->Open();
		int iKeepers = 0;
		string stDeleteIds;
		AutoDeleteFileSet deleteUs(false, false);
		while( rsBackups->Next() ) {
			if ( iKeepers >= policy.MaxNumberOfBackups ) {
				if ( !stDeleteIds.empty() ) stDeleteIds += ",";
				stDeleteIds += rsBackups->GetFieldValue(0);
				deleteUs.AddFile( getAbsolutePath(rsBackups->GetFieldValue(1)) );
			}
			else {
				++iKeepers;
			}
		}
		rsBackups.CloseAndDelete();
		
		if ( stDeleteIds.empty() )
			return;

		sql = "DELETE FROM " + PROJECT_BACKUP_TAB_NAME
			+ " WHERE ProjectBackup_Id IN (" + stDeleteIds + ")";
		dbConnection.ExecuteSql(sql);
		deleteUs.SetAutoDelete(true);
	}
}

void ProjectBackup::SaveBackup(const string& stProjectGuid,
							   const std::string& stUserGuid,
							   const string& tag,
							   const string& stTmpFile)
{
	                                             
	const BackupPolicy backupPolicy = GetBackupPolicy();
	if ( backupPolicy.MaxNumberOfBackups <=0 )
		return;

	long lErr = 0;
	const long long lFileSize = GetFileSize(stTmpFile, lErr);
	if ( lErr != 0 ) {
		throw CrException("Failed to determine the size of the project backup.\nReason: "
			+ ErrorResolver(lErr).Resolve(lErr));
	}

	const string stAbsolutePermanentFileName( GetJpkFileName(stUserGuid, stProjectGuid) );
	const string stRelativePermanentFileName( getRelativePath(stAbsolutePermanentFileName) );

	kCopyFile(stTmpFile, stAbsolutePermanentFileName);
	string sql("INSERT INTO " + PROJECT_BACKUP_TAB_NAME + " "
		+ PROJECT_BACKUP_TAB_COLS
		+ " VALUES ('"
		+ stProjectGuid + "','"
		+ stUserGuid + "', NOW(),"
		+ toString(lFileSize) + ",'"
		+ SqlEscape(tag) + "','"
		+ SqlEscape(stRelativePermanentFileName) + "'"
		+ ")");
	GetDbConnection().ExecuteSql(sql);

	RemoveOldBackups(stProjectGuid, stUserGuid, backupPolicy, GetDbConnection());
}
