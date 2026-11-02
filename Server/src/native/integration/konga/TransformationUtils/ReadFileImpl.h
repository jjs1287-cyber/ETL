/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */

#ifndef TRANSFORMATION_UTILS_READ_WRITE_FILES_H
#define TRANSFORMATION_UTILS_READ_WRITE_FILES_H

#include <string>

namespace konga {

	class OperationDataElementHolder;

	                                                                          
	                                              
	                                                                      
	void ReadFromSource(const std::string& sPrefixedSourceGUID, std::string& stContents,
		OperationDataElementHolder* pDEHolder, const std::string& sOverrideFileFilter, const UserInfo& currentUser);

	void ReadFromSourceAndBase64Encode(const std::string& sPrefixedSourceGUID, std::string& stBase64EncodedContents,
		OperationDataElementHolder* pDEHolder, const std::string& sOverrideFileFilter, const UserInfo& currentUser);

	void GetFileNameListFromSource(const std::string& sPrefixedSourceGUID,
		OperationDataElementHolder* pDEHolder, const UserInfo& currentUser, std::vector<std::string>&list);
}

#endif                                           

