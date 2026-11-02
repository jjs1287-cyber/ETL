/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */

#ifndef CLEANUP_SERVICE_OPERATION_LOG_CLEANUP_RULE_H
#define CLEANUP_SERVICE_OPERATION_LOG_CLEANUP_RULE_H

#include "CleanupRule.h"

namespace konga {

	class OperationLogCleanupRule : public CleanupRule {
	public:
		OperationLogCleanupRule(long ageDays, const std::string& status )
			: m_valid(-1), m_ageDays(ageDays), m_stStatus(status) {}
		~OperationLogCleanupRule(void) {}

		std::string getName() const {
			return "OperationLog Archive";
		}

		bool ValidateRule(std::vector<std::string>& warnings);

		bool Execute(std::vector<std::string>& warnings);

		long getAgeDays() const;

		std::string getStatus() const;

	private:
		short m_valid;
		long m_ageDays;
		std::string m_stStatus;
	};

}                   

#endif                                                

