/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <vector>

#include <konga/Utils/FileUtils.h>

#include <konga/ItemAccess/UserAccess.h>
#include <konga/DbRelations/EntityIdConstants.h>

#include "SourceTargetPipelinePluginExecution.h"
#include "SourceDataLocation.h"
#include "ProcessedFiles.h"
#include "OperationEngineException.h"
#include "EndpointCompression.h"

using namespace konga;
using std::string;
using std::vector;

SourceDataLocation::SourceDataLocation(void) :
m_pPluginSelector(0)
{
}

SourceDataLocation::~SourceDataLocation(void) {
}

long SourceDataLocation::getIgnoreFirstLines() const {
	return m_SourceData.m_lIgnoreFirst > 0 ? m_SourceData.m_lIgnoreFirst : 0;
}

long SourceDataLocation::getIgnoreLastLines() const {
	return m_SourceData.m_lIgnoreLast > 0 ? m_SourceData.m_lIgnoreLast : 0;
}

void SourceDataLocation::doPreSourceTransfer(OperationContext& context,
											 OperationDataElementHolder& deHolder,
											 ProcessedFiles& files) const
{
	if ( m_pPluginSelector == 0 )
		return;
	ExecutePipelinePluginAtSourceOrTarget(true, this->m_SourceData.m_lSourceId,
		this->m_SourceData.m_sSourceName, context, deHolder, files, *m_pPluginSelector, 0);
}

void SourceDataLocation::doPostSourceTransfer(OperationContext& context,
											  OperationDataElementHolder& deHolder,
											  ProcessedFiles& files) const
{
	expandEndpointArchives(*this, files);
	const long ignoreFirst = getIgnoreFirstLines();
	const long ignoreLast = getIgnoreLastLines();
	if ( ignoreFirst > 0 || ignoreLast > 0 ) {
		try {
			const vector<ProcessedFile>& vFiles = files.getProcessedFilesByRef();
			for( vector<ProcessedFile>::const_iterator cit = vFiles.begin();
				cit != vFiles.end(); ++cit)
			{
				const string& localSourceFile = cit->getLocalSourceLocation();
				if ( FileExists(localSourceFile) ) {
					RemoveFirstAndLastLines(localSourceFile, ignoreFirst, ignoreLast);
				}
			}
		}
		catch( const CrException& ex ) {
			throw OperationEngineException("Failed to remove first/last lines of source file.\nReason: "
				+ ex.GetErrorString());
		}
	}

	unsigned long ulNofFilesIn = files.getProcessedFilesByRef().size();
	if ( m_pPluginSelector != 0 ) {
		ExecutePipelinePluginAtSourceOrTarget(true, this->m_SourceData.m_lSourceId,
			this->m_SourceData.m_sSourceName, context, deHolder, files, *m_pPluginSelector, 1);

		if ( files.empty() ) {
			                                                                        
			if ( ulNofFilesIn == 0 )
				throw OperationEngineException("No source file matched the given file filter", false);
			else
				throw OperationEngineException("No source files to process after running post-target pipeline plugins.", false);
		}
	}

	                                                              
	if ( files.filesToProcess() == 0 ) {
		throw OperationEngineException("All the matched files were found in the history table. No files to process.", false);
	}
}

bool SourceDataLocation::userHasReadPermission(const UserInfo& user, DbConnection* pTranDbConn) const {
	const long id = getSourceId();
	if ( id < 1 )
		return true;

	if ( pTranDbConn ) {
		UserAccess userAccess(user, *pTranDbConn);
		return userAccess.HasReadPermission(id, SourceEntityId);
	}
	else {
		return konga::UserHasReadPermission(user.GetId(), id, SourceEntityId);
	}
}

bool SourceDataLocation::userHasExecutePermission(const UserInfo& user, DbConnection* pTranDbConn) const {
	const long id = getSourceId();
	if ( id < 1 )
		return true;

	if ( pTranDbConn ) {
		UserAccess userAccess(user, *pTranDbConn);
		return userAccess.HasExecutePermission(id, SourceEntityId);
	}
	else {
		return konga::UserHasExecutePermission(user.GetId(), id, SourceEntityId);
	}
}

void SourceDataLocation::finishSourceFiles(const OperationEngineContext& context, ProcessedFiles& files) const {
 bool compressed=false;for(const auto& file:files.getProcessedFilesByRef())compressed|=!file.archiveEntryName.empty();
 if(!compressed){doPostTransformation(context,files);return;}
 ProcessedFiles original;std::map<std::string,size_t> archives;
 for(const auto& file:files.getProcessedFilesByRef()) {
  if(file.archiveEntryName.empty()){original.add(file);continue;}
  auto found=archives.find(file.getRemoteSourceLocation());
  if(found==archives.end()){archives[file.getRemoteSourceLocation()]=original.getProcessedFilesByRef().size();original.add(file);}
  else if(file.hasError())original.getProcessedFilesByRef()[found->second].addError(file.getError());
 }
 for(auto& item:original.getProcessedFilesByRef())item.archiveEntryName.clear();
 doPostTransformation(context,original);
 std::map<std::string,std::string> warnings;
 for(const auto& result:original.getProcessedFilesByRef())if(result.hasWarning())warnings[result.getRemoteSourceLocation()]=result.getWarning();
 for(auto& file:files.getProcessedFilesByRef()){auto found=warnings.find(file.getRemoteSourceLocation());if(found!=warnings.end())file.addWarning(found->second);}
}
