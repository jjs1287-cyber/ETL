/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#ifndef TRANSFORMATION_DATA_STORE_CLEANUP_RULE_H
#define TRANSFORMATION_DATA_STORE_CLEANUP_RULE_H

#include "CleanupRule.h"

namespace konga {

	                         
	  
	class TransformationDataStoreRule : public CleanupRule {
	public:
		TransformationDataStoreRule(int ageDays) : m_iAgeDays(ageDays) {}

		~TransformationDataStoreRule(void) {}

		std::string getName() const {
			return "Transformation Data Store cleanup";
		}

		bool ValidateRule(std::vector<std::string>& warnings);

		bool Execute(std::vector<std::string>& warnings);

	private:
		int m_iAgeDays;
	};
}


#endif                                            

