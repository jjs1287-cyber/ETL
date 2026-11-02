/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */

#include <konga/Utils/util.h>
#include <konga/Utils/FileUtils.h>
#include <konga/Utils/CrException.h>

#include <konga/DbRelations/EntityIdConstants.h>

#include <konga/Zip/ZipUtils.h>
#include <konga/Server/Server.h>

#include <konga/PipelinePluginEntityManager/PipelinePluginEntityManager.h>
#include <konga/PipelinePluginExecutor/PipelinePluginExecutor.h>
#include <konga/PipelinePluginExecutor/OutputFiles.h>

#include <konga/PluginManager/PluginManifestStore.h>
#include <konga/PluginManager/PipelinePosition.h>
#include <konga/PluginManager/PipelinePluginManifest.h>
#include "TreeMapperUtils.h"

using namespace konga;
using std::string;
using std::vector;

namespace {
	string GetTmpFileNameForGuid(const string& stGuid) {
		return string(GetTemporaryLoadedSourceDataDirWithSlash()+stGuid);
	}
}

void konga::WriteIncomingSourceDataToFile(const char* const pZippedBase64EncodedData,
		string& stData, string& stSourceFileName)
{
	const string stFileName(GetTmpFileNameForGuid(konga::GetGUIDString()));
	
	                                                                          
	                                    
	if ( !pZippedBase64EncodedData ) {
		stData.clear();
		write_string_to_file(stFileName, stData);
	}
	else {
		                                                                           
		ZipUtils::DecodeUnzipAndWriteToFile(stFileName, pZippedBase64EncodedData, false);
		read_file_to_string(stFileName, stData);
	}

	stSourceFileName = stFileName;
}

  
                                                                          
                           
 
                                               
                               
                        
  
                                                        
                                             
 
  

  
                                                                                       
                          
                            
                                  
                                                            
                                                                       
         
  

                                   
                        
                                                            
                           
                 
         
  
                                    
                                                                                     
                                                    
  

                                                        
                               
                                                                         

                                         
 
  

bool konga::RunSourcePlugin(const string& stUniquePluginName,
					 const string& stInputFile,
					 const string& stRelativePosition,
					 const OperationContext& context,
					 const OperationDataElementHolder& deHolder,
					 PluginManifestStore& pluginStore,
					 DbConnection& tranDbConn)
{
	PipelinePluginEntityManager mgr(tranDbConn);
	const long lPluginId = mgr.GetPluginId(stUniquePluginName);
	if ( lPluginId == 0 )
		throw CrException("Failed to run plugin '" + stUniquePluginName
		+ "'. The plugin does not exist on this server.");

	boost::shared_ptr<PluginManifest> pPluginManifest( pluginStore.GetManifest(lPluginId) );

	PipelinePluginManifest* pManifest =
		dynamic_cast<PipelinePluginManifest*>(pPluginManifest.get());
	if ( pManifest == 0 ) {
		throw CrException("The plugin '" + stUniquePluginName
			+ "' is not a pipeline plugin.");
	}

	const int iRelativePositionId = PipelinePosition::GetRelativePositionId(stRelativePosition, SourceEntityId);
	vector<string> inputFiles;
	if ( iRelativePositionId == PIPELINE_POSITION_POST ) {
		inputFiles.push_back(stInputFile);
	}

	PipelinePosition pipelinePosition(SourceEntityId, iRelativePositionId, 50, false);
	bool bHasNewFiles = false;
	bool bInputChanged = false;
	vector<OutputFile> outputFiles;
	PipelinePluginExecutor executor;
	executor.ExecutePipelinePlugin(*pManifest, pipelinePosition, context, deHolder, inputFiles, bHasNewFiles, outputFiles);
	if ( bHasNewFiles ) {
		if ( outputFiles.empty() ) {
			throw CrException("No source file remaining after running plugin '"
				+ stUniquePluginName + "'. Please run a plugin that produces some output.");
		}
		bInputChanged = true;
		const OutputFile& newSourceFile = *outputFiles.begin();
		const string& stNewSourceFile = newSourceFile.Name;
		                                                                                     
		                                                    
		if ( !EquivalentPaths(stNewSourceFile, stInputFile) ) {
			if ( !FileExists(stNewSourceFile) ) {
				throw CrException("The plugin '" + stUniquePluginName
					+ "' produced an inconsistent result. It reported that a file was created at '"
					+ stNewSourceFile + "' but that file does not exist. Please contact the author of the plugin for support.");
			}
			kCopyFile(stNewSourceFile, stInputFile, true);
		}
	}

	return bInputChanged;
}
