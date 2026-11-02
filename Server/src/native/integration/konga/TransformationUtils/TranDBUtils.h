/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef TRANSFORMATION_UTILS_TRANDBUTILS_H_
#define TRANSFORMATION_UTILS_TRANDBUTILS_H_

#include <string>

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/TranDbUtils/DbConnectionInfoStruct.h>

#include <konga/FileTransfer/HttpOptions.h>

namespace konga {
	class DbConnection;
	class OperationContext;
	class OperationDataElementHolder;
	class LdapFilterCollection;
	class UserInfo;
	class KeyValues;

	                                                                       
	DbConnection* GetTranDbConnection();

	unsigned long getSourceConnectionInfo(DbConnection& tranDbConn, const UserInfo& userInfo, int sourceID,
		const OperationDataElementHolder* const pDEHolder, DbConnectionInfoStruct& dbConnInfo, std::string& sErr);

	unsigned long getTargetConnectionInfo(DbConnection& tranDbConn, const UserInfo& userInfo, int targetID,
		const OperationDataElementHolder* const pDEHolder, DbConnectionInfoStruct& dbConnInfo, std::string& sErr);

	                                                                    
	                                                
	long getLocationId(const std::string& prefixedGuid);

	                                                                                   
	long getLocationId(const std::string& prefixedGuid, DbConnection& tranDbConnection);
	long getLocationId(const std::string& prefixedGuid, bool& isSource, DbConnection& tranDbConnection);

	                                                                              
	long GetOperationIdFromTag(const std::string& prefixedGuid, DbConnection& tranDbConnection);

	                                                                       
	                                                                                     
	                          
	                                                                                        
	                                                                                                  
	std::string getConnectionStringFromDb(DbConnection* pTranDbConn, const UserInfo& userInfo, const std::string& prefixedGuid,
		const OperationDataElementHolder* const pDEHolder, DbConnectionInfoStruct& dbConnInfo);

	void getLdapFiltersForTransformation(long lTranId, LdapFilterCollection& filterCollection,
		DbConnection& dbConnection);

	void replaceGlobalDataElementsInCollection(LdapFilterCollection& filterCollection, const OperationDataElementHolder& deHolder);

	                                
	enum TestTransformationSourceType {
		SOURCE_TYPE_FILE = 0,	                                          
		SOURCE_TYPE_FILTER = 1,	                                                      
		SOURCE_TYPE_NO_SOURCE = 2	                      
	};

	TestTransformationSourceType getTestTransformationSourceType(int transformation_ID, FILE* debug_log);

	HttpOptions GetRuntimeHttpOptionsForEntity(long lItemId, int EntityTypeId, const OperationDataElementHolder& deHolder, DbConnection* pTranDb = 0);

	HttpOptions GetRuntimeHttpOptions(const KeyValues& keyValues, const OperationDataElementHolder& deHolder);
}

#endif                                       

