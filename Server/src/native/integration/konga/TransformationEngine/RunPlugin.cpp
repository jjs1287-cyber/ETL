/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#include <exception>
#include <vector>

#include <konga/Utils/CrException.h>

#include <konga/DbRelations/EntityIdConstants.h>

#include <konga/PluginManager/PluginManifestStore.h>
#include <konga/PluginManager/PipelinePosition.h>
#include <konga/PluginManager/PipelinePluginManifest.h>

#include <konga/PipelinePluginExecutor/PipelinePluginExecutor.h>

#include <konga/TransformationUtils/TransformationDataHolder.h>

#include "RunPlugin.h"

using namespace konga;
using std::string;
using std::vector;

void konga::RunPlugin(const string& plugin_tag,
					  TransformationDataHolder& tranDataHolder,
					  string& stError) {
	try {

		                                                                                                                                  
		                                      
		string stUniqueIdentifier;
		if( plugin_tag.size() > 11 && plugin_tag.compare(0, 5, "<TAG>") == 0 && plugin_tag.compare(plugin_tag.size()-6, 6, "</TAG>") == 0 ) {
			                             
			stUniqueIdentifier = plugin_tag.substr(5, plugin_tag.size() - 11);
		}
		else {
			stUniqueIdentifier = plugin_tag;
		}

		                                            
		if ( stUniqueIdentifier.size() > 7 && stUniqueIdentifier.compare(0, 7, "plugin:") == 0 ) {
			stUniqueIdentifier = stUniqueIdentifier.substr(7);
		}

		PluginManifestStore* const pManifestStore = tranDataHolder.getPluginManifestStore();

		boost::shared_ptr<PluginManifest> p = pManifestStore->GetManifest(stUniqueIdentifier);

		PipelinePluginManifest* pManifest
			= dynamic_cast<PipelinePluginManifest*>(p.get());
		if ( pManifest == 0 ) {
			throw CrException("The plugin '" + stUniqueIdentifier + "' is not a pipeline plugin and can't be executed by RunPlugin.");
		}

		PipelinePluginExecutor exec;

		PipelinePosition pipelinePosition(ScriptEntityId, PIPELINE_POSTITION_INSIDE, 0, true);

		                                                                                    
		vector<string> inputFiles;
		bool bHasNewFiles;
		vector<OutputFile> outputFiles;

		exec.ExecutePipelinePlugin(*pManifest, pipelinePosition, *tranDataHolder.getOperationContext(),
			*tranDataHolder.getOperationDataElementHolder(), inputFiles, bHasNewFiles, outputFiles);
	}
	catch( const CrException& ex ) {
		stError = ex.GetErrorString();
	}
	catch( const std::exception& ex ) {
		stError = ex.what();
	}
}

