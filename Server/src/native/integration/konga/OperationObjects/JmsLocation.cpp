/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#include <memory>	                

#include <konga/JmsEngine/ComponentInfo.h>
#include <konga/Networking/CommandUtils.h>
#include <konga/Networking/Command.h>
#include <konga/Server/Server.h>			                              
#include <konga/Utils/Settings.h>			                 

#include "JmsLocation.h"

using namespace konga;
using namespace konga::networking;
using std::string;
using std::vector;
using std::map;

void JmsLocation::testJmsLocation() const {

	std::auto_ptr<Command> pCommand( new Command("TestConnection") );
	map<string, string>::const_iterator cit = m_locationProperties.begin();
	for(; cit != m_locationProperties.end(); ++cit ) {
		const string& keyName = cit->first;
		const string& value = cit->second;
		pCommand->AddArgument(keyName);
		pCommand->AddArgument(value);
	}

	string sUserName, sPassword;
	GetInternalAdminCredentials(sUserName, sPassword);

	const long lPort = konga::GetOptionAsInt(stJMS_SERVICE_COMPONENT_NAME, "CommandPort", lJMS_SERVICE_DEFAULT_PORT);
	SendCommand(pCommand.release(), sUserName, sPassword, stJMS_SERVICE_COMPONENT_NAME, lPort);
}
