/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#ifndef KONGA_LOG_BILLING_TAB_UTILS_H
#define KONGA_LOG_BILLING_TAB_UTILS_H

#include <string>

namespace konga {

	class CrDateTime;

	class BillingTabUtils {
	public:
		explicit BillingTabUtils(DbConnection& TranDbConnection) : m_dbConnection(TranDbConnection) { }
		~BillingTabUtils(void) { }

		                                                        
		void DeleteOldLogs(const CrDateTime& deleteBeforeDate);

		                                       
		void ArchiveOldLogs(const CrDateTime& deleteBeforeDate);
		
		                                                                                      
		void DeleteLogsForOperation(long lOperationId, const CrDateTime& deleteBeforeDate);

		                                             
		void DeleteLogsForOperation(long lOperationId);

	private:
		DbConnection& m_dbConnection;
	};
}
#endif                                 

