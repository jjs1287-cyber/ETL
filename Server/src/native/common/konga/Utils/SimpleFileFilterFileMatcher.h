/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#ifndef KONGA_UTILS_SIMPLE_FILE_FILTER_FILE_MATCHER_H
#define KONGA_UTILS_SIMPLE_FILE_FILTER_FILE_MATCHER_H

#include <string>

#include <boost/regex.hpp> 

#include "Matcher.h"

namespace konga {
	   
                                                                                  
                                                
                                                                   
                                                          
    
	class SimpleFileFilterFileMatcher : public Matcher {
	public:
		SimpleFileFilterFileMatcher(const std::string& stFileFilter, bool bCaseSensitive, bool bUseRegEx);
		virtual ~SimpleFileFilterFileMatcher(void);

		virtual bool isMatch(const std::string& sFileName) const;

	private:
		bool m_bCaseSensitive, m_bUseRegEx;
		std::string m_stSimpleFileFilter;
		boost::regex m_regex;
	};

}

#endif                                                 

