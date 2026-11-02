/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#ifndef TREE_MAPPER_INFO_PROVIDER_UTILS_H
#define TREE_MAPPER_INFO_PROVIDER_UTILS_H

#include <string>

namespace konga {

	class OperationContext;
	class OperationDataElementHolder;
	class DbConnection;
	class PluginManifestStore;

	                                                                          
	                                                        
	                                                                
	                                                                             
	                        
	                                            
	void WriteIncomingSourceDataToFile(const char* const pZippedBase64EncodedData,
		std::string& stData, std::string& stSourceFileName);

                                                                           
                         

                                                                                   
                                              

	                                              
	                                        
	bool RunSourcePlugin(const std::string& stUniquePluginName, const std::string& stInputFile,
		const std::string& stRelativePosition, const OperationContext& context, const OperationDataElementHolder& deHolder,
		PluginManifestStore& pluginStore, DbConnection& tranDbConn);

}

#endif                                     
