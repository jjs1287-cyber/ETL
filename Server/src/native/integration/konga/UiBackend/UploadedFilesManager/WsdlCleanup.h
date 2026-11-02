/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef ETLFORGE_WSDL_CLEANUP_H
#define ETLFORGE_WSDL_CLEANUP_H

#include <string>

namespace konga {
	class DbConnection;

	void CleanWsdlFiles(DbConnection& dbConn, std::string& sWarnings);
}

#endif                           

