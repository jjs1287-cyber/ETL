/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#include <konga/Utils/StatusReceiver.h>
#include <konga/Utils/CrException.h>

#include <konga/Db/DbMaintenance/PgSqlMaintenance.h>

#include "DbMaintenanceCleanupRule.h"

using namespace konga;
using std::string;
using std::vector;

namespace {
	class CleanupServiceStatusReceiver : public StatusReceiver {
	public:
		CleanupServiceStatusReceiver(vector<string>& warnings)
			: m_warnings(warnings) {}
		~CleanupServiceStatusReceiver(void) {}

		void Status(const std::string& sStatus) {
		}

		                                                                      
		                                        
		void Progress(float prog) {
		}

		                                                                    
		void ReportError(const std::string& sError) {
			m_warnings.push_back(sError);
		}

	private:
		vector<string>& m_warnings;
	};
}

bool DbMaintenanceCleanupRule::ValidateRule(vector<string>& warnings) {
	                                                       
	return true;
}

bool DbMaintenanceCleanupRule::Execute(vector<string>& warnings) {
	bool retVal = true;
	CleanupServiceStatusReceiver status(warnings);
	
	if ( m_bDoVacuum ) {
		try {
			konga::PgSqlVacuum(m_sConnectionString, m_bDoFullVacuum, status);
		}
		catch( const CrException& ex ) {
			warnings.push_back("Failed to vacuum database: " + ex.GetErrorString());
			retVal = false;
		}
	}

	if ( m_bDoReindexing ) {
		try {
			konga::PgSqlReindex(m_sConnectionString, status);
		}
		catch( const CrException& ex ) {
			warnings.push_back("Failed to reindex database: " + ex.GetErrorString());
			retVal = false;
		}
	}

	return retVal;
}

