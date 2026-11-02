/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */

#ifndef KONGA_TRANSFORMATION_ENGINE_KONGA_PROTOCOL_RPC_INTERFACE
#define KONGA_TRANSFORMATION_ENGINE_KONGA_PROTOCOL_RPC_INTERFACE

#include <string>

#include <konga/Server/JavaLaunchDetails.h>		                           
#include <konga/Utils/ProcessUtils.h>			                      

#include "RpcInterface.h"
#include "TransformationEngineApi.h"			                           

namespace konga {

struct DbLocationConnectionInfo;

namespace TransformationEngine { 

class TRANSFORMATIONENGINE_API KongaProtocolRpcInterface : public RpcInterface {
public:
	KongaProtocolRpcInterface(void);
	virtual ~KongaProtocolRpcInterface(void) {};

	virtual ProcessIdT Create();
	virtual ProcessIdT Start();
	virtual void Stop();
	virtual void Shutdown();

	virtual void writeFlatDbSourceToFile(const konga::DbLocationConnectionInfo& connInfo,
										 std::string driverClass, 
                                         std::string sqlStatement,
                                         std::string outputFilePath,
                                         char delimiter);

	virtual void writeFlatDbSourceToTableXmlFile(const konga::DbLocationConnectionInfo& connInfo,
												 std::string driverClass, 
												 std::string sqlStatement,
												 std::string outputFilePath);


private:
	JavaLaunchDetails m_launchDetails;
	ProcessIdT m_pid;
	std::string m_sUserName, m_sPassword;
};

}                            
}                   


#endif                                                            
