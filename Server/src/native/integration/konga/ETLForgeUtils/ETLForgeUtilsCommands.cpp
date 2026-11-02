/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */

#include <iostream>

#include <vector>

#include <konga/Utils/CrException.h>
#include <konga/Utils/StringUtils.h>			          
#include <konga/Utils/Settings.h>			          

#include <konga/Networking/BasicCommands.h>		               

#include <konga/Server/Server.h>				                                

                                                           
#include <konga/ProcessEngine/ComponentInfo.h>
#include <konga/Scheduler/ComponentInfo.h>
#include <konga/FileCleanup/ComponentInfo.h>
#include <konga/JmsEngine/ComponentInfo.h>


#include "ETLForgeUtilsCommands.h"

using namespace konga;
using std::string;
using std::vector;

void konga::SendCommandToModule(const string& module, const string& command, bool bQuiet) {

	                                                  
	                                        
	string module_lower(module);
	ToLower(module_lower);

	string module_name;
	long defaultPort = 0;
	if ( module_lower.find("process") != string::npos &&
		module_lower.find("engine") != string::npos )
	{
		module_name = stPROCESSENGINE_COMPONENT_NAME;
		defaultPort = lPROCESSENGINE_DEFAULT_PORT;
	}
	else if ( module_lower.find("scheduler") != string::npos ) {
		module_name = stSCHEDULER_COMPONENT_NAME;
		defaultPort = lSCHEDULER_DEFAULT_PORT;
	}
	else if ( module_lower.find("cleanup") != string::npos ) {
		module_name = stFILE_CLEANUP_COMPONENT_NAME;
		defaultPort = lFILE_CLEANUP_DEFAULT_PORT;
	}
	else if ( module_lower.find("jms") != string::npos ) {
		module_name = stJMS_SERVICE_COMPONENT_NAME;
		defaultPort = lJMS_SERVICE_DEFAULT_PORT;
	}

	                                            
	if ( module_name.empty() )
		throw CrException("There is no module with the name \"" + module
		+ "\"\nValid module names are:\n"
		+ stPROCESSENGINE_COMPONENT_NAME + "\n"
		+ stSCHEDULER_COMPONENT_NAME + "\n"
		+ stFILE_CLEANUP_COMPONENT_NAME + "\n"
		+ stJMS_SERVICE_COMPONENT_NAME);

	                       
	long configuredPort = 
		konga::GetOptionAsInt(module_name, "CommandPort", defaultPort);

	string userName, pwd;
	GetInternalAdminCredentials(userName, pwd);

	vector<string> result = networking::GenericCmd(command, userName, pwd,
		static_cast<unsigned long>(configuredPort), module_name);

	if ( bQuiet )
		return;

	if ( result.empty() )
		std::cout << "Command succeded with no response." << std::endl;
	else
		std::cout << "Command succeded with response:" << std::endl;

	for( vector<string>::const_iterator cit = result.begin();
		cit != result.end(); ++cit ) {
			std::cout << *cit << std::endl;
	}
}

