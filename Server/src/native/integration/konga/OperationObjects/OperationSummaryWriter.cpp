/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbConnection.h>

#include "ProcessedFiles.h"

#include "OperationSummaryWriter.h"

using namespace konga;
using std::vector;
using std::string;

OperationSummaryWriter::OperationSummaryWriter(void) {}
OperationSummaryWriter::~OperationSummaryWriter(void) {}

void OperationSummaryWriter::Write(long long llOperationInstanceId, ProcessedFiles& processedFiles, DbConnection& tranDbConn) {
	if ( processedFiles.empty() ) return;

	long lSourceFiles = 0, lTargetFiles = 0;
	long long llSourceRecords = 0, llSourceSize = 0, llTargetRecords = 0, llTargetSize = 0, llTotalTransformationTime = 0;

	vector<ProcessedFile>& files = processedFiles.getProcessedFilesByRef();
	for( vector<ProcessedFile>::const_iterator cit = files.begin(); cit != files.end(); ++cit ) {
		const ProcessedFile& file = *cit;

		llSourceRecords += file.getSourceRecords();
		llSourceSize += file.getSourceSize();
		llTargetRecords += file.getTargetRecords();
		llTargetSize += file.getTargetSize();

		if ( !file.getRemoteSourceLocation().empty() ) {
			++lSourceFiles;
		}
		if ( !file.getRemoteTargetLocation().empty() ) {
			++lTargetFiles;
		}

		llTotalTransformationTime += file.getTransformationTime();
	}

	const string sql("INSERT INTO operationsummarytab (operation_instance_id, source_files, target_files, source_size, target_size, source_records, target_records, total_transformation_time) VALUES ("
		+ toString(llOperationInstanceId) + "," + toString(lSourceFiles) + "," + toString(lTargetFiles) + "," + toString(llSourceSize) + "," + toString(llTargetSize) + ","
		+ toString(llSourceRecords) + "," + toString(llTargetRecords) + "," + toString(llTotalTransformationTime) + ")");
	tranDbConn.ExecuteSql(sql);
}
