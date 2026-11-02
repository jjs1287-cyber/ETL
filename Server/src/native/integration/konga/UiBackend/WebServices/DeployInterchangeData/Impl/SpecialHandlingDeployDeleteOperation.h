/*
 * Copyright (c) 1998-2006 Jitterbit, Inc.
 *
 */
#ifndef KONGA_WS_DEPLOY_SPECIAL_HANDLING_DEPLOY_DELETE_OPERATION_H
#define KONGA_WS_DEPLOY_SPECIAL_HANDLING_DEPLOY_DELETE_OPERATION_H

#include <konga/OperationLog/OperationLog.h>

#include "SpecialHandlingDeployDelete.h"

namespace konga {
	
	class DbConnection;

namespace interchange_deploy {

	                                
	class SpecialHandlingDeployDeleteOperation : public SpecialHandlingDeployDeleteDependents {

	public:
		SpecialHandlingDeployDeleteOperation(DbConnection& dbConnection,
			const std::vector<DependentTableInfo>& dependentTables) :
		  SpecialHandlingDeployDeleteDependents(dbConnection, dependentTables),
		    m_bJmsOperationWasDeleted(false), m_bScheduledOperationWasDeleted(false),
			m_dbConnection(dbConnection),
			m_operationLog(dbConnection) {}
		virtual ~SpecialHandlingDeployDeleteOperation(void) {};

		virtual bool HandleRow(long operationId);

		                                                                 
		bool JmsOperationWasDeleted() const;

		                                                            
		bool ScheduledOperationWasDeleted() const;

	private:
		bool m_bJmsOperationWasDeleted, m_bScheduledOperationWasDeleted;
		DbConnection& m_dbConnection;
		konga::OperationLog m_operationLog;
	};

}                   
}                                

#endif                                                              

