/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */

#include <string>
#include <cassert>

#include <konga/Utils/StringUtils.h>	           
#include <konga/Utils/CrException.h>

#include <konga/Db/DbUtils/DbConnection.h>

#include <konga/DbRelations/EntityIdConstants.h>

#include "SpecialHandlingDeployDeleteJmsMessage.h"

using namespace konga;
using namespace konga::interchange_deploy;
using std::vector;
using std::string;

SpecialHandlingDeployDeleteJmsMessage::~SpecialHandlingDeployDeleteJmsMessage(void) {
	assert(m_vDeletedJmsMessages.empty());
}

bool SpecialHandlingDeployDeleteJmsMessage::HandleRow(long id) {
	m_vDeletedJmsMessages.push_back(id);
	return true;
}

void SpecialHandlingDeployDeleteJmsMessage::DeleteDependents(DbConnection& dbConnection) {
	if ( m_vDeletedJmsMessages.empty() )
		return;

	vector<long>::const_iterator cit = m_vDeletedJmsMessages.begin();
	string sql("DELETE FROM DataElementsTab WHERE OwnerEntityTypeId = "
		+ toString(JmsMessageEntityId) + " AND ownerentityid IN (");
	for(; cit != m_vDeletedJmsMessages.end(); ++cit ) {
		const long jmsMsgId = *cit;
		if ( cit != m_vDeletedJmsMessages.begin() )
			sql += ",";
		sql += toString(jmsMsgId);
	}
	sql += ")";
	dbConnection.ExecuteSql(sql);
	m_vDeletedJmsMessages.clear();
}
