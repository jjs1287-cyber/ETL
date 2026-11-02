/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef ETLFORGE_MESSAGING_OPERATION_MESSAGE_UTILS_H
#define ETLFORGE_MESSAGING_OPERATION_MESSAGE_UTILS_H

#include <string>

namespace konga {

	class DbConnection;
	class OperationDataElementHolder;
	
	const long UNDEFINED_UID = -999999;
	const long UNDEFINED_SESSION_ID = -1;
	const long long UNDEFINED_OPERATION_INSTANCE_ID = -1;

	                                                                              
	long long AddOperationToQueue(long lOperationId, long lSessionId, long long llParentOperationInstanceId, const std::string& stFrom, const std::string& stSubject, long runAsUserId = UNDEFINED_UID);

	                                                             
	                                       
	long long AddOperationToQueue(DbConnection* pMsgDbConn, DbConnection* pTranDbConn,
		long lOperationId, long lSessionId, long long llParentOperationInstanceId,
		const std::string& stFrom, const std::string& stSubject, long runAsUserId = UNDEFINED_UID);

	                                                                                        
	                                                                                 
	                                                                                                     
	long long AddOperationToQueueOverrideSource(long lOperationId, const std::string& stFrom, const std::string& stSubject,
		const std::string& stSourceOverrideFileName, bool bFileIsExclusive,
		OperationDataElementHolder& deHolder, long runAsUserId = UNDEFINED_UID);

	std::string GetProcessVariablesForOperation(long lOperationId);

	                                                         
	                                                                
	                
	bool IsOperationInQueue(long lOperationId, const std::string& sFrom = "",
		DbConnection* pMsgDbConnection = 0);
}

#endif                                                

