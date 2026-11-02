/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef KONGA_OPERATION_ENGINE_OPERATION_SUMMARY_WRITER
#define KONGA_OPERATION_ENGINE_OPERATION_SUMMARY_WRITER

namespace konga {

	class ProcessedFiles;
	class DbConnection;

	class OperationSummaryWriter {
	public:
		OperationSummaryWriter(void);
		~OperationSummaryWriter(void);

		void Write(long long llOperationInstanceId, ProcessedFiles& files, DbConnection& tranDbConn);

	};
}

#endif                                                   
