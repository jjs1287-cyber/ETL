/*
 * Copyright (c) 1998-2006 Jitterbit, Inc.
 *
 */
#include <algorithm>		            

#include <konga/Db/DbUtils/TableColumnsOdbc.h>	               

#include "DatabaseDriverManager.h"

using namespace konga;
using namespace konga::interchange_deploy;


DatabaseDriverManager::DatabaseDriverManager(void) : m_bWasInitialized(false) {
}
DatabaseDriverManager::~DatabaseDriverManager(void)
{
}

void DatabaseDriverManager::GetDrivers() const {
	                             
	OdbcColumns odbcInfo;
	if( !odbcInfo.GetDrivers(m_vExistingDrivers) ) {
		                                                                              
		                                                                              
		                         
                                               
	}
	m_bWasInitialized = true;
}

bool DatabaseDriverManager::ExistsDriver(const std::string& stDriverName) const {
	if ( !m_bWasInitialized ) {
		GetDrivers();
	}
	return std::find(m_vExistingDrivers.begin(), m_vExistingDrivers.end(), stDriverName) != m_vExistingDrivers.end();
}
