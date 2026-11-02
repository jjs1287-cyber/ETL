/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#ifndef DB_MAINTENANCE_CLEANUP_RULE_H
#define DB_MAINTENANCE_CLEANUP_RULE_H

#include "CleanupRule.h"

namespace konga {

	                                                              
	  
	class DbMaintenanceCleanupRule : public CleanupRule {
	public:
		DbMaintenanceCleanupRule(const std::string& sConnectionString, bool bDoVacuum, bool bDoFullVacuum, bool bDoReindexing)
			: m_bDoVacuum(bDoVacuum), m_bDoFullVacuum(bDoFullVacuum), m_bDoReindexing(bDoReindexing),
			m_sConnectionString(sConnectionString) {}

		~DbMaintenanceCleanupRule(void) {}

		std::string getName() const {
			return "Database Maintenance (vacuum/re-index)";
		}

		bool ValidateRule(std::vector<std::string>& warnings);

		bool Execute(std::vector<std::string>& warnings);

	private:
		bool m_bDoVacuum;
		bool m_bDoFullVacuum;
		bool m_bDoReindexing;

		std::string m_sConnectionString;

	};
}

#endif                                 
