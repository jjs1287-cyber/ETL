/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/DbRelations/EntityIdConstants.h>

#include <konga/UiBackend/GuidMappings/GuidToIdMapper.h>

#include "DeployUtils.h"

#include "SpecialHandlingDeploySalesforceUpdate.h"

using namespace konga;
using namespace konga::interchange_deploy;
using std::string;

namespace {
	unsigned int GetColPos(const string& sColName, const xsd__string_Array& columnNames) {
		vector<string> vColName(1);
		vColName[0] = sColName;
		vColPosT colPos = GetFieldIndexes(columnNames, vColName);
		return colPos.at(0);
	}
}

SpecialHandlingDeploySalesforceUpdate::SpecialHandlingDeploySalesforceUpdate(const xsd__string_Array& columnNames,
																	 DbConnection& dbConnection,
																	 GuidToIdMapper& guidToIdMapper) :

m_iSalesforceUpdateIdColPos(GetColPos("salesforceupdate_id", columnNames)),
m_iOperationIdPos(GetColPos("operation_id", columnNames)),
m_dbConnection(dbConnection), m_guidToIdMapper(guidToIdMapper)
{
}

bool SpecialHandlingDeploySalesforceUpdate::HandleRow(InterchangeDataRowStruct& row) {
	if ((row.m_vFields.size() - 1) < m_iSalesforceUpdateIdColPos || (row.m_vFields.size() - 1) < m_iOperationIdPos) {
		throw InternalErrorException("Internal Error: Not enough fields received for SalesforceUpdatesTab.");
	}
	NullableString& opGuid = row.m_vFields.at(m_iOperationIdPos);
	if (opGuid != 0) {
		const string opGuidAsString(opGuid.str());
		long localOpId = m_guidToIdMapper.GetId(opGuidAsString, OperationEntityId);
		row.m_vFields[m_iOperationIdPos] = toString(localOpId);
	} else {
		                          
	}
	return false;
}

bool SpecialHandlingDeploySalesforceUpdate::HandleRowAfterInsert(long newId, InterchangeDataRowStruct& row) {
	return false;
}

bool SpecialHandlingDeploySalesforceUpdate::HandleRowAfterUpdate(long id, InterchangeDataRowStruct& row) {
	return false;
}
