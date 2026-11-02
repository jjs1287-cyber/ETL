/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <vector>

#include <konga/Utils/FileUtils.h>

#include "ProcessedFiles.h"
#include "OperationEngineContext.h"
#include "ArchiveOperation.h"

using namespace std;
using namespace konga;

void ArchiveOperation::doOperation(ProcessedFiles& files, OperationEngineContext& context, OperationDataElementHolder& deHolder) {
	vector<ProcessedFile>& transfered_files = files.getProcessedFilesByRef();
	for( vector<ProcessedFile>::iterator it = transfered_files.begin();
		 it != transfered_files.end(); ++it ) {
		ProcessedFile& file = *it;
		if ( file.isInHistory() ) {
			continue;
		}
		                                                                           
		file.setLocalTargetLocation( file.getLocalSourceLocation() );
		long lErr = 0;
		long long lSize = GetFileSize(file.getLocalSourceLocation(), lErr);
		if ( lErr != 0)
			lSize = 0;
		file.setTargetSize(lSize);
		context.setSourceSize(lSize);
	}
}

