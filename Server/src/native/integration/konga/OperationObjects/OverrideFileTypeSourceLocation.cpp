/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <konga/Utils/FileUtils.h>
#include <konga/Utils/CrException.h>
#include <konga/Utils/util.h>

#include <konga/OperationObjects/ProcessedFiles.h>
#include <konga/OperationObjects/OperationEngineContext.h>

#include "OverrideFileTypeSourceLocation.h"

using namespace konga;
using std::string;
using std::vector;

const long OverrideFileTypeSourceLocation::HTTP_ENDOINT_SOURCE_ID = -98;

OverrideFileTypeSourceLocation::~OverrideFileTypeSourceLocation(void) {
	                                                                                                       
	if ( m_bIsExclusive ) {
		if ( FileExists(m_sOverrideFileName) ) {
			try {
				kDeleteFile(m_sOverrideFileName);
			}
			catch( const CrException& ) {
				                       
			}
		}

		                                                
		const string base_dir(konga::GetBranchPath(m_sOverrideFileName));
		if ( IsDirectoryEmpty(base_dir) ) {
			try {
				kRemoveDirectory(base_dir);
			}
			catch( const CrException& ) {
				                       
			}
		}
	}
}

vector<string> OverrideFileTypeSourceLocation::getFileList() const {
	vector<string> v;
	v.push_back(m_sOverrideFileName);
	return v;
}

string OverrideFileTypeSourceLocation::getFileFilter() const {
	return string();
}

void OverrideFileTypeSourceLocation::doTransferImpl(const OperationEngineContext& context, ProcessedFiles& files, OperationProgress& progress) const {
	if ( FileExists(m_sOverrideFileName) ) {
		const string localFileName(context.getLocalTmpDirForOperation() + "sourceOverride");
		kCopyFile(m_sOverrideFileName, localFileName);
		ProcessedFile file;
		file.setFileChangeTimeNow();
		file.setLocalSourceLocation(localFileName);

		long err = 0;
		const long long size = GetFileSize(localFileName, err);
		file.setSourceSize(size);
		file.setTransferedNow();

		files.add(file);
	}
}

