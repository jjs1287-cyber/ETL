/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef ETLFORGE_UPLOADEDFILESMANAGER_XML_FILES_MANAGER
#define ETLFORGE_UPLOADEDFILESMANAGER_XML_FILES_MANAGER

#include <string>
#include <vector>

namespace konga {
	class DbConnection;
	class UploadedFile;
	class ZippedUploadedFile;
	class UploadedFilesManager;
	class GuidToIdMapper;
	class UserAccess;
	class UserInfo;

	long RemoveXmlFile(const std::string& stGUID, DbConnection& tranDbConnection);

	long AddXmlFile(const ZippedUploadedFile& incomingFile, bool bExists, bool bAdd,
		UploadedFilesManager& fileManager, GuidToIdMapper& guidMapper,
		UserAccess& userAccess, DbConnection& dbConnection);

	long CloneXmlFile(const long lXmlId, DbConnection& tranDbConnection, const UserInfo& userInfo, GuidToIdMapper& guidMapper);

	void AddXmlDependency(long lParentXmlId, long lChildXmlId, DbConnection& dbConnection);

	void GetXmlDependencies(const long lXmlId, std::vector<UploadedFile>& dependencies,
		const GuidToIdMapper& guidMapper, const UserAccess& userAccess,
		const UploadedFilesManager& fileManager, DbConnection& dbConnection);

}                   

#endif                                                   

