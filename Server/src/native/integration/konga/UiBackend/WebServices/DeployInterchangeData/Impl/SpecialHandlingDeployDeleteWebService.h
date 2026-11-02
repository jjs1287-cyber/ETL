/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
#ifndef KONGA_WS_SPECIALHANDLING_DEPLOY_DELETE_WEBSERVICE_H
#define KONGA_WS_SPECIALHANDLING_DEPLOY_DELETE_WEBSERVICE_H

#include "SpecialHandlingDeployDelete.h"

namespace konga {
	
	class DbConnection;
	class GuidToIdMapper;

namespace interchange_deploy {

	                                                         
	                                                   
	class SpecialHandlingDeployDeleteWebService : public SpecialHandlingDeployDeleteDependents {

	public:
		SpecialHandlingDeployDeleteWebService(DbConnection& dbConnection, const std::vector<DependentTableInfo>& dependentTables,
			GuidToIdMapper& guidToIdMapper) :
		  SpecialHandlingDeployDeleteDependents(dbConnection, dependentTables),
			  m_dbConnection(dbConnection), m_guidToIdMapper(guidToIdMapper) {}

		virtual ~SpecialHandlingDeployDeleteWebService(void);

		virtual bool HandleRow(long webServiceId);

		virtual bool Delete();

	private:
		DbConnection& m_dbConnection;
		GuidToIdMapper& m_guidToIdMapper;
	};

}                   
}                                

#endif                                                       

