/*
 * Copyright (c) 1998-2006 Jitterbit, Inc.
 *
 */

#ifndef KONGA_UTILS_REGEXUTILS_H
#define KONGA_UTILS_REGEXUTILS_H

#include <vector>

namespace konga {
	                                                                      
	                                                   
	void RegExMatch(const std::string& sInput, const std::string& sRegEx,
		std::vector<std::string>& matches);

	                                                                 
	                               
	                                                              
	void RegExReplace(const std::string& sInput, const std::string& sRegEx,
		const std::string& sFormat, std::string& sResult);
}

#endif                            

