/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#ifndef KONGA_LOG_XFER_HIST_UTILS_H
#define KONGA_LOG_XFER_HIST_UTILS_H

#include <string>

namespace konga {

	class CrDateTime;

	class TransferHistoryUtils {
	public:
		explicit TransferHistoryUtils(DbConnection& TranDbConnection) : m_dbConnection(TranDbConnection) { }
		~TransferHistoryUtils(void) { }

		                                                        
		void DeleteOldLogs(const CrDateTime& deleteBeforeDate);

		                                       
		void ArchiveOldLogs(const CrDateTime& deleteBeforeDate);
		
		                                                                                      
		void DeleteLogsForOperation(long lOperationId, const CrDateTime& deleteBeforeDate);

		                                             
		void DeleteLogsForOperation(long lOperationId);

	private:
		DbConnection& m_dbConnection;
	};
}
#endif                               

