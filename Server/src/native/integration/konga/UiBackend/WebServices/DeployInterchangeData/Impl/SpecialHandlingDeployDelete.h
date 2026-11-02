/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef KONGA_WS_SPECIAL_HANDLING_DEPLOY_DELETE_H
#define KONGA_WS_SPECIAL_HANDLING_DEPLOY_DELETE_H

#include <string>
#include <vector>

namespace konga {
	
	class DbConnection;
	class DependentTableInfo;

namespace interchange_deploy {

	                                                           
	class SpecialHandlingDeployDelete {
	public:
		SpecialHandlingDeployDelete(void) {}
		virtual ~SpecialHandlingDeployDelete(void) {}

		                                           
		virtual bool HandleRow(long id) = 0;

		                                                                  
		virtual bool Delete() = 0;

	};

	                                       
	                                                                           
	class SpecialHandlingDeployDeleteDependents : public SpecialHandlingDeployDelete {

	public:
		SpecialHandlingDeployDeleteDependents(DbConnection& dbConnection,
			const std::vector<DependentTableInfo>& dependentTables)
			: m_dbConnection(dbConnection), m_dependentTables(dependentTables),
			m_bDeleteCallNeeded(false) {}
		virtual ~SpecialHandlingDeployDeleteDependents(void);

		                                                                                 
		                                           
		virtual bool HandleRow(long id);

		virtual bool Delete();

	private:
		DbConnection& m_dbConnection;
		const std::vector<DependentTableInfo>& m_dependentTables;
		std::string m_stDeleteSql;
		bool m_bDeleteCallNeeded;
	};

}                   
}                                

#endif                                             

