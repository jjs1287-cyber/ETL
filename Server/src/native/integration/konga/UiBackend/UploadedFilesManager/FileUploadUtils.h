/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef ETLFORGE_UPLOADED_FILES_MANAGER_FILE_UPLOAD_UTILS_H
#define ETLFORGE_UPLOADED_FILES_MANAGER_FILE_UPLOAD_UTILS_H

#include <string>

namespace konga {

	class UserInfo;
	class GuidToIdMapper;

	std::string GetUserGuid(const UserInfo& userInfo, const GuidToIdMapper& guidMapper);

	                                                      
	                                                     
	                   
	                                                  
	void VerifyPathForUploadedFile(const std::string& fileName);

	                                         
	                                                  
	                                                                               
	void SplitFullPath(const std::string& stFullPath, std::string& stPath, std::string& stName);

	                                                                        
	                                                                   
	bool GetServerPath(const std::string& stClientPath, const std::string& stUserGuid,
		std::string& stPath, std::string& stName);

	                                                   
	std::string GetClientPath(const std::string& stCanonicalPath, const std::string& stName);

	                                                                      
	                                                                
	std::string GetUploadedFilesDir();

	                                                                                
	void StripUploadedFilesDirFromPath(std::string& stPath);


}                   

#endif                                                       

