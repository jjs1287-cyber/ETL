/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#include <vector>

#include <konga/Utils/CrException.h>
#include <konga/Utils/FileUtils.h>
#include <konga/DbRelations/EntityIdConstants.h>

#include <konga/PluginManager/PipelinePluginSelector.h>
#include <konga/PipelinePluginExecutor/PipelinePluginExecutor.h>
#include <konga/PluginManager/PipelinePluginManifest.h>

#include "OperationEngineException.h"
#include "OperationEngineContext.h"
#include "ProcessedFiles.h"

#include "SourceTargetPipelinePluginExecution.h"

using namespace konga;
using std::string;
using std::vector;

namespace {

	const ProcessedFile* GetOriginFile(const string& sOriginFile,
		ProcessedFiles& files, bool bIsSource) {
		vector<ProcessedFile>& processedFiles = files.getProcessedFilesByRef();
		vector<ProcessedFile>::const_iterator cit = processedFiles.begin();
		for(; cit != processedFiles.end(); ++cit ) {
			const ProcessedFile& file = *cit;
			const string& sLocalLocation = bIsSource ? file.getLocalSourceLocation() : file.getLocalTargetLocation();
			const bool bIsOrigin = konga::EquivalentPaths(sOriginFile, sLocalLocation);
			if ( bIsOrigin )
				return &file;
		}

		return 0;
	}

	void AddNewFiles(bool bIsSource, int iRelativePositionId,
		const vector<OutputFile>& outputFiles, ProcessedFiles& files) {
		if ( outputFiles.empty() ) {
			files.getProcessedFilesByRef().clear();
			return;
		}

		vector<ProcessedFile> newFiles;
		vector<OutputFile>::const_iterator citOutputFiles = outputFiles.begin();
		for(; citOutputFiles != outputFiles.end(); ++citOutputFiles ) {
			const OutputFile& outputFile = *citOutputFiles;
			ProcessedFile newFile;

			if ( !FileExists(outputFile.Name) ) {
				newFile.addError("A file reported as created by a pipeline plugin does not exist ("
					+ outputFile.Name + ").");
				newFiles.push_back(newFile);
				continue;
			}

			                                                                                                             
			const ProcessedFile* pOriginFile = 0;
			if ( outputFile.OriginFiles.size() == 1 ) {
				pOriginFile = GetOriginFile(*outputFile.OriginFiles.begin(), files, bIsSource);
				if ( pOriginFile != 0 ) {
					newFile = *pOriginFile;
				}
			}

			                                                 
			                       
			long lerr = 0;
			const long long size = konga::GetFileSize(outputFile.Name, lerr);
			if ( bIsSource ) {
				newFile.setLocalSourceLocation(outputFile.Name);
				newFile.setSourceSize(size);
			}
			else {
				newFile.setLocalTargetLocation(outputFile.Name);
				newFile.setTargetSize(size);
			}

			                                                              
			if ( pOriginFile == 0 ) {
				newFile.setFileChangeTimeNow();
				if ( bIsSource ) {
					newFile.setTransferedNow();
				}
			}

			if ( !outputFile.Warning.GetWarning().empty() ) {
				switch(outputFile.Warning.GetSeverity()) {
					case FileWarning::Severity_Information:
						                     
						break;
					case FileWarning::Severity_Warning:
						newFile.addWarning(outputFile.Warning.GetWarning());
						break;
					case FileWarning::Severity_Error:
						newFile.addError(outputFile.Warning.GetWarning());
						break;
					default:
						break;
				};
			}
			newFiles.push_back(newFile);
		}

		files.getProcessedFilesByRef() = newFiles;
	}

}                     

void konga::ExecutePipelinePluginAtSourceOrTarget(bool bIsSource,
												  long lEntityId,
												  const string& sItemName,
												  const OperationContext& context,
												  OperationDataElementHolder& deHolder,
												  ProcessedFiles& files,
												  PipelinePluginSelector& selector,
												  int iRelativePositionId)
{
	const string sEntityType(bIsSource ? "source" : "target");
	const int iEntityTypeId = bIsSource ? SourceEntityId : TargetEntityId;
	try {
		PluginManifestListT manifests = selector.GetPipelinePluginInstances(context.getOperationId(), 0,
			iEntityTypeId, lEntityId, iRelativePositionId);

		SortPipelinePlugins(manifests, iEntityTypeId);

		PluginManifestListT::const_iterator cit = manifests.begin();
		PipelinePluginExecutor executor;
		for(; cit != manifests.end(); ++cit ) {
			PipelinePluginManifest* pluginManifest = dynamic_cast<PipelinePluginManifest*>(cit->get());
			if ( pluginManifest == 0 )
				continue;

			if ( !pluginManifest->ExecuteAtPipelinePosition(iEntityTypeId, iRelativePositionId) )
				continue;

			vector<string> inputFiles;
			vector<ProcessedFile>& inFiles = files.getProcessedFilesByRef();
			for( vector<ProcessedFile>::const_iterator cit = inFiles.begin(); cit != inFiles.end(); ++cit ) {
				const ProcessedFile& file = *cit;
				if ( bIsSource && file.isInHistory() )
					continue;

				const string sInputFileName = ( bIsSource ? file.getLocalSourceLocation()
					: file.getLocalTargetLocation() );
				if ( sInputFileName.empty() || !FileExists(sInputFileName) )
					continue;
				inputFiles.push_back(sInputFileName);
			}

			bool bHasNewFiles = false;
			vector<OutputFile> outputFiles;

			PipelinePosition pipelinePosition(bIsSource?SourceEntityId:TargetEntityId,
				iRelativePositionId, 0, false);
			executor.ExecutePipelinePlugin(*pluginManifest, pipelinePosition,
				context, deHolder, inputFiles, bHasNewFiles, outputFiles);
			if ( bHasNewFiles ) {
				AddNewFiles(bIsSource, iRelativePositionId, outputFiles, files);
			}
		}
	}
	catch( const CrException& ex ) {
		throw OperationEngineException(
			"Failed to execute pipeline plugin for " + sEntityType
			+ " \"" + sItemName + "\" . Reason:\n"
			+ ex.GetErrorString());
	}

}

