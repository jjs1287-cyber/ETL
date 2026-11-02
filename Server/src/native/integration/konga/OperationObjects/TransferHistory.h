/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_OPERATION_TRANSFER_HISTORY_H
#define KONGA_OPERATION_TRANSFER_HISTORY_H

#include "ProcessedFiles.h"

namespace konga {
	class DbConnection;

	class TransferHistory {
	public:
		TransferHistory(DbConnection* pConnection) : m_pDbConnection(pConnection) { }
		~TransferHistory(void) { }

		                                                                                  
		                        
		bool IsAlreadyTransferred(long lOperationId, ProcessedFile& file) const;

		                                                              
		void AddToTransferHistory(long lOperationId, ProcessedFiles& files) const;

		void AddToTransferHistory(long lOperationId, const ProcessedFile& file) const;

	private:
		bool isActive() const;
		DbConnection* m_pDbConnection;	                                 
	};
}
#endif                                      

