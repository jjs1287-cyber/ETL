/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */

#ifndef KONGA_TRANSFORMATION_ENGINE_RPC_INTERFACE
#define KONGA_TRANSFORMATION_ENGINE_RPC_INTERFACE

#include <string>

#include <konga/Utils/ProcessUtils.h>	             

namespace konga {

struct DbLocationConnectionInfo;

namespace TransformationEngine { 

   
  
                                                                  
                                                   
  
                                                        
    
   
class RpcInterface {
public:
	RpcInterface(void) {}
	virtual ~RpcInterface(void) {}

	virtual ProcessIdT Start() = 0;
	virtual void Stop() = 0;
	virtual void Shutdown() = 0;

	virtual void writeFlatDbSourceToFile(const konga::DbLocationConnectionInfo& connInfo,
										 std::string driverClass, 
										 std::string sqlStatement,
										 std::string outputFilePath,
										 char delimiter) = 0 ;

	virtual void writeFlatDbSourceToTableXmlFile(const konga::DbLocationConnectionInfo& connInfo,
												 std::string driverClass, 
												 std::string sqlStatement,
												 std::string outputFilePath) = 0 ;
};

}                            
}                   


#endif                                             
