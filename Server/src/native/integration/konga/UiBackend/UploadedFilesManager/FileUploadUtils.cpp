/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */

#include <konga/Utils/CrException.h>
#include <konga/Crypto/Crypto.h>

#include <konga/DbRelations/EntityIdConstants.h>

#include <konga/ItemAccess/UserInfo.h>

#include <konga/UiBackend/GuidMappings/DbGuidToIdMapper.h>

#include "FileUploadUtils.h"

using namespace konga;
using std::string;

namespace {
	const char forbiddenChars[] = ":*?\"<>|~";
}

string konga::GetUserGuid(const UserInfo& userInfo, const GuidToIdMapper& guidMapper) {
	const string& stUserGuid = guidMapper.GetGuid(userInfo.GetId(), UserEntityId);
	if ( stUserGuid.empty() ) {
		throw CrException("Internal Error: Failed to find User GUID for user "
			+ userInfo.GetLogin());
	}
	return stUserGuid;
}


void konga::VerifyPathForUploadedFile(const string& fileName) {
	if ( fileName.find("..") != string::npos )
		throw CrException("The path \"" + fileName + "\" is not valid. \"..\" is not allowed.");

	if ( fileName.find("\\\\") != string::npos )
		throw CrException("The path \"" + fileName + "\" is not valid. \"\\\\\" is not allowed.");

	if ( fileName.find_first_of(forbiddenChars) != string::npos )
		throw CrException("The path \"" + fileName + "\" is not valid. It contains at least one" \
			" of the forbidden characters " + string(forbiddenChars));
}

void konga::SplitFullPath(const std::string& stFullPath_in, std::string& stPath, std::string& stName) {
	stPath.clear();
	stName.clear();
	string stFullPath(stFullPath_in);
	ReplaceCharWithChar(stFullPath, '\\', '/');
	string::size_type ipos = stFullPath.find_last_of("/");
	if ( ipos == string::npos ) {
		                                  
		stName = stFullPath;
	}
	else if ( ipos == stFullPath.size() - 1 ) {
		                        
		stPath = stFullPath;
	}
	else {
		            
		stPath = stFullPath.substr(0, ipos+1);
		stName = stFullPath.substr(ipos+1);
	}
}

bool konga::GetServerPath(const std::string& stLocator, const std::string& stUserGuid,
	std::string& stPath, std::string& stName)
{
	bool bIsUrl = false;
	string stClientPath;
	if ( stLocator.find("://") != string::npos ) {
		                         
		bIsUrl = true;
		stClientPath = md5(stLocator) + ".wsdl";
	}
	else {
		stClientPath = stLocator;
	}

                                                           
	SplitFullPath(stClientPath, stPath, stName);
	stPath.insert(0, "/");
	stPath.insert(0, stUserGuid);
	return bIsUrl;
}

std::string konga::GetClientPath(const std::string& stCanonicalPath, const std::string& stName) {
	string stClientPath(stCanonicalPath + stName);
	string::size_type ipos = stClientPath.find("/");
	if ( ipos == string::npos ) {
		return stClientPath;
	}
	else if ( ipos == stClientPath.size()-1 ) {
		                                           
		return stClientPath;
	}
	else {
		                                 
		return stClientPath.substr(ipos+1);
	}
}

std::string konga::GetUploadedFilesDir() {
	                                                          
	return std::string("DataInterchange/UserFiles/");
}

void konga::StripUploadedFilesDirFromPath(std::string& stPath) {
	const string& stUploadedFilesDir = GetUploadedFilesDir();
	string::size_type pos = stPath.find(stUploadedFilesDir);
	if ( pos != string::npos ) {
		stPath.erase(0, stUploadedFilesDir.size());
	}
}
