/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
#include <memory>		           

#include <konga/Utils/BinaryData.h>
#include <konga/Utils/Settings.h>

#include <konga/Networking/CrStreamSocket.h>
#include <konga/Networking/Request.h>
#include <konga/Networking/Response.h>
#include <konga/Networking/Command.h>
#include <konga/Networking/Result.h>
#include <konga/Networking/CommandUtils.h>
#include <konga/Networking/BasicCommands.h>	                                     

#include <konga/ProcessEngine/Engine/ui_enums.h>		                           
#include <konga/Scheduler/ComponentInfo.h>
#include <konga/ProcessEngine/ComponentInfo.h>
#include <konga/JmsEngine/ComponentInfo.h>

#include <konga/UiBackend/WebServices/Utils/WsUtils.h>
#include <konga/UiBackend/WebServices/Utils/UiBackendLogging.h>		                  

#include "AdminImpl.h"

using namespace axiscpp;
using namespace konga;
using namespace konga::networking;
using std::string;

namespace {
	void AdminAuditLog(const string& user, const string& server, const string& action) {
		auditLog(user + " " + action + " the " + server);
	}
}

AdminImpl::AdminImpl(const char* user, const char* password)
: WsImplBaseAuthenticator(user, password)
{
	if ( !m_UserInfo.IsAdministrator() )
		throw CrException("User " + m_UserInfo.GetLogin() + " is not an administrator.");
	m_user=toString(user);
	m_password=toString(password);
}

                                       
ServerStatus* AdminImpl::getServerStatus(int iDetailLevel, unsigned long lCommandPort, const char*sServerName) {
	Request req(m_user, m_password);
	Command* pCmd = new Command("GetStatus");
	pCmd->AddArgument( toString(iDetailLevel) );
	req.AddCommand( pCmd );
	
	try {
		auto_ptr<Response> pResp( SendRequest(req, lCommandPort, sServerName) );

		if( pResp->res_size() != 1 )
			throw CrException("Incorrect number of command results in response to GetStatus command. Got " +
				toString(pResp->res_size()) + ", expected 1.");
		const Result& result = **pResp->res_begin();

		if( result.ret_val_size() != 2 )
			throw CrException("Incorrect number of return values in response to GetStatus command. Got " +
				toString(result.ret_val_size()) + ", expected 2.");

		const string& sStatusEnum = *result.ret_val_begin();
		const string& sStatusString = *(result.ret_val_begin() + 1);

		auto_ptr<ServerStatus> pStatus( new ServerStatus() );
		pStatus->setDescription(const_cast<xsd__string>(sStatusString.c_str()), true);
		pStatus->StatusBits = atol(sStatusEnum.c_str());
		return pStatus.release();
	} catch( const CrSocketException& e) {
		UIBACKEND_TRACE("Socket exception calling GetStatus: " + e.GetErrorString());
		const string stError("Server is unreachable. (Reason: " + e.GetErrorString() + ")");

		auto_ptr<ServerStatus> pStatus( new ServerStatus() );
		pStatus->setDescription(const_cast<xsd__string>(stError.c_str()), true);
		pStatus->StatusBits = rsUNREACHABLE;
		return pStatus.release();
	}
}

                                       
xsd__boolean AdminImpl::isServerRunning(unsigned long lCommandPort, const char*pServerName) {
    std::auto_ptr<ServerStatus> status(getServerStatus(0, lCommandPort, pServerName));
    return (status->StatusBits & rsRUNNING) && !(status->StatusBits & (rsERROR | rsUNREACHABLE)) ? true_ : false_;
}

                 
  
void AdminImpl::startEngine() {
	long lCommandPort = konga::GetOptionAsInt(stPROCESSENGINE_COMPONENT_NAME, "CommandPort", lPROCESSENGINE_DEFAULT_PORT);
	Start(m_user, m_password, lCommandPort, stPROCESSENGINE_COMPONENT_NAME);
	AdminAuditLog(m_user, stPROCESSENGINE_COMPONENT_NAME, "started");
}

void AdminImpl::stopEngine() {
	long lCommandPort = konga::GetOptionAsInt(stPROCESSENGINE_COMPONENT_NAME, "CommandPort", lPROCESSENGINE_DEFAULT_PORT);
	Stop(m_user, m_password, lCommandPort, stPROCESSENGINE_COMPONENT_NAME);
	AdminAuditLog(m_user, stPROCESSENGINE_COMPONENT_NAME, "stopped");
}

