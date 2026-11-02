/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#ifndef KONGA_SERVER_SERVER_H
#define KONGA_SERVER_SERVER_H

#include <string>

#include <konga/Utils/Version.h>

namespace konga {
	class DbConnection;

	std::string GetKeyValue(const std::string& stKeyName, const std::string& stDefaultValue = "");
	void SetKeyValue(const std::string& stKeyName, const std::string& stKeyValue);

	std::string GetKeyValue(const std::string& stKeyName, DbConnection& dbConnection, const std::string& stDefaultValue = "");
	void SetKeyValue(const std::string& stKeyName, const std::string& stKeyValue, DbConnection& dbConnection);

	std::string GetBigKeyValue(const std::string& stKeyName, const std::string& stDefaultValue = "");
	void SetBigKeyValue(const std::string& stKeyName, const std::string& stKeyValue);

	std::string GetBigKeyValue(const std::string& stKeyName, DbConnection& dbConnection, const std::string& stDefaultValue = "");
	void SetBigKeyValue(const std::string& stKeyName, const std::string& stKeyValue, DbConnection& dbConnection);

	std::string GetServerGUID();
	std::string GetServerGUID(DbConnection& dbConnection);

	bool HasServerGuid();

	void SetServerGUID(const std::string& stServerGuid);

	std::string ResetServerGuid();

	Version GetDbVersion();

	std::string GetServerLabel();
	void SetServerLabel(const std::string& sLabel);

	void GetUserCredentials(long lUserId, std::string& sUserName, std::string& sMd5Password);
	void GetAdminCredentials(std::string& sUserName, std::string& sPassword);
	void GetInternalAdminCredentials(std::string& sUserName, std::string& sPassword);

	                                                                
	                                                                         
	                                                              
	std::string GetDataDirectoryWithSlash();

	                                                                         
	                                              
	std::string GetTemporaryLoadedSourceDataDirWithSlash();

	                                                                 
	                                                      
	std::string GetUploadedProjectsBaseDirectoryWithSlash();

	                                                              
	std::string GetUploadedWsdlBaseDirectoryWithSlash();

	                                               
	                                                                       
	                             
	                                                                              
	std::string GetSourceJtrFileName(long lTransformationId);
	std::string GetTargetJtrFileName(long lTransformationId);

	struct JavaLaunchDetails;
	void GetJavaLaunchDetails(JavaLaunchDetails& javaLaunchDetails, const std::string& sComponentName, unsigned long lDefaultPort);
}                   

#endif                         

