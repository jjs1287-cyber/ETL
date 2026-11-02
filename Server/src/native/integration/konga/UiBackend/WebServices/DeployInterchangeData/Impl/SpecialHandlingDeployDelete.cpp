/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
#include <sstream>
#include <cassert>

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/DbRelations/DependentTableInfo.h>

#include <konga/UiBackend/WebServices/Utils/UiBackendLogging.h>		                  

#include "SpecialHandlingDeployDelete.h"

using namespace konga;
using namespace konga::interchange_deploy;
using std::string;
using std::stringstream;

SpecialHandlingDeployDeleteDependents::~SpecialHandlingDeployDeleteDependents(void) {
	assert(!m_bDeleteCallNeeded);
}

bool SpecialHandlingDeployDeleteDependents::HandleRow(long lId) {
	if ( !m_bDeleteCallNeeded )
		m_bDeleteCallNeeded = true;
	else
		m_stDeleteSql += ",";
	m_stDeleteSql += toString(lId);
	return true;
}

bool SpecialHandlingDeployDeleteDependents::Delete() {
	if ( !m_bDeleteCallNeeded )
		return false;

	std::vector<DependentTableInfo>::const_iterator cit = m_dependentTables.begin();
	for(; cit != m_dependentTables.end(); ++cit ) {
		stringstream ssDeleteSql;
		ssDeleteSql << "DELETE FROM " << cit->GetTableName()
			        << " WHERE " << cit->GetForeignKeyName() << " IN(" << m_stDeleteSql << ")";
		const string stDeleteSql( ssDeleteSql.str() );
		UIBACKEND_TRACE("SpecialHandlingDeployDeleteDependents: " + stDeleteSql);
		m_dbConnection.ExecuteSql( stDeleteSql );
	}

	m_bDeleteCallNeeded = false;
	m_stDeleteSql.clear();
	return true;
}