xsd__boolean AdminImpl::isEngineRunning() {
	long lCommandPort = konga::GetOptionAsInt(stPROCESSENGINE_COMPONENT_NAME, "CommandPort", lPROCESSENGINE_DEFAULT_PORT);
	return isServerRunning(lCommandPort, stPROCESSENGINE_COMPONENT_NAME.c_str());
}

ServerStatus* AdminImpl::getEngineStatus(int iDetailLevel) {
	long lCommandPort = konga::GetOptionAsInt(stPROCESSENGINE_COMPONENT_NAME, "CommandPort", lPROCESSENGINE_DEFAULT_PORT);
	return getServerStatus(iDetailLevel, lCommandPort, stPROCESSENGINE_COMPONENT_NAME.c_str());
}

                    
  
void AdminImpl::startScheduler() {
	long lCommandPort = konga::GetOptionAsInt(stSCHEDULER_COMPONENT_NAME, "CommandPort", lSCHEDULER_DEFAULT_PORT);
	Start(m_user, m_password, lCommandPort, stSCHEDULER_COMPONENT_NAME);
	AdminAuditLog(m_user, stSCHEDULER_COMPONENT_NAME, "started");
}

void AdminImpl::stopScheduler() {
	long lCommandPort = konga::GetOptionAsInt(stSCHEDULER_COMPONENT_NAME, "CommandPort", lSCHEDULER_DEFAULT_PORT);
	Stop(m_user, m_password, lCommandPort, stSCHEDULER_COMPONENT_NAME);
	AdminAuditLog(m_user, stSCHEDULER_COMPONENT_NAME, "stopped");
}

xsd__boolean AdminImpl::isSchedulerRunning() {
	long lCommandPort = konga::GetOptionAsInt(stSCHEDULER_COMPONENT_NAME, "CommandPort", lSCHEDULER_DEFAULT_PORT);
	return isServerRunning(lCommandPort, stSCHEDULER_COMPONENT_NAME.c_str());
}

ServerStatus* AdminImpl::getSchedulerStatus(int iDetailLevel) {
	long lCommandPort = konga::GetOptionAsInt( stSCHEDULER_COMPONENT_NAME, "CommandPort", lSCHEDULER_DEFAULT_PORT);
	return getServerStatus(iDetailLevel, lCommandPort, stSCHEDULER_COMPONENT_NAME.c_str());
}

                    

  
void AdminImpl::startJmsEngine() {
	long lCommandPort = konga::GetOptionAsInt(stJMS_SERVICE_COMPONENT_NAME, "CommandPort", lJMS_SERVICE_DEFAULT_PORT);
	Start(m_user, m_password, lCommandPort, stJMS_SERVICE_COMPONENT_NAME);
	AdminAuditLog(m_user, stJMS_SERVICE_COMPONENT_NAME, "started");
}

void AdminImpl::stopJmsEngine() {
	long lCommandPort = konga::GetOptionAsInt(stJMS_SERVICE_COMPONENT_NAME, "CommandPort", lJMS_SERVICE_DEFAULT_PORT);
	Stop(m_user, m_password, lCommandPort, stJMS_SERVICE_COMPONENT_NAME);
	AdminAuditLog(m_user, stJMS_SERVICE_COMPONENT_NAME, "stopped");
}

xsd__boolean AdminImpl::isJmsEngineRunning() {
	long lCommandPort = konga::GetOptionAsInt(stJMS_SERVICE_COMPONENT_NAME, "CommandPort", lJMS_SERVICE_DEFAULT_PORT);
	return isServerRunning(lCommandPort, stJMS_SERVICE_COMPONENT_NAME.c_str());
}

ServerStatus* AdminImpl::getJmsEngineStatus(int iDetailLevel) {
	long lCommandPort = konga::GetOptionAsInt( stJMS_SERVICE_COMPONENT_NAME, "CommandPort", lJMS_SERVICE_DEFAULT_PORT);
	return getServerStatus(iDetailLevel, lCommandPort, stJMS_SERVICE_COMPONENT_NAME.c_str());
}
