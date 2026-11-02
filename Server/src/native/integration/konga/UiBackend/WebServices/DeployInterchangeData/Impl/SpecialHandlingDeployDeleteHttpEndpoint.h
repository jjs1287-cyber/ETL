/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef KONGA_WS_DEPLOY_SPECIAL_HANDLING_DEPLOY_DELETE_HTTP_ENDPOINT_H
#define KONGA_WS_DEPLOY_SPECIAL_HANDLING_DEPLOY_DELETE_HTTP_ENDPOINT_H

#include <vector>

#include "SpecialHandlingDeployDelete.h"

namespace konga {
	
class DbConnection;

namespace interchange_deploy {

	                                                            
	class SpecialHandlingDeployDeleteHttpEndpoint : public SpecialHandlingDeployDeleteDependents {
	public:
		SpecialHandlingDeployDeleteHttpEndpoint(DbConnection& dbConnection,
			const std::vector<DependentTableInfo>& dependentTables);
		virtual ~SpecialHandlingDeployDeleteHttpEndpoint(void);

		virtual bool HandleRow(long locationId);

	private:
		DbConnection& m_dbConnection;
	};

}                                
}                   

#endif                                                                  
