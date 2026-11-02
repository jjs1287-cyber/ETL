/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef KONGA_TRAN_DB_UTILS
#define KONGA_TRAN_DB_UTILS

#include <string>

namespace konga {

	class DbConnection;

	std::string getDecryptedPasswordFromDb(const long lId, int iEntityTypeId);
	std::string getEncryptedPasswordForSourceFromDb(const long lId);
	std::string getEncryptedPasswordForTargetFromDb(const long lId);

	std::string getJmsProviderClasspathFromDb(std::string& sProviderNames);

	                                                                                                                
	std::string getLocationGuid(const std::string& prefixedGUID, bool& isSource);

	                                                                            
	std::string getItemName(const long lId, int iEntityTypeId, DbConnection& tranDbConnection);
}                   

#endif                       

