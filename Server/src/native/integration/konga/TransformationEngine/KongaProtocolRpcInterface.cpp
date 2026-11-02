/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */

#include <memory>	                

#include <konga/Utils/CrException.h>	              
#include <konga/Utils/ProcessUtils.h>	          
#include <konga/Utils/Settings.h>		                 
#include <konga/Utils/StringUtils.h>	           

#include <konga/Log/logging.h>			                

#include <konga/Networking/BasicCommands.h>           
#include <konga/Networking/CommandUtils.h>
#include <konga/Networking/Command.h>

#include <konga/Server/Server.h>		                                                        

#include <konga/TransformationUtils/DbLocationConnectionInfo.h>

#include "componentinfo.h"
#include "KongaProtocolRpcInterface.h"

using namespace konga;
using namespace konga::networking;
using namespace konga::TransformationEngine;
using std::string;

KongaProtocolRpcInterface::KongaProtocolRpcInterface() : m_pid(0) {
	GetJavaLaunchDetails(m_launchDetails, konga::stTRANSFORMATION_ENGINE_COMPONENT_NAME,
							konga::lTRANSFORMATION_ENGINE_DEFAULT_PORT);
	GetInternalAdminCredentials(m_sUserName, m_sPassword);
}

ProcessIdT KongaProtocolRpcInterface::Create() {
	try {
		m_pid = RunProc(m_launchDetails.sProcessPath, m_launchDetails.sArgs, m_launchDetails.sWorkingDir,
			m_launchDetails.sStdOutPath, m_launchDetails.sStdErrPath);
		return m_pid;
	} catch(CrException& e) {
		string sErr = "Failed to start " + konga::stTRANSFORMATION_ENGINE_COMPONENT_NAME +
			", with the following startup parameters:" +
			"\nPath: " + m_launchDetails.sProcessPath + 
			"\nArguments: " + m_launchDetails.sArgs +
			"\nWorking directory: " + m_launchDetails.sWorkingDir +
			"\nstandard output path: " + m_launchDetails.sStdOutPath +
			"\nstandard error path: " + m_launchDetails.sStdErrPath +
			"\n\nReason: " + e.GetErrorString();
		ReportError(sErr, konga::stTRANSFORMATION_ENGINE_COMPONENT_NAME);
		throw;
	}
}

ProcessIdT KongaProtocolRpcInterface::Start() {
		try {
			ProcessIdT pid = GetProcId(m_launchDetails.sProcessPath);
			if( pid != 0 ) {
				try {
					networking::Start(m_sUserName, m_sPassword, m_launchDetails.lPort,
						konga::stTRANSFORMATION_ENGINE_COMPONENT_NAME);
					return pid;
				} catch(CrException&) {
					                                                    
					                                                            
					                                                 
				}
			}

			return Create();
		} catch(CrException& e) {
			string sErr = "Failed to start " + konga::stTRANSFORMATION_ENGINE_COMPONENT_NAME +
				". Reason: " + e.GetErrorString();
			ReportError(sErr, konga::stTRANSFORMATION_ENGINE_COMPONENT_NAME);
			throw;
		}
}

void KongaProtocolRpcInterface::Stop() {
	networking::Stop(m_sUserName, m_sPassword, 
		m_launchDetails.lPort, konga::stTRANSFORMATION_ENGINE_COMPONENT_NAME);
}

void KongaProtocolRpcInterface::Shutdown() {
	networking::Shutdown(m_sUserName, m_sPassword, 
		m_launchDetails.lPort, konga::stTRANSFORMATION_ENGINE_COMPONENT_NAME);
}

void KongaProtocolRpcInterface::writeFlatDbSourceToFile(const DbLocationConnectionInfo& connInfo, 
														string driverClass, 
														string sqlStatement, 
														string outputFilePath, 
														char delimiter)
{
	std::auto_ptr<Command> pCommand( new Command("WriteFlatDbSourceToFile") );
	pCommand->AddArgument(driverClass);
	pCommand->AddArgument(connInfo.DriverName);
	pCommand->AddArgument(connInfo.Server);
	pCommand->AddArgument(toString(connInfo.Port));
	pCommand->AddArgument(connInfo.Database);
	pCommand->AddArgument(connInfo.User);
	pCommand->AddArgument(connInfo.Password);
	pCommand->AddArgument(sqlStatement);
	pCommand->AddArgument(outputFilePath);
	pCommand->AddArgument(toString(delimiter));

	const long lPort = konga::GetOptionAsInt(stTRANSFORMATION_ENGINE_COMPONENT_NAME, 
												"CommandPort", lTRANSFORMATION_ENGINE_DEFAULT_PORT);
	SendCommand(pCommand.release(), m_sUserName, m_sPassword, stTRANSFORMATION_ENGINE_COMPONENT_NAME, lPort);
}

void KongaProtocolRpcInterface::writeFlatDbSourceToTableXmlFile(const konga::DbLocationConnectionInfo& connInfo,
																string driverClass, 
																string sqlStatement,
																string outputFilePath)
{
	std::auto_ptr<Command> pCommand( new Command("WriteFlatDbSourceToFileInTableFormat") );
	pCommand->AddArgument(driverClass);
	pCommand->AddArgument(connInfo.DriverName);
	pCommand->AddArgument(connInfo.Server);
	pCommand->AddArgument(toString(connInfo.Port));
	pCommand->AddArgument(connInfo.Database);
	pCommand->AddArgument(connInfo.User);
	pCommand->AddArgument(connInfo.Password);
	pCommand->AddArgument(sqlStatement);
	pCommand->AddArgument(outputFilePath);
	const long lPort = konga::GetOptionAsInt(stTRANSFORMATION_ENGINE_COMPONENT_NAME, 
												"CommandPort", lTRANSFORMATION_ENGINE_DEFAULT_PORT);
	SendCommand(pCommand.release(), m_sUserName, m_sPassword, stTRANSFORMATION_ENGINE_COMPONENT_NAME, lPort);
}
