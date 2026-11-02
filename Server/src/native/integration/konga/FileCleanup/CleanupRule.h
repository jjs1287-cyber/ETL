/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */

#ifndef CLEANUP_SERVICE_CLEANUP_RULE_H
#define CLEANUP_SERVICE_CLEANUP_RULE_H

#include <vector>
#include <string>

namespace konga {

	                              
	                                      
	class CleanupRule {
	public:
		CleanupRule(void) {}
		virtual ~CleanupRule(void) {}

		virtual std::string getName() const = 0;

		virtual bool ValidateRule(std::vector<std::string>& warnings) = 0;

		virtual bool Execute(std::vector<std::string>& warnings) = 0;

	};
}             

#endif                                  

