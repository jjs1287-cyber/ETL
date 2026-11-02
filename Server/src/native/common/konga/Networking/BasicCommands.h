/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_BASIC_COMMANDS_H
#define KONGA_BASIC_COMMANDS_H

#include <string>
#include <vector>

namespace konga {
namespace networking {

                                                                                       
                                                                            
                                                                      

void Start(const std::string& sUserName, const std::string& sPassword,
		   unsigned long lCommandPort, const std::string& stComponentName);

void Stop(const std::string& sUserName, const std::string& sPassword,
		   unsigned long lCommandPort, const std::string& stComponentName);

void Shutdown(const std::string& sUserName, const std::string& sPassword,
		   unsigned long lCommandPort, const std::string& stComponentName);

std::vector<std::string> GenericCmd(const std::string& sCmd, const std::string& sUserName, const std::string& sPassword,
					   unsigned long lCommandPort, const std::string& stComponentName);

std::vector<std::string> GenericCmdWithArgs(const std::string& sCmd, const std::vector<std::string>& vArgs,
											const std::string& sUserName, const std::string& sPassword,
											unsigned long lCommandPort, const std::string& stComponentName);

}                   
}                        

#endif                          
