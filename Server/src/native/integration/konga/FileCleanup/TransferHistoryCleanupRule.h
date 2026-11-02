/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */

#ifndef CLEANUP_SERVICE_TRANSFER_HISTORY_CLEANUP_RULE_H
#define CLEANUP_SERVICE_TRANSFER_HISTORY_CLEANUP_RULE_H

#include "CleanupRule.h"

namespace konga {

	class TransferHistoryCleanupRule : public CleanupRule {
	public:
		TransferHistoryCleanupRule(long ageDays)
			: m_valid(-1), m_ageDays(ageDays) {}
		~TransferHistoryCleanupRule(void) {}

		std::string getName() const {
			return "Transfer History Archive";
		}

		bool ValidateRule(std::vector<std::string>& warnings);

		bool Execute(std::vector<std::string>& warnings);

		long getAgeDays() const;

	private:
		short m_valid;
		long m_ageDays;
	};

}                   

#endif                                                   

