/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef ETLFORGE_DB_MAINTENANCE_PGSQL_H
#define ETLFORGE_DB_MAINTENANCE_PGSQL_H

#include <string>
#include <vector>

#include <konga/Utils/StatusReceiver.h>

namespace konga {

	extern const std::string DB_NULL_STR;

	void PgSqlVacuum(const std::string& stConnectionString, bool full, StatusReceiver& caller);
	void PgSqlReindex(const std::string& stConnectionString, StatusReceiver& caller);

	                                                             
	                                                                                    
	                                       
	                                                                                              
	void PgSqlBackup(const std::string& sConnectionString,
		const std::string& sBackupDir, StatusReceiver& caller,
		const std::vector<std::string>& vTablesToIgnore);

	                                                     
	                                                                               
	                                                                                      
	void PgSqlRestore(const std::string& sConnectionString,
		const std::string& sBackupDir, StatusReceiver& caller);
}

#endif                                   

