/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef ETLFORGE_OPERATION_ENGINE_CONTEXT_H
#define ETLFORGE_OPERATION_ENGINE_CONTEXT_H

#include <string>

#include <konga/OperationContext/DefaultOperationContext.h>
#include <konga/OperationContext/DataElementHandler.h>

namespace konga {

	class Operation;
	class DbConnection;
	class ProcessedFile;
	class OperationDataElementHolder;

	class OperationEngineContext : public DefaultOperationContext, public DataElementHandler {
	public:
		OperationEngineContext(const UserInfo& currentUser, Debuggee* pDebuggee, OperationDataElementHolder& deHolder);
		virtual ~OperationEngineContext(void);

	                                          
	public:
		virtual std::string getOperationName() const;
		virtual long getOperationId() const;
		virtual std::string getOperationGuid() const;

		virtual DbConnection* getTranDbConnection() const;

	                                
	public:
		virtual DataElement* getDataElement(const std::string& deName) const;

	                                           
	public:
		                                                   
		                                                                                                 
		                                                                                          
		                                                 
		bool isAlreadyTransferred(ProcessedFile& file) const;

		void setSourceLocator(const std::string& sSourceLocator);
		void setSourceSize(long lSourceSize);

		                                                                     
	protected:
		friend class OperationEngine;

		void setOperation(const Operation* p) { m_pOperation = p; }
		void setDbConnection(DbConnection* p) { m_pDbConnection = p; }

	private:
		                                   
		std::string getLastError() const;
		std::string getError() const;

	private:
		const Operation* m_pOperation;
		OperationDataElementHolder& m_deHolder;
		long m_lSourceSize;
		mutable std::string m_OperationGuid;
		DbConnection* m_pDbConnection;
		std::string m_sSourceLocator;
	};
}

#endif                                       

