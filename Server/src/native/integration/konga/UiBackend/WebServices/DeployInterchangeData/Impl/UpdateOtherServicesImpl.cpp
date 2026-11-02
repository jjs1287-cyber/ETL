/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */

#include <konga/Utils/CrException.h>
#include <konga/Utils/Settings.h>					                 

#include <konga/Networking/BasicCommands.h>
#include <konga/Server/Server.h>					                              
#include <konga/JmsEngine/ComponentInfo.h>
#include <konga/ScheduleUtils/UpdateScheduler.h>

#include <konga/UiBackend/WebServices/Utils/UiBackendLogging.h>		                  

#include "DeployInterchangeDataImpl.h"

using namespace konga;
using namespace konga::interchange_deploy;

void DeployInterchangeDataImpl::UpdateScheduler() const {
	try {
		konga::UpdateScheduler();
	}
	catch( const CrException& ex ) {
		const string& stWarning = "Failed to send update command to the ETLForge Scheduler." \
			" This means that updated schedules will not be executed until the" \
			" ETLForge Scheduler has been restarted.\n" + ex.GetErrorString();
		UIBACKEND_TRACE(stWarning);
		AddWarning(0, "", stWarning);
	}
}

void DeployInterchangeDataImpl::UpdateJmsEngine() const {
	try {
		string stUserName, stPassword;
		GetInternalAdminCredentials(stUserName, stPassword);
		const long lPort = konga::GetOptionAsInt(stJMS_SERVICE_COMPONENT_NAME,
			"CommandPort", lJMS_SERVICE_DEFAULT_PORT);
		if ( lPort < 0 ) {
			throw CrException("Server Configuration Error: JMSEngine/CommandPort is negative");
		}

		networking::GenericCmd("Update", stUserName, stPassword, static_cast<unsigned long>(lPort), stJMS_SERVICE_COMPONENT_NAME);
	}
	catch( const CrException& ex ) {
		const string& stWarning = "Failed to send update command to the ETLForge JMS Engine." \
			" This means that updated JMS Sources will not be active until the" \
			" ETLForge JMS Engine has been restarted.\n" + ex.GetErrorString();
		UIBACKEND_TRACE(stWarning);
		AddWarning(0, "", stWarning);
	}
}
