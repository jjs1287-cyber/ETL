/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */

#include <string>
#include <memory>		           

#include <boost/scoped_array.hpp>

#include <konga/Utils/Settings.h>	                           
#include <konga/Utils/FileUtils.h>	              
#include <konga/Utils/DBConnectionInfo.h>

#include <konga/Utils/PortDefs.h>	                                             
#include <konga/Db/DbUtils/SmartDbPtr.h>
#include <konga/Db/DbUtils/DynamicDbRecordSet.h>
#include <konga/Db/DbUtils/DbConnection.h>

#include <konga/PluginManager/PluginManager.h>

#include <konga/ItemAccess/UserAccess.h>

#include <konga/UiBackend/WebServices/Utils/WsAllocations.h>

#include "../FilterAsXml.hpp"
#include "../FilterAsXml_Array.hpp"

#include "PluginManagementImpl.h"


using namespace konga;
using std::string;
using std::auto_ptr;
using namespace boost;

FilterAsXml_Array* PluginManagementImpl::GetFilters() {

	  

                                                                               

                                   
                                                                                               

                                                 
                                          
 
                                                

                                                                
                                               
                                                           
                       
                                                         

                                 
  
                                                     
                                                                                                     
                                                                      
                                                                          
   
  
                                 

                        
                                       
               
                  
                               
         
            
                                    
         
          
                                                                                                   
  
                                                    
                                           
                                                                                           
                                                                     
  

                                                           
                                                                                          
  
   

	                          
	auto_ptr<FilterAsXml_Array> pRetVal( new FilterAsXml_Array() );

	                                
	vector<string> pipelinePlugins;
	PluginManager::GetPipelinePlugins(pipelinePlugins);
	const unsigned long nFilters = pipelinePlugins.size();

	if( nFilters > 0 ) {
		FilterAsXml** arrFilterAsXml = allocate_pointer_array<FilterAsXml>(nFilters);
		AutoDeletePointerArray<FilterAsXml> adopt_arrDownloadedFile(arrFilterAsXml, nFilters);

		                                                        
		vector<string>::const_iterator citFilterFileName = pipelinePlugins.begin();
		for(int i=0; citFilterFileName != pipelinePlugins.end(); ++citFilterFileName, ++i) {
			auto_ptr<FilterAsXml> pFilterAsXml( new FilterAsXml() );
			long nBytesRead=0;
			string sFileReadError;
			char* pRawFileBuf = read_file(*citFilterFileName, nBytesRead, sFileReadError);
			if( !pRawFileBuf )
				continue;                          
			scoped_array<char> pFileBuf( pRawFileBuf );
			pFilterAsXml->setxmlRepresentation(pFileBuf.get(), true);
			arrFilterAsXml[i] = pFilterAsXml.release();
		}

		pRetVal->set(arrFilterAsXml, nFilters);
	}

	return pRetVal.release();
}

