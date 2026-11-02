/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */

#include <konga/Utils/CrException.h>
#include <konga/Utils/Settings.h>					                 

#include <konga/Networking/BasicCommands.h>
#include <konga/Server/Server.h>					                              
#include <konga/Scheduler/ComponentInfo.h>

#include "UpdateScheduler.h"

using namespace konga;
using std::string;

void konga::UpdateScheduler() {
	string stUserName, stPassword;
	GetInternalAdminCredentials(stUserName, stPassword);
	const long lPort = konga::GetOptionAsInt(stSCHEDULER_COMPONENT_NAME,
		"CommandPort", lSCHEDULER_DEFAULT_PORT);
	if ( lPort < 0 ) {
		throw CrException("Server Configuration Error: Scheduler/CommandPort is negative");
	}

	networking::GenericCmd("Update", stUserName, stPassword, static_cast<unsigned long>(lPort), stSCHEDULER_COMPONENT_NAME);
}

