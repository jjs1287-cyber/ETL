/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#include <konga/DbRelations/DatabaseTableNameMappings.h>
#include <konga/DbRelations/EntityIdConstants.h>

#include <konga/ItemAccess/AccessSqlGenerator.h>

#include "OperationTreeCheckoutSqlGeneratorForInterchangeProject.h"

using namespace konga;
using std::string;

string interchange_checkout::OperationTreeCheckoutSqlGeneratorForInterchangeProject::GetSql() {
	return string(
		"SELECT DISTINCT guid, ipv.interchangeproject_id, interchangeproject_name, description "
		"FROM guidsforinterchangeprojectsview ipv "
		"WHERE ipv.item_id > 0 AND ("
		+ konga::GetAccessControlWhereClause(m_userInfo, getIdName(nameInterchangeProjects), InterchangeProjectEntityId)
		+ ")"
		);
}

