/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>
#include <konga/Db/DbUtils/DbException.h>

#include <konga/OperationLog/OperationLog.h>

#include "OperationLogCleanupRule.h"

using namespace konga;
using std::string;
using std::vector;

long OperationLogCleanupRule::getAgeDays() const {
	return m_ageDays;
}

string OperationLogCleanupRule::getStatus() const {
	return m_stStatus;
}

                                                                     
                                                     
                                                  

bool OperationLogCleanupRule::ValidateRule(std::vector<std::string>& warnings) {
	if ( m_valid >= 0 )
		return m_valid > 0;

	m_valid = 1;
	if ( !m_stStatus.empty() &&
		!EqualsIgnoreCase(m_stStatus, "OK") &&
		!EqualsIgnoreCase(m_stStatus, "Success") &&
		!EqualsIgnoreCase(m_stStatus, "Warning") &&
		!EqualsIgnoreCase(m_stStatus, "Alert") &&
		!EqualsIgnoreCase(m_stStatus, "Error") &&
		!EqualsIgnoreCase(m_stStatus, "Fatal") )
	{
		warnings.push_back("Status " + m_stStatus + " is not valid. " \
			"Valid values are: Success, Warning or Error.");
		m_valid = 0;
	}

	if ( m_ageDays < 0 ) {
		warnings.push_back("File age " + konga::toString(m_ageDays)
			+ " is invalid. The age in days has to be specified as a non-negative integer.");
		m_valid = 0;
	}

	return m_valid > 0;
}

bool OperationLogCleanupRule::Execute(vector<string>& warnings) {
	if ( !ValidateRule(warnings) )
		return true;	                                                                 

	try {
		SmartDbPtr<DbConnection> pConn( konga::GetBackEndDbConnection("TranDb") );
		OperationLog operationLog(*pConn);
		CrDateTime deleteBeforeDate = CrDateTime::GetCurrentUtcTime() - CrTimeDuration(getAgeDays()*24, 0, 0);
		operationLog.ArchiveOldLogs(deleteBeforeDate, getStatus());
	}
	catch( const CrException& ex ) {
		warnings.push_back( ex.GetErrorString() );
		return false;
	}

	return true;
}

