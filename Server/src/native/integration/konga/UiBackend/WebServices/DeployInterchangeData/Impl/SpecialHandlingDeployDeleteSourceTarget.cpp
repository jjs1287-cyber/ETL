/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */

#include <string>
#include <cassert>

#include <konga/Utils/CrException.h>
#include <konga/Utils/StringUtils.h>	             

#include <konga/Db/DbUtils/DbConnection.h>

#include <konga/DbRelations/EntityIdConstants.h>

#include "SpecialHandlingDeployDeleteSourceTarget.h"

using namespace konga;
using namespace konga::interchange_deploy;
using std::vector;
using std::string;

SpecialHandlingDeployDeleteSourceTarget::SpecialHandlingDeployDeleteSourceTarget(bool bIsSource,
																				 DbConnection& dbConnection,
																				 const std::vector<DependentTableInfo>& dependentTables)
: SpecialHandlingDeployDeleteDependents(dbConnection, dependentTables),
m_bIsSource(bIsSource), m_bJmsLocationsWereDeleted(false), m_dbConnection(dbConnection) {}

SpecialHandlingDeployDeleteSourceTarget::~SpecialHandlingDeployDeleteSourceTarget(void) {
	assert(m_vDeletedLocations.empty());
}

bool SpecialHandlingDeployDeleteSourceTarget::HandleRow(long id) {
	SpecialHandlingDeployDeleteDependents::HandleRow(id);
	m_vDeletedLocations.push_back(id);
	return true;
}

bool SpecialHandlingDeployDeleteSourceTarget::Delete() {
	SpecialHandlingDeployDeleteDependents::Delete();
	if ( m_vDeletedLocations.empty() )
		return false;

	const int iEntityTypeId = m_bIsSource ? SourceEntityId : TargetEntityId;
	string sql("DELETE FROM JmsLocationPropertiesTab WHERE JmsLocationEntityTypeId="
		+ toString(iEntityTypeId) + " AND JmsLocationId IN (");

	vector<long>::const_iterator cit = m_vDeletedLocations.begin();
	for(; cit != m_vDeletedLocations.end(); ++cit ) {
		const long locationId = *cit;
		if ( cit != m_vDeletedLocations.begin() )
			sql += ",";
		sql += toString(locationId);
	}
	sql += ")";

	m_dbConnection.ExecuteSql(sql);
	if ( m_dbConnection.GetAffectedRowCount() > 0 )
		m_bJmsLocationsWereDeleted = true;

	m_vDeletedLocations.clear();
	return true;
}
