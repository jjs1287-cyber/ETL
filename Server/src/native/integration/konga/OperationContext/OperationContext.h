/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#ifndef KONGA_OPERATION_CONTEXT_H
#define KONGA_OPERATION_CONTEXT_H

#include <string>
#include <vector>

#include <konga/ItemAccess/UserInfo.h>

namespace konga {

	class ErrorReporter;
	class UserInfo;
	class Debuggee;
	class DbConnection;

	                                                                 
	class OperationContext {
	public:
		OperationContext(void) {}
		virtual ~OperationContext(void) {}

		                                                                        
		virtual std::string getLocalTmpDirForOperation() const = 0;
		virtual void setLocalTmpDirForOperationDeleteFlag(bool bDeleteIt) = 0;
		virtual void registerTemporaryLogFile(const std::string&) {}

		virtual void setErrorReporter(ErrorReporter* errorReporter) = 0;

		virtual std::string getOperationName() const = 0;
		virtual void setOperationName(const std::string& stOperationName) = 0;

		virtual long getOperationId() const = 0;

		virtual long long getOperationInstanceId() const = 0;

		virtual std::string getOperationGuid() const = 0;
		virtual void SetOperationGuid(const std::string& stOperationGuid) = 0;

		virtual UserInfo getCurrentUser() const = 0;

		                                                                                
		virtual DbConnection* getTranDbConnection() const = 0;

		virtual void CancelOperationChain(const std::string& stMsg) = 0;

		virtual bool isDebugEnabled() const = 0;

		virtual std::string getOperationChainInstanceGuid() = 0;
		virtual void setOperationChainInstanceGuid(const std::string& guid) = 0;

		virtual Debuggee* getDebuggee() const = 0;
	};
}

#endif                             

