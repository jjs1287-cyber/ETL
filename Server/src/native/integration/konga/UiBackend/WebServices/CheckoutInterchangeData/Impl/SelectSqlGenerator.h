/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_WS_SELECT_SQL_GENERATOR_H
#define KONGA_WS_SELECT_SQL_GENERATOR_H

#include <string>

namespace konga {
namespace interchange_checkout {

	                                                      
	                                                                                    
	                                 
	class SelectSqlGenerator {
	public:
		SelectSqlGenerator(void) {}
		virtual ~SelectSqlGenerator(void) {}

		virtual std::string GetSql() = 0;
	};
}
}

#endif                                   

