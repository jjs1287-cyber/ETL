/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef OPERATION_ENGINE_OPERATION_UTILS
#define OPERATION_ENGINE_OPERATION_UTILS

#include <string>

                                                   
namespace konga {

	class DbConnection;

	void LogOperationSuccess(long long llOperationInstanceId, const std::string& sWarnings);
	void LogOperationError(long lOperationId, long long llOperationInstanceId, const std::string& stError);

	                                                                                                   
	long GetWebServiceId(DbConnection* pTranDb, const long lHostedWebServiceOperationId, std::string& sOperationName);
}

#endif                                    

