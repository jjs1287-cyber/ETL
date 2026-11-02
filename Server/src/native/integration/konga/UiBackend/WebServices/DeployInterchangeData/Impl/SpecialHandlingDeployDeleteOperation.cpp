/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */

#include <string>

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>
#include <konga/Db/DbUtils/DynamicDbRecordSet.h>

#include <konga/OperationPipeline/OperationPipeline.h>
#include <konga/OperationObjects/OperationType.h>

#include "DeployUtils.h"

#include "SpecialHandlingDeployDeleteOperation.h"

using namespace konga;
using namespace konga::interchange_deploy;
using std::vector;
using std::string;

bool SpecialHandlingDeployDeleteOperation::HandleRow(long operationId) {
	SpecialHandlingDeployDeleteDependents::HandleRow(operationId);

	m_operationLog.DeleteLogsForOperation(operationId);

	vector<OperationActivity> dummy;
	OperationPipeline::setOperationPipeline(operationId, dummy, m_dbConnection);

	                               
	string sql("DELETE FROM CommonSourceOperationsTab " \
		"WHERE Operation_ID = " + toString(operationId));
	m_dbConnection.ExecuteSql(sql);

	sql = "SELECT Operation_Type_Id, Source_Id, Schedule_Id FROM OperationsTab " \
		"WHERE Operation_Id = " + toString(operationId);

	SmartDbPtr<DynamicDbRecordSet> rs( DynamicDbRecordSet::MakeInstance(&m_dbConnection, sql) );
	rs->Open();
	if ( rs->Next() ) {
		const int operationTypeId = rs->IsFieldNull(0) ? -1 : atoi(rs->GetFieldValue(0).c_str());
		const long sourceId = rs->IsFieldNull(1) ? -1 : atol(rs->GetFieldValue(1).c_str());
		if ( sourceId > 0 ) {
			if ( IsJmsSource(sourceId, m_dbConnection) ) {
				m_bJmsOperationWasDeleted = true;
			}
		}

		const int scheduleId = rs->IsFieldNull(2) ? -1 : atoi(rs->GetFieldValue(2).c_str());
		if ( scheduleId > 0 )
			m_bScheduledOperationWasDeleted = true;
	}

	return true;
}

bool SpecialHandlingDeployDeleteOperation::JmsOperationWasDeleted() const {
	return m_bJmsOperationWasDeleted;
}

bool SpecialHandlingDeployDeleteOperation::ScheduledOperationWasDeleted() const {
	return m_bScheduledOperationWasDeleted;
}

