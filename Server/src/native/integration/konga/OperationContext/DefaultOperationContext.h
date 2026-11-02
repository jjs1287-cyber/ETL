/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef KONGA_OPERATION_CONTEXT_OPERATION_ENGINE_IMPL_H
#define KONGA_OPERATION_CONTEXT_OPERATION_ENGINE_IMPL_H

#include <string>
#include <vector>
#include <stack>

#include <boost/noncopyable.hpp>
#include <boost/thread/recursive_mutex.hpp>

#include "OperationContext.h"

namespace konga {
	class AutoCleanupTempDir;
	class Debuggee;

	class DefaultOperationContext : public OperationContext, boost::noncopyable {
	public:
		DefaultOperationContext(const UserInfo& currentUser, Debuggee* pDebuggee, long lOpId=-1, const std::string& sOpName = "",
			const std::string& sOpGuid = "");

		virtual ~DefaultOperationContext(void);

		virtual std::string getLocalTmpDirForOperation() const;	               
		virtual void setLocalTmpDirForOperationDeleteFlag(bool bDeleteIt);
		virtual void registerTemporaryLogFile(const std::string& path);
		void completeSuccessfulOperation();

		virtual void setErrorReporter(ErrorReporter* errorReporter);

		virtual std::string getOperationName() const;	               
		virtual void setOperationName(const std::string& stOperationName);

		virtual long getOperationId() const;	               

		virtual long long getOperationInstanceId() const;

		virtual std::string getOperationGuid() const;	               
		virtual void SetOperationGuid(const std::string& stOperationGuid);	               

		virtual UserInfo getCurrentUser() const;	               

		virtual DbConnection* getTranDbConnection() const;	                                       

		void setTranDbConnection(DbConnection* pDbConn) { m_pDbConn = pDbConn; }

		virtual void CancelOperationChain(const std::string& stMsg);	               

		virtual bool isDebugEnabled() const;
		
		virtual std::string getOperationChainInstanceGuid();
		virtual void setOperationChainInstanceGuid(const std::string& guid);

		virtual Debuggee* getDebuggee() const;

		                                                               
		  
	public:
		bool wasOperationChainCancelled(std::string& stMsg);

		void setCurrentUser(const UserInfo& userInfo);

		void setDebugFlag(bool flag);

		void setOperationInstanceId(long long llOperationInstanceId);
		
	protected:
		                                                        
		const ErrorReporter* getErrorReporter() const;

	private:
		void CleanupTmpDir() const;

	protected:
		mutable boost::recursive_mutex m_ThreadSyncLock;

	private:
		std::stack<ErrorReporter*> m_ErrorReporters;

		bool m_bOperationChainCancelled;
		bool m_bDebug;

		long m_lOperationId;
		long long m_llOperationInstanceId;
		UserInfo m_CurrentUser;

		std::string m_stOperationName;
		std::string m_stOperationGuid;
		std::string m_stOperationChainCancelMsg;
		std::string m_stOperationChainInstanceId;

		mutable std::string m_stLocalTmpDir;	                 
		mutable AutoCleanupTempDir* m_pAutoCleanupTempDir;
		bool m_bDeleteLocalTmpDir;
		bool m_bCompletedSuccessfully;
		std::vector<std::string> m_temporaryLogFiles;

		Debuggee* m_pDebuggee;
		DbConnection* m_pDbConn;
	};
}

#endif                                                   
