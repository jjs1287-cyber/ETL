/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DBTABLE_H
#define KONGA_DBTABLE_H

#include <string>

namespace {
	const std::string DBTABLE_TYPE_TABLE("TABLE");
	const std::string DBTABLE_TYPE_VIEW("VIEW");
	const std::string DBTABLE_TYPE_SYSTEM_TABLE("SYSTEM TABLE");
}

namespace konga {

	struct DbTable {
		DbTable()  {}
		std::string Catalog;
		std::string Schema;
		std::string Name;
		std::string Type;	                                             
	};

}                   

#endif                   
