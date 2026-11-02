/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef KONGA_TRANSFORMATION_UTILS_DBLOCATION_CONNECTIONINFO
#define KONGA_TRANSFORMATION_UTILS_DBLOCATION_CONNECTIONINFO

#include <string>

#include <konga/Db/TranDbUtils/DatabaseDriverType.h>

namespace konga {

	class DbConnection;
	class OperationDataElementHolder;

	struct DbLocationConnectionInfo {
		konga::DatabaseDriverType DriverType;
		long Port;                                                           
		std::string DriverName;
		std::string Server;
		std::string Database;
		std::string User;
		std::string Password;
		std::string ManualConnectionString;
		std::string OptionalConnectionParams;
		std::string Path;
		int Secured;
	};

	class DbLocationConnectionInfoFactory {
	
	public:
		DbLocationConnectionInfo fromSourcesTab(konga::DbConnection* tranDb, int sourceId);
		DbLocationConnectionInfo fromTargetsTab(konga::DbConnection* tranDb, int targetId);
	
	};

	void replaceGlobalDataElementsInDbLocationConnectionInfo(DbLocationConnectionInfo& dbLocationConnectionInfo, const OperationDataElementHolder& deHolder);
}

#endif                                                        

