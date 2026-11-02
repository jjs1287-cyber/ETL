/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#ifndef KONGA_UTILS_STRING_MATCHER_H
#define KONGA_UTILS_STRING_MATCHER_H

#include <string>

namespace konga {

	                                          
	class Matcher {
	public:
		Matcher(void) {};
		virtual ~Matcher(void) {};

		                                        
		virtual bool isMatch(const std::string& s) const = 0;
	};

	                                                
	class AllFilesMatcher : public Matcher {
	public:
		AllFilesMatcher(void) {}
		~AllFilesMatcher(void) {}
		bool isMatch(const std::string& sFileName) const { return true; }
	};

}

#endif                                

