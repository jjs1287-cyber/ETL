/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#include <konga/DbRelations/DatabaseTableNameMappings.h>
#include <konga/DbRelations/EntityIdConstants.h>

#include <konga/ItemAccess/AccessSqlGenerator.h>

#include "OperationTreeCheckoutSqlGeneratorForSchedule.h"

using namespace konga;
using std::string;

string interchange_checkout::OperationTreeCheckoutSqlGeneratorForSchedule::GetSql() {
	return string(
		"SELECT guid, schedule_id, interchangeproject_id, schedule_name FROM guidsforschedulesview g "
		"inner join interchangeprojectschedulestab i "
		"on g.schedule_id = i.item_id "
		"WHERE schedule_id > 0 AND interchangeproject_id > 0 AND ("
		+ konga::GetAccessControlWhereClause(m_userInfo, getIdName(nameSchedules), ScheduleEntityId)
		+ ")"
		);
}
