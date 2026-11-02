/*
 * Copyright (c) 1998-2006 Jitterbit, Inc.
 *
 */

#ifndef ETLFORGE_UTILS_ODBC_CONNECTION_STRING_UTILS_H
#define ETLFORGE_UTILS_ODBC_CONNECTION_STRING_UTILS_H

#include <string>

namespace konga {

	                                                                                 
	                                                                                  
	                                        
	std::string ReplacePassword(const std::string& connectStr, const std::string& newPassword);

	                                                                                         
	                                                                                      
	                                                                                             
	                          
	std::string ExtractAndReplacePassword(std::string& connectStr, const std::string& newPassword);

	                                                                                                          
	void AppendConnectString(std::string& sConnectStr, const std::string& sAdditionalConnectParameters);

	                                                                                         
	bool HasPwd(const std::string& connectStr);

}
#endif                                                 

