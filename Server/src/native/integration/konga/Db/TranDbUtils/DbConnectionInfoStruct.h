/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef ETLFORGE_DB_TRANDBUTILS_DB_CONNECTION_INFO_STRUCT_H
#define ETLFORGE_DB_TRANDBUTILS_DB_CONNECTION_INFO_STRUCT_H

#include <string>

#include <konga/Db/DbUtils/DbConnection.h>

#include "DatabaseDriverType.h"

namespace konga {

	struct DbConnectionInfoStruct {
		DbConnectionInfoStruct(void) : timeOutSeconds(0), txnIsolationLevel(konga::UNDEFINED), driverType(DBTYPE_UNDEFINED), port(-1), fetchSize(0) {}
		std::string connectionString;
		long timeOutSeconds;
		TransactionIsolationLevel txnIsolationLevel;
		std::string beginQuote, endQuote;
		DatabaseDriverType driverType;

		                                                      
		long port;
		int fetchSize;
		std::string server;
		std::string database;
		std::string user;
		std::string password;
		std::string manualConnectionString;
		std::string additionalParams;
		std::string driverName;
	};

}

#endif                                                       

