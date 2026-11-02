/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */

#include <konga/HttpEndpoint/HttpTrigger.h>

#include "SpecialHandlingDeployDeleteHttpEndpoint.h"

using namespace konga;
using namespace konga::interchange_deploy;

SpecialHandlingDeployDeleteHttpEndpoint::SpecialHandlingDeployDeleteHttpEndpoint(DbConnection& dbConnection,
																				 const std::vector<DependentTableInfo>& dependentTables)
: SpecialHandlingDeployDeleteDependents(dbConnection, dependentTables),
m_dbConnection(dbConnection) {}

SpecialHandlingDeployDeleteHttpEndpoint::~SpecialHandlingDeployDeleteHttpEndpoint(void) {
}

bool SpecialHandlingDeployDeleteHttpEndpoint::HandleRow(long id) {
	SpecialHandlingDeployDeleteDependents::HandleRow(id);
	HttpTrigger trigger;
	trigger.ItemId = id;
	trigger.Type = HttpTriggerType_HttpEndpoint;
	konga::removeHttpTrigger(trigger, m_dbConnection);

	return true;
}
