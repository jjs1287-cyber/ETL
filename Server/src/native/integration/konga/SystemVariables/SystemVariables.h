/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef KONGA_SYSTEM_VARIABLES_H
#define KONGA_SYSTEM_VARIABLES_H

#include <string>

namespace konga {

	class OperationDataElementHolder;

	                                                      
	                                                     
	                                                                                   
	                                         
	std::string GetSystemVariable(const std::string& sName, const OperationDataElementHolder* const pDEHolder, const std::string& sDefault);

	                                                 
	long GetSystemVariableAsLong(const std::string& sName, const OperationDataElementHolder* const pDEHolder, long lDefault);
	bool GetSystemVariableAsBool(const std::string& sName, const OperationDataElementHolder* const pDEHolder, bool bDefault);
}

#endif                            
