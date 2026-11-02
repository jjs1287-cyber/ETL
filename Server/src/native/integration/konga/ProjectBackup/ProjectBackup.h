/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef KONGA_ETLFORGE_PROJECT_BACKUP_H
#define KONGA_ETLFORGE_PROJECT_BACKUP_H

#include <string>

#include <konga/Db/DbUtils/DbConnectionHolder.h>

namespace konga {

	class ProjectBackup : public DbConnectionHolder {
	public:
		ProjectBackup(DbConnection& dbConnection)
			: DbConnectionHolder(&dbConnection) {}
		~ProjectBackup(void) {}

		                                  
		void SaveBackup(const std::string& projectGuid, const std::string& userGuid, const std::string& tag,
			const std::string& stProjectFile);

	private:
	};
}

#endif                                   

