/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef ETLFORGE_UPLOADEDFILESMANAGER_WSDL_FILES_MANAGER
#define ETLFORGE_UPLOADEDFILESMANAGER_WSDL_FILES_MANAGER

#include <string>
#include <vector>

#include "UploadedFileTypes.h"

namespace konga {
	class DbConnection;
	class UserAccess;
	class UserInfo;
	class UploadedFilesManager;
	class GuidToIdMapper;
	class ZippedUploadedFile;
	class UploadedFile;

	                                         
	long AddWsdlFile(long wsdlId, const std::string& stGuid, const std::string& stWsdlName,
		const std::string& stUrl, UploadedWsdlFileType wsdlType, ZippedUploadedFile& incomingFile, DbConnection& tranDbConnection,
		UserAccess& userAccess, UploadedFilesManager& fileManager, GuidToIdMapper& guidMapper,
		bool bAdd, bool bExists, int iStatus = 1, const std::string& stApplicationMessage = "",
		const std::string& stUserComment = "");

	                                                                             
	                                                                                                                  
	long CloneWsdl(const long wsdlId, DbConnection& tranDbConnection, const UserInfo& userInfo, UserAccess& userAccess,
		UploadedFilesManager& fileManager, GuidToIdMapper& guidMapper);

	void AddWsdlDependency(long lParentId, long lChildId, DbConnection& dbConnection);

	void GetWsdlDependencies(const long lWsdlId, std::vector<UploadedFile>& dependencies,
		const GuidToIdMapper& guidMapper, const UserAccess& userAccess,
		const UploadedFilesManager& fileManager, DbConnection& dbConnection);

	void RemoveWsdlFile(const std::string& stGUID);

	                                                                             
	                                                                       
	                                                                              
	                              
	void ValidateExistingAndMissingWsdlDependencies(std::vector<std::string>& vExistingIncludes,
		std::vector<std::string>& vMissingIncludes, const UploadedFile& parentWsdl,
		const GuidToIdMapper& guidToIdMapper, DbConnection& dbConnection);

}                   

#endif                                                    

