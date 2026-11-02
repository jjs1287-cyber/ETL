/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/DbRelations/EntityIdConstants.h>

#include <konga/UiBackend/GuidMappings/GuidToIdMapper.h>

#include "DeployUtils.h"

#include "SpecialHandlingDeploySalesforceDelete.h"

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

SpecialHandlingDeploySalesforceDelete::SpecialHandlingDeploySalesforceDelete(const xsd__string_Array& columnNames,
																	 DbConnection& dbConnection,
																	 GuidToIdMapper& guidToIdMapper) :

m_iSalesforceDeleteIdColPos(GetColPos("salesforcedelete_id", columnNames)),
m_iOperationIdPos(GetColPos("operation_id", columnNames)),
m_dbConnection(dbConnection), m_guidToIdMapper(guidToIdMapper)
{
}

bool SpecialHandlingDeploySalesforceDelete::HandleRow(InterchangeDataRowStruct& row) {
	if ((row.m_vFields.size() - 1) < m_iSalesforceDeleteIdColPos || (row.m_vFields.size() - 1) < m_iOperationIdPos) {
		throw InternalErrorException("Internal Error: Not enough fields received for SalesforceDeletesTab.");
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

bool SpecialHandlingDeploySalesforceDelete::HandleRowAfterInsert(long newId, InterchangeDataRowStruct& row) {
	return false;
}

bool SpecialHandlingDeploySalesforceDelete::HandleRowAfterUpdate(long id, InterchangeDataRowStruct& row) {
	return false;
}
