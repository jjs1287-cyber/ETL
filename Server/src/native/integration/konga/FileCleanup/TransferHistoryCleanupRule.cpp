/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <konga/Utils/DateUtils.h>
#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>
#include <konga/Db/DbUtils/DbException.h>

#include <konga/OperationLog/TransferHistoryUtils.h>

#include "TransferHistoryCleanupRule.h"

using namespace konga;
using std::string;
using std::vector;

long TransferHistoryCleanupRule::getAgeDays() const {
	return m_ageDays;
}

                                                                     
                                                     
                                                  

bool TransferHistoryCleanupRule::ValidateRule(std::vector<std::string>& warnings) {
	if ( m_valid >= 0 )
		return m_valid > 0;

	m_valid = 1;
	if ( m_ageDays < 0 ) {
		warnings.push_back("File age " + konga::toString(m_ageDays)
			+ " is invalid. The age in days has to be specified as a non-negative integer.");
		m_valid = 0;
	}

	return m_valid > 0;
}

bool TransferHistoryCleanupRule::Execute(vector<string>& warnings) {
	if ( !ValidateRule(warnings) )
		return true;	                                                                 

	try {
		SmartDbPtr<DbConnection> pConn( konga::GetBackEndDbConnection("TranDb") );
		TransferHistoryUtils transferHistoryUtils(*pConn);
		CrDateTime deleteBeforeDate = CrDateTime::GetCurrentUtcTime() - CrTimeDuration(getAgeDays()*24, 0, 0);
		transferHistoryUtils.ArchiveOldLogs(deleteBeforeDate);
	}
	catch( const CrException& ex ) {
		warnings.push_back( ex.GetErrorString() );
		return false;
	}

	return true;
}

