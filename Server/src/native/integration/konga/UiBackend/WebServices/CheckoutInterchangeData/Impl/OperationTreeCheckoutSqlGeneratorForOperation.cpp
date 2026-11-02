/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#include <konga/DbRelations/DatabaseTableNameMappings.h>
#include <konga/DbRelations/EntityIdConstants.h>

#include <konga/ItemAccess/AccessSqlGenerator.h>

#include "OperationTreeCheckoutSqlGeneratorForOperation.h"

using namespace konga;
using std::string;

string interchange_checkout::OperationTreeCheckoutSqlGeneratorForOperation::GetSql() {
	return string(
		"SELECT guid, operation_id, interchangeproject_id, operation_name, schedule_id, description FROM guidsforoperationsview g "
		"inner join interchangeprojectoperationstab i "
		"on g.operation_id = i.item_id "
		"WHERE operation_id > 0 AND interchangeproject_id > 0 AND ("
		+ konga::GetAccessControlWhereClause(m_userInfo, getIdName(nameOperations), OperationEntityId)
		+ ")"
		);
}
