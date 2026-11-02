/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
#ifndef KONGA_WS_DEPLOY_SPECIAL_HANDLING_DEPLOY_DELETE_SOURCE_OR_TARGET_H
#define KONGA_WS_DEPLOY_SPECIAL_HANDLING_DEPLOY_DELETE_SOURCE_OR_TARGET_H

#include <vector>

#include "SpecialHandlingDeployDelete.h"

namespace konga {
	
class DbConnection;

namespace interchange_deploy {

	                                                              
	class SpecialHandlingDeployDeleteSourceTarget : public SpecialHandlingDeployDeleteDependents {
	public:
		SpecialHandlingDeployDeleteSourceTarget(bool bIsSource, DbConnection& dbConnection,
			const std::vector<DependentTableInfo>& dependentTables);
		virtual ~SpecialHandlingDeployDeleteSourceTarget(void);

		virtual bool HandleRow(long locationId);

		virtual bool Delete();

		bool JmsLocationsWereDeleted() const;

	private:
		bool m_bIsSource;
		bool m_bJmsLocationsWereDeleted;
		DbConnection& m_dbConnection;
		std::vector<long> m_vDeletedLocations;
	};

	class SpecialHandlingDeployDeleteSource : public SpecialHandlingDeployDeleteSourceTarget {
	public:
		SpecialHandlingDeployDeleteSource(DbConnection& dbConnection,
			const std::vector<DependentTableInfo>& dependentTables) : SpecialHandlingDeployDeleteSourceTarget(true, dbConnection, dependentTables) {}
	};

	class SpecialHandlingDeployDeleteTarget : public SpecialHandlingDeployDeleteSourceTarget {
	public:
		SpecialHandlingDeployDeleteTarget(DbConnection& dbConnection,
			const std::vector<DependentTableInfo>& dependentTables) : SpecialHandlingDeployDeleteSourceTarget(false, dbConnection, dependentTables) {}
	};

}                                
}                   

#endif                                                                     
