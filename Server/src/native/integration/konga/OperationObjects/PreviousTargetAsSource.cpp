/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <konga/Utils/CrException.h>
#include <konga/Utils/FileUtils.h>
#include <konga/Utils/FilesTraverser.h>

#include "ProcessedFiles.h"

#include "OperationEngineException.h"
#include "OperationEngineContext.h"
#include "PreviousTargetAsSource.h"

using namespace konga;
using std::string;
using std::vector;

const string PreviousTargetAsSource::GLOBAL_DE_NAME_FOR_SOURCE_PATH = "__PreviousSourcesDirectory";
const int PreviousTargetAsSource::SOURCE_ID = -99;

namespace {
	class GetProcessedFilesFileAction : public FileAction {
	public:
		GetProcessedFilesFileAction(ProcessedFiles& processedFiles,
			const string& stOperationTmpDir) : FileAction(),
		m_processedFiles(processedFiles),
		m_stOperationTmpDir(stOperationTmpDir) {}

		~GetProcessedFilesFileAction(void) {}
		
		void doAction(const std::string& stFilePath) {
			long lErr = 0;
			long long lSize = GetFileSize(stFilePath, lErr);
			if ( lErr || lSize < 0)
				lSize = 0;

			const string stLocalSourceFile(m_stOperationTmpDir + GetLeafPath(stFilePath));
			kCopyFile(stFilePath, stLocalSourceFile);

			ProcessedFile file("", lSize, 1, 1, 1970);
			file.setInHistory(false);
			file.setLocalSourceLocation(stLocalSourceFile);
			file.setTransferedNow();
			m_processedFiles.add(file);
		}

	private:
		ProcessedFiles& m_processedFiles;
		string m_stOperationTmpDir;
	};
}

PreviousTargetAsSource::~PreviousTargetAsSource(void) {
	if ( IsDirectory(m_stLocalDirectory) ) {
		try {
			kRemoveDirectory(m_stLocalDirectory);
		}
		catch( const CrException& ) {
			                       
		}
	}
}

void PreviousTargetAsSource::doTransfer(const OperationEngineContext& context, ProcessedFiles& files, OperationProgress& progress) const {

	if ( m_stLocalDirectory.empty() ) {
		throw OperationEngineException("Internal Error: Failed to get the targets from previous operation " \
			" because no local directory has been set.");
	}

	if ( !IsDirectory(m_stLocalDirectory) ) {
		throw OperationEngineException("Internal Error: The directory \"" + m_stLocalDirectory
			+ "\" does not exist. Targets from previous operation should have been in that directory.");
	}

	AllFilesMatcher matcher;
	GetProcessedFilesFileAction action(files, context.getLocalTmpDirForOperation());
	FilesTraverser traverser(action, matcher);

	traverser.traverseDirectory(m_stLocalDirectory, false);	                                  

	if ( files.empty() )
		throw OperationEngineException("No targets from previous source were found. " \
			"Maybe the previous operation did not produce any target files?");
}

void PreviousTargetAsSource::getFirstSourceFile(const OperationEngineContext& context, ProcessedFiles& files) const {
	throw OperationEngineException("Getting the first source file is not possible when the target of a previous " \
		"operation is used as source.");
}
